package com.habosa.notificationbox.adapter;

import android.graphics.Canvas;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

/**
 * Created by samstern on 3/7/18.
 */
public class SwipeHelper extends ItemTouchHelper.Callback {

    public interface Listener {

        void onItemDismissed(int position);

    }

    private final Listener mListener;

    public SwipeHelper(Listener listener) {
        mListener = listener;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        int dragFlags = 0;
        int swipeFlags = ItemTouchHelper.RIGHT;

        return makeMovementFlags(dragFlags, swipeFlags);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView,
                          RecyclerView.ViewHolder viewHolder,
                          RecyclerView.ViewHolder target) {

        // TODO: Should I actually return true?
        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        if (mListener != null) {
            mListener.onItemDismissed(viewHolder.getAdapterPosition());

            // TODO: Here and elsewhere, enforce the cast
            ((NotificationAdapter.ViewHolder) viewHolder).resetSwipe();
        }
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView,
                            RecyclerView.ViewHolder viewHolder,
                            float dX, float dY,
                            int actionState, boolean isCurrentlyActive) {

        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            NotificationAdapter.ViewHolder nvh = (NotificationAdapter.ViewHolder) viewHolder;
            nvh.setSwipeDx(dX);
        } else {
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY,
                    actionState, isCurrentlyActive);
        }
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return false;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return true;
    }
}
