package com.habosa.notificationbox.model;

import android.app.Notification;
import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Bundle;
import android.service.notification.StatusBarNotification;

/**
 * Model object for Notification display information.
 */
@Entity(tableName = "NotificationInfo")
public class NotificationInfo {

    @PrimaryKey
    private int id;

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

    // Actions
    // TODO

    public NotificationInfo() {

    }

    public NotificationInfo(StatusBarNotification sbn) {
        this.id = sbn.getId();
        this.postTime = sbn.getPostTime();
        this.packageName = sbn.getPackageName();

        Bundle extras = sbn.getNotification().extras;
        if (extras.containsKey(Notification.EXTRA_TITLE)) {
            this.title = extras.getCharSequence(Notification.EXTRA_TITLE).toString();
        }

        if (extras.containsKey(Notification.EXTRA_TEXT)) {
            this.body = extras.getCharSequence(Notification.EXTRA_TEXT).toString();
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
