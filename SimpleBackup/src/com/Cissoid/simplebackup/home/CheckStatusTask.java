package com.Cissoid.simplebackup.home;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Environment;

import com.Cissoid.simplebackup.MainActivity;
import com.Cissoid.simplebackup.R;
import com.Cissoid.simplebackup.util.DBUtil;
import com.Cissoid.simplebackup.util.ShellUtil;

public class CheckStatusTask extends AsyncTask<Void, Void, Void>
{
    private HomePageFragment fragment;
    private MainActivity activity;
    private com.Cissoid.simplebackup.Status status;
    private ProgressDialog progressDialog;

    public CheckStatusTask( HomePageFragment fragment )
    {
        this.fragment = fragment;
        this.activity = (MainActivity) fragment.getActivity();
    }

    @Override
    protected void onPreExecute()
    {
        // TODO Auto-generated method stub
        super.onPreExecute();
        progressDialog = new ProgressDialog(fragment.getActivity());
        progressDialog.setMessage(fragment
                .getString(R.string.main_dialog_checking_status));
        progressDialog.setCancelable(false);
        progressDialog.show();

    }

    @Override
    protected Void doInBackground( Void... params )
    {
        // TODO Auto-generated method stub
        status = new com.Cissoid.simplebackup.Status();
        // root权限
        if ( ShellUtil.RootCmd("") )
            status.setRoot(true);
        try
        {
            Thread.sleep(1000);
        }
        catch (InterruptedException e1)
        {

        }
        // busybox是否安装
        if ( ShellUtil.Cmd("busybox") )
            status.setBusybox(true);
        try
        {
            Thread.sleep(1000);
        }
        catch (InterruptedException e)
        {

        }
        // SD卡是否存在
        if ( Environment.getExternalStorageState().equalsIgnoreCase(
                Environment.MEDIA_MOUNTED) )

            status.setSdcard(true);
        try
        {
            Thread.sleep(1000);
        }
        catch (InterruptedException e)
        {

        }
        //百度云是否登录
        if ( DBUtil.query(activity).length() != 0 )
            status.setBae(true);
        try
        {
            Thread.sleep(1000);
        }
        catch (InterruptedException e)
        {

        }
        return null;
    }

    @Override
    protected void onPostExecute( Void result )
    {
        // TODO Auto-generated method stub
        super.onPostExecute(result);
        fragment.refresh(status);
        progressDialog.dismiss();
    }
}
