package de.benjamingeese.themovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import de.benjamingeese.themovies.data.Movie;

public class MainActivity extends AppCompatActivity implements MovieListFragment.OnListFragmentInteractionListener {
    boolean favoritesFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }


    public void onListFragmentInteraction(Movie movie) {
        Intent intentToShowMovieDetails = new Intent(this, MovieDetailActivity.class);
        intentToShowMovieDetails.putExtra(MovieDetailActivity.INTENT_MOVIE_EXTRA_KEY, movie);
        startActivity(intentToShowMovieDetails);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        MovieListFragment movieListFragment = (MovieListFragment) getSupportFragmentManager().findFragmentById(R.id.movie_list_fragment);

        // Handle item selection
        switch (item.getItemId()) {
            case R.id.item_sort_most_popular:
                movieListFragment.setSortOrder(MovieListFragment.LIST_SORT_ORDER_MOST_POPULAR);
                return true;
            case R.id.item_sort_top_rated:
                movieListFragment.setSortOrder(MovieListFragment.LIST_SORT_ORDER_TOP_RATED);
                return true;
            case R.id.item_filter_favorites:
                favoritesFilter = !favoritesFilter;
                movieListFragment.showFavorites(favoritesFilter);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
