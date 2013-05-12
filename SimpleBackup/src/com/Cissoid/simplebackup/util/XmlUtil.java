package com.Cissoid.simplebackup.util;

import java.io.InputStream;
import java.io.Writer;

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
                case XmlPullParser.START_DOCUMENT :// 文档开始事件,可以进行数据初始化处理
                    break;
                case XmlPullParser.START_TAG :// 开始元素事件
                    String name = parser.getName();
                    if ( name.equalsIgnoreCase("app") )
                    {
                        appInfo = new AppInfo(activity);
                    }
                    else if ( appInfo != null )
                    {
                        // 程序名
                        if ( name.equalsIgnoreCase("name") )
                            appInfo.setName(EncryptionUtil.decrypt(
                                    parser.nextText(), EncryptionUtil.BASE64));
                        // 包名
                        else if ( name.equalsIgnoreCase("package") )
                            appInfo.setPackageName(EncryptionUtil.decrypt(
                                    parser.nextText(), EncryptionUtil.BASE64));
                        // 版本
                        else if ( name.equalsIgnoreCase("version") )
                            appInfo.setVersion(EncryptionUtil.decrypt(
                                    parser.nextText(), EncryptionUtil.BASE64));
                        // 版本号
                        else if ( name.equalsIgnoreCase("versioncode") )
                            appInfo.setVersionCode(parser.next());
                        // 备份时间
                        else if ( name.equalsIgnoreCase("backuptime") )
                            appInfo.setBackupTime(EncryptionUtil.decrypt(
                                    parser.nextText(), EncryptionUtil.BASE64));
                        // 备份模式
                        else if ( name.equalsIgnoreCase("mode") )
                            appInfo.mode = parser.next();
                    }
                    break;
                case XmlPullParser.END_TAG :// 结束元素事件
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

    public static void writeAppCfg( Writer writer , AppInfo... appInfos )
    {
        XmlSerializer serializer = Xml.newSerializer();
        try
        {
            serializer.setOutput(writer);
            serializer.startDocument("UTF-8", true);
            // 第一个参数为命名空间,如果不使用命名空间,可以设置为null
            for ( AppInfo appInfo : appInfos )
            {
                serializer.startTag(null, "app");
                // 程序名
                serializer.startTag(null, "name");
                serializer.text(EncryptionUtil.encrypt(appInfo.getName(),
                        EncryptionUtil.BASE64));
                serializer.endTag(null, "name");
                // 包名
                serializer.startTag(null, "package");
                serializer.text(EncryptionUtil.encrypt(
                        appInfo.getPackageName(), EncryptionUtil.BASE64));
                serializer.endTag(null, "package");
                // 版本
                serializer.startTag(null, "version");
                serializer.text(EncryptionUtil.encrypt(appInfo.getVersion(),
                        EncryptionUtil.BASE64));
                serializer.endTag(null, "version");
                // 版本号versionCode
                serializer.startTag(null, "versioncode");
                serializer.text(String.valueOf(appInfo.getVersionCode()));
                serializer.endTag(null, "versioncode");
                // 备份时间

                serializer.startTag(null, "backuptime");
                serializer.text(EncryptionUtil.encrypt(appInfo.getBackupTime(),
                        EncryptionUtil.BASE64));
                serializer.endTag(null, "backuptime");
                // 备份模式
                serializer.startTag(null, "mode");
                serializer.text(String.valueOf(appInfo.mode));
                serializer.endTag(null, "mode");
                serializer.endTag(null, "app");
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
