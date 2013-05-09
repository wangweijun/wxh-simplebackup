package com.Cissoid.simplebackup;

import android.app.Application;

public class SimpleBackupApplication extends Application
{
    private boolean isRoot = false;

    /**
     * @return the isRoot
     */
    public boolean isRoot()
    {
        return isRoot;
    }

    /**
     * @param isRoot
     *            the isRoot to set
     */
    public void setRoot(boolean isRoot)
    {
        this.isRoot = isRoot;
    }

}
