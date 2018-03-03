package com.habosa.notificationbox;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.habosa.notificationbox.adapter.NotificationAdapter;
import com.habosa.notificationbox.model.NotificationDisplayInfo;
import com.habosa.notificationbox.model.NotificationInfo;
import com.habosa.notificationbox.notifications.NotificationActionCache;
import com.habosa.notificationbox.viewmodel.MainActivityViewModel;

import java.util.List;

public class MainActivity extends AppCompatActivity implements NotificationAdapter.Listener {

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
        mAdapter = new NotificationAdapter(this);
        mManager = new LinearLayoutManager(this);

        mRecycler.setLayoutManager(mManager);
        mRecycler.setAdapter(mAdapter);

        // Observe notifications
        mViewModel.getNotifications().observe(this, new Observer<List<NotificationDisplayInfo>>() {
            @Override
            public void onChanged(@Nullable List<NotificationDisplayInfo> displayInfos) {
                if (displayInfos == null) {
                    return;
                }

                onNotifications(displayInfos);
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

        mViewModel.requestNotificationInfos();
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

    @Override
    public void onNotificationClicked(NotificationInfo info) {
        boolean launched = NotificationActionCache.launchAction(this, info);
        if (launched) {
            showToast("Opening...");

            // TODO: Should we always delete after launch?
            mViewModel.removeNotification(info);
        } else {
            showSnackbar("Oops! There was a problem opening that.");
        }
    }

    private void onNotifications(@NonNull List<NotificationDisplayInfo> displayInfos) {
        mAdapter.clear();

        for (NotificationDisplayInfo ndi : displayInfos) {
            mAdapter.add(ndi);
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

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void showSnackbar(String message) {
        Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_SHORT).show();
    }
}
