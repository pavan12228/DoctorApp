package mycompany.com.doctorapp.doctormodule.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import mycompany.com.doctorapp.R;
import mycompany.com.doctorapp.doctormodule.adapters.SheduleAdapter;
import mycompany.com.doctorapp.doctormodule.models.SheduleModel;
import mycompany.com.doctorapp.utils.StringConstants;
import mycompany.com.doctorapp.utils.Util;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by santhu on 8/13/2016.
 */
public class SheduleFragment extends Fragment implements View.OnClickListener {
    SheduleAdapter sheduleAdapter;
    private ListView mListview;
    private TextView mAddSchedule;
    List<SheduleModel> sheduleModelList ;
    Util mUtil;
    private int mUserId;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.layout_shedule_fragment,container,false);
        initializeViews(v);
        mUtil = new Util();
        mUtil.getSharedpreferences(getActivity());
        mUserId = Integer.parseInt(mUtil.getString(StringConstants.USERID));
        getScheduleDoctor();

        Log.d("Lokesh","size is after method ::"+sheduleModelList.size());
//                    sheduleModelList.get(1).getFromDate()+" "+sheduleModelList.get(1).getFromTime());




        return v;
    }

    private void getScheduleDoctor() {
        if (Util.isNetworkAvailable(getActivity())) {
            mUtil.showProgressDialog(getActivity());
            mUtil.getBaseClassService(getActivity(), "").getDoctorSchedule(mUserId,new Callback<JsonObject>() {
                @Override
                public void success(JsonObject jsonObject, Response response) {
                    if (jsonObject.get(StringConstants.STATUS).getAsString().equalsIgnoreCase("Success")) {
                        mUtil.dismissDialog();
                        JsonArray jsonArray =jsonObject.get("data").getAsJsonArray();
                        for (int i = 0; i < jsonArray.size(); i++) {
                            SheduleModel sheduleModel = new SheduleModel();
                            JsonObject object = jsonArray.get(i).getAsJsonObject();
                            sheduleModel.setFromDate(object.get("min_slot").getAsString());
                            sheduleModel.setToDate(object.get("max_slot").getAsString());
                            sheduleModel.setBatch_id(object.get("batchid").getAsString());
                            ArrayList<String> stringArrayList = new ArrayList<String>();
                            JsonArray jsonArray1 = object.get("timeSlots").getAsJsonArray();
                            Log.d("array size",jsonArray1.size()+" size of jsonarray");
                            for (int i1 = 0; i1 < jsonArray1.size(); i1++) {
                                JsonElement object1 = jsonArray1.get(i1);
                                stringArrayList.add(object1.getAsString());
                                Log.d("Lokesh", "success() returned: " +object1.getAsString()
                                        +" and size is"+jsonArray1.size());
                            }
                            sheduleModel.setTimeSlots(stringArrayList);
                            sheduleModelList.add(sheduleModel);
                            Log.d("Lokesh",sheduleModelList.size()+" in looop");


                        }
                    } else {
                        mUtil.dismissDialog();
                        try {
                            Toast.makeText(getActivity(), jsonObject.get("data").getAsString(), Toast.LENGTH_LONG).show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    Log.d("Lokesh",sheduleModelList.size()+" in looop down");
                    sheduleAdapter = new SheduleAdapter(getActivity(),sheduleModelList,getFragmentManager());
                    mListview.setAdapter(sheduleAdapter);

                }

                @Override
                public void failure(RetrofitError error) {
                    mUtil.dismissDialog();
                    mUtil.serviceCallFailermsg(error,getActivity());
                    Log.d("Lokesh",error.toString());
                }
            });

        }
    }



    private void initializeViews(View v) {
        mListview = (ListView)v.findViewById(R.id.shedule_fragment_listview);

        mAddSchedule = (TextView)v.findViewById(R.id.shedule_fragment_addshedule_tv);
        mAddSchedule.setOnClickListener(this);
        sheduleModelList = new ArrayList<>();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.shedule_fragment_addshedule_tv:
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
//                EditSheduleFragment editSheduleFragment = new EditSheduleFragment();
                AddScheduleFragment addScheduleFragment = new AddScheduleFragment();
                fragmentTransaction.replace(R.id.container,addScheduleFragment);
                fragmentTransaction.commit();
        }
    }

    private void callToast(String s) {
        Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
    }
}
