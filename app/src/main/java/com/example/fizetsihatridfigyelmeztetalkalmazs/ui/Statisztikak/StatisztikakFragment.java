package com.example.fizetsihatridfigyelmeztetalkalmazs.ui.Statisztikak;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.fizetsihatridfigyelmeztetalkalmazs.DataBaseHelper;
import com.example.fizetsihatridfigyelmeztetalkalmazs.R;
import com.example.fizetsihatridfigyelmeztetalkalmazs.Szamla;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class StatisztikakFragment extends Fragment {

    DataBaseHelper dataBaseHelper;
    String devizaJel = "";
    List<Szamla> listaElvegzettSzamlak = new ArrayList<>();
    List<Szamla> listaNemElvegzettSzamlak = new ArrayList<>();
    List<Map.Entry<Szamla, Date>> listaSzamlaDatumokkal = new ArrayList<Map.Entry<Szamla, Date>>();
    List<Integer> listaHaviAtlagok = new ArrayList<>();
    List<Integer> listaEvszakAtlagok = new ArrayList<>();
    TextView textViewOsszesenBefizetett, textViewTovabbiakbanBefizetendo, textViewElmulasztott, textViewHaviAtlag;

    PieChart pieChartEvszakAtlagok;
    List<PieEntry> pieEntries = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_statisztikak, container, false);
        dataBaseHelper = new DataBaseHelper(getContext());

        textViewOsszesenBefizetett = root.findViewById(R.id.textViewOsszesenBefizetett);
        textViewTovabbiakbanBefizetendo = root.findViewById(R.id.textViewTovabbiakbanBefizetendo);
        textViewElmulasztott = root.findViewById(R.id.textViewElmulasztott);
        textViewHaviAtlag = root.findViewById(R.id.textViewHaviAtlag);
        pieChartEvszakAtlagok = root.findViewById(R.id.pieChartEvszakAtlagok);

        listaElvegzettSzamlak = dataBaseHelper.AdatbazisbolElvegzettekLekerese();
        listaNemElvegzettSzamlak = dataBaseHelper.AdatbazisbolNemElvegzettekLekerese();

        OsszesenBefizetettBeallítas();
        TovabbiakbanBefizetendoBeallitas();
        ElmulasztottBeallitas();
        HaviAtlagBeallitas();
        ValutaBeallitas();
        DiagramBeallitas();

        return root;
    }

    private void DiagramBeallitas()
    {
        listaSzamlaDatumokkal.clear();
        listaEvszakAtlagok.clear();
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");

        for (Szamla sz : listaElvegzettSzamlak)
        {
            Date parsed = null;
            try {
                parsed = format.parse(sz.getSzamlaHatarido());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Date date = new Date(parsed.getTime());

            listaSzamlaDatumokkal.add(new AbstractMap.SimpleEntry<Szamla, Date>(sz, date));
        }

        int tavaszSum = 0;
        int nyarSum = 0;
        int oszSum = 0;
        int telSum = 0;

        int tavaszSzamlaCounter = 0;
        int nyarSzamlaCounter = 0;
        int oszSzamlaCounter = 0;
        int telSzamlaCounter = 0;

        for (Map.Entry<Szamla, Date> sz : listaSzamlaDatumokkal)
        {
            Log.d("honap", String.valueOf(sz.getValue().getMonth()));
            if ((((0 <= sz.getValue().getMonth()) && (sz.getValue().getMonth() <= 1)) || sz.getValue().getMonth() == 11)
            && (sz.getValue().getYear() == format.getCalendar().getTime().getYear()))
            {
                telSum += sz.getKey().getSzamlaOsszeg();
                telSzamlaCounter++;
            }
            else if ((2 <= sz.getValue().getMonth()) && (sz.getValue().getMonth() <= 4)
                    && (sz.getValue().getYear() == format.getCalendar().getTime().getYear()))
            {
                tavaszSum += sz.getKey().getSzamlaOsszeg();
                tavaszSzamlaCounter++;
            }
            else if ((5 <= sz.getValue().getMonth()) && (sz.getValue().getMonth() <= 7)
                    && (sz.getValue().getYear() == format.getCalendar().getTime().getYear()))
            {
                nyarSum += sz.getKey().getSzamlaOsszeg();
                nyarSzamlaCounter++;
            }
            else if ((sz.getValue().getYear() == format.getCalendar().getTime().getYear()))
            {
                oszSum += sz.getKey().getSzamlaOsszeg();
                oszSzamlaCounter++;
            }
        }

        if (telSzamlaCounter == 0) telSzamlaCounter = 1;
        if (tavaszSzamlaCounter == 0) tavaszSzamlaCounter = 1;
        if (nyarSzamlaCounter == 0) nyarSzamlaCounter = 1;
        if (oszSzamlaCounter == 0) oszSzamlaCounter = 1;

        listaEvszakAtlagok.add(telSum/telSzamlaCounter);
        listaEvszakAtlagok.add(tavaszSum/tavaszSzamlaCounter);
        listaEvszakAtlagok.add(nyarSum/nyarSzamlaCounter);
        listaEvszakAtlagok.add(oszSum/oszSzamlaCounter);

        pieEntries.add(new PieEntry(listaEvszakAtlagok.get(0), "Tél"));
        pieEntries.add(new PieEntry(listaEvszakAtlagok.get(1), "Tavasz"));
        pieEntries.add(new PieEntry(listaEvszakAtlagok.get(2), "Nyár"));
        pieEntries.add(new PieEntry(listaEvszakAtlagok.get(3), "Ősz"));

        List<Integer> colors = new ArrayList<>();
        colors.add(Color.rgb(52, 58, 89));
        colors.add(Color.rgb(135, 115, 27));
        colors.add(Color.rgb(117, 0, 0));
        colors.add(Color.rgb(117, 51, 0));

        PieDataSet pieDataSet = new PieDataSet(pieEntries, "Évszakok");
//        pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        pieDataSet.setColors(colors);
        pieDataSet.setValueTextColor(Color.WHITE);
        pieDataSet.setValueTextSize(16f);

        PieData pieData = new PieData(pieDataSet);

        pieChartEvszakAtlagok.setData(pieData);
        pieChartEvszakAtlagok.getDescription().setEnabled(false);
        pieChartEvszakAtlagok.setCenterTextSize(30f);
        pieChartEvszakAtlagok.setCenterText(devizaJel);
        pieChartEvszakAtlagok.setHoleColor(Color.rgb(161, 161, 161));
        pieChartEvszakAtlagok.animate();
    }

    private void ValutaBeallitas()
    {
        String valuta = dataBaseHelper.getValuta();
        if (valuta.equals("HUF"))
        {
            textViewOsszesenBefizetett.setText(textViewOsszesenBefizetett.getText() + " Ft");
            textViewTovabbiakbanBefizetendo.setText(textViewTovabbiakbanBefizetendo.getText() + " Ft");
            textViewElmulasztott.setText(textViewElmulasztott.getText() + " Ft");
            textViewHaviAtlag.setText(textViewHaviAtlag.getText() + " Ft");
            devizaJel = "Ft";
        }
        else if (valuta.equals("EUR"))
        {
            textViewOsszesenBefizetett.setText("€ " + textViewOsszesenBefizetett.getText());
            textViewTovabbiakbanBefizetendo.setText("€ " + textViewTovabbiakbanBefizetendo.getText());
            textViewElmulasztott.setText("€ " + textViewElmulasztott.getText());
            textViewHaviAtlag.setText("€ " + textViewHaviAtlag.getText());
            devizaJel = "€";
        }
        else
        {
            textViewOsszesenBefizetett.setText("$ " + textViewOsszesenBefizetett.getText());
            textViewTovabbiakbanBefizetendo.setText("$ " + textViewTovabbiakbanBefizetendo.getText());
            textViewElmulasztott.setText("$ " + textViewElmulasztott.getText());
            textViewHaviAtlag.setText("$ " + textViewHaviAtlag.getText());
            devizaJel = "$";
        }
    }

    private void HaviAtlagBeallitas()
    {
        listaSzamlaDatumokkal.clear();
        listaHaviAtlagok.clear();
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");

        for (Szamla sz : listaElvegzettSzamlak)
        {
            Date parsed = null;
            try {
                parsed = format.parse(sz.getSzamlaHatarido());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Date date = new Date(parsed.getTime());

            listaSzamlaDatumokkal.add(new AbstractMap.SimpleEntry<Szamla, Date>(sz, date));
        }

        int honap = 1;
        int sum = 0;
        int szamlaCounter = 0;

        while(honap < 13)
        {
            for (Map.Entry<Szamla, Date> sz : listaSzamlaDatumokkal)
            {
                if ((sz.getValue().getMonth() == honap) && (sz.getValue().getYear() == format.getCalendar().getTime().getYear()))
                {
                    sum += sz.getKey().getSzamlaOsszeg();
                    szamlaCounter++;
                }
            }
            if (szamlaCounter == 0)
            {
                honap++;
            }
            else
            {
                listaHaviAtlagok.add(sum / szamlaCounter);
                sum = 0;
                szamlaCounter = 0;
                honap++;
            }
        }

        sum = 0;
        for (int haviAtlag : listaHaviAtlagok)
        {
            sum += haviAtlag;
        }
        textViewHaviAtlag.setText(String.valueOf(sum/listaHaviAtlagok.size()));
    }

    private void ElmulasztottBeallitas()
    {
        listaSzamlaDatumokkal.clear();
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");

        for (Szamla sz : listaNemElvegzettSzamlak)
        {
            Date parsed = null;
            try {
                parsed = format.parse(sz.getSzamlaHatarido());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Date date = new Date(parsed.getTime());

            listaSzamlaDatumokkal.add(new AbstractMap.SimpleEntry<Szamla, Date>(sz, date));
        }

        int sum = 0;

        for(Map.Entry<Szamla, Date> sz : listaSzamlaDatumokkal)
        {
            if (System.currentTimeMillis() > sz.getValue().getTime())
            {
                sum += sz.getKey().getSzamlaOsszeg();
            }
        }
        textViewElmulasztott.setText(String.valueOf(sum));
    }

    private void TovabbiakbanBefizetendoBeallitas()
    {
        int sum = 0;
        for(Szamla sz : listaNemElvegzettSzamlak)
        {
            sum += sz.getSzamlaOsszeg();
        }
        textViewTovabbiakbanBefizetendo.setText(String.valueOf(sum));
    }

    private void OsszesenBefizetettBeallítas()
    {
        int sum = 0;
        for(Szamla sz : listaElvegzettSzamlak)
        {
            sum += sz.getSzamlaOsszeg();
        }
        textViewOsszesenBefizetett.setText(String.valueOf(sum));
    }
}