package com.mushuichuan.threefragmetsswitcher;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

/**
 * Created by Liyanshun on 2016/5/24.
 */
public class SwitcherView extends RelativeLayout {
    private static final String TAG = "SwitcherView";
    private View mBodyLayout;
    private FrameLayout mChildMiddle, mChildLeft, mChildRight;
    private LayoutParams mMiddleParam, mLeftParam, mRightParam;
    private OnClickListener mLeftListener, mRightListener;
    private float startX;
    private float endX;

    public SwitcherView(Context context) {
        super(context);
        initChildView();
    }

    public SwitcherView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initChildView();
    }

    public SwitcherView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initChildView();
    }

    void initChildView() {
        mLeftListener = new OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "left click");
                switchLeftAndMiddle();
            }
        };
        mRightListener = new OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "right click");
                switchRightndMiddle();
            }
        };
        mBodyLayout = LayoutInflater.from(getContext()).inflate(R.layout.body_view, null, false);
        mChildMiddle = (FrameLayout) mBodyLayout.findViewById(R.id.child_middle);
        mChildLeft = (FrameLayout) mBodyLayout.findViewById(R.id.child_left);
        mChildRight = (FrameLayout) mBodyLayout.findViewById(R.id.child_right);
        addView(mBodyLayout);
        mMiddleParam = (LayoutParams) mChildMiddle.getLayoutParams();
        mLeftParam = (LayoutParams) mChildLeft.getLayoutParams();
        mRightParam = (LayoutParams) mChildRight.getLayoutParams();

        resetListener();
    }

    void resetListener() {
        mChildMiddle.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.d(TAG, event.toString());
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        startX = event.getX();
                        Log.d(TAG, "startx:" + startX);
                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                        endX = event.getX();
                        Log.d(TAG, "endX:" + endX);
                        if (endX > startX && endX > (startX + 100)) {
                            switchRightndMiddle();
                            return true;
                        } else if (endX < startX && endX < (startX - 100)) {
                            switchLeftAndMiddle();
                            return true;
                        }
                        break;
                    }
                }
                return false;
            }
        });
        mChildLeft.setOnTouchListener(null);
        mChildRight.setOnTouchListener(null);
        mChildMiddle.setOnClickListener(null);
        mChildLeft.setOnClickListener(mLeftListener);
        mChildRight.setOnClickListener(mRightListener);
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
                resetListener();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }

    public void switchRightndMiddle() {
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
                resetListener();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });


    }
}
