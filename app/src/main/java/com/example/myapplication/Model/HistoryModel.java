package com.example.myapplication.Model;

public class HistoryModel {
    String history;
    String link;
    String title;
    String thumb;

    public HistoryModel(String history, String link, String title, String thumb) {
        this.history = history;
        this.link = link;
        this.title = title;
        this.thumb = thumb;
    }

    public String getHistory() {
        return history;
    }

    public void setHistory(String history) {
        this.history = history;
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