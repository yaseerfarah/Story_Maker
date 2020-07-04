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

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

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

}
