package de.benjamingeese.themovies;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import de.benjamingeese.themovies.VideosFragment.OnListFragmentInteractionListener;
import de.benjamingeese.themovies.data.VideoEntry;

public class MyVideoRecyclerViewAdapter extends RecyclerView.Adapter<MyVideoRecyclerViewAdapter.ViewHolder> {

    private final List<VideoEntry> mValues;
    private final OnListFragmentInteractionListener mListener;

    public MyVideoRecyclerViewAdapter(List<VideoEntry> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.video, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        VideoEntry entry = mValues.get(position);
        holder.mItem = entry;
        holder.mNameTextView.setText(entry.getName());
        holder.mTypeTextView.setText(entry.getType());

        holder.mWatchButton.setOnClickListener(v -> {
            if (null != mListener) {
                // Notify the active callbacks interface (the activity, if the
                // fragment is attached to one) that an item has been selected.
                mListener.onVideoListFragmentInteraction(holder.mItem);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mNameTextView;
        public final TextView mTypeTextView;
        public final Button mWatchButton;

        public VideoEntry mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mNameTextView = view.findViewById(R.id.video_name_tv);
            mTypeTextView = view.findViewById(R.id.video_type_tv);
            mWatchButton = view.findViewById(R.id.watch_video_btn);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mNameTextView.getText() + "'";
        }
    }
}
