package com.Cissoid.simplebackup;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat.Builder;

public class SimpleBackupApplication extends Application
{
    /**
     * 线程池
     */
    private ExecutorService executorService = Executors.newFixedThreadPool(10);
    /**
     * root标志
     */
    private boolean hasRoot = false;
    /**
     * busybox标志
     */
    private boolean hasBusybox = false;

    private boolean hasSDCard = false;

    public void showNotification( final int icon , final CharSequence ticker ,
            final CharSequence contentTitle , final CharSequence contentText )
    {
        // 添加状态栏通知
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Intent intent = new Intent(this, MainActivity.class);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                intent, 0);
        Notification notification = new Builder(this).setTicker(ticker)
                .setContentTitle(contentTitle).setContentText(contentText)
                .setSmallIcon(icon).setWhen(System.currentTimeMillis())
                .setContentIntent(pendingIntent).build();
        notification.flags = Notification.FLAG_NO_CLEAR;
        notificationManager.notify(1, notification);
    }

    // 以下为需要用到的getter，setter

    /**
     * @return the executorService
     */
    public ExecutorService getExecutorService()
    {
        return executorService;
    }

    /**
     * @return the hasRoot
     */

    public boolean isRoot()
    {
        return hasRoot;
    }

    /**
     * @param hasRoot
     *            the isRoot to set
     */
    public void setRoot( boolean hasRoot )
    {
        this.hasRoot = hasRoot;
    }

    /**
     * @return the hasBusybox
     */
    public boolean hasBusybox()
    {
        return hasBusybox;
    }

    /**
     * @param hasBusybox
     *            the hasBusybox to set
     */
    public void setBusybox( boolean hasBusybox )
    {
        this.hasBusybox = hasBusybox;
    }

    /**
     * @return the hasSDCard
     */
    public boolean hasSDCard()
    {
        return hasSDCard;
    }

    /**
     * @param hasSDCard
     *            the hasSDCard to set
     */
    public void setSDCard( boolean hasSDCard )
    {
        this.hasSDCard = hasSDCard;
    }

    public boolean canBackupData()
    {
        return hasRoot && hasBusybox;
    }
}
