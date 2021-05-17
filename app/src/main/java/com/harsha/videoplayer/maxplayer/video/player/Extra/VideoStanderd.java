package com.harsha.videoplayer.maxplayer.video.player.Extra;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.core.view.GravityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.work.WorkRequest;

import com.harsha.videoplayer.maxplayer.video.player.R;

import java.text.SimpleDateFormat;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;

public class VideoStanderd extends Video {
    protected static Timer DISMISS_CONTROL_VIEW_TIMER = null;
    public static int LAST_GET_BATTERYLEVEL_PERCENT = 70;
    public static long LAST_GET_BATTERYLEVEL_TIME;
    public ImageView app_video_crop;
    public ImageView app_video_lock;
    public ImageView app_video_more;
    public ImageView app_video_mute;
    public ImageView app_video_next;
    public ImageView app_video_previous;
    public ImageView app_video_repete;
    public ImageView app_video_shuffle;
    public ImageView app_video_speed;
    public ImageView app_video_unlock;
    public ImageView backButton;
    public BroadcastReceiver battertReceiver = new BroadcastReceiver() {
        /* class com.videoplayer.hdmaxplayer.video.player.saxhdvideoplayer.Extra.VideoStanderd.AnonymousClass1 */

        public void onReceive(Context context, Intent intent) {
            if ("android.intent.action.BATTERY_CHANGED".equals(intent.getAction())) {
                VideoStanderd.LAST_GET_BATTERYLEVEL_PERCENT = (intent.getIntExtra("level", 0) * 100) /
                        intent.getIntExtra("scale", 100);
                VideoStanderd.this.setBatteryLevel();
                try {
                    jzvdContext.unregisterReceiver(VideoStanderd.this.battertReceiver);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    };
    public ImageView batteryLevel;
    public LinearLayout batteryTimeLayout;
    public TextView clarity;
    public PopupWindow clarityPopWindow;
    protected ArrayDeque<Runnable> delayTask = new ArrayDeque<>();
    protected long doubleTime = 200;
    protected long lastClickTime = 0;
    public ProgressBar loadingProgressBar;
    protected Dialog mBrightnessDialog;
    protected ProgressBar mDialogBrightnessProgressBar;
    protected TextView mDialogBrightnessTextView;
    protected ImageView mDialogIcon;
    protected ProgressBar mDialogProgressBar;
    protected TextView mDialogSeekTime;
    protected TextView mDialogTotalTime;
    protected ImageView mDialogVolumeImageView;
    protected ProgressBar mDialogVolumeProgressBar;
    protected TextView mDialogVolumeTextView;
    protected DismissControlViewTimerTask mDismissControlViewTimerTask;
    protected boolean mIsWifi;
    protected Dialog mProgressDialog;
    public TextView mRetryBtn;
    public LinearLayout mRetryLayout;
    protected Dialog mVolumeDialog;
    public ArrayList<MediaData> media_datas = new ArrayList<>();
    OnVideoCompleteListener onVideoCompleteListener;
    public ImageView play_back_speed_close;
    public ImageView posterImageView;
    public TextView replayTextView;
    Animation slideDownAnimation;
    Animation slideUpAnimation;
    public TextView speed05;
    public TextView speed10;
    public TextView speed125;
    public TextView speed15;
    public TextView speed175;
    public TextView speed20;
    public TextView speed75;
    public LinearLayout speed_layout;
    public ImageView tinyBackImageView;
    public TextView titleTextView;
    public TextView videoCurrentTime;
    public ImageView video_background_play;
    public ImageView video_floating_mode;
    public LinearLayout video_playback_options;
    private ImageView video_playlist;
    private ImageView video_playlist_close;
    public ImageView video_screenshot;
    private RecyclerView videoplaylist_recycler;

    public BroadcastReceiver wifiReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            boolean isWifiConnected;
            if ("android.net.conn.CONNECTIVITY_CHANGE".equals(intent.getAction()) && VideoStanderd.this.mIsWifi != (isWifiConnected = Utils.isWifiConnected(context))) {
                VideoStanderd.this.mIsWifi = isWifiConnected;
                if (!VideoStanderd.this.mIsWifi && !Video.WIFI_TIP_DIALOG_SHOWED && VideoStanderd.this.state == 5) {
                    VideoStanderd.this.startButton.performClick();
                    VideoStanderd.this.showWifiDialog();
                }
            }
        }
    };

    public interface OnVideoCompleteListener {
        void onBackClick();

        void onBackgroundEnable(boolean z);

        void onDeleteCallBack();

        void onFloatingwindowClick();

        void onNextClick();

        void onPreviousClick();

        void onSelectVideo(int i);

        void onShuffleClick(boolean z);

        void onTakescreenShot();

        void onVideoComplete();
    }

    @Override
    public int getLayoutId() {
        return R.layout.jz_layout_std;
    }

    public VideoStanderd(Context context) {
        super(context);
    }

    public VideoStanderd(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @Override
    public void init(Context context) {
        super.init(context);
        this.slideDownAnimation = AnimationUtils.loadAnimation(context, R.anim.pop_from_bottom_anim_in);
        this.slideUpAnimation = AnimationUtils.loadAnimation(context, R.anim.pop_from_bottom_anim_out);
        this.batteryTimeLayout = (LinearLayout) findViewById(R.id.battery_time_layout);
        this.titleTextView = (TextView) findViewById(R.id.title);
        this.backButton = (ImageView) findViewById(R.id.back);
        this.posterImageView = (ImageView) findViewById(R.id.poster);
        this.loadingProgressBar = (ProgressBar) findViewById(R.id.loading);
        this.tinyBackImageView = (ImageView) findViewById(R.id.back_tiny);
        this.batteryLevel = (ImageView) findViewById(R.id.battery_level);
        this.videoCurrentTime = (TextView) findViewById(R.id.video_current_time);
        this.replayTextView = (TextView) findViewById(R.id.replay_text);
        this.clarity = (TextView) findViewById(R.id.clarity);
        this.mRetryBtn = (TextView) findViewById(R.id.retry_btn);
        this.mRetryLayout = (LinearLayout) findViewById(R.id.retry_layout);
        this.app_video_lock = (ImageView) findViewById(R.id.app_video_lock);
        this.app_video_unlock = (ImageView) findViewById(R.id.app_video_unlock);
        this.app_video_crop = (ImageView) findViewById(R.id.app_video_crop);
        this.app_video_more = (ImageView) findViewById(R.id.app_video_more);
        this.app_video_next = (ImageView) findViewById(R.id.app_video_next);
        this.app_video_previous = (ImageView) findViewById(R.id.app_video_previous);
        this.video_playback_options = (LinearLayout) findViewById(R.id.video_playback_options);
        this.speed_layout = (LinearLayout) findViewById(R.id.speed_layout);
        this.video_playlist = (ImageView) findViewById(R.id.video_playlist);
        this.video_playlist_close = (ImageView) findViewById(R.id.video_list_close);
        this.videoplaylist_recycler = (RecyclerView) findViewById(R.id.videoplaylist_recycler);
        this.play_back_speed_close = (ImageView) findViewById(R.id.play_back_speed_close);
        this.video_screenshot = (ImageView) findViewById(R.id.video_screenshot);
        this.speed05 = (TextView) findViewById(R.id.speed05);
        this.speed75 = (TextView) findViewById(R.id.speed075);
        this.speed10 = (TextView) findViewById(R.id.speed10);
        this.speed125 = (TextView) findViewById(R.id.speed125);
        this.speed15 = (TextView) findViewById(R.id.speed15);
        this.speed175 = (TextView) findViewById(R.id.speed175);
        this.speed20 = (TextView) findViewById(R.id.speed20);
        this.app_video_shuffle = (ImageView) findViewById(R.id.app_video_shuffle);
        this.app_video_repete = (ImageView) findViewById(R.id.app_video_repete);
        this.app_video_speed = (ImageView) findViewById(R.id.app_video_speed);
        this.app_video_mute = (ImageView) findViewById(R.id.app_video_mute);
        this.video_floating_mode = (ImageView) findViewById(R.id.video_floating_mode);
        this.video_background_play = (ImageView) findViewById(R.id.video_background_play);
        if (this.batteryTimeLayout == null) {
            this.batteryTimeLayout = new LinearLayout(context);
        }
        if (this.titleTextView == null) {
            this.titleTextView = new TextView(context);
        }
        if (this.backButton == null) {
            this.backButton = new ImageView(context);
        }
        if (this.posterImageView == null) {
            this.posterImageView = new ImageView(context);
        }
        if (this.loadingProgressBar == null) {
            this.loadingProgressBar = new ProgressBar(context);
        }
        if (this.tinyBackImageView == null) {
            this.tinyBackImageView = new ImageView(context);
        }
        if (this.batteryLevel == null) {
            this.batteryLevel = new ImageView(context);
        }
        if (this.videoCurrentTime == null) {
            this.videoCurrentTime = new TextView(context);
        }
        if (this.replayTextView == null) {
            this.replayTextView = new TextView(context);
        }
        if (this.clarity == null) {
            this.clarity = new TextView(context);
        }
        if (this.mRetryBtn == null) {
            this.mRetryBtn = new TextView(context);
        }
        if (this.mRetryLayout == null) {
            this.mRetryLayout = new LinearLayout(context);
        }
        this.posterImageView.setOnClickListener(this);
        this.backButton.setOnClickListener(this);
        this.tinyBackImageView.setOnClickListener(this);
        this.clarity.setOnClickListener(this);
        this.mRetryBtn.setOnClickListener(this);
        this.app_video_lock.setOnClickListener(this);
        this.app_video_unlock.setOnClickListener(this);
        this.app_video_crop.setOnClickListener(this);
        this.app_video_next.setOnClickListener(this);
        this.app_video_previous.setOnClickListener(this);
        this.app_video_more.setOnClickListener(this);
        this.app_video_shuffle.setOnClickListener(this);
        this.app_video_repete.setOnClickListener(this);
        this.app_video_speed.setOnClickListener(this);
        this.app_video_mute.setOnClickListener(this);
        this.video_playlist.setOnClickListener(this);
        this.video_playlist_close.setOnClickListener(this);
        this.video_floating_mode.setOnClickListener(this);
        this.video_background_play.setOnClickListener(this);
        this.speed_layout.setOnClickListener(this);
        this.speed05.setOnClickListener(this);
        this.speed75.setOnClickListener(this);
        this.speed10.setOnClickListener(this);
        this.speed125.setOnClickListener(this);
        this.speed15.setOnClickListener(this);
        this.speed175.setOnClickListener(this);
        this.speed20.setOnClickListener(this);
        this.play_back_speed_close.setOnClickListener(this);
        this.video_screenshot.setOnClickListener(this);
    }

    @Override
    public void setUp(DataSource dataSource, int i, Class cls) {
        if (System.currentTimeMillis() - this.gobakFullscreenTime >= 200 && System.currentTimeMillis() - this.gotoFullscreenTime >= 200) {
            super.setUp(dataSource, i, cls);
            this.titleTextView.setText(dataSource.title);
            setScreen(i);
        }
    }

    @Override
    public void changeUrl(DataSource dataSource, long j) {
        super.changeUrl(dataSource, j);
        this.titleTextView.setText(dataSource.title);
    }

    public void changeStartButtonSize(int i) {
        ViewGroup.LayoutParams layoutParams = this.startButton.getLayoutParams();
        layoutParams.height = i;
        layoutParams.width = i;
        ViewGroup.LayoutParams layoutParams2 = this.loadingProgressBar.getLayoutParams();
        layoutParams2.height = i;
        layoutParams2.width = i;
    }

    @Override
    public void onStateNormal() {
        super.onStateNormal();
        changeUiToNormal();
    }

    @Override
    public void onStatePreparing() {
        super.onStatePreparing();
        changeUiToPreparing();
    }

    @Override
    public void onStatePreparingPlaying() {
        super.onStatePreparingPlaying();
        changeUIToPreparingPlaying();
    }

    @Override
    public void onStatePreparingChangeUrl() {
        super.onStatePreparingChangeUrl();
        changeUIToPreparingChangeUrl();
    }

    @SuppressLint("WrongConstant")
    @Override
    public void onStatePlaying() {
        super.onStatePlaying();
        changeUiToPlayingClear();
        startDismissControlViewTimer();
        this.app_video_play.setImageResource(R.drawable.hplib_ic_pause);
        this.startButton.setImageResource(R.drawable.jz_click_pause_selector);
        this.replayTextView.setVisibility(8);
    }

    @Override
    public void onStatePause() {
        super.onStatePause();
        changeUiToPauseShow();
        cancelDismissControlViewTimer();
    }

    @Override
    public void onStateError() {
        super.onStateError();
        changeUiToError();
    }

    @SuppressLint("WrongConstant")
    public void setAdapter() {
        VideolistAdapter video_list_Adapter = new VideolistAdapter(getApplicationContext(), this.media_datas, new VideolistAdapter.OnClickVideo() {

            @SuppressLint("WrongConstant")
            @Override
            public void onClickVideo(int i) {
                VideoStanderd.this.onVideoCompleteListener.onSelectVideo(i);
                VideoStanderd.this.videoplaylist_recycler.setVisibility(8);
                VideoStanderd.this.video_playlist_close.setVisibility(8);
            }
        });
        this.videoplaylist_recycler.setLayoutManager(new LinearLayoutManager(getApplicationContext(), 0, false));
        this.videoplaylist_recycler.setAdapter(video_list_Adapter);
    }

    public void setOnVideoCompleteListener(OnVideoCompleteListener onVideoCompleteListener2) {
        this.onVideoCompleteListener = onVideoCompleteListener2;
    }

    @Override
    public void onStateAutoComplete() {
        super.onStateAutoComplete();
        this.onVideoCompleteListener.onVideoComplete();
        changeUiToComplete();
        cancelDismissControlViewTimer();
    }

    @Override
    public void startVideo() {
        super.startVideo();
        registerWifiListener(getApplicationContext());
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        int id = view.getId();
        if (id == R.id.surface_container) {
            int action = motionEvent.getAction();
            if (action != 0 && action == 1) {
                if (this.screen != 1 || !this.isLockScreen) {
                    startDismissControlViewTimer();
                    if (this.mChangePosition) {
                        long duration = getDuration();
                        long j = this.mSeekTimePosition * 100;
                        if (duration == 0) {
                            duration = 1;
                        }
                        long j2 = j / duration;
                    }
                    Runnable r0 = new Runnable() {
                        public final void run() {
                            if (!mChangePosition && !mChangeVolume) {
                                onClickUiToggle();
                            }
                        }
                    };
                    view.postDelayed(r0, this.doubleTime + 20);
                    this.delayTask.add(r0);
                    while (this.delayTask.size() > 2) {
                        this.delayTask.pollFirst();
                    }
                    long currentTimeMillis = System.currentTimeMillis();
                    if (currentTimeMillis - this.lastClickTime < this.doubleTime) {
                        Iterator<Runnable> it = this.delayTask.iterator();
                        while (it.hasNext()) {
                            view.removeCallbacks(it.next());
                        }
                        if (this.state == 5 || this.state == 6) {
                            Log.d("JZVD", "doublClick [" + hashCode() + "] ");
                            this.startButton.performClick();
                        }
                    }
                    this.lastClickTime = currentTimeMillis;
                } else {
                    if (motionEvent.getX() == motionEvent.getX() || motionEvent.getY() == motionEvent.getY()) {
                        startDismissControlViewTimer();
                        onClickUiToggle();
                    }
                    return true;
                }
            }
            if (this.screen == 1 && this.isLockScreen) {
                return true;
            }
        } else if (id == R.id.bottom_seek_progress) {
            int action2 = motionEvent.getAction();
            if (action2 == 0) {
                cancelDismissControlViewTimer();
            } else if (action2 == 1) {
                startDismissControlViewTimer();
            }
        }
        return super.onTouch(view, motionEvent);
    }

    @SuppressLint("WrongConstant")
    @Override
    public void onClick(View view) {
        super.onClick(view);
        int id = view.getId();
        if (id == R.id.poster) {
            clickPoster();
        } else if (id == R.id.surface_container) {
            clickSurfaceContainer();
            PopupWindow popupWindow = this.clarityPopWindow;
            if (popupWindow != null) {
                popupWindow.dismiss();
            }
        } else if (id == R.id.back) {
            clickBack();
        } else if (id == R.id.back_tiny) {
            clickBackTiny();
        } else if (id == R.id.clarity) {
            clickClarity();
        } else if (id == R.id.retry_btn) {
            clickRetryBtn();
        } else if (id == R.id.app_video_lock) {
            if (this.screen == 1 || this.screen == 0) {
                this.app_video_lock.setTag(1);
                if (!this.isLockScreen) {
                    this.isLockScreen = true;
                    this.app_video_unlock.setVisibility(0);
                    dissmissControlView(1);
                }
            }
        } else if (id == R.id.app_video_unlock) {
            this.app_video_lock.setVisibility(0);
            this.app_video_unlock.setVisibility(8);
            this.isLockScreen = false;
            this.bottomContainer.setVisibility(0);
            this.topContainer.setVisibility(0);
            this.centerContainer.setVisibility(0);
            this.startButton.setVisibility(0);
            this.app_video_n_skip.setVisibility(0);
            this.app_video_p_skip.setVisibility(0);
            this.backButton.setVisibility(0);
        } else if (id == R.id.app_video_crop) {
            if (this.aspectRatio == 0) {
                Video.setVideoImageDisplayType(1);
                this.aspectRatio = 1;
            } else if (this.aspectRatio == 1) {
                Video.setVideoImageDisplayType(2);
                this.aspectRatio = 2;
            } else if (this.aspectRatio == 2) {
                Video.setVideoImageDisplayType(3);
                this.aspectRatio = 3;
            } else {
                Video.setVideoImageDisplayType(0);
                this.aspectRatio = 0;
            }
            this.rlAds.setVisibility(8);
        } else if (id == R.id.app_video_next) {
            this.onVideoCompleteListener.onNextClick();
            this.rlAds.setVisibility(8);
        } else if (id == R.id.app_video_previous) {
            this.onVideoCompleteListener.onPreviousClick();
            this.rlAds.setVisibility(8);
        } else if (id == R.id.app_video_shuffle) {
            if (this.isShuffle) {
                this.isShuffle = false;
                this.app_video_shuffle.setBackgroundResource(R.drawable.bg_circle_tansparant);
            } else {
                this.isShuffle = true;
                this.app_video_shuffle.setBackgroundResource(R.drawable.bg_circle_selected);
            }
            this.onVideoCompleteListener.onShuffleClick(this.isShuffle);
        } else if (id == R.id.app_video_repete) {
            if (this.isLooping) {
                this.app_video_repete.setImageResource(R.drawable.ic_repeat_black_24dp);
                this.isLooping = false;
                return;
            }
            this.app_video_repete.setImageResource(R.drawable.ic_repeat_one_black_24dp);
            this.isLooping = true;
            this.rlAds.setVisibility(8);
        } else if (id == R.id.app_video_mute) {
            if (!this.isMute) {
                this.app_video_mute.setImageResource(R.drawable.ic_volume_off_read);
                muteAudio();
                this.isMute = true;
                return;
            }
            this.app_video_mute.setImageResource(R.drawable.ic_volume_off_white_36dp);
            unMuteAudio();
            this.isMute = false;
        } else if (id == R.id.video_floating_mode) {
            this.onVideoCompleteListener.onFloatingwindowClick();
        } else if (id == R.id.video_background_play) {
            if (this.isBackgroundEnable) {
                this.isBackgroundEnable = false;
                this.video_background_play.setImageResource(R.drawable.ic_background_video);
            } else {
                this.isBackgroundEnable = true;
                this.video_background_play.setImageResource(R.drawable.ic_background_video_read);
            }
            this.onVideoCompleteListener.onBackgroundEnable(this.isBackgroundEnable);
        } else if (id == R.id.app_video_more) {
            if (!this.isShowMoreOption) {
                this.isShowMoreOption = true;
                this.slideUpAnimation.setAnimationListener(new Animation.AnimationListener() {


                    public void onAnimationRepeat(Animation animation) {
                    }

                    public void onAnimationStart(Animation animation) {
                    }

                    public void onAnimationEnd(Animation animation) {
                        VideoStanderd.this.video_playback_options.clearAnimation();
                    }
                });
                this.video_playback_options.setVisibility(0);
                this.video_playback_options.startAnimation(this.slideUpAnimation);
                return;
            }
            this.isShowMoreOption = false;
            this.video_playback_options.startAnimation(this.slideDownAnimation);
            this.slideDownAnimation.setAnimationListener(new Animation.AnimationListener() {

                public void onAnimationRepeat(Animation animation) {
                }

                public void onAnimationStart(Animation animation) {
                }

                public void onAnimationEnd(Animation animation) {
                    VideoStanderd.this.video_playback_options.setVisibility(8);
                    VideoStanderd.this.video_playback_options.clearAnimation();
                }
            });
        } else if (id == R.id.app_video_speed) {
            this.rlAds.setVisibility(8);
            if (this.speed_layout.getVisibility() == 0) {
                this.speed_layout.setVisibility(8);
                this.isSpeed = false;
                return;
            }
            if (this.videoplaylist_recycler.getVisibility() == 0) {
                this.videoplaylist_recycler.setVisibility(8);
                this.video_playlist_close.setVisibility(8);
            }
            this.speed_layout.setVisibility(0);
            this.isSpeed = true;
        } else if (id == R.id.speed05) {
            this.mediaInterface.setSpeed(0.5f);
            setSpeedViewBackground(this.speed05, this.speed75, this.speed10, this.speed125, this.speed15, this.speed175, this.speed20);
        } else if (id == R.id.speed075) {
            this.mediaInterface.setSpeed(0.75f);
            setSpeedViewBackground(this.speed75, this.speed05, this.speed10, this.speed125, this.speed15, this.speed175, this.speed20);
        } else if (id == R.id.speed10) {
            this.mediaInterface.setSpeed(1.0f);
            setSpeedViewBackground(this.speed10, this.speed75, this.speed05, this.speed125, this.speed15, this.speed175, this.speed20);
        } else if (id == R.id.speed125) {
            this.mediaInterface.setSpeed(1.25f);
            setSpeedViewBackground(this.speed125, this.speed75, this.speed10, this.speed05, this.speed15, this.speed175, this.speed20);
        } else if (id == R.id.speed15) {
            this.mediaInterface.setSpeed(1.5f);
            setSpeedViewBackground(this.speed15, this.speed75, this.speed10, this.speed125, this.speed05, this.speed175, this.speed20);
        } else if (id == R.id.speed175) {
            this.mediaInterface.setSpeed(1.75f);
            setSpeedViewBackground(this.speed175, this.speed75, this.speed10, this.speed125, this.speed15, this.speed05, this.speed20);
        } else if (id == R.id.speed20) {
            this.mediaInterface.setSpeed(2.0f);
            setSpeedViewBackground(this.speed20, this.speed75, this.speed10, this.speed125, this.speed15, this.speed175, this.speed05);
        } else if (id == R.id.play_back_speed_close) {
            this.speed_layout.setVisibility(8);
        } else if (id == R.id.video_screenshot) {
            this.onVideoCompleteListener.onTakescreenShot();
        } else if (id == R.id.app_video_n_skip) {
            this.mediaInterface.seekTo(this.mediaInterface.getCurrentPosition() + WorkRequest.MIN_BACKOFF_MILLIS);
        } else if (id == R.id.app_video_p_skip) {
            if (this.mediaInterface.getCurrentPosition() > WorkRequest.MIN_BACKOFF_MILLIS) {
                this.mediaInterface.seekTo(this.mediaInterface.getCurrentPosition() - WorkRequest.MIN_BACKOFF_MILLIS);
            }
        } else if (id == R.id.video_list_close) {
            this.videoplaylist_recycler.setVisibility(8);
            this.video_playlist_close.setVisibility(8);
        } else if (id == R.id.video_playlist) {
            if (this.speed_layout.getVisibility() == 0) {
                this.speed_layout.setVisibility(8);
            }
            this.videoplaylist_recycler.setVisibility(0);
            this.video_playlist_close.setVisibility(0);
            this.rlAds.setVisibility(8);
        }
    }

    public void setSpeedViewBackground(View view, View view2, View view3, View view4, View view5, View view6, View view7) {
        view.setBackgroundResource(R.drawable.jz_retry);
        this.state = 5;
        updateStartImage();
        view2.setBackgroundResource(0);
        view3.setBackgroundResource(0);
        view4.setBackgroundResource(0);
        view5.setBackgroundResource(0);
        view6.setBackgroundResource(0);
        view7.setBackgroundResource(0);
    }

    public boolean IsLoopingVideo() {
        return this.isLooping;
    }

    @SuppressLint("WrongConstant")
    public void clickRetryBtn() {
        if (this.dataSource.urlsMap.isEmpty() || this.dataSource.getCurrentUrl() == null) {
            Toast.makeText(this.jzvdContext, getResources().getString(R.string.no_url), 0).show();
        } else if (this.dataSource.getCurrentUrl().toString().startsWith("file") || this.dataSource.getCurrentUrl().toString().startsWith("/") || Utils.isWifiConnected(this.jzvdContext) || WIFI_TIP_DIALOG_SHOWED) {
            this.seekToInAdvance = this.mCurrentPosition;
            startVideo();
        } else {
            showWifiDialog();
        }
    }

    public void clickClarity() {
        onCLickUiToggleToClear();
        @SuppressLint("WrongConstant") final LinearLayout linearLayout = (LinearLayout) ((LayoutInflater)
                this.jzvdContext.getSystemService("layout_inflater")).inflate(R.layout.jz_layout_clarity, (ViewGroup) null);
        OnClickListener r2 = new OnClickListener() {
            public final void onClick(View view) {
                dataSource.currentUrlIndex = ((Integer) view.getTag()).intValue();
                changeUrl(dataSource, getCurrentPositionWhenPlaying());
                clarity.setText(dataSource.getCurrentKey().toString());
                for (int i = 0; i < linearLayout.getChildCount(); i++) {
                    if (i == dataSource.currentUrlIndex) {
                        ((TextView) linearLayout.getChildAt(i)).setTextColor(Color.parseColor("#fff85959"));
                    } else {
                        ((TextView) linearLayout.getChildAt(i)).setTextColor(Color.parseColor("#ffffff"));
                    }
                }
                PopupWindow popupWindow = clarityPopWindow;
                if (popupWindow != null) {
                    popupWindow.dismiss();
                }
            }
        };
        for (int i = 0; i < this.dataSource.urlsMap.size(); i++) {
            String keyFromDataSource = this.dataSource.getKeyFromDataSource(i);
            TextView textView = (TextView) View.inflate(this.jzvdContext, R.layout.jz_layout_clarity_item, null);
            textView.setText(keyFromDataSource);
            textView.setTag(Integer.valueOf(i));
            linearLayout.addView(textView, i);
            textView.setOnClickListener(r2);
            if (i == this.dataSource.currentUrlIndex) {
                textView.setTextColor(Color.parseColor("#fff85959"));
            }
        }
        PopupWindow popupWindow = new PopupWindow((View) linearLayout, Utils.dip2px(this.jzvdContext, 240.0f), -1, true);
        this.clarityPopWindow = popupWindow;
        popupWindow.setContentView(linearLayout);
        this.clarityPopWindow.setAnimationStyle(R.style.pop_animation);
        this.clarityPopWindow.showAtLocation(this.textureViewContainer, GravityCompat.END, 0, 0);
    }

    public void lambda$clickClarity$1$JzvdStd(LinearLayout linearLayout, View view) {
        this.dataSource.currentUrlIndex = ((Integer) view.getTag()).intValue();
        changeUrl(this.dataSource, getCurrentPositionWhenPlaying());
        this.clarity.setText(this.dataSource.getCurrentKey().toString());
        for (int i = 0; i < linearLayout.getChildCount(); i++) {
            if (i == this.dataSource.currentUrlIndex) {
                ((TextView) linearLayout.getChildAt(i)).setTextColor(Color.parseColor("#fff85959"));
            } else {
                ((TextView) linearLayout.getChildAt(i)).setTextColor(Color.parseColor("#ffffff"));
            }
        }
        PopupWindow popupWindow = this.clarityPopWindow;
        if (popupWindow != null) {
            popupWindow.dismiss();
        }
    }

    public void clickBackTiny() {
        clearFloatScreen();
    }

    public void clickBack() {
        this.onVideoCompleteListener.onBackClick();
    }

    public void clickSurfaceContainer() {
        startDismissControlViewTimer();
    }

    @SuppressLint("WrongConstant")
    public void clickPoster() {
        if (this.dataSource == null || this.dataSource.urlsMap.isEmpty() || this.dataSource.getCurrentUrl() == null) {
            Toast.makeText(this.jzvdContext, getResources().getString(R.string.no_url), 0).show();
        } else if (this.state == 0) {
            if (this.dataSource.getCurrentUrl().toString().startsWith("file") || this.dataSource.getCurrentUrl().toString().startsWith("/") || Utils.isWifiConnected(this.jzvdContext) || WIFI_TIP_DIALOG_SHOWED) {
                startVideo();
            } else {
                showWifiDialog();
            }
        } else if (this.state == 7) {
            onClickUiToggle();
        }
    }

    @SuppressLint("WrongConstant")
    @Override
    public void setScreenNormal() {
        super.setScreenNormal();
        this.app_video_lock.setVisibility(0);
        this.fullscreenButton.setImageResource(R.drawable.jz_enlarge);
        this.backButton.setVisibility(0);
        this.tinyBackImageView.setVisibility(4);
        changeStartButtonSize((int) getResources().getDimension(R.dimen.jz_start_button_w_h_normal));
        this.batteryTimeLayout.setVisibility(8);
        this.clarity.setVisibility(8);
    }

    @SuppressLint("WrongConstant")
    @Override
    public void setScreenFullscreen() {
        super.setScreenFullscreen();
        this.app_video_lock.setImageResource(R.drawable.ic_lock_open);
        this.app_video_lock.setVisibility(0);
        this.fullscreenButton.setImageResource(R.drawable.jz_enlarge);
        this.backButton.setVisibility(0);
        this.tinyBackImageView.setVisibility(4);
        if (this.dataSource.urlsMap.size() == 1) {
            this.clarity.setVisibility(8);
        } else {
            this.clarity.setText(this.dataSource.getCurrentKey().toString());
            this.clarity.setVisibility(0);
        }
        changeStartButtonSize((int) getResources().getDimension(R.dimen.jz_start_button_w_h_fullscreen));
        setSystemTimeAndBattery();
    }

    @SuppressLint("WrongConstant")
    @Override
    public void setScreenTiny() {
        super.setScreenTiny();
        this.tinyBackImageView.setVisibility(0);
        setAllControlsVisiblity(4, 8, 4, 4, 4, 4, 4);
        this.batteryTimeLayout.setVisibility(8);
        this.clarity.setVisibility(8);
    }

    @Override
    public void showWifiDialog() {
        super.showWifiDialog();
        AlertDialog.Builder builder = new AlertDialog.Builder(this.jzvdContext);
        builder.setMessage(getResources().getString(R.string.tips_not_wifi));
        builder.setPositiveButton(getResources().getString(R.string.tips_not_wifi_confirm), new DialogInterface.OnClickListener() {
            public final void onClick(DialogInterface dialogInterface, int i) {
                VideoStanderd.this.lambda$showWifiDialog$2$JzvdStd(dialogInterface, i);
            }
        });
        builder.setNegativeButton(getResources().getString(R.string.tips_not_wifi_cancel), new DialogInterface.OnClickListener() {
            public final void onClick(DialogInterface dialogInterface, int i) {
                VideoStanderd.this.lambda$showWifiDialog$3$JzvdStd(dialogInterface, i);
            }
        });
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            public void onCancel(DialogInterface dialogInterface) {
                dialogInterface.dismiss();
                Video.releaseAllVideos();
                VideoStanderd.this.clearFloatScreen();
            }
        });
        builder.create().show();
    }

