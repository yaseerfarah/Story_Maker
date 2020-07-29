package eg.com.ivas.ivas_story_maker.View;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.dailystudio.app.utils.BitmapUtils;
import com.dailystudio.development.Logger;
import com.gturedi.views.StatefulLayout;

import java.util.ArrayList;
import java.util.List;

import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import eg.com.ivas.ivas_story_maker.Adapter.CardFilterAdapter;
import eg.com.ivas.ivas_story_maker.Interface.DeeplabInterface;
import eg.com.ivas.ivas_story_maker.ML.DeeplabMobile;
import eg.com.ivas.ivas_story_maker.POJO.FilterInfo;
import eg.com.ivas.ivas_story_maker.POJO.SpiralFilter;
import eg.com.ivas.ivas_story_maker.R;
import eg.com.ivas.ivas_story_maker.Util.ImageUtils;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import ja.burhanrashid52.photoeditor.ItemSelectListener;
import ja.burhanrashid52.photoeditor.OnPhotoEditorListener;
import ja.burhanrashid52.photoeditor.OnSaveBitmap;
import ja.burhanrashid52.photoeditor.PhotoEditorView;
import ja.burhanrashid52.photoeditor.PhotoFilter;
import ja.burhanrashid52.photoeditor.ViewType;

import static eg.com.ivas.ivas_story_maker.Util.ImageUtils.getBitmapFromAsset;
import static eg.com.ivas.ivas_story_maker.View.ImageEditor.CROP_BITMAB;


public class ImageSpiral extends Fragment implements ItemSelectListener<SpiralFilter> {


   private DeeplabInterface deeplabInterface;
   private ja.burhanrashid52.photoeditor.PhotoEditor photoEditor;

    private CardFilterAdapter<SpiralFilter> filterCardViewAdapter;
    private List<SpiralFilter> filterInfoList=new ArrayList<>();

    @BindView(R.id.photoEditorView)
    PhotoEditorView photoEditorView;

    @BindView(R.id.done)
    ImageButton done;

    @BindView(R.id.close)
    ImageButton close;


    @BindView(R.id.progress)
    ProgressBar progressBar;

    @BindView(R.id.stateful)
    StatefulLayout statefulLayout;

    @BindView(R.id.filterRecycler)
    RecyclerView filterRecyclerView;

    private ImageView backFilter,frontFilter;


