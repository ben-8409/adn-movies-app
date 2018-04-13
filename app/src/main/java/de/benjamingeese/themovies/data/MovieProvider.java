package de.benjamingeese.themovies.data;

import android.annotation.TargetApi;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

public class MovieProvider extends ContentProvider {

    public static final int CODE_MOVIES = 100;
    public static final int CODE_MOVIE_WITH_ID = 101;

    public static final String CONTENT_AUTHORITY = "de.benjamingeese.themovies.provider";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    private static final String TYPE = "vnd.android.cursor";
    private static final String SUBTYPE_ITEM = "vnd.android.cursor.item";
    private static final String SUBTYPE_DIR = "vnd.android.cursor.dir";

    private MoviesDbHelper mOpenHelper;
    private UriMatcher sUriMatcher = buildUriMatcher();

    public static UriMatcher buildUriMatcher() {

        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = CONTENT_AUTHORITY;

        matcher.addURI(authority, MoviesContract.PATH_MOVIES, CODE_MOVIES);

        matcher.addURI(authority, MoviesContract.PATH_MOVIES + "/#", CODE_MOVIE_WITH_ID);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new MoviesDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        Cursor cursor;

        switch (sUriMatcher.match(uri)) {
            case CODE_MOVIES:
                Log.d("MovieProvider", "Matched code movies");
                cursor = mOpenHelper.getReadableDatabase().query(
                        /* Table we are going to query */
                        MoviesContract.MovieEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case CODE_MOVIE_WITH_ID:
                Log.d("MovieProvider", "Matched code movie");
                String id = uri.getLastPathSegment();
                cursor = mOpenHelper.getReadableDatabase().query(
                        MoviesContract.MovieEntry.TABLE_NAME,
                        projection,
                        "WHERE " + MoviesContract.MovieEntry._ID + " = ?",
                        new String[]{id},
                        null,
                        null,
                        sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    /**
     * Implement this to handle requests for the MIME type of the data at the
     * given URI.
     *
     * @param uri the URI to query.
     * @return a MIME type string, or {@code null} if there is no type.
     */
    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        final String providerPart = CONTENT_AUTHORITY + "." + MoviesContract.MovieEntry.TABLE_NAME;
        switch (sUriMatcher.match(uri)) {
            case CODE_MOVIES:
                return TYPE + "." + SUBTYPE_DIR + "/" + providerPart;
            case CODE_MOVIE_WITH_ID:
                return TYPE + "." + SUBTYPE_ITEM + "/" + providerPart;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        if (values != null) {
            long id = values.getAsLong(MoviesContract.MovieEntry._ID);
            //db.beginTransaction();
            db.insert(MoviesContract.MovieEntry.TABLE_NAME, null, values);
            //db.endTransaction();
            getContext().getContentResolver().notifyChange(uri, null);
            return uri.buildUpon()
                    .appendPath(String.valueOf(id)).build();
        } else {
            return null;
        }
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        /* Users of the delete method will expect the number of rows deleted to be returned. */
        int numRowsDeleted;

        /*
         * This trick is from the SunShine codebase to make sure the number of rows deleted is returned
         */
        if (null == selection) selection = "1";

        switch (sUriMatcher.match(uri)) {

            case CODE_MOVIES:
                numRowsDeleted = mOpenHelper.getWritableDatabase().delete(
                        MoviesContract.MovieEntry.TABLE_NAME,
                        selection,
                        selectionArgs);

                break;

            case CODE_MOVIE_WITH_ID:
                String id = uri.getLastPathSegment();
                numRowsDeleted = mOpenHelper.getWritableDatabase().delete(
                        MoviesContract.MovieEntry.TABLE_NAME,
                        "WHERE " + MoviesContract.MovieEntry._ID + " = ?",
                        new String[]{id});
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        /* If we actually deleted any rows, notify that a change has occurred to this URI */
        if (numRowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return numRowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        throw new RuntimeException("Update is not implemented for TheMovies");
    }

    @Override
    @TargetApi(11)
    public void shutdown() {
        mOpenHelper.close();
        super.shutdown();
    }
}
