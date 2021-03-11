package com.example.fizetsihatridfigyelmeztetalkalmazs.ui.Kezdolap;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.fizetsihatridfigyelmeztetalkalmazs.R;

public class KezdolapFragment extends Fragment {

    private KezdolapViewModel kezdolapViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        kezdolapViewModel =
                new ViewModelProvider(this).get(KezdolapViewModel.class);
        View root = inflater.inflate(R.layout.fragment_kezdolap, container, false);
        kezdolapViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
//                textView.setText(s);
            }
        });
        return root;
    }
}