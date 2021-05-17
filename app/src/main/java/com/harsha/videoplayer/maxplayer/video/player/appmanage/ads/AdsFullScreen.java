package com.harsha.videoplayer.maxplayer.video.player.appmanage.ads;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.exifinterface.media.ExifInterface;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.InterstitialAd;
import com.facebook.ads.InterstitialAdListener;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.harsha.videoplayer.maxplayer.video.player.R;
import com.harsha.videoplayer.maxplayer.video.player.appmanage.AdConfig;
import com.harsha.videoplayer.maxplayer.video.player.appmanage.model.CustomAd;
import com.harsha.videoplayer.maxplayer.video.player.appmanage.network.NetworkConnectivityReceiver;

import java.util.Random;

public class AdsFullScreen {
    Activity activity;
    FrameLayout frameLayout;
    Intent intent;
    InterstitialAd interstitialAdFb;
    boolean isResult = false;
    com.google.android.gms.ads.InterstitialAd mInterstitialAd;

    public interface AdsFullScreenListener {
        void onObjectReady(String str);
    }

    public AdsFullScreen(Activity activity2) {
        this.activity = activity2;
    }

    public AdsFullScreen(Activity activity2, FrameLayout frameLayout2) {
        this.activity = activity2;
        this.frameLayout = frameLayout2;
    }

    public void getIsResult(boolean z) {
        this.isResult = z;
    }

    public void loadFullAd() {
        if (AdConfig.getAppData == null || AdConfig.getAllData == null || !NetworkConnectivityReceiver.isConnected()) {
            noAds();
        } else if (AdConfig.getAppData.getIsactive().equalsIgnoreCase("1")) {
            adMobeAd();
        } else if (AdConfig.getAppData.getIsactive().equalsIgnoreCase(ExifInterface.GPS_MEASUREMENT_2D)) {
            facebookAd();
        } else if (AdConfig.getAppData.getIsactive().equalsIgnoreCase(ExifInterface.GPS_MEASUREMENT_3D)) {
            noAds();
        } else {
            AdConfig.getAppData.getIsactive().equalsIgnoreCase("4");
        }
    }

    public void showFullAd(Intent intent2) {
        this.intent = intent2;
        if (AdConfig.getAllData == null || AdConfig.getAppData == null || !NetworkConnectivityReceiver.isConnected()) {
            openNextActivity();
        } else if (AdConfig.getAppData.getIsactive().equalsIgnoreCase("1")) {
            com.google.android.gms.ads.InterstitialAd interstitialAd = this.mInterstitialAd;
            if (interstitialAd == null || !interstitialAd.isLoaded()) {
                InterstitialAd interstitialAd2 = this.interstitialAdFb;
                if (interstitialAd2 == null || !interstitialAd2.isAdLoaded()) {
                    customAd();
                } else {
                    this.interstitialAdFb.show();
                }
            } else {
                this.mInterstitialAd.show();
            }
        } else if (AdConfig.getAppData.getIsactive().equalsIgnoreCase(ExifInterface.GPS_MEASUREMENT_2D)) {
            InterstitialAd interstitialAd3 = this.interstitialAdFb;
            if (interstitialAd3 == null || !interstitialAd3.isAdLoaded()) {
                com.google.android.gms.ads.InterstitialAd interstitialAd4 = this.mInterstitialAd;
                if (interstitialAd4 == null || !interstitialAd4.isLoaded()) {
                    customAd();
                } else {
                    this.mInterstitialAd.show();
                }
            } else {
                this.interstitialAdFb.show();
            }
        } else if (AdConfig.getAppData.getIsactive().equalsIgnoreCase(ExifInterface.GPS_MEASUREMENT_3D)) {
            openNextActivity();
        } else if (AdConfig.getAppData.getIsactive().equalsIgnoreCase("4")) {
            customAd();
        }
    }

    public void adMobeAd() {
        com.google.android.gms.ads.InterstitialAd interstitialAd = new com.google.android.gms.ads.InterstitialAd(this.activity);
        this.mInterstitialAd = interstitialAd;
        interstitialAd.setAdUnitId("" + AdConfig.getAppData.getAdmobinterstitial());
        this.mInterstitialAd.loadAd(new AdRequest.Builder().build());
        this.mInterstitialAd.setAdListener(new AdListener() {
            /* class com.videoplayer.hdmaxplayer.video.player.saxhdvideoplayer.appmanage.ads.AdsFullScreen.AnonymousClass1 */

            @Override // com.google.android.gms.ads.AdListener
            public void onAdClicked() {
            }

            @Override // com.google.android.gms.ads.AdListener
            public void onAdFailedToLoad(int i) {
            }

            @Override // com.google.android.gms.ads.AdListener
            public void onAdLeftApplication() {
            }

            @Override // com.google.android.gms.ads.AdListener
            public void onAdLoaded() {
            }

            @Override // com.google.android.gms.ads.AdListener
            public void onAdOpened() {
            }

            @Override // com.google.android.gms.ads.AdListener
            public void onAdClosed() {
                AdsFullScreen.this.mInterstitialAd.loadAd(new AdRequest.Builder().build());
                AdsFullScreen.this.openNextActivity();
            }
        });
    }

