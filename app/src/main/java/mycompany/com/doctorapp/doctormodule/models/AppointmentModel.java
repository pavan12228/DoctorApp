package mycompany.com.doctorapp.doctormodule.models;

/**
 * Created by sciens1 on 8/12/2016.
 */
public class AppointmentModel {
    private String time;
    private String mobile;
    private String patientId;
    private String appointmentStatus;

    public String getAppointmentStatus() {
        return appointmentStatus;
    }

    public void setAppointmentStatus(String appointmentStatus) {
        this.appointmentStatus = appointmentStatus;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(String appointmentId) {
        this.appointmentId = appointmentId;
    }

    private String appointmentId;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    private String email;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
