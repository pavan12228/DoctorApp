package mycompany.com.doctorapp.doctormodule;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
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
import mycompany.com.doctorapp.doctormodule.Fragments.AppointmentFragment;
import mycompany.com.doctorapp.doctormodule.Fragments.DoctorProfileFragment;
import mycompany.com.doctorapp.doctormodule.Fragments.DrugsFragment;
import mycompany.com.doctorapp.doctormodule.Fragments.SheduleFragment;
import mycompany.com.doctorapp.utils.StringConstants;
import mycompany.com.doctorapp.utils.Util;

public class DoctorHomeActivity extends AppCompatActivity implements View.OnClickListener {
    private TabLayout mTablayout;
    private ImageView mMenuImage,mDummy;
    private TextView mChangePassword,tvLogout;
    private RelativeLayout mMenuRelative;
    private Util mUtil;
    private static final String PREF_ACCOUNT_NAME = "accountName";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_home);
        mUtil = new Util();
        initializeViews();

        final FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        DrugsFragment drugsFragment = new DrugsFragment();
        fragmentTransaction.add(R.id.container,drugsFragment);
        fragmentTransaction.commit();

        mTablayout = (TabLayout) findViewById(R.id.tab_layout);
        mTablayout.addTab(mTablayout.newTab().setText("DRUGS"));
        mTablayout.addTab(mTablayout.newTab().setText("APPOINTMENTS"));
        mTablayout.addTab(mTablayout.newTab().setText("PROFILE"));
        mTablayout.addTab(mTablayout.newTab().setText("SHEDULE"));
        mTablayout.setTabTextColors(getResources().getColor(R.color.unselected),getResources().getColor(R.color.white));
        mTablayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                int position = tab.getPosition();
//                if (position == 0) {
//                    final FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//                    HomeFragment home=new HomeFragment();
//                    fragmentTransaction.replace(R.id.container,home);
//                    fragmentTransaction.commit();
//
//                }
                 if (position == 0) {
                    final FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    DrugsFragment drugsFragment = new DrugsFragment();
                    fragmentTransaction.replace(R.id.container,drugsFragment);
                    fragmentTransaction.commit();
                } else if (position == 1) {
                    final FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    AppointmentFragment appointmentsFragment = new AppointmentFragment();
                    fragmentTransaction.replace(R.id.container,appointmentsFragment);
                    fragmentTransaction.commit();
                } else if (position == 2) {
                    final FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    DoctorProfileFragment doctorProfileFragment = new DoctorProfileFragment();
                    fragmentTransaction.replace(R.id.container,doctorProfileFragment);
                    fragmentTransaction.commit();
                }
                else if (position == 3) {
                    final FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    SheduleFragment sheduleFragment = new SheduleFragment();
                    fragmentTransaction.replace(R.id.container,sheduleFragment);
                    fragmentTransaction.commit();
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
//        mMenuImage = (ImageView) findViewById(R.id.activityhome_menu_iv);
//        mMenuImage.setOnClickListener(this);

//        mDummy = (ImageView) findViewById(R.id.dummy_image);
//        mDummy.setOnClickListener(this);

//        mMenuRelative = (RelativeLayout) findViewById(R.id.activity_home_menu_rl);
//        mMenuRelative.setVisibility(View.GONE);
//        mMenuRelative.setOnClickListener(this);

//        mChangePassword = (TextView)findViewById(R.id.doctor_home_change_password_tv);
//        mChangePassword.setOnClickListener(this);

//        tvLogout = (TextView) findViewById(R.id.doctor_home_logout_tv);
//        tvLogout.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
//            case R.id.activity_home_menu_rl:
//                mMenuRelative.setVisibility(View.GONE);
//                break;

//            case R.id.activityhome_menu_iv:
//                if (mMenuRelative.getVisibility() == View.GONE)
//                    mMenuRelative.setVisibility(View.VISIBLE);
//                else if (mMenuRelative.getVisibility() == View.VISIBLE)
//                    mMenuRelative.setVisibility(View.GONE);
//                break;
//
//            case R.id.doctor_home_change_password_tv:
//                Intent changePassworsIntent = new Intent(DoctorHomeActivity.this, ChangePasswordActivity.class);
//                changePassworsIntent.putExtra("type","doctor");
//                startActivity(changePassworsIntent);
//                break;

//            case R.id.doctor_home_logout_tv:
//                startActivity(new Intent(DoctorHomeActivity.this, LoginActivity.class));
//                finish();
//                break;

//            case R.id.dummy_image:
//                mMenuRelative.setVisibility(View.GONE);
//                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.game_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.new_game:
                Intent changePasswordIntent = new Intent(DoctorHomeActivity.this, ChangePasswordActivity.class);
                changePasswordIntent.putExtra("type","doctor");
                startActivity(changePasswordIntent);
                return true;
            case R.id.help:
                mUtil.setString(StringConstants.USERID, String.valueOf(0));
                startActivity(new Intent(DoctorHomeActivity.this, LoginActivity.class)); // this line and above lines are no need,the below line does.
                mUtil.getSharedpreferences(this).edit().clear().apply();
                finish();
                return true;
            default:  return super.onOptionsItemSelected(item);
        }
    }



}
