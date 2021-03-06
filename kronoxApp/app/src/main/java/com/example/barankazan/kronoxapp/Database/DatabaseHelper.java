package com.example.barankazan.kronoxapp.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Tar hand om databasen för att skapa databasen, uppgradera veriosnen, lägga till data,
 * hämta data, radera data och allt annat.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Schedule.db";
    private static final int DATABASE_VERSION = 1;
    public static final String DATABASE_TABLE ="schedules";
    public static final String COLUMN_ID = "ID";
    public static final String COLUMN_NAME = "NAME";
    public static final String COLUMN_URL = "URL";

    /**
     * Konstruktor som hämtar context och ställer in namnen för databasen samt versionen.
     * @param context
     */

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Metod anropas bara om lokala databasen existerar inte.
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_TABLE = "CREATE TABLE "+DATABASE_TABLE+" ("+COLUMN_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+COLUMN_NAME+" TEXT, "+COLUMN_URL+" TEXT);";

        db.execSQL(SQL_CREATE_TABLE);
    }

    /**
     * Om konstruktor kräver version som är högre än den existerande databas körs den här metoden.
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
    }
}
