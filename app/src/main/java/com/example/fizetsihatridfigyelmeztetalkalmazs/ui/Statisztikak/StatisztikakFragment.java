package com.example.fizetsihatridfigyelmeztetalkalmazs.ui.Statisztikak;

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
import com.example.fizetsihatridfigyelmeztetalkalmazs.ui.Kezdolap.KezdolapViewModel;

public class StatisztikakFragment extends Fragment {

    private StatisztikakViewModel statisztikakViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        statisztikakViewModel =
                new ViewModelProvider(this).get(StatisztikakViewModel.class);
        View root = inflater.inflate(R.layout.fragment_statisztikak, container, false);
        statisztikakViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
//                textView.setText(s);
            }
        });
        return root;
    }
}