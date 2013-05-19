package com.cissoid.simplebackup.sms;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cissoid.simplebackup.MainActivity;
import com.cissoid.simplebackup.Status;

public class SmsFragment extends ListFragment
{
    public static final String ARG_SECTION_NUMBER = "section_number";

    private Smslist smslist;
    private SmsAdapter smsAdapter = null;
    private Status status;

    @Override
    public void onCreate( Bundle savedInstanceState )
    {
        status = new Status();
        Bundle bundle = getArguments();
        status.setSdcard(bundle.getBoolean("sdcard"));
        status.setRoot(getArguments().getBoolean("root"));
        status.setBusybox(bundle.getBoolean("busybox"));
        status.setBae(getArguments().getBoolean("bae"));
        super.onCreate(savedInstanceState);
        smslist = new Smslist((MainActivity) getActivity());
        smsAdapter = new SmsAdapter(this, smslist);
        this.setListAdapter(smsAdapter);
    }

    @Override
    public View onCreateView( LayoutInflater inflater , ViewGroup container ,
            Bundle savedInstanceState )
    {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    /**
     * @return the status
     */
    public Status getStatus()
    {
        return status;
    }

    /**
     * @param status
     *            the status to set
     */
    public void setStatus( Status status )
    {
        this.status = status;
    }
}
