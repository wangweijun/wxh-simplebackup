package com.cissoid.simplebackup.util;

import android.util.Base64;

public class EncryptionUtil
{
    public static final int BASE64 = 0x0;

    public static String encrypt( String text , int mode )
    {
        return Base64.encodeToString(text.getBytes(), Base64.DEFAULT);
    }

    public static String decrypt( String text , int mode )
    {
        return new String(Base64.decode(text, Base64.DEFAULT));
    }
}
