package com.harsha.videoplayer.maxplayer.video.player.Service;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.IBinder;
import android.widget.RemoteViews;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.BaseRequestOptions;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.bumptech.glide.signature.ObjectKey;
import com.harsha.videoplayer.maxplayer.video.player.Activity.VideoPlayerActivity;
import com.harsha.videoplayer.maxplayer.video.player.R;
import com.harsha.videoplayer.maxplayer.video.player.Util.Constant;
import com.harsha.videoplayer.maxplayer.video.player.Util.VideoPlayerManager;
import com.harsha.videoplayer.maxplayer.video.player.Util.VideoPlayerUtils;
import com.harsha.videoplayer.maxplayer.video.player.Extra.MediaData;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class VideoPlayAsAudioService extends Service {
    static final String CHANNEL_ID = "video_player_channel";
    public static final String NEXT_ACTION = "hdvideo.hd.videoplayer.hd.videoplayer.next";
    public static final String NOTIFICATION_CLICK_ACTION = "notification_click";
    public static final String PAUSE_ACTION = "hdvideo.hd.videoplayer.hd.videoplayer.pause";
    public static final String PLAY_ACTION = "hdvideo.hd.videoplayer.hd.videoplayer.play";
    public static final String PREVIOUS_ACTION = "hdvideo.hd.videoplayer.hd.videoplayer.previous";
    public static final String STOP_ACTION = "hdvideo.hd.videoplayer.hd.videoplayer.stop";
    public static final String TOGGLEPAUSE_ACTION = "hdvideo.hd.videoplayer.hd.videoplayer.togglepause";
    public static int videoPosition;
    Handler handler = new Handler();
    boolean isRefresh = false;
    boolean isUpdate = false;
    private final BroadcastReceiver mIntentReceiver = new BroadcastReceiver() {
        /* class com.videoplayer.hdmaxplayer.video.player.saxhdvideoplayer.Service.VideoPlayAsAudioService.AnonymousClass1 */

        public void onReceive(Context context, Intent intent) {
            VideoPlayAsAudioService.this.handleCommandIntent(intent);
        }
    };
    private NotificationManagerCompat mNotificationManager;
    private long mNotificationPostTime = 0;
    MediaPlayer mediaPlayer;
    private NotificationCompat.Builder notificationBuilder;
    int notificationId;
    Runnable runnable;
    ArrayList<MediaData> videoList = new ArrayList<>();

    private void createChannel() {
    }

    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onCreate() {
        super.onCreate();
        this.mNotificationManager = NotificationManagerCompat.from(this);
        this.notificationBuilder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID);
        MediaPlayer mediaPlayer2 = new MediaPlayer();
        this.mediaPlayer = mediaPlayer2;
        mediaPlayer2.stop();
        this.mediaPlayer.setDisplay(null);
        this.videoList = VideoPlayerManager.getVideoList();
        createChannel();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(PAUSE_ACTION);
        intentFilter.addAction(STOP_ACTION);
        intentFilter.addAction(NEXT_ACTION);
        intentFilter.addAction(PREVIOUS_ACTION);
        registerReceiver(this.mIntentReceiver, intentFilter);
    }

    @SuppressLint("WrongConstant")
    public int onStartCommand(Intent intent, int i, int i2) {
        if (intent != null) {
            if (intent.getAction() == null || !intent.getAction().equals(NOTIFICATION_CLICK_ACTION)) {
                if (!this.isUpdate) {
                    videoPosition = intent.getIntExtra(Constant.EXTRA_VIDEO_POSITION, 0);
                    videoPlay();
                    this.isUpdate = true;
                }
                handleCommandIntent(intent);
                managePlayVideo();
            } else {
                this.handler.removeCallbacks(this.runnable);
                stopForeground(true);
                stopSelf();
            }
        }
        return 1;
    }

    public void managePlayVideo() {
        if (this.videoList != null) {
            this.mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                public void onCompletion(MediaPlayer mediaPlayer) {
                    if (VideoPlayAsAudioService.videoPosition != VideoPlayAsAudioService.this.videoList.size()) {
                        VideoPlayAsAudioService.videoPosition++;
                        if (VideoPlayAsAudioService.this.videoList.get(VideoPlayAsAudioService.videoPosition).getLayoutType() == 1) {
                            VideoPlayAsAudioService.videoPosition++;
                        }
                        VideoPlayAsAudioService.this.isRefresh = true;
                        VideoPlayAsAudioService.this.handler.removeCallbacks(VideoPlayAsAudioService.this.runnable);
                        VideoPlayAsAudioService.this.videoPlay();
                    }
                }
            });
        }
    }

    public void videoPlay() {
        try {
            this.mediaPlayer.reset();
            this.mediaPlayer.setDataSource(this.videoList.get(videoPosition).getPath());
            this.mediaPlayer.setAudioStreamType(3);
            this.mediaPlayer.prepareAsync();
            this.mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                public void onPrepared(MediaPlayer mediaPlayer) {
                    VideoPlayAsAudioService.this.mediaPlayer.seekTo(VideoPlayAsAudioService.this.videoList.get(VideoPlayAsAudioService.videoPosition).getVideoLastPlayPosition());
                    VideoPlayAsAudioService.this.mediaPlayer.start();
                    VideoPlayAsAudioService.this.updateNotification();
                    VideoPlayAsAudioService.this.runnable.run();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleCommandIntent(Intent intent) {
        String action = intent.getAction();
        if (NEXT_ACTION.equals(action)) {
            if (videoPosition != this.videoList.size()) {
                int i = videoPosition + 1;
                videoPosition = i;
                if (this.videoList.get(i).getLayoutType() == 1) {
                    videoPosition++;
                }
                this.isRefresh = true;
                this.handler.removeCallbacks(this.runnable);
                videoPlay();
            }
        } else if (PREVIOUS_ACTION.equals(action)) {
            int i2 = videoPosition;
            if (i2 != 0) {
                int i3 = i2 - 1;
                videoPosition = i3;
                if (this.videoList.get(i3).getLayoutType() == 1) {
                    videoPosition--;
                }
                this.isRefresh = true;
                this.handler.removeCallbacks(this.runnable);
                videoPlay();
            }
        } else if (PAUSE_ACTION.equals(action)) {
            this.mediaPlayer.pause();
            this.isRefresh = true;
            updateNotification();
        } else if (PLAY_ACTION.equals(action)) {
            this.mediaPlayer.start();
            this.isRefresh = true;
            updateNotification();
        } else if (STOP_ACTION.equals(action)) {
            this.mediaPlayer.stop();
            this.isRefresh = false;
            this.handler.removeCallbacks(this.runnable);
            stopForeground(true);
            stopSelf();
            this.mNotificationManager.cancelAll();
            this.mNotificationManager.cancel(this.notificationId);
        }
    }

    public void updateNotification() {
        this.notificationId = hashCode();
        this.mNotificationManager.cancelAll();
        if (!this.isRefresh) {
            startForeground(this.notificationId, buildNotification());
        } else {
            this.mNotificationManager.notify(this.notificationId, buildNotification());
        }
    }

    private Notification buildNotification() {
        final RemoteViews remoteViews = new RemoteViews(getPackageName(), (int) R.layout.layout_notification_collapse);
        final RemoteViews remoteViews2 = new RemoteViews(getPackageName(), (int) R.layout.layout_notification);
        String name = this.videoList.get(videoPosition).getName();
        boolean isPlaying = this.mediaPlayer.isPlaying();
        String str = VideoPlayerUtils.makeShortTimeString(this, (long) (this.mediaPlayer.getCurrentPosition() / 1000)) + " / "
                + VideoPlayerUtils.makeShortTimeString(this, (long) (this.videoList.get(videoPosition).getVideoDuration() / 1000));
        int i = isPlaying ? R.drawable.ic_pause : R.drawable.ic_play;
        @SuppressLint("WrongConstant") PendingIntent activity = PendingIntent.getActivity(this, 0, new Intent(VideoPlayerActivity
                .getIntent(this, videoList, videoPosition, true)), 134217728);
        File file = new File(this.videoList.get(videoPosition).getPath());
        Glide.with(this).load(this.videoList.get(videoPosition).getPath()).apply((BaseRequestOptions<?>) ((RequestOptions)
                ((RequestOptions) ((RequestOptions) new RequestOptions().signature(new ObjectKey(file.getAbsolutePath()
                        + file.lastModified()))).priority(Priority.LOW)).diskCacheStrategy(DiskCacheStrategy.RESOURCE)))
                .transition(DrawableTransitionOptions.withCrossFade()).into(new SimpleTarget<Drawable>() {
            @Override
            public void onResourceReady(@NonNull Drawable drawable, @Nullable Transition<? super Drawable> transition) {
                remoteViews.setImageViewBitmap(R.id.songImage, VideoPlayAsAudioService.drawableToBitmap(drawable));
                remoteViews2.setImageViewBitmap(R.id.songImage, VideoPlayAsAudioService.drawableToBitmap(drawable));
            }
        });
        if (this.mNotificationPostTime == 0) {
            this.mNotificationPostTime = System.currentTimeMillis();
        }
        remoteViews.setTextViewText(R.id.songName, name);
        remoteViews.setTextViewText(R.id.artistName, str);
        remoteViews.setImageViewResource(R.id.play_pause, i);
        remoteViews2.setTextViewText(R.id.songName, name);
        remoteViews2.setTextViewText(R.id.artistName, str);
        remoteViews2.setImageViewResource(R.id.play_pause, i);
        this.notificationBuilder.setSmallIcon(R.mipmap.ic_launcher).setContentIntent(activity).setCustomContentView(remoteViews).setCustomBigContentView(remoteViews2);
        if (isPlaying) {
            remoteViews.setOnClickPendingIntent(R.id.play_pause, retrievePlaybackAction(PAUSE_ACTION));
            remoteViews2.setOnClickPendingIntent(R.id.play_pause, retrievePlaybackAction(PAUSE_ACTION));
        } else {
            remoteViews.setOnClickPendingIntent(R.id.play_pause, retrievePlaybackAction(PLAY_ACTION));
            remoteViews2.setOnClickPendingIntent(R.id.play_pause, retrievePlaybackAction(PLAY_ACTION));
        }
        remoteViews.setOnClickPendingIntent(R.id.next, retrievePlaybackAction(NEXT_ACTION));
        remoteViews.setOnClickPendingIntent(R.id.close, retrievePlaybackAction(STOP_ACTION));
        remoteViews2.setOnClickPendingIntent(R.id.next, retrievePlaybackAction(NEXT_ACTION));
        remoteViews2.setOnClickPendingIntent(R.id.previous, retrievePlaybackAction(PREVIOUS_ACTION));
        remoteViews2.setOnClickPendingIntent(R.id.close, retrievePlaybackAction(STOP_ACTION));
        this.runnable = new Runnable() {
            public void run() {
                VideoPlayAsAudioService.this.isRefresh = true;
                VideoPlayAsAudioService.this.updateNotification();
                VideoPlayAsAudioService.this.handler.postDelayed(VideoPlayAsAudioService.this.runnable, 1000);
            }
        };
        return this.notificationBuilder.build();
    }

    public static Bitmap drawableToBitmap(Drawable drawable) {
        Bitmap bitmap;
        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if (bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }
        if (drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    private PendingIntent retrievePlaybackAction(String str) {
        ComponentName componentName = new ComponentName(this, VideoPlayAsAudioService.class);
        Intent intent = new Intent(str);
        intent.setComponent(componentName);
        intent.putExtra(Constant.EXTRA_VIDEO_POSITION, videoPosition);
        return PendingIntent.getService(this, 0, intent, 0);
    }

    public void onDestroy() {
        super.onDestroy();
        this.handler.removeCallbacks(this.runnable);
        this.mediaPlayer.stop();
        stopSelf();
        stopForeground(true);
        this.mNotificationManager.cancelAll();
        unregisterReceiver(this.mIntentReceiver);
    }
}
