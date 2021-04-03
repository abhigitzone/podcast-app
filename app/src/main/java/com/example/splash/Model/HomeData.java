package com.example.splash.Model;

public class HomeData {
    int podImg;
    String podTitle;

    public HomeData(int podImg, String podTitle) {
        this.podImg = podImg;
        this.podTitle = podTitle;
    }

    public int getPodImg() {
        return podImg;
    }

    public void setPodImg(int podImg) {
        this.podImg = podImg;
    }

    public String getPodTitle() {
        return podTitle;
    }

    public void setPodTitle(String podTitle) {
        this.podTitle = podTitle;
    }
}
