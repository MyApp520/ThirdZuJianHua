package com.example.commonlib.util;

import android.graphics.Bitmap;
import android.view.View;

/**
 * Created by smile on 2019/4/24.
 */

public class BitmapUtil {

    /**
     * view转成bitmap.
     *
     * @param view
     * @return the view bitmap
     */
    private Bitmap getViewBitmap(View view) {
        view.setDrawingCacheEnabled(true);
        //测量view
        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());

        view.buildDrawingCache();
        Bitmap cacheBitmap = view.getDrawingCache();
        Bitmap bitmap = null;
        if (cacheBitmap != null) {
            bitmap = Bitmap.createBitmap(cacheBitmap);
            cacheBitmap.recycle();
        }
        view.setDrawingCacheEnabled(false);
        return bitmap;
    }
}
