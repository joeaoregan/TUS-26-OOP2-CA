@echo off
title Guitar Rental Management System - Java 25
:: Set a standard size that works in legacy CMD, but don't force a relaunch
mode con: cols=100 lines=50

cls

echo ======================================================
echo   Guitar Rental Management System: JAR Execution
echo ======================================================

:: 1. Check if the JAR exists before trying to run it
if not exist "guitarrental\target\oop2-guitarrental-0.0.1-SNAPSHOT.jar" (
    echo [ERROR] JAR file not found in guitarrental\target\
    echo Please run 'mvn clean package' inside the guitarrental folder first.
    echo.
    pause
    exit /b
)

:: 2. Output the current Java version
echo Current Java Version:
java -version
echo.

:: 3. Run the JAR with Preview Features enabled
echo [LAUNCH] Starting application from JAR...
java --enable-preview -jar "guitarrental\target\oop2-guitarrental-0.0.1-SNAPSHOT.jar"

echo.
echo ======================================================
echo   Application Terminated.
echo ======================================================
pause