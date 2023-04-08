package com.example.appandroid.sqlite;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class SQLiteHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "store.db";
    private static int DATABASE_VERSION = 1;
    public SQLiteHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String tableUser = "CREATE TABLE user(" +
        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
        "username TEXT, password TEXT)";
        sqLiteDatabase.execSQL(tableUser);

        String tableProduct = "CREATE TABLE product(" +
        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
        "nameProduct TEXT, cost INTEGER, quantity INTEGER, type TEXT, pathImage: TEXT)";
        sqLiteDatabase.execSQL(tableProduct);

        String tableCart = "CREATE TABLE cart(" +
        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
        "nameProduct TEXT, quantityOrder INTEGER, costProduct INTEGER, idUser INTEGER, pathImage TEXT," +
        "FOREIGN KEY(idUser) REFERENCES user(id))";
        sqLiteDatabase.execSQL(tableCart);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
    }
}
