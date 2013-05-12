package com.Cissoid.simplebackup.app;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.os.Environment;

import com.Cissoid.simplebackup.MainActivity;
import com.Cissoid.simplebackup.util.XmlUtil;

public class Applist
{
    private MainActivity activity;
    /**
     * Ӧ���б�
     */
    private ArrayList<AppInfo> appInfos;
    /**
     * �б��ѡ��־
     */
    private boolean isMultiselect;

    public Applist( MainActivity activity )
    {
        this.activity = activity;
        appInfos = getData();
        isMultiselect = false;
    }

    public int getCount()
    {
        return appInfos.size();
    }

    public AppInfo getItem( int position )
    {
        if ( position < 0 || position >= appInfos.size() )
            return null;
        return appInfos.get(position);
    }

    public ArrayList<AppInfo> getData()
    {
        ArrayList<AppInfo> appInfos = new ArrayList<AppInfo>();
        AppInfo appInfo;
        List<PackageInfo> packages = activity.getPackageManager()
                .getInstalledPackages(0);
        // ��ȡ�Ѱ�װӦ��
        for ( int i = 0 ; i < packages.size() ; i++ )
        {
            PackageInfo packageInfo = packages.get(i);
            // ��ϵͳӦ��
            if ( (packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0 )
            {
                appInfo = new AppInfo(activity);
                appInfo.type = AppInfo.TYPE_LOCAL;
                appInfo.setName(packageInfo.applicationInfo.loadLabel(
                        activity.getPackageManager()).toString());
                appInfo.setPackageName(packageInfo.packageName);
                appInfo.setVersion(packageInfo.versionName);
                appInfo.setVersionCode(packageInfo.versionCode);
                appInfo.setIcon(packageInfo.applicationInfo.loadIcon(activity
                        .getPackageManager()));
                appInfo.setAppPath(packageInfo.applicationInfo.sourceDir);
                appInfo.setDataPath(packageInfo.applicationInfo.dataDir);
                appInfos.add(appInfo);
                appInfo.setId(appInfos.indexOf(appInfo));
            }
            // ϵͳӦ��
            else
            {
            }
        }
        // ��ȡSD���ϵı���
        FileInputStream fileInputStream;
        File file;
        ArrayList<String> paths = GetXmls();
        if ( paths != null )
        {
            try
            {
                for ( String path : paths )
                {
                    file = new File(path);
                    fileInputStream = new FileInputStream(file);
                    appInfo = XmlUtil.readAppCfg(fileInputStream, activity);
                    boolean flag = false;
                    for ( AppInfo a : appInfos )
                    {
                        if ( a.getPackageName().equalsIgnoreCase(
                                appInfo.getPackageName()) )
                        {
                            a.setBackupAppInfo(appInfo);
                            flag = true;
                            break;
                        }
                    }
                    if ( flag == false )
                    {
                        appInfo.type = AppInfo.TYPE_SDCARD;
                        appInfos.add(appInfo);
                    }
                }
            }
            catch (FileNotFoundException e)
            {

            }
            catch (NullPointerException e)
            {

            }
        }

        // ����
        Collections.sort(appInfos, new SortByName());
        return appInfos;
    }

    class SortByName implements Comparator
    {
        @Override
        public int compare( Object obj1 , Object obj2 )
        {
            int type1 = ((AppInfo) obj1).type;
            int type2 = ((AppInfo) obj2).type;
            if ( type1 != type2 )
                return type1 - type2;
            String name1 = ((AppInfo) obj1).getName();
            String name2 = ((AppInfo) obj2).getName();
            return name1.compareTo(name2);
        }
    }

    public ArrayList<String> GetXmls() // ����Ŀ¼����չ�����Ƿ�������ļ���
    {
        ArrayList<String> paths = new ArrayList<String>();
        File[] files = new File(Environment.getExternalStorageDirectory()
                + "/SimpleBackup/").listFiles();
        File f;
        if ( files == null )
        {
            return null;
        }
        for ( int i = 0 ; i < files.length ; i++ )
        {
            f = files[i];
            if ( !f.canRead() )
            {
                return null;
            }
            if ( f.isFile() )
            {
                if ( f.getName().contains(".xml") ) // �ж���չ��
                {
                    paths.add(f.getPath());
                }
            }
        }
        return paths;
    }

    /**
     * ���ö�ѡ��־
     * 
     * @param flag
     */
    public void setMultiSelect( boolean flag )
    {
        isMultiselect = flag;
    }

    /**
     * �ж��Ƿ��ѡ
     * 
     * @return ��ѡ״̬
     */
    public boolean isMultiSelect()
    {
        return isMultiselect;
    }
}
