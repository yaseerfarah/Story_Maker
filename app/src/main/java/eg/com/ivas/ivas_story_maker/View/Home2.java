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
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import butterknife.BindView;
import butterknife.ButterKnife;
import eg.com.ivas.ivas_story_maker.Adapter.HomeRycyclerAdapter;
import eg.com.ivas.ivas_story_maker.POJO.CategoryInfo;
import eg.com.ivas.ivas_story_maker.POJO.TemplateInfo;
import eg.com.ivas.ivas_story_maker.R;
import eg.com.ivas.ivas_story_maker.Util.EndlessRecyclerViewListener;
import eg.com.ivas.ivas_story_maker.Util.GridSpacingItemDecoration;

import static eg.com.ivas.ivas_story_maker.Adapter.HomeRycyclerAdapter.CATEGORY_R;
import static eg.com.ivas.ivas_story_maker.Adapter.HomeRycyclerAdapter.PROGRESS;
import static eg.com.ivas.ivas_story_maker.Adapter.HomeRycyclerAdapter.TEMPLATE_R;

public class Home2 extends Fragment {


    @BindView(R.id.homeRecycler)
    RecyclerView homeRecycler;

    HomeRycyclerAdapter homeRycyclerAdapter;
    GridLayoutManager gridLayoutManager;

    NavController navController;

    List<CategoryInfo> categoryInfoList=new ArrayList<>();

    private boolean isLoading=false;

    public Home2() {
        // Required empty public constructor

        List<TemplateInfo>templateInfos=new ArrayList<>();

        templateInfos.add(new TemplateInfo("https://placeit-assets2.s3-accelerate.amazonaws.com/custom-pages/instagram-story-video-maker-videos/instagram-story-video-maker-with-overlapping-text.jpg","https://placeit-assets2.s3-accelerate.amazonaws.com/custom-pages/instagram-story-video-maker-videos/instagram-story-video-maker-with-overlapping-text.jpg"));
        templateInfos.add(new TemplateInfo("https://img0-placeit-net.s3-accelerate.amazonaws.com/uploads/stage/stage_image/65715/optimized_large_thumb_stage.jpg","https://img0-placeit-net.s3-accelerate.amazonaws.com/uploads/stage/stage_image/65715/optimized_large_thumb_stage.jpg"));
        templateInfos.add(new TemplateInfo("https://i.pinimg.com/originals/67/3e/9e/673e9e04e01caf7870660f2b85bf0ab6.jpg","https://i.pinimg.com/originals/67/3e/9e/673e9e04e01caf7870660f2b85bf0ab6.jpg"));
        templateInfos.add(new TemplateInfo("https://i.pinimg.com/474x/cb/fe/54/cbfe545465736e5662b8d2f2c1eecb51.jpg","https://i.pinimg.com/474x/cb/fe/54/cbfe545465736e5662b8d2f2c1eecb51.jpg"));
        templateInfos.add(new TemplateInfo("https://i.pinimg.com/474x/cb/fe/54/cbfe545465736e5662b8d2f2c1eecb51.jpg","https://i.pinimg.com/474x/cb/fe/54/cbfe545465736e5662b8d2f2c1eecb51.jpg"));
        templateInfos.add(new TemplateInfo("https://placeit-assets0.s3-accelerate.amazonaws.com/custom-pages/make-an-instagram-story-templates/insta-story-maker-for-clothing-brand-accounts-593-576x1024.png","https://placeit-assets2.s3-accelerate.amazonaws.com/custom-pages/instagram-story-video-maker-videos/instagram-story-video-maker-with-overlapping-text.jpg"));


        categoryInfoList.add(new CategoryInfo("https://upload.wikimedia.org/wikipedia/commons/5/51/IMessage_logo.svg","رسائل",templateInfos));
        categoryInfoList.add(new CategoryInfo("https://upload.wikimedia.org/wikipedia/commons/5/51/IMessage_logo.svg","قصص",templateInfos));
        categoryInfoList.add(new CategoryInfo("https://upload.wikimedia.org/wikipedia/commons/5/51/IMessage_logo.svg","صمم صورك",templateInfos));
        categoryInfoList.add(new CategoryInfo("https://upload.wikimedia.org/wikipedia/commons/5/51/IMessage_logo.svg","عام",templateInfos));
        categoryInfoList.add(new CategoryInfo("https://upload.wikimedia.org/wikipedia/commons/5/51/IMessage_logo.svg","افلام",templateInfos));

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home2, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this,view);

        navController= Navigation.findNavController(view);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int displayWidth = displayMetrics.widthPixels;

        homeRycyclerAdapter=new HomeRycyclerAdapter(getContext(),categoryInfoList,navController);
        gridLayoutManager=new GridLayoutManager(getContext(),2);

        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                switch (homeRycyclerAdapter.getItemViewType(position)){
                    case TEMPLATE_R:
                        return 1;

                    default:
                        return 2;
                }
            }
        });


        homeRecycler.setLayoutManager(gridLayoutManager);
        homeRecycler.setAdapter(homeRycyclerAdapter);
        homeRecycler.addItemDecoration(new GridSpacingItemDecoration(2,GridSpacingItemDecoration.dpToPx(5,getResources()),GridSpacingItemDecoration.Category,displayWidth,getResources().getDimensionPixelSize(R.dimen._140sdp)));
        homeRecycler.addOnScrollListener(new EndlessRecyclerViewListener(gridLayoutManager) {
            @Override
            public void onLoadMore(RecyclerView view) {
                if (!isLoading) {
                    // homeRycyclerAdapter.addLoading();
                }
            }
        });





    }
}