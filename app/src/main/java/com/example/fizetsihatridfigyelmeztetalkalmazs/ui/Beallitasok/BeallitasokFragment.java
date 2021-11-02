package com.example.fizetsihatridfigyelmeztetalkalmazs.ui.Beallitasok;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.telephony.mbms.MbmsErrors;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.fizetsihatridfigyelmeztetalkalmazs.DataBaseHelper;
import com.example.fizetsihatridfigyelmeztetalkalmazs.R;

import java.util.ArrayList;
import java.util.List;

public class BeallitasokFragment extends Fragment {

    Spinner spinnerErtesites, spinnerErtesitesiMod, spinnerValuta;
    EditText editTextErtesitesIdopontja;
    DataBaseHelper dataBaseHelper;
    Button buttonMentes;

    List<String> listBeallitasok = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_beallitasok, container, false);

        spinnerErtesites = root.findViewById(R.id.spinnerErtesites);
        spinnerErtesitesiMod = root.findViewById(R.id.spinnerErtesitesiMod);
        spinnerValuta = root.findViewById(R.id.spinnerValuta);
        editTextErtesitesIdopontja = root.findViewById(R.id.editTextErtesitesIdopontja);
        buttonMentes = root.findViewById(R.id.buttonMentes);

        dataBaseHelper = new DataBaseHelper(getActivity());
        listBeallitasok = dataBaseHelper.AdatbazisbolBeallitasokLekerese();

        if (listBeallitasok.size() == 0)
        {
            dataBaseHelper.AlapBeallitasokHozzaadasa();
            listBeallitasok = dataBaseHelper.AdatbazisbolBeallitasokLekerese();
        }

        int ertesitesPos = getErtesitesPos();
        int ertesitesModPos = getErtesitesModPos();
        int valutaPos = getValutaPos();

        setBeallitasok(ertesitesPos, ertesitesModPos, valutaPos);

        buttonMentes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("beall", "beallitasokmentese lefutott");
                if (editTextErtesitesIdopontja.getText().toString().length() < 3 )
                {
                    Toast.makeText(getContext(), "Hiba történt az Értesítés időpontja mentésénél", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    dataBaseHelper.BeallitasokMentese(spinnerErtesites.getSelectedItem().toString(), editTextErtesitesIdopontja.getText().toString().replace(":", "")
                            , spinnerErtesitesiMod.getSelectedItem().toString(), spinnerValuta.getSelectedItem().toString());
                }
            }
        });

        return root;
    }



    public void setBeallitasok(int ertesitesPos, int ertesitesModPos, int valutaPos)
    {
        if (ertesitesPos == -1 || ertesitesModPos == -1 || valutaPos == -1)
        {
            Toast.makeText(getContext(), "Hiba történt a beállítások beolvasása során.", Toast.LENGTH_LONG).show();
        }
        else
        {
            spinnerErtesites.setSelection(ertesitesPos);
            spinnerErtesitesiMod.setSelection(ertesitesModPos);
            spinnerValuta.setSelection(valutaPos);
            String idopont = listBeallitasok.get(1);
            if (idopont.length() == 2)
            {
                editTextErtesitesIdopontja.setText("0:" + idopont);
            }
            else if (idopont.length() == 3)
            {
                editTextErtesitesIdopontja.setText(idopont.charAt(0) + ":" + idopont.subSequence(1, 3));
            }
            else
            {
                editTextErtesitesIdopontja.setText(idopont.subSequence(0, 2) + ":" + idopont.subSequence(2, 4));
            }
        }
    }


    public int getErtesitesPos()
    {
        switch (listBeallitasok.get(0))
        {
            case "Aznap":
                return 0;
            case "Előtte 1 nappal":
                return 1;
            case "Előtte 3 nappal":
                return 2;
            case "Előtte 1 héttel":
                return 3;
        }
        return -1;
    }


    public int getErtesitesModPos()
    {
        switch (listBeallitasok.get(2))
        {
            case "Rendszer értesítés":
                return 0;
            case "E-mail":
                return 1;
            case "Rendszer értesítés + E-mail":
                return 2;
        }
        return -1;
    }


    public int getValutaPos()
    {
        switch (listBeallitasok.get(3))
        {
            case "HUF":
                return 0;
            case "EUR":
                return 1;
            case "USD":
                return 2;
        }
        return -1;
    }
}