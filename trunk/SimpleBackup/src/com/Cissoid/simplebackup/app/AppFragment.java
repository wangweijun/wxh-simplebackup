/**
 * 
 */
package com.Cissoid.simplebackup.app;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.Cissoid.simplebackup.MainActivity;
import com.Cissoid.simplebackup.R;
import com.Cissoid.simplebackup.Status;

/**
 * 应用备份对应的Fragment
 * 
 * @author Wxh
 * 
 */
public class AppFragment extends ListFragment
{
    public static final String ARG_SECTION_NUMBER = "section_number";
    private Applist applist;
    private AppAdapter appAdapter = null;
    private Status status;

    @Override
    public void onCreate( Bundle savedInstanceState )
    {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        status = new Status();
        status.setSdcard(getArguments().getBoolean("sdcard"));
        status.setRoot(getArguments().getBoolean("root"));
        status.setBusybox(getArguments().getBoolean("busybox"));
        applist = new Applist((MainActivity) getActivity());
        appAdapter = new AppAdapter(this, applist);
        setListAdapter(appAdapter);
    }

    @Override
    public View onCreateView( LayoutInflater inflater , ViewGroup container ,
            Bundle savedInstanceState )
    {
        return inflater.inflate(R.layout.fragment, container, false);
    }

    public void refresh()
    {
        appAdapter.notifyDataSetChanged();
        appAdapter.notifyDataSetInvalidated();
    }

    public void refleshAll()
    {
        applist = new Applist((MainActivity) getActivity());
        appAdapter = new AppAdapter(this, applist);
        setListAdapter(appAdapter);
        appAdapter.notifyDataSetChanged();
        appAdapter.notifyDataSetInvalidated();
    }

    /**
     * @return the status
     */
    public Status getStatus()
    {
        return status;
    }
}
