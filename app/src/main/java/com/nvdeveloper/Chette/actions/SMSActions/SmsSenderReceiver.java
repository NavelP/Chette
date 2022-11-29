package com.nvdeveloper.Chette.actions.SMSActions;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nvdeveloper.Chette.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SmsSenderReceiver extends Fragment {

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://chette-72742-default-rtdb.firebaseio.com/");
    ImageView chat_profile_image;
    String name;
    String chat_profile_url;

    int profile_image;

    public SmsSenderReceiver(String name, String chat_profile) {
        if (chat_profile.equals("none"))
            profile_image = R.drawable.chette_splash_screen_image;

        this.chat_profile_url = chat_profile;
        this.name = name;
    }

    ArrayList<department_messages_p> department_chats;

    TextView department_name;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sms_sender_receiver_activity, container, false);

        department_name = view.findViewById(R.id.department_name);
        chat_profile_image = view.findViewById(R.id.chat_profile);

        if (chat_profile_url.equals("none")) {
            chat_profile_image.setImageResource(profile_image);
        }else{
            Picasso.with(getContext()).load(chat_profile_url).into(chat_profile_image);
        }

        department_name.setText(name);

        department_chats = new ArrayList<>();

        FetchChats();

        return view;
    }

    //method to fetch the chats
    private void FetchChats() {
        databaseReference.child("complaints").child("text").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot department : snapshot.getChildren()) {

                    for (DataSnapshot department_hotline : department.getChildren()) {
                        for (DataSnapshot message : department_hotline.getChildren()) {
                            department_chats.add(new department_messages_p(
                                    message.child("text").getValue(String.class), department_hotline.getKey(), message.child("time_send").getValue(String.class)
                            ));
                        }
                    }

                }

               /* adapter = new single_sms_display_adapter(getContext(), chats, displaySMSChats.this);
                recyclerView.setAdapter(adapter);*/
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
