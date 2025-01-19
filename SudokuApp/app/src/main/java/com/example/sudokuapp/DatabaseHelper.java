package com.example.sudokuapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;
import android.database.Cursor;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "users.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_USERS = "users";
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_PASSWORD = "password";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_USERS + " (" +
                COLUMN_USERNAME + " TEXT PRIMARY KEY, " +
                COLUMN_PASSWORD + " TEXT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }

    public boolean addUser(String username, String hashedPassword) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, username);
        values.put(COLUMN_PASSWORD, hashedPassword);
        long result = db.insert(TABLE_USERS, null, values);
        return result != -1;
    }

    public boolean checkUser(String username, String hashedPassword) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        try {
            String query = "SELECT * FROM " + TABLE_USERS + " WHERE " +
                    COLUMN_USERNAME + "=? AND " + COLUMN_PASSWORD + "=?";
            cursor = db.rawQuery(query, new String[]{username, hashedPassword});

            return cursor.moveToFirst();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
}
