package com.example.fizetsihatridfigyelmeztetalkalmazs.ui.Hozzaadas;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.fizetsihatridfigyelmeztetalkalmazs.DataBaseHelper;
import com.example.fizetsihatridfigyelmeztetalkalmazs.MainActivity;
import com.example.fizetsihatridfigyelmeztetalkalmazs.R;
import com.example.fizetsihatridfigyelmeztetalkalmazs.Szamla;

public class HozzaadasFragment extends Fragment {

    EditText editTextTetelNev, editTextOsszeg, editTextHatarido;
    RadioButton radioButtonEgyszeri, radioButtonIsmetlodo;
    Spinner spinnerIsmetlodes;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_hozzaadas, container, false);


        editTextTetelNev = root.findViewById(R.id.editTextTetelNev);
        editTextOsszeg = root.findViewById(R.id.editTextOsszeg);
        editTextHatarido = root.findViewById(R.id.editTextHatarido);
        radioButtonEgyszeri = root.findViewById(R.id.radioButtonEgyszeri);
        radioButtonIsmetlodo = root.findViewById(R.id.radioButtonIsmetlodo);
        spinnerIsmetlodes = root.findViewById(R.id.spinnerIsmetlodes);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.ismetlodes_array, android.R.layout.simple_spinner_item);

        spinnerIsmetlodes.setAdapter(adapter);


        radioButtonEgyszeri.setChecked(true);


        return root;
    }

    public void HozzaadasGombOnClick(View v)
    {
        DataBaseHelper dataBaseHelper = new DataBaseHelper(getActivity());

        Szamla sz;

        try
        {
//            sz = new Szamla();
        }
        catch (Exception e)
        {
            Toast.makeText(getActivity(), "Hiba történt a hozzáadás során", Toast.LENGTH_LONG).show();
        }
    }
}