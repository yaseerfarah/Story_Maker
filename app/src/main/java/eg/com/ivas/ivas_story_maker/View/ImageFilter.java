package eg.com.ivas.ivas_story_maker.View;

import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import eg.com.ivas.ivas_story_maker.Adapter.CardFilterAdapter;
import eg.com.ivas.ivas_story_maker.POJO.FilterInfo;
import eg.com.ivas.ivas_story_maker.R;
import ja.burhanrashid52.photoeditor.FilterCompleteListener;
import ja.burhanrashid52.photoeditor.ItemSelectListener;
import ja.burhanrashid52.photoeditor.OnSaveBitmap;
import ja.burhanrashid52.photoeditor.PhotoEditorView;
import ja.burhanrashid52.photoeditor.PhotoFilter;

import static eg.com.ivas.ivas_story_maker.View.ImageEditor.CROP_BITMAB;


public class ImageFilter extends Fragment implements ItemSelectListener<FilterInfo> {


   private ja.burhanrashid52.photoeditor.PhotoEditor photoEditor;
   private CardFilterAdapter<FilterInfo> filterCardViewAdapter;
   private List<FilterInfo> filterInfoList=new ArrayList<>();

   @BindView(R.id.photoEditorView)
    PhotoEditorView photoEditorView;

    @BindView(R.id.done)
    ImageButton done;
    @BindView(R.id.close)
    ImageButton close;

    @BindView(R.id.progress)
    ProgressBar progressBar;

    @BindView(R.id.filterRecycler)
    RecyclerView filterRecyclerView;


    public ImageFilter() {
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

        photoEditor = new ja.burhanrashid52.photoeditor.PhotoEditor.Builder(getContext(), photoEditorView)
                .build();

        assignfilterList();
        photoEditorView.getSource().setImageBitmap(CROP_BITMAB);

        filterRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.HORIZONTAL,false));
        filterCardViewAdapter=new CardFilterAdapter<FilterInfo>(getContext(),filterInfoList,this);
        filterRecyclerView.setAdapter(filterCardViewAdapter);



        close.setOnClickListener(v -> {

            ImageEditor.isDoneCroping=false;
            Navigation.findNavController(view).navigateUp();
        });



        done.setOnClickListener(v -> {
            progressBar.setVisibility(View.VISIBLE);
            photoEditorView.saveFilter(new OnSaveBitmap() {
                @Override
                public void onBitmapReady(Bitmap saveBitmap) {
                    progressBar.setVisibility(View.INVISIBLE);
                    CROP_BITMAB=saveBitmap;
                    ImageEditor.isDoneCroping=true;
                    Navigation.findNavController(view).navigateUp();
                }

                @Override
                public void onFailure(Exception e) {

                }
            });

        });

    }



    private void assignfilterList(){

        filterInfoList.add(new FilterInfo("filters/original.jpg", PhotoFilter.NONE));
        filterInfoList.add(new FilterInfo("filters/auto_fix.png", PhotoFilter.AUTO_FIX));
        filterInfoList.add(new FilterInfo("filters/brightness.png", PhotoFilter.BRIGHTNESS));
        filterInfoList.add(new FilterInfo("filters/contrast.png", PhotoFilter.CONTRAST));
        filterInfoList.add(new FilterInfo("filters/documentary.png", PhotoFilter.DOCUMENTARY));
        filterInfoList.add(new FilterInfo("filters/dual_tone.png", PhotoFilter.DUE_TONE));
        filterInfoList.add(new FilterInfo("filters/fill_light.png", PhotoFilter.FILL_LIGHT));
        filterInfoList.add(new FilterInfo("filters/fish_eye.png", PhotoFilter.FISH_EYE));
        filterInfoList.add(new FilterInfo("filters/grain.png", PhotoFilter.GRAIN));
        filterInfoList.add(new FilterInfo("filters/gray_scale.png", PhotoFilter.GRAY_SCALE));
        filterInfoList.add(new FilterInfo("filters/lomish.png", PhotoFilter.LOMISH));
        filterInfoList.add(new FilterInfo("filters/negative.png", PhotoFilter.NEGATIVE));
        filterInfoList.add(new FilterInfo("filters/posterize.png", PhotoFilter.POSTERIZE));
        filterInfoList.add(new FilterInfo("filters/saturate.png", PhotoFilter.SATURATE));
        filterInfoList.add(new FilterInfo("filters/sepia.png", PhotoFilter.SEPIA));
        filterInfoList.add(new FilterInfo("filters/sharpen.png", PhotoFilter.SHARPEN));
        filterInfoList.add(new FilterInfo("filters/temprature.png", PhotoFilter.TEMPERATURE));
        filterInfoList.add(new FilterInfo("filters/tint.png", PhotoFilter.TINT));
        filterInfoList.add(new FilterInfo("filters/vignette.png", PhotoFilter.VIGNETTE));
        filterInfoList.add(new FilterInfo("filters/cross_process.png", PhotoFilter.CROSS_PROCESS));
        filterInfoList.add(new FilterInfo("filters/b_n_w.png", PhotoFilter.BLACK_WHITE));
        filterInfoList.add(new FilterInfo("filters/flip_horizental.png", PhotoFilter.FLIP_HORIZONTAL));
        filterInfoList.add(new FilterInfo("filters/flip_vertical.png", PhotoFilter.FLIP_VERTICAL));
        filterInfoList.add(new FilterInfo("filters/rotate.png", PhotoFilter.ROTATE));

    }


    @Override
    public void onSelect(FilterInfo item) {
        progressBar.setVisibility(View.VISIBLE);
        filterRecyclerView.setVisibility(View.INVISIBLE);
        done.setVisibility(View.INVISIBLE);
        close.setVisibility(View.INVISIBLE);
        photoEditor.setFilterEffect(item.getTitle(), new FilterCompleteListener() {
            @Override
            public void onFilterComplete() {
                Objects.requireNonNull(getActivity()).runOnUiThread(() -> {
                    progressBar.setVisibility(View.INVISIBLE);
                    filterRecyclerView.setVisibility(View.VISIBLE);
                    done.setVisibility(View.VISIBLE);
                    close.setVisibility(View.VISIBLE);
                });

            }
        });
    }
}