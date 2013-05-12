package com.Cissoid.simplebackup.app;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.wxhcn.simplebackup.R;

/**
 * 
 * @author Wxh
 * @time 2013.5.3
 */

public class AppInfoDialog extends DialogFragment
{
    private AppInfo appInfo;

    public AppInfo getAppInfo()
    {
        return appInfo;
    }

    public void setAppInfo( AppInfo appInfo )
    {
        this.appInfo = appInfo;
    }

    /**
     * 获得一个实例
     * 
     * @param appInfo
     * @return 生成的实例
     */
    static AppInfoDialog newInstance( Context context , AppInfo appInfo )
    {
        AppInfoDialog appInfoDialog = new AppInfoDialog();
        appInfoDialog.setAppInfo(appInfo);
        return appInfoDialog;
    }

    @Override
    public Dialog onCreateDialog( Bundle savedInstanceState )
    {
        Dialog dialog = new Dialog(getActivity());
        dialog.setTitle(appInfo.getName());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.app_dialog, null);
        Button backupButton = (Button) view
                .findViewById(R.id.app_dialog_backup);
        Button restoreButton = (Button) view
                .findViewById(R.id.app_dialog_restore);
        Button uninstallButton = (Button) view
                .findViewById(R.id.app_dialog_uninstall);
        backupButton.setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick( View v )
            {
                appInfo.backup();
                dismiss();
            }
        });
        restoreButton.setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick( View v )
            {
                appInfo.restore();
                dismiss();
            }
        });
        uninstallButton.setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick( View v )
            {
                appInfo.uninstall();
                dismiss();
            }
        });
        dialog.setContentView(view);
        return dialog;
    }
}
