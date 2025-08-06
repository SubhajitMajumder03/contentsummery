import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

/**
 * Main class for the Doctor's Appointment Booking System
 * Handles user interaction, appointment management, and booking process
 */
public class AppointmentBookingSystem {
    private List<Appointment> appointments;
    private Scanner scanner;
    private EmailServiceSimple emailService;
    private int nextAppointmentId;
    private static final DateTimeFormatter INPUT_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    
    public AppointmentBookingSystem() {
        this.appointments = new ArrayList<>();
        this.scanner = new Scanner(System.in);
        this.nextAppointmentId = 1;
        
        // Initialize email service with demo credentials
        // In production, these should be loaded from environment variables or config file
        this.emailService = new EmailServiceSimple("demo@hospital.com", "demopassword");
    }
    
    /**
     * Main entry point of the application
     */
    public static void main(String[] args) {
        AppointmentBookingSystem system = new AppointmentBookingSystem();
        system.displayWelcome();
        system.runMainMenu();
    }
    
    /**
     * Display welcome message
     */
    private void displayWelcome() {
        System.out.println("╔════════════════════════════════════════════════════════════╗");
        System.out.println("║              DOCTOR'S APPOINTMENT BOOKING SYSTEM           ║");
        System.out.println("║                     Welcome to MediBook                    ║");
        System.out.println("╚════════════════════════════════════════════════════════════╝");
        System.out.println();
    }
    
    /**
     * Main menu loop
     */
    private void runMainMenu() {
        while (true) {
            displayMainMenu();
            int choice = getIntInput("Enter your choice: ");
            
            switch (choice) {
                case 1:
                    addAppointmentSlots();
                    break;
                case 2:
                    displayAvailableSlots();
                    break;
                case 3:
                    bookAppointment();
                    break;
                case 4:
                    viewBookedAppointments();
                    break;
                case 5:
                    cancelAppointment();
                    break;
                case 6:
                    searchAppointments();
                    break;
                case 7:
                    System.out.println("Thank you for using MediBook! Goodbye!");
                    return;
                default:
                    System.out.println("❌ Invalid choice! Please select a number between 1-7.");
            }
            
            System.out.println("\nPress Enter to continue...");
            scanner.nextLine();
        }
    }
    
