package eg.com.ivas.ivas_story_maker.POJO;

import eg.com.ivas.ivas_story_maker.Interface.FilterBase;
import ja.burhanrashid52.photoeditor.PhotoFilter;

public class FilterInfo implements FilterBase {

    private String previewImage;
    private PhotoFilter Title;

    public FilterInfo(String previewImage, PhotoFilter title) {
        this.previewImage = previewImage;
        Title = title;
    }

    public String getPreviewImage() {
        return previewImage;
    }

    public PhotoFilter getTitle() {
        return Title;
    }

    @Override
    public String getFilterTitle() {
        return getTitle().name();
    }

    @Override
    public String getFilterPreviewImage() {
        return getPreviewImage();
    }
}
