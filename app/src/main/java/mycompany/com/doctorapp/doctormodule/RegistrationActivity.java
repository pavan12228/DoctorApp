package mycompany.com.doctorapp.doctormodule;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

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
import mycompany.com.doctorapp.utils.StringConstants;
import mycompany.com.doctorapp.utils.Util;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.MultipartTypedOutput;
import retrofit.mime.TypedFile;
import retrofit.mime.TypedString;

public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener {

    EditText etRegisterNo, etHospitalName, etHospitalAddress, etName,
            etExperience,etQualificatoin, etEmailId, etPhoneNumber, etFreeAmount, etPassword,
            etConfirmPassword, etDesignationHospDoc, etLocation;

    Spinner spDoctorSpecialization, spCity, spDistrict,
            spState;
    Button btUploadProImage, btAddProfileImage,
            btUploadHospImage, btAddHospImage,
            btSignUp, btCamera, btGallery;
    TextView tvSignIn;
    String sRegisterNo, sHospitalName, sHospitalAddress, sName,
            sExperience,sQualificatoin, sEmailId, sPhoneNumber, sFreeAmount, sPassword,
            sConfirmPassword, sDesignationHospDoc, sLocation;
    private ImageView mCloseSelectImagesLinear;
    private int PICK_IMAGE_REQUEST = 1;
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1888;
    Bitmap bitmap;
    Util mUtil;
    File file,fileProfile,fileHospital;
    List<File> files = new ArrayList<>();
    List<String> specializationList, stateList, districtList, cityList;
    List<String> sSpecializationId, sStateListId, sDistrictListId, sCityListId;
    private LinearLayout mSelectImageOptions;
    private LinearLayout mSelectImagesLinear;
    private int mSpecialization, mState, mDistrict, mCity, sSelectedDist;
    ArrayAdapter<String> cityAdapter;
    int imageSelection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        mUtil = new Util();
        mUtil.getSharedpreferences(this);
        initializeView();

        getSpecialization();
        getState();


    }


    private void initializeView() {
        specializationList = new ArrayList<>();
        cityList = new ArrayList<>();
        stateList = new ArrayList<>();
        districtList = new ArrayList<>();

        sSpecializationId = new ArrayList<>();
        sStateListId = new ArrayList<>();
        sDistrictListId = new ArrayList<>();
        sCityListId = new ArrayList<>();


        etRegisterNo = (EditText) findViewById(R.id.register_reg_no_et);
        etHospitalName = (EditText) findViewById(R.id.register_hospital_name_et);
        etHospitalAddress = (EditText) findViewById(R.id.register_hospital_address_et);
        etName = (EditText) findViewById(R.id.register_doctor_name_et);
        etExperience = (EditText) findViewById(R.id.register_experience_et);
        etQualificatoin = (EditText) findViewById(R.id.register_qualification_et);
        etEmailId = (EditText) findViewById(R.id.register_email_id_et);
        etPhoneNumber = (EditText) findViewById(R.id.register_phonenumber_et);
        etFreeAmount = (EditText) findViewById(R.id.register_freeamount_et);
        etPassword = (EditText) findViewById(R.id.register_password_et);
        etConfirmPassword = (EditText) findViewById(R.id.register_confirmpassword_et);
        etDesignationHospDoc = (EditText) findViewById(R.id.register_designation_hos_doc_et);
        etLocation = (EditText) findViewById(R.id.register_location_et);

        spDoctorSpecialization = (Spinner) findViewById(R.id.register_doctor_specialization_spinner);
        spCity = (Spinner) findViewById(R.id.register_city_spinner);
        spDistrict = (Spinner) findViewById(R.id.register_district_spinner);
        spState = (Spinner) findViewById(R.id.register_state_spinner);

        btUploadProImage = (Button) findViewById(R.id.register_upload_profile_image_bt);
        btUploadProImage.setOnClickListener(this);

        btAddProfileImage = (Button) findViewById(R.id.register_add_profile_image_bt);
        btAddProfileImage.setOnClickListener(this);

        btUploadHospImage = (Button) findViewById(R.id.register_upload_hosp_image_bt);
        btUploadHospImage.setOnClickListener(this);

        btAddHospImage = (Button) findViewById(R.id.register_add_hospital_image_bt);
        btAddHospImage.setOnClickListener(this);

        btSignUp = (Button) findViewById(R.id.register_signup_bt);
        btSignUp.setOnClickListener(this);

        tvSignIn = (TextView) findViewById(R.id.register_signin_tv);
        tvSignIn.setOnClickListener(this);


        mSelectImagesLinear = (LinearLayout) findViewById(R.id.select_images_linear);
        mSelectImagesLinear.setVisibility(View.GONE);

        mCloseSelectImagesLinear = (ImageView) findViewById(R.id.select_image_close);
        mCloseSelectImagesLinear.setOnClickListener(this);

        btCamera = (Button) findViewById(R.id.select_image_camera);
        btCamera.setOnClickListener(this);

        btGallery = (Button) findViewById(R.id.select_image_gallery);
        btGallery.setOnClickListener(this);

    }

    private void validateRegisterFields() {

        sRegisterNo = etRegisterNo.getText().toString();
        sHospitalName = etHospitalName.getText().toString();
        sHospitalAddress = etHospitalAddress.getText().toString();
        sName = etName.getText().toString();
        sExperience = etExperience.getText().toString();
        sQualificatoin = etQualificatoin.getText().toString();
        sEmailId = etEmailId.getText().toString();
        sPhoneNumber = etPhoneNumber.getText().toString();
        sFreeAmount = etFreeAmount.getText().toString();
        sPassword = etPassword.getText().toString();
        sConfirmPassword = etConfirmPassword.getText().toString();
        sDesignationHospDoc = etDesignationHospDoc.getText().toString();
        sLocation = etLocation.getText().toString();


        if (sRegisterNo.length() == 0 | sHospitalName.length() == 0 | sHospitalAddress.length() == 0 |
                sName.length() == 0 | sExperience.length() == 0 | sEmailId.length() == 0 |
                sPhoneNumber.length() == 0 | sFreeAmount.length() == 0 | sPassword.length() == 0 |
                sConfirmPassword.length() == 0 | sDesignationHospDoc.length() == 0 | sLocation.length() == 0) {
            Toast.makeText(RegistrationActivity.this, "Please enter all credentials!", Toast.LENGTH_SHORT).show();
        } else if (!signupEmail(sEmailId)) {
            Toast.makeText(RegistrationActivity.this, "Invalid email!", Toast.LENGTH_SHORT).show();
        } else if (!sPassword.equals(sConfirmPassword)) {
            Toast.makeText(RegistrationActivity.this, "Password does not match!", Toast.LENGTH_LONG).show();
        } else if (sPhoneNumber.length() != 10) {
            Toast.makeText(RegistrationActivity.this, "Invalid mobile number!", Toast.LENGTH_LONG).show();
        } else {
            postRegistration();
        }
    }

    private void getCity() {

        if (mUtil.isNetworkAvailable(this)) {
            mUtil.getBaseClassService(this, "").getCities(mDistrict, new Callback<JsonObject>() {
                @Override
                public void success(JsonObject jsonObject, Response response) {
                    if (jsonObject.get(StringConstants.STATUS).getAsString().equalsIgnoreCase("sucess")) {
                        JsonArray jsonArray = jsonObject.getAsJsonArray(StringConstants.DATA);
                        cityList.clear();
                        sCityListId.clear();
                        for (int i = 0; i < jsonArray.size(); i++) {
                            JsonObject object = jsonArray.get(i).getAsJsonObject();
                            cityList.add(object.get("street_name").getAsString());
                            sCityListId.add(object.get("street_id").getAsString());
                        }

                        cityAdapter = new ArrayAdapter<String>(RegistrationActivity.this, R.layout.spinner_layout_item, cityList);
                        spCity.setAdapter(cityAdapter);
                        spCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                mCity= Integer.parseInt(sCityListId.get(i));
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });
                        cityAdapter.notifyDataSetChanged();
                    } else {
                        try {
                            Toast.makeText(RegistrationActivity.this, jsonObject.get(StringConstants.errorMessage).getAsString(), Toast.LENGTH_LONG).show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    mUtil.dismissDialog();
                    mUtil.serviceCallFailermsg(error,RegistrationActivity.this);
                }
            });
        }
    }

    private void getDistrict() {

        if (mUtil.isNetworkAvailable(this)) {
            mUtil.getBaseClassService(this, "").getDistrict(mState, new Callback<JsonObject>() {
                @Override
                public void success(JsonObject jsonObject, Response response) {
                    if (jsonObject.get(StringConstants.STATUS).getAsString().equalsIgnoreCase("sucess")) {
                        JsonArray jsonArray = jsonObject.getAsJsonArray(StringConstants.DATA);
                        districtList.clear();
                        sDistrictListId.clear();
                        for (int i = 0; i < jsonArray.size(); i++) {
                            JsonObject object = jsonArray.get(i).getAsJsonObject();
                            districtList.add(object.get("district_name").getAsString());
                            sDistrictListId.add(object.get("district_id").getAsString());
                        }
                        ArrayAdapter<String> districtAdapter = new ArrayAdapter<String>(RegistrationActivity.this, R.layout.spinner_layout_item, districtList);
                        spDistrict.setAdapter(districtAdapter);
                        districtAdapter.notifyDataSetChanged();
                        spDistrict.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                mDistrict = Integer.parseInt(sDistrictListId.get(i));
                                getCity();
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {
                                callToast("You selected nothing!");
                            }
                        });
                    } else {
                        try {
                            Toast.makeText(RegistrationActivity.this, jsonObject.get(StringConstants.errorMessage).getAsString(), Toast.LENGTH_LONG).show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    mUtil.dismissDialog();
                    Toast.makeText(RegistrationActivity.this, "Retro Error!", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void getState() {
        if (mUtil.isNetworkAvailable(this)) {
            mUtil.showProgressDialog(this);
            mUtil.getBaseClassService(this, "").getStates(new Callback<JsonObject>() {
                @Override
                public void success(JsonObject jsonObject, Response response) {
                    if (jsonObject.get(StringConstants.STATUS).getAsString().equalsIgnoreCase("sucess")) {
                        JsonArray jsonArray = jsonObject.getAsJsonArray(StringConstants.DATA);
                        mUtil.dismissDialog();
                        stateList.clear();
                        for (int i = 0; i < jsonArray.size(); i++) {
                            JsonObject object = jsonArray.get(i).getAsJsonObject();
                            stateList.add(object.get("state_name").getAsString());
                            sStateListId.add(object.get("state_id").getAsString());
                        }
                        final ArrayAdapter<String> stateAdapter = new ArrayAdapter<String>(RegistrationActivity.this, R.layout.spinner_layout_item, stateList);
                        spState.setAdapter(stateAdapter);
                        spState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                mState = Integer.parseInt(sStateListId.get(i));
                                getDistrict();
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {
                                callToast("You selected Nothing");
                            }
                        });

                    } else {
                        mUtil.dismissDialog();
                        try {
                            Toast.makeText(RegistrationActivity.this, jsonObject.get(StringConstants.errorMessage).getAsString(), Toast.LENGTH_LONG).show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    Toast.makeText(RegistrationActivity.this, "Retro Error!", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void getSpecialization() {
        if (mUtil.isNetworkAvailable(this)) {
            mUtil.showProgressDialog(this);
            mUtil.getBaseClassService(this, "").getSpecilizations(new Callback<JsonObject>() {
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
                        ArrayAdapter<String> specializationModelArrayAdapter = new ArrayAdapter<String>(RegistrationActivity.this, R.layout.spinner_layout_item, specializationList);

                        spDoctorSpecialization.setAdapter(specializationModelArrayAdapter);
                        spDoctorSpecialization.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
                    callToast("Retro Error !");
                }
            });
        }
    }

    private void postRegistration() {
        if (mUtil.isNetworkAvailable(this)) {
            mUtil.showProgressDialog(this);

            MultipartTypedOutput mto = new MultipartTypedOutput();
            mto.addPart("doctor_name", new TypedString(sName));
            mto.addPart("hospital_regd_no", new TypedString(sRegisterNo));
            mto.addPart("Name_of_the_hospital", new TypedString(sHospitalName));
            mto.addPart("hospital_address", new TypedString(sHospitalAddress));
            mto.addPart("specialization", new TypedString( ""+mSpecialization));
            mto.addPart("experience", new TypedString(sExperience));
            mto.addPart("dr_qualification", new TypedString(sQualificatoin));
            mto.addPart("mobile", new TypedString(sPhoneNumber));
            mto.addPart("fee", new TypedString(sFreeAmount));
            mto.addPart("description", new TypedString(sName));
            mto.addPart("location", new TypedString(sLocation));
            mto.addPart("city", new TypedString(""+mCity));
            mto.addPart("district", new TypedString(""+mDistrict));
            mto.addPart("state", new TypedString(""+mState));
            mto.addPart("email_id", new TypedString(sEmailId));
            mto.addPart("password", new TypedString(sPassword));
            mto.addPart("doctor_image", new TypedFile("image/*", fileProfile));
            mto.addPart("hospital_image", new TypedFile("image/*", fileHospital));


            mUtil.getBaseClassService(this, "").postDoctorRegisteraction(mto, new Callback<JsonObject>() {
                @Override
                public void success(JsonObject jsonObject, Response response) {
                    if (jsonObject.get(StringConstants.STATUS).getAsString().equalsIgnoreCase("success")) {
                        mUtil.dismissDialog();
                        callToast("Registration Successful !");
                        Log.d("Lokesh",response.toString());
                    } else {
                        mUtil.dismissDialog();
                        try {
                            callToast(jsonObject.get("errorMessage").getAsString());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        try {
                            callToast(jsonObject.get("message").getAsString());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }


                @Override
                public void failure(RetrofitError error) {
                    mUtil.serviceCallFailermsg(error,RegistrationActivity.this);
                }
            });
        }
    }

    private boolean signupEmail(String email) {
        String emailPattern =
                "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile(emailPattern);
        Matcher matcher = pattern.matcher(email);

        return matcher.matches();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
    //            case R.id.register_upload_profile_image_bt:
//                mSelectImagesLinear.setVisibility(View.VISIBLE);
////                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)mSelectImagesLinear.getLayoutParams();
////                params.addRule(RelativeLayout.ALIGN_PARENT_TOP, R.id.register_upload_profile_image_bt);
////                mSelectImagesLinear.setLayoutParams(params);

//                break;
            case R.id.register_add_profile_image_bt:
                mSelectImagesLinear.setVisibility(View.VISIBLE);
                imageSelection = 0;
                break;
//            case R.id.register_upload_hosp_image_bt:
//                mSelectImagesLinear.setVisibility(View.VISIBLE);
//                imageSelection = 1;
//                break;
            case R.id.register_add_hospital_image_bt:
                mSelectImagesLinear.setVisibility(View.VISIBLE);
                imageSelection = 1;
                break;
            case R.id.register_signup_bt:
                validateRegisterFields();

                break;
            case R.id.register_signin_tv:
                finish();
                break;
            case R.id.select_image_close:
                mSelectImagesLinear.setVisibility(View.GONE);
                break;


            case R.id.select_image_gallery:
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
                break;

            case R.id.select_image_camera:
                Intent intentCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intentCamera,
                        CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
                break;

        }

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

                bitmap = BitmapFactory.decodeByteArray(byteArray, 0,
                        byteArray.length);
                convertToFile(bitmap);
                mSelectImagesLinear.setVisibility(View.GONE);
                callToast("Image selected!");
            }
        }
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();

            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                convertToFile(bitmap);
                mSelectImagesLinear.setVisibility(View.GONE);
                callToast("Image selected!");


            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private void convertToFile(Bitmap bitmap) {
        file = new File(this.getCacheDir(), "file"+System.currentTimeMillis());
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
            if (imageSelection == 0){
                fileProfile = file;
            }else {
               fileHospital = file;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void callToast(String msg) {
        Toast.makeText(RegistrationActivity.this, msg, Toast.LENGTH_SHORT).show();
    }

    public void callLog(String msg1, String msg2) {
        Log.e(msg1, msg2);
    }


}
