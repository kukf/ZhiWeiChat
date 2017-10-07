package com.doohaa.chat.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;


import com.doohaa.chat.utils.MapUtils;
import com.doohaa.chat.utils.Validator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class BaseDatabaseHelper extends SQLiteOpenHelper {
    private static final String TAG = "BaseDatabaseHelper";

    public BaseDatabaseHelper(Context context, String name, CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public void queryAsync(String table, String[] columns, String whereClause, String[] whereArgs, String orderBy,
                           DatabaseListener listener) {
        new DatabaseTask(listener).execute(new DatabaseArgs(DatabaseArgs.ArgsType.QUERY, table, columns, whereClause, whereArgs, orderBy));
    }

    public Cursor query(String table, String[] columns, String whereClause, String[] whereArgs, String orderBy) {
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.query(table, columns, whereClause, whereArgs, null, null, orderBy);
        return cursor;
    }

    public List<Map<String, Object>> queryToMapList(String table, String[] columns, String whereClause,
                                                    String[] whereArgs, String orderBy) {
        Cursor cursor = query(table, columns, whereClause, whereArgs, orderBy);

        List<Map<String, Object>> mapList = MapUtils.convertCursorToMapList(cursor);
        if (cursor != null /*&& !cursor.isClosed()*/) {
            cursor.close();
        }

        return mapList;
    }

    public <T> List<T> queryToObjectList(String table, String[] columns, String whereClause, String[] whereArgs,
                                         String orderBy, Class<? extends T> clazz) {
        List<Map<String, Object>> mapList = queryToMapList(table, columns, whereClause, whereArgs, orderBy);
        List<T> objList = null;

        try {
            objList = MapUtils.convertMapListToObjectList(mapList, clazz);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return objList;
    }

    public Cursor rawQuery(String sql) {
        return rawQuery(sql, null);
    }

    public void rawQueryAsync(String sql, DatabaseListener listener) {
        new DatabaseTask(listener).execute(new DatabaseArgs(DatabaseArgs.ArgsType.RAW_QUERY, sql));
    }

    public Cursor rawQuery(String sql, String[] selection) {
        SQLiteDatabase db = getWritableDatabase();

        return db.rawQuery(sql, selection);
    }

    public List<Map<String, Object>> rawQueryToMapList(String sql) {
        Cursor cursor = rawQuery(sql);

        List<Map<String, Object>> mapList = MapUtils.convertCursorToMapList(cursor);
        if (cursor != null /*&& !cursor.isClosed()*/) {
            cursor.close();
        }

        return mapList;
    }

    public <T> List<T> rawQueryToObjectList(String sql, Class<? extends T> clazz) {
        List<Map<String, Object>> mapList = rawQueryToMapList(sql);
        List<T> objList = null;

        try {
            objList = MapUtils.convertMapListToObjectList(mapList, clazz);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return objList;
    }

    public long insert(String table, ContentValues values) {
        SQLiteDatabase db = getWritableDatabase();

        return db.insert(table, null, values);
    }

    public void insertAsync(String table, ContentValues values, DatabaseListener listener) {
        List<ContentValues> valuesList = new ArrayList<ContentValues>();
        valuesList.add(values);

        new DatabaseTask(listener).execute(new DatabaseArgs(DatabaseArgs.ArgsType.INSERT, table, valuesList));
    }

    public void insertAsync(String table, List<ContentValues> valuesList, DatabaseListener listener) {
        new DatabaseTask(listener).execute(new DatabaseArgs(DatabaseArgs.ArgsType.INSERT_ROWS, table, valuesList));
    }

    public long insert(String table, List<ContentValues> valuesList) {
        int count = 0;

        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            for (ContentValues values : valuesList) {
                if (insert(table, values) > 0) {
                    count++;
                }
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }

        return count;
    }

    public long insert(String table, Map<String, Object> map) {
        return insert(table, MapUtils.convertMapToContentValues(map));
    }

    public long insertMapList(String table, List<Map<String, Object>> mapList) {
        return insert(table, MapUtils.convertMapListToContentValuesList(mapList));
    }

    public long replace(String table, ContentValues values) {
        SQLiteDatabase db = getWritableDatabase();

        return db.replace(table, null, values);
    }

    public void replaceAsync(String table, ContentValues values, DatabaseListener listener) {
        List<ContentValues> valuesList = new ArrayList<ContentValues>();
        valuesList.add(values);

        new DatabaseTask(listener).execute(new DatabaseArgs(DatabaseArgs.ArgsType.REPLACE, table, valuesList));
    }

    public void replaceAsync(String table, List<ContentValues> valuesList, DatabaseListener listener) {
        new DatabaseTask(listener).execute(new DatabaseArgs(DatabaseArgs.ArgsType.REPLACE_ROWS, table, valuesList));
    }

    public long replace(String table, List<ContentValues> valuesList) {
        int count = 0;

        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            for (ContentValues values : valuesList) {
                if (replace(table, values) > 0) {
                    count++;
                }
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }

        return count;
    }

    public long replace(String table, Map<String, Object> map) {
        return replace(table, MapUtils.convertMapToContentValues(map));
    }

    public long replaceMapList(String table, List<Map<String, Object>> mapList) {
        return replace(table, MapUtils.convertMapListToContentValuesList(mapList));
    }

    public void deleteAsync(String table, String whereClause, String[] whereArgs, DatabaseListener listener) {
        new DatabaseTask(listener).execute(new DatabaseArgs(DatabaseArgs.ArgsType.DELETE, table, whereClause, whereArgs));
    }

    public long delete(String table, String whereClause, String[] whereArgs) {
        SQLiteDatabase db = getWritableDatabase();

        return db.delete(table, whereClause, whereArgs);
    }

    public void updateAsync(String table, ContentValues values, String whereClause, String[] whereArgs,
                            DatabaseListener listener) {
        new DatabaseTask(listener).execute(new DatabaseArgs(DatabaseArgs.ArgsType.UPDATE, table, whereClause, whereArgs, values));
    }

    public long update(String table, ContentValues values, String whereClause, String[] whereArgs) {
        SQLiteDatabase db = getWritableDatabase();

        return db.update(table, values, whereClause, whereArgs);
    }

    public long update(String table, Map<String, Object> map, String whereClause, String[] whereArgs) {
        return update(table, MapUtils.convertMapToContentValues(map), whereClause, whereArgs);
    }

    public void execQuery(String sql) {
        SQLiteDatabase db = getWritableDatabase();

        db.execSQL(sql);
    }

    public void execSqlAsync(String sql, DatabaseListener listener) {
        new DatabaseTask(listener).execute(new DatabaseArgs(DatabaseArgs.ArgsType.EXEC, sql));
    }

    public void execSql(String sql) {
        SQLiteDatabase db = getWritableDatabase();

        db.execSQL(sql);
    }

    public void execQuery(String sql, String[] bindArgs) {
        SQLiteDatabase db = getWritableDatabase();

        db.execSQL(sql, bindArgs);
    }

    protected static final class DatabaseArgs {
        public enum ArgsType {
            QUERY,
            RAW_QUERY,
            INSERT,
            INSERT_ROWS,
            REPLACE,
            REPLACE_ROWS,
            DELETE,
            UPDATE,
            EXEC,
        }

        public ArgsType type;

        public String table;
        public String[] projection;
        public String selection;
        public String[] selectionArgs;
        public String orderBy;
        public List<ContentValues> valuesList;
        public String sql;

        public DatabaseArgs(ArgsType type, String table, String[] projection, String selection, String[] selectionArgs,
                            String orderBy) {
            this.type = type;
            this.table = table;
            this.projection = projection;
            this.selection = selection;
            this.selectionArgs = selectionArgs;
            this.orderBy = orderBy;
        }

        public DatabaseArgs(ArgsType type, String table, String selection, String[] selectionArgs,
                            ContentValues values) {
            this.type = type;
            this.table = table;
            this.selection = selection;
            this.selectionArgs = selectionArgs;
            this.valuesList = new ArrayList<ContentValues>();
            valuesList.add(values);
        }

        public DatabaseArgs(ArgsType type, String table, String selection, String[] selectionArgs) {
            this.type = type;
            this.table = table;
            this.selection = selection;
            this.selectionArgs = selectionArgs;
        }

        public DatabaseArgs(ArgsType type, String table, List<ContentValues> valueList) {
            this.type = type;
            this.table = table;
            this.valuesList = valueList;
        }

        public DatabaseArgs(ArgsType type, String sql) {
            this.type = type;
            this.sql = sql;
        }
    }

    private class DatabaseTask extends AsyncTask<DatabaseArgs, Void, Object> {
        private DatabaseListener listener;
        private DatabaseArgs args;

        DatabaseTask(DatabaseListener listener) {
            this.listener = listener;
        }

        @Override
        protected Object doInBackground(DatabaseArgs... params) {
            args = params[0];

            switch (args.type) {
                case QUERY:
                    return BaseDatabaseHelper.this.query(args.table, args.projection, args.selection, args.selectionArgs, args.orderBy);

                case INSERT:
                    if (args.valuesList == null || args.valuesList.size() < 1) {
                        return -1;
                    } else {
                        return BaseDatabaseHelper.this.insert(args.table, args.valuesList.get(0));
                    }

                case INSERT_ROWS:
                    return BaseDatabaseHelper.this.insert(args.table, args.valuesList);

                case REPLACE:
                    if (args.valuesList == null || args.valuesList.size() < 1) {
                        return -1;
                    } else {
                        return BaseDatabaseHelper.this.replace(args.table, args.valuesList.get(0));
                    }

                case REPLACE_ROWS:
                    return BaseDatabaseHelper.this.replace(args.table, args.valuesList);

                case DELETE:
                    return BaseDatabaseHelper.this.delete(args.table, args.selection, args.selectionArgs);

                case UPDATE:
                    if (args.valuesList == null || args.valuesList.size() < 1) {
                        return 0;
                    } else {
                        return BaseDatabaseHelper.this.update(args.table, args.valuesList.get(0), args.selection, args.selectionArgs);
                    }
                case EXEC:
                    if (Validator.isNotEmpty(args.sql)) {
                        BaseDatabaseHelper.this.execSql(args.sql);
                    }
                    return 0;
                case RAW_QUERY:
                    return BaseDatabaseHelper.this.rawQuery(args.sql);
                default:
                    return null;
            }
        }

        @Override
        protected void onPostExecute(Object result) {
            switch (args.type) {
                case QUERY:
                    listener.onQueryComplete((Cursor) result);
                    break;

                case INSERT:
                case INSERT_ROWS:
                    listener.onInsertComplete((Long) result);
                    break;

                case REPLACE:
                case REPLACE_ROWS:
                    listener.onReplaceComplete((Long) result);
                    break;

                case DELETE:
                    listener.onDeleteComplete((Long) result);
                    break;

                case UPDATE:
                    listener.onUpdateComplete((Long) result);
                    break;

                case EXEC:
                    listener.onUseSqlComplete();
                    break;

                case RAW_QUERY:
                    listener.onQueryComplete((Cursor) result);
                    break;
                default:
                    break;
            }
        }
    }
}
