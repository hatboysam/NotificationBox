package com.habosa.notificationbox;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.View;

public class MainActivity extends FragmentActivity implements
        View.OnClickListener {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.button_enable).setOnClickListener(this);
        findViewById(R.id.button_notify).setOnClickListener(this);
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
        bindNotification(notification);
    }

    private void bindNotification(final Notification notification) {
        NotificationView nv = (NotificationView) findViewById(R.id.view_notification);
        nv.bind(notification);
        nv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (notification.contentIntent == null) {
                    Log.w(TAG, "No content intent for: " + notification);
                    return;
                }

                try {
                    notification.contentIntent.send();
                } catch (PendingIntent.CanceledException e) {
                    Log.e(TAG, "Can't launch", e);
                }
            }
        });
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
