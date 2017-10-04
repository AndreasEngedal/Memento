package com.engedaludvikling.memento.fragments;

import android.app.ProgressDialog;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.engedaludvikling.memento.MainActivity;
import com.engedaludvikling.memento.R;

public class BaseFragment extends Fragment {

    private ProgressDialog mProgressDialog;
    private MainActivity mainActivity;

    public <T> T findViewById(int id) {
        return (T) getView().findViewById(id);
    }

    public MainActivity getMainActivity() {
        if (mainActivity == null)
            mainActivity = (MainActivity) getActivity();
        return mainActivity;
    }

    public void showProgressDialog(String loadingText) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(getContext());
            mProgressDialog.setIndeterminateDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.progress_dialog_standard));
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setCancelable(false);
        }
        mProgressDialog.setMessage(loadingText);
        mProgressDialog.show();
    }

    public void dismissProgressDialog() {
        if (mProgressDialog != null)
            mProgressDialog.dismiss();
        else
            Log.i("BASETAG", "Called dismissProgressDialog() without showing it!");
    }
}
