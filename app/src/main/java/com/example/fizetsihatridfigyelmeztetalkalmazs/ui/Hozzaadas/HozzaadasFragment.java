package com.example.fizetsihatridfigyelmeztetalkalmazs.ui.Hozzaadas;

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

public class HozzaadasFragment extends Fragment {

    private HozzaadasModel hozzaadasModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        hozzaadasModel =
                new ViewModelProvider(this).get(HozzaadasModel.class);
        View root = inflater.inflate(R.layout.fragment_hozzaadas, container, false);
        final TextView textView = root.findViewById(R.id.text_gallery);
        hozzaadasModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}