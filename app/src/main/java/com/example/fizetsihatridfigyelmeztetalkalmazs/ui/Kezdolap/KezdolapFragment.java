package com.example.fizetsihatridfigyelmeztetalkalmazs.ui.Kezdolap;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fizetsihatridfigyelmeztetalkalmazs.DataBaseHelper;
import com.example.fizetsihatridfigyelmeztetalkalmazs.MyRecyclerViewAdapter;
import com.example.fizetsihatridfigyelmeztetalkalmazs.R;
import com.example.fizetsihatridfigyelmeztetalkalmazs.SendMailTask;
import com.example.fizetsihatridfigyelmeztetalkalmazs.Szamla;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class KezdolapFragment extends Fragment implements MyRecyclerViewAdapter.ItemClickListener {

    RecyclerView recyclerViewSzamlak;
    DataBaseHelper dataBaseHelper;
    List<Szamla> listaRecyclerView = new ArrayList<>();
    List<Map.Entry<Szamla, Date>> listaSzamlaDatumokkal = new ArrayList<Map.Entry<Szamla, Date>>();
    List<Szamla> listaSzamlak;

    MyRecyclerViewAdapter adapter;

    AlertDialog.Builder dialogBuilder;
    AlertDialog dialog;

    ImageView imageViewKereses, imageViewTorles, imageViewSzures;
    EditText editTextSzamlaNev;
    Spinner spinnerSzures;


    View root;
    LayoutInflater globalInflater;
    ViewGroup globalContainer;

    FirebaseAuth mAuth;


    private final BroadcastReceiver tickReceiver = new BroadcastReceiver(){
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().compareTo(Intent.ACTION_TIME_TICK) == 0) {
                Log.v("timeChangedReceiver", "timeChangedReceiver");
                try {
                    Ertesites();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        requireActivity().unregisterReceiver(tickReceiver);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_kezdolap, container, false);
        globalInflater = inflater;
        globalContainer = container;

        mAuth = FirebaseAuth.getInstance();

        requireActivity().registerReceiver(tickReceiver, new IntentFilter(Intent.ACTION_TIME_TICK)); // register the broadcast receiver to receive TIME_TICK

        recyclerViewSzamlak = root.findViewById(R.id.recyclerViewBefizetettSzamlak);
        imageViewKereses = root.findViewById(R.id.imageViewKereses);
        imageViewTorles = root.findViewById(R.id.imageViewTorles);
        imageViewSzures = root.findViewById(R.id.imageViewSzures);
        editTextSzamlaNev = root.findViewById(R.id.editTextSzamlaNev);
        spinnerSzures = root.findViewById(R.id.spinnerSzures);

        mAuth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onAuthStateChanged(@NonNull @NotNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null)
                {
                    KitoltesRendezes(spinnerSzures.getSelectedItem().toString());
                }
            }
        });

        dataBaseHelper = new DataBaseHelper(getContext());
        listaSzamlak = dataBaseHelper.AdatbazisbolNemElvegzettekLekerese(mAuth.getCurrentUser() == null? "" : mAuth.getCurrentUser().getEmail());

        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.szures_array, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerSzures.setAdapter(spinnerAdapter);
        spinnerSzures.setSelection(2);

        recyclerViewSzamlak.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new MyRecyclerViewAdapter(getContext(), listaRecyclerView);
        adapter.setClickListener(this);
        recyclerViewSzamlak.setAdapter(adapter);



        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerViewSzamlak);



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
                    listaRecyclerView.addAll(listaSzamlak);
                    adapter.notifyDataSetChanged();
                }
            }
        });


        editTextSzamlaNev.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                List<Szamla> listaKeresettSzamlak = new ArrayList<>();

                for (Szamla sz : listaSzamlak) {
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
                listaRecyclerView.addAll(listaSzamlak);
                adapter.notifyDataSetChanged();
            }
        });



        imageViewSzures.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (spinnerSzures.getVisibility() == View.INVISIBLE)
                {
                    spinnerSzures.setVisibility(View.VISIBLE);
                    spinnerSzures.performClick();
                }
                else
                {
                    spinnerSzures.setVisibility(View.INVISIBLE);
                }
            }
        });


        spinnerSzures.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
        listaSzamlak.clear();
        listaSzamlak = dataBaseHelper.AdatbazisbolNemElvegzettekLekerese(mAuth.getCurrentUser() == null? "" : mAuth.getCurrentUser().getEmail());
        switch (feltetel)
        {
            case "↑ Abc":
                listaRecyclerView.clear();
                listaRecyclerView.addAll(RendezesAbcNovekvo(listaSzamlak));
                adapter.notifyDataSetChanged();
                break;
            case "↓ Abc":
                listaRecyclerView.clear();
                listaRecyclerView.addAll(RendezesAbcCsokkeno(listaSzamlak));
                adapter.notifyDataSetChanged();
                break;
            case "↑ Dátum":
                listaRecyclerView.clear();
                listaRecyclerView.addAll(RendezesDatumNovekvo(listaSzamlak));
                adapter.notifyDataSetChanged();
                break;
            case "↓ Dátum":
                listaRecyclerView.clear();
                listaRecyclerView.addAll(RendezesDatumCsokkeno(listaSzamlak));
                adapter.notifyDataSetChanged();
                break;
            case "↑ Összeg":
                listaRecyclerView.clear();
                listaRecyclerView.addAll(RendezesOsszegNovekvo(listaSzamlak));
                adapter.notifyDataSetChanged();
                break;
            case "↓ Összeg":
                listaRecyclerView.clear();
                listaRecyclerView.addAll(RendezesOsszegCsokkeno(listaSzamlak));
                adapter.notifyDataSetChanged();
                break;
        }
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

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void onSwiped(@NonNull @NotNull RecyclerView.ViewHolder viewHolder, int direction)
        {
            int position = viewHolder.getAdapterPosition();
//            Szamla toroltszamla = listaSzamlak.get(position);
            Szamla toroltszamla = adapter.getItem(position);

            if (direction == ItemTouchHelper.LEFT)
            {
                listaRecyclerView.remove(position);
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

                listaRecyclerView.remove(position);
                adapter.notifyItemRemoved(position);
                adapter.notifyItemRangeChanged(position, listaSzamlak.size());

                adapter.notifyDataSetChanged();

                dialog.hide();
            }
        });

        imageViewSzerkesztes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                szerkesztesDialog(position);
                dialog.hide();
            }
        });
    }

    private void szerkesztesDialog(int position)
    {
        Dialog dialog2;
        final View szerkesztespopup = getLayoutInflater().inflate(R.layout.szerkesztespopup, null);
        dialog2 = new Dialog(getContext());
        dialog2.setContentView(szerkesztespopup);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog2.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        dialog2.show();
        dialog2.getWindow().setAttributes(lp);


        EditText editTextTetelNev2, editTextOsszeg2, editTextHatarido2;
        RadioGroup radioGroupTipus2;
        RadioButton radioButtonEgyszeri2, radioButtonIsmetlodo2;
        Spinner spinnerIsmetlodes2;
        Button buttonMentes2, buttonMegse2, buttonTorles2;


        editTextTetelNev2 = szerkesztespopup.findViewById(R.id.editTextTetelNev2);
        editTextOsszeg2 = szerkesztespopup.findViewById(R.id.editTextOsszeg2);
        editTextHatarido2 = szerkesztespopup.findViewById(R.id.editTextHatarido2);
        radioGroupTipus2 = szerkesztespopup.findViewById(R.id.radioGroupTipus2);
        radioButtonEgyszeri2 = szerkesztespopup.findViewById(R.id.radioButtonEgyszeri2);
        radioButtonIsmetlodo2 = szerkesztespopup.findViewById(R.id.radioButtonIsmetlodo2);
        spinnerIsmetlodes2 = szerkesztespopup.findViewById(R.id.spinnerIsmetlodes2);
        buttonMentes2 = szerkesztespopup.findViewById(R.id.buttonMentes2);
        buttonMegse2 = szerkesztespopup.findViewById(R.id.buttonMegse2);
        buttonTorles2 = szerkesztespopup.findViewById(R.id.buttonTorles2);

        final Calendar myCalendar = Calendar.getInstance();

        dataBaseHelper = new DataBaseHelper(getContext());
        List<String> listSpinner = new ArrayList<>();
        listSpinner.add("Havonta");
        listSpinner.add("2 havonta");
        listSpinner.add("3 havonta");
        listSpinner.add("Félévente");
        listSpinner.add("Évente");



        Szamla sz = adapter.getItem(position);
        editTextTetelNev2.setText(sz.getTetelNev());
        editTextOsszeg2.setText(String.valueOf(sz.getSzamlaOsszeg()));
        editTextHatarido2.setText(sz.getSzamlaHatarido());

        if (sz.getSzamlaTipus().equals("egyszeri"))
        {
            radioButtonEgyszeri2.setChecked(true);
            spinnerIsmetlodes2.setVisibility(View.INVISIBLE);
        }
        else
        {
            radioButtonIsmetlodo2.setChecked(true);
            for (String s : listSpinner)
            {
                if (s.equals(sz.getIsmetlodesGyakorisag()))
                    spinnerIsmetlodes2.setSelection(listSpinner.indexOf(s));
            }
        }

        DatePickerDialog.OnDateSetListener datum = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "yyyy/MM/dd";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());
                editTextHatarido2.setText(sdf.format(myCalendar.getTime()));
            }
        };

        editTextHatarido2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getContext(), datum, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        buttonMentes2.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                if (adapter.getItem(position).getSzamlaTipus().equals("egyszeri"))
                {
                    if (!editTextTetelNev2.getText().toString().equals(sz.getTetelNev())) {
                        dataBaseHelper.FrissitesTetelnev(adapter.getItem(position), editTextTetelNev2.getText().toString());
                        listaSzamlak = dataBaseHelper.AdatbazisbolNemElvegzettekLekerese(mAuth.getCurrentUser().getEmail());
                        KitoltesRendezes(spinnerSzures.getSelectedItem().toString());
                    }
                    if (Integer.parseInt(editTextOsszeg2.getText().toString()) != sz.getSzamlaOsszeg()) {
                        dataBaseHelper.FrissitesOsszeg(adapter.getItem(position), Integer.parseInt(editTextOsszeg2.getText().toString()));
                        listaSzamlak = dataBaseHelper.AdatbazisbolNemElvegzettekLekerese(mAuth.getCurrentUser().getEmail());
                        KitoltesRendezes(spinnerSzures.getSelectedItem().toString());
                    }
                    if (!editTextHatarido2.getText().equals(sz.getSzamlaHatarido())) {
                        dataBaseHelper.FrissitesHatarido(adapter.getItem(position), editTextHatarido2.getText().toString());
                        listaSzamlak = dataBaseHelper.AdatbazisbolNemElvegzettekLekerese(mAuth.getCurrentUser().getEmail());
                        KitoltesRendezes(spinnerSzures.getSelectedItem().toString());
                    }
                    if (radioButtonIsmetlodo2.isChecked()) //Ismétlődő számlára szerkesztés
                    {
                        dataBaseHelper.FrissitesSzamlaTipus(adapter.getItem(position), "ismetlodo");
                        dataBaseHelper.FrissitesIsmetlodesGyakorisag(adapter.getItem(position), spinnerIsmetlodes2.getSelectedItem().toString());



                        List<Szamla> szList = new ArrayList<>();
                        boolean sikeres = false;
                        String ismetlodes = spinnerIsmetlodes2.getSelectedItem().toString();
                        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
                        Date hataridoKezdodes = null;
                        try {
                            hataridoKezdodes = format.parse(editTextHatarido2.getText().toString());
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        Date hatarido = hataridoKezdodes;

                        Log.d("hatarido", format.format(hatarido));
                        FirebaseAuth auth = FirebaseAuth.getInstance();

                        switch (ismetlodes)
                        {
                            case "Havonta":
                                for (int i=0; i<12; i++)
                                {
                                    hatarido.setMonth(hatarido.getMonth()+1);

                                    szList.add(new Szamla(editTextTetelNev2.getText().toString(), Integer.parseInt(editTextOsszeg2.getText().toString()),
                                            format.format(hatarido), "ismetlodo", spinnerIsmetlodes2.getSelectedItem().toString(), auth.getCurrentUser().getEmail()));
                                }
                                break;
                            case "2 havonta":
                                for(int i=0; i<6; i++)
                                {
                                    hatarido.setMonth(hatarido.getMonth()+2);

                                    szList.add(new Szamla(editTextTetelNev2.getText().toString(), Integer.parseInt(editTextOsszeg2.getText().toString()),
                                            format.format(hatarido), "ismetlodo", spinnerIsmetlodes2.getSelectedItem().toString(), auth.getCurrentUser().getEmail()));
                                }
                                break;
                            case "3 havonta":
                                for(int i=0; i<4; i++)
                                {
                                    hatarido.setMonth(hatarido.getMonth()+3);

                                    szList.add(new Szamla(editTextTetelNev2.getText().toString(), Integer.parseInt(editTextOsszeg2.getText().toString()),
                                            format.format(hatarido), "ismetlodo", spinnerIsmetlodes2.getSelectedItem().toString(), auth.getCurrentUser().getEmail()));
                                }
                                break;
                            case "Félévente":
                                for(int i=0; i<2; i++)
                                {
                                    hatarido.setMonth(hatarido.getMonth()+6);

                                    szList.add(new Szamla(editTextTetelNev2.getText().toString(), Integer.parseInt(editTextOsszeg2.getText().toString()),
                                            format.format(hatarido), "ismetlodo", spinnerIsmetlodes2.getSelectedItem().toString(), auth.getCurrentUser().getEmail()));
                                }
                                break;
                            case "Évente":
                                hatarido.setMonth(hatarido.getMonth()+12);

                                szList.add(new Szamla(editTextTetelNev2.getText().toString(), Integer.parseInt(editTextOsszeg2.getText().toString()),
                                        format.format(hatarido), "ismetlodo", spinnerIsmetlodes2.getSelectedItem().toString(), auth.getCurrentUser().getEmail()));
                                break;
                        }

                        for(Szamla s : szList)
                        {
                            sikeres = dataBaseHelper.AdatbazishozHozzaadas(s);
                            KitoltesRendezes(spinnerSzures.getSelectedItem().toString());
                        }
                        if (sikeres) {
                            Toast.makeText(getContext(), "További számlák sikeresen hozzáadva.", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getContext(), "Nem sikerült a számlákat hozzáadni.", Toast.LENGTH_LONG).show();
                        }
                    }
                }
                else
                {
                    dialogBuilder = new AlertDialog.Builder(getContext());
                    final View ismSzamlaSzerkesztesPopupView = getLayoutInflater().inflate(R.layout.ismetlodoszamlaszerkesztespopup, null);
                    Button buttonMindegyik, buttonEgy;

                    buttonMindegyik = ismSzamlaSzerkesztesPopupView.findViewById(R.id.buttonMindegyik);
                    buttonEgy = ismSzamlaSzerkesztesPopupView.findViewById(R.id.buttonEgy);

                    dialogBuilder.setView(ismSzamlaSzerkesztesPopupView);
                    Dialog dialogSzerkIsmPopup = dialogBuilder.create();
                    dialogSzerkIsmPopup.setCanceledOnTouchOutside(false);
                    dialogSzerkIsmPopup.show();

                    buttonEgy.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (!editTextTetelNev2.getText().toString().equals(sz.getTetelNev())) {
                                dataBaseHelper.FrissitesTetelnev(adapter.getItem(position), editTextTetelNev2.getText().toString());
                                listaSzamlak = dataBaseHelper.AdatbazisbolNemElvegzettekLekerese(mAuth.getCurrentUser().getEmail());
                                KitoltesRendezes(spinnerSzures.getSelectedItem().toString());
                            }
                            if (Integer.parseInt(editTextOsszeg2.getText().toString()) != sz.getSzamlaOsszeg()) {
                                dataBaseHelper.FrissitesOsszeg(adapter.getItem(position), Integer.parseInt(editTextOsszeg2.getText().toString()));
                                listaSzamlak = dataBaseHelper.AdatbazisbolNemElvegzettekLekerese(mAuth.getCurrentUser().getEmail());
                                KitoltesRendezes(spinnerSzures.getSelectedItem().toString());
                            }
                            if (!editTextHatarido2.getText().toString().equals(sz.getSzamlaHatarido())) {
                                dataBaseHelper.FrissitesHatarido(adapter.getItem(position), editTextHatarido2.getText().toString());
                                listaSzamlak = dataBaseHelper.AdatbazisbolNemElvegzettekLekerese(mAuth.getCurrentUser().getEmail());
                                KitoltesRendezes(spinnerSzures.getSelectedItem().toString());
                            }
                            if (radioButtonEgyszeri2.isChecked())
                            {
                                dataBaseHelper.FrissitesSzamlaTipus(adapter.getItem(position), "egyszeri");
                                dataBaseHelper.FrissitesIsmetlodesGyakorisag(adapter.getItem(position), "NULL");
                                KitoltesRendezes(spinnerSzures.getSelectedItem().toString());
                            }
                            dialogSzerkIsmPopup.dismiss();
                        }
                    });

                    buttonMindegyik.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            if (radioButtonEgyszeri2.isChecked())
                            {
                                Toast.makeText(getContext(), "Érvénytelen parancs", Toast.LENGTH_LONG).show();
                                dialogSzerkIsmPopup.dismiss();
                                dialog2.cancel();
                            }

                            List<Szamla> listaSzerkTorlendoSzamlak = new ArrayList<>();
                            SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
                            Date hataridoKezdodes = null;
                            try {
                                hataridoKezdodes = format.parse(adapter.getItem(position).getSzamlaHatarido());
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            Date hatarido = hataridoKezdodes;
                            FirebaseAuth auth = FirebaseAuth.getInstance();

                            //TODO ha törlök ismétlődő számlát, akkor újra létrehozza, és szerkeszti
                            switch (adapter.getItem(position).getIsmetlodesGyakorisag())
                            {
                                case "Havonta":
                                    for (int i=0; i<13; i++)
                                    {
                                        listaSzerkTorlendoSzamlak.add(new Szamla(adapter.getItem(position).getTetelNev(), adapter.getItem(position).getSzamlaOsszeg(),
                                                format.format(hatarido), "ismetlodo", adapter.getItem(position).getIsmetlodesGyakorisag(), auth.getCurrentUser().getEmail()));
                                        hatarido.setMonth(hatarido.getMonth()+1);
                                    }
                                    break;
                                case "2 havonta":
                                    for(int i=0; i<7; i++)
                                    {
                                        listaSzerkTorlendoSzamlak.add(new Szamla(adapter.getItem(position).getTetelNev(), adapter.getItem(position).getSzamlaOsszeg(),
                                                format.format(hatarido), "ismetlodo", adapter.getItem(position).getIsmetlodesGyakorisag(), auth.getCurrentUser().getEmail()));
                                        hatarido.setMonth(hatarido.getMonth()+2);
                                    }
                                    break;
                                case "3 havonta":
                                    for(int i=0; i<5; i++)
                                    {
                                        listaSzerkTorlendoSzamlak.add(new Szamla(adapter.getItem(position).getTetelNev(), adapter.getItem(position).getSzamlaOsszeg(),
                                                format.format(hatarido), "ismetlodo", adapter.getItem(position).getIsmetlodesGyakorisag(), auth.getCurrentUser().getEmail()));
                                        hatarido.setMonth(hatarido.getMonth()+3);
                                    }
                                    break;
                                case "Félévente":
                                    for(int i=0; i<3; i++)
                                    {
                                        listaSzerkTorlendoSzamlak.add(new Szamla(adapter.getItem(position).getTetelNev(), adapter.getItem(position).getSzamlaOsszeg(),
                                                format.format(hatarido), "ismetlodo", adapter.getItem(position).getIsmetlodesGyakorisag(), auth.getCurrentUser().getEmail()));
                                        hatarido.setMonth(hatarido.getMonth()+6);
                                    }
                                    break;
                                case "Évente":
                                    for (int i=0; i<2; i++)
                                    {
                                        listaSzerkTorlendoSzamlak.add(new Szamla(adapter.getItem(position).getTetelNev(), adapter.getItem(position).getSzamlaOsszeg(),
                                                format.format(hatarido), "ismetlodo", adapter.getItem(position).getIsmetlodesGyakorisag(), auth.getCurrentUser().getEmail()));
                                        hatarido.setMonth(hatarido.getMonth()+12);
                                    }
                                    break;
                            }



                            if (!editTextTetelNev2.getText().toString().equals(listaSzerkTorlendoSzamlak.get(0).getTetelNev())) {
                                SzamlakTorleseHozzaadasa(listaSzerkTorlendoSzamlak, format);
                            }
                            if (Integer.parseInt(editTextOsszeg2.getText().toString()) != listaSzerkTorlendoSzamlak.get(0).getSzamlaOsszeg()) {
                                SzamlakTorleseHozzaadasa(listaSzerkTorlendoSzamlak, format);
                            }
                            if (!editTextHatarido2.getText().toString().equals(listaSzerkTorlendoSzamlak.get(0).getSzamlaHatarido())) {
                                SzamlakTorleseHozzaadasa(listaSzerkTorlendoSzamlak, format);
                            }
                            if (!spinnerIsmetlodes2.getSelectedItem().toString().equals(listaSzerkTorlendoSzamlak.get(0).getIsmetlodesGyakorisag())) {
                                SzamlakTorleseHozzaadasa(listaSzerkTorlendoSzamlak, format);
                            }

                            KitoltesRendezes(spinnerSzures.getSelectedItem().toString());

                            dialogSzerkIsmPopup.dismiss();
                        }
                    });
                }
                dialog2.dismiss();
            }

            @RequiresApi(api = Build.VERSION_CODES.N)
            private void SzamlakTorleseHozzaadasa(List<Szamla> listaSzerkTorlendoSzamlak, SimpleDateFormat format) {
                Date hatarido;
                Date hataridoKezdodes;
                for (Szamla sz : listaSzerkTorlendoSzamlak)
                {
                    dataBaseHelper.Torles(sz);
                }

                List<Szamla> listaSzerkUjSzamlak = new ArrayList<>();

                hataridoKezdodes = null;
                try {
                    hataridoKezdodes = format.parse(editTextHatarido2.getText().toString());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                hatarido = hataridoKezdodes;
                FirebaseAuth auth = FirebaseAuth.getInstance();

                switch (spinnerIsmetlodes2.getSelectedItem().toString())
                {
                    case "Havonta":
                        for (int i=0; i<13; i++)
                        {
                            listaSzerkUjSzamlak.add(new Szamla(editTextTetelNev2.getText().toString(), Integer.valueOf(editTextOsszeg2.getText().toString()),
                                    format.format(hatarido), "ismetlodo", spinnerIsmetlodes2.getSelectedItem().toString(), auth.getCurrentUser().getEmail()));
                            hatarido.setMonth(hatarido.getMonth()+1);
                        }
                        break;
                    case "2 havonta":
                        for(int i=0; i<7; i++)
                        {
                            listaSzerkUjSzamlak.add(new Szamla(editTextTetelNev2.getText().toString(), Integer.valueOf(editTextOsszeg2.getText().toString()),
                                    format.format(hatarido), "ismetlodo", spinnerIsmetlodes2.getSelectedItem().toString(), auth.getCurrentUser().getEmail()));
                            hatarido.setMonth(hatarido.getMonth()+2);
                        }
                        break;
                    case "3 havonta":
                        for(int i=0; i<5; i++)
                        {
                            listaSzerkUjSzamlak.add(new Szamla(editTextTetelNev2.getText().toString(), Integer.valueOf(editTextOsszeg2.getText().toString()),
                                    format.format(hatarido), "ismetlodo", spinnerIsmetlodes2.getSelectedItem().toString(), auth.getCurrentUser().getEmail()));
                            hatarido.setMonth(hatarido.getMonth()+3);
                        }
                        break;
                    case "Félévente":
                        for(int i=0; i<3; i++)
                        {
                            listaSzerkUjSzamlak.add(new Szamla(editTextTetelNev2.getText().toString(), Integer.valueOf(editTextOsszeg2.getText().toString()),
                                    format.format(hatarido), "ismetlodo", spinnerIsmetlodes2.getSelectedItem().toString(), auth.getCurrentUser().getEmail()));
                            hatarido.setMonth(hatarido.getMonth()+6);
                        }
                        break;
                    case "Évente":
                        for (int i=0; i<2; i++)
                        {
                            listaSzerkUjSzamlak.add(new Szamla(editTextTetelNev2.getText().toString(), Integer.valueOf(editTextOsszeg2.getText().toString()),
                                    format.format(hatarido), "ismetlodo", spinnerIsmetlodes2.getSelectedItem().toString(), auth.getCurrentUser().getEmail()));
                            hatarido.setMonth(hatarido.getMonth()+12);
                        }
                        break;
                }

                for (Szamla ujsz : listaSzerkUjSzamlak)
                {
                    dataBaseHelper.AdatbazishozHozzaadas(ujsz);
                }

                listaSzamlak.clear();
                listaSzamlak = dataBaseHelper.AdatbazisbolNemElvegzettekLekerese(mAuth.getCurrentUser().getEmail());
                KitoltesRendezes(spinnerSzures.getSelectedItem().toString());

            }
        });

        buttonMegse2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog2.cancel();
            }
        });

        buttonTorles2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteItemDialog(position);
                dialog2.dismiss();
            }
        });

        radioGroupTipus2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == radioButtonEgyszeri2.getId())
                {
                    spinnerIsmetlodes2.setVisibility(View.INVISIBLE);
                }
                if (checkedId == radioButtonIsmetlodo2.getId())
                {
                    spinnerIsmetlodes2.setVisibility(View.VISIBLE);
                }
            }
        });
    }


    //DIALOG
    @RequiresApi(api = Build.VERSION_CODES.N)
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
            dataBaseHelper.Torles(toroltszamla);
            listaSzamlak.clear();
            listaSzamlak = dataBaseHelper.AdatbazisbolNemElvegzettekLekerese(mAuth.getCurrentUser().getEmail());
            KitoltesRendezes(spinnerSzures.getSelectedItem().toString());
            dialog.hide();
        });

        buttonMegse.setOnClickListener(v -> {
            listaRecyclerView.add(position, toroltszamla);
            adapter.notifyItemInserted(position);
            dialog.hide();
        });
    }

    //DIALOG
    public void deleteItemDialog(int position)
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
            listaRecyclerView.remove(position);
            adapter.notifyItemRemoved(position);
            adapter.notifyItemRangeChanged(position, listaRecyclerView.size());
            adapter.notifyDataSetChanged();
            dialog.dismiss();
        });

        buttonMegse.setOnClickListener(v -> {
            dialog.cancel();
        });
    }

    public void Ertesites() throws Exception {
        if (mAuth.getCurrentUser() == null)
            return;

        String ertesites = dataBaseHelper.getErtesites();
        String ertesitesIdopont = dataBaseHelper.getErtesitesIdopont();
        String ertesitesMod = dataBaseHelper.getErtesitesMod();
        Calendar currentDate = Calendar.getInstance();

        if  (currentDate.getTime().getDate() == 10)
        {
            ErtesitesKovetkezoHonapMagasabbKiadas(ertesitesIdopont);
        }

        switch (ertesitesMod) {
            case "Rendszer értesítés":
                ErtesitesSwitchRendszerErtesites(ertesites, ertesitesIdopont);
                break;
            case "E-mail":
                ErtesitesSwitchEmailErtesites(ertesites, ertesitesIdopont);
                break;
            case "Rendszer értesítés + E-mail":
                ErtesitesSwitchRendszerErtesites(ertesites, ertesitesIdopont);
                ErtesitesSwitchEmailErtesites(ertesites, ertesitesIdopont);
                break;
        }
    }

    private void ErtesitesKovetkezoHonapMagasabbKiadas(String ertesitesIdopont) throws Exception {
        listaSzamlaDatumokkal.clear();

        Date currentTime = Calendar.getInstance().getTime();
        Calendar currentDate = Calendar.getInstance();

        List<Szamla> listaElvegzettSzamlak = new ArrayList<>();
        listaElvegzettSzamlak = dataBaseHelper.AdatbazisbolElvegzettekLekerese(mAuth.getCurrentUser().getEmail());


        for (Szamla sz : listaElvegzettSzamlak) {
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

        for (Szamla sz : listaSzamlak) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
            Date parsed = null;
            try {
                parsed = format.parse(sz.getSzamlaHatarido());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Date date = new Date(parsed.getTime());

            if (date.getYear() == currentDate.getTime().getYear() && date.getMonth() == currentDate.getTime().getMonth()+1) {
                listaSzamlaDatumokkal.add(new AbstractMap.SimpleEntry<Szamla, Date>(sz, date));
            }
        }


        Map<Integer, Integer> mapHaviOsszegek = new HashMap<>();
        Map<Integer, Integer> mapKovetkezoHonapOsszeg = new HashMap<>();
        currentDate.add(Calendar.MONTH, 1);


        for (Map.Entry<Szamla, Date> sz: listaSzamlaDatumokkal)
        {
            int honap = sz.getValue().getMonth();
            if (currentDate.getTime().getMonth() == honap && currentDate.getTime().getYear() == sz.getValue().getYear())
            {
                int osszeg = mapKovetkezoHonapOsszeg.containsKey(honap) ? mapKovetkezoHonapOsszeg.get(honap) : 0;
                osszeg += sz.getKey().getSzamlaOsszeg();
                mapKovetkezoHonapOsszeg.put(honap, osszeg);
            }
            else
            {
                int osszeg = mapHaviOsszegek.containsKey(honap) ? mapHaviOsszegek.get(honap) : 0;
                osszeg += sz.getKey().getSzamlaOsszeg();
                mapHaviOsszegek.put(honap, osszeg);
            }
        }

        if (mapHaviOsszegek.size() == 0)
            return;

        int sum = 0;
        int atlag = 0;

        for (int key : mapHaviOsszegek.keySet())
        {
            sum += mapHaviOsszegek.get(key);
        }

        atlag = sum/mapHaviOsszegek.size();
        sum = 0;

        for (int key : mapKovetkezoHonapOsszeg.keySet())
        {
            sum += mapKovetkezoHonapOsszeg.get(key);
        }

        atlag = (int) (atlag*1.2);

        if (sum > atlag)
        {
            String clock;

            if (String.valueOf(currentTime.getMinutes()).length() == 1)
            {
                clock = String.valueOf(currentTime.getHours()) + "0" + String.valueOf(currentTime.getMinutes());
            }
            else
            {
                clock = String.valueOf(currentTime.getHours()) + String.valueOf(currentTime.getMinutes());
            }

            if (clock.equals(ertesitesIdopont))
            {
                if (dataBaseHelper.getErtesitesMod().equals("Rendszer értesítés"))
                {
                    ErtesitesKuldesFigyelmeztetes();
                }
                else if (dataBaseHelper.getErtesitesMod().equals("E-mail"))
                {
                    EmailKuldesFigyelmeztetes();
                }
                else
                {
                    ErtesitesKuldesFigyelmeztetes();
                    EmailKuldesFigyelmeztetes();
                }
            }
        }
    }


    private void ErtesitesSwitchEmailErtesites(String ertesites, String ertesitesIdopont) {
        listaSzamlaDatumokkal.clear();

        Date currentTime = Calendar.getInstance().getTime();
        Calendar currentDate = Calendar.getInstance();


        for (Szamla sz : listaSzamlak) {
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

        String clock;

        if (String.valueOf(currentTime.getMinutes()).length() == 1)
        {
            clock = String.valueOf(currentTime.getHours()) + "0" + String.valueOf(currentTime.getMinutes());
        }
        else
        {
            clock = String.valueOf(currentTime.getHours()) + String.valueOf(currentTime.getMinutes());
        }

        int szamlaCounter = 0;

        switch (ertesites) {
            case "Aznap":
                szamlaCounter = 0;

                if (clock.equals(ertesitesIdopont))
                {
                    for (Map.Entry<Szamla, Date> sz : listaSzamlaDatumokkal)
                    {
                        if (sz.getValue().getYear() == currentDate.getTime().getYear() && sz.getValue().getMonth() == currentDate.getTime().getMonth() && sz.getValue().getDate() == currentDate.getTime().getDate())
                        {
                            szamlaCounter++;
                        }
                    }

                    if (szamlaCounter > 0)
                    {
                        try {
                            EmailKuldes("Mai napon", szamlaCounter);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                break;
            case "Előtte 1 nappal":
                szamlaCounter = 0;
                currentDate.add(Calendar.DATE, 1);

                if (clock.equals(ertesitesIdopont))
                {
                    for (Map.Entry<Szamla, Date> sz : listaSzamlaDatumokkal)
                    {
                        if (sz.getValue().getYear() == currentDate.getTime().getYear() && sz.getValue().getMonth() == currentDate.getTime().getMonth() && sz.getValue().getDate() == currentDate.getTime().getDate())
                        {
                            szamlaCounter++;
                        }
                    }

                    if (szamlaCounter > 0)
                    {
                        try {
                            EmailKuldes("Holnap", szamlaCounter);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                break;
            case "Előtte 3 nappal":
                szamlaCounter = 0;
                currentDate.add(Calendar.DATE, 3);

                if (clock.equals(ertesitesIdopont))
                {
                    for (Map.Entry<Szamla, Date> sz : listaSzamlaDatumokkal)
                    {
                        if (sz.getValue().getYear() == currentDate.getTime().getYear() && sz.getValue().getMonth() == currentDate.getTime().getMonth() && sz.getValue().getDate() == currentDate.getTime().getDate())
                        {
                            szamlaCounter++;
                        }
                    }

                    if (szamlaCounter > 0)
                    {
                        try {
                            EmailKuldes("Három nap múlva", szamlaCounter);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                break;
            case "Előtte 1 héttel":
                szamlaCounter = 0;
                currentDate.add(Calendar.DATE, 7);

                if (clock.equals(ertesitesIdopont))
                {
                    for (Map.Entry<Szamla, Date> sz : listaSzamlaDatumokkal)
                    {
                        Log.d("timeChangedReceiver", sz.getValue().getDate() + "   -   " + currentDate.getTime().getDate());
                        if (sz.getValue().getYear() == currentDate.getTime().getYear() && sz.getValue().getMonth() == currentDate.getTime().getMonth() && sz.getValue().getDate() == currentDate.getTime().getDate())
                        {
                            szamlaCounter++;
                        }
                    }

                    if (szamlaCounter > 0)
                    {
                        try {
                            EmailKuldes("Egy hét múlva", szamlaCounter);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                break;
        }
    }

    private void EmailKuldes(String contentText, int szamlaCounter) throws Exception
    {
        String fromEmail = "szamlaalkalmazas@gmail.com";
        String fromPassword = "SzamlaAlkalmazas1";
        String toEmails = mAuth.getCurrentUser().getEmail();
        String emailSubject = "Számla Értesítés!";
        String emailBody = contentText + " " + szamlaCounter + "db számla esedékes.";
        new SendMailTask(getActivity()).execute(fromEmail,
                fromPassword, toEmails, emailSubject, emailBody);
    }

    private void EmailKuldesFigyelmeztetes() throws Exception
    {
        String fromEmail = "szamlaalkalmazas@gmail.com";
        String fromPassword = "SzamlaAlkalmazas1";
        String toEmails = mAuth.getCurrentUser().getEmail();
        String emailSubject = "Figyelem!";
        String emailBody = "Következő hónapban magasabb kiadás várható.";
        new SendMailTask(getActivity()).execute(fromEmail,
                fromPassword, toEmails, emailSubject, emailBody);
    }

    public void ErtesitesSwitchRendszerErtesites(String ertesites, String ertesitesIdopont)
    {
        listaSzamlaDatumokkal.clear();

        Date currentTime = Calendar.getInstance().getTime();
        Calendar currentDate = Calendar.getInstance();

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

        String clock;

        if (String.valueOf(currentTime.getMinutes()).length() == 1)
        {
             clock = String.valueOf(currentTime.getHours()) + "0" + String.valueOf(currentTime.getMinutes());
        }
        else
        {
             clock = String.valueOf(currentTime.getHours()) + String.valueOf(currentTime.getMinutes());
        }

        int szamlaCounter = 0;

        switch (ertesites)
        {
            case "Aznap":
                szamlaCounter = 0;

                if (clock.equals(ertesitesIdopont))
                {
                    for (Map.Entry<Szamla, Date> sz : listaSzamlaDatumokkal)
                    {
                        if (sz.getValue().getYear() == currentDate.getTime().getYear() && sz.getValue().getMonth() == currentDate.getTime().getMonth() && sz.getValue().getDate() == currentDate.getTime().getDate())
                        {
                            szamlaCounter++;
                        }
                    }

                    if (szamlaCounter > 0)
                    {
                        ErtesitesKuldes("Mai napon", szamlaCounter);
                    }
                }

                break;
            case "Előtte 1 nappal":
                szamlaCounter = 0;
                currentDate.add(Calendar.DATE, 1);

                if (clock.equals(ertesitesIdopont))
                {
                    for (Map.Entry<Szamla, Date> sz : listaSzamlaDatumokkal)
                    {
                        if (sz.getValue().getYear() == currentDate.getTime().getYear() && sz.getValue().getMonth() == currentDate.getTime().getMonth() && sz.getValue().getDate() == currentDate.getTime().getDate())
                        {
                            szamlaCounter++;
                        }
                    }

                    if (szamlaCounter > 0)
                    {
                        ErtesitesKuldes("Holnap", szamlaCounter);
                    }
                }
                break;
            case "Előtte 3 nappal":
                szamlaCounter = 0;
                currentDate.add(Calendar.DATE, 3);

                if (clock.equals(ertesitesIdopont))
                {
                    for (Map.Entry<Szamla, Date> sz : listaSzamlaDatumokkal)
                    {
                        if (sz.getValue().getYear() == currentDate.getTime().getYear() && sz.getValue().getMonth() == currentDate.getTime().getMonth() && sz.getValue().getDate() == currentDate.getTime().getDate())
                        {
                            szamlaCounter++;
                        }
                    }

                    if (szamlaCounter > 0)
                    {
                        ErtesitesKuldes("Három nap múlva", szamlaCounter);
                    }
                }
                break;
            case "Előtte 1 héttel":
                szamlaCounter = 0;
                currentDate.add(Calendar.DATE, 7);

                if (clock.equals(ertesitesIdopont))
                {
                    for (Map.Entry<Szamla, Date> sz : listaSzamlaDatumokkal)
                    {
                        Log.d("timeChangedReceiver", sz.getValue().getDate() + "   -   " + currentDate.getTime().getDate());
                        if (sz.getValue().getYear() == currentDate.getTime().getYear() && sz.getValue().getMonth() == currentDate.getTime().getMonth() && sz.getValue().getDate() == currentDate.getTime().getDate())
                        {
                            szamlaCounter++;
                        }
                    }

                    if (szamlaCounter > 0)
                    {
                        ErtesitesKuldes("Egy hét múlva", szamlaCounter);
                    }
                }
                break;
        }
    }


    public void ErtesitesKuldes(String contentText, int szamlaCounter)
    {
        NotificationManager mNotificationManager;

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(getActivity().getApplicationContext(), "notify_001");
        Intent ii = new Intent(getActivity().getApplicationContext(), KezdolapFragment.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(getActivity(), 0, ii, 0);

        mBuilder.setContentIntent(pendingIntent)
                .setContentTitle("Számla értesítés!")
                .setContentText(contentText + " " + szamlaCounter + "db számla esedékes.")
                .setSmallIcon(R.drawable.szamla)
                .setPriority(Notification.PRIORITY_MAX)
                .setSound(Uri.parse("android.resource://"
                        + getActivity().getPackageName() + "/" + R.raw.juntos));

        mNotificationManager =
                (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            String channelId = "channel1";
            NotificationChannel channel = new NotificationChannel(
                    channelId,
                    "Számla értesítés",
                    NotificationManager.IMPORTANCE_HIGH);
            mNotificationManager.createNotificationChannel(channel);
            mBuilder.setChannelId(channelId);
        }

        mNotificationManager.notify(0, mBuilder.build());

        Log.d("timeChangedReceiver", "Notification lefutott");
    }

    public void ErtesitesKuldesFigyelmeztetes()
    {
        NotificationManager mNotificationManager;

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(getActivity().getApplicationContext(), "notify_001");
        Intent ii = new Intent(getActivity().getApplicationContext(), KezdolapFragment.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(getActivity(), 0, ii, 0);

        mBuilder.setContentIntent(pendingIntent)
                .setContentTitle("Figyelem!")
                .setContentText("Következő hónapban magasabb kiadás várható.")
                .setSmallIcon(R.drawable.szamla)
                .setPriority(Notification.PRIORITY_MAX)
                .setSound(Uri.parse("android.resource://"
                        + getActivity().getPackageName() + "/" + R.raw.juntos));

        mNotificationManager =
                (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            String channelId = "channel1";
            NotificationChannel channel = new NotificationChannel(
                    channelId,
                    "Számla értesítés",
                    NotificationManager.IMPORTANCE_HIGH);
            mNotificationManager.createNotificationChannel(channel);
            mBuilder.setChannelId(channelId);
        }

        mNotificationManager.notify(0, mBuilder.build());

        Log.d("timeChangedReceiver", "Notification lefutott");
    }
}