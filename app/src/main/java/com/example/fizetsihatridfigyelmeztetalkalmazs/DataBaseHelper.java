package com.example.fizetsihatridfigyelmeztetalkalmazs;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class DataBaseHelper extends SQLiteOpenHelper {

    public static final String SZAMLA_TABLA = "SZAMLA_TABLA";
    public static final String BEALLITASOK_TABLA = "BEALLITASOK_TABLA";

    //  Számla tábla attribútumok
    public static final String COLUMN_ID = "ID";
    public static final String COLUMN_TETEL_NEV = "TETEL_NEV";
    public static final String COLUMN_SZAMLA_OSSZEG = "SZAMLA_OSSZEG";
    public static final String COLUMN_SZAMLA_HATARIDO = "SZAMLA_HATARIDO";
    public static final String COLUMN_SZAMLA_TIPUS = "SZAMLA_TIPUS";
    public static final String COLUMN_ISMETLODES_GYAKORISAG = "ISMETLODES_GYAKORISAG";
    public static final String COLUMN_ELVEGZETT = "ELVEGZETT";


    // Beállítások tábla attribútumok
    public static final String COLUMN_ERTESITES = "ERTESITES";
    public static final String COLUMN_ERTESITES_IDOPONT = "ERTESITES_IDOPONT";
    public static final String COLUMN_ERTESITES_MOD = "ERTESITES_MOD";
    public static final String COLUMN_VALUTA = "VALUTA";

    public DataBaseHelper(@Nullable Context context) {
        super(context, "szamlak.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String createTableStatement = "CREATE TABLE " + SZAMLA_TABLA + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_TETEL_NEV + " TEXT, " + COLUMN_SZAMLA_OSSZEG + " INTEGER, " + COLUMN_SZAMLA_HATARIDO + " TEXT, " + COLUMN_SZAMLA_TIPUS + " TEXT, " + COLUMN_ISMETLODES_GYAKORISAG + " TEXT, " + COLUMN_ELVEGZETT + " BOOL)";

        String createTableStatement2 = "CREATE TABLE " + BEALLITASOK_TABLA + " (" + COLUMN_ERTESITES + " TEXT, " + COLUMN_ERTESITES_IDOPONT + " TIME, " + COLUMN_ERTESITES_MOD + " TEXT, " + COLUMN_VALUTA + " TEXT)";

        db.execSQL(createTableStatement);
        db.execSQL(createTableStatement2);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }





    public List<Szamla> AdatbazisbolOsszesLekerese()
    {
        List<Szamla> returnList = new ArrayList<>();

        String queryString = "SELECT * FROM " + SZAMLA_TABLA;

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(queryString, null);

        if (cursor.moveToFirst())
        {
            do {
                int id = cursor.getInt(0);
                String tetelNev = cursor.getString(1);
                int szamlaOsszeg = cursor.getInt(2);
                String szamlaHatarido = cursor.getString(3);
                String szamlaTipus = cursor.getString(4);
                String ismetlodesGyakorisag = cursor.getString(5);
                boolean elvegzett = cursor.getInt(6) == 1 ? true: false;

                Szamla sz = new Szamla(tetelNev, szamlaOsszeg, szamlaHatarido, szamlaTipus, ismetlodesGyakorisag, elvegzett);

                returnList.add(sz);

            } while (cursor.moveToNext());
        }
        else
        {

        }

        cursor.close();
        db.close();

        return returnList;
    }





    public List<Szamla> AdatbazisbolNemElvegzettekLekerese()
    {
        List<Szamla> returnList = new ArrayList<>();

        String queryString = "SELECT * FROM " + SZAMLA_TABLA + " WHERE " + COLUMN_ELVEGZETT + " = 0";

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(queryString, null);

        if (cursor.moveToFirst())
        {
            do {
                int id = cursor.getInt(0);
                String tetelNev = cursor.getString(1);
                int szamlaOsszeg = cursor.getInt(2);
                String szamlaHatarido = cursor.getString(3);
                String szamlaTipus = cursor.getString(4);
                String ismetlodesGyakorisag = cursor.getString(5);
                boolean elvegzett = cursor.getInt(6) == 1 ? true: false;

                Szamla sz = new Szamla(tetelNev, szamlaOsszeg, szamlaHatarido, szamlaTipus, ismetlodesGyakorisag, elvegzett);

                returnList.add(sz);

            } while (cursor.moveToNext());
        }
        else
        {

        }

        cursor.close();
        db.close();

        return returnList;
    }





    public List<Szamla> AdatbazisbolElvegzettekLekerese()
    {
        List<Szamla> returnList = new ArrayList<>();

        String queryString = "SELECT * FROM " + SZAMLA_TABLA + " WHERE " + COLUMN_ELVEGZETT + " = 1";

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(queryString, null);

        if (cursor.moveToFirst())
        {
            do {
                int id = cursor.getInt(0);
                String tetelNev = cursor.getString(1);
                int szamlaOsszeg = cursor.getInt(2);
                String szamlaHatarido = cursor.getString(3);
                String szamlaTipus = cursor.getString(4);
                String ismetlodesGyakorisag = cursor.getString(5);
                boolean elvegzett = cursor.getInt(6) == 1 ? true: false;

                Szamla sz = new Szamla(tetelNev, szamlaOsszeg, szamlaHatarido, szamlaTipus, ismetlodesGyakorisag, elvegzett);

                returnList.add(sz);

            } while (cursor.moveToNext());
        }
        else
        {

        }

        cursor.close();
        db.close();

        return returnList;
    }





    public boolean AdatbazishozHozzaadas(Szamla sz)
    {
//        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
//        String strDate = dateFormat.format(sz.getSzamlaHatarido());

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_TETEL_NEV, sz.getTetelNev());
        cv.put(COLUMN_SZAMLA_OSSZEG, sz.getSzamlaOsszeg());
        cv.put(COLUMN_SZAMLA_HATARIDO,  sz.getSzamlaHatarido());
        cv.put(COLUMN_SZAMLA_TIPUS, sz.getSzamlaTipus());
        cv.put(COLUMN_ISMETLODES_GYAKORISAG, sz.getIsmetlodesGyakorisag());
        cv.put(COLUMN_ELVEGZETT, sz.isElvegzett());

        long insert = db.insert(SZAMLA_TABLA, null, cv);

        db.close();

        if (insert == -1)
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    public void ElvegzetteNyilvanitas(Szamla sz)
    {
        String queryString = "UPDATE " + SZAMLA_TABLA +
                                " SET " + COLUMN_ELVEGZETT + " = 1 " +
                                "WHERE " + COLUMN_TETEL_NEV + " = '" + sz.getTetelNev() + "' AND " + COLUMN_SZAMLA_HATARIDO + " = '" + sz.getSzamlaHatarido() + "'";


        //UPDATE SZAMLA_TABLA SET ELVEGZETT = 1 WHERE TETEL_NEV = sz.getTetelNev() AND SZAMLA_HATARIDO = sz.getSzamlaHatarido();

        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(queryString);
        db.close();
    }

    public void Torles(Szamla sz)
    {
        String queryString = "DELETE FROM " + SZAMLA_TABLA +
                " WHERE " + COLUMN_TETEL_NEV + " = '" + sz.getTetelNev() + "' AND " + COLUMN_SZAMLA_HATARIDO + " = '" + sz.getSzamlaHatarido() + "'";


        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(queryString);
        db.close();
    }

    public void OsszesTorlese()
    {
        String queryString = "DELETE FROM " + SZAMLA_TABLA;


        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(queryString);
        db.close();
    }

    public List<String> AdatbazisbolBeallitasokLekerese()
    {
        List<String> returnList = new ArrayList<>();

        String queryString = "SELECT * FROM " + BEALLITASOK_TABLA;

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(queryString, null);

        if (cursor.moveToFirst())
        {
            do {
                String ertesites = cursor.getString(0);
                int ertesitesIdopont = cursor.getInt(1);
                String ertesitesMod = cursor.getString(2);
                String valuta = cursor.getString(3);

                returnList.add(ertesites);
                returnList.add(String.valueOf(ertesitesIdopont));
                returnList.add(ertesitesMod);
                returnList.add(valuta);

            } while (cursor.moveToNext());
        }
        else
        {

        }

        cursor.close();
        db.close();

        return returnList;
    }

    public void BeallitasokMentese(String ertesites, String ertesitesIdopontja, String ertesitesiMod, String valuta)
    {
        String queryString = "UPDATE " + BEALLITASOK_TABLA +
                " SET " + COLUMN_ERTESITES + " = '" + ertesites + "', " + COLUMN_ERTESITES_IDOPONT + " = " + ertesitesIdopontja + ", " +
                COLUMN_ERTESITES_MOD + " = '" + ertesitesiMod + "', " + COLUMN_VALUTA + " = '" + valuta + "'";


        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(queryString);
        db.close();
    }

    public String getValuta()
    {
        String returnString = "";

        String queryString = "SELECT * FROM " + BEALLITASOK_TABLA;

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(queryString, null);

        if (cursor.moveToFirst())
        {
            do {
                String valuta = cursor.getString(3);

                returnString = valuta;

            } while (cursor.moveToNext());
        }
        else
        {

        }

        cursor.close();
        db.close();

        return returnString;
    }

    public void FrissitesTetelnev(Szamla sz, String ujnev)
    {
        String queryString = "UPDATE " + SZAMLA_TABLA +
                " SET " + COLUMN_TETEL_NEV + " = '" + ujnev + "' " +
                "WHERE " + COLUMN_SZAMLA_OSSZEG + " = " + sz.getSzamlaOsszeg() + " AND " + COLUMN_SZAMLA_HATARIDO + " = '" + sz.getSzamlaHatarido() + "'";


        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(queryString);
        db.close();
    }

    public void FrissitesOsszeg(Szamla sz, int ujosszeg)
    {
        String queryString = "UPDATE " + SZAMLA_TABLA +
                " SET " + COLUMN_SZAMLA_OSSZEG + " = " + ujosszeg + " " +
                "WHERE " + COLUMN_TETEL_NEV + " = '" + sz.getTetelNev() + "' AND " + COLUMN_SZAMLA_HATARIDO + " = '" + sz.getSzamlaHatarido() + "'";


        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(queryString);
        db.close();
    }

    public void FrissitesHatarido(Szamla sz, String ujhatido)
    {
        String queryString = "UPDATE " + SZAMLA_TABLA +
                " SET " + COLUMN_SZAMLA_HATARIDO + " = '" + ujhatido + "' " +
                "WHERE " + COLUMN_TETEL_NEV + " = '" + sz.getTetelNev() + "' AND " + COLUMN_SZAMLA_OSSZEG + " = " + sz.getSzamlaOsszeg() + "";


        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(queryString);
        db.close();
    }

    public void FrissitesSzamlaTipus(Szamla sz, String ujtipus)
    {
        String queryString = "UPDATE " + SZAMLA_TABLA +
                " SET " + COLUMN_SZAMLA_TIPUS + " = '" + ujtipus + "' " +
                "WHERE " + COLUMN_TETEL_NEV + " = '" + sz.getTetelNev() + "' AND " + COLUMN_SZAMLA_HATARIDO + " = '" + sz.getSzamlaHatarido() + "'";


        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(queryString);
        db.close();
    }

    public void FrissitesIsmetlodesGyakorisag(Szamla sz, String ujgyakorisag)
    {
        String queryString = "UPDATE " + SZAMLA_TABLA +
                " SET " + COLUMN_ISMETLODES_GYAKORISAG + " = '" + ujgyakorisag + "' " +
                "WHERE " + COLUMN_TETEL_NEV + " = '" + sz.getTetelNev() + "' AND " + COLUMN_SZAMLA_HATARIDO + " = '" + sz.getSzamlaHatarido() + "'";


        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(queryString);
        db.close();
    }

    public void FrissitesIsmetlodesGyakorisagNull(Szamla sz, String ujgyakorisag)
    {
        String queryString = "UPDATE " + SZAMLA_TABLA +
                " SET " + COLUMN_ISMETLODES_GYAKORISAG + " = " + ujgyakorisag + " " +
                "WHERE " + COLUMN_TETEL_NEV + " = '" + sz.getTetelNev() + "' AND " + COLUMN_SZAMLA_HATARIDO + " = '" + sz.getSzamlaHatarido() + "'";


        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(queryString);
        db.close();
    }

}



























