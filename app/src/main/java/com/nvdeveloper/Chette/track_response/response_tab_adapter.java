package com.nvdeveloper.Chette.track_response;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

public class response_tab_adapter extends FragmentStatePagerAdapter {
    ArrayList<String> tab_names = new ArrayList<>();
    String department;

    public response_tab_adapter(@NonNull FragmentManager fm, String department) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.department = department;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Fragment tabs = null;

        switch (position){
            case 0:
                tabs = new common_complaints_fragment("my_complaints", department);
                break;
            case 1:
                tabs = new common_complaints_fragment("other_complaints", department);
                break;
        }
        return tabs;
    }

    @Override
    public int getCount() {
        return tab_names.size();
    }

    public void add(){
        tab_names.add("My Complaints");
        tab_names.add("Other Complaints");
    }

    @Override
    public CharSequence getPageTitle(int position){
        return tab_names.get(position);
    }
}

