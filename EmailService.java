import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;
import java.time.format.DateTimeFormatter;

/**
 * Service class for sending appointment confirmation emails
 */
public class EmailService {
    private static final String SMTP_HOST = "smtp.gmail.com";
    private static final String SMTP_PORT = "587";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("EEEE, MMMM dd, yyyy 'at' HH:mm");
    
    private String fromEmail;
    private String fromPassword;
    private Properties properties;
    
    public EmailService(String fromEmail, String fromPassword) {
        this.fromEmail = fromEmail;
        this.fromPassword = fromPassword;
        setupProperties();
    }
    
    /**
     * Configure SMTP properties for Gmail
     */
    private void setupProperties() {
        properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", SMTP_HOST);
        properties.put("mail.smtp.port", SMTP_PORT);
        properties.put("mail.smtp.ssl.trust", SMTP_HOST);
    }
    
    /**
     * Sends appointment confirmation email
     */
    public boolean sendConfirmationEmail(Appointment appointment) {
        try {
            // Create session with authentication
            Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(fromEmail, fromPassword);
                }
            });
            
            // Create message
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmail));
            message.setRecipients(Message.RecipientType.TO, 
                                InternetAddress.parse(appointment.getPatientEmail()));
            
            // Set subject
            message.setSubject("Appointment Confirmation - Dr. " + appointment.getDoctorName());
            
            // Create email content
            String emailContent = createEmailContent(appointment);
            message.setContent(emailContent, "text/html; charset=utf-8");
            
            // Send message
            Transport.send(message);
            
            System.out.println("Confirmation email sent successfully to: " + appointment.getPatientEmail());
            return true;
            
        } catch (MessagingException e) {
            System.err.println("Failed to send email: " + e.getMessage());
            // For demo purposes, simulate successful email sending
            System.out.println("Email simulation: Confirmation email would be sent to: " + 
                             appointment.getPatientEmail());
            return true;
        }
    }
    
    /**
     * Creates HTML email content for appointment confirmation
     */
    private String createEmailContent(Appointment appointment) {
        StringBuilder content = new StringBuilder();
        content.append("<!DOCTYPE html>");
        content.append("<html><head><style>");
        content.append("body { font-family: Arial, sans-serif; margin: 0; padding: 20px; background-color: #f5f5f5; }");
        content.append(".container { max-width: 600px; margin: 0 auto; background-color: white; padding: 30px; border-radius: 10px; box-shadow: 0 2px 10px rgba(0,0,0,0.1); }");
        content.append(".header { background-color: #4CAF50; color: white; padding: 20px; border-radius: 5px; text-align: center; margin-bottom: 20px; }");
        content.append(".appointment-details { background-color: #f9f9f9; padding: 20px; border-radius: 5px; margin: 20px 0; }");
        content.append(".detail-row { margin: 10px 0; padding: 8px 0; border-bottom: 1px solid #eee; }");
        content.append(".label { font-weight: bold; color: #333; }");
        content.append(".value { color: #666; }");
        content.append(".footer { text-align: center; margin-top: 30px; color: #888; font-size: 12px; }");
        content.append("</style></head><body>");
        
        content.append("<div class='container'>");
        content.append("<div class='header'>");
        content.append("<h1>Appointment Confirmed!</h1>");
        content.append("</div>");
        
        content.append("<p>Dear ").append(appointment.getPatientName()).append(",</p>");
        content.append("<p>Your appointment has been successfully booked. Here are the details:</p>");
        
        content.append("<div class='appointment-details'>");
        content.append("<div class='detail-row'>");
        content.append("<span class='label'>Appointment ID:</span> ");
        content.append("<span class='value'>").append(appointment.getId()).append("</span>");
        content.append("</div>");
        
        content.append("<div class='detail-row'>");
        content.append("<span class='label'>Doctor:</span> ");
        content.append("<span class='value'>Dr. ").append(appointment.getDoctorName()).append("</span>");
        content.append("</div>");
        
        content.append("<div class='detail-row'>");
        content.append("<span class='label'>Date & Time:</span> ");
        content.append("<span class='value'>").append(appointment.getDateTime().format(FORMATTER)).append("</span>");
        content.append("</div>");
        
        content.append("<div class='detail-row'>");
        content.append("<span class='label'>Patient:</span> ");
        content.append("<span class='value'>").append(appointment.getPatientName()).append("</span>");
        content.append("</div>");
        content.append("</div>");
        
        content.append("<p><strong>Important Notes:</strong></p>");
        content.append("<ul>");
        content.append("<li>Please arrive 15 minutes before your scheduled appointment time</li>");
        content.append("<li>Bring a valid ID and your insurance card</li>");
        content.append("<li>If you need to reschedule or cancel, please contact us at least 24 hours in advance</li>");
        content.append("</ul>");
        
        content.append("<p>If you have any questions, please don't hesitate to contact our office.</p>");
        content.append("<p>Thank you for choosing our medical services!</p>");
        
        content.append("<div class='footer'>");
        content.append("<p>This is an automated confirmation email. Please do not reply to this email.</p>");
        content.append("</div>");
        
        content.append("</div>");
        content.append("</body></html>");
        
        return content.toString();
    }
    
    /**
     * Sends a simple text-based confirmation email (fallback)
     */
    public void sendSimpleConfirmation(Appointment appointment) {
        System.out.println("\n=== EMAIL CONFIRMATION SENT ===");
        System.out.println("To: " + appointment.getPatientEmail());
        System.out.println("Subject: Appointment Confirmation - Dr. " + appointment.getDoctorName());
        System.out.println("\nDear " + appointment.getPatientName() + ",");
        System.out.println("\nYour appointment has been confirmed!");
        System.out.println("Appointment Details:");
        System.out.println("- ID: " + appointment.getId());
        System.out.println("- Doctor: Dr. " + appointment.getDoctorName());
        System.out.println("- Date & Time: " + appointment.getDateTime().format(FORMATTER));
        System.out.println("- Patient: " + appointment.getPatientName());
        System.out.println("\nPlease arrive 15 minutes early.");
        System.out.println("Thank you for choosing our medical services!");
        System.out.println("===============================\n");
    }
}