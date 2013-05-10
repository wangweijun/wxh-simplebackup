package com.Cissoid.simplebackup.util;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;

import android.util.Xml;

import com.Cissoid.simplebackup.app.AppInfo;

/**
 * 
 * @author Wxh
 * 
 */
public class XmlUtil
{
    public static ArrayList<AppInfo> readXML( InputStream inStream )
    {
        XmlPullParser parser = Xml.newPullParser();
        try
        {
            parser.setInput(inStream, "UTF-8");
            int eventType = parser.getEventType();
            AppInfo currentApp = null;
            ArrayList<AppInfo> appInfos = null;
            while (eventType != XmlPullParser.END_DOCUMENT)
            {
                switch ( eventType )
                {
                case XmlPullParser.START_DOCUMENT :// 文档开始事件,可以进行数据初始化处理
                    appInfos = new ArrayList<AppInfo>();
                    break;
                case XmlPullParser.START_TAG :// 开始元素事件
                    String name = parser.getName();
                    if ( name.equalsIgnoreCase("app") )
                    {
                        currentApp = new AppInfo();
                        currentApp.setId(Integer.valueOf(parser
                                .getAttributeValue(null, "id")));
                    }
                    else if ( currentApp != null )
                    {
                        if ( name.equalsIgnoreCase("name") )
                        {
                            currentPerson.setName(parser.nextText());// 如果后面是Text元素,即返回它的值
                        }
                        else if ( name.equalsIgnoreCase("age") )
                        {
                            currentPerson.setAge(new Short(parser.nextText()));
                        }
                    }
                    break;
                case XmlPullParser.END_TAG :// 结束元素事件
                    if ( parser.getName().equalsIgnoreCase("person")
                            && currentPerson != null )
                    {
                        persons.add(currentPerson);
                        currentPerson = null;
                    }
                    break;
                }
                eventType = parser.next();
            }
            inStream.close();
            return persons;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }
}
