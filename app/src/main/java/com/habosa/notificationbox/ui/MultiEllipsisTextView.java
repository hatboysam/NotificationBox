package com.habosa.notificationbox.ui;

import android.content.Context;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.util.AttributeSet;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * TextView that ellipsizes each line, if necessary.
 */
public class MultiEllipsisTextView extends android.support.v7.widget.AppCompatTextView {

    private static final Pattern LINE_PATTERN = Pattern.compile(".+($|\\n)");

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
            sb.append(lines.get(i));
            if (i < lines.size() - 1) {
                sb.append('\n');
            }
        }

        // TODO: Rather than using a regex matcher, I can almost definitely do this inline
        //       while building the string
        Matcher matcher = LINE_PATTERN.matcher(sb);
        while(matcher.find()) {
            sb.setSpan(new EllipsizeLineSpan(),
                    matcher.start(), matcher.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        setText(sb.toString());
    }
}
