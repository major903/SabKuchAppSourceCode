package vedam.subkuch.update;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import androidx.core.content.pm.PackageInfoCompat;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vedam.subkuch.BuildConfig;
import vedam.subkuch.R;
import vedam.subkuch.utils.UiUtil;

/** Coordinates update checks, in-app APK downloads, and Android's installer. */
public final class AppUpdateManager {
    private static final String APK_MIME_TYPE = "application/vnd.android.package-archive";
    private static final ExecutorService DOWNLOAD_EXECUTOR = Executors.newSingleThreadExecutor();

    private AppUpdateManager() {
    }

    public static void checkForUpdates(Activity activity) {
        if (BuildConfig.APP_UPDATE_MANIFEST_URL.trim().isEmpty()) {
            Toast.makeText(activity, R.string.update_unavailable, Toast.LENGTH_LONG).show();
            return;
        }
        UiUtil.showProgressDialog(activity, activity.getString(R.string.update_checking));
        AppUpdateClient.check(new Callback<AppUpdateManifest>() {
            @Override
            public void onResponse(Call<AppUpdateManifest> call,
                                   Response<AppUpdateManifest> response) {
                UiUtil.cancelProgressDialog();
                if (!response.isSuccessful() || response.body() == null) {
                    showUnavailable(activity);
                    return;
                }
                AppUpdateManifest manifest = response.body();
                if (!manifest.isValid()) {
                    Toast.makeText(activity, R.string.update_invalid, Toast.LENGTH_LONG).show();
                    return;
                }
                if (manifest.getVersionCode() <= currentVersionCode(activity)) {
                    Toast.makeText(activity, R.string.update_latest, Toast.LENGTH_LONG).show();
                    return;
                }
                showUpdateDialog(activity, manifest);
            }

            @Override
            public void onFailure(Call<AppUpdateManifest> call, Throwable throwable) {
                UiUtil.cancelProgressDialog();
                showUnavailable(activity);
            }
        });
    }

    private static void showUpdateDialog(Activity activity, AppUpdateManifest manifest) {
        String message = activity.getString(
                R.string.update_available_message,
                manifest.getVersionName(),
                manifest.getChangelog());
        AlertDialog dialog = new AlertDialog.Builder(activity)
                .setTitle(R.string.update_available_title)
                .setMessage(message)
                .setNegativeButton(R.string.no, null)
                .setPositiveButton(R.string.download_update, null)
                .create();
        dialog.setOnShowListener(ignored -> dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                .setOnClickListener(view -> {
                    dialog.dismiss();
                    startDownload(activity, manifest);
                }));
        dialog.setCancelable(!manifest.isMandatory());
        dialog.show();
    }

