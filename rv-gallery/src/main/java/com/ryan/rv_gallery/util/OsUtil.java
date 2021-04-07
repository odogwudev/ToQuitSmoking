package com.ryan.rv_gallery.util;

import android.app.Activity;
import android.content.res.Resources;
import android.util.DisplayMetrics;

/**
 *
 * @author RyanLee
 * @date 2017/11/29
 */

public class OsUtil {
    public static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    public static int pxToDp(int px) {
        return (int) (px / Resources.getSystem().getDisplayMetrics().density);
    }


    /**
     * Get screen width
     *
     * @return
     */
    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    /**
     * Get screen height
     *
     * @return
     */
    public static int getScreenHeigth() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

    /**
     * Get screen width
     *
     * @return
     */
    public static int getScreenWidth(Activity activity) {
        DisplayMetrics outMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;

    }
}
