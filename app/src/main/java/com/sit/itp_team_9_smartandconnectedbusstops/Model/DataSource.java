package com.sit.itp_team_9_smartandconnectedbusstops.Model;

public class DataSource {

    private int imageUrl;
    private String title;
    private String content;

    public DataSource(int imageUrl, String title, String content) {
        this.imageUrl = imageUrl;
        this.title = title;
        this.content = content;
    }

    public int getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(int imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
