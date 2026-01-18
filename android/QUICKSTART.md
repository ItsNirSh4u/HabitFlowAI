# HabitFlow Android - Quick Start Guide

## 🚀 Quick Setup (5 minutes)

### Prerequisites Check
- [ ] Android Studio installed (Hedgehog 2023.1.1+)
- [ ] JDK 17+ installed
- [ ] Android device or emulator ready
- [ ] Backend server running (port 3000)

### Step-by-Step Setup

#### 1. Open Project
```bash
# Navigate to Android project
cd habitflow-poc/habitflow-android

# Open in Android Studio
# File > Open > Select habitflow-android folder
```

#### 2. Sync Dependencies
- Wait for Gradle sync to complete automatically
- If issues occur: File > Sync Project with Gradle Files

#### 3. Configure Backend
The default configuration works for Android Emulator:
- **Emulator**: `http://10.0.2.2:3000` ✅ (already configured)
- **Physical Device**: Update in `app/build.gradle.kts`:
  ```kotlin
  buildConfigField("String", "API_BASE_URL", "\"http://YOUR_IP:3000\"")
  ```

#### 4. Start Backend Server
```bash
# In a separate terminal
cd habitflow-poc/habitflow-backend
npm install
npm start
```

#### 5. Build & Run
**Option A: Android Studio**
- Click the green ▶️ Run button
- Select your device/emulator
- Wait for installation

**Option B: Command Line**
```bash
# Build
gradlew.bat assembleDebug

# Install
gradlew.bat installDebug

# Launch
adb shell am start -n com.habitflow.app/.MainActivity
```

**Option C: Build Script**
```bash
# Windows
build-android.bat run

# This will build, install, and launch the app
```

## 🎯 First Launch Flow

### 1. Splash Screen
- Animated HabitFlow logo appears
- Backend health check runs
- Database initializes

### 2. Onboarding
- Welcome message displays
- Single question about motivation style:
  - **Option A**: "Encouraging words" → The Supporter 💪
  - **Option B**: "Competitive challenge" → The Challenger 🔥

### 3. Main Screen
After selecting persona, you'll see:
- **Persona Header**: Shows your chosen persona with emoji
- **Statistics Card**: Daily progress (0/3 initially)
- **Motivation Card**: Persona-specific message
- **AI Button**: "Get AI Motivation 🤖"
- **Habit List**: Three default habits to check off
- **Progress Bar**: Visual progress indicator

### 4. Try AI Motivation
1. Tap "Get AI Motivation 🤖" button
2. Beautiful dialog appears with loading animation
3. AI generates personalized motivation message
4. Can refresh for new messages or dismiss

## 📱 Features to Test

### Basic Features
- [ ] Complete onboarding
- [ ] Check/uncheck habits
- [ ] Watch progress bar update
- [ ] See completion feedback message
- [ ] Request AI motivation
- [ ] Reset persona

### AI Features
- [ ] AI loading animation
- [ ] Different messages for each persona
- [ ] Refresh AI motivation
- [ ] Context-aware messages

### Data Persistence
- [ ] Close and reopen app (state persists)
- [ ] Complete habits (remains saved)
- [ ] Selected persona persists

## 🎨 The Two Personas