    public void lambda$showWifiDialog$2$JzvdStd(DialogInterface dialogInterface, int i) {
        dialogInterface.dismiss();
        WIFI_TIP_DIALOG_SHOWED = true;
        if (this.state == 6) {
            this.startButton.performClick();
        } else {
            startVideo();
        }
    }

    public void lambda$showWifiDialog$3$JzvdStd(DialogInterface dialogInterface, int i) {
        dialogInterface.dismiss();
        releaseAllVideos();
        clearFloatScreen();
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        super.onStartTrackingTouch(seekBar);
        cancelDismissControlViewTimer();
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        super.onStopTrackingTouch(seekBar);
        startDismissControlViewTimer();
    }

    @SuppressLint("WrongConstant")
    public void onClickUiToggle() {
        if (this.bottomContainer.getVisibility() != 0) {
            setSystemTimeAndBattery();
            this.clarity.setText(this.dataSource.getCurrentKey().toString());
        }
        if ((this.screen == 1 || this.screen == 0) && this.isLockScreen) {
            this.app_video_unlock.setVisibility(0);
        } else if (this.state == 1) {
            changeUiToPreparing();
            if (this.bottomContainer.getVisibility() != 0) {
                setSystemTimeAndBattery();
            }
        } else if (this.state == 5) {
            if (this.bottomContainer.getVisibility() == 0) {
                changeUiToPlayingClear();
            } else {
                changeUiToPlayingShow();
            }
        } else if (this.state == 6) {
            if (this.bottomContainer.getVisibility() == 0) {
                changeUiToPauseClear();
            } else {
                changeUiToPauseShow();
            }
        }
    }

