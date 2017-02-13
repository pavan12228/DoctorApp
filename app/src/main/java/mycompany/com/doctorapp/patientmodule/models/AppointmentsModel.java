package mycompany.com.doctorapp.patientmodule.models;

import java.io.Serializable;

/**
 * Created by Dell on 8/11/2016.
 */
public class AppointmentsModel implements Serializable{
    String image;
    String name;
    String address;
    String date;
    String doctorId;
    String dateOnly;
    String timeOnly;

    public String getTimeOnly() {
        return timeOnly;
    }

    public void setTimeOnly(String timeOnly) {
        this.timeOnly = timeOnly;
    }

    String appointmentId;

    public String getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(String appointmentId) {
        this.appointmentId = appointmentId;
    }

    public String getDateOnly() {
        return dateOnly;
    }

    public void setDateOnly(String dateOnly) {
        this.dateOnly = dateOnly;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    String doctorQualification;
    String mExperience;

    public String getmExperience() {
        return mExperience;
    }

    public void setmExperience(String mExperience) {
        this.mExperience = mExperience;
    }

    public String getDoctorSpecialization() {
        return doctorSpecialization;
    }

    public void setDoctorSpecialization(String doctorSpecialization) {
        this.doctorSpecialization = doctorSpecialization;
    }

    public String getDoctorQualification() {
        return doctorQualification;
    }

    public void setDoctorQualification(String doctorQualification) {
        this.doctorQualification = doctorQualification;
    }

    String doctorSpecialization;

    public String getReschedule() {
        return reschedule;
    }

    public void setReschedule(String reschedule) {
        this.reschedule = reschedule;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    String reschedule;
}
