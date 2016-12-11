package com.example.webviewtest;

import android.graphics.Bitmap;

/**
 * Created by QDQ on 2016/8/25.
 */
public class StoriesData {
    private String date="";
    private String title="";
    private String images="";
    private int id=0;

    public int getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public String getTitle() {
        return title;
    }

    public String getImages() {
        return images;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