    public void setSystemTimeAndBattery() {
        this.videoCurrentTime.setText(new SimpleDateFormat("HH:mm").format(new Date()));
        if (System.currentTimeMillis() - LAST_GET_BATTERYLEVEL_TIME > WorkRequest.DEFAULT_BACKOFF_DELAY_MILLIS) {
            LAST_GET_BATTERYLEVEL_TIME = System.currentTimeMillis();
            this.jzvdContext.registerReceiver(this.battertReceiver, new IntentFilter("android.intent.action.BATTERY_CHANGED"));
            return;
        }
        setBatteryLevel();
    }

    public void setBatteryLevel() {
        int i = LAST_GET_BATTERYLEVEL_PERCENT;
        if (i < 15) {
            this.batteryLevel.setBackgroundResource(R.drawable.jz_battery_level_10);
        } else if (i >= 15 && i < 40) {
            this.batteryLevel.setBackgroundResource(R.drawable.jz_battery_level_30);
        } else if (i >= 40 && i < 60) {
            this.batteryLevel.setBackgroundResource(R.drawable.jz_battery_level_50);
        } else if (i >= 60 && i < 80) {
            this.batteryLevel.setBackgroundResource(R.drawable.jz_battery_level_70);
        } else if (i >= 80 && i < 95) {
            this.batteryLevel.setBackgroundResource(R.drawable.jz_battery_level_90);
        } else if (i >= 95 && i <= 100) {
            this.batteryLevel.setBackgroundResource(R.drawable.jz_battery_level_100);
        }
    }

