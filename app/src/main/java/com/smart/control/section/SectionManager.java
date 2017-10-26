package com.smart.control.section;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * @author:dongpo 创建时间: 9/2/2016
 * 修改:
 */
public class SectionManager {
    /**
     * 与之关联的activity
     */
    private final SectionActivity mAttachActivity;
    /**
     * section回退栈
     */
    private final Stack<Section> mSectionStack;
    /**
     * 已经添加过的section集合，在回退栈中的section必须也在此集合中
     */
    private final List<Section> mSectionList;
    /**
     * activity被回收后重新建立的数据
     */
    private Bundle mSavedInstanceState;
    /**
     * 是否正在执行进入动画
     */
    private boolean mEnterAnimStart = false;
    /**
     * 是否正在制定离场动画
     */
    private boolean mExitAnimStart = false;

    public SectionManager(SectionActivity attachActivity) {
        mAttachActivity = attachActivity;
        mSectionStack = new Stack<>();
        mSectionList = new ArrayList<>();
    }

    /**
     * 从回退栈弹出，存在情况 UI上的没有加入到回退栈，
     * 回退栈定义：只加入回退栈后可以回退到该页面，不加入的则不能回到，但不加入到回退栈的页面可以回退到加入回退栈的页面
     */
    public void popStack() {
        if (isAnimationStart()) {
            Log.d("section", "animation is start, will wait the animation done, then do next action");
            return;
        }

        Section section = getTopShowSectionFromList();
        //获取回退栈中的最后一个元素，并弹出
        if (section != null) {
            remove(section.getContainId(), section);
        }
    }

    /**
     * 将一个section放入到一个回退栈中
     *
     * @param section
     */
    public void putStack(Section section) {
        if (section != null && section.isAdded()) {
            mSectionStack.push(section);
            section.setBackStack(true);
        }
    }

    /**
     * 将一个section加入到集合中
     *
     * @param section
     */
    private void addSectionToAddList(Section section) {
        if (section != null) {
            mSectionList.add(section);
        }
    }

    /**
     * 根据tag找到对应的section 就是将tag放到view.setTag()成员变量中
     *
     * @param tag
     * @return
     */
    public Section findSectionByTag(String tag) {
        if (tag == null) {
            return null;
        }
        for (int i = 0; i < mSectionStack.size(); i++) {

            Section section = mSectionStack.get(i);
            String sectionTag = null;

            Object targetTag = section.getTag();
            if (targetTag instanceof String) {
                sectionTag = (String) targetTag;
            }
            if (TextUtils.equals(tag, sectionTag)) {
                return section;
            }
        }
        return null;
    }

    /**
     * 添加一个section，并同步在view显示
     *
     * @param containId    容器的resource id 容器必须为Framelayout
     * @param newSection   需要添加的section
     * @param preSection   前一个section，一般为上一个正在显示的section，主要用于过度动画使用
     * @param tag          给newSection 加入一个tag
     * @param addBackStack 是否加入到回退栈
     */
    private void add(int containId, Section newSection, Section preSection, String tag, boolean addBackStack) {
        if (isAnimationStart()) {
            Log.d("section", "animation is start, will wait the animation done, then do next action");
            return;
        }

        checkNull(newSection);
        checkContainerId(containId);

        newSection.setContainId(containId);
        //将containId 设置给section
        if (!TextUtils.isEmpty(tag)) {
            newSection.setTag(tag);
        }
        //将section加入到集合中
        addSectionToAddList(newSection);
        //标记section已经加入到集合中
        newSection.setAddState(true);
        newSection.setBackStack(addBackStack);

        if (preSection != null) {
            executeLifeRecycle(preSection, Section.STATE_ON_RESUME);
            //如果上一个正在显示的section不为null，则需要调整生命周期为onStop
            executeLifeRecycle(preSection, Section.STATE_ON_STOP);
        }
        //新添加的section，调整生命周期为当前activity的生命周期
        executeLifeRecycle(newSection, mAttachActivity.getActivityCurrentState());

        //展示section
        showSection(containId, newSection, preSection, true);
    }

    public void add(int containId, Section section, boolean addBackStack) {
        Section topShowSection = getTopShowSectionFromList(containId);
        //获取在添加新的section前，正在显示的section
        if (topShowSection == null) {
            topShowSection = new DefaultSection(mAttachActivity);
            replace(containId, topShowSection, true);
        }

        if(topShowSection instanceof DefaultSection){
            //如果现在展示的为空白view，则再进行添加的时候，需将其加入回退栈
            topShowSection.setBackStack(true);
        }

        add(containId, section, topShowSection, null, addBackStack);
    }

