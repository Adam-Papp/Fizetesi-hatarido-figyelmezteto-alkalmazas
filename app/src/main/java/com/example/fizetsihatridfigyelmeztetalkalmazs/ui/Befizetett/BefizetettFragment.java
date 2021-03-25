package com.example.fizetsihatridfigyelmeztetalkalmazs.ui.Befizetett;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fizetsihatridfigyelmeztetalkalmazs.DataBaseHelper;
import com.example.fizetsihatridfigyelmeztetalkalmazs.MyRecyclerViewAdapter;
import com.example.fizetsihatridfigyelmeztetalkalmazs.R;
import com.example.fizetsihatridfigyelmeztetalkalmazs.Szamla;

import java.util.List;

public class BefizetettFragment extends Fragment {

    RecyclerView recyclerViewBefizetettSzamlak;
    DataBaseHelper dataBaseHelper;
    List<Szamla> listaSzamlak;
    ArrayAdapter arrayAdapterSzamlak;

    MyRecyclerViewAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_befizetett, container, false);

        recyclerViewBefizetettSzamlak = root.findViewById(R.id.recyclerViewBefizetettSzamlak);

        dataBaseHelper = new DataBaseHelper(getContext());
        listaSzamlak = dataBaseHelper.AdatbazisbolElvegzettekLekerese();
        recyclerViewBefizetettSzamlak.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new MyRecyclerViewAdapter(getContext(), listaSzamlak);
        recyclerViewBefizetettSzamlak.setAdapter(adapter);

        return root;
    }
}