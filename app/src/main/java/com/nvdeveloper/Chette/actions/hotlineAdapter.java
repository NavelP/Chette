package com.nvdeveloper.Chette.actions;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nvdeveloper.Chette.R;

import java.util.ArrayList;

public class hotlineAdapter extends RecyclerView.Adapter<hotlineAdapter.MyHolder> {
    ArrayList<hotline_p> hotlines;
    Context context;
    String action;
    ManipulateActions manipulateActions;
    String department_name;

    public hotlineAdapter(ArrayList<hotline_p> hotlines, Context context, String action, ManipulateActions manipulateActions, String department_name) {
        this.hotlines = hotlines;
        this.context = context;
        this.action = action;
        this.manipulateActions = manipulateActions;
        this.department_name = department_name;
    }

    @NonNull
    @Override
    public hotlineAdapter.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_hotline, parent, false);

        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull hotlineAdapter.MyHolder holder, int position) {
        holder.hotlineNumber.setText(hotlines.get(position).getHotline_number());

        if(action.equals("call")){
            holder.videoCall.setVisibility(View.GONE);
            holder.inApp.setImageResource(R.drawable.ic_baseline_call_24);

            holder.inApp.setVisibility(View.GONE);
            holder.InAppText.setVisibility(View.GONE);

            holder.outApp.setImageResource(R.drawable.ic_baseline_call_24);
            holder.OutAppText.setText("Default Phone");
            holder.InAppText.setText("Chette Call");

            holder.inApp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    manipulateActions.MakeChetteCall(holder.hotlineNumber.getText().toString().trim());
                }
            });

            holder.outApp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    manipulateActions.MakeDefaultPhoneCall(holder.hotlineNumber.getText().toString().trim());
                }
            });
        }
        else if(action.equals("chat")){
            holder.videoCall.setVisibility(View.GONE);

            holder.inApp.setVisibility(View.GONE);
            holder.InAppText.setVisibility(View.GONE);

            holder.inApp.setImageResource(R.drawable.ic_baseline_message_24);
            holder.outApp.setImageResource(R.drawable.ic_baseline_message_24);

            holder.OutAppText.setText("Default Sms");
            holder.InAppText.setText("Chette Chat");

            holder.inApp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    manipulateActions.SendChetteSms(holder.hotlineNumber.getText().toString().trim());
                }
            });

            holder.outApp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    manipulateActions.SendDefaultSms(holder.hotlineNumber.getText().toString().trim());
                }
            });
        }
        else if(action.equals("photo")){
            holder.videoCall.setVisibility(View.GONE);
            holder.inApp.setImageResource(R.drawable.ic_baseline_photo_album_24);
            holder.outApp.setImageResource(R.drawable.ic_baseline_photo_camera_24);
            holder.InAppText.setText("Gallery");
            holder.OutAppText.setText("Camera");

            holder.inApp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    manipulateActions.PostGalleryPhoto(holder.hotlineNumber.getText().toString().trim(), department_name);
                }
            });

            holder.outApp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    manipulateActions.PostCameraPhoto(holder.hotlineNumber.getText().toString().trim(), department_name);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return hotlines.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder{
        TextView hotlineNumber, InAppText, OutAppText;
        ImageView inApp, outApp;
        LinearLayout videoCall;
        public MyHolder(@NonNull View itemView) {
            super(itemView);

            hotlineNumber = itemView.findViewById(R.id.hotlineNumber);
            inApp = itemView.findViewById(R.id.InAppAction);
            outApp = itemView.findViewById(R.id.OutAppAction);
            videoCall = itemView.findViewById(R.id.videocall);
            InAppText = itemView.findViewById(R.id.inAppText);
            OutAppText = itemView.findViewById(R.id.outAppText);
        }
    }
}
