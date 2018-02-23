package com.habosa.notificationbox.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.Collections;
import java.util.Set;

/**
 * Utilities for working with {@link SharedPreferences}.
 */
public class PreferenceUtils {

    private static final String TAG = "PreferenceUtils";
    private static final String KEY_ALL_SELECTED = "key_all_selected";
    private static final String KEY_PREFIX_APP_SELECTED = "key_app_selected_";

    private final SharedPreferences mPrefs;

    public PreferenceUtils(Context context) {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public Set<String> getAllSelected() {
        return mPrefs.getStringSet(KEY_ALL_SELECTED, Collections.<String>emptySet());
    }

    public void setAppSelected(String packageName, boolean selected) {
        Log.d(TAG, "setAppSelected:" + packageName + "=" + selected);
        mPrefs.edit()
                .putBoolean(KEY_PREFIX_APP_SELECTED + packageName, selected)
                .apply();

        if (selected) {
            addToSelected(packageName);
        } else {
            removeFromSelected(packageName);
        }
    }

    public boolean getAppSelected(String packageName) {
        return mPrefs.getBoolean(KEY_PREFIX_APP_SELECTED + packageName, false);
    }

    private void addToSelected(String packageName) {
        Set<String> allSelected = getAllSelected();
        if (allSelected.contains(packageName)) {
            return;
        }

        allSelected.add(packageName);
        mPrefs.edit()
                .putStringSet(KEY_ALL_SELECTED, allSelected)
                .apply();
    }

    private void removeFromSelected(String packageName) {
        Set<String> allSelected = getAllSelected();
        if (!allSelected.contains(packageName)) {
            return;
        }

        allSelected.remove(packageName);
        mPrefs.edit()
                .putStringSet(KEY_ALL_SELECTED, allSelected)
                .apply();
    }
}
