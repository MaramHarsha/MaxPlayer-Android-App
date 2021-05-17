package com.harsha.videoplayer.maxplayer.video.player.Extra;

import android.graphics.SurfaceTexture;
import android.media.MediaPlayer;
import android.media.PlaybackParams;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.view.Surface;

import java.util.Map;

public class MediaSystem1 extends MediaInterface implements MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnBufferingUpdateListener, MediaPlayer.OnSeekCompleteListener, MediaPlayer.OnErrorListener, MediaPlayer.OnInfoListener, MediaPlayer.OnVideoSizeChangedListener {
    public MediaPlayer mediaPlayer;

    public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
        return false;
    }

    public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i, int i2) {
    }

    public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {
    }

    public MediaSystem1(Video vd) {
        super(vd);
    }

    @Override
    public void prepare() {
        release();
        this.mMediaHandlerThread = new HandlerThread("JZVD");
        this.mMediaHandlerThread.start();
        this.mMediaHandler = new Handler(this.mMediaHandlerThread.getLooper());
        this.handler = new Handler();
        this.mMediaHandler.post(new Runnable() {
            public final void run() {
                hand();
            }
        });
    }

    private void hand() {
        try {
            MediaPlayer mediaPlayer2 = new MediaPlayer();
            mediaPlayer = mediaPlayer2;
            mediaPlayer2.setAudioStreamType(3);
            mediaPlayer.setLooping(vd.dataSource.looping);
            mediaPlayer.setOnPreparedListener(this);
            mediaPlayer.setOnCompletionListener(this);
            mediaPlayer.setOnBufferingUpdateListener(this);
            mediaPlayer.setScreenOnWhilePlaying(true);
            mediaPlayer.setOnSeekCompleteListener(this);
            mediaPlayer.setOnErrorListener(this);
            mediaPlayer.setOnInfoListener(this);
            mediaPlayer.setOnVideoSizeChangedListener(this);
            MediaPlayer.class.getDeclaredMethod("setDataSource", String.class, Map.class).invoke(mediaPlayer, vd.dataSource.getCurrentUrl().toString(), vd.dataSource.headerMap);
            mediaPlayer.prepareAsync();
            mediaPlayer.setSurface(new Surface(SAVED_SURFACE));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void start() {
        this.mMediaHandler.post(new Runnable() {
            public final void run() {
                mediaPlayer.start();
            }
        });
    }

    @Override
    public void pause() {
        mMediaHandler.post(new Runnable() {
            public final void run() {
                mediaPlayer.pause();
            }
        });
    }

    @Override
    public boolean isPlaying() {
        return this.mediaPlayer.isPlaying();
    }

    @Override
    public void seekTo(long j) {
        this.mMediaHandler.post(new Runnable() {

            public final void run() {
                try {
                    mediaPlayer.seekTo((int) j);
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void release() {
        if (this.mMediaHandler != null && this.mMediaHandlerThread != null && this.mediaPlayer != null) {
            HandlerThread handlerThread = this.mMediaHandlerThread;
            MediaPlayer mediaPlayer2 = this.mediaPlayer;
            MediaInterface.SAVED_SURFACE = null;
            this.mMediaHandler.post(new Runnable() {
                public final void run() {
                    mediaPlayer2.setSurface(null);
                    mediaPlayer2.release();
                    handlerThread.quit();
                }
            });
            this.mediaPlayer = null;
        }
    }

    @Override
    public long getCurrentPosition() {
        MediaPlayer mediaPlayer2 = this.mediaPlayer;
        if (mediaPlayer2 != null) {
            return (long) mediaPlayer2.getCurrentPosition();
        }
        return 0;
    }

    @Override
    public long getDuration() {
        MediaPlayer mediaPlayer2 = this.mediaPlayer;
        if (mediaPlayer2 != null) {
            return (long) mediaPlayer2.getDuration();
        }
        return 0;
    }

    @Override
    public void setVolume(float f, float f2) {
        if (this.mMediaHandler != null) {
            mMediaHandler.post(new Runnable() {
                @Override
                public void run() {
                    MediaPlayer mediaPlayer2 = mediaPlayer;
                    if (mediaPlayer2 != null) {
                        mediaPlayer2.setVolume(f, f2);
                    }
                }
            });
        }
    }

    @Override
    public void setSpeed(float f) {
        if (Build.VERSION.SDK_INT >= 23) {
            PlaybackParams playbackParams = this.mediaPlayer.getPlaybackParams();
            playbackParams.setSpeed(f);
            this.mediaPlayer.setPlaybackParams(playbackParams);
        }
    }

    public void onPrepared(MediaPlayer mediaPlayer2) {
        this.handler.post(new Runnable() {
            public final void run() {
                vd.onPrepared();
            }
        });
    }

    public void onCompletion(MediaPlayer mediaPlayer2) {
        this.handler.post(new Runnable() {
            public final void run() {
                vd.onCompletion();
            }
        });
    }

    public void onBufferingUpdate(MediaPlayer mediaPlayer2, int i) {
        this.handler.post(new Runnable() {

            public final void run() {
                vd.setBufferProgress(i);
            }
        });
    }


    public void onSeekComplete(MediaPlayer mediaPlayer2) {
        this.handler.post(new Runnable() {
            public final void run() {
                vd.onSeekComplete();
            }
        });
    }

    public boolean onError(MediaPlayer mediaPlayer2, int i, int i2) {
        this.handler.post(new Runnable() {

            public final void run() {
                vd.onError(i, i2);
            }
        });
        return true;
    }

    public boolean onInfo(MediaPlayer mediaPlayer2, int i, int i2) {
        this.handler.post(new Runnable() {
            public final void run() {
                vd.onInfo(i, i2);
            }
        });
        return false;
    }

    public void onVideoSizeChanged(MediaPlayer mediaPlayer2, int i, int i2) {
        this.handler.post(new Runnable() {
            public final void run() {
                vd.onVideoSizeChanged(i, i2);
            }
        });
    }

    @Override
    public void setSurface(Surface surface) {
        this.mediaPlayer.setSurface(surface);
    }

    public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int i, int i2) {
        if (SAVED_SURFACE == null) {
            SAVED_SURFACE = surfaceTexture;
            prepare();
            return;
        }
        this.vd.textureView.setSurfaceTexture(SAVED_SURFACE);
    }
}
