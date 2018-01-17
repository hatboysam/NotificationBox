package com.habosa.notificationbox;

import android.app.Notification;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.habosa.notificationbox.messages.MessageReceiver;
import com.habosa.notificationbox.messages.MessageSender;
import com.habosa.notificationbox.messages.NotificationRequest;
import com.habosa.notificationbox.messages.NotificationResult;

public class NotificationService extends NotificationListenerService {

    private static String TAG = "NotificationService";
    private MessageReceiver<NotificationRequest> mReceiver =
            new MessageReceiver<NotificationRequest>(NotificationRequest.class) {
                @Override
                public void onMessage(NotificationRequest message) {
                    onMessagesRequested();
                }
            };

    @Override
    public void onListenerConnected() {
        super.onListenerConnected();
        LocalBroadcastManager.getInstance(this)
                .registerReceiver(mReceiver, MessageReceiver.getFilter());
    }

    @Override
    public void onListenerDisconnected() {
        super.onListenerDisconnected();
        LocalBroadcastManager.getInstance(this)
                .unregisterReceiver(mReceiver);
    }

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

        // Send the notification
        MessageSender.sendMessage(this, new NotificationResult(notification));

        // TODO: Cancel it
        // cancelNotification(notification.getKey());
    }

    private void onMessagesRequested() {
        for (StatusBarNotification sbn : getActiveNotifications()) {
            MessageSender.sendMessage(this, new NotificationResult(sbn));
        }
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification notification) {
        super.onNotificationRemoved(notification);
        Log.d(TAG, "onNotificationRemoved:" + notification.toString());
    }
}
