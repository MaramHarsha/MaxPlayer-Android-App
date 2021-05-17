package com.harsha.videoplayer.maxplayer.video.player.appmanage.adopenAd;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.appopen.AppOpenAd;
import com.harsha.videoplayer.maxplayer.video.player.Util.VideoPlayerManager;

import java.util.Date;

public class AppOpenManager implements Application.ActivityLifecycleCallbacks, LifecycleObserver {
    private static final String ADUNITID = "ca-app-pub-3940256099942544/1033173712";
    private static final String LOGTAG = "AppOpenManager";
    public static MutableLiveData<Boolean> isShowing = new MutableLiveData<>();
    public static boolean isShowingAd = false;
    private AppOpenAd appOpenAd = null;
    private Activity currentActivity;
    private long loadTime = 0;
    private final VideoPlayerManager myApplication;

    public void onActivityCreated(Activity activity, Bundle bundle) {
    }

    public void onActivityPaused(Activity activity) {
    }

    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
    }

    public void onActivityStopped(Activity activity) {
    }

    public AppOpenManager(VideoPlayerManager videoPlayerManager) {
        this.myApplication = videoPlayerManager;
        isShowing.postValue(Boolean.valueOf(isShowingAd));
        videoPlayerManager.registerActivityLifecycleCallbacks(this);
        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void onStart() {
        showAdIfAvailable();
        Log.d(LOGTAG, "onStart");
    }

    public void fetchAd() {
        if (!isAdAvailable()) {
            AppOpenAd.AppOpenAdLoadCallback loadCallback = new AppOpenAd.AppOpenAdLoadCallback() {

                @Override
                public void onAppOpenAdFailedToLoad(LoadAdError loadAdError) {
                }

                @Override
                public void onAppOpenAdLoaded(AppOpenAd appOpenAd) {
                    AppOpenManager.this.appOpenAd = appOpenAd;
                    AppOpenManager.this.loadTime = new Date().getTime();
                }
            };
            AppOpenAd.load(this.myApplication, ADUNITID, getAdRequest(), 1, loadCallback);
        }
    }

    private AdRequest getAdRequest() {
        return new AdRequest.Builder().build();
    }

    public boolean isAdAvailable() {
        return this.appOpenAd != null && wasLoadTimeLessThanNHoursAgo(4);
    }

    private boolean wasLoadTimeLessThanNHoursAgo(long j) {
        return new Date().getTime() - this.loadTime < j * 3600000;
    }

    public void showAdIfAvailable() {
        if (isShowingAd || !isAdAvailable()) {
            Log.d(LOGTAG, "Can not show ad.");
            fetchAd();
            return;
        }
        Log.d(LOGTAG, "Will show ad.");
        this.appOpenAd.show(this.currentActivity, new FullScreenContentCallback() {


            @Override
            public void onAdFailedToShowFullScreenContent(AdError adError) {
            }

            @Override
            public void onAdDismissedFullScreenContent() {
                AppOpenManager.this.appOpenAd = null;
                AppOpenManager.isShowingAd = false;
                AppOpenManager.isShowing.postValue(Boolean.valueOf(AppOpenManager.isShowingAd));
                AppOpenManager.this.fetchAd();
            }

            @Override
            public void onAdShowedFullScreenContent() {
                AppOpenManager.isShowingAd = true;
                AppOpenManager.isShowing.postValue(Boolean.valueOf(AppOpenManager.isShowingAd));
            }
        });
    }

    public void onActivityStarted(Activity activity) {
        this.currentActivity = activity;
    }

    public void onActivityResumed(Activity activity) {
        this.currentActivity = activity;
    }

    public void onActivityDestroyed(Activity activity) {
        this.currentActivity = null;
    }
}