    public void facebookAd() {
        Activity activity2 = this.activity;
        this.interstitialAdFb = new InterstitialAd(activity2, "" + AdConfig.getAppData.getFbinterstitial());
        InterstitialAdListener r0 = new InterstitialAdListener() {


            @Override
            public void onAdClicked(Ad ad) {
            }

            @Override
            public void onAdLoaded(Ad ad) {
            }

            @Override
            public void onInterstitialDisplayed(Ad ad) {
            }

            @Override
            public void onLoggingImpression(Ad ad) {
            }

            @Override
            public void onInterstitialDismissed(Ad ad) {
                AdsFullScreen.this.openNextActivity();
                AdsFullScreen.this.interstitialAdFb.loadAd();
            }

            @Override
            public void onError(Ad ad, AdError adError) {
                Log.e("getFbinterstitial==", "======" + adError.getErrorMessage() + "" + adError.getErrorCode());
            }
        };
        InterstitialAd interstitialAd = this.interstitialAdFb;
        interstitialAd.loadAd(interstitialAd.buildLoadAdConfig().withAdListener(r0).build());
    }

    @SuppressLint("WrongConstant")
    public void customAd() {
        if (AdConfig.getAllData == null || AdConfig.getAllData.getCustomAds() == null) {
            openNextActivity();
        } else if (AdConfig.getAllData.getCustomAds().size() > 0) {
            int nextInt = new Random().nextInt(AdConfig.getAllData.getCustomAds().size());
            @SuppressLint("WrongConstant") View inflate = ((LayoutInflater) this.activity.getSystemService("layout_inflater")).inflate(R.layout.custom_interstitial_ad1, (ViewGroup) null);
            final CustomAd customAd = AdConfig.getAllData.getCustomAds().get(nextInt);
            RequestManager with = Glide.with(this.activity);
            with.load("" + customAd.getLogo()).into((ImageView) inflate.findViewById(R.id.img_icon));
            ((TextView) inflate.findViewById(R.id.txtappname)).setText(customAd.getAppname());
            ((TextView) inflate.findViewById(R.id.txtdescription)).setText(customAd.getAdsshort());
            RequestManager with2 = Glide.with(this.activity);
            with2.load("" + customAd.getAdsBigImage()).into((ImageView) inflate.findViewById(R.id.img_big_img));
            ImageView imageView = (ImageView) inflate.findViewById(R.id.btn_close);
            imageView.setVisibility(0);
            ((RelativeLayout) inflate.findViewById(R.id.rl_timer)).setVisibility(8);
            ((Button) inflate.findViewById(R.id.btninstall)).setOnClickListener(new View.OnClickListener() {

                public void onClick(View view) {
                    try {
                        Activity activity = AdsFullScreen.this.activity;
                        activity.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("market://details?id=" + customAd.getPackagename())));
                    } catch (ActivityNotFoundException unused) {
                        Activity activity2 = AdsFullScreen.this.activity;
                        activity2.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("http://play.google.com/store/apps/details?id=" + customAd.getPackagename())));
                    }
                }
            });
            this.frameLayout.removeAllViews();
            this.frameLayout.addView(inflate);
            this.frameLayout.setVisibility(0);
            imageView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    AdsFullScreen.this.frameLayout.setVisibility(8);
                    AdsFullScreen.this.openNextActivity();
                }
            });
        }
    }

    public void openNextActivity() {
        try {
            if (this.isResult) {
                this.activity.startActivity(this.intent);
                this.activity.finish();
                return;
            }
            this.activity.startActivity(this.intent);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this.activity, "Error occurred! \n Try agin", 0).show();
        }
    }

    public void loadingErrorAdmob() {
        if (AdConfig.getAppData.getIsactive().equalsIgnoreCase(ExifInterface.GPS_MEASUREMENT_2D)) {
            customAd();
        } else {
            facebookAd();
        }
    }

    public void loadingErrorFacebook() {
        if (AdConfig.getAppData.getIsactive().equalsIgnoreCase("1")) {
            customAd();
        } else {
            adMobeAd();
        }
    }

    public void noAds() {
        FrameLayout frameLayout2 = this.frameLayout;
        if (frameLayout2 != null) {
            frameLayout2.setVisibility(8);
        }
    }
}
