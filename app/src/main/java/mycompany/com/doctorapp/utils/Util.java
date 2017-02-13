package mycompany.com.doctorapp.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.ProgressBar;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import mycompany.com.doctorapp.R;
import mycompany.com.doctorapp.services.RetroHelper;
import mycompany.com.doctorapp.services.ServiceOperations;
import retrofit.RetrofitError;
import retrofit.mime.TypedByteArray;

/**
 * Created by Dell on 8/30/2016.
 */
public class Util {
    private static final String MyPREFERENCES = "MyPrefs";
    private static SharedPreferences sharedpreferences;
    MaterialDialog ringProgressDialog = null;

    public static SharedPreferences getSharedpreferences(Context context) {
        if (sharedpreferences == null)
            sharedpreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        return sharedpreferences;
    }

    public static void setString(String key, String value) {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static String getString(String key) {
        return sharedpreferences.getString(key, "");
    }

    public static void setBoolean(String key, boolean value) {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public static boolean getBoolean(String key) {
        return sharedpreferences.getBoolean(key, false);
    }

    public static void setInt(String key, String value) {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putInt(key, Integer.parseInt(value));
        editor.commit();
    }

    public static int getInt(String key) {
        return sharedpreferences.getInt(key, 0);
    }


    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        if (info == null) return false;
        NetworkInfo.State network = info.getState();
        return (network == NetworkInfo.State.CONNECTED || network == NetworkInfo.State.CONNECTING);
    }

    public  void showProgressDialog(final Context ctx) {
        if (ringProgressDialog == null) {
            ringProgressDialog = new MaterialDialog.Builder(ctx)
                    .title(ctx.getResources().getString(R.string.app_name))
                    .content("Please wait.... ")
                    .progress(true, 0)
                    .theme(Theme.LIGHT)
                    .cancelable(false)
                    .show();
            ringProgressDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                @Override
                public void onShow(DialogInterface dialog) {
                    ProgressBar v = (ProgressBar) ringProgressDialog.findViewById(android.R.id.progress);
                    v.getIndeterminateDrawable().setColorFilter(ctx.getResources().getColor(R.color.colorPrimary),
                            android.graphics.PorterDuff.Mode.MULTIPLY);
                }
            });
        }
    }

    public void dismissDialog() {
        if (ringProgressDialog != null) {
            if (ringProgressDialog.isShowing()) {
                ringProgressDialog.dismiss();
                ringProgressDialog = null;
            }
        }
    }

    public void singelButtonAlertDialog(Context ctx, String title, String message) {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(ctx);
        builder.theme(Theme.LIGHT);
        if (title != null) {
            builder.title(title);
        }
        builder.content(message);

        builder.positiveText("Ok");

        builder.positiveColorRes(R.color.colorPrimary);
        builder.cancelable(false);
        builder.show();

    }

    public void singelButtonAlertDialog(Context ctx, String title, String message, final MaterialDialog.ButtonCallback cb) {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(ctx);
        builder.theme(Theme.LIGHT);
        if (title != null) {
            builder.title(title);
        }
        builder.content(message);

        builder.positiveText("Yes");
        builder.negativeText("No");

        builder.positiveColorRes(R.color.colorPrimary);
        builder.negativeColorRes(R.color.md_edittext_error);
        builder.callback(cb);
        builder.cancelable(false);
        builder.show();

    }


    public void twoButtonAlertDialog(Context ctx, String title, String message, boolean okCancel,
                                     final MaterialDialog.ButtonCallback cb) {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(ctx);
        builder.theme(Theme.LIGHT);
        if (title != null) {
            builder.title(title);
        }
        builder.content(message);

        if (okCancel) {
            builder.negativeText(android.R.string.cancel);
            builder.positiveText(android.R.string.ok);
        } else {
            builder.negativeText("No");
            builder.positiveText("Yes");
        }

        builder.positiveColorRes(R.color.colorPrimary);
        builder.negativeColorRes(R.color.md_edittext_error);
        builder.callback(cb);
        builder.cancelable(false);
        builder.show();

    }


    public ServiceOperations getBaseClassService(Context ctx, String url) {
        return new RetroHelper().getAdapter(ctx, url).create(ServiceOperations.class);
    }
    public void serviceCallFailermsg(RetrofitError error, Context mcontext) {
        if (error != null) {
            try {
                String body = new String(((TypedByteArray) error.getResponse().getBody()).getBytes());
                Log.v("failure", body.toString());
                JsonObject errorJson = new Gson().fromJson(body, JsonObject.class);
                singelButtonAlertDialog(mcontext, mcontext.getResources().getString(R.string.app_name), errorJson.get(StringConstants.message).getAsString());
            } catch (Exception e) {
                singelButtonAlertDialog(mcontext, mcontext.getResources().getString(R.string.app_name), mcontext.getString(R.string.errormessage));
            }
        } else {
            singelButtonAlertDialog(mcontext, mcontext.getResources().getString(R.string.app_name), mcontext.getString(R.string.errormessage));
        }
    }

    /*
    callback = new MaterialDialog.ButtonCallback() {
        @Override
        public void onPositive(MaterialDialog dialog) {
        }

        @Override
        public void onNegative(MaterialDialog dialog) {
            dialog.dismiss();
        }
    };
    */

}

