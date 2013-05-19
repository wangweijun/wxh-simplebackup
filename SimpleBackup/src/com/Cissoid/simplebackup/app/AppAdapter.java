package com.cissoid.simplebackup.app;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.cissoid.simplebackup.R;

public class AppAdapter extends BaseAdapter
{
    private AppFragment fragment;
    private Applist applist;
    private boolean flag;

    /**
     * 
     * @param fragment
     *            绑定的Fragment
     * @param applist
     *            App列表
     */
    public AppAdapter( AppFragment fragment , Applist applist )
    {
        // this.listView = listView;
        this.fragment = fragment;
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
    public Object getItem( int position )
    {
        // TODO Auto-generated method stub
        return applist.getItem(position);
    }

    @Override
    public long getItemId( int position )
    {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView( int position , View convertView , ViewGroup parent )
    {
        // TODO Auto-generated method stub
        View view = null;
        // 利用convertView提高效率
        if ( convertView != null )
        {
            view = convertView;
        }
        else
        {
            view = new View(fragment.getActivity());
        }
        final AppInfo appItem = applist.getItem(position);
        LayoutInflater layoutInflater = (LayoutInflater) fragment.getActivity()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = layoutInflater.inflate(R.layout.app_info, null);
        view.setClickable(true);
        appItem.setView(view);
        // 单击应用条目，显示详情
        view.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick( View v )
            {
                AppInfoDialog appInfoDialog = AppInfoDialog.newInstance(
                        fragment.getStatus(), appItem);
                appInfoDialog.show(fragment.getFragmentManager(), "info");
            }
        });

        ImageView appIcon = (ImageView) view.findViewById(R.id.app_icon);
        TextView appName = (TextView) view.findViewById(R.id.app_name);
        TextView appVersion = (TextView) view.findViewById(R.id.app_version);
        TextView backupInfo = (TextView) view
                .findViewById(R.id.app_backup_info);
        CheckBox checkBox = (CheckBox) view.findViewById(R.id.app_check);
        if ( appName != null )
            appName.setText(appItem.getName());
        if ( appVersion != null )
            appVersion.setText(appItem.getVersion());
        if ( appIcon != null )
            appIcon.setImageDrawable(appItem.getIcon());
        backupInfo.setText(appItem.getBackupInfo());
        switch ( appItem.type )
        {
        case AppInfo.TYPE_LOCAL :
            backupInfo.setTextColor(Color.rgb(255, 68, 68));
            break;
        case AppInfo.TYPE_LOCAL_SDCARD :
            backupInfo.setTextColor(Color.rgb(51, 181, 229));
            break;
        case AppInfo.TYPE_SDCARD :
            backupInfo.setTextColor(Color.rgb(255, 187, 51));
            break;
        }
        if ( checkBox != null && applist.isMultiSelect() == true )
        {
            checkBox.setVisibility(View.VISIBLE);
            checkBox.setChecked(flag);
        }
        else
        {
            checkBox.setChecked(false);
            checkBox.setVisibility(View.INVISIBLE);
        }
        return view;
    }
}
