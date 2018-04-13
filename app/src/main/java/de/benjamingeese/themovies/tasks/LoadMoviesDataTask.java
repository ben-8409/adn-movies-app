package de.benjamingeese.themovies.tasks;

import android.os.AsyncTask;

import java.io.IOException;
import java.util.List;

import de.benjamingeese.themovies.AsyncTaskCompleteListener;
import de.benjamingeese.themovies.data.Movie;
import de.benjamingeese.themovies.utilities.MovieDBApiUtils;
import okhttp3.HttpUrl;

public class LoadMoviesDataTask extends AsyncTask<String, Void, List<Movie>> {
    private AsyncTaskCompleteListener<List<Movie>> listener;

    public LoadMoviesDataTask(AsyncTaskCompleteListener<List<Movie>> listener) {
        this.listener = listener;
    }

    @Override
    protected List<Movie> doInBackground(String... params) {

        if (params.length == 0) return null;

        String path = params[0];
        HttpUrl url = MovieDBApiUtils.buildURL(path);
        try {
            String moviesApiResponse = MovieDBApiUtils.makeGetRequest(url);
            return MovieDBApiUtils.parseMovieListFromJSON(moviesApiResponse);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(List<Movie> movieListEntries) {
        listener.onTaskComplete(movieListEntries);
    }
}
