package com.habosa.notificationbox.notifications;

import android.service.notification.StatusBarNotification;
import android.text.TextUtils;

import java.util.Locale;

/**
 * Created by samstern on 3/3/18.
 */
public class NotificationKey {

    private static final String SEP = ":";

    private final String mKey;
    private final long mId;
    private final String mTag;

    public NotificationKey(StatusBarNotification sbn) {
        String key = TextUtils.isEmpty(sbn.getKey()) ? "KEY" : sbn.getKey();
        key = key.replace(SEP, "~");

        String tag = TextUtils.isEmpty(sbn.getTag()) ? "TAG" : sbn.getTag();
        tag = tag.replace(SEP, "~");

        mKey = key;
        mId = sbn.getId();
        mTag = tag;
    }

    public NotificationKey(String keyString) {
        String[] parts = keyString.split(":");
        if (parts.length < 3) {
            throw new IllegalArgumentException("String has < 3 parts: " + keyString);
        }

        mKey = parts[0];
        mId = Long.parseLong(parts[1]);
        mTag = parts[2];
    }

    @Override
    public String toString() {
        return String.format(Locale.getDefault(), "%s:%d:%s", mKey, mId, mTag);
    }
}
