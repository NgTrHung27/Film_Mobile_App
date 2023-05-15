package com.example.myapplication.Model;

import com.google.firebase.firestore.DocumentSnapshot;

public class FavouriteModel {
    String id;
    String link;
    String title;
    String thumb;
    boolean isChecked;

    public FavouriteModel(DocumentSnapshot snapshot) {
        this.link = snapshot.getString("link");
        this.title = snapshot.getString("title");
        this.thumb = snapshot.getString("thumb");
    }



    public FavouriteModel(String link, String title, String thumb) {
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}