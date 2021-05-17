package com.harsha.videoplayer.maxplayer.video.player.appmanage.model;

import androidx.core.app.NotificationCompat;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MainAdmanage {
    @SerializedName("count")
    @Expose
    private Integer count;
    @SerializedName("data")
    @Expose
    private AllData data;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName(NotificationCompat.CATEGORY_STATUS)
    @Expose
    private Boolean status;
    @SerializedName("statusCode")
    @Expose
    private Integer statusCode;

    public Integer getStatusCode() {
        return this.statusCode;
    }

    public void setStatusCode(Integer num) {
        this.statusCode = num;
    }

    public Boolean getStatus() {
        return this.status;
    }

    public void setStatus(Boolean bool) {
        this.status = bool;
    }

    public AllData getData() {
        return this.data;
    }

    public void setData(AllData allData) {
        this.data = allData;
    }

    public Integer getCount() {
        return this.count;
    }

    public void setCount(Integer num) {
        this.count = num;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String str) {
        this.message = str;
    }
}
