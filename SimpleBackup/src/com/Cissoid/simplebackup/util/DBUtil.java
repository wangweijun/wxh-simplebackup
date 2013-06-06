package com.Cissoid.simplebackup.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

import com.Cissoid.simplebackup.MainActivity;

public class DBUtil
{
    private static final String DB_NAME = "bae.db";
    private static final String TABLE_NAME = "tokens";

    public static void create( MainActivity activity )
    {
        new DBHelper(activity, DB_NAME, null, 1);
    }

    public static void insert( MainActivity activity , ContentValues values )
    {
        DBHelper dbHelper = new DBHelper(activity, DB_NAME, null, 1);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.insert(TABLE_NAME, "", values);
    }

    public static String query( MainActivity activity )
    {
        DBHelper dbHelper = new DBHelper(activity, DB_NAME, null, 1);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        if ( db != null )
        {
            Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null,
                    null);
            if ( cursor.moveToFirst() )
            {
                return cursor.getString(0);
            }
        }
        return "";
    }

    public static void delete( MainActivity activity )
    {
        DBHelper dbHelper = new DBHelper(activity, DB_NAME, null, 1);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(TABLE_NAME, null, null);
    }

    private static class DBHelper extends SQLiteOpenHelper
    {

        public DBHelper( Context context , String name , CursorFactory factory ,
                int version )
        {
            super(context, name, factory, version);
            // TODO Auto-generated constructor stub
        }

        @Override
        public void onCreate( SQLiteDatabase db )
        {
            db.execSQL("create table tokens (token text)");
        }

        @Override
        public void onUpgrade( SQLiteDatabase db , int oldVersion ,
                int newVersion )
        {
            // TODO Auto-generated method stub

        }
    }
}
