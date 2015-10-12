package com.sidelance.ngchat;

/**
 * Created by Ivan on 10/4/2015.
 */

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

/**
 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    private static final String TAG = SectionsPagerAdapter.class.getSimpleName();

    protected Context mContext;

    public SectionsPagerAdapter(Context context, FragmentManager fm)
    {
        super(fm);
        mContext = context;
        Log.d(TAG, "Create");
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below)// .

        switch (position){
            case 0:
                return new InboxFragment();
            case 1:
                return new FriendsFragment();
        }

        return null;

    }

    @Override
    public int getCount() {
        // Show 3 total pages.
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return mContext.getString(R.string.TITLE_SECTION1);
            case 1:
                return mContext.getString(R.string.TITLE_SECTION2);
//            case 2:
//                return mContext.getString(R.string.TITLE_SECTION3);
        }
        return null;
    }
}