package com.harsha.videoplayer.maxplayer.video.player.Activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.harsha.videoplayer.maxplayer.video.player.Adapter.HideAdapter;
import com.harsha.videoplayer.maxplayer.video.player.Extra.MediaData;
import com.harsha.videoplayer.maxplayer.video.player.R;
import com.harsha.videoplayer.maxplayer.video.player.Util.Constant;
import com.harsha.videoplayer.maxplayer.video.player.Util.Utils;
import com.harsha.videoplayer.maxplayer.video.player.Util.VideoPlayerManager;
import com.harsha.videoplayer.maxplayer.video.player.appmanage.ads.AdsBanner;
import com.harsha.videoplayer.maxplayer.video.player.appmanage.ads.AdsFullScreen;

import java.io.File;
import java.util.ArrayList;

public class HidevideoActivity extends AppCompatActivity implements View.OnClickListener {
    public static RecyclerView hiderecycler;
    public static ImageView ivnodata;
    public AdsFullScreen adsFullScreen;
    private ImageView backhide;
    FrameLayout frameLayoutCustomAd;
    private ProgressBar progress;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_hidevideo);
        @SuppressLint("WrongConstant") NetworkInfo activeNetworkInfo = ((ConnectivityManager) getSystemService("connectivity")).getActiveNetworkInfo();
        if (activeNetworkInfo != null) {
            activeNetworkInfo.isConnected();
        }
        initView();
        new AdsBanner(this, (RelativeLayout) findViewById(R.id.rl_ad), (RelativeLayout) findViewById(R.id.rl_layout)).loadBannerAd();
        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.frameLayoutCustomAd);
        this.frameLayoutCustomAd = frameLayout;
        AdsFullScreen adsFullScreen2 = new AdsFullScreen(this, frameLayout);
        this.adsFullScreen = adsFullScreen2;
        adsFullScreen2.getIsResult(false);
        this.adsFullScreen.loadFullAd();
        initListener();
        new Thread(() -> HidevideoActivity.this.getVideo()).start();
    }

    public void getVideo() {
        final ArrayList arrayList = new ArrayList();
        if (new File(Constant.HIDE_PATH).exists()) {
            File[] listFiles = new File(Constant.HIDE_PATH).listFiles();
            if (listFiles != null) {
                for (int i = 0; i < listFiles.length; i++) {
                    MediaData media_Data = new MediaData();
                    media_Data.setName(listFiles[i].getName());
                    media_Data.setPath(listFiles[i].getPath());
                    media_Data.setFolder(listFiles[i].getParentFile().getName());
                    media_Data.setLength(String.valueOf(listFiles[i].length()));
                    media_Data.setAddeddate(String.valueOf(listFiles[i].lastModified()));
                    media_Data.setModifieddate(String.valueOf(listFiles[i].lastModified()));
                    MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
                    mediaMetadataRetriever.setDataSource(listFiles[i].getPath());
                    String extractMetadata = mediaMetadataRetriever.extractMetadata(9);
                    int intValue = Integer.parseInt(mediaMetadataRetriever.extractMetadata(18));
                    int intValue2 = Integer.parseInt(mediaMetadataRetriever.extractMetadata(19));
                    mediaMetadataRetriever.release();
                    String duration = Utils.setDuration((long) Integer.parseInt(extractMetadata));
                    media_Data.setResolution(intValue + "×" + intValue2);
                    media_Data.setDuration(duration);
                    arrayList.add(media_Data);
                }
            }
            runOnUiThread(() -> HidevideoActivity.this.initAdapter(arrayList, VideoPlayerManager.getViewBy()));
        }
    }

    @SuppressLint("WrongConstant")
    public void initAdapter(ArrayList<MediaData> arrayList, int i) {
        this.progress.setVisibility(8);
        if (arrayList.size() > 0) {
            hiderecycler.setVisibility(0);
            ivnodata.setVisibility(8);
            HideAdapter hide_Adapter = new HideAdapter(this, arrayList, i);
            if (i == 0) {
                hiderecycler.setLayoutManager(new LinearLayoutManager(this, 1, false));
            } else {
                hiderecycler.setLayoutManager(new GridLayoutManager((Context) this, 2, 1, false));
            }
            hiderecycler.setAdapter(hide_Adapter);
            return;
        }
        hiderecycler.setVisibility(8);
        ivnodata.setVisibility(0);
    }

    private void initListener() {
        this.backhide.setOnClickListener(this);
    }

    private void initView() {
        hiderecycler = (RecyclerView) findViewById(R.id.hide_recycler);
        progress = (ProgressBar) findViewById(R.id.progress);
        backhide = (ImageView) findViewById(R.id.back_hide);
        ivnodata = (ImageView) findViewById(R.id.iv_nodata);
    }

    public void onClick(View view) {
        if (view.getId() == R.id.back_hide) {
            onBackPressed();
        }
    }

    @SuppressLint("WrongConstant")
    @Override
    public void onBackPressed() {
        FrameLayout frameLayout;
        if (this.adsFullScreen == null || (frameLayout = this.frameLayoutCustomAd) == null || frameLayout.getVisibility() != 0) {
            super.onBackPressed();
            return;
        }
        this.frameLayoutCustomAd.setVisibility(8);
        this.adsFullScreen.openNextActivity();
    }

    public void myStartActivity(Intent intent) {
        AdsFullScreen adsFullScreen2 = this.adsFullScreen;
        if (adsFullScreen2 == null || this.frameLayoutCustomAd == null) {
            startActivity(intent);
        } else {
            adsFullScreen2.showFullAd(intent);
        }
    }
}
