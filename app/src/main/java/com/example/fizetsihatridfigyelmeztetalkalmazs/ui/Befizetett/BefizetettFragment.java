package com.example.fizetsihatridfigyelmeztetalkalmazs.ui.Befizetett;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

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
import com.google.firebase.auth.FirebaseUser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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

    ImageView imageViewKereses, imageViewSzures, imageViewTorles;
    EditText editTextSzamlaNev;
    Spinner spinnerSzures2;

    FirebaseAuth mAuth;

    @RequiresApi(api = Build.VERSION_CODES.N)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_befizetett, container, false);

        mAuth = FirebaseAuth.getInstance();

        recyclerViewBefizetettSzamlak = root.findViewById(R.id.recyclerViewBefizetettSzamlak);
        imageViewKereses = root.findViewById(R.id.imageViewKereses);
        imageViewSzures = root.findViewById(R.id.imageViewSzures);
        imageViewTorles = root.findViewById(R.id.imageViewTorles);
        editTextSzamlaNev = root.findViewById(R.id.editTextSzamlaNev);
        spinnerSzures2 = root.findViewById(R.id.spinnerSzures2);

        dataBaseHelper = new DataBaseHelper(getContext());
        listaElvegzettSzamlak = dataBaseHelper.AdatbazisbolElvegzettekLekerese(FirebaseAuth.getInstance().getCurrentUser().getEmail());
        recyclerViewBefizetettSzamlak.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new MyRecyclerViewAdapter(getContext(), listaRecyclerView);
        recyclerViewBefizetettSzamlak.setAdapter(adapter);

        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.szures_array, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerSzures2.setAdapter(spinnerAdapter);
        spinnerSzures2.setSelection(3);

