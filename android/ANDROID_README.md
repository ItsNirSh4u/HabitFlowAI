# HabitFlow Android App

## Overview
HabitFlow is an AI-powered habit tracking Android application built with Kotlin and Jetpack Compose. It features personalized motivation based on user personas and integrates with AI services for dynamic habit recommendations.

## Features

### 🎯 Core Features
- **Persona-Based Motivation**: Choose between "The Supporter" (encouraging) or "The Challenger" (competitive) personas
- **AI-Powered Motivation**: Get personalized motivational messages powered by AI
- **Daily Habit Tracking**: Track your daily habits with an intuitive checkbox interface
- **Progress Visualization**: See your daily progress with beautiful progress indicators
- **Animated UI**: Smooth animations and transitions throughout the app

### 🤖 AI Integration
- Real-time AI motivation generation via backend API
- Fallback to intelligent mock responses when API unavailable
- Context-aware messages based on habit completion status
- Persona-specific AI coaching style

### 📱 Technical Features
- **Kotlin** + **Jetpack Compose** for modern UI
- **Room Database** for local data persistence
- **Retrofit** for network communication
- **Coroutines & Flow** for reactive programming
- **MVVM Architecture** with ViewModels
- **Material 3** design system

## Project Structure

```
habitflow-android/
├── app/
│   ├── src/main/
│   │   ├── java/com/habitflow/app/
│   │   │   ├── data/
│   │   │   │   ├── api/          # API service & client
│   │   │   │   ├── local/        # Room database
│   │   │   │   ├── model/        # Data models
│   │   │   │   └── repository/   # Data repository
│   │   │   ├── ui/
│   │   │   │   ├── components/   # Reusable UI components
│   │   │   │   ├── screens/      # App screens
│   │   │   │   ├── theme/        # App theme
│   │   │   │   └── viewmodel/    # ViewModels
│   │   │   ├── HabitFlowApplication.kt
│   │   │   └── MainActivity.kt
│   │   └── res/                  # Resources
│   └── build.gradle.kts
```

## Setup Instructions

### Prerequisites
- Android Studio Hedgehog (2023.1.1) or later
- JDK 17 or later
- Android SDK (API 24+)
- Backend server running (see habitflow-backend README)

### Installation

1. **Clone the repository**
   ```bash
   cd habitflow-poc/habitflow-android
   ```

2. **Configure Backend URL**
   - For emulator: Default is `http://10.0.2.2:3000` (localhost)
   - For physical device: Update `API_BASE_URL` in `app/build.gradle.kts`

3. **Sync Gradle**
   - Open project in Android Studio
   - Wait for Gradle sync to complete
   - Resolve any dependency issues

4. **Build & Run**
   ```bash
   # Build
   ./gradlew assembleDebug
   
   # Install on connected device
   ./gradlew installDebug
   ```

## Architecture

### Data Flow
```
UI (Composables) → ViewModel → Repository → [API Service | Local Database]
```

### Key Components

#### 1. **MainActivity**
Entry point that initializes the app and sets up the navigation

#### 2. **HabitFlowViewModel**
Manages UI state and business logic:
- Onboarding flow
- Persona selection
- Habit tracking
- AI motivation requests

#### 3. **Repository Layer**
Single source of truth for data:
- API calls with Retrofit
- Local caching with Room
- Error handling and fallbacks

#### 4. **UI Screens**
- `OnboardingScreen`: Persona selection questionnaire
- `MainScreen`: Main habit tracking interface
- `PersonaDetailScreen`: Detailed persona information
- `SplashScreen`: App launch screen

#### 5. **AI Components**
- `AIMotivationDialog`: Animated dialog for AI messages
- `StatisticsCard`: User progress visualization

## Configuration

### Build Configuration
Located in `app/build.gradle.kts`:
- `compileSdk`: 34
- `minSdk`: 24
- `targetSdk`: 34

### API Configuration
```kotlin
buildConfigField("String", "API_BASE_URL", "\"http://10.0.2.2:3000\"")
```

## Dependencies

### Core
- Kotlin 1.9.x
- Compose BOM 2023.10.01
- Material 3

### Networking
- Retrofit 2.9.0
- OkHttp 4.12.0
- Gson converter

### Database
- Room 2.6.1

### UI
- Compose UI
- Navigation Compose 2.7.5
- Lifecycle ViewModel Compose 2.6.2

