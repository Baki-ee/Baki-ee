package com.example.roadsidecarhelp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.roadsidecarhelp.model.Users;

public class DBHelper extends SQLiteOpenHelper {

    public DBHelper(@Nullable Context context) {
        //create database here
        super(context, "roadside.db", null, 1);
    }
//created when the database is created ffor the first time to enter first user
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String customertable = "create table users (id integer primary key autoincrement,email text, password text,name text,username text,contact text,address text)";
        sqLiteDatabase.execSQL(customertable);

    }
//updated user table data
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("drop table if exists users");
        onCreate(sqLiteDatabase);

    }
//inserts new user
    public long insertusers(Users users) {

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();


        ContentValues cv = new ContentValues();
        cv.put("email", users.getEmail());
        cv.put("password",users.getPassword());
        cv.put("name",users.getName());
        cv.put("username",users.getUsername());
        cv.put("contact",users.getContact());
        cv.put("address",users.getAddress());

        return sqLiteDatabase.insert("users", null, cv);


    }
}

