package de.benjamingeese.themovies.utilities;

import android.net.Uri;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.benjamingeese.themovies.BuildConfig;
import de.benjamingeese.themovies.data.Movie;
import de.benjamingeese.themovies.data.ReviewEntry;
import de.benjamingeese.themovies.data.VideoEntry;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Utility Class to handle requests to themoviedb.
 * Please note: You need a themoviedb api token which is not
 * included in this project. Instead, gradle is used to read the THE_MOVIE_DB_API_TOKEN from you user
 * gradle.properties file.
 * <p>
 * See: https://stackoverflow.com/questions/33134031/is-there-a-safe-way-to-manage-api-keys
 * and https://gist.github.com/VDenis/46c222b16683447bab33
 * Thanks to @VDenis (Denis Vlasov, https://gist.github.com/VDenis)
 * for the great example on Stack Overflow!
 */
public class MovieDBApiUtils {

    public static final String PATH_POPULAR = "movie/popular";
    public static final String PATH_TOP_RATED = "movie/top_rated";
    public static final String PATH_MOVIE_TRAILER = "movie/%KEY%/videos";

    private static final String API_KEY = BuildConfig.THE_MOVIE_DB_API_TOKEN;
    private static final String BASE_URL = "https://api.themoviedb.org/3";
    //TODO: dynamically choose size from "w92", "w154", "w185", "w342", "w500", "w780", or "original"
    private static final String IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w185";
    private static final String PARAM_API_KEY = "api_key";

    private static final String JSON_MOVIE_LIST_RESULT_OBJECT_KEY_ID = "id";
    private static final String JSON_MOVIE_LIST_RESULT_OBJECT_KEY_VIDEO = "video";
    private static final String JSON_MOVIE_LIST_RESULT_OBJECT_KEY_VOTE_AVERAGE = "vote_average";
    private static final String JSON_MOVIE_LIST_RESULT_OBJECT_KEY_TITLE = "title";
    private static final String JSON_MOVIE_LIST_RESULT_OBJECT_KEY_POPULARITY = "popularity";
    private static final String JSON_MOVIE_LIST_RESULT_OBJECT_KEY_POSTER_PATH = "poster_path";
    private static final String JSON_MOVIE_LIST_RESULT_OBJECT_KEY_ORIGINAL_LANGUAGE = "original_language";
    private static final String JSON_MOVIE_LIST_RESULT_OBJECT_KEY_ORIGINAL_TITLE = "original_title";
    private static final String JSON_MOVIE_LIST_RESULT_OBJECT_KEY_GENRE_LIST = "genre_ids";
    private static final String JSON_MOVIE_LIST_RESULT_OBJECT_KEY_BACKDROP_PATH = "backdrop_path";
    private static final String JSON_MOVIE_LIST_RESULT_OBJECT_KEY_ADULT = "adult";
    private static final String JSON_MOVIE_LIST_RESULT_OBJECT_KEY_DATE = "release_date";
    private static final String JSON_MOVIE_LIST_RESULT_OBJECT_KEY_OVERVIEW = "overview";
    private static final String JSON_MOVIE_LIST_TOTAL_RESULTS = "total_results";
    private static final String JSON_RESULTS_KEY = "results";

    //TRAILERS
    private static final String JSON_MOVIE_VIDEOS_KEY_ID = "id";
    private static final String JSON_MOVIE_VIDEOS_KEY_KEY = "key";
    private static final String JSON_MOVIE_VIDEOS_KEY_NAME = "name";
    private static final String JSON_MOVIE_VIDEOS_KEY_SITE = "site";
    private static final String JSON_MOVIE_VIDEOS_KEY_TYPE = "type";
    private static final String JSON_MOVIE_VIDEOS_KEY_SIZE = "size";
    private static final String JSON_MOVIE_VIDEOS_KEY_LANG = "iso_639_1";
    private static final String JSON_MOVIE_VIDEOS_KEY_REGION = "iso_3166_1";

    //REVIEWS
    private static final String PATH_REVIEWS = "movie/%KEY%/reviews";
    private static final String JSON_MOVIE_REVIEW_ID = "id";
    private static final String JSON_MOVIE_REVIEW_AUTHOR = "author";
    private static final String JSON_MOVIE_REVIEW_CONTENT = "content";
    private static final String JSON_MOVIE_REVIEW_URL = "url";


    public static HttpUrl buildURL(String path) {
        HttpUrl url = HttpUrl.parse(BASE_URL);
        if (null != url) {
            url = url.newBuilder()
                    .addPathSegments(path)
                    .addQueryParameter(PARAM_API_KEY, API_KEY)
                    .build();
        }

        return url;
    }

    public static HttpUrl buildReviewsUrl(String movieId) {
        return buildURL(PATH_REVIEWS.replace("%KEY%", movieId));
    }

    public static Uri buildImageURI(String imagePath) {
        return Uri.parse(IMAGE_BASE_URL).buildUpon()
                .appendEncodedPath(imagePath)
                .build();
    }

    public static String makeGetRequest(HttpUrl url) throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = client.newCall(request).execute();
        ResponseBody body = response.body();
        if (null != body) {
            return body.string();
        } else {
            return null;
        }
    }


    public static Movie parseMovieListEntryFromJSON(JSONObject entry) throws JSONException {
        Movie movie = new Movie();

        movie.setUid(entry.getLong(JSON_MOVIE_LIST_RESULT_OBJECT_KEY_ID));
        movie.setVideo(entry.getBoolean(JSON_MOVIE_LIST_RESULT_OBJECT_KEY_VIDEO));
        movie.setVoteAverage(entry.getDouble(JSON_MOVIE_LIST_RESULT_OBJECT_KEY_VOTE_AVERAGE));
        movie.setTitle(entry.getString(JSON_MOVIE_LIST_RESULT_OBJECT_KEY_TITLE));
        movie.setPopularity(entry.getDouble(JSON_MOVIE_LIST_RESULT_OBJECT_KEY_POPULARITY));
        movie.setPosterPath(entry.getString(JSON_MOVIE_LIST_RESULT_OBJECT_KEY_POSTER_PATH));
        movie.setOriginalLanguage(entry.getString(JSON_MOVIE_LIST_RESULT_OBJECT_KEY_ORIGINAL_LANGUAGE));
        movie.setOriginalTitle(entry.getString(JSON_MOVIE_LIST_RESULT_OBJECT_KEY_ORIGINAL_TITLE));
        JSONArray jsonGenreIds = entry.getJSONArray(JSON_MOVIE_LIST_RESULT_OBJECT_KEY_GENRE_LIST);
        List<Integer> genreIds = new ArrayList<>(jsonGenreIds.length());
        for (int i = 0; i < jsonGenreIds.length(); i++) {
            genreIds.add(jsonGenreIds.getInt(i));
        }
        movie.setGenreIds(genreIds);
        movie.setBackdropPath(entry.getString(JSON_MOVIE_LIST_RESULT_OBJECT_KEY_BACKDROP_PATH));
        movie.setAdult(entry.getBoolean(JSON_MOVIE_LIST_RESULT_OBJECT_KEY_ADULT));
        movie.setReleaseDate(entry.getString(JSON_MOVIE_LIST_RESULT_OBJECT_KEY_DATE));
        movie.setOverview(entry.getString(JSON_MOVIE_LIST_RESULT_OBJECT_KEY_OVERVIEW));


        return movie;
    }


    public static List<Movie> parseMovieListFromJSON(String json) {
        List<Movie> movieListEntries = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(json);
            if (jsonObject.getLong(JSON_MOVIE_LIST_TOTAL_RESULTS) > 0) {
                JSONArray results = jsonObject.getJSONArray(JSON_RESULTS_KEY);
                for (int i = 0; i < results.length(); i++) {
                    movieListEntries.add(parseMovieListEntryFromJSON(results.getJSONObject(i)));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return movieListEntries;
    }

    public static VideoEntry parseVideoItem(JSONObject jsonObject) throws JSONException {
        VideoEntry video = new VideoEntry();
        video.setId(jsonObject.getString(JSON_MOVIE_VIDEOS_KEY_ID));
        video.setLanguage(jsonObject.getString(JSON_MOVIE_VIDEOS_KEY_LANG));
        video.setRegion(jsonObject.getString(JSON_MOVIE_VIDEOS_KEY_REGION));
        video.setKey(jsonObject.getString(JSON_MOVIE_VIDEOS_KEY_KEY));
        video.setName(jsonObject.getString(JSON_MOVIE_VIDEOS_KEY_NAME));
        video.setSite(jsonObject.getString(JSON_MOVIE_VIDEOS_KEY_SITE));
        video.setSize(jsonObject.getInt(JSON_MOVIE_VIDEOS_KEY_SIZE));
        video.setType(jsonObject.getString(JSON_MOVIE_VIDEOS_KEY_TYPE));

        return video;
    }

    public static List<VideoEntry> parseVideoListFromJSON(String json) {
        List<VideoEntry> videos = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray results = jsonObject.getJSONArray(JSON_RESULTS_KEY);
            for (int i = 0; i < results.length(); i++) {
                videos.add(parseVideoItem(results.getJSONObject(i)));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return videos;
    }

    private static ReviewEntry parseReviewItem(JSONObject jsonObject) throws JSONException {
        ReviewEntry review = new ReviewEntry();
        review.setId(jsonObject.getString(JSON_MOVIE_REVIEW_ID));
        review.setAuthor(jsonObject.getString(JSON_MOVIE_REVIEW_AUTHOR));
        review.setContent(jsonObject.getString(JSON_MOVIE_REVIEW_CONTENT));
        review.setUrl(jsonObject.getString(JSON_MOVIE_REVIEW_URL));
        return review;
    }

    public static List<ReviewEntry> parseReviewsListFromJson(String reviewsApiResponse) {
        List<ReviewEntry> reviews = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(reviewsApiResponse);
            JSONArray results = jsonObject.getJSONArray(JSON_RESULTS_KEY);
            for (int i = 0; i < results.length(); i++) {
                reviews.add(parseReviewItem(results.getJSONObject(i)));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return reviews;
    }


}
