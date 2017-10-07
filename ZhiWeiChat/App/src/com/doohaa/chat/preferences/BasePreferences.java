package com.doohaa.chat.preferences;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;
import java.util.Map;

public abstract class BasePreferences {
    private static final String TAG = "BasePreferences";
    public static final int PREF_VERSION = 1;

    private Context context;
    private String prefsName;
    private int prefsMode;

    private boolean autoCommit;

    private Map<String, Object> data = new HashMap<String, Object>();

    public BasePreferences(Context context, String prefsName) {
        this(context, prefsName, Context.MODE_PRIVATE, true);
    }

    public BasePreferences(Context context, String prefsName, int prefsMode) {
        this(context, prefsName, prefsMode, true);
    }

    public BasePreferences(Context context, String prefsName, int prefsMode, boolean autoCommit) {
        if (context == null) {
            throw new IllegalArgumentException("Context is null.");
        }

        this.context = context.getApplicationContext();
        this.prefsName = prefsName;
        this.prefsMode = prefsMode;
        this.autoCommit = autoCommit;
    }

    public SharedPreferences getSharedPreferences() {
        return context.getSharedPreferences(prefsName, prefsMode);
    }

    public Object get(String key) {
        return get(key, null);
    }

    public Object get(String key, Object defaultValue) {
        if (!data.containsKey(key)) {
            SharedPreferences sp = getSharedPreferences();
            if (sp.contains(key)) {
                Map<String, ?> prefData = sp.getAll();
                data.putAll(prefData);
            }
        }

        if (data.containsKey(key)) {
            return data.get(key);
        }

        return defaultValue;
    }

    public void put(String key, Object value) {
        data.put(key, value);

        if (autoCommit) {
            SharedPreferences sp = getSharedPreferences();
            SharedPreferences.Editor editor = sp.edit();

            putEditor(editor, key, value);
            commitEditor(editor);
        }
    }

    public void put(String key, boolean value) {
        put(key, Boolean.valueOf(value));
    }

    public void put(String key, int value) {
        put(key, Integer.valueOf(value));
    }

    public void put(String key, long value) {
        put(key, Long.valueOf(value));
    }

    public void putEditor(SharedPreferences.Editor editor, String key, Object value) {
        if (value instanceof Integer) {
            editor.putInt(key, (Integer) value);
        } else if (value instanceof Boolean) {
            editor.putBoolean(key, (Boolean) value);
        } else if (value instanceof Long) {
            editor.putLong(key, (Long) value);
        } else if (value instanceof Float) {
            editor.putFloat(key, (Float) value);
        } else {
            if (value != null) {
                editor.putString(key, value.toString());
            } else {
                editor.putString(key, null);
            }
        }
    }

    public void commitEditor(SharedPreferences.Editor editor) {
        editor.apply();
    }

    public void commit() {
        SharedPreferences sp = getSharedPreferences();
        SharedPreferences.Editor editor = sp.edit();

        for (Map.Entry<String, Object> entry : data.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            putEditor(editor, key, value);
        }

        commitEditor(editor);
    }

    public void clear() {
        SharedPreferences sp = getSharedPreferences();
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.apply();
        data.clear();
    }
}