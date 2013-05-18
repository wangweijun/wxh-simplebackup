package com.Cissoid.simplebackup;

import java.io.File;
import java.lang.reflect.Field;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NotificationCompat.Builder;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.View;
import android.view.ViewConfiguration;

import com.Cissoid.simplebackup.app.AppFragment;
import com.Cissoid.simplebackup.home.HomePageFragment;
import com.Cissoid.simplebackup.sms.SmsBackupTask;
import com.Cissoid.simplebackup.sms.SmsFragment;
import com.Cissoid.simplebackup.sms.ThreadInfo;
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
    public static final int HANDLER_CLOSE_PROGRESSDIALOG = 1;
    public static final int HANDLER_SHOW_PROGRESSDIALOG = 2;
    public static final int HANDLER_SHOW_NOTIFICATION = 3;
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
    private ProgressDialog progressDialog = null;
    private Status status = null;
    private AppFragment appFragment = null;
    private HomePageFragment homePageFragment = null;
    private SmsFragment smsFragment = null;
    private DrawableChangeView drawableChangeView;
    public Handler handler = new Handler()
    {
        public void handleMessage( Message msg )
        {
            switch ( msg.what )
            {
            // 刷新UI消息
            case HANDLER_INVALIDATE :
            {
                if ( appFragment != null )
                    appFragment.refresh();
                break;
            }
            // 显示ProgressDialog
            case HANDLER_SHOW_PROGRESSDIALOG :
            {
                Bundle bundle = msg.getData();
                String title = bundle.getString("title");
                String message = bundle.getString("message");
                showProgressDialog(title, message);
                break;
            }
            // 关闭ProgressDialog
            case HANDLER_CLOSE_PROGRESSDIALOG :
            {
                closeProgressDialog();
                break;
            }
            // 显示通知
            case HANDLER_SHOW_NOTIFICATION :
            {
                Bundle bundle = msg.getData();
                int id = bundle.getInt("id");
                String ticker = bundle.getString("ticker");
                String contentTitle = bundle.getString("title");
                String contentText = bundle.getString("text");
                int flags = bundle.getInt("flags");
                showNotification(id, ticker, contentTitle, contentText, flags);
                break;
            }
            // 安装应用
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

    public Menu getMenu()
    {
        return menu;
    }

    public void setStatus( Status status )
    {
        this.status = status;
    }

    public Status getStatus()
    {
        return status;
    }

    @Override
    protected void onCreate( Bundle savedInstanceState )
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 系统状态
        status = init();

        mSectionsPagerAdapter = new SectionPagerAdapter(
                getSupportFragmentManager(), this);

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);

        mViewPager.setAdapter(mSectionsPagerAdapter);

        mViewPager.setOnPageChangeListener(new OnPageChangeListener()
        {

            @Override
            public void onPageSelected( int arg0 )
            {
            }

            @Override
            public void onPageScrolled( int arg0 , float arg1 , int arg2 )
            {

            }

            @Override
            public void onPageScrollStateChanged( int arg0 )
            {
            }
        });
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        if ( appFragment != null )
            appFragment.refleshAll();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
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
        else
        {
            progressDialog.setMessage(message);
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
     * 
     * @param appFragment
     */
    public void setAppFragment( AppFragment appFragment )
    {
        this.appFragment = appFragment;
    }

    private Status init()
    {
        Status status = new Status();

        // root权限
        if ( ShellUtil.RootCmd("") )
        {
            status.setRoot(true);
        }

        // busybox是否安装
        if ( ShellUtil.Cmd("busybox") )
        {
            status.setBusybox(true);
        }

        // SD卡是否存在
        if ( Environment.getExternalStorageState().equalsIgnoreCase(
                Environment.MEDIA_MOUNTED) )
        {
            status.setSdcard(true);
        }
        return status;
    }

    /**
     * @param homePageFragment
     *            the homePageFragment to set
     */
    public void setHomePageFragment( HomePageFragment homePageFragment )
    {
        this.homePageFragment = homePageFragment;
    }

    /**
     * @param smsFragment
     *            the smsFragment to set
     */
    public void setSmsFragment( SmsFragment smsFragment )
    {
        this.smsFragment = smsFragment;
    }
}
