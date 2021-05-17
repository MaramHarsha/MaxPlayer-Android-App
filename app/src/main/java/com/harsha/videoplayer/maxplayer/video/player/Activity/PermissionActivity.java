package com.harsha.videoplayer.maxplayer.video.player.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.harsha.videoplayer.maxplayer.video.player.R;
import com.harsha.videoplayer.maxplayer.video.player.Util.Constant;

import java.io.File;

public class PermissionActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView btnAllow;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_permission);
        initView();
        initListener();
    }

    private void initListener() {
        this.btnAllow.setOnClickListener(this);
    }

    private void initView() {
        this.btnAllow = (TextView) findViewById(R.id.btnAllow);
    }

    public void onClick(View view) {
        if (view.getId() == R.id.btnAllow && Build.VERSION.SDK_INT >= 23) {
            checkPermissionAndThenLoad();
        }
    }

    private void checkPermissionAndThenLoad() {
        ActivityCompat.requestPermissions(this, new String[]{"android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE"}, 101);
    }

    @SuppressLint("WrongConstant")
    @Override
    public void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
        super.onRequestPermissionsResult(i, strArr, iArr);
        if (i != 101) {
            return;
        }
        if (ContextCompat.checkSelfPermission(this, "android.permission.READ_EXTERNAL_STORAGE") == 0) {
            if (!new File(Constant.HIDE_PATH).exists()) {
                new File(Constant.HIDE_PATH).mkdirs();
            }
            startActivity(new Intent(this, HomeActivity.class));
            finish();
            return;
        }
        Toast.makeText(this, "Allow this permission!", 0).show();
    }
}
