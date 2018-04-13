package de.benjamingeese.themovies;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import de.benjamingeese.themovies.ReviewsFragment.OnListFragmentInteractionListener;
import de.benjamingeese.themovies.data.ReviewEntry;

public class MyReviewRecyclerViewAdapter extends RecyclerView.Adapter<MyReviewRecyclerViewAdapter.ViewHolder> {

    private final List<ReviewEntry> mValues;
    private final OnListFragmentInteractionListener mListener;

    MyReviewRecyclerViewAdapter(List<ReviewEntry> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_review, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mAuthorView.setText(holder.mItem.getAuthor());
        holder.mContentView.setText(holder.mItem.getContent());

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
        final TextView mAuthorView;
        final TextView mContentView;
        ReviewEntry mItem;

        ViewHolder(View view) {
            super(view);
            mView = view;
            mAuthorView = view.findViewById(R.id.review_author);
            mContentView = view.findViewById(R.id.review_content);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
