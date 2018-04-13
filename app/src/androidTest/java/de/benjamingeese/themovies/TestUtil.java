package de.benjamingeese.themovies;

import android.content.Context;

import de.benjamingeese.themovies.data.Movie;
import de.benjamingeese.themovies.test.R;

public class TestUtil {
    public static Movie createMovie(long id, Context context) {
        Movie movie = new Movie();
        movie.setUid(1);
        movie.setTitle(context.getString(R.string.text_movie_title));
        return movie;
    }
}