    @SuppressLint("WrongConstant")
    public void onCLickUiToggleToClear() {
        if (this.state == 1) {
            if (this.bottomContainer.getVisibility() == 0) {
                changeUiToPreparing();
            }
        } else if (this.state == 5) {
            if (this.bottomContainer.getVisibility() == 0) {
                changeUiToPlayingClear();
            }
        } else if (this.state == 6) {
            if (this.bottomContainer.getVisibility() == 0) {
                changeUiToPauseClear();
            }
        } else if (this.state == 7 && this.bottomContainer.getVisibility() == 0) {
            changeUiToComplete();
        }
    }

    @Override
    public void onProgress(int i, long j, long j2) {
        super.onProgress(i, j, j2);
    }

    @Override
    public void setBufferProgress(int i) {
        super.setBufferProgress(i);
    }

    @Override
    public void resetProgressAndTime() {
        super.resetProgressAndTime();
    }

    public void changeUiToNormal() {
        int i = this.screen;
        if (i == 0 || i == 1) {
            setAllControlsVisiblity(0, 8, 0, 4, 0, 4, 4);
            updateStartImage();
        }
    }

    public void changeUiToPreparing() {
        int i = this.screen;
        if (i == 0 || i == 1) {
            setAllControlsVisiblity(4, 8, 4, 0, 0, 4, 4);
            updateStartImage();
        }
    }

