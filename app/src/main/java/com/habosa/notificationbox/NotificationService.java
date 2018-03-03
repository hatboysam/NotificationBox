package com.habosa.notificationbox;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationCompatExtras;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.habosa.notificationbox.data.AppDatabase;
import com.habosa.notificationbox.data.NotificationDao;
import com.habosa.notificationbox.model.NotificationInfo;
import com.habosa.notificationbox.notifications.NotificationActionCache;
import com.habosa.notificationbox.util.BackgroundUtils;
import com.habosa.notificationbox.util.PreferenceUtils;

public class NotificationService extends NotificationListenerService {

    private static final String TAG = "NotificationService";

    private static final int SUMMARY_NOTIF_ID = 101;

    private static final String SUMMARY_CHANNEL_ID = "summary";
    private static final String SUMMARY_CHANNEL_NAME = "Summary";
    private static final String SUMMARY_CHANNEL_DESC = "Summary of inbox";

    private NotificationDao mNotificationDao;
    private PreferenceUtils mPrefs;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate");

        mNotificationDao = AppDatabase.getInstance(getApplicationContext()).notificationDao();
        mPrefs = new PreferenceUtils(this);
    }

    @Override
    public void onListenerConnected() {
        super.onListenerConnected();
        Log.d(TAG, "onListenerConnected");
    }

    @Override
    public void onListenerDisconnected() {
        super.onListenerDisconnected();
        Log.d(TAG, "onListenerDisconnected");
    }

    @Override
    public void onNotificationPosted(StatusBarNotification notification) {
        super.onNotificationPosted(notification);
        Log.d(TAG, "onNotificationPosted:" + notification.toString());

        if (!shouldKeepNotification(notification)) {
            Log.d(TAG, "onNotificationPosted: ignoring.");
            return;
        }

        Log.d(TAG, "Intercepting notification:  " + notification.toString());

        // Create NotificationInfo
        NotificationInfo info = new NotificationInfo(notification);

        // Store the notification
        insert(info, notification);

        // Cancel the notification, it's ours now
        cancelNotification(notification.getKey());

        // Show a notification
        showSummaryNotification();
        // TODO: Send a broadcast here to force the app to reload if it's open
        // TODO: We should have a long-standing notification showing how many things we have stored
    }

    private void insert(final NotificationInfo info, final StatusBarNotification sbn) {
        // TODO: I need to do more sane thread management.
        BackgroundUtils.EXECUTOR.execute(new Runnable() {
            @Override
            public void run() {
                mNotificationDao.insert(info);
                NotificationActionCache.put(info, sbn.getNotification().contentIntent);
            }
        });
    }

    private void showSummaryNotification() {
        NotificationManagerCompat manager = NotificationManagerCompat.from(this);

        // Create channel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager sysManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            NotificationChannel channel = new NotificationChannel(
                    SUMMARY_CHANNEL_ID,
                    SUMMARY_CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT);

            channel.setDescription(SUMMARY_CHANNEL_DESC);

            sysManager.createNotificationChannel(channel);
        }

        Intent homeIntent = new Intent(this, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(
                this, 0, homeIntent, 0);

        // TODO: show count and some example names
        Notification notification = new NotificationCompat.Builder(this, SUMMARY_CHANNEL_ID)
                .setChannelId(SUMMARY_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notifications_off_white_24dp)
                .setContentTitle("You have notifications!")
                .setContentText("Open Notification Box to see them")
                .setContentIntent(contentIntent)
                .setAutoCancel(true)
                .build();

        manager.notify(SUMMARY_NOTIF_ID, notification);
    }

    private boolean shouldKeepNotification(StatusBarNotification notification) {
        Bundle extras = notification.getNotification().extras;

        Log.d(TAG, "Keys for :" + notification);
        for (String key : extras.keySet()) {
            Log.d(TAG, "\t" + key);
        }

        // Ongoing notification like USB debugging, non dismissable
        if (notification.isOngoing()) {
            return false;
        }

        // Ignore "group summary" notifications
        if (extras.getBoolean(NotificationCompatExtras.EXTRA_GROUP_SUMMARY, false)) {
            return false;
        }

        // Ongoing media notification
        // TODO: This catches Play Music but not BeyondPod
        if (extras.containsKey(Notification.EXTRA_MEDIA_SESSION)) {
            return false;
        }

        // Check if we are watching this application
        return mPrefs.getAppSelected(notification.getPackageName());
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification notification) {
        super.onNotificationRemoved(notification);
        Log.d(TAG, "onNotificationRemoved:" + notification.toString());
    }
}
