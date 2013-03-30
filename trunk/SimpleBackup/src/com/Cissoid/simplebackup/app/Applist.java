package com.Cissoid.simplebackup.app;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;

public class Applist
{
    private Context context;
    private ArrayList<AppInfo> appInfos;

    public Applist(Context context)
    {
        this.context = context;
        appInfos = getData();
    }

    public Context getContext()
    {
        return context;
    }

    public int getCount()
    {
        return appInfos.size();
    }

    public AppInfo getItem(int position)
    {
        if (position < 0 || position >= appInfos.size())
            return null;
        return appInfos.get(position);
    }

    public ArrayList<AppInfo> getData()
    {
        ArrayList<AppInfo> appInfos = new ArrayList<AppInfo>();
        AppInfo appInfo;
        List<PackageInfo> packages = context.getPackageManager()
                .getInstalledPackages(0);
        for (int i = 0; i < packages.size(); i++)
        {
            PackageInfo packageInfo = packages.get(i);
            if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0)
            {
                appInfo = new AppInfo();
                appInfo.setName(packageInfo.applicationInfo.loadLabel(
                        context.getPackageManager()).toString());
                appInfo.setPackageName(packageInfo.packageName);
                appInfo.setVersion(packageInfo.versionName);
                appInfo.setIcon(packageInfo.applicationInfo.loadIcon(context
                        .getPackageManager()));
                appInfos.add(appInfo);
            }
        }
        return appInfos;
    }
}