    public void changeUIToPreparingPlaying() {
        int i = this.screen;
        if (i == 0 || i == 1) {
            setAllControlsVisiblity(0, 0, 4, 0, 4, 4, 4);
            updateStartImage();
        }
    }

    public void changeUIToPreparingChangeUrl() {
        int i = this.screen;
        if (i == 0 || i == 1) {
            setAllControlsVisiblity(4, 8, 4, 0, 0, 4, 4);
            updateStartImage();
        }
    }

    @SuppressLint("WrongConstant")
    public void changeUiToPlayingShow() {
        if (this.screen != 1) {
            int i = this.screen;
            if (i == 0 || i == 1) {
                setAllControlsVisiblity(0, 0, 0, 4, 4, 4, 4);
                updateStartImage();
            }
        } else if (this.isLockScreen) {
            this.topContainer.setVisibility(8);
            this.centerContainer.setVisibility(8);
            this.bottomContainer.setVisibility(8);
            this.startButton.setVisibility(8);
            this.app_video_n_skip.setVisibility(8);
            this.app_video_p_skip.setVisibility(8);
        } else {
            this.topContainer.setVisibility(0);
            this.centerContainer.setVisibility(0);
            this.bottomContainer.setVisibility(0);
            this.startButton.setVisibility(0);
            this.app_video_n_skip.setVisibility(0);
            this.app_video_p_skip.setVisibility(0);
        }
    }

