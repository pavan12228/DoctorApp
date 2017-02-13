package mycompany.com.doctorapp.doctormodule.Fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import mycompany.com.doctorapp.R;
import mycompany.com.doctorapp.utils.DoctorAppApplication;
import mycompany.com.doctorapp.utils.StringConstants;
import mycompany.com.doctorapp.utils.Util;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.MultipartTypedOutput;
import retrofit.mime.TypedFile;
import retrofit.mime.TypedString;

/**
 * Created by sciens1 on 8/13/2016.
 */
public class EditDoctorProfileFragment extends Fragment implements View.OnClickListener {
    private DoctorAppApplication application;
    private ImageLoader mImageLoader;
    private Button mUpdate, mCamera,mGallery;
    private Util mUtil;
    private EditText etName,etExperience,etFee,etHospitalName,etAddress,etQualification;
    private Spinner spSpecialization;
    private String sName,sExperience,sFee,sHospitalName,sAddress,sQualification,sProfilePic,
    name,qualification,specialization,fee,hopiatal,address;
    private ImageView ivProfilePic;
    List<String> specializationList;
    List<String> sSpecializationId;
    private int mSpecialization,mUserId,id,experience;
    private  boolean serviceDone = false;
    private File file;
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1888;
    private int PICK_IMAGE_REQUEST = 1;
    private   Bitmap bitmap;
    private Dialog editProfileDialog;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.layout_edit_doctor_profile_fragment,container,false);
        mUtil = new Util();
        mUtil.getSharedpreferences(getActivity());
        initializeViews(v);
        getSpecialization();

        Bundle profileData  = getArguments();

        name = profileData.getString("name");
        qualification = profileData.getString("qualification");
        specialization = profileData.getString("qualification");
        experience = profileData.getInt("experience");
        fee = profileData.getString("fee");
        hopiatal = profileData.getString("hospital");
        address = profileData.getString("address");
        id  = profileData.getInt("specialization_id");
        sProfilePic = profileData.getString("profile_pic");
        mImageLoader.displayImage(sProfilePic,ivProfilePic);


        Log.d("Lokesh",""+name+
                qualification+
                specialization+
                experience+
                fee+
                hopiatal+
                address);
        Log.d("Lokesh","specialization id is "+specializationList.indexOf(specialization));
        if(name != null) etName.setText(name);
        if(qualification != null) etQualification.setText(qualification);
            if(specialization != null) {
                selectValue(specializationList,specialization);
            }
                if(experience != 0) etExperience.setText(String.valueOf(experience));
                    if(fee != null) etFee.setText(fee);
                        if(hopiatal != null) etHospitalName.setText(hopiatal);
                            if(address != null) etAddress.setText(address);


        return v;
    }

    private void initializeViews(View v) {
        mUpdate = (Button)v.findViewById(R.id.edit_doctor_profile_update_button);
        mUpdate.setOnClickListener(this);

        specializationList = new ArrayList<>();
        sSpecializationId = new ArrayList<>();

        mUserId = Integer.parseInt(mUtil.getString(StringConstants.USERID));


        etName = (EditText) v.findViewById(R.id.edit_doctor_profile_name_et);
        etExperience    = (EditText) v.findViewById(R.id.edit_doctor_profile_experience_et);
        etFee    = (EditText) v.findViewById(R.id.edit_doctor_profile_fee_et);
        etHospitalName = (EditText) v.findViewById(R.id.edit_profile_hospitalname_et);
        etAddress = (EditText) v.findViewById(R.id.edit_doctor_profile_address);
        etQualification = (EditText) v.findViewById(R.id.edit_doctor_profile_qualification);
        spSpecialization = (Spinner) v.findViewById(R.id.edit_doctor_profile_dentist_spinner);
        ivProfilePic = (ImageView) v.findViewById(R.id.edit_doctor_profile_fragment_profile_pic);
        ivProfilePic.setOnClickListener(this);
        application = new DoctorAppApplication();
        mImageLoader = application.generalImageLoader;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.edit_doctor_profile_update_button:
               validateFields();
                break;
            case R.id.edit_doctor_profile_fragment_profile_pic:
                editProfileDialog = new Dialog(getActivity());
                editProfileDialog.setTitle("Select Option");
                editProfileDialog.setContentView(R.layout.select_image_options2);
                initDialogView(editProfileDialog);
                editProfileDialog.setCancelable(true);
                editProfileDialog.show();
                break;

            case R.id.select_image_camera:
                Intent intentCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intentCamera, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
                break;
            case R.id.select_image_gallery:
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
                break;

        }
    }

    private void initDialogView(Dialog editProfileDialog) {
        mCamera = (Button) editProfileDialog.findViewById(R.id.select_image_camera);
        mCamera.setOnClickListener(this);

        mGallery = (Button) editProfileDialog.findViewById(R.id.select_image_gallery);
        mGallery.setOnClickListener(this);
    }

    private boolean validateFields() {
        boolean serviceCalled = false;
        sName = etName.getText().toString();
        sExperience = etExperience.getText().toString();
        sFee = etFee.getText().toString();
        sHospitalName = etHospitalName.getText().toString();
        sAddress = etAddress.getText().toString();
        sQualification = etQualification.getText().toString();

        if(etName.length() == 0 | etExperience.length() == 0| etFee.length() == 0|
                etHospitalName.length() == 0|etAddress.length() ==0|etQualification.length() ==0){
            callToast("Please enter all credentials!");
        }else if(!nameTest(sName)) {
                callToast("Enter valid name");
        }else if(!numberTest(sExperience)){
                callToast("Enter valid experience");
        } else if(!feeTest(sFee)){
                callToast("Enter valid fee");
        }else {
             postEditProfile();
        }
        return serviceCalled;
    }

    private boolean postEditProfile() {

        if (mUtil.isNetworkAvailable(getActivity())) {
            mUtil.showProgressDialog(getActivity());

            MultipartTypedOutput mto = new MultipartTypedOutput();
            mto.addPart("id", new TypedString(String.valueOf(mUserId)));
            mto.addPart("doctor_name", new TypedString(sName));
            mto.addPart("specialization", new TypedString(""+mSpecialization));
            mto.addPart("experience", new TypedString(sExperience));
            mto.addPart("fee", new TypedString(sFee));
            mto.addPart("Name_of_the_hospital", new TypedString(sHospitalName));
            mto.addPart("hospital_address", new TypedString(sAddress));
            mto.addPart("qualification", new TypedString(sQualification));
                if(file != null)
                mto.addPart("photo_URL", new TypedFile("image/*", file));

            mUtil.getBaseClassService(getActivity(), "").postDoctorEditProfile(mto,new Callback<JsonObject>() {
                @Override
                public void success(JsonObject jsonObject, Response response) {
                    if (jsonObject.get(StringConstants.STATUS).getAsString().equalsIgnoreCase("sucess") |
                            jsonObject.get(StringConstants.STATUS).getAsString().equalsIgnoreCase("success")) {
                        mUtil.dismissDialog();
                        Toast.makeText(getActivity(), "Success!", Toast.LENGTH_SHORT).show();
                        serviceDone = true;
                        JsonArray asJsonArray = jsonObject.getAsJsonArray(StringConstants.DATA);
                        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                        DoctorProfileFragment doctorProfileFragment = new DoctorProfileFragment();
                        fragmentTransaction.replace(R.id.container, doctorProfileFragment);
                        fragmentTransaction.commit();
                    } else {
                        mUtil.dismissDialog();
                        try {
                            Toast.makeText(getActivity(), jsonObject.get("errorMessages").getAsString(),
                                    Toast.LENGTH_LONG).show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    mUtil.dismissDialog();
                    Log.e("Lokesh", error.toString());
                  mUtil.serviceCallFailermsg(error,getActivity());
                }
            });
        }
        return serviceDone;
    }

    private void getSpecialization() {
        if (mUtil.isNetworkAvailable(getActivity())) {
            mUtil.showProgressDialog(getActivity());
            mUtil.getBaseClassService(getActivity(), "").getSpecilizations(new Callback<JsonObject>() {
                @Override
                public void success(JsonObject jsonObject, Response response) {
                    if (jsonObject.get(StringConstants.STATUS).getAsString().equalsIgnoreCase("sucess")) {
                        JsonArray jsonArray = jsonObject.getAsJsonArray(StringConstants.DATA);
                        mUtil.dismissDialog();
                        for (int i = 0; i < jsonArray.size(); i++) {
                            JsonObject object = jsonArray.get(i).getAsJsonObject();
                            specializationList.add(object.get("specialization_name").getAsString());
                            sSpecializationId.add(object.get("specialization_id").getAsString());
                        }

                        ArrayAdapter<String> specializationModelArrayAdapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_layout_item_big, specializationList);

                        spSpecialization.setAdapter(specializationModelArrayAdapter);
                        spSpecialization.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                mSpecialization = Integer.parseInt(sSpecializationId.get(i));
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });
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


    private void selectValue( List<String> sl, Object value) {
        Log.d("Lokesh","Selected values is "+sl);
        for (int i = 0; i < sl.size(); i++) {
            if (sl.get(i).equals(value)) {
                spSpecialization.setSelection(i+1);
                Log.d("Lokesh","Selected values is (after)"+i);
                break;
            }
        }
    }

    public void callToast(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }

            /** name test */
    private boolean nameTest(String name) {
        String namePattern = "^[a-zA-Z ]+$";

        Pattern pattern = Pattern.compile(namePattern);
        Matcher matcher = pattern.matcher(name);

        return matcher.matches();
    }

             /** 1-100 number test*/
    private boolean numberTest(String number) {
        String numberPattern =
                "^[0-9]*$";
        Pattern pattern = Pattern.compile(numberPattern);
        Matcher matcher = pattern.matcher(number);

        return matcher.matches();
    }
    private boolean feeTest(String number) {
        String numberPattern =
                "^[1-9]+[0-9]*$";
        Pattern pattern = Pattern.compile(numberPattern);
        Matcher matcher = pattern.matcher(number);

        return matcher.matches();
    }

    /** ^\d+\s[A-z]+\s[A-z]+  for address*/
    private boolean addressTest(String number) {
        String numberPattern =
                "^\\d+\\s[A-z]+\\s[A-z]+";
        Pattern pattern = Pattern.compile(numberPattern);
        Matcher matcher = pattern.matcher(number);

        return matcher.matches();
    }



    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {

                Bitmap bmp = (Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream stream = new ByteArrayOutputStream();

                if (bmp != null) {
                    bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
                }
                byte[] byteArray = stream.toByteArray();

                bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
                ivProfilePic.setImageBitmap(bitmap);
                convertToFile(bitmap,(int) System.currentTimeMillis());
                callToast("Image selected!");
                editProfileDialog.dismiss();



            }
        }
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();

            try {
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                ivProfilePic.setImageBitmap(bitmap);
                convertToFile(bitmap, (int) System.currentTimeMillis());
                callToast("Image selected!");
                editProfileDialog.dismiss();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }


    private void convertToFile(Bitmap bitmap, int i) {
        file = new File(getActivity().getCacheDir(), "file"+i);
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
        byte[] bitmapdata = bos.toByteArray();

//write the bytes in file
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            fos.write(bitmapdata);
            fos.flush();
            fos.close();


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}

