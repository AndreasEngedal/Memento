package com.engedaludvikling.memento.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.engedaludvikling.memento.R;
import com.engedaludvikling.memento.fragments.start.LogInFragment;
import com.engedaludvikling.memento.utils.FirebaseAuthHandler;
import com.engedaludvikling.memento.utils.FragmentHandler;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainMenuFragment extends BaseFragment {

    private FragmentHandler mFragmentHandler;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_menu, container, false);

        if (view != null)
            ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mFragmentHandler = new FragmentHandler(getMainActivity());
    }

    @OnClick(R.id.main_menu_button_sign_out) void OnSignOut() {
        FirebaseAuthHandler firebaseAuthHandler = FirebaseAuthHandler.getFirebaseAuthHandler();
        firebaseAuthHandler.signOut();

        ArrayList<View> sharedElements = new ArrayList<>();
        sharedElements.add(ButterKnife.findById(getMainActivity(), R.id.main_menu_logo_imageview));
        mFragmentHandler.startTransactionWithFastFading(new LogInFragment(), sharedElements, false);
    }
}
