package com.harsha.videoplayer.maxplayer.video.player.Activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.harsha.videoplayer.maxplayer.video.player.Adapter.VideoAdapter;
import com.harsha.videoplayer.maxplayer.video.player.Extra.MediaData;
import com.harsha.videoplayer.maxplayer.video.player.Model.Folder;
import com.harsha.videoplayer.maxplayer.video.player.R;
import com.harsha.videoplayer.maxplayer.video.player.Util.VideoPlayerManager;
import com.harsha.videoplayer.maxplayer.video.player.appmanage.ads.AdsBanner;
import com.harsha.videoplayer.maxplayer.video.player.appmanage.ads.AdsFullScreen;

import java.util.ArrayList;

public class VideolistActivity extends AppCompatActivity implements View.OnClickListener {
    public static ImageView ivnodata;
    public static RecyclerView videolist;
    public AdsFullScreen adsFullScreen;
    private ImageView backfolder;
    FrameLayout frameLayoutCustomAd;
    private Folder mediadata;
    private ProgressBar progress;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_videolist);
        @SuppressLint("WrongConstant") NetworkInfo activeNetworkInfo = ((ConnectivityManager) getSystemService("connectivity")).getActiveNetworkInfo();
        if (activeNetworkInfo != null) {
            activeNetworkInfo.isConnected();
        }
        this.mediadata = (Folder) getIntent().getSerializableExtra("data");
        initView();
        initListener();
        new AdsBanner(this, (RelativeLayout) findViewById(R.id.rl_ad), (RelativeLayout) findViewById(R.id.rl_layout)).loadBannerAd();
        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.frameLayoutCustomAd);
        this.frameLayoutCustomAd = frameLayout;
        AdsFullScreen adsFullScreen2 = new AdsFullScreen(this, frameLayout);
        this.adsFullScreen = adsFullScreen2;
        adsFullScreen2.getIsResult(false);
        this.adsFullScreen.loadFullAd();
    }

    private void initListener() {
        this.backfolder.setOnClickListener(this);
    }

    private void initView() {
        videolist = (RecyclerView) findViewById(R.id.video_list);
        this.progress = (ProgressBar) findViewById(R.id.progress);
        TextView foldername = (TextView) findViewById(R.id.folder_name);
        this.backfolder = (ImageView) findViewById(R.id.back_folder);
        ivnodata = (ImageView) findViewById(R.id.iv_nodata);
        foldername.setText(this.mediadata.getName());
        initAdapter(this.mediadata.getMedia_data(), VideoPlayerManager.getViewBy());
    }

    @SuppressLint("WrongConstant")
    private void initAdapter(ArrayList<MediaData> arrayList, int i) {
        this.progress.setVisibility(8);
        if (arrayList.size() > 0) {
            videolist.setVisibility(0);
            ivnodata.setVisibility(8);
            VideoAdapter video_Adapter = new VideoAdapter(this, arrayList, i, 1);
            if (i == 0) {
                videolist.setLayoutManager(new LinearLayoutManager(this, 1, false));
            } else {
                videolist.setLayoutManager(new GridLayoutManager((Context) this, 3, 1, false));
            }
            videolist.setAdapter(video_Adapter);
            return;
        }
        videolist.setVisibility(8);
        ivnodata.setVisibility(0);
    }

    public void onClick(View view) {
        if (view.getId() == R.id.back_folder) {
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
