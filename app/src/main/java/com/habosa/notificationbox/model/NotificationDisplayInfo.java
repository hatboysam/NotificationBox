package com.habosa.notificationbox.model;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.WorkerThread;
import android.util.Log;

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

}