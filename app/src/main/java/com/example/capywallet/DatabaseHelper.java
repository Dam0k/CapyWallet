package com.example.capywallet;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "expenses.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_EXPENSES = "expenses";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_AMOUNT = "amount";
    public static final String COLUMN_CATEGORY = "category";

    public static final String TABLE_IMAGES = "images";
    public static final String COLUMN_IMAGE_ID = "_id";
    public static final String COLUMN_IMAGE_DATA = "image_data";

    public static final String TABLE_INCOME = "income";
    public static final String COLUMN_INCOME_ID = "_id";
    public static final String COLUMN_INCOME_AMOUNT = "amount";

    private static final String CREATE_TABLE_EXPENSES = "CREATE TABLE " + TABLE_EXPENSES + "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_NAME + " TEXT, " + COLUMN_AMOUNT + " REAL, " + COLUMN_CATEGORY + " TEXT" + ")";

    private static final String CREATE_TABLE_IMAGES = "CREATE TABLE " + TABLE_IMAGES + "(" + COLUMN_IMAGE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_IMAGE_DATA + " BLOB" + ")";

    private static final String CREATE_TABLE_INCOME = "CREATE TABLE " + TABLE_INCOME + "(" + COLUMN_INCOME_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_INCOME_AMOUNT + " REAL" + ")";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_EXPENSES);
        db.execSQL(CREATE_TABLE_IMAGES);
        db.execSQL(CREATE_TABLE_INCOME);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXPENSES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_IMAGES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_INCOME);
        onCreate(db);
    }

    public double getIncome() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_INCOME;
        Cursor cursor = db.rawQuery(query, null);
        double income = 0;
        if (cursor.moveToFirst()) {
            income = cursor.getDouble(cursor.getColumnIndex(COLUMN_INCOME_AMOUNT));
        }
        cursor.close();
        db.close();
        return income;
    }

    public long saveIncome(double income) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_INCOME_AMOUNT, income);
        long id = db.insert(TABLE_INCOME, null, values);
        db.close();
        return id;
    }

    public long addExpense(Expense expense) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, expense.getName());
        values.put(COLUMN_AMOUNT, expense.getAmount());
        values.put(COLUMN_CATEGORY, expense.getCategory());
        long id = db.insert(TABLE_EXPENSES, null, values);
        db.close();
        return id;
    }

    public List<Expense> getAllExpenses() {
        List<Expense> expenseList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_EXPENSES;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Expense expense = new Expense();
                expense.setId(cursor.getLong(cursor.getColumnIndex(COLUMN_ID)));
                expense.setName(cursor.getString(cursor.getColumnIndex(COLUMN_NAME)));
                expense.setAmount(cursor.getDouble(cursor.getColumnIndex(COLUMN_AMOUNT)));
                expense.setCategory(cursor.getString(cursor.getColumnIndex(COLUMN_CATEGORY)));
                expenseList.add(expense);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return expenseList;
    }

    public int deleteExpense(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int deletedRows = db.delete(TABLE_EXPENSES, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
        return deletedRows;
    }

    public long saveImage(byte[] imageData) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_IMAGE_DATA, imageData);
        long id = db.insert(TABLE_IMAGES, null, values);
        db.close();
        return id;
    }

    public byte[] getImage(long imageId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_IMAGES, new String[]{COLUMN_IMAGE_DATA}, COLUMN_IMAGE_ID + " = ?", new String[]{String.valueOf(imageId)}, null, null, null, null);
        byte[] imageData = null;
        if (cursor != null && cursor.moveToFirst()) {
            imageData = cursor.getBlob(cursor.getColumnIndex(COLUMN_IMAGE_DATA));
            cursor.close();
        }
        db.close();
        return imageData;
    }

    public long getLastImageId() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_IMAGES, new String[]{COLUMN_IMAGE_ID}, null, null, null, null, COLUMN_IMAGE_ID + " DESC", "1");
        long lastImageId = -1;
        if (cursor != null && cursor.moveToFirst()) {
            lastImageId = cursor.getLong(cursor.getColumnIndex(COLUMN_IMAGE_ID));
            cursor.close();
        }
        db.close();
        return lastImageId;
    }

    public long getExistingIncomeId() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " + COLUMN_INCOME_ID + " FROM " + TABLE_INCOME;
        Cursor cursor = db.rawQuery(query, null);
        long incomeId = -1;
        if (cursor.moveToFirst()) {
            incomeId = cursor.getLong(cursor.getColumnIndex(COLUMN_INCOME_ID));
        }
        cursor.close();
        db.close();
        return incomeId;
    }

    public int updateIncome(long incomeId, double income) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_INCOME_AMOUNT, income);
        int rowsAffected = db.update(TABLE_INCOME, values, COLUMN_INCOME_ID + " = ?", new String[]{String.valueOf(incomeId)});
        db.close();
        return rowsAffected;
    }

    public double getTotalExpenses() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT SUM(" + COLUMN_AMOUNT + ") FROM " + TABLE_EXPENSES;
        Cursor cursor = db.rawQuery(query, null);
        double totalExpenses = 0;
        if (cursor.moveToFirst()) {
            totalExpenses = cursor.getDouble(0);
        }
        cursor.close();
        db.close();
        return totalExpenses;
    }
}