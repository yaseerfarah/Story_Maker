package eg.com.ivas.ivas_story_maker.POJO;

import eg.com.ivas.ivas_story_maker.Interface.FilterBase;

public class SpiralFilter implements FilterBase {

    private boolean is_2_Part;
    private String frontPart;
    private String backPart;
    private String preview;
    private String title;


    public SpiralFilter(String title,String preview, String backPart) {
        this.title=title;
        this.backPart = backPart;
        this.preview = preview;
        this.is_2_Part=false;
    }

    public SpiralFilter(String title,String preview,String frontPart, String backPart) {
        this.title=title;
        this.frontPart = frontPart;
        this.backPart = backPart;
        this.preview = preview;
        this.is_2_Part=true;
    }

    public boolean isIs_2_Part() {
        return is_2_Part;
    }

    public String getFrontPart() {
        return frontPart;
    }

    public String getBackPart() {
        return backPart;
    }

    public String getPreview() {
        return preview;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public String getFilterTitle() {
        return getTitle();
    }

    @Override
    public String getFilterPreviewImage() {
        return getPreview();
    }
}
