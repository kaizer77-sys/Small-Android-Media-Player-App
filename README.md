[![Releases](https://img.shields.io/badge/Download-Releases-blue?logo=github)](https://github.com/kaizer77-sys/Small-Android-Media-Player-App/releases)

# Small Android Media Player App — Jetpack Compose Audio Player

[![Kotlin](https://img.shields.io/badge/Kotlin-1.9-blue?logo=kotlin)](https://kotlinlang.org/)
[![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-UI-brightgreen?logo=jetpack)](https://developer.android.com/jetpack/compose)
[![Clean Architecture](https://img.shields.io/badge/Architecture-Clean-orange)](https://en.wikipedia.org/wiki/Software_architecture)
[![Hilt](https://img.shields.io/badge/DI-Dagger%20Hilt-purple?logo=dagger)](https://dagger.dev/hilt/)
[![Coroutines](https://img.shields.io/badge/Coroutines-StateFlow-blueviolet?logo=kotlin)](https://kotlinlang.org/docs/coroutines-overview.html)
[![MediaSession](https://img.shields.io/badge/MediaSession-Playback-gray)](https://developer.android.com/guide/topics/media-apps/media-session)
[![Releases](https://img.shields.io/github/v/release/kaizer77-sys/Small-Android-Media-Player-App?label=Latest%20Release&logo=github)](https://github.com/kaizer77-sys/Small-Android-Media-Player-App/releases)

A compact Android audio player built with Jetpack Compose. The app lists local audio files on the device. It shows cover art, title, artist, and duration for each track. It uses modern Android components and patterns: clean architecture, MVVM, Hilt, coroutines, Flow/StateFlow, and MediaSession. The UI uses Compose and AndroidX navigation with deep links. The app uses notifications and media controls to provide consistent playback.

Table of contents
- Features
- Screenshots
- Supported audio and metadata
- Architecture overview
- Core modules and responsibilities
- Key libraries and technologies
- Quick start
- Install from Releases
- Build and run locally
- Permissions and runtime behavior
- How the media scan works
- Playing audio: service, MediaSession, and notifications
- Deep links and navigation
- State management and UI patterns
- Testing approach
- Performance notes
- Debugging tips
- Contributing
- Roadmap
- License
- Acknowledgements
- Contact

Features
- Scan device storage and list audio files (MP3, WAV, M4A, FLAC, and more).
- Show song cover art (embedded or generated), song title, artist, and duration.
- Play, pause, skip, seek, and queue tracks.
- Lock-screen controls and notification controls via MediaSession.
- Background playback via a foreground service.
- Notifications with media controls and album art.
- Deep link support to open a track or playlist directly.
- MVVM with ViewModel, StateFlow, and LiveData compatibility.
- Dependency injection with Dagger Hilt.
- Coroutines and Flow for background work.
- Jetpack Compose UI and navigation.
- Small APK footprint and fast startup.

Screenshots
![Player screen](https://images.unsplash.com/photo-1511379938547-c1f69419868d?auto=format&fit=crop&w=1200&q=80)
![Library list](https://images.unsplash.com/photo-1511671782779-c97d3d27a1d4?auto=format&fit=crop&w=1200&q=80)

Supported audio and metadata
- Files: MP3, WAV, M4A, AAC, OGG, FLAC, and other Android-supported formats.
- Metadata: title, artist, album, duration, track number.
- Cover art: embedded album art, sidecar images, and fallback generated art.
- MediaStore is the primary source for file discovery and metadata.

Architecture overview
The app follows a layered clean architecture. It uses three main layers:
- Presentation: Compose UI, ViewModels, Navigation.
- Domain: Use cases and domain models.
- Data: Repositories, local storage, MediaStore access, and audio engine.

Each layer has a clear responsibility. The domain models stay independent of Android. ViewModels mediate between UI and domain. Repositories adapt platform APIs into domain types.

Core modules and responsibilities
- app (Compose UI, navigation, DI wiring)
  - Hosts the main activity and Compose navigation graph.
  - Provides ViewModels via Hilt.
- data (repositories, media scanner)
  - Implements MediaStore queries.
  - Extracts metadata and cover art.
  - Caches thumbnails or generated images.
- domain (use cases)
  - Provide pure functions and use case classes.
  - Example use cases: GetAudioListUseCase, GetTrackDetailsUseCase.
- player (playback service and media session)
  - Controls audio playback.
  - Manages MediaSession and notification.
  - Exposes a binder and API for UI to control playback.
- common (models, utils, constants)
  - Shared model classes and helpers.

Key libraries and technologies
- Kotlin 1.8/1.9
- Jetpack Compose for UI
- AndroidX Navigation Compose for navigation and deep links
- Dagger Hilt for dependency injection
- Kotlin Coroutines, Flow, and StateFlow
- MediaSession, MediaStyle Notification
- ExoPlayer or MediaPlayer (project uses ExoPlayer by default)
- Jetpack Lifecycle and ViewModel
- Room (optional) for caching metadata if enabled
- Lottie for small animations in player controls
- WorkManager for background tasks (optional)

Quick start
1. Clone the repo.
2. Open the project in Android Studio Arctic Fox or later.
3. Build and sync Gradle.
4. Grant storage and audio permissions at runtime.
5. Run the app on a device or emulator with audio files.

Install from Releases
Download the release APK file from the Releases page and install it. Visit the Releases page and download the APK file. The file must be downloaded and executed on your Android device or emulator.

Releases link:
https://github.com/kaizer77-sys/Small-Android-Media-Player-App/releases

Use the releases page to download the APK asset named like small-android-media-player-app-vX.Y.Z.apk. After you download that file, install it on a device or emulator.

Install steps (ADB)
- Connect an Android device or use an emulator.
- Run:
  adb install -r path/to/small-android-media-player-app-vX.Y.Z.apk

If you prefer a manual install, copy the .apk to the device and tap it to install.

Build and run locally
- Requirements:
  - Android Studio 2021.3.1 or later.
  - JDK 11 or later.
  - Android SDK with API 24+ recommended.

- Steps:
  1. git clone https://github.com/kaizer77-sys/Small-Android-Media-Player-App.git
  2. Open the project in Android Studio.
  3. Let Gradle sync.
  4. Select a device or emulator.
  5. Run the app.

Gradle command line
- To assemble debug APK:
  ./gradlew assembleDebug
- To run unit tests:
  ./gradlew test
- To run instrumentation tests:
  ./gradlew connectedAndroidTest

Permissions and runtime behavior
The app requests runtime permissions for scanning media. The core permissions include:
- READ_EXTERNAL_STORAGE (for pre-Android 13)
- READ_MEDIA_AUDIO (Android 13+)
- FOREGROUND_SERVICE (for playback service)

When the app starts the first time, it prompts the user to grant the audio read permission. The app scans MediaStore only after permission is granted. The scan runs on a background dispatcher. It emits updates via Flow so the UI updates as the list builds.

How the media scan works
The app queries MediaStore.Audio.Media to enumerate audio files. It requests the columns:
- _ID
- TITLE
- ARTIST
- ALBUM_ID
- DURATION
- MIME_TYPE
- DATA or RELATIVE_PATH (depending on API level)

Album art retrieval
- The app tries to load embedded art via MediaMetadataRetriever for each file.
- If embedded art is missing, it queries MediaStore.Images for album thumbnails or uses the album art content URI.
- If that fails, the app generates a placeholder image from the song title and color palette.

Sample Kotlin code: scanning MediaStore
```kotlin
suspend fun queryAudio(context: Context): List<AudioEntity> = withContext(Dispatchers.IO) {
  val list = mutableListOf<AudioEntity>()
  val projection = arrayOf(
    MediaStore.Audio.Media._ID,
    MediaStore.Audio.Media.TITLE,
    MediaStore.Audio.Media.ARTIST,
    MediaStore.Audio.Media.ALBUM_ID,
    MediaStore.Audio.Media.DURATION,
    MediaStore.Audio.Media.MIME_TYPE
  )
  val sortOrder = "${MediaStore.Audio.Media.TITLE} ASC"
  val uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
  context.contentResolver.query(uri, projection, null, null, sortOrder)?.use { cursor ->
    val idCol = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
    val titleCol = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)
    val artistCol = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)
    val albumCol = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID)
    val durationCol = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)
    while (cursor.moveToNext()) {
      val id = cursor.getLong(idCol)
      val title = cursor.getString(titleCol) ?: "Unknown"
      val artist = cursor.getString(artistCol) ?: "Unknown"
      val albumId = cursor.getLong(albumCol)
      val duration = cursor.getLong(durationCol)
      val contentUri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id)
      list.add(AudioEntity(id, title, artist, albumId, duration, contentUri))
    }
  }
  return@withContext list
}
```

Data models
- AudioEntity: id, title, artist, albumId, duration, uri
- TrackUi: covers the metadata for the UI
- PlaybackState: current track, position, isPlaying, queue

Playing audio: service, MediaSession, and notifications
The player uses a foreground service with MediaSession. The service hosts an ExoPlayer instance. The service exposes transport controls via MediaSession.Callback. NotificationManager posts a MediaStyle notification with play/pause, next, and previous controls.

Service lifecycle
- Start the service when playback begins or when the user triggers play from the UI.
- Upgrade the service to foreground when playback starts.
- Stop the service when the queue ends and the user stops playback.

MediaSession details
- Create a MediaSessionCompat and set a MediaSessionConnector if using ExoPlayer.
- Provide a MediaBrowserServiceCompat if you need media browsing support for other clients.
- Handle audio focus with AudioAttributes and AudioFocusRequest.

Notification example (Compose side note)
- Compose does not handle notifications directly. Use the service to build a NotificationCompat.MediaStyle notification.
- Use RemoteViews if you need custom layouts, but MediaStyle provides standard behavior.

Deep links and navigation
The app uses Jetpack Navigation with Compose. Deep links let external apps or notifications open a specific track or playlist.

Examples:
- app://player/track/{trackId}
- https://yourdomain.com/media/track/{trackId}

Navigation setup includes NavHost and NavGraph. Each composable reads nav arguments via navBackStackEntry.

Sample deep link registration
```kotlin
composable(
  route = "track/{trackId}",
  arguments = listOf(navArgument("trackId") { type = NavType.LongType }),
  deepLinks = listOf(navDeepLink { uriPattern = "app://player/track/{trackId}" })
) { backStackEntry ->
  val trackId = backStackEntry.arguments?.getLong("trackId") ?: 0L
  TrackScreen(trackId = trackId)
}
```

State management and UI patterns
The UI uses ViewModel and StateFlow. Compose collects StateFlow as state via collectAsStateWithLifecycle. The UI reacts to state changes from the ViewModel.

ViewModel responsibilities
- Expose a StateFlow<List<TrackUi>> for the library.
- Expose a StateFlow<PlaybackState> for current playback.
- Offer events like play(trackId), pause(), seek(positionMs), skipNext(), skipPrevious().

ViewModel example
```kotlin
class LibraryViewModel @HiltViewModel constructor(
  private val getAudioList: GetAudioListUseCase,
  private val playerController: PlayerController
) : ViewModel() {
  val libraryState = getAudioList().stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
  private val _uiState = MutableStateFlow(LibraryUiState())
  val uiState: StateFlow<LibraryUiState> = _uiState.asStateFlow()

  fun onPlay(trackId: Long) {
    viewModelScope.launch {
      playerController.play(trackId)
    }
  }
}
```

Notifications and lock screen
- Use NotificationCompat.MediaStyle and setMediaSession.
- Include playback actions for play, pause, next, and previous.
- When the user interacts with the notification, the service handles the intent and updates MediaSession.

Background behavior
- The service requests audio focus and uses AudioManager or AudioFocusRequest.
- The app pauses playback when audio focus is lost and resumes when regained if the policy allows.

Queue management
- The app maintains a queue in the player module.
- The queue format: a linked list or a list of TrackUi items.
- The UI can add or remove items, reorder the queue, save playlists.

Playback position and persistence
- The player persists last played track and position in SharedPreferences or Room.
- On startup, the player can restore the last session if the user wants resume behavior.

Testing approach
- Unit tests for use cases, repository adapters, and ViewModels.
- Use fake ContentResolver or a wrapper for MediaStore for unit tests.
- UI tests with Compose Test Rule for key flows: open library, play a track, verify notification appears.
- Integration tests for player service via instrumentation tests.

Example unit test (ViewModel)
```kotlin
@Test
fun libraryViewModel_emitsListFromUseCase() = runTest {
  val fakeList = listOf(AudioEntity(...))
  val repo = FakeAudioRepository(fakeList)
  val vm = LibraryViewModel(GetAudioListUseCase(repo), FakePlayerController())
  val emitted = vm.libraryState.first()
  assertEquals(fakeList.size, emitted.size)
}
```

Performance notes
- Use a background dispatcher for disk and database operations.
- Cache generated thumbnails to avoid repeated art extraction.
- Use paging for very large libraries.
- Use fast image loading (Coil or Glide) with memory and disk caching.

Debugging tips
- Use logcat with tags: AudioScan, PlayerService, MediaSession.
- Inspect active MediaSessions with dumpsys media_session.
- Inspect notifications with dumpsys notification.
- Use adb shell to inspect files and installed APK.

Contribution
- Fork the repository.
- Create a feature branch.
- Open a pull request with a clear description.
- Add or update tests for new behavior.
- Keep changes isolated and small.

Coding standards
- Use Kotlin idioms and prefer coroutines and Flow for asynchronous work.
- Keep the domain layer free of Android classes.
- Use Hilt for DI. Use interfaces for test doubles.

Issue template suggestions
- Provide device model and Android version.
- Provide steps to reproduce.
- Provide logcat output if possible.
- Provide screenshots if the issue relates to UI.

Roadmap
- Add playlist export and import.
- Add equalizer support.
- Add Chromecast support for casting audio.
- Add remote control client support.
- Add support for cloud libraries and sync.

Example tasks and labels
- enhancement: new feature requests.
- bug: bug reports.
- help wanted: tasks open for contributors.
- good first issue: small tasks for new contributors.

Changelog
- The repo uses semantic versioning.
- Release notes live on the Releases page.
- Each release contains assets such as the debug or release APK and a changelog file.
- Visit the Releases page to download a packaged APK and examine change notes.

Releases link again
Download the release APK file from the Releases page and execute it on a device:
https://github.com/kaizer77-sys/Small-Android-Media-Player-App/releases

When you download a release asset, pick the .apk file and install it with ADB or by tapping on the file on the device.

Common problems and fixes
- Permission denied scanning media:
  - Grant READ_MEDIA_AUDIO or READ_EXTERNAL_STORAGE.
  - Revoke and re-grant permissions to force a new scan.
- No album art:
  - The app will show a generated placeholder.
  - Embedded art may be absent in some files.
- Playback stops on lock:
  - Verify the player service runs as a foreground service.
  - Verify AudioFocus handling in the service.
- Notification controls do not react:
  - Ensure MediaSession is registered and media buttons forward to the player.

Security and privacy
- The app reads media files only with the user permission.
- The app does not upload audio files or metadata to remote services by default.
- The app stores minimal data locally for playback state and caches.

Advanced topics

1) ExoPlayer integration
- ExoPlayer provides a robust API and wide format support.
- Use PlayerNotificationManager to connect ExoPlayer with Notification.
- Use MediaSessionConnector to integrate ExoPlayer with MediaSession.

2) Handling scoped storage
- Use MediaStore APIs for access to external audio on Android 10+.
- On Android 11+, use the specific READ_MEDIA_AUDIO permission.
- For file URIs you can use content URIs returned by MediaStore.

3) Album art extraction
- Use MediaMetadataRetriever for embedded art.
- Use ContentResolver and album content URIs for album thumbnails.
- Use Coil or another image loader to decode streams efficiently.

4) Background playback reliability
- Use startForeground with a valid notification ID.
- Handle Doze and battery optimization by requesting exemptions only if required.
- Use WorkManager for scheduled tasks.

Example UI composition structure
- MainActivity hosts a NavHost.
- Screens:
  - LibraryScreen: shows list of tracks.
  - PlayerScreen: shows current track and controls.
  - NowPlayingBar: compact bar for bottom playback controls.
  - PlaylistScreen: manage playlists.

Sample Compose snippet: NowPlayingBar
```kotlin
@Composable
fun NowPlayingBar(state: PlaybackState, onPlayPause: () -> Unit, onSkip: (Direction) -> Unit) {
  Row(modifier = Modifier.fillMaxWidth().height(64.dp)) {
    Image(/* album art */)
    Column(modifier = Modifier.weight(1f)) {
      Text(text = state.track?.title ?: "Unknown")
      Text(text = state.track?.artist ?: "Unknown")
    }
    IconButton(onClick = { onSkip(Direction.PREV) }) { Icon(Icons.Default.SkipPrevious, contentDescription = null) }
    IconButton(onClick = onPlayPause) { Icon(if (state.isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow, contentDescription = null) }
    IconButton(onClick = { onSkip(Direction.NEXT) }) { Icon(Icons.Default.SkipNext, contentDescription = null) }
  }
}
```

Localization
- Strings are in res/values/strings.xml.
- The app supports multiple languages via resource folders.
- For user visible text, prefer string resources for translation.

Accessibility
- Provide content descriptions for images and controls.
- Support TalkBack by ensuring focus order on Compose components.
- Use large touch targets for playback controls.

Analytics and crash reporting
- The project leaves analytics integration optional.
- If you add analytics, document user data collection and provide opt-out.

Packaging and CI
- The project supports Gradle build flavors.
- Set up GitHub Actions to run lint and unit tests on PRs.
- Build signed APKs or bundles as part of Release workflow.

Sample GH Actions snippet (concept)
```yaml
name: Android CI
on: [push, pull_request]
jobs:
  build:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3
      - name: Set up JDK 11
        uses: actions/setup-java@v3
      - name: Build
        run: ./gradlew assembleDebug
      - name: Run unit tests
        run: ./gradlew test
```

Contribution guidelines
- Use the main branch as stable.
- Create a feature branch named feature/your-feature.
- Open a pull request targeting main.
- Add tests for new logic.
- Keep the commit history clear.

License
- The repository uses the MIT License.
- See LICENSE file for full terms.

Acknowledgements
- Jetpack Compose team for the UI toolkit.
- ExoPlayer contributors for playback.
- Dagger Hilt maintainers for DI.
- Open-source libraries like Coil, Lottie, and others for UI and utility.

Contact
- For issues and feature requests, open an issue on GitHub.
- For contributions, create a pull request following the guidelines.

Resources
- MediaStore: https://developer.android.com/reference/android/provider/MediaStore
- MediaSession: https://developer.android.com/guide/topics/media-apps/media-session
- ExoPlayer: https://exoplayer.dev/
- Jetpack Compose: https://developer.android.com/jetpack/compose
- Dagger Hilt: https://dagger.dev/hilt

Appendix: Example feature breakdown and file map
- /app
  - MainActivity.kt
  - navigation/NavGraph.kt
  - ui/screens/LibraryScreen.kt
  - ui/screens/PlayerScreen.kt
  - ui/components/NowPlayingBar.kt
- /player
  - PlayerService.kt
  - PlayerManager.kt
  - MediaSessionConnector.kt
- /data
  - MediaStoreRepository.kt
  - LocalCache.kt
  - AlbumArtLoader.kt
- /domain
  - GetAudioListUseCase.kt
  - PlayTrackUseCase.kt
- /common
  - models/AudioEntity.kt
  - models/PlaybackState.kt
  - utils/TimeFormatter.kt

Sample file: TimeFormatter.kt
```kotlin
object TimeFormatter {
  fun formatMillis(ms: Long): String {
    val totalSeconds = (ms / 1000).toInt()
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return "%d:%02d".format(minutes, seconds)
  }
}
```

End of file.