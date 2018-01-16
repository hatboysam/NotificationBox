package com.habosa.notificationbox;

import android.app.Notification;
import android.app.PendingIntent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by samstern on 1/15/18.
 */

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

    private static final String TAG = "NotificationAdapter";
    private List<Notification> mNotifications = new ArrayList<>();

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

    public void add(Notification notification) {
        mNotifications.add(notification);
        notifyItemInserted(getItemCount() - 1);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private NotificationView mNotificationView;

        public ViewHolder(View itemView) {
            super(itemView);

            mNotificationView = (NotificationView) itemView.findViewById(R.id.item_view_notification);
        }

        public void bind(final Notification notification) {
            mNotificationView.bind(notification);
            mNotificationView.setOnClickListener(new View.OnClickListener() {
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
