package mycompany.com.doctorapp.common_activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import mycompany.com.doctorapp.R;
import mycompany.com.doctorapp.patientmodule.HomeActivity;
import mycompany.com.doctorapp.utils.StringConstants;
import mycompany.com.doctorapp.utils.Util;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class SignupActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText etFirstName,etLastName,etEmail, etMobileNumber, etPassword, etConfirmPassword;
    private Button btSingup;
    private TextView tvSignin;
    private String sFirstName, sLastName, sEmail, sMobileNumber, sPassword, sConfirmPassword;
    private Util mUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        mUtil = new Util();
        mUtil.getSharedpreferences(this);
         inittializeFields();

    }

    private void inittializeFields() {
        etFirstName = (EditText)findViewById(R.id.activitysignup_firstname_et);
        etLastName = (EditText) findViewById(R.id.activitysignup_lastname_et);
        etEmail = (EditText) findViewById(R.id.activitysignup_emailid_et);
        etMobileNumber = (EditText) findViewById(R.id.activitysignup_mobilenumber_et);
        etPassword = (EditText) findViewById(R.id.activitylogin_password_et);
        etConfirmPassword = (EditText) findViewById(R.id.activitysignup_confirmpassword_et);

        tvSignin = (TextView) findViewById(R.id.activitysignup_singin);
        tvSignin.setOnClickListener(this);

        btSingup = (Button) findViewById(R.id.activitysignup_signup_tv);
        btSingup.setOnClickListener(this);
    }
    private void validateRegisterFields() {
        sFirstName = etFirstName.getText().toString();
        sLastName = etLastName.getText().toString();
        sEmail = etEmail.getText().toString();
        sMobileNumber = etMobileNumber.getText().toString();
        sPassword = etPassword.getText().toString();
        sConfirmPassword = etConfirmPassword.getText().toString();


        if (etFirstName.length() == 0 | etLastName.length() == 0 | etEmail.length() == 0 |
                etMobileNumber.length() == 0 | etPassword.length() == 0 | etConfirmPassword.length() == 0) {

            Toast.makeText(SignupActivity.this, "Please enter all credentials!", Toast.LENGTH_SHORT).show();
        } else if (!signupEmail(sEmail)) {
            Toast.makeText(SignupActivity.this, "Invalid email!", Toast.LENGTH_SHORT).show();
        } else if (!sPassword.equals(sConfirmPassword)) {
            Log.d("pssss", sPassword + " " + sConfirmPassword);
            Toast.makeText(SignupActivity.this, "Password does not match!", Toast.LENGTH_LONG).show();
        } else if (sMobileNumber.length() != 10) {
            Toast.makeText(SignupActivity.this, "Invalid mobile number!", Toast.LENGTH_LONG).show();
        } else {
            postRegistration();
        }
    }

    private boolean signupEmail(String email) {
        String emailPattern =
                "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile(emailPattern);
        Matcher matcher = pattern.matcher(email);

        return matcher.matches();
    }

    private void postRegistration() {
        if (mUtil.isNetworkAvailable(this)) {
            mUtil.showProgressDialog(this);
            mUtil.getBaseClassService(this,"").register(sFirstName, sLastName, sEmail, sPassword, sMobileNumber, new Callback<JsonObject>() {
                @Override
                public void success(JsonObject jsonObject, Response response) {
//                    Toast.makeText(SignupActivity.this, "response is"+response.getStatus()+jsonObject.get(StringConstants.STATUS).getAsString(), Toast.LENGTH_SHORT).show();
                    if(jsonObject.get(StringConstants.STATUS).getAsString().equalsIgnoreCase("success")){
                       Toast.makeText(SignupActivity.this, "Registration success!", Toast.LENGTH_SHORT).show();
                       JsonObject dataObject = jsonObject.getAsJsonObject(StringConstants.DATA);
                       mUtil.setString(StringConstants.USERID, dataObject.get(StringConstants.USERID).getAsString());
                       mUtil.dismissDialog();
                        Intent intent = new Intent(SignupActivity.this,HomeActivity.class);
                        intent.putExtra("activity","SignupActivity");
                        startActivity(intent);
                       finish();

                   }else {
                       mUtil.dismissDialog();
                       try {
                           Toast.makeText(SignupActivity.this, jsonObject.get(StringConstants.errorMessage).getAsString(), Toast.LENGTH_LONG).show();
                       } catch (Exception e) {
                           e.printStackTrace();
                       }
                   }
                }

                @Override
                public void failure(RetrofitError error) {
                    Log.e("SingupActivity",error.toString());
                    mUtil.dismissDialog();
                    mUtil.serviceCallFailermsg(error,SignupActivity.this);
                }
            });

        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.activitysignup_signup_tv:
                validateRegisterFields();
                break;

            case R.id.activitysignup_singin:
                finish();
                break;

        }
    }
}
