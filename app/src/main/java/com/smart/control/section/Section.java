package com.smart.control.section;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;

/**
 * @author:dongpo 创建时间: 9/2/2016
 * 描述:
 * 修改:
 */
public class Section extends FrameLayout {
    private int mContainId;
    private boolean mAddState = false;
    public static final int STATE_NO_ADD = -1;
    public static final int STATE_ON_ATTACH = 1;
    public static final int STATE_ON_CREATE = 3;
    public static final int STATE_ON_START = 5;
    public static final int STATE_ON_RESUME = 7;

    public static final int STATE_ON_PAUSE = 9;
    public static final int STATE_ON_STOP = 11;
    public static final int STATE_ON_DESTROY = 13;


    public static final int STATE_ON_RESTART = 4;

    private int mLifeState = STATE_NO_ADD;
    public Activity mAttachActivity;
    private ViewGroup mContainerView;
    /**
     * 是否在回退栈中
     */
    private boolean mIsBackStack;
    /**
     * 是否处于正在销毁的状态
     */
    private boolean mIsFinishing;
    private View mContentView;
    /**
     * 是否处于显示状态，与声明周期无关
     */
    private boolean mShowState;
    private int mLeftEnterAnim;
    private int mRightExitAnim;
    private int mLeftExitAnim;
    private int mRightEnterAnim;

    public Section(Context context) {
        super(context);
    }

    public Section(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void onAttach(Activity activity) {
        mAttachActivity = activity;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return null;
    }

    public void onCreate(Bundle savedInstanceState){

    }

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState){

    }

    public void onActivityCreated(){

    }

    public void onStart() {

    }

    public void onResume() {
    }

    public void onPause() {
    }

    public void onRestart() {
    }

    public void onStop() {
    }

    public void onDetach() {
    }

    public void onDestroy() {
    }

    public void onDestroyView(){
    }

    public void onShow() {
        onHiddenChanged(true);
    }

    public void onHide() {
        onHiddenChanged(false);
    }

    public void onHiddenChanged(boolean hidden){

    }

    /**
     *
     * @param leftEnterAnim
     * @param rightExitAnim
     * @param leftExitAnim
     * @param rightEnterAnim
     */
    public void overridePendingTransition(int leftEnterAnim, int rightExitAnim, int leftExitAnim, int rightEnterAnim) {
        mLeftEnterAnim = leftEnterAnim;
        mRightExitAnim = rightExitAnim;
        mLeftExitAnim = leftExitAnim;
        mRightEnterAnim = rightEnterAnim;
    }

    public View getContentView() {
        return mContentView;
    }

    public void setContainId(int containId) {
        mContainId = containId;
    }

    public int getContainId() {
        return mContainId;
    }

    public ViewGroup getContainerView() {
        if (mContainerView == null) {
            mContainerView = (ViewGroup) mAttachActivity.findViewById(mContainId);
        }
        return mContainerView;
    }

    public View getView(){
        return mContentView;
    }

    public boolean isAdded() {
        return mContainId > 0 && mAddState;
    }

    public boolean isResume() {
        return mContainId > 0 && mLifeState == STATE_ON_RESUME;
    }

    public boolean isStart() {
        return mContainId > 0 && mLifeState == STATE_ON_START;
    }

    public boolean isReStart() {
        return mContainId > 0 && mLifeState == STATE_ON_RESTART;
    }

    public boolean isPause() {
        return mContainId > 0 && mLifeState == STATE_ON_PAUSE;
    }

    public boolean isStop() {
        return mContainId > 0 && mLifeState == STATE_ON_STOP;
    }

    public boolean isDestroy() {
        return mContainId > 0 && mLifeState == STATE_ON_DESTROY;
    }

    public boolean isAttach() {
        return mContainId > 0 && mLifeState == STATE_ON_ATTACH;
    }

    public boolean isDetach() {
        return mContainId > 0 && mLifeState == STATE_ON_DESTROY;
    }

    public boolean isShow() {
        return mShowState;
    }

    public boolean isHide() {
        return !mShowState;
    }

    public void setShowState(boolean showState) {
        mShowState = showState;
    }

    public void setAddState(boolean addState) {
        mAddState = addState;
    }

    public boolean isBackStack() {
        return mIsBackStack;
    }

    public void setBackStack(boolean isBackStack) {
        mIsBackStack = isBackStack;
    }

    public int getCurrentLifeState() {
        return mLifeState;
    }

    public void setCurrentLifeState(int lifeState) {
        mLifeState = lifeState;
    }

    public boolean isFinishing() {
        return mIsFinishing;
    }

    public void setFinishing(boolean isFinishing) {
        mIsFinishing = isFinishing;
    }

    public void setContentView(View contentView) {
        mContentView = contentView;
    }

    public Animation getLeftEnterAnimation() {
        if (mLeftEnterAnim > 0) {
            return AnimationUtils.loadAnimation(getContext(), mLeftEnterAnim);
        }
        return null;
    }

    public Animation getRightExitAnimation() {
        if (mRightExitAnim > 0) {
            return AnimationUtils.loadAnimation(getContext(), mRightExitAnim);
        }
        return null;
    }

    public Animation getLeftExitAnimation() {
        if (mRightExitAnim > 0) {
            return AnimationUtils.loadAnimation(getContext(), mLeftExitAnim);
        }
        return null;
    }

    public Animation getRightEnterAnimation() {
        if (mRightExitAnim > 0) {
            return AnimationUtils.loadAnimation(getContext(), mRightEnterAnim);
        }
        return null;
    }

    public void onSaveInstanceState(Bundle outState) {

    }

    public String getString(int resId){
        return mAttachActivity.getString(resId);
    }
}
