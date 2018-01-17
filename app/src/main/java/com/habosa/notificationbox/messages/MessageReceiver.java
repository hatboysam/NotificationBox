package com.habosa.notificationbox.messages;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.NonNull;

/**
 * Created by samstern on 1/16/18.
 */
public abstract class MessageReceiver<T extends Message> extends BroadcastReceiver {

    public static final String ACTION = "action_message";
    public static final String KEY_MESSAGE = "key_message";

    @NonNull
    public static IntentFilter getFilter() {
        return new IntentFilter(ACTION);
    }

    private final Class<T> mClass;

    public MessageReceiver(Class<T> tClass) {
        mClass = tClass;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (!intent.hasExtra(KEY_MESSAGE)) {
            return;
        }

        Message message = intent.getParcelableExtra(KEY_MESSAGE);
        if (!mClass.isInstance(message)) {
            return;
        }

        onMessage((T) message);
    }

    public abstract void onMessage(T message);
}
