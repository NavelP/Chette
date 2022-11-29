package com.nvdeveloper.Chette.track_response;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nvdeveloper.Chette.R;

import java.util.ArrayList;

public class TrackResponsePage extends Fragment{
    ViewPager viewPager;
    response_tab_adapter adapter;
    TabLayout tabLayout;

    Button button_select_department;

    String selected_department;

    ArrayList<String> department_names;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://chette-72742-default-rtdb.firebaseio.com/");

    FragmentManager fm;
    public TrackResponsePage(FragmentManager fm){
        this.fm = fm;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_track_response, container, false);

        department_names = new ArrayList<>();
        button_select_department = view.findViewById(R.id.track_response_select_department);

        //calling method to initialize tabs
        InitializeTabs(view, "ALL");
        //fill the department list
        FillDepartmentList(view);

        return view;
    }
    //method to fill Array List
    private void FillDepartmentList(View fragmentView){
        databaseReference.child("departments").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                department_names.add("ALL");
                for(DataSnapshot department_hotline : snapshot.getChildren()){
                    department_names.add(department_hotline.child("name").getValue(String.class));
                }

                button_select_department.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        CreatePopUpMenu(fragmentView);
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void InitializeTabs(View view, String department){
        tabLayout = view.findViewById(R.id.response_tab_layout);
        viewPager = view.findViewById(R.id.response_viewPager);

        adapter = new response_tab_adapter(fm, department);
        adapter.add();

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    //create the pop up menu
    private void CreatePopUpMenu(View view){
        PopupMenu popupMenu = new PopupMenu(getContext(), button_select_department);

        for(int i = 0; i < department_names.size(); i++){
            popupMenu.getMenu().add(department_names.get(i));
        }

        popupMenu.getMenuInflater().inflate(R.menu.department_names, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                selected_department = menuItem.getTitle().toString().trim();

                InitializeTabs(view, selected_department);
                return true;
            }
        });

        popupMenu.show();
    }

}
