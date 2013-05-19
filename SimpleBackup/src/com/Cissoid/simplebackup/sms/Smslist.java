package com.cissoid.simplebackup.sms;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Hashtable;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.ContactsContract;

import com.cissoid.simplebackup.MainActivity;
import com.cissoid.simplebackup.util.XmlUtil;

public class Smslist
{
    /**
     * ��ȡ���ŵ�Uri
     */
    private final Uri SMS_URI = Uri.parse("content://sms");
    /**
     * ��ȡ�Ի���Uri
     */
    private final Uri THREADS_URI = Uri
            .parse("content://mms-sms/conversations?simple=true");
    /**
     * ��ȡ��ϵ�˵�Uri
     */
    private final Uri CONTACT_URI = ContactsContract.Data.CONTENT_URI;
    private MainActivity activity;

    /**
     * ����Ի���Ϣ���б�
     */
    private ArrayList<ThreadInfo> threadInfos;
    /**
     * �б��ѡ��־
     */
    private boolean isMultiselect;

    public Smslist( MainActivity activity )
    {
        this.activity = activity;
        threadInfos = getData();
    }

    public ArrayList<ThreadInfo> getData()
    {
        ContentResolver contentResolver = activity.getContentResolver();
        Hashtable<String, ThreadInfo> smsHashByAddress = new Hashtable<String, ThreadInfo>();
        Hashtable<String, ThreadInfo> smsHashByThreadID = new Hashtable<String, ThreadInfo>();
        // ��ȡ������Ϣ
        ArrayList<ThreadInfo> threadInfos = new ArrayList<ThreadInfo>();
        // ��sms��
        String[] projection = new String[]
        { "distinct thread_id", "address" };
        Cursor cursor = contentResolver.query(SMS_URI, projection, null, null,
                null);
        if ( cursor != null )
        {
            ThreadInfo threadInfo;
            while (cursor.moveToNext())
            {
                threadInfo = new ThreadInfo();
                threadInfo.setThreadID(cursor.getString(cursor
                        .getColumnIndex("thread_id")));
                threadInfo.setAddress(cursor.getString(cursor
                        .getColumnIndex("address")));
                threadInfos.add(threadInfo);
                smsHashByAddress.put(threadInfo.getAddress(), threadInfo);
                smsHashByThreadID.put(threadInfo.getThreadID(), threadInfo);
            }
            cursor.close();
        }
        // ��contacts��
        cursor = contentResolver.query(CONTACT_URI, null,
                "mimetype='vnd.android.cursor.item/phone_v2'", null, null);
        if ( cursor != null )
        {
            while (cursor.moveToNext())
            {
                String person = cursor.getString(cursor
                        .getColumnIndex("display_name"));
                String address = cursor.getString(cursor
                        .getColumnIndex("data1"));
                if ( smsHashByAddress.get(address) != null )
                    smsHashByAddress.get(address).setPerson(person);
            }
            cursor.close();
        }
        // ��threads��
        projection = new String[]
        { "_id", "message_count", "snippet" };
        cursor = contentResolver.query(THREADS_URI, projection, null, null,
                null);
        if ( cursor != null )
        {
            while (cursor.moveToNext())
            {
                String threadID = cursor
                        .getString(cursor.getColumnIndex("_id"));
                long count = cursor.getLong(cursor
                        .getColumnIndex("message_count"));
                String snippet = cursor.getString(cursor
                        .getColumnIndex("snippet"));
                if ( smsHashByThreadID.get(threadID) != null )
                {
                    smsHashByThreadID.get(threadID).setNumber(count);
                    smsHashByThreadID.get(threadID).setSnippet(snippet);
                }
            }
            cursor.close();
        }
        // �鱸����Ϣ
        ArrayList<String> paths = GetXmls();
        if ( paths != null )
            for ( String path : paths )
            {
                try
                {
                    File file = new File(path);
                    FileInputStream fileInputStream = new FileInputStream(file);
                    ThreadInfo threadInfo = XmlUtil
                            .readThreadInfo(fileInputStream);
                    if ( smsHashByAddress.get(threadInfo.getAddress()) != null )
                    {
                        smsHashByAddress.get(threadInfo.getAddress())
                                .setBakNumber(threadInfo.getNumber());
                        smsHashByAddress.get(threadInfo.getAddress())
                                .setBackupTime(threadInfo.getBackupTime());
                    }
                    else
                    {
                        threadInfos.add(threadInfo);
                    }
                }
                catch (FileNotFoundException e)
                {

                }
            }
        return threadInfos;
    }

    public ArrayList<String> GetXmls()
    {
        ArrayList<String> paths = new ArrayList<String>();
        File[] files = new File(Environment.getExternalStorageDirectory()
                + "/SimpleBackup/SMS").listFiles();
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
     * ��ȡ�Ի���Ŀ
     * 
     * @return �Ի���Ŀ
     */
    public int getCount()
    {
        return threadInfos.size();
    }

    /**
     * ����λ�û�ȡ�Ի�
     * 
     * @param position
     *            λ��
     * @return ��λ�õĶԻ�
     */
    public ThreadInfo getItem( int position )
    {
        return threadInfos.get(position);
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
