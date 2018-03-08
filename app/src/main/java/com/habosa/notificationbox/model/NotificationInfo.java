package com.habosa.notificationbox.model;

import android.app.Notification;
import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.os.Bundle;
import android.service.notification.StatusBarNotification;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.habosa.notificationbox.notifications.NotificationKey;
import com.habosa.notificationbox.util.CustomConverters;

/**
 * Model object for Notification display information.
 */
@Entity(tableName = "NotificationInfo")
@TypeConverters({CustomConverters.class})
public class NotificationInfo {

    @PrimaryKey
    @NonNull
    private NotificationKey key;

    // App Package Name
    @ColumnInfo(name = "packageName")
    private String packageName;

    // Time
    @ColumnInfo(name = "postTime")
    private long postTime;

    // Title
    @ColumnInfo(name = "title")
    private String title;

    // Body
    @ColumnInfo(name = "body")
    private String body;

    public NotificationInfo() {
        // TODO: I really should not have to do this...
        this.key = new NotificationKey("a:123:c");
    }

    public NotificationInfo(StatusBarNotification sbn) {
        this.key = new NotificationKey(sbn);
        this.postTime = sbn.getPostTime();
        this.packageName = sbn.getPackageName();

        Bundle extras = sbn.getNotification().extras;
        if (extras.containsKey(Notification.EXTRA_TITLE)) {
            CharSequence title = extras.getCharSequence(Notification.EXTRA_TITLE);
            this.title = title == null ? null : title.toString();
        }

        if (extras.containsKey(Notification.EXTRA_TEXT)) {
            CharSequence text = extras.getCharSequence(Notification.EXTRA_TEXT);
            this.body = text == null ? null : text.toString();
        }
    }

    public NotificationKey getKey() {
        return key;
    }

    public void setKey(NotificationKey key) {
        this.key = key;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public long getPostTime() {
        return postTime;
    }

    public void setPostTime(long postTime) {
        this.postTime = postTime;
    }

    @Nullable
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Nullable
    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
