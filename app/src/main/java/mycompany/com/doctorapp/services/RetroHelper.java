package mycompany.com.doctorapp.services;

import android.content.Context;
import android.util.Log;

import com.squareup.okhttp.OkHttpClient;

import mycompany.com.doctorapp.utils.StringConstants;
import retrofit.RestAdapter;
import retrofit.client.OkClient;

/**
 * Created by Dell on 8/30/2016.
 */
public class RetroHelper {

    public static String mEnvironment = StringConstants.DEV;
    private static Context mContext;
    public static RestAdapter getAdapter(Context ctx, String serverUrl) {
        mContext=ctx;
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(StringConstants.BASE_CLASSES_URL+serverUrl)
                .setLogLevel(RestAdapter.LogLevel.FULL).setLog(new RestAdapter.Log() {
                    @Override
                    public void log(String msg) {
                        Log.i("Retro Helper", msg);
                    }
                })
                .setClient(new OkClient(new OkHttpClient()))
                .build();


        return restAdapter;
    }
}


