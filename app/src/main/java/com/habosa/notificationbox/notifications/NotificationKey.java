package com.habosa.notificationbox.notifications;

import android.service.notification.StatusBarNotification;

/**
 * Created by samstern on 3/3/18.
 */
public class NotificationKey {

    private final String mKey;
    private final int mId;
    private final String mTag;

    public NotificationKey(StatusBarNotification sbn) {
        mKey = sbn.getKey();
        mId = sbn.getId();
        mTag = sbn.getTag();
    }

    public NotificationKey(String keyString) {
        String[] parts = keyString.split(":");
        if (parts.length != 3) {
            throw new IllegalArgumentException("String has !=3 parts: " + keyString);
        }

        mKey = parts[0];
        mId = Integer.parseInt(parts[1]);
        mTag = parts[2];
    }

    @Override
    public String toString() {
        return String.format("%s:%d:%s", mKey, mId, mTag);
    }
}
