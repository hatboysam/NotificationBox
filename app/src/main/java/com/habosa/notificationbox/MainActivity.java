package com.habosa.notificationbox;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.service.notification.StatusBarNotification;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity implements
        View.OnClickListener {

    // TODO: ButterKnife

    private static final String TAG = "MainActivity";

    private NotificationReceiver mNotificationReceiver = new NotificationReceiver() {
        @Override
        public void onNotification(StatusBarNotification notification) {
            MainActivity.this.onNotification(notification);
        }
    };

    private RecyclerView mRecycler;
    private NotificationAdapter mAdapter;
    private LinearLayoutManager mManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.button_enable).setOnClickListener(this);
        findViewById(R.id.button_notify).setOnClickListener(this);

        mRecycler = (RecyclerView) findViewById(R.id.notifications_recycler);
        mAdapter = new NotificationAdapter();
        mManager = new LinearLayoutManager(this);

        mRecycler.setLayoutManager(mManager);
        mRecycler.setAdapter(mAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(this).registerReceiver(mNotificationReceiver,
                NotificationReceiver.getIntentFilter());
    }


    @Override
    protected void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mNotificationReceiver);
    }

    private void onNotification(StatusBarNotification notification) {
        // TODO: What other information is there besides the Notification object?
        Log.d(TAG, "onNotification: " + notification);
        mAdapter.add(notification.getNotification());
    }


    private void showNotification() {
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                new Intent(Settings.ACTION_BLUETOOTH_SETTINGS), 0);
        final Notification notification = new NotificationCompat.Builder(this)
                .setContentTitle("Launch Bluetooth Settings")
                .setContentText("If you click this, they will come.")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pendingIntent)
                .build();

        NotificationManagerCompat.from(this).notify(0, notification);
    }

    private void launchNotificationAccessSettings() {
        startActivity(new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_enable:
                launchNotificationAccessSettings();
                break;
            case R.id.button_notify:
                showNotification();
                break;
        }
    }
}
