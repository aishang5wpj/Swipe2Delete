package com.xiaohongshu.swipe2delete;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.Scroller;


/**
 * Created by wupengjian on 16/11/9.
 * <p>
 * http://blog.csdn.net/aishang5wpj/article/details/54093911
 */
public class Swipe2DeletePart4 extends ViewGroup {

    private static final int STATUS_NORMAL = 0;
    private static final int STATUS_EXPAND = 1;
    private static final int HOVER_TAP_SLOP = 10;
    private static final int HOVER_TAP_TIMEOUT = 150;
    private View mCenterView;
    private Scroller mScroller;
    private ViewConfiguration mViewConfiguration;
    private float mLastTouchX, mScrollX;
    private int mMaxScrollDistance, mMinScrollDistance;
    private int mStatus = STATUS_NORMAL;
    private MotionEvent mMoveDownEvent;
    private OnItemClickListener mOnItemClickListener;

    public Swipe2DeletePart4(Context context) {
        this(context, null);
    }

    public Swipe2DeletePart4(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Swipe2DeletePart4(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mScroller = new Scroller(context);
        mViewConfiguration = ViewConfiguration.get(context);
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
                mMoveDownEvent = MotionEvent.obtain(event);
                break;
            case MotionEvent.ACTION_MOVE:

                updateScrollX(mLastTouchX - event.getRawX());
                break;
            case MotionEvent.ACTION_UP:
                int downX = (int) mMoveDownEvent.getRawX();
                int downY = (int) mMoveDownEvent.getRawY();
                //如果事件坐标在以按下时坐标为中心的宽度为 2 * HOVER_TAP_SLOP 的正方形内,则认为这个事件是点击事件
                Rect rect = new Rect(downX - HOVER_TAP_SLOP, downY - HOVER_TAP_SLOP, downX + HOVER_TAP_SLOP, downY + HOVER_TAP_SLOP);
                //如果按下手指和抬起手指时的 坐标和时间 相差不是很大,则可以认为是点击
                boolean intent2Click = rect.contains((int) event.getRawX(), (int) event.getRawY());
                boolean isTimeNotTooLong = event.getEventTime() - event.getDownTime() < HOVER_TAP_TIMEOUT;
                if (intent2Click && isTimeNotTooLong) {

                    handleClickEvent(event);
                } else {

                    toggleStatus();
                }
                break;
            case MotionEvent.ACTION_CANCEL:
                toggleStatus();
                break;
        }
        mLastTouchX = event.getRawX();
        return true;
    }

    /**
     * 处理点击事件
     *
     * @param event
     */
    private void handleClickEvent(MotionEvent event) {

        int index = 0;
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() == GONE) {
                continue;
            }
            if (isMotionEventInView(child, event)) {
                //如果点击的是主item,如果当前是菜单展开状态,则先收起菜单,并消费此次点击
                if (child == mCenterView && mStatus != STATUS_NORMAL) {

                    hideMenu();
                } else if (null != mOnItemClickListener) {

                    mOnItemClickListener.onItemClick(child, index, child == mCenterView);
                }
                break;
            }
            index++;
        }
    }

    /**
     * 判断点击事件是否在view中
     *
     * @param view
     * @param event
     * @return
     */
    private boolean isMotionEventInView(View view, MotionEvent event) {
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        int x = location[0];
        int y = location[1];
        boolean isBeyondLeft = event.getRawX() < x;
        boolean isBeyondTop = event.getRawY() < y;
        boolean isBeyondRight = event.getRawX() > (x + view.getMeasuredWidth());
        boolean isBeyondBottom = event.getRawY() > (y + view.getMeasuredHeight());
        return !isBeyondLeft && !isBeyondTop && !isBeyondRight && !isBeyondBottom;
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
        smoothScrollTo((int) dx, 0);
    }

    private void smoothScrollTo(int fx, int fy) {
        int dx = fx - mScroller.getFinalX();
        int dy = fy - mScroller.getFinalY();
        smoothScrollBy(dx, dy);
    }

    private void smoothScrollBy(int dx, int dy) {
        mScroller.startScroll(mScroller.getFinalX(), mScroller.getFinalY(), dx, dy);
        invalidate();
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            postInvalidate();
        }
        super.computeScroll();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int index, boolean isCenterView);
    }
}
