# HabitFlow Android App
Built for HabitFlow POC demonstration

## 👥 Credits

MIT License - see LICENSE file

## 📄 License

- For physical device, use your computer's local IP address
- For emulator, backend should be accessible at http://10.0.2.2:3000
- Verify backend is running on http://localhost:3000
- Check Logcat for API responses

## 🐛 Debugging

- `ApiClient`: Retrofit configuration
- `HabitFlowApiService`: Retrofit API interface
### Services

- `MainScreen`: Habit tracking UI
- `OnboardingScreen`: Questionnaire UI
### Screens

- `HabitFlowRepository`: Data layer abstraction
### Repositories

- `HabitFlowViewModel`: Main app state management
### ViewModels

## 🎯 Key Components

- lastUpdated (Long)
- onboardingCompleted (Boolean)
- selectedPersonaId (String)
- id (Int, PK = 1)
### User Settings Table

- date (String, ISO format)
- completed (Boolean)
- name (String)
- id (Int, PK, auto-increment)
### Habits Table

- color, emoji
- name, title, motivationMsg, completionMsg
- id (String, PK)
### Personas Table

## 🗄️ Database Schema

- `GET /api/health` - Backend health check
- `POST /api/motivation/generate` - Get AI motivation
- `POST /api/persona/determine` - Determine user persona

## 🔌 API Endpoints Used

   - Returns to onboarding screen
   - Option to choose different persona
5. **Persona Reset**

   - Message displayed in special card
   - AI generates personalized message based on progress
   - Request fresh motivation from backend
4. **AI Integration**

   - Track progress percentage
   - See completion feedback message
   - Check off habits as completed
3. **Habit Tracking**

   - Button to get AI-generated motivation
   - Shows progress bar
   - Lists 3 daily habits to track
   - Displays default motivation message
   - Shows personalized greeting with persona emoji
2. **Main Screen**

   - Persona saved to local database
   - App determines persona (Supporter or Challenger)
   - User answers a questionnaire
1. **Onboarding Screen**

## 🎨 App Flow

APK location: `app/build/outputs/apk/release/app-release.apk`
```
./gradlew assembleRelease
```bash
### Release APK

APK location: `app/build/outputs/apk/debug/app-debug.apk`
```
./gradlew assembleDebug
```bash
### Debug APK

## 📦 Build APK

     ```
     ./gradlew installDebug
     ./gradlew assembleDebug
     ```bash
   - Or use command line:
   - Click the "Run" button in Android Studio
5. **Build and Run**:

   ```
   npm run dev
   cd ../habitflow-backend
   ```bash
4. **Run the backend**:

   - For physical device: Edit `app/build.gradle.kts` and change the API_BASE_URL to your computer's IP
   - For emulator: Already configured to `http://10.0.2.2:3000`
3. **Configure Backend URL**:

   - Let Android Studio sync and download dependencies
2. **Sync Gradle**:

   Open in Android Studio
   ```
   cd habitflow-android
   ```bash
1. **Open the project**:

### Installation

- Gradle 8.2+
- Android SDK 34
- JDK 17
- Android Studio Hedgehog (2023.1.1) or later

### Prerequisites

## 🔧 Setup Instructions

```
└── MainActivity.kt
├── HabitFlowApplication.kt
│   └── viewmodel/     # ViewModels
│   ├── theme/         # Material theme configuration
│   ├── screens/       # Composable screens
├── ui/
│   └── repository/    # Repository implementation
│   ├── model/         # Data models
│   ├── local/         # Room database entities and DAOs
│   ├── api/           # API service and client
├── data/
app/
```

## 🏗️ Project Structure

- **Target SDK**: 34 (Android 14)
- **Min SDK**: 24 (Android 7.0)
- **DI**: Manual dependency injection
- **Async**: Kotlin Coroutines + Flow
- **Networking**: Retrofit + OkHttp
- **Database**: Room
- **Architecture**: MVVM with Repository pattern
- **UI Framework**: Jetpack Compose
- **Language**: Kotlin

## 📱 Tech Stack

- **REST API Integration**: Connects to TypeScript backend
- **Local Storage**: Room database for offline functionality
- **Jetpack Compose UI**: Modern, Material 3 design
- **Daily Habit Tracking**: Track 3 core habits daily
- **AI-Powered Motivation**: Get personalized motivation messages from AI
- **2 Personas**: Supporter (💪) and Challenger (🔥)

## 🚀 Features

Native Android application for HabitFlow AI - A persona-based habit tracking app with AI-powered motivation.


