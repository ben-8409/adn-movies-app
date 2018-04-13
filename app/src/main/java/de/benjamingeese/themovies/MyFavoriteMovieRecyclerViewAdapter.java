package de.benjamingeese.themovies;

import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import de.benjamingeese.themovies.MovieListFragment.OnListFragmentInteractionListener;
import de.benjamingeese.themovies.data.Movie;
import de.benjamingeese.themovies.utilities.MovieDBApiUtils;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Movie} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 */
public class MyFavoriteMovieRecyclerViewAdapter extends RecyclerView.Adapter<MyMovieRecyclerViewAdapter.ViewHolder> {

    private final OnListFragmentInteractionListener mListener;
    private Cursor mCursor;

    MyFavoriteMovieRecyclerViewAdapter(OnListFragmentInteractionListener listener) {
        mListener = listener;
    }

    @NonNull
    @Override
    public MyMovieRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_movie, parent, false);
        return new MyMovieRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyMovieRecyclerViewAdapter.ViewHolder holder, int position) {
        //this is a cursor adapter, so we need to move the cursor to the position
        mCursor.moveToPosition(position);

        Movie entry = new Movie();
        entry.setUid(mCursor.getLong(MovieListFragment.INDEX_FAVORITE_MOVIE_ID));
        entry.setTitle(mCursor.getString(MovieListFragment.INDEX_FAVORITE_MOVIE_TITLE));
        entry.setPosterPath(mCursor.getString(MovieListFragment.INDEX_FAVORITE_MOVIE_POSTER_PATH));
        entry.setFavorite(true);
        holder.mItem = entry;
        holder.mTitleTextView.setText(entry.getTitle());
        Uri imageUri = MovieDBApiUtils.buildImageURI(entry.getPosterPath());
        Picasso.with(holder.mImageView.getContext())
                .load(imageUri)
                .placeholder(R.drawable.ic_poster_placeholder)
                .into(holder.mImageView);

        holder.mView.setOnClickListener(v -> {
            if (null != mListener) {
                // Notify the active callbacks interface (the activity, if the
                // fragment is attached to one) that an item has been selected.
                mListener.onListFragmentInteraction(holder.mItem);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mCursor != null ? mCursor.getCount() : 0;
    }

    void swapCursor(Cursor newCursor) {
        mCursor = newCursor;
        notifyDataSetChanged();
    }
}
