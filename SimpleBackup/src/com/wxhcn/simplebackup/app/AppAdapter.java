package com.wxhcn.simplebackup.app;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.wxhcn.simplebackup.R;

public class AppAdapter extends BaseAdapter {
	private Context context;
	ArrayList<AppInfo> arrayList = new ArrayList<AppInfo>();

	public AppAdapter(Context context, ArrayList<AppInfo> arrayList) {
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
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View view=convertView;
		final AppInfo appUnit=arrayList.get(position);
		if(view==null)
		{
    		LayoutInflater layoutInflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    		view=layoutInflater.inflate(R.layout.appitem, null);
    		view.setClickable(true);
		}
    	ImageView appIcon=(ImageView)view.findViewById(R.id.app_icon);
    	TextView appName=(TextView)view.findViewById(R.id.app_title);

    	TextView appVersion=(TextView) view.findViewById(R.id.app_info);
    	if(appName!=null)
    		appName.setText(appUnit.appName);
    	if(appVersion!=null)
    		appVersion.setText(appUnit.versionName);
    	if(appIcon!=null)
    		appIcon.setImageDrawable(appUnit.appIcon);
		return view;
	}

}
