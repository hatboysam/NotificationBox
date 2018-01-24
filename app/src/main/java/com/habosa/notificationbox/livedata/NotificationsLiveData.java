package com.habosa.notificationbox.livedata;

import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.service.notification.StatusBarNotification;
import android.support.v4.content.LocalBroadcastManager;

import com.habosa.notificationbox.messages.MessageReceiver;
import com.habosa.notificationbox.messages.MessageSender;
import com.habosa.notificationbox.messages.NotificationRequest;
import com.habosa.notificationbox.messages.NotificationResult;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by samstern on 1/23/18.
 */
public class NotificationsLiveData extends MutableLiveData<List<StatusBarNotification>> {

    private Context mContext;
    private List<StatusBarNotification> mNotifications = new ArrayList<>();

    private MessageReceiver<NotificationResult> mNotificationReceiver =
            new MessageReceiver<NotificationResult>(NotificationResult.class) {
                @Override
                public void onMessage(NotificationResult message) {
                    onNotification(message.get());
                }
            };

    public NotificationsLiveData(Context context) {
        mContext = context;
    }

    public void refresh() {
        MessageSender.send(mContext, new NotificationRequest());
    }

    @Override
    protected void onActive() {
        super.onActive();

        LocalBroadcastManager
                .getInstance(mContext)
                .registerReceiver(mNotificationReceiver, MessageReceiver.getFilter());
    }

    @Override
    protected void onInactive() {
        super.onInactive();

        LocalBroadcastManager
                .getInstance(mContext)
                .unregisterReceiver(mNotificationReceiver);

        mNotifications.clear();
    }

    private void onNotification(StatusBarNotification sbn) {
        mNotifications.add(sbn);
        postValue(mNotifications);
    }
}
