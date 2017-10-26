package com.smart.control.section;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {


    private FragmentStack mFragmentStack;
    public static final String KEY_COUNT = "count";
    private int mCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFragmentStack = FragmentStack.create(getSupportFragmentManager(), R.id.fl_container);
        findViewById(R.id.btn_push).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCount++;
                TextFragment fragment = new TextFragment();
                Bundle args = new Bundle();
                args.putInt(KEY_COUNT, mCount);
                fragment.setArguments(args);
                mFragmentStack.push(fragment, null);
            }
        });

        findViewById(R.id.btn_pop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mFragmentStack.hasBackStack()) {
                    mCount--;
                    mFragmentStack.pop();
                }
            }
        });

        findViewById(R.id.btn_replace).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mFragmentStack.replace(new TextFragment3(), null);
            }
        });

        findViewById(R.id.btn_clear).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mFragmentStack.clear();
            }
        });
    }
}
