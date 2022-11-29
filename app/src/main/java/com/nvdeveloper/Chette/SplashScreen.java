package com.nvdeveloper.Chette;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.nvdeveloper.Chette.account_activities.SignUp;
import com.nvdeveloper.Chette.user.MainActivityUser;

public class SplashScreen extends AppCompatActivity {

    TextView loading_text;
    public static final String [] Loading = {"Loading.", "Loading..", "Loading...", "Loading...."};
    String username = "", email = "", phoneNumber = "";

    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        setStatusBarColor();

        sharedPreferences = getApplicationContext().getSharedPreferences("ACCOUNT_EXISTENCE", Context.MODE_PRIVATE);
        loading_text = findViewById(R.id.loading_text);

        /*sharedPreferences.edit().putString("phoneNumber", "+254758958165").apply();
        sharedPreferences.edit().putString("email", "oo").apply();
        sharedPreferences.edit().putString("username", "sankoh").apply();*/

        for(int i = 0; i < 5; i++){
            AnimateLoading((i + 1) * 1000);
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                try{
                    username = sharedPreferences.getString("username", username);
                    email = sharedPreferences.getString("email", email);
                    phoneNumber = sharedPreferences.getString("phoneNumber", phoneNumber);


                    if(TextUtils.isEmpty(username) || TextUtils.isEmpty(email) || TextUtils.isEmpty(phoneNumber)){
                        startActivity(new Intent(
                                getApplicationContext(), SignUp.class
                        ));

                        finish();
                    }else{
                        Intent intent = new Intent(getApplicationContext(), MainActivityUser.class);
                        intent.putExtra("username", username);
                        intent.putExtra("email", email);
                        intent.putExtra("phoneNumber", phoneNumber);

                        startActivity(intent);

                        finish();
                    }
                }catch(NullPointerException e){
                    e.printStackTrace();
                }
            }
        }, 5000);
    }

    private void setStatusBarColor(){
        getSupportActionBar().hide();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        android.graphics.drawable.Drawable background = getApplicationContext().getResources().getDrawable(R.drawable.gradient_status);
        getWindow().setBackgroundDrawable(background);
    }

    private void animateEachSecond(){
        for(int i = 0; i < 4; i++){
            int finalI = i;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    loading_text.setText(Loading[finalI]);
                }
            }, ((finalI + 2) * 100));
        }
    }

    private void AnimateLoading(int delayMillis){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                animateEachSecond();
            }
        }, delayMillis);
    }
}