package com.habosa.notificationbox.model;

import android.graphics.drawable.Drawable;

/**
 * Information needed to display an app.
 */
public class AppDisplayInfo {

    public final String packageName;
    public final String name;
    public final Drawable icon;

    public AppDisplayInfo(String packageName, String name, Drawable icon) {
        this.packageName = packageName;
        this.name = name;
        this.icon = icon;
    }

}
