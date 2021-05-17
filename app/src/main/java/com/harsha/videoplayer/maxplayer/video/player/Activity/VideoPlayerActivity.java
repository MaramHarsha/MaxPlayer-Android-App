package com.harsha.videoplayer.maxplayer.video.player.Activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.provider.Settings;
import android.view.Menu;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.harsha.videoplayer.maxplayer.video.player.Extra.MediaData;
import com.harsha.videoplayer.maxplayer.video.player.Extra.VideoStanderd;
import com.harsha.videoplayer.maxplayer.video.player.R;
import com.harsha.videoplayer.maxplayer.video.player.Service.FloatingWidgetService;
import com.harsha.videoplayer.maxplayer.video.player.Service.VideoPlayAsAudioService;
import com.harsha.videoplayer.maxplayer.video.player.Util.Constant;
import com.harsha.videoplayer.maxplayer.video.player.Util.VideoPlayerManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class VideoPlayerActivity extends AppCompatActivity {
    boolean isAutoPlay = true;
    boolean isBackgroundEnable = false;
    boolean isContinueWatching = false;
    boolean isFloatingVideo = false;
    boolean isResumeVideo = false;
    boolean isService = false;
    boolean isShuffleClick = false;
    CountDownTimer sleepCountDownTimer;
    long sleepTimeMiliSecond;
    int videoLastProgress;
    HashMap<String, Integer> videoPlayLastPositionList = new HashMap<>();
    public VideoStanderd videoPlayer;
    int videoPosition;
    ArrayList<MediaData> videosList = new ArrayList<>();


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    public static Intent getIntent(Context context, ArrayList<MediaData> arrayList, int i) {
        Intent intent = new Intent(context, VideoPlayerActivity.class);
        intent.putExtra(Constant.EXTRA_VIDEO_LIST, arrayList);
        intent.putExtra(Constant.EXTRA_VIDEO_POSITION, i);
        return intent;
    }

    public static Intent getInstance(Context context, int i, boolean z) {
        Intent intent = new Intent(context, VideoPlayerActivity.class);
        intent.putExtra(Constant.EXTRA_FLOATING_VIDEO, i);
        intent.putExtra(Constant.EXTRA_IS_FLOATING_VIDEO, z);
        return intent;
    }

    public static Intent getIntent(Context context, ArrayList<MediaData> arrayList, int i, boolean z) {
        Intent intent = new Intent(context, VideoPlayerActivity.class);
        intent.putExtra(Constant.EXTRA_VIDEO_LIST, arrayList);
        intent.putExtra(Constant.EXTRA_VIDEO_POSITION, i);
        intent.putExtra(Constant.EXTRA_BACKGROUND_VIDEO_PLAY_POSITION, z);
        return intent;
    }

    public static Intent getInstanceContinueWatching(Context context, ArrayList<MediaData> arrayList, int i, boolean z) {
        Intent intent = new Intent(context, VideoPlayerActivity.class);
        intent.putExtra(Constant.EXTRA_VIDEO_POSITION, i);
        intent.putExtra(Constant.EXTRA_VIDEO_LIST, arrayList);
        intent.putExtra(Constant.EXTRA_IS_CONTINUE_WATCHING_VIDEO, z);
        return intent;
    }

    @Override
    public void onWindowFocusChanged(boolean z) {
        super.onWindowFocusChanged(z);
        if (z) {
            getWindow().setFlags(1024, 1024);
        }
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        requestWindowFeature(1);
        getWindow().setFlags(1024, 1024);
        setContentView(R.layout.activity_video_player);
        this.videoPlayer = (VideoStanderd) findViewById(R.id.videoPlayer);
        startService(new Intent(this, VideoPlayAsAudioService.class).setAction(VideoPlayAsAudioService.NOTIFICATION_CLICK_ACTION));
        if (getIntent() != null) {
            this.videosList = (ArrayList<MediaData>) getIntent().getSerializableExtra(Constant.EXTRA_VIDEO_LIST);
            this.videoPosition = getIntent().getIntExtra(Constant.EXTRA_VIDEO_POSITION, 0);
        }
        this.videoPlayer.media_datas = this.videosList;
        this.videoPlayer.setAdapter();
        if (this.videosList == null) {
            this.videosList = VideoPlayerManager.getVideoList();
        }
        boolean booleanExtra = getIntent().getBooleanExtra(Constant.EXTRA_IS_FLOATING_VIDEO, false);
        this.isFloatingVideo = booleanExtra;
        if (booleanExtra) {
            this.videoPosition = VideoPlayerManager.getFloatingVideoPosition();
            this.videoLastProgress = getIntent().getIntExtra(Constant.EXTRA_FLOATING_VIDEO, 0);
        }
        this.isContinueWatching = getIntent().getBooleanExtra(Constant.EXTRA_IS_CONTINUE_WATCHING_VIDEO, false);
        boolean booleanExtra2 = getIntent().getBooleanExtra(Constant.EXTRA_BACKGROUND_VIDEO_PLAY_POSITION, false);
        this.isService = booleanExtra2;
        if (booleanExtra2) {
            this.videoLastProgress = VideoPlayerManager.getFloatingVideoPosition();
        }
        initView();
    }

    public void initView() {
        setVideoResume();
        videoPlayer.setOnVideoCompleteListener(new VideoStanderd.OnVideoCompleteListener() {

            @Override
            public void onDeleteCallBack() {
            }

            @Override
            public void onTakescreenShot() {
            }

            @Override
            public void onVideoComplete() {
                isFloatingVideo = false;
                if (videoPlayer.IsLoopingVideo()) {
                    videoPlayer.startVideo();
                } else if (isAutoPlay) {
                    if (isShuffleClick) {
                        getRandomVideoPosition();
                    }
                    if (videoPosition != videosList.size() - 1) {
                        videoPosition++;
                        if (videosList.size() > videoPosition) {
                            if (videosList.get(videoPosition).getLayoutType() == 1) {
                                videoPosition++;
                            }
                            if (videosList.size() > videoPosition) {
                                setVideoResume();
                            }
                        }
                    }
                    videoPlayLastPositionList.put(videosList.get(videoPosition).getPath(),
                            Integer.valueOf((int) videoPlayer.getCurrentDuration()));
                    VideoPlayerManager.setLastPlayVideos(videosList.get(videoPosition));
                }
            }

            @Override
            public void onNextClick() {
                VideoPlayerManager.setContinueWatchingVideos(VideoPlayerActivity.this.videosList.get(VideoPlayerActivity.this.videoPosition));
                if (VideoPlayerActivity.this.isShuffleClick) {
                    VideoPlayerActivity.this.getRandomVideoPosition();
                }
                if (VideoPlayerActivity.this.videoPosition != VideoPlayerActivity.this.videosList.size() - 1) {
                    VideoPlayerActivity.this.videoPosition++;
                    if (VideoPlayerActivity.this.videosList.get(VideoPlayerActivity.this.videoPosition).getLayoutType() == 1) {
                        VideoPlayerActivity.this.videoPosition++;
                    }
                    if (VideoPlayerActivity.this.videoPosition < VideoPlayerActivity.this.videosList.size()) {
                        VideoPlayerActivity.this.setVideoResume();
                    }
                }
                VideoPlayerActivity.this.videoPlayLastPositionList.put(VideoPlayerActivity.this.videosList.get(VideoPlayerActivity.this.videoPosition).getPath(), Integer.valueOf((int) VideoPlayerActivity.this.videoPlayer.getCurrentDuration()));
            }

            @Override
            public void onPreviousClick() {
                VideoPlayerManager.setContinueWatchingVideos(VideoPlayerActivity.this.videosList.get(VideoPlayerActivity.this.videoPosition));
                if (VideoPlayerActivity.this.isShuffleClick) {
                    VideoPlayerActivity.this.getRandomVideoPosition();
                }
                if (VideoPlayerActivity.this.videoPosition != 0) {
                    VideoPlayerActivity.this.videoPosition--;
                    if (VideoPlayerActivity.this.videosList.get(VideoPlayerActivity.this.videoPosition).getLayoutType() == 1) {
                        VideoPlayerActivity.this.videoPosition--;
                    }
                    if (VideoPlayerActivity.this.videoPosition != -1) {
                        VideoPlayerActivity.this.setVideoResume();
                    }
                }
                VideoPlayerActivity.this.videoPlayLastPositionList.put(VideoPlayerActivity.this.videosList.get(VideoPlayerActivity.this.videoPosition).getPath(), Integer.valueOf((int) VideoPlayerActivity.this.videoPlayer.getCurrentDuration()));
            }

            @SuppressLint("WrongConstant")
            @Override
            public void onFloatingwindowClick() {
                if (Build.VERSION.SDK_INT < 23) {
                    setValue();
                    startService(new Intent(VideoPlayerActivity.this, FloatingWidgetService.class).putExtra(Constant.EXTRA_VIDEO_POSITION, videoPosition));
                    finish();
                } else if (Settings.canDrawOverlays(VideoPlayerActivity.this)) {
                    setValue();
                    startService(new Intent(VideoPlayerActivity.this, FloatingWidgetService.class).putExtra(Constant.EXTRA_VIDEO_POSITION, videoPosition));
                    finish();
                } else {
                    askForSystemOverlayPermission();
                    Toast.makeText(VideoPlayerActivity.this, "System Alert Window Permission Is Required For Floating Widget.", 1).show();
                }
            }

            @Override
            public void onBackClick() {
                try {
                    showLeaveDialog();
                } catch (Exception e) {
                    e.printStackTrace();
                    showLeaveDialog();
                }
            }

            @Override
            public void onShuffleClick(boolean z) {
                VideoPlayerActivity.this.isShuffleClick = z;
            }

            @Override
            public void onBackgroundEnable(boolean z) {
                VideoPlayerActivity.this.isBackgroundEnable = z;
            }

            @Override
            public void onSelectVideo(int i) {
                VideoPlayerActivity.this.videoPosition = i;
                VideoPlayerActivity.this.setVideoResume();
                VideoPlayerActivity.this.videoPlayLastPositionList.put(VideoPlayerActivity.this.videosList.get(VideoPlayerActivity.this.videoPosition).getPath(), Integer.valueOf((int) VideoPlayerActivity.this.videoPlayer.getCurrentDuration()));
            }
        });
    }

    public void setVideoResume() {
        this.videoLastProgress = 0;
        if (this.videosList.size() > this.videoPosition) {
            HashMap<String, Integer> videoLastPosition = VideoPlayerManager.getVideoLastPosition();
            if (this.isResumeVideo) {
                if (videoLastPosition.containsKey(this.videosList.get(this.videoPosition).getPath())) {
                    this.videoLastProgress = (int) Double.parseDouble(String.valueOf(videoLastPosition.get(this.videosList.get(this.videoPosition).getPath())));
                }
            } else if (this.isContinueWatching) {
                this.videoLastProgress = (int) Double.parseDouble(String.valueOf(this.videosList.get(this.videoPosition).getVideoLastPlayPosition()));
            }
            final int i2 = this.videoPlayer.screen;
            this.videoPlayer.setUp(this.videosList.get(this.videoPosition).getPath(), this.videosList.get(this.videoPosition).getName(), 0);
            this.videoPlayer.startVideo();
            new Handler().postDelayed(() -> {
                if (i2 != -1) {
                    VideoPlayerActivity.this.videoPlayer.screen = i2;
                }
            }, 500);
            this.videoPlayer.gotoFullscreen();
        }
    }

    public void setValue() {
        MediaData media_Data = this.videosList.get(this.videoPosition);
        media_Data.setVideoLastPlayPosition((int) this.videoPlayer.getCurrentDuration());
        VideoPlayerManager.setLastPlayVideos(media_Data);
        VideoPlayerManager.setFloatingVideoPosition(this.videoPosition);
        VideoPlayerManager.setVideoList(this.videosList);
        VideoPlayerManager.setIsFloatingVideo(true);
    }

    public void getRandomVideoPosition() {
        this.videoPosition = new Random().nextInt(this.videosList.size());
    }

    public void askForSystemOverlayPermission() {
        if (Build.VERSION.SDK_INT >= 23 && !Settings.canDrawOverlays(this)) {
            startActivityForResult(new Intent("android.settings.action.MANAGE_OVERLAY_PERMISSION", Uri.parse("package:" + getPackageName())), 123);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        VideoStanderd vdStd = this.videoPlayer;
        if (vdStd != null) {
            vdStd.onPause();
        }
        startBackgroundVideoPlayService();
    }

    @Override
    public void onResume() {
        super.onResume();
        VideoStanderd vdStd = this.videoPlayer;
        if (vdStd != null) {
            vdStd.onStart();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (this.videoPlayer != null) {
            if (((long) this.videosList.get(this.videoPosition).getVideoDuration()) != this.videoPlayer.getCurrentDuration()) {
                MediaData media_Data = this.videosList.get(this.videoPosition);
                media_Data.setVideoLastPlayPosition((int) this.videoPlayer.getCurrentDuration());
                VideoPlayerManager.setContinueWatchingVideos(media_Data);
            }
            this.videoPlayLastPositionList.put(this.videosList.get(this.videoPosition).getPath(), Integer.valueOf((int) this.videoPlayer.getCurrentDuration()));
            VideoPlayerManager.setVideoLastPosition(this.videoPlayLastPositionList);
            this.videoPlayer.onPause();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
    }

    @Override
    public void onBackPressed() {
        try {
            this.videoPlayer.onPause();
            showLeaveDialog();
        } catch (Exception e) {
            e.printStackTrace();
            showLeaveDialog();
        }
    }

    public void showLeaveDialog() {
        finish();
    }

    public void startBackgroundVideoPlayService() {
        if (this.isBackgroundEnable) {
            MediaData media_Data = this.videosList.get(this.videoPosition);
            media_Data.setVideoLastPlayPosition((int) this.videoPlayer.getCurrentDuration());
            this.videosList.set(this.videoPosition, media_Data);
            VideoPlayerManager.setVideoList(this.videosList);
            stopService(new Intent(this, VideoPlayAsAudioService.class));
            if (Build.VERSION.SDK_INT >= 26) {
                startForegroundService(new Intent(this, VideoPlayAsAudioService.class).putExtra(Constant.EXTRA_VIDEO_POSITION, this.videoPosition));
            } else {
                startService(new Intent(this, VideoPlayAsAudioService.class).putExtra(Constant.EXTRA_VIDEO_POSITION, this.videoPosition));
            }
        }
    }
}
