package com.example.fizetsihatridfigyelmeztetalkalmazs.ui.Kezdolap;

import android.app.AlertDialog;
import android.graphics.Canvas;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fizetsihatridfigyelmeztetalkalmazs.DataBaseHelper;
import com.example.fizetsihatridfigyelmeztetalkalmazs.MyRecyclerViewAdapter;
import com.example.fizetsihatridfigyelmeztetalkalmazs.R;
import com.example.fizetsihatridfigyelmeztetalkalmazs.Szamla;

import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class KezdolapFragment extends Fragment implements MyRecyclerViewAdapter.ItemClickListener {

    RecyclerView recyclerViewSzamlak;
    DataBaseHelper dataBaseHelper;
    List<Map.Entry<Szamla, Date>> listaSzamlaDatumokkal = new ArrayList<Map.Entry<Szamla, Date>>();
    List<Szamla> listaSzamlak;

    MyRecyclerViewAdapter adapter;

    AlertDialog.Builder dialogBuilder;
    AlertDialog dialog;

    @RequiresApi(api = Build.VERSION_CODES.N)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_kezdolap, container, false);

        recyclerViewSzamlak = root.findViewById(R.id.recyclerViewBefizetettSzamlak);

        dataBaseHelper = new DataBaseHelper(getContext());
        listaSzamlak = dataBaseHelper.AdatbazisbolNemElvegzettekLekerese();

        for (Szamla sz : listaSzamlak)
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

        listaSzamlaDatumokkal.sort((o1, o2) -> o1.getValue().compareTo(o2.getValue()));

        listaSzamlak.clear();

        for(Map.Entry<Szamla, Date> sz : listaSzamlaDatumokkal)
        {
            listaSzamlak.add(sz.getKey());
        }


        recyclerViewSzamlak.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new MyRecyclerViewAdapter(getContext(), listaSzamlak);
        adapter.setClickListener(this);
        recyclerViewSzamlak.setAdapter(adapter);

//        arrayAdapterSzamlak = new ArrayAdapter<Szamla>(getContext(), android.R.layout.simple_list_item_1, listaSzamlak);
//        recyclerViewSzamlak.setAdapter(arrayAdapterSzamlak);


        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerViewSzamlak);

        return root;
    }

    @Override
    public void onItemClick(View view, int position) {
        createNewItemDialog(position);
    }


    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull @NotNull RecyclerView recyclerView, @NonNull @NotNull RecyclerView.ViewHolder viewHolder, @NonNull @NotNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull @NotNull RecyclerView.ViewHolder viewHolder, int direction)
        {
            int position = viewHolder.getAdapterPosition();
            Szamla toroltszamla = listaSzamlak.get(position);

            if (direction == ItemTouchHelper.LEFT)
            {
                listaSzamlak.remove(position);
                adapter.notifyItemRemoved(position);

                deleteItemDialog(position, toroltszamla);
            }
        }

        @Override
        public void onChildDraw(@NonNull @NotNull Canvas c, @NonNull @NotNull RecyclerView recyclerView, @NonNull @NotNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

            new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addBackgroundColor(ContextCompat.getColor(getContext(), R.color.winered))
                    .addActionIcon(R.drawable.ic_baseline_delete_sweep_24)
                    .create()
                    .decorate();

            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    };










    //DIALOG
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
                Toast.makeText(getContext(), "Számla sikeresen elvégzetté jelölve:\n" + adapter.getItem(position).getTetelNev(), Toast.LENGTH_LONG).show();
                dataBaseHelper.ElvegzetteNyilvanitas(adapter.getItem(position));

                listaSzamlak.remove(position);
//                recyclerViewSzamlak.removeViewAt(position);
                adapter.notifyItemRemoved(position);
                adapter.notifyItemRangeChanged(position, listaSzamlak.size());

                adapter.notifyDataSetChanged();

                dialog.hide();
            }
        });

        imageViewSzerkesztes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Szerkesztés lefutott: " + adapter.getItem(position), Toast.LENGTH_SHORT).show();
                dialog.hide();
            }
        });
    }










    //DIALOG
    public void deleteItemDialog(int position, Szamla toroltszamla)
    {
        dialogBuilder = new AlertDialog.Builder(getContext());
        final View deleteItemPopupView = getLayoutInflater().inflate(R.layout.deleteitempopup, null);
        Button buttonIgen, buttonMegse;

        buttonIgen = deleteItemPopupView.findViewById(R.id.buttonIgen);
        buttonMegse = deleteItemPopupView.findViewById(R.id.buttonMegse);

        dialogBuilder.setView(deleteItemPopupView);
        dialog = dialogBuilder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();


        buttonIgen.setOnClickListener(v -> {
            dataBaseHelper.Torles(adapter.getItem(position));
            dialog.hide();
        });

        buttonMegse.setOnClickListener(v -> {
            listaSzamlak.add(position, toroltszamla);
            adapter.notifyItemInserted(position);
            dialog.hide();
        });
    }
}













