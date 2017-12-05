package com.unicom.db.operation;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.unicom.db.update.DbUtils;

import java.util.List;

/**
 * Created by liut46 on 2017/11/1.
 */

public class DbOperation<T> {
    public static final String AUTHORITY = "com.unicom.number.provider.UnicomProvider";
    protected ContentResolver mResolver;
    protected Context mContext;
    protected Uri uri;

    public DbOperation(Context context, Class tClass){
        mContext = context;
        mResolver = mContext.getContentResolver();
        uri =  Uri.parse("content://" + AUTHORITY + "/" + DbUtils.getTableName(tClass));
    }

    public Uri insert(T t){
        ContentValues values = DbUtils.Object2ContentValue(t);
        return mResolver.insert(uri, values);
    }

    public void insert(List<T> list){
       for(T t : list){
           insert(t);
       }
    }

    public  int delete(String where, String[] selectionArgs){
        return mResolver.delete(uri, where, selectionArgs);
    }
    public int update(T t, String where, String[] whereArgs){
        ContentValues values = DbUtils.Object2ContentValue(t);
        return mResolver.update(uri,values, where, whereArgs);
    }

    public List<T> queryList(Class<T> tClass, String[] projection, String selection, String[] selectionArgs, String sortOrder){
        Cursor cursor = mResolver.query(uri,projection, selection, selectionArgs, sortOrder);
        return DbUtils.cursor2List(cursor, tClass);

    }

    public T queryObject(Class<T> tClass, String[] projection, String selection, String[] selectionArgs, String sortOrder){
        Cursor cursor = mResolver.query(uri,projection, selection, selectionArgs, sortOrder);
        return DbUtils.cursor2Object(cursor, tClass);

    }
}
