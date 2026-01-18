#!/usr/bin/env pwsh
# HabitFlow Android - Verification Script
# Checks if all components are ready for building

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "HabitFlow Android - Verification Check" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

$errors = 0
$warnings = 0

# Check if in correct directory
if (-not (Test-Path "app/build.gradle.kts")) {
    Write-Host "❌ ERROR: Not in habitflow-android directory!" -ForegroundColor Red
    Write-Host "   Please run this script from habitflow-android folder" -ForegroundColor Yellow
    exit 1
}

Write-Host "✅ Correct directory" -ForegroundColor Green

# Check required files
$requiredFiles = @(
    "app/src/main/java/com/habitflow/app/MainActivity.kt",
    "app/src/main/java/com/habitflow/app/HabitFlowApplication.kt",
    "app/src/main/java/com/habitflow/app/data/model/Models.kt",
    "app/src/main/java/com/habitflow/app/data/repository/HabitFlowRepository.kt",
    "app/src/main/java/com/habitflow/app/data/api/ApiClient.kt",
    "app/src/main/java/com/habitflow/app/data/api/HabitFlowApiService.kt",
    "app/src/main/java/com/habitflow/app/data/local/Database.kt",
    "app/src/main/java/com/habitflow/app/ui/viewmodel/HabitFlowViewModel.kt",
    "app/src/main/java/com/habitflow/app/ui/screens/MainScreen.kt",
    "app/src/main/java/com/habitflow/app/ui/screens/OnboardingScreen.kt",
    "app/src/main/java/com/habitflow/app/ui/screens/PersonaDetailScreen.kt",
    "app/src/main/java/com/habitflow/app/ui/screens/SplashScreen.kt",
    "app/src/main/java/com/habitflow/app/ui/components/AIComponents.kt",
    "app/src/main/java/com/habitflow/app/ui/components/HabitCreationComponents.kt",
    "app/src/main/java/com/habitflow/app/ui/theme/Color.kt",
    "app/src/main/java/com/habitflow/app/ui/theme/Theme.kt",
    "app/src/main/AndroidManifest.xml",
    "app/build.gradle.kts",
    "build.gradle.kts",
    "settings.gradle.kts"
)

Write-Host "`nChecking required files..." -ForegroundColor Cyan
foreach ($file in $requiredFiles) {
    if (Test-Path $file) {
        Write-Host "  ✅ $file" -ForegroundColor Green
    } else {
        Write-Host "  ❌ MISSING: $file" -ForegroundColor Red
        $errors++
    }
}

# Check documentation files
Write-Host "`nChecking documentation..." -ForegroundColor Cyan
$docFiles = @(
    "ANDROID_README.md",
    "QUICKSTART.md",
    "IMPLEMENTATION_SUMMARY.md"
)

foreach ($file in $docFiles) {
    if (Test-Path $file) {
        Write-Host "  ✅ $file" -ForegroundColor Green
    } else {
        Write-Host "  ⚠️  MISSING: $file" -ForegroundColor Yellow
        $warnings++
    }
}

# Check Gradle wrapper
Write-Host "`nChecking Gradle wrapper..." -ForegroundColor Cyan
if (Test-Path "gradlew.bat") {
    Write-Host "  ✅ gradlew.bat exists" -ForegroundColor Green
} else {
    Write-Host "  ❌ gradlew.bat missing" -ForegroundColor Red
    $errors++
}

# Check for common issues
Write-Host "`nChecking for common issues..." -ForegroundColor Cyan

# Check AndroidManifest.xml for internet permission
$manifest = Get-Content "app/src/main/AndroidManifest.xml" -Raw
if ($manifest -match "android.permission.INTERNET") {
    Write-Host "  ✅ Internet permission declared" -ForegroundColor Green
} else {
    Write-Host "  ❌ Missing INTERNET permission in manifest" -ForegroundColor Red
    $errors++
}

# Check if backend URL is configured
$buildGradle = Get-Content "app/build.gradle.kts" -Raw
if ($buildGradle -match "API_BASE_URL") {
    Write-Host "  ✅ API_BASE_URL configured" -ForegroundColor Green
} else {
    Write-Host "  ⚠️  API_BASE_URL not found in build.gradle.kts" -ForegroundColor Yellow
    $warnings++
}

# Check Java/Kotlin version
Write-Host "`nChecking build configuration..." -ForegroundColor Cyan
if ($buildGradle -match "JavaVersion.VERSION_17") {
    Write-Host "  ✅ Java 17 configured" -ForegroundColor Green
} else {
    Write-Host "  ⚠️  Java version configuration not found" -ForegroundColor Yellow
    $warnings++
}

# Summary
Write-Host "`n========================================" -ForegroundColor Cyan
Write-Host "Verification Summary" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan

if ($errors -eq 0 -and $warnings -eq 0) {
    Write-Host "✅ ALL CHECKS PASSED!" -ForegroundColor Green
    Write-Host "`nYour Android app is ready to build!" -ForegroundColor Green
    Write-Host "`nNext steps:" -ForegroundColor Cyan
    Write-Host "  1. Ensure Android Studio is open" -ForegroundColor White
    Write-Host "  2. Sync Gradle files" -ForegroundColor White
    Write-Host "  3. Run: gradlew.bat assembleDebug" -ForegroundColor White
    Write-Host "  4. Install: gradlew.bat installDebug" -ForegroundColor White
    Write-Host "`nOr use the build script:" -ForegroundColor Cyan
    Write-Host "  build-android.bat run" -ForegroundColor White
} else {
    if ($errors -gt 0) {
        Write-Host "❌ Found $errors ERROR(S)" -ForegroundColor Red
    }
    if ($warnings -gt 0) {
        Write-Host "⚠️  Found $warnings WARNING(S)" -ForegroundColor Yellow
    }

    if ($errors -gt 0) {
        Write-Host "`n⚠️  Please fix errors before building" -ForegroundColor Red
        exit 1
    } else {
        Write-Host "`n✅ Warnings can be ignored, app should build" -ForegroundColor Green
    }
}

Write-Host "`n========================================" -ForegroundColor Cyan
Write-Host ""

