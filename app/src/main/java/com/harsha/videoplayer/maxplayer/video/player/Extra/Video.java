package com.harsha.videoplayer.maxplayer.video.player.Extra;

import android.annotation.SuppressLint;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.media.AudioManager;
import android.os.Build;
import android.provider.Settings;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.solver.widgets.analyzer.BasicMeasure;

import com.harsha.videoplayer.maxplayer.video.player.R;
import com.harsha.videoplayer.maxplayer.video.player.appmanage.ads.AdsBanner;

import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;

public abstract class Video extends FrameLayout implements View.OnClickListener, SeekBar.OnSeekBarChangeListener, View.OnTouchListener {
    public static LinkedList<ViewGroup> CONTAINER_LIST = new LinkedList<>();
    public static int FULLSCREEN_ORIENTATION = 6;
    public static int NORMAL_ORIENTATION = 1;
    public static int ON_PLAY_PAUSE_TMP_STATE = 0;
    public static boolean SAVE_PROGRESS = true;
    public static final int SCREEN_FULLSCREEN = 1;
    public static final int SCREEN_NORMAL = 0;
    public static final int SCREEN_TINY = 2;
    public static final int STATE_AUTO_COMPLETE = 7;
    public static final int STATE_ERROR = 8;
    public static final int STATE_IDLE = -1;
    public static final int STATE_NORMAL = 0;
    public static final int STATE_PAUSE = 6;
    public static final int STATE_PLAYING = 5;
    public static final int STATE_PREPARED = 4;
    public static final int STATE_PREPARING = 1;
    public static final int STATE_PREPARING_CHANGE_URL = 2;
    public static final int STATE_PREPARING_PLAYING = 3;
    public static final String TAG = "JZVD";
    public static final int THRESHOLD = 80;
    public static boolean TOOL_BAR_EXIST = true;
    public static int VIDEO_IMAGE_DISPLAY_TYPE = 0;
    public static final int VIDEO_IMAGE_DISPLAY_TYPE_ADAPTER = 0;
    public static final int VIDEO_IMAGE_DISPLAY_TYPE_FILL_PARENT = 1;
    public static final int VIDEO_IMAGE_DISPLAY_TYPE_FILL_SCROP = 2;
    public static final int VIDEO_IMAGE_DISPLAY_TYPE_ORIGINAL = 3;
    public static boolean WIFI_TIP_DIALOG_SHOWED = false;
    public static int backUpBufferState = -1;
    public static Video currentVd = null;
    public static long lastAutoFullscreenTime = 0;
    public static AudioManager.OnAudioFocusChangeListener onAudioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {
        public void onAudioFocusChange(int i) {
            if (i == -2) {
                try {
                    Video vd = Video.currentVd;
                    if (vd != null && vd.state == 5) {
                        vd.startButton.performClick();
                        vd.app_video_play.performClick();
                    }
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                }
                Log.d("JZVD", "AUDIOFOCUS_LOSS_TRANSIENT [" + hashCode() + "]");
            } else if (i == -1) {
                Video.releaseAllVideos();
                Log.d("JZVD", "AUDIOFOCUS_LOSS [" + hashCode() + "]");
            }
        }
    };
    protected Timer UPDATE_PROGRESS_TIMER;
    public ImageView app_video_n_skip;
    public ImageView app_video_p_skip;
    public ImageView app_video_play;
    protected int aspectRatio = 0;
    protected int blockHeight;
    protected int blockIndex;
    protected ViewGroup.LayoutParams blockLayoutParams;
    protected int blockWidth;
    public ViewGroup bottomContainer;
    public ViewGroup centerContainer;
    protected int currentSpeedIndex = 2;
    public TextView currentTimeTextView;
    public DataSource dataSource;
    public ImageView fullscreenButton;
    protected long gobakFullscreenTime = 0;
    protected long gotoFullscreenTime = 0;
    public int heightRatio = 0;
    protected boolean isBackgroundEnable = false;
    protected boolean isLockScreen = false;
    protected boolean isLooping = false;
    protected boolean isMute = false;
    protected boolean isShowMoreOption = false;
    protected boolean isShuffle = false;
    protected boolean isSpeed = false;
    protected Context jzvdContext;
    protected AudioManager mAudioManager;
    protected boolean mChangeBrightness;
    protected boolean mChangePosition;
    protected boolean mChangeVolume;
    protected long mCurrentPosition;
    protected float mDownX;
    protected float mDownY;
    protected float mGestureDownBrightness;
    protected long mGestureDownPosition;
    protected int mGestureDownVolume;
    protected ProgressTimerTask mProgressTimerTask;
    protected int mScreenHeight;
    protected int mScreenWidth;
    protected long mSeekTimePosition;
    protected boolean mTouchingProgressBar;
    public MediaInterface mediaInterface;
    public Class mediaInterfaceClass;
    public int positionInList = -1;
    public boolean preloading = false;
    public SeekBar progressBar;
    RelativeLayout rlAds;
    public int screen = -1;
    public long seekToInAdvance = 0;
    public int seekToManulPosition = -1;
    public ImageView startButton;
    public int state = -1;
    public TextureView1 textureView;
    public ViewGroup textureViewContainer;
    public ViewGroup topContainer;
    public TextView totalTimeTextView;
    public int videoRotation = 0;
    public int widthRatio = 0;

    public void dismissBrightnessDialog() {
    }

