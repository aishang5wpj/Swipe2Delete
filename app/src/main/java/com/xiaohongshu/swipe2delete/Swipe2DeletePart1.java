package com.xiaohongshu.swipe2delete;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;


/**
 * Created by wupengjian on 16/11/9.
 * <p/>
 * http://blog.csdn.net/aishang5wpj/article/details/54093911
 */
public class Swipe2DeletePart1 extends ViewGroup {

    private View mCenterView;

    public Swipe2DeletePart1(Context context) {
        this(context, null);
    }

    public Swipe2DeletePart1(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Swipe2DeletePart1(Context context, AttributeSet attrs, int defStyleAttr) {
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
}
