package com.habosa.notificationbox;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.service.notification.StatusBarNotification;
import android.support.annotation.NonNull;

/**
 * Created by samstern on 1/15/18.
 */
public abstract class NotificationReceiver extends BroadcastReceiver {

    public static final String ACTION = "action_notification_receiver";

    private static final String KEY_STATUS_BAR_NOTIFICATION = "key_status_bar_notification";

    @NonNull
    public static Intent getBroadcastIntent(@NonNull StatusBarNotification notification) {
        Intent intent = new Intent();
        intent.setAction(ACTION);
        intent.putExtra(KEY_STATUS_BAR_NOTIFICATION, notification);
        return intent;
    }

    @NonNull
    public static IntentFilter getIntentFilter() {
        return new IntentFilter(ACTION);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (!intent.hasExtra(KEY_STATUS_BAR_NOTIFICATION)) {
            return;
        }

        StatusBarNotification notification = intent.getParcelableExtra(KEY_STATUS_BAR_NOTIFICATION);
        onNotification(notification);
    }

    public abstract void onNotification(StatusBarNotification notification);

}
