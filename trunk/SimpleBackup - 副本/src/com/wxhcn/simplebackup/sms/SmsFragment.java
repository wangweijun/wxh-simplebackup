package com.wxhcn.simplebackup.sms;

import android.app.ListFragment;
import android.os.Bundle;

public class SmsFragment extends ListFragment {
    public static final String ARG_SECTION_NUMBER = "section_number";


    public SmsFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        SmsAdapter adapter = new SmsAdapter(getActivity());
        this.setListAdapter(adapter);

    }

  

}
