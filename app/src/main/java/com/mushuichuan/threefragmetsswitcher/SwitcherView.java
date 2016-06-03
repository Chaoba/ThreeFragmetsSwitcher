package com.mushuichuan.threefragmetsswitcher;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import static java.lang.Math.abs;

/**
 * Created by Liyanshun on 2016/5/24.
 */
public class SwitcherView extends RelativeLayout {
    private static final String TAG = "SwitcherView";
    private final float middleProportion;
    private final int sideMarginTopAndDown, middleMarginTopAndDown, middleMarginLeftAndRight;
    private FrameLayout mChildMiddle, mChildLeft, mChildRight;
    private LayoutParams mMiddleParam, mLeftParam, mRightParam;
    private float startX;
    private float endX;
    private int middleLeft;
    private int middleRight;
    private final int CLICK_THRESHOLD = 50;

    public SwitcherView(Context context) {
        this(context, null);
    }

    public SwitcherView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SwitcherView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray mTypedArray = context.obtainStyledAttributes(attrs, R.styleable.SwticherView);
        middleProportion = mTypedArray.getFloat(R.styleable.SwticherView_middleProportion, 0.75f);
        sideMarginTopAndDown = mTypedArray.getDimensionPixelSize(R.styleable.SwticherView_sideMarginTopAndDown, 0);
        middleMarginTopAndDown = mTypedArray.getDimensionPixelSize(R.styleable.SwticherView_middleMarginTopAndDown, 0);
        middleMarginLeftAndRight = mTypedArray.getDimensionPixelSize(R.styleable.SwticherView_middleMarginLeftAndRight, 0);
        initChildView();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            if (ev.getX() < middleLeft || ev.getX() > middleRight)
                return true;
        }
        return false;
    }
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        Log.i(TAG, "dispatchTouchEvent:" + event.toString());
        boolean handled = super.dispatchTouchEvent(event);
        Log.i(TAG, "handled:" + handled);

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                startX = event.getX();
                Log.i(TAG, "startx:" + startX);
            }
            break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP: {
                endX = event.getX();
                Log.i(TAG, "endX:" + endX);
                if (abs(endX - startX) < CLICK_THRESHOLD) {
                    //点击事件
                    if (startX < middleLeft) {
                        switchLeftAndMiddle();
                    } else if (startX > middleRight) {
                        switchRightAndMiddle();
                    }
                } else {
                    //滑动事件
                    if (endX > startX) {
                        switchRightAndMiddle();
                    } else if (endX < startX) {
                        switchLeftAndMiddle();
                    }
                }
                break;
            }
        }
        return true;
    }
    void initChildView() {
        mChildMiddle = (FrameLayout) findViewById(R.id.child_middle);
        mChildLeft = (FrameLayout) findViewById(R.id.child_left);
        mChildRight = (FrameLayout) findViewById(R.id.child_right);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (mChildMiddle == null) {
            initChildView();
        }
        int middleWidth = (int) (r * middleProportion);
        middleLeft = (r - middleWidth) / 2;
        middleRight = r - (r - middleWidth) / 2;
        mChildMiddle.layout(middleLeft, t + middleMarginTopAndDown, middleRight, b - middleMarginTopAndDown);


        int leftRight = middleLeft - middleMarginLeftAndRight;
        int leftLeft = -(middleWidth - leftRight);
        mChildLeft.layout(leftLeft, t + sideMarginTopAndDown, leftRight, b - sideMarginTopAndDown);

        int rightLeft = middleRight + middleMarginLeftAndRight;
        int rightRight = rightLeft + middleWidth;
        mChildRight.layout(rightLeft, t + sideMarginTopAndDown, rightRight, b - sideMarginTopAndDown);

        mMiddleParam = (LayoutParams) mChildMiddle.getLayoutParams();
        mLeftParam = (LayoutParams) mChildLeft.getLayoutParams();
        mRightParam = (LayoutParams) mChildRight.getLayoutParams();
    }

    public void switchLeftAndMiddle() {
        Animation leftInAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.slide_left_in);
        mChildLeft.startAnimation(leftInAnimation);
        Animation leftOutAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.slide_left_out);
        mChildMiddle.startAnimation(leftOutAnimation);
        leftOutAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mChildMiddle.setLayoutParams(mLeftParam);
                mChildLeft.setLayoutParams(mMiddleParam);
                FrameLayout temp = mChildMiddle;
                mChildMiddle = mChildLeft;
                mChildLeft = temp;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }

    public void switchRightAndMiddle() {
        Animation leftInAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.slide_right_in);
        mChildRight.startAnimation(leftInAnimation);
        Animation rightOutAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.slide_right_out);
        mChildMiddle.startAnimation(rightOutAnimation);
        rightOutAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mChildMiddle.setLayoutParams(mRightParam);
                mChildRight.setLayoutParams(mMiddleParam);
                FrameLayout temp = mChildMiddle;
                mChildMiddle = mChildRight;
                mChildRight = temp;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }
}
