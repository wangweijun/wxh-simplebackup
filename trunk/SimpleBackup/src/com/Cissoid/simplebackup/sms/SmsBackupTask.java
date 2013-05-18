package com.Cissoid.simplebackup.sms;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.widget.Toast;

import com.Cissoid.simplebackup.MainActivity;
import com.Cissoid.simplebackup.util.XmlUtil;
import com.wxhcn.simplebackup.R;

/**
 * �������ű��ݵ��첽��
 * 
 * @author Wxh
 * 
 */
public class SmsBackupTask extends AsyncTask<ThreadInfo, Integer, Integer>
{
    /**
     * ��ȡ���ŵ�Uri
     */
    private final Uri SMS_URI = Uri.parse("content://sms");
    private MainActivity activity;
    private ProgressDialog progressDialog;

    public SmsBackupTask( MainActivity activity )
    {
        this.activity = activity;
        progressDialog = new ProgressDialog(activity);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setTitle(activity
                .getString(R.string.sms_dialog_backup_title));
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
        for ( ThreadInfo threadInfo : params )
        {
            // ��ʾ������
            progressDialog.setMax((int) threadInfo.getNumber());
            progressDialog.setProgress(0);

            // ��ѯ�öԻ��Ķ���
            String[] selectionArgs =
            { threadInfo.getThreadID() };
            Cursor cursor = contentResolver.query(SMS_URI, null, "thread_id=?",
                    selectionArgs, "date DESC");
            if ( cursor != null )
            {
                ArrayList<SmsInfo> smsInfos = new ArrayList<SmsInfo>();
                try
                {
                    SmsInfo smsInfo;
                    int num = 1;
                    while (cursor.moveToNext())
                    {
                        onProgressUpdate(num);
                        smsInfo = new SmsInfo();
                        // address
                        smsInfo.setAddress(cursor.getString(cursor
                                .getColumnIndex("address")) + "");
                        // date
                        smsInfo.setDate(cursor.getString(cursor
                                .getColumnIndex("date")) + "");
                        // protocol
                        smsInfo.setProtocol(cursor.getString(cursor
                                .getColumnIndex("protocol")) + "");
                        // read
                        smsInfo.setRead(cursor.getString(cursor
                                .getColumnIndex("read")) + "");
                        // status
                        smsInfo.setStatus(cursor.getString(cursor
                                .getColumnIndex("status")) + "");
                        // type
                        smsInfo.setType(cursor.getString(cursor
                                .getColumnIndex("type")) + "");
                        // reply path present
                        smsInfo.setReplyPathPresent(cursor.getString(cursor
                                .getColumnIndex("reply_path_present")) + "");
                        // body
                        smsInfo.setBody(cursor.getString(cursor
                                .getColumnIndex("body")) + "");
                        // locked
                        smsInfo.setLocked(cursor.getString(cursor
                                .getColumnIndex("locked")) + "");
                        // error code
                        smsInfo.setErrorCode(cursor.getString(cursor
                                .getColumnIndex("error_code")) + "");
                        // seen
                        smsInfo.setSeen(cursor.getString(cursor
                                .getColumnIndex("seen")) + "");
                        smsInfos.add(smsInfo);
                        num++;
                    }

                }
                catch (IllegalArgumentException e)
                {
                    break;
                }
                catch (IllegalStateException e)
                {
                    break;
                }
                // д���ļ�
                try
                {
                    File file = new File(
                            Environment.getExternalStorageDirectory()
                                    + "/SimpleBackup/SMS/"
                                    + threadInfo.getAddress() + ".xml");
                    // �����ļ���
                    file.getParentFile().mkdirs();
                    FileOutputStream outputStream;
                    outputStream = new FileOutputStream(file);
                    OutputStreamWriter outputStreamWriter = new OutputStreamWriter(
                            outputStream, "UTF-8");
                    BufferedWriter writer = new BufferedWriter(
                            outputStreamWriter);
                    // д��XML
                    XmlUtil.writeSMStoXML(writer, smsInfos, threadInfo,
                            activity);
                    writer.flush();
                    writer.close();
                }
                catch (FileNotFoundException e)
                {
                    Toast.makeText(activity,
                            R.string.sms_dialog_file_not_found,
                            Toast.LENGTH_SHORT).show();
                }
                catch (UnsupportedEncodingException e)
                {

                }
                catch (IOException e)
                {

                }
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
        super.onPostExecute(result);
        progressDialog.dismiss();
    }
}