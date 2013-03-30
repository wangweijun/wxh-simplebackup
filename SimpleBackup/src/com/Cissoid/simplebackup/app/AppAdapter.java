package com.Cissoid.simplebackup.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.wxhcn.simplebackup.R;

public class AppAdapter extends BaseAdapter
{
    private Applist applist;

    public AppAdapter(Applist applist)
    {
        this.applist = applist;
    }

    @Override
    public int getCount()
    {
        // TODO Auto-generated method stub
        // return arrayList.size();
        return applist.getCount();
    }

    @Override
    public Object getItem(int position)
    {
        // TODO Auto-generated method stub
        return applist.getItem(position);
    }

    @Override
    public long getItemId(int position)
    {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        // TODO Auto-generated method stub
        View view = convertView;
        final AppInfo appItem = applist.getItem(position);
        if (view == null)
        {
            LayoutInflater layoutInflater = (LayoutInflater) applist
                    .getContext().getSystemService(
                            Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.appitem, null);
            view.setClickable(true);
            view.setOnClickListener(new OnClickListener()
            {
                
                @Override
                public void onClick(View v)
                {
                    // TODO Auto-generated method stub
                    Toast.makeText(applist.getContext(),
                            ((TextView) v.findViewById(R.id.app_title)).getText(),
                            Toast.LENGTH_SHORT).show();
                }
            });
        }
        ImageView appIcon = (ImageView) view.findViewById(R.id.app_icon);
        TextView appName = (TextView) view.findViewById(R.id.app_title);

        TextView appVersion = (TextView) view.findViewById(R.id.app_info);
        if (appName != null)
            appName.setText(appItem.getName());

        if (appVersion != null)
            appVersion.setText(appItem.getVersion());
        if (appIcon != null)
            appIcon.setImageDrawable(appItem.getIcon());
        return view;
    }
}