    public void changeUiToPlayingClear() {
        int i = this.screen;
        if (i == 0 || i == 1) {
            setAllControlsVisiblity(4, 8, 4, 4, 4, 0, 4);
        }
    }

    @SuppressLint("WrongConstant")
    public void changeUiToPauseShow() {
        if (this.isLockScreen) {
            this.bottomContainer.setVisibility(8);
            this.topContainer.setVisibility(8);
            this.centerContainer.setVisibility(8);
            this.startButton.setVisibility(8);
            this.app_video_n_skip.setVisibility(8);
            this.app_video_p_skip.setVisibility(8);
            return;
        }
        int i = this.screen;
        if (i == 0 || i == 1) {
            setAllControlsVisiblity(0, 0, 0, 4, 4, 4, 4);
            updateStartImage();
        }
    }

    public void changeUiToPauseClear() {
        int i = this.screen;
        if (i == 0 || i == 1) {
            setAllControlsVisiblity(4, 8, 4, 4, 4, 0, 4);
        }
    }

    public void changeUiToComplete() {
        clickBack();
    }

    public void changeUiToError() {
        int i = this.screen;
        if (i == 0) {
            setAllControlsVisiblity(4, 8, 0, 4, 4, 4, 0);
            updateStartImage();
        } else if (i == 1) {
            setAllControlsVisiblity(0, 8, 0, 4, 4, 4, 0);
            updateStartImage();
        }
    }

