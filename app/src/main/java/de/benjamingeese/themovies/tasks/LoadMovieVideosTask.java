package de.benjamingeese.themovies.tasks;

import android.os.AsyncTask;

import java.io.IOException;
import java.util.List;

import de.benjamingeese.themovies.AsyncTaskCompleteListener;
import de.benjamingeese.themovies.data.VideoEntry;
import de.benjamingeese.themovies.utilities.MovieDBApiUtils;
import okhttp3.HttpUrl;

public class LoadMovieVideosTask extends AsyncTask<String, Void, List<VideoEntry>> {
    private AsyncTaskCompleteListener<List<VideoEntry>> listener;

    public LoadMovieVideosTask(AsyncTaskCompleteListener<List<VideoEntry>> listener) {
        this.listener = listener;
    }

    @Override
    protected List<VideoEntry> doInBackground(String... params) {

        if (params.length == 0) return null;

        String movie_key = params[0];
        String path = MovieDBApiUtils.PATH_MOVIE_TRAILER.replace("%KEY%", movie_key);
        HttpUrl url = MovieDBApiUtils.buildURL(path);
        try {
            String moviesApiResponse = MovieDBApiUtils.makeGetRequest(url);
            return MovieDBApiUtils.parseVideoListFromJSON(moviesApiResponse);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(List<VideoEntry> results) {
        listener.onTaskComplete(results);
    }
}
