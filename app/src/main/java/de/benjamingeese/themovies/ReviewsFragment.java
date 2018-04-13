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
    private Movie mMovie;
    private OnListFragmentInteractionListener mListener;
    private List<ReviewEntry> mReviews;
    private MyReviewRecyclerViewAdapter mReviewsAdapter;

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
        mReviews = new ArrayList<>();
        mReviewsAdapter = new MyReviewRecyclerViewAdapter(mReviews, mListener);

        if (mMovie != null) {

            loadReviews();
        }

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
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(mReviewsAdapter);
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(ReviewEntry item);
    }
}
