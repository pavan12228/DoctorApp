package mycompany.com.doctorapp.patientmodule;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import mycompany.com.doctorapp.R;
import mycompany.com.doctorapp.common_activities.ChangePasswordActivity;
import mycompany.com.doctorapp.common_activities.LoginActivity;
import mycompany.com.doctorapp.patientmodule.fragments.AppointmentsFragment;
import mycompany.com.doctorapp.patientmodule.fragments.HomeFragment;
import mycompany.com.doctorapp.patientmodule.fragments.ProfileFragment;
import mycompany.com.doctorapp.utils.StringConstants;
import mycompany.com.doctorapp.utils.Util;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {
    private TabLayout mTablayout;
    private ImageView mMenuIv,mDummy;
    private RelativeLayout mMenu_rl, mRelativelayout;
    private TextView tvChangePassword,tvAboutUs,tvLogout;
    Util mUtil;
    private int userId;
    private int activityStarted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mUtil = new Util();
        mUtil.getSharedpreferences(this);

        if(!mUtil.getString(StringConstants.USERID).equals(""))
        userId = Integer.parseInt(mUtil.getString(StringConstants.USERID));


        initializeViews();


        FragmentManager fragmentManager = getSupportFragmentManager(); /**first this fragment will be loaded.*/
        final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        HomeFragment home = new HomeFragment();
        fragmentTransaction.add(R.id.activityhome_container, home);
        fragmentTransaction.commit();


        mTablayout.addTab(mTablayout.newTab().setText("HOME"));


        if(userId != 0) {    /** if userId is not zero , guest mode is not enabled*/
            mTablayout.addTab(mTablayout.newTab().setText("APPOINTMENTS"));
            mTablayout.addTab(mTablayout.newTab().setText("PROFILE"));
        }

        if(userId != 0)         /** if userId is not zero, whole tabs are shown  and works*/
            mTablayout.setOnClickListener(this);
        mTablayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                int position = tab.getPosition();
                if (position == 0) {

                    FragmentManager fragmentManager = getSupportFragmentManager();
                    final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    HomeFragment home = new HomeFragment();
                    fragmentTransaction.replace(R.id.activityhome_container, home);
                    fragmentTransaction.commit();

                } else if (position == 1) {

                    final FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    AppointmentsFragment appointmentsFragment = new AppointmentsFragment();
                    fragmentTransaction.replace(R.id.activityhome_container, appointmentsFragment);
                    fragmentTransaction.commit();
                } else if (position == 2) {
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    ProfileFragment profileFragment = new ProfileFragment();
                    fragmentTransaction.replace(R.id.activityhome_container, profileFragment);
                    fragmentTransaction.commit();
                } else if (position == 3) {
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void initializeViews() {
        mMenu_rl = (RelativeLayout) findViewById(R.id.activity_home_menu_rl); // no need (Doubt)
        mMenu_rl.setVisibility(View.GONE);          // no need (Doubt)
        mMenu_rl.setOnClickListener(this);          // no need (Doubt)

        mRelativelayout = (RelativeLayout) findViewById(R.id.relativelayout);
        mRelativelayout.setOnClickListener(this);

        mTablayout = (TabLayout) findViewById(R.id.activityhome_tab_layout);

        tvChangePassword = (TextView) findViewById(R.id.activityhome_changepassword);
        tvChangePassword.setOnClickListener(this);

        tvLogout = (TextView) findViewById(R.id.activityhome_logout);
        tvLogout.setOnClickListener(this);

        if(userId == 0 ) {              /** without login ,logout,changepassword should not be shown to user in menu*/
            tvLogout.setVisibility(View.GONE);
            tvChangePassword.setVisibility(View.GONE);
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(userId != 0) {           /** without login, menu can not be shown to user.*/
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.game_menu, menu);
            return true;
        }else return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.new_game:
                Intent changePasswordIntent = new Intent(HomeActivity.this, ChangePasswordActivity.class);
                changePasswordIntent.putExtra("type","patient");    /** if patient is sent, ChangePasswordActivity is enabled for patient.*/
                startActivity(changePasswordIntent);
                return true;
            case R.id.help:
                mUtil.setString(StringConstants.USERID, String.valueOf(0)); /** setting sharedPreferenses to zero,means logging out.*/
                startActivity(new Intent(HomeActivity.this, LoginActivity.class)); // this line and above lines are no need,the below line does.
                mUtil.getSharedpreferences(this).edit().clear().commit();
                finishAffinity();
                return true;
            default:  return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(activityStarted != 0){
            if (userId == 0) {
                finish();
                startActivity(getIntent());
            }
        }
        activityStarted++;
    }
}

