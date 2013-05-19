package com.cissoid.simplebackup.sms;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.widget.Toast;

import com.cissoid.simplebackup.MainActivity;
import com.cissoid.simplebackup.R;
import com.cissoid.simplebackup.util.XmlUtil;

/**
 * �ָ��������첽��
 * 
 * @author Wxh
 * @since 2013.5.15
 * @version 0.1
 */
public class SmsRestoreTask extends AsyncTask<ThreadInfo, Integer, Integer>
{
    /**
     * ��ȡ���ŵ�Uri
     */
    private final Uri SMS_URI = Uri.parse("content://sms");
    /**
     * ��Activity
     */
    private MainActivity activity;
    private SmsFragment fragment;
    /**
     * �������Ի���
     */
    private ProgressDialog progressDialog;

    public SmsRestoreTask( SmsFragment fragment )
    {
        this.fragment = fragment;
        this.activity = (MainActivity) fragment.getActivity();
        progressDialog = new ProgressDialog(activity);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setTitle(R.string.sms_dialog_restore_title);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    @Override
    protected void onPreExecute()
    {

        super.onPreExecute();
    }

    @Override
    protected Integer doInBackground( ThreadInfo... params )
    {
        ContentResolver contentResolver = activity.getContentResolver();
        Cursor cursor;

        for ( ThreadInfo threadInfo : params )
        {
            ArrayList<SmsInfo> smsInfos;

            // ����ϵ������Ӧ�������ļ�
            File file = new File(Environment.getExternalStorageDirectory()
                    + "/SimpleBackup/SMS/" + threadInfo.getAddress() + ".xml");
            try
            {
                FileInputStream inputStream = new FileInputStream(file);
                // ��xml�л�ȡSmsInfo�б�
                smsInfos = XmlUtil.readSMSfromXML(inputStream);
                // ��xml��ȡ�ɹ�
                if ( smsInfos != null )
                {
                    progressDialog.setMax((int) smsInfos.size());
                    int num = 1;
                    // ���б���ÿһ��SmsInfo����������ݿ�
                    for ( SmsInfo smsInfo : smsInfos )
                    {
                        onProgressUpdate(num);
                        // �����Ƿ���������Ѵ���
                        cursor = contentResolver.query(SMS_URI, null, "date=?",
                                new String[]
                                { smsInfo.getDate() }, null);
                        // ����������
                        if ( cursor.moveToFirst() )
                            continue;
                        ContentValues values = new ContentValues();
                        values.put("address", smsInfo.getAddress());
                        values.put("date", smsInfo.getDate());
                        values.put("protocol", smsInfo.getProtocol());
                        values.put("read", smsInfo.getRead());
                        values.put("status", smsInfo.getStatus());
                        values.put("type", smsInfo.getType());
                        values.put("reply_path_present",
                                smsInfo.getReplyPathPresent());
                        values.put("body", smsInfo.getBody());
                        values.put("locked", smsInfo.getLocked());
                        values.put("error_code", smsInfo.getErrorCode());
                        values.put("seen", smsInfo.getSeen());
                        // ����
                        contentResolver.insert(SMS_URI, values);
                        num++;
                    }
                }
            }
            catch (FileNotFoundException e)
            {
                Toast.makeText(activity, R.string.sms_dialog_file_not_found,
                        Toast.LENGTH_SHORT).show();
            }
        }
        return null;
    }

    @Override
    protected void onProgressUpdate( Integer... values )
    {
        super.onProgressUpdate(values);
        progressDialog.setProgress(values[0]);
    }

    @Override
    protected void onPostExecute( Integer result )
    {
        // TODO Auto-generated method stub
        super.onPostExecute(result);
        fragment.refresh();
        // ��ɺ�ر�
        progressDialog.dismiss();
    }
}
