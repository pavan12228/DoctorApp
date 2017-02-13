package mycompany.com.doctorapp.services;

import com.google.gson.JsonObject;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.POST;
import retrofit.http.Query;
import retrofit.mime.MultipartTypedOutput;

/**
 * Created by Dell on 8/30/2016.
 */
public interface ServiceOperations {
    @FormUrlEncoded
    @POST("/patient_register")
    public void register(@Field("firstname") String firstname, @Field("lastname") String lastname,
                         @Field("email_id") String email, @Field("password") String password,
                         @Field("mobile_number") String mobilenumber, Callback<JsonObject> callback);

    @FormUrlEncoded
    @POST("/user_login")
    public void patiantLogin(@Field("email_id") String email_id, @Field("password") String password,
                             Callback<JsonObject> callback);

    @FormUrlEncoded
    @POST("/login")
    void doctorLogin(@Field("email_id")String email_id,@Field("password") String password,
                     @Field("user_type") int type, Callback<JsonObject> callback);


    @FormUrlEncoded
    @POST("/social_user_register")
    void postGamilFaceBookService(@Field("email_id") String emailId,@Field("social_type") String socialType,
                                  @Field("user_type") String userType, Callback<JsonObject> callback);

    @FormUrlEncoded
    @POST("/forgot_password")
    void forgotPassword(@Field("type") int type,@Field("email") String email,Callback<JsonObject> callback);


    @GET("/patient_profile/")
    public void getPatientProfile(@Query("id") int id, Callback<JsonObject> callback);

    @FormUrlEncoded
    @POST("/updatePatient")
    void postPatientProfile(@Field("id") int id,@Field("firstname") String firstName,
                            @Field("lastname") String lastName,
                            @Field("mobile_number") String mobilenumber,Callback<JsonObject> callback);

    @FormUrlEncoded
    @POST("/changePassword")
    void patientChangePassword(@Field("login_id") int id,@Field("old_password") String oldP,
                               @Field("new_password") String newP,@Field("type") int type,
                               Callback<JsonObject> callback);


    @GET("/patientAppointments")
    void getPatientAppointments(@Query("patient_id") int doctor_id, Callback<JsonObject> callback);

    @GET("/getSpecializations")
    void getSpecilizations(Callback<JsonObject> callback);

    @GET("/getStates")
    void getStates(Callback<JsonObject> callback);

    @GET("/getDistricts")
    void getDistrict(@Query("state_id") int state, Callback<JsonObject> callback);

    @GET("/getCities")
    void getCities(@Query("district_id") int city,Callback<JsonObject> callback);

    @Headers("Accept:application/json")
    @POST("/doctor_register")
    void postDoctorRegisteraction(@Body MultipartTypedOutput attachments,
                                  Callback<JsonObject> callback);

    @GET("/doctor_profile")
    void getDoctorDetailsDetails(@Query("id") int id,Callback<JsonObject> callback);

//    @FormUrlEncoded
//    @POST("/updateDoctor")
//    void postDoctorEditProfile(@Field("id") int id,@Field("doctor_name") String doctor_name,
//                               @Field("specialization") int specialization,
//                               @Field("experience") String experience,@Field("fee") String fee,
//                               @Field("Name_of_the_hospital") String Name_of_the_hospital,
//                               @Field("hospital_address") String hospital_address,
//                               @Field("qualification") String qualification,
//                               Callback<JsonObject> callback);


    @Headers("Accept:application/json")
    @POST("/updateDoctor")
    void postDoctorEditProfile(@Body MultipartTypedOutput attachments,
                                  Callback<JsonObject> callback);


    @GET("/doctorAppointments")
    void doctorAppointments(@Query("doctor_id") int id,Callback<JsonObject> callback);

    @GET("/drug_data")
    void getDrugs(Callback<JsonObject> callback);

    @FormUrlEncoded
    @POST("/doctorTimeSlot")
    void  getDoctorTimeSlots(@Field("id") int id,@Field("date") String date,
                             Callback<JsonObject> callback );

    @FormUrlEncoded
    @POST("/getDoctorAvailableAppointments")
    void getBookedTimeings(@Field("doctorId") int id,@Field("date") String date,@Field("time") int time,
                           Callback<JsonObject> callback );

    @FormUrlEncoded
    @POST("/cancelAppointments")
    void cancelAppointmentFromDoctor(@Field("appointmentid") String appointId,
                                     @Field("patient_id") String pId, Callback<JsonObject> callback);

    @FormUrlEncoded
    @POST("/bookAppointments")
    void postBookDoctor(@Field("doctor_id") int doctor_id,@Field("patient_id") int  patientId,
                        @Field("schedule_time") String scheduleTime,
                        @Field("schedule_date") String scheduleDate, Callback<JsonObject> callback);

    @FormUrlEncoded
    @POST("/patientResheduleApp")
    void postBookDoctorAppointments(@Field("appointment_id") int appointmentId,@Field("date") String date,
                        @Field("time") String time, Callback<JsonObject> callback);

    @GET("/schduleLists")
    void getDoctorSchedule(@Query("doctorid") int id, Callback<JsonObject> callback);

    @FormUrlEncoded
    @POST("/searchDoctor")
    void searchDoctor(@Field("hospital_name") String hospitalname,@Field("districts") String districts,
                      @Field("search_location") String searchlocation,@Field("search_specialistion")
                      String searchspecialistion, @Field("doctor_name") String doctorname,
                      Callback<JsonObject> callback);

    @FormUrlEncoded
    @POST("/addrEditSchedule")
    void addScheduleDoctor(@Field("doctorid") int mUserId, @Field("from_date") String fromDate,
                           @Field("to_date") String toDate, @Field("slots[]") String slt,Callback<JsonObject> callback);

    @FormUrlEncoded
    @POST("/addrEditSchedule")
    void editScheduleDoctor(@Field("doctorid") int mUserId, @Field("from_date") String fromDate,
                           @Field("to_date") String toDate, @Field("slots[]") String slt,@Field("batchId") int batchId, Callback<JsonObject> callback);

    @FormUrlEncoded
    @POST("/cancelDoctorSchedule")
    void cancelShedule(@Field("doctorid") int doctorId,@Field("batchid") int batchId,
                       Callback<JsonObject> callback);



}
