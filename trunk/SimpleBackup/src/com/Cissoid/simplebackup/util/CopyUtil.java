package com.cissoid.simplebackup.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class CopyUtil
{
    static public boolean copyFile( String oldPath , String newPath )
    {
        File oldFile = new File(oldPath);
        File newFile = new File(newPath);
        newFile.getParentFile().mkdirs();
        if ( !oldFile.exists() || !oldFile.isFile() || !oldFile.canRead() )
            return false;
        try
        {
            FileInputStream fosfrom = new FileInputStream(oldFile);
            FileOutputStream fosto = new FileOutputStream(newFile);
            byte bt[] = new byte[1024];
            int c;
            while ((c = fosfrom.read(bt)) > 0)
            {
                fosto.write(bt, 0, c); // 将内容写到新文件当中
            }
            fosfrom.close();
            fosto.close();
            return true;
        }
        catch (Exception ex)
        {
            return false;
        }
    }

    static public boolean copyWithRoot( String from , String to )
    {
        String cmd = "busybox cp -r " + from + " " + to;
        return ShellUtil.RootCmd(cmd);
    }
}
