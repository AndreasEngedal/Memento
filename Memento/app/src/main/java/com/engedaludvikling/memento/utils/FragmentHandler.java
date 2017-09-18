package com.engedaludvikling.memento.utils;


import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.transition.Fade;
import android.transition.TransitionInflater;
import android.transition.TransitionSet;
import android.view.View;

import com.engedaludvikling.memento.MainActivity;
import com.engedaludvikling.memento.R;
import com.engedaludvikling.memento.fragments.BaseFragment;

import java.util.ArrayList;

/**
 * Created by Andreas Engedal on 14-09-2017.
 */

public class FragmentHandler {

    private static final long MOVE_DEFAULT_TIME = 1000;
    private static final long FADE_DEFAULT_TIME = 500;
    private static final long MOVE_FAST_TIME = 700;
    private static final long FADE_FAST_TIME = 300;

    private MainActivity mMainActivity;
    private FragmentManager mFragmentManager;

    public FragmentHandler(MainActivity activity) {
        mMainActivity = activity;
        mFragmentManager = mMainActivity.getSupportFragmentManager();
    }

    public FragmentManager getFragmentManager() {
        return mFragmentManager;
    }

    public void startTransactionWithoutBackStack(BaseFragment fragment) {
        mFragmentManager.beginTransaction().replace(R.id.main_content_frame, fragment).commit();
    }

    public void startTransactionWithFading(Context context, BaseFragment nextFragment, ArrayList<View> sharedElements, boolean addToBackStack) {
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();

        BaseFragment previousFragment = (BaseFragment) mFragmentManager.findFragmentById(R.id.main_content_frame);

        // 1. Exit for Previous Fragment
        Fade exitFade = new Fade();
        exitFade.setDuration(FADE_DEFAULT_TIME);
        previousFragment.setExitTransition(exitFade);

        // 2. Shared Elements Transition
        TransitionSet enterTransitionSet = new TransitionSet();
        enterTransitionSet.addTransition(TransitionInflater.from(context).inflateTransition(android.R.transition.move));
        enterTransitionSet.setDuration(MOVE_DEFAULT_TIME);
        enterTransitionSet.setStartDelay(FADE_DEFAULT_TIME);
        nextFragment.setSharedElementEnterTransition(enterTransitionSet);

        // 3. Enter Transition for New Fragment
        Fade enterFade = new Fade();
        enterFade.setStartDelay(MOVE_DEFAULT_TIME + FADE_DEFAULT_TIME);
        enterFade.setDuration(FADE_DEFAULT_TIME);
        nextFragment.setEnterTransition(enterFade);

        for (View element : sharedElements)
            fragmentTransaction.addSharedElement(element, element.getTransitionName());

        fragmentTransaction.replace(R.id.main_content_frame, nextFragment);

        if (addToBackStack)
            fragmentTransaction.addToBackStack(null);

        fragmentTransaction.commitAllowingStateLoss();
    }

    public void startTransactionWithFastFading(BaseFragment newFragment, ArrayList<View> sharedElements, boolean addToBackStack) {
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();

        BaseFragment previousFragment = (BaseFragment) mFragmentManager.findFragmentById(R.id.main_content_frame);

        // 1. Exit for Previous Fragment
        Fade exitFade = new Fade();
        exitFade.setDuration(FADE_FAST_TIME);
        previousFragment.setExitTransition(exitFade);

        // 2. Shared Elements Transition
        TransitionSet enterTransitionSet = new TransitionSet();
        enterTransitionSet.addTransition(TransitionInflater.from(mMainActivity).inflateTransition(android.R.transition.move));
        enterTransitionSet.setDuration(MOVE_FAST_TIME);
        enterTransitionSet.setStartDelay(FADE_FAST_TIME);
        newFragment.setSharedElementEnterTransition(enterTransitionSet);

        // 3. Enter Transition for New Fragment
        Fade enterFade = new Fade();
        enterFade.setStartDelay(MOVE_FAST_TIME + FADE_FAST_TIME);
        enterFade.setDuration(FADE_FAST_TIME);
        newFragment.setEnterTransition(enterFade);

        for (View element : sharedElements)
            fragmentTransaction.addSharedElement(element, element.getTransitionName());

        fragmentTransaction.replace(R.id.main_content_frame, newFragment);

        if (addToBackStack)
            fragmentTransaction.addToBackStack(null);

        fragmentTransaction.commitAllowingStateLoss();
    }
}
