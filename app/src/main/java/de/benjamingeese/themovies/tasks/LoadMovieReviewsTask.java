package de.benjamingeese.themovies.tasks;

import android.os.AsyncTask;

import java.io.IOException;
import java.util.List;

import de.benjamingeese.themovies.AsyncTaskCompleteListener;
import de.benjamingeese.themovies.data.ReviewEntry;
import de.benjamingeese.themovies.utilities.MovieDBApiUtils;
import okhttp3.HttpUrl;

public class LoadMovieReviewsTask extends AsyncTask<String, Void, List<ReviewEntry>> {
    private AsyncTaskCompleteListener<List<ReviewEntry>> listener;

    public LoadMovieReviewsTask(AsyncTaskCompleteListener<List<ReviewEntry>> listener) {
        this.listener = listener;
    }

    @Override
    protected List<ReviewEntry> doInBackground(String... params) {

        if (params.length == 0) return null;

        String movie_key = params[0];
        HttpUrl url = MovieDBApiUtils.buildReviewsUrl(movie_key);
        try {
            String reviewsApiResponse = MovieDBApiUtils.makeGetRequest(url);
            return MovieDBApiUtils.parseReviewsListFromJson(reviewsApiResponse);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(List<ReviewEntry> results) {
        listener.onTaskComplete(results);
    }
}
