package com.Cissoid.simplebackup.util;

import java.io.File;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DBUtil
{
    private static final String DB_NAME = "bae.db";
    private static final String TABLE_NAME = "tokens";

    public static void create()
    {
        if ( !exists() )
        {
            SQLiteDatabase db = SQLiteDatabase.openDatabase(DB_NAME, null,
                    SQLiteDatabase.CREATE_IF_NECESSARY);
            db.execSQL("create table " + TABLE_NAME + "(token,text);");
        }
    }

    public static SQLiteDatabase open()
    {
        if ( exists() )
            return SQLiteDatabase.openDatabase(DB_NAME, null,
                    SQLiteDatabase.CREATE_IF_NECESSARY);
        return null;
    }

    public static void insert( ContentValues values )
    {
        SQLiteDatabase db = open();
        if ( db != null )
        {
            db.insert(TABLE_NAME, "", values);
        }
    }

    public static String query()
    {
        SQLiteDatabase db = open();
        if ( db != null )
        {
            Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null,
                    null);
            if ( cursor != null )
            {
                cursor.moveToFirst();
                return cursor.getString(0);
            }
        }
        return "";
    }

    private static boolean exists()
    {
        File file = new File(DB_NAME);
        return file.exists();
    }
}
