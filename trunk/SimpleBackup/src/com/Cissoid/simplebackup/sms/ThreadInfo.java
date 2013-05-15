package com.Cissoid.simplebackup.sms;

/**
 * 存储每一个对话的Bean类，用于在列表中显示
 * 
 * @author Wxh
 * 
 */
public class ThreadInfo
{
    /**
     * 对话ID
     */
    private String threadID = "";
    /**
     * 电话号
     */
    private String address = "";
    /**
     * 联系人名字
     */
    private String person = "";

    /**
     * 短信条数
     */
    private long number = 0;
    /**
     * 若有备份，则为备份的数目
     */
    private long bakNumber = 0;
    /**
     * 最新一条短信内容
     */
    private String snippet = "";

    /**
     * 要显示的备份信息
     */
    private String backupTime = null;

    /**
     * @return the threadID
     */
    public String getThreadID()
    {
        return threadID;
    }

    /**
     * @param threadID
     *            the threadID to set
     */
    public void setThreadID( String threadID )
    {
        this.threadID = threadID;
    }

    /**
     * @return the address
     */
    public String getAddress()
    {
        return address;
    }

    /**
     * @param address
     *            the address to set
     */
    public void setAddress( String address )
    {
        this.address = address;
    }

    /**
     * @return the person
     */
    public String getPerson()
    {
        return person;
    }

    /**
     * @param person
     *            the person to set
     */
    public void setPerson( String person )
    {
        this.person = person;
    }

    /**
     * @return the number
     */
    public long getNumber()
    {
        return number;
    }

    /**
     * @param number
     *            the number to set
     */
    public void setNumber( long number )
    {
        this.number = number;
    }

    /**
     * @return the snippet
     */
    public String getSnippet()
    {
        if ( snippet.length() >= 15 )
            return snippet.substring(0, 15) + "...";
        else
            return snippet;
    }

    /**
     * @param snippet
     *            the snippet to set
     */
    public void setSnippet( String snippet )
    {
        this.snippet = snippet;
    }

    /**
     * @return the backupTime
     */
    public String getBackupTime()
    {
        return backupTime;
    }

    /**
     * @param backupTime
     *            the backupTime to set
     */
    public void setBackupTime( String backupTime )
    {
        this.backupTime = backupTime;
    }

    /**
     * @return the bakNumber
     */
    public long getBakNumber()
    {
        return bakNumber;
    }

    /**
     * @param bakNumber
     *            the bakNumber to set
     */
    public void setBakNumber( long bakNumber )
    {
        this.bakNumber = bakNumber;
    }
}
