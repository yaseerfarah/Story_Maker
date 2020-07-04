package eg.com.ivas.ivas_story_maker.POJO;

public class TemplateInfo {
    private String previewImage;
    private String templateImage;

    public TemplateInfo() {
    }

    public TemplateInfo(String previewImage, String templateImage) {
        this.previewImage = previewImage;
        this.templateImage = templateImage;
    }

    public String getPreviewImage() {
        return previewImage;
    }

    public String getTemplateImage() {
        return templateImage;
    }
}
