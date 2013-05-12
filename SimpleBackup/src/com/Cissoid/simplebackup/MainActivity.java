package com.Cissoid.simplebackup;

import java.io.File;
import java.lang.reflect.Field;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NotificationCompat.Builder;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.ViewConfiguration;

import com.Cissoid.simplebackup.app.AppFragment;
import com.Cissoid.simplebackup.util.ShellUtil;
import com.wxhcn.simplebackup.R;

/**
 * 
 * @author Wxh
 * @since 2013.03.05
 * 
 */
public class MainActivity extends FragmentActivity
{
    public static final int STATUS_OK = 0;
    public static final int STATUS_NO_ROOT = 1;
    public static final int STATUS_NO_BUSYBOX = 2;
    public static final int STATUS_NO_SDCARD = 3;

    public static final int HANDLER_INVALIDATE = 0;
    public static final int HANDLER_CLOSEPROGRESSDIALOG = 1;
    public static final int HANDLER_SHOWPROGRESSDIALOG = 2;
    public static final int HANDLER_SHOWNOTIFICATION = 3;
    public static final int HANDLER_INSTALL = 4;
    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
     * will keep every loaded fragment in memory. If this becomes too memory
     * intensive, it may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private Menu menu;
    private AppService appService;
    private ProgressDialog progressDialog = null;

    private AppFragment appFragment = null;

    public Handler handler = new Handler()
    {
        public void handleMessage( Message msg )
        {
            switch ( msg.what )
            {
            case HANDLER_INVALIDATE :
            {
                if ( appFragment != null )
                    appFragment.refresh();
                break;
            }
            case HANDLER_SHOWPROGRESSDIALOG :
            {
                Bundle bundle = msg.getData();
                CharSequence title = bundle.getCharSequence("title");
                CharSequence message = bundle.getCharSequence("text");
                showProgressDialog(title, message);
                break;
            }
            case HANDLER_CLOSEPROGRESSDIALOG :
            {
                closeProgressDialog();
                break;
            }
            case HANDLER_SHOWNOTIFICATION :
            {
                Bundle bundle = msg.getData();
                int id = bundle.getInt("id");
                CharSequence ticker = bundle.getCharSequence("ticker");
                CharSequence contentTitle = bundle.getCharSequence("title");
                CharSequence contentText = bundle.getCharSequence("text");
                int flags = bundle.getInt("flags");
                showNotification(id, ticker, contentTitle, contentText, flags);
                break;
            }
            case HANDLER_INSTALL :
            {
                Bundle bundle = msg.getData();
                String path = bundle.getString("path");
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.fromFile(new File(path)),
                        "application/vnd.android.package-archive");
                startActivity(intent);
                break;
            }
            }
            super.handleMessage(msg);
        };
    };

    /**
     * @return the handler
     */
    public Handler getHandler()
    {
        return handler;
    }

    public AppService getService()
    {
        return appService;
    }

    public Menu getMenu()
    {
        return menu;
    }

    @Override
    protected void onCreate( Bundle savedInstanceState )
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSectionsPagerAdapter = new SectionPagerAdapter(
                getSupportFragmentManager(), this);

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        getActionBar().setDisplayShowTitleEnabled(false);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        // Force to use overflow button
        try
        {
            ViewConfiguration config = ViewConfiguration.get(this);
            Field menuKeyField = ViewConfiguration.class
                    .getDeclaredField("sHasPermanentMenuKey");
            if ( menuKeyField != null )
            {
                menuKeyField.setAccessible(true);
                menuKeyField.setBoolean(config, false);
            }
        }
        catch (Exception ex)
        {
            // Ignore
        }

        Intent intent = new Intent(this, AppService.class);
        bindService(intent, connection, BIND_AUTO_CREATE);
    }

    private ServiceConnection connection = new ServiceConnection()
    {

        @Override
        public void onServiceDisconnected( ComponentName name )
        {
            appService = null;
        }

        @Override
        public void onServiceConnected( ComponentName name , IBinder service )
        {
            appService = ((AppService.ServiceBinder) service).getService();
            System.out.println("Service连接成功");
            // 执行Service内部自己的方法
        }
    };

    @Override
    public boolean onCreateOptionsMenu( Menu menu )
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        this.menu = menu;

        return true;
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        unbindService(connection);
    }

    /**
     * 启动时检查各项设置
     * 
     * @return 检查结果
     */
    private int init()
    {
        // root权限
        if ( !ShellUtil.RootCmd("") )
            return STATUS_NO_ROOT;
        // busybox是否安装
        if ( !ShellUtil.Cmd("busybox") )
            return STATUS_NO_BUSYBOX;
        // SD卡是否存在
        if ( !ShellUtil.Cmd("cd /mnt/sdcard") )
            return STATUS_NO_SDCARD;
        // 用户程序数目
        //
        return STATUS_OK;
    }

    /**
     * 显示进度框
     * 
     * @param title
     *            标题
     * @param message
     *            内容
     */
    public void showProgressDialog( CharSequence title , CharSequence message )
    {
        if ( progressDialog == null )
        {
            progressDialog = new ProgressDialog(this);
            progressDialog.setCancelable(false);
            progressDialog.setTitle(title);
            progressDialog.setMessage(message);
            progressDialog.show();
        }
    }

    /**
     * 关闭已打开的进度框
     */
    public void closeProgressDialog()
    {
        if ( progressDialog != null )
        {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

    /**
     * 显示一条通知
     * 
     * @param id
     *            该通知的标示符
     * @param ticker
     *            状态栏显示消息
     * @param contentTitle
     *            通知栏标题
     * @param contentText
     *            通知栏内容
     * @param flags
     *            是否可被清除
     */
    public void showNotification( final int id , final CharSequence ticker ,
            final CharSequence contentTitle , final CharSequence contentText ,
            final int flags )
    {
        // 添加状态栏通知
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Intent intent = new Intent(this, MainActivity.class);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                intent, 0);
        Notification notification = new Builder(this).setTicker(ticker)
                .setContentTitle(contentTitle).setContentText(contentText)
                .setSmallIcon(R.drawable.ic_launcher)
                .setWhen(System.currentTimeMillis())
                .setContentIntent(pendingIntent).build();
        notification.flags = flags;
        notificationManager.notify(id, notification);
    }

    /**
     * @param fragment
     *            the appFragment to set
     */
    public void setAppFragment( Fragment fragment )
    {
        this.appFragment = (AppFragment) fragment;
    }
}
