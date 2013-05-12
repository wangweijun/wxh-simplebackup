package com.Cissoid.simplebackup.app;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import com.Cissoid.simplebackup.MainActivity;
import com.Cissoid.simplebackup.SimpleBackupApplication;
import com.Cissoid.simplebackup.util.CopyUtil;

/**
 * @author Wxh
 * 
 */
public class AppInfo
{
    public static final int TYPE_LOCALSDCARD = 0x0;
    public static final int TYPE_LOCAL = 0x1;
    public static final int TYPE_SDCARD = 0x10;
    public static final int MODE_APPDATA = 0x0;
    public static final int MODE_APPONLY = 0x1;
    public static final int MODE_DATAONLY = 0x10;
    private int id;
    /**
     * 是SD卡上的备份还是已安装程序
     */
    public int type = TYPE_LOCAL;
    /**
     * 备份模式
     */
    public int mode = MODE_APPDATA;
    /**
     * 应用名称
     */
    private String name = "";
    /**
     * 应用的包名
     */
    private String packageName = "";
    /**
     * 版本号
     */
    private String version = "";
    private int versionCode = 0;
    /**
     * 图标
     */
    private Drawable icon = null;
    /**
     * 应用安装路径
     */
    private String appPath = "";
    /**
     * 应用数据路径
     */
    private String dataPath = "";
    /**
     * 备份时间
     */
    private String backupTime = "无备份";

    private MainActivity activity;
    private View view = null;

    public AppInfo( MainActivity activity )
    {
        this.activity = activity;
    }

    public void backup()
    {
        SimpleBackupApplication application = (SimpleBackupApplication) activity
                .getApplication();
        final Handler handler = activity.getHandler();
        AppThread thread = new AppThread(activity, this);
        thread.mode = AppThread.MODE_BACKUP;
        application.getExecutorService().submit(thread);
    }

    public void restore()
    {
        SimpleBackupApplication application = (SimpleBackupApplication) activity
                .getApplication();
        final Handler handler = activity.getHandler();
        AppThread thread = new AppThread(activity, this);
        thread.mode = AppThread.MODE_RESTORE;
        application.getExecutorService().submit(thread);
    }

    public void uninstall()
    {
        Uri uri = Uri.fromParts("package", this.getPackageName(), null);
        Intent intent = new Intent(Intent.ACTION_DELETE, uri);
        activity.startActivity(intent);
    }

    /**
     * @return the id
     */
    public int getId()
    {
        return id;
    }

    /**
     * @param id
     *            the id to set
     */
    public void setId( int id )
    {
        this.id = id;
    }

    /**
     * @return the name
     */
    public String getName()
    {
        return name;
    }

    /**
     * @param name
     *            the name to set
     */
    public void setName( String name )
    {
        this.name = name;
    }

    /**
     * @return the packageName
     */
    public String getPackageName()
    {
        return packageName;
    }

    /**
     * @param packageName
     *            the packageName to set
     */
    public void setPackageName( String packageName )
    {
        this.packageName = packageName;
    }

    /**
     * @return the version
     */
    public String getVersion()
    {
        return version;
    }

    /**
     * @param version
     *            the version to set
     */
    public void setVersion( String version )
    {
        this.version = version;
    }

    /**
     * @return the versionCode
     */
    public int getVersionCode()
    {
        return versionCode;
    }

    /**
     * @param versionCode
     *            the versionCode to set
     */
    public void setVersionCode( int versionCode )
    {
        this.versionCode = versionCode;
    }

    /**
     * @return the icon
     */
    public Drawable getIcon()
    {
        return icon;
    }

    /**
     * @param icon
     *            the icon to set
     */
    public void setIcon( Drawable icon )
    {
        this.icon = icon;
    }

    /**
     * @return the appPath
     */
    public String getAppPath()
    {
        return appPath;
    }

    /**
     * @param appPath
     *            the appPath to set
     */
    public void setAppPath( String appPath )
    {
        this.appPath = appPath;
    }

    /**
     * @return the dataPath
     */
    public String getDataPath()
    {
        return dataPath;
    }

    /**
     * @param dataPath
     *            the dataPath to set
     */
    public void setDataPath( String dataPath )
    {
        this.dataPath = dataPath;
    }

    /**
     * @return the backupTime
     */
    public String getBackupTime()
    {
        return backupTime;
    }

    /**
     * @param backupTime
     *            the backupTime to set
     */
    public void setBackupTime( String backupTime )
    {
        this.backupTime = backupTime;
    }

    /**
     * @param backupAppInfo
     *            the backupAppInfo to set
     */
    public void setBackupAppInfo( AppInfo backupAppInfo )
    {
        this.backupTime = backupAppInfo.getBackupTime();
        type = TYPE_LOCALSDCARD;
        mode = backupAppInfo.mode;
    }

    /**
     * @return the view
     */
    public View getView()
    {
        return view;
    }

    /**
     * @param view
     *            the view to set
     */
    public void setView( View view )
    {
        this.view = view;
    }
}
