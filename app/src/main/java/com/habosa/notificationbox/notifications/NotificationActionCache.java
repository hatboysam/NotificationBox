package com.habosa.notificationbox.notifications;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.Nullable;
import android.util.Log;
import android.util.LruCache;

import com.habosa.notificationbox.model.NotificationInfo;

/**
 * Created by samstern on 3/3/18.
 */
public class NotificationActionCache {

    private static final String TAG = "NotificationAction";

    private static LruCache<String, PendingIntent> sCache = new LruCache<>(100);

    public static void put(NotificationInfo info, PendingIntent intent) {
        if (intent == null || info.getKey() == null || info.getKey().toString() == null) {
            Log.w(TAG, "Null key or value: " + info.getKey() + ", " + intent);
            return;
        }
        
        sCache.put(info.getKey().toString(), intent);
    }

    @Nullable
    public static PendingIntent get(NotificationInfo info) {
        return sCache.get(info.getKey().toString());
    }

    public static boolean launchAction(Context context, NotificationInfo info) {
        PendingIntent pendingIntent = get(info);
        if (pendingIntent != null) {
            try {
                Log.d(TAG, "Sending pending intent");
                pendingIntent.send();
                return true;
            } catch (PendingIntent.CanceledException e) {
                Log.w(TAG, "Could not send intent", e);
                e.printStackTrace();
                return false;
            }
        } else {
            Log.d(TAG, "Attemtping to send backup intent for: " + info.getPackageName());

            PackageManager pm = context.getPackageManager();
            Intent backupIntent = pm.getLaunchIntentForPackage(info.getPackageName());
            if (backupIntent == null) {
                Log.w(TAG, "No launch intent for package: " + info.getPackageName());
                return false;
            }

            context.startActivity(backupIntent);
            return true;
        }


    }

}
