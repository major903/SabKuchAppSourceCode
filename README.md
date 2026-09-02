# SabKuch – All-in-One Android App

<p align="center">
  <img src="app/src/main/res/mipmap-xxxhdpi/icon.png" alt="SabKuch all-in-one Android app icon" width="128">
</p>

**SabKuch** (also searched as **Sabkuch**, **Subkuch**, or **Sub Kuch**) is an all-in-one Android app by Techvedam. It brings local discovery and community services together in one place, including business directories, jobs, events, classifieds, learning, entertainment, and more.

[Download SabKuch on Google Play](https://play.google.com/store/apps/details?id=vedam.subkuch) · [Official website](https://sabkuchworld.com) · [Privacy policy](https://sabkuchworld.com/privacypolicy.html)

## What you can do with SabKuch

- Discover businesses and public utilities in the local directory.
- Find and post jobs, events, classifieds, needs, and offers.
- Explore movies, learning content, and community information.
- Connect through phone book, chat, dating, and matrimonial features.
- Ask questions and find professional or local services.
- Contribute useful local data and manage Vedam Coins in the wallet.

SabKuch is expanding its services across Mysuru, Bengaluru, Chennai, Mumbai, Delhi, Ahmedabad, and other cities in India and worldwide.

## Android project

This repository contains the official SabKuch Android source code for the `vedam.subkuch` application.

| Component | Technology |
| --- | --- |
| Mobile app | Android, Kotlin, Java |
| UI | AndroidX, Material Components, data binding, view binding |
| Networking | Retrofit, OkHttp, Gson |
| Local data | Room |
| Cloud services | Firebase Analytics, Cloud Messaging, Crashlytics, Firestore |
| Maps and location | Google Maps, Google Play services, Leku |
| Build | Gradle, Android Gradle Plugin, KSP, JDK 17 |

## Build locally

### Requirements

- Android Studio with Android SDK 37
- JDK 17
- A valid `app/google-services.json` for the configured Firebase project
- A Google Maps API key supplied through `SABKUCH_MAPS_API_KEY` or the `mapsApiKey` Gradle property

### Debug build

On Windows:

```powershell
.\gradlew.bat :app:assembleDebug
```

On macOS or Linux:

```bash
./gradlew :app:assembleDebug
```

The generated debug APK is written under `app/build/outputs/apk/debug/`.

## Project structure

```text
app/       Main SabKuch Android application
leku/      In-repository location picker module
docs/      Project and release documentation
website/   Public support pages
```

## App updates

The app can read release metadata from [`update.json`](update.json). Maintainer instructions are available in [`docs/INTERNAL_APK_UPDATES.md`](docs/INTERNAL_APK_UPDATES.md).

## Support

- Google Play: [SabKuch by Techvedam](https://play.google.com/store/apps/details?id=vedam.subkuch)
- Website: [sabkuchworld.com](https://sabkuchworld.com)
- Account deletion: [Delete your SabKuch account](https://sabkuchworld.com/delete-account.html)
