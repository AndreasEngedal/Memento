package com.engedaludvikling.memento.utils;

import android.util.Log;

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class FirebaseAuthHandler {

    private static FirebaseAuthHandler mFirebaseAuthHandler;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private FirebaseAuthHandler() {
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = firebaseAuth -> {
            FirebaseUser user = getCurrentUser();
            if (user != null) {
                // User is signed in
                Log.d("FirebaseTag", "onAuthStateChanged:signed_in:" + user.getUid());
            } else {
                // User is signed out
                Log.d("FirebaseTag", "onAuthStateChanged:signed_out");
            }
        };
    }

    public static FirebaseAuthHandler getFirebaseAuthHandler() {
        if (mFirebaseAuthHandler == null)
            mFirebaseAuthHandler = new FirebaseAuthHandler();
        return mFirebaseAuthHandler;
    }

    public FirebaseAuth getFirebaseAuth() {
        return mAuth;
    }

    public FirebaseUser getCurrentUser() {
        return mAuth.getCurrentUser();
    }

    public boolean isUserLoggedIn() {
        return mAuth.getCurrentUser() != null;
    }

    public void signOut() {
        mAuth.signOut();
        LoginManager.getInstance().logOut();
    }

    public void addAuthStateListener() {
        mAuth.addAuthStateListener(mAuthListener);
    }

    public void removeAuthStateListener() {
        mAuth.removeAuthStateListener(mAuthListener);
    }

}
