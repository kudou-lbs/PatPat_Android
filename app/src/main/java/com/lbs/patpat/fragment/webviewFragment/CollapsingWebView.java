package com.lbs.patpat.fragment.webviewFragment;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.webkit.WebView;

/**
 * 目的是为了解决滑动冲突，似乎没效果。
 * */

public class CollapsingWebView extends WebView {
    private GestureDetector detector;
    private boolean isScrollBottom = false;

    public CollapsingWebView(Context context) {
        this(context, null);
    }

    public CollapsingWebView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CollapsingWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        detector = new GestureDetector(context, new SimpleOnGestureListener() {
            @Override
            public boolean onDown(MotionEvent e) {
                if (!isScrollBottom) {
                    requestDisallowInterceptTouchEvent(true);
                } else {
                    isScrollBottom = false;
                }
                return false;
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                float webcontent = getContentHeight() * getScale();
                float webnow = getHeight() + getScrollY();
                isScrollBottom = (Math.abs(webcontent - webnow) < 1);
                return true;
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        detector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

}
