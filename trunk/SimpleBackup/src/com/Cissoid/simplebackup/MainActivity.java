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
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NotificationCompat.Builder;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.ViewConfiguration;

import com.Cissoid.simplebackup.app.AppFragment;
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

    private AppFragment appFragment = null;

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

//        getActionBar().setDisplayShowTitleEnabled(false);
//        getActionBar().setDisplayHomeAsUpEnabled(true);

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
        ((SimpleBackupApplication) getApplication()).getExecutorService()
                .submit(new CheckStatusThread(this));
    }

    @Override
    public boolean onCreateOptionsMenu( Menu menu )
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        this.menu = menu;

        return true;
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
     * @param fragment
     *            the appFragment to set
     */
    public void setAppFragment( Fragment fragment )
    {
        this.appFragment = (AppFragment) fragment;
    }
}
