package de.benjamingeese.themovies;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import de.benjamingeese.themovies.data.Movie;
import de.benjamingeese.themovies.data.MoviesContract;
import de.benjamingeese.themovies.data.MoviesDbHelper;

import static org.junit.Assert.assertEquals;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class DataTests {

    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("de.benjamingeese.themovies", appContext.getPackageName());
    }

    @Before
    public void setUp() {
        deleteAllRecordsFromMovieTable();
    }

    @Test
    public void testInsertFavorite() throws Exception {
        Context appContext = InstrumentationRegistry.getTargetContext();


        //create a movie
        Movie test = TestUtil.createMovie(1, appContext);
        //create values
        ContentValues cv = new ContentValues();
        cv.put(MoviesContract.MovieEntry._ID, test.getUid());
        cv.put(MoviesContract.MovieEntry.COLUMN_NAME_TITLE, test.getTitle());

        ContentResolver cr = appContext.getContentResolver();

        Log.i("DataTests", MoviesContract.MovieEntry.CONTENT_URI.toString());
        //query the db
        int countItemsBefore =
                cr.query(MoviesContract.MovieEntry.CONTENT_URI, null, null, null, null)
                        .getCount();
        assertEquals(0, countItemsBefore);

        //insert the movie
        cr.insert(MoviesContract.MovieEntry.CONTENT_URI, cv);

        //query the database
        Cursor cursorAfter =
                cr.query(MoviesContract.MovieEntry.CONTENT_URI, null, null, null, null);
        assertEquals("Should be one movie more in the database", countItemsBefore + 1, cursorAfter.getCount());
        cursorAfter.moveToNext();
        assertEquals("Movie ids match", test.getUid(), cursorAfter.getLong(0));
        assertEquals("Movie titles match", test.getTitle(), cursorAfter.getString(1));

    }

    private void deleteAllRecordsFromMovieTable() {
        /* Access writable database through WeatherDbHelper */
        MoviesDbHelper helper = new MoviesDbHelper(InstrumentationRegistry.getTargetContext());
        SQLiteDatabase database = helper.getWritableDatabase();

        database.delete(MoviesContract.MovieEntry.TABLE_NAME, null, null);

        database.close();
    }

}
