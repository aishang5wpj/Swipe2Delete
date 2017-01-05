package com.xiaohongshu.swipe2delete;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;


/**
 * Created by wupengjian on 16/11/9.
 * <p/>
 * http://blog.csdn.net/aishang5wpj/article/details/54093911
 */
public class Swipe2DeletePart2 extends ViewGroup {

    private static final int STATUS_NORMAL = 0;
    private static final int STATUS_EXPAND = 1;
    private View mCenterView;
    private float mLastTouchX, mScrollX;
    private int mMaxScrollDistance, mMinScrollDistance;
    private int mStatus = STATUS_NORMAL;

    public Swipe2DeletePart2(Context context) {
        this(context, null);
    }

    public Swipe2DeletePart2(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Swipe2DeletePart2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        mCenterView = null;
        mMaxScrollDistance = 0;

        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() == GONE) {
                continue;
            }
            int childWidth;
            if (mCenterView == null) {
                mCenterView = child;
                childWidth = MeasureSpec.makeMeasureSpec(widthSize, MeasureSpec.EXACTLY);
            } else {
                childWidth = heightMeasureSpec;
                //最大滚动距离就是所有菜单item的宽度的和
                mMaxScrollDistance += MeasureSpec.getSize(childWidth);
            }
            child.measure(childWidth, heightMeasureSpec);
        }
        setMeasuredDimension(widthSize, heightSize);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        int childCount = getChildCount();
        int offset = 0;
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() == GONE) {
                continue;
            }
            setChildFrame(child, offset, 0, child.getMeasuredWidth(), child.getMeasuredHeight());
            offset += child.getMeasuredWidth();
        }
    }

    private void setChildFrame(View child, int left, int top, int width, int height) {
        child.layout(left, top, left + width, top + height);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                break;
            case MotionEvent.ACTION_MOVE:

                updateScrollX(mLastTouchX - event.getRawX());
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:

                toggleStatus();
                break;
        }
        mLastTouchX = event.getRawX();
        return true;
    }

    private void toggleStatus() {
        int scrollThreshold = getMeasuredHeight();
        if (mScrollX < scrollThreshold) {

            hideMenu();
        } else if (mScrollX > scrollThreshold) {

            showMenu();
        }
    }

    /**
     * 显示菜单
     */
    private void showMenu() {

        mStatus = STATUS_EXPAND;
        updateScrollX(mMaxScrollDistance);
    }

    /**
     * 隐藏菜单
     */
    private void hideMenu() {

        mStatus = STATUS_NORMAL;
        updateScrollX(-mMaxScrollDistance);
    }

    private void updateScrollX(float distance) {

        float dx = mScrollX + distance;
        if (dx < mMinScrollDistance) {

            dx = mMinScrollDistance;
        } else if (dx > mMaxScrollDistance) {

            dx = mMaxScrollDistance;
        }
        mScrollX = dx;
        scrollTo((int) dx, 0);
    }
}
