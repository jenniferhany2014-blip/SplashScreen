# Zoony Store — Android E-Commerce Demo

Zoony Store is a native Android application built with **Kotlin** and **Jetpack Compose**. The project demonstrates a small e-commerce-style application with a local authentication/session layer, product browsing, product details, remote API access, offline product caching, Arabic/English resources, selectable light/dark/system themes, navigation, and dependency injection.

> **Project type:** Android application  
> **Application ID:** `com.example.splashscreen`  
> **Namespace:** `com.example.splashscreen`  
> **App name:** Zoony Store  
> **Minimum Android version:** API 24 (Android 7.0)  
> **Target SDK:** 36  
> **Compile SDK:** 36.1

---

## Table of Contents

- [Project Overview](#project-overview)
- [Main Features](#main-features)
- [Technology Stack](#technology-stack)
- [Dependency Versions](#dependency-versions)
- [Why These Technologies Are Used](#why-these-technologies-are-used)
- [Application Architecture](#application-architecture)
- [Project Structure](#project-structure)
- [Application Startup and Splash Screen](#application-startup-and-splash-screen)
- [Authentication Flow](#authentication-flow)
- [Navigation](#navigation)
- [Home and Navigation Drawer](#home-and-navigation-drawer)
- [Products and Remote API](#products-and-remote-api)
- [Offline Caching with Room](#offline-caching-with-room)
- [Session and Preferences with DataStore](#session-and-preferences-with-datastore)
- [Dependency Injection with Hilt](#dependency-injection-with-hilt)
- [ViewModels and UI State](#viewmodels-and-ui-state)
- [Error Handling](#error-handling)
- [Localization](#localization)
- [Theme System](#theme-system)
- [Reusable UI Components](#reusable-ui-components)
- [Build Configuration](#build-configuration)
- [How to Run the Project](#how-to-run-the-project)
- [How the Data Flows Through the App](#how-the-data-flows-through-the-app)
- [Screen-by-Screen Summary](#screen-by-screen-summary)
- [Testing](#testing)
- [GitHub / Repository Notes](#github--repository-notes)
- [Known Limitations and Future Improvements](#known-limitations-and-future-improvements)
- [Learning / Design Rationale](#learning--design-rationale)

---

## Project Overview

The application is designed as a simple store application called **Zoony Store**.

The main user journey is:

```text
Application starts
       |
       v
Android Splash Screen
       |
       v
MainActivity
       |
       v
Read login state from DataStore
       |
       +-----------------------------+
       |                             |
       v                             v
Not logged in                    Logged in
       |                             |
       v                             v
    Login                         Home
       |                             |
       |                       +-----+-----+---------+
       |                       |           |         |
       |                       v           v         v
       |                    Products    Profile   Settings
       |                       |
       |                       v
       |                 Product Details
       |
       v
    Sign Up
       |
       v
    Home
```

The project intentionally separates UI, state management, data access, persistence, networking, dependency injection, and error mapping instead of putting all logic directly inside Composable functions.

---

## Main Features

### Authentication

- Login screen.
- Sign-up screen.
- Local account storage using Android DataStore Preferences.
- Local credential validation.
- Email format validation.
- Password validation.
- Password confirmation validation.
- Login and sign-up loading states.
- Navigation to Home after successful authentication.
- Logout from the navigation drawer.
- Navigation back-stack cleanup after login/sign-up/logout so the user does not simply return to the previous authentication screen.

### Products

- Product list.
- Product cards containing:
  - product image
  - title
  - category
  - price
  - rating
- Product details screen.
- Product search through the remote API.
- Retry UI when a request fails.
- Remote data refresh.
- Local Room cache for previously downloaded products.
- Cached products can remain visible when the network request fails.

### UI / UX

- Jetpack Compose UI.
- Material 3 components.
- Zoony red/black/white brand palette.
- Reusable text fields and buttons.
- Branded logo component.
- Navigation drawer.
- English resources.
- Arabic resources.
- Right-to-left layout support through Android locale configuration.
- System / Light / Dark theme selection.
- AndroidX SplashScreen API.

### Architecture / Engineering

- ViewModel-based state management.
- Kotlin Coroutines.
- Kotlin Flow / StateFlow.
- Hilt dependency injection.
- Repository pattern for product data.
- Room database for product caching.
- Retrofit + Gson for REST API access.
- Centralized application error mapping.
- Gradle Version Catalog (`libs.versions.toml`).

---

# Technology Stack

| Technology | Purpose |
|---|---|
| Kotlin | Main programming language |
| Jetpack Compose | Declarative UI |
| Material 3 | UI components and theming |
| Navigation Compose | Screen navigation |
| AndroidX Activity Compose | Compose activity integration |
| AndroidX Lifecycle ViewModel | Screen state and business logic |
| Kotlin Coroutines | Asynchronous operations |
| Kotlin Flow / StateFlow | Reactive state |
| DataStore Preferences | Local session/account/theme preferences |
| Room | Local product database/cache |
| Retrofit | REST API client |
| Gson Converter | JSON-to-Kotlin object conversion for Retrofit |
| Coil Compose | Product image loading |
| Hilt | Dependency injection |
| KSP | Annotation processing/code generation for Room and Hilt |
| AndroidX SplashScreen | Startup splash screen |
| AndroidX Core KTX | Android Kotlin extensions |

---

# Dependency Versions

The following versions are taken from the project's current `gradle/libs.versions.toml`.

| Dependency | Version |
|---|---:|
| Android Gradle Plugin | 9.3.1 |
| Kotlin | 2.2.10 |
| Kotlin Compose Plugin | 2.2.10 |
| KSP | 2.2.10-2.0.2 |
| Gradle Wrapper | 9.5.0 |
| Compile SDK | 36.1 |
| Target SDK | 36 |
| Minimum SDK | 24 |
| Compose BOM | 2026.02.01 |
| Activity Compose | 1.8.0 |
| Lifecycle | 2.8.7 |
| Core KTX | 1.10.1 |
| Navigation Compose | 2.9.0 |
| AndroidX SplashScreen | 1.2.0 |
| DataStore Preferences | 1.1.1 |
| Retrofit | 2.11.0 |
| Coil Compose | 2.7.0 |
| Room | 2.8.3 |
| Hilt | 2.59.2 |
| Hilt Navigation Compose | 1.2.0 |
| JUnit | 4.13.2 |
| AndroidX Test JUnit | 1.1.5 |
| Espresso | 3.5.1 |

The Compose libraries use the Compose BOM, so individual Compose UI library versions are controlled by the BOM rather than being declared separately.

---

# Why These Technologies Are Used

## Kotlin

Kotlin is the primary Android language used throughout the project. It provides concise syntax, null-safety, coroutines, sealed classes, extension functions, and strong interoperability with Android APIs.

## Jetpack Compose

Compose is used instead of XML layouts for the application UI.

This makes UI state directly observable and allows screens such as Login, Sign Up, Products, Profile, Settings, and Product Details to be implemented as composable functions.

For example, a screen can react directly to ViewModel state:

```kotlin
val uiState by viewModel.uiState.collectAsStateWithLifecycle()
```

The UI then renders the appropriate state instead of manually updating individual views.

## Material 3

Material 3 supplies standard components such as:

- `Scaffold`
- `TopAppBar`
- `Button`
- `TextButton`
- `OutlinedTextField`
- `Card`
- `ModalNavigationDrawer`
- `NavigationDrawerItem`
- `RadioButton`
- `CircularProgressIndicator`

The project adds a custom Zoony color scheme on top of Material 3.

## ViewModel

ViewModels keep business and screen state outside Composables.

This prevents UI functions from becoming responsible for long-running operations such as database access, authentication checks, or network requests.

## Coroutines

Network, database, and DataStore operations are suspend operations. Coroutines allow these operations to run asynchronously without blocking the UI thread.

## Flow / StateFlow

DataStore exposes preferences as `Flow`, while product ViewModels use `StateFlow` for UI state.

This allows the UI to automatically update when persistent settings or screen state changes.

## Hilt

Hilt creates and supplies shared dependencies such as:

- `SessionManager`
- `Retrofit`
- `ProductApiService`
- `ProductDatabase`
- `ProductDao`
- ViewModels

This avoids manually constructing these dependencies throughout the application.

## Retrofit

Retrofit is used to communicate with the remote product REST API.

The current base URL is:

```text
https://dummyjson.com/
```

## Room

Room provides a structured local SQLite database abstraction. It is used specifically as a product cache so the application can display previously loaded products if a later network request fails.

## DataStore

DataStore Preferences is used for small persistent key/value data such as:

- login state
- name
- email
- password
- theme selection

DataStore is preferred here over a database because these values are simple application preferences/session information rather than relational product data.

## Coil

Coil loads remote product images asynchronously inside Compose using `AsyncImage`.

## KSP

KSP is used for generated code required by Room and Hilt.

---

# Application Architecture

The project follows a lightweight layered architecture:

```text
                    ┌─────────────────────────┐
                    │     Jetpack Compose     │
                    │        UI Screens       │
                    └────────────┬────────────┘
                                 │
                                 v
                    ┌─────────────────────────┐
                    │       ViewModels        │
                    │ State + validation +    │
                    │ coroutine operations    │
                    └────────────┬────────────┘
                                 │
                                 v
                    ┌─────────────────────────┐
                    │      Repositories       │
                    │   ProductRepository     │
                    └───────┬─────────┬───────┘
                            │         │
                            v         v
                    ┌───────────┐  ┌─────────────┐
                    │ Retrofit  │  │    Room     │
                    │ Remote API│  │ Local Cache │
                    └───────────┘  └─────────────┘

                    Session / Preferences
                             │
                             v
                       ┌────────────┐
                       │ DataStore  │
                       └────────────┘

                    Dependency Provider
                             │
                             v
                          ┌──────┐
                          │ Hilt │
                          └──────┘
```

### Responsibility of each layer

**UI:** Displays state and sends user actions to ViewModels.

**ViewModel:** Owns screen state, validation, coroutine work, and conversion of repository results into UI state.

**Repository:** Hides the actual product data sources from ViewModels.

**Retrofit:** Talks to the remote product API.

**Room:** Stores product cache data locally.

**DataStore:** Stores lightweight persistent session/preferences.

**Hilt:** Constructs and provides dependencies.

**ErrorMapper:** Converts technical exceptions into application-level errors that the UI can understand.

---

# Project Structure

```text
SplashScreen/
│
├── app/
│   ├── build.gradle.kts
│   │
│   └── src/
│       ├── main/
│       │   ├── AndroidManifest.xml
│       │   │
│       │   ├── java/com/example/splashscreen/
│       │   │   ├── MainActivity.kt
│       │   │   ├── ZoonyApplication.kt
│       │   │   ├── LoginScreen.kt
│       │   │   ├── SignUpScreen.kt
│       │   │   ├── Home.kt
│       │   │   ├── Profile.kt
│       │   │   ├── Settings.kt
│       │   │   ├── Product.kt
│       │   │   ├── ProductApiService.kt
│       │   │   ├── ProductListScreen.kt
│       │   │   ├── ProductDetailScreen.kt
│       │   │   ├── ProductRepository.kt
│       │   │   ├── SessionManager.kt
│       │   │   ├── LocaleHelper.kt
│       │   │   │
│       │   │   ├── data/
│       │   │   │   ├── ProductEntity.kt
│       │   │   │   ├── ProductDao.kt
│       │   │   │   ├── ProductDatabase.kt
│       │   │   │   └── ProductConverters.kt
│       │   │   │
│       │   │   ├── di/
│       │   │   │   ├── AppModule.kt
│       │   │   │   ├── DatabaseModule.kt
│       │   │   │   └── NetworkModule.kt
│       │   │   │
│       │   │   ├── error/
│       │   │   │   ├── AppError.kt
│       │   │   │   └── ErrorMapper.kt
│       │   │   │
│       │   │   ├── model/
│       │   │   │   └── DrawerScreen.kt
│       │   │   │
│       │   │   ├── ui/components/
│       │   │   │   ├── LanguageToggleButton.kt
│       │   │   │   ├── ZoonyLogo.kt
│       │   │   │   ├── ZoonyPrimaryButton.kt
│       │   │   │   └── ZoonyTextField.kt
│       │   │   │
│       │   │   ├── ui/theme/
│       │   │   │   ├── Color.kt
│       │   │   │   ├── Theme.kt
│       │   │   │   └── Type.kt
│       │   │   │
│       │   │   └── viewmodel/
│       │   │       ├── HomeViewModel.kt
│       │   │       ├── LoginViewModel.kt
│       │   │       ├── ProductDetailViewModel.kt
│       │   │       ├── ProductListViewModel.kt
│       │   │       ├── ProfileViewModel.kt
│       │   │       ├── SettingsViewModel.kt
│       │   │       └── SignUpViewModel.kt
│       │   │
│       │   └── res/
│       │       ├── drawable/
│       │       ├── mipmap-*/
│       │       ├── values/
│       │       │   ├── colors.xml
│       │       │   ├── strings.xml
│       │       │   └── themes.xml
│       │       ├── values-ar/
│       │       │   └── strings.xml
│       │       └── xml/
│       │           ├── backup_rules.xml
│       │           └── data_extraction_rules.xml
│       │
│       ├── test/
│       │   └── ExampleUnitTest.kt
│       │
│       └── androidTest/
│           └── ExampleInstrumentedTest.kt
│
├── gradle/
│   ├── libs.versions.toml
│   └── wrapper/
│
├── build.gradle.kts
├── gradle.properties
├── settings.gradle.kts
├── gradlew
└── gradlew.bat
```

> The uploaded project also contains Android Studio/Gradle generated directories and Git metadata. These should generally not be committed when they are already covered by `.gitignore` or are machine-generated.

---

# Application Startup and Splash Screen

The application uses AndroidX SplashScreen.

`MainActivity` calls:

```kotlin
installSplashScreen()
```

and does so **before** `super.onCreate()`.

The manifest uses:

```xml
android:theme="@style/Theme.App.Starting"
```

The starting theme is defined in `res/values/themes.xml`:

```xml
<style name="Theme.App.Starting" parent="Theme.SplashScreen">
    <item name="windowSplashScreenBackground">#FFFFFF</item>
    <item name="windowSplashScreenAnimatedIcon">@drawable/my_logo1</item>
    <item name="postSplashScreenTheme">@style/Theme.App</item>
</style>
```

### Why use a separate splash theme?

The splash theme belongs to Android startup, while `Theme.App` is the real application theme used after the splash screen.

This prevents the startup window from being dependent on the Compose UI being created first.

The splash screen uses the Zoony logo from:

```text
app/src/main/res/drawable/my_logo1.png
```

---

# Authentication Flow

## Login

`LoginScreen` collects email and password input and delegates authentication to `LoginViewModel`.

The ViewModel checks:

1. Email is not blank.
2. Email matches `Patterns.EMAIL_ADDRESS`.
3. Password is not blank.
4. Stored credentials match the entered credentials.

If successful:

```kotlin
sessionManager.setLoggedIn(true)
```

Then navigation goes to Home and removes Login from the back stack:

```kotlin
navController.navigate("home") {
    popUpTo("login") {
        inclusive = true
    }
    launchSingleTop = true
}
```

This is important because pressing Back should not return the authenticated user to the login screen.

## Sign Up

`SignUpScreen` collects:

- name
- email
- password
- confirm password

`SignUpViewModel` validates:

- name has at least 2 characters after trimming
- email format is valid
- password is at least 8 characters
- password contains an uppercase letter
- password contains a lowercase letter
- password contains a digit
- password contains a special character
- confirmation password matches

After successful registration, `SessionManager.saveAccount()` stores the account and sets the login state to true.

The user is then sent to Home.

---

# Navigation

Navigation is implemented using Navigation Compose.

The current graph contains these routes:

| Route | Screen |
|---|---|
| `login` | Login screen |
| `signup` | Sign-up screen |
| `home` | Main application shell |
| `product_detail/{productId}` | Product detail screen |

The product ID is passed as an integer navigation argument:

```kotlin
navArgument("productId") {
    type = NavType.IntType
}
```

Product cards navigate using:

```kotlin
navController.navigate("product_detail/${product.id}")
```

### Why use Navigation Compose?

It keeps navigation declarative and integrates directly with Compose. It also provides back-stack management and typed navigation arguments through `NavType`.

---

# Home and Navigation Drawer

`Home.kt` is the main authenticated application shell.

It uses `ModalNavigationDrawer` and provides three internal sections:

```kotlin
enum class DrawerScreen {
    HOME,
    SETTINGS,
    PROFILE
}
```

These are internal logical values, not translated strings. The visible labels are translated through Android resources.

The drawer contains:

- Home
- Profile
- Settings
- Logout

The top app bar also contains the language switch button.

`HomeViewModel` stores the currently selected drawer section and handles logout.

---

# Products and Remote API

The product API is defined by `ProductApiService`.

### Base URL

```text
https://dummyjson.com/
```

### Endpoints used

#### Get products

```http
GET /products
```

Implemented as:

```kotlin
@GET("products")
suspend fun getProducts(): ProductListResponse
```

#### Get one product

```http
GET /products/{id}
```

Implemented as:

```kotlin
@GET("products/{id}")
suspend fun getProductById(@Path("id") id: Int): Product
```

#### Search products

```http
GET /products/search?q={query}
```

Implemented as:

```kotlin
@GET("products/search")
suspend fun searchProducts(
    @Query("q") query: String
): ProductListResponse
```

### Product model

`Product` contains:

- `id`
- `title`
- `description`
- `category`
- `price`
- `rating`
- `thumbnail`
- `images`

`ProductListResponse` contains:

- `products`
- `total`
- `skip`
- `limit`

Gson converts the JSON responses into these Kotlin data classes.

---

# Offline Caching with Room

Room is used as a local cache for products.

The database is named:

```text
zoony_products.db
```

The table is:

```text
products
```

The entity is `ProductEntity`.

### DAO operations

`ProductDao` provides:

```kotlin
getAll()
count()
insertAll()
deleteAll()
```

Products are ordered by ID:

```sql
SELECT * FROM products ORDER BY id ASC
```

### Product conversion

The project keeps API/domain models separate from the Room entity:

```text
Product <----> ProductEntity
```

Conversion functions are:

```kotlin
ProductEntity.toProduct()
Product.toEntity()
```

### Why a TypeConverter?

`Product.images` is a `List<String>`, which Room cannot store directly as a normal SQLite column.

`ProductConverters` uses Gson to serialize the list into a string and deserialize it again.

### Cache-first behavior

When `ProductListViewModel.fetchProducts()` runs:

1. It attempts to read cached products from Room.
2. If cached products exist, they are displayed immediately.
3. The remote API is requested to refresh the list.
4. Fresh products are saved into Room.
5. If the network request fails and cached products exist, the cached products remain visible.
6. If there is no cache and the request fails, an error state is displayed.

This provides a basic offline-friendly experience.

---

# Session and Preferences with DataStore

`SessionManager` owns the DataStore preferences.

The DataStore file is named:

```text
zoony_prefs
```

The stored keys are:

| Key | Type | Purpose |
|---|---|---|
| `is_logged_in` | Boolean | Authentication/session state |
| `user_name` | String | Local account name |
| `user_email` | String | Local account email |
| `user_password` | String | Local account password |
| `theme_mode` | String | `system`, `light`, or `dark` |

### Reactive state

`SessionManager` exposes:

```kotlin
val isLoggedIn: Flow<Boolean>
val user: Flow<UserAccount?>
val themeMode: Flow<String>
```

The Compose root collects these flows and reacts to changes.

### Why `Boolean?` is used in the UI

The app starts collecting `isLoggedIn` with:

```kotlin
collectAsState(initial = null)
```

This creates three conceptual states:

```text
null  -> DataStore state has not been read yet
false -> user is not logged in
true  -> user is logged in
```

The temporary `null` state allows the app to show a progress indicator while the persistent session value is being obtained.

---

# Dependency Injection with Hilt

Hilt is initialized by:

```kotlin
@HiltAndroidApp
class ZoonyApplication : Application()
```

`MainActivity` is an injection entry point:

```kotlin
@AndroidEntryPoint
class MainActivity : ComponentActivity()
```

ViewModels are created with:

```kotlin
@HiltViewModel
```

and injected using constructor injection.

## Hilt modules

### `AppModule`

Provides the singleton `SessionManager` using the application context.

### `NetworkModule`

Provides:

- singleton `Retrofit`
- singleton `ProductApiService`

### `DatabaseModule`

Provides:

- singleton `ProductDatabase`
- `ProductDao`

### Dependency graph

```text
ZoonyApplication
      |
      v
     Hilt
      |
      +------------------+
      |                  |
      v                  v
SessionManager       Retrofit
      |                  |
      |                  v
      |           ProductApiService
      |
      |              +-------------+
      |              |
      |              v
      |         ProductRepository
      |              ^
      |              |
      |          ProductDao
      |              ^
      |              |
      |       ProductDatabase
      |
      v
Login / SignUp / Settings / Profile ViewModels
```

---

# ViewModels and UI State

## `LoginViewModel`

Responsible for:

- email state
- password state
- loading state
- validation
- local credential validation
- error messages

## `SignUpViewModel`

Responsible for:

- registration form state
- password requirements
- validation
- account creation
- loading state
- error messages

## `HomeViewModel`

Responsible for:

- selected drawer section
- logout operation

## `ProductListViewModel`

Responsible for:

- product loading
- cache lookup
- API refresh
- search
- loading/success/error UI states

It uses a sealed UI state:

```text
Loading
Success(products, fromCache)
Error(error, cachedProducts)
```

## `ProductDetailViewModel`

Loads one product by ID and exposes:

```text
Loading
Success(product)
Error(error)
```

## `ProfileViewModel`

Exposes the locally stored user flow from `SessionManager`.

## `SettingsViewModel`

Exposes the current theme and writes theme changes to DataStore.

---

# Error Handling

The application does not expose raw technical exceptions directly to the UI.

Instead, `AppError` defines a common error model:

```text
Network
Http(code)
Unauthorized
NotFound
EmptyResponse
Database
Unknown
```

`ErrorMapper` converts technical exceptions into these values.

### Mapping

| Technical condition | Application error |
|---|---|
| `IOException` | `Network` |
| HTTP 401 / 403 | `Unauthorized` |
| HTTP 404 | `NotFound` |
| Other HTTP errors | `Http(code)` |
| `SQLException` | `Database` |
| `IllegalStateException` | `EmptyResponse` |
| Anything else | `Unknown` |

The UI then uses `ErrorMapper.userMessage()` to display a user-friendly message.

### Why centralize error mapping?

Without an error mapper, every screen would need to know about Retrofit exceptions, Room exceptions, HTTP status codes, and other technical details.

With the mapper, ViewModels can work with a small application-level error model and the UI can display consistent messages.

---

# Localization

The project includes:

```text
res/values/strings.xml
res/values-ar/strings.xml
```

English is the default language and Arabic is provided as the alternate language.

The UI uses Android string resources instead of hardcoding most visible labels:

```kotlin
stringResource(R.string.app_name)
```

### Arabic support

`LocaleHelper` supports:

```text
en
ar
```

It changes the `Configuration` locale and layout direction and recreates the activity when the user switches languages.

Arabic strings are located in:

```text
app/src/main/res/values-ar/strings.xml
```

### Language switch

The Home and Login screens expose a language button.

The current language is determined from the active Android configuration.

The project also contains `LanguageToggleButton.kt`, but the current implementation of that reusable component is intentionally disabled; language switching is currently implemented directly by the screens that need it.

---

# Theme System

The project defines a custom Zoony Material 3 theme.

## Brand colors

From `Color.kt`:

| Color | Value | Role |
|---|---|---|
| Zoony Red | `#E4001C` | Primary brand color |
| Zoony Red Dark | `#B3001A` | Darker/error variant |
| Zoony Black | `#0D0D0D` | Secondary / dark text |
| Zoony White | `#FFFFFF` | Light background / foreground |
| Zoony Gray | `#F5F5F5` | Subtle surfaces |
| Zoony Text Gray | `#6E6E6E` | Secondary text |

## Theme modes

The application supports:

```text
System Default
Light
Dark
```

The selected value is persisted through DataStore.

The root Compose function calculates the effective theme:

```kotlin
val darkTheme = when (themeMode) {
    SessionManager.THEME_DARK -> true
    SessionManager.THEME_LIGHT -> false
    else -> isSystemInDarkTheme()
}
```

`SplashScreenTheme` then chooses either the Zoony light scheme or Zoony dark scheme.

### System bars

The theme also updates the Android status bar color and the status bar icon appearance using `WindowCompat`.

---

# Reusable UI Components

The project contains a small component layer under `ui/components`.

## `ZoonyLogo`

Displays `my_logo1.png` as a circular logo.

Default size:

```text
120.dp
```

Used on authentication screens.

## `ZoonyPrimaryButton`

A branded full-width Material button.

- Zoony red background.
- Rounded corners.
- 48dp height.
- White text.

## `ZoonyTextField`

Reusable `OutlinedTextField` supporting:

- label
- error state
- supporting text
- password transformation
- Zoony red focused border/label/cursor

## `LanguageToggleButton`

A placeholder reusable component for language switching. It is currently disabled while the active screens handle language switching directly.

---

# Build Configuration

The application module uses:

```kotlin
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt.android)
}
```

Java compatibility is set to Java 17:

```kotlin
compileOptions {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}
```

Compose is enabled with:

```kotlin
buildFeatures {
    compose = true
}
```

The project uses a Gradle Version Catalog in:

```text
gradle/libs.versions.toml
```

This keeps dependency coordinates and versions centralized instead of scattering version strings throughout build files.

---

# How to Run the Project

## Requirements

Install:

1. Android Studio with support for the project's Android Gradle Plugin/Kotlin versions.
2. Android SDK platform 36 / the required 36.1 compile platform.
3. JDK 17.
4. An Android emulator or physical Android device.

## Clone

```bash
git clone <your-repository-url>
cd SplashScreen
```

## Open in Android Studio

Open the root `SplashScreen` directory.

Allow Gradle to sync and download the required dependencies.

## Run

Select the `app` configuration and run it on an emulator/device.

Alternatively, from the project root:

```bash
./gradlew :app:assembleDebug
```

On Windows:

```bat
gradlew.bat :app:assembleDebug
```

To install the debug APK using Gradle/Android Studio, use the normal Android Studio Run action or the appropriate connected-device Gradle task.

---

# How the Data Flows Through the App

## Login

```text
LoginScreen
    |
    v
LoginViewModel
    |
    v
SessionManager
    |
    v
DataStore
```

The stored email/password are compared with the values entered on the Login screen.

## Sign Up

```text
SignUpScreen
    |
    v
SignUpViewModel
    |
    v
SessionManager
    |
    v
DataStore
```

After saving the account, the login state is set to `true`.

## Product list

```text
ProductListScreen
       |
       v
ProductListViewModel
       |
       v
ProductRepository
      / \
     /   \
    v     v
 Room   Retrofit
  |       |
  v       v
Cache   DummyJSON
```

The ViewModel first reads the cache, then attempts a remote refresh.

## Product details

```text
ProductDetailScreen
        |
        v
ProductDetailViewModel
        |
        v
ProductRepository
        |
        v
ProductApiService
        |
        v
DummyJSON
```

---

# Screen-by-Screen Summary

## Login Screen

Purpose:

- authenticate an existing local account
- switch language
- move to Sign Up

Main UI:

- Zoony logo
- app name
- welcome subtitle
- email field
- password field
- login button
- create-account button
- language switch

## Sign Up Screen

Purpose:

- create the local account

Main UI:

- name
- email
- password
- confirm password
- password requirements
- sign-up button
- return-to-login action

## Home

Purpose:

- authenticated application shell
- product browsing
- drawer navigation
- logout

## Products

Purpose:

- show products from the API
- show cached products when available
- search products
- open product details

## Product Details

Purpose:

- show a selected product's image, title, category, rating, price, and description
- provide back navigation
- retry failed requests

## Profile

Purpose:

- display the locally stored account information

Displayed fields:

- name
- email
- masked password representation

## Settings

Purpose:

- choose theme mode

Available options:

- System Default
- Light
- Dark

---

# Testing

The project contains the standard Android test locations:

```text
app/src/test/
app/src/androidTest/
```

Current files include the generated example unit and instrumentation tests.

The Gradle configuration also includes dependencies for:

- JUnit
- AndroidX JUnit
- Espresso
- Compose UI testing

The application would benefit from replacing the generated example tests with tests for the actual application behavior.

Recommended future tests include:

- Login validation.
- Sign-up password validation.
- Login success/failure.
- DataStore session behavior.
- Product repository cache behavior.
- Product ViewModel loading/error states.
- Navigation behavior.
- Theme persistence.
- Arabic/English resource behavior.
- Compose UI tests for Login and Product screens.

---

# GitHub / Repository Notes

## What should be committed

The source project should normally contain:

```text
app/
gradle/
build.gradle.kts
gradle.properties
gradlew
gradlew.bat
settings.gradle.kts
```

along with the source/resource files.

## What should not be committed

The project `.gitignore` already excludes generated/local files such as:

```text
.gradle
local.properties
.idea/caches
.idea/libraries
.idea/modules.xml
.idea/workspace.xml
build
captures
.externalNativeBuild
.cxx
```

`local.properties` should remain local because it normally contains machine-specific Android SDK information.

Generated build outputs should also not be pushed.

## Recommended first GitHub commit

A clean repository should have a structure similar to:

```text
SplashScreen/
├── app/
├── gradle/
├── .gitignore
├── build.gradle.kts
├── gradle.properties
├── gradlew
├── gradlew.bat
├── settings.gradle.kts
└── README.md
```

---

# Known Limitations and Future Improvements

This project is a learning/demo application, so there are several areas that should be improved before treating it as a production e-commerce application.

## 1. Password security

The current implementation stores the account password directly in DataStore Preferences.

That is **not appropriate for a production authentication system**.

A production application should not store raw passwords locally. A real backend authentication system should issue a secure session/token, and sensitive credentials should be handled using an appropriate authentication architecture.

## 2. Local authentication only

The Login and Sign Up flow is local. There is no real user-account backend.

The product API is remote, but authentication is not connected to the product API.

## 3. Product search is remote

Normal product loading uses Room caching, but search requests are sent to the remote API and are not separately cached.

## 4. Product details are remote

Product detail requests currently use the API directly rather than reading the selected product from Room first.

## 5. Tests are mostly placeholders

The project includes the standard generated tests, but the application-specific behavior should have dedicated unit/UI tests.

## 6. Language handling could be modernized

The current `LocaleHelper` directly updates the resource configuration and recreates the Activity. A future version could use Android's more modern per-app language APIs where appropriate.

## 7. Some strings remain directly in Kotlin

Most user-facing labels are resource-based, but some product/detail/error strings are still written directly in Kotlin. A production localization pass should move all visible text into string resources.

## 8. Package organization can be cleaned up

The project has a `viewmodel` directory, but `ProfileViewModel.kt` is currently declared in the `com.example.splashscreen` package rather than `com.example.splashscreen.viewmodel`. This works but is inconsistent with the directory structure and could be cleaned up.

## 9. UI polish

The project is functional but can be expanded with:

- pull-to-refresh
- better product cards
- image loading placeholders
- empty-state screens
- pagination
- cart functionality
- favorites
- checkout
- product filtering
- better accessibility semantics
- improved responsive layouts

---

# Learning / Design Rationale

The project is intentionally built around several Android development concepts rather than implementing everything inside one Activity.

### Why Compose?

To practice declarative Android UI and reactive rendering.

### Why ViewModels?

To keep screen state and business logic independent from the Composable lifecycle.

### Why Coroutines?

To perform DataStore, Room, and Retrofit operations asynchronously.

### Why Flow/StateFlow?

To make persistent state and UI state observable and reactive.

### Why Hilt?

To learn dependency injection and avoid manually constructing repositories, databases, Retrofit services, and session managers.

### Why Retrofit?

To demonstrate communication with a REST API using Kotlin suspend functions.

### Why Room?

To demonstrate structured local persistence and a simple offline-first/cache strategy.

### Why DataStore?

To store lightweight application preferences and local session state without using a relational database for simple key/value data.

### Why a Repository?

To separate the ViewModels from the actual data sources. The ViewModel should not need to know whether products came from Retrofit, Room, or another future source.

### Why an ErrorMapper?

To avoid spreading Retrofit/Room exception handling throughout the UI and create one application-level error model.

### Why resource strings?

To support localization and keep user-facing text separate from business logic.

### Why a theme layer?

To centralize the application's visual identity and allow light/dark/system modes to be changed consistently.

### Why a splash screen theme?

To provide a proper Android startup experience before the Compose UI is ready.

---

# Summary

Zoony Store is a Kotlin Android application demonstrating a modern Compose-oriented application structure:

```text
Kotlin
  +
Jetpack Compose
  +
Material 3
  +
Navigation Compose
  +
ViewModel / Coroutines / Flow
  +
Hilt
  +
Retrofit / Gson
  +
Room
  +
DataStore
  +
Coil
  +
AndroidX SplashScreen
  +
English / Arabic resources
  +
Light / Dark / System themes
```

The central design goal is to keep responsibilities separated:

```text
UI -> ViewModel -> Repository -> Data Sources
                         |          |
                         |          +--> Retrofit / API
                         +-------------> Room / Cache

Session / Preferences -> DataStore

Dependencies -> Hilt

Technical Exceptions -> ErrorMapper -> AppError -> UI message
```

This makes the project easier to understand, maintain, test, and extend with future features such as a real authentication backend, shopping cart, favorites, checkout, pagination, and stronger security.

---

## License

No project-specific license is currently defined in the provided source. Add a license file if this repository is intended for public redistribution or reuse.
