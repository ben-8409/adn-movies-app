package de.benjamingeese.themovies.utilities;

import android.net.Uri;

public class YoutubeUrlUtils {
    private final static String BASE_URL = "https://youtube.com";
    private final static String WATCH_PATH = "watch";
    private final static String VIDEO_KEY = "v";

    public static Uri buildWatchVideoUrl(String videoKey) {
        return Uri.parse(BASE_URL).buildUpon()
                .appendEncodedPath(WATCH_PATH)
                .appendQueryParameter(VIDEO_KEY, videoKey)
                .build();
    }
}