    public void setAllControlsVisiblity(int i, int i2, int i3, int i4, int i5, int i6, int i7) {
        this.topContainer.setVisibility(i);
        this.centerContainer.setVisibility(i2);
        this.bottomContainer.setVisibility(i2);
        this.startButton.setVisibility(i3);
        this.app_video_n_skip.setVisibility(i3);
        this.app_video_p_skip.setVisibility(i3);
        this.loadingProgressBar.setVisibility(i4);
        this.posterImageView.setVisibility(i5);
        this.mRetryLayout.setVisibility(i7);
    }

    @SuppressLint("WrongConstant")
    public void updateStartImage() {
        if (this.state == 5) {
            this.startButton.setVisibility(0);
            this.app_video_n_skip.setVisibility(0);
            this.app_video_p_skip.setVisibility(0);
            this.app_video_play.setImageResource(R.drawable.hplib_ic_pause);
            this.startButton.setImageResource(R.drawable.jz_click_pause_selector);
            this.replayTextView.setVisibility(8);
        } else if (this.state == 8) {
            this.startButton.setVisibility(4);
            this.app_video_n_skip.setVisibility(4);
            this.app_video_p_skip.setVisibility(4);
            this.replayTextView.setVisibility(8);
        } else if (this.state == 7) {
            this.startButton.setVisibility(0);
            this.app_video_n_skip.setVisibility(0);
            this.app_video_p_skip.setVisibility(0);
            this.startButton.setImageResource(R.drawable.jz_click_replay_selector);
            this.replayTextView.setVisibility(0);
        } else {
            this.startButton.setImageResource(R.drawable.jz_click_play_selector);
            this.app_video_play.setImageResource(R.drawable.hplib_ic_play_download);
            this.replayTextView.setVisibility(8);
        }
    }

    @Override
    public void showProgressDialog(float f, String str, long j, String str2, long j2) {
        super.showProgressDialog(f, str, j, str2, j2);
        if (this.mProgressDialog == null) {
            View inflate = LayoutInflater.from(this.jzvdContext).inflate(R.layout.jz_dialog_progress, (ViewGroup) null);
            this.mDialogProgressBar = (ProgressBar) inflate.findViewById(R.id.duration_progressbar);
            this.mDialogSeekTime = (TextView) inflate.findViewById(R.id.tv_current);
            this.mDialogTotalTime = (TextView) inflate.findViewById(R.id.tv_duration);
            this.mDialogIcon = (ImageView) inflate.findViewById(R.id.duration_image_tip);
            this.mProgressDialog = createDialogWithView(inflate);
        }
        if (!this.mProgressDialog.isShowing()) {
            this.mProgressDialog.show();
        }
        this.mDialogSeekTime.setText(str);
        TextView textView = this.mDialogTotalTime;
        textView.setText(" / " + str2);
        this.mDialogProgressBar.setProgress(j2 <= 0 ? 0 : (int) ((j * 100) / j2));
        if (f > 0.0f) {
            this.mDialogIcon.setBackgroundResource(R.drawable.jz_forward_icon);
        } else {
            this.mDialogIcon.setBackgroundResource(R.drawable.jz_backward_icon);
        }
        onCLickUiToggleToClear();
    }

