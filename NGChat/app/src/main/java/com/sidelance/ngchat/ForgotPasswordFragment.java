package com.sidelance.ngchat;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A placeholder fragment containing a simple view.
 */
public class ForgotPasswordFragment extends Fragment {

    private static final String TAG = ForgotPasswordFragment.class.getSimpleName();

    public ForgotPasswordFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "View Created");
        return inflater.inflate(R.layout.fragment_forgot_password, container, false);

    }
}
