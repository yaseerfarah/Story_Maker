package eg.com.ivas.ivas_story_maker.POJO;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class CategoryInfo {

    @SerializedName("id")
    private int id;
    @SerializedName("image")
    private String iconImage;
    @SerializedName("title")
    private String title;

    private List<TemplateInfo> templateInfoList;
    private int currentPage;
    private boolean hasNext;


    public CategoryInfo(String iconImage, String title, List<TemplateInfo> templateInfoList) {
        this.iconImage = iconImage;
        this.title = title;
        this.templateInfoList = templateInfoList;
    }

    public String getIconImage() {
        return iconImage;
    }

    public String getTitle() {
        return title;
    }

    public List<TemplateInfo> getTemplateInfoList() {
        return templateInfoList;
    }

    public int getId() {
        return id;
    }
    public int getCurrentPage() {
        return currentPage;
    }

    public boolean isHasNext() {
        return hasNext;
    }

    public void setIconImage(String iconImage) {
        this.iconImage = iconImage;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setTemplateInfoList(List<TemplateInfo> templateInfoList) {
        this.templateInfoList = templateInfoList;
    }

    public void addTemplateInfoList(List<TemplateInfo> templateInfoList) {
       if(this.templateInfoList==null){
          this.templateInfoList=new ArrayList<>();
       }
        this.templateInfoList.addAll(templateInfoList);

    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public void setHasNext(boolean hasNext) {
        this.hasNext = hasNext;
    }

    public void setId(int id) {
        this.id = id;
    }
}
