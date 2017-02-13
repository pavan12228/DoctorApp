package mycompany.com.doctorapp.common_activities;

import android.Manifest;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.BuildConfig;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.LoggingBehavior;
import com.facebook.ProfileTracker;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.GooglePlayServicesAvailabilityIOException;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.ExponentialBackOff;
import com.google.api.services.gmail.GmailScopes;
import com.google.gson.JsonObject;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import mycompany.com.doctorapp.R;
import mycompany.com.doctorapp.doctormodule.DoctorHomeActivity;
import mycompany.com.doctorapp.doctormodule.RegistrationActivity;
import mycompany.com.doctorapp.patientmodule.HomeActivity;
import mycompany.com.doctorapp.utils.AbstractGetNameTask;
import mycompany.com.doctorapp.utils.GetNameInForeground;
import mycompany.com.doctorapp.utils.StringConstants;
import mycompany.com.doctorapp.utils.Util;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static mycompany.com.doctorapp.utils.StringConstants.USERID;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener,EasyPermissions.PermissionCallbacks {
    private TextView mForgot,tvSkip,tvFacebookLogin;
    private RadioButton mDoctorButton,mPatientButton;
    private EditText etEmail,etPassword;
    private String sName,sEmail,sPassword,mAcitvity,gmailAccountName;
    private Button btSignIn,btSignUp;
    private Util mUtil;
    private int mUserId;
    private LoginButton loginButton;
    private CallbackManager callbackManager;
    private AccessTokenTracker accessTokenTracker;
    private AccessToken accessToken;
    private ProfileTracker profileTracker;

    //********gmail****************//
   public GoogleAccountCredential mCredential;


    ProgressDialog mProgress;

    static final int REQUEST_ACCOUNT_PICKER = 1000;
    static final int REQUEST_AUTHORIZATION = 1001;
    static final int REQUEST_GOOGLE_PLAY_SERVICES = 1002;
    static final int REQUEST_PERMISSION_GET_ACCOUNTS = 1003;

    private static final String BUTTON_TEXT = "Call Gmail API";
    private static final String PREF_ACCOUNT_NAME = "accountName";
    private static final String[] SCOPES = {GmailScopes.GMAIL_LABELS};

    ImageLoader imageLoader;
    String SCOPE="oauth2:https://www.googleapis.com/auth/userinfo.profile";
    private ImageView imageView;
    private Button mGmailBt;
    private TextView mOutputText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mUtil = new Util();
        FacebookSdk.sdkInitialize(getApplicationContext());                                         //created for fb
        AppEventsLogger.activateApp(this);


        if (BuildConfig.DEBUG) {                                                                       //created for fb
            FacebookSdk.setIsDebugEnabled(true);                                                      //created for fb
            FacebookSdk.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS);                     //created for fb
        }                                     //created for fb

         accessToken = AccessToken.getCurrentAccessToken();                                          //created for fb
        callbackManager = CallbackManager.Factory.create();                                           //created for fb
        LoginManager loginManager = null;                                                           //created for fb
//        loginManager.logInWithReadPermissions(this, Arrays.asList("public_profile"));


        Util.getSharedpreferences(this);
        int mUserIdTemp = 0;
        if(!mUtil.getString(StringConstants.USERID).equals("")) {           /* checking user id exists are not*/
            mUserIdTemp = Integer.parseInt(mUtil.getString(StringConstants.USERID));
        }
        if(mUserIdTemp != 0){
            String mType = Util.getString("type");
            if(mType.equals("patient")){                /* if user id exists ,checking which type,then starts activity */
                startActivity(new Intent(LoginActivity.this,HomeActivity.class));
                finish();
            }else if(mType.equals("doctor")) {
                startActivity(new Intent(LoginActivity.this,DoctorHomeActivity.class));
                finish();
            }

        }
        Intent intent = getIntent();


        if (!intent.hasExtra("activity")) {
            mAcitvity = "LoginActivity";
        } else {
            mAcitvity = intent.getStringExtra("activity");
        }

        Log.d("mAcitvity", "onCreate() returned: " + "" + mAcitvity);


        initializeViews();
        Bundle bundle = intent.getExtras();
        if(bundle != null ) {
            String mSkip = intent.getStringExtra("skip");
            if(mSkip.equals("skipFromPatientTimeSlot"))
                tvSkip.setVisibility(View.GONE);
        }


        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(
                    AccessToken oldAccessToken,
                    AccessToken currentAccessToken) {          //created for fb
                accessToken = currentAccessToken;
            }
        };

        loginButton.setReadPermissions(Arrays.asList("email"/*, "user_friends", "email", "user_birthday"*/));//created for fb
