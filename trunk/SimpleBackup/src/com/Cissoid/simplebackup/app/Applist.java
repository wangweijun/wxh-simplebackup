package com.Cissoid.simplebackup.app;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;

public class Applist
{
    private Context context;
    /**
     * Ӧ���б�
     */
    private ArrayList<AppInfo> appInfos;
    /**
     * �б��ѡ��־
     */
    private boolean isMultiselect;

    public Applist( Context context )
    {
        this.context = context;
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
        List<PackageInfo> packages = context.getPackageManager()
                .getInstalledPackages(0);
        // ��ȡ�Ѱ�װӦ��
        for ( int i = 0 ; i < packages.size() ; i++ )
        {
            PackageInfo packageInfo = packages.get(i);
            // ��ϵͳӦ��
            if ( (packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0 )
            {
                appInfo = new AppInfo();
                appInfo.setName(packageInfo.applicationInfo.loadLabel(
                        context.getPackageManager()).toString());
                appInfo.setPackageName(packageInfo.packageName);
                appInfo.setVersion(packageInfo.versionName);
                appInfo.setIcon(packageInfo.applicationInfo.loadIcon(context
                        .getPackageManager()));
                appInfo.setAppPath(packageInfo.applicationInfo.sourceDir);
                appInfo.setDataPath(packageInfo.applicationInfo.dataDir);
                appInfo.setType(AppInfo.TYPE_LOCAL);
                appInfos.add(appInfo);
                appInfo.setId(appInfos.indexOf(appInfo));
            }
            // ϵͳӦ��
            else
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
            int type1 = ((AppInfo) obj1).getType();
            int type2 = ((AppInfo) obj2).getType();
            if ( type1 != type2 )
                return type1 - type2;
            String name1 = ((AppInfo) obj1).getName();
            String name2 = ((AppInfo) obj2).getName();
            return name1.compareTo(name2);
        }
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
