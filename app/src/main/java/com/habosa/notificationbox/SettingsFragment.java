package com.habosa.notificationbox;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;

/**
 * Created by samstern on 3/26/18.
 */
public class SettingsFragment extends PreferenceFragmentCompat
        implements SharedPreferences.OnSharedPreferenceChangeListener {

    public static final String TAG = "SettingsFragment";

    public static final String KEY_SHOW_COUNT_NOTIFICATION = "key_show_count_notification";
    public static final String KEY_COUNT_NOTIFICATION_MODE = "key_count_notification_mode";

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.app_preferences);

        onSharedPreferenceChanged(getPreferenceManager().getSharedPreferences(),
                KEY_SHOW_COUNT_NOTIFICATION);
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (KEY_SHOW_COUNT_NOTIFICATION.equals(key)) {
            boolean enabled = sharedPreferences.getBoolean(KEY_SHOW_COUNT_NOTIFICATION, true);

            Preference preference = findPreference(KEY_COUNT_NOTIFICATION_MODE);
            ListPreference listPreference = (ListPreference) preference;
            listPreference.setEnabled(enabled);
        }
    }
}
