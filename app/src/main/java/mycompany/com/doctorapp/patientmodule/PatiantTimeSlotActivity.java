package mycompany.com.doctorapp.patientmodule;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import mycompany.com.doctorapp.R;
import mycompany.com.doctorapp.common_activities.LoginActivity;
import mycompany.com.doctorapp.patientmodule.adapters.TimingsAdapter;
import mycompany.com.doctorapp.patientmodule.models.AppointmentsModel;
import mycompany.com.doctorapp.patientmodule.models.HoursModel;
import mycompany.com.doctorapp.patientmodule.models.SearchDoctorModel;
import mycompany.com.doctorapp.utils.StringConstants;
import mycompany.com.doctorapp.utils.Util;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class PatiantTimeSlotActivity extends AppCompatActivity implements View.OnClickListener   {
    TextView tvDoctorName,tvDoctorDetails,mTimeSlotMorning,mTimeSlotAfternoon,mTimeSlotEvenining,mTimeSlotNight;
    LinearLayout mMorningLinear1,mMorningLinear2,mEveningLinear1,mEveningLinear2,mNightLinear1,
            mNightLinear2,mAfternoonLinear1,mAfternoonLinear2;
    ImageView mBack,mMenu;
    ArrayAdapter<String> stringArrayAdapter;
    CalendarView calendarView;
    RelativeLayout mRelativeLayout,morningDownRl,afterDownRl,eveningDownRl,nightDownRl;
    private GridView gvMorningTimings,gvAfternoonTimings,gvEveningTimings,gvNightTimings;
    private ListView mTimeSlotUpperLv;
    String[] timings ;
    private String sSelectedDate,selected;
    Util mUtil;
    private List<String> hoursList = new ArrayList<>();
    private List<HoursModel> hoursModelList = new ArrayList<>();
    private int s,mUserId;
    private String doctor_id,mAppointmentId,sDate,sTime , mFrom = null,selectedTime;
    private long currentTime;
    private SearchDoctorModel searchDoctorModel;
    private String[] mTimeTags  = {
            ":00:00",":10:00",
            ":20:00",":30:00",
            ":40:00",":50:00"};
    private String[] mAfternoonTimings ;
    private final  String TAG = "LoekshComedy";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_time_slot);
        initializeViews();
        mUtil = new Util();

