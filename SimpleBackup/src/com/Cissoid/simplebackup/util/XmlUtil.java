package com.Cissoid.simplebackup.util;

import java.io.InputStream;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlSerializer;

import android.util.Xml;

import com.Cissoid.simplebackup.MainActivity;
import com.Cissoid.simplebackup.app.AppInfo;

/**
 * 
 * @author Wxh
 * 
 */
public class XmlUtil
{
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
                            appInfo.setName(parser.nextText());
                        // ����
                        else if ( name.equalsIgnoreCase("package") )
                            appInfo.setPackageName(parser.nextText());
                        // �汾
                        else if ( name.equalsIgnoreCase("version") )
                            appInfo.setVersion(parser.nextText());
                        // �汾��
                        else if ( name.equalsIgnoreCase("versioncode") )
                            appInfo.setVersionCode(parser.next());
                        // ����ʱ��
                        else if ( name.equalsIgnoreCase("backuptime") )
                            appInfo.setBackupTime(parser.nextText());
                        // ����ģʽ
                        else if ( name.equalsIgnoreCase("mode") )
                            appInfo.mode = parser.next();
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

    public static String writeAppCfg( Writer writer , AppInfo... appInfos )
    {
        XmlSerializer serializer = Xml.newSerializer();
        try
        {
            serializer.setOutput(writer);
            serializer.startDocument("UTF-8", true);
            // ��һ������Ϊ�����ռ�,�����ʹ�������ռ�,��������Ϊnull
            for ( AppInfo appInfo : appInfos )
            {
                serializer.startTag(null, "app");
                // ������
                serializer.startTag(null, "name");
                serializer.text(appInfo.getName());
                serializer.endTag(null, "name");
                // ����
                serializer.startTag(null, "package");
                serializer.text(appInfo.getPackageName());
                serializer.endTag(null, "package");
                // �汾
                serializer.startTag(null, "version");
                serializer.text(appInfo.getVersion());
                serializer.endTag(null, "version");
                // �汾��versionCode
                serializer.startTag(null, "versioncode");
                serializer.text(String.valueOf(appInfo.getVersionCode()));
                serializer.endTag(null, "versioncode");
                // ����ʱ��
                SimpleDateFormat formatter = new SimpleDateFormat(
                        "yyyy��MM��dd��  HH:mm:ss");
                Date curDate = new Date(System.currentTimeMillis());
                String time = formatter.format(curDate);
                serializer.startTag(null, "backuptime");
                serializer.text(time);
                serializer.endTag(null, "backuptime");
                // ����ģʽ
                serializer.startTag(null, "mode");
                serializer.text(appInfo.getPackageName());
                serializer.endTag(null, "mode");
                serializer.endTag(null, "app");
            }
            serializer.endDocument();
            return writer.toString();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }
}
