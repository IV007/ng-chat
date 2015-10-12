package com.sidelance.ngchat;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Ivan Utsalo
 */
public class InboxFragment extends ListFragment {


    private static final String TAG = InboxFragment.class.getSimpleName();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "View Created");
        View rootView = inflater.inflate(R.layout.fragment_inbox, container, false);
        return rootView;
    }
}
