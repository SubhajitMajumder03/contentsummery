#!/bin/bash

# Doctor's Appointment Booking System - Run Script
echo "============================================"
echo "  Doctor's Appointment Booking System"
echo "            Starting MediBook..."
echo "============================================"
echo

# Compile Java files
echo "📦 Compiling Java files..."
javac Appointment.java EmailServiceSimple.java AppointmentBookingSystem.java

if [ $? -eq 0 ]; then
    echo "✅ Compilation successful!"
    echo
    echo "🚀 Starting the application..."
    echo
    # Run the application
    java AppointmentBookingSystem
else
    echo "❌ Compilation failed!"
    exit 1
fi