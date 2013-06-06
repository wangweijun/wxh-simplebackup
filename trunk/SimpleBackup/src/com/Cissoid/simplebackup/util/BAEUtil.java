package com.Cissoid.simplebackup.util;

import android.content.ContentValues;
import android.os.Environment;

import com.Cissoid.simplebackup.MainActivity;
import com.Cissoid.simplebackup.home.HomePageFragment;
import com.Cissoid.simplebackup.sms.ThreadInfo;
import com.baidu.oauth.BaiduOAuth;
import com.baidu.oauth.BaiduOAuth.BaiduOAuthResponse;
import com.baidu.pcs.BaiduPCSClient;
import com.baidu.pcs.BaiduPCSStatusListener;

public class BAEUtil
{
    private final static String mbApiKey = "6yw5ukARxUWE32QqzIRvUj6x";
    private final static String mbRootPath = "/apps/SimpleBackup";

    public static void login( final HomePageFragment fragment )
    {

        BaiduOAuth oauthClient = new BaiduOAuth();
        oauthClient.startOAuth(fragment.getActivity(), mbApiKey,
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
                            DBUtil.insert(
                                    (MainActivity) fragment.getActivity(),
                                    values);
                            mkdir((MainActivity) fragment.getActivity());
                            fragment.refresh(true);
                        }
                    }

                    @Override
                    public void onCancel()
                    {

                    }
                });
    }

    private static void mkdir( MainActivity activity )
    {
        final String mbOauth = DBUtil.query(activity);
        if ( mbOauth.length() != 0 )
        {
            Thread workThread = new Thread(new Runnable()
            {
                public void run()
                {

                    BaiduPCSClient api = new BaiduPCSClient();
                    api.setAccessToken(mbOauth);
                    String path = mbRootPath + "/" + "SMS";
                    // final BaiduPCSActionInfo.PCSFileInfoResponse ret =
                    api.makeDir(path);
                }
            });

            workThread.start();
        }
    }

    public static void upload( MainActivity activity ,
            final ThreadInfo threadInfo )
    {
        final String mbOauth = DBUtil.query(activity);
        if ( mbOauth.length() != 0 )
        {
            Thread workThread = new Thread(new Runnable()
            {
                public void run()
                {
                    BaiduPCSClient api = new BaiduPCSClient();
                    api.setAccessToken(mbOauth);
                    String tmpFile = Environment.getExternalStorageDirectory()
                            + "/SimpleBackup/SMS/" + threadInfo.getAddress()
                            + ".xml";
                    api.deleteFile(mbRootPath + "/SMS/"
                            + threadInfo.getAddress() + ".xml");
                    api.uploadFile(tmpFile,
                            mbRootPath + "/SMS/" + threadInfo.getAddress()
                                    + ".xml", new BaiduPCSStatusListener()
                            {
                                @Override
                                public void onProgress( long bytes , long total )
                                {
                                    // TODO Auto-generated method stub

                                    final long bs = bytes;
                                    final long tl = total;
                                }

                                @Override
                                public long progressInterval()
                                {
                                    return 1000;
                                }
                            });
                }
            });
            workThread.start();
        }
    }

    public static void logout( final HomePageFragment fragment )
    {
        final String mbOauth = DBUtil.query((MainActivity) fragment
                .getActivity());
        if ( mbOauth.length() != 0 )
        {
            Thread workThread = new Thread(new Runnable()
            {
                @Override
                public void run()
                {
                    BaiduOAuth oauth = new BaiduOAuth();
                    oauth.logout(mbOauth);
                    DBUtil.delete((MainActivity) fragment.getActivity());
                }
            });
            workThread.start();
            fragment.refresh(true);
        }
    }
}
