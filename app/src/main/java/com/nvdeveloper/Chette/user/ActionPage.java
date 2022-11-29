package com.nvdeveloper.Chette.user;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.nvdeveloper.Chette.R;
import com.nvdeveloper.Chette.actions.ReadConstitution2010;
import com.nvdeveloper.Chette.actions.forward_action_to_departments;

public class ActionPage extends Fragment {

    String username, email, phone;
    Context context;
    ManipulateFragments manipulateFragments;

    public ActionPage(String username, String email, String phone, Context context, ManipulateFragments manipulateFragments) {
        this.username = username;
        this.email = email;
        this.phone = phone;
        this.context = context;
        this.manipulateFragments = manipulateFragments;
    }

    FloatingActionButton plus, call, camera, message, constitution;
    Animation fromBottom, toBottom, rotateOpen, rotateClose;

    boolean isOpen = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.action_page_activity, container, false);

        //FAB actions
        InitializeFABS(view);

        manipulateFragments.changeFragment(R.id.action_page_frame_layout, new action_page_main_fragment(
                username, email, phone, context, plus
        ));

        return view;
    }
    private void InitializeFABS(View view){
        plus = view.findViewById(R.id.plus);
        call = view.findViewById(R.id.call);
        camera = view.findViewById(R.id.camera);
        message = view.findViewById(R.id.sms);
        constitution = view.findViewById(R.id.constitution);


        fromBottom = AnimationUtils.loadAnimation(context, R.anim.from_bottom_anim);
        toBottom = AnimationUtils.loadAnimation(context, R.anim.to_bottom_anim);
        rotateOpen = AnimationUtils.loadAnimation(context, R.anim.rotate_open_anim);
        rotateClose = AnimationUtils.loadAnimation(context, R.anim.rotate_close_anim);

        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setFABS();
            }
        });

        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setFABS();
                makeACall();
            }
        });

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setFABS();
                takeAPhoto();
            }
        });

        message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setFABS();
                sendAMessage();
            }
        });

        constitution.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setFABS();
                ReadConstitution();
            }
        });

    }

    private void ReadConstitution() {
        manipulateFragments.changeFragment(R.id.action_page_frame_layout, new ReadConstitution2010());
    }

    private void sendAMessage() {
        manipulateFragments.changeFragment(R.id.action_page_frame_layout, new forward_action_to_departments("chat", manipulateFragments));
    }

    private void takeAPhoto() {
        manipulateFragments.changeFragment(R.id.action_page_frame_layout, new forward_action_to_departments("photo", manipulateFragments));
    }

    private void makeACall() {
        manipulateFragments.changeFragment(R.id.action_page_frame_layout, new forward_action_to_departments("call", manipulateFragments));
    }

    private void setFABS() {
        if(isOpen){
            plus.startAnimation(rotateClose);
            call.startAnimation(toBottom);
            call.setClickable(false);
            camera.startAnimation(toBottom);
            camera.setClickable(false);
            message.startAnimation(toBottom);
            message.setClickable(false);
            constitution.startAnimation(toBottom);
            constitution.setClickable(false);

            isOpen = false;
        }
        else{
            plus.startAnimation(rotateOpen);
            call.startAnimation(fromBottom);
            call.setClickable(true);
            camera.startAnimation(fromBottom);
            camera.setClickable(true);
            message.startAnimation(fromBottom);
            message.setClickable(true);
            constitution.startAnimation(fromBottom);
            constitution.setClickable(true);

            isOpen = true;
        }
    }

}
