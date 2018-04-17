package com.habosa.notificationbox.model;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.WorkerThread;
import android.text.TextUtils;
import android.util.Log;

import java.util.concurrent.TimeUnit;

/**
 * Created by samstern on 2/27/18.
 */
public class NotificationDisplayInfo {

    private static final String TAG = "NotificationDisplayInfo";

    public final NotificationInfo info;
    public Drawable icon;
    public String appName;

    public NotificationDisplayInfo(NotificationInfo info) {
        this.info = info;
    }

    @WorkerThread
    public void load(@NonNull PackageManager pm, @NonNull  Resources resources) {
        String packageName = info.getPackageName();

        // TODO: Better defaults
        this.appName = "Unknown App";
        this.icon = resources.getDrawable(android.R.drawable.ic_dialog_alert);

        try {
            ApplicationInfo appInfo = pm.getApplicationInfo(packageName, 0);
            appName = pm.getApplicationLabel(appInfo).toString();
            icon = pm.getApplicationIcon(appInfo);
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, "getApplicationInfo", e);
        }
    }

    public String getDisplayTitle() {
        if (TextUtils.isEmpty(info.getTitle())) {
            return appName;
        } else {
            return info.getTitle();
        }
    }

    public String getTimeAgo() {
        long diff = System.currentTimeMillis() - info.getPostTime();
        long minutesAgo = TimeUnit.MILLISECONDS.toMinutes(diff);
        long hoursAgo = TimeUnit.MILLISECONDS.toHours(diff);
        long daysAgo = TimeUnit.MILLISECONDS.toDays(diff);

        if (minutesAgo < 1) {
            return "now";
        } else if (minutesAgo < 60) {
            return "" + minutesAgo + "m";
        } else if (hoursAgo < 24) {
            return "" + hoursAgo + "h";
        } else {
            return "" + daysAgo + "d";
        }
    }

}
