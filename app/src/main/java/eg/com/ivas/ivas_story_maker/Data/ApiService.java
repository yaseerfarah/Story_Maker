package eg.com.ivas.ivas_story_maker.Data;

import eg.com.ivas.ivas_story_maker.POJO.ArrayBaseResponse;
import eg.com.ivas.ivas_story_maker.POJO.CategoryInfo;
import eg.com.ivas.ivas_story_maker.POJO.ObjectBaseResponse;
import eg.com.ivas.ivas_story_maker.POJO.TemplateInfo;
import eg.com.ivas.ivas_story_maker.POJO.TemplatePagination;
import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {


    @GET("category")
    Observable<Response<ArrayBaseResponse<CategoryInfo>>> getAllCategories();

    @GET("content")
    Observable<Response<ObjectBaseResponse<TemplatePagination>>> getAllTemplatesByCategoryID(@Query("category_id") int id,
                                                                                             @Query("page") int page);



}
