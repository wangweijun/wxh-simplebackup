package com.Cissoid.simplebackup.app;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
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
    private AppInfo backupAppInfo = null;
    /**
     * ��SD���ϵı��ݻ����Ѱ�װ����
     */
    public int type = TYPE_LOCAL;
    /**
     * ����ģʽ
     */
    public int mode = MODE_APPDATA;
    /**
     * Ӧ������
     */
    private String name = "";
    /**
     * Ӧ�õİ���
     */
    private String packageName = "";
    /**
     * �汾��
     */
    private String version = "";
    private int versionCode = 0;
    /**
     * ͼ��
     */
    private Drawable icon = null;
    /**
     * Ӧ�ð�װ·��
     */
    private String appPath = "";
    /**
     * Ӧ������·��
     */
    private String dataPath = "";
    /**
     * ����ʱ��
     */
    private String backupTime = null;

    private MainActivity activity;

    public AppInfo( MainActivity activity )
    {
        this.activity = activity;
    }

    public void backup()
    {
        SimpleBackupApplication application = (SimpleBackupApplication) activity
                .getApplication();
        final Handler handler = activity.getHandler();
        application.getExecutorService().submit(new AppThread(handler, this));
    }

    public void restore()
    {
        // Ӧ�ð�װĿ¼
        String originalAppPath = this.getAppPath();
        // Ӧ������Ŀ¼
        String originalDataPath = this.getDataPath();
        // ����Ŀ¼
        String backupPath = Environment.getExternalStorageDirectory()
                + "/SimpleBackup/" + this.getPackageName();
        if ( CopyUtil.copyFile(backupPath + ".apk", originalAppPath)
                && CopyUtil.copyWithRoot(backupPath, originalDataPath) )
            Toast.makeText(activity, "success", Toast.LENGTH_SHORT).show();
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
     * @return the backupAppInfo
     */
    public AppInfo getBackupAppInfo()
    {
        return backupAppInfo;
    }

    /**
     * @param backupAppInfo
     *            the backupAppInfo to set
     */
    public void setBackupAppInfo( AppInfo backupAppInfo )
    {
        this.backupAppInfo = backupAppInfo;
        type = TYPE_LOCALSDCARD;
        mode = backupAppInfo.mode;
    }
}