    /**
     * 从添加列表中移除一个section
     *
     * @param section
     */
    public void remove(int containId, Section section) {
        if (isAnimationStart()) {
            Log.d("section", "animation is start, will wait the animation done, then do next action");
            return;
        }
        checkNull(section);
        checkContainerId(containId);

        if (!section.isAdded()) {
            Log.d("section", "section is not add, will not do the remove action");
            return;
        }

        //如果section为当前正在运行的section，则需要调整UI
        if (section.isResume() && section.isShow()) {
            //获取当前栈顶前一个的section，可能为null
            Section topShowSection = getTopSectionFromStack(containId);
            remove(containId, section, topShowSection);
        } else {
            //如果不在运行状态，直接进行移除
            deleteSectionFromList(section);
        }

    }

    /**
     * 从内存中清除一个section
     *
     * @param section
     */
    private void deleteSectionFromList(Section section) {
        if (section.isAdded()) {
            mSectionList.remove(section);
            section.setAddState(false);
        }

        if (section.isBackStack()) {
            mSectionStack.remove(section);
            section.setBackStack(false);
        }
    }

    /**
     * 从添加列表中移除一个section
     *
     * @param section
     */
    private void remove(int containId, Section section, Section preSection) {
        remove(containId, section, preSection, true);
    }

    /**
     * 从添加列表中移除一个section
     *
     * @param containId  section 对应的Framelayout resource id
     * @param section    需要remove掉的section
     * @param preSection 需要回退显示的section
     * @param withAnim   是否需要伴随动画显示
     */
    private void remove(int containId, Section section, Section preSection, boolean withAnim) {
        mSectionList.remove(section);
        section.setAddState(false);
        section.setFinishing(true);
        executeLifeRecycle(section, Section.STATE_ON_DESTROY);
        if (preSection != null) {
            removeSectionIfInBackStack(preSection);
            executeLifeRecycle(preSection, Section.STATE_ON_RESTART);
            executeLifeRecycle(preSection, Section.STATE_ON_START);
            executeLifeRecycle(preSection, Section.STATE_ON_RESUME);
        }

        if (withAnim) {
            hideSection(containId, section, preSection);
        }
    }

    /**
     * 将这个section显示出来
     *
     * @param containId
     * @param newSection
     * @param preSection
     */
    private void showSection(int containId, Section newSection, Section preSection, boolean addBackStack) {
        View containerView = mAttachActivity.findViewById(containId);
        if (containerView instanceof FrameLayout) {
            FrameLayout container = (FrameLayout) containerView;
            addViewWithAnimation(container, newSection, preSection, addBackStack);
        } else {
            throw new IllegalArgumentException("container must be FrameLayout");
        }
    }

    /**
     * 将这个section显示出来
     *
     * @param containId
     * @param newSection
     * @param preSection
     */
    private void hideSection(int containId, Section newSection, Section preSection) {
        View containerView = mAttachActivity.findViewById(containId);
        if (containerView instanceof FrameLayout) {
            FrameLayout container = (FrameLayout) containerView;
            removeViewWithAnimation(container, newSection, preSection);
        } else {
            throw new IllegalArgumentException("container must be FrameLayout");
        }
    }


    /**
     * @param section state:显示不修改集合中的顺序和个数，只是修改UI状态，生命周期没有影响，会影响状态
     *                condition:当前这个section必须在onResume或者onPause状态
     */
    public void show(Section section, boolean addBackStack) {
        if (isAnimationStart()) {
            Log.d("section", "animation is start, will wait the animation done, then do next action");
            return;
        }

        checkNull(section);
        checkIfAdded(section);

        if (section.isShow()) {
            Log.d("section", "the section is already in show state, will not show again");
            return;
        }

        section.setBackStack(addBackStack);
        section.onShow();
        int containId = section.getContainId();
        Section topShowSection = getTopShowSectionFromList(containId);
        showSection(containId, section, topShowSection, false);
    }

    public void show(Section section) {
        show(section, false);
    }

    /**
     * @param section state:隐藏不修改集合中的顺序和个数，只是修改UI状态
     *                condition:当前这个section必须在onResume或者onPause状态
     */
    public void hide(Section section) {
        if (isAnimationStart()) {
            Log.d("section", "animation is start, will wait the animation done, then do next action");
            return;
        }
        checkNull(section);
        checkIfAdded(section);
        int containId = section.getContainId();
        checkContainerId(containId);
        if (section.isHide()) {
            Log.d("section", "section is already in hide state, will not take hide action");
            return;
        }

        Section beforeTopShowSection = getTopSectionFromStack(containId);
        section.onHide();
        hideSection(containId, section, beforeTopShowSection);
    }

