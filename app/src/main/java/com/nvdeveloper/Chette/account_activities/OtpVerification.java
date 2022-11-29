package com.nvdeveloper.Chette.account_activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nvdeveloper.Chette.user.MainActivityUser;
import com.nvdeveloper.Chette.R;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class OtpVerification extends AppCompatActivity {

    FirebaseAuth myAuth;
    Button verify_code;
    EditText ver_code;
    TextView sendPhone, resendCode;

    String username, email, phoneNumber;
    String verificationID;

    String TAG = "myP";
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://chette-72742-default-rtdb.firebaseio.com/");

    SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_verification);

        getSupportActionBar().hide();

        sharedPreferences  = getApplicationContext().getSharedPreferences("ACCOUNT_EXISTENCE", Context.MODE_PRIVATE);

        setStatusBarColor();

        verify_code = findViewById(R.id.verify_button);
        ver_code = findViewById(R.id.ver_code);

        sendPhone = findViewById(R.id.sendPhone);
        resendCode = findViewById(R.id.resendCode);

        Bundle bundle = getIntent().getExtras();
        assert bundle != null;

        username = bundle.getString("username");
        email = bundle.getString("email");
        phoneNumber = bundle.getString("phoneNumber");

        sendPhone.setText("A verification code will be send to " + phoneNumber);

        //for verifying the code
        verify_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user_otp = ver_code.getText().toString().trim();
                if(TextUtils.isEmpty(user_otp)){
                    Toast.makeText(OtpVerification.this, "Please enter the code", Toast.LENGTH_SHORT).show();
                }else{
                    verifySendCode(user_otp);
                }
            }
        });

        //for resending the code
        resendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendVerificationCode(phoneNumber);
            }
        });

        myAuth = FirebaseAuth.getInstance();

        sendVerificationCode(phoneNumber);

    }

    private void verifySendCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationID, code);
        signInByCredentials(credential);
    }

    private void signInByCredentials(PhoneAuthCredential credential) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        HashMap<String, String> reg_user = new HashMap<>();
        reg_user.put("username", username);
        reg_user.put("email", email);

        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    databaseReference.child("users").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.hasChild(phoneNumber)){
                                Toast.makeText(OtpVerification.this, "User with that phone number already exists", Toast.LENGTH_SHORT).show();
                            }else{
                                snapshot.getRef().child(phoneNumber).setValue(reg_user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(OtpVerification.this, "Account created Successfully!", Toast.LENGTH_SHORT).show();

                                        sharedPreferences.edit().putString("phoneNumber", phoneNumber).apply();
                                        sharedPreferences.edit().putString("email", email).apply();
                                        sharedPreferences.edit().putString("username", username).apply();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(OtpVerification.this, "Failed to create Account because " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(OtpVerification.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

                    Intent intent = new Intent(getApplicationContext(), MainActivityUser.class);
                    intent.putExtra("username", username);
                    intent.putExtra("email", email);
                    intent.putExtra("phoneNumber", phoneNumber);

                    startActivity(intent);

                    finish();

                }else{
                    Toast.makeText(OtpVerification.this, "Failed! Wrong verification code", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void sendVerificationCode(String phoneNumber) {

        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(myAuth)
                        .setPhoneNumber(phoneNumber)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
            mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential credential) {

            final String code = credential.getSmsCode();

            if(code != null){
                verifySendCode(code);
            }
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Toast.makeText(OtpVerification.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCodeSent(@NonNull String s,
                @NonNull PhoneAuthProvider.ForceResendingToken token) {
           super.onCodeSent(s, token);

           verificationID = s;
        }
    };

    private void setStatusBarColor(){
        getSupportActionBar().hide();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        android.graphics.drawable.Drawable background = getApplicationContext().getResources().getDrawable(R.drawable.gradient_status);
        getWindow().setBackgroundDrawable(background);
    }
}