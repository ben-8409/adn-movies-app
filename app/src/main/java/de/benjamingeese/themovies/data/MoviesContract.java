package de.benjamingeese.themovies.data;

import android.net.Uri;
import android.provider.BaseColumns;

import static de.benjamingeese.themovies.data.MovieProvider.BASE_CONTENT_URI;


public final class MoviesContract {

    //Movies table
    public static final String PATH_MOVIES = "movies";

    private MoviesContract() {
    }

    public static class MovieEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_MOVIES)
                .build();
        public static final String TABLE_NAME = "movies";
        public static final String COLUMN_NAME_TITLE = "title";
    }
}
