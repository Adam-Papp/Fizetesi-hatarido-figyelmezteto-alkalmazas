package com.example.fizetsihatridfigyelmeztetalkalmazs.ui.Beallitasok;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fizetsihatridfigyelmeztetalkalmazs.R;

public class BeallitasokFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_beallitasok, container, false);


        return root;
    }
}