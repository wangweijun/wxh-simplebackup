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
import com.wxhcn.simplebackup.R;

public class AppThread implements Runnable
{
    public static final int MODE_BACKUP_ALL = 0x0;
    public static final int MODE_BACKUP_APP = 0x1;
    public static final int MODE_BACKUP_DATA = 0x2;
    public static final int MODE_RESTORE_ALL = 0x10;
    public static final int MODE_RESTORE_APP = 0x11;
    public static final int MODE_RESTORE_DATA = 0x12;
    public static final int MODE_DELETE = 0x20;
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
        case MODE_BACKUP_ALL :
        case MODE_BACKUP_APP :
        case MODE_BACKUP_DATA :
            backup();
            break;
        case MODE_RESTORE_ALL :
        case MODE_RESTORE_APP :
        case MODE_RESTORE_DATA :
            restore();
            break;
        case MODE_DELETE :
            break;
        default :
            break;
        }
    }

    /**
     * ����
     */
    private void backup()
    {
        for ( AppInfo appInfo : appInfos )
        {
            // ��ʾ�Ի���
            showDialog("���ڱ���...", appInfo.getName());
            // ��ʾ֪ͨ
            showNotification(appInfo.getId(), "���ڱ���:" + appInfo.getName(),
                    "���ڱ���", appInfo.getName(), Notification.FLAG_NO_CLEAR);
            // Ӧ�ð�װĿ¼
            String originalAppPath = appInfo.getAppPath();
            // Ӧ������Ŀ¼
            String originalDataPath = appInfo.getDataPath();
            // ����Ŀ¼
            String backupPath = Environment.getExternalStorageDirectory()
                    + "/SimpleBackup/" + appInfo.getPackageName();
            // ������װ��
            if ( mode != MODE_BACKUP_DATA )
                CopyUtil.copyFile(originalAppPath, backupPath + ".apk");
            // ���������ļ�
            if ( mode != MODE_BACKUP_APP )
            {
                String cmd = "busybox tar zcvf " + backupPath + ".tar.gz "
                        + originalDataPath;
                ShellUtil.RootCmd(cmd);
            }

            // ѹ��
            ArrayList<File> resFileList = new ArrayList<File>();
            File apk;
            if ( mode != MODE_BACKUP_DATA )
            {
                apk = new File(backupPath + ".apk");
                if ( apk.exists() )
                    resFileList.add(apk);
            }
            File data;
            if ( mode != MODE_BACKUP_APP )
            {
                data = new File(backupPath + ".tar.gz");
                if ( data.exists() )
                    resFileList.add(data);
            }
            File zipFile = new File(backupPath + ".zip");
            try
            {
                // ѹ���ļ�
                ZipUtil.zipFiles(resFileList, zipFile);
                // ɾ��ԭʼ�ļ�
                String cmd = "rm -r ";
                if ( mode != MODE_BACKUP_DATA )
                    cmd += backupPath + ".apk ";
                if ( mode != MODE_BACKUP_APP )
                    cmd += backupPath + ".tar.gz";
                ShellUtil.Cmd(cmd);
                // ��¼����ʱ��
                SimpleDateFormat formatter = new SimpleDateFormat(
                        activity.getString(R.string.app_list_backup_time)
                                + ":yyyy.MM.dd HH:mm");
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
            Message msg = new Message();
            msg.what = MainActivity.HANDLER_CLOSE_PROGRESSDIALOG;
            handler.sendMessage(msg);
            // ��ʾ�ɹ�֪ͨ
            showNotification(appInfo.getId(),
                    activity.getString(R.string.notification_backup_complete)
                            + ":" + appInfo.getName(),
                    activity.getString(R.string.notification_backup_complete),
                    appInfo.getName(), Notification.FLAG_ONLY_ALERT_ONCE);
            msg = new Message();
            msg.what = MainActivity.HANDLER_INVALIDATE;
            handler.sendMessage(msg);

            // ˢ��UI
            appInfo.setBackupAppInfo(appInfo);
            appInfo.getView().postInvalidate();
        }
    }

    /**
     * ��ԭ
     */
    private void restore()
    {
        for ( AppInfo appInfo : appInfos )
        {
            // ��ʾ�Ի���
            showDialog("���ڻ�ԭ", appInfo.getName());
            // ��ʾ֪ͨ
            showNotification(appInfo.getId(), "���ڻ�ԭ:" + appInfo.getName(),
                    "���ڻ�ԭ", appInfo.getName(), Notification.FLAG_NO_CLEAR);

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
            if ( mode != MODE_RESTORE_DATA )
            {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(
                        Uri.fromFile(new File(restorePath
                                + appInfo.getPackageName() + ".apk")),
                        "application/vnd.android.package-archive");
                activity.startActivity(intent);
            }
            // ��ԭ����
            if ( mode != MODE_RESTORE_APP )
            {
                String cmd = "busybox tar zxvf " + restorePath
                        + appInfo.getPackageName() + ".tar.gz";
                ShellUtil.RootCmd(cmd);
            }
            // �رնԻ���
            Message msg = new Message();
            msg.what = MainActivity.HANDLER_CLOSE_PROGRESSDIALOG;
            handler.sendMessage(msg);
            // ��ʾ�ɹ�֪ͨ
            showNotification(appInfo.getId(), "��ԭ���:" + appInfo.getName(),
                    "��ԭ���", appInfo.getName(),
                    Notification.FLAG_ONLY_ALERT_ONCE);
            msg = new Message();
            msg.what = MainActivity.HANDLER_INVALIDATE;
            handler.sendMessage(msg);
        }
    }

    /**
     * ��ʾ�Ի���
     * 
     * @param title
     * @param text
     */
    private void showDialog( String title , String text )
    {
        Message msg = new Message();
        msg.what = MainActivity.HANDLER_SHOW_PROGRESSDIALOG;
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        bundle.putString("message", text);
        msg.setData(bundle);
        handler.sendMessage(msg);
    }

    /**
     * ��ʾ֪ͨ
     * 
     * @param id
     * @param ticker
     * @param title
     * @param text
     * @param flag
     */
    private void showNotification( int id , String ticker , String title ,
            String text , int flag )
    {
        Message msg = new Message();
        msg.what = MainActivity.HANDLER_SHOW_NOTIFICATION;
        Bundle bundle = new Bundle();
        bundle.putInt("id", id);
        bundle.putString("ticker", ticker);
        bundle.putString("title", title);
        bundle.putString("text", text);
        bundle.putInt("flags", flag);
        msg.setData(bundle);
        handler.sendMessage(msg);
    }
}
