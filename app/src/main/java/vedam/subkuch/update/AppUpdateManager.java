package vedam.subkuch.update;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import java.io.File;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import vedam.subkuch.BuildConfig;
import vedam.subkuch.R;
import vedam.subkuch.utils.UiUtil;

/** Coordinates update checks and hands downloaded APKs to Android's installer. */
public final class AppUpdateManager {
    static final String PREFS_NAME = "app_update";
    static final String PREF_DOWNLOAD_ID = "download_id";
    static final String PREF_DOWNLOAD_PATH = "download_path";
    private static final String APK_MIME_TYPE = "application/vnd.android.package-archive";

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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O
                && !activity.getPackageManager().canRequestPackageInstalls()) {
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

        String fileName = buildFileName(manifest);
        File downloadDirectory = activity.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
        if (downloadDirectory == null) {
            Toast.makeText(activity, R.string.update_download_failed, Toast.LENGTH_LONG).show();
            return;
        }
        if (!downloadDirectory.exists() && !downloadDirectory.mkdirs()) {
            Toast.makeText(activity, R.string.update_download_failed, Toast.LENGTH_LONG).show();
            return;
        }
        File destination = new File(downloadDirectory, fileName);
        if (destination.exists() && !destination.delete()) {
            Toast.makeText(activity, R.string.update_download_failed, Toast.LENGTH_LONG).show();
            return;
        }

        DownloadManager downloadManager = (DownloadManager) activity.getSystemService(Context.DOWNLOAD_SERVICE);
        if (downloadManager == null) {
            Toast.makeText(activity, R.string.update_download_failed, Toast.LENGTH_LONG).show();
            return;
        }
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(manifest.getApkUrl()))
                .setTitle(activity.getString(R.string.app_name) + " " + manifest.getVersionName())
                .setDescription(activity.getString(R.string.update_download_started))
                .setMimeType(APK_MIME_TYPE)
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(false);
        request.setDestinationInExternalFilesDir(
                activity, Environment.DIRECTORY_DOWNLOADS, fileName);
        long downloadId;
        try {
            downloadId = downloadManager.enqueue(request);
        } catch (RuntimeException exception) {
            Toast.makeText(activity, R.string.update_download_failed, Toast.LENGTH_LONG).show();
            return;
        }
        activity.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
                .edit()
                .putLong(PREF_DOWNLOAD_ID, downloadId)
                .putString(PREF_DOWNLOAD_PATH, destination.getAbsolutePath())
                .apply();
        Toast.makeText(activity, R.string.update_download_started, Toast.LENGTH_LONG).show();
    }

    private static long currentVersionCode(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                return packageInfo.getLongVersionCode();
            }
            return packageInfo.versionCode;
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
