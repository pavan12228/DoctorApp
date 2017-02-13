package mycompany.com.doctorapp.patientmodule.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import mycompany.com.doctorapp.R;
import mycompany.com.doctorapp.patientmodule.adapters.AppointmentsAdapter;
import mycompany.com.doctorapp.patientmodule.models.AppointmentsModel;
import mycompany.com.doctorapp.utils.StringConstants;
import mycompany.com.doctorapp.utils.Util;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Dell on 8/11/2016.
 */
public class AppointmentsFragment extends Fragment  {
    ListView mListView;
    List<AppointmentsModel> mAppointmentsModelList;
    private Util mUtil;
    private int mUserId;

    AppointmentsModel appointmentsModel = new AppointmentsModel();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_appointments, container, false);
        mUtil = new Util();
        mUtil.getSharedpreferences(getActivity());
        mUserId = Integer.parseInt(mUtil.getString(StringConstants.USERID));
        initializeViews(view);
        getAppointments();


        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });

        return view;
    }


    private void getAppointments() {
        if (mUtil.isNetworkAvailable(getActivity())) {
            mUtil.showProgressDialog(getActivity());
            mUtil.getBaseClassService(getActivity(), "").getPatientAppointments(mUserId, new Callback<JsonObject>() {
                @Override
                public void success(JsonObject jsonObject, Response response) {
                    if (jsonObject.get(StringConstants.STATUS).getAsString().equalsIgnoreCase("success")
                            | jsonObject.get(StringConstants.STATUS).getAsString().equalsIgnoreCase("sucess")) {

                        mUtil.dismissDialog();
                        JsonArray jsonArray =  jsonObject.getAsJsonArray(StringConstants.DATA);
                        for (int i = 0; i < jsonArray.size(); i++) {
                                AppointmentsModel appointmentsModel = new AppointmentsModel();
                                JsonObject object = jsonArray.get(i).getAsJsonObject();

                            appointmentsModel.setName(object.get("doctor_name").getAsString());
                            appointmentsModel.setDoctorId(object.get("doctor_id").getAsString());
                            appointmentsModel.setDoctorQualification(object.get("dr_qualification").getAsString());
                            appointmentsModel.setDoctorSpecialization(object.get("specialization_name").getAsString());
                            appointmentsModel.setmExperience(object.get("experience").getAsString());
                            appointmentsModel.setDate(object.get("schedule_date").getAsString()+" "
                                    +object.get("schedule_time").getAsString());
                            appointmentsModel.setDateOnly(object.get("schedule_date").getAsString());
                            appointmentsModel.setTimeOnly(object.get("schedule_time").getAsString());

                            appointmentsModel.setAppointmentId(object.get("appointmentId").getAsString());
                            mAppointmentsModelList.add(appointmentsModel);
                        }
//                        getDoctorDetails();
                        AppointmentsAdapter appointmentsAdapter = new AppointmentsAdapter(getContext(), mAppointmentsModelList);
                        Log.e("Lokesh",""+mAppointmentsModelList);
                        appointmentsAdapter.notifyDataSetChanged();
                        mListView.setAdapter(appointmentsAdapter);
                        mListView.setDivider(null);


                    } else {
                        mUtil.dismissDialog();
                        try {
                       callToast(jsonObject.get(StringConstants.errorMessage).getAsString());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                }

                @Override
                public void failure(RetrofitError error) {
                    mUtil.serviceCallFailermsg(error,getActivity());
                }
            });
        }
    }

    private void initializeViews(View view) {
        mListView = (ListView) view.findViewById(R.id.fragmentappointments_listview);
        mAppointmentsModelList = new ArrayList<>();
    }
    public void callToast(String msg){
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }
}
