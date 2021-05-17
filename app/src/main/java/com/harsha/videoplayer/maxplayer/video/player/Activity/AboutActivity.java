package com.harsha.videoplayer.maxplayer.video.player.Activity;

import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.harsha.videoplayer.maxplayer.video.player.BuildConfig;
import com.harsha.videoplayer.maxplayer.video.player.R;
import com.harsha.videoplayer.maxplayer.video.player.appmanage.ads.AdsNative;

public class AboutActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_about);
        ImageView backabout = (ImageView) findViewById(R.id.back_about);
        ((TextView) findViewById(R.id.version)).setText("( " + BuildConfig.VERSION_NAME + " )");
        backabout.setOnClickListener(view -> AboutActivity.this.onBackPressed());
        new AdsNative(this, (FrameLayout) findViewById(R.id.fl_adplaceholder));
    }
}
