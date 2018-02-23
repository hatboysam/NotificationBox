package com.habosa.notificationbox.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

/**
 * Utilities for working with {@link SharedPreferences}.
 */
public class PreferenceUtils {

    private static final String TAG = "PreferenceUtils";
    private static final String KEY_PREFIX_APP_SELECTED = "app_selected_";

    private final SharedPreferences mPrefs;

    public PreferenceUtils(Context context) {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void setAppSelected(String packageName, boolean selected) {
        Log.d(TAG, "setAppSelected:" + packageName + "=" + selected);
        mPrefs.edit()
                .putBoolean(KEY_PREFIX_APP_SELECTED + packageName, selected)
                .apply();
    }

    public boolean getAppSelected(String packageName) {
        return mPrefs.getBoolean(KEY_PREFIX_APP_SELECTED + packageName, false);
    }

}
