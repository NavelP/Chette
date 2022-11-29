package com.nvdeveloper.Chette.actions;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nvdeveloper.Chette.R;

import java.util.ArrayList;

public class departments_adapter extends RecyclerView.Adapter<departments_adapter.MyHolder> {
    ArrayList<departments_p> departments;
    Context context;

    hotlineAdapter adapter;
    ArrayList<hotline_p> hotlines;
    String action;
    ManipulateActions manipulateActions;

    public departments_adapter(ArrayList<departments_p> departments, Context context, String action, ManipulateActions manipulateActions) {
        this.departments = departments;
        this.context = context;
        this.action = action;
        this.manipulateActions = manipulateActions;
    }

    @NonNull
    @Override
    public departments_adapter.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_department_display, parent, false);

        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull departments_adapter.MyHolder holder, int position) {
        holder.departmentName.setText(departments.get(position).getDepartment_name());

        holder.hotlines.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        holder.hotlines.setLayoutManager(linearLayoutManager);

        hotlines = new ArrayList<>();

        for(String hotline : departments.get(position).getHotline_numbers()){
            hotlines.add(new hotline_p(hotline));
        }

        adapter = new hotlineAdapter(hotlines, context, action, manipulateActions, holder.departmentName.getText().toString().trim());
        holder.hotlines.setAdapter(adapter);
    }

    @Override
    public int getItemCount() {
        return departments.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder{
        TextView departmentName;
        RecyclerView hotlines;
        public MyHolder(@NonNull View itemView) {
            super(itemView);

            departmentName = itemView.findViewById(R.id.department_name);
            hotlines = itemView.findViewById(R.id.departmentHotlines);
        }
    }
}
