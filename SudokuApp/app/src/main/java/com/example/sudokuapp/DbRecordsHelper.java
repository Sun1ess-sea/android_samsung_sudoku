package com.example.sudokuapp;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;
import android.util.Log;

public class DbRecordsHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "newRecords.db"; // База для рекордов
    private static final int DATABASE_VERSION = 3; // Оставляем фиксированное значение

    public static final String TABLE_RECORDS = "records";
    public static final String COLUMN_ID = "id"; // Новый уникальный ID
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_LEVEL = "level";
    public static final String COLUMN_TIME = "time";

    public DbRecordsHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Проверяем, существует ли уже таблица (чтобы избежать дублирования)
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_RECORDS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + // Уникальный ID
                COLUMN_USERNAME + " TEXT, " +
                COLUMN_LEVEL + " TEXT, " +
                COLUMN_TIME + " TEXT" +
                ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Если потребуется обновление, можно изменить структуру таблицы
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECORDS);
        onCreate(db);
    }

    // Метод для добавления нового рекорда (без конфликтов)
    public boolean addRecord(String username, String level, String time) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_USERNAME, username);
        values.put(COLUMN_LEVEL, level);
        values.put(COLUMN_TIME, time);

        long result = db.insert(TABLE_RECORDS, null, values);

        if (result == -1) {
            Log.e("DatabaseRecordsHelper", "Ошибка при добавлении");
            return false; // Ошибка при вставке
        } else {
            Log.d("DatabaseRecordsHelper", "Рекорд добавлен успешно");
            return true; // Успех
        }
    }

    // Метод для получения всех рекордов (без дубликатов)
    public Cursor getAllRecords() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_RECORDS + " ORDER BY " + COLUMN_TIME + " ASC", null);
    }

    public Cursor getTopRecords() {
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT r.id AS _id, r.username, r.level, MIN(r.time) AS time " +
                "FROM records r " +
                "GROUP BY r.username, r.level " +
                "ORDER BY r.username ASC, r.level ASC";

        return db.rawQuery(query, null);
    }

}
