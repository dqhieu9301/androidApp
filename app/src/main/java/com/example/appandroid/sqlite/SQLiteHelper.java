package com.example.appandroid.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.appandroid.model.Product;

import java.util.ArrayList;
import java.util.List;

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
        "nameProduct TEXT, cost INTEGER, quantity INTEGER, type TEXT, pathImage TEXT)";
        sqLiteDatabase.execSQL(tableProduct);

        String tableCart = "CREATE TABLE cart(" +
        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
        "nameProduct TEXT, quantityOrder INTEGER, costProduct INTEGER, idUser INTEGER, pathImage TEXT," +
        "FOREIGN KEY(idUser) REFERENCES user(id))";
        sqLiteDatabase.execSQL(tableCart);

        List<Product> productList = new ArrayList<>();
        productList.add(new Product("Pizza 1", "Pizza", "pizza_chicken", 300000, 40));
        productList.add(new Product("Pizza Chicken", "Pizza", "pizza_chicken", 300000, 40));
        productList.add(new Product("Pizza Chicken", "Pizza", "pizza_chicken", 300000, 40));
        productList.add(new Product("Pizza Chicken", "Pizza", "pizza_chicken", 300000, 40));
        productList.add(new Product("Pizza Chicken", "Pizza", "pizza_chicken", 300000, 40));
        productList.add(new Product("Pizza 1", "Hamburger", "pizza_chicken", 300000, 40));

        productList.forEach((item) -> {
            ContentValues values = new ContentValues();
            values.put("nameProduct", item.getNameProduct());
            values.put("cost", item.getCost());
            values.put("quantity", item.getCost());
            values.put("type", item.getType());
            values.put("pathImage", item.getPathImage());
            long id = sqLiteDatabase.insert("product", null, values);
        });

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
    }

    public List<Product> getListProductByType(String type) {
        List<Product> productList = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();

        String selection = "type = ?";
        String[] selectionArgs = {type};
        Cursor cursor = sqLiteDatabase.query("product", null ,selection,
                selectionArgs, null , null, null );
        while (cursor.moveToNext()) {
            String column1Value = cursor.getString(cursor.getColumnIndexOrThrow("nameProduct"));
            String column2Value = cursor.getString(cursor.getColumnIndexOrThrow("cost"));
            String column3Value = cursor.getString(cursor.getColumnIndexOrThrow("quantity"));
            String column4Value = cursor.getString(cursor.getColumnIndexOrThrow("type"));
            String column5Value = cursor.getString(cursor.getColumnIndexOrThrow("pathImage"));
            productList.add(new Product(column1Value, column4Value, column5Value, Integer.parseInt(column2Value), Integer.parseInt(column3Value)));
        }
        return  productList;
    }
}
