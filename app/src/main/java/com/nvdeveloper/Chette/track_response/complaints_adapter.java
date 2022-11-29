package com.nvdeveloper.Chette.track_response;

import android.app.ActionBar;
import android.content.Context;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.nvdeveloper.Chette.R;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class complaints_adapter extends RecyclerView.Adapter<complaints_adapter.myViewHolder> {

    Context context;
    ArrayList<complaint_p> complaints;
    String complaint_recycle_view;
    ManipulateComplaints manipulateComplaints;
    String mine_or_others;

    public complaints_adapter(Context context, ArrayList<complaint_p> complaints, String complaint_recycle_view,
                              ManipulateComplaints manipulateComplaints, String mine_or_others) {
        this.context = context;
        this.complaints = complaints;
        this.complaint_recycle_view = complaint_recycle_view;
        this.manipulateComplaints = manipulateComplaints;
        this.mine_or_others = mine_or_others;
    }

    CardView holderCard;
    ImageView image;
    TextView dp_name;
    RelativeLayout rl;

    LinearLayout boost_layout, boost_num_layout;

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_complaint, parent, false);

        holderCard = view.findViewById(R.id.holderCard);
        image = view.findViewById(R.id.image_complaint);
        dp_name = view.findViewById(R.id.department_name);
        rl = view.findViewById(R.id.rl);

        boost_layout = view.findViewById(R.id.boost_layout);
        boost_num_layout = view.findViewById(R.id.boost_num_layout);

        if(complaint_recycle_view.equals("trending")){
            SetTrendingCardDimensions(150, 250);
        }else if(complaint_recycle_view.equals("other")){
            SetOtherCardDimensions();
        }

        return new myViewHolder(view);
    }

    private void SetTrendingCardDimensions(int WidthDimensions, int HeightDimensions){
        RecyclerView.LayoutParams lp = (RecyclerView.LayoutParams)holderCard.getLayoutParams();
        lp.width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, WidthDimensions, context.getResources().getDisplayMetrics());
        lp.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, HeightDimensions, context.getResources().getDisplayMetrics());
        holderCard.setLayoutParams(lp);

    }

    private void SetOtherCardDimensions(){
        RecyclerView.LayoutParams params = new RecyclerView.LayoutParams(
                RecyclerView.LayoutParams.WRAP_CONTENT, RecyclerView.LayoutParams.WRAP_CONTENT
        );

        params.setMargins(5, 10, 5, 10);
        holderCard.setLayoutParams(params);

        LinearLayout.LayoutParams action_buttons = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT
        );
        action_buttons.setMarginEnd(90);
        boost_layout.setLayoutParams(action_buttons);

        rl.removeView(image);

        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                500);

        lp.addRule(RelativeLayout.BELOW, dp_name.getId());
        rl.addView(image, lp);
       // lp.removeRule(RelativeLayout.BELOW);

    }
    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        String type = "";
        holder.department_name.setText(complaints.get(position).getComplaint_department_name());
        if(complaints.get(position).getComplaint_text().equals("")){
            type = "images";
            holder.text_complaint.setVisibility(View.GONE);
            if(complaints.get(position).getComplaint_image_url().equals("")){
                holder.image_complaint.setImageResource(R.drawable.chat_bg);
            }else{
                Picasso.with(context).load(complaints.get(position).getComplaint_image_url()).into(holder.image_complaint);
            }
            if(complaints.get(position).getImage_caption().equals("")){
                complaints.get(position).setImage_caption("No Caption");
            }
            holder.image_caption.setText(complaints.get(position).getImage_caption());
        }else{
            type = "text";
            holder.image_complaint.setVisibility(View.GONE);
            holder.text_complaint.setText(complaints.get(position).getComplaint_text());
        }


        holder.num_boosts.setText(complaints.get(position).getBoosts() + " boosts");

        String finalType = type;
        if(mine_or_others.equals("my_complaints")){
            holder.boost_image.setImageResource(R.drawable.ic_baseline_cancel_24);
            holder.boost_text.setText("pull down");

            holder.boost.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    manipulateComplaints.DeleteAComplaint(finalType, complaints.get(holder.getAdapterPosition()).getComplaint_department_name(),
                            complaints.get(holder.getAdapterPosition()).getDate(), complaints.get(holder.getAdapterPosition()).getUser_phone(),
                            complaints.get(holder.getAdapterPosition()).getComplaint_db_id());
                }
            });

        }else if(mine_or_others.equals("other_complaints")){
            holder.boost_image.setImageResource(R.drawable.ic_baseline_campaign_24);
            holder.boost_text.setText("boost");

            holder.boost.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    manipulateComplaints.MakeABoost(finalType, complaints.get(holder.getAdapterPosition()).getComplaint_department_name(),
                            complaints.get(holder.getAdapterPosition()).getDate(), complaints.get(holder.getAdapterPosition()).getUser_phone(),
                            complaints.get(holder.getAdapterPosition()).getComplaint_db_id());
                }
            });

        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                manipulateComplaints.EnlargeOnClick(holder.department_name.getText().toString(),
                        complaints.get(holder.getAdapterPosition()).getComplaint_image_url(), complaints.get(holder.getAdapterPosition()).getImage_caption(),
                        complaints.get(holder.getAdapterPosition()).getBoosts() + " boosts");
            }
        });

    }

    @Override
    public int getItemCount() {
        return complaints.size();
    }

    public class myViewHolder extends RecyclerView.ViewHolder{
        TextView department_name, text_complaint, image_caption;
        ImageView image_complaint, boost_image;
        LinearLayout boost;
        TextView num_boosts, boost_text;
        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            department_name = itemView.findViewById(R.id.department_name);
            text_complaint = itemView.findViewById(R.id.text_complaint);
            image_caption = itemView.findViewById(R.id.image_caption);

            image_complaint = itemView.findViewById(R.id.image_complaint);
            boost = itemView.findViewById(R.id.boost_layout);
            num_boosts = itemView.findViewById(R.id.num_boosts);

            boost_text = itemView.findViewById(R.id.boostText);
            boost_image = itemView.findViewById(R.id.boost_image);
        }
    }
}
