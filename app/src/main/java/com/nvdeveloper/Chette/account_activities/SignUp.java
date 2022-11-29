package com.nvdeveloper.Chette.account_activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.nvdeveloper.Chette.R;
import com.nvdeveloper.Chette.SplashScreen;

public class SignUp extends AppCompatActivity {

    Button signUp;
    EditText username, email, phone;
    com.hbb20.CountryCodePicker ccp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        getSupportActionBar().hide();
        setStatusBarColor();

        username = findViewById(R.id.username);
        email = findViewById(R.id.email);
        phone = findViewById(R.id.phone);
        ccp = findViewById(R.id.country_Code);

        signUp = findViewById(R.id.sign_up);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String u_username = username.getText().toString().trim();
                String u_email = email.getText().toString().trim();
                String u_phone = phone.getText().toString().trim();
                String u_ccp = ccp.getTextView_selectedCountry().getText().toString().trim();

                //checking if any field is empty
                if(u_username.isEmpty() || u_email.isEmpty() || u_phone.isEmpty() || u_ccp.isEmpty()){
                    Toast.makeText(SignUp.this, "Can not submit blank fields", Toast.LENGTH_SHORT).show();
                }else{
                    //removing first three characters after concatenating with the country code
                    u_phone = (u_ccp + u_phone);
                    u_phone = u_phone.substring(3);

                    String finalU_phone = u_phone;

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(getApplicationContext(), OtpVerification.class);
                            intent.putExtra("username", u_username);
                            intent.putExtra("email", u_email);
                            intent.putExtra("phoneNumber", finalU_phone);

                            startActivity(intent);
                        }
                    }, 4000);

                    AlertDialog alert;
                    AlertDialog.Builder builder = new AlertDialog.Builder(SignUp.this);
                    View dialog_view = getLayoutInflater().inflate(R.layout.fetching_code_dialog, null);

                    builder.setView(dialog_view);

                    alert = builder.create();

                    alert.show();

                }
            }
        });

    }

    private void setStatusBarColor(){
        getSupportActionBar().hide();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        android.graphics.drawable.Drawable background = getApplicationContext().getResources().getDrawable(R.drawable.gradient_status);
        getWindow().setBackgroundDrawable(background);
    }
}