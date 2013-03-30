package com.wxhcn.simplebackup.sms;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.wxhcn.simplebackup.R;
import com.wxhcn.simplebackup.util.DBAccess;

public class SmsAdapter extends BaseAdapter {
    private static final Uri   URI       = Uri.parse("content://sms");
    private Context            context;
    private ArrayList<SmsInfo> arrayList = new ArrayList<SmsInfo>();

    public SmsAdapter(Context context) {
        this.context = context;
        arrayList = getData();
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View view = convertView;
        if (view == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.smsitem, null);
            view.setClickable(true);
            view.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    // TODO Auto-generated method stub
                    Toast.makeText(context,
                            arrayList.get(position).getAddress(),
                            Toast.LENGTH_SHORT).show();
                }
            });
        }
        final SmsInfo smsInfo = arrayList.get(position);
        if (smsInfo == null)
            return view;
        TextView address = (TextView) view.findViewById(R.id.sms_address);
        if (address != null)
            address.setText("(" + smsInfo.getAddress() + ")");
        TextView count = (TextView) view.findViewById(R.id.sms_count);
        if (count != null)
            count.setText(smsInfo.getNumber() + "条");
        return view;
    }

    public ArrayList<SmsInfo> getData() {
        ArrayList<SmsInfo> arrayList = new ArrayList<SmsInfo>();
        Log.v("test", "开始查询");
        String[] projection = new String[] { "distinct " + SmsField.THREAD_ID,
                SmsField.ADDRESS,
                "COUNT(" + SmsField.THREAD_ID + ") AS 'count'" };
        Cursor cursor = DBAccess.query(context, URI, projection,
                SmsField.THREAD_ID);
        int thread_id = cursor.getColumnIndex(SmsField.THREAD_ID);
        int address = cursor.getColumnIndex(SmsField.ADDRESS);
        int count = cursor.getColumnIndex("count");
        if (cursor != null) {
            while (cursor.moveToNext()) {
                SmsInfo smsinfo = new SmsInfo();
                smsinfo.setPerson(cursor.getString(thread_id));
                smsinfo.setAddress(cursor.getString(address));
                smsinfo.setNumber(cursor.getInt(count));
                arrayList.add(smsinfo);
            }
            cursor.close();
        }
        return arrayList;
    }
}
