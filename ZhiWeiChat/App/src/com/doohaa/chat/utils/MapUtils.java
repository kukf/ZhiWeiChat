package com.doohaa.chat.utils;

import android.content.ContentValues;
import android.database.Cursor;

import org.apache.http.NameValuePair;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Created by Linecorp on 2015-07-28.
 */
public class MapUtils {
    /**
     * Convert primitive and String type fields to map
     */
    public static Map<String, Object> convertObjectToMap(Object obj) throws IllegalArgumentException, IllegalAccessException {
        Map<String, Object> map = new HashMap<String, Object>();

        if (obj != null) {
            Class<?> clazz = obj.getClass();
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);

                Class<?> type = field.getType();
                int modifier = type.getModifiers();
                if (Modifier.isStatic(modifier)) {
                    continue;
                } else if (type.isPrimitive()) {
                    map.put(field.getName(), String.valueOf(field.get(obj)));
                } else if (type.equals(String.class)) {
                    map.put(field.getName(), field.get(obj));
                }
            }
        }

        return map;
    }

    public static <T> T convertMapToObject(Map<String, Object> map, Class<? extends T> clazz) throws Exception {
        if (clazz == null) {
            throw new IllegalArgumentException("class is null");
        }

        T obj = clazz.newInstance();

        Set<String> keys = map.keySet();
        for (String key : keys) {
            String value = (String) map.get(key);

            try {
                Field field = clazz.getDeclaredField(key);
                field.setAccessible(true);

                Class<?> type = field.getType();
                int modifier = type.getModifiers();
                if (Modifier.isStatic(modifier)) {
                    continue;
                } else if (type.isPrimitive()) {
                    if (type.equals(Character.TYPE) && value.length() > 0) {
                        field.set(obj, value.charAt(0));
                    } else if (type.equals(Byte.TYPE) && value.length() > 0) {
                        field.set(obj, Byte.parseByte(value));
                    } else if (type.equals(Short.TYPE)) {
                        field.set(obj, Short.parseShort(value));
                    } else if (type.equals(Integer.TYPE)) {
                        field.set(obj, Integer.parseInt(value));
                    } else if (type.equals(Long.TYPE)) {
                        field.set(obj, Long.parseLong(value));
                    } else if (type.equals(Float.TYPE)) {
                        field.set(obj, Float.parseFloat(value));
                    } else if (type.equals(Double.TYPE)) {
                        field.set(obj, Double.parseDouble(value));
                    }
                } else if (type.equals(String.class)) {
                    field.set(obj, value);
                }
            } catch (NoSuchFieldException e) {
                // nothing to do
            } catch (NumberFormatException e) {
                // nothing to do
            }
        }

        return obj;
    }

    public static <T> ArrayList<T> convertMapListToObjectList(List<Map<String, Object>> mapList, Class<? extends T> clazz) throws Exception {
        if (clazz == null) {
            throw new IllegalArgumentException("class is null");
        }

        ArrayList<T> objList = new ArrayList<T>();

        for (Map<String, Object> map : mapList) {
            T obj = convertMapToObject(map, clazz);
            objList.add(obj);
        }

        return objList;
    }

    public static List<Map<String, Object>> convertCursorToMapList(Cursor cursor) {
        List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();

        if (cursor != null && cursor.moveToFirst()) {
            do {
                Map<String, Object> map = new HashMap<String, Object>();

                for (int i = 0; i < cursor.getColumnCount(); i++) {
                    map.put(cursor.getColumnName(i), cursor.getString(i));
                }

                mapList.add(map);
            } while (cursor.moveToNext());
        }

        return mapList;
    }

    public static <T> ArrayList<T> convertCursorToObjectList(Cursor cursor, Class<? extends T> clazz) throws Exception {
        if (clazz == null) {
            throw new IllegalArgumentException("class is null");
        }

        return convertMapListToObjectList(convertCursorToMapList(cursor), clazz);
    }

    public static Map<String, Object> convertContentValuesToMap(ContentValues values) {
        Map<String, Object> map = new HashMap<String, Object>();

        Set<String> keys = values.keySet();
        for (String key : keys) {
            Object value = values.get(key);

            map.put(key, value);
        }

        return map;
    }

    public static ContentValues convertMapToContentValues(Map<String, Object> map) {
        ContentValues values = new ContentValues();

        Set<String> keys = map.keySet();
        for (String key : keys) {
            String value = (String) map.get(key);

            values.put(key, value);
        }

        return values;
    }

    public static List<ContentValues> convertMapListToContentValuesList(List<Map<String, Object>> mapList) {
        ArrayList<ContentValues> valuesList = new ArrayList<ContentValues>();

        for (Map<String, Object> map : mapList) {
            ContentValues values = convertMapToContentValues(map);
            valuesList.add(values);
        }

        return valuesList;
    }

    public static Map<String, Object> convertNameValuePairToMap(NameValuePair value) {
        Map<String, Object> map = new HashMap<String, Object>();

        map.put(value.getName(), value.getValue());

        return map;
    }

    public static Map<String, Object> convertNameValuePairListToMap(List<NameValuePair> values) {
        Map<String, Object> map = new HashMap<String, Object>();

        for (NameValuePair value : values) {
            map.put(value.getName(), value.getValue());
        }

        return map;
    }
}
