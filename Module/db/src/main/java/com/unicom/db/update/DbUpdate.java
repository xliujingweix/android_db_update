package com.unicom.db.update;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.unicom.db.anotation.DbField;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by liut46 on 2017/11/29.
 */

public class DbUpdate {

    /**
     * 更新数据库
     * @param db
     * @param list
     */
    public static void update(SQLiteDatabase db, List<Class> list){
        //原始数据库的信息
        Map<String, List<String>> mapOrigin = dbInfos(db);
        for(String m : mapOrigin.keySet()){
            Log.e("orgin db:", "table:" + m + ",colums:" + mapOrigin.get(m).toString());
        }
        //新数据库的信息
        Map<String, List<String>> mapNew = tableInfos(list);

        for(String m : mapNew.keySet()){
            Log.e("new db:", "table:" + m + ",colums:" + mapNew.get(m).toString());
        }

        for(Class clazz : list){
            String table = DbUtils.getTableName(clazz);
            if(mapOrigin.containsKey(table)){
                //线比较字段是否有所不同
                List<String> adds = new ArrayList<>();
                for(String column : columnsFromTable(clazz)){
                    if(!mapOrigin.get(table).contains(column)){
                        adds.add(column);
                    }
                }
                if(adds.size() > 0){
                    String addColumns = "";
                    for (int i = 0; i < adds.size() - 2; i++){
                        addColumns += columnsFromTable(clazz, adds.get(i)) + ",";
                    }
                    addColumns += columnsFromTable(clazz, adds.get(adds.size() - 1)) ;
                    db.execSQL("ALTER TABLE " + DbUtils.getTableName(clazz) + " ADD COLUMN " + addColumns + ";");
                }
            }
            else
            {
                db.execSQL(DbUtils.createTableSql(clazz));
            }
        }
    }

    /**
     * 获取数据库中用户表名字及其中的字段名
     * @param db
     * @return
     */
    private static Map<String, List<String>> dbInfos(SQLiteDatabase db){
        Map<String, List<String>> map = new HashMap<>();
        Cursor cursor = db.rawQuery("select * from sqlite_master where type = ?", new String[]{"table"});
        if(cursor!= null && !cursor.isClosed()){
            while (cursor.moveToNext()){
                String tableName = cursor.getString(cursor.getColumnIndex("name"));
                Cursor table = db.rawQuery("select * from " + tableName, null);
                String[] columnNames = table.getColumnNames();
                map.put(tableName, Arrays.asList(columnNames));
                StringBuilder sb = new StringBuilder();
                table.close();
                //调试代码
                for (String name:columnNames) {
                    sb.append(name+ ",");
                }
                Log.e("sql", tableName + ":" + sb.toString());
            }
        }
        cursor.close();
        return map;
    }

    /**
     * 获取所有新表的信息
     * @param list
     * @return
     */
    private static Map<String,List<String>> tableInfos(List<Class> list) {
        Map<String,List<String>> map = new HashMap<>();
        for(Class clazz : list){
            String tableName = DbUtils.getTableName(clazz);
            List<String> columns = columnsFromTable(clazz);
            map.put(tableName, columns);
        }
        return map;
    }

    /**
     * 根据字段名获取该字段的的建表信息
     * @param clazz
     * @param fieldStr
     * @return
     */
    private static String columnsFromTable(Class clazz, String fieldStr) {
        Field field = null;
        try {
            field = clazz.getField(fieldStr);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        if(field !=  null){
            field.setAccessible(true);
        }
        String columnValue = null;
        if(field.getAnnotation(DbField.class) != null){
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
        }
        return columnValue;
    }

    /**
     * 获取能作为表的字段名的属性列表
     * @param clazz
     * @return
     */
    private static List<String> columnsFromTable(Class clazz) {
        List<String> columns = new ArrayList<>();
        Field[] columnFields = clazz.getFields();
        for(Field field:columnFields){
            field.setAccessible(true);
        }
        for(Field field:columnFields){
            if(field.getAnnotation(DbField.class) != null) {
                columns.add(field.getName());
            }
        }
        Log.e("table", columns.toString());
        return columns;
    }


}
