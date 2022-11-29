package com.nvdeveloper.Chette.actions.SMSActions;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nvdeveloper.Chette.R;
import com.nvdeveloper.Chette.user.ManipulateFragments;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class displaySMSChats extends Fragment implements ManipulateFragments {
    RecyclerView recyclerView;
    single_sms_display_adapter adapter;
    ArrayList<single_sms_chat_display_p> chats;

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://chette-72742-default-rtdb.firebaseio.com/");

    Calendar calendar;
    SimpleDateFormat simpleDateFormat;
    String myDate;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.display_sms_chats_activity, container, false);

        chats = new ArrayList<>();

        recyclerView = view.findViewById(R.id.allchatsdisplay);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        databaseReference.child("complaints").child("text").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot department : snapshot.getChildren()){
                    int message_count = 0;
                    for(DataSnapshot department_hotline : department.getChildren()){
                        message_count += department_hotline.getChildrenCount();
                    }
                    chats.add(new single_sms_chat_display_p(
                            "This is a sample text message that has been used for development",
                            (int)message_count + "",
                            getTime(), "12:30pm", R.drawable.chette_splash_screen_image, department.getKey()
                    ));
                }

                adapter = new single_sms_display_adapter(getContext(), chats, displaySMSChats.this);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        return view;
    }

    private String getTime() {
        calendar = Calendar.getInstance();

        simpleDateFormat = new SimpleDateFormat("HH:mm:ss");

        myDate = simpleDateFormat.format(calendar.getTime());

        return myDate;
    }

    @Override
    public void changeFragment(int frameLayoutId, Fragment fragment) {
        getActivity().getSupportFragmentManager().beginTransaction().replace(
                frameLayoutId, fragment
        ).commit();
    }

}
