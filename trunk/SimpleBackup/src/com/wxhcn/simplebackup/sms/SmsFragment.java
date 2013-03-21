package com.wxhcn.simplebackup.sms;

import java.util.ArrayList;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wxhcn.simplebackup.R;
import com.wxhcn.simplebackup.util.DBAccess;

public class SmsFragment extends ListFragment {
    public static final String ARG_SECTION_NUMBER = "section_number";
    private static final Uri   URI                = Uri.parse("content://sms");

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        SmsAdapter adapter = new SmsAdapter(getActivity(), getData());
        setListAdapter(adapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        return inflater.inflate(R.layout.fragment, container, false);
    }

    public ArrayList<SmsInfo> getData() {
        ArrayList<SmsInfo> arrayList = new ArrayList<SmsInfo>();
        Log.v("test", "¿ªÊ¼²éÑ¯");
        String[] projection = new String[] { "distinct " + SmsField.THREAD_ID,
                SmsField.ADDRESS, SmsField.PERSON,
                "COUNT(" + SmsField.THREAD_ID + ") AS 'count'" };
        Cursor cursor = DBAccess.query(getActivity(), URI, projection,
                SmsField.THREAD_ID);
        int thread_id = cursor.getColumnIndex(SmsField.THREAD_ID);
        int address = cursor.getColumnIndex(SmsField.ADDRESS);
        int person = cursor.getColumnIndex(SmsField.PERSON);
        int count = cursor.getColumnIndex("count");
        if (cursor != null) {
            while (cursor.moveToNext()) {
                SmsInfo smsinfo = new SmsInfo();
                smsinfo.setPerson(cursor.getString(thread_id));
                smsinfo.setAddress(cursor.getString(address));
                smsinfo.setPerson(cursor.getString(person));
                smsinfo.setNumber(cursor.getInt(count));
                arrayList.add(smsinfo);
            }
            cursor.close();
        }

        // String[] projection = new String[] { SmsField.ADDRESS,
        // SmsField.PERSON,
        // "COUNT(address) AS count" };
        // Process process = null;
        // try {
        // process = Runtime.getRuntime().exec("/system/xbin/su");
        // Runtime.getRuntime()
        // .exec("chmod 777 /data/data/com.android.provider.telephony/databases/mmssms.db");
        // SQLiteDatabase sqLiteDatabase = SQLiteDatabase
        // .openDatabase(
        // "/data/data/com.android.provider.telephony/databases/mmssms.db",
        // null, SQLiteDatabase.OPEN_READWRITE);
        // Cursor cursor = sqLiteDatabase.query("sms", projection, null, null,
        // SmsField.ADDRESS, null, null);
        //
        // int countColumn = cursor.getColumnIndex("count");
        // if (cursor != null) {
        // while (cursor.moveToNext()) {
        // SmsInfo smsInfo = new SmsInfo();
        // smsInfo.setAddress(cursor.getString(countColumn));
        // arrayList.add(smsInfo);
        // }
        // process.waitFor();
        //
        // }
        // } catch (IOException e) {
        // // TODO Auto-generated catch block
        // e.printStackTrace();
        // } catch (InterruptedException e) {
        // // TODO Auto-generated catch block
        // e.printStackTrace();
        // } finally {
        // process.destroy();
        // }

        return arrayList;
    }
}
