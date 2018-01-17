package com.habosa.notificationbox;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.service.notification.StatusBarNotification;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by samstern on 1/15/18.
 */

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

    private static final String TAG = "NotificationAdapter";
    private List<StatusBarNotification> mNotifications = new ArrayList<>();

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_notification, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(mNotifications.get(position));
    }

    @Override
    public int getItemCount() {
        return mNotifications.size();
    }

    public void add(StatusBarNotification sbn) {
        mNotifications.add(sbn);
        notifyItemInserted(getItemCount() - 1);
    }

    public void clear() {
        mNotifications.clear();
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView mIconView;
        private TextView mAppNameView;
        private TextView mTimeView;
        private TextView mTitleView;
        private TextView mBodyView;

        public ViewHolder(View itemView) {
            super(itemView);

            mIconView = itemView.findViewById(R.id.notification_image_icon);
            mAppNameView = itemView.findViewById(R.id.notification_text_app_name);
            mTimeView = itemView.findViewById(R.id.notification_text_time);
            mTitleView = itemView.findViewById(R.id.notification_text_title);
            mBodyView = itemView.findViewById(R.id.notification_text_body);
        }

        private Context getContext() {
            return itemView.getContext();
        }

        public void bind(StatusBarNotification sbn) {
            final Notification notification = sbn.getNotification();
            Bundle extras = notification.extras;

            // TODO: Turn into a custom view that binds Notification

            String packageName = sbn.getPackageName();
            PackageManager pm = getContext().getPackageManager();
            String appName = "Unknown App";

            // TODO: Better default
            Drawable icon = getContext().getDrawable(android.R.drawable.ic_dialog_alert);

            try {
                ApplicationInfo info = pm.getApplicationInfo(packageName, 0);
                appName = pm.getApplicationLabel(info).toString();
                icon = pm.getApplicationIcon(info);
            } catch (PackageManager.NameNotFoundException e) {
                Log.e(TAG, "getApplicationInfo", e);
            }

            // TODO: Better time estimate
            long postTime = sbn.getPostTime();
            long now = System.currentTimeMillis();
            long hoursAgo = TimeUnit.MILLISECONDS.toHours(now - postTime);
            String timeString = "" + hoursAgo + "h";

            mIconView.setImageDrawable(icon);
            mAppNameView.setText(appName);
            mTimeView.setText(timeString);
            mTitleView.setText(extras.getString(Notification.EXTRA_TITLE));
            mBodyView.setText(extras.getString(Notification.EXTRA_TEXT));

            // TODO: Need to bind other actions like "Copy" and "Share" on Pushbullet
            itemView.setOnClickListener(new View.OnClickListener() {
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
    }

}
