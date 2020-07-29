package eg.com.ivas.ivas_story_maker.Interface;

import java.util.List;

import eg.com.ivas.ivas_story_maker.POJO.CategoryInfo;

public interface MvpPresenter {

    void getAllCategories();

    void getNextPage(CategoryInfo categoryInfo);

    void disposeNextPageCall();

    /////////////////////////////////////////////////

    void onAllDataReady(List<CategoryInfo> categoryInfoList);

    void onNextPageReady(CategoryInfo categoryInfo);


    void onError(Throwable throwable);

}
