# Doctor's Appointment Booking System (MediBook)

A Java-based console application for managing doctor's appointment slots and patient bookings with email confirmation functionality.

## Features

- 👨‍⚕️ **Add Doctor's Appointment Slots**: Create time slots for different doctors
- 📅 **View Available Slots**: Display all open appointment slots
- 📝 **Book Appointments**: Allow patients to book available slots
- 📧 **Email Confirmation**: Send professional HTML email confirmations
- 📋 **View Booked Appointments**: Manage all confirmed appointments
- ❌ **Cancel Appointments**: Cancel existing bookings
- 🔍 **Search Functionality**: Search by doctor name or date
- ✅ **Input Validation**: Comprehensive error handling and validation

## System Requirements

- Java 17 or higher
- Maven 3.6 or higher
- Internet connection (for email functionality)
- Gmail account (for SMTP email sending)

## Project Structure

```
appointment-booking-system/
├── src/main/java/
│   ├── Appointment.java           # Appointment data model
│   ├── EmailService.java          # Email handling service
│   └── AppointmentBookingSystem.java # Main application class
├── pom.xml                        # Maven configuration
└── README.md                      # This file
```

## Quick Start

### 1. Clone or Download the Project

```bash
git clone <repository-url>
cd appointment-booking-system
```

### 2. Compile the Application

**Option A: Using Maven (Recommended)**
```bash
mvn clean compile
```

**Option B: Manual Compilation**
```bash
# Compile Java files
javac -cp ".:lib/*" *.java

# Note: You'll need to download JavaMail dependencies manually
```

### 3. Run the Application

**Option A: Using Run Scripts (Recommended)**
```bash
# For Linux/Mac
./run.sh

# For Windows
run.bat
```

**Option B: Direct Java Execution**
```bash
# Compile first
javac Appointment.java EmailServiceSimple.java AppointmentBookingSystem.java

# Then run
java AppointmentBookingSystem
```

**Option C: Using Maven (if pom.xml is configured)**
```bash
mvn exec:java
```

## Email Configuration

### Demo Version (Current)
The current version uses `EmailServiceSimple.java` which **simulates email sending** through console output. This works immediately without any external dependencies or configuration.

### Production Version (Optional)
For real email sending, use `EmailService.java` with Gmail SMTP:

1. **Update AppointmentBookingSystem.java** to use EmailService instead of EmailServiceSimple:
   ```java
   this.emailService = new EmailService("your-email@gmail.com", "your-app-password");
   ```

2. **Add JavaMail Dependencies** to your classpath

3. **Configure Gmail**:
   - Enable 2-Factor Authentication on your Gmail account
   - Generate an App Password (Google Account → Security → App passwords)
   - Use the app password instead of your regular password

4. **The demo version is perfect for testing** and shows exactly what the email would contain.

## Usage Guide

### Starting the Application

When you run the application, you'll see the MediBook welcome screen with a menu:

```
╔════════════════════════════════════════════════════════════╗
║              DOCTOR'S APPOINTMENT BOOKING SYSTEM           ║
║                     Welcome to MediBook                    ║
╚════════════════════════════════════════════════════════════╝

                        MAIN MENU
============================================================
1. 👨‍⚕️  Add Doctor's Appointment Slots
2. 📅  View Available Appointment Slots
3. 📝  Book an Appointment
4. 📋  View Booked Appointments
5. ❌  Cancel an Appointment
6. 🔍  Search Appointments
7. 🚪  Exit
============================================================
```

### 1. Adding Appointment Slots

- Select option 1
- Enter doctor's name
- Add time slots in format: `yyyy-MM-dd HH:mm`
- Example: `2024-01-15 09:00`
- Type 'done' when finished

### 2. Booking an Appointment

- Select option 3
- Choose from available slots by ID
- Enter patient name and email
- Confirm the booking
- Receive email confirmation

### 3. Managing Appointments

- **View Available**: See all open time slots
- **View Booked**: See all confirmed appointments
- **Cancel**: Remove existing bookings
- **Search**: Find appointments by doctor or date

## Sample Workflow

1. **Admin adds slots**:
   ```
   Doctor: Smith
   Slots: 2024-01-15 09:00, 2024-01-15 14:00
   ```

2. **Patient books appointment**:
   ```
   Select ID: 1
   Name: John Doe
   Email: john@example.com
   ```

3. **System sends confirmation**:
   ```
   ✅ Appointment booked successfully!
   📧 Sending confirmation email...
   🎉 Booking completed! Confirmation email sent to john@example.com
   ```

## Email Template

The system sends professional HTML emails with:
- Appointment details (ID, doctor, date/time, patient)
- Important notes and instructions
- Professional formatting with CSS styling
- Responsive design

## Development

### Building from Source

```bash
# Clone the repository
git clone <repository-url>
cd appointment-booking-system

# Compile and package
mvn clean package

# Run tests (if any)
mvn test

# Create executable JAR
mvn clean package
java -jar target/appointment-booking-system-1.0.0.jar
```

### Dependencies

The project uses:
- **JavaMail API** (1.6.2): Email functionality
- **Activation API** (1.1.1): Required for JavaMail
- **JUnit** (5.9.2): Testing framework (optional)

### Customization

**Add New Features**:
- Modify `AppointmentBookingSystem.java` for new menu options
- Extend `Appointment.java` for additional fields
- Update `EmailService.java` for different email templates

**Database Integration**:
- Replace `List<Appointment>` with database connectivity
- Add JPA/Hibernate dependencies
- Implement repository pattern

## Troubleshooting

### Common Issues

1. **Compilation Errors**:
   ```bash
   # Ensure Java 17+ is installed
   java -version
   
   # Update JAVA_HOME if needed
   export JAVA_HOME=/path/to/java17
   ```

2. **Email Not Sending**:
   - Check internet connection
   - Verify Gmail credentials
   - Enable "Less secure app access" or use App Password
   - Check firewall/antivirus settings

3. **Maven Issues**:
   ```bash
   # Clear Maven cache
   mvn clean install -U
   
   # Skip tests if needed
   mvn clean package -DskipTests
   ```

4. **Date Format Errors**:
   - Use exact format: `yyyy-MM-dd HH:mm`
   - Example: `2024-01-15 09:00` (not `01/15/2024 9:00 AM`)

### Input Validation

The system validates:
- ✅ Date format correctness
- ✅ Email format validation
- ✅ Duplicate slot prevention
- ✅ Past date prevention
- ✅ Empty field validation

## License

This project is created for educational purposes. Feel free to modify and distribute.

## Support

For issues or questions:
1. Check this README for common solutions
2. Review error messages carefully
3. Ensure all requirements are met
4. Verify input formats

## Future Enhancements

Potential improvements:
- 📱 Web interface using Spring Boot
- 🗄️ Database persistence (MySQL/PostgreSQL)
- 🔐 User authentication and roles
- 📱 SMS notifications
- 📊 Reporting and analytics
- 🌐 REST API for mobile apps
- 📅 Calendar integration
- 💳 Payment processing

---

**Made with ❤️ for efficient healthcare management**