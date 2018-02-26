package com.habosa.notificationbox;

import android.app.Notification;
import android.os.Bundle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import com.habosa.notificationbox.data.AppDatabase;
import com.habosa.notificationbox.data.NotificationDao;
import com.habosa.notificationbox.model.NotificationInfo;
import com.habosa.notificationbox.util.BackgroundUtils;

public class NotificationService extends NotificationListenerService {

    private static final String TAG = "NotificationService";

    private NotificationDao mNotificationDao;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate");

        mNotificationDao = AppDatabase.getInstance(getApplicationContext()).notificationDao();
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

        // Create NotificationInfo
        NotificationInfo info = new NotificationInfo(notification);

        // Store the notification
        insert(info);

        // TODO: Cancel it
        // cancelNotification(notification.getKey());
    }

    private void insert(final NotificationInfo info) {
        // TODO: I need to do more sane thread management.
        BackgroundUtils.EXECUTOR.execute(new Runnable() {
            @Override
            public void run() {
                mNotificationDao.insert(info);
            }
        });
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

        // Ongoing media notification
        // TODO: This catches Play Music but not BeyondPod
        if (extras.containsKey(Notification.EXTRA_MEDIA_SESSION)) {
            return false;
        }

        return true;
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification notification) {
        super.onNotificationRemoved(notification);
        Log.d(TAG, "onNotificationRemoved:" + notification.toString());
    }
}
