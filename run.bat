@echo off
REM Doctor's Appointment Booking System - Run Script for Windows

echo ============================================
echo   Doctor's Appointment Booking System
echo             Starting MediBook...
echo ============================================
echo.

REM Compile Java files
echo 📦 Compiling Java files...
javac Appointment.java EmailServiceSimple.java AppointmentBookingSystem.java

if %errorlevel% equ 0 (
    echo ✅ Compilation successful!
    echo.
    echo 🚀 Starting the application...
    echo.
    REM Run the application
    java AppointmentBookingSystem
) else (
    echo ❌ Compilation failed!
    pause
    exit /b 1
)

pause