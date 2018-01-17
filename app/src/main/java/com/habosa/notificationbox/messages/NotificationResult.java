package com.habosa.notificationbox.messages;

import android.os.Bundle;
import android.service.notification.StatusBarNotification;

/**
 * Created by samstern on 1/16/18.
 */
public class NotificationResult extends Message {

    private static final String ID = "NotificationResult";
    private static final String EXTRA_NOTIFICATION = "extra_notification";

    public NotificationResult(StatusBarNotification sbn) {
        super(ID);

        Bundle extras = new Bundle();
        extras.putParcelable(EXTRA_NOTIFICATION, sbn);

        setExtras(extras);
    }

    public StatusBarNotification get() {
        return getExtras().getParcelable(EXTRA_NOTIFICATION);
    }
}
