package com.harsha.videoplayer.maxplayer.video.player.appmanage.model;

import androidx.core.app.NotificationCompat;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AppData {
    @SerializedName("acname")
    @Expose
    private String acname;
    @SerializedName("admangeid")
    @Expose
    private String admangeid;
    @SerializedName("admobbanner")
    @Expose
    private String admobbanner;
    @SerializedName("admobinterstitial")
    @Expose
    private String admobinterstitial;
    @SerializedName("admobnative")
    @Expose
    private String admobnative;
    @SerializedName("admobvideo")
    @Expose
    private String admobvideo;
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
    @SerializedName("appremoved")
    @Expose
    private String appremoved;
    @SerializedName("appversion")
    @Expose
    private String appversion;
    @SerializedName("createdat")
    @Expose
    private String createdat;
    @SerializedName("fbbanner")
    @Expose
    private String fbbanner;
    @SerializedName("fbinterstitial")
    @Expose
    private String fbinterstitial;
    @SerializedName("fbnative")
    @Expose
    private String fbnative;
    @SerializedName("fbvideo")
    @Expose
    private String fbvideo;
    @SerializedName("isactive")
    @Expose
    private String isactive;
    @SerializedName("iscompulsoryupdate")
    @Expose
    private String iscompulsoryupdate;
    @SerializedName("isincustomeads")
    @Expose
    private String isincustomeads;
    @SerializedName("ismsg")
    @Expose
    private String ismsg;
    @SerializedName("logo")
    @Expose
    private String logo;
    @SerializedName(NotificationCompat.CATEGORY_MESSAGE)
    @Expose
    private String msg;
    @SerializedName("newappurl")
    @Expose
    private String newappurl;
    @SerializedName("packagename")
    @Expose
    private String packagename;
    @SerializedName("sabanner")
    @Expose
    private String sabanner;
    @SerializedName("sainterstitial")
    @Expose
    private String sainterstitial;
    @SerializedName("sanative")
    @Expose
    private String sanative;
    @SerializedName("savideo")
    @Expose
    private String savideo;
    @SerializedName("updatedat")
    @Expose
    private String updatedat;

    public AppData(String str, String str2, String str3, String str4, String str5, String str6, String str7) {
        this.isactive = str;
        this.admobbanner = str2;
        this.admobinterstitial = str3;
        this.admobnative = str4;
        this.fbbanner = str5;
        this.fbinterstitial = str6;
        this.fbnative = str7;
    }

    public String getAdmangeid() {
        return this.admangeid;
    }

    public void setAdmangeid(String str) {
        this.admangeid = str;
    }

    public String getAcname() {
        return this.acname;
    }

    public void setAcname(String str) {
        this.acname = str;
    }

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

    public String getIsactive() {
        return this.isactive;
    }

    public void setIsactive(String str) {
        this.isactive = str;
    }

    public String getAdmobbanner() {
        return this.admobbanner;
    }

    public void setAdmobbanner(String str) {
        this.admobbanner = str;
    }

    public String getAdmobinterstitial() {
        return this.admobinterstitial;
    }

    public void setAdmobinterstitial(String str) {
        this.admobinterstitial = str;
    }

    public String getAdmobnative() {
        return this.admobnative;
    }

    public void setAdmobnative(String str) {
        this.admobnative = str;
    }

    public String getAdmobvideo() {
        return this.admobvideo;
    }

    public void setAdmobvideo(String str) {
        this.admobvideo = str;
    }

    public String getFbbanner() {
        return this.fbbanner;
    }

    public void setFbbanner(String str) {
        this.fbbanner = str;
    }

    public String getFbinterstitial() {
        return this.fbinterstitial;
    }

    public void setFbinterstitial(String str) {
        this.fbinterstitial = str;
    }

    public String getFbnative() {
        return this.fbnative;
    }

    public void setFbnative(String str) {
        this.fbnative = str;
    }

    public String getFbvideo() {
        return this.fbvideo;
    }

    public void setFbvideo(String str) {
        this.fbvideo = str;
    }

    public String getSabanner() {
        return this.sabanner;
    }

    public void setSabanner(String str) {
        this.sabanner = str;
    }

    public String getSainterstitial() {
        return this.sainterstitial;
    }

    public void setSainterstitial(String str) {
        this.sainterstitial = str;
    }

    public String getSanative() {
        return this.sanative;
    }

    public void setSanative(String str) {
        this.sanative = str;
    }

    public String getSavideo() {
        return this.savideo;
    }

    public void setSavideo(String str) {
        this.savideo = str;
    }

    public String getIscompulsoryupdate() {
        return this.iscompulsoryupdate;
    }

    public void setIscompulsoryupdate(String str) {
        this.iscompulsoryupdate = str;
    }

    public String getAppversion() {
        return this.appversion;
    }

    public void setAppversion(String str) {
        this.appversion = str;
    }

    public String getAppremoved() {
        return this.appremoved;
    }

    public void setAppremoved(String str) {
        this.appremoved = str;
    }

    public String getNewappurl() {
        return this.newappurl;
    }

    public void setNewappurl(String str) {
        this.newappurl = str;
    }

    public String getIsmsg() {
        return this.ismsg;
    }

    public void setIsmsg(String str) {
        this.ismsg = str;
    }

    public String getMsg() {
        return this.msg;
    }

    public void setMsg(String str) {
        this.msg = str;
    }

    public String getIsincustomeads() {
        return this.isincustomeads;
    }

    public void setIsincustomeads(String str) {
        this.isincustomeads = str;
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

    public String getCreatedat() {
        return this.createdat;
    }

    public void setCreatedat(String str) {
        this.createdat = str;
    }

    public String getUpdatedat() {
        return this.updatedat;
    }

    public void setUpdatedat(String str) {
        this.updatedat = str;
    }
}
