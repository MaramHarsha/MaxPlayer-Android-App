package com.harsha.videoplayer.maxplayer.video.player.Util;

import android.content.Context;

public class VideoPlayerUtils {
    public static final String makeShortTimeString(Context context, long j) {
        int i = (int) j;
        int i2 = i % 60;
        int i3 = (i / 60) % 60;
        int i4 = i / 3600;
        if (i4 > 0) {
            return String.format("%02d:%02d:%02d", Integer.valueOf(i4), Integer.valueOf(i3), Integer.valueOf(i2));
        }
        return String.format("%02d:%02d", Integer.valueOf(i3), Integer.valueOf(i2));
    }
}