    /**
     * Display main menu options
     */
    private void displayMainMenu() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("                        MAIN MENU");
        System.out.println("=".repeat(60));
        System.out.println("1. 👨‍⚕️  Add Doctor's Appointment Slots");
        System.out.println("2. 📅  View Available Appointment Slots");
        System.out.println("3. 📝  Book an Appointment");
        System.out.println("4. 📋  View Booked Appointments");
        System.out.println("5. ❌  Cancel an Appointment");
        System.out.println("6. 🔍  Search Appointments");
        System.out.println("7. 🚪  Exit");
        System.out.println("=".repeat(60));
    }
    
    /**
     * Add new appointment slots for doctors
     */
    private void addAppointmentSlots() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("           ADD APPOINTMENT SLOTS");
        System.out.println("=".repeat(50));
        
        System.out.print("Enter doctor's name: ");
        String doctorName = scanner.nextLine().trim();
        
        if (doctorName.isEmpty()) {
            System.out.println("❌ Doctor name cannot be empty!");
            return;
        }
        
        System.out.println("\nEnter appointment slots (format: yyyy-MM-dd HH:mm)");
        System.out.println("Example: 2024-01-15 09:00");
        System.out.println("Enter 'done' when finished adding slots");
        
        int slotsAdded = 0;
        while (true) {
            System.out.print("Enter appointment date and time: ");
            String input = scanner.nextLine().trim();
            
            if (input.equalsIgnoreCase("done")) {
                break;
            }
            
            try {
                LocalDateTime dateTime = LocalDateTime.parse(input, INPUT_FORMATTER);
                
                // Check if the slot already exists
                boolean exists = appointments.stream()
                    .anyMatch(apt -> apt.getDoctorName().equalsIgnoreCase(doctorName) 
                             && apt.getDateTime().equals(dateTime));
                
                if (exists) {
                    System.out.println("⚠️  Slot already exists for Dr. " + doctorName + " at " + input);
                    continue;
                }
                
                // Check if the date/time is in the past
                if (dateTime.isBefore(LocalDateTime.now())) {
                    System.out.println("⚠️  Cannot create appointment in the past!");
                    continue;
                }
                
                Appointment appointment = new Appointment(nextAppointmentId++, doctorName, dateTime);
                appointments.add(appointment);
                slotsAdded++;
                System.out.println("✅ Added slot: " + appointment);
                
            } catch (DateTimeParseException e) {
                System.out.println("❌ Invalid date format! Please use yyyy-MM-dd HH:mm");
            }
        }
        
        System.out.println("\n✅ Successfully added " + slotsAdded + " appointment slots for Dr. " + doctorName);
    }
    
    /**
     * Display all available appointment slots
     */
    private void displayAvailableSlots() {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("                   AVAILABLE APPOINTMENT SLOTS");
        System.out.println("=".repeat(70));
        
        List<Appointment> availableSlots = appointments.stream()
            .filter(apt -> !apt.isBooked())
            .sorted(Comparator.comparing(Appointment::getDateTime))
            .toList();
        
        if (availableSlots.isEmpty()) {
            System.out.println("❌ No available appointment slots found!");
            System.out.println("💡 Add some appointment slots first using option 1.");
            return;
        }
        
        System.out.printf("%-5s %-20s %-25s %-10s%n", "ID", "Doctor", "Date & Time", "Status");
        System.out.println("-".repeat(70));
        
        for (Appointment appointment : availableSlots) {
            System.out.printf("%-5d %-20s %-25s %-10s%n",
                appointment.getId(),
                "Dr. " + appointment.getDoctorName(),
                appointment.getDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                "AVAILABLE");
        }
        
        System.out.println("\nTotal available slots: " + availableSlots.size());
    }
    
    /**
     * Book an appointment
     */
    private void bookAppointment() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("              BOOK APPOINTMENT");
        System.out.println("=".repeat(50));
        
        // First show available slots
        List<Appointment> availableSlots = appointments.stream()
            .filter(apt -> !apt.isBooked())
            .sorted(Comparator.comparing(Appointment::getDateTime))
            .toList();
        
        if (availableSlots.isEmpty()) {
            System.out.println("❌ No available appointment slots!");
            return;
        }
        
        System.out.println("Available appointment slots:");
        System.out.printf("%-5s %-20s %-25s%n", "ID", "Doctor", "Date & Time");
        System.out.println("-".repeat(50));
        
        for (Appointment appointment : availableSlots) {
            System.out.printf("%-5d %-20s %-25s%n",
                appointment.getId(),
                "Dr. " + appointment.getDoctorName(),
                appointment.getDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        }
        
        // Get appointment selection
        int appointmentId = getIntInput("\nEnter appointment ID to book: ");
        
        Appointment selectedAppointment = appointments.stream()
            .filter(apt -> apt.getId() == appointmentId && !apt.isBooked())
            .findFirst()
            .orElse(null);
        
        if (selectedAppointment == null) {
            System.out.println("❌ Invalid appointment ID or appointment already booked!");
            return;
        }
        
        // Get patient details
        System.out.print("Enter patient name: ");
        String patientName = scanner.nextLine().trim();
        
        if (patientName.isEmpty()) {
            System.out.println("❌ Patient name cannot be empty!");
            return;
        }
        
        System.out.print("Enter patient email: ");
        String patientEmail = scanner.nextLine().trim();
        
        if (patientEmail.isEmpty() || !EmailServiceSimple.isValidEmail(patientEmail)) {
            System.out.println("❌ Please enter a valid email address!");
            return;
        }
        
        // Confirm booking
        System.out.println("\n" + "=".repeat(50));
        System.out.println("             BOOKING CONFIRMATION");
        System.out.println("=".repeat(50));
        System.out.println("Appointment Details:");
        System.out.println("- ID: " + selectedAppointment.getId());
        System.out.println("- Doctor: Dr. " + selectedAppointment.getDoctorName());
        System.out.println("- Date & Time: " + selectedAppointment.getDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        System.out.println("- Patient: " + patientName);
        System.out.println("- Email: " + patientEmail);
        
        System.out.print("\nConfirm booking? (y/n): ");
        String confirmation = scanner.nextLine().trim().toLowerCase();
        
        if (confirmation.equals("y") || confirmation.equals("yes")) {
            // Book the appointment
            if (selectedAppointment.bookAppointment(patientName, patientEmail)) {
                System.out.println("\n✅ Appointment booked successfully!");
                
                // Send confirmation email
                System.out.println("📧 Sending confirmation email...");
                emailService.sendSimpleConfirmation(selectedAppointment);
                
                System.out.println("🎉 Booking completed! Confirmation email sent to " + patientEmail);
            } else {
                System.out.println("❌ Failed to book appointment!");
            }
        } else {
            System.out.println("❌ Booking cancelled.");
        }
    }
    
    /**
     * View all booked appointments
     */
    private void viewBookedAppointments() {
        System.out.println("\n" + "=".repeat(80));
        System.out.println("                        BOOKED APPOINTMENTS");
        System.out.println("=".repeat(80));
        
        List<Appointment> bookedAppointments = appointments.stream()
            .filter(Appointment::isBooked)
            .sorted(Comparator.comparing(Appointment::getDateTime))
            .toList();
        
        if (bookedAppointments.isEmpty()) {
            System.out.println("❌ No booked appointments found!");
            return;
        }
        
        System.out.printf("%-5s %-20s %-25s %-20s %-25s%n", 
                         "ID", "Doctor", "Date & Time", "Patient", "Email");
        System.out.println("-".repeat(80));
        
        for (Appointment appointment : bookedAppointments) {
            System.out.printf("%-5d %-20s %-25s %-20s %-25s%n",
                appointment.getId(),
                "Dr. " + appointment.getDoctorName(),
                appointment.getDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                appointment.getPatientName(),
                appointment.getPatientEmail());
        }
        
        System.out.println("\nTotal booked appointments: " + bookedAppointments.size());
    }
    
    /**
     * Cancel a booked appointment
     */
    private void cancelAppointment() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("            CANCEL APPOINTMENT");
        System.out.println("=".repeat(50));
        
        List<Appointment> bookedAppointments = appointments.stream()
            .filter(Appointment::isBooked)
            .sorted(Comparator.comparing(Appointment::getDateTime))
            .toList();
        
        if (bookedAppointments.isEmpty()) {
            System.out.println("❌ No booked appointments to cancel!");
            return;
        }
        
        System.out.println("Booked appointments:");
        System.out.printf("%-5s %-20s %-25s %-20s%n", "ID", "Doctor", "Date & Time", "Patient");
        System.out.println("-".repeat(70));
        
        for (Appointment appointment : bookedAppointments) {
            System.out.printf("%-5d %-20s %-25s %-20s%n",
                appointment.getId(),
                "Dr. " + appointment.getDoctorName(),
                appointment.getDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                appointment.getPatientName());
        }
        
        int appointmentId = getIntInput("\nEnter appointment ID to cancel: ");
        
        Appointment appointmentToCancel = appointments.stream()
            .filter(apt -> apt.getId() == appointmentId && apt.isBooked())
            .findFirst()
            .orElse(null);
        
        if (appointmentToCancel == null) {
            System.out.println("❌ Invalid appointment ID or appointment not booked!");
            return;
        }
        
        System.out.print("Are you sure you want to cancel this appointment? (y/n): ");
        String confirmation = scanner.nextLine().trim().toLowerCase();
        
        if (confirmation.equals("y") || confirmation.equals("yes")) {
            appointmentToCancel.cancelBooking();
            System.out.println("✅ Appointment cancelled successfully!");
        } else {
            System.out.println("❌ Cancellation aborted.");
        }
    }
    
    /**
     * Search appointments by doctor name or date
     */
    private void searchAppointments() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("            SEARCH APPOINTMENTS");
        System.out.println("=".repeat(50));
        
        System.out.println("Search options:");
        System.out.println("1. Search by doctor name");
        System.out.println("2. Search by date (yyyy-MM-dd)");
        
        int choice = getIntInput("Enter choice: ");
        
        switch (choice) {
            case 1:
                searchByDoctor();
                break;
            case 2:
                searchByDate();
                break;
            default:
                System.out.println("❌ Invalid choice!");
        }
    }
    
    /**
     * Search appointments by doctor name
     */
    private void searchByDoctor() {
        System.out.print("Enter doctor name (partial match allowed): ");
        String doctorQuery = scanner.nextLine().trim().toLowerCase();
        
        List<Appointment> matches = appointments.stream()
            .filter(apt -> apt.getDoctorName().toLowerCase().contains(doctorQuery))
            .sorted(Comparator.comparing(Appointment::getDateTime))
            .toList();
        
        displaySearchResults(matches, "doctor name containing '" + doctorQuery + "'");
    }
    
    /**
     * Search appointments by date
     */
    private void searchByDate() {
        System.out.print("Enter date (yyyy-MM-dd): ");
        String dateQuery = scanner.nextLine().trim();
        
        try {
            List<Appointment> matches = appointments.stream()
                .filter(apt -> apt.getDateTime().toLocalDate().toString().equals(dateQuery))
                .sorted(Comparator.comparing(Appointment::getDateTime))
                .toList();
            
            displaySearchResults(matches, "date " + dateQuery);
        } catch (Exception e) {
            System.out.println("❌ Invalid date format! Please use yyyy-MM-dd");
        }
    }
    
    /**
     * Display search results
     */
    private void displaySearchResults(List<Appointment> appointments, String criteria) {
        System.out.println("\nSearch results for " + criteria + ":");
        
        if (appointments.isEmpty()) {
            System.out.println("❌ No appointments found!");
            return;
        }
        
        System.out.printf("%-5s %-20s %-25s %-15s %-20s%n", 
                         "ID", "Doctor", "Date & Time", "Status", "Patient");
        System.out.println("-".repeat(85));
        
        for (Appointment appointment : appointments) {
            System.out.printf("%-5d %-20s %-25s %-15s %-20s%n",
                appointment.getId(),
                "Dr. " + appointment.getDoctorName(),
                appointment.getDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                appointment.isBooked() ? "BOOKED" : "AVAILABLE",
                appointment.isBooked() ? appointment.getPatientName() : "");
        }
        
        System.out.println("\nFound " + appointments.size() + " appointment(s)");
    }
    
    /**
     * Get integer input with error handling
     */
    private int getIntInput(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                int value = Integer.parseInt(scanner.nextLine().trim());
                return value;
            } catch (NumberFormatException e) {
                System.out.println("❌ Please enter a valid number!");
            }
        }
    }
    

}