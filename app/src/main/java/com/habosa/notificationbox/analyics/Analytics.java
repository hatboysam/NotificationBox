package com.habosa.notificationbox.analyics;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.habosa.notificationbox.model.NotificationInfo;

public class Analytics {

    private static final String BUILD_BOARD = "build_board";
    private static final String BUILD_BRAND = "build_brand";

    private static final String EVENT_NOTIF_DISMISS = "notif_dismiss";
    private static final String EVENT_NOTIF_CLICKED = "notif_clicked";
    private static final String EVENT_NOTIF_DISMISS_ALL = "notif_dismiss_all";

    private static final String KEY_PACKAGE_NAME = "package_name";

    private static FirebaseAnalytics sAnalytics;

    private static FirebaseAnalytics getAnalytics(Context context) {
      if (sAnalytics == null) {
          sAnalytics = FirebaseAnalytics.getInstance(context);
          sAnalytics.setUserProperty(BUILD_BOARD, Build.BOARD);
          sAnalytics.setUserProperty(BUILD_BRAND, Build.BRAND);
      }

      return sAnalytics;
    }

    public static void logNotificationDismiss(Context context, NotificationInfo info) {
        Bundle bundle = new Bundle();
        bundle.putString(KEY_PACKAGE_NAME, info.getPackageName());

        getAnalytics(context).logEvent(EVENT_NOTIF_DISMISS, bundle);
    }

    public static void logNotificationClicked(Context context, NotificationInfo info) {
        Bundle bundle = new Bundle();
        bundle.putString(KEY_PACKAGE_NAME, info.getPackageName());

        getAnalytics(context).logEvent(EVENT_NOTIF_CLICKED, bundle);
    }

    public static void logAllDismissed(Context context) {
        getAnalytics(context).logEvent(EVENT_NOTIF_DISMISS_ALL, new Bundle());
    }

}
