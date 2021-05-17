package com.harsha.videoplayer.maxplayer.video.player.Model;


import com.harsha.videoplayer.maxplayer.video.player.Extra.MediaData;

import java.io.Serializable;
import java.util.ArrayList;

public class Folder implements Serializable {
    ArrayList<MediaData> mediadata;
    String name;

    public String getName() {
        return this.name;
    }

    public void setName(String str) {
        this.name = str;
    }

    public ArrayList<MediaData> getMedia_data() {
        return this.mediadata;
    }
    public void setMedia_data(ArrayList<MediaData> arrayList) {
        this.mediadata = arrayList;
    }
}
