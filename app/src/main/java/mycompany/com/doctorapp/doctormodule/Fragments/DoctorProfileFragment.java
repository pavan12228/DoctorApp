package mycompany.com.doctorapp.doctormodule.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.util.ArrayList;
import java.util.List;

import mycompany.com.doctorapp.R;
import mycompany.com.doctorapp.utils.DoctorAppApplication;
import mycompany.com.doctorapp.utils.StringConstants;
import mycompany.com.doctorapp.utils.Util;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by santhu on 8/11/2016.
 */
public class DoctorProfileFragment extends Fragment implements View.OnClickListener {
    private TextView mEditProfile, tvDoctorName, tvQualification, tvExperience, tvFee, tvHopspitalName, tvLocation, tvLandMark;
    private ImageView ivProfilePic,ivHospitalPic;
    private Util mUtil;
    private int mUserId,id,experienceId;
    private DoctorAppApplication mApplication;

    private ImageLoader mImageLoader;
    private DisplayImageOptions logooptions;

    private List<String> specializationList;
    private List<String> sSpecializationId;

    private String spialization,stateName,qualification,mProfilePic;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_doctor_profile, container, false);
        mUtil = new Util();
        mUtil.getSharedpreferences(getActivity());
        mUserId= Integer.parseInt(mUtil.getString(StringConstants.USERID));
        initializeViews(view);

        logooptions = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.no_image_available)
                .showImageForEmptyUri(R.drawable.no_image_available)
                .displayer(new RoundedBitmapDisplayer((int) getResources().getDimension(R.dimen.margin_250)))
                .cacheOnDisk(true)
                .showImageOnFail(R.drawable.no_image_available)
                .cacheInMemory(true)
                .build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getActivity())
                .memoryCache(new LruMemoryCache(2 * 1024 * 1024))
                .defaultDisplayImageOptions(logooptions)
                .writeDebugLogs()
                .build();
        mImageLoader = ImageLoader.getInstance();
        mImageLoader.init(config);


        getDoctorProfile();


        return view;
    }

    private void getDoctorProfile() {
        if (mUtil.isNetworkAvailable(getActivity())) {
            mUtil.showProgressDialog(getActivity());
            Log.d("Lokesh","user id is:"+mUserId);
            mUtil.getBaseClassService(getActivity(), "").getDoctorDetailsDetails(mUserId, new Callback<JsonObject>() {
                @Override
                public void success(JsonObject jsonObject, Response response) {
                    if (jsonObject.get(StringConstants.STATUS).getAsString().equalsIgnoreCase("sucess")) {
                        mUtil.dismissDialog();
                        JsonObject dataObject = jsonObject.getAsJsonObject(StringConstants.DATA);

                        if(dataObject.get("first_name").getAsString() != null)
                        tvDoctorName.setText(dataObject.get("first_name").getAsString());

                        if(dataObject.get("dr_qualification").getAsString() != null) {
                            qualification = dataObject.get("dr_qualification").getAsString();
                            tvQualification.setText(qualification);
                        }

                        if(dataObject.get("specialization_name").getAsString() != null) {
                            spialization = dataObject.get("specialization_name").getAsString();
//                        id = Integer.parseInt(dataObject.get("specialization_id").getAsString());  //not in service
                            tvQualification.append(" " + spialization);
                        }

                        if(dataObject.get("address").getAsString() != null)
                        tvLandMark.setText(dataObject.get("address").getAsString());

                        if(dataObject.get("location").getAsString() != null)
                        tvLocation.setText(dataObject.get("location").getAsString());

                        if(dataObject.get("state_name").getAsString() != null) {
                            stateName = dataObject.get("state_name").getAsString();
                            tvLocation.append(stateName);
                        }

                        if(dataObject.get("experience").getAsString() != null) {
                            experienceId = Integer.parseInt(dataObject.get("experience").getAsString());
                            tvExperience.setText(String.valueOf(experienceId));
                            tvExperience.append("year experience ");
                        }

                        if(dataObject.get("fee").getAsString() != null)
                        tvFee.setText(dataObject.get("fee").getAsString());

                        if(dataObject.get("name").getAsString() != null)
                        tvHopspitalName.setText(dataObject.get("name").getAsString());

                        if( dataObject.get("photo_URL").getAsString() != null)
                        mImageLoader.displayImage(StringConstants.IMAGE_BASE_URL +
                                dataObject.get("photo_URL").getAsString(), ivProfilePic);

                        if(dataObject.get("photo_URL").getAsString() != null)
                        mProfilePic = StringConstants.IMAGE_BASE_URL +
                                dataObject.get("photo_URL").getAsString();

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
                    mUtil.serviceCallFailermsg(error,getActivity());

                }
            });
        }
    }

    private void initializeViews(View view) {
        mEditProfile = (TextView) view.findViewById(R.id.doctor_profile_editprofile_tv);
        mEditProfile.setOnClickListener(this);

        specializationList = new ArrayList<>();
        sSpecializationId = new ArrayList<>();

        tvDoctorName = (TextView) view.findViewById(R.id.fragment_doctor_profile_name_tv);
        tvQualification = (TextView) view.findViewById(R.id.fragment_doctor_profile_qualification);
        tvExperience = (TextView) view.findViewById(R.id.fragment_doctor_profile_experience);
        tvFee = (TextView) view.findViewById(R.id.fragment_doctor_profile_fee);
        tvHopspitalName = (TextView) view.findViewById(R.id.fragment_doctor_profile_hopitalname);
        tvLocation = (TextView) view.findViewById(R.id.fragment_doctor_profile_location);
        tvLandMark = (TextView) view.findViewById(R.id.fragment_doctor_profile_landmark);
        ivProfilePic = (ImageView) view.findViewById(R.id.doctor_profile_profilepic);
        ivHospitalPic = (ImageView) view.findViewById(R.id.doctor_profile_building_iv);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.doctor_profile_editprofile_tv:
                Bundle proflidata = new Bundle();
                proflidata.putString("name",tvDoctorName.getText().toString());
                proflidata.putString("qualification",qualification);
                proflidata.putString("specialization",spialization);
                proflidata.putInt("specialization_id",id+1);
                proflidata.putInt("experience",experienceId);
                proflidata.putString("fee",tvFee.getText().toString());
                proflidata.putString("hospital",tvHopspitalName.getText().toString());
                proflidata.putString("address",tvLandMark.getText().toString());
                proflidata.putString("profile_pic",mProfilePic);

                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                EditDoctorProfileFragment editDoctorProfileFragment = new EditDoctorProfileFragment();
                editDoctorProfileFragment.setArguments(proflidata);
                fragmentTransaction.replace(R.id.container, editDoctorProfileFragment);
                fragmentTransaction.commit();
                break;

        }
    }



    public void callToast(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }
}
