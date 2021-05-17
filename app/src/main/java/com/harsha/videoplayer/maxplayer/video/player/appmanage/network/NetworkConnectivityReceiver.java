package com.harsha.videoplayer.maxplayer.video.player.appmanage.network;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.harsha.videoplayer.maxplayer.video.player.Util.VideoPlayerManager;

public class NetworkConnectivityReceiver extends BroadcastReceiver {
    public static ConnectivityReceiverListener connectivityReceiverListener;

    public interface ConnectivityReceiverListener {
        void onNetworkConnectionChanged(boolean z);
    }

    public static boolean isConnected() {
        @SuppressLint("WrongConstant")
        NetworkInfo activeNetworkInfo = ((ConnectivityManager) VideoPlayerManager.getInstance().getApplicationContext().getSystemService("connectivity")).getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

    public void onReceive(Context context, Intent intent) {
        @SuppressLint("WrongConstant")
        NetworkInfo activeNetworkInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
        boolean z = activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
        ConnectivityReceiverListener connectivityReceiverListener2 = connectivityReceiverListener;
        if (connectivityReceiverListener2 != null) {
            connectivityReceiverListener2.onNetworkConnectionChanged(z);
        }
    }
}
