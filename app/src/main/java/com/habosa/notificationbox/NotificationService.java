package com.habosa.notificationbox;

import android.app.Notification;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

public class NotificationService extends NotificationListenerService {

    private static String TAG = "NotificationService";

    @Override
    public void onNotificationPosted(StatusBarNotification notification) {
        super.onNotificationPosted(notification);
        Log.d(TAG, "onNotificationPosted:" + notification.toString());

        Notification.Action[] actions = notification.getNotification().actions;
        if (actions != null) {
            for (Notification.Action action : actions) {
                Log.d(TAG, "action:" + action.title);
            }
        }
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification notification) {
        super.onNotificationRemoved(notification);
        Log.d(TAG, "onNotificationRemoved:" + notification.toString());
    }
}
