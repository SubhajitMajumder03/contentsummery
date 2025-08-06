import java.time.format.DateTimeFormatter;

/**
 * Simplified email service for demonstration (no external dependencies required)
 * This version simulates email sending for testing purposes
 */
public class EmailServiceSimple {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("EEEE, MMMM dd, yyyy 'at' HH:mm");
    
    private String fromEmail;
    private String fromPassword;
    
    public EmailServiceSimple(String fromEmail, String fromPassword) {
        this.fromEmail = fromEmail;
        this.fromPassword = fromPassword;
    }
    
    /**
     * Simulates sending appointment confirmation email
     */
    public boolean sendConfirmationEmail(Appointment appointment) {
        // Simulate email sending with console output
        sendSimpleConfirmation(appointment);
        return true;
    }
    
    /**
     * Sends a console-based confirmation email simulation
     */
    public void sendSimpleConfirmation(Appointment appointment) {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("                EMAIL CONFIRMATION SENT");
        System.out.println("=".repeat(60));
        System.out.println("From: " + fromEmail);
        System.out.println("To: " + appointment.getPatientEmail());
        System.out.println("Subject: Appointment Confirmation - Dr. " + appointment.getDoctorName());
        System.out.println();
        System.out.println("Dear " + appointment.getPatientName() + ",");
        System.out.println();
        System.out.println("Your appointment has been confirmed!");
        System.out.println();
        System.out.println("APPOINTMENT DETAILS:");
        System.out.println("┌─────────────────────────────────────────────┐");
        System.out.println("│ ID: " + String.format("%-39s", appointment.getId()) + "│");
        System.out.println("│ Doctor: " + String.format("%-33s", "Dr. " + appointment.getDoctorName()) + "│");
        System.out.println("│ Date & Time: " + String.format("%-28s", appointment.getDateTime().format(FORMATTER)) + "│");
        System.out.println("│ Patient: " + String.format("%-32s", appointment.getPatientName()) + "│");
        System.out.println("│ Email: " + String.format("%-34s", appointment.getPatientEmail()) + "│");
        System.out.println("└─────────────────────────────────────────────┘");
        System.out.println();
        System.out.println("IMPORTANT NOTES:");
        System.out.println("• Please arrive 15 minutes before your scheduled time");
        System.out.println("• Bring a valid ID and your insurance card");
        System.out.println("• To reschedule/cancel, contact us 24 hours in advance");
        System.out.println();
        System.out.println("If you have any questions, please contact our office.");
        System.out.println("Thank you for choosing our medical services!");
        System.out.println();
        System.out.println("Best regards,");
        System.out.println("MediBook Appointment System");
        System.out.println("=".repeat(60));
        System.out.println("✉️  Email successfully sent to " + appointment.getPatientEmail());
        System.out.println("=".repeat(60) + "\n");
    }
    
    /**
     * Validates email format
     */
    public static boolean isValidEmail(String email) {
        return email != null && email.contains("@") && email.contains(".") && email.length() > 5;
    }
}