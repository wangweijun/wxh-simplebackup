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
        return contentResolver.query(uri, projection, "0=0) group by "
                + GroupBy, null, null);
    }
}
