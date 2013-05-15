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
     * 备份
     */
    private void backup()
    {
        for ( AppInfo appInfo : appInfos )
        {
            // 显示对话框
            showDialog("正在备份...", appInfo.getName());
            // 显示通知
            showNotification(appInfo.getId(), "正在备份:" + appInfo.getName(),
                    "正在备份", appInfo.getName(), Notification.FLAG_NO_CLEAR);
            // 应用安装目录
            String originalAppPath = appInfo.getAppPath();
            // 应用数据目录
            String originalDataPath = appInfo.getDataPath();
            // 备份目录
            String backupPath = Environment.getExternalStorageDirectory()
                    + "/SimpleBackup/" + appInfo.getPackageName();
            // 拷贝安装包
            if ( mode != MODE_BACKUP_DATA )
                CopyUtil.copyFile(originalAppPath, backupPath + ".apk");
            // 拷贝数据文件
            if ( mode != MODE_BACKUP_APP )
            {
                String cmd = "busybox tar zcvf " + backupPath + ".tar.gz "
                        + originalDataPath;
                ShellUtil.RootCmd(cmd);
            }

            // 压缩
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
                // 压缩文件
                ZipUtil.zipFiles(resFileList, zipFile);
                // 删除原始文件
                String cmd = "rm -r ";
                if ( mode != MODE_BACKUP_DATA )
                    cmd += backupPath + ".apk ";
                if ( mode != MODE_BACKUP_APP )
                    cmd += backupPath + ".tar.gz";
                ShellUtil.Cmd(cmd);
                // 记录备份时间
                SimpleDateFormat formatter = new SimpleDateFormat(
                        activity.getString(R.string.app_list_backup_time)
                                + ":yyyy.MM.dd HH:mm");
                Date curDate = new Date(System.currentTimeMillis());
                String time = formatter.format(curDate);
                appInfo.setBackupTime(time);
                // 创建xml
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
            // 关闭对话框
            Message msg = new Message();
            msg.what = MainActivity.HANDLER_CLOSE_PROGRESSDIALOG;
            handler.sendMessage(msg);
            // 显示成功通知
            showNotification(appInfo.getId(),
                    activity.getString(R.string.notification_backup_complete)
                            + ":" + appInfo.getName(),
                    activity.getString(R.string.notification_backup_complete),
                    appInfo.getName(), Notification.FLAG_ONLY_ALERT_ONCE);
            msg = new Message();
            msg.what = MainActivity.HANDLER_INVALIDATE;
            handler.sendMessage(msg);

            // 刷新UI
            appInfo.setBackupAppInfo(appInfo);
            appInfo.getView().postInvalidate();
        }
    }

    /**
     * 还原
     */
    private void restore()
    {
        for ( AppInfo appInfo : appInfos )
        {
            // 显示对话框
            showDialog("正在还原", appInfo.getName());
            // 显示通知
            showNotification(appInfo.getId(), "正在还原:" + appInfo.getName(),
                    "正在还原", appInfo.getName(), Notification.FLAG_NO_CLEAR);

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
            // 安装apk
            if ( mode != MODE_RESTORE_DATA )
            {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(
                        Uri.fromFile(new File(restorePath
                                + appInfo.getPackageName() + ".apk")),
                        "application/vnd.android.package-archive");
                activity.startActivity(intent);
            }
            // 还原数据
            if ( mode != MODE_RESTORE_APP )
            {
                String cmd = "busybox tar zxvf " + restorePath
                        + appInfo.getPackageName() + ".tar.gz";
                ShellUtil.RootCmd(cmd);
            }
            // 关闭对话框
            Message msg = new Message();
            msg.what = MainActivity.HANDLER_CLOSE_PROGRESSDIALOG;
            handler.sendMessage(msg);
            // 显示成功通知
            showNotification(appInfo.getId(), "还原完成:" + appInfo.getName(),
                    "还原完成", appInfo.getName(),
                    Notification.FLAG_ONLY_ALERT_ONCE);
            msg = new Message();
            msg.what = MainActivity.HANDLER_INVALIDATE;
            handler.sendMessage(msg);
        }
    }

    /**
     * 显示对话框
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
     * 显示通知
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
