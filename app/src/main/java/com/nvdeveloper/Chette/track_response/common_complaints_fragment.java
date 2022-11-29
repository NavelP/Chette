package com.nvdeveloper.Chette.track_response;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nvdeveloper.Chette.R;
import com.nvdeveloper.Chette.StoreFilesLocally;
import com.nvdeveloper.Chette.user.MainActivityUser;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class common_complaints_fragment extends Fragment implements ManipulateComplaints {
    String tab_type;
    String department;

    public common_complaints_fragment(String tab_type, String department) {
        this.tab_type = tab_type;
        this.department = department;
    }

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://chette-72742-default-rtdb.firebaseio.com/");
    ArrayList<complaint_p> complaints;
    ArrayList<complaint_p> trending_compaints;
    complaints_adapter adapterTrending, adapterOther;

    LinearLayout no_complaint_alert;

    RecyclerView trending, other;

    StoreFilesLocally storeFilesLocally;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.complaints_common_layout, container, false);

        complaints = new ArrayList<>();
        trending_compaints = new ArrayList<>();

        storeFilesLocally = new StoreFilesLocally(getContext());

        trending = view.findViewById(R.id.trending_complaints_recycle_view);
        other = view.findViewById(R.id.other_complaints_recycle_view);

        no_complaint_alert = view.findViewById(R.id.no_complaints_alert);

        trending.setHasFixedSize(true);
        other.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManagerTrending = new LinearLayoutManager(getContext());
        linearLayoutManagerTrending.setOrientation(RecyclerView.HORIZONTAL);
        trending.setLayoutManager(linearLayoutManagerTrending);

        LinearLayoutManager linearLayoutManagerOther = new LinearLayoutManager(getContext());
        linearLayoutManagerOther.setOrientation(RecyclerView.VERTICAL);
        other.setLayoutManager(linearLayoutManagerOther);

        adapterTrending = new complaints_adapter(getContext(), trending_compaints, "trending",
                common_complaints_fragment.this, tab_type);
        trending.setAdapter(adapterTrending);

        adapterOther = new complaints_adapter(getContext(), complaints, "other",
                common_complaints_fragment.this, tab_type);
        other.setAdapter(adapterOther);

        FetchComplaints();

        return view;
    }

    public void SortTrending() {
        trending_compaints.clear();
        complaints.sort(new Comparator<complaint_p>() {
            public int compare(complaint_p m1, complaint_p m2) {
                if (Integer.valueOf(m1.getBoosts()) == Integer.valueOf(m2.getBoosts())) {
                    return 0;
                } else if (Integer.parseInt(m1.getBoosts()) > Integer.parseInt(m2.getBoosts())) {
                    return -1;
                } else {
                    return 1;
                }
            }
        });

        for (complaint_p c : complaints) {
            trending_compaints.add(c);

            if (trending_compaints.size() == 3) {
                break;
            }
        }
    }

    private void FetchComplaints() {
        if (MainActivityUser.INTERNET_CONNECTION_STATUS.equals("CONNECTED")) {
            if (tab_type.equals("my_complaints")) {
                if (department.equals("ALL")) {
                    databaseReference.child("complaints").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            complaints.clear();
                            for (DataSnapshot image_text : snapshot.getChildren()) {
                                for (DataSnapshot department : image_text.getChildren()) {
                                    for (DataSnapshot day : department.getChildren()) {
                                        if (day.hasChild(MainActivityUser.phone)) {
                                            for (DataSnapshot daily_posts_id : day.child(MainActivityUser.phone).getChildren()) {
                                                if (image_text.getKey().equals("images")) {
                                                    complaints.add(new complaint_p(
                                                            department.getKey(), "", daily_posts_id.child("imageUrl").getValue(String.class),
                                                            daily_posts_id.child("caption").getValue(String.class), "0", day.getKey(),
                                                            daily_posts_id.child("time_send").getValue(String.class), MainActivityUser.phone, daily_posts_id.getKey(),
                                                            daily_posts_id.child("boosts").getValue(String.class) + "")
                                                    );
                                                }
                                            }
                                        }
                                    }
                                }
                            }


                            if (complaints.size() == 0) {
                                no_complaint_alert.setVisibility(View.VISIBLE);
                            } else {
                                no_complaint_alert.setVisibility(View.GONE);
                            }


                            SortTrending();

                            adapterTrending.notifyDataSetChanged();
                            adapterOther.notifyDataSetChanged();

                            //inserting the fetched data into our local storage
                            for (complaint_p c : complaints) {
                                storeFilesLocally.InsertComplaint(
                                        c.getComplaint_department_name(), c.getComplaint_text(), c.getBoosts(), MainActivityUser.phone
                                );
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                } else {

                    databaseReference.child("complaints").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            complaints.clear();
                            for (DataSnapshot image_text : snapshot.getChildren()) {
                                for (DataSnapshot db_department : image_text.getChildren()) {
                                    if (db_department.getKey().equals(department)) {
                                        for (DataSnapshot day : db_department.getChildren()) {
                                            if (day.hasChild(MainActivityUser.phone)) {
                                                for (DataSnapshot daily_posts_id : day.child(MainActivityUser.phone).getChildren()) {
                                                    if (image_text.getKey().equals("images")) {
                                                        complaints.add(new complaint_p(
                                                                department, "", daily_posts_id.child("imageUrl").getValue(String.class),
                                                                daily_posts_id.child("caption").getValue(String.class), "0", day.getKey(),
                                                                daily_posts_id.child("time_send").getValue(String.class), MainActivityUser.phone, daily_posts_id.getKey(),
                                                                daily_posts_id.child("boosts").getValue(String.class) + ""
                                                        ));
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }

                            if (complaints.size() == 0) {
                                no_complaint_alert.setVisibility(View.VISIBLE);
                            } else {
                                no_complaint_alert.setVisibility(View.GONE);
                            }

                            SortTrending();

                            adapterTrending.notifyDataSetChanged();
                            adapterOther.notifyDataSetChanged();

                            //inserting the fetched data into our local storage
                            for (complaint_p c : complaints) {
                                storeFilesLocally.InsertComplaint(
                                        c.getComplaint_department_name(), c.getComplaint_text(), c.getBoosts(), MainActivityUser.phone
                                );
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            } else {
                if (department.equals("ALL")) {
                    databaseReference.child("complaints").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            complaints.clear();
                            for (DataSnapshot image_text : snapshot.getChildren()) {
                                for (DataSnapshot department : image_text.getChildren()) {
                                    for (DataSnapshot day : department.getChildren()) {
                                        for (DataSnapshot chette_user : day.getChildren()) {
                                            for (DataSnapshot daily_posts_id : chette_user.getChildren()) {
                                                if (image_text.getKey().equals("images")) {
                                                    complaints.add(new complaint_p(
                                                            department.getKey(), "", daily_posts_id.child("imageUrl").getValue(String.class),
                                                            daily_posts_id.child("caption").getValue(String.class), "0", day.getKey(),
                                                            daily_posts_id.child("time_send").getValue(String.class), chette_user.getKey(), daily_posts_id.getKey(),
                                                            daily_posts_id.child("boosts").getValue(String.class) + ""
                                                    ));
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            if (complaints.size() == 0) {
                                no_complaint_alert.setVisibility(View.VISIBLE);
                            } else {
                                no_complaint_alert.setVisibility(View.GONE);
                            }

                            SortTrending();

                            adapterTrending.notifyDataSetChanged();
                            adapterOther.notifyDataSetChanged();

                            //inserting the fetched data into our local storage
                            for (complaint_p c : complaints) {
                                storeFilesLocally.InsertComplaint(
                                        c.getComplaint_department_name(), c.getComplaint_text(), c.getBoosts(), MainActivityUser.phone
                                );
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                } else {

                    databaseReference.child("complaints").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            complaints.clear();
                            for (DataSnapshot image_text : snapshot.getChildren()) {
                                for (DataSnapshot db_department : image_text.getChildren()) {
                                    if (db_department.getKey().equals(department)) {
                                        for (DataSnapshot day : db_department.getChildren()) {
                                            for (DataSnapshot chette_user : day.getChildren()) {
                                                for (DataSnapshot daily_posts_id : chette_user.getChildren()) {
                                                    if (image_text.getKey().equals("images")) {
                                                        complaints.add(new complaint_p(
                                                                department, "", daily_posts_id.child("imageUrl").getValue(String.class),
                                                                daily_posts_id.child("caption").getValue(String.class), "0", day.getKey(),
                                                                daily_posts_id.child("time_send").getValue(String.class), chette_user.getKey(), daily_posts_id.getKey(),
                                                                daily_posts_id.child("boosts").getValue(String.class) + ""
                                                        ));
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            if (complaints.size() == 0) {
                                no_complaint_alert.setVisibility(View.VISIBLE);
                            } else {
                                no_complaint_alert.setVisibility(View.GONE);
                            }

                            SortTrending();

                            adapterTrending.notifyDataSetChanged();
                            adapterOther.notifyDataSetChanged();

                            //inserting the fetched data into our local storage
                            for (complaint_p c : complaints) {
                                storeFilesLocally.InsertComplaint(
                                        c.getComplaint_department_name(), c.getComplaint_text(), c.getBoosts(), MainActivityUser.phone
                                );
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        } else if (MainActivityUser.INTERNET_CONNECTION_STATUS.equals("DISCONNECTED")) {
            if (tab_type.equals("my_complaints")) {
                if (department.equals("ALL")) {
                    complaints.clear();
                    complaints = storeFilesLocally.getData(MainActivityUser.phone, "");
                }else{
                    complaints.clear();
                    complaints = storeFilesLocally.getData(MainActivityUser.phone, department);
                }


            } else {
                if (department.equals("ALL")) {
                    complaints.clear();
                    complaints = storeFilesLocally.getData("", "");
                }else{
                    complaints.clear();
                    complaints = storeFilesLocally.getData("", department);
                }


            }
            if (complaints.size() == 0) {
                no_complaint_alert.setVisibility(View.VISIBLE);
            } else {
                no_complaint_alert.setVisibility(View.GONE);
            }
            SortTrending();
            adapterTrending.notifyDataSetChanged();
            adapterOther.notifyDataSetChanged();
        }

}

    @Override
    public void MakeABoost(String image_text, String department, String day, String chette_user, String complaint_db_id) {

        databaseReference.child("complaints").child(image_text).child(department).
                child(day).child(chette_user).child(complaint_db_id).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String current_boost = snapshot.child("boosts").getValue(String.class) + "";

                        int add_boost = (Integer.parseInt(current_boost) + 1);

                        snapshot.getRef().child("boosts").setValue(add_boost + "").addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(getContext(), "Complaint boosted successfully!", Toast.LENGTH_SHORT).show();
                                complaints.clear();
                                FetchComplaints();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getContext(), "Could not boost Complaint because " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getContext(), "Could not boost because " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void DeleteAComplaint(String image_text, String department, String day, String chette_user, String complaint_db_id) {
        AlertDialog alertDialog
                = (new AlertDialog.Builder(getContext())
                .setTitle("Delete Complaint").setMessage("Are you sure you want to delete this Post")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        databaseReference.child("complaints").child(image_text).child(department).
                                child(day).child(chette_user).child(complaint_db_id).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(getContext(), "Deleted Successfully", Toast.LENGTH_SHORT).show();
                                        complaints.clear();
                                        FetchComplaints();
                                        dialogInterface.dismiss();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getContext(), "Could not delete because " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                        dialogInterface.dismiss();
                                    }
                                });
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })).create();

        alertDialog.show();
    }

    @Override
    public void EnlargeOnClick(String passed_department_name, String image_url, String image_caption, String Votes) {
        View v = getLayoutInflater().inflate(R.layout.enlarged_layout_view, null);
        AlertDialog alertDialog = (new AlertDialog.Builder(getContext()).setView(v).setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        })).create();

        TextView department_name = v.findViewById(R.id.department_name);
        department_name.setText(passed_department_name);

        ImageView image = v.findViewById(R.id.image_complaint);
        Picasso.with(getContext()).load(image_url).into(image);

        TextView caption = v.findViewById(R.id.image_caption);
        caption.setText(image_caption);

        TextView boost_num = v.findViewById(R.id.num_boosts);
        boost_num.setText(Votes);

        alertDialog.show();
    }

}
