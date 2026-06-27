@echo off
echo ========================================
echo   Java POS System - Build Script
echo ========================================

where javac >nul 2>nul
if errorlevel 1 (
    echo [ERROR] Java JDK not found. Please install Java 17+ and add it to PATH.
    pause
    exit /b 1
)

java -version
echo.

if not exist out mkdir out

echo [INFO] Compiling...
dir /s /b src\*.java > sources.txt
javac -d out @sources.txt

if errorlevel 1 (
    echo [ERROR] Compilation failed.
    del sources.txt
    pause
    exit /b 1
)

del sources.txt
echo [OK] Compilation successful.

echo [INFO] Packaging JAR...
echo Main-Class: pos.Main > manifest.txt
jar cfm JavaPOS.jar manifest.txt -C out .
del manifest.txt

echo [OK] JavaPOS.jar created.
echo.
echo   To run: java -jar JavaPOS.jar
echo.

set /p run="Run the application now? (y/n): "
if /i "%run%"=="y" java -jar JavaPOS.jar

pause
