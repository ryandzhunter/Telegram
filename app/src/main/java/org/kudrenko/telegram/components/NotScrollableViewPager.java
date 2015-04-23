package org.kudrenko.telegram.components;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class NotScrollableViewPager extends ViewPager {
    public NotScrollableViewPager(Context context) {
        super(context);
    }

    public NotScrollableViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return true;
    }
}