//        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile"));
        loginButton.registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        // App code
                        Log.e("onSuccess", "--------" + loginResult.getAccessToken());
                        Log.e("Token", "--------" + loginResult.getAccessToken().getToken());
                        Log.e("Permision", "--------" + loginResult.getRecentlyGrantedPermissions());
//                        Profile profile1 = Profile.getCurrentProfile();    // not working herer
//                            callToast(profile1.getFirstName());           // not working herer ,work at graph request

//                        Log.e("Image URI", "--" + profile.getLinkUri());

                        Log.e("OnGraph", "------------------------");



                        callToast("facebook login success");



                        GraphRequest request = GraphRequest.newMeRequest(
                                loginResult.getAccessToken(),
                                new GraphRequest.GraphJSONObjectCallback() {
                                    @Override
                                    public void onCompleted(
                                            JSONObject object,
                                            GraphResponse response) {
                                        // Application code
                                        String emailFromFacebook = null;
                                        try {
                                             emailFromFacebook = object.getString("email");

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                        try {
                                            emailFromFacebook = object.getString("data");
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                        Log.d("Lokesh", "response is :"+response.toString() + " JosnObject is:"+object);



                                        if (mDoctorButton.isChecked()){
                                            getUserIdFromGmailServiceAndFaceBook(emailFromFacebook,"facebook","2");
                                        }else if (mPatientButton.isChecked()){
                                            getUserIdFromGmailServiceAndFaceBook(emailFromFacebook,"facebook","1");
                                        }
                                    }
                                });
                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "id,first_name,last_name,email");
                        request.setParameters(parameters);

                        request.executeAsync();
                    }

                    @Override
                    public void onCancel() {
                        // App code
                        callToast("facebook login cancel");

                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                        callToast("facebook login error");
                        Log.d("Lokesh","exception is :"+exception);

                    }
                });

        //********gmail****************//




        mGmailBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getResultsFromApi();
            }
        });
        mProgress = new ProgressDialog(this);
        mProgress.setMessage("Calling Gmail API ...");


        // Initialize credentials and service object.
        mCredential = GoogleAccountCredential.usingOAuth2(
                getApplicationContext(), Arrays.asList(SCOPE))
                .setBackOff(new ExponentialBackOff());




    }


