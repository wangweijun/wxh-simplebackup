package com.Cissoid.simplebackup.app;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.Cissoid.simplebackup.MainActivity;
import com.Cissoid.simplebackup.SimpleBackupApplication;
import com.Cissoid.simplebackup.util.CopyUtil;
import com.Cissoid.simplebackup.util.ShellUtil;
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
     * ���һ��ʵ��
     * 
     * @param appInfo
     * @return ���ɵ�ʵ��
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
                onBackup();
            }
        });
        restoreButton.setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick( View v )
            {
                restore();
            }
        });
        uninstallButton.setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick( View v )
            {
                uninstall();
            }
        });
        dialog.setContentView(view);
        return dialog;
    }

    private void onBackup()
    {
        SimpleBackupApplication application = ((SimpleBackupApplication) getActivity()
                .getApplication());
        final Handler handler = ((MainActivity) getActivity()).getHandler();
        application.getExecutorService()
                .submit(new AppThread(handler, appInfo));
        this.dismiss();
    }

    private void restore()
    {
        if ( !ShellUtil.Cmd("busybox") )
        {
            Toast.makeText(getActivity(), "busybox not found",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        // Ӧ�ð�װĿ¼
        String originalAppPath = appInfo.getAppPath();
        // Ӧ������Ŀ¼
        String originalDataPath = appInfo.getDataPath();
        // ����Ŀ¼
        String backupPath = Environment.getExternalStorageDirectory()
                + "/SimpleBackup/" + appInfo.getPackageName();
        if ( CopyUtil.copyFile(backupPath + ".apk", originalAppPath)
                && CopyUtil.copyWithRoot(backupPath, originalDataPath) )
            Toast.makeText(getActivity(), "success", Toast.LENGTH_SHORT).show();
    }

    private void uninstall()
    {
        this.dismiss();
        Uri uri = Uri.fromParts("package", appInfo.getPackageName(), null);
        Intent intent = new Intent(Intent.ACTION_DELETE, uri);
        getActivity().startActivity(intent);

    }
}
