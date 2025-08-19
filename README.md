# Blog Management Android App

A modern Android application for managing blog posts with full API operations, built using Jetpack Compose and following MVVM architecture pattern.

## Features

- **View All Posts**: Display all blog posts in a clean list with circular images and titles
- **Post Details**: View full blog post details with large image, title, and content
- **Create Posts**: Add new blog posts with image, title, and content
- **Edit Posts**: Update existing blog posts
- **Delete Posts**: Remove blog posts with confirmation dialog
- **Image Upload**: Select images from device gallery
- **Loading States**: Visual feedback during network operations
- **Error Handling**: User-friendly error messages for network issues

## Architecture

**Design Pattern**: MVVM (Model-View-ViewModel)

### Architecture Components:
- **Model**: `Blog` data class representing blog post structure
- **View**: Jetpack Compose UI screens and components
- **ViewModel**: `BlogViewModel` managing UI state and business logic
- **Repository**: `BlogRepository` handling data operations
- **API Service**: Retrofit interface for network communication

### Project Structure:
```
app/src/main/java/com/example/app/
├── data/
│   ├── api/
│   │   ├── BlogApiService.kt      # Retrofit API interface
│   │   └── NetworkModule.kt       # Network configuration
│   ├── model/
│   │   └── Blog.kt               # Data model
│   └── repository/
│       └── BlogRepository.kt     # Data repository
├── ui/
│   ├── component/
│   │   └── NetworkDialog.kt  # Check Internet
│   ├── dialog/
│   │   └── CreateEditBlogDialog.kt  # Create/Edit dialog
│   ├── navigation/
│   │   └── BlogNavigation.kt     # Navigation setup
│   ├── screen/
│   │   ├── BlogListScreen.kt     # Main list screen
│   │   └── BlogDetailScreen.kt   # Detail screen
│   └── viewmodel/
│       └── BlogViewModel.kt      # ViewModel
├── util/
│   └── NetworkMonitor.kt  # Monitor Internet
│   
└── MainActivity.kt               # Main activity
```

## External Packages Used

### Core Dependencies:
Retrofit 3.0.0
Why: Industry standard for making REST API calls, with excellent support for Kotlin Coroutines.
OkHttp 5.1.0 (with Logging Interceptor)
Why: A powerful HTTP client with built-in logging for requests/responses, making debugging easier.
Coil 2.7.0
Why: A modern, lightweight image loading library optimized for Jetpack Compose.
Jetpack Compose Navigation 2.9.3
Why: Type safe navigation library designed specifically for Jetpack Compose screens.
Lifecycle ViewModel Compose 2.9.2
Why: Provides lifecycle aware state management for UI layers.
Kotlin Coroutines 1.10.2
Why: Simplifies asynchronous programming and background task handling in a clean and efficient way.
Material 3 (1.3.2)
Why: The latest UI component library from Google, following the modern Material Design System.

### Architecture Benefits:
- **MVVM Pattern**: Clear separation of concerns, testable code
- **Repository Pattern**: Centralized data access, easy to mock for testing
- **Coroutines**: Asynchronous operations without blocking UI thread
- **StateFlow**: Reactive state management with lifecycle awareness

## API Integration

Base URL: `http://taskapi.astra-tech.net/`

## UI/UX Features

- **Material Design 3**: Modern, accessible UI components
- **Responsive Layout**: Adapts to different screen sizes
- **Loading Indicators**: Visual feedback during operations
- **Error Handling**: User-friendly error messages
- **Image Optimization**: Circular thumbnails in list, full images in details
- **Intuitive Navigation**: Back button, floating action button for creation

## Setup Instructions

1. Clone the repository
2. Open in Android Studio
3. Sync Gradle dependencies
4. Run the app on device/emulator with API level 24+

## Permissions Required

- `INTERNET`: For API communication
- `READ_EXTERNAL_STORAGE`: For image selection from gallery
- `ACCESS_NETWORK_STATE`: To check internet connectivity status before making API calls