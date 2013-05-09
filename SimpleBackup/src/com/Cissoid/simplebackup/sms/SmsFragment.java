package com.Cissoid.simplebackup.sms;

import android.os.Bundle;
import android.support.v4.app.ListFragment;

public class SmsFragment extends ListFragment
{
    public static final String ARG_SECTION_NUMBER = "section_number";

    public SmsFragment()
    {

    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        SmsAdapter adapter = new SmsAdapter(getActivity());
        this.setListAdapter(adapter);
    }

    @Override
    public void onStart()
    {
        // TODO Auto-generated method stub
        super.onStart();
        setEmptyText("test");
    }

}
