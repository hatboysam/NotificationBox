package com.habosa.notificationbox;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.service.notification.StatusBarNotification;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.habosa.notificationbox.adapter.NotificationAdapter;
import com.habosa.notificationbox.viewmodel.MainActivityViewModel;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    // TODO: ButterKnife

    private static final String TAG = "MainActivity";

    private RecyclerView mRecycler;
    private NotificationAdapter mAdapter;
    private LinearLayoutManager mManager;

    private MainActivityViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // TODO: Should I have to do this?  If so where?
        startService(new Intent(this, NotificationService.class));

        mViewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);

        mRecycler = findViewById(R.id.notifications_recycler);
        mAdapter = new NotificationAdapter();
        mManager = new LinearLayoutManager(this);

        mRecycler.setLayoutManager(mManager);
        mRecycler.setAdapter(mAdapter);

        // Observe notifications
        mViewModel.getNotifications().observe(this,
                new Observer<List<StatusBarNotification>>() {
                    @Override
                    public void onChanged(@Nullable List<StatusBarNotification> notifications) {
                        if (notifications == null) {
                            return;
                        }

                        onNotifications(notifications);
                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!hasNotificationPermissions()) {
            // TODO: Softer UX
            launchNotificationAccessSettings();
        }

        mViewModel.requestNotifications();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_select_apps:
                startActivity(new Intent(this, AppSelectionActivity.class));
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private synchronized void onNotifications(List<StatusBarNotification> notifications) {
        Log.d(TAG, "onNotifications: " + notifications.size());

        mAdapter.clear();
        for (StatusBarNotification sbn : notifications) {
            mAdapter.add(sbn);
        }
    }

    private boolean hasNotificationPermissions() {
        return NotificationManagerCompat
                .getEnabledListenerPackages(this)
                .contains(getPackageName());
    }

    private void launchNotificationAccessSettings() {
        startActivity(new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"));
    }
}
