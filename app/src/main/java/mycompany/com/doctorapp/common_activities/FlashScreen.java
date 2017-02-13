package mycompany.com.doctorapp.common_activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import mycompany.com.doctorapp.R;

public class FlashScreen extends AppCompatActivity {
    private static int SPLASH_TIME_OUT = 2000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flash_screen);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                Intent i = new Intent(FlashScreen.this,LoginActivity.class);
                startActivity(i);
                finish();
                // close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}
