package com.Cissoid.simplebackup.sms;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.Cissoid.simplebackup.MainActivity;
import com.wxhcn.simplebackup.R;

public class SmsAdapter extends BaseAdapter
{

    private Fragment fragment;
    private Smslist smslist;
    private boolean flag;

    public SmsAdapter( Fragment fragment , Smslist smslist )
    {
        this.fragment = fragment;
        this.smslist = smslist;
    }

    @Override
    public int getCount()
    {
        return smslist.getCount();
    }

    @Override
    public Object getItem( int position )
    {
        return smslist.getItem(position);
    }

    @Override
    public long getItemId( int position )
    {
        return position;
    }

    @Override
    public View getView( final int position , View convertView ,
            ViewGroup parent )
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
        final ThreadInfo threadInfo = smslist.getItem(position);
        LayoutInflater layoutInflater = (LayoutInflater) fragment.getActivity()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = layoutInflater.inflate(R.layout.sms_info, null);
        view.setClickable(true);
        if ( threadInfo == null )
            return view;
        view.setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick( View v )
            {
                Builder builder = new Builder(fragment.getActivity());
                builder.setMessage(R.string.sms_alert_message)
                        .setPositiveButton(R.string.sms_alert_backup,
                                new DialogInterface.OnClickListener()
                                {

                                    @Override
                                    public void onClick(
                                            DialogInterface dialog , int which )
                                    {
                                        // TODO Auto-generated method stub
                                        new SmsBackupTask(
                                                (MainActivity) fragment
                                                        .getActivity())
                                                .execute(threadInfo);
                                    }
                                })
                        .setNegativeButton(R.string.sms_alert_restore,
                                new DialogInterface.OnClickListener()
                                {

                                    @Override
                                    public void onClick(
                                            DialogInterface dialog , int which )
                                    {
                                        // TODO Auto-generated method stub
                                        new SmsRestoreTask(
                                                (MainActivity) fragment
                                                        .getActivity())
                                                .execute(threadInfo);
                                    }
                                }).show();
            }
        });
        TextView person = (TextView) view.findViewById(R.id.sms_person);
        TextView address = (TextView) view.findViewById(R.id.sms_address);
        TextView count = (TextView) view.findViewById(R.id.sms_count);
        TextView snippet = (TextView) view.findViewById(R.id.sms_snippet);
        TextView backupInfo = (TextView) view
                .findViewById(R.id.sms_backup_info);
        CheckBox checkBox = (CheckBox) view.findViewById(R.id.sms_check);
        if ( threadInfo.getPerson().length() == 0 )
        {
            if ( address != null )
                address.setText(threadInfo.getAddress());
        }
        else
        {
            if ( person != null )
                person.setText(threadInfo.getPerson());
            if ( address != null )
                address.setText("(" + threadInfo.getAddress() + ")");
        }
        if ( count != null )
            count.setText(threadInfo.getNumber() + "条");
        if ( snippet != null )
            snippet.setText(threadInfo.getSnippet());
        if ( backupInfo != null )
            backupInfo.setText("test");
        if ( checkBox != null && smslist.isMultiSelect() == true )
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

    public void setSelectAll( boolean flag )
    {
        this.flag = flag;

    }
}
