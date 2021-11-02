package com.example.fizetsihatridfigyelmeztetalkalmazs;

public class Felhasznalo {

    private String email, jelszo;

    public Felhasznalo() {
    }

    public Felhasznalo(String email, String jelszo) {
        this.email = email;
        this.jelszo = jelszo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getJelszo() {
        return jelszo;
    }

    public void setJelszo(String jelszo) {
        this.jelszo = jelszo;
    }
}
