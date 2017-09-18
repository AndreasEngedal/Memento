package com.engedaludvikling.memento.fragments.start;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.engedaludvikling.memento.R;
import com.engedaludvikling.memento.fragments.BaseFragment;
import com.engedaludvikling.memento.utils.FirebaseAuthHandler;
import com.engedaludvikling.memento.utils.FragmentHandler;
import com.engedaludvikling.memento.utils.IOnBackPressedListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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

        mFragmentHandler.startTransactionWithFastFading(LogInFragment.newInstance(), sharedElements, false);
    }

    private boolean isInputValid() {
        if (mEditTextEmail.getText().toString().equals("") && mEditTextPassword.getText().toString().equals("")) {
            Snackbar.make(getView(), getString(R.string.auth_no_email_or_password_entered), Snackbar.LENGTH_SHORT);
            return false;
        } else if (mEditTextEmail.getText().toString().equals("")) {
            Snackbar.make(getView(), getString(R.string.auth_no_email_entered), Snackbar.LENGTH_SHORT);
            return false;
        } else if (mEditTextPassword.getText().toString().equals("")) {
            Snackbar.make(getView(), getString(R.string.auth_no_password_entered), Snackbar.LENGTH_SHORT);
            return false;
        }

        return true;
    }

    @OnClick(R.id.auth_email_login_button)
    void login() {
        if (isInputValid()) {
            mFirebaseAuthHandler.getFirebaseAuth()
                    .signInWithEmailAndPassword(mEditTextEmail.getText().toString(), mEditTextPassword.getText().toString())
                    .addOnCompleteListener(task -> {
                        task.addOnSuccessListener(authResult -> {
                            ArrayList<View> sharedElements = new ArrayList<>();
                            sharedElements.add(ButterKnife.findById(getMainActivity(), R.id.auth_email_logo_imageview));

                            mFragmentHandler.startTransactionWithFastFading(LogInFragment.newInstance(), sharedElements, false);
                        });
                        task.addOnFailureListener(e -> {
                            Snackbar.make(getView(), getString(R.string.auth_incorrect_login), Snackbar.LENGTH_SHORT);
                        });
                    });
        }
    }

    @OnClick(R.id.auth_email_create_account_button)
    void createAccount() {
        if (isInputValid()) {
            mFirebaseAuthHandler.getFirebaseAuth()
                    .createUserWithEmailAndPassword(mEditTextEmail.getText().toString(), mEditTextPassword.getText().toString())
                    .addOnCompleteListener(task -> {
                        task.addOnSuccessListener(authResult -> {
                            ArrayList<View> sharedElements = new ArrayList<>();
                            sharedElements.add(ButterKnife.findById(getMainActivity(), R.id.auth_email_logo_imageview));

                            mFragmentHandler.startTransactionWithFastFading(LogInFragment.newInstance(), sharedElements, false);
                        });

                        task.addOnFailureListener(e -> Snackbar.make(getView(), getString(R.string.auth_something_went_wrong), Snackbar.LENGTH_SHORT));
                    });
        }
    }
}
