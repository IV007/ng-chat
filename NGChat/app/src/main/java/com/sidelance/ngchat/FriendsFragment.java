package com.sidelance.ngchat;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import java.util.List;

/**
 * Created by Ivan Utsalo
 */
public class FriendsFragment extends ListFragment {

    private static final String TAG = FriendsFragment.class.getSimpleName();

    protected List<ParseUser> mFriends;
    protected ParseRelation<ParseUser> mFriendsRelation;
    protected ParseUser mCurrentUser;

    @Override
    public void onResume() {
        super.onResume();

        Log.d(TAG, "Resumed");

        mCurrentUser = ParseUser.getCurrentUser();
        mFriendsRelation = mCurrentUser.getRelation(ParseConstants.KEY_FRIENDS_RELATION);

        getActivity().setProgressBarIndeterminateVisibility(true);

        displayFriendList();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_friends, container, false);
    }

    private void displayFriendList() {

        ParseQuery<ParseUser> query = mFriendsRelation.getQuery();
        query.addAscendingOrder(ParseConstants.KEY_USERNAME);
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> friends, ParseException e) {

                getActivity().setProgressBarIndeterminateVisibility(false);

                if (e == null) {
                    // list returned - look for a match

                    String[] friendSize = new String[0];
                    if (friends.size() > 0) {

                        mFriends = friends;
                        friendSize = new String[mFriends.size()];
                        int i = 0;
                        for (ParseUser user : mFriends) {
                            friendSize[i] = user.getUsername();
                            i++;
                        }


                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                            getListView().getContext(),
                            android.R.layout.simple_list_item_1,
                            friendSize);
                    setListAdapter(adapter);

                } else {
                    Log.e(TAG, e.getMessage());
                    Tools.displayErrorDialog(e.getMessage(), getString(R.string.ERROR_TITLE), getContext());

                }
            }
        });
    }
}
