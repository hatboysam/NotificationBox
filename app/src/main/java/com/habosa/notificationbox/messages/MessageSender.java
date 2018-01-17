package com.habosa.notificationbox.messages;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;

/**
 * Created by samstern on 1/16/18.
 */
public class MessageSender {

    public static <T extends Message> void sendMessage(Context context, T message) {
        LocalBroadcastManager.getInstance(context).sendBroadcast(getBroadcast(message));
    }

    @NonNull
    private static <T extends Message> Intent getBroadcast(T message) {
        Intent intent = new Intent(MessageReceiver.ACTION);
        intent.putExtra(MessageReceiver.KEY_MESSAGE, message);

        return intent;
    }

}
