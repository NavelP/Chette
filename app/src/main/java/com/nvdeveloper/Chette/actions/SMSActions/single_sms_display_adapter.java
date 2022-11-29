package com.nvdeveloper.Chette.actions.SMSActions;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nvdeveloper.Chette.R;
import com.nvdeveloper.Chette.user.ManipulateFragments;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class single_sms_display_adapter extends RecyclerView.Adapter<single_sms_display_adapter.MyHolder> {
    Context context;
    ArrayList<single_sms_chat_display_p> chats;
    ManipulateFragments manipulateFragments;

    public single_sms_display_adapter(Context context, ArrayList<single_sms_chat_display_p> chats, ManipulateFragments manipulateFragments) {
        this.context = context;
        this.chats = chats;
        this.manipulateFragments = manipulateFragments;
    }

    @NonNull
    @Override
    public single_sms_display_adapter.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_chat_display, parent, false);

        return new MyHolder(view);
    }

    @FunctionalInterface
    interface getImageUrl{
        String ProfileUrl();
    }
    @Override
    public void onBindViewHolder(@NonNull single_sms_display_adapter.MyHolder holder, int position) {
        if(chats.get(position).getImage_url() == null){
            holder.profile.setImageResource(chats.get(position).getImage());
        }else{
            Picasso.with(context).load(chats.get(position).getImage_url()).into(holder.profile);
        }
        holder.message_snippet.setText(chats.get(position).getMost_recent_message());

        holder.message_number.setText(chats.get(position).getNumber_of_messages());

        holder.time_received.setText(chats.get(position).getTime_sent());

        holder.last_seen.setText("Now");

        holder.name.setText(chats.get(position).getName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getImageUrl Profile_url;

                manipulateFragments.changeFragment(R.id.action_page_frame_layout, new SmsSenderReceiver(holder.name.getText().toString().trim()
                , (Profile_url = ()->{
                    if(chats.get(holder.getAdapterPosition()).getImage_url() == null){
                        return "none";
                    }else{
                        return chats.get(holder.getAdapterPosition()).getImage_url();
                    }
                }).ProfileUrl()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return chats.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder{
        CircleImageView profile;
        TextView message_snippet, last_seen, time_received, message_number, name;
        public MyHolder(@NonNull View itemView) {
            super(itemView);

            profile = itemView.findViewById(R.id.profile);
            message_snippet = itemView.findViewById(R.id.textMessageSnippet);
            last_seen = itemView.findViewById(R.id.last_seen);
            time_received = itemView.findViewById(R.id.time_received);
            message_number = itemView.findViewById(R.id.message_num);
            name = itemView.findViewById(R.id.name);
        }
    }
}
