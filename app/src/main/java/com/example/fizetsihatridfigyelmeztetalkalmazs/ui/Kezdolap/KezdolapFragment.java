package com.example.fizetsihatridfigyelmeztetalkalmazs.ui.Kezdolap;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fizetsihatridfigyelmeztetalkalmazs.DataBaseHelper;
import com.example.fizetsihatridfigyelmeztetalkalmazs.MainActivity;
import com.example.fizetsihatridfigyelmeztetalkalmazs.MyRecyclerViewAdapter;
import com.example.fizetsihatridfigyelmeztetalkalmazs.R;
import com.example.fizetsihatridfigyelmeztetalkalmazs.Szamla;

import java.util.List;

public class KezdolapFragment extends Fragment implements MyRecyclerViewAdapter.ItemClickListener {

    RecyclerView recyclerViewSzamlak;
    DataBaseHelper dataBaseHelper;
    List<Szamla> listaSzamlak;
    ArrayAdapter arrayAdapterSzamlak;

    MyRecyclerViewAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_kezdolap, container, false);

        recyclerViewSzamlak = root.findViewById(R.id.recyclerViewSzamlak);

        dataBaseHelper = new DataBaseHelper(getContext());
        listaSzamlak = dataBaseHelper.AdatbazisbolOsszesLekerese();
        recyclerViewSzamlak.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new MyRecyclerViewAdapter(getContext(), listaSzamlak);
        adapter.setClickListener(this);
//        recyclerViewSzamlak.setAdapter(adapter);

//        arrayAdapterSzamlak = new ArrayAdapter<Szamla>(getContext(), android.R.layout.simple_list_item_1, listaSzamlak);
//        recyclerViewSzamlak.setAdapter(arrayAdapterSzamlak);

        return root;
    }

    @Override
    public void onItemClick(View view, int position) {
        Toast.makeText(getContext(), "You clicked " + adapter.getItem(position) + " on row number " + position, Toast.LENGTH_SHORT).show();
    }
}