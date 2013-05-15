package com.Cissoid.simplebackup.sms;

/**
 * �洢ÿһ���Ի���Bean�࣬�������б�����ʾ
 * 
 * @author Wxh
 * 
 */
public class ThreadInfo
{
    /**
     * �Ի�ID
     */
    private String threadID = "";
    /**
     * �绰��
     */
    private String address = "";
    /**
     * ��ϵ������
     */
    private String person = "";

    /**
     * ��������
     */
    private long number = 0;
    /**
     * ���б��ݣ���Ϊ���ݵ���Ŀ
     */
    private long bakNumber = 0;
    /**
     * ����һ����������
     */
    private String snippet = "";

    /**
     * Ҫ��ʾ�ı�����Ϣ
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
