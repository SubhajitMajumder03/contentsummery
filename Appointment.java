import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents a doctor's appointment slot
 */
public class Appointment {
    private int id;
    private String doctorName;
    private LocalDateTime dateTime;
    private boolean isBooked;
    private String patientName;
    private String patientEmail;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    
    public Appointment(int id, String doctorName, LocalDateTime dateTime) {
        this.id = id;
        this.doctorName = doctorName;
        this.dateTime = dateTime;
        this.isBooked = false;
        this.patientName = null;
        this.patientEmail = null;
    }
    
    // Getters
    public int getId() {
        return id;
    }
    
    public String getDoctorName() {
        return doctorName;
    }
    
    public LocalDateTime getDateTime() {
        return dateTime;
    }
    
    public boolean isBooked() {
        return isBooked;
    }
    
    public String getPatientName() {
        return patientName;
    }
    
    public String getPatientEmail() {
        return patientEmail;
    }
    
    // Setters
    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }
    
    public void setPatientEmail(String patientEmail) {
        this.patientEmail = patientEmail;
    }
    
    /**
     * Books the appointment for a patient
     */
    public boolean bookAppointment(String patientName, String patientEmail) {
        if (!isBooked) {
            this.isBooked = true;
            this.patientName = patientName;
            this.patientEmail = patientEmail;
            return true;
        }
        return false;
    }
    
    /**
     * Cancels the appointment booking
     */
    public void cancelBooking() {
        this.isBooked = false;
        this.patientName = null;
        this.patientEmail = null;
    }
    
    @Override
    public String toString() {
        return String.format("ID: %d | Doctor: %s | Time: %s | Status: %s", 
                           id, doctorName, dateTime.format(FORMATTER), 
                           isBooked ? "BOOKED" : "AVAILABLE");
    }
    
    /**
     * Returns a detailed string representation including patient info if booked
     */
    public String getDetailedInfo() {
        String basic = toString();
        if (isBooked && patientName != null) {
            basic += " | Patient: " + patientName;
        }
        return basic;
    }
}