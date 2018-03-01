package com.vicki.bored.bored.movies;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;


class PagerAdapter extends FragmentStatePagerAdapter {
    public String plot=MainActivity.plot;
    public String trailers=MainActivity.trailers;
    public String reviews=MainActivity.reviews;
    public PagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                OverviewFragment tab1 = new OverviewFragment();
                return tab1;
            case 1:
                TrailerFragment tab2 = new TrailerFragment();
                return tab2;
            case 2:
                ReviewFragment tab3 = new ReviewFragment();
                return tab3;
            default:
                return null;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return plot;
            case 1:
                return trailers;
            case 2:
                return reviews;
        }
        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }
}
