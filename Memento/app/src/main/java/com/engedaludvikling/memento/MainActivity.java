package com.engedaludvikling.memento;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.engedaludvikling.memento.fragments.BaseFragment;
import com.engedaludvikling.memento.fragments.start.LogInFragment;
import com.engedaludvikling.memento.fragments.start.StartFragment;
import com.engedaludvikling.memento.utils.FirebaseAuthHandler;
import com.engedaludvikling.memento.utils.FragmentHandler;

import java.util.ArrayList;

import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    private final int startTimer = 2000;

    FirebaseAuthHandler mFirebaseAuthHandler;
    FragmentHandler mFragmentHandler;

    private Handler mDelayedTransactionHandler = new Handler();
    private Runnable mRunnable = this::performTransition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        mFirebaseAuthHandler = FirebaseAuthHandler.getFirebaseAuthHandler();
        mFragmentHandler = new FragmentHandler(this);

        mFragmentHandler.startTransactionWithoutBackStack(new StartFragment());
        mDelayedTransactionHandler.postDelayed(mRunnable, startTimer);
    }

    private void performTransition() {
        if (isDestroyed()) {
            return;
        }

        BaseFragment previousFragment = (BaseFragment) mFragmentHandler.getFragmentManager().findFragmentById(R.id.main_content_frame);
        BaseFragment nextFragment = LogInFragment.newInstance();

        ArrayList<View> sharedElements = new ArrayList<>();
        sharedElements.add(ButterKnife.findById(this, R.id.start_logo));

        mFragmentHandler.startTransactionWithFading(this, previousFragment, nextFragment, sharedElements, false);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mFirebaseAuthHandler.addAuthStateListener();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mFirebaseAuthHandler.removeAuthStateListener();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDelayedTransactionHandler.removeCallbacks(mRunnable);
    }
}
