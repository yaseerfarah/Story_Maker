package eg.com.ivas.ivas_story_maker.View;

import android.animation.Animator;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.gturedi.views.CustomStateOptions;
import com.gturedi.views.StatefulLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import eg.com.ivas.ivas_story_maker.Adapter.HomeRycyclerAdapter;
import eg.com.ivas.ivas_story_maker.BroadcastReceiver.NetworkReceiver;
import eg.com.ivas.ivas_story_maker.Interface.InternetStatus;
import eg.com.ivas.ivas_story_maker.Interface.MvpHomeView;
import eg.com.ivas.ivas_story_maker.Interface.MvpPresenter;
import eg.com.ivas.ivas_story_maker.POJO.CategoryInfo;
import eg.com.ivas.ivas_story_maker.POJO.TemplateInfo;
import eg.com.ivas.ivas_story_maker.Presenter.HomePresenter;
import eg.com.ivas.ivas_story_maker.R;
import eg.com.ivas.ivas_story_maker.Util.EndlessRecyclerViewListener;
import eg.com.ivas.ivas_story_maker.Util.GridSpacingItemDecoration;
import eg.com.ivas.ivas_story_maker.ViewModel.MvpModel;
import es.dmoral.toasty.Toasty;

import static eg.com.ivas.ivas_story_maker.Adapter.HomeRycyclerAdapter.CATEGORY_R;
import static eg.com.ivas.ivas_story_maker.Adapter.HomeRycyclerAdapter.TEMPLATE_R;

public class Home extends Fragment implements MvpHomeView, InternetStatus {


    @BindView(R.id.homeRecycler)
    RecyclerView homeRecycler;

    @BindView(R.id.stateful)
    StatefulLayout statefulLayout;

    private MvpModel mvpModel;
    private MvpPresenter mvpPresenter;

    private CustomStateOptions networkCustom=new CustomStateOptions().image(R.drawable.ic_baseline_signal_wifi_off_24);
    private NetworkReceiver networkReceiver;

    private HomeRycyclerAdapter homeRycyclerAdapter;
    private GridLayoutManager gridLayoutManager;

    private NavController navController;

    private List<CategoryInfo> categoryInfoList=new ArrayList<>();


    List<TemplateInfo>templateInfos=new ArrayList<>();

