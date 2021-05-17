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
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.exifinterface.media.ExifInterface;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdOptionsView;
import com.facebook.ads.NativeAd;
import com.facebook.ads.NativeAdLayout;
import com.facebook.ads.NativeAdListener;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.VideoController;
import com.google.android.gms.ads.VideoOptions;
import com.google.android.gms.ads.formats.MediaView;
import com.google.android.gms.ads.formats.NativeAdOptions;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAdView;
import com.harsha.videoplayer.maxplayer.video.player.R;
import com.harsha.videoplayer.maxplayer.video.player.Util.VideoPlayerManager;
import com.harsha.videoplayer.maxplayer.video.player.appmanage.AdConfig;
import com.harsha.videoplayer.maxplayer.video.player.appmanage.model.CustomAd;
import com.harsha.videoplayer.maxplayer.video.player.appmanage.network.NetworkConnectivityReceiver;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AdsNative {
    Activity activity;
    FrameLayout layout;

    public AdsNative(Activity activity2, FrameLayout frameLayout) {
        this.activity = activity2;
        this.layout = frameLayout;
        NativeAds();
    }

    public void NativeAds() {
        if (AdConfig.getAppData == null || AdConfig.getAllData == null || !NetworkConnectivityReceiver.isConnected()) {
            noAds();
        } else if (this.layout == null) {
        } else {
            if (AdConfig.getAppData.getIsactive().equalsIgnoreCase("1")) {
                nativeAdmobe();
            } else if (AdConfig.getAppData.getIsactive().equalsIgnoreCase(ExifInterface.GPS_MEASUREMENT_2D)) {
                nativeFacebook();
            } else if (AdConfig.getAppData.getIsactive().equalsIgnoreCase(ExifInterface.GPS_MEASUREMENT_3D)) {
                noAds();
            } else if (AdConfig.getAppData.getIsactive().equalsIgnoreCase("4")) {
                nativeCustom();
            }
        }
    }

    public void nativeAdmobe() {
        AdLoader.Builder builder = new AdLoader.Builder(this.activity, AdConfig.getAppData.getAdmobnative());
        builder.forUnifiedNativeAd(new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {
            @Override
            public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {
                UnifiedNativeAdView unifiedNativeAdView = (UnifiedNativeAdView) AdsNative.this.activity.getLayoutInflater().inflate(R.layout.native_ad_admob, (ViewGroup) null);
                AdsNative.this.populateUnifiedNativeAdView(unifiedNativeAd, unifiedNativeAdView);
                AdsNative.this.layout.removeAllViews();
                AdsNative.this.layout.addView(unifiedNativeAdView);
            }
        });
        builder.withNativeAdOptions(new NativeAdOptions.Builder().setVideoOptions(new VideoOptions.Builder().setStartMuted(true).build()).build());
        builder.withAdListener(new AdListener() {
            /* class com.videoplayer.hdmaxplayer.video.player.saxhdvideoplayer.appmanage.ads.AdsNative.AnonymousClass2 */

            @Override // com.google.android.gms.ads.AdListener
            public void onAdFailedToLoad(int i) {
                AdsNative.this.nativeCustom();
            }
        }).build().loadAd(new AdRequest.Builder().build());
    }

    public void nativeFacebook() {
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(-1, -2);
        ScrollView scrollView = new ScrollView(this.activity);
        scrollView.setLayoutParams(layoutParams);
        this.layout.addView(scrollView);
        FrameLayout.LayoutParams layoutParams2 = new FrameLayout.LayoutParams(-1, -2);
        final NativeAdLayout nativeAdLayout = new NativeAdLayout(this.activity);
        nativeAdLayout.setLayoutParams(layoutParams2);
        scrollView.addView(nativeAdLayout);
        Activity activity2 = this.activity;
        final NativeAd nativeAd = new NativeAd(activity2, "" + AdConfig.getAppData.getFbnative());
        nativeAd.loadAd(nativeAd.buildLoadAdConfig().withAdListener(new NativeAdListener() {
            @Override
            public void onAdClicked(Ad ad) {
            }

            @Override
            public void onLoggingImpression(Ad ad) {
            }

            @Override
            public void onMediaDownloaded(Ad ad) {
            }

            @Override
            public void onError(Ad ad, AdError adError) {
                Log.e("getFbnative", "======" + adError.getErrorMessage());
                AdsNative.this.nativeCustom();
            }

            @Override
            public void onAdLoaded(Ad ad) {
                if (nativeAd != null && nativeAd == ad) {
                    AdsNative adsNative = AdsNative.this;
                    adsNative.inflateAd(nativeAd, nativeAdLayout, adsNative.activity);
                }
            }
        }).build());
    }

    public void nativeCustom() {
        if (AdConfig.getAllData == null || AdConfig.getAllData.getCustomAds() == null) {
            noAds();
        } else if (AdConfig.getAllData.getCustomAds().size() > 0) {
            int nextInt = new Random().nextInt(AdConfig.getAllData.getCustomAds().size());
            View inflate = ((LayoutInflater) this.activity.getSystemService("layout_inflater")).inflate(R.layout.custom_native_ad, (ViewGroup) null);
            final CustomAd customAd = AdConfig.getAllData.getCustomAds().get(nextInt);
            RequestManager with = Glide.with(VideoPlayerManager.getInstance());
            with.load("" + customAd.getLogo()).into((ImageView) inflate.findViewById(R.id.img_icon));
            ((TextView) inflate.findViewById(R.id.txtappname)).setText(customAd.getAppname());
            ((TextView) inflate.findViewById(R.id.txtdescription)).setText(customAd.getAdsshort());
            RequestManager with2 = Glide.with(VideoPlayerManager.getInstance());
            with2.load("" + customAd.getAdsBigImage()).into((ImageView) inflate.findViewById(R.id.img_big_img));
            ((Button) inflate.findViewById(R.id.btninstall)).setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    try {
                        Activity activity = AdsNative.this.activity;
                        activity.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("market://details?id=" + customAd.getPackagename())));
                    } catch (ActivityNotFoundException unused) {
                        Activity activity2 = AdsNative.this.activity;
                        activity2.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("http://play.google.com/store/apps/details?id=" + customAd.getPackagename())));
                    }
                }
            });
            this.layout.addView(inflate);
        }
    }

    private void populateUnifiedNativeAdView(UnifiedNativeAd unifiedNativeAd, UnifiedNativeAdView unifiedNativeAdView) {
        unifiedNativeAdView.setMediaView((MediaView) unifiedNativeAdView.findViewById(R.id.ad_media));
        unifiedNativeAdView.setHeadlineView(unifiedNativeAdView.findViewById(R.id.ad_headline));
        unifiedNativeAdView.setBodyView(unifiedNativeAdView.findViewById(R.id.ad_body));
        unifiedNativeAdView.setCallToActionView(unifiedNativeAdView.findViewById(R.id.ad_call_to_action));
        unifiedNativeAdView.setIconView(unifiedNativeAdView.findViewById(R.id.ad_app_icon));
        unifiedNativeAdView.setPriceView(unifiedNativeAdView.findViewById(R.id.ad_price));
        unifiedNativeAdView.setStarRatingView(unifiedNativeAdView.findViewById(R.id.ad_stars));
        unifiedNativeAdView.setStoreView(unifiedNativeAdView.findViewById(R.id.ad_store));
        unifiedNativeAdView.setAdvertiserView(unifiedNativeAdView.findViewById(R.id.ad_advertiser));
        ((TextView) unifiedNativeAdView.getHeadlineView()).setText(unifiedNativeAd.getHeadline());
        if (unifiedNativeAd.getBody() == null) {
            unifiedNativeAdView.getBodyView().setVisibility(4);
        } else {
            unifiedNativeAdView.getBodyView().setVisibility(0);
            ((TextView) unifiedNativeAdView.getBodyView()).setText(unifiedNativeAd.getBody());
        }
        if (unifiedNativeAd.getCallToAction() == null) {
            unifiedNativeAdView.getCallToActionView().setVisibility(4);
        } else {
            unifiedNativeAdView.getCallToActionView().setVisibility(0);
            ((Button) unifiedNativeAdView.getCallToActionView()).setText(unifiedNativeAd.getCallToAction());
        }
        if (unifiedNativeAd.getIcon() == null) {
            unifiedNativeAdView.getIconView().setVisibility(8);
        } else {
            ((ImageView) unifiedNativeAdView.getIconView()).setImageDrawable(unifiedNativeAd.getIcon().getDrawable());
            unifiedNativeAdView.getIconView().setVisibility(0);
        }
        if (unifiedNativeAd.getPrice() == null) {
            unifiedNativeAdView.getPriceView().setVisibility(4);
        } else {
            unifiedNativeAdView.getPriceView().setVisibility(0);
            ((TextView) unifiedNativeAdView.getPriceView()).setText(unifiedNativeAd.getPrice());
        }
        if (unifiedNativeAd.getStore() == null) {
            unifiedNativeAdView.getStoreView().setVisibility(4);
        } else {
            unifiedNativeAdView.getStoreView().setVisibility(0);
            ((TextView) unifiedNativeAdView.getStoreView()).setText(unifiedNativeAd.getStore());
        }
        if (unifiedNativeAd.getStarRating() == null) {
            unifiedNativeAdView.getStarRatingView().setVisibility(4);
        } else {
            ((RatingBar) unifiedNativeAdView.getStarRatingView()).setRating(unifiedNativeAd.getStarRating().floatValue());
            unifiedNativeAdView.getStarRatingView().setVisibility(0);
        }
        if (unifiedNativeAd.getAdvertiser() == null) {
            unifiedNativeAdView.getAdvertiserView().setVisibility(4);
        } else {
            ((TextView) unifiedNativeAdView.getAdvertiserView()).setText(unifiedNativeAd.getAdvertiser());
            unifiedNativeAdView.getAdvertiserView().setVisibility(0);
        }
        unifiedNativeAdView.setNativeAd(unifiedNativeAd);
        VideoController videoController = unifiedNativeAd.getVideoController();
        if (videoController.hasVideoContent()) {
            videoController.setVideoLifecycleCallbacks(new VideoController.VideoLifecycleCallbacks() {
                /* class com.videoplayer.hdmaxplayer.video.player.saxhdvideoplayer.appmanage.ads.AdsNative.AnonymousClass5 */

                @Override // com.google.android.gms.ads.VideoController.VideoLifecycleCallbacks
                public void onVideoEnd() {
                    super.onVideoEnd();
                }
            });
        }
    }


    private void inflateAd(NativeAd nativeAd, NativeAdLayout nativeAdLayout, Activity activity2) {
        nativeAd.unregisterView();
        int i = 0;
        LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(activity2).inflate(R.layout.native_ad_fb, (ViewGroup) nativeAdLayout, false);
        nativeAdLayout.addView(linearLayout);
        LinearLayout linearLayout2 = (LinearLayout) linearLayout.findViewById(R.id.ad_choices_container);
        AdOptionsView adOptionsView = new AdOptionsView(activity2, nativeAd, nativeAdLayout);
        linearLayout2.removeAllViews();
        linearLayout2.addView(adOptionsView, 0);
        com.facebook.ads.MediaView mediaView = (com.facebook.ads.MediaView) linearLayout.findViewById(R.id.native_ad_icon);
        TextView textView = (TextView) linearLayout.findViewById(R.id.native_ad_title);
        com.facebook.ads.MediaView mediaView2 = (com.facebook.ads.MediaView) linearLayout.findViewById(R.id.native_ad_media);
        TextView textView2 = (TextView) linearLayout.findViewById(R.id.native_ad_sponsored_label);
        Button button = (Button) linearLayout.findViewById(R.id.native_ad_call_to_action);
        textView.setText(nativeAd.getAdvertiserName());
        ((TextView) linearLayout.findViewById(R.id.native_ad_body)).setText(nativeAd.getAdBodyText());
        ((TextView) linearLayout.findViewById(R.id.native_ad_social_context)).setText(nativeAd.getAdSocialContext());
        if (!nativeAd.hasCallToAction()) {
            i = 4;
        }
        button.setVisibility(i);
        button.setText(nativeAd.getAdCallToAction());
        textView2.setText(nativeAd.getSponsoredTranslation());
        List<View> arrayList = new ArrayList<>();
        arrayList.add(textView);
        arrayList.add(button);
        nativeAd.registerViewForInteraction(linearLayout, mediaView2, mediaView, arrayList);
    }

    public void loadingErrorAdmob() {
        if (AdConfig.getAppData.getIsactive().equalsIgnoreCase(ExifInterface.GPS_MEASUREMENT_2D)) {
            nativeCustom();
        } else {
            nativeFacebook();
        }
    }

    public void loadingErrorFacebook() {
        if (AdConfig.getAppData.getIsactive().equalsIgnoreCase("1")) {
            nativeCustom();
        } else {
            nativeAdmobe();
        }
    }

    @SuppressLint("WrongConstant")
    public void noAds() {
        FrameLayout frameLayout = this.layout;
        if (frameLayout != null) {
            frameLayout.setVisibility(8);
        }
    }
}
