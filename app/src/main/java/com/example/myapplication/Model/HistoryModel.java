package com.example.myapplication.Model;

public class HistoryModel {
    String link;
    String title;
    String thumb;
//    String history;

    public HistoryModel(String link, String title, String thumb) {
        this.link = link;
        this.title = title;
        this.thumb = thumb;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }
}