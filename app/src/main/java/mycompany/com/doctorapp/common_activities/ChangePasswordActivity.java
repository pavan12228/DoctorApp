package mycompany.com.doctorapp.common_activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;

import mycompany.com.doctorapp.R;
import mycompany.com.doctorapp.utils.StringConstants;
import mycompany.com.doctorapp.utils.Util;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ChangePasswordActivity extends Activity implements View.OnClickListener {
    private ImageView mMenuToolbar,mBackToolbar;
    private TextView mTitleToolbar,tvSubmit;
    private EditText etOld,etNew,etConfirm;
    Util mUtil;
    private String sOld,sNew,sConfirm,type;
    private int userId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        mUtil = new Util();
        mUtil.getSharedpreferences(this);
        userId = Integer.parseInt(mUtil.getString(StringConstants.USERID));
        initializeViews();

        Intent intent = getIntent();
        type =intent.getStringExtra("type");

    }

//    private void doctorChangePassword() {
//        if (mUtil.isNetworkAvailable(this)) {
//            mUtil.showProgressDialog(this);
//            mUtil.getBaseClassService(this, "").patientChangePassword(userId, sOld, sNew, 2, new Callback<JsonObject>() {
//                @Override
//                public void success(JsonObject jsonObject, Response response) {
//                    if (jsonObject.get(StringConstants.STATUS).getAsString().equalsIgnoreCase("success")) {
//                        mUtil.dismissDialog();
//                        Toast.makeText(ChangePasswordActivity.this, jsonObject.get("message").getAsString(), Toast.LENGTH_SHORT).show();
//                    } else {
//                        mUtil.dismissDialog();
//                        try {
//                            Toast.makeText(ChangePasswordActivity.this, jsonObject.get("message").getAsString() + "!!", Toast.LENGTH_LONG).show();
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }
//
//                @Override
//                public void failure(RetrofitError error) {
//                    mUtil.dismissDialog();
//                    mUtil.serviceCallFailermsg(error,ChangePasswordActivity.this);
//                }
//            });
//            }
//        }

    private void mChangePassword(int type) {
        if (mUtil.isNetworkAvailable(this)) {
            mUtil.showProgressDialog(this);
            mUtil.getBaseClassService(this, "").patientChangePassword(userId, sOld, sNew, type, new Callback<JsonObject>() {
                @Override
                public void success(JsonObject jsonObject, Response response) {
                    if (jsonObject.get(StringConstants.STATUS).getAsString().equalsIgnoreCase("success")) {
                        mUtil.dismissDialog();
                        Toast.makeText(ChangePasswordActivity.this, jsonObject.get("message").getAsString(), Toast.LENGTH_SHORT).show();
                    } else {
                        mUtil.dismissDialog();
                        try {
                            Toast.makeText(ChangePasswordActivity.this, jsonObject.get("message").getAsString()+"!!", Toast.LENGTH_LONG).show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    mUtil.dismissDialog();
                    mUtil.serviceCallFailermsg(error,ChangePasswordActivity.this);
                }
            });

        }
    }

    private void initializeViews() {
        mMenuToolbar = (ImageView)findViewById(R.id.toolbar_menu);
        mMenuToolbar.setVisibility(View.GONE);

        mBackToolbar = (ImageView)findViewById(R.id.commontoolbarbacktitlesinglemenu_back_iv);
        mBackToolbar.setOnClickListener(this);

        mTitleToolbar = (TextView)findViewById(R.id.toolbar_title);
        mTitleToolbar.setText("Change Password");

        etOld = (EditText) findViewById(R.id.change_password_old_et);
        etNew = (EditText) findViewById(R.id.change_password_new_et);
        etConfirm = (EditText) findViewById(R.id.change_password_confirm);

        tvSubmit = (TextView) findViewById(R.id.change_password_submit);
        tvSubmit.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.commontoolbarbacktitlesinglemenu_back_iv:
                finish();
                break;
            case R.id.change_password_submit:
                validateFields();
                break;
        }
    }

    private void validateFields() {
        sOld = etOld.getText().toString();
        sNew = etNew.getText().toString();
        sConfirm = etConfirm.getText().toString();

        if(etOld.length() ==0 | etNew.length() == 0 | etConfirm.length() == 0){
            callToast("Enter all fields !");
        }else  if(etNew.equals(etConfirm)){
            Log.e("Lokesh",etNew.getText().toString()+" "+etConfirm.getText().toString());
            callToast("Password is not matched!");
        }else {
            if(type.equals("doctor")){
                mChangePassword(2);
            }else if(type.equals("patient")){
                mChangePassword(1);
            }

        }

    }

    public void callToast(String msg){
        Toast.makeText(ChangePasswordActivity.this, msg, Toast.LENGTH_SHORT).show();
    }
}
