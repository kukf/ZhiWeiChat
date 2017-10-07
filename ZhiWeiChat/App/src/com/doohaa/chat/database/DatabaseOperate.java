package com.doohaa.chat.database;


import android.content.Context;

import com.doohaa.chat.utils.Validator;


public class DatabaseOperate {
    public static void insertOrReplaceHomeData(Context context, String id, String value, DatabaseListener listener) {
        DatabaseHelper db = DatabaseHelper.getInstance(context);
        long currentTimeMillis = System.currentTimeMillis();
        String sql;
        if (Validator.isNotEmpty(id)) {
            sql = "INSERT OR REPLACE INTO " + DatabaseConstants.NOTE.TABLE_NAME + "("
                    + DatabaseConstants.NOTE.NOTE_ID
                    + ", " + DatabaseConstants.NOTE.CONTENT
                    + ", " + DatabaseConstants.NOTE.UPDATE_TIME + ") VALUES("
                    + id + ", '" + value + "', '" + currentTimeMillis + "');";

        } else {
            sql = "INSERT OR REPLACE INTO " + DatabaseConstants.NOTE.TABLE_NAME + "("
                    + DatabaseConstants.NOTE.CONTENT
                    + ", " + DatabaseConstants.NOTE.UPDATE_TIME + ") VALUES("
                    + "'" + value + "', '" + currentTimeMillis + "');";

        }
        db.execSqlAsync(sql, listener);
    }

    public static void queryNoteById(Context context, String id,
                                     DatabaseListener listener) {
        String whereClause = DatabaseConstants.NOTE.NOTE_ID + " = ? ";
        String[] whereArgs = {id};
        DatabaseHelper dbHelper = DatabaseHelper.getInstance(context);
        dbHelper.queryAsync(DatabaseConstants.NOTE.TABLE_NAME, DatabaseConstants.NOTE.COLUMNS, whereClause, whereArgs, "", listener);
    }

    public static void queryAllNote(Context context, DatabaseListener listener) {
        DatabaseHelper db = DatabaseHelper.getInstance(context);
        String order = DatabaseConstants.NOTE.UPDATE_TIME + " DESC";
        db.queryAsync(DatabaseConstants.NOTE.TABLE_NAME, DatabaseConstants.NOTE.COLUMNS, null, null, order, listener);
    }
}
