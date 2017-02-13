package mycompany.com.doctorapp.utils;

import android.app.Application;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import mycompany.com.doctorapp.R;

/**
 * Created by Dell on 8/30/2016.
 */
public class DoctorAppApplication extends Application{
    public static DisplayImageOptions generalOptions,rounderCornerOptions;
    public static ImageLoader generalImageLoader;
    public static ImageLoader rounderCornerImageLoader;


    @Override
    public void onCreate() {
        super.onCreate();

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

//        Logging App Activations
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

        generalOptions = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.no_image_available)
                .showImageForEmptyUri(R.drawable.no_image_available)
                .cacheOnDisk(true)
                .showImageOnFail(R.drawable.no_image_available)
                .cacheInMemory(true)
                .build();

        rounderCornerOptions = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.no_image_available)
                .showImageForEmptyUri(R.drawable.no_image_available)
                .displayer(new RoundedBitmapDisplayer(50))
                .cacheOnDisk(true)
                .showImageOnFail(R.drawable.no_image_available)
                .cacheInMemory(true)
                .build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(DoctorAppApplication.this)
                .memoryCache(new LruMemoryCache(2 * 1024 * 1024))
                .defaultDisplayImageOptions(generalOptions)
                .denyCacheImageMultipleSizesInMemory()
                .writeDebugLogs()
                .build();
        generalImageLoader = ImageLoader.getInstance();
        generalImageLoader.init(config);


        ImageLoaderConfiguration rounderCornerconfig = new ImageLoaderConfiguration.Builder(DoctorAppApplication.this)
                .memoryCache(new LruMemoryCache(2 * 1024 * 1024))
                .defaultDisplayImageOptions(rounderCornerOptions)
                .denyCacheImageMultipleSizesInMemory()
                .writeDebugLogs()
                .build();
        rounderCornerImageLoader = ImageLoader.getInstance();
        rounderCornerImageLoader.init(rounderCornerconfig);
    }
}
