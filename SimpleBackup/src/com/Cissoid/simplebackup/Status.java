package com.Cissoid.simplebackup;

import java.io.Serializable;

public class Status
{
    private static final long serialVersionUID = 1L;
    private boolean root = true;
    private boolean busybox = true;
    private boolean sdcard = true;
    private boolean bae = true;

    /**
     * @return the root
     */
    public boolean isRoot()
    {
        return root;
    }

    /**
     * @param root
     *            the root to set
     */
    public void setRoot( boolean root )
    {
        this.root = root;
    }

    /**
     * @return the busybox
     */
    public boolean isBusybox()
    {
        return busybox;
    }

    /**
     * @param busybox
     *            the busybox to set
     */
    public void setBusybox( boolean busybox )
    {
        this.busybox = busybox;
    }

    /**
     * @return the sdcard
     */
    public boolean isSdcard()
    {
        return sdcard;
    }

    /**
     * @param sdcard
     *            the sdcard to set
     */
    public void setSdcard( boolean sdcard )
    {
        this.sdcard = sdcard;
    }

    /**
     * @return the bae
     */
    public boolean isBae()
    {
        return bae;
    }

    /**
     * @param bae
     *            the bae to set
     */
    public void setBae( boolean bae )
    {
        this.bae = bae;
    }
}
