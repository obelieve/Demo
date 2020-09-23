package com.github.obelieve.community.bean;

public class CommentData {

    private String media_scale;
    private String media_type;
    private String media;

    public CommentData(String media_scale, String media_type, String media) {
        this.media_scale = media_scale;
        this.media_type = media_type;
        this.media = media;
    }

    public String getMedia_scale() {
        return media_scale;
    }

    public void setMedia_scale(String media_scale) {
        this.media_scale = media_scale;
    }

    public String getMedia_type() {
        return media_type;
    }

    public void setMedia_type(String media_type) {
        this.media_type = media_type;
    }

    public String getMedia() {
        return media;
    }

    public void setMedia(String media) {
        this.media = media;
    }
}
