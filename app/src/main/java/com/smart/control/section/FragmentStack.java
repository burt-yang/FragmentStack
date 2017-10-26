package com.smart.control.section;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import java.util.LinkedList;

/**
 *
 */
public final class FragmentStack {
    private LinkedList<Fragment> mStack = new LinkedList<>();
    private FragmentActivity mActivity;
    private FragmentManager mFragmentManager;
    private int mContainerId;

    private FragmentStack(FragmentActivity activity, int containerId) {
        this.mActivity = activity;
        mFragmentManager = activity.getSupportFragmentManager();
        this.mContainerId = containerId;
    }

    /**
     * Create an instance for a specific container.
     */
    public static FragmentStack create(FragmentActivity activity, int containerId) {
        return new FragmentStack(activity, containerId);
    }

    public int size() {
        return mStack.size();
    }

    public Fragment peek() {
        return mStack.peekLast();
    }

    /**
     * Replaces the entire mStack with this fragment.
     */
    public void replace(Fragment fragment, String tag) {
        if (fragment != null) {
            FragmentTransaction transaction = mFragmentManager.beginTransaction();
            clear(transaction);
            push(transaction, fragment, tag);
            transaction.commit();
        }
    }

    /**
     * replace top fragment only
     */
    public void replaceTop(Fragment fragment, String tag) {
        if (fragment != null) {
            FragmentTransaction transaction = mFragmentManager.beginTransaction();
            Fragment topFragment = mStack.pollLast();
            removeFragment(transaction, topFragment);
            showFragment(transaction, fragment, tag);
            transaction.commit();
        }
    }


    /**
     * Adds a new fragment to the mStack and displays it.
     */
    public void push(Fragment fragment, String tag) {
        if (fragment != null) {
            FragmentTransaction transaction = mFragmentManager.beginTransaction();
            showFragment(transaction, fragment, tag);
            mStack.add(fragment);
        }
    }

    private void push(FragmentTransaction transaction, Fragment fragment, String tag) {
        if (fragment != null && !fragment.isAdded()) {
            detachTop(transaction);
            transaction.add(fragment, tag);
            mStack.add(fragment);
        }
    }

    private void detachTop(FragmentTransaction transaction) {
        if (mStack.size() > 0) {
            Fragment f = mStack.peekLast();
            hideFragment(transaction, f);
        }
    }

    /**
     * Removes the fragment at the top of the mStack and displays the previous one. This will not do
     * anything if there is
     * only one fragment in the mStack.
     *
     * @return Whether a transaction has been enqueued.
     */
    public void pop() {
        if (mStack.size() > 1) {
            FragmentTransaction transaction = mFragmentManager.beginTransaction();
            removeFragment(transaction, mStack.pollLast());
            Fragment f = mStack.peekLast();
            showFragment(transaction, f, f.getTag());
            transaction.commit();
        }
    }

    private void showFragment(FragmentTransaction transaction, Fragment fragment, String tag) {
        if (fragment.isAdded()) {
            if (fragment.isHidden()) {
                transaction.show(fragment);
            }
        } else {
            push(transaction, fragment, tag);
        }
    }

    private void hideFragment(FragmentTransaction transaction, Fragment fragment) {
        if (fragment.isAdded() && !fragment.isHidden()) {
            transaction.hide(fragment);
        }
    }

    private void clear(FragmentTransaction transaction) {
        while (mStack.size() > 0) {
            Fragment fragment = mStack.pollLast();
            removeFragment(transaction, fragment);
        }
    }

    private void removeFragment(FragmentTransaction transaction, Fragment fragment) {
        if (fragment != null && fragment.isAdded()) {
            transaction.remove(fragment);
        }
    }
}
