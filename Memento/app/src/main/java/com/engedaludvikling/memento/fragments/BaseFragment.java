package com.engedaludvikling.memento.fragments;

import android.support.v4.app.Fragment;

import com.engedaludvikling.memento.MainActivity;

/**
 * Created by Andreas Engedal on 14-09-2017.
 */

public class BaseFragment extends Fragment {

    private MainActivity mainActivity;

    public <T> T findViewById(int id) {
        return (T) getView().findViewById(id);
    }

    public MainActivity getMainActivity() {
        if (mainActivity == null)
            mainActivity = (MainActivity) getActivity();
        return mainActivity;
    }
}
