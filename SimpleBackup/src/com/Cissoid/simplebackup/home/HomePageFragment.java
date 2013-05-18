/**
 * 
 */
package com.Cissoid.simplebackup.home;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.Cissoid.simplebackup.MainActivity;
import com.Cissoid.simplebackup.Status;
import com.Cissoid.simplebackup.util.ShellUtil;
import com.wxhcn.simplebackup.R;

/**
 * @author Wxh
 * 
 */
public class HomePageFragment extends Fragment
{
    public static final String ARG_SECTION_NUMBER = "section_number";
    private Status status;

    @Override
    public void onCreate( Bundle savedInstanceState )
    {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        ProgressDialog progressDialog = new ProgressDialog(getActivity());
        status = new Status();
        progressDialog.setTitle(R.string.main_dialog_checking_status);
        progressDialog.setMessage(getActivity().getString(
                R.string.main_dialog_checking_root));
        progressDialog.show();
        // root权限
        if ( ShellUtil.RootCmd("") )
        {
            status.setRoot(true);
        }
        try
        {
            Thread.sleep(1000);
        }
        catch (InterruptedException e1)
        {

        }

        // busybox是否安装
        progressDialog.setMessage(getActivity().getString(
                R.string.main_dialog_checking_busybox));
        if ( ShellUtil.Cmd("busybox") )
        {
            status.setBusybox(true);
        }
        try
        {
            Thread.sleep(1000);
        }
        catch (InterruptedException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // SD卡是否存在
        progressDialog.setMessage(getActivity().getString(
                R.string.main_dialog_checking_sdcard));
        if ( Environment.getExternalStorageState().equalsIgnoreCase(
                Environment.MEDIA_MOUNTED) )
        {
            status.setSdcard(true);
         
        }
        try
        {
            wait(1500);
            Thread.sleep(1000);
        }
        catch (InterruptedException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        progressDialog.dismiss();
    }

    public View onCreateView( LayoutInflater inflater , ViewGroup container ,
            Bundle savedInstanceState )
    {
        status = new Status();
        // setHasOptionsMenu(true);
        View rootView = inflater.inflate(R.layout.homepage, container, false);
        TextView statusSDCard = (TextView) rootView
                .findViewById(R.id.home_status_sdcard);
        TextView statusRoot = (TextView) rootView
                .findViewById(R.id.home_status_root);
        TextView statusBusybox = (TextView) rootView
                .findViewById(R.id.home_status_busybox);
        TextView messageSDCard = (TextView) rootView
                .findViewById(R.id.home_message_sdcard);
        TextView messageRoot = (TextView) rootView
                .findViewById(R.id.home_message_root);
        TextView messageBusybox = (TextView) rootView
                .findViewById(R.id.home_message_busybox);
        if ( status.isSdcard() )
        {
            statusSDCard.setText(R.string.home_status_success);
            statusSDCard.setTextColor(Color.rgb(51, 181, 229));
            messageSDCard.setText(R.string.home_sdcard_success);
        }
        else
        {
            statusSDCard.setText(R.string.home_status_failed);
            statusSDCard.setTextColor(Color.rgb(255, 68, 68));
            messageSDCard.setText(R.string.home_sdcard_false);
        }
        if ( status.isRoot() )
        {
            statusRoot.setText(R.string.home_status_success);
            statusRoot.setTextColor(Color.rgb(51, 181, 229));
            messageRoot.setText(R.string.home_root_success);
        }
        else
        {
            statusRoot.setText(R.string.home_status_failed);
            statusRoot.setTextColor(Color.rgb(255, 68, 68));
            messageRoot.setText(R.string.home_root_false);
        }
        if ( status.isBusybox() )
        {
            statusBusybox.setText(R.string.home_status_success);
            statusBusybox.setTextColor(Color.rgb(51, 181, 229));
            messageBusybox.setText(R.string.home_busybox_success);
        }
        else
        {
            statusBusybox.setText(R.string.home_status_failed);
            statusBusybox.setTextColor(Color.rgb(255, 68, 68));
            messageBusybox.setText(R.string.home_busybox_false);
        }
        return rootView;
    }

    // @Override
    // public void onCreateOptionsMenu( Menu menu , MenuInflater inflater )
    // {
    // menu.clear();
    // getActivity().getMenuInflater().inflate(R.menu.main, menu);
    // menu.findItem(R.id.menu_multi_select).setVisible(false);
    // super.onCreateOptionsMenu(menu, inflater);
    // }
}
