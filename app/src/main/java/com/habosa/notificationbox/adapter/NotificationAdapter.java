package com.habosa.notificationbox.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.habosa.notificationbox.R;
import com.habosa.notificationbox.model.NotificationDisplayInfo;
import com.habosa.notificationbox.model.NotificationInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter to display NotificationDisplayInfo.
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

    public class ViewHolder extends RecyclerView.ViewHolder implements SwipeHelper.Swipeable {

        private final View mMainView;

        private ImageView mIconView;
        private TextView mTimeView;
        private TextView mTitleView;
        private TextView mBodyView;

        public ViewHolder(View itemView) {
            super(itemView);

            mMainView = itemView.findViewById(R.id.item_notification_main);

            mIconView = itemView.findViewById(R.id.notification_image_icon);
            mTimeView = itemView.findViewById(R.id.notification_text_time);
            mTitleView = itemView.findViewById(R.id.notification_text_title);
            mBodyView = itemView.findViewById(R.id.notification_text_body);
        }

        public void bind(final NotificationDisplayInfo displayInfo) {
            setSwipeDx(0);

            mIconView.setImageDrawable(displayInfo.icon);
            mTimeView.setText(displayInfo.getTimeAgo());

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

        @Override
        public void setSwipeDx(float dX) {
            // Move the main view to the right
            mMainView.setTranslationX(dX);
        }

        @Override
        public void resetSwipe() {
            mMainView.setTranslationX(0);
        }
    }

}
