package com.nvdeveloper.Chette.actions.ImageActions;

public class Image_P {
    String caption;
    String imageUrl;

    public Image_P(String caption, String imageUrl) {

        if(caption.trim().equals("")) caption = "No Caption";
        this.caption = caption;
        this.imageUrl = imageUrl;
    }

    public Image_P() {
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
