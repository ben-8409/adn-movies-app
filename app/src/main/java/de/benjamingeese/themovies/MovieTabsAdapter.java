package de.benjamingeese.themovies;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class MovieTabsAdapter extends FragmentPagerAdapter {

    private final Context context;

    MovieTabsAdapter(FragmentManager fm, Context c) {
        super(fm);
        context = c;
    }

    /**
     * Return the Fragment associated with a specified position.
     *
     * @param position
     */
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new MovieDetailFragment();
            case 1:
                return new VideosFragment();
            case 2:
                return new ReviewsFragment();
        }

        return null;
    }

    /**
     * Return the number of views available.
     */
    @Override
    public int getCount() {
        return 3;
    }


    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return context.getString(R.string.tab_text_1);
            case 1:
                return context.getString(R.string.tab_text_2);
            case 2:
                return context.getString(R.string.tab_text_3);
        }
        ;
        return null;
    }
}
