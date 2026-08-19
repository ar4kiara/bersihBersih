# Storage Inspector Pro

Native Android storage analyzer, cleaner, file browser foundation, and media reviewer focused on making a nearly-full phone understandable and safe to clean.

> Core product loop: **SCAN → REKAP → FILTER → PREVIEW → SELECT → CLEAN**

Credit: **@ar4kiara**

## Current status

### Completed in this baseline
- Native Kotlin + Jetpack Compose + Material 3 project.
- Room-backed global file metadata index.
- Real shared-storage scan through `MediaStore.Files` (no fake dashboard data).
- Batched scan on `Dispatchers.IO`; stale-index reconciliation after successful scan.
- Category classification for media, documents, archives, APK/XAPK, database, backup, code, temp, unknown, and other.
- Source classification for WhatsApp, WhatsApp Business, Telegram, Camera, Download, Documents, App Media, and shared storage.
- Overview with indexed file count, analyzed size, category breakdown and scan progress.
- Analyzer with text/source/category search and large-file size presets.
- Large-files-first browser backed by the local index.
- Hidden-file view logic.
- Media preview: images with Coil; video/audio with Media3 streaming from URI.
- Delete from indexed content with success/failure accounting and immediate DB update.
- Duplicate candidate pipeline: size grouping then SHA-256 verification only for candidates.
- Cleaner review cards with conservative wording; no dangerous one-tap “clean everything”.
- Access & Capabilities screen with honest Android limitations and permission actions.
- Offline-first design, no analytics, tracker, login, server, or cloud upload.
- Light/dark theme follows system.
- Unit tests for file-type classification, source classification, and size formatting.
- GitHub Actions workflow for unit tests, lint, and debug APK build.

### Partial / next implementation layer
- Full file-manager folder tree/breadcrumb UI.
- Copy/cut/paste/move/new-folder/rename UI and queued progress controller.
- Multi-select, range-select, cleaning summary, review swipe queue, trash retention.
- SAF user-defined sources and persisted mappings for vendor/clone paths.
- Folder-size cache and richer Storage Map.
- Full combined Analyzer filters (age + source multi-select + sort matrix) currently have a lean baseline UI.
- Document/PDF/APK metadata preview.
- Optional All Files provider traversal beyond MediaStore where Android policy permits it.
- Background incremental reconciliation/WorkManager scheduling.
- ZIP compress/extract.
- Shizuku/root abstractions remain future-ready work, not falsely advertised as active.

## Android storage limitations
Android apps cannot freely read another app's private `/data/data` directory. Clone/dual-app storage can also be isolated in another Android profile. This app therefore treats capabilities explicitly rather than claiming invisible data was scanned.

Baseline scanning uses MediaStore and runtime media permissions. `MANAGE_EXTERNAL_STORAGE` is declared for a future/full file-manager provider and is opt-in through Android Settings; core UI remains usable with reduced access without it. Publishing to Google Play requires policy review before relying on All Files Access.

## Architecture

```
UI / Compose
  └─ MainViewModel
      ├─ Room FileDao / global metadata index
      ├─ StorageScanner (MediaStore, batched IO)
      ├─ DuplicateFinder (size candidates → SHA-256)
      ├─ FileOperationEngine
      └─ PermissionManager

Domain
  ├─ FileTypeClassifier
  └─ SourceClassifier
```

The code is intentionally split so additional providers (SAF, All Files, Shizuku, root) can be added without moving storage logic into Compose screens.

## Build

Requirements:
- JDK 17
- Android SDK 35
- Gradle 8.11.1 or Android Studio with a compatible Gradle/AGP toolchain

```bash
gradle testDebugUnitTest
gradle lintDebug
gradle assembleDebug
```

Debug APK output:

```text
app/build/outputs/apk/debug/app-debug.apk
```

GitHub Actions also uploads the debug APK as `storage-inspector-debug-apk` after a successful build.

## First run
1. Open the app.
2. Go to **More → Access & Capabilities** and grant the media access you want to analyze.
3. Return to **Overview** and tap **Scan storage**.
4. Open **Analyzer / Files / Cleaner** to review actual indexed content.
5. Preview files before deleting them.

## Safety principles
- No automatic permanent cleaning.
- Hidden/old files are never declared junk just because of their name or age.
- Duplicate status requires content hash verification of size-matched candidates.
- Storage/API failures are reported rather than silently treated as success.
- Core operations run off the UI thread.

## Privacy
All core analysis is local. The baseline contains no account system, tracker SDK, ad SDK, analytics SDK, or cloud sync.
