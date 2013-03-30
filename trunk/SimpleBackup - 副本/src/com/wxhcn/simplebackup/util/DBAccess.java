package com.wxhcn.simplebackup.util;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

public class DBAccess {

    public static Cursor query(Context context, Uri uri, String[] projection,
            String GroupBy) {
        ContentResolver contentResolver = context.getContentResolver();
        if (uri == null || projection == null)
            return null;
        if (GroupBy != null || GroupBy.length() != 0)
            GroupBy = "0=0) group by (" + GroupBy;
        return contentResolver.query(uri, projection, GroupBy, null, null);
    }
}
