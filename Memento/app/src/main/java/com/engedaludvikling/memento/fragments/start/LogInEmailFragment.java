package com.engedaludvikling.memento.fragments.start;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.engedaludvikling.memento.R;
import com.engedaludvikling.memento.fragments.BaseFragment;
import com.engedaludvikling.memento.fragments.MainMenuFragment;
import com.engedaludvikling.memento.utils.FirebaseAuthHandler;
import com.engedaludvikling.memento.utils.FragmentHandler;
import com.engedaludvikling.memento.utils.IOnBackPressedListener;
import com.google.android.gms.tasks.OnFailureListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;

public class LogInEmailFragment extends BaseFragment implements IOnBackPressedListener {

    @BindView(R.id.auth_email_email_edittext)
    EditText mEditTextEmail;
    @BindView(R.id.auth_email_password_edittext)
    EditText mEditTextPassword;

    FirebaseAuthHandler mFirebaseAuthHandler;
    FragmentHandler mFragmentHandler;

    public static LogInEmailFragment newInstance() {
        return new LogInEmailFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_auth_email_login, container, false);

        if (view != null)
            ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getMainActivity().setOnBackPressedListener(this);

        mFirebaseAuthHandler = FirebaseAuthHandler.getFirebaseAuthHandler();
        mFragmentHandler = new FragmentHandler(getMainActivity());
    }

    @Override
    public void onBackPressed() {
        ArrayList<View> sharedElements = new ArrayList<>();
        sharedElements.add(ButterKnife.findById(getMainActivity(), R.id.auth_email_top_button));
        sharedElements.add(ButterKnife.findById(getMainActivity(), R.id.auth_email_logo_imageview));

        mFragmentHandler.startTransactionWithFastFading(new LogInFragment(), sharedElements, false);
    }

    private boolean isInputValid() {
        if (mEditTextEmail.getText().toString().equals("") && mEditTextPassword.getText().toString().equals("")) {
            Snackbar.make(getMainActivity().getCoordinatorLayout(), getString(R.string.auth_no_email_or_password_entered), Snackbar.LENGTH_SHORT).show();
            return false;
        } else if (mEditTextEmail.getText().toString().equals("")) {
            Snackbar.make(getMainActivity().getCoordinatorLayout(), getString(R.string.auth_no_email_entered), Snackbar.LENGTH_SHORT).show();
            return false;
        } else if (mEditTextPassword.getText().toString().equals("")) {
            Snackbar.make(getMainActivity().getCoordinatorLayout(), getString(R.string.auth_no_password_entered), Snackbar.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void onLoginFailed(String errorMessage) {
        dismissProgressDialog();
        Toasty.error(getContext(), errorMessage, Toast.LENGTH_LONG).show();
    }

    private void onLoginSuccess(String successMessage) {
        dismissProgressDialog();
        Toasty.success(getContext(), successMessage).show();

        ArrayList<View> sharedElements = new ArrayList<>();
        sharedElements.add(ButterKnife.findById(getMainActivity(), R.id.auth_email_logo_imageview));
        mFragmentHandler.startTransactionWithFastFading(new MainMenuFragment(), sharedElements, false);
    }

    @OnClick(R.id.auth_email_login_button)
    void login() {
        if (isInputValid()) {
            showProgressDialog(getString(R.string.auth_progress_dialog_logging_in));

            mFirebaseAuthHandler.getFirebaseAuth()
                    .signInWithEmailAndPassword(mEditTextEmail.getText().toString(), mEditTextPassword.getText().toString())
                    .addOnCompleteListener(task -> {
                        task.addOnSuccessListener(authResult -> {
                            onLoginSuccess(getString(R.string.auth_logged_in));
                        });
                        task.addOnFailureListener(e -> {
                            onLoginFailed(e.getMessage());
                        });
                    });
        }
    }

    @OnClick(R.id.auth_email_create_account_button)
    void createAccount() {
        if (isInputValid()) {
            showProgressDialog(getString(R.string.auth_progress_dialog_creating_account));

            mFirebaseAuthHandler.getFirebaseAuth()
                    .createUserWithEmailAndPassword(mEditTextEmail.getText().toString(), mEditTextPassword.getText().toString())
                    .addOnCompleteListener(task -> {
                        task.addOnSuccessListener(authResult -> {
                            onLoginSuccess(getString(R.string.auth_account_created));
                        });
                        task.addOnFailureListener(e -> {
                            onLoginFailed(e.getMessage());
                        });
                    });
        }
    }
}
