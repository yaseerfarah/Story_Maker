package eg.com.ivas.ivas_story_maker.Interface;

import java.util.List;

import eg.com.ivas.ivas_story_maker.POJO.CategoryInfo;

public interface MvpHomeView {

    void updateHomeList(List<CategoryInfo> categoryInfoList);

    void updateNextPage(CategoryInfo categoryInfo);

    void onErrorOccurred(Throwable throwable);

}