    /**
     * @param section state:取代一个section，会影响栈的顺序，不会影响个数
     *                condition:当前这个section必须在onResume或者onPause状态
     *                1.获取之前的section，调用finish 队列中移除
     *                2.把现在的section加入
     */
    private void replace(int containId, Section section, boolean addBackStack, String tag) {
        if (isAnimationStart()) {
            Log.d("section", "animation is start, will wait the animation done, then do next action");
            return;
        }
        //先获取当前正在显示的section
        Section topShowSection = getTopShowSectionFromList(containId);
        add(containId, section, topShowSection, tag, addBackStack);
        if (topShowSection != null) {
            topShowSection.setBackStack(false);
            executeLifeRecycle(topShowSection, Section.STATE_ON_DESTROY);
            remove(containId, topShowSection, null, false);
        }
    }

    public void replace(int containId, Section section, boolean addBackStack) {
        replace(containId, section, addBackStack, null);
    }

    /**
     * 获取指定container顶部的section
     *
     * @param containerId
     * @return
     */
    public Section getTopShowSectionFromList(int containerId) {
        for (int i = mSectionList.size() - 1; i >= 0; i--) {
            Section section = mSectionList.get(i);
            if (section.getContainId() == containerId) {
                if (!section.isFinishing() && section.isShow()) {
                    return section;
                }
            }
        }
        return null;
    }

    /**
     * 获取指定container顶部的section
     *
     * @return
     */
    public Section getTopShowSectionFromList() {
        for (int i = mSectionList.size() - 1; i >= 0; i--) {
            Section section = mSectionList.get(i);
            if (section.isResume() && !section.isFinishing() && section.isShow()) {
                return section;
            }
        }
        return null;
    }

    /**
     * 获取指定container中在回退栈中的section
     *
     * @param containerId
     * @return
     */
    public Section getTopSectionFromStack(int containerId) {
        for (int i = mSectionStack.size() - 1; i >= 0; i--) {
            Section section = mSectionStack.get(i);
            if (section.getContainId() == containerId) {
                if (section.isStop() && !section.isFinishing()) {
                    return section;
                }
            }
        }
        return null;
    }

    public void removeSectionIfInBackStack(Section section) {
        if (section.isBackStack()) {
            mSectionStack.remove(section);
            section.setBackStack(false);
        }
    }


    private FrameLayout.LayoutParams getDefaultLayoutParam() {
        return new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    }

    public void checkNull(Object object) {
        if (object == null) {
            throw new NullPointerException("param can't be null");
        }
    }

    public void checkContainerId(int containerId) {
        if (containerId <= 0) {
            throw new IllegalArgumentException("containerId can't be found");
        }
    }

    private void checkIfAdded(Section section) {
        if (!section.isAdded()) {
            throw new IllegalArgumentException("section is not added before");
        }
    }


    public void onCreate(@Nullable Bundle savedInstanceState) {
        mSavedInstanceState = savedInstanceState;
        dispatchLifeRecycle(Section.STATE_ON_CREATE);
    }

    public void onSaveInstanceState(Bundle outState) {
        for (int i = 0; i < mSectionList.size(); i++) {
            Section section = mSectionList.get(i);
            section.onSaveInstanceState(outState);
        }
    }

    public void dispatchLifeRecycle(int activityState) {
        for (int i = 0; i < mSectionList.size(); i++) {
            Section section = mSectionList.get(i);
            if (section.isShow()) {
                executeLifeRecycle(section, activityState);
            }
        }
    }

    /**
     * @param section Section 生命周期路径
     *                1. onAttach - onCreateView - onStart - onResume
     *                2. onResume - onPause
     *                3. onResume - onPause - onStop
     *                4. onPause - onResume
     *                5. onPause - onStop -  11 - 4 -5 - 7
     */
    private void executeLifeRecycle(Section section, int activityState) {
        int sectionState = section.getCurrentLifeState();
        while (activityState != sectionState) {
            if (activityState - sectionState >= 2) {
                sectionState += 2;
            } else if (activityState - sectionState <= -2) {
                sectionState -= 2;
            } else if (activityState % 2 != 0) {
                if (activityState > sectionState) {
                    sectionState++;
                } else {
                    sectionState--;
                }
            }

            if (activityState == Section.STATE_ON_RESTART) {
                sectionState = Section.STATE_ON_RESTART;
            }
            switch (sectionState) {
                case Section.STATE_ON_ATTACH:
                    section.onAttach(mAttachActivity);
                    break;
                case Section.STATE_ON_CREATE:
                    section.onCreate(mSavedInstanceState);
                    View contentView = section.onCreateView(mAttachActivity.getLayoutInflater(), section.getContainerView(), mSavedInstanceState);
                    checkNull(contentView);
                    section.setContentView(contentView);
                    section.addView(contentView, getDefaultLayoutParam());
                    section.onViewCreated(contentView, mSavedInstanceState);
                    section.onActivityCreated();
                    break;
                case Section.STATE_ON_START:
                    section.onStart();
                    break;
                case Section.STATE_ON_RESUME:
                    section.onResume();
                    break;
                case Section.STATE_ON_PAUSE:
                    section.onPause();
                    break;
                case Section.STATE_ON_STOP:
                    section.onStop();
                    break;
                case Section.STATE_ON_RESTART:
                    section.onRestart();
                    break;
                case Section.STATE_ON_DESTROY:
                    section.onDestroyView();
                    section.onDestroy();
                    section.onDetach();
                    deleteSectionFromList(section);
                    section.setFinishing(false);
                    //检查此时section是否还有其他引用
                    break;
            }
            section.setCurrentLifeState(sectionState);
        }
    }

