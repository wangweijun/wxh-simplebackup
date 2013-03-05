package com.wxhcn.simplebackup.sms;

import java.util.ArrayList;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wxhcn.simplebackup.R;

public class SmsFragment extends ListFragment {
	public static final String ARG_SECTION_NUMBER = "section_number";

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
		String[] projection = new String[] { "_id", "address", "person",
				"body", "date", "type" };
		Log.v("test", "¿ªÊ¼²éÑ¯");
		Cursor cusor = getActivity().managedQuery(
				Uri.parse("content://sms/inbox"), projection, null, null,
				"date desc");
		int nameColumn = cusor.getColumnIndex("person");
		int phoneNumberColumn = cusor.getColumnIndex("address");
		int smsbodyColumn = cusor.getColumnIndex("body");
		int dateColumn = cusor.getColumnIndex("date");
		int typeColumn = cusor.getColumnIndex("type");
		if (cusor != null) {
			 while (cusor.moveToNext()) {
			SmsInfo smsinfo = new SmsInfo();
			smsinfo.setName(cusor.getString(nameColumn));
			smsinfo.setDate(cusor.getString(dateColumn));
			smsinfo.setPhoneNumber(cusor.getString(phoneNumberColumn));
			smsinfo.setSms(cusor.getString(smsbodyColumn));
			smsinfo.setType(cusor.getString(typeColumn));
			arrayList.add(smsinfo);
			Log.v("test", smsinfo.getSms());
			 }
			cusor.close();
		}
		return arrayList;
	}

}
