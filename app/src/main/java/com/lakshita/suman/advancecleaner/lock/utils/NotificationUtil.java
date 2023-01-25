package com.lakshita.suman.advancecleaner.lock.utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;

import androidx.annotation.RequiresApi;

import com.lakshita.suman.advancecleaner.R;
import com.lakshita.suman.advancecleaner.lock.activities.main.MainLockActivity;

@RequiresApi(api = Build.VERSION_CODES.O)
public class NotificationUtil {

    private static final String NOTIFICATION_CHANNEL_ID = "10101";


    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void createNotification(Service mContext) {
        Intent resultIntent = new Intent(mContext, MainLockActivity.class);
        resultIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent resultPendingIntent;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            resultPendingIntent = PendingIntent.getActivity(mContext, 112 /* Request code */, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        } else {
            resultPendingIntent = PendingIntent.getActivity(mContext, 112 /* Request code */, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        }
        NotificationManager mNotificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);

        int importance = NotificationManager.IMPORTANCE_LOW;
        NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, mContext.getString(R.string.app_background_task), importance);
        mNotificationManager.createNotificationChannel(notificationChannel);

        Notification.Builder mBuilder = new Notification.Builder(mContext);
        mBuilder.setSmallIcon(android.R.color.transparent);
        mBuilder.setChannelId(NOTIFICATION_CHANNEL_ID);
        mBuilder
                .setContentText(mContext.getString(R.string.app_runing_background))
                .setAutoCancel(false)
                .setContentIntent(resultPendingIntent);
        mContext.startForeground(145, mBuilder.build());

        new Handler().postDelayed(() -> {
            if (mNotificationManager != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    mNotificationManager.deleteNotificationChannel(NOTIFICATION_CHANNEL_ID);
                }
                mNotificationManager.cancel(145);
            }
        }, 300);
    }

    public static void cancelNotification(Service mContext) {
        NotificationManager mNotificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.cancel(145);
    }


}