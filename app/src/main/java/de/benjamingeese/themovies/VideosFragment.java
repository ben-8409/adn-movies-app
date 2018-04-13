package de.benjamingeese.themovies;

import android.content.Context;
import android.os.Bundle;
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
import de.benjamingeese.themovies.data.VideoEntry;
import de.benjamingeese.themovies.tasks.LoadMovieVideosTask;

import static de.benjamingeese.themovies.MovieDetailActivity.INTENT_MOVIE_EXTRA_KEY;

public class VideosFragment extends Fragment implements AsyncTaskCompleteListener<List<VideoEntry>> {

    private static final String TAG = "ReviewsFragment";
    private static final String ARG_MOVIE = "movie";
    private Movie mMovie;
    private OnListFragmentInteractionListener mListener;
    private RecyclerView mVideosRv;
    private MyVideoRecyclerViewAdapter mVideosAdapter;
    private ArrayList<VideoEntry> mVideos;
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public VideosFragment() {
    }

    @SuppressWarnings("unused")
    public static VideosFragment newInstance(Movie movie) {
        VideosFragment fragment = new VideosFragment();
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
        mVideos = new ArrayList<>();
        mVideosAdapter = new MyVideoRecyclerViewAdapter(mVideos, mListener);

        if (mMovie != null) {

            loadVideos();
        }

    }

    private void loadVideos() {
        Log.d(TAG, "Loading video data using AsyncTask");
        new LoadMovieVideosTask(this).execute(String.valueOf(mMovie.getUid()));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_video_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(mVideosAdapter);
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
    public void onTaskComplete(List<VideoEntry> result) {
        if (result != null && result.size() > 0) {
            mVideos.clear();
            mVideos.addAll(result);
            mVideosAdapter.notifyDataSetChanged();
        }
    }

    public interface OnListFragmentInteractionListener {
        void onVideoListFragmentInteraction(VideoEntry entry);
    }
}
