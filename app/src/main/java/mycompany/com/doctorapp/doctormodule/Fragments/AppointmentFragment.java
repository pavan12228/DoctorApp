package mycompany.com.doctorapp.doctormodule.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import mycompany.com.doctorapp.R;
import mycompany.com.doctorapp.doctormodule.adapters.AppointmentAdapter;
import mycompany.com.doctorapp.doctormodule.models.AppointmentModel;
import mycompany.com.doctorapp.utils.StringConstants;
import mycompany.com.doctorapp.utils.Util;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by santhu on 8/12/2016.
 */
public class AppointmentFragment extends Fragment {
    private ListView mListview;

    List<AppointmentModel> appointmentModelList ;
    Util mUtil;
    int mUserId;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.layout_appointment_fragment, container, false);
        mUtil = new Util();
        mUtil.getSharedpreferences(getActivity());
        mUserId = Integer.parseInt(mUtil.getString(StringConstants.USERID));

        appointmentModelList = new ArrayList<>();

        mListview = (ListView) v.findViewById(R.id.appointment_fragment_listview);
        mListview.setDivider(null);
        mListview.setDividerHeight(0);

        getAppointments();



        return v;
    }

    private void getAppointments() {
        if (mUtil.isNetworkAvailable(getActivity())) {
            mUtil.showProgressDialog(getActivity());
            mUtil.getBaseClassService(getActivity(), "").doctorAppointments(mUserId, new Callback<JsonObject>() {
                @Override
                public void success(JsonObject jsonObject, Response response) {
                    if(jsonObject.get(StringConstants.STATUS).getAsString().equalsIgnoreCase("sucess")) {
                        mUtil.dismissDialog();
                        JsonArray jsonArray= jsonObject.getAsJsonArray(StringConstants.DATA);
                        for (int i = 0; i < jsonArray.size(); i++) {
                            JsonObject object = jsonArray.get(i).getAsJsonObject();
                            AppointmentModel appointmentModel = new AppointmentModel();

                            appointmentModel.setEmail(object.get("email_id").getAsString());
                            appointmentModel.setMobile(object.get("mobile_no").getAsString());
                            appointmentModel.setTime(object.get("schedule_date").getAsString()+" "+
                                    object.get("schedule_time").getAsString());
                            appointmentModel.setAppointmentId(object.get("appointmentId").getAsString());
                            appointmentModel.setPatientId(object.get("patient_id").getAsString());
                            appointmentModel.setAppointmentStatus(object.get("appointmentStatus").getAsString());
                            appointmentModelList.add(appointmentModel);
                        }
                        AppointmentAdapter appointmentAdapter = new AppointmentAdapter(getActivity(), appointmentModelList);
                        mListview.setAdapter(appointmentAdapter);
                        appointmentAdapter.notifyDataSetChanged();


                    }
                    else {
                        mUtil.dismissDialog();
                        try {
                            callToast(jsonObject.get("data").getAsString());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    mUtil.dismissDialog();
                    mUtil.serviceCallFailermsg(error,getActivity());

                }
            });
        }
    }

    public  void callToast(String msg){
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }
}


