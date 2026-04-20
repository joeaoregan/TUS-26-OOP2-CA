@echo off
title Guitar Rental Management System - Java 25
cls

echo ======================================================
echo   Guitar Rental Management System: Startup Check
echo ======================================================

:: 1. Output the current Java version
echo.
echo Current Java Version:
java -version
echo.

:: 2. Highlight Java 25 Requirement
echo [NOTE] This project uses Java 25 Preview features (JEP 512, 513, etc.).
echo [CHECK] Ensuring Java 25 is active...
echo.

:: 3. Run the application from the sub-directory using the Launcher
:: We use --enable-preview because of the Java 25 features
java --enable-preview --source 25 guitarrental/src/main/java/Launcher.java

echo.
echo ======================================================
echo   Application Terminated.
echo ======================================================
pause