//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) { \//created for fb
//        super.onActivityResult(requestCode, resultCode, data);
//        callbackManager.onActivityResult(requestCode, resultCode, data);
//    }

    @Override
    public void onDestroy() {                                     //created for fb
        super.onDestroy();
        accessTokenTracker.stopTracking();
    }








    private void initializeViews() {
        loginButton = (LoginButton) findViewById(R.id.login_button);

        mGmailBt = (Button) findViewById(R.id.activitylogin_loginbygmail_tv);                                              // for gmail

        etEmail = (EditText) findViewById(R.id.activitylogin_email_et);
        etPassword = (EditText) findViewById(R.id.activitylogin_password_et);

        btSignUp = (Button) findViewById(R.id.activitylogin_signup_bt);
        btSignUp.setOnClickListener(this);

        btSignIn = (Button) findViewById(R.id.activitylogin_siginin_bt);
        btSignIn.setOnClickListener(this);

        mDoctorButton = (RadioButton) findViewById(R.id.activitylogin_dlogin_left_rb);
        mDoctorButton.setOnClickListener(this);

        mPatientButton = (RadioButton) findViewById(R.id.activitylogin_plogin_right_rb);
        mPatientButton.setOnClickListener(this);

        mForgot = (TextView)findViewById(R.id.login_forgot_tv);
        mForgot.setOnClickListener(this);

        tvFacebookLogin = (TextView) findViewById(R.id.activitylogin_loginbyfb_tv);                                     //created for fb
        tvFacebookLogin.setOnClickListener(this);


        tvSkip = (TextView) findViewById(R.id.activity_login_skip_tv);
        tvSkip.setVisibility(View.VISIBLE);

        tvSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, HomeActivity.class));
            }
        });



        loginButton.setReadPermissions("email");                                     //created for fb
        // If using in a fragment

        // Other app specific specialization

        // Callback registration
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()){
            case R.id.activitylogin_siginin_bt:
                    validateLoginFields();
                break;

            case R.id.activitylogin_signup_bt:
                if (mDoctorButton.isChecked()){
                    Intent intent1 = new Intent(getApplicationContext(),RegistrationActivity.class);
                    startActivity(intent1);
                }else if (mPatientButton.isChecked()){
                    Intent intent1 = new Intent(getApplicationContext(),SignupActivity.class);
                    startActivity(intent1);
                }
                break;

            case R.id.activitylogin_dlogin_left_rb:
                break;

            case R.id.activitylogin_plogin_right_rb:
                break;

            case R.id.login_forgot_tv:
                if (mDoctorButton.isChecked()){
                    Intent forgotIntent = new Intent(LoginActivity.this,ForgotPassworsActivity.class);
                    forgotIntent.putExtra("type","doctor");
                    startActivity(forgotIntent);
                }else if (mPatientButton.isChecked()){
                    Intent forgotIntent = new Intent(LoginActivity.this,ForgotPassworsActivity.class);
                    forgotIntent.putExtra("type","patient");
                    startActivity(forgotIntent);
                }

                break;
            case R.id.activitylogin_loginbyfb_tv:                                     //created for fb
                loginButton.performClick();
                break;
        }
    }

    private void validateLoginFields() {
        sEmail = etEmail.getText().toString();
        sPassword = etPassword.getText().toString();

        if (  etEmail.length() == 0 | etPassword.length() == 0)
            Toast.makeText(LoginActivity.this, "Please enter all credentials!", Toast.LENGTH_SHORT).show();
        else
        if (!signupEmail(sEmail)) {
            Toast.makeText(LoginActivity.this, "Invalid email!", Toast.LENGTH_SHORT).show();
        }else
        {
            if (mDoctorButton.isChecked()){
                postDoctorLoginAction();
            }else if (mPatientButton.isChecked()){
                postPatientLoginAction();
            }

        }
    }



    private boolean signupEmail(String email) {
        String emailPattern =
                "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile(emailPattern);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private void postDoctorLoginAction() {
        if(mAcitvity.equals("PatiantTimeSlotActivity")) {
            /** finishing only, not settng anything to onActivityResult() patinet time slot activity*/
            callToast("Please select patient to login");
//                            finish();
        }else{
            if (Util.isNetworkAvailable(this)) {
                mUtil.showProgressDialog(this);
                mUtil.getBaseClassService(this, "").doctorLogin(sEmail, sPassword, 2, new Callback<JsonObject>() {
                    @Override
                    public void success(JsonObject jsonObject, Response response) {
                        if (jsonObject.get(StringConstants.STATUS).getAsString().equalsIgnoreCase("success")) {
                            mUtil.dismissDialog();
                            Toast.makeText(LoginActivity.this, "Login success!", Toast.LENGTH_SHORT).show();
                            JsonObject dataObject = jsonObject.getAsJsonObject(StringConstants.DATA);
                            mUtil.setString(USERID, dataObject.get(USERID).getAsString());
                            Util.setString("type", "doctor");

                            {
                                startActivity(new Intent(LoginActivity.this, DoctorHomeActivity.class));
                                finish();
                            }

                        } else {
                            mUtil.dismissDialog();
                            try {
                                Toast.makeText(LoginActivity.this, jsonObject.get("message").getAsString(), Toast.LENGTH_LONG).show();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            try {
                                Toast.makeText(LoginActivity.this, jsonObject.get("errorMessages").getAsString(), Toast.LENGTH_LONG).show();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        mUtil.dismissDialog();
                        mUtil.serviceCallFailermsg(error, LoginActivity.this);
                        Log.e("Lokesh", error.toString());
                    }
                });
            }
        }
    }

    private void postPatientLoginAction() {
//        printKeyHash(LoginActivity.this);
        if (Util.isNetworkAvailable(this)) {
            mUtil.showProgressDialog(this);
            mUtil.getBaseClassService(this, "").doctorLogin(sEmail, sPassword,1, new Callback<JsonObject>() {
                @Override
                public void success(JsonObject jsonObject, Response response) {
                    if(jsonObject.get(StringConstants.STATUS).getAsString().equalsIgnoreCase("success")){
                        mUtil.dismissDialog();
                        Toast.makeText(LoginActivity.this, "Login success!", Toast.LENGTH_SHORT).show();
                        JsonObject dataObject = jsonObject.getAsJsonObject(StringConstants.DATA);

                        /****** USER_ID is used in HomeActivity,
                         * if not exists it display via guest mode******/

                        Util.setString(USERID, dataObject.get(USERID).getAsString());
                        Util.setString("type","patient");

                        if(mAcitvity.equals("LoginActivity")){
                            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                            finish();
                        }
                        if(mAcitvity.equals("SignupActivity")){
                            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                            finish();
                        }else if(mAcitvity.equals("PatiantTimeSlotActivity")) {
                            setResult(200);
                            finish();
                        }
                    }else {
                        mUtil.dismissDialog();
                        try {
                            Toast.makeText(LoginActivity.this, jsonObject.get("message").getAsString(), Toast.LENGTH_LONG).show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    mUtil.dismissDialog();
                    mUtil.serviceCallFailermsg(error,LoginActivity.this);

                    Log.e("LoginActivity","retroerror"+error.toString());
                }
            });
        }

    }

    public void callToast(String msg){
        Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show();
    }

    public static String printKeyHash(Activity context) {
        PackageInfo packageInfo;
        String key = null;
        try {
            //getting application package name, as defined in manifest
            String packageName = context.getApplicationContext().getPackageName();

            //Retriving package info
            packageInfo = context.getPackageManager().getPackageInfo(packageName,
                    PackageManager.GET_SIGNATURES);

            Log.e("Package Name=", context.getApplicationContext().getPackageName());

            for (android.content.pm.Signature signature : packageInfo.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                key = new String(Base64.encode(md.digest(), 0));

                // String key = new String(Base64.encodeBytes(md.digest()));
                Log.e("Key Hash=", key);
            }
        } catch (PackageManager.NameNotFoundException e1) {
            Log.e("Name not found", e1.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.e("No such an algorithm", e.toString());
        } catch (Exception e) {
            Log.e("Exception", e.toString());
        }

        return key;
    }


    //*************gmail *******************//
    private void getResultsFromApi() {
        if (!isGooglePlayServicesAvailable()) {
            acquireGooglePlayServices();
        } else if (mCredential.getSelectedAccountName() == null) {
            chooseAccount();
        } else if (!isDeviceOnline()) {
//            mOutputText.setText("No network connection available.");
            Toast.makeText(LoginActivity.this, "No network connection available.", Toast.LENGTH_SHORT).show();
        } else {
            new MakeRequestTask(mCredential).execute();
        }
    }

    /**
     * Attempts to set the account used with the API credentials. If an account
     * name was previously saved it will use that one; otherwise an account
     * picker dialog will be shown to the user. Note that the setting the
     * account to use with the credentials object requires the app to have the
     * GET_ACCOUNTS permission, which is requested here if it is not already
     * present. The AfterPermissionGranted annotation indicates that this
     * function will be rerun automatically whenever the GET_ACCOUNTS permission
     * is granted.
     */
    @AfterPermissionGranted(REQUEST_PERMISSION_GET_ACCOUNTS)
    private void chooseAccount() {
        if (EasyPermissions.hasPermissions(
                this, Manifest.permission.GET_ACCOUNTS)) {
             gmailAccountName = getPreferences(Context.MODE_PRIVATE)
                    .getString(PREF_ACCOUNT_NAME, null);
            String info = null;
            try {
                info = mUtil.getString("GMAILINFO");
            } catch (Exception e) {
                e.printStackTrace();
                info = "";
            }

            if (info.equals("yes")) {
                mCredential.setSelectedAccountName(gmailAccountName);
                getResultsFromApi();
            } else {
                // Start a dialog from which the user can choose an account
                startActivityForResult(
                        mCredential.newChooseAccountIntent(),
                        REQUEST_ACCOUNT_PICKER);
            }
        } else {
            // Request the GET_ACCOUNTS permission via a user dialog
            EasyPermissions.requestPermissions(
                    this,
                    "This app needs to access your Google account (via Contacts).",
                    REQUEST_PERMISSION_GET_ACCOUNTS,
                    Manifest.permission.GET_ACCOUNTS);
        }
    }

    /**
     * Called when an activity launched here (specifically, AccountPicker
     * and authorization) exits, giving you the requestCode you started it with,
     * the resultCode it returned, and any additional data from it.
     *
     * @param requestCode code indicating which activity result is incoming.
     * @param resultCode  code indicating the result of the incoming
     *                    activity result.
     * @param data        Intent (containing result data) returned by incoming
     *                    activity result.
     */
    @Override
    protected void onActivityResult(
            int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_GOOGLE_PLAY_SERVICES:
                if (resultCode != RESULT_OK) {
                   /* mOutputText.setText(
                            "This app requires Google Play Services. Please install " +
                                    "Google Play Services on your device and relaunch this app.");*/
                    Toast.makeText(LoginActivity.this, "\"This app requires Google Play Services. Please install \" +\n" +
                            " \"Google Play Services on your device and relaunch this app.\"", Toast.LENGTH_LONG).show();
                } else {
                    getResultsFromApi();
                }
                break;
            case REQUEST_ACCOUNT_PICKER:
                if (resultCode == RESULT_OK && data != null &&
                        data.getExtras() != null) {
                    String accountName =
                            data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
                    if (accountName != null) {
                        SharedPreferences settings =
                                getPreferences(Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString(PREF_ACCOUNT_NAME, accountName);
                        editor.apply();
                        mCredential.setSelectedAccountName(accountName);
                        getResultsFromApi();
                    }
                }
                break;
            case REQUEST_AUTHORIZATION:
                if (resultCode == RESULT_OK) {
                    getResultsFromApi();
                }
                break;
        }

        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * Respond to requests for permissions at runtime for API 23 and above.
     *
     * @param requestCode  The request code passed in
     *                     requestPermissions(android.app.Activity, String, int, String[])
     * @param permissions  The requested permissions. Never null.
     * @param grantResults The grant results for the corresponding permissions
     *                     which is either PERMISSION_GRANTED or PERMISSION_DENIED. Never null.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(
                requestCode, permissions, grantResults, this);
    }

    /**
     * Callback for when a permission is granted using the EasyPermissions
     * library.
     *
     * @param requestCode The request code associated with the requested
     *                    permission
     * @param list        The requested permission list. Never null.
     */
    @Override
    public void onPermissionsGranted(int requestCode, List<String> list) {
        // Do nothing.
    }

    /**
     * Callback for when a permission is denied using the EasyPermissions
     * library.
     *
     * @param requestCode The request code associated with the requested
     *                    permission
     * @param list        The requested permission list. Never null.
     */
    @Override
    public void onPermissionsDenied(int requestCode, List<String> list) {
        // Do nothing.
    }

    /**
     * Checks whether the device currently has a network connection.
     *
     * @return true if the device has a network connection, false otherwise.
     */
    private boolean isDeviceOnline() {
        ConnectivityManager connMgr =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    /**
     * Check that Google Play services APK is installed and up to date.
     *
     * @return true if Google Play Services is available and up to
     * date on this device; false otherwise.
     */
    private boolean isGooglePlayServicesAvailable() {
        GoogleApiAvailability apiAvailability =
                GoogleApiAvailability.getInstance();
        final int connectionStatusCode =
                apiAvailability.isGooglePlayServicesAvailable(this);
        return connectionStatusCode == ConnectionResult.SUCCESS;
    }

    /**
     * Attempt to resolve a missing, out-of-date, invalid or disabled Google
     * Play Services installation via a user dialog, if possible.
     */
    private void acquireGooglePlayServices() {
        GoogleApiAvailability apiAvailability =
                GoogleApiAvailability.getInstance();
        final int connectionStatusCode =
                apiAvailability.isGooglePlayServicesAvailable(this);
        if (apiAvailability.isUserResolvableError(connectionStatusCode)) {
            showGooglePlayServicesAvailabilityErrorDialog(connectionStatusCode);
        }
    }


    /**
     * Display an error dialog showing that Google Play Services is missing
     * or out of date.
     *
     * @param connectionStatusCode code describing the presence (or lack of)
     *                             Google Play Services on this device.
     */
    void showGooglePlayServicesAvailabilityErrorDialog(
            final int connectionStatusCode) {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        Dialog dialog = apiAvailability.getErrorDialog(
                LoginActivity.this,
                connectionStatusCode,
                REQUEST_GOOGLE_PLAY_SERVICES);
        dialog.show();
    }

    /**
     * An asynchronous task that handles the Gmail API call.
     * Placing the API calls in their own task ensures the UI stays responsive.
     */
    class MakeRequestTask extends AsyncTask<Void, Void, List<String>> {
        private com.google.api.services.gmail.Gmail mService = null;
        private Exception mLastError = null;

        public MakeRequestTask(GoogleAccountCredential credential) {
            HttpTransport transport = AndroidHttp.newCompatibleTransport();
            JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
            mService = new com.google.api.services.gmail.Gmail.Builder(
                    transport, jsonFactory, credential)
                    .setApplicationName("Gmail API Android Quickstart")
                    .build();
        }

        /**
         * Background task to call Gmail API.
         *
         * @param params no parameters needed for this task.
         */
        @Override
        protected List<String> doInBackground(Void... params) {
            try {
                return getDataFromApi();
            } catch (Exception e) {
                mLastError = e;
                cancel(true);
                return null;
            }
        }

        /**
         * Fetch a list of Gmail labels attached to the specified account.
         *
         * @return List of Strings labels.
         * @throws IOException
         */
        private List<String> getDataFromApi() throws IOException {
            // Get the labels in the user's account.
            String user = "me";
            List<String> labels = new ArrayList<String>();          //here lables are not in use.
//                ListLabelsResponse listResponse =
//                        mService.users().labels().list(user).execute();
//                for (Label label : listResponse.getLabels()) {
//                    labels.add(label.getName());
//                }
            String accountName = getPreferences(Context.MODE_PRIVATE)
                    .getString(PREF_ACCOUNT_NAME, null);
            new GetNameInForeground(LoginActivity.this, accountName,SCOPE).execute();


            return labels;                                        //here lables are not in use.
        }


        @Override
        protected void onPreExecute() {
//            mOutputText.setText("");
//            mProgress.show();
        }

        @Override
        protected void onPostExecute(List<String> output) {
            mProgress.hide();

            output.add(0, "Data retrieved using the Gmail API:");               //here it's not needed.


            JSONObject jsonObject;

            imageLoader = ImageLoader.getInstance();
            imageLoader.init(ImageLoaderConfiguration.createDefault(LoginActivity.this));
            try {
                jsonObject = new JSONObject(AbstractGetNameTask.GOOGLE_USER_DATA);
                Log.d("Lokesh",jsonObject.toString());
                String s  =  jsonObject.getString("name");
                String pic = jsonObject.getString("picture");
//                imageLoader.displayImage(pic,imageView);
//                mOutputText.setText(s);
                Log.d("Lokesh","user name is :"+s);
                if(s.length() > 0){
                    mUtil.setString("GMAILINFO","yes");
                    /*  USER_ID has to be taken,before going to HomeActivity   */

                    if (mDoctorButton.isChecked()){
                        getUserIdFromGmailServiceAndFaceBook(gmailAccountName,"googleplus","2");
                    }else if (mPatientButton.isChecked()){
                        getUserIdFromGmailServiceAndFaceBook(gmailAccountName,"googleplus","1");
                    }




//                    startActivity(new Intent(LoginActivity.this,HomeActivity.class));

                }else {
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }

        @Override
        protected void onCancelled() {
            mProgress.hide();
            if (mLastError != null) {
                if (mLastError instanceof GooglePlayServicesAvailabilityIOException) {
                    showGooglePlayServicesAvailabilityErrorDialog(
                            ((GooglePlayServicesAvailabilityIOException) mLastError)
                                    .getConnectionStatusCode());
                } else if (mLastError instanceof UserRecoverableAuthIOException) {
                    startActivityForResult(
                            ((UserRecoverableAuthIOException) mLastError).getIntent(),
                            LoginActivity.REQUEST_AUTHORIZATION);
                } else {
                    /*mOutputText.setText("The following error occurred:\n"
                            + mLastError.getMessage());*/
                }
            } else {
//                mOutputText.setText("Request cancelled.");
            }
        }
    }

    // this method send mail to service, the service gives userId, through this userId user or doctor could login.
    private void getUserIdFromGmailServiceAndFaceBook(String mail,String accountType,String userType) {
        if (Util.isNetworkAvailable(this)) {
            mUtil.showProgressDialog(this);
            mUtil.getBaseClassService(this, "").postGamilFaceBookService(mail,accountType,
                    userType,new Callback<JsonObject>() {
                @Override
                public void success(JsonObject jsonObject, Response response) {
                    if (jsonObject.get(StringConstants.STATUS).getAsString().equalsIgnoreCase("success")|
                            jsonObject.get(StringConstants.STATUS).getAsString().equalsIgnoreCase("fail")) {
                        mUtil.dismissDialog();

                        JsonObject dataObject = jsonObject.getAsJsonObject(StringConstants.DATA);

                        mUtil.setString(USERID, dataObject.get("id").getAsString());  /////////////////////////////******************11/3/2016


                        if (mDoctorButton.isChecked()){                 //here manually checked the usertype,
                                                                        // need to check user type i.e, (doctor or patient) using json response.
                            if(mAcitvity.equals("PatiantTimeSlotActivity")) {  //gmail login , from patient timeslot activity,it will be closed aftet verified and return to patient time slot activity,perform pending action
                                setResult(200);
                                finish();
                            }else {
                                Util.setString("type", "doctor");
                                startActivity(new Intent(LoginActivity.this, DoctorHomeActivity.class));
                                finish();
                            }
                        }else if (mPatientButton.isChecked()){
                            if(mAcitvity.equals("PatiantTimeSlotActivity")) {       //gmail login , from patient timeslot activity,it will be closed aftet verified and return to patient time slot activity,perform pending action
                                setResult(200);
                                finish();
                            }else {
                                Util.setString("type", "patient");
                                startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                                finish();
                            }
                        }
                    } else /*if(jsonObject.get("message").getAsString()           // checks if already exists user
                            .contains("This Email Already Registered,Please Give another")){
                        mUtil.dismissDialog();


                    }else*/{
                        mUtil.dismissDialog();
                        try {
                            Toast.makeText(LoginActivity.this, jsonObject.get("message").getAsString(), Toast.LENGTH_LONG).show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        try {
                            Toast.makeText(LoginActivity.this, jsonObject.get("errorMessages").getAsString(), Toast.LENGTH_LONG).show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    mUtil.dismissDialog();
                    mUtil.serviceCallFailermsg(error, LoginActivity.this);
                    Log.e("Lokesh", error.toString());
                }
            });
        }
    }


    public void disconnectFromFacebook() {

        if (AccessToken.getCurrentAccessToken() == null) {
            return; // already logged out
        }

        new GraphRequest(AccessToken.getCurrentAccessToken(), "/me/permissions/", null, HttpMethod.DELETE, new GraphRequest
                .Callback() {
            @Override
            public void onCompleted(GraphResponse graphResponse) {

                LoginManager.getInstance().logOut();

            }
        }).executeAsync();
    }


}
