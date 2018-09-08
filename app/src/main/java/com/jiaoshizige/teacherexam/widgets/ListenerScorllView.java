package com.jiaoshizige.teacherexam.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * Created by Administrator on 2017/10/13.
 */

public class ListenerScorllView extends ScrollView {
    private OnScollChangedListener onScollChangedListener = null;

    public ListenerScorllView(Context context) {
        super(context);
    }

    public ListenerScorllView(Context context, AttributeSet attrs,
                              int defStyle) {
        super(context, attrs, defStyle);
    }

    public ListenerScorllView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setOnScollChangedListener(OnScollChangedListener onScollChangedListener) {
        this.onScollChangedListener = onScollChangedListener;
    }

    @Override
    protected void onScrollChanged(int x, int y, int oldx, int oldy) {
        super.onScrollChanged(x, y, oldx, oldy);
        if (onScollChangedListener != null) {
            onScollChangedListener.onScrollChanged(this, x, y, oldx, oldy);
        }
    }

    public interface OnScollChangedListener {

        void onScrollChanged(ListenerScorllView scrollView, int x, int y, int oldx, int oldy);

    }
}
