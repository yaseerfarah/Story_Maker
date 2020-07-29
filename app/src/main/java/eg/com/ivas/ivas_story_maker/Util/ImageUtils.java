/* Copyright 2015 The TensorFlow Authors. All Rights Reserved.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
==============================================================================*/

package eg.com.ivas.ivas_story_maker.Util;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;

/**
 * Utility class for manipulating images.
 **/
public class ImageUtils {

    public static Bitmap tfResizeBilinear(Bitmap bitmap, Drawable drawable, int w, int h) {
        if (bitmap == null) {
            return null;
        }

        Bitmap resized = Bitmap.createBitmap(w, h,
                Bitmap.Config.ARGB_8888);

        final Canvas canvas = new Canvas(resized);
        canvas.drawBitmap(bitmap,
                new Rect(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight()),
                new Rect(0, 0, w, h),
                null);

        return resized;
    }


    public static Bitmap getBitmapFromAsset(Context context, String strName) {
        AssetManager assetManager = context.getAssets();
        InputStream istr = null;
        try {
            istr = assetManager.open(strName);
            return BitmapFactory.decodeStream(istr);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public static   Bitmap decodeSampledBitmapFromResource(Context context,Uri uri,
                                                   int reqWidth, int reqHeight) {
        try {
            InputStream inputStream=context.getContentResolver().openInputStream(uri);
            // First decode with inJustDecodeBounds=true to check dimensions
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(inputStream, null, options);

            // Calculate inSampleSize
            options.inSampleSize =calculateInSampleSize(options, reqWidth, reqHeight);

            // Decode bitmap with inSampleSize set
            options.inJustDecodeBounds = false;
            InputStream inputStream1=context.getContentResolver().openInputStream(uri);

            return BitmapFactory.decodeStream(inputStream1,null,options);
        }catch (Exception e){
            e.fillInStackTrace();
            return null;
        }


    }

}
