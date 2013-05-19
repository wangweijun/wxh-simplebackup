package com.Cissoid.simplebackup.util;

import android.content.ContentValues;

import com.Cissoid.simplebackup.MainActivity;
import com.baidu.oauth.BaiduOAuth;
import com.baidu.oauth.BaiduOAuth.BaiduOAuthResponse;

public class BAEUtil
{
    private final static String mbApiKey = "6yw5ukARxUWE32QqzIRvUj6x";
    private final static String mbRootPath = "/apps/SimpleBackup";

    public static void login( final MainActivity activity )
    {

        BaiduOAuth oauthClient = new BaiduOAuth();
        oauthClient.startOAuth(activity, mbApiKey,
                new BaiduOAuth.OAuthListener()
                {
                    @Override
                    public void onException( String msg )
                    {

                    }

                    @Override
                    public void onComplete( BaiduOAuthResponse response )
                    {
                        if ( null != response )
                        {
                            ContentValues values = new ContentValues();
                            values.put("token", response.getAccessToken());
                            DBUtil.insert(values);
                        }
                    }

                    @Override
                    public void onCancel()
                    {

                    }
                });
    }
}