    private static void startDownload(Activity activity, AppUpdateManifest manifest) {
        if (!activity.getPackageManager().canRequestPackageInstalls()) {
            new AlertDialog.Builder(activity)
                    .setTitle(R.string.update_install_permission_title)
                    .setMessage(R.string.update_install_permission_message)
                    .setNegativeButton(R.string.no, null)
                    .setPositiveButton(R.string.open_settings, (dialog, which) -> {
                        Intent intent = new Intent(
                                Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES,
                                Uri.parse("package:" + activity.getPackageName()));
                        activity.startActivity(intent);
                    })
                    .show();
            return;
        }

        File downloadDirectory = activity.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
        if (downloadDirectory == null) {
            showDownloadFailed(activity, null);
            return;
        }
        if (!downloadDirectory.exists() && !downloadDirectory.mkdirs()) {
            showDownloadFailed(activity, null);
            return;
        }

        File destination = new File(downloadDirectory, buildFileName(manifest));
        if (destination.exists() && !destination.delete()) {
            showDownloadFailed(activity, null);
            return;
        }

        View progressView = LayoutInflater.from(activity)
                .inflate(R.layout.dialog_update_download, null);
        ProgressBar progressBar = progressView.findViewById(R.id.progress_update_download);
        TextView details = progressView.findViewById(R.id.tv_update_download_details);
        AtomicBoolean cancelled = new AtomicBoolean(false);
        AlertDialog progressDialog = new AlertDialog.Builder(activity)
                .setTitle(R.string.update_downloading_title)
                .setView(progressView)
                .setNegativeButton(R.string.cancel, null)
                .create();
        progressDialog.setOnShowListener(ignored -> progressDialog
                .getButton(AlertDialog.BUTTON_NEGATIVE)
                .setOnClickListener(view -> {
                    cancelled.set(true);
                    progressDialog.dismiss();
                }));
        progressDialog.setOnCancelListener(dialog -> cancelled.set(true));
        progressDialog.show();

        Call<ResponseBody> downloadCall = AppUpdateClient.getApi().downloadApk(manifest.getApkUrl());
        progressDialog.setOnDismissListener(dialog -> {
            if (cancelled.get()) {
                downloadCall.cancel();
                if (destination.exists()) {
                    //noinspection ResultOfMethodCallIgnored
                    destination.delete();
                }
            }
        });
        downloadCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (!response.isSuccessful() || response.body() == null) {
                    activity.runOnUiThread(() -> showDownloadFailed(activity, progressDialog));
                    return;
                }
                ResponseBody body = response.body();
                DOWNLOAD_EXECUTOR.execute(() -> copyApk(
                        activity,
                        progressDialog,
                        progressBar,
                        details,
                        body,
                        destination,
                        cancelled));
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                if (!cancelled.get()) {
                    activity.runOnUiThread(() -> showDownloadFailed(activity, progressDialog));
                }
            }
        });
    }

    private static void copyApk(Activity activity,
                                AlertDialog progressDialog,
                                ProgressBar progressBar,
                                TextView details,
                                ResponseBody body,
                                File destination,
                                AtomicBoolean cancelled) {
        long totalBytes = body.contentLength();
        long downloadedBytes = 0L;
        byte[] buffer = new byte[16 * 1024];
        try (InputStream input = body.byteStream(); OutputStream output =
                new java.io.FileOutputStream(destination)) {
            int read;
            while (!cancelled.get() && (read = input.read(buffer)) != -1) {
                output.write(buffer, 0, read);
                downloadedBytes += read;
                long currentBytes = downloadedBytes;
                activity.runOnUiThread(() -> updateDownloadProgress(
                        progressBar, details, currentBytes, totalBytes));
            }
            output.flush();
            if (cancelled.get()) {
                return;
            }
            activity.runOnUiThread(() -> {
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                showInstallDialog(activity, destination);
            });
        } catch (IOException exception) {
            if (destination.exists()) {
                //noinspection ResultOfMethodCallIgnored
                destination.delete();
            }
            if (!cancelled.get()) {
                activity.runOnUiThread(() -> showDownloadFailed(activity, progressDialog));
            }
        }
    }

    private static void updateDownloadProgress(ProgressBar progressBar,
                                               TextView details,
                                               long downloadedBytes,
                                               long totalBytes) {
        if (totalBytes > 0) {
            int percent = (int) Math.min(100L, downloadedBytes * 100L / totalBytes);
            progressBar.setIndeterminate(false);
            progressBar.setProgress(percent);
            details.setText(String.format(
                    Locale.US,
                    "%d%% — %s / %s",
                    percent,
                    formatMegabytes(downloadedBytes),
                    formatMegabytes(totalBytes)));
        } else {
            progressBar.setIndeterminate(true);
            details.setText(String.format(
                    Locale.US,
                    "%s downloaded",
                    formatMegabytes(downloadedBytes)));
        }
    }

    private static String formatMegabytes(long bytes) {
        return String.format(Locale.US, "%.1f MB", bytes / (1024d * 1024d));
    }

    private static void showInstallDialog(Activity activity, File apkFile) {
        new AlertDialog.Builder(activity)
                .setTitle(R.string.update_ready_title)
                .setMessage(R.string.update_ready_message)
                .setNegativeButton(R.string.cancel, null)
                .setPositiveButton(R.string.install_update,
                        (dialog, which) -> installApk(activity, apkFile))
                .show();
    }

    private static void installApk(Activity activity, File apkFile) {
        if (!activity.getPackageManager().canRequestPackageInstalls()) {
            new AlertDialog.Builder(activity)
                    .setTitle(R.string.update_install_permission_title)
                    .setMessage(R.string.update_install_permission_message)
                    .setNegativeButton(R.string.no, null)
                    .setPositiveButton(R.string.open_settings, (dialog, which) -> {
                        Intent intent = new Intent(
                                Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES,
                                Uri.parse("package:" + activity.getPackageName()));
                        activity.startActivity(intent);
                    })
                    .show();
            return;
        }
        try {
            Uri apkUri = FileProvider.getUriForFile(
                    activity,
                    activity.getPackageName() + ".provider",
                    apkFile);
            Intent installIntent = new Intent(Intent.ACTION_VIEW)
                    .setDataAndType(apkUri, APK_MIME_TYPE)
                    .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            activity.startActivity(installIntent);
        } catch (Exception exception) {
            Toast.makeText(activity, R.string.update_download_failed, Toast.LENGTH_LONG).show();
        }
    }

    private static void showDownloadFailed(Activity activity, AlertDialog progressDialog) {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        Toast.makeText(activity, R.string.update_download_failed, Toast.LENGTH_LONG).show();
    }

    private static long currentVersionCode(android.content.Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return PackageInfoCompat.getLongVersionCode(packageInfo);
        } catch (Exception exception) {
            return Long.MAX_VALUE;
        }
    }

    private static String buildFileName(AppUpdateManifest manifest) {
        String version = manifest.getVersionName().replaceAll("[^A-Za-z0-9._-]", "_");
        return "SabKuch-v" + version + ".apk";
    }

    private static void showUnavailable(Activity activity) {
        Toast.makeText(activity, R.string.update_unavailable, Toast.LENGTH_LONG).show();
    }
}
