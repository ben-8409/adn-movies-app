package de.benjamingeese.themovies;

import org.json.JSONObject;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import de.benjamingeese.themovies.data.Movie;
import de.benjamingeese.themovies.utilities.MovieDBApiUtils;
import okhttp3.HttpUrl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class MovieDBUtilsTest {

    @Test
    public void builtUrlAreCorrect() throws Exception {
        /*
         * The url should look as follows:
         * HTTPS://BASE_URL/PATH?api_key=THE_MOVIE_DB_API_TOKEN
         */
        //MOST_POPULAR: https://api.themoviedb.org/3/movie/popular?api_key=THE_MOVIE_DB_API_TOKEN
        String popularUrl = "https://api.themoviedb.org/3/movie/popular?api_key=" + BuildConfig.THE_MOVIE_DB_API_TOKEN;
        assertEquals(popularUrl, MovieDBApiUtils.buildURL(MovieDBApiUtils.PATH_POPULAR).toString());
    }

    @Test
    public void apiCallWorks() throws Exception {
        HttpUrl url = MovieDBApiUtils.buildURL(MovieDBApiUtils.PATH_POPULAR);
        assertNotNull(url);
        String apiResponse = MovieDBApiUtils.makeGetRequest(url);
        System.out.println(apiResponse);
        assertNotNull(apiResponse);
        assertTrue(apiResponse.length() > 0);
    }

    @Test
    public void parseMovieListEntry() throws Exception {
        String exampleResponse = "{\"vote_count\":1," +
                "\"id\":1," +
                "\"video\":true," +
                "\"vote_average\":5," +
                "\"title\":\"Good Movie\"," +
                "\"popularity\":400.89," +
                "\"poster_path\":\"/test_poster_path.jpg\"," +
                "\"original_language\":\"de\"," +
                "\"original_title\":\"Guter Film\"," +
                "\"genre_ids\":[10749]," +
                "\"backdrop_path\":\"/test_backdrop_path.jpg\"," +
                "\"adult\":false," +
                "\"overview\":\"A simply good movie\"," +
                "\"release_date\":\"2017-04-21\"}";
        JSONObject exampleJSON = new JSONObject(exampleResponse);
        Movie movie = MovieDBApiUtils.parseMovieListEntryFromJSON(exampleJSON);

        assertEquals(1, movie.getUid());
        assertTrue("This entry should have a video.", movie.hasVideo());
        assertFalse("This is not an adult movie", movie.isAdult());
        assertEquals("The vote average does not match", 5, movie.getVoteAverage(), 0.0);
        assertEquals("Good Movie", movie.getTitle());
        assertEquals("Guter Film", movie.getOriginalTitle());
        assertEquals("de", movie.getOriginalLanguage());
        assertEquals(400.89, movie.getPopularity(), 0.01);
        assertEquals("/test_poster_path.jpg", movie.getPosterPath());
        assertEquals("/test_backdrop_path.jpg", movie.getBackdropPath());
        assertEquals("A simply good movie", movie.getOverview());
        assertEquals("2017-04-21", movie.getReleaseDate());
        List<Integer> genres = new ArrayList<>();
        genres.add(10749);
        assertEquals(genres, movie.getGenreIds());

    }

    @Test
    public void parseMovieList() throws Exception {
        String exampleResponse = "{\"page\":1,\"total_results\":1,\"total_pages\":1,\"results\":[{\"vote_count\":1,\"id\":1,\"video\":false,\"vote_average\":5,\"title\":\"Good Movie\",\"popularity\":400.89,\"poster_path\":\"/6uOMVZ6oG00xjq0KQiExRBw2s3P.jpg\",\"original_language\":\"de\",\"original_title\":\"Guter Film\",\"genre_ids\":[10749],\"backdrop_path\":\"/iBM6zvlOmcfodGhUa36sy7pM2Er.jpg\",\"adult\":false,\"overview\":\"A simply good movie\",\"release_date\":\"2017-04-21\"}]}";
        List<Movie> list = MovieDBApiUtils.parseMovieListFromJSON(exampleResponse);
        assertEquals("Wrong number of results", 1, list.size());
    }
}
