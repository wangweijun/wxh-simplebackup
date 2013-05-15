package com.Cissoid.simplebackup.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

import android.util.Xml;

import com.Cissoid.simplebackup.MainActivity;
import com.Cissoid.simplebackup.app.AppInfo;
import com.Cissoid.simplebackup.sms.SmsInfo;
import com.Cissoid.simplebackup.sms.ThreadInfo;
import com.wxhcn.simplebackup.R;

/**
 * 
 * @author Wxh
 * 
 */
public class XmlUtil
{
    /**
     * ��XML�л�ȡ������Ϣ
     * 
     * @param inStream
     *            �����FileInputStream
     * @param activity
     * @return
     */
    public static AppInfo readAppCfg( InputStream inStream ,
            MainActivity activity )
    {
        XmlPullParser parser = Xml.newPullParser();
        try
        {
            parser.setInput(inStream, "UTF-8");
            int eventType = parser.getEventType();
            AppInfo appInfo = null;
            while (eventType != XmlPullParser.END_DOCUMENT)
            {
                switch ( eventType )
                {
                case XmlPullParser.START_DOCUMENT :// �ĵ���ʼ�¼�,���Խ������ݳ�ʼ������
                    break;
                case XmlPullParser.START_TAG :// ��ʼԪ���¼�
                    String name = parser.getName();
                    if ( name.equalsIgnoreCase("app") )
                    {
                        appInfo = new AppInfo(activity);
                    }
                    else if ( appInfo != null )
                    {
                        // ������
                        if ( name.equalsIgnoreCase("name") )
                            appInfo.setName(EncryptionUtil.decrypt(
                                    parser.nextText(), EncryptionUtil.BASE64));
                        // ����
                        else if ( name.equalsIgnoreCase("package") )
                            appInfo.setPackageName(EncryptionUtil.decrypt(
                                    parser.nextText(), EncryptionUtil.BASE64));
                        // �汾
                        else if ( name.equalsIgnoreCase("version") )
                            appInfo.setVersion(EncryptionUtil.decrypt(
                                    parser.nextText(), EncryptionUtil.BASE64));
                        // �汾��
                        else if ( name.equalsIgnoreCase("versioncode") )
                            appInfo.setVersionCode(parser.next());
                        // ����ʱ��
                        else if ( name.equalsIgnoreCase("backuptime") )
                            appInfo.setBackupTime(EncryptionUtil.decrypt(
                                    parser.nextText(), EncryptionUtil.BASE64));
                        // ����ģʽ
                        else if ( name.equalsIgnoreCase("mode") )
                            appInfo.mode = Integer.parseInt(parser.nextText());
                    }
                    break;
                case XmlPullParser.END_TAG :// ����Ԫ���¼�
                    break;
                }
                eventType = parser.next();
            }
            inStream.close();
            appInfo.type = AppInfo.TYPE_SDCARD;
            return appInfo;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public static void writeAppCfg( Writer writer , AppInfo appInfo )
    {
        XmlSerializer serializer = Xml.newSerializer();
        try
        {
            serializer.setOutput(writer);
            serializer.startDocument("UTF-8", true);
            // ��һ������Ϊ�����ռ�,�����ʹ�������ռ�,��������Ϊnull

            serializer.startTag(null, "app");
            // ������
            serializer.startTag(null, "name");
            serializer.text(EncryptionUtil.encrypt(appInfo.getName(),
                    EncryptionUtil.BASE64));
            serializer.endTag(null, "name");
            // ����
            serializer.startTag(null, "package");
            serializer.text(EncryptionUtil.encrypt(appInfo.getPackageName(),
                    EncryptionUtil.BASE64));
            serializer.endTag(null, "package");
            // �汾
            serializer.startTag(null, "version");
            serializer.text(EncryptionUtil.encrypt(appInfo.getVersion(),
                    EncryptionUtil.BASE64));
            serializer.endTag(null, "version");
            // �汾��versionCode
            serializer.startTag(null, "versioncode");
            serializer.text(String.valueOf(appInfo.getVersionCode()));
            serializer.endTag(null, "versioncode");
            // ����ʱ��
            serializer.startTag(null, "backuptime");
            serializer.text(EncryptionUtil.encrypt(appInfo.getBackupTime(),
                    EncryptionUtil.BASE64));
            serializer.endTag(null, "backuptime");
            // ����ģʽ
            serializer.startTag(null, "mode");
            serializer.text(String.valueOf(appInfo.mode));
            serializer.endTag(null, "mode");
            serializer.endTag(null, "app");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static void writeSMStoXML( Writer writer ,
            ArrayList<SmsInfo> smsInfos , ThreadInfo threadInfo ,
            MainActivity activity )
    {
        XmlSerializer serializer = Xml.newSerializer();
        try
        {
            serializer.setOutput(writer);
            serializer.startDocument("UTF-8", true);

            serializer.startTag(null, "thread");
            serializer.attribute(null, "person", EncryptionUtil.encrypt(
                    threadInfo.getPerson(), EncryptionUtil.BASE64));
            serializer.attribute(null, "address", EncryptionUtil.encrypt(
                    threadInfo.getAddress(), EncryptionUtil.BASE64));
            serializer.attribute(null, "number",
                    String.valueOf(threadInfo.getNumber()));
            // ��¼����ʱ��
            String time = new SimpleDateFormat(":yyyy.MM.dd HH:mm")
                    .format(new Date(System.currentTimeMillis()));
            serializer.attribute(null, "backupTime", time);

            for ( SmsInfo smsInfo : smsInfos )
            {
                serializer.startTag(null, "sms");

                // address
                serializer.startTag(null, "address");
                serializer.text(EncryptionUtil.encrypt(smsInfo.getAddress(),
                        EncryptionUtil.BASE64));
                serializer.endTag(null, "address");

                // date
                serializer.startTag(null, "date");
                serializer.text(EncryptionUtil.encrypt(smsInfo.getDate(),
                        EncryptionUtil.BASE64));
                serializer.endTag(null, "date");

                // protocol
                serializer.startTag(null, "protocol");
                serializer.text(smsInfo.getProtocol());
                serializer.endTag(null, "protocol");

                // read
                serializer.startTag(null, "read");
                serializer.text(smsInfo.getRead());
                serializer.endTag(null, "read");

                // status
                serializer.startTag(null, "status");
                serializer.text(smsInfo.getStatus());
                serializer.endTag(null, "status");

                // type
                serializer.startTag(null, "type");
                serializer.text(smsInfo.getType());
                serializer.endTag(null, "type");

                // reply path present
                serializer.startTag(null, "reply_path_present");
                serializer.text(smsInfo.getReplyPathPresent());
                serializer.endTag(null, "reply_path_present");

                // body
                serializer.startTag(null, "body");
                serializer.text(EncryptionUtil.encrypt(smsInfo.getBody(),
                        EncryptionUtil.BASE64));
                serializer.endTag(null, "body");

                // locked
                serializer.startTag(null, "locked");
                serializer.text(smsInfo.getLocked());
                serializer.endTag(null, "locked");

                // error code
                serializer.startTag(null, "error_code");
                serializer.text(smsInfo.getErrorCode());
                serializer.endTag(null, "error_code");

                // seen
                serializer.startTag(null, "seen");
                serializer.text(smsInfo.getSeen());
                serializer.endTag(null, "seen");

                serializer.endTag(null, "sms");
            }
            serializer.endTag(null, "thread");
            serializer.endDocument();
        }
        catch (IllegalArgumentException e)
        {

        }
        catch (IllegalStateException e)
        {

        }
        catch (IOException e)
        {

        }
    }

    /**
     * ��XML��ϢתΪSmsInfo�б�
     * 
     * @param inputStream
     *            �����FileInputStream
     * @param activity
     * @return
     */
    public static ArrayList<SmsInfo> readSMSfromXML( InputStream inputStream )
    {
        ArrayList<SmsInfo> smsInfos = new ArrayList<SmsInfo>();
        XmlPullParser parser = Xml.newPullParser();
        try
        {
            SmsInfo smsInfo = null;
            parser.setInput(inputStream, "UTF-8");
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT)
            {
                switch ( eventType )
                {
                // �ĵ���ʼ��־
                case XmlPullParser.START_DOCUMENT :
                    break;
                // ��ǩ��ʼ
                case XmlPullParser.START_TAG :
                {
                    String name = parser.getName();
                    if ( name.equalsIgnoreCase("sms") )
                        smsInfo = new SmsInfo();
                    else
                    {
                        if ( name.equalsIgnoreCase("address") )
                            smsInfo.setAddress(EncryptionUtil.decrypt(
                                    parser.nextText(), EncryptionUtil.BASE64));
                        else if ( name.equalsIgnoreCase("date") )
                            smsInfo.setDate(EncryptionUtil.decrypt(
                                    parser.nextText(), EncryptionUtil.BASE64));
                        else if ( name.equalsIgnoreCase("protocol") )
                            smsInfo.setProtocol(parser.nextText());
                        else if ( name.equalsIgnoreCase("read") )
                            smsInfo.setRead(parser.nextText());
                        else if ( name.equalsIgnoreCase("status") )
                            smsInfo.setStatus(parser.nextText());
                        else if ( name.equalsIgnoreCase("type") )
                            smsInfo.setType(parser.nextText());
                        else if ( name.equalsIgnoreCase("reply_path_present") )
                            smsInfo.setReplyPathPresent(parser.nextText());
                        else if ( name.equalsIgnoreCase("body") )
                            smsInfo.setBody(EncryptionUtil.decrypt(
                                    parser.nextText(), EncryptionUtil.BASE64));
                        else if ( name.equalsIgnoreCase("locked") )
                            smsInfo.setLocked(parser.nextText());
                        else if ( name.equalsIgnoreCase("error_code") )
                            smsInfo.setErrorCode(parser.nextText());
                        else if ( name.equalsIgnoreCase("seen") )
                            smsInfo.setSeen(parser.nextText());
                    }
                    break;
                }
                // ��ǩ����
                case XmlPullParser.END_TAG :
                    String name = parser.getName();
                    if ( name.equalsIgnoreCase("sms") )
                        smsInfos.add(smsInfo);
                    break;
                }
                eventType = parser.next();
            }
        }
        catch (XmlPullParserException e)
        {

        }
        catch (IOException e)
        {

        }
        return smsInfos;
    }

    /**
     * ��ȡ�����ļ�������Ϣ
     * 
     * @param inputStream
     * @return
     */
    public static ThreadInfo readThreadInfo( InputStream inputStream )
    {
        ThreadInfo threadInfo = new ThreadInfo();
        try
        {
            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(inputStream, "UTF-8");
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT)
            {
                switch ( eventType )
                {
                case XmlPullParser.START_TAG :
                    String name = parser.getName();
                    if ( name.equalsIgnoreCase("thread") )
                    {
                        threadInfo.setPerson(EncryptionUtil.decrypt(
                                parser.getAttributeValue(0),
                                EncryptionUtil.BASE64));
                        threadInfo.setAddress(EncryptionUtil.decrypt(
                                parser.getAttributeValue(1),
                                EncryptionUtil.BASE64));
                        threadInfo.setNumber(Long.parseLong(parser
                                .getAttributeValue(2)));
                        threadInfo.setBackupTime(parser.getAttributeValue(3));
                    }
                    break;
                }
                eventType = parser.next();
            }
        }
        catch (XmlPullParserException e)
        {

        }
        catch (IOException e)
        {

        }
        return threadInfo;
    }
}
