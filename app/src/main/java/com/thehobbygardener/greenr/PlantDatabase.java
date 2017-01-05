package com.thehobbygardener.greenr;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

public class PlantDatabase extends SQLiteAssetHelper {

    private static final String DATABASE_NAME = "plants.db";
    private static final int DATABASE_VERSION = 1;

    public PlantDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public Cursor getPlantDetails(){

        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        String[] sqlSelect = {
                "NAME",
                "SUN_REQ",
                "PH",
                "SOIL",
                "COMPANION",
                "ALLY",
                "ENEMY"
        };
        String sqlTables = "PLANTS";

        qb.setTables(sqlTables);
        Cursor cursor = qb.query(db, sqlSelect, null, null, null, null, null);

        cursor.moveToFirst();
        return cursor;
    }

}
