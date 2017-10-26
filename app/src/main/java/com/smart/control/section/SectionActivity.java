package com.smart.control.section;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * @author:dongpo 创建时间: 9/2/2016
 * 描述:
 * 修改:
 */
public class SectionActivity extends AppCompatActivity {

    private SectionManager mSectionManager;
    private int mActivityState = 0;

    @Override
    protected final void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSectionManager = new SectionManager(this);
        mActivityState = Section.STATE_ON_CREATE;
        onActivityCreate(savedInstanceState);
        mSectionManager.onCreate(savedInstanceState);
    }

    protected void onActivityCreate(@Nullable Bundle savedInstanceState) {
    }

    protected SectionManager getSectionManager(){
        return mSectionManager;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mActivityState = Section.STATE_ON_RESUME;
        mSectionManager.dispatchLifeRecycle(mActivityState);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        mActivityState = Section.STATE_ON_RESTART;
        mSectionManager.dispatchLifeRecycle(mActivityState);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mActivityState = Section.STATE_ON_START;
        mSectionManager.dispatchLifeRecycle(mActivityState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mActivityState = Section.STATE_ON_DESTROY;
        mSectionManager.dispatchLifeRecycle(mActivityState);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mActivityState = Section.STATE_ON_PAUSE;
        mSectionManager.dispatchLifeRecycle(mActivityState);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mActivityState = Section.STATE_ON_STOP;
        mSectionManager.dispatchLifeRecycle(mActivityState);
    }

    public int getActivityCurrentState(){
        return mActivityState;
    }

    @Override
    public void onBackPressed() {
        if (mSectionManager.hasBackStack()) {
            mSectionManager.popStack();
        }else{
            super.onBackPressed();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mSectionManager.onSaveInstanceState(outState);
    }
}
