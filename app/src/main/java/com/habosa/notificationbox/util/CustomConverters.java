package com.habosa.notificationbox.util;

import android.arch.persistence.room.TypeConverter;

import com.habosa.notificationbox.notifications.NotificationKey;

public class CustomConverters {

    @TypeConverter
    public static NotificationKey keyFrommString(String string) {
        return new NotificationKey(string);
    }

    @TypeConverter
    public static String stringFromKey(NotificationKey key) {
        return key.toString();
    }

}
