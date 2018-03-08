package com.habosa.notificationbox;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationCompatExtras;
import android.support.v4.app.NotificationManagerCompat;
import android.text.TextUtils;
import android.util.Log;

import com.habosa.notificationbox.data.AppDatabase;
import com.habosa.notificationbox.data.NotificationDao;
import com.habosa.notificationbox.model.NotificationInfo;
import com.habosa.notificationbox.notifications.NotificationActionCache;
import com.habosa.notificationbox.util.BackgroundUtils;
import com.habosa.notificationbox.util.PreferenceUtils;

public class NotificationService extends NotificationListenerService {

    private static final String TAG = "NotificationService";

    public static final String ACTION_REBIND = "action_rebind";

    private static final int SUMMARY_NOTIF_ID = 101;

    private static final String SUMMARY_CHANNEL_ID = "summary";
    private static final String SUMMARY_CHANNEL_NAME = "Summary";
    private static final String SUMMARY_CHANNEL_DESC = "Summary of inbox";

    private NotificationDao mNotificationDao;
    private PreferenceUtils mPrefs;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate");

        mNotificationDao = AppDatabase.getInstance(getApplicationContext()).notificationDao();
        mPrefs = new PreferenceUtils(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand: " + intent);
        if (intent != null
                && !TextUtils.isEmpty(intent.getAction())
                && intent.getAction().equals(ACTION_REBIND)) {
            tryReconnectService();
        }

        return START_STICKY;
    }

    @Override
    public void onListenerConnected() {
        super.onListenerConnected();
        Log.d(TAG, "onListenerConnected");
    }

    @Override
    public void onListenerDisconnected() {
        super.onListenerDisconnected();
        Log.d(TAG, "onListenerDisconnected");
    }

    @Override
    public void onNotificationPosted(StatusBarNotification notification) {
        super.onNotificationPosted(notification);
        Log.d(TAG, "onNotificationPosted:" + notification.toString());

        if (!shouldKeepNotification(notification)) {
            Log.d(TAG, "onNotificationPosted: ignoring.");
            return;
        }

        Log.d(TAG, "Intercepting notification:  " + notification.toString());

        // Create NotificationInfo
        NotificationInfo info = new NotificationInfo(notification);

        // Store the notification
        insert(info, notification);

        // Cancel the notification, it's ours now
        cancelNotification(notification.getKey());

        // Show a notification
        // TODO: same deal
        BackgroundUtils.EXECUTOR.execute(new Runnable() {
            @Override
            public void run() {
                int count = mNotificationDao.count();
                showSummaryNotification(count);
            }
        });

        // TODO: Send a broadcast here to force the app to reload if it's open
    }

    private void insert(final NotificationInfo info, final StatusBarNotification sbn) {
        // TODO: I need to do more sane thread management.
        BackgroundUtils.EXECUTOR.execute(new Runnable() {
            @Override
            public void run() {
                mNotificationDao.insert(info);
                NotificationActionCache.put(info, sbn.getNotification().contentIntent);
            }
        });
    }

    private void showSummaryNotification(int count) {
        NotificationManagerCompat manager = NotificationManagerCompat.from(this);

        // Create channel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager sysManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            NotificationChannel channel = new NotificationChannel(
                    SUMMARY_CHANNEL_ID,
                    SUMMARY_CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_LOW);

            channel.setDescription(SUMMARY_CHANNEL_DESC);

            sysManager.createNotificationChannel(channel);
        }

        Intent homeIntent = new Intent(this, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(
                this, 0, homeIntent, 0);

        String title = getResources().getQuantityString(
                R.plurals.notifications_summary_title, count, count);
        String body = getString(R.string.notifications_summary_body);

        Notification notification = new NotificationCompat.Builder(this, SUMMARY_CHANNEL_ID)
                .setChannelId(SUMMARY_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notifications_off_white_24dp)
                .setContentTitle(title)
                .setContentText(body)
                .setContentIntent(contentIntent)
                .setAutoCancel(true)
                .setVibrate(null)
                .setSound(null)
                .build();

        manager.notify(SUMMARY_NOTIF_ID, notification);
    }

    private boolean shouldKeepNotification(StatusBarNotification notification) {
        Bundle extras = notification.getNotification().extras;

        Log.d(TAG, "Keys for :" + notification);
        for (String key : extras.keySet()) {
            Log.d(TAG, "\t" + key);
        }

        // Ongoing notification like USB debugging, non dismissable
        if (notification.isOngoing()) {
            return false;
        }

        // Ignore "group summary" notifications
        if (extras.getBoolean(NotificationCompatExtras.EXTRA_GROUP_SUMMARY, false)) {
            return false;
        }

        // Ongoing media notification
        // TODO: This catches Play Music but not BeyondPod
        if (extras.containsKey(Notification.EXTRA_MEDIA_SESSION)) {
            return false;
        }

        // Check if we are watching this application
        return mPrefs.getAppSelected(notification.getPackageName());
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification notification) {
        super.onNotificationRemoved(notification);
        Log.d(TAG, "onNotificationRemoved:" + notification.toString());
    }

    /**
     * Try to reconnect the service.
     * https://stackoverflow.com/questions/45124673/notification-listener-service-does-not-work-after-app-is-crashed
     */
    public void tryReconnectService() {
        toggleNotificationListenerService();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            ComponentName componentName = new ComponentName(
                    getApplicationContext(), NotificationService.class);

            NotificationListenerService.requestRebind(componentName);
        }
    }

    /**
     * Try deactivate/activate your component service
     */
    private void toggleNotificationListenerService() {
        PackageManager pm = getPackageManager();
        pm.setComponentEnabledSetting(new ComponentName(this, NotificationService.class),
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
        pm.setComponentEnabledSetting(new ComponentName(this, NotificationService.class),
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
    }
}