//        List<Szamla> returnLista = new ArrayList<>();
//
//        for (Szamla sz : listaElvegzettSzamlak)
//        {
//            SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
//            Date parsed = null;
//            try {
//                parsed = format.parse(sz.getSzamlaHatarido());
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
//            Date date = new Date(parsed.getTime());
//
//            listaSzamlaDatumokkal.add(new AbstractMap.SimpleEntry<Szamla, Date>(sz, date));
//        }
//
//        listaSzamlaDatumokkal.sort((o1, o2) -> o2.getValue().compareTo(o1.getValue()));
//
//
//        for(Map.Entry<Szamla, Date> sz : listaSzamlaDatumokkal)
//        {
//            returnLista.add(sz.getKey());
//        }
//
//        listaRecyclerView.clear();
//        listaRecyclerView.addAll(returnLista);
//        adapter.notifyDataSetChanged();


        imageViewKereses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editTextSzamlaNev.getVisibility() == View.INVISIBLE)
                {
                    editTextSzamlaNev.setVisibility(View.VISIBLE);
                    imageViewTorles.setVisibility(View.VISIBLE);
                    listaRecyclerView.clear();
                }
                else
                {
                    editTextSzamlaNev.setVisibility(View.INVISIBLE);
                    imageViewTorles.setVisibility(View.INVISIBLE);
                    InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(root.getWindowToken(), 0);
                    editTextSzamlaNev.setText("");
                    listaRecyclerView.clear();
                    listaRecyclerView.addAll(listaElvegzettSzamlak);
                    adapter.notifyDataSetChanged();
                }
            }
        });

        editTextSzamlaNev.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                List<Szamla> listaKeresettSzamlak = new ArrayList<>();

                for (Szamla sz : listaElvegzettSzamlak) {
                    if (sz.getTetelNev().toLowerCase().contains(editTextSzamlaNev.getText().toString().toLowerCase())
                            || sz.getSzamlaHatarido().toLowerCase().contains(editTextSzamlaNev.getText().toString().toLowerCase())
                            || String.valueOf(sz.getSzamlaOsszeg()).contains(editTextSzamlaNev.getText().toString()))
                        listaKeresettSzamlak.add(sz);
                }

                listaRecyclerView.clear();
                listaRecyclerView.addAll(listaKeresettSzamlak);
                adapter.notifyDataSetChanged();

                Log.d("keres", String.valueOf(listaKeresettSzamlak.size()));

                return false;
            }
        });


        imageViewTorles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTextSzamlaNev.setText("");
                listaRecyclerView.clear();
                listaRecyclerView.addAll(listaElvegzettSzamlak);
                adapter.notifyDataSetChanged();
            }
        });

        imageViewSzures.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (spinnerSzures2.getVisibility() == View.INVISIBLE)
                {
                    spinnerSzures2.setVisibility(View.VISIBLE);
                    spinnerSzures2.performClick();
                }
                else
                {
                    spinnerSzures2.setVisibility(View.INVISIBLE);
                }
            }
        });


        spinnerSzures2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String feltetel = parent.getItemAtPosition(position).toString();
                KitoltesRendezes(feltetel);
                Log.d("spinner", parent.getItemAtPosition(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return root;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void KitoltesRendezes(String feltetel)
    {
        listaElvegzettSzamlak.clear();
        listaElvegzettSzamlak = dataBaseHelper.AdatbazisbolElvegzettekLekerese(mAuth.getCurrentUser() == null? "" : mAuth.getCurrentUser().getEmail());
        switch (feltetel)
        {
            case "↑ Abc":
                listaRecyclerView.clear();
                listaRecyclerView.addAll(RendezesAbcNovekvo(listaElvegzettSzamlak));
                adapter.notifyDataSetChanged();
                break;
            case "↓ Abc":
                listaRecyclerView.clear();
                listaRecyclerView.addAll(RendezesAbcCsokkeno(listaElvegzettSzamlak));
                adapter.notifyDataSetChanged();
                break;
            case "↑ Dátum":
                listaRecyclerView.clear();
                listaRecyclerView.addAll(RendezesDatumNovekvo(listaElvegzettSzamlak));
                adapter.notifyDataSetChanged();
                break;
            case "↓ Dátum":
                listaRecyclerView.clear();
                listaRecyclerView.addAll(RendezesDatumCsokkeno(listaElvegzettSzamlak));
                adapter.notifyDataSetChanged();
                break;
            case "↑ Összeg":
                listaRecyclerView.clear();
                listaRecyclerView.addAll(RendezesOsszegNovekvo(listaElvegzettSzamlak));
                adapter.notifyDataSetChanged();
                break;
            case "↓ Összeg":
                listaRecyclerView.clear();
                listaRecyclerView.addAll(RendezesOsszegCsokkeno(listaElvegzettSzamlak));
                adapter.notifyDataSetChanged();
                break;
        }
    }

    public List<Szamla> RendezesAbcNovekvo(List<Szamla> lista)
    {
        List<Szamla> returnLista = new ArrayList<>();
        returnLista.addAll(lista);

        Collections.sort(returnLista, new Comparator<Szamla>() {
            @Override
            public int compare(Szamla o1, Szamla o2) {
                return o1.getTetelNev().compareToIgnoreCase(o2.getTetelNev());
            }
        });

        return returnLista;
    }


    public List<Szamla> RendezesAbcCsokkeno(List<Szamla> lista)
    {
        List<Szamla> returnLista = new ArrayList<>();
        returnLista.addAll(lista);

        Collections.sort(returnLista, new Comparator<Szamla>() {
            @Override
            public int compare(Szamla o1, Szamla o2) {
                return o2.getTetelNev().compareToIgnoreCase(o1.getTetelNev());
            }
        });

        return returnLista;
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    public List<Szamla> RendezesDatumNovekvo(List<Szamla> lista)
    {
        List<Szamla> returnLista = new ArrayList<>();
        listaSzamlaDatumokkal.clear();

        for (Szamla sz : lista)
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


        for(Map.Entry<Szamla, Date> sz : listaSzamlaDatumokkal)
        {
            returnLista.add(sz.getKey());
        }

        return returnLista;
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    public List<Szamla> RendezesDatumCsokkeno(List<Szamla> lista)
    {
        List<Szamla> returnLista = new ArrayList<>();
        listaSzamlaDatumokkal.clear();

        for (Szamla sz : lista)
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

        return returnLista;
    }


    public List<Szamla> RendezesOsszegNovekvo(List<Szamla> lista)
    {
        List<Szamla> returnLista = new ArrayList<>();
        returnLista.addAll(lista);

        Collections.sort(returnLista, new Comparator<Szamla>() {
            @Override
            public int compare(Szamla o1, Szamla o2) {
                return Integer.valueOf(o1.getSzamlaOsszeg()).compareTo(Integer.valueOf(o2.getSzamlaOsszeg()));
            }
        });

        return returnLista;
    }


    public List<Szamla> RendezesOsszegCsokkeno(List<Szamla> lista)
    {
        List<Szamla> returnLista = new ArrayList<>();
        returnLista.addAll(lista);

        Collections.sort(returnLista, new Comparator<Szamla>() {
            @Override
            public int compare(Szamla o1, Szamla o2) {
                return Integer.valueOf(o2.getSzamlaOsszeg()).compareTo(Integer.valueOf(o1.getSzamlaOsszeg()));
            }
        });

        return returnLista;
    }
}