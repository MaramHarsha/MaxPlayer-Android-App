package com.harsha.videoplayer.maxplayer.video.player.Activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.gson.Gson;
import com.harsha.videoplayer.maxplayer.video.player.R;
import com.harsha.videoplayer.maxplayer.video.player.Util.VideoPlayerManager;
import com.harsha.videoplayer.maxplayer.video.player.appmanage.AdConfig;
import com.harsha.videoplayer.maxplayer.video.player.appmanage.adopenAd.AppOpenManager;
import com.harsha.videoplayer.maxplayer.video.player.appmanage.encdec.ServerDecrypt;
import com.harsha.videoplayer.maxplayer.video.player.appmanage.model.AdsCallObj;
import com.harsha.videoplayer.maxplayer.video.player.appmanage.model.AllData;
import com.harsha.videoplayer.maxplayer.video.player.appmanage.model.AppData;
import com.harsha.videoplayer.maxplayer.video.player.appmanage.model.LoadData;
import com.harsha.videoplayer.maxplayer.video.player.appmanage.model.MainAdmanage;

import org.json.JSONObject;

import java.util.ArrayList;

public class SplashActivity extends AppCompatActivity {
    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_splash);
//        if (NetworkConnectivityReceiver.isConnected()) {
//            getAppManageData();
//        } else {
        defaultAdData();
//        }
    }

    public boolean checkPermission(String str) {
        return ContextCompat.checkSelfPermission(this, str) == 0;
    }

    public void getAppManageData() {
        String encryptByPublic = ServerDecrypt.encryptByPublic(new Gson().toJson(new AdsCallObj(VideoPlayerManager.getInstance().ssh, getPackageName())));
        VideoPlayerManager.getInstance();
        AndroidNetworking.post(new String(Base64.decode(VideoPlayerManager.getNativeKeyAdManage(), 0))).addStringBody(encryptByPublic).setTag((Object) "test").setPriority(Priority.MEDIUM).build().getAsJSONObject(new JSONObjectRequestListener() {

            @Override
            public void onResponse(JSONObject jSONObject) {
                LoadData loadData = (LoadData) new Gson().fromJson(jSONObject.toString(), LoadData.class);
                if (loadData == null || loadData.getData().equalsIgnoreCase("")) {
                    SplashActivity.this.defaultAdData();
                    return;
                }
                try {
                    if (!loadData.getData().equalsIgnoreCase("") && !loadData.getToken().equalsIgnoreCase("")) {
                        AdConfig.mainAdmanage = (MainAdmanage) new Gson().fromJson(ServerDecrypt.decrypt(loadData.getData(), ServerDecrypt.decryptByPublic(loadData.getToken())), MainAdmanage.class);
                        if (AdConfig.mainAdmanage.getData() != null) {
                            AdConfig.getAllData = AdConfig.mainAdmanage.getData();
                            AdConfig.getAppData = AdConfig.getAllData.getMain().get(0);
                            SplashActivity.this.openNext();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    SplashActivity.this.defaultAdData();
                }
            }

            @Override
            public void onError(ANError aNError) {
                SplashActivity.this.defaultAdData();
            }
        });
    }

    public void defaultAdData() {
        AppData appData = new AppData("1", getResources().getString(R.string.banner_ad_unit_id), getResources().getString(R.string.interstitial_ad_unit_id), getResources().getString(R.string.native_ad_unit_id), getResources().getString(R.string.fb_banner_ad_unit_id), getResources().getString(R.string.fb_interstitial_ad_unit_id), getResources().getString(R.string.fb_native_ad_unit_id));
        ArrayList arrayList = new ArrayList();
        arrayList.add(appData);
        AdConfig.getAllData = new AllData(arrayList);
        AdConfig.getAppData = AdConfig.getAllData.getMain().get(0);
        openNext();
    }

    public void openNext() {
        if (AppOpenManager.isShowingAd) {
            AppOpenManager.isShowing.observe(this, bool -> {
                if (!bool.booleanValue()) {
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            SplashActivity.this.nextActivity();
                        }
                    }, 1500);
                }
            });
        } else {
            new Handler().postDelayed(() -> SplashActivity.this.nextActivity(), 1500);
        }
    }

    public void nextActivity() {
        if (Build.VERSION.SDK_INT < 23) {
            startActivity(new Intent(this, HomeActivity.class));
            finish();
        } else if (!checkPermission("android.permission.READ_EXTERNAL_STORAGE") || !checkPermission("android.permission.WRITE_EXTERNAL_STORAGE")) {
            startActivity(new Intent(this, PermissionActivity.class));
            finish();
        } else {
            startActivity(new Intent(this, HomeActivity.class));
            finish();
        }
    }
}
