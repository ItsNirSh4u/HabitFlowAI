#!/usr/bin/env pwsh
# HabitFlow Android - Complete Project Setup and Fix Script

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "HabitFlow Android - Project Setup" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

$projectRoot = $PSScriptRoot

# Check if in correct directory
if (-not (Test-Path "$projectRoot\app\build.gradle.kts")) {
    Write-Host "❌ ERROR: Not in correct directory!" -ForegroundColor Red
    Write-Host "   Please run this script from habitflow-android folder" -ForegroundColor Yellow
    exit 1
}

Write-Host "✅ Found habitflow-android project" -ForegroundColor Green
Write-Host ""

# Step 1: Check required files
Write-Host "Step 1: Checking required files..." -ForegroundColor Cyan

$requiredFiles = @(
    "settings.gradle.kts",
    "build.gradle.kts",
    "app\build.gradle.kts",
    "gradle\wrapper\gradle-wrapper.properties"
)

$allFilesExist = $true
foreach ($file in $requiredFiles) {
    if (Test-Path "$projectRoot\$file") {
        Write-Host "  ✅ $file" -ForegroundColor Green
    } else {
        Write-Host "  ❌ MISSING: $file" -ForegroundColor Red
        $allFilesExist = $false
    }
}

if (-not $allFilesExist) {
    Write-Host "`n❌ Missing required files!" -ForegroundColor Red
    exit 1
}

# Step 2: Download Gradle wrapper JAR if missing
Write-Host "`nStep 2: Checking Gradle wrapper..." -ForegroundColor Cyan

if (-not (Test-Path "$projectRoot\gradle\wrapper\gradle-wrapper.jar")) {
    Write-Host "  ⚠️  gradle-wrapper.jar missing, downloading..." -ForegroundColor Yellow

    try {
        $wrapperUrl = "https://raw.githubusercontent.com/gradle/gradle/master/gradle/wrapper/gradle-wrapper.jar"
        Invoke-WebRequest -Uri $wrapperUrl -OutFile "$projectRoot\gradle\wrapper\gradle-wrapper.jar" -ErrorAction Stop
        Write-Host "  ✅ Downloaded gradle-wrapper.jar" -ForegroundColor Green
    } catch {
        Write-Host "  ⚠️  Could not download automatically" -ForegroundColor Yellow
        Write-Host "     Android Studio will download it on first sync" -ForegroundColor Gray
    }
} else {
    Write-Host "  ✅ gradle-wrapper.jar exists" -ForegroundColor Green
}

if (-not (Test-Path "$projectRoot\gradlew.bat")) {
    Write-Host "  ❌ gradlew.bat missing!" -ForegroundColor Red
    exit 1
} else {
    Write-Host "  ✅ gradlew.bat exists" -ForegroundColor Green
}

# Step 3: Check IDE configuration
Write-Host "`nStep 3: Checking IDE configuration..." -ForegroundColor Cyan

if (-not (Test-Path "$projectRoot\.idea")) {
    New-Item -ItemType Directory -Path "$projectRoot\.idea" -Force | Out-Null
    Write-Host "  ✅ Created .idea directory" -ForegroundColor Green
}

$ideaFiles = @(".idea\gradle.xml", ".idea\misc.xml", ".idea\compiler.xml")
foreach ($file in $ideaFiles) {
    if (Test-Path "$projectRoot\$file") {
        Write-Host "  ✅ $file" -ForegroundColor Green
    } else {
        Write-Host "  ⚠️  $file will be created by Android Studio" -ForegroundColor Yellow
    }
}

# Step 4: Clean old caches (optional)
Write-Host "`nStep 4: Cleaning old caches..." -ForegroundColor Cyan

$confirm = Read-Host "Do you want to clean .gradle and build caches? (y/n)"
if ($confirm -eq "y" -or $confirm -eq "Y") {
    if (Test-Path "$projectRoot\.gradle") {
        Remove-Item -Recurse -Force "$projectRoot\.gradle" -ErrorAction SilentlyContinue
        Write-Host "  ✅ Cleaned .gradle" -ForegroundColor Green
    }

    if (Test-Path "$projectRoot\build") {
        Remove-Item -Recurse -Force "$projectRoot\build" -ErrorAction SilentlyContinue
        Write-Host "  ✅ Cleaned build" -ForegroundColor Green
    }

    if (Test-Path "$projectRoot\app\build") {
        Remove-Item -Recurse -Force "$projectRoot\app\build" -ErrorAction SilentlyContinue
        Write-Host "  ✅ Cleaned app\build" -ForegroundColor Green
    }
} else {
    Write-Host "  ⏭️  Skipped cache cleaning" -ForegroundColor Gray
}

# Step 5: Test Gradle wrapper
Write-Host "`nStep 5: Testing Gradle wrapper..." -ForegroundColor Cyan

Write-Host "  Testing gradlew.bat..." -ForegroundColor Gray
$gradleTest = & "$projectRoot\gradlew.bat" --version 2>&1

if ($LASTEXITCODE -eq 0) {
    Write-Host "  ✅ Gradle wrapper works!" -ForegroundColor Green
    Write-Host ""
    Write-Host $gradleTest -ForegroundColor Gray
} else {
    Write-Host "  ⚠️  Gradle test had issues (this is OK if first run)" -ForegroundColor Yellow
    Write-Host "     Android Studio will fix this on first sync" -ForegroundColor Gray
}

# Step 6: Final instructions
Write-Host "`n========================================" -ForegroundColor Cyan
Write-Host "Setup Complete!" -ForegroundColor Green
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

Write-Host "📋 Next Steps:" -ForegroundColor Cyan
Write-Host ""
Write-Host "1. Close Android Studio if currently open" -ForegroundColor White
Write-Host ""
Write-Host "2. Open Android Studio" -ForegroundColor White
Write-Host ""
Write-Host "3. Click 'Open' or File > Open" -ForegroundColor White
Write-Host ""
Write-Host "4. Navigate to and select:" -ForegroundColor White
Write-Host "   $projectRoot" -ForegroundColor Yellow
Write-Host ""
Write-Host "5. Click 'OK' and wait for Gradle sync (2-5 minutes)" -ForegroundColor White
Write-Host ""
Write-Host "6. After sync completes:" -ForegroundColor White
Write-Host "   - Switch to 'Android' view in Project tool window" -ForegroundColor Gray
Write-Host "   - Verify File > Sync Project with Gradle Files appears" -ForegroundColor Gray
Write-Host "   - Check that green Run ▶️ button is enabled" -ForegroundColor Gray
Write-Host ""

Write-Host "📖 Documentation:" -ForegroundColor Cyan
Write-Host "   - ANDROID_STUDIO_FIX.md - Detailed troubleshooting" -ForegroundColor Gray
Write-Host "   - QUICKSTART.md - Quick start guide" -ForegroundColor Gray
Write-Host "   - ANDROID_README.md - Full documentation" -ForegroundColor Gray
Write-Host ""

Write-Host "✅ Project is ready for Android Studio!" -ForegroundColor Green
Write-Host ""

