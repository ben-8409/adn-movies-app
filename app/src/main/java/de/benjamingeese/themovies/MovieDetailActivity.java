package de.benjamingeese.themovies;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import de.benjamingeese.themovies.data.Movie;
import de.benjamingeese.themovies.data.MoviesContract;
import de.benjamingeese.themovies.data.ReviewEntry;
import de.benjamingeese.themovies.data.VideoEntry;
import de.benjamingeese.themovies.utilities.YoutubeUrlUtils;

public class MovieDetailActivity extends AppCompatActivity implements
        VideosFragment.OnListFragmentInteractionListener,
        ReviewsFragment.OnListFragmentInteractionListener {
    public static final String INTENT_MOVIE_EXTRA_KEY = "movie";
    static final String BUNDLE_MOVIE_KEY = "movie";
    private static final String TAG = "MovieDetailActivity";

    private Movie mMovie;
    private ViewPager mViewPager;
    private MovieTabsAdapter mMovieTabsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);


        //Load bundle if we have one.
        if (savedInstanceState != null && savedInstanceState.containsKey(BUNDLE_MOVIE_KEY)) {
            mMovie = savedInstanceState.getParcelable(BUNDLE_MOVIE_KEY);
        }

        //Check if started by intent
        Intent intentThatStartedTheActivity = getIntent();
        if (intentThatStartedTheActivity.hasExtra(INTENT_MOVIE_EXTRA_KEY)) {
            mMovie = intentThatStartedTheActivity.getParcelableExtra(INTENT_MOVIE_EXTRA_KEY);
            setTitle(mMovie.getTitle());
        }

        //Setup tabs
        mMovieTabsAdapter = new MovieTabsAdapter(getSupportFragmentManager(), MovieDetailActivity.this);
        mViewPager = findViewById(R.id.movie_pager);
        mViewPager.setAdapter(mMovieTabsAdapter);

        // Give the TabLayout the ViewPager
        TabLayout tabLayout = findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(mViewPager);

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelable(BUNDLE_MOVIE_KEY, mMovie);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onListFragmentInteraction(ReviewEntry item) {
        Toast.makeText(this, "Review clicked: " + item.getAuthor(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onVideoListFragmentInteraction(VideoEntry item) {
        if (!VideoEntry.SITE_YOUTUBE.equals(item.getSite())) {
            Toast.makeText(this, "Unsupported site: " + item.getSite(), Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(item.getKey())) {
            Toast.makeText(this, "Video key is empty", Toast.LENGTH_SHORT).show();
        } else {
            Intent showVideo = new Intent(Intent.ACTION_VIEW);
            Uri uri = YoutubeUrlUtils.buildWatchVideoUrl(item.getKey());
            Log.v(TAG, "Url: " + uri.toString());
            showVideo.setData(YoutubeUrlUtils.buildWatchVideoUrl(item.getKey()));
            startActivity(showVideo);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.movie_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.item_set_favorite:
                setFavorite();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        //update favorite icon color for favorites
        MenuItem favorite = menu.findItem(R.id.item_set_favorite);
        if (favorite != null) {
            if (mMovie.isFavorite()) {
                favorite.setIcon(R.drawable.ic_star_white_24dp);
            } else {
                favorite.setIcon(R.drawable.ic_star_black_24dp);
            }
        }
        return super.onPrepareOptionsMenu(menu);
    }

    private void setFavorite() {
        if (mMovie.isFavorite()) {
            Uri uri = ContentUris.withAppendedId(MoviesContract.MovieEntry.CONTENT_URI, mMovie.getUid());
            //where not necessary since we handle this through the content uri
            getContentResolver().delete(uri, null, null);
            mMovie.setFavorite(false);
            Toast.makeText(this, R.string.toast_unfavorited_movie, Toast.LENGTH_SHORT).show();
        } else {
            //create content values
            ContentValues values = new ContentValues();
            values.put(MoviesContract.MovieEntry._ID, mMovie.getUid());
            values.put(MoviesContract.MovieEntry.COLUMN_NAME_TITLE, mMovie.getTitle());
            values.put(MoviesContract.MovieEntry.COLUMN_POSTER_URL, mMovie.getPosterPath());
        /*new AsyncQueryHandler(getContentResolver()) {

            @Override
            protected void onInsertComplete(int token, Object cookie, Uri uri) {
                Toast.makeText(MovieDetailActivity.this, "New favorite saved", Toast.LENGTH_SHORT).show();
            }
        }.startInsert(1, null, MoviesContract.MovieEntry.CONTENT_URI, values);*/
            getContentResolver().insert(MoviesContract.MovieEntry.CONTENT_URI, values);
            mMovie.setFavorite(true);
            Toast.makeText(this, R.string.toast_favorited_movie, Toast.LENGTH_SHORT).show();

        }
        invalidateOptionsMenu();
    }


}
