package com.smile.studio.libsmilestudio.google;

import androidx.annotation.NonNull;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by admin on 28/12/2016.
 */

public class YoutubeUtil {

    public static final String YOUTUBE = "https://www.youtube.com";
    public static final String YOUTUBE_VIDEO = "https://www.youtube.com/watch?v=";
    final static String reg = "(?:youtube(?:-nocookie)?\\.com\\/(?:[^\\/\\n\\s]+\\/\\S+\\/|(?:v|e(?:mbed)?)\\/|\\S*?[?&]v=)|youtu\\.be\\/)([a-zA-Z0-9_-]{11})";

    public static String getVideoId(@NonNull String videoUrl) {
        Pattern pattern = Pattern.compile(reg, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(videoUrl);
        if (matcher.find())
            return matcher.group(1);
        return null;
    }
}
