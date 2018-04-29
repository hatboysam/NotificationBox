package com.habosa.notificationbox.ui;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.util.AttributeSet;

import java.util.List;

/**
 * TextView that ellipsizes each line, if necessary.
 */
public class MultiEllipsisTextView extends AppCompatTextView {

    public MultiEllipsisTextView(Context context) {
        super(context);
    }

    public MultiEllipsisTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MultiEllipsisTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    public void setText(List<String> lines) {
        // TODO: Add a feature to limit the max number of lines
        SpannableStringBuilder sb = new SpannableStringBuilder();
        for (int i = 0; i < lines.size(); i++) {
            int beforeSize = sb.length();
            sb.append(lines.get(i));
            int afterSize = sb.length();

            // Add a newline to all but the last line
            if (i < lines.size() - 1) {
                sb.append('\n');
            }

            // Ellipsize the span
            sb.setSpan(new EllipsizeLineSpan(),
                    beforeSize, afterSize - 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        }

        setText(sb.toString());
    }
}
