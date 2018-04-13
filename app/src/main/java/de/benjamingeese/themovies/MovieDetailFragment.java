package de.benjamingeese.themovies;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import de.benjamingeese.themovies.data.Movie;
import de.benjamingeese.themovies.utilities.MovieDBApiUtils;
import de.benjamingeese.themovies.utilities.Utils;

import static de.benjamingeese.themovies.MovieDetailActivity.INTENT_MOVIE_EXTRA_KEY;

public class MovieDetailFragment extends Fragment {

    private static final String TAG = "MovieDetailFragment";

    private static final String ARG_MOVIE = "movie";
    private static final int MOVIE_LOADER_ID = 101;
    private Movie mMovie;
    private ImageView mPosterImageView;
    private TextView mTitleTv;
    private TextView mReleaseDateTv;
    private TextView mVoteAverageTv;
    private TextView mSynopsisTv;

    public MovieDetailFragment() {
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
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie_detail, container, false);

        mPosterImageView = view.findViewById(R.id.detail_poster_iv);
        mTitleTv = view.findViewById(R.id.detail_title_tv);
        mReleaseDateTv = view.findViewById(R.id.detail_release_date_tv);
        mVoteAverageTv = view.findViewById(R.id.detail_vote_average_tv);
        mSynopsisTv = view.findViewById(R.id.detail_synopsis_tv);

        populateUI();

        return view;

    }

    private void populateUI() {
        if (mMovie != null) {
            if (mPosterImageView != null && !TextUtils.isEmpty(mMovie.getPosterPath())) {
                Picasso.with(getActivity())
                        .load(MovieDBApiUtils.buildImageURI(mMovie.getPosterPath()))
                        .into(mPosterImageView);
            }
            if (mTitleTv != null) {
                mTitleTv.setText(mMovie.getTitle());
            }
            if (mReleaseDateTv != null) {
                mReleaseDateTv.setText(mMovie.getReleaseDate());
            }
            if (mVoteAverageTv != null) {
                mVoteAverageTv.setText(String.format(Utils.getCurrentLocale(getActivity()), "%.1f", mMovie.getVoteAverage()));
            }
            if (mSynopsisTv != null) {
                mSynopsisTv.setText(mMovie.getOverview());
            }
        }
    }

}
