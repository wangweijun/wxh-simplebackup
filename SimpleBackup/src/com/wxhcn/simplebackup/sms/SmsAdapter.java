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
	private Context context;
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
			view = layoutInflater.inflate(R.layout.smslist, null);
			view.setClickable(true);
		}
		final SmsInfo smsInfo = arrayList.get(position);
		TextView name = (TextView) view.findViewById(R.id.sms_name);
		if (name != null)
			name.setText(smsInfo.getSms());
		Log.v("test", smsInfo.getName());
		return view;
	}

}
