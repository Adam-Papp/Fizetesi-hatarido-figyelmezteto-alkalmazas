package com.example.fizetsihatridfigyelmeztetalkalmazs.ui.Statisztikak;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.fizetsihatridfigyelmeztetalkalmazs.R;

public class StatisztikakFragment extends Fragment {


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_statisztikak, container, false);


        return root;
    }
}