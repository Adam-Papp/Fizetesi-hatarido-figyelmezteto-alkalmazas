package com.example.fizetsihatridfigyelmeztetalkalmazs;

import androidx.annotation.NonNull;

import java.util.Date;

public class Szamla
{
    private int id;
    private String tetelNev;
    private int szamlaOsszeg;
    private String szamlaHatarido;
    private String szamlaTipus;
    private String ismetlodesGyakorisag;
    private boolean elvegzett;

    public Szamla(String tn, int szo, String szh, String szt)
    {
        tetelNev = tn;
        szamlaOsszeg = szo;
        szamlaHatarido = szh;
        szamlaTipus = szt;
        ismetlodesGyakorisag = null;
        elvegzett = false;
    }

    public Szamla(String tn, int szo, String szh, String szt, String ig)
    {
        tetelNev = tn;
        szamlaOsszeg = szo;
        szamlaHatarido = szh;
        szamlaTipus = szt;
        ismetlodesGyakorisag = ig;
        elvegzett = false;
    }

    public Szamla(String tn, int szo, String szh, String szt, String ig, boolean e)
    {
        tetelNev = tn;
        szamlaOsszeg = szo;
        szamlaHatarido = szh;
        szamlaTipus = szt;
        ismetlodesGyakorisag = ig;
        elvegzett = false;
        elvegzett = e;
    }

    public Szamla(int id, String tn, int szo, String szh, String szt, String ig, boolean e)
    {
        this.id = id;
        tetelNev = tn;
        szamlaOsszeg = szo;
        szamlaHatarido = szh;
        szamlaTipus = szt;
        ismetlodesGyakorisag = ig;
        elvegzett = false;
        elvegzett = e;
    }

    public int getID() { return id; }

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

    public String getSzamlaHatarido() {
        return szamlaHatarido;
    }

    public void setSzamlaHatarido(String szamlaHatarido) {
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

    @Override
    public String toString() {
        return "Szamla{" +
                "tetelNev='" + tetelNev + '\'' +
                ", szamlaOsszeg=" + szamlaOsszeg +
                ", szamlaHatarido='" + szamlaHatarido + '\'' +
                ", szamlaTipus='" + szamlaTipus + '\'' +
                ", ismetlodesGyakorisag='" + ismetlodesGyakorisag + '\'' +
                ", elvegzett=" + elvegzett +
                '}';
    }
}
