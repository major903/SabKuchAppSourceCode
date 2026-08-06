# Internal APK updates

The app checks this file from GitHub:

`https://raw.githubusercontent.com/forexveda/SabkuchApp/upgrade_android_14/update.json`

For every team release:

1. Build and sign the release APK with the same signing key as the installed app.
2. Create a GitHub Release whose tag matches the `versionName`, for example `v1.10`.
3. Upload the APK with the filename referenced by `apkUrl`.
4. Update `update.json` with the new `versionCode`, `versionName`, APK URL, and changelog.
5. Publish the GitHub Release and commit the updated `update.json`.

The app compares `versionCode`, downloads the APK through Android DownloadManager, and opens the system installer using the app's `FileProvider`. Android still requires the user to confirm installation.
