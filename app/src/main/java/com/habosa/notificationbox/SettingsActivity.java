package com.habosa.notificationbox;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        FragmentManager fm = getSupportFragmentManager();
        if (fm.findFragmentByTag(SettingsFragment.TAG) == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.pref_container, new SettingsFragment(), SettingsFragment.TAG)
                    .commit();
        }
    }
}
