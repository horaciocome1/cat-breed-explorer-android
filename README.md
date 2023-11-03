# Cat Breed Explorer

![GitHub Workflow Status (with event)](https://img.shields.io/github/actions/workflow/status/horaciocome1/cat-breed-explorer-android/android-build.yml?label=build) ![GitHub Workflow Status (with event)](https://img.shields.io/github/actions/workflow/status/horaciocome1/cat-breed-explorer-android/android-testing.yml?label=unit%20test) ![GitHub Workflow Status (with event)](https://img.shields.io/github/actions/workflow/status/horaciocome1/cat-breed-explorer-android/android-lint.yml?label=lint) ![GitHub top language](https://img.shields.io/github/languages/top/horaciocome1/cat-breed-explorer-android) ![GitHub release (with filter)](https://img.shields.io/github/v/release/horaciocome1/cat-breed-explorer-android) ![GitHub repo size](https://img.shields.io/github/repo-size/horaciocome1/cat-breed-explorer-android) ![GitHub code size in bytes](https://img.shields.io/github/languages/code-size/horaciocome1/cat-breed-explorer-android) ![GitHub commit activity (branch)](https://img.shields.io/github/commit-activity/w/horaciocome1/cat-breed-explorer-android)

CatBreedExplorer is a native Android app that allows users to explore various cat breeds using TheCatAPI. The app provides a comprehensive list of cat breeds, including images, origins, and detailed information. Users can mark their favorite breeds, search for specific breeds, and even explore offline with locally stored data.

## Features

- **Breed List Screen:**
  - Display a list of cat breeds with images, names, and origins.
  - Additional information can be viewed for each breed.
  - Search bar to filter breeds by name.
  - Navigate to the Detail screen by selecting a breed.
  
- **Detail Screen:**
  - View detailed information for a selected breed, including image, name, origin, life span, temperament, and description.
  - Option to add/remove the breed from favorites.
  
- **Favorites Screen:**
  - List of favorite cat breeds.
  - Search bar to filter favorite breeds by name.
  - Navigate to the Detail screen by selecting a breed.

- **Offline Functionality:**
  - All data is cached locally, allowing the app to function offline.
  - Utilizes local storage to display information when there is no internet connection.

## Installation

1. Clone the
   repository: `git clone https://horaciocome1-admin@bitbucket.org/horaciocome1/cat-breed-explorer-android.git`
2. Open the project in your preferred IDE (requires *Android Studio Giraffe | 2022.3.1 Patch 2*)
3. Install `CMake` and `NDK` from `SDK Manager -> SDK Tools`
4. Ask for `libnative-lib.cpp`
5. Copy received `libnative-lib.cpp` to `app/src/main/cpp`
6. Build and run the app on your Android device/emulator.

## Usage

- Browse the list of cat breeds on the Breed List screen.
- Select a breed to view detailed information on the Detail screen.
- Add or remove breeds from favorites using the button provided.
- Explore your favorite breeds with the favorites filter on toolbar.
- Use the search bar to filter breeds by name on both the Breed List and Favorites screens.

## Technologies Used

- Native Android development (Kotlin)
- [TheCatAPI](https://thecatapi.com/) - for cat breed data
- [Ktor](https://ktor.io/docs/welcome.html) - a framework to easily build connected applications –
  web applications, HTTP services, mobile and browser applications
- [Kotlin Serialization](https://kotlinlang.org/docs/serialization.html) - provides sets of
  libraries for all supported platforms – JVM, JavaScript, Native – and for various serialization
  formats – JSON, CBOR, protocol buffers, and others
- [Kotlin Coroutines](https://kotlinlang.org/docs/coroutines-guide.html) - a rich library for
  coroutines developed by JetBrains
- [Jetpack Compose](https://developer.android.com/jetpack/compose) - recommended modern toolkit for
  building native UI
- [Material 3](https://m3.material.io) - latest version of Google’s open-source design system
- [Jetpack Room](https://developer.android.com/jetpack/androidx/releases/room) - provides an
  abstraction layer over SQLite to allow for more robust database access while harnessing the full
  power of SQLite
- [Jetpack DataStore](https://developer.android.com/topic/libraries/architecture/datastore) - a data
  storage solution that allows you to store key-value pairs or typed objects with protocol buffers
- [Dagger Hilt](https://dagger.dev/hilt) - provides a standard way to incorporate Dagger dependency
  injection into an Android application
- [KSP](https://github.com/google/ksp) - provides a simplified compiler plugin API that leverages
  the power of Kotlin while keeping the learning curve at a minimum
- [MockK](https://mockk.io) - mocking library for Kotlin
- [Timber](https://github.com/JakeWharton/timber) - a logger with a small, extensible API which
  provides utility on top of Android's normal Log class
- [Compose destinations](https://composedestinations.rafaelcosta.xyz) - processes annotations and
  generates code that uses Official Jetpack Compose Navigation under the hood
- [Coil](https://coil-kt.github.io/coil/) - image loading library for Android backed by Kotlin
  Coroutines
- [CMake](https://developer.android.com/ndk/guides/cmake) - compile and debug native code for your
  app

## Contribution

Contributions are welcome! Please follow these steps if you want to contribute to the project.

1. Fork the repository.
2. Create a new branch: `git checkout -b feature/new-feature`
3. Make your changes and commit them: `git commit -m 'Add new feature'`
4. Push to the branch: `git push origin feature/new-feature`
5. Submit a pull request.

## License

This project is licensed under the Apache License, Version 2.0 License - see the [http://www.apache.org/licenses/LICENSE-2.0](http://www.apache.org/licenses/LICENSE-2.0) doc for details.

### External assets

 - <a href="https://www.flaticon.com/free-icons/cat" title="cat icons">Cat icons created by Icon Mela - Flaticon</a>

---

**CatBreedExplorer** - Explore the world of cat breeds with this delightful app! 🐾

For more information, contact [Horácio Comé](https://horaciocome1.github.io/).
