package com.habosa.notificationbox;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.habosa.notificationbox.adapter.NotificationAdapter;
import com.habosa.notificationbox.adapter.SwipeHelper;
import com.habosa.notificationbox.model.NotificationDisplayInfo;
import com.habosa.notificationbox.model.NotificationInfo;
import com.habosa.notificationbox.notifications.NotificationActionCache;
import com.habosa.notificationbox.viewmodel.MainActivityViewModel;

import java.util.List;

public class MainActivity extends AppCompatActivity implements
        NotificationAdapter.Listener,
        SwipeHelper.Listener {

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

        // Every time the user opens the app, make sure the service is started
        startService(new Intent(this, NotificationService.class)
                .setAction(NotificationService.ACTION_REBIND));

        mViewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);

        mRecycler = findViewById(R.id.notifications_recycler);
        mAdapter = new NotificationAdapter(this);
        mManager = new LinearLayoutManager(this);

        mRecycler.setLayoutManager(mManager);
        mRecycler.setAdapter(mAdapter);

        // Listen for swipes
        ItemTouchHelper touchHelper = new ItemTouchHelper(new SwipeHelper(this));
        touchHelper.attachToRecyclerView(mRecycler);

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
            explanNotificationAccess();
            return;
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
    public void onItemDismissed(int position) {
        Log.d(TAG, "onItemDismissed: " + position);

        NotificationInfo info = mAdapter.getItem(position).info;
        mAdapter.notifyItemRemoved(position);
        mViewModel.removeNotification(info);
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

    private void explanNotificationAccess() {
        new AlertDialog.Builder(this)
                .setTitle("Grant Notification Access")
                .setMessage("NotificationBox needs access to your notifications to work.")
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        launchNotificationAccessSettings();
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .show();
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
