package com.example.myapplication.Model;

public class ScaryModel {
    String Scast;
    String Scountry;
    String Sdesc;
    String Seps;
    String Slength;
    String Srating;
    String Sthumb;
    String Stitle;

    public ScaryModel() {
    }

    public ScaryModel(String scast, String scountry, String sdesc, String seps,
                      String slength, String srating, String sthumb, String stitle) {
        Scast = scast;
        Scountry = scountry;
        Sdesc = sdesc;
        Seps = seps;
        Slength = slength;
        Srating = srating;
        Sthumb = sthumb;
        Stitle = stitle;
    }

    public String getScast() {
        return Scast;
    }

    public void setScast(String scast) {
        Scast = scast;
    }

    public String getScountry() {
        return Scountry;
    }

    public void setScountry(String scountry) {
        Scountry = scountry;
    }

    public String getSdesc() {
        return Sdesc;
    }

    public void setSdesc(String sdesc) {
        Sdesc = sdesc;
    }

    public String getSeps() {
        return Seps;
    }

    public void setSeps(String seps) {
        Seps = seps;
    }

    public String getSlength() {
        return Slength;
    }

    public void setSlength(String slength) {
        Slength = slength;
    }

    public String getSrating() {
        return Srating;
    }

    public void setSrating(String srating) {
        Srating = srating;
    }

    public String getSthumb() {
        return Sthumb;
    }

    public void setSthumb(String sthumb) {
        Sthumb = sthumb;
    }

    public String getStitle() {
        return Stitle;
    }

    public void setStitle(String stitle) {
        Stitle = stitle;
    }
}
