package com.Cissoid.simplebackup.app;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.zip.ZipException;

import android.app.Notification;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;

import com.Cissoid.simplebackup.MainActivity;
import com.Cissoid.simplebackup.util.CopyUtil;
import com.Cissoid.simplebackup.util.ShellUtil;
import com.Cissoid.simplebackup.util.XmlUtil;
import com.Cissoid.simplebackup.util.ZipUtil;

public class AppThread implements Runnable
{
    public static final int MODE_BACKUP = 0x0;
    public static final int MODE_RESTORE = 0x1;
    public static final int MODE_DELETE = 0x10;
    private MainActivity activity;
    private Handler handler;
    private AppInfo[] appInfos;
    public int mode;

    public AppThread( MainActivity activity , AppInfo... appInfos )
    {
        this.activity = activity;
        this.handler = activity.handler;
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
        case MODE_RESTORE :
            restore();
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
            // ������װ��
            CopyUtil.copyFile(originalAppPath, backupPath + ".apk");
            // ���������ļ�
            String cmd = "busybox tar zcvf " + backupPath + ".tar.gz "
                    + originalDataPath;
            ShellUtil.RootCmd(cmd);

            // ѹ��
            ArrayList<File> resFileList = new ArrayList<File>();
            File apk = new File(backupPath + ".apk");
            File data = new File(backupPath + ".tar.gz");
            if ( apk.exists() )
                resFileList.add(apk);
            if ( data.exists() )
                resFileList.add(data);
            File zipFile = new File(backupPath + ".zip");
            try
            {
                // ѹ���ļ�
                ZipUtil.zipFiles(resFileList, zipFile);
                // ɾ��ԭʼ�ļ�
                ShellUtil.Cmd("rm -r " + backupPath + ".apk  " + backupPath
                        + ".tar.gz");
                // ��¼����ʱ��
                SimpleDateFormat formatter = new SimpleDateFormat(
                        "����ʱ��:yyyy.MM.dd HH:mm");
                Date curDate = new Date(System.currentTimeMillis());
                String time = formatter.format(curDate);
                appInfo.setBackupTime(time);
                // ����xml
                File xmlFile = new File(backupPath + ".xml");
                FileOutputStream outStream = new FileOutputStream(xmlFile);
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(
                        outStream, "UTF-8");
                BufferedWriter writer = new BufferedWriter(outputStreamWriter);
                XmlUtil.writeAppCfg(writer, appInfo);
                writer.flush();
                writer.close();
            }
            catch (IOException e)
            {

            }
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
            msg = new Message();
            msg.what = MainActivity.HANDLER_INVALIDATE;
            handler.sendMessage(msg);

            // ˢ��UI
            appInfo.setBackupAppInfo(appInfo);
            appInfo.getView().postInvalidate();
        }
    }

    private void restore()
    {
        for ( AppInfo appInfo : appInfos )
        {
            // ��ʾ�Ի���
            Message msg = new Message();
            msg.what = MainActivity.HANDLER_SHOWPROGRESSDIALOG;
            Bundle bundle = new Bundle();
            bundle.putCharSequence("title", "���ڻ�ԭ...");
            bundle.putCharSequence("text", appInfo.getName());
            msg.setData(bundle);
            handler.sendMessage(msg);
            // ��ʾ֪ͨ
            msg = new Message();
            msg.what = MainActivity.HANDLER_SHOWNOTIFICATION;
            bundle.putInt("id", appInfo.getId());
            bundle.putCharSequence("ticker", "���ڻ�ԭ��" + appInfo.getName());
            bundle.putCharSequence("title", "���ڻ�ԭ");
            bundle.putCharSequence("text", appInfo.getName());
            bundle.putInt("flags", Notification.FLAG_NO_CLEAR);
            msg.setData(bundle);
            handler.sendMessage(msg);

            String restorePath = Environment.getExternalStorageDirectory()
                    + "/SimpleBackup/";
            File restoreFile = new File(restorePath + appInfo.getPackageName()
                    + ".zip");
            try
            {
                ZipUtil.upZipFile(restoreFile, restorePath);
            }
            catch (ZipException e)
            {

            }
            catch (IOException e)
            {

            }
            // ��װapk
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(
                    Uri.fromFile(new File(restorePath
                            + appInfo.getPackageName() + ".apk")),
                    "application/vnd.android.package-archive");
            activity.startActivity(intent);

            // ��ԭ����
            String cmd = "busybox tar zxvf " + restorePath + appInfo.getPackageName()
                    + ".tar.gz";
            ShellUtil.RootCmd(cmd);

            // �رնԻ���
            msg = new Message();
            msg.what = MainActivity.HANDLER_CLOSEPROGRESSDIALOG;
            handler.sendMessage(msg);
            // ��ʾ�ɹ�֪ͨ
            msg = new Message();
            msg.what = MainActivity.HANDLER_SHOWNOTIFICATION;
            bundle.putCharSequence("ticker", "��ԭ��ɣ�" + appInfo.getName());
            bundle.putCharSequence("title", "��ԭ���");
            bundle.putInt("flags", Notification.FLAG_ONLY_ALERT_ONCE);
            msg.setData(bundle);
            handler.sendMessage(msg);
            msg = new Message();
            msg.what = MainActivity.HANDLER_INVALIDATE;
            handler.sendMessage(msg);
        }
    }
}
