package com.mushuichuan.threefragmetsswitcher;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

public class MainActivity2 extends AppCompatActivity {
    private int width;
    private float x;
    private final int MARGIN = 100;

    private boolean isSideClick(float x) {
        return x < MARGIN || x > width - MARGIN;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        final ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
        viewPager.setPageTransformer(true,new ZoomPageTransformer());
        viewPager.setOffscreenPageLimit(2);
        viewPager.setAdapter(new SelfDefineFragmentPageAdapter(getFragmentManager()));

        viewPager.setCurrentItem(1);
        RelativeLayout body = (RelativeLayout) findViewById(R.id.body);
        x = body.getWidth();
        if (body != null) {
            body.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent ev) {
                    if (ev.getAction() == MotionEvent.ACTION_DOWN) {
                        if (isSideClick(ev.getX())) {
                            x = ev.getX();
                        }
                    } else if (ev.getAction() == MotionEvent.ACTION_UP) {
                        if (isSideClick(ev.getX())) {
                            int currentItem = viewPager.getCurrentItem();
                            if (ev.getX() < MARGIN) {
                                viewPager.setCurrentItem(currentItem - 1, true);
                            } else if (ev.getX() > width - MARGIN) {
                                viewPager.setCurrentItem(currentItem + 1, true);
                            }
                        }
                    }
                    return true;
                }
            });

        }
    }


    class SelfDefineFragmentPageAdapter extends FragmentPagerAdapter {
        public SelfDefineFragmentPageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position % 3) {
                case 0:
                    return new BlankFragment1();
                case 1:
                    return new BlankFragment2();
                case 2:
                    return new BlankFragment3();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 6;
        }
    }
}
