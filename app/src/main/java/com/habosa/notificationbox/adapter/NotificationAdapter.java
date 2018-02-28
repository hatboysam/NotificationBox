package com.habosa.notificationbox.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.habosa.notificationbox.R;
import com.habosa.notificationbox.model.NotificationDisplayInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by samstern on 1/15/18.
 */
public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

    private static final String TAG = "NotificationAdapter";
    private List<NotificationDisplayInfo> mNotifications = new ArrayList<>();

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

    public void add(NotificationDisplayInfo info) {
        mNotifications.add(info);
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

        public void bind(NotificationDisplayInfo displayInfo) {
            // TODO: Turn into a custom view that binds Notification

            long diff = System.currentTimeMillis() - displayInfo.info.getPostTime();
            long minutesAgo = TimeUnit.MILLISECONDS.toMinutes(diff);
            long hoursAgo = TimeUnit.MILLISECONDS.toHours(diff);
            long daysAgo = TimeUnit.MILLISECONDS.toDays(diff);

            String timeString;
            if (minutesAgo < 1) {
                timeString = "now";
            } else if (minutesAgo < 60) {
                timeString = "" + minutesAgo + "m";
            } else if (hoursAgo < 24) {
                timeString = "" + hoursAgo + "h";
            } else {
                timeString = "" + daysAgo + "d";
            }

            mIconView.setImageDrawable(displayInfo.icon);
            mAppNameView.setText(displayInfo.appName);
            mTimeView.setText(timeString);
            mTitleView.setText(displayInfo.info.getTitle());
            mBodyView.setText(displayInfo.info.getBody());

            // TODO: Actions
            // TODO: Need to bind other actions like "Copy" and "Share" on Pushbullet
//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (notification.contentIntent == null) {
//                        Log.w(TAG, "No content intent for: " + notification);
//                        return;
//                    }
//
//                    try {
//                        notification.contentIntent.send();
//                    } catch (PendingIntent.CanceledException e) {
//                        Log.e(TAG, "Can't launch", e);
//                    }
//                }
//            });
        }
    }

}
