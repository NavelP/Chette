package com.nvdeveloper.Chette.user;

import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.nvdeveloper.Chette.InternetConnectionStatus;
import com.nvdeveloper.Chette.R;
import com.nvdeveloper.Chette.track_response.TrackResponsePage;

public class MainActivityUser extends AppCompatActivity implements ManipulateFragments{

    public static String username, email, phone;
    BottomNavigationView bottomNavigationView;

    InternetConnectionStatus internetConnectionStatus = new InternetConnectionStatus();
    public static String INTERNET_CONNECTION_STATUS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_user);

        getSupportActionBar().hide();

        //registering the broadcast receiver
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(internetConnectionStatus, filter);

        setStatusBarColor();
        bottomNavigationView = findViewById(R.id.chette_bottom_nav);

        Bundle bundle = getIntent().getExtras();

        assert bundle != null;

        username = bundle.getString("username");
        email = bundle.getString("email");
        phone = bundle.getString("phoneNumber");

        getSupportFragmentManager().beginTransaction().replace(
                R.id.chette_frame_layout, new ActionPage(username, email, phone, getApplicationContext(), MainActivityUser.this)
        ).commit();

        bottomNavigationView.setOnNavigationItemSelectedListener(nav);

    }
    private void setStatusBarColor(){
        getSupportActionBar().hide();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        android.graphics.drawable.Drawable background = getApplicationContext().getResources().getDrawable(R.drawable.gradient_status);
        getWindow().setBackgroundDrawable(background);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener nav =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch(item.getItemId()){
                        case R.id.action:
                            getSupportFragmentManager().beginTransaction().replace(
                                    R.id.chette_frame_layout, new ActionPage(username, email, phone, getApplicationContext(), MainActivityUser.this)
                            ).commit();
                            break;

                        case R.id.track_response:
                            getSupportFragmentManager().beginTransaction().replace(
                                    R.id.chette_frame_layout, new TrackResponsePage(getSupportFragmentManager())
                            ).commit();
                            break;
                    }

                    return true;
                }
            };

    @Override
    public void changeFragment(int frameLayoutId, Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(
                frameLayoutId, fragment
        ).commit();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(internetConnectionStatus);
    }

}
