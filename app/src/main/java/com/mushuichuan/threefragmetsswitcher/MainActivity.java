package com.mushuichuan.threefragmetsswitcher;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

public class MainActivity extends AppCompatActivity {
    Fragment f3, f1, f2;
    SwitcherView mView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        setContentView(R.layout.activity_main);
        mView = (SwitcherView) findViewById(R.id.switcherview);
        f1 = new BlankFragment1();
        f2 = new BlankFragment2();
        f3 = new BlankFragment3();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.child_middle, f1);
        transaction.replace(R.id.child_left, f2);
        transaction.replace(R.id.child_right, f3);
        transaction.commit();
    }

}
