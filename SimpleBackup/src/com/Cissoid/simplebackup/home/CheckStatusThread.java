package com.Cissoid.simplebackup.home;

import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;

import com.Cissoid.simplebackup.MainActivity;
import com.Cissoid.simplebackup.R;
import com.Cissoid.simplebackup.Status;
import com.Cissoid.simplebackup.util.ShellUtil;

public class CheckStatusThread implements Runnable
{
    private HomePageFragment fragment;
    private MainActivity activity;

    public CheckStatusThread( HomePageFragment fragment )
    {
        this.fragment = fragment;
        this.activity = (MainActivity) fragment.getActivity();
    }

    @Override
    public void run()
    {
        Status status = new Status();
        Handler handler = activity.getHandler();
        Bundle bundle = new Bundle();
        Message message = new Message();
        message.what = MainActivity.HANDLER_SHOW_PROGRESSDIALOG;

        bundle.putString("title",
                activity.getString(R.string.main_dialog_checking_status));
        bundle.putString("message",
                activity.getString(R.string.main_dialog_checking_root));
        message.setData(bundle);
        handler.sendMessage(message);
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
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        bundle.putString("message",
                activity.getString(R.string.main_dialog_checking_busybox));
        message = new Message();
        message.what = MainActivity.HANDLER_SHOW_PROGRESSDIALOG;
        message.setData(bundle);
        handler.sendMessage(message);
        // busybox是否安装
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
        bundle.putString("message",
                activity.getString(R.string.main_dialog_checking_sdcard));
        message = new Message();
        message.what = MainActivity.HANDLER_SHOW_PROGRESSDIALOG;
        message.setData(bundle);
        handler.sendMessage(message);
        // SD卡是否存在
        if ( Environment.getExternalStorageState().equalsIgnoreCase(
                Environment.MEDIA_MOUNTED) )
        {
            status.setSdcard(true);
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
        message = new Message();
        message.what = MainActivity.HANDLER_CLOSE_PROGRESSDIALOG;
        handler.sendMessage(message);
        activity.setStatus(status);
    }
}
