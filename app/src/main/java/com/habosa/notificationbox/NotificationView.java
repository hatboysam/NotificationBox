package com.habosa.notificationbox;

import android.app.Notification;
import android.content.Context;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.View;

public class NotificationView extends CardView {

    public NotificationView(Context context) {
        super(context);
    }

    public NotificationView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO: Why was this here?
        // inflate(context, R.layout.view_notification, this);
    }

    public NotificationView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void bind(Notification notification) {
        // Clear
        this.removeAllViews();

        // Render notification
        View contentView = notification.contentView.apply(getContext(), this);
        this.addView(contentView);
    }

}
