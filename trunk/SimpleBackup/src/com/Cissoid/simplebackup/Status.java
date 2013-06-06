package com.Cissoid.simplebackup;

public class Status
{
    private boolean root = false;
    private boolean busybox = false;
    private boolean sdcard = false;
    private boolean bae = false;

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
