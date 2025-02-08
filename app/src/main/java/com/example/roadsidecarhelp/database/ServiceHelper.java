package com.example.roadsidecarhelp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ServiceHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "services.db";
    private static final int DATABASE_VERSION = 1;
// names of services table
    public static final String TABLE_SERVICES = "services";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_TYPE = "type"; // Gas station, hotel, restaurant
    public static final String COLUMN_LATITUDE = "latitude";
    public static final String COLUMN_LONGITUDE = "longitude";
    public static final String COLUMN_CONTACT = "contact";

    public ServiceHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
//called when the database is created for the first time to create services table
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_SERVICES + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_TYPE + " TEXT, " +
                COLUMN_LATITUDE + " REAL, " +
                COLUMN_LONGITUDE + " REAL, " +
                COLUMN_CONTACT + " TEXT)";
        db.execSQL(createTable);
    }
//deletes old service table and makes a new one
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SERVICES);
        onCreate(db);
    }
//adds new service
    public void insertService(String name, String type, double latitude, double longitude, String contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_TYPE, type);
        values.put(COLUMN_LATITUDE, latitude);
        values.put(COLUMN_LONGITUDE, longitude);
        values.put(COLUMN_CONTACT, contact);
        db.insert(TABLE_SERVICES, null, values);
        db.close();
    }
//shows all services
    public Cursor getAllServices() {

        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_SERVICES, null);
    }


}

