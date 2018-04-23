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
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.habosa.notificationbox.adapter.NotificationAdapter;
import com.habosa.notificationbox.adapter.SwipeHelper;
import com.habosa.notificationbox.model.NotificationDisplayInfo;
import com.habosa.notificationbox.model.NotificationInfo;
import com.habosa.notificationbox.notifications.NotificationActionCache;
import com.habosa.notificationbox.viewmodel.MainActivityViewModel;

import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements
        NotificationAdapter.Listener,
        SwipeHelper.Listener {

    // TODO: ButterKnife

    private static final String TAG = "MainActivity";

    private static final int[] ALL_DONE_EMOJIS = new int[]{
            R.drawable.man_dancing_u1f57a,
            R.drawable.man_lifting_weights_u1f3cb,
            R.drawable.woman_dancing_u1f483,
            R.drawable.woman_tipping_hand_u1f481,
    };

    private static final int[] ALL_DONE_MESSAGES = new int[] {
        R.string.all_done_1,
        R.string.all_done_2,
        R.string.all_done_3,
        R.string.all_done_4,
    };

    private ImageView mAllDoneImage;
    private TextView mAllDoneText;
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

        mAllDoneImage = findViewById(R.id.all_done_emoji);
        mAllDoneText = findViewById(R.id.all_done_text);
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
            case R.id.menu_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
            case R.id.menu_dismiss_all:
                // TODO: hide this option when there are no notifs
                onDismissAllClicked();
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

        showAllDoneMessage(displayInfos.isEmpty());
    }

    private void onDismissAllClicked() {
        new AlertDialog.Builder(this)
                .setTitle("Dismiss All")
                .setMessage("Are you sure you want to dismiss all notifications?")
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mViewModel.dismissAll();
                    }
                })
                .setNegativeButton(android.R.string.cancel, null)
                .create()
                .show();
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

    private void showAllDoneMessage(boolean show) {
        int visibility = show ? View.VISIBLE : View.GONE;
        mAllDoneText.setVisibility(visibility);
        mAllDoneImage.setVisibility(visibility);

        if (show) {
            Random random = new Random();

            int emoji = ALL_DONE_EMOJIS[random.nextInt(ALL_DONE_EMOJIS.length)];
            int message = ALL_DONE_MESSAGES[random.nextInt(ALL_DONE_MESSAGES.length)];

            mAllDoneImage.setImageResource(emoji);
            mAllDoneText.setText(message);
        }
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void showSnackbar(String message) {
        Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_SHORT).show();
    }
}
