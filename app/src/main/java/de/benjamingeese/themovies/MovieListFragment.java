package de.benjamingeese.themovies;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import de.benjamingeese.themovies.data.Movie;
import de.benjamingeese.themovies.data.MoviesContract;
import de.benjamingeese.themovies.tasks.LoadMoviesDataTask;
import de.benjamingeese.themovies.utilities.MovieDBApiUtils;

import static android.content.ContentValues.TAG;

public class MovieListFragment extends Fragment
        implements AsyncTaskCompleteListener<List<Movie>>,
        LoaderManager.LoaderCallbacks<Cursor> {

    public static final String LIST_SORT_ORDER_MOST_POPULAR = "most_popular";
    public static final String LIST_SORT_ORDER_TOP_RATED = "top_rated";
    public static final String LIST_FILTER_FAVORITES = "favorites";
    public static final int INDEX_FAVORITE_MOVIE_ID = 0;
    public static final int INDEX_FAVORITE_MOVIE_TITLE = 1;
    private static final String ARG_COLUMN_COUNT = "column-count";
    private static final String BUNDLE_MOVIE_LIST = "movies";
    private static final int ID_FAVORITES_LOADER = 101;
    private int mColumnCount = 2;
    private OnListFragmentInteractionListener mListener;


    private String mPath = MovieDBApiUtils.PATH_POPULAR;
    private MyMovieRecyclerViewAdapter mAdapter;
    private MyFavoriteMovieRecyclerViewAdapter mFavoritesAdapter;
    private ArrayList<Movie> mMovies;
    private RecyclerView mRecyclerView;

    public MovieListFragment() {
    }

    @SuppressWarnings("unused")
    public static MovieListFragment newInstance(int columnCount) {
        MovieListFragment fragment = new MovieListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (savedInstanceState == null || !savedInstanceState.containsKey(BUNDLE_MOVIE_LIST)) {
            mMovies = new ArrayList<>();
            loadMovieData();
        } else {
            Log.v(TAG, "Loaded list from bundle");
            mMovies = savedInstanceState.getParcelableArrayList(BUNDLE_MOVIE_LIST);
        }

        mAdapter = new MyMovieRecyclerViewAdapter(mMovies, mListener);
        mFavoritesAdapter = new MyFavoriteMovieRecyclerViewAdapter(mListener);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            mRecyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                mRecyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            mRecyclerView.setAdapter(mAdapter);
        }
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelableArrayList(BUNDLE_MOVIE_LIST, mMovies);
        super.onSaveInstanceState(outState);
    }

    private void loadMovieData() {
        Log.d(TAG, "Loading movie data using AsyncTask");
        new LoadMoviesDataTask(this).execute(mPath);
    }

    public void setSortOrder(String listSortOrderMostPopular) {
        switch (listSortOrderMostPopular) {
            case LIST_SORT_ORDER_MOST_POPULAR:
                mPath = MovieDBApiUtils.PATH_POPULAR;
                loadMovieData();
                break;
            case LIST_SORT_ORDER_TOP_RATED:
                mPath = MovieDBApiUtils.PATH_TOP_RATED;
                loadMovieData();
                break;
            default:
                return;
        }
        loadMovieData();
    }

    public void showFavorites(boolean favorites) {
        if (favorites) {
            mRecyclerView.swapAdapter(mFavoritesAdapter, true);
            getActivity().getSupportLoaderManager().initLoader(ID_FAVORITES_LOADER, null, this);
        } else {
            mRecyclerView.swapAdapter(mAdapter, true);
            loadMovieData();
        }
    }

    @Override
    public void onTaskComplete(List<Movie> movieListEntries) {
        if (movieListEntries != null && movieListEntries.size() > 0) {
            mMovies.clear();
            mMovies.addAll(movieListEntries);
            mAdapter.notifyDataSetChanged();
        }
    }


    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        switch (id) {

            case ID_FAVORITES_LOADER:
                Uri favoriteMoviesQuery = MoviesContract.MovieEntry.CONTENT_URI;

                return new android.support.v4.content.CursorLoader(getContext(),
                        favoriteMoviesQuery,
                        null,
                        null,
                        null,
                        null);

            default:
                throw new RuntimeException("Unknown loader id: " + id);
        }
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        mFavoritesAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        mFavoritesAdapter.swapCursor(null);
    }

    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(Movie movie);
    }
}
