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
            System.out.println("Service���ӳɹ�");
            // ִ��Service�ڲ��Լ��ķ���
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
     * ����ʱ����������
     * 
     * @return �����
     */
    private int init()
    {
        // rootȨ��
        if ( !ShellUtil.RootCmd("") )
            return STATUS_NO_ROOT;
        // busybox�Ƿ�װ
        if ( !ShellUtil.Cmd("busybox") )
            return STATUS_NO_BUSYBOX;
        // SD���Ƿ����
        if ( !ShellUtil.Cmd("cd /mnt/sdcard") )
            return STATUS_NO_SDCARD;
        // �û�������Ŀ
        //
        return STATUS_OK;
    }

    /**
     * ��ʾ���ȿ�
     * 
     * @param title
     *            ����
     * @param message
     *            ����
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
     * �ر��Ѵ򿪵Ľ��ȿ�
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
     * ��ʾһ��֪ͨ
     * 
     * @param id
     *            ��֪ͨ�ı�ʾ��
     * @param ticker
     *            ״̬����ʾ��Ϣ
     * @param contentTitle
     *            ֪ͨ������
     * @param contentText
     *            ֪ͨ������
     * @param flags
     *            �Ƿ�ɱ����
     */
    public void showNotification( final int id , final CharSequence ticker ,
            final CharSequence contentTitle , final CharSequence contentText ,
            final int flags )
    {
        // ���״̬��֪ͨ
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
