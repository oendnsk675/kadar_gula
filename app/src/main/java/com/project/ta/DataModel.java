package com.project.ta;

public class DataModel {
    private String konten;
    private String id;
    private String image;
    private String date;
    private String title;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public String getKonten() {
        return konten;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setKonten(String konten) {
        this.konten = konten;
    }
}
