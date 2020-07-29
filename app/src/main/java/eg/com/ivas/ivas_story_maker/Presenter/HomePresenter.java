package eg.com.ivas.ivas_story_maker.Presenter;

import java.lang.ref.WeakReference;
import java.util.List;

import eg.com.ivas.ivas_story_maker.Interface.MvpHomeView;
import eg.com.ivas.ivas_story_maker.Interface.MvpPresenter;
import eg.com.ivas.ivas_story_maker.POJO.CategoryInfo;
import eg.com.ivas.ivas_story_maker.ViewModel.MvpModel;

public class HomePresenter implements MvpPresenter {

    private MvpModel mvpModel;
    private WeakReference<MvpHomeView> homeWeakReference;

    public HomePresenter(MvpModel mvpModel, MvpHomeView mvpHomeView) {
        this.mvpModel = mvpModel;
        this.homeWeakReference = new WeakReference<>(mvpHomeView);
    }


    @Override
    public void getAllCategories() {
        mvpModel.getAllData();
    }

    @Override
    public void getNextPage(CategoryInfo categoryInfo) {
        mvpModel.getNextPage(categoryInfo);
    }

    @Override
    public void disposeNextPageCall() {
        mvpModel.clear();
    }


    ////////////////////////////////


    @Override
    public void onAllDataReady(List<CategoryInfo> categoryInfoList) {
        if (homeWeakReference.get()!=null){
            homeWeakReference.get().updateHomeList(categoryInfoList);
        }
    }

    @Override
    public void onNextPageReady(CategoryInfo categoryInfo) {
        if (homeWeakReference.get()!=null){
            homeWeakReference.get().updateNextPage(categoryInfo);
        }
    }

    @Override
    public void onError(Throwable throwable) {
        if (homeWeakReference.get()!=null){
            homeWeakReference.get().onErrorOccurred(throwable);
        }
    }
}
