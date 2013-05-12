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
            // 显示对话框
            Message msg = new Message();
            msg.what = MainActivity.HANDLER_SHOWPROGRESSDIALOG;
            Bundle bundle = new Bundle();
            bundle.putCharSequence("title", "正在备份...");
            bundle.putCharSequence("text", appInfo.getName());
            msg.setData(bundle);
            handler.sendMessage(msg);
            // 显示通知
            msg = new Message();
            msg.what = MainActivity.HANDLER_SHOWNOTIFICATION;
            bundle.putInt("id", appInfo.getId());
            bundle.putCharSequence("ticker", "正在备份：" + appInfo.getName());
            bundle.putCharSequence("title", "正在备份");
            bundle.putCharSequence("text", appInfo.getName());
            bundle.putInt("flags", Notification.FLAG_NO_CLEAR);
            msg.setData(bundle);
            handler.sendMessage(msg);

            // 应用安装目录
            String originalAppPath = appInfo.getAppPath();
            // 应用数据目录
            String originalDataPath = appInfo.getDataPath();
            // 备份目录
            String backupPath = Environment.getExternalStorageDirectory()
                    + "/SimpleBackup/" + appInfo.getPackageName();
            // 拷贝安装包
            CopyUtil.copyFile(originalAppPath, backupPath + ".apk");
            // 拷贝数据文件
            String cmd = "busybox tar zcvf " + backupPath + ".tar.gz "
                    + originalDataPath;
            ShellUtil.RootCmd(cmd);

            // 压缩
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
                // 压缩文件
                ZipUtil.zipFiles(resFileList, zipFile);
                // 删除原始文件
                ShellUtil.Cmd("rm -r " + backupPath + ".apk  " + backupPath
                        + ".tar.gz");
                // 记录备份时间
                SimpleDateFormat formatter = new SimpleDateFormat(
                        "备份时间:yyyy.MM.dd HH:mm");
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
            msg = new Message();
            msg.what = MainActivity.HANDLER_CLOSEPROGRESSDIALOG;
            handler.sendMessage(msg);
            // 显示成功通知
            msg = new Message();
            msg.what = MainActivity.HANDLER_SHOWNOTIFICATION;
            bundle.putCharSequence("ticker", "备份完成：" + appInfo.getName());
            bundle.putCharSequence("title", "备份完成");
            bundle.putInt("flags", Notification.FLAG_ONLY_ALERT_ONCE);
            msg.setData(bundle);
            handler.sendMessage(msg);
            msg = new Message();
            msg.what = MainActivity.HANDLER_INVALIDATE;
            handler.sendMessage(msg);

            // 刷新UI
            appInfo.setBackupAppInfo(appInfo);
            appInfo.getView().postInvalidate();
        }
    }

    private void restore()
    {
        for ( AppInfo appInfo : appInfos )
        {
            // 显示对话框
            Message msg = new Message();
            msg.what = MainActivity.HANDLER_SHOWPROGRESSDIALOG;
            Bundle bundle = new Bundle();
            bundle.putCharSequence("title", "正在还原...");
            bundle.putCharSequence("text", appInfo.getName());
            msg.setData(bundle);
            handler.sendMessage(msg);
            // 显示通知
            msg = new Message();
            msg.what = MainActivity.HANDLER_SHOWNOTIFICATION;
            bundle.putInt("id", appInfo.getId());
            bundle.putCharSequence("ticker", "正在还原：" + appInfo.getName());
            bundle.putCharSequence("title", "正在还原");
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
            // 安装apk
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(
                    Uri.fromFile(new File(restorePath
                            + appInfo.getPackageName() + ".apk")),
                    "application/vnd.android.package-archive");
            activity.startActivity(intent);

            // 还原数据
            String cmd = "busybox tar zxvf " + restorePath + appInfo.getPackageName()
                    + ".tar.gz";
            ShellUtil.RootCmd(cmd);

            // 关闭对话框
            msg = new Message();
            msg.what = MainActivity.HANDLER_CLOSEPROGRESSDIALOG;
            handler.sendMessage(msg);
            // 显示成功通知
            msg = new Message();
            msg.what = MainActivity.HANDLER_SHOWNOTIFICATION;
            bundle.putCharSequence("ticker", "还原完成：" + appInfo.getName());
            bundle.putCharSequence("title", "还原完成");
            bundle.putInt("flags", Notification.FLAG_ONLY_ALERT_ONCE);
            msg.setData(bundle);
            handler.sendMessage(msg);
            msg = new Message();
            msg.what = MainActivity.HANDLER_INVALIDATE;
            handler.sendMessage(msg);
        }
    }
}
