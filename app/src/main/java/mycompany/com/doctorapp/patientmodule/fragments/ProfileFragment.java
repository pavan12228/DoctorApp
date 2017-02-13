package mycompany.com.doctorapp.patientmodule.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import mycompany.com.doctorapp.R;
import mycompany.com.doctorapp.utils.StringConstants;
import mycompany.com.doctorapp.utils.Util;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Dell on 8/10/2016.
 */
public class ProfileFragment extends Fragment implements View.OnClickListener, TextWatcher {


    private ImageView ivEditFirstName,ivEditSecondName,ivEditMobile, ivEditMail;

    private TextView ivUpdate,tvFirstName,tvLastName,tvMobile,tvEmail,tvCountry,tvCity;

    private EditText etFirstName,etLastName,etMobile,etMail;
    private String sFirstName,sLastName,sMobileNumber,sEmail;


    private Util mUtil;
    private int mUserId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile,null);
        mUtil = new Util();
        mUtil.getSharedpreferences(getActivity());
        mUserId = Integer.parseInt(mUtil.getString(StringConstants.USERID));
        initializeViews(view);
        getProfile();
        return  view;
    }

    private void getProfile() {
        if (mUtil.isNetworkAvailable(getActivity())) {
            mUtil.showProgressDialog(getActivity());
            mUtil.getBaseClassService(getActivity(), "").getPatientProfile(mUserId , new Callback<JsonObject>() {
                @Override
                public void success(JsonObject jsonObject, Response response) {
                    Log.e("Lokesh","userdi is "+mUserId);
                    if(jsonObject.get(StringConstants.STATUS).getAsString().equalsIgnoreCase("success")
                            | jsonObject.get(StringConstants.STATUS).getAsString().equalsIgnoreCase("sucess")) {
                        mUtil.dismissDialog();
                        JsonObject dataObject = jsonObject.getAsJsonObject(StringConstants.DATA);
                        tvFirstName.setText(dataObject.get("first_name").getAsString());
//                        sFirstName = dataObject.get("first_name").getAsString();
                        try {
                            tvLastName.setText(dataObject.get("last_name").getAsString());
//                            sLastName = dataObject.get("last_name").getAsString();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        tvMobile.setText(dataObject.get("mobile_no").getAsString());
//                        sMobileNumber = dataObject.get("mobile_no").getAsString();

                        tvEmail.setText(dataObject.get("email_id").getAsString());
                    /*    tvCountry.setText(dataObject.get("state_name").getAsString());
                        tvCity.setText(dataObject.get("street_name").getAsString());*/
                    }else {
                        mUtil.dismissDialog();
                        try {
                            Toast.makeText(getActivity(), jsonObject.get(StringConstants.errorMessage).getAsString(), Toast.LENGTH_LONG).show();
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
    private void postProfile() {
        if (mUtil.isNetworkAvailable(getActivity())) {
            mUtil.showProgressDialog(getActivity());
            mUtil.getBaseClassService(getActivity(), "").postPatientProfile(mUserId,sFirstName,
                    sLastName, sMobileNumber, new Callback<JsonObject>() {
                @Override
                public void success(JsonObject jsonObject, Response response) {
                    if(jsonObject.get(StringConstants.STATUS).getAsString().equalsIgnoreCase("success")){
                        mUtil.dismissDialog();
                        Toast.makeText(getActivity(), "Update success!", Toast.LENGTH_SHORT).show();
                        getProfile();
                    }else {
                        mUtil.dismissDialog();
                        try {
                            Toast.makeText(getActivity(), jsonObject.get(StringConstants.errorMessage).getAsString(), Toast.LENGTH_SHORT).show();
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
        etFirstName = (EditText) view.findViewById(R.id.fragment_pprofile_firstname_et);
        etFirstName.addTextChangedListener(this);

        etLastName = (EditText)view.findViewById(R.id.fragment_pprofile_lastname_et);
        etLastName.addTextChangedListener(this);

        etMobile = (EditText) view.findViewById(R.id.fragment_pprofile_mobile_et);
        etMobile.addTextChangedListener(this);

//        etMail = (EditText) view.findViewById(R.id.fragment_pprofile_mail_et);
//        etMail.addTextChangedListener(this);


        ivEditFirstName = (ImageView)view.findViewById(R.id.fragment_pprofile_firstname_edit_ic);
        ivEditFirstName.setOnClickListener(this);

        ivEditSecondName = (ImageView)view.findViewById(R.id.fragment_pprofile_lastname_edit_ic);
        ivEditSecondName.setOnClickListener(this);


        ivEditMobile = (ImageView)view.findViewById(R.id.fragment_pprofile_mobile_edit_ic);
        ivEditMobile.setOnClickListener(this);

//        ivEditMail = (ImageView)view.findViewById(R.id.fragment_pprofile_mail_edit_ic);
//        ivEditMail.setOnClickListener(this);

        ivUpdate = (TextView) view.findViewById(R.id.fragmentprofile_update_tv);
        ivUpdate.setOnClickListener(this);

        tvFirstName = (TextView) view.findViewById(R.id.fragment_pprofile_firstname_tv);
        tvLastName = (TextView) view.findViewById(R.id.fragment_pprofile_lastname_tv);
        tvMobile = (TextView) view.findViewById(R.id.fragment_pprofile_mobile_tv);
        tvEmail = (TextView) view.findViewById(R.id.fragment_pprofile_mailaddress_tv);




    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.fragment_pprofile_firstname_et:
                etFirstName.setEnabled(true);
                etFirstName.requestFocus();
                break;
            case R.id.fragment_pprofile_lastname_et:
                etLastName.requestFocus();
                break;


            case R.id.fragmentprofile_update_tv:
                validateFields();
                break;


            case R.id.fragment_pprofile_firstname_edit_ic:
                   etFirstName.requestFocus();
                break;
            case R.id.fragment_pprofile_lastname_edit_ic:
                    etLastName.requestFocus();
                break;
            case R.id.fragment_pprofile_mobile_edit_ic:
                etMobile.requestFocus();
                break;
//            case R.id.fragment_pprofile_mail_edit_ic:
//                etMail.requestFocus();
//                break;

        }


    }

    private void validateFields() {
        if(etFirstName.length() != 0) {
            sFirstName = etFirstName.getText().toString();
        }else
        sFirstName = tvFirstName.getText().toString();
        if(etLastName.length() !=0)
        sLastName = etLastName.getText().toString();
        else
        sLastName = tvLastName.getText().toString();

        if(etMobile.length() != 0)
        sMobileNumber = etMobile.getText().toString();
        else
        sMobileNumber = tvMobile.getText().toString();



        /*if (etFirstName.length() == 0 | etLastName.length() ==0 | etMail.length() == 0 | etMobile.length() == 0 ) {
            Toast.makeText(getActivity(), "Please enter all credentials!", Toast.LENGTH_SHORT).show();
        } else*//* if (!signupEmail(sEmail)) {
            Toast.makeText(getActivity(), "Invalid email!", Toast.LENGTH_SHORT).show();
        } else */
        if (sMobileNumber.length() != 10 && sMobileNumber.length() != 0) {
            Toast.makeText(getActivity(), "Invalid mobile number!", Toast.LENGTH_LONG).show();
        } else {
            postProfile();
        }

    }


    // *******following 3 methods for entering edittext for enable/disable update button.********

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    if(etFirstName.getText().length() != 0 | etLastName.getText().length() !=0 |
            etMobile.getText().length() != 0 /*| etMail.getText().length() != 0 */   |   i != 0 )
        ivUpdate.setVisibility(View.VISIBLE);
        else
        ivUpdate.setVisibility(View.INVISIBLE);
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    private boolean signupEmail(String email) {
        String emailPattern =
                "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile(emailPattern);
        Matcher matcher = pattern.matcher(email);

        return matcher.matches();
    }
}
