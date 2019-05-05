package com.example.commonlib.view;

import android.content.Context;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

/**
 * @Created SiberiaDante
 * @Describe：
 * @CreateTime: 2017/12/17
 * @UpDateTime:
 * @Email: 2654828081@qq.com
 * @GitHub: https://github.com/SiberiaDante
 */

public class CommonNestedScrollView extends NestedScrollView {
    private boolean isNeedScroll = false;
    private float xDistance, yDistance, xLast, yLast;
    private int scaledTouchSlop;

    public CommonNestedScrollView(Context context) {
        super(context, null);
    }

    public CommonNestedScrollView(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
    }

    public CommonNestedScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        scaledTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                xDistance = yDistance = 0f;
                xLast = ev.getX();
                yLast = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                final float curX = ev.getX();
                final float curY = ev.getY();

                xDistance += Math.abs(curX - xLast);
                yDistance += Math.abs(curY - yLast);
                xLast = curX;
                yLast = curY;
//                Log.e("SiberiaDante", "xDistance ：" + xDistance + "---yDistance:" + yDistance);
                return !(xDistance >= yDistance || yDistance < scaledTouchSlop) && isNeedScroll;

        }
        return super.onInterceptTouchEvent(ev);
    }

    /**
     * 该方法用来处理NestedScrollView是否拦截滑动事件
     *
     * 1、NestedScrollView嵌套RecyclerView滑动冲突我们使用事件拦截处理，这里处理的是NestedScrollView的滑动，
     * 2、首先滑动的时候肯定是需要NestedScrollView的滑动事件，所以我们默认不拦截NestedScrollView的滑动事件，
     * 3、直到TabLayout顶部悬浮的时候，我们拦截NestedScrollView的滑动事件，交给RecyclerView来处理
     * @param isNeedScroll
     */
    public void setNeedScroll(boolean isNeedScroll) {
        this.isNeedScroll = isNeedScroll;
    }
}