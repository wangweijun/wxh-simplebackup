package com.Cissoid.simplebackup.app;

import android.graphics.drawable.Drawable;

/**
 * @author Wxh
 * 
 */
public class AppInfo
{
    public static final int TYPE_LOCAL = 1;
    public static final int TYPE_SDCARD = 2;
    private int id;
    private int type;
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
    private String backupTime = "�ޱ���";

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
     * @return the type
     */
    public int getType()
    {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType( int type )
    {
        this.type = type;
    }

}