//        currentTime();

        Intent intent = getIntent();
        if(intent.getStringExtra("adapter").equals("appointment")){
            mFrom = "appointment";   /* setting it is coming from appointments*/
            AppointmentsModel appointmentsModel = (AppointmentsModel) intent.getSerializableExtra("model");
            doctor_id = appointmentsModel.getDoctorId();
            mAppointmentId  = appointmentsModel.getAppointmentId();
            tvDoctorName.setText(appointmentsModel.getName());
            sDate = appointmentsModel.getDateOnly();
            SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
            Date t = null;
            try {
                t = ft.parse(sDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            long mDate = t.getTime();
            calendarView.setDate(mDate);   /* setting appointment date to calendar view*/



            tvDoctorDetails.setText(appointmentsModel.getDoctorSpecialization()+ ", " +
                    appointmentsModel.getDoctorQualification()+
                    ", " + appointmentsModel.getmExperience()+" Years of Experience");
        }else if(intent.getStringExtra("adapter").equals("SearchAdapter")) {
            mFrom = "searchadapter";
            searchDoctorModel = (SearchDoctorModel) intent.getSerializableExtra("model");
            doctor_id = searchDoctorModel.getDoctor_id();
            tvDoctorName.setText(searchDoctorModel.getName());
            tvDoctorDetails.setText(searchDoctorModel.getSpecialization()+ ", " + searchDoctorModel.getQualification()+
                    ", " + searchDoctorModel.getExperience()+" Years of Experience");
        }



        mUtil = new Util();
        mUtil.getSharedpreferences(this);
        Collections.sort(hoursList);
        stringArrayAdapter = new ArrayAdapter<String>(this,R.layout.inflator_timings_list,R.id.showtime,hoursList);
        mTimeSlotUpperLv.setAdapter(stringArrayAdapter);


        new PostTimeSlotUpper().execute();/* when activity is opened,current date slots to be shown,
                                            next time calendar clicks postTimeSlotUpper() method is used,
                                            postTimeSlotUpper() can not be used first time,
                                            due to main thread goes off immediately.*/

    }



    public void bookAppointmentService(final String s){

        mUtil.twoButtonAlertDialog(this,"Confirm message","Confirm Schedule ? ",true,new MaterialDialog.ButtonCallback() {
            @Override
            public void onPositive(MaterialDialog dialog) {
                if (mFrom.equals("searchadapter")) {
                    if (Util.isNetworkAvailable(getApplicationContext())) {
//                        mUtil.showProgressDialog(getApplicationContext());
                        mUtil.getBaseClassService(getApplicationContext(), "").postBookDoctor(Integer.parseInt(doctor_id),
                                mUserId, s, sSelectedDate, new Callback<JsonObject>() {
                                    @Override
                                    public void success(JsonObject jsonObject, Response response) {
                                        if (jsonObject.get(StringConstants.STATUS).getAsString().equalsIgnoreCase("Success")) {
                                            mUtil.dismissDialog();
                                            callToast(jsonObject.get("message").getAsString());
                                            if(SearchDoctorsActivity.mClosedByPatientActivity != null) {
                                                SearchDoctorsActivity.mClosedByPatientActivity.finish();
                                            }
                                            finish();
                                        } else {
                                            mUtil.dismissDialog();
                                            try {
                                                Toast.makeText(PatiantTimeSlotActivity.this,
                                                        jsonObject.get("message").getAsString(), Toast.LENGTH_LONG).show();
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }

                                    @Override
                                    public void failure(RetrofitError error) {
                                        mUtil.dismissDialog();
                                        mUtil.serviceCallFailermsg(error, PatiantTimeSlotActivity.this);
                                    }
                                });


                    }
                }else if(mFrom.equals("appointment")){
                    if (Util.isNetworkAvailable(getApplicationContext())) {
                        mUtil.getBaseClassService(getApplicationContext(), "").postBookDoctorAppointments(Integer.parseInt(mAppointmentId),
                                sSelectedDate, String.valueOf(s),new Callback<JsonObject>() {
                                    @Override
                                    public void success(JsonObject jsonObject, Response response) {
                                        if (jsonObject.get(StringConstants.STATUS).getAsString().equalsIgnoreCase("Success")) {
                                            mUtil.dismissDialog();
                                            callToast(jsonObject.get("message").getAsString());
                                            if(SearchDoctorsActivity.mClosedByPatientActivity != null) {
                                                SearchDoctorsActivity.mClosedByPatientActivity.finish();
                                            }
                                            finish();
                                        } else {
                                            mUtil.dismissDialog();
                                            try {
                                                Toast.makeText(PatiantTimeSlotActivity.this,
                                                        jsonObject.get("errorMessages").getAsString(), Toast.LENGTH_LONG).show();
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }

                                    @Override
                                    public void failure(RetrofitError error) {
                                        mUtil.dismissDialog();
                                        mUtil.serviceCallFailermsg(error, PatiantTimeSlotActivity.this);
                                    }
                                });


                    }
                }
            }

            @Override
            public void onNegative(MaterialDialog dialog) {
                dialog.dismiss();
            }
        });



    }


    /* after user login, bookAppointmentService() is executed. */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == 200) {
            bookAppointmentService(selectedTime);
        }
    }

    /* if user already login bookAppointmentService() is executed,or login screen willbe opened*/
    private void bookAppointment(String s) {
        if (!Util.getString(StringConstants.USERID).isEmpty() && !Util.getString(StringConstants.USERID).equals(null))
            mUserId = Integer.parseInt(mUtil.getString(StringConstants.USERID));
        if (mUserId == 0) {
            Intent intent = new Intent(PatiantTimeSlotActivity.this, LoginActivity.class);
            intent.putExtra("activity", "PatiantTimeSlotActivity");
            intent.putExtra("skip","skipFromPatientTimeSlot");
            selectedTime  = s;
            startActivityForResult(intent, 100);

        }else {
            bookAppointmentService(s);
        }
    }


    private void initializeViews() {
        mMenu = (ImageView) findViewById(R.id.toolbar_menu);
        mMenu.setVisibility(View.GONE);   /* menu is no need here,therefor it is hidden in this activity*/

        mEveningLinear1 = (LinearLayout) findViewById(R.id.evening_ll);

        mNightLinear1 = (LinearLayout) findViewById(R.id.night3_ll);

        gvMorningTimings = (GridView) findViewById(R.id.morning_gv);

        gvAfternoonTimings = (GridView) findViewById(R.id.afternoon_gv);
        gvEveningTimings = (GridView) findViewById(R.id.evening_gv);
        gvNightTimings = (GridView) findViewById(R.id.night_gv);


        mMorningLinear1 = (LinearLayout) findViewById(R.id.morning_ll);

        mAfternoonLinear1 = (LinearLayout) findViewById(R.id.activitypatianttimeslot__afternoon_ll);


        mBack = (ImageView) findViewById(R.id.commontoolbarbacktitlesinglemenu_back_iv);
        mBack.setOnClickListener(this);

        morningDownRl = (RelativeLayout) findViewById(R.id.hiddenlayout1);
        morningDownRl.setOnClickListener(this);
        morningDownRl.setEnabled(false); /* disables click listener in layout */

        afterDownRl = (RelativeLayout) findViewById(R.id.hiddenlayout2);
        afterDownRl.setOnClickListener(this);
        afterDownRl.setEnabled(false);    /* disables click listener in layout */

        eveningDownRl = (RelativeLayout) findViewById(R.id.hiddenlayout3);
        eveningDownRl.setOnClickListener(this);
        eveningDownRl.setEnabled(false);    /* disables click listener in layout */

        nightDownRl = (RelativeLayout) findViewById(R.id.hiddenlayout4);
        nightDownRl.setOnClickListener(this);
        nightDownRl.setEnabled(false);     /* disables click listener in layout */

        mTimeSlotUpperLv = (ListView) findViewById(R.id.time_slot_lv);


        calendarView = (CalendarView) findViewById(R.id.cal_small);


        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView calendarView, int i, int i1, int i2) {
                sSelectedDate = i+"-"+(i1+1)+"-"+i2;
                mMorningLinear1.setVisibility(View.GONE);       /* when calendar_view changed,all other GV's should be closed.*/
                mAfternoonLinear1.setVisibility(View.GONE);     /* when calendar_view changed,all other GV's should be closed.*/
                mEveningLinear1.setVisibility(View.GONE);       /* when calendar_view changed,all other GV's should be closed.*/
                mNightLinear1.setVisibility(View.GONE);         /* when calendar_view changed,all other GV's should be closed.*/
                postTimeSlotUpper();



            }
        });

        mTimeSlotUpperLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView textView = (TextView) view.findViewById(R.id.showtime);
                String raw = textView.getText().toString();

                String ss[] = raw.split("-");
                int e  = Integer.parseInt(ss[0]);
                if(e <12 & raw.contains("A")){
                    hoursModelList.clear();
                    new GetBookedTimeSlots().execute(e);
                    morningDownRl.performClick();
                }else if(e<4 & raw.contains("P")){
                    hoursModelList.clear();
                    new GetBookedTimeSlots().execute(e);
                    afterDownRl.performClick();
                }else if(e<7 & raw.contains("P")){
                    hoursModelList.clear();
                    new GetBookedTimeSlots().execute(e);
                    eveningDownRl.performClick();
                }else if(e<12 & raw.contains("P")){
                    hoursModelList.clear();
                    new GetBookedTimeSlots().execute(e);
                    nightDownRl.performClick();
                }
            }
        });

        tvDoctorName = (TextView) findViewById(R.id.activitypatienttimeslot_name_tv);
        tvDoctorDetails = (TextView) findViewById(R.id.activity_patient_doctordetails_tv);
        mAfternoonTimings = new String[mTimeTags.length];
    }



    /* Does service for getting booked timings to show in red color in gv
     * @param e*/
    private void getBookedTimeSlots(int e) {
        if (Util.isNetworkAvailable(this)) {
            mUtil.getBaseClassService(this, "").getBookedTimeings(Integer.parseInt(doctor_id),
                    sSelectedDate,e,new Callback<JsonObject>() {
                        @Override
                        public void success(JsonObject jsonObject, Response response) {
                            if(jsonObject.get(StringConstants.STATUS).getAsInt() == 1){

                                JsonArray jsonArray = jsonObject.get(StringConstants.DATA).getAsJsonArray();
                                for (int i = 0; i < jsonArray.size(); i++) {
                                    JsonObject object = jsonArray.get(i).getAsJsonObject();
                                    HoursModel hoursModel = new HoursModel();
                                    hoursModel.setTime(object.get("slotTime").getAsString());
                                    Log.d("SANTHOSH","data::"+object.get("slotTime").getAsString());
                                    hoursModel.setStatus(object.get("status").getAsInt());
                                    hoursModelList.add(hoursModel);
                                }

                                TimingsAdapter morningAdapter = new TimingsAdapter(PatiantTimeSlotActivity.this,hoursModelList);
                                /* below animated layouts  */
                                gvMorningTimings.setAdapter(morningAdapter);
                                gvMorningTimings.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                        TextView tvv = (TextView) view.findViewById(R.id.showtime);
                                        if(!tvv.getTag().equals("noClick")){
                                            bookAppointment(tvv.getText().toString());
                                        }else{
                                            callToast("Some one else booked appointment");
                                        }
                                    }
                                });


                                TimingsAdapter afternoonAdapter = new TimingsAdapter(PatiantTimeSlotActivity.this,hoursModelList);
                                gvAfternoonTimings.setAdapter(afternoonAdapter);
                                gvAfternoonTimings.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                        TextView tvv = (TextView) view.findViewById(R.id.showtime);
                                        if(!tvv.getTag().equals("noClick")){
                                            bookAppointment(tvv.getText().toString());
                                        }else{
                                            callToast("Some one else booked appointment");
                                        }
                                    }
                                });


                                TimingsAdapter eveningAdapter = new TimingsAdapter(PatiantTimeSlotActivity.this,hoursModelList);
                                gvEveningTimings.setAdapter(eveningAdapter);
                                gvEveningTimings.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                        TextView tvv = (TextView) view.findViewById(R.id.showtime);
                                        if(!tvv.getTag().equals("noClick")){
                                            bookAppointment(tvv.getText().toString());
                                        }else{
                                            callToast("Some one else booked appointment");
                                        }
                                    }
                                });

                                TimingsAdapter nightAdapter = new TimingsAdapter(PatiantTimeSlotActivity.this,hoursModelList);
                                gvNightTimings.setAdapter(nightAdapter);
                                gvNightTimings.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                        TextView tvv = (TextView) view.findViewById(R.id.showtime);
                                        if(!tvv.getTag().equals("noClick")){
                                            bookAppointment(tvv.getText().toString());
                                        }else{
                                            callToast("Some one else booked appointment");
                                        }
                                    }
                                });
                            }else {
                                Toast.makeText(PatiantTimeSlotActivity.this, "hi", Toast.LENGTH_SHORT).show();
                                try {
                                    Toast.makeText(PatiantTimeSlotActivity.this, jsonObject.get("errorMessages").getAsString(),
                                            Toast.LENGTH_LONG).show();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }


                        }

                        @Override
                        public void failure(RetrofitError error) {
                            Log.e("Lokesh",error.toString());
                            mUtil.serviceCallFailermsg(error,PatiantTimeSlotActivity.this);
                        }
                    });

        }




    }

    private void postTimeSlotUpper() {
        if (Util.isNetworkAvailable(this)) {
            mUtil.getBaseClassService(this, "").getDoctorTimeSlots(Integer.parseInt(doctor_id),
                    sSelectedDate,new Callback<JsonObject>() {
                @Override
                public void success(JsonObject jsonObject, Response response) {
                    if(jsonObject.get(StringConstants.STATUS).getAsString().equalsIgnoreCase("success")){
                        hoursList.clear();
                        JsonArray jsonArray = jsonObject.get(StringConstants.DATA).getAsJsonArray();
                        for (int i = 0; i < jsonArray.size(); i++) {
                                JsonObject object = jsonArray.get(i).getAsJsonObject();
                            s = object.get("starttime").getAsInt();
                            if(s<12) {
                                hoursList.add(s + "-" + (s + 1) + " A.M.");
                            }
                            else{
                                int temp = s;
                                temp = temp-12;
                                hoursList.add(temp+"-"+(temp+1)+" P.M");
                            }
                        }
                        stringArrayAdapter.notifyDataSetChanged();
                    }else {
                        try {
                            Toast.makeText(PatiantTimeSlotActivity.this, jsonObject.get("errorMessages").getAsString(),
                                    Toast.LENGTH_LONG).show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    Log.e("Lokesh",error.toString());
                    mUtil.serviceCallFailermsg(error,PatiantTimeSlotActivity.this);
                }
            });

        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.hiddenlayout1:
                /* when first gv is opend ,others are closed */
                mAfternoonLinear1.setVisibility(View.GONE);
                mEveningLinear1.setVisibility(View.GONE);
                mNightLinear1.setVisibility(View.GONE);
                mMorningLinear1.setVisibility(View.VISIBLE);
                break;

            case R.id.hiddenlayout2:
                mMorningLinear1.setVisibility(View.GONE);
                mEveningLinear1.setVisibility(View.GONE);
                mNightLinear1.setVisibility(View.GONE);
                mAfternoonLinear1.setVisibility(View.VISIBLE);
                break;
            case R.id.hiddenlayout3:
                mMorningLinear1.setVisibility(View.GONE);
                mAfternoonLinear1.setVisibility(View.GONE);
                mNightLinear1.setVisibility(View.GONE);
                mEveningLinear1.setVisibility(View.VISIBLE);
                break;

            case R.id.hiddenlayout4:
                mMorningLinear1.setVisibility(View.GONE);
                mAfternoonLinear1.setVisibility(View.GONE);
                mEveningLinear1.setVisibility(View.GONE);
                mNightLinear1.setVisibility(View.VISIBLE);
                break;
            case R.id.commontoolbarbacktitlesinglemenu_back_iv:
                mBack.setBackgroundColor(getResources().getColor(R.color.backclicked));
                finish();
                break;
        }

    }
    public void callToast(String msg){
        Toast.makeText(PatiantTimeSlotActivity.this, msg, Toast.LENGTH_SHORT).show();
    }


        /* current day time_slots has to be shown , so first time service should done via asyntask*/
 public class PostTimeSlotUpper extends AsyncTask<URL, Integer, Long> {
        protected Long doInBackground(URL... urls) {
            java.util.Date date= new Date();   /** setting current month */
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            int month = cal.get(Calendar.MONTH);
            int day  = cal.get(Calendar.DAY_OF_MONTH);
            int year  = cal.get(Calendar.YEAR);
            sSelectedDate = year+"-"+(month+1)+"-"+day;
            Log.d(TAG, "initializeViews() returned: " +sSelectedDate );

            postTimeSlotUpper();
            return 2l;
        }

        protected void onProgressUpdate(Integer... progress) {

        }

        protected void onPostExecute(Long result) {

        }
    }

    public class GetBookedTimeSlots extends AsyncTask<Integer, Integer, Long> {
        protected Long doInBackground(final Integer... urls) {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    int e  = urls[0];
                    getBookedTimeSlots(e);


                }
            });
            return 2l;
        }

        protected void onProgressUpdate(Integer... progress) {

        }

        protected void onPostExecute(Long result) {

        }
    }
}
