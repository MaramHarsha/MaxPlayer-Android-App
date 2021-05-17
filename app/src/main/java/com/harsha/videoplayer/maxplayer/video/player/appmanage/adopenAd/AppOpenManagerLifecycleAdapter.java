package com.harsha.videoplayer.maxplayer.video.player.appmanage.adopenAd;

import android.annotation.SuppressLint;

import androidx.lifecycle.GeneratedAdapter;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MethodCallsLogger;

@SuppressLint("RestrictedApi")
public class AppOpenManagerLifecycleAdapter implements GeneratedAdapter {
    final AppOpenManager mReceiver;

    AppOpenManagerLifecycleAdapter(AppOpenManager appOpenManager) {
        this.mReceiver = appOpenManager;
    }

    @Override
    public void callMethods(LifecycleOwner lifecycleOwner, Lifecycle.Event event, boolean z, MethodCallsLogger methodCallsLogger) {
        boolean z2 = methodCallsLogger != null;
        if (z || event != Lifecycle.Event.ON_START) {
            return;
        }
        if (!z2 || methodCallsLogger.approveCall("onStart", 1)) {
            this.mReceiver.onStart();
        }
    }
}
