package com.habosa.notificationbox;

import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.habosa.notificationbox.messages.MessageReceiver;
import com.habosa.notificationbox.messages.MessageSender;
import com.habosa.notificationbox.messages.NotificationRequest;
import com.habosa.notificationbox.messages.NotificationResult;

public class NotificationService extends NotificationListenerService {

    private static final String TAG = "NotificationService";

    private MessageReceiver<NotificationRequest> mReceiver =
            new MessageReceiver<NotificationRequest>(NotificationRequest.class) {
                @Override
                public void onMessage(NotificationRequest message) {
                    onMessagesRequested();
                }
            };

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate");
    }

    @Override
    public void onListenerConnected() {
        super.onListenerConnected();

        Log.d(TAG, "onListenerConnected");
        LocalBroadcastManager.getInstance(this)
                .registerReceiver(mReceiver, MessageReceiver.getFilter());
    }

    @Override
    public void onListenerDisconnected() {
        super.onListenerDisconnected();

        Log.d(TAG, "onListenerDisconnected");
        LocalBroadcastManager.getInstance(this)
                .unregisterReceiver(mReceiver);
    }

    @Override
    public void onNotificationPosted(StatusBarNotification notification) {
        super.onNotificationPosted(notification);
        Log.d(TAG, "onNotificationPosted:" + notification.toString());

        if (!shouldKeepNotification(notification)) {
            Log.d(TAG, "onNotificationPosted: ignoring.");
            return;
        }

        // Send the notification
        MessageSender.send(this, new NotificationResult(notification));

        // TODO: Cancel it
        // cancelNotification(notification.getKey());
    }

    private boolean shouldKeepNotification(StatusBarNotification notification) {
        // TODO: Why does this not ignore music?
        if (notification.isOngoing()) {
            return false;
        }

        return true;
    }

    private void onMessagesRequested() {
        Log.d(TAG, "onMessagesRequested");
        for (StatusBarNotification sbn : getActiveNotifications()) {
            if (shouldKeepNotification(sbn)) {
                MessageSender.send(this, new NotificationResult(sbn));
            }

        }
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification notification) {
        super.onNotificationRemoved(notification);
        Log.d(TAG, "onNotificationRemoved:" + notification.toString());
    }
}
