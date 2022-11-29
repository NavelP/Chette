package com.nvdeveloper.Chette.actions;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.barteksc.pdfviewer.PDFView;
import com.nvdeveloper.Chette.R;

public class ReadConstitution2010 extends Fragment {
    PDFView pdf;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.read_constitution_activity, container, false);

        pdf = view.findViewById(R.id.view_book);
        pdf.fromAsset("constitution.pdf").load();
        return view;
    }
}
