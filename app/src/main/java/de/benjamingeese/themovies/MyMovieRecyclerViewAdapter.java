package de.benjamingeese.themovies;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import de.benjamingeese.themovies.MovieListFragment.OnListFragmentInteractionListener;
import de.benjamingeese.themovies.data.Movie;
import de.benjamingeese.themovies.utilities.MovieDBApiUtils;

public class MyMovieRecyclerViewAdapter extends RecyclerView.Adapter<MyMovieRecyclerViewAdapter.ViewHolder> {

    private final List<Movie> mValues;
    private final OnListFragmentInteractionListener mListener;

    public MyMovieRecyclerViewAdapter(List<Movie> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_movie, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Movie entry = mValues.get(position);
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
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final View mView;
        final TextView mTitleTextView;
        final ImageView mImageView;
        Movie mItem;

        ViewHolder(View view) {
            super(view);
            mView = view;
            mTitleTextView = view.findViewById(R.id.title_tv);
            mImageView = view.findViewById(R.id.poster_iv);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mTitleTextView.getText() + "'";
        }
    }
}
