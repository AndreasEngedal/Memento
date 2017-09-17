package com.engedaludvikling.memento.fragments.start;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.engedaludvikling.memento.R;
import com.engedaludvikling.memento.fragments.BaseFragment;

import butterknife.ButterKnife;

/**
 * Created by Andreas Engedal on 17-09-2017.
 */

public class LogInEmailFragment extends BaseFragment {

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
}
