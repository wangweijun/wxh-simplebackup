package com.cissoid.simplebackup.sms;

public class SmsInfo
{
    /**
     * 手机号
     */
    private String address;
    /**
     * 发送日期
     */
    private String date;
    /**
     * 送达日期
     */
    private String date_sent;
    /**
     * 协议
     */
    private String protocol;
    /**
     * 已读标记
     */
    private String read;

    private String status;
    private String type;
    private String replyPathPresent;
    /**
     * 短信内容
     */
    private String body;
    private String locked;
    private String errorCode;
    /**
     * 已读标志
     */
    private String seen;

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
     * @return the date
     */
    public String getDate()
    {
        return date;
    }

    /**
     * @param date
     *            the date to set
     */
    public void setDate( String date )
    {
        this.date = date;
    }

    /**
     * @return the protocol
     */
    public String getProtocol()
    {
        return protocol;
    }

    /**
     * @param protocol
     *            the protocol to set
     */
    public void setProtocol( String protocol )
    {
        this.protocol = protocol;
    }

    /**
     * @return the read
     */
    public String getRead()
    {
        return read;
    }

    /**
     * @param read
     *            the read to set
     */
    public void setRead( String read )
    {
        this.read = read;
    }

    /**
     * @return the status
     */
    public String getStatus()
    {
        return status;
    }

    /**
     * @param status
     *            the status to set
     */
    public void setStatus( String status )
    {
        this.status = status;
    }

    /**
     * @return the type
     */
    public String getType()
    {
        return type;
    }

    /**
     * @param type
     *            the type to set
     */
    public void setType( String type )
    {
        this.type = type;
    }

    /**
     * @return the replyPathPresent
     */
    public String getReplyPathPresent()
    {
        return replyPathPresent;
    }

    /**
     * @param replyPathPresent
     *            the replyPathPresent to set
     */
    public void setReplyPathPresent( String replyPathPresent )
    {
        this.replyPathPresent = replyPathPresent;
    }

    /**
     * @return the body
     */
    public String getBody()
    {
        return body;
    }

    /**
     * @param body
     *            the body to set
     */
    public void setBody( String body )
    {
        this.body = body;
    }

    /**
     * @return the locked
     */
    public String getLocked()
    {
        return locked;
    }

    /**
     * @param locked
     *            the locked to set
     */
    public void setLocked( String locked )
    {
        this.locked = locked;
    }

    /**
     * @return the errorCode
     */
    public String getErrorCode()
    {
        return errorCode;
    }

    /**
     * @param errorCode
     *            the errorCode to set
     */
    public void setErrorCode( String errorCode )
    {
        this.errorCode = errorCode;
    }

    /**
     * @return the seen
     */
    public String getSeen()
    {
        return seen;
    }

    /**
     * @param seen
     *            the seen to set
     */
    public void setSeen( String seen )
    {
        this.seen = seen;
    }

    /**
     * @return the date_sent
     */
    public String getDate_sent()
    {
        return date_sent;
    }

    /**
     * @param date_sent
     *            the date_sent to set
     */
    public void setDate_sent( String date_sent )
    {
        this.date_sent = date_sent;
    }
}
