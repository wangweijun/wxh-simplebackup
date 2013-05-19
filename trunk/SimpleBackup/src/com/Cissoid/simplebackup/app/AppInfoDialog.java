package com.cissoid.simplebackup.app;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.cissoid.simplebackup.R;
import com.cissoid.simplebackup.Status;

/**
 * 
 * @author Wxh
 * @time 2013.5.3
 */

public class AppInfoDialog extends DialogFragment
{
    private Status status;
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
    static AppInfoDialog newInstance( Status status , AppInfo appInfo )
    {
        AppInfoDialog appInfoDialog = new AppInfoDialog();
        appInfoDialog.setAppInfo(appInfo);
        appInfoDialog.setStatus(status);
        return appInfoDialog;
    }

    @Override
    public Dialog onCreateDialog( Bundle savedInstanceState )
    {
        Dialog dialog = new Dialog(getActivity());
        dialog.setTitle(appInfo.getName());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.app_dialog, null);
        Button backupAllButton = (Button) view
                .findViewById(R.id.app_dialog_backup_all);
        Button backupAppButton = (Button) view
                .findViewById(R.id.app_dialog_backup_app);
        Button backupDataButton = (Button) view
                .findViewById(R.id.app_dialog_backup_data);
        Button restoreButton = (Button) view
                .findViewById(R.id.app_dialog_restore_all);
        Button uninstallButton = (Button) view
                .findViewById(R.id.app_dialog_uninstall);
        if ( status.isRoot() && status.isBusybox() )
        {
            backupAllButton.setOnClickListener(new OnClickListener()
            {

                @Override
                public void onClick( View v )
                {
                    appInfo.mode = AppInfo.MODE_APP_DATA;
                    appInfo.backup(AppThread.MODE_BACKUP_ALL);
                    dismiss();
                }
            });
            backupDataButton.setOnClickListener(new OnClickListener()
            {

                @Override
                public void onClick( View v )
                {
                    appInfo.mode = AppInfo.MODE_DATA_ONLY;
                    appInfo.backup(AppThread.MODE_BACKUP_DATA);
                    dismiss();
                }
            });
        }
        else
        {
            backupAllButton.setClickable(false);
            backupAllButton.setVisibility(View.INVISIBLE);
            backupDataButton.setClickable(false);
            backupDataButton.setVisibility(View.INVISIBLE);
        }

        backupAppButton.setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick( View v )
            {
                appInfo.mode = AppInfo.MODE_APP_ONLY;
                appInfo.backup(AppThread.MODE_BACKUP_APP);
                dismiss();
            }
        });

        restoreButton.setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick( View v )
            {
                appInfo.restore(AppThread.MODE_RESTORE_ALL);
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

    /**
     * @param status
     *            the status to set
     */
    public void setStatus( Status status )
    {
        this.status = status;
    }
}
