package com.example.fizetsihatridfigyelmeztetalkalmazs.ui.Kezdolap;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Canvas;
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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
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
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
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
    List<Szamla> listaKeresettSzamlak;

    MyRecyclerViewAdapter adapter;

    AlertDialog.Builder dialogBuilder;
    AlertDialog dialog;

    ImageView imageViewKereses, imageViewTorles, imageViewSzures;
    EditText editTextSzamlaNev;
    Spinner spinnerSzures;


    View root;
    LayoutInflater globalInflater;
    ViewGroup globalContainer;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_kezdolap, container, false);
        globalInflater = inflater;
        globalContainer = container;

        recyclerViewSzamlak = root.findViewById(R.id.recyclerViewBefizetettSzamlak);
        imageViewKereses = root.findViewById(R.id.imageViewKereses);
        imageViewTorles = root.findViewById(R.id.imageViewTorles);
        imageViewSzures = root.findViewById(R.id.imageViewSzures);
        editTextSzamlaNev = root.findViewById(R.id.editTextSzamlaNev);
        spinnerSzures = root.findViewById(R.id.spinnerSzures);

        dataBaseHelper = new DataBaseHelper(getContext());
        listaSzamlak = dataBaseHelper.AdatbazisbolNemElvegzettekLekerese();

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
                    if (sz.getTetelNev().toLowerCase().contains(editTextSzamlaNev.getText().toString().toLowerCase()))
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

        @Override
        public void onSwiped(@NonNull @NotNull RecyclerView.ViewHolder viewHolder, int direction)
        {
            int position = viewHolder.getAdapterPosition();
            Szamla toroltszamla = listaSzamlak.get(position);

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

//        Locale magyar = new Locale("hu");
//        Collator magyarCollator = Collator.getInstance(magyar);
//
//        magyarCollator.compare(returnLista, new Comparator<Szamla>() {
//            @Override
//            public int compare(Szamla o1, Szamla o2) {
//                return o1.getTetelNev().compareToIgnoreCase(o2.getTetelNev());
//            }
//        });


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
                if (!editTextTetelNev2.getText().toString().equals(sz.getTetelNev()))
                {
                    dataBaseHelper.FrissitesTetelnev(adapter.getItem(position), editTextTetelNev2.getText().toString());
                    listaSzamlak = dataBaseHelper.AdatbazisbolNemElvegzettekLekerese();
                    KitoltesRendezes(spinnerSzures.getSelectedItem().toString());
                }
                if (Integer.parseInt(editTextOsszeg2.getText().toString()) != sz.getSzamlaOsszeg())
                {
                    dataBaseHelper.FrissitesOsszeg(adapter.getItem(position), Integer.parseInt(editTextOsszeg2.getText().toString()));
                    listaSzamlak = dataBaseHelper.AdatbazisbolNemElvegzettekLekerese();
                    KitoltesRendezes(spinnerSzures.getSelectedItem().toString());
                }
                //TODO ismétlődőnél ugyanaz lesz az év
//                if (!editTextHatarido2.getText().equals(sz.getSzamlaHatarido()))
//                {
//                    dataBaseHelper.FrissitesHatarido(adapter.getItem(position), editTextHatarido2.getText().toString());
//                    listaSzamlak = dataBaseHelper.AdatbazisbolNemElvegzettekLekerese();
//                    KitoltesRendezes(spinnerSzures.getSelectedItem().toString());
//                }
                //TODO típusnak megfelelően törölni kell/hozzáadni kell
                if (sz.getSzamlaTipus().equals("egyszeri") && radioButtonIsmetlodo2.isChecked())
                {
                    dataBaseHelper.FrissitesSzamlaTipus(adapter.getItem(position), "ismetlodo");
                    dataBaseHelper.FrissitesIsmetlodesGyakorisag(adapter.getItem(position), spinnerIsmetlodes2.getSelectedItem().toString());
                }
                if (sz.getSzamlaTipus().equals("ismetlodo") && radioButtonEgyszeri2.isChecked())
                {
                    dataBaseHelper.FrissitesSzamlaTipus(adapter.getItem(position), "egyszeri");
                    dataBaseHelper.FrissitesIsmetlodesGyakorisagNull(adapter.getItem(position), "null");
                }
                dialog2.dismiss();
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
                dataBaseHelper.Torles(adapter.getItem(position));

                listaRecyclerView.remove(position);
                adapter.notifyItemRemoved(position);
                adapter.notifyItemRangeChanged(position, listaSzamlak.size());

                adapter.notifyDataSetChanged();

                dialog2.hide();
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
            listaRecyclerView.add(position, toroltszamla);
            adapter.notifyItemInserted(position);
            dialog.hide();
        });
    }
}













