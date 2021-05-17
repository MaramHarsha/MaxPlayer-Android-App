package com.harsha.videoplayer.maxplayer.video.player.appmanage.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class AllData {
    @SerializedName("customAds")
    @Expose
    private ArrayList<CustomAd> customAds = null;
    @SerializedName("main")
    @Expose
    private ArrayList<AppData> main = null;

    public AllData(ArrayList<AppData> arrayList) {
        this.main = arrayList;
    }

    public ArrayList<AppData> getMain() {
        return this.main;
    }

    public void setMain(ArrayList<AppData> arrayList) {
        this.main = arrayList;
    }

    public ArrayList<CustomAd> getCustomAds() {
        return this.customAds;
    }

    public void setCustomAds(ArrayList<CustomAd> arrayList) {
        this.customAds = arrayList;
    }
}
