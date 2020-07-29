package eg.com.ivas.ivas_story_maker.POJO;

import com.google.gson.annotations.SerializedName;

public class TemplateInfo {
    @SerializedName("id")
    private int id;
    @SerializedName("image_preview")
    private String previewImage;
    @SerializedName("path")
    private String templateImage;

    public TemplateInfo() {
    }

    public TemplateInfo(int id, String previewImage, String templateImage) {
        this.id = id;
        this.previewImage = previewImage;
        this.templateImage = templateImage;
    }

    public String getPreviewImage() {
        return previewImage;
    }

    public String getTemplateImage() {
        return templateImage;
    }

    public int getId() {
        return id;
    }
}
