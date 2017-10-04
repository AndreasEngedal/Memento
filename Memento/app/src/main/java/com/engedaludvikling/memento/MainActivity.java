package com.engedaludvikling.memento;

import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.engedaludvikling.memento.fragments.MainMenuFragment;
import com.engedaludvikling.memento.fragments.start.LogInFragment;
import com.engedaludvikling.memento.fragments.start.StartFragment;
import com.engedaludvikling.memento.utils.FirebaseAuthHandler;
import com.engedaludvikling.memento.utils.FragmentHandler;
import com.engedaludvikling.memento.utils.IOnBackPressedListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    private final int startTimer = 2000;
    private IOnBackPressedListener mOnBackPressedListener;

    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseAuthHandler mFirebaseAuthHandler;
    private FragmentHandler mFragmentHandler;

    private Handler mDelayedTransactionHandler = new Handler();
    private Runnable mRunnable = this::performTransition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        mFirebaseAuthHandler = FirebaseAuthHandler.getFirebaseAuthHandler();
        mFragmentHandler = new FragmentHandler(this);

        mFragmentHandler.startTransactionWithoutFading(new StartFragment(), false);
        mDelayedTransactionHandler.postDelayed(mRunnable, startTimer);

        mAuthListener = firebaseAuth -> {
            FirebaseUser user = mFirebaseAuthHandler.getCurrentUser();

            Log.i("AuthTag", "Auth state changed to " + String.valueOf(user != null));
        };
    }

    private void performTransition() {
        if (isDestroyed()) {
            return;
        }

        ArrayList<View> sharedElements = new ArrayList<>();
        sharedElements.add(ButterKnife.findById(this, R.id.start_logo));

        if (FirebaseAuthHandler.getFirebaseAuthHandler().getCurrentUser() == null)
            mFragmentHandler.startTransactionWithFading(new LogInFragment(), sharedElements, false);
        else
            mFragmentHandler.startTransactionWithFading(new MainMenuFragment(), sharedElements, false);
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseAuth.getInstance().addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        FirebaseAuth.getInstance().removeAuthStateListener(mAuthListener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDelayedTransactionHandler.removeCallbacks(mRunnable);
    }

    public void setOnBackPressedListener(IOnBackPressedListener listener) {
        mOnBackPressedListener = listener;
    }

    public CoordinatorLayout getCoordinatorLayout() {
        return ButterKnife.findById(this, R.id.coordinator_layout);
    }

    @Override
    public void onBackPressed() {
        if (mOnBackPressedListener != null) {
            mOnBackPressedListener.onBackPressed();
            mOnBackPressedListener = null;
        }

        else if (mFragmentHandler.getFragmentManager().getBackStackEntryCount() != 0)
            mFragmentHandler.getFragmentManager().popBackStack();
        else
            super.onBackPressed();
    }
}

// TODO: 02-10-2017 Fiks login transitions så de ikke venter på Auth State Changed, det er alt for langsomt og fucker LogInEmail op 