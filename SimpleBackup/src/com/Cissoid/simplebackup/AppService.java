package com.Cissoid.simplebackup;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.widget.Toast;

import com.Cissoid.simplebackup.app.AppInfo;
import com.Cissoid.simplebackup.util.CopyUtil;
import com.Cissoid.simplebackup.util.ShellUtil;

public class AppService extends Service
{
    private Context context;
    public Handler handler = new Handler()
    {
        public void handleMessage(Message msg)
        {
        };
    };

    public void setContext(Context context)
    {
        this.context = context;
    }

    public class ServiceBinder extends Binder
    {
        AppService getService()
        {
            return AppService.this;
        }
    }

    private ServiceBinder binder = new ServiceBinder();

    @Override
    public IBinder onBind(Intent intent)
    {
        // TODO Auto-generated method stub
        return binder;
    }

    @Override
    public void onCreate()
    {
        // TODO Auto-generated method stub
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        // TODO Auto-generated method stub
        return super.onStartCommand(intent, flags, startId);
    }

    public int backup(final AppInfo appInfo)
    {
        if (!ShellUtil.Cmd("busybox"))
            return -1;

        // 应用安装目录
        String originalAppPath = appInfo.getAppPath();
        // 应用数据目录
        String originalDataPath = appInfo.getDataPath();
        // 备份目录
        String backupPath = Environment.getExternalStorageDirectory()
                + "/SimpleBackup/" + appInfo.getPackageName();

        if (CopyUtil.copyFile(originalAppPath, backupPath + ".apk")
                && CopyUtil.copyWithRoot(originalDataPath, backupPath))
        {
            Toast.makeText(context, "success", Toast.LENGTH_SHORT).show();
            return 1;
        }
        return 0;
    }
}
