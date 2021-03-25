package com.example.fizetsihatridfigyelmeztetalkalmazs.ui.Kezdolap;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fizetsihatridfigyelmeztetalkalmazs.DataBaseHelper;
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

    AlertDialog.Builder dialogBuilder;
    AlertDialog dialog;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_kezdolap, container, false);

        recyclerViewSzamlak = root.findViewById(R.id.recyclerViewBefizetettSzamlak);

        dataBaseHelper = new DataBaseHelper(getContext());
        listaSzamlak = dataBaseHelper.AdatbazisbolNemElvegzettekLekerese();
        recyclerViewSzamlak.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new MyRecyclerViewAdapter(getContext(), listaSzamlak);
        adapter.setClickListener(this);
        recyclerViewSzamlak.setAdapter(adapter);

//        arrayAdapterSzamlak = new ArrayAdapter<Szamla>(getContext(), android.R.layout.simple_list_item_1, listaSzamlak);
//        recyclerViewSzamlak.setAdapter(arrayAdapterSzamlak);

        return root;
    }

    @Override
    public void onItemClick(View view, int position) {
        Toast.makeText(getContext(), "You clicked " + adapter.getItem(position) + " on row number " + position, Toast.LENGTH_SHORT).show();
        createNewItemDialog(position);
    }



    public void createNewItemDialog(int position)
    {
        dialogBuilder = new AlertDialog.Builder(getContext());
        final View itemPopupView = getLayoutInflater().inflate(R.layout.itempopup, null);
        ImageView imageViewPipa, imageViewSzerkesztes;

        imageViewPipa = itemPopupView.findViewById(R.id.imageViewPipa);
        imageViewSzerkesztes = itemPopupView.findViewById(R.id.imageViewSzerkesztes);

        dialogBuilder.setView(itemPopupView);
        dialog = dialogBuilder.create();
        dialog.show();

        imageViewPipa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Pipa lefutott: " + adapter.getItem(position), Toast.LENGTH_SHORT).show();
                dataBaseHelper.ElvegzetteNyilvanitas(adapter.getItem(position));

                listaSzamlak.remove(position);
                recyclerViewSzamlak.removeViewAt(position);
                adapter.notifyItemRemoved(position);
                adapter.notifyItemRangeChanged(position, listaSzamlak.size());

                adapter.notifyDataSetChanged();
            }
        });

        imageViewSzerkesztes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Szerkesztés lefutott: " + adapter.getItem(position), Toast.LENGTH_SHORT).show();
            }
        });
    }
}













