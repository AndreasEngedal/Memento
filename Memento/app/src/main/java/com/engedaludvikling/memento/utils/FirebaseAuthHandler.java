package com.engedaludvikling.memento.utils;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class FirebaseAuthHandler {

    private static FirebaseAuthHandler mFirebaseAuthHandler;

    private FirebaseAuth mAuth;

    private FirebaseAuthHandler() {
        mAuth = FirebaseAuth.getInstance();
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
}
