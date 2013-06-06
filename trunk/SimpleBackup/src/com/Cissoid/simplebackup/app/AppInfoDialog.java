package com.Cissoid.simplebackup.app;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.Cissoid.simplebackup.R;
import com.Cissoid.simplebackup.Status;

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
        Button restoreAllButton = (Button) view
                .findViewById(R.id.app_dialog_restore_all);
        Button restoreAppButton = (Button) view
                .findViewById(R.id.app_dialog_restore_app);
        Button restoreDataButton = (Button) view
                .findViewById(R.id.app_dialog_restore_data);
        Button deleteButton = (Button) view
                .findViewById(R.id.app_dialog_delete);
        Button uninstallButton = (Button) view
                .findViewById(R.id.app_dialog_uninstall);

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

        restoreAllButton.setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick( View v )
            {
                appInfo.restore(AppThread.MODE_RESTORE_ALL);
                dismiss();
            }
        });
        restoreAppButton.setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick( View v )
            {
                appInfo.restore(AppThread.MODE_RESTORE_APP);
                dismiss();
            }
        });
        restoreDataButton.setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick( View arg0 )
            {
                appInfo.restore(AppThread.MODE_RESTORE_DATA);
                dismiss();
            }
        });
        deleteButton.setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick( View arg0 )
            {
                appInfo.delete();
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
        if ( appInfo.type == AppInfo.TYPE_SDCARD )
        {
            backupAllButton.setEnabled(false);
            backupAppButton.setEnabled(false);
            backupDataButton.setEnabled(false);
        }
        if ( appInfo.type == AppInfo.TYPE_LOCAL )
        {
            restoreAllButton.setEnabled(false);
            restoreAppButton.setEnabled(false);
            restoreDataButton.setEnabled(false);
            deleteButton.setEnabled(false);
        }
        
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
