package com.harsha.videoplayer.maxplayer.video.player.Util;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.StrictMode;
import android.util.Base64;
import android.util.Log;
import com.facebook.ads.AdSettings;
import com.facebook.ads.AudienceNetworkAds;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.gson.Gson;
import com.harsha.videoplayer.maxplayer.video.player.Model.HistoryVideo;
import com.harsha.videoplayer.maxplayer.video.player.Extra.MediaData;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class VideoPlayerManager extends Application {
    private static final String TAG = "Prefrance_Manager";
    private static VideoPlayerManager mInstance;
    static SharedPreferences.Editor prefEditor;
    static SharedPreferences preferences;
    public String ssh;

    public static native String getNativeKeyAdManage();

    public native String getToken();

    static {
//        System.loadLibrary("keys");
    }

    public static synchronized VideoPlayerManager getInstance() {
        VideoPlayerManager videoPlayerManager;
        synchronized (VideoPlayerManager.class) {
            synchronized (VideoPlayerManager.class) {
                videoPlayerManager = mInstance;
            }
            return videoPlayerManager;
        }
    }

    public void onCreate() {
        super.onCreate();
        SharedPreferences sharedPreferences = getSharedPreferences("video_player", 0);
        preferences = sharedPreferences;
        SharedPreferences.Editor edit = sharedPreferences.edit();
        prefEditor = edit;
        edit.commit();
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        AudienceNetworkAds.initialize(this);
        AudienceNetworkAds.initialize(this);
        AdSettings.addTestDevice("3eb54496-7fc6-4a86-b48d-2194dda3a13a");
        AdSettings.addTestDevice("5c3c1ec5-e788-4c7f-84f3-5ccb907f381e");
        mInstance = this;
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().build());
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        MobileAds.setRequestConfiguration(MobileAds.getRequestConfiguration().toBuilder().setTestDeviceIds(Arrays.asList("D3F23F1BFA0086CDE26201AB0E4645A6")).build());
        try {
            @SuppressLint("WrongConstant")
            Signature[] signatureArr = getPackageManager().getPackageInfo(getPackageName(), 64).signatures;
            for (Signature signature : signatureArr) {
                MessageDigest instance = MessageDigest.getInstance("SHA");
                instance.update(signature.toByteArray());
                this.ssh = new String(Base64.encode(instance.digest(), 0));
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("name not found", e.toString());
        } catch (NoSuchAlgorithmException e2) {
            Log.e("no such an algorithm", e2.toString());
        } catch (Exception e3) {
            Log.e("exception", e3.toString());
        }
    }

    public static String getRecentPlay() {
        return preferences.getString("recent", "");
    }

    public static void putRecentPlay(String str) {
        prefEditor.putString("recent", str);
        prefEditor.commit();
    }

    public static int getViewBy() {
        return preferences.getInt("view_by", 0);
    }

    public static void putViewBy(int i) {
        prefEditor.putInt("view_by", i);
        prefEditor.commit();
    }

    public static String getSecurityQuestion() {
        return preferences.getString("security_question", "");
    }

    public static void putSecurityQuestion(String str) {
        prefEditor.putString("security_question", str);
        prefEditor.commit();
    }

    public static String getAnswerQuestion() {
        return preferences.getString("security_answer", "");
    }

    public static void putAnswerQuestion(String str) {
        prefEditor.putString("security_answer", str);
        prefEditor.commit();
    }

    public static String getPass() {
        return preferences.getString("pass", "");
    }

    public static void putPass(String str) {
        prefEditor.putString("pass", str);
        prefEditor.commit();
    }

    public static boolean getSetPass() {
        return preferences.getBoolean("set_pass", false);
    }

    public static void putSetPass(boolean z) {
        prefEditor.putBoolean("set_pass", z);
        prefEditor.commit();
    }

    public static boolean getSetQuestion() {
        return preferences.getBoolean("set_question", false);
    }

    public static void putSetQuestion(boolean z) {
        prefEditor.putBoolean("set_question", z);
        prefEditor.commit();
    }

    public static HashMap<String, Integer> getVideoLastPosition() {
        HashMap<String, Integer> hashMap = new HashMap<>();
        String string = preferences.getString(Constant.PREF_VIDEO_LAST_POSITION, "");
        return string != null ? (HashMap) new Gson().fromJson(string, HashMap.class) : hashMap;
    }

    public static void setVideoLastPosition(HashMap<String, Integer> hashMap) {
        HashMap hashMap2 = new HashMap();
        if (hashMap != null) {
            String string = preferences.getString(Constant.PREF_VIDEO_LAST_POSITION, "");
            if (string != null) {
                hashMap2 = (HashMap) new Gson().fromJson(string, HashMap.class);
            }
            if (hashMap2 != null) {
                hashMap2.putAll(hashMap);
            } else {
                hashMap2 = new HashMap();
                hashMap2.putAll(hashMap);
            }
        }
        SharedPreferences.Editor edit = preferences.edit();
        edit.putString(Constant.PREF_VIDEO_LAST_POSITION, new Gson().toJson(hashMap2));
        edit.apply();
    }

    public static void setVideoList(ArrayList<MediaData> arrayList) {
        HistoryVideo historyVideo = new HistoryVideo(arrayList);
        prefEditor.remove(Constant.PREF_VIDEO_LIST);
        prefEditor.commit();
        prefEditor.putString(Constant.PREF_VIDEO_LIST, new Gson().toJson(historyVideo));
        prefEditor.apply();
    }

    public static ArrayList<MediaData> getVideoList() {
        HistoryVideo historyVideo;
        ArrayList<MediaData> arrayList = new ArrayList<>();
        String string = preferences.getString(Constant.PREF_VIDEO_LIST, "");
        if (!(string == null || (historyVideo = (HistoryVideo) new Gson().fromJson(string, HistoryVideo.class)) == null)) {
            arrayList.addAll(historyVideo.getVideoList());
        }
        return arrayList;
    }

    public static void setFloatingVideoPosition(int i) {
        prefEditor.putInt(Constant.PREF_FLOATING_VIDEO_POSITION, i);
        prefEditor.apply();
    }

    public static int getFloatingVideoPosition() {
        return preferences.getInt(Constant.PREF_FLOATING_VIDEO_POSITION, 0);
    }

    public static void setIsFloatingVideo(boolean z) {
        prefEditor.putBoolean(Constant.PREF_IS_FLOATING_VIDEO, z);
        prefEditor.apply();
    }

    public boolean isFloatingVideo() {
        return preferences.getBoolean(Constant.PREF_IS_FLOATING_VIDEO, false);
    }

    public static void setLastPlayVideos(MediaData media_Data) {
        prefEditor.putString(Constant.PREF_LAST_PLAY_VIDEOS, new Gson().toJson(media_Data));
        prefEditor.apply();
    }

    public static MediaData getLastPlayVideos() {
        String string = preferences.getString(Constant.PREF_LAST_PLAY_VIDEOS, "");
        if (string != null) {
            return (MediaData) new Gson().fromJson(string, MediaData.class);
        }
        return null;
    }

    public static void setContinueWatchingVideos(MediaData media_Data) {
        ArrayList arrayList = new ArrayList();
        if (media_Data != null) {
            if (getContinueWatchingVideos() != null) {
                arrayList.addAll(getContinueWatchingVideos());
                if (arrayList.size() != 0) {
                    for (int i = 0; i < arrayList.size(); i++) {
                        if (((MediaData) arrayList.get(i)).getPath().equals(media_Data.getPath())) {
                            arrayList.remove(i);
                        }
                    }
                }
            }
            arrayList.add(0, media_Data);
        }
        prefEditor.putString(Constant.PREF_CONTINUE_WATCHING_VIDEO, new Gson().toJson(new HistoryVideo(arrayList)));
        prefEditor.apply();
    }

    public void updateContinueWatchingVideo(ArrayList<MediaData> arrayList) {
        prefEditor.putString(Constant.PREF_CONTINUE_WATCHING_VIDEO, new Gson().toJson(new HistoryVideo(arrayList)));
        prefEditor.apply();
    }

    public static List<MediaData> getContinueWatchingVideos() {
        HistoryVideo historyVideo;
        ArrayList arrayList = new ArrayList();
        String string = preferences.getString(Constant.PREF_CONTINUE_WATCHING_VIDEO, "");
        if (!(string == null || (historyVideo = (HistoryVideo) new Gson().fromJson(string, HistoryVideo.class)) == null)) {
            arrayList.addAll(historyVideo.getVideoList());
        }
        return arrayList;
    }
}