    public boolean hasBackStack() {
        return mSectionStack.size() > 0;
    }

    public void addViewWithAnimation(final FrameLayout container, final Section newSection, final Section preSection, final boolean addBackStack) {
        newSection.setVisibility(View.INVISIBLE);
        container.addView(newSection, getDefaultLayoutParam());
        container.post(new Runnable() {
            @Override
            public void run() {

                Animation enterAnimation = newSection.getRightEnterAnimation();
                if (enterAnimation != null) {
                    //如果有设置动画，执行动画
                    enterAnimation.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                            newSection.setVisibility(View.VISIBLE);
                            mEnterAnimStart = true;
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            //动画结束，设置可见状态
                            newSection.setShowState(true);
                            mEnterAnimStart = false;
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                    newSection.startAnimation(enterAnimation);
                } else {
                    //如果没有设置动画，则直接显示
                    newSection.setVisibility(View.VISIBLE);
                    newSection.setShowState(true);
                    mEnterAnimStart = false;
                }

                if (preSection != null) {
                    Animation exitAnimation = preSection.getLeftExitAnimation();
                    if (exitAnimation != null) {
                        exitAnimation.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {
                                mExitAnimStart = true;
                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                container.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        container.removeView(preSection);
                                        preSection.setShowState(false);
                                        if (addBackStack && preSection.isBackStack()) {
                                            putStack(preSection);
                                        }
                                        mExitAnimStart = false;
                                    }
                                });

                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {

                            }
                        });
                        preSection.startAnimation(exitAnimation);
                    } else {
                        container.removeView(preSection);
                        preSection.setShowState(false);
                        if (addBackStack && preSection.isBackStack()) {
                            putStack(preSection);
                        }
                        mExitAnimStart = false;
                    }
                }
            }
        });
    }

    public void removeViewWithAnimation(final FrameLayout container, final Section currentSection, final Section lastSection) {
        if (lastSection != null) {
            lastSection.setVisibility(View.INVISIBLE);
            ViewGroup parent = (ViewGroup) lastSection.getParent();
            if (parent != null) {
                parent.removeView(lastSection);
            }
            container.addView(lastSection, getDefaultLayoutParam());
        }

        container.post(new Runnable() {
            @Override
            public void run() {
                if (lastSection != null) {
                    Animation enterAnimation = lastSection.getLeftEnterAnimation();
                    if (enterAnimation != null) {
                        enterAnimation.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {
                                lastSection.setVisibility(View.VISIBLE);
                                mEnterAnimStart = true;
                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                lastSection.setShowState(true);
                                mEnterAnimStart = false;
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {

                            }
                        });
                        lastSection.startAnimation(enterAnimation);
                    } else {
                        lastSection.setVisibility(View.VISIBLE);
                        lastSection.setShowState(true);
                        mEnterAnimStart = false;
                    }
                }

                Animation exitAnimation = currentSection.getRightExitAnimation();
                if (exitAnimation != null) {
                    exitAnimation.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                            mExitAnimStart = true;
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            container.post(new Runnable() {
                                @Override
                                public void run() {
                                    container.removeView(currentSection);
                                    currentSection.setShowState(false);
                                    mExitAnimStart = false;
                                }
                            });
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                    currentSection.startAnimation(exitAnimation);
                } else {
                    container.removeView(currentSection);
                    currentSection.setShowState(false);
                    mExitAnimStart = false;
                }
            }
        });
    }

    private boolean isAnimationStart() {
        return mEnterAnimStart || mExitAnimStart;
    }

    public class DefaultSection extends Section {
        public DefaultSection(Context context) {
            super(context);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            FrameLayout defaultView = new FrameLayout(getContext());
            defaultView.setLayoutParams(getDefaultLayoutParam());
            return defaultView;
        }
    }

}
