package eg.com.ivas.ivas_story_maker.View;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import eg.com.ivas.ivas_story_maker.Adapter.CardCategoryAdapter;
import eg.com.ivas.ivas_story_maker.Adapter.CardTemplateAdapter;
import eg.com.ivas.ivas_story_maker.Adapter.HomeRycyclerAdapter;
import eg.com.ivas.ivas_story_maker.POJO.CategoryInfo;
import eg.com.ivas.ivas_story_maker.POJO.TemplateInfo;
import eg.com.ivas.ivas_story_maker.R;
import eg.com.ivas.ivas_story_maker.Util.EndlessRecyclerViewListener;
import eg.com.ivas.ivas_story_maker.Util.GridSpacingItemDecoration;

public class Home extends Fragment {


    @BindView(R.id.categoryRecycle)
    RecyclerView categoryRecycler;

    @BindView(R.id.templateRecycle)
    RecyclerView templateRecycler;

    CardCategoryAdapter cardCategoryAdapter;
    CardTemplateAdapter cardTemplateAdapter;

    NavController navController;

    List<CategoryInfo> categoryInfoList=new ArrayList<>();
    private boolean isLoading=false;

    public Home() {
        // Required empty public constructor

        List<TemplateInfo>templateInfos=new ArrayList<>();

        templateInfos.add(new TemplateInfo("https://placeit-assets2.s3-accelerate.amazonaws.com/custom-pages/instagram-story-video-maker-videos/instagram-story-video-maker-with-overlapping-text.jpg","https://placeit-assets2.s3-accelerate.amazonaws.com/custom-pages/instagram-story-video-maker-videos/instagram-story-video-maker-with-overlapping-text.jpg"));
        templateInfos.add(new TemplateInfo("https://img0-placeit-net.s3-accelerate.amazonaws.com/uploads/stage/stage_image/65715/optimized_large_thumb_stage.jpg","https://img0-placeit-net.s3-accelerate.amazonaws.com/uploads/stage/stage_image/65715/optimized_large_thumb_stage.jpg"));
        templateInfos.add(new TemplateInfo("https://i.pinimg.com/originals/67/3e/9e/673e9e04e01caf7870660f2b85bf0ab6.jpg","https://i.pinimg.com/originals/67/3e/9e/673e9e04e01caf7870660f2b85bf0ab6.jpg"));
        templateInfos.add(new TemplateInfo("https://i.pinimg.com/474x/cb/fe/54/cbfe545465736e5662b8d2f2c1eecb51.jpg","https://i.pinimg.com/474x/cb/fe/54/cbfe545465736e5662b8d2f2c1eecb51.jpg"));
        templateInfos.add(new TemplateInfo("https://i.pinimg.com/474x/cb/fe/54/cbfe545465736e5662b8d2f2c1eecb51.jpg","https://i.pinimg.com/474x/cb/fe/54/cbfe545465736e5662b8d2f2c1eecb51.jpg"));
        templateInfos.add(new TemplateInfo("https://placeit-assets2.s3-accelerate.amazonaws.com/custom-pages/instagram-story-video-maker-videos/instagram-story-video-maker-with-overlapping-text.jpg","https://placeit-assets2.s3-accelerate.amazonaws.com/custom-pages/instagram-story-video-maker-videos/instagram-story-video-maker-with-overlapping-text.jpg"));
        templateInfos.add(new TemplateInfo("https://i.pinimg.com/originals/67/3e/9e/673e9e04e01caf7870660f2b85bf0ab6.jpg","https://i.pinimg.com/originals/67/3e/9e/673e9e04e01caf7870660f2b85bf0ab6.jpg"));
        templateInfos.add(new TemplateInfo("https://i.pinimg.com/originals/67/3e/9e/673e9e04e01caf7870660f2b85bf0ab6.jpg","https://i.pinimg.com/originals/67/3e/9e/673e9e04e01caf7870660f2b85bf0ab6.jpg"));


        categoryInfoList.add(new CategoryInfo("https://upload.wikimedia.org/wikipedia/commons/5/51/IMessage_logo.svg","Message",templateInfos));
        categoryInfoList.add(new CategoryInfo("https://upload.wikimedia.org/wikipedia/commons/5/51/IMessage_logo.svg","Story",templateInfos));
        categoryInfoList.add(new CategoryInfo("https://upload.wikimedia.org/wikipedia/commons/5/51/IMessage_logo.svg","Insta",templateInfos));
        categoryInfoList.add(new CategoryInfo("https://upload.wikimedia.org/wikipedia/commons/5/51/IMessage_logo.svg","General",templateInfos));
        categoryInfoList.add(new CategoryInfo("https://upload.wikimedia.org/wikipedia/commons/5/51/IMessage_logo.svg","Love",templateInfos));

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this,view);

        navController= Navigation.findNavController(view);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int displayWidth = displayMetrics.widthPixels;

        cardTemplateAdapter=new CardTemplateAdapter(getContext(),navController);
        cardCategoryAdapter=new CardCategoryAdapter(getContext(),categoryInfoList,cardTemplateAdapter);

        categoryRecycler.setAdapter(cardCategoryAdapter);
        categoryRecycler.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.HORIZONTAL,false));
        categoryRecycler.setHasFixedSize(true);

        templateRecycler.setAdapter(cardTemplateAdapter);
        GridLayoutManager gridLayoutManager=new GridLayoutManager(getContext(),2);
        templateRecycler.setLayoutManager(gridLayoutManager);
        templateRecycler.addItemDecoration(new GridSpacingItemDecoration(2,GridSpacingItemDecoration.dpToPx(5,getResources()),GridSpacingItemDecoration.Category,displayWidth,getResources().getDimensionPixelSize(R.dimen._140sdp)));
        templateRecycler.addOnScrollListener(new EndlessRecyclerViewListener(gridLayoutManager) {
            @Override
            public void onLoadMore(RecyclerView view) {
                if (!isLoading) {

                }
            }
        });









    }
}