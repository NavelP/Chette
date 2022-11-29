package com.nvdeveloper.Chette.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.WindowManager;

import com.nvdeveloper.Chette.R;

public class MainActivityAdmin extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_admin);

        getSupportActionBar().hide();

        setStatusBarColor();
    }

    private void setStatusBarColor(){
        getSupportActionBar().hide();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        android.graphics.drawable.Drawable background = getApplicationContext().getResources().getDrawable(R.drawable.gradient_status);
        getWindow().setBackgroundDrawable(background);
    }
}