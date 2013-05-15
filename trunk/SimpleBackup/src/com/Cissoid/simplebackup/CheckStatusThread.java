package com.Cissoid.simplebackup;

import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;

import com.Cissoid.simplebackup.util.ShellUtil;
import com.wxhcn.simplebackup.R;

public class CheckStatusThread implements Runnable
{
    private MainActivity activity;

    public CheckStatusThread( MainActivity activity )
    {
        this.activity = activity;
    }

    @Override
    public void run()
    {
        SimpleBackupApplication application = (SimpleBackupApplication) activity
                .getApplication();
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
            application.setRoot(true);
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
            application.setBusybox(true);
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
            application.setSDCard(true);
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
    }

}
