package eg.com.ivas.ivas_story_maker.ViewModel;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.ViewModel;
import eg.com.ivas.ivas_story_maker.Data.ApiService;
import eg.com.ivas.ivas_story_maker.Data.RetrofitClient;
import eg.com.ivas.ivas_story_maker.Interface.MvpPresenter;
import eg.com.ivas.ivas_story_maker.POJO.ArrayBaseResponse;
import eg.com.ivas.ivas_story_maker.POJO.CategoryInfo;
import eg.com.ivas.ivas_story_maker.POJO.ObjectBaseResponse;
import eg.com.ivas.ivas_story_maker.POJO.TemplatePagination;
import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class MvpModel extends ViewModel {

    private MvpPresenter presenter;
    private ApiService apiService;
    private  CompositeDisposable disposables;

    private List<CategoryInfo> categoryInfoList;

    public void initialize(MvpPresenter presenter){
        this.presenter = presenter;
        this.apiService = RetrofitClient.getClient().create(ApiService.class);
        this.disposables=new CompositeDisposable();
        this.categoryInfoList=new ArrayList<>();

    }


    public void getAllData(){
        disposables.add(apiService.getAllCategories()
                .subscribeOn(Schedulers.io())
                .map(retrofit2.Response::body)
                .map(ArrayBaseResponse<CategoryInfo>::getResponseList)
                .flatMap(Observable::fromIterable)
                .flatMap(categoryInfo -> getAllTemplates(categoryInfo,1))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onNext,this::onError,this::onComplete)




        );
    }


    private Observable<CategoryInfo> getAllTemplates(CategoryInfo categoryInfo,int page){

        return apiService.getAllTemplatesByCategoryID(categoryInfo.getId(),page)
                .subscribeOn(Schedulers.io())
                .map(retrofit2.Response::body)
                .map(ObjectBaseResponse<TemplatePagination>::getResponse)
                .map(templatePagination -> {
                    categoryInfo.setCurrentPage(page);
                    categoryInfo.addTemplateInfoList(templatePagination.getTemplateInfoList());
                    if (templatePagination.getNextPageUrl()==null){
                        categoryInfo.setHasNext(false);
                    }else {
                        categoryInfo.setHasNext(true);
                    }

                    return categoryInfo;

                });

    }


    public void getNextPage(CategoryInfo categoryInfo){
        int page=categoryInfo.getCurrentPage();
        page++;
        disposables.add(getAllTemplates(categoryInfo,page)
        .observeOn(AndroidSchedulers.mainThread())
                .singleOrError()
                .subscribe(categoryInfo1 -> {
                    presenter.onNextPageReady(categoryInfo1);
                },this::onError)


        );

    }




    private void onNext(CategoryInfo categoryInfo){
        categoryInfoList.add(categoryInfo);

    }
    private void onComplete(){
        presenter.onAllDataReady(categoryInfoList);

    }
    private void onError(Throwable throwable){
        categoryInfoList.clear();
        //Log.e(getClass().getName(),throwable.getMessage());
        presenter.onError(throwable);

    }


    public void clear(){
        disposables.clear();
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        clear();
    }
}
