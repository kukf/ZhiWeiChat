package com.doohaa.chat.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DatabaseHelper extends BaseDatabaseHelper {
    private final static String TAG = "DatabaseHelper";
    private static DatabaseHelper instance = null;

    private SQLiteDatabase _db;

    public DatabaseHelper(Context context) {
        super(context, DatabaseConstants.DATABASE_NAME, null, DatabaseConstants.DATABASE_VERSION);

        _db = getWritableDatabase();
    }

    private void debugPrintTable(SQLiteDatabase db) {
        String sql = "SELECT name FROM sqlite_master WHERE type='table';";
        Cursor c = db.rawQuery(sql, null);
        if (c.moveToFirst() == false) {
            return;
        }
        for (int index = 0; index < c.getCount(); index++) {
            c.moveToPosition(index);
        }
    }

    public static synchronized DatabaseHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseHelper(context.getApplicationContext());
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // create Table
        createNoteTable(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        if (newVersion <= oldVersion) {
            return;
        }
        dropAll(db);
        onCreate(db);
    }

    public void dropAll(SQLiteDatabase db) {
        // drop table
        dropNoteTable(db);
    }

    private void createNoteTable(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + DatabaseConstants.NOTE.TABLE_NAME
                + " ("
                + DatabaseConstants.NOTE.NOTE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + DatabaseConstants.NOTE.CONTENT + " TEXT,"
                + DatabaseConstants.NOTE.UPDATE_TIME + " TEXT"
                + ");");
    }

    private void dropNoteTable(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseConstants.NOTE.TABLE_NAME);
    }
}
