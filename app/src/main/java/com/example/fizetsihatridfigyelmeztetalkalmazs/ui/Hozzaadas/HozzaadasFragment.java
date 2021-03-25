package com.example.fizetsihatridfigyelmeztetalkalmazs.ui.Hozzaadas;

import android.app.DatePickerDialog;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.fizetsihatridfigyelmeztetalkalmazs.DataBaseHelper;
import com.example.fizetsihatridfigyelmeztetalkalmazs.R;
import com.example.fizetsihatridfigyelmeztetalkalmazs.Szamla;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HozzaadasFragment extends Fragment {

    EditText editTextTetelNev, editTextOsszeg, editTextHatarido;
    RadioButton radioButtonEgyszeri, radioButtonIsmetlodo;
    RadioGroup radioGroupTipus;
    Spinner spinnerIsmetlodes;
    Button buttonHozzaadas, buttonMegsem;
    final Calendar myCalendar = Calendar.getInstance();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_hozzaadas, container, false);


        editTextTetelNev = root.findViewById(R.id.editTextTetelNev);
        editTextOsszeg = root.findViewById(R.id.editTextOsszeg);
        editTextHatarido = root.findViewById(R.id.editTextHatarido);
        radioButtonEgyszeri = root.findViewById(R.id.radioButtonEgyszeri);
        radioButtonIsmetlodo = root.findViewById(R.id.radioButtonIsmetlodo);
        radioGroupTipus = root.findViewById(R.id.radioGroupTipus);
        spinnerIsmetlodes = root.findViewById(R.id.spinnerIsmetlodes);
        buttonHozzaadas = root.findViewById(R.id.buttonHozzaadas);
        buttonMegsem = root.findViewById(R.id.buttonTorles);

        editTextTetelNev.setText("");
        editTextOsszeg.setText("");
        editTextHatarido.setText("");


        DatePickerDialog.OnDateSetListener datum = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }
        };

        editTextHatarido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getContext(), datum, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });



        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.ismetlodes_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerIsmetlodes.setAdapter(adapter);


        radioButtonEgyszeri.setChecked(true);
        spinnerIsmetlodes.setVisibility(View.GONE);

        radioGroupTipus.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == radioButtonEgyszeri.getId())
                {
                    spinnerIsmetlodes.setVisibility(View.GONE);
                }
                if (checkedId == radioButtonIsmetlodo.getId())
                {
                    spinnerIsmetlodes.setVisibility(View.VISIBLE);
                }
            }
        });

        buttonHozzaadas.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                DataBaseHelper dataBaseHelper = new DataBaseHelper(getActivity());

                Log.d("onclickButtonHozzaadas", "onClick esemény elindult");

                Szamla sz;
                List<Szamla> szList;
                boolean sikeres = false;

                try
                {
                    Log.d("onclickButtonHozzaadas", editTextTetelNev.getText().toString());
                    Log.d("onclickButtonHozzaadas", editTextOsszeg.getText().toString());
                    Log.d("onclickButtonHozzaadas", editTextHatarido.getText().toString());
                    if (!editTextTetelNev.getText().toString().equals("") && !editTextOsszeg.getText().toString().equals("") && !editTextHatarido.getText().toString().equals(""))
                    {
                        if (radioButtonEgyszeri.isChecked())
                        {
                            sz = new Szamla(editTextTetelNev.getText().toString(), Integer.parseInt(editTextOsszeg.getText().toString()),
                                    editTextHatarido.getText().toString(), "egyszeri");
                            sikeres = dataBaseHelper.AdatbazishozHozzaadas(sz);
                        }
                        else
                        {
                            szList = new ArrayList<>();

                            szList.add(new Szamla(editTextTetelNev.getText().toString(), Integer.parseInt(editTextOsszeg.getText().toString()),
                                    editTextHatarido.getText().toString(), "ismetlodo", spinnerIsmetlodes.getSelectedItem().toString()));

                            String ismetlodes = spinnerIsmetlodes.getSelectedItem().toString();
                            SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
                            Date hataridoKezdodes = format.parse(editTextHatarido.getText().toString());
                            Date hatarido = hataridoKezdodes;

                            Log.d("hatarido", format.format(hatarido));

                            switch (ismetlodes)
                            {
                                case "Havonta":
                                    for (int i=0; i<12; i++)
                                    {
                                        hatarido.setMonth(hatarido.getMonth()+1);

                                        szList.add(new Szamla(editTextTetelNev.getText().toString(), Integer.parseInt(editTextOsszeg.getText().toString()),
                                                format.format(hatarido), "ismetlodo", spinnerIsmetlodes.getSelectedItem().toString()));
                                    }
                                    break;
                                case "2 havonta":
                                    for(int i=0; i<6; i++)
                                    {
                                        hatarido.setMonth(hatarido.getMonth()+2);

                                        szList.add(new Szamla(editTextTetelNev.getText().toString(), Integer.parseInt(editTextOsszeg.getText().toString()),
                                                format.format(hatarido), "ismetlodo", spinnerIsmetlodes.getSelectedItem().toString()));
                                    }
                                    break;
                                case "3 havonta":
                                    for(int i=0; i<4; i++)
                                    {
                                        hatarido.setMonth(hatarido.getMonth()+3);

                                        szList.add(new Szamla(editTextTetelNev.getText().toString(), Integer.parseInt(editTextOsszeg.getText().toString()),
                                                format.format(hatarido), "ismetlodo", spinnerIsmetlodes.getSelectedItem().toString()));
                                    }
                                    break;
                                case "Félévente":
                                    for(int i=0; i<2; i++)
                                    {
                                        hatarido.setMonth(hatarido.getMonth()+6);

                                        szList.add(new Szamla(editTextTetelNev.getText().toString(), Integer.parseInt(editTextOsszeg.getText().toString()),
                                                format.format(hatarido), "ismetlodo", spinnerIsmetlodes.getSelectedItem().toString()));
                                    }
                                    break;
                                case "Évente":
                                    hatarido.setMonth(hatarido.getMonth()+12);

                                    szList.add(new Szamla(editTextTetelNev.getText().toString(), Integer.parseInt(editTextOsszeg.getText().toString()),
                                            format.format(hatarido), "ismetlodo", spinnerIsmetlodes.getSelectedItem().toString()));
                                    break;
                            }

                            for(Szamla s : szList)
                            {
                                sikeres = dataBaseHelper.AdatbazishozHozzaadas(s);
                            }
                        }
                        editTextTetelNev.setText("");
                        editTextOsszeg.setText("");
                        editTextHatarido.setText("");


                        Toast.makeText(getContext(), "Sikeres: " + sikeres, Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        throw new Exception();
                    }
                }
                catch (Exception e)
                {
                    Toast.makeText(getActivity(), "Hiba történt a hozzáadás során", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        });


        buttonMegsem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                editTextHatarido.setText("");
                editTextOsszeg.setText("");
                editTextTetelNev.setText("");
                radioButtonEgyszeri.toggle();
            }
        });


        return root;
    }



    private void updateLabel() {
        String myFormat = "yyyy/MM/dd";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());

        editTextHatarido.setText(sdf.format(myCalendar.getTime()));
    }
}



















