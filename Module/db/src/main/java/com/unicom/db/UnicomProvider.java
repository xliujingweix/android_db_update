package com.unicom.db;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.unicom.db.bean.CommonRelation;
import com.unicom.db.bean.DownloadVideoInfo;
import com.unicom.db.bean.OrganizationInfo;
import com.unicom.db.bean.SimpleOrgInfo;
import com.unicom.db.update.DbUpdate;
import com.unicom.db.update.DbUtils;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by liut46 on 2017/10/31.
 */

public class UnicomProvider extends ContentProvider {

    public static final String AUTHORITY = "com.unicom.number.provider.UnicomProvider";
    private Context mContext;
    private SQLiteDatabase db;

    private static List<Class> dbBeans = new ArrayList<>();
    private static Map<Integer, Class> tableMap = new HashMap<>();
    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        dbBeans.add(CommonRelation.class);
        dbBeans.add(SimpleOrgInfo.class);
        //dbBeans.add(OrganizationInfo.class);
        //dbBeans.add(DownloadVideoInfo.class);
    }

    @Override
    public boolean onCreate() {
        init();
        mContext = getContext();
        db = new UnicomSqliteHelper(mContext).getReadableDatabase();
        DbUpdate.update(db, dbBeans);
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        String tableName = getTableName(uri);
        if(tableName == null){
            throw new IllegalArgumentException("Unsurpport Uri:" + uri);
        }
        return db.query(tableName, projection, selection, selectionArgs,null, null, sortOrder, null);
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        String tableName = getTableName(uri);
        if(tableName == null){
            throw new IllegalArgumentException("Unsurpport Uri:" + uri);
        }
        db.insertWithOnConflict(tableName, null, contentValues, SQLiteDatabase.CONFLICT_REPLACE);
        mContext.getContentResolver().notifyChange(uri, null);
        return uri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        String tableName = getTableName(uri);
        if(tableName == null){
            throw new IllegalArgumentException("Unsurpport Uri:" + uri);
        }
        int count = db.delete(tableName, selection, selectionArgs);
        if(count > 0) {
            mContext.getContentResolver().notifyChange(uri, null);
        }
        return count;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String selection, @Nullable String[] selectionArgs) {
        String tableName = getTableName(uri);
        if(tableName == null){
            throw new IllegalArgumentException("Unsurpport Uri:" + uri);
        }
        int count = db.update(tableName, contentValues, selection, selectionArgs);
        if(count > 0) {
            mContext.getContentResolver().notifyChange(uri, null);
        }
        return count;
    }

    private String getTableName(Uri uri){
        int key = uriMatcher.match(uri);
        Class clazz = tableMap.get(key);
        String tableName = DbUtils.getTableName(clazz);
        return tableName;
    }

    private static void init() {
        for(int i = 0; i <dbBeans.size(); i++){
            tableMap.put(i + 1, dbBeans.get(i));
        }
        for (int index : tableMap.keySet()) {
            uriMatcher.addURI(AUTHORITY, DbUtils.getTableName(tableMap.get(index)), index);
        }
    }
}
