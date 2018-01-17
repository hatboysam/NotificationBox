package com.habosa.notificationbox;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.service.notification.StatusBarNotification;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.habosa.notificationbox.messages.MessageReceiver;
import com.habosa.notificationbox.messages.MessageSender;
import com.habosa.notificationbox.messages.NotificationRequest;
import com.habosa.notificationbox.messages.NotificationResult;

public class MainActivity extends AppCompatActivity implements
        View.OnClickListener {

    // TODO: ButterKnife

    private static final String TAG = "MainActivity";

    private MessageReceiver<NotificationResult> mNotificationReceiver =
            new MessageReceiver<NotificationResult>(NotificationResult.class) {
                @Override
                public void onMessage(NotificationResult message) {
                    onNotification(message.get());
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
        findViewById(R.id.button_request).setOnClickListener(this);

        mRecycler = findViewById(R.id.notifications_recycler);
        mAdapter = new NotificationAdapter();
        mManager = new LinearLayoutManager(this);

        mRecycler.setLayoutManager(mManager);
        mRecycler.setAdapter(mAdapter);

        // TODO: Should I have to do this?  If so where?
        startService(new Intent(this, NotificationService.class));
    }

    @Override
    protected void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(this).registerReceiver(mNotificationReceiver,
                MessageReceiver.getFilter());
    }


    @Override
    protected void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mNotificationReceiver);
    }

    private void onNotification(StatusBarNotification notification) {
        Log.d(TAG, "onNotification: " + notification);
        mAdapter.add(notification);
    }

    private void requestNotifications() {
        mAdapter.clear();
        MessageSender.send(this, new NotificationRequest());
    }

    private boolean hasNotificationPermissions() {
        // TODO: Where does this magic string come from?
        String notificationListenerString = Settings.Secure.getString(getContentResolver(),"enabled_notification_listeners");
        if (notificationListenerString == null || !notificationListenerString.contains(getPackageName())) {
           return false;
        }

        return true;
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
            case R.id.button_request:
                requestNotifications();
                break;
        }
    }
}