    public ImageSpiral() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_image_filter, container, false);
    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this,view);

        deeplabInterface=new DeeplabMobile();
        deeplabInterface.initialize(getContext());
        photoEditor = new ja.burhanrashid52.photoeditor.PhotoEditor.Builder(getContext(), photoEditorView)
                .build();

        filterInfoList.addAll(init_SpiralList());
        filterRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.HORIZONTAL,false));




        photoEditor.setOnPhotoEditorListener(new OnPhotoEditorListener() {
            @Override
            public void onEditTextChangeListener(View rootView, String text, int colorCode) {



            }

            @Override
            public void onAddViewListener(ViewType viewType, View view1, int numberOfAddedViews) {
                if (viewType==ViewType.TEMPLATE){

                    view1.setElevation(1);
                }else {
                    if (backFilter==null){
                        view1.setElevation(0);
                        backFilter=(ImageView) view1.findViewById(ja.burhanrashid52.photoeditor.R.id.imgPhotoEditorImage);

                        backFilter.setVisibility(View.INVISIBLE);

                    }else {
                        view1.setElevation(2);
                        frontFilter=(ImageView)view1.findViewById(ja.burhanrashid52.photoeditor.R.id.imgPhotoEditorImage);

                        frontFilter.setVisibility(View.INVISIBLE);

                    }
                }

            }

            @Override
            public void onRemoveViewListener(ViewType viewType,View view1, int numberOfAddedViews) {

            }

            @Override
            public void onStartViewChangeListener(ViewType viewType) {

            }

            @Override
            public void onStopViewChangeListener(ViewType viewType) {

            }
        });






        statefulLayout.showLoading(" ");
        photoEditorView.getSource().setImageBitmap(CROP_BITMAB);
        photoEditor.addSpiralFilter();
        photoEditor.addSpiralFilter();
        done.setVisibility(View.INVISIBLE);

        photoEditorView.getSource().post(() -> {

            segment(CROP_BITMAB,photoEditorView.getSource().getDrawable())
                    .subscribeOn(Schedulers.computation())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(bitmap -> {
                        photoEditor.addAiImage(bitmap);
                        filterCardViewAdapter=new CardFilterAdapter<SpiralFilter>(getContext(),filterInfoList,this);
                        filterRecyclerView.setAdapter(filterCardViewAdapter);
                        done.setVisibility(View.VISIBLE);
                       statefulLayout.showContent();
                    },throwable -> {

                        Log.e(getClass().getName(),throwable.getMessage());
                        progressBar.setVisibility(View.INVISIBLE);
                    });


        });






        close.setOnClickListener(v -> {

            ImageEditor.isDoneCroping=false;
            Navigation.findNavController(view).navigateUp();
        });


        done.setOnClickListener(v -> {

            statefulLayout.showLoading(" ");
            photoEditor.saveAsBitmap(new OnSaveBitmap() {
                @Override
                public void onBitmapReady(Bitmap saveBitmap) {
                    CROP_BITMAB=saveBitmap;
                    ImageEditor.isDoneCroping=true;
                    Navigation.findNavController(view).navigateUp();
                }

                @Override
                public void onFailure(Exception e) {
                   statefulLayout.showContent();

                    Log.e(getClass().getName(),e.getMessage());
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            },getActivity());

        });




    }



    private Single<Bitmap> segment(Bitmap bitmap, Drawable drawable){

        return Single.create(emitter -> {

            try {
                final int w = drawable.getIntrinsicWidth();
                final int h = drawable.getIntrinsicHeight();
                Logger.debug("decoded file dimen: [%d x %d]", w, h);


                float resizeRatio = (float) deeplabInterface.getInputSize() / Math.max(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                int rw = Math.round(w * resizeRatio);
                int rh = Math.round(h * resizeRatio);

                Logger.debug("resize bitmap: ratio = %f, [%d x %d] -> [%d x %d]",
                        resizeRatio, w, h, rw, rh);

                Bitmap resized = ImageUtils.tfResizeBilinear(bitmap,drawable, rw, rh);

                Bitmap mask = deeplabInterface.segment(resized);

                mask = BitmapUtils.createClippedBitmap(mask,
                        (mask.getWidth() - rw) / 2,
                        (mask.getHeight() - rh) / 2,
                        rw, rh);
                mask = BitmapUtils.scaleBitmap(mask, w, h);


                final Bitmap cropped = cropBitmapWithMask(bitmap,drawable, mask);

                emitter.onSuccess(cropped);

            }catch (Exception e){
                emitter.onError(e);
            }

        });

    }






    private Bitmap cropBitmapWithMask(Bitmap original,Drawable drawable, Bitmap mask) {
        if (original == null
                || mask == null) {
            return null;
        }

        final int w = drawable.getIntrinsicWidth();
        final int h = drawable.getIntrinsicHeight();
        if (w <= 0 || h <= 0) {
            return null;
        }

        Bitmap cropped = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);


        Canvas canvas = new Canvas(cropped);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        canvas.drawBitmap(original, 0, 0, null);
        canvas.drawBitmap(mask, 0, 0, paint);
        paint.setXfermode(null);

        return cropped;
    }



    private List<SpiralFilter> init_SpiralList(){
        List<SpiralFilter> spiralFilterList=new ArrayList<>();

        spiralFilterList.add(new SpiralFilter("Glitter","SpiralFilters/glitter_icon.png","SpiralFilters/glitter_front.png","SpiralFilters/glitter_back.png"));
        spiralFilterList.add(new SpiralFilter("Heart Neon","SpiralFilters/heart_neon_icon.png","SpiralFilters/heart_neon_front.png","SpiralFilters/heart_neon_back.png"));
        spiralFilterList.add(new SpiralFilter("Heart Spiral","SpiralFilters/heart_spiral_icon.png","SpiralFilters/heart_spiral_front.png","SpiralFilters/heart_spiral_back.png"));
        spiralFilterList.add(new SpiralFilter("Spiral","SpiralFilters/spi2_icon.png","SpiralFilters/spiral1.png","SpiralFilters/spiral2.png"));
        spiralFilterList.add(new SpiralFilter("Valentine","SpiralFilters/valentine2_icon.png","SpiralFilters/valentine2.png"));
        spiralFilterList.add(new SpiralFilter("Wings","SpiralFilters/wings_icon.png","SpiralFilters/wings.png"));

        return spiralFilterList;
    }



    @Override
    public void onSelect(SpiralFilter spiralFilter) {
        if (spiralFilter.isIs_2_Part()){
            backFilter.setImageBitmap(getBitmapFromAsset(getContext(),spiralFilter.getBackPart()));
            frontFilter.setVisibility(View.VISIBLE);
            backFilter.setVisibility(View.VISIBLE);
            frontFilter.setImageBitmap(getBitmapFromAsset(getContext(),spiralFilter.getFrontPart()));
        }else {
            frontFilter.setVisibility(View.INVISIBLE);

            backFilter.setImageBitmap(getBitmapFromAsset(getContext(),spiralFilter.getBackPart()));

        }
    }
}