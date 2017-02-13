package mycompany.com.doctorapp.doctormodule.models;

import java.util.ArrayList;

/**
 * Created by santhu on 8/13/2016.
 */
public class SheduleModel {
    private String id;
    private String fromDate;
    private String toDate;

    public String getBatch_id() {
        return batch_id;
    }

    public void setBatch_id(String batch_id) {
        this.batch_id = batch_id;
    }

    private String batch_id;


    public ArrayList<String> getTimeSlots() {
        return timeSlots;
    }

    public void setTimeSlots(ArrayList<String> timeSlots) {
        this.timeSlots = timeSlots;
    }

    private ArrayList<String> timeSlots;





    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    private String fromTime;

    public String getToTime() {
        return toTime;
    }

    public void setToTime(String toTime) {
        this.toTime = toTime;
    }

    public String getFromTime() {
        return fromTime;
    }

    public void setFromTime(String fromTime) {
        this.fromTime = fromTime;
    }

    private String toTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