### The Supporter 💪
**Color**: Warm Red (#FF6B6B)

**Style**: 
- Encouraging and positive
- Celebrates small wins
- Supportive coaching

**Example Messages**:
- "You're doing amazing! Keep that positive energy flowing!"
- "I'm so proud of your dedication!"
- "Every small win is a step toward greatness!"

**Best For**:
- People who respond to positive reinforcement
- Those who need encouragement
- Users who prefer gentle motivation

---

### The Challenger 🔥
**Color**: Teal Blue (#4ECDC4)

**Style**:
- Competitive and intense
- Direct and action-oriented
- Pushes you to excel

**Example Messages**:
- "Time to dominate! Show those habits who's boss!"
- "You didn't come this far to only come this far!"
- "Winners find ways, losers find excuses. You're a winner!"

**Best For**:
- Competitive individuals
- Those motivated by challenges
- Users who prefer direct communication

## 🔧 Troubleshooting

### "Cannot connect to backend"
**Problem**: App shows error or AI doesn't work

**Solutions**:
1. Verify backend is running: `http://localhost:3000/api/health`
2. Check backend terminal for errors
3. For emulator: Ensure using `10.0.2.2` not `localhost`
4. For device: Use your computer's IP address

**Test Backend**:
```bash
# From your computer
curl http://localhost:3000/api/health

# From emulator (via adb)
adb shell
curl http://10.0.2.2:3000/api/health
```

### "App crashes on launch"
**Solutions**:
1. Clean and rebuild:
   ```bash
   gradlew.bat clean
   gradlew.bat assembleDebug
   ```
2. Uninstall app from device
3. Check Android Studio Logcat for errors
4. Verify minimum SDK 24+

### "Habits don't save"
**Solutions**:
1. Check Room database initialization
2. Uninstall and reinstall app (clears corrupt DB)
3. Check Logcat for database errors

### "AI motivation not appearing"
**Solutions**:
1. Ensure backend is running
2. Check network permissions in manifest (already configured)
3. Try offline mode (uses mock responses)
4. Check API response in Logcat

### "Build errors"
**Solutions**:
1. Sync Gradle: File > Sync Project with Gradle Files
2. Invalidate caches: File > Invalidate Caches > Invalidate and Restart
3. Update Gradle wrapper if needed
4. Check JDK version (need 17+)

## 📊 Viewing Data

### Database Inspector (Android Studio)
1. Run app in debug mode
2. View > Tool Windows > App Inspection
3. Click "Database Inspector" tab
4. Explore tables: personas, habits, user_settings

### Logcat
1. View > Tool Windows > Logcat
2. Filter by package: `com.habitflow.app`
3. Look for tags:
   - `HabitFlowViewModel`
   - `HabitFlowRepository`
   - `ApiClient`

### Network Inspection
1. Use OkHttp logging (already configured)
2. Check Logcat for API calls
3. Look for `OkHttp` tag
4. Shows request/response details

## 🎯 Demo Script

Perfect for showing the app to others:

### 1. Fresh Start
```bash
# Uninstall app
adb uninstall com.habitflow.app

# Reinstall
gradlew.bat installDebug

# Launch
adb shell am start -n com.habitflow.app/.MainActivity
```

### 2. Walkthrough
1. **Splash**: "Watch the animated logo"
2. **Onboarding**: "Let's choose The Challenger for intense motivation"
3. **Main Screen**: "Here are 3 daily habits to track"
4. **Check Habit**: "Tap to complete 'Drink water'"
5. **Watch Progress**: "See the progress bar fill up"
6. **AI Button**: "Now let's get AI motivation..."
7. **AI Dialog**: "Beautiful animation while it generates"
8. **AI Message**: "Personalized message based on our persona!"
9. **Complete All**: "Complete all habits for special feedback"

### 3. Compare Personas
1. Tap "Choose Another Persona"
2. Select The Supporter
3. Request AI motivation
4. Compare the different tone and style

## 🔬 Advanced Testing

### Test Different Network Scenarios
```bash
# Simulate slow network
adb shell settings put global airplane_mode_on 1
# Wait a bit
adb shell settings put global airplane_mode_on 0

# The app should handle gracefully with fallback messages
```

### Test Data Persistence
```bash
# 1. Complete some habits
# 2. Force close app
adb shell am force-stop com.habitflow.app

# 3. Relaunch
adb shell am start -n com.habitflow.app/.MainActivity

# Habits should still be marked complete
```

### Stress Test
- Check/uncheck habits rapidly
- Request AI motivation multiple times
- Switch between personas multiple times
- All should work smoothly

## 📈 Performance Tips

### Faster Builds
1. Enable Gradle daemon
2. Increase heap size in `gradle.properties`:
   ```properties
   org.gradle.jvmargs=-Xmx4096m
   ```
3. Enable parallel execution:
   ```properties
   org.gradle.parallel=true
   ```

### Faster Emulator
1. Use x86/x64 images (not ARM)
2. Enable hardware acceleration (HAXM/KVM)
3. Allocate more RAM to emulator

## 🎓 Learning Resources

### Code Structure
- `MainActivity.kt` → App entry point
- `HabitFlowViewModel.kt` → Business logic
- `OnboardingScreen.kt` → Persona selection
- `MainScreen.kt` → Main tracking UI
- `AIComponents.kt` → AI motivation dialogs

### Key Concepts Demonstrated
- Jetpack Compose UI
- Material 3 Design
- MVVM Architecture
- Kotlin Coroutines
- Room Database
- Retrofit Networking
- StateFlow & SharedFlow
- Composable animations

## 🚢 Building Release APK

### For Distribution
```bash
# 1. Build release APK
gradlew.bat assembleRelease

# 2. Sign the APK (you'll need a keystore)
# Follow Android docs for signing

# 3. APK location
# app/build/outputs/apk/release/app-release.apk
```

### App Bundle (For Play Store)
```bash
gradlew.bat bundleRelease
```

## 📱 Device Requirements

### Minimum Requirements
- Android 7.0 (API 24) or higher
- 50 MB storage
- Internet connection (for AI features)

### Recommended
- Android 12+ (Material You support)
- 100 MB storage
- 2 GB RAM

### Tested On
- ✅ Pixel 5 Emulator (API 31)
- ✅ Generic Phone (API 24-34)
- ✅ Physical devices (Android 10+)

## 🎉 Success Indicators

You'll know everything is working when:
- ✅ App launches without crashes
- ✅ Onboarding completes smoothly
- ✅ Habits can be checked/unchecked
- ✅ Progress updates in real-time
- ✅ AI motivation generates messages
- ✅ Persona colors theme the UI
- ✅ Data persists after app restart
- ✅ Animations are smooth

## 📞 Getting Help

If stuck:
1. Check Logcat for errors
2. Review this Quick Start guide
3. Check ANDROID_README.md for details
4. Verify backend is running
5. Try clean rebuild

## 🎯 Next Steps

Once basic app works:
1. Try both personas
2. Explore AI motivation variations
3. Test offline mode
4. Review the code structure
5. Customize habits
6. Experiment with themes

---

**Ready to start?** → Run: `gradlew.bat installDebug`

Built with ❤️ using Kotlin & Jetpack Compose

