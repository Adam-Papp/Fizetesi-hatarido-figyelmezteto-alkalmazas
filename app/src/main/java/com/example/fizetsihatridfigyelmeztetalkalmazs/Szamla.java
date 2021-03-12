package com.example.fizetsihatridfigyelmeztetalkalmazs;

import androidx.annotation.NonNull;

import java.util.Date;

public class Szamla
{
    private String tetelNev;
    private int szamlaOsszeg;
    private Date szamlaHatarido;
    private String szamlaTipus;
    private String ismetlodesGyakorisag;
    private boolean elvegzett;

    public Szamla(String tn, int szo, Date szh, String szt)
    {
        tetelNev = tn;
        szamlaOsszeg = szo;
        szamlaHatarido = szh;
        szamlaTipus = szt;
        elvegzett = false;
    }

    public Szamla(String tn, int szo, Date szh, String szt, String ig)
    {
        tetelNev = tn;
        szamlaOsszeg = szo;
        szamlaHatarido = szh;
        szamlaTipus = szt;
        ismetlodesGyakorisag = ig;
        elvegzett = false;
    }

    public boolean isElvegzett() {
        return elvegzett;
    }

    public void setElvegzett(boolean elvegzett) {
        this.elvegzett = elvegzett;
    }

    public String getTetelNev() {
        return tetelNev;
    }

    public void setTetelNev(String tetelNev) {
        this.tetelNev = tetelNev;
    }

    public int getSzamlaOsszeg() {
        return szamlaOsszeg;
    }

    public void setSzamlaOsszeg(int szamlaOsszeg) {
        this.szamlaOsszeg = szamlaOsszeg;
    }

    public Date getSzamlaHatarido() {
        return szamlaHatarido;
    }

    public void setSzamlaHatarido(Date szamlaHatarido) {
        this.szamlaHatarido = szamlaHatarido;
    }

    public String getSzamlaTipus() {
        return szamlaTipus;
    }

    public void setSzamlaTipus(String szamlaTipus) {
        this.szamlaTipus = szamlaTipus;
    }

    public String getIsmetlodesGyakorisag() {
        return ismetlodesGyakorisag;
    }

    public void setIsmetlodesGyakorisag(String ismetlodesGyakorisag) {
        this.ismetlodesGyakorisag = ismetlodesGyakorisag;
    }

    @NonNull
    @Override
    public String toString() {
        return super.toString();
    }
}
