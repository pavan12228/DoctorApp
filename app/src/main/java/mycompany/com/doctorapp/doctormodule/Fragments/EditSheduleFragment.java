package mycompany.com.doctorapp.doctormodule.Fragments;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

import mycompany.com.doctorapp.R;
import mycompany.com.doctorapp.utils.StringConstants;
import mycompany.com.doctorapp.utils.Util;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by sciens1 on 8/13/2016.
 */
public class EditSheduleFragment extends Fragment implements View.OnClickListener {
    private Button mReshedule;
    private Util mUtil;
    public EditText mFromDate,mToDate;
    static final int DATE_PICKER_ID = 1111;
    private int year,month,day,spSelectedPosition,mUserId,selectedSechedule,currentHour;
    private  String year1,month1,day1,year2,month2,day2,sFromDate,sToDate,selected,fromdate,todate;
    private ArrayList<String> timeslots = new ArrayList<>();
    private Spinner spTimeSlot;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.layout_edit_shedule_fragment,container,false);

        Bundle bundle = getArguments();
        selectedSechedule = bundle.getInt("selectedSechedule");
        sFromDate  = bundle.getString("fromdate");
        sToDate = bundle.getString("todate");

        mUtil = new Util();
        mUserId = Integer.parseInt(mUtil.getString(StringConstants.USERID));

        initView(v);

        initCalender();


        ArrayAdapter<String> spTimeslotsList = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item,timeslots);
        spTimeSlot.setAdapter(spTimeslotsList);
        spTimeSlot.setPrompt("Time Slot");



        return v;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.edit_shedule_shedule_tv:
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                SheduleFragment sheduleFragment = new SheduleFragment();
                fragmentTransaction.replace(R.id.container,sheduleFragment);
                fragmentTransaction.commit();
                break;

            case R.id.edit_shedule_reshedule_tv:
                fromdate = year1+"-"+month1+"-"+day1;
                todate = year2+"-"+month2+"-"+day2;

                /** validation */


                    if (year1 == null & month1 == null & day1 == null) {
                        fromdate = sFromDate;
                    }

                    if (year2 == null & month2 == null & day2 == null) {
                        todate = sToDate;
                    }
                    if (spSelectedPosition == 0) {
                        Toast.makeText(getActivity(), "Please select time slot", Toast.LENGTH_SHORT).show();
                    } else {
                        Log.d("Lokesh", "userId,froomDate,toDate" + mUserId + "" + fromdate + " " + todate + " ");
                        mUtil.twoButtonAlertDialog(getActivity(),"Confirm message","Confirm Edit Schedule ? ",true,new MaterialDialog.ButtonCallback(){
                            @Override
                            public void onPositive(MaterialDialog dialog) {
                                mUtil.getBaseClassService(getActivity(), "").editScheduleDoctor(mUserId, fromdate,
                                        todate, String.valueOf(spSelectedPosition - 1), selectedSechedule, new Callback<JsonObject>() {
                                            @Override
                                            public void success(JsonObject jsonObject, Response response) {
                                                Log.d("Lokesh", response.toString());
                                                if (jsonObject.get(StringConstants.STATUS).getAsString().equalsIgnoreCase("Success")) {
                                                    mUtil.dismissDialog();
                                                    try {
                                                        callToast(jsonObject.get("message").getAsString());
                                                        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                                                        SheduleFragment sheduleFragment = new SheduleFragment();
                                                        fragmentTransaction.replace(R.id.container,sheduleFragment);
                                                        fragmentTransaction.commit();
                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                    }
                                                } else {
                                                    mUtil.dismissDialog();
                                                    try {
                                                        Toast.makeText(getActivity(), jsonObject.get("data").getAsString(), Toast.LENGTH_LONG).show();
                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                            }

                                            @Override
                                            public void failure(RetrofitError error) {
                                                mUtil.dismissDialog();
                                                mUtil.serviceCallFailermsg(error, getActivity());

                                                Log.d("Lokesh", error.toString());
                                            }
                                        });
                            }

                            @Override
                            public void onNegative(MaterialDialog dialog) {
                                dialog.dismiss();
                            }
                        });
                }
                break;
        }
    }

    private void initView(View v) {

        Calendar cal = Calendar.getInstance(TimeZone.getDefault());
        final DatePickerDialog datePicker = new DatePickerDialog(getActivity(),
                R.style.AppTheme, datePickerListener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH));


        datePicker.setCancelable(false);
        datePicker.setTitle("Select the date");
        datePicker.getDatePicker().setMinDate(System.currentTimeMillis()-1000);

        mReshedule = (Button)v.findViewById(R.id.edit_shedule_reshedule_tv);
        mReshedule.setOnClickListener(this);



        mFromDate = (EditText) v.findViewById(R.id.edit_shedule_fromdate_et);
        mFromDate.setHint(sFromDate);
        mFromDate.setInputType(InputType.TYPE_NULL);
        mFromDate.setTag("fromdate");
        mFromDate.setOnClickListener(this);
        mFromDate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                datePicker.show();
                selected  = "formdate";
                return false;
            }
        });

        mToDate = (EditText) v.findViewById(R.id.edit_shedule_todate_et);
        mToDate.setHint(sToDate);
        mToDate.setInputType(InputType.TYPE_NULL);
        mToDate.setTag("todate");
        mToDate.setOnClickListener(this);
        mToDate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                datePicker.show();
                selected = "todate";
                return false;

            }
        });

        spTimeSlot  = (Spinner) v.findViewById(R.id.edit_shedule_timeslot_et);
        spTimeSlot.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spSelectedPosition = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        timeslots.add(0,"Select time Slot");
        timeslots.add(1,"12:00 AM - 01:00 AM");timeslots.add(2,"01:00 AM - 02:00 AM");
        timeslots.add(3,"2:00 AM - 3:00 AM");timeslots.add(4,"3:00 AM - 4:00 AM");timeslots.add(5,"4:00 AM - 5:00 AM");
        timeslots.add(6,"5:00 AM - 6:00 AM");timeslots.add(7,"6:00 AM - 7:00 AM");timeslots.add(8,"7:00 AM - 8:00 AM");
        timeslots.add(9,"8:00 AM - 9:00 AM");timeslots.add(10,"9:00 AM - 10:00 AM");timeslots.add(11,"10:00 AM - 11:00 AM");
        timeslots.add(12,"11:00 AM - 12:00 PM");

        timeslots.add(13,"12:00 PM - 01:00 PM");timeslots.add(14,"01:00 PM - 02:00 PM");
        timeslots.add(15,"2:00 PM - 3:00 PM");timeslots.add(16,"3:00 PM - 4:00 PM");timeslots.add(17,"4:00 PM - 5:00 PM");
        timeslots.add(18,"5:00 PM - 6:00 PM");timeslots.add(19,"6:00 PM - 7:00 PM");timeslots.add(20,"7:00 PM - 8:00 PM");
        timeslots.add(21,"8:00 PM - 9:00 PM");timeslots.add(22,"9:00 PM - 10:00 PM");timeslots.add(23,"10:00 PM - 11:00 PM");
        timeslots.add(24,"11:00 PM - 12:00 AM");

    }

    private void initCalender() {
        final Calendar c = Calendar.getInstance();
        year  = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day   = c.get(Calendar.DAY_OF_MONTH);

//        Calendar now = Calendar.getInstance();
//        currentHour = now.get(Calendar.HOUR);
//
//        Log.d("Lokesh","time slot list is"+timeslots);
//        for (int i = 1; i <= currentHour ; i++) {             // when get the current hour 'list' is removed values upto current hour.
//            timeslots.remove(i);
//
//        }
//        Log.d("Lokesh","time slot list is after" +timeslots);


    }
    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {

        // when dialog box is closed, below method will be called.
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {

            if(selected.equals("formdate")){
                year1 = String.valueOf(selectedYear);
                month1 = String.valueOf(selectedMonth + 1);
                day1 = String.valueOf(selectedDay);
                mFromDate.setText(day1 + "/" + month1 + "/" + year1);}

            if(selected.equals("todate")) {
                year2 = String.valueOf(selectedYear);
                month2 = String.valueOf(selectedMonth + 1);
                day2 = String.valueOf(selectedDay);

                if(Integer.valueOf(day1) <= selectedDay & Integer.valueOf(month1)-1 <= selectedMonth
                        & Integer.valueOf(year1) <=selectedYear  ) {
                    mToDate.setText(day2 + "/" + month2 + "/" + year2);
                }
                else {
                    callToast("Please select valid date");
                }
            }

        }
    };
 public void callToast(String s){
     Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
 }

}
