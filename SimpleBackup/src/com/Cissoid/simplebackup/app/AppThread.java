package com.Cissoid.simplebackup.app;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import android.app.Notification;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;

import com.Cissoid.simplebackup.MainActivity;
import com.Cissoid.simplebackup.util.CopyUtil;
import com.Cissoid.simplebackup.util.ShellUtil;
import com.Cissoid.simplebackup.util.ZipUtil;

public class AppThread implements Runnable
{
    public static final int MODE_BACKUP = 0x0;
    private Handler handler;
    private AppInfo[] appInfos;
    public int mode;

    public AppThread( Handler handler , AppInfo... appInfos )
    {
        this.handler = handler;
        this.appInfos = appInfos;
    }

    @Override
    public void run()
    {
        switch ( mode )
        {
        case MODE_BACKUP :
            backup();
            break;

        default :
            break;
        }
    }

    private void backup()
    {
        for ( AppInfo appInfo : appInfos )
        {
            // ��ʾ�Ի���
            Message msg = new Message();
            msg.what = MainActivity.HANDLER_SHOWPROGRESSDIALOG;
            Bundle bundle = new Bundle();
            bundle.putCharSequence("title", "���ڱ���...");
            bundle.putCharSequence("text", appInfo.getName());
            msg.setData(bundle);
            handler.sendMessage(msg);
            // ��ʾ֪ͨ
            msg = new Message();
            msg.what = MainActivity.HANDLER_SHOWNOTIFICATION;
            bundle.putInt("id", appInfo.getId());
            bundle.putCharSequence("ticker", "���ڱ��ݣ�" + appInfo.getName());
            bundle.putCharSequence("title", "���ڱ���");
            bundle.putCharSequence("text", appInfo.getName());
            bundle.putInt("flags", Notification.FLAG_NO_CLEAR);
            msg.setData(bundle);
            handler.sendMessage(msg);

            // Ӧ�ð�װĿ¼
            String originalAppPath = appInfo.getAppPath();
            // Ӧ������Ŀ¼
            String originalDataPath = appInfo.getDataPath();
            // ����Ŀ¼
            String backupPath = Environment.getExternalStorageDirectory()
                    + "/SimpleBackup/" + appInfo.getPackageName();
            CopyUtil.copyFile(originalAppPath, backupPath + ".apk");
            CopyUtil.copyWithRoot(originalDataPath, backupPath);

            // ѹ��
            ArrayList<File> resFileList = new ArrayList<File>();
            File apk = new File(backupPath + ".apk");
            File data = new File(backupPath);
            if ( apk.exists() )
                resFileList.add(apk);
            if ( data.exists() )
                resFileList.add(data);
            File zipFile = new File(backupPath + ".sb");
            try
            {
                ZipUtil.zipFiles(resFileList, zipFile);
                ShellUtil.Cmd("rm -r " + backupPath + ".apk  " + backupPath);
                // �رնԻ���
                msg = new Message();
                msg.what = MainActivity.HANDLER_CLOSEPROGRESSDIALOG;
                handler.sendMessage(msg);
                // ��ʾ�ɹ�֪ͨ
                msg = new Message();
                msg.what = MainActivity.HANDLER_SHOWNOTIFICATION;
                bundle.putCharSequence("ticker", "������ɣ�" + appInfo.getName());
                bundle.putCharSequence("title", "�������");
                bundle.putInt("flags", Notification.FLAG_ONLY_ALERT_ONCE);
                msg.setData(bundle);
                handler.sendMessage(msg);
            }
            catch (IOException e)
            {

            }
        }
    }
}
