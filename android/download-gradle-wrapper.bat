@echo off
REM Script to download Gradle wrapper JAR

echo Downloading Gradle wrapper JAR...

powershell -Command "& {Invoke-WebRequest -Uri 'https://raw.githubusercontent.com/gradle/gradle/master/gradle/wrapper/gradle-wrapper.jar' -OutFile 'gradle\wrapper\gradle-wrapper.jar'}"

if exist "gradle\wrapper\gradle-wrapper.jar" (
    echo.
    echo SUCCESS! Gradle wrapper downloaded.
    echo.
    echo Now you can:
    echo 1. Close this window
    echo 2. Re-open Android Studio
    echo 3. File ^> Open ^> Select habitflow-android folder
    echo 4. Wait for Gradle sync
    echo.
) else (
    echo.
    echo ERROR: Could not download gradle-wrapper.jar
    echo Please check your internet connection.
    echo.
    echo Alternative: Let Android Studio download it automatically
    echo when you open the project.
    echo.
)

pause

