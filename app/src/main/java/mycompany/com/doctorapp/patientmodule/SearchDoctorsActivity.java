package mycompany.com.doctorapp.patientmodule;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import mycompany.com.doctorapp.R;
import mycompany.com.doctorapp.patientmodule.adapters.SearchDoctorsAdapter;
import mycompany.com.doctorapp.patientmodule.models.SearchDoctorModel;
import mycompany.com.doctorapp.utils.StringConstants;
import mycompany.com.doctorapp.utils.Util;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class SearchDoctorsActivity extends AppCompatActivity implements View.OnClickListener {
    ListView mListView;
    private ImageView mBack,mMenu;
    private TextView tvSearchDoctorTitle,tvSearchDoctorSubTitle;
    Util mUtil;
    private int mUserId,selectedField=0;
    List<SearchDoctorModel> searchDoctorModelList;
    private String hospitalname=null,districts=null,searchlocation=null,searchspecialistion=null,
            doctorname=null,speciality;
    public static Activity mClosedByPatientActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_doctors);
        mUtil = new Util();
        Util.getSharedpreferences(this);
        initilizeViews();
        mClosedByPatientActivity = this;
//        mUserId  = Integer.parseInt(mUtil.getString(StringConstants.USERID));

        Bundle bundle = getIntent().getBundleExtra("bundle");
        String s = bundle.getString("field1");
        String s1 = bundle.getString("field2");
        speciality = bundle.getString("speciality");
//        if (speciality != null) {
//            speciality = null;
//        }
        selectedField = bundle.getInt("selectedFields");

        if (s != null   |   s1 != null) {
            tvSearchDoctorTitle.setText(s+" "+s1);
//            tvSearchDoctorSubTitle.setText(s1);
            searchDoctor(s,s1);
        }
        if (speciality !=  null) {
            tvSearchDoctorTitle.setText(speciality);
            searchDoctor(speciality,null);
        }



    }


    private void searchDoctor(String s, String s1) {
        if (Util.isNetworkAvailable(getApplicationContext())) {
            if(selectedField ==1){
                searchspecialistion = s;
                searchlocation = s1;
            } else if(selectedField ==2){
                districts = s;
                searchlocation = s1;
            } else if(selectedField ==3){
                hospitalname = s;
                doctorname =s1;
            }else {
                searchspecialistion = speciality;
            }
            mUtil.getBaseClassService(getApplicationContext(), "").searchDoctor(hospitalname,
                    districts,searchlocation,searchspecialistion,doctorname,new Callback<JsonObject>() {
                @Override
                public void success(JsonObject jsonObject, Response response) {
                    if(jsonObject.get(StringConstants.STATUS).getAsString().equalsIgnoreCase("success")){
//                        mUtil.dismissDialog();

                        JsonObject obj= jsonObject.getAsJsonObject(StringConstants.DATA);
                        JsonArray jsonArray = obj.get("doctors_list").getAsJsonArray();
                        if(jsonArray.size() == 0){

                        }else {
                            for (int i = 0; i < jsonArray.size(); i++) {
                                SearchDoctorModel searchDoctorModel = new SearchDoctorModel();
                                JsonObject object = jsonArray.get(i).getAsJsonObject();
                                try {
                                    searchDoctorModel.setHospitol(object.get("name").getAsString());
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                try {
                                    searchDoctorModel.setName(object.get("first_name").getAsString());
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                try {
                                    searchDoctorModel.setFee(object.get("fee").getAsString());
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                try {
                                    searchDoctorModel.setAddress(object.get("location").getAsString());
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                try {
                                    searchDoctorModel.setDoctor_id(object.get("doctor_id").getAsString());
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                try {
                                    searchDoctorModel.setExperience(object.get("experience").getAsString());
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                try {
                                    searchDoctorModel.setQualification(object.get("dr_qualification").getAsString());
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                try {
                                    searchDoctorModel.setSpecialization(object.get("specialization_name").getAsString());
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                searchDoctorModelList.add(searchDoctorModel);
                            }
                            SearchDoctorsAdapter searchDoctorsAdapter = new SearchDoctorsAdapter(getApplicationContext(), searchDoctorModelList);
                            mListView.setAdapter(searchDoctorsAdapter);
                            mListView.setDivider(null);
                        }
                    }else {
                        mUtil.dismissDialog();
                        try {
                            Toast.makeText(getApplicationContext(), jsonObject.get("errorMessages").getAsString(), Toast.LENGTH_LONG).show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    mUtil.dismissDialog();
                    Log.e("Lokesh",error.toString());
                    mUtil.serviceCallFailermsg(error,SearchDoctorsActivity.this);

                }
            });
        }
    }


    private void initilizeViews() {
        mMenu = (ImageView) findViewById(R.id.menuicon_iv);
        mMenu.setVisibility(View.GONE);   /** menu is no need here, therefore its hidden in this activity*/

        mListView = (ListView) findViewById(R.id.activitysearchdoctors_listview);
        searchDoctorModelList = new ArrayList<>();

        mBack = (ImageView) findViewById(R.id.back_iv);
        mBack.setOnClickListener(this);

        tvSearchDoctorTitle = (TextView) findViewById(R.id.maintitle_tv);
        tvSearchDoctorSubTitle = (TextView) findViewById(R.id.subtitle_tv);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back_iv:
                mBack.setBackgroundColor(getResources().getColor(R.color.backclicked));
                finish();
                break;
        }
    }



    void callToast(String msg){
        Toast.makeText(SearchDoctorsActivity.this, msg, Toast.LENGTH_SHORT).show();
    }
}