    public void dismissProgressDialog() {
    }

    public void dismissVolumeDialog() {
    }

    public abstract int getLayoutId();

    public void onSeekComplete() {
    }

    public void showBrightnessDialog(int i) {
    }

    public void showProgressDialog(float f, String str, long j, String str2, long j2) {
    }

    public void showVolumeDialog(float f, int i) {
    }

    public void showWifiDialog() {
    }

    public Video(Context context) {
        super(context);
        init(context);
    }

    public Video(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context);
    }

    public static void goOnPlayOnResume() {
        Video vd = currentVd;
        if (vd != null) {
            int i = vd.state;
            if (i == 6) {
                if (ON_PLAY_PAUSE_TMP_STATE == 6) {
                    vd.onStatePause();
                    currentVd.mediaInterface.pause();
                } else {
                    vd.onStatePlaying();
                    currentVd.mediaInterface.start();
                }
                ON_PLAY_PAUSE_TMP_STATE = 0;
            } else if (i == 1) {
                vd.startVideo();
            }
            Video vd2 = currentVd;
            if (vd2.screen == 1) {
                Utils.hideStatusBar(vd2.jzvdContext);
                Utils.hideSystemUI(currentVd.jzvdContext);
            }
        }
    }

    public static void goOnPlayOnPause() {
        Video vd = currentVd;
        if (vd != null) {
            int i = vd.state;
            if (i == 7 || i == 0 || i == 8) {
                releaseAllVideos();
            } else if (i == 1) {
                setCurrentVd(vd);
                currentVd.state = 1;
            } else {
                ON_PLAY_PAUSE_TMP_STATE = i;
                vd.onStatePause();
                currentVd.mediaInterface.pause();
            }
        }
    }

    public static void startFullscreenDirectly(Context context, Class cls, String str, String str2) {
        startFullscreenDirectly(context, cls, new DataSource(str, str2));
    }

    public static void startFullscreenDirectly(Context context, Class cls, DataSource dataSource2) {
        Utils.hideStatusBar(context);
        Utils.setRequestedOrientation(context, FULLSCREEN_ORIENTATION);
        Utils.hideSystemUI(context);
        ViewGroup viewGroup = (ViewGroup) Utils.scanForActivity(context).getWindow().getDecorView();
        try {
            Video vd = (Video) cls.getConstructor(Context.class).newInstance(context);
            viewGroup.addView(vd, new LayoutParams(-1, -1));
            vd.setUp(dataSource2, 1);
            vd.startVideo();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    public static void releaseAllVideos() {
        Log.d("JZVD", "releaseAllVideos");
        Video vd = currentVd;
        if (vd != null) {
            vd.reset();
            currentVd = null;
        }
    }

    public static void setCurrentVd(Video vd) {
        Video vd2 = currentVd;
        if (vd2 != null) {
            vd2.reset();
        }
        currentVd = vd;
    }

    public static void setTextureViewRotation(int i) {
        TextureView1 textureView1;
        Video vd = currentVd;
        if (vd != null && (textureView1 = vd.textureView) != null) {
            textureView1.setRotation((float) i);
        }
    }

    public static void setVideoImageDisplayType(int i) {
        TextureView1 textureView1;
        VIDEO_IMAGE_DISPLAY_TYPE = i;
        Video vd = currentVd;
        if (vd != null && (textureView1 = vd.textureView) != null) {
            textureView1.requestLayout();
        }
    }

    public void init(Context context) {
        View.inflate(context, getLayoutId(), this);
        this.jzvdContext = context;
        this.rlAds = (RelativeLayout) findViewById(R.id.rl_ad);
        AdsBanner adsBanner = new AdsBanner(getContext(), this.rlAds, (RelativeLayout) findViewById(R.id.rl_layout));
        adsBanner.setIsFull();
        adsBanner.loadBannerAd();
        this.startButton = (ImageView) findViewById(R.id.start);
        this.app_video_play = (ImageView) findViewById(R.id.app_video_play);
        this.fullscreenButton = (ImageView) findViewById(R.id.fullscreen);
        this.app_video_p_skip = (ImageView) findViewById(R.id.app_video_p_skip);
        this.app_video_n_skip = (ImageView) findViewById(R.id.app_video_n_skip);
        this.progressBar = (SeekBar) findViewById(R.id.bottom_seek_progress);
        this.currentTimeTextView = (TextView) findViewById(R.id.current);
        this.totalTimeTextView = (TextView) findViewById(R.id.total);
        this.bottomContainer = (ViewGroup) findViewById(R.id.layout_bottom);
        this.centerContainer = (ViewGroup) findViewById(R.id.layout_center);
        this.textureViewContainer = (ViewGroup) findViewById(R.id.surface_container);
        this.topContainer = (ViewGroup) findViewById(R.id.layout_top);
        if (this.startButton == null) {
            this.startButton = new ImageView(context);
        }
        if (this.fullscreenButton == null) {
            this.fullscreenButton = new ImageView(context);
        }
        if (this.progressBar == null) {
            this.progressBar = new SeekBar(context);
        }
        if (this.currentTimeTextView == null) {
            this.currentTimeTextView = new TextView(context);
        }
        if (this.totalTimeTextView == null) {
            this.totalTimeTextView = new TextView(context);
        }
        if (this.bottomContainer == null) {
            this.bottomContainer = new LinearLayout(context);
        }
        if (this.textureViewContainer == null) {
            this.textureViewContainer = new FrameLayout(context);
        }
        if (this.topContainer == null) {
            this.topContainer = new RelativeLayout(context);
        }
        if (this.centerContainer == null) {
            this.centerContainer = new RelativeLayout(context);
        }
        this.startButton.setOnClickListener(this);
        this.app_video_play.setOnClickListener(this);
        this.fullscreenButton.setOnClickListener(this);
        this.app_video_n_skip.setOnClickListener(this);
        this.app_video_p_skip.setOnClickListener(this);
        this.progressBar.setOnSeekBarChangeListener(this);
        this.bottomContainer.setOnClickListener(this);
        this.textureViewContainer.setOnClickListener(this);
        this.textureViewContainer.setOnTouchListener(this);
        this.mScreenWidth = getContext().getResources().getDisplayMetrics().widthPixels;
        this.mScreenHeight = getContext().getResources().getDisplayMetrics().heightPixels;
        this.state = -1;
    }

    public void setUp(String str, String str2) {
        setUp(new DataSource(str, str2), 0);
    }

    public void setUp(String str, String str2, int i) {
        setUp(new DataSource(str, str2), i);
    }

    public void setUp(DataSource dataSource2, int i) {
        setUp(dataSource2, i, MediaSystem.class);
    }

    public void setUp(String str, String str2, int i, Class cls) {
        setUp(new DataSource(str, str2), i, cls);
    }

    public void setUp(DataSource dataSource2, int i, Class cls) {
        this.dataSource = dataSource2;
        this.screen = i;
        onStateNormal();
        this.mediaInterfaceClass = cls;
    }

    public void setMediaInterface(Class cls) {
        reset();
        this.mediaInterfaceClass = cls;
    }

    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.start || id == R.id.app_video_play) {
            clickStart();
        } else if (id == R.id.fullscreen) {
            clickFullscreen();
        }
    }

    @SuppressLint("WrongConstant")
    public boolean backPress() {
        Video vd;
        Video vd2;
        Log.i("JZVD", "backPress");
        if (CONTAINER_LIST.size() != 0 && (vd2 = currentVd) != null) {
            this.rlAds.setVisibility(8);
            vd2.gotoNormalScreen();
            return true;
        } else if (CONTAINER_LIST.size() != 0 || (vd = currentVd) == null || vd.screen == 0) {
            return false;
        } else {
            vd.clearFloatScreen();
            return true;
        }
    }

    public void clickFullscreen() {
        Log.i("JZVD", "onClick fullscreen [" + hashCode() + "] ");
        if (this.state == 7) {
            return;
        }
        if (this.screen == 1) {
            backPress();
        } else {
            gotoFullscreen();
        }
    }

    @SuppressLint("WrongConstant")
    public void clickStart() {
        Log.i("JZVD", "onClick start [" + hashCode() + "] ");
        DataSource dataSource2 = this.dataSource;
        if (dataSource2 == null || dataSource2.urlsMap.isEmpty() || this.dataSource.getCurrentUrl() == null) {
            Toast.makeText(getContext(), getResources().getString(R.string.no_url), 0).show();
            return;
        }
        int i = this.state;
        if (i == 0) {
            if (this.dataSource.getCurrentUrl().toString().startsWith("file") || this.dataSource.getCurrentUrl().toString().startsWith("/") || Utils.isWifiConnected(getContext()) || WIFI_TIP_DIALOG_SHOWED) {
                startVideo();
            } else {
                showWifiDialog();
            }
        } else if (i == 5) {
            Log.e("JZVD", "pauseVideo [" + hashCode() + "] ");
            this.mediaInterface.pause();
            onStatePause();
            if (1 == this.screen) {
                this.startButton.setImageResource(R.drawable.jz_click_play_selector);
                this.app_video_play.setImageResource(R.drawable.hplib_ic_play_download);
                this.rlAds.setVisibility(0);
            }
        } else if (i == 6) {
            Log.e("JZVD", "playVideo [" + hashCode() + "] ");
            this.mediaInterface.start();
            onStatePlaying();
            if (1 == this.screen) {
                this.app_video_play.setImageResource(R.drawable.hplib_ic_pause);
                this.startButton.setImageResource(R.drawable.jz_click_pause_selector);
                this.rlAds.setVisibility(8);
            }
        } else if (i == 7) {
            startVideo();
        }
    }

    public boolean onTouch(View view, MotionEvent motionEvent) {
        float x = motionEvent.getX();
        float y = motionEvent.getY();
        if (view.getId() != R.id.surface_container) {
            return false;
        }
        int action = motionEvent.getAction();
        if (action == 0) {
            touchActionDown(x, y);
            return false;
        } else if (action == 1) {
            touchActionUp();
            return false;
        } else if (action != 2) {
            return false;
        } else {
            touchActionMove(x, y);
            return false;
        }
    }

    public void touchActionUp() {
        Log.i("JZVD", "onTouch surfaceContainer actionUp [" + hashCode() + "] ");
        this.mTouchingProgressBar = false;
        dismissProgressDialog();
        dismissVolumeDialog();
        dismissBrightnessDialog();
        if (this.mChangePosition) {
            this.mediaInterface.seekTo(this.mSeekTimePosition);
            long duration = getDuration();
            long j = this.mSeekTimePosition * 100;
            if (duration == 0) {
                duration = 1;
            }
            this.progressBar.setProgress((int) (j / duration));
        }
        startProgressTimer();
    }

    public void touchActionMove(float f, float f2) {
        Log.i("JZVD", "onTouch surfaceContainer actionMove [" + hashCode() + "] ");
        float f3 = f - this.mDownX;
        float f4 = f2 - this.mDownY;
        float abs = Math.abs(f3);
        float abs2 = Math.abs(f4);
        int i = this.screen;
        if (i != 1 || this.isLockScreen) {
            if (i == 0 && !this.isLockScreen) {
                if (this.mDownX <= ((float) Utils.getScreenWidth(getContext())) && this.mDownY >= ((float) Utils.getStatusBarHeight(getContext()))) {
                    if (!this.mChangePosition && !this.mChangeVolume && !this.mChangeBrightness && (abs > 80.0f || abs2 > 80.0f)) {
                        cancelProgressTimer();
                        if (abs >= 80.0f) {
                            if (this.state != 8) {
                                this.mChangePosition = true;
                                this.mGestureDownPosition = getCurrentPositionWhenPlaying();
                            }
                        } else if (this.mDownX < ((float) this.mScreenWidth) * 0.5f) {
                            this.mChangeBrightness = true;
                            WindowManager.LayoutParams attributes = Utils.getWindow(getContext()).getAttributes();
                            if (attributes.screenBrightness < 0.0f) {
                                try {
                                    this.mGestureDownBrightness = (float) Settings.System.getInt(getContext().getContentResolver(), "screen_brightness");
                                    Log.i("JZVD", "current system brightness: " + this.mGestureDownBrightness);
                                } catch (Settings.SettingNotFoundException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                this.mGestureDownBrightness = attributes.screenBrightness * 255.0f;
                                Log.i("JZVD", "current activity brightness: " + this.mGestureDownBrightness);
                            }
                        } else {
                            this.mChangeVolume = true;
                            this.mGestureDownVolume = this.mAudioManager.getStreamVolume(3);
                        }
                    }
                } else {
                    return;
                }
            }
        } else if (this.mDownX <= ((float) Utils.getScreenWidth(getContext())) && this.mDownY >= ((float) Utils.getStatusBarHeight(getContext()))) {
            if (!this.mChangePosition && !this.mChangeVolume && !this.mChangeBrightness && (abs > 80.0f || abs2 > 80.0f)) {
                cancelProgressTimer();
                if (abs >= 80.0f) {
                    if (this.state != 8) {
                        this.mChangePosition = true;
                        this.mGestureDownPosition = getCurrentPositionWhenPlaying();
                    }
                } else if (this.mDownX < ((float) this.mScreenHeight) * 0.5f) {
                    this.mChangeBrightness = true;
                    WindowManager.LayoutParams attributes2 = Utils.getWindow(getContext()).getAttributes();
                    if (attributes2.screenBrightness < 0.0f) {
                        try {
                            this.mGestureDownBrightness = (float) Settings.System.getInt(getContext().getContentResolver(), "screen_brightness");
                            Log.i("JZVD", "current system brightness: " + this.mGestureDownBrightness);
                        } catch (Settings.SettingNotFoundException e2) {
                            e2.printStackTrace();
                        }
                    } else {
                        this.mGestureDownBrightness = attributes2.screenBrightness * 255.0f;
                        Log.i("JZVD", "current activity brightness: " + this.mGestureDownBrightness);
                    }
                } else {
                    this.mChangeVolume = true;
                    this.mGestureDownVolume = this.mAudioManager.getStreamVolume(3);
                }
            }
        } else {
            return;
        }
        if (this.mChangePosition) {
            long duration = getDuration();
            long j = (long) ((int) (((float) this.mGestureDownPosition) + ((((float) duration) * f3) / ((float) this.mScreenWidth))));
            this.mSeekTimePosition = j;
            if (j > duration) {
                this.mSeekTimePosition = duration;
            }
            showProgressDialog(f3, Utils.stringForTime(this.mSeekTimePosition), this.mSeekTimePosition, Utils.stringForTime(duration), duration);
        }
        if (this.mChangeVolume) {
            f4 = -f4;
            int streamMaxVolume = this.mAudioManager.getStreamMaxVolume(3);
            this.mAudioManager.setStreamVolume(3, this.mGestureDownVolume + ((int) (((((float) streamMaxVolume) * f4) * 3.0f) / ((float) this.mScreenHeight))), 0);
            showVolumeDialog(-f4, (int) (((float) ((this.mGestureDownVolume * 100) / streamMaxVolume)) + (((f4 * 3.0f) * 100.0f) / ((float) this.mScreenHeight))));
        }
        if (this.mChangeBrightness) {
            float f5 = -f4;
            WindowManager.LayoutParams attributes3 = Utils.getWindow(getContext()).getAttributes();
            float f6 = (this.mGestureDownBrightness + ((float) ((int) (((f5 * 255.0f) * 3.0f) / ((float) this.mScreenHeight))))) / 255.0f;
            if (f6 >= 1.0f) {
                attributes3.screenBrightness = 1.0f;
            } else if (f6 <= 0.0f) {
                attributes3.screenBrightness = 0.01f;
            } else {
                attributes3.screenBrightness = f6;
            }
            Utils.getWindow(getContext()).setAttributes(attributes3);
            showBrightnessDialog((int) (((this.mGestureDownBrightness * 100.0f) / 255.0f) + (((f5 * 3.0f) * 100.0f) / ((float) this.mScreenHeight))));
        }
    }

    public void touchActionDown(float f, float f2) {
        Log.i("JZVD", "onTouch surfaceContainer actionDown [" + hashCode() + "] ");
        this.mTouchingProgressBar = true;
        this.mDownX = f;
        this.mDownY = f2;
        this.mChangeVolume = false;
        this.mChangePosition = false;
        this.mChangeBrightness = false;
    }

    public void onStateNormal() {
        Log.i("JZVD", "onStateNormal  [" + hashCode() + "] ");
        this.state = 0;
        cancelProgressTimer();
        MediaInterface mediaInterface2 = this.mediaInterface;
        if (mediaInterface2 != null) {
            mediaInterface2.release();
        }
    }

    public void onStatePreparing() {
        Log.i("JZVD", "onStatePreparing  [" + hashCode() + "] ");
        this.state = 1;
        resetProgressAndTime();
    }

    public void onStatePreparingPlaying() {
        Log.i("JZVD", "onStatePreparingPlaying  [" + hashCode() + "] ");
        this.state = 3;
    }

    public void onStatePreparingChangeUrl() {
        Log.i("JZVD", "onStatePreparingChangeUrl  [" + hashCode() + "] ");
        this.state = 2;
        releaseAllVideos();
        startVideo();
    }

    public void changeUrl(DataSource dataSource2, long j) {
        this.dataSource = dataSource2;
        this.seekToInAdvance = j;
        onStatePreparingChangeUrl();
    }

    public void onPrepared() {
        Log.i("JZVD", "onPrepared  [" + hashCode() + "] ");
        this.state = 4;
        if (!this.preloading) {
            this.mediaInterface.start();
            this.preloading = false;
        }
        if (this.dataSource.getCurrentUrl().toString().toLowerCase().contains("mp3") || this.dataSource.getCurrentUrl().toString().toLowerCase().contains("wma") || this.dataSource.getCurrentUrl().toString().toLowerCase().contains("aac") || this.dataSource.getCurrentUrl().toString().toLowerCase().contains("m4a") || this.dataSource.getCurrentUrl().toString().toLowerCase().contains("wav")) {
            onStatePlaying();
        }
    }

    public void startPreloading() {
        this.preloading = true;
        startVideo();
    }

    public void startVideoAfterPreloading() {
        if (this.state == 4) {
            this.mediaInterface.start();
            return;
        }
        this.preloading = false;
        startVideo();
    }

    public void onStatePlaying() {
        Log.i("JZVD", "onStatePlaying  [" + hashCode() + "] ");
        if (this.state == 4) {
            long j = this.seekToInAdvance;
            if (j != 0) {
                this.mediaInterface.seekTo(j);
                this.seekToInAdvance = 0;
            } else {
                long savedProgress = Utils.getSavedProgress(getContext(), this.dataSource.getCurrentUrl());
                if (savedProgress != 0) {
                    this.mediaInterface.seekTo(savedProgress);
                }
            }
        }
        this.state = 5;
        startProgressTimer();
    }

    public void onStatePause() {
        Log.i("JZVD", "onStatePause  [" + hashCode() + "] ");
        this.state = 6;
        startProgressTimer();
    }

    public void onStateError() {
        Log.i("JZVD", "onStateError  [" + hashCode() + "] ");
        this.state = 8;
        cancelProgressTimer();
    }

    public void onStateAutoComplete() {
        Log.i("JZVD", "onStateAutoComplete  [" + hashCode() + "] ");
        this.state = 7;
        cancelProgressTimer();
        this.progressBar.setProgress(100);
        this.currentTimeTextView.setText(this.totalTimeTextView.getText());
    }

    public void onInfo(int i, int i2) {
        Log.d("JZVD", "onInfo what - " + i + " Extra - " + i2);
        if (i == 3) {
            Log.d("JZVD", "MEDIA_INFO_VIDEO_RENDERING_START");
            int i3 = this.state;
            if (i3 == 4 || i3 == 2 || i3 == 3) {
                onStatePlaying();
            }
        } else if (i == 701) {
            Log.d("JZVD", "MEDIA_INFO_BUFFERING_START");
            backUpBufferState = this.state;
            setState(3);
        } else if (i == 702) {
            Log.d("JZVD", "MEDIA_INFO_BUFFERING_END");
            int i4 = backUpBufferState;
            if (i4 != -1) {
                setState(i4);
                backUpBufferState = -1;
            }
        }
    }

    public void onError(int i, int i2) {
        Log.e("JZVD", "onError " + i + " - " + i2 + " [" + hashCode() + "] ");
        if (i != 38 && i2 != -38 && i != -38 && i2 != 38 && i2 != -19) {
            onStateError();
            this.mediaInterface.release();
        }
    }

    public void onCompletion() {
        Runtime.getRuntime().gc();
        Log.i("JZVD", "onAutoCompletion  [" + hashCode() + "] ");
        cancelProgressTimer();
        dismissBrightnessDialog();
        dismissProgressDialog();
        dismissVolumeDialog();
        onStateAutoComplete();
        this.mediaInterface.release();
        Utils.scanForActivity(getContext()).getWindow().clearFlags(128);
        Utils.saveProgress(getContext(), this.dataSource.getCurrentUrl(), 0);
        if (this.screen == 1) {
            if (CONTAINER_LIST.size() == 0) {
                clearFloatScreen();
            } else {
                gotoNormalCompletion();
            }
        }
    }

    public void gotoNormalCompletion() {
        this.gobakFullscreenTime = System.currentTimeMillis();
        ((ViewGroup) Utils.scanForActivity(this.jzvdContext).getWindow().getDecorView()).removeView(this);
        this.textureViewContainer.removeView(this.textureView);
        CONTAINER_LIST.getLast().removeViewAt(this.blockIndex);
        CONTAINER_LIST.getLast().addView(this, this.blockIndex, this.blockLayoutParams);
        CONTAINER_LIST.pop();
        setScreenNormal();
        Utils.showStatusBar(this.jzvdContext);
        Utils.setRequestedOrientation(this.jzvdContext, NORMAL_ORIENTATION);
        Utils.showSystemUI(this.jzvdContext);
    }

    @SuppressLint("WrongConstant")
    public void reset() {
        Log.i("JZVD", "reset  [" + hashCode() + "] ");
        int i = this.state;
        if (i == 5 || i == 6) {
            Utils.saveProgress(getContext(), this.dataSource.getCurrentUrl(), getCurrentPositionWhenPlaying());
        }
        cancelProgressTimer();
        dismissBrightnessDialog();
        dismissProgressDialog();
        dismissVolumeDialog();
        onStateNormal();
        this.textureViewContainer.removeAllViews();
        ((AudioManager) getApplicationContext().getSystemService("audio")).abandonAudioFocus(onAudioFocusChangeListener);
        Utils.scanForActivity(getContext()).getWindow().clearFlags(128);
        MediaInterface mediaInterface2 = this.mediaInterface;
        if (mediaInterface2 != null) {
            mediaInterface2.release();
        }
    }

    public void setState(int i) {
        switch (i) {
            case 0:
                onStateNormal();
                return;
            case 1:
                onStatePreparing();
                return;
            case 2:
                onStatePreparingChangeUrl();
                return;
            case 3:
                onStatePreparingPlaying();
                return;
            case 4:
            default:
                return;
            case 5:
                onStatePlaying();
                return;
            case 6:
                onStatePause();
                return;
            case 7:
                onStateAutoComplete();
                return;
            case 8:
                onStateError();
                return;
        }
    }

    public void setScreen(int i) {
        if (i == 0) {
            setScreenNormal();
        } else if (i == 1) {
            setScreenFullscreen();
        } else if (i == 2) {
            setScreenTiny();
        }
    }

    public void startVideo() {
        Log.d("JZVD", "startVideo [" + hashCode() + "] ");
        setCurrentVd(this);
        try {
            this.mediaInterface = (MediaInterface) this.mediaInterfaceClass.getConstructor(Video.class).newInstance(this);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e2) {
            e2.printStackTrace();
        } catch (InstantiationException e3) {
            e3.printStackTrace();
        } catch (InvocationTargetException e4) {
            e4.printStackTrace();
        }
        addTextureView();
        @SuppressLint("WrongConstant")
        AudioManager audioManager = (AudioManager) getApplicationContext().getSystemService("audio");
        this.mAudioManager = audioManager;
        audioManager.requestAudioFocus(onAudioFocusChangeListener, 3, 2);
        Utils.scanForActivity(getContext()).getWindow().addFlags(128);
        onStatePreparing();
        if (1 == this.screen) {
            this.app_video_play.setImageResource(R.drawable.hplib_ic_pause);
            this.startButton.setImageResource(R.drawable.jz_click_pause_selector);
        }
    }

    public void muteAudio() {
        if (Build.VERSION.SDK_INT >= 23) {
            try {
                this.mAudioManager.adjustStreamVolume(4, -100, 0);
                this.mAudioManager.adjustStreamVolume(3, -100, 0);
                this.mAudioManager.adjustStreamVolume(2, -100, 0);
                this.mAudioManager.adjustStreamVolume(1, -100, 0);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
                this.mAudioManager.setStreamMute(5, true);
                this.mAudioManager.setStreamMute(4, true);
                this.mAudioManager.setStreamMute(3, true);
                this.mAudioManager.setStreamMute(2, true);
                this.mAudioManager.setStreamMute(1, true);
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }

    public void unMuteAudio() {
        if (Build.VERSION.SDK_INT >= 23) {
            try {
                this.mAudioManager.adjustStreamVolume(5, 100, 0);
                this.mAudioManager.adjustStreamVolume(4, 100, 0);
                this.mAudioManager.adjustStreamVolume(3, 100, 0);
                this.mAudioManager.adjustStreamVolume(2, 100, 0);
                this.mAudioManager.adjustStreamVolume(1, 100, 0);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
                this.mAudioManager.setStreamMute(5, false);
                this.mAudioManager.setStreamMute(4, false);
                this.mAudioManager.setStreamMute(3, false);
                this.mAudioManager.setStreamMute(2, false);
                this.mAudioManager.setStreamMute(1, false);
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }

    @SuppressLint("WrongConstant")
    public void onMeasure(int i, int i2) {
        int i3 = this.screen;
        if (i3 == 1 || i3 == 2) {
            super.onMeasure(i, i2);
        } else if (this.widthRatio == 0 || this.heightRatio == 0) {
            super.onMeasure(i, i2);
        } else {
            int size = MeasureSpec.getSize(i);
            int i4 = (int) ((((float) size) * ((float) this.heightRatio)) / ((float) this.widthRatio));
            setMeasuredDimension(size, i4);
            getChildAt(0).measure(MeasureSpec.makeMeasureSpec(size, BasicMeasure.EXACTLY),
                    MeasureSpec.makeMeasureSpec(i4, BasicMeasure.EXACTLY));
        }
    }

    public void addTextureView() {
        Log.d("JZVD", "addTextureView [" + hashCode() + "] ");
        TextureView1 textureView1 = this.textureView;
        if (textureView1 != null) {
            this.textureViewContainer.removeView(textureView1);
        }
        TextureView1 textureView12 = new TextureView1(getContext().getApplicationContext());
        this.textureView = textureView12;
        textureView12.setSurfaceTextureListener(this.mediaInterface);
        this.textureViewContainer.addView(this.textureView, new LayoutParams(-1, -1, 17));
    }

    public void clearFloatScreen() {
        Utils.showStatusBar(getContext());
        Utils.setRequestedOrientation(getContext(), NORMAL_ORIENTATION);
        Utils.showSystemUI(getContext());
        ((ViewGroup) Utils.scanForActivity(getContext()).getWindow().getDecorView()).removeView(this);
        MediaInterface mediaInterface2 = this.mediaInterface;
        if (mediaInterface2 != null) {
            mediaInterface2.release();
        }
        currentVd = null;
    }

    public void onVideoSizeChanged(int i, int i2) {
        Log.i("JZVD", "onVideoSizeChanged  [" + hashCode() + "] ");
        TextureView1 textureView1 = this.textureView;
        if (textureView1 != null) {
            int i3 = this.videoRotation;
            if (i3 != 0) {
                textureView1.setRotation((float) i3);
            }
            this.textureView.setVideoSize(i, i2);
        }
    }

    public void startProgressTimer() {
        Log.i("JZVD", "startProgressTimer:  [" + hashCode() + "] ");
        cancelProgressTimer();
        this.UPDATE_PROGRESS_TIMER = new Timer();
        ProgressTimerTask progressTimerTask = new ProgressTimerTask();
        this.mProgressTimerTask = progressTimerTask;
        this.UPDATE_PROGRESS_TIMER.schedule(progressTimerTask, 0, 300);
    }

    public void cancelProgressTimer() {
        Timer timer = this.UPDATE_PROGRESS_TIMER;
        if (timer != null) {
            timer.cancel();
        }
        ProgressTimerTask progressTimerTask = this.mProgressTimerTask;
        if (progressTimerTask != null) {
            progressTimerTask.cancel();
        }
    }

    public void onProgress(int i, long j, long j2) {
        this.mCurrentPosition = j;
        if (!this.mTouchingProgressBar) {
            int i2 = this.seekToManulPosition;
            if (i2 != -1) {
                if (i2 <= i) {
                    this.seekToManulPosition = -1;
                } else {
                    return;
                }
            } else if (i != 0) {
                this.progressBar.setProgress(i);
            }
        }
        if (j != 0) {
            this.currentTimeTextView.setText(Utils.stringForTime(j));
        }
        this.totalTimeTextView.setText(Utils.stringForTime(j2));
    }

    public void setBufferProgress(int i) {
        if (i != 0) {
            this.progressBar.setSecondaryProgress(i);
        }
    }

    public void resetProgressAndTime() {
        this.mCurrentPosition = 0;
        this.progressBar.setProgress(0);
        this.progressBar.setSecondaryProgress(0);
        this.currentTimeTextView.setText(Utils.stringForTime(0));
        this.totalTimeTextView.setText(Utils.stringForTime(0));
    }

    public long getCurrentPositionWhenPlaying() {
        int i = this.state;
        if (i != 5 && i != 6 && i != 3) {
            return 0;
        }
        try {
            return this.mediaInterface.getCurrentPosition();
        } catch (IllegalStateException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public long getCurrentDuration() {
        return this.mediaInterface.getCurrentPosition();
    }

    public void onPause() {
        this.mediaInterface.pause();
    }

    public void setSeekTo(int i) {
        MediaInterface mediaInterface2 = this.mediaInterface;
        if (mediaInterface2 != null) {
            mediaInterface2.seekTo((long) i);
        }
    }

    public void onStart() {
        MediaInterface mediaInterface2 = this.mediaInterface;
        if (mediaInterface2 != null) {
            mediaInterface2.start();
        }
    }

    public long getDuration() {
        try {
            return this.mediaInterface.getDuration();
        } catch (IllegalStateException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public void onStartTrackingTouch(SeekBar seekBar) {
        Log.i("JZVD", "bottomProgress onStartTrackingTouch [" + hashCode() + "] ");
        cancelProgressTimer();
        for (ViewParent parent = getParent(); parent != null; parent = parent.getParent()) {
            parent.requestDisallowInterceptTouchEvent(true);
        }
    }

    public void onStopTrackingTouch(SeekBar seekBar) {
        Log.i("JZVD", "bottomProgress onStopTrackingTouch [" + hashCode() + "] ");
        startProgressTimer();
        for (ViewParent parent = getParent(); parent != null; parent = parent.getParent()) {
            parent.requestDisallowInterceptTouchEvent(false);
        }
        int i = this.state;
        if (i == 5 || i == 6) {
            long progress = (((long) seekBar.getProgress()) * getDuration()) / 100;
            this.seekToManulPosition = seekBar.getProgress();
            this.mediaInterface.seekTo(progress);
            Log.i("JZVD", "seekTo " + progress + " [" + hashCode() + "] ");
        }
    }

    public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
        if (z) {
            this.currentTimeTextView.setText(Utils.stringForTime((((long) i) * getDuration()) / 100));
        }
    }

    public void cloneAJzvd(ViewGroup viewGroup) {
        try {
            Video vd = (Video) getClass().getConstructor(Context.class).newInstance(getContext());
            vd.setId(getId());
            vd.setMinimumWidth(this.blockWidth);
            vd.setMinimumHeight(this.blockHeight);
            viewGroup.addView(vd, this.blockIndex, this.blockLayoutParams);
            vd.setUp(this.dataSource.cloneMe(), 0, this.mediaInterfaceClass);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e2) {
            e2.printStackTrace();
        } catch (InvocationTargetException e3) {
            e3.printStackTrace();
        } catch (NoSuchMethodException e4) {
            e4.printStackTrace();
        }
    }

    public void gotoFullscreen() {
        this.gotoFullscreenTime = System.currentTimeMillis();
        ViewGroup viewGroup = (ViewGroup) getParent();
        this.blockLayoutParams = getLayoutParams();
        this.blockIndex = viewGroup.indexOfChild(this);
        this.blockWidth = getWidth();
        this.blockHeight = getHeight();
        viewGroup.removeView(this);
        cloneAJzvd(viewGroup);
        CONTAINER_LIST.add(viewGroup);
        ((ViewGroup) Utils.scanForActivity(this.jzvdContext).getWindow().getDecorView()).addView(this, new LayoutParams(-1, -1));
        setScreenFullscreen();
        Utils.hideStatusBar(this.jzvdContext);
        Utils.setRequestedOrientation(this.jzvdContext, FULLSCREEN_ORIENTATION);
        Utils.hideSystemUI(this.jzvdContext);
    }

    public void gotoNormalScreen() {
        this.gobakFullscreenTime = System.currentTimeMillis();
        ((ViewGroup) Utils.scanForActivity(this.jzvdContext).getWindow().getDecorView()).removeView(this);
        LinkedList<ViewGroup> linkedList = CONTAINER_LIST;
        if (!(linkedList == null || linkedList.getLast() == null)) {
            CONTAINER_LIST.getLast().removeViewAt(this.blockIndex);
            CONTAINER_LIST.getLast().addView(this, this.blockIndex, this.blockLayoutParams);
            CONTAINER_LIST.pop();
        }
        setScreenNormal();
        Utils.showStatusBar(this.jzvdContext);
        Utils.setRequestedOrientation(this.jzvdContext, NORMAL_ORIENTATION);
        Utils.showSystemUI(this.jzvdContext);
    }

    public void setScreenNormal() {
        this.screen = 0;
    }

    public void setScreenFullscreen() {
        this.screen = 1;
    }

    public void setScreenTiny() {
        this.screen = 2;
    }

    public void autoFullscreen(float f) {
        int i;
        if (currentVd != null) {
            int i2 = this.state;
            if ((i2 == 5 || i2 == 6) && (i = this.screen) != 1 && i != 2) {
                if (f > 0.0f) {
                    Utils.setRequestedOrientation(getContext(), 0);
                } else {
                    Utils.setRequestedOrientation(getContext(), 8);
                }
                gotoFullscreen();
            }
        }
    }

    public void autoQuitFullscreen() {
        if (System.currentTimeMillis() - lastAutoFullscreenTime > 2000 && this.state == 5 && this.screen == 1) {
            lastAutoFullscreenTime = System.currentTimeMillis();
            backPress();
        }
    }

    public Context getApplicationContext() {
        Context applicationContext;
        Context context = getContext();
        return (context == null || (applicationContext = context.getApplicationContext()) == null) ? context : applicationContext;
    }

    public static class JZAutoFullscreenListener implements SensorEventListener {
        public void onAccuracyChanged(Sensor sensor, int i) {
        }

        public void onSensorChanged(SensorEvent sensorEvent) {
            float f = sensorEvent.values[0];
            float f2 = sensorEvent.values[1];
            float f3 = sensorEvent.values[2];
            if ((f < -12.0f || f > 12.0f) && System.currentTimeMillis() - Video.lastAutoFullscreenTime > 2000) {
                if (Video.currentVd != null) {
                    Video.currentVd.autoFullscreen(f);
                }
                Video.lastAutoFullscreenTime = System.currentTimeMillis();
            }
        }
    }

    public class ProgressTimerTask extends TimerTask {
        public ProgressTimerTask() {
        }

        public void run() {
            if (Video.this.state == 5 || Video.this.state == 6 || Video.this.state == 3) {
                Video.this.post(new Runnable() {
                    /* class com.videoplayer.hdmaxplayer.video.player.saxhdvideoplayer.Extra.Video.ProgressTimerTask.AnonymousClass1 */

                    public final void run() {
                        ProgressTimerTask.this.lambda$run$0$Jzvd$ProgressTimerTask();
                    }
                });
            }
        }

        public void lambda$run$0$Jzvd$ProgressTimerTask() {
            long currentPositionWhenPlaying = Video.this.getCurrentPositionWhenPlaying();
            long duration = Video.this.getDuration();
            Video.this.onProgress((int) ((100 * currentPositionWhenPlaying) / (duration == 0 ? 1 : duration)), currentPositionWhenPlaying, duration);
        }
    }
}
