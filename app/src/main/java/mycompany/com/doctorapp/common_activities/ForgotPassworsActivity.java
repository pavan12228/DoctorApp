package mycompany.com.doctorapp.common_activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.JsonObject;

import mycompany.com.doctorapp.R;
import mycompany.com.doctorapp.utils.StringConstants;
import mycompany.com.doctorapp.utils.Util;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ForgotPassworsActivity extends Activity {
    private Button mSubmit;
    private ImageView mCancel;
    private String type=null;
    private Util mUtil;
    private EditText etEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_passwors);


        etEmail = (EditText) findViewById(R.id.activity_forgot_email_et);
        Intent intent = getIntent();
        type = intent.getStringExtra("type");
        mUtil = new Util();


        mSubmit = (Button)findViewById(R.id.forgot_activity_submit_button);
        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(type.equals("doctor")){
                    postForgot(2);
                }else if(type.equals("patient")){
                    postForgot(1);
                }
            }
        });

        mCancel = (ImageView)findViewById(R.id.forgot_cancel_button);
        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void postForgot(int type) {
        if (Util.isNetworkAvailable(this)) {
            mUtil.showProgressDialog(this);
            mUtil.getBaseClassService(this, "").forgotPassword(type,etEmail.getText().toString(),
                    new Callback<JsonObject>() {
                @Override
                public void success(JsonObject jsonObject, Response response) {
                    if(jsonObject.get(StringConstants.STATUS).getAsString().equalsIgnoreCase("success")){
                        mUtil.dismissDialog();
                       callToast(jsonObject.get("message").getAsString());
                        finish();
                    }else {
                        mUtil.dismissDialog();
                        try {
                            callToast(jsonObject.get("message").getAsString());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        try {
                            callToast(jsonObject.get("errorMessages").getAsString());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    mUtil.dismissDialog();
                    mUtil.serviceCallFailermsg(error,ForgotPassworsActivity.this);
                }
            });
        }
    }

   public void callToast(String m){
       Toast.makeText(this, m, Toast.LENGTH_SHORT).show();
   }
}
