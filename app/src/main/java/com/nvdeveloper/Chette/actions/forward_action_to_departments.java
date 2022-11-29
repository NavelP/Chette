package com.nvdeveloper.Chette.actions;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Telephony;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

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
import com.nvdeveloper.Chette.actions.ImageActions.ImageCompressorAndUploader;
import com.nvdeveloper.Chette.actions.SMSActions.displaySMSChats;
import com.nvdeveloper.Chette.user.ManipulateFragments;
import com.nvdeveloper.Chette.user.action_page_main_fragment;

import java.util.ArrayList;
import java.util.Arrays;

public class forward_action_to_departments extends Fragment implements ManipulateActions {

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://chette-72742-default-rtdb.firebaseio.com/");
    ArrayList<departments_p> departments;
    departments_adapter adapter;
    RecyclerView department_recycleView;

    String action;
    ManipulateFragments manipulateFragments;

    EditText search_department;

    ArrayList<departments_p> searched_list;

    public forward_action_to_departments(String action, ManipulateFragments manipulateFragments) {
        this.action = action;
        this.manipulateFragments = manipulateFragments;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.display_departments, container, false);

        department_recycleView = view.findViewById(R.id.departmentLists);
        search_department = view.findViewById(R.id.search_department);
        searched_list = new ArrayList<>();

        SearchDepartment();

        FetChDepartmentsData();
        return view;
    }

    private void SearchDepartment(){
        search_department.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                search_department.setHint("    Search a Department");
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String inputted_department_name = search_department.getText().toString().trim();
                searched_list.clear();

                for(departments_p depart : departments){
                    if(depart.getDepartment_name().toLowerCase().startsWith(inputted_department_name.toLowerCase())){
                        searched_list.add(depart);
                    }
                }
                adapter = new departments_adapter(searched_list, getContext(), action, forward_action_to_departments.this);
                department_recycleView.setAdapter(adapter);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                search_department.setHint("    Search a Department");
            }
        });
    }

    @FunctionalInterface
    interface FetchHotlines {
        String[] getHotlines();
    }

    private void FetChDepartmentsData() {
        departments = new ArrayList<>();

        databaseReference.child("departments").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                FetchHotlines fetchHotlines;
                for (DataSnapshot department : snapshot.getChildren()) {
                    departments.add(new departments_p(
                            department.child("name").getValue(String.class), (fetchHotlines = () -> {
                        String[] hotlines = new String[(int) department.getChildrenCount()];
                        int i = 0;
                        for (DataSnapshot hotline : department.getChildren()) {
                            if (hotline.getKey().equals("name")) {
                                hotlines[0] = department.getKey();
                                i++;
                                continue;
                            }
                            if (hotline.getValue(String.class).equals("")) {
                                hotlines = Arrays.copyOf(hotlines, hotlines.length - 1);
                                continue;
                            }

                            hotlines[i] = hotline.getValue(String.class);
                            i++;
                        }
                        return hotlines;
                    }).getHotlines()
                    ));

                }
                department_recycleView.setHasFixedSize(true);

                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
                linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
                department_recycleView.setLayoutManager(linearLayoutManager);

                adapter = new departments_adapter(departments, getContext(), action, forward_action_to_departments.this);
                department_recycleView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void MakeDefaultPhoneCall(String phone_number) {
        startActivity(new Intent(Intent.ACTION_DIAL,
                Uri.parse("tel: " + phone_number)));
    }

    @Override
    public void MakeChetteCall(String phone_number) {

    }

    @Override
    public void PostGalleryPhoto(String phone_number, String department_name) {
        manipulateFragments.changeFragment(R.id.action_page_frame_layout, new ImageCompressorAndUploader("gallery", department_name, phone_number,
                forward_action_to_departments.this));
    }

    @Override
    public void PostCameraPhoto(String phone_number, String department_name) {
        manipulateFragments.changeFragment(R.id.action_page_frame_layout, new ImageCompressorAndUploader("photo", department_name, phone_number,
                forward_action_to_departments.this));
    }

    @Override
    public void SendDefaultSms(String phone_number) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) // At least KitKat
        {
            String defaultSmsPackageName = Telephony.Sms.getDefaultSmsPackage(getContext()); // Need to change the build to API 19

            Intent sendIntent = new Intent(Intent.ACTION_SEND);
            sendIntent.setType("text/plain");
            sendIntent.putExtra("address",phone_number);
            sendIntent.putExtra(Intent.EXTRA_TEXT, "A Report submitted to the Authorities by A Chette Application USer");

            if (defaultSmsPackageName != null)// Can be null in case that there is no default, then the user would be able to choose
            // any app that support this intent.
            {
                sendIntent.setPackage(defaultSmsPackageName);
            }
            startActivity(sendIntent);

        }
        else // For early versions, do what worked for you before.
        {
            Intent smsIntent = new Intent(android.content.Intent.ACTION_VIEW);
            smsIntent.setType("vnd.android-dir/mms-sms");
            smsIntent.putExtra("address",phone_number);
            smsIntent.putExtra("sms_body","A Report submitted to the Authorities by A Chette Application USer");
            startActivity(smsIntent);
        }
    }

    @Override
    public void SendChetteSms(String phone_number) {
        manipulateFragments.changeFragment(R.id.action_page_frame_layout, new action_page_main_fragment(phone_number));
        getActivity().getSupportFragmentManager().beginTransaction().replace(
                R.id.chette_chat_frame_layout, new displaySMSChats()
        ).commit();
    }

}