## API Endpoints

The app communicates with these backend endpoints:

- `POST /api/persona/determine` - Determine user persona
- `GET /api/persona/all` - Get all personas
- `POST /api/motivation/generate` - Generate AI motivation
- `POST /api/habits/recommendations` - Get habit recommendations
- `GET /api/health` - Backend health check

## Database Schema

### Personas Table
- `id` (String, PK): Persona type
- `name`, `title`, `motivationMsg`, `completionMsg`
- `color`, `emoji`

### Habits Table
- `id` (Int, PK): Auto-generated
- `name` (String): Habit description
- `completed` (Boolean): Completion status
- `date` (String): ISO date

### User Settings Table
- `id` (Int, PK): Always 1
- `selectedPersonaId` (String): Current persona
- `onboardingCompleted` (Boolean)
- `lastUpdated` (Long): Timestamp

## Persona Types

### 1. The Supporter 💪
- **Color**: Warm Red (#FF6B6B)
- **Style**: Encouraging, positive, supportive
- **Best for**: Users who respond to positive reinforcement

### 2. The Challenger 🔥
- **Color**: Teal Blue (#4ECDC4)
- **Style**: Competitive, intense, direct
- **Best for**: Users motivated by challenges and competition

## Features in Detail

### Onboarding Flow
1. User sees welcome screen with app intro
2. Single question determines persona preference
3. Persona is saved locally and synced with backend
4. User proceeds to main habit tracking screen

### Habit Tracking
- Daily habits auto-generated on first launch
- Check/uncheck habits throughout the day
- Real-time progress calculation
- Animated feedback on completion

### AI Motivation
1. User taps "Get AI Motivation 🤖"
2. Loading animation shows processing
3. Backend generates persona-specific message
4. Beautiful dialog displays motivation
5. User can refresh for new message or dismiss

## Customization

### Adding New Personas
1. Update `PersonaType` enum in `Models.kt`
2. Add persona definition in backend `personaService.ts`
3. Update UI theme colors if needed

### Adding New Habits
Modify `initializeTodayHabits()` in `HabitFlowRepository.kt`:
```kotlin
val defaultHabits = listOf(
    HabitEntity(name = "Your habit here", date = today),
    // Add more habits...
)
```

## Troubleshooting

### Common Issues

**1. Cannot connect to backend**
- Ensure backend is running on port 3000
- For emulator, use `10.0.2.2` instead of `localhost`
- Check firewall settings

**2. Build errors**
- Clean and rebuild: `./gradlew clean build`
- Invalidate caches in Android Studio
- Check Gradle version compatibility

**3. Room database errors**
- Uninstall app to clear database
- Check entity schema matches
- Verify migration strategy if schema changed

**4. Compose rendering issues**
- Update Compose BOM to latest stable version
- Check for conflicting dependencies
- Clear compose cache

## Testing

### Manual Testing Checklist
- [ ] Onboarding flow completes
- [ ] Persona selection persists
- [ ] Habits can be checked/unchecked
- [ ] Progress updates correctly
- [ ] AI motivation generates
- [ ] App handles offline mode
- [ ] Persona reset works

### Test Backend Connection
```kotlin
// In your test code
suspend fun testBackend() {
    val response = apiService.healthCheck()
    assert(response.isSuccessful)
}
```

## Performance Optimization

- Lazy loading of habit lists
- Efficient recomposition with `remember` and `derivedStateOf`
- Database queries on IO dispatcher
- Image assets optimized
- Minimal overdraw with Material 3

## Future Enhancements

- [ ] Weekly/monthly statistics
- [ ] Habit streaks calculation
- [ ] Custom habit creation
- [ ] Habit categories
- [ ] Dark mode support
- [ ] Widgets
- [ ] Notifications/reminders
- [ ] Social sharing
- [ ] Achievement badges

## Contributing

When contributing to the Android app:
1. Follow Kotlin coding conventions
2. Use Compose best practices
3. Add comments for complex logic
4. Test on multiple screen sizes
5. Ensure backward compatibility (API 24+)

## License

MIT License - see main project LICENSE file

## Support

For issues specific to Android:
- Check logcat for errors
- Enable verbose logging in `ApiClient.kt`
- Review Room database inspector in Android Studio

---

Built with ❤️ using Kotlin & Jetpack Compose

