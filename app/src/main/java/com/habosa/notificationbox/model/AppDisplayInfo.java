package com.habosa.notificationbox.model;

import android.graphics.drawable.Drawable;

/**
 * Created by samstern on 2/11/18.
 */
public class AppDisplayInfo {

    public final String title;
    public final Drawable icon;

    public AppDisplayInfo(String title, Drawable icon) {
        this.title = title;
        this.icon = icon;
    }

}
