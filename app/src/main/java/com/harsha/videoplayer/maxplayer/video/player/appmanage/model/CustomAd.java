package com.harsha.videoplayer.maxplayer.video.player.appmanage.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CustomAd {
    @SerializedName("adsBigImage")
    @Expose
    private String adsBigImage;
    @SerializedName("adslong")
    @Expose
    private String adslong;
    @SerializedName("adsshort")
    @Expose
    private String adsshort;
    @SerializedName("appname")
    @Expose
    private String appname;
    @SerializedName("logo")
    @Expose
    private String logo;
    @SerializedName("packagename")
    @Expose
    private String packagename;

    public String getPackagename() {
        return this.packagename;
    }

    public void setPackagename(String str) {
        this.packagename = str;
    }

    public String getAppname() {
        return this.appname;
    }

    public void setAppname(String str) {
        this.appname = str;
    }

    public String getLogo() {
        return this.logo;
    }

    public void setLogo(String str) {
        this.logo = str;
    }

    public String getAdsshort() {
        return this.adsshort;
    }

    public void setAdsshort(String str) {
        this.adsshort = str;
    }

    public String getAdslong() {
        return this.adslong;
    }

    public void setAdslong(String str) {
        this.adslong = str;
    }

    public String getAdsBigImage() {
        return this.adsBigImage;
    }

    public void setAdsBigImage(String str) {
        this.adsBigImage = str;
    }
}
