package com.Cissoid.simplebackup.util;

import java.io.DataOutputStream;
import java.io.IOException;

public class ShellUtil
{
    static public boolean Cmd(String cmd)
    {
        // StringBuilder stringBuilder = null;
        try
        {
            Runtime.getRuntime().exec(cmd);
            // BufferedReader bufferedReader = new BufferedReader(
            // new InputStreamReader(process.getInputStream()));
            //
            // String line = "";
            // stringBuilder = new StringBuilder(line);
            // while ((line = bufferedReader.readLine()) != null)
            // {
            // stringBuilder.append(line);
            // stringBuilder.append("\n");
            // }
        }
        catch (IOException e)
        {
            return false;
        }
        return true;
    }

    /**
     * root权限执行命令
     * 
     * @param cmd
     *            命令
     * @return 成功：true 失败：false
     */
    static public boolean RootCmd(String cmd)
    {
        Process process = null;
        DataOutputStream os = null;
        try
        {
            process = Runtime.getRuntime().exec("su");
            os = new DataOutputStream(process.getOutputStream());
            os.writeBytes(cmd + "\n");
            os.writeBytes("exit\n");
            os.flush();
            process.waitFor();
        }
        catch (Exception e)
        {
            return false;
        }
        finally
        {
            try
            {
                if (os != null)
                {
                    os.close();
                }
                process.destroy();
            }
            catch (Exception e)
            {
            }
        }
        return true;
    }
}
