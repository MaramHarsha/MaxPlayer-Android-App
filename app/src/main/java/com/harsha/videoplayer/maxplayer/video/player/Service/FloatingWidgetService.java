package com.harsha.videoplayer.maxplayer.video.player.Service;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.VideoView;

import com.facebook.ads.AdError;
import com.harsha.videoplayer.maxplayer.video.player.Activity.VideoPlayerActivity;
import com.harsha.videoplayer.maxplayer.video.player.R;
import com.harsha.videoplayer.maxplayer.video.player.Util.Constant;
import com.harsha.videoplayer.maxplayer.video.player.Util.VideoPlayerManager;
import com.harsha.videoplayer.maxplayer.video.player.Extra.MediaData;

import java.util.ArrayList;

public class FloatingWidgetService extends Service {
    ImageView closeWindow;
    View floatingView;
    ImageView floatingWindow;
    ImageView next;
    WindowManager.LayoutParams params;
    ImageView playPause;
    ImageView previous;
    MediaData video;
    public ArrayList<MediaData> videoList;
    public int videoPosition;
    VideoView videoView;
    WindowManager windowManager;

    public IBinder onBind(Intent intent) {
        return null;
    }

    public int onStartCommand(Intent intent, int i, int i2) {
        this.videoPosition = intent.getIntExtra(Constant.EXTRA_VIDEO_POSITION, 0);
        return super.onStartCommand(intent, i, i2);
    }

    public void onCreate() {
        super.onCreate();
        this.floatingView = LayoutInflater.from(this).inflate(R.layout.floating_widget_layout, (ViewGroup) null);
        this.videoList = VideoPlayerManager.getVideoList();
        this.video = VideoPlayerManager.getLastPlayVideos();
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(-2, -2, Build.VERSION.SDK_INT >= 26 ? 2038 : AdError.CACHE_ERROR_CODE, 8, -3);
        this.params = layoutParams;
        layoutParams.gravity = 51;
        this.params.x = 0;
        this.params.y = 100;
        @SuppressLint("WrongConstant")
        WindowManager windowManager2 = (WindowManager) getSystemService("window");
        this.windowManager = windowManager2;
        windowManager2.addView(this.floatingView, this.params);
        this.playPause = (ImageView) this.floatingView.findViewById(R.id.playPause);
        this.closeWindow = (ImageView) this.floatingView.findViewById(R.id.closeWindow);
        this.floatingWindow = (ImageView) this.floatingView.findViewById(R.id.floatingWindow);
        this.next = (ImageView) this.floatingView.findViewById(R.id.next);
        this.previous = (ImageView) this.floatingView.findViewById(R.id.previous);
        VideoView videoView2 = (VideoView) this.floatingView.findViewById(R.id.videoView);
        this.videoView = videoView2;
        MediaData media_Data = this.video;
        if (media_Data != null) {
            videoView2.setVideoPath(media_Data.getPath());
            this.videoView.seekTo(this.video.getVideoLastPlayPosition());
            this.videoView.start();
        }
        this.videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            public void onCompletion(MediaPlayer mediaPlayer) {
                if (FloatingWidgetService.this.videoPosition < FloatingWidgetService.this.videoList.size() - 1) {
                    FloatingWidgetService.this.videoPosition++;
                    FloatingWidgetService.this.videoView.setVideoPath(FloatingWidgetService.this.videoList.get(FloatingWidgetService.this.videoPosition).getPath());
                    FloatingWidgetService.this.videoView.start();
                    return;
                }
                FloatingWidgetService.this.videoView.pause();
                FloatingWidgetService.this.playPause.setImageResource(R.drawable.hplib_ic_play_download);
            }
        });
        this.next.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                if (FloatingWidgetService.this.videoPosition < FloatingWidgetService.this.videoList.size() - 1) {
                    FloatingWidgetService.this.videoPosition++;
                    FloatingWidgetService.this.videoView.setVideoPath(FloatingWidgetService.this.videoList.get(FloatingWidgetService.this.videoPosition).getPath());
                    FloatingWidgetService.this.videoView.start();
                }
            }
        });
        this.previous.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                if (FloatingWidgetService.this.videoPosition > 0) {
                    FloatingWidgetService floatingWidgetService = FloatingWidgetService.this;
                    floatingWidgetService.videoPosition--;
                    FloatingWidgetService.this.videoView.setVideoPath(FloatingWidgetService.this.videoList.get(FloatingWidgetService.this.videoPosition).getPath());
                    FloatingWidgetService.this.videoView.start();
                }
            }
        });
        this.closeWindow.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                VideoPlayerManager.setIsFloatingVideo(false);
                FloatingWidgetService.this.stopSelf();
            }
        });
        this.playPause.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                if (FloatingWidgetService.this.videoView.isPlaying()) {
                    FloatingWidgetService.this.videoView.pause();
                    FloatingWidgetService.this.playPause.setImageResource(R.drawable.hplib_ic_play_download);
                    return;
                }
                FloatingWidgetService.this.videoView.start();
                FloatingWidgetService.this.playPause.setImageResource(R.drawable.hplib_ic_pause);
            }
        });
        this.floatingWindow.setOnClickListener(new View.OnClickListener() {

            @SuppressLint("WrongConstant")
            public void onClick(View view) {
                VideoPlayerManager.setIsFloatingVideo(false);
                FloatingWidgetService floatingWidgetService = FloatingWidgetService.this;
                floatingWidgetService.startActivity(VideoPlayerActivity.getInstance(floatingWidgetService.getApplicationContext(),
                        FloatingWidgetService.this.videoView.getCurrentPosition(), true).setFlags(268435456));
                FloatingWidgetService.this.stopSelf();
            }
        });
        this.floatingView.findViewById(R.id.mainParentRelativeLayout).setOnTouchListener(new View.OnTouchListener() {
            float mTouchX;
            float mTouchY;
            int mXAxis;
            int mYAxis;

            public boolean onTouch(View view, MotionEvent motionEvent) {
                int action = motionEvent.getAction();
                if (action != 0) {
                    if (action != 1) {
                        if (action != 2) {
                            return false;
                        }
                        FloatingWidgetService.this.params.x = this.mXAxis + ((int) (motionEvent.getRawX() - this.mTouchX));
                        FloatingWidgetService.this.params.y = this.mYAxis + ((int) (motionEvent.getRawY() - this.mTouchY));
                        FloatingWidgetService.this.windowManager.updateViewLayout(FloatingWidgetService.this.floatingView, FloatingWidgetService.this.params);
                    }
                    return true;
                }
                this.mXAxis = FloatingWidgetService.this.params.x;
                this.mYAxis = FloatingWidgetService.this.params.y;
                this.mTouchX = motionEvent.getRawX();
                this.mTouchY = motionEvent.getRawY();
                return true;
            }
        });
    }

    public void onDestroy() {
        super.onDestroy();
        View view = this.floatingView;
        if (view != null) {
            this.windowManager.removeView(view);
        }
    }
}
