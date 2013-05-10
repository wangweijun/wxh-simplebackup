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
                case XmlPullParser.START_DOCUMENT :// �ĵ���ʼ�¼�,���Խ������ݳ�ʼ������
                    appInfos = new ArrayList<AppInfo>();
                    break;
                case XmlPullParser.START_TAG :// ��ʼԪ���¼�
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
                            currentPerson.setName(parser.nextText());// ���������TextԪ��,����������ֵ
                        }
                        else if ( name.equalsIgnoreCase("age") )
                        {
                            currentPerson.setAge(new Short(parser.nextText()));
                        }
                    }
                    break;
                case XmlPullParser.END_TAG :// ����Ԫ���¼�
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
