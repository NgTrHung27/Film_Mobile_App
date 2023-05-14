package com.example.myapplication.Model;

public class FavouriteModel {
    String link;
    String thumb;
    String title;

    public FavouriteModel(String link, String thumb, String title) {
        this.link = link;
        this.thumb = thumb;
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}