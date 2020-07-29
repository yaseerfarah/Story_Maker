package eg.com.ivas.ivas_story_maker.POJO;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TemplatePagination {

    @SerializedName("contents")
    private List<TemplateInfo> templateInfoList;
    @SerializedName("next_page_url")
    private String nextPageUrl;


    public List<TemplateInfo> getTemplateInfoList() {
        return templateInfoList;
    }

    public String getNextPageUrl() {
        return nextPageUrl;
    }
}
