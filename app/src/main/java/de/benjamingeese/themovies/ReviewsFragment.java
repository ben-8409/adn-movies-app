package de.benjamingeese.themovies;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import de.benjamingeese.themovies.data.Movie;
import de.benjamingeese.themovies.data.ReviewEntry;
import de.benjamingeese.themovies.tasks.LoadMovieReviewsTask;

import static de.benjamingeese.themovies.MovieDetailActivity.INTENT_MOVIE_EXTRA_KEY;

public class ReviewsFragment extends Fragment implements AsyncTaskCompleteListener<List<ReviewEntry>> {

    private static final String TAG = "ReviewsFragment";

    private static final String ARG_MOVIE = "movie";
    private static final String LAYOUT_MANAGER_STATE = "layout-manager-state";
    private static final String REVIEWS_BUNDLE_KEY = "bundle-reviews-key";
    private Movie mMovie;
    private OnListFragmentInteractionListener mListener;
    private ArrayList<ReviewEntry> mReviews;
    private MyReviewRecyclerViewAdapter mReviewsAdapter;
    private RecyclerView recyclerView;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ReviewsFragment() {
    }

    @SuppressWarnings("unused")
    public static ReviewsFragment newInstance(Movie movie) {
        ReviewsFragment fragment = new ReviewsFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_MOVIE, movie);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Load bundle if we have one.
        if (savedInstanceState != null && savedInstanceState.containsKey(MovieDetailActivity.BUNDLE_MOVIE_KEY)) {
            mMovie = savedInstanceState.getParcelable(MovieDetailActivity.BUNDLE_MOVIE_KEY);
        } else if (getArguments() != null) {
            mMovie = getArguments().getParcelable(ARG_MOVIE);
            Log.d(TAG, "Movie id: " + mMovie);
        } else if (getActivity() != null && getActivity().getIntent() != null) {
            mMovie = getActivity().getIntent().getParcelableExtra(INTENT_MOVIE_EXTRA_KEY);
        } else {
            throw new RuntimeException(getActivity().toString()
                    + " must supply Movie for Reviews to load");
        }


        //load movies from bundle or net
        if ( savedInstanceState != null && savedInstanceState.containsKey(REVIEWS_BUNDLE_KEY)) {
            mReviews = savedInstanceState.getParcelableArrayList(REVIEWS_BUNDLE_KEY);
        } else {
            mReviews = new ArrayList<>();
            if (mMovie != null) {
                loadReviews();
            }
        }
        mReviewsAdapter = new MyReviewRecyclerViewAdapter(mReviews, mListener);
    }

    private void loadReviews() {
        Log.d(TAG, "Load reviews");
        new LoadMovieReviewsTask(this).execute(String.valueOf(mMovie.getUid()));
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_review_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(mReviewsAdapter);
        }

        if(savedInstanceState != null && savedInstanceState.containsKey(LAYOUT_MANAGER_STATE)) {
            recyclerView.getLayoutManager().onRestoreInstanceState(savedInstanceState.getParcelable(LAYOUT_MANAGER_STATE));
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

    /**
     * Invoked by the AsyncTask when completed
     *
     * @param result The result of the async execution
     */
    @Override
    public void onTaskComplete(List<ReviewEntry> result) {
        if (result != null && result.size() > 0) {
            mReviews.clear();
            mReviews.addAll(result);
            mReviewsAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        //save scroll position
        outState.putParcelable(LAYOUT_MANAGER_STATE, recyclerView.getLayoutManager().onSaveInstanceState());
        if(mReviews!=null) {
            outState.putParcelableArrayList(REVIEWS_BUNDLE_KEY, mReviews);
        }
        super.onSaveInstanceState(outState);
    }


    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(ReviewEntry item);
    }
}
