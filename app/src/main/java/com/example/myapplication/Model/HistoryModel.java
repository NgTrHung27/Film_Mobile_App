package com.example.myapplication.Model;

public class HistoryModel {

    public HistoryModel(String history, String link, String title, String thumb) {
        History = history;
        Link = link;
        Title = title;
        Thumb = thumb;
    }

    public String getHistory() {
        return History;
    }

    public void setHistory(String history) {
        History = history;
    }

    public String getLink() {
        return Link;
    }

    public void setLink(String link) {
        Link = link;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getThumb() {
        return Thumb;
    }

    public void setThumb(String thumb) {
        Thumb = thumb;
    }

    String History;
    String Link;
    String Title;
    String Thumb;

}
