/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.smile.studio.libsmilestudio.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.os.Build;
import android.text.Html;
import android.text.Spanned;
import android.view.Display;
import android.view.WindowManager;

import java.text.Normalizer;
import java.util.Random;
import java.util.regex.Pattern;

/**
 * A collection of utility methods, all static.
 */
public class Utils {
    private static Random rand = new Random();

    public static String removeAccent(String s) {
        String temp = Normalizer.normalize(s, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(temp).replaceAll("").replaceAll("Đ|đ", "d");
    }

    @SuppressWarnings("deprecation")
    public static Spanned fromHtml(String html) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY);
        } else {
            return Html.fromHtml(html);
        }
    }

    public static int random(int n) {
        return rand.nextInt(n);
    }

    public static String bytesToSize(long bytes) {
        String[] sizes = {"Bytes", "KB", "MB", "GB", "TB"};
        if (bytes == 0)
            return "0 Byte";
        int i = (int) (Math.floor(Math.log(bytes) / Math.log(1024)));
        return String.format("%3.1f" + " " + sizes[i], bytes / Math.pow(1024, i));
    }

    public static String convertNumberLongToString(long money) {
        if (money < 1000)
            return "" + money;
        else if (money < 1000000) {
            return convertSub(money) + "K";
        } else if (money < 1000000000) {
            return convertSub(money / 1000) + "M";
        } else if (money < 1000000000000L) {
            return convertSub(money / 1000000) + "B";
        } else if (money < 1000000000000000L) {
            return convertSub(money / 1000000000) + "KB";
        } else
            return convertSub(money / 1000000000000L) + "MB";
    }

    public static final String convertSub(long l) {
        if (l > 100000)
            return (l / 1000) + "";
        else if (l > 10000) {
            if ((l / 100) % 10 == 0)
                return (l / 1000) + "";
            else
                return (l / 1000) + "." + ((l / 100) % 10);
        } else {
            if ((l / 10) % 100 == 0)
                return (l / 1000) + "";
            else {
                if (((l / 10) % 10) == 0)
                    return (l / 1000) + "." + ((l / 100) % 10);
                else
                    return (l / 1000) + "." + ((l / 10) % 100);
            }
        }
    }

    /**
     * Returns the screen/display size
     */
    public static Point getDisplaySize(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size;
    }

    public static int convertDpToPixel(Context ctx, int dp) {
        float density = ctx.getResources().getDisplayMetrics().density;
        return Math.round((float) dp * density);
    }

    public static String version(Context context) throws PackageManager.NameNotFoundException {
        PackageManager manager = context.getPackageManager();
        PackageInfo packageinfo;
        packageinfo = manager.getPackageInfo(context.getPackageName(), 0);
        return packageinfo.versionName;
    }

    /**
     * Formats time in milliseconds to hh:mm:ss string format.
     */
    public static String formatMillis(int millis) {
        String result = "";
        int hr = millis / 3600000;
        millis %= 3600000;
        int min = millis / 60000;
        millis %= 60000;
        int sec = millis / 1000;
        if (hr > 0) {
            result += hr + ":";
        }
        if (min >= 0) {
            if (min > 9) {
                result += min + ":";
            } else {
                result += "0" + min + ":";
            }
        }
        if (sec > 9) {
            result += sec;
        } else {
            result += "0" + sec;
        }
        return result;
    }
}
