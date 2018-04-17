package com.habosa.notificationbox.ui;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.text.style.LineBackgroundSpan;
import android.text.style.ReplacementSpan;

/**
 * See: https://stackoverflow.com/a/18411726/324977
 */
public class EllipsizeLineSpan extends ReplacementSpan implements LineBackgroundSpan {

    private static final String ELLIPSIS = "\u2026";

    private int mLayoutLeft = 0;
    private int mLayoutRight = 0;

    public EllipsizeLineSpan() {
    }

    @Override
    public void drawBackground(Canvas c, Paint p,
                               int left, int right,
                               int top, int baseline, int bottom,
                               CharSequence text, int start, int end, int lnum) {
        Rect clipRect = new Rect();
        c.getClipBounds(clipRect);
        mLayoutLeft = clipRect.left;
        mLayoutRight = clipRect.right;
    }

    @Override
    public int getSize(@NonNull Paint paint,
                       CharSequence text, int start, int end,
                       Paint.FontMetricsInt fm) {
        return mLayoutRight - mLayoutLeft;
    }

    @Override
    public void draw(@NonNull Canvas canvas,
                     CharSequence text, int start, int end,
                     float x, int top, int y, int bottom,
                     @NonNull Paint paint) {

        float textWidth = paint.measureText(text, start, end);
        int textRight = (int) (x + Math.ceil(textWidth));
        boolean textFits = textRight < mLayoutRight;

        if (textFits) {
            // Draw the text without the ellipsis
            canvas.drawText(text, start, end, x, y, paint);
        } else {
            // Draw the text with an ellipsis at the end
            float ellipsisWidth = paint.measureText(ELLIPSIS);

            // Move 'end' to the ellipsis point
            int maxWidth = (int) (mLayoutRight - x - ellipsisWidth);
            int newEnd = start + paint.breakText(text, start, end,
                    true, maxWidth, null);
            canvas.drawText(text, start, newEnd, x, y, paint);
            canvas.drawText(ELLIPSIS, x + paint.measureText(text, start, newEnd), y, paint);
        }
    }
}