    public Home() {
        // Required empty public constructor



        templateInfos.add(new TemplateInfo(123,"https://placeit-assets2.s3-accelerate.amazonaws.com/custom-pages/instagram-story-video-maker-videos/instagram-story-video-maker-with-overlapping-text.jpg","https://placeit-assets2.s3-accelerate.amazonaws.com/custom-pages/instagram-story-video-maker-videos/instagram-story-video-maker-with-overlapping-text.jpg"));
        templateInfos.add(new TemplateInfo(321,"https://img0-placeit-net.s3-accelerate.amazonaws.com/uploads/stage/stage_image/65715/optimized_large_thumb_stage.jpg","https://img0-placeit-net.s3-accelerate.amazonaws.com/uploads/stage/stage_image/65715/optimized_large_thumb_stage.jpg"));
        templateInfos.add(new TemplateInfo(231,"https://i.pinimg.com/originals/67/3e/9e/673e9e04e01caf7870660f2b85bf0ab6.jpg","https://i.pinimg.com/originals/67/3e/9e/673e9e04e01caf7870660f2b85bf0ab6.jpg"));
        templateInfos.add(new TemplateInfo(147,"https://i.pinimg.com/474x/cb/fe/54/cbfe545465736e5662b8d2f2c1eecb51.jpg","https://i.pinimg.com/474x/cb/fe/54/cbfe545465736e5662b8d2f2c1eecb51.jpg"));
        templateInfos.add(new TemplateInfo(741,"https://i.pinimg.com/474x/cb/fe/54/cbfe545465736e5662b8d2f2c1eecb51.jpg","https://i.pinimg.com/474x/cb/fe/54/cbfe545465736e5662b8d2f2c1eecb51.jpg"));
        templateInfos.add(new TemplateInfo(417,"https://placeit-assets0.s3-accelerate.amazonaws.com/custom-pages/make-an-instagram-story-templates/insta-story-maker-for-clothing-brand-accounts-593-576x1024.png","https://placeit-assets2.s3-accelerate.amazonaws.com/custom-pages/instagram-story-video-maker-videos/instagram-story-video-maker-with-overlapping-text.jpg"));

        templateInfos.add(new TemplateInfo(369,"https://placeit-assets2.s3-accelerate.amazonaws.com/custom-pages/instagram-story-video-maker-videos/instagram-story-video-maker-with-overlapping-text.jpg","https://placeit-assets2.s3-accelerate.amazonaws.com/custom-pages/instagram-story-video-maker-videos/instagram-story-video-maker-with-overlapping-text.jpg"));
        templateInfos.add(new TemplateInfo(963,"https://img0-placeit-net.s3-accelerate.amazonaws.com/uploads/stage/stage_image/65715/optimized_large_thumb_stage.jpg","https://img0-placeit-net.s3-accelerate.amazonaws.com/uploads/stage/stage_image/65715/optimized_large_thumb_stage.jpg"));
        templateInfos.add(new TemplateInfo(639,"https://i.pinimg.com/originals/67/3e/9e/673e9e04e01caf7870660f2b85bf0ab6.jpg","https://i.pinimg.com/originals/67/3e/9e/673e9e04e01caf7870660f2b85bf0ab6.jpg"));
        templateInfos.add(new TemplateInfo(693,"https://i.pinimg.com/474x/cb/fe/54/cbfe545465736e5662b8d2f2c1eecb51.jpg","https://i.pinimg.com/474x/cb/fe/54/cbfe545465736e5662b8d2f2c1eecb51.jpg"));
        templateInfos.add(new TemplateInfo(789,"https://i.pinimg.com/474x/cb/fe/54/cbfe545465736e5662b8d2f2c1eecb51.jpg","https://i.pinimg.com/474x/cb/fe/54/cbfe545465736e5662b8d2f2c1eecb51.jpg"));
        templateInfos.add(new TemplateInfo(987,"https://placeit-assets0.s3-accelerate.amazonaws.com/custom-pages/make-an-instagram-story-templates/insta-story-maker-for-clothing-brand-accounts-593-576x1024.png","https://placeit-assets2.s3-accelerate.amazonaws.com/custom-pages/instagram-story-video-maker-videos/instagram-story-video-maker-with-overlapping-text.jpg"));


        templateInfos.add(new TemplateInfo(897,"https://placeit-assets2.s3-accelerate.amazonaws.com/custom-pages/instagram-story-video-maker-videos/instagram-story-video-maker-with-overlapping-text.jpg","https://placeit-assets2.s3-accelerate.amazonaws.com/custom-pages/instagram-story-video-maker-videos/instagram-story-video-maker-with-overlapping-text.jpg"));
        templateInfos.add(new TemplateInfo(879,"https://img0-placeit-net.s3-accelerate.amazonaws.com/uploads/stage/stage_image/65715/optimized_large_thumb_stage.jpg","https://img0-placeit-net.s3-accelerate.amazonaws.com/uploads/stage/stage_image/65715/optimized_large_thumb_stage.jpg"));
        templateInfos.add(new TemplateInfo(258,"https://i.pinimg.com/originals/67/3e/9e/673e9e04e01caf7870660f2b85bf0ab6.jpg","https://i.pinimg.com/originals/67/3e/9e/673e9e04e01caf7870660f2b85bf0ab6.jpg"));
        templateInfos.add(new TemplateInfo(852,"https://i.pinimg.com/474x/cb/fe/54/cbfe545465736e5662b8d2f2c1eecb51.jpg","https://i.pinimg.com/474x/cb/fe/54/cbfe545465736e5662b8d2f2c1eecb51.jpg"));
        templateInfos.add(new TemplateInfo(582,"https://i.pinimg.com/474x/cb/fe/54/cbfe545465736e5662b8d2f2c1eecb51.jpg","https://i.pinimg.com/474x/cb/fe/54/cbfe545465736e5662b8d2f2c1eecb51.jpg"));
        templateInfos.add(new TemplateInfo(528,"https://placeit-assets0.s3-accelerate.amazonaws.com/custom-pages/make-an-instagram-story-templates/insta-story-maker-for-clothing-brand-accounts-593-576x1024.png","https://placeit-assets2.s3-accelerate.amazonaws.com/custom-pages/instagram-story-video-maker-videos/instagram-story-video-maker-with-overlapping-text.jpg"));

/*
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


*/

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mvpModel= ViewModelProviders.of(this).get(MvpModel.class);
        mvpPresenter=new HomePresenter(mvpModel,this);
        mvpModel.initialize(mvpPresenter);
        networkReceiver=new NetworkReceiver(this);

    }

    @Override
    public void onStart() {
        super.onStart();
        IntentFilter netFilter=new IntentFilter();
        netFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        getActivity().registerReceiver(networkReceiver,netFilter);
    }

    @Override
    public void onStop() {
        super.onStop();
        getActivity().unregisterReceiver(networkReceiver);
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
        Objects.requireNonNull(getActivity()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int displayWidth = displayMetrics.widthPixels;

        homeRycyclerAdapter=new HomeRycyclerAdapter(getContext(),categoryInfoList,mvpPresenter,navController);
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
        homeRecycler.getRecycledViewPool().setMaxRecycledViews(CATEGORY_R,0);
        homeRecycler.addItemDecoration(new GridSpacingItemDecoration(2,GridSpacingItemDecoration.dpToPx(5,getResources()),GridSpacingItemDecoration.Category,displayWidth,getResources().getDimensionPixelSize(R.dimen._140sdp)));
        homeRecycler.addOnScrollListener(new EndlessRecyclerViewListener(gridLayoutManager) {
            @Override
            public void onLoadMore(RecyclerView view) {

                CategoryInfo currentCategory= homeRycyclerAdapter.getCurrentCategory();

                if (!homeRycyclerAdapter.isLoading()&&currentCategory.isHasNext()) {
                     homeRycyclerAdapter.addLoading();
                     mvpPresenter.getNextPage(homeRycyclerAdapter.getCurrentCategory());
                }
            }
        });





    }

    @Override
    public void updateHomeList(List<CategoryInfo> listCategoryInfo) {
       // Toast.makeText(getContext(), String.valueOf(categoryInfos.get(0).getTemplateInfoList().get(0).getPreviewImage()), Toast.LENGTH_SHORT).show();
       /* List<TemplateInfo> templateInfoss=new ArrayList<>();
        templateInfoss.addAll(listCategoryInfo.get(0).getTemplateInfoList());
        templateInfoss.addAll(listCategoryInfo.get(0).getTemplateInfoList());
        templateInfoss.addAll(listCategoryInfo.get(0).getTemplateInfoList());
        templateInfoss.addAll(listCategoryInfo.get(0).getTemplateInfoList());
        listCategoryInfo.get(1).setTemplateInfoList(templateInfoss);
        listCategoryInfo.get(0).setHasNext(true);
        listCategoryInfo.get(0).setTemplateInfoList(templateInfos);*/

        statefulLayout.showContent();
        categoryInfoList.addAll(listCategoryInfo);
        homeRycyclerAdapter.notifyDataSetChanged();
        homeRycyclerAdapter.onSelect(listCategoryInfo.get(0));

    }

    @Override
    public void updateNextPage(CategoryInfo categoryInfo) {
        for(CategoryInfo cat:categoryInfoList){
            if (cat.getId()==categoryInfo.getId()){
                cat.setTemplateInfoList(categoryInfo.getTemplateInfoList());
            }
        }

        homeRycyclerAdapter.updateNextPage(categoryInfo);



    }

    @Override
    public void onErrorOccurred(Throwable throwable) {
        Log.e(getClass().getName(), Objects.requireNonNull(throwable.getMessage()));
        Toasty.error(Objects.requireNonNull(getContext()),throwable.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void Connect() {
      //  Toast.makeText(getContext(), "hi", Toast.LENGTH_SHORT).show();
        if (categoryInfoList.isEmpty()){
            statefulLayout.showLoading(" ");
            mvpPresenter.getAllCategories();
        }else {
            statefulLayout.showContent();
        }
    }

    @Override
    public void notConnect() {
        statefulLayout.showCustom(networkCustom.message(getResources().getString(R.string.check_connection)));

    }


}