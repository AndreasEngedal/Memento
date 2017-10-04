package com.engedaludvikling.memento.fragments.start;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.engedaludvikling.memento.R;
import com.engedaludvikling.memento.fragments.BaseFragment;
import com.engedaludvikling.memento.fragments.MainMenuFragment;
import com.engedaludvikling.memento.utils.FirebaseAuthHandler;
import com.engedaludvikling.memento.utils.FragmentHandler;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;
import es.dmoral.toasty.Toasty;

public class LogInFragment extends BaseFragment implements GoogleApiClient.OnConnectionFailedListener {

    private static final int RC_SIGN_IN = 1000;
    private static final String FACEBOOKTAG = "FACEBOOKTAG";

    //@Nullable @BindView(R.id.auth_login_logo_imageview) ImageView ivLoginLogo;
    //@Nullable @BindView(R.id.auth_login_button) Button btnLogin;

    @Nullable
    @BindView(R.id.auth_connected_logo_imageview)
    ImageView ivConnectedLogo;

    private FragmentHandler mFragmentHandler;
    private FirebaseAuthHandler mFirebaseAuthHandler;

    @Nullable
    @BindView(R.id.auth_login_hidden_facebook_button)
    LoginButton btnFbLogIn;
    private CallbackManager mCallbackManager;

    private GoogleApiClient mGoogleApiClient;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_auth_login, container, false);

        if (view != null)
            ButterKnife.bind(this, view);
        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mFirebaseAuthHandler = FirebaseAuthHandler.getFirebaseAuthHandler();
        mFragmentHandler = new FragmentHandler(getMainActivity());

        if (!mFirebaseAuthHandler.isUserLoggedIn()) {
            initializeGoogleSignInHandler();
            initializeFacebookLoginHandler();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mGoogleApiClient != null)
            mGoogleApiClient.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.stopAutoManage(getMainActivity());
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(getMainActivity(), "Google connection failed", Toast.LENGTH_SHORT).show();
    }

    private void initializeGoogleSignInHandler() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                    .enableAutoManage(LogInFragment.super.getActivity() /* FragmentActivity */, this /* OnConnectionFailedListener */)
                    .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                    .build();
        }
    }

    private void initializeFacebookLoginHandler() {
        mCallbackManager = CallbackManager.Factory.create();
        assert btnFbLogIn != null;
        btnFbLogIn.setReadPermissions("email", "public_profile");
        btnFbLogIn.setFragment(this);
        btnFbLogIn.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d(FACEBOOKTAG, "facebook:onCancel");
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(FACEBOOKTAG, "facebook:onError", error);
            }
        });
    }

    private void handleFacebookAccessToken(AccessToken token) {
        try {
            AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
            mFirebaseAuthHandler.getFirebaseAuth()
                    .signInWithCredential(credential)
                    .addOnSuccessListener(e -> {
                        onLoginSuccess();
                    }).addOnFailureListener(e -> {
                        onLoginFailed(e.getMessage());
                    });
        } catch (Exception e) {
            Log.e(FACEBOOKTAG, e.getLocalizedMessage());
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        showProgressDialog(getString(R.string.auth_progress_dialog_logging_in));

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                handleSignInResult(result);
            } else {
                Toast.makeText(LogInFragment.super.getContext(), "Connection with Google failed", Toast.LENGTH_SHORT).show();
            }
        } else
            mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            GoogleSignInAccount acct = result.getSignInAccount();
            AuthCredential credential = GoogleAuthProvider.getCredential(acct != null ? acct.getIdToken() : null, null);
            mFirebaseAuthHandler.getFirebaseAuth()
                    .signInWithCredential(credential)
                    .addOnSuccessListener(authResult -> {
                        onLoginSuccess();
                    }).addOnFailureListener(authResult -> {
                onLoginFailed(authResult.getMessage());
            });
        } else {
            Toast.makeText(LogInFragment.super.getContext(), "ResultCode failed", Toast.LENGTH_SHORT).show();
        }
    }

    private void onLoginFailed(String errorMessage) {
        dismissProgressDialog();
        Toasty.error(getContext(), errorMessage, Toast.LENGTH_LONG).show();

    }

    private void onLoginSuccess() {
        dismissProgressDialog();
        Toasty.success(getContext(), getString(R.string.auth_logged_in)).show();

        ArrayList<View> sharedElements = new ArrayList<>();
        sharedElements.add(ButterKnife.findById(getMainActivity(), R.id.auth_login_logo_imageview));
        mFragmentHandler.startTransactionWithFastFading(new MainMenuFragment(), sharedElements, false);
    }

    @Optional
    @OnClick(R.id.auth_login_email_button)
    void emailLogin() {
        ArrayList<View> sharedElements = new ArrayList<>();
        sharedElements.add(ButterKnife.findById(getMainActivity(), R.id.auth_login_logo_imageview));
        sharedElements.add(ButterKnife.findById(getMainActivity(), R.id.auth_login_email_button));

        mFragmentHandler.startTransactionWithFastFading(LogInEmailFragment.newInstance(), sharedElements, false);
    }

    @Optional
    @OnClick(R.id.auth_login_facebook_button)
    void facebookLogin() {
        assert btnFbLogIn != null;
        btnFbLogIn.performClick();
    }

    @Optional
    @OnClick(R.id.auth_login_google_button)
    void googleLogin() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Optional
    @OnClick(R.id.auth_login_skip_textview)
    void skip() {
        mFragmentHandler.startTransactionWithFastFading(new MainMenuFragment(), false);
    }
}
