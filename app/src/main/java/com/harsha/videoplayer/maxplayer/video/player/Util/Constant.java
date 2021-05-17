package com.harsha.videoplayer.maxplayer.video.player.Util;

import android.os.Environment;
import com.harsha.videoplayer.maxplayer.video.player.Extra.MediaData;
import java.util.ArrayList;

public class Constant {
    public static final String EXTRA_BACKGROUND_VIDEO_PLAY_POSITION = "background_video_play_position";
    public static final String EXTRA_FLOATING_VIDEO = "floating_video";
    public static final String EXTRA_FOLDER_NAME = "folder_name";
    public static final String EXTRA_FOLDER_PATH = "folder_path";
    public static final String EXTRA_IS_CONTINUE_WATCHING_VIDEO = "is_continue_watching_video";
    public static final String EXTRA_IS_FLOATING_VIDEO = "is_floating_video";
    public static final String EXTRA_VIDEO = "extra_video";
    public static final String EXTRA_VIDEO_LIST = "video_list";
    public static final String EXTRA_VIDEO_POSITION = "video_position";
    public static String HIDE_PATH = (Environment.getExternalStorageDirectory().toString() + "/.Hide_Video");
    public static final String PREF_CONTINUE_WATCHING_VIDEO = "pref_continue_watching_video";
    public static final String PREF_FLOATING_VIDEO_POSITION = "pref_floating_video_position";
    public static final String PREF_IS_FLOATING_VIDEO = "pref_is_floating_video";
    public static final String PREF_LAST_PLAY_VIDEOS = "last_play_videos";
    public static final String PREF_VIDEO_LAST_POSITION = "pref_video_last_position";
    public static final String PREF_VIDEO_LIST = "pref_video_list";
    public static String YOUR_PATH = (Environment.getExternalStorageDirectory().toString() + "/Your_Video");
    public static ArrayList<MediaData> media_datas = new ArrayList<>();
}
