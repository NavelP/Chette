package com.nvdeveloper.Chette.user;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.nvdeveloper.Chette.R;
import com.nvdeveloper.Chette.actions.SMSActions.displaySMSChats;

public class action_page_main_fragment extends Fragment {

    String username, email, phone;
    Context context;

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://chette-72742-default-rtdb.firebaseio.com/");

    public action_page_main_fragment(String username, String email, String phone, Context context, FloatingActionButton plus) {
        this.username = username;
        this.email = email;
        this.phone = phone;
        this.context = context;
        this.plus = plus;
    }

    String hotline_chat_number;

    public action_page_main_fragment(String hotline_chat_number){
        this.hotline_chat_number = hotline_chat_number;
    }

    EditText usernameText, emailText, phoneText;
    ImageView u_edit, e_edit, p_edit;
    Button delete, edit;

    RelativeLayout account_visibility;
    ImageView arrow;
    CardView account_info;

    TextView account_dir;
    FloatingActionButton plus;

    SharedPreferences sharedPreferences;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_layout_action_page, container, false);

        sharedPreferences = getContext().getSharedPreferences("ACCOUNT_EXISTENCE", Context.MODE_PRIVATE);

        getActivity().getSupportFragmentManager().beginTransaction().replace(
                R.id.chette_chat_frame_layout, new displaySMSChats()
        ).commit();

        InitializeLayout(view);

        return view;
    }

    private void InitializeLayout(View view){
        account_dir = view.findViewById(R.id.account_dir);


        usernameText = view.findViewById(R.id.username);
        emailText = view.findViewById(R.id.email);
        phoneText = view.findViewById(R.id.phone);

        u_edit = view.findViewById(R.id.edit_username);
        e_edit = view.findViewById(R.id.edit_email);
        p_edit = view.findViewById(R.id.edit_phone);

        delete = view.findViewById(R.id.delete_account);

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog alertDialog = new AlertDialog.Builder(getContext()).
                        setTitle("Deleting Account").setMessage("Are you sure you want to delete this Account?")
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        }).setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                databaseReference.child("users").child(phone).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(getContext(), "Account Deleted", Toast.LENGTH_SHORT).show();

                                        sharedPreferences.edit().putString("phoneNumber", "").apply();
                                        sharedPreferences.edit().putString("email", "").apply();
                                        sharedPreferences.edit().putString("username", "").apply();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getContext(), "Could not delete Account because " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });

                                dialogInterface.dismiss();
                            }
                        }).create();

                alertDialog.show();
            }
        });

        edit = view.findViewById(R.id.Edit_account);

        edit.setEnabled(false);

        usernameText.setText(username);
        emailText.setText(email);
        phoneText.setText(phone);

        account_visibility = view.findViewById(R.id.account_visibility);
        arrow = view.findViewById(R.id.arrow);
        account_info = view.findViewById(R.id.account_info);

        account_visibility.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(account_info.getVisibility() == View.GONE){
                    account_info.setVisibility(View.VISIBLE);
                    arrow.setImageResource(R.drawable.ic_baseline_keyboard_arrow_down_24);
                    plus.setVisibility(View.INVISIBLE);
                    account_dir.setText("Click to Collapse Account Info");
                }else if(account_info.getVisibility() == View.VISIBLE){
                    account_info.setVisibility(View.GONE);
                    arrow.setImageResource(R.drawable.ic_baseline_keyboard_arrow_right_24);
                    plus.setVisibility(View.VISIBLE);
                    account_dir.setText("Click to View Account Info");
                }
            }
        });
    }
}
