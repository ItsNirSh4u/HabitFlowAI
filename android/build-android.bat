@echo off
REM HabitFlow Android - Build and Test Script
REM This script builds the Android app and provides various commands

echo ========================================
echo HabitFlow Android - Build Script
echo ========================================
echo.

if "%1"=="" (
    echo Usage: build-android.bat [command]
    echo.
    echo Available commands:
    echo   clean       - Clean build artifacts
    echo   build       - Build debug APK
    echo   release     - Build release APK
    echo   install     - Install debug APK on connected device
    echo   run         - Build and run on connected device
    echo   test        - Run unit tests
    echo   lint        - Run lint checks
    echo   dependencies - Show dependency tree
    echo   check       - Run all checks
    echo.
    exit /b 1
)

cd "%~dp0"

if "%1"=="clean" (
    echo Cleaning project...
    gradlew.bat clean
    echo Clean complete!
)

if "%1"=="build" (
    echo Building debug APK...
    gradlew.bat assembleDebug
    echo Build complete! APK location:
    echo app\build\outputs\apk\debug\app-debug.apk
)

if "%1"=="release" (
    echo Building release APK...
    gradlew.bat assembleRelease
    echo Release build complete!
)

if "%1"=="install" (
    echo Installing on device...
    gradlew.bat installDebug
    echo Installation complete!
)

if "%1"=="run" (
    echo Building and launching app...
    gradlew.bat installDebug
    adb shell am start -n com.habitflow.app/.MainActivity
    echo App launched!
)

if "%1"=="test" (
    echo Running tests...
    gradlew.bat test
    echo Tests complete!
)

if "%1"=="lint" (
    echo Running lint checks...
    gradlew.bat lint
    echo Lint check complete!
)

if "%1"=="dependencies" (
    echo Showing dependencies...
    gradlew.bat app:dependencies
)

if "%1"=="check" (
    echo Running all checks...
    gradlew.bat check
    echo All checks complete!
)

echo.
echo ========================================
echo Done!
echo ========================================

