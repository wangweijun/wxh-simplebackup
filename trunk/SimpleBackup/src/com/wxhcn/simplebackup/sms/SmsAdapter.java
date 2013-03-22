package com.wxhcn.simplebackup.sms;

import java.util.ArrayList;

import com.wxhcn.simplebackup.R;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class SmsAdapter extends BaseAdapter {
    private Context            context;
    private ArrayList<SmsInfo> arrayList = new ArrayList<SmsInfo>();

    public SmsAdapter(Context context, ArrayList<SmsInfo> arrayList) {
        this.context = context;
        this.arrayList.clear();
        for (int i = 0; i < arrayList.size(); i++)
            this.arrayList.add(arrayList.get(i));
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return arrayList.size();
    }

    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return arrayList.get(arg0);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View view = convertView;
        if (view == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.smsitem, null);
            view.setClickable(true);
        }
        final SmsInfo smsInfo = arrayList.get(position);
        if (smsInfo == null)
            return view;
        TextView address = (TextView) view.findViewById(R.id.sms_address);
        if (address != null)
            address.setText("(" + smsInfo.getAddress() + ")");
        TextView count = (TextView) view.findViewById(R.id.sms_count);
        if (count != null)
            count.setText(smsInfo.getNumber() + "Ìõ");
        return view;
    }

}
