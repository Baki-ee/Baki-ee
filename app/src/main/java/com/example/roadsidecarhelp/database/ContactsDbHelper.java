package com.example.roadsidecarhelp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.roadsidecarhelp.model.Contact;

import java.util.ArrayList;

public class ContactsDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "contacts.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_CONTACTS = "contacts";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_PHONE = "phoneNumber";
    private static final String COLUMN_RELATIONSHIP = "relationship";
//initializes database
    public ContactsDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
//creates table when database is created
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_CONTACTS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_PHONE + " TEXT, " +
                COLUMN_RELATIONSHIP + " TEXT)";
        db.execSQL(createTable);
    }
//update database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);
        onCreate(db);
    }
//add contact
    public void addContact(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, contact.getName());
        values.put(COLUMN_PHONE, contact.getPhoneNumber());
        values.put(COLUMN_RELATIONSHIP, contact.getRelationship());

        db.insert(TABLE_CONTACTS, null, values);
        db.close();
    }
//shows all contacts
    public ArrayList<Contact> getAllContacts() {
        ArrayList<Contact> contactsList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_CONTACTS, null);

        if (cursor.moveToFirst()) {
            do {
                Contact contact = new Contact(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PHONE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_RELATIONSHIP))
                );
                contactsList.add(contact);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return contactsList;
    }
//updates contact
    public void updateContact(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, contact.getName());
        values.put(COLUMN_PHONE, contact.getPhoneNumber());
        values.put(COLUMN_RELATIONSHIP, contact.getRelationship());

        db.update(TABLE_CONTACTS, values, COLUMN_ID + " = ?", new String[]{String.valueOf(contact.getId())});
        db.close();
    }
// delets contact with matching coulmn id
    public void deleteContact(int contactId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CONTACTS, COLUMN_ID + " = ?", new String[]{String.valueOf(contactId)});
        db.close();
    }
}

