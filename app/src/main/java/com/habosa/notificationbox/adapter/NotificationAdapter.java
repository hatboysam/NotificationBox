package com.habosa.notificationbox.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.habosa.notificationbox.R;
import com.habosa.notificationbox.model.NotificationDisplayInfo;
import com.habosa.notificationbox.model.NotificationInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by samstern on 1/15/18.
 */
public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

    private static final String TAG = "NotificationAdapter";

    private final Listener mListener;
    private List<NotificationDisplayInfo> mNotifications = new ArrayList<>();

    public interface Listener {

        void onNotificationClicked(NotificationInfo info);

    }

    public NotificationAdapter(Listener listener) {
        this.mListener = listener;
    }

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

    public NotificationDisplayInfo getItem(int position) {
        return mNotifications.get(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final View mBehindView;
        private final View mMainView;

        private ImageView mIconView;
        private TextView mTimeView;
        private TextView mTitleView;
        private TextView mBodyView;

        public ViewHolder(View itemView) {
            super(itemView);

            mBehindView = itemView.findViewById(R.id.item_notification_behind);
            mMainView = itemView.findViewById(R.id.item_notification_main);

            mIconView = itemView.findViewById(R.id.notification_image_icon);
            mTimeView = itemView.findViewById(R.id.notification_text_time);
            mTitleView = itemView.findViewById(R.id.notification_text_title);
            mBodyView = itemView.findViewById(R.id.notification_text_body);
        }

        private Context getContext() {
            return itemView.getContext();
        }

        public void bind(final NotificationDisplayInfo displayInfo) {
            // TODO: Turn into a custom view that binds Notification
            setSwipeDx(0);

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
            mTimeView.setText(timeString);

            mTitleView.setText(displayInfo.getDisplayTitle());
            mBodyView.setText(displayInfo.info.getBody());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        mListener.onNotificationClicked(displayInfo.info);
                    }
                }
            });
        }

        public void setSwipeDx(float dX) {
            float width = (float) itemView.getWidth();
            float swipeFraction = Math.abs(dX) / width;

            if (dX > 0) {
                // Stretch the red "behind view" to the right
                mBehindView.setVisibility(View.VISIBLE);
                mBehindView.setLayoutParams(
                        new RelativeLayout.LayoutParams((int) dX, mBehindView.getHeight()));
            } else {
                mBehindView.setVisibility(View.INVISIBLE);
            }


            // Move the main view to the right and lower the alpha
            mMainView.setTranslationX(dX);
            mMainView.setAlpha(1.0f - swipeFraction);
        }

        public void resetSwipe() {
            mBehindView.setVisibility(View.INVISIBLE);
        }
    }

}
