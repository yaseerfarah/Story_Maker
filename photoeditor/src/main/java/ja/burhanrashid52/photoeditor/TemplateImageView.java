package ja.burhanrashid52.photoeditor;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.widget.ImageView;

import androidx.annotation.Nullable;

@SuppressLint("AppCompatCustomView")
public class TemplateImageView extends ImageView {
    public TemplateImageView(Context context) {
        super(context);
    }

    public TemplateImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TemplateImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public TemplateImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Drawable d = getDrawable();
        if (d != null) {
            if (d.getIntrinsicWidth()>=d.getIntrinsicHeight()) {

                int w = MeasureSpec.getSize(widthMeasureSpec);
                int h = w * d.getIntrinsicHeight() / d.getIntrinsicWidth();

                setMeasuredDimension(w,h);


            }else {
                int h = MeasureSpec.getSize(heightMeasureSpec);
                int w = h * d.getIntrinsicWidth() / d.getIntrinsicHeight();


                setMeasuredDimension(w, h);
            }
        }
        else super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }


}
