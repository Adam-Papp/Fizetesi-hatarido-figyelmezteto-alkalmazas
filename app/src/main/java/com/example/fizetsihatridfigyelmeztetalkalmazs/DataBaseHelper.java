package com.example.fizetsihatridfigyelmeztetalkalmazs;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DataBaseHelper extends SQLiteOpenHelper {

    public static final String SZAMLA_TABLA = "SZAMLA_TABLA";

    public static final String COLUMN_ID = "ID";
    public static final String COLUMN_TETEL_NEV = "TETEL_NEV";
    public static final String COLUMN_SZAMLA_OSSZEG = "SZAMLA_OSSZEG";
    public static final String COLUMN_SZAMLA_HATARIDO = "SZAMLA_HATARIDO";
    public static final String COLUMN_SZAMLA_TIPUS = "SZAMLA_TIPUS";
    public static final String COLUMN_ISMETLODES_GYAKORISAG = "ISMETLODES_GYAKORISAG";

    public DataBaseHelper(@Nullable Context context) {
        super(context, "szamlak.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String createTableStatement = "CREATE TABLE " + SZAMLA_TABLA + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_TETEL_NEV + " TEXT, " + COLUMN_SZAMLA_OSSZEG + " INTEGER, " + COLUMN_SZAMLA_HATARIDO + " TEXT, " + COLUMN_SZAMLA_TIPUS + " TEXT, " + COLUMN_ISMETLODES_GYAKORISAG + " TEXT)";

        db.execSQL(createTableStatement);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
