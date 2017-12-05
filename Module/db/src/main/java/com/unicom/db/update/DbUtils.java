package com.unicom.db.update;

import android.content.ContentValues;
import android.database.Cursor;

import com.unicom.db.anotation.DbField;
import com.unicom.db.anotation.DbTable;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by liut46 on 2017/10/31.
 */

public class DbUtils {

    private static Map<Class, String> tableNameCaches = new HashMap<>();


    public static <T> String getTableName(Class<T> tClass){
        String tableName = null;
        if(tableNameCaches.containsKey(tClass)){
            return tableNameCaches.get(tClass);
        }
        tableName = tClass.getAnnotation(DbTable.class).value();
        if(tableName == null){
            return null;
        }
        else
        {
            tableNameCaches.put(tClass, tableName);
        }
        return tableName;
    }

    public static <T> ContentValues Object2ContentValue(T t){
        ContentValues values = new ContentValues();
        Field[] columnFields = t.getClass().getFields();
        for(Field field:columnFields){
            field.setAccessible(true);
        }
        for(Field field:columnFields){
            if(field.getAnnotation(DbField.class) != null) {
                try {
                    values.put(field.getName(), field.get(t)==null?null:field.get(t).toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return values;
    }


    public static <T> List<T> cursor2List(Cursor cursor, Class<T> tClass){
        List<T> list = new ArrayList<>();
        try {
            if (cursor != null && !cursor.isClosed()) {
                while (cursor.moveToNext()) {

                    T t = tClass.newInstance();
                    Field[] columnFields = tClass.getFields();
                    for (Field field : columnFields) {
                        field.setAccessible(true);
                    }
                    for (Field field : columnFields) {
                        if (field.getAnnotation(DbField.class) != null) {
                            String type = field.getAnnotation(DbField.class).type();
                            if (type.equals("Integer")) {
                                field.set(t, cursor.getInt(cursor.getColumnIndex(field.getName())));
                            } else {
                                field.set(t, cursor.getString(cursor.getColumnIndex(field.getName())));
                            }
                        }
                    }
                    list.add(t);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return list;
    }

    public static <T> T cursor2Object(Cursor cursor, Class<T> tClass){
        T t = null;
        try {
            if (cursor != null && !cursor.isClosed()) {
                if (cursor.moveToNext()) {
                    t = tClass.newInstance();
                    Field[] columnFields = tClass.getFields();
                    for (Field field : columnFields) {
                        field.setAccessible(true);
                    }
                    for (Field field : columnFields) {
                        if (field.getAnnotation(DbField.class) != null) {
                            String type = field.getAnnotation(DbField.class).type();
                            if (type.equals("Integer")) {
                                field.set(t, cursor.getInt(cursor.getColumnIndex(field.getName())));
                            } else {
                                field.set(t, cursor.getString(cursor.getColumnIndex(field.getName())));
                            }
                        }
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return t;
    }

    public static <T> String createTableSql(Class<T> clazz){
        String sql = null;
        //获取表名
        String tableName = clazz.getAnnotation(DbTable.class).value();
        if(tableName == null){
            return null;
        }
        //获取对象属性名
        Field[] columnFields = clazz.getFields();
        for(Field field:columnFields){
            field.setAccessible(true);
        }
        //获取各表的字段名以及该字段的属性
        List<String> columns = new ArrayList<>();
        for(Field field:columnFields){
            if(field.getAnnotation(DbField.class) != null){
                String columnValue = null;
                String columnName = field.getAnnotation(DbField.class).column();
                String type = field.getAnnotation(DbField.class).type();
                boolean unique = field.getAnnotation(DbField.class).unique();
                boolean notNull = field.getAnnotation(DbField.class).notNull();
                if(columnName != null){
                    if(type.equals("Integer")){
                        columnValue = columnName + " INTEGER";
                    }
                    else
                    {
                        columnValue = columnName + " TEXT";
                    }
                    if (unique){
                        columnValue += " UNIQUE";
                    }
                    if(notNull) {
                        columnValue +=  " NOT NULL";
                    }
                }
                columns.add(columnValue);
            }
        }
        if(columns.size() == 0){
            return null;
        }
        sql = "CREATE TABLE " + tableName + "(_id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,";
        for (int i = 0; i < columns.size() - 1 ; i++){
            sql += " "+ columns.get(i) + ",";
        }
        sql += columns.get(columns.size() - 1) + ")";

        return sql;
    }

}
