package eg.com.ivas.ivas_story_maker.POJO;

import java.util.List;

public class CategoryInfo {

    private String iconImage;
    private String title;
    private List<TemplateInfo> templateInfoList;


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
}