    @Override
    public void dismissProgressDialog() {
        super.dismissProgressDialog();
        Dialog dialog = this.mProgressDialog;
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    @Override
    public void showVolumeDialog(float f, int i) {
        super.showVolumeDialog(f, i);
        if (this.mVolumeDialog == null) {
            View inflate = LayoutInflater.from(this.jzvdContext).inflate(R.layout.jz_dialog_volume, (ViewGroup) null);
            this.mDialogVolumeImageView = (ImageView) inflate.findViewById(R.id.volume_image_tip);
            this.mDialogVolumeTextView = (TextView) inflate.findViewById(R.id.tv_volume);
            this.mDialogVolumeProgressBar = (ProgressBar) inflate.findViewById(R.id.volume_progressbar);
            this.mVolumeDialog = createDialogWithViewL(inflate);
        }
        if (!this.mVolumeDialog.isShowing()) {
            this.mVolumeDialog.show();
        }
        if (i <= 0) {
            this.mDialogVolumeImageView.setImageResource(R.drawable.jz_close_volume);
        } else {
            this.mDialogVolumeImageView.setImageResource(R.drawable.jz_add_volume);
        }
        if (i > 100) {
            i = 100;
        } else if (i < 0) {
            i = 0;
        }
        TextView textView = this.mDialogVolumeTextView;
        textView.setText(i + "%");
        this.mDialogVolumeProgressBar.setProgress(i);
        onCLickUiToggleToClear();
    }

    @Override
    public void dismissVolumeDialog() {
        super.dismissVolumeDialog();
        Dialog dialog = this.mVolumeDialog;
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    @Override
    public void showBrightnessDialog(int i) {
        super.showBrightnessDialog(i);
        if (this.mBrightnessDialog == null) {
            View inflate = LayoutInflater.from(this.jzvdContext).inflate(R.layout.jz_dialog_brightness, (ViewGroup) null);
            this.mDialogBrightnessTextView = (TextView) inflate.findViewById(R.id.tv_brightness);
            this.mDialogBrightnessProgressBar = (ProgressBar) inflate.findViewById(R.id.brightness_progressbar);
            this.mBrightnessDialog = createDialogWithViewR(inflate);
        }
        if (!this.mBrightnessDialog.isShowing()) {
            this.mBrightnessDialog.show();
        }
        if (i > 100) {
            i = 100;
        } else if (i < 0) {
            i = 0;
        }
        TextView textView = this.mDialogBrightnessTextView;
        textView.setText(i + "%");
        this.mDialogBrightnessProgressBar.setProgress(i);
        onCLickUiToggleToClear();
    }

    @Override
    public void dismissBrightnessDialog() {
        super.dismissBrightnessDialog();
        Dialog dialog = this.mBrightnessDialog;
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    public Dialog createDialogWithView(View view) {
        Dialog dialog = new Dialog(this.jzvdContext, R.style.jz_style_dialog_progress);
        dialog.setContentView(view);
        Window window = dialog.getWindow();
        window.addFlags(8);
        window.addFlags(32);
        window.addFlags(16);
        window.setLayout(-2, -2);
        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.gravity = 17;
        window.setAttributes(attributes);
        return dialog;
    }

    public Dialog createDialogWithViewL(View view) {
        Dialog dialog = new Dialog(this.jzvdContext, R.style.jz_style_dialog_progress);
        dialog.setContentView(view);
        Window window = dialog.getWindow();
        window.addFlags(8);
        window.addFlags(32);
        window.addFlags(16);
        window.setLayout(-2, -2);
        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.gravity = 3;
        window.setAttributes(attributes);
        return dialog;
    }

    public Dialog createDialogWithViewR(View view) {
        Dialog dialog = new Dialog(this.jzvdContext, R.style.jz_style_dialog_progress);
        dialog.setContentView(view);
        Window window = dialog.getWindow();
        window.addFlags(8);
        window.addFlags(32);
        window.addFlags(16);
        window.setLayout(-2, -2);
        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.gravity = 5;
        window.setAttributes(attributes);
        return dialog;
    }

    public void startDismissControlViewTimer() {
        cancelDismissControlViewTimer();
        DISMISS_CONTROL_VIEW_TIMER = new Timer();
        DismissControlViewTimerTask dismissControlViewTimerTask = new DismissControlViewTimerTask();
        this.mDismissControlViewTimerTask = dismissControlViewTimerTask;
        DISMISS_CONTROL_VIEW_TIMER.schedule(dismissControlViewTimerTask, 2500);
        Log.e("startDismiss", "=======");
    }

    public void cancelDismissControlViewTimer() {
        Timer timer = DISMISS_CONTROL_VIEW_TIMER;
        if (timer != null) {
            timer.cancel();
        }
        DismissControlViewTimerTask dismissControlViewTimerTask = this.mDismissControlViewTimerTask;
        if (dismissControlViewTimerTask != null) {
            dismissControlViewTimerTask.cancel();
        }
    }

    @Override
    public void onCompletion() {
        super.onCompletion();
        cancelDismissControlViewTimer();
    }

    @Override
    public void reset() {
        super.reset();
        cancelDismissControlViewTimer();
        unregisterWifiListener(getApplicationContext());
    }

    public void dissmissControlView(final int i) {
        if (this.state != 0 && this.state != 8 && this.state != 7) {
            post(new Runnable() {
                public final void run() {
                    VideoStanderd.this.lambda$dissmissControlView$4$JzvdStd(i);
                }
            });
        }
    }

    @SuppressLint("WrongConstant")
    public void lambda$dissmissControlView$4$JzvdStd(int i) {
        this.bottomContainer.setVisibility(8);
        this.topContainer.setVisibility(4);
        this.startButton.setVisibility(4);
        this.app_video_n_skip.setVisibility(4);
        this.app_video_p_skip.setVisibility(4);
        this.centerContainer.setVisibility(4);
        if (i == 1) {
            this.speed_layout.setVisibility(8);
            this.videoplaylist_recycler.setVisibility(8);
            this.video_playlist_close.setVisibility(8);
        }
        int i2 = this.screen;
        cancelProgressTimer();
    }

    public void registerWifiListener(Context context) {
        if (context != null) {
            this.mIsWifi = Utils.isWifiConnected(context);
            context.registerReceiver(this.wifiReceiver, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
            this.app_video_play.setImageResource(R.drawable.hplib_ic_pause);
            this.startButton.setImageResource(R.drawable.jz_click_pause_selector);
        }
    }

    public void unregisterWifiListener(Context context) {
        if (context != null) {
            try {
                context.unregisterReceiver(this.wifiReceiver);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        }
    }

    public class DismissControlViewTimerTask extends TimerTask {
        public DismissControlViewTimerTask() {
        }

        public void run() {
            VideoStanderd.this.dissmissControlView(0);
        }
    }
}
