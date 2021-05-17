package com.harsha.videoplayer.maxplayer.video.player.appmanage.ads;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.exifinterface.media.ExifInterface;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.harsha.videoplayer.maxplayer.video.player.R;
import com.harsha.videoplayer.maxplayer.video.player.Util.VideoPlayerManager;
import com.harsha.videoplayer.maxplayer.video.player.appmanage.AdConfig;
import com.harsha.videoplayer.maxplayer.video.player.appmanage.model.CustomAd;
import com.harsha.videoplayer.maxplayer.video.player.appmanage.network.NetworkConnectivityReceiver;

import java.util.Random;

public class AdsBanner {
    Context context;
    boolean isFull = false;
    RelativeLayout rlBannerAd;
    RelativeLayout rlLayout;

    public AdsBanner(Context context2, RelativeLayout relativeLayout, RelativeLayout relativeLayout2) {
        this.context = context2;
        this.rlBannerAd = relativeLayout2;
        this.rlLayout = relativeLayout;
    }

    public void setIsFull() {
        this.isFull = true;
    }

    public void loadBannerAd() {
        if (AdConfig.getAppData == null || AdConfig.getAllData == null || !NetworkConnectivityReceiver.isConnected()) {
            noAds();
        } else if (AdConfig.getAppData.getIsactive().equalsIgnoreCase("1")) {
            bannerAdAdmob();
        } else if (AdConfig.getAppData.getIsactive().equalsIgnoreCase(ExifInterface.GPS_MEASUREMENT_2D)) {
            if (!this.isFull) {
                bannerAdFacebook();
            } else {
                noAds();
            }
        } else if (AdConfig.getAppData.getIsactive().equalsIgnoreCase(ExifInterface.GPS_MEASUREMENT_3D)) {
            noAds();
        } else if (!AdConfig.getAppData.getIsactive().equalsIgnoreCase("4")) {

        } else {
            if (!this.isFull) {
                bannerAdCustom();
            } else {
                noAds();
            }
        }
    }

    public void bannerAdAdmob() {
        AdView adView = new AdView(this.context);
        if (this.isFull) {
            adView.setAdSize(AdSize.MEDIUM_RECTANGLE);
        } else {
            adView.setAdSize(AdSize.SMART_BANNER);
        }
        adView.setAdUnitId(AdConfig.getAppData.getAdmobbanner());
        adView.setAdListener(new AdListener() {
            /* class com.videoplayer.hdmaxplayer.video.player.saxhdvideoplayer.appmanage.ads.AdsBanner.AnonymousClass1 */

            @Override // com.google.android.gms.ads.AdListener
            public void onAdFailedToLoad(int i) {
                AdsBanner.this.bannerAdCustom();
            }
        });
        AdRequest build = new AdRequest.Builder().build();
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(-2, -2);
        layoutParams.addRule(14);
        this.rlBannerAd.addView(adView, layoutParams);
        adView.loadAd(build);
    }

    public void bannerAdFacebook() {
        com.facebook.ads.AdView adView = new com.facebook.ads.AdView(this.context, AdConfig.getAppData.getFbbanner(), com.facebook.ads.AdSize.BANNER_HEIGHT_50);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(-2, -2);
        layoutParams.addRule(14);
        com.facebook.ads.AdListener r2 = new com.facebook.ads.AdListener() {
            /* class com.videoplayer.hdmaxplayer.video.player.saxhdvideoplayer.appmanage.ads.AdsBanner.AnonymousClass2 */

            @Override // com.facebook.ads.AdListener
            public void onAdClicked(Ad ad) {
            }

            @Override // com.facebook.ads.AdListener
            public void onAdLoaded(Ad ad) {
            }

            @Override // com.facebook.ads.AdListener
            public void onLoggingImpression(Ad ad) {
            }

            @Override // com.facebook.ads.AdListener
            public void onError(Ad ad, AdError adError) {
                AdsBanner.this.bannerAdCustom();
            }
        };
        this.rlBannerAd.addView(adView, layoutParams);
        adView.loadAd(adView.buildLoadAdConfig().withAdListener(r2).build());
    }

    public void bannerAdCustom() {
        if (AdConfig.getAllData == null || AdConfig.getAllData.getCustomAds() == null) {
            noAds();
        } else if (AdConfig.getAllData.getCustomAds().size() > 0) {
            final CustomAd customAd = AdConfig.getAllData.getCustomAds().get(new Random().nextInt(AdConfig.getAllData.getCustomAds().size()));
            View inflate = ((LayoutInflater) this.context.getSystemService("layout_inflater")).inflate(R.layout.custom_banner_ad, (ViewGroup) null);
            RequestManager with = Glide.with(VideoPlayerManager.getInstance());
            with.load("" + customAd.getLogo()).into((ImageView) inflate.findViewById(R.id.img_icon));
            ((TextView) inflate.findViewById(R.id.txtappname)).setText(customAd.getAppname());
            ((TextView) inflate.findViewById(R.id.txtdescription)).setText(customAd.getAdsshort());
            ((Button) inflate.findViewById(R.id.btninstall)).setOnClickListener(new View.OnClickListener() {
                /* class com.videoplayer.hdmaxplayer.video.player.saxhdvideoplayer.appmanage.ads.AdsBanner.AnonymousClass3 */

                public void onClick(View view) {
                    try {
                        Context context = AdsBanner.this.context;
                        context.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("market://details?id=" + customAd.getPackagename())));
                    } catch (ActivityNotFoundException unused) {
                        Context context2 = AdsBanner.this.context;
                        context2.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("http://play.google.com/store/apps/details?id=" + customAd.getPackagename())));
                    }
                }
            });
            TypedValue typedValue = new TypedValue();
            if (this.context.getTheme().resolveAttribute(16843499, typedValue, true)) {
                this.rlBannerAd.getLayoutParams().height = TypedValue.complexToDimensionPixelSize(typedValue.data, this.context.getResources().getDisplayMetrics());
            }
            this.rlBannerAd.removeAllViews();
            this.rlBannerAd.addView(inflate);
        }
    }

    public void loadingErrorAdmob() {
        if (AdConfig.getAppData.getIsactive().equalsIgnoreCase(ExifInterface.GPS_MEASUREMENT_2D)) {
            bannerAdCustom();
        } else {
            bannerAdFacebook();
        }
    }

    public void loadingErrorFacebook() {
        if (AdConfig.getAppData.getIsactive().equalsIgnoreCase("1")) {
            bannerAdCustom();
        } else {
            bannerAdAdmob();
        }
    }

    public void noAds() {
        RelativeLayout relativeLayout = this.rlBannerAd;
        if (relativeLayout != null) {
            relativeLayout.setVisibility(8);
        }
    }
}
