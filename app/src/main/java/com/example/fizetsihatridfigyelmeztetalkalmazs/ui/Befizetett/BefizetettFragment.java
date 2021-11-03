package com.example.fizetsihatridfigyelmeztetalkalmazs.ui.Befizetett;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fizetsihatridfigyelmeztetalkalmazs.DataBaseHelper;
import com.example.fizetsihatridfigyelmeztetalkalmazs.MyRecyclerViewAdapter;
import com.example.fizetsihatridfigyelmeztetalkalmazs.R;
import com.example.fizetsihatridfigyelmeztetalkalmazs.Szamla;
import com.google.firebase.auth.FirebaseAuth;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class BefizetettFragment extends Fragment {

    RecyclerView recyclerViewBefizetettSzamlak;
    DataBaseHelper dataBaseHelper;
    List<Szamla> listaRecyclerView = new ArrayList<>();
    List<Szamla> listaElvegzettSzamlak;
    List<Map.Entry<Szamla, Date>> listaSzamlaDatumokkal = new ArrayList<Map.Entry<Szamla, Date>>();

    MyRecyclerViewAdapter adapter;

    @RequiresApi(api = Build.VERSION_CODES.N)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_befizetett, container, false);

        recyclerViewBefizetettSzamlak = root.findViewById(R.id.recyclerViewBefizetettSzamlak);

        dataBaseHelper = new DataBaseHelper(getContext());
        listaElvegzettSzamlak = dataBaseHelper.AdatbazisbolElvegzettekLekerese(FirebaseAuth.getInstance().getCurrentUser().getEmail());
        recyclerViewBefizetettSzamlak.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new MyRecyclerViewAdapter(getContext(), listaRecyclerView);
        recyclerViewBefizetettSzamlak.setAdapter(adapter);

        List<Szamla> returnLista = new ArrayList<>();

        for (Szamla sz : listaElvegzettSzamlak)
        {
            SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
            Date parsed = null;
            try {
                parsed = format.parse(sz.getSzamlaHatarido());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Date date = new Date(parsed.getTime());

            listaSzamlaDatumokkal.add(new AbstractMap.SimpleEntry<Szamla, Date>(sz, date));
        }

        listaSzamlaDatumokkal.sort((o1, o2) -> o2.getValue().compareTo(o1.getValue()));


        for(Map.Entry<Szamla, Date> sz : listaSzamlaDatumokkal)
        {
            returnLista.add(sz.getKey());
        }

        listaRecyclerView.clear();
        listaRecyclerView.addAll(returnLista);
        adapter.notifyDataSetChanged();

        return root;
    }
}