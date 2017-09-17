package com.engedaludvikling.memento.fragments.start;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.engedaludvikling.memento.R;
import com.engedaludvikling.memento.fragments.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StartFragment extends BaseFragment {

    @BindView(R.id.start_logo) ImageView ivStartImage;

    public static StartFragment newInstance()
    {
        return new StartFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_start_screen, container, false);
        if (view != null)
            ButterKnife.bind(this, view);

        return view;
    }
}
