package com.harsha.videoplayer.maxplayer.video.player.Extra;

import java.io.Serializable;

public class MediaData implements Serializable {
    String addeddate;
    String duration;
    String folder;
    public int layoutType;
    String length;
    String modifieddate;
    String name;
    String path;
    String resolution;
    int videoDuration;
    public int videoLastPlayPosition = 0;

    public String getName() {
        return this.name;
    }

    public void setName(String str) {
        this.name = str;
    }

    public String getPath() {
        return this.path;
    }

    public void setPath(String str) {
        this.path = str;
    }

    public String getFolder() {
        return this.folder;
    }

    public void setFolder(String str) {
        this.folder = str;
    }

    public String getLength() {
        return this.length;
    }

    public void setLength(String str) {
        this.length = str;
    }

    public String getAddeddate() {
        return this.addeddate;
    }

    public void setAddeddate(String str) {
        this.addeddate = str;
    }

    public String getModifieddate() {
        return this.modifieddate;
    }

    public void setModifieddate(String str) {
        this.modifieddate = str;
    }

    public String getDuration() {
        return this.duration;
    }

    public void setDuration(String str) {
        this.duration = str;
    }

    public String getResolution() {
        return this.resolution;
    }

    public void setResolution(String str) {
        this.resolution = str;
    }

    public int getVideoDuration() {
        return this.videoDuration;
    }

    public void setVideoDuration(int i) {
        this.videoDuration = i;
    }

    public int getLayoutType() {
        return this.layoutType;
    }

    public void setLayoutType(int i) {
        this.layoutType = i;
    }

    public int getVideoLastPlayPosition() {
        return this.videoLastPlayPosition;
    }

    public void setVideoLastPlayPosition(int i) {
        this.videoLastPlayPosition = i;
    }
}
