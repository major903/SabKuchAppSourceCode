package vedam.subkuch.update;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import java.io.File;

import vedam.subkuch.R;

/** Installs an APK after DownloadManager reports a successful download. */
public final class AppUpdateDownloadReceiver extends BroadcastReceiver {
    private static final String APK_MIME_TYPE = "application/vnd.android.package-archive";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (!DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(intent.getAction())) {
            return;
        }
        long completedId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1L);
        if (completedId < 0) {
            return;
        }
        android.content.SharedPreferences preferences = context.getSharedPreferences(
                AppUpdateManager.PREFS_NAME, Context.MODE_PRIVATE);
        long expectedId = preferences.getLong(AppUpdateManager.PREF_DOWNLOAD_ID, -1L);
        if (completedId != expectedId) {
            return;
        }

        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        if (downloadManager == null || !isSuccessful(downloadManager, completedId)) {
            clearDownload(preferences);
            Toast.makeText(context, R.string.update_download_failed, Toast.LENGTH_LONG).show();
            return;
        }
        String path = preferences.getString(AppUpdateManager.PREF_DOWNLOAD_PATH, "");
        File apkFile = new File(path == null ? "" : path);
        if (!apkFile.isFile()) {
            clearDownload(preferences);
            Toast.makeText(context, R.string.update_download_failed, Toast.LENGTH_LONG).show();
            return;
        }
        try {
            Uri apkUri = FileProvider.getUriForFile(
                    context,
                    context.getPackageName() + ".provider",
                    apkFile);
            Intent installIntent = new Intent(Intent.ACTION_VIEW)
                    .setDataAndType(apkUri, APK_MIME_TYPE)
                    .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(installIntent);
            clearDownload(preferences);
        } catch (Exception exception) {
            Toast.makeText(context, R.string.update_download_failed, Toast.LENGTH_LONG).show();
        }
    }

    private boolean isSuccessful(DownloadManager manager, long downloadId) {
        DownloadManager.Query query = new DownloadManager.Query().setFilterById(downloadId);
        try (android.database.Cursor cursor = manager.query(query)) {
            return cursor != null
                    && cursor.moveToFirst()
                    && cursor.getInt(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_STATUS))
                    == DownloadManager.STATUS_SUCCESSFUL;
        }
    }

    private void clearDownload(android.content.SharedPreferences preferences) {
        preferences.edit()
                .remove(AppUpdateManager.PREF_DOWNLOAD_ID)
                .remove(AppUpdateManager.PREF_DOWNLOAD_PATH)
                .apply();
    }
}
