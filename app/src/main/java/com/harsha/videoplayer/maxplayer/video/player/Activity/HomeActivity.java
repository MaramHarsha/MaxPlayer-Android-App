package com.harsha.videoplayer.maxplayer.video.player.Activity;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.harsha.videoplayer.maxplayer.video.player.Adapter.HomeAdapter;
import com.harsha.videoplayer.maxplayer.video.player.BuildConfig;
import com.harsha.videoplayer.maxplayer.video.player.Model.EventBus;
import com.harsha.videoplayer.maxplayer.video.player.R;
import com.harsha.videoplayer.maxplayer.video.player.Util.VideoPlayerManager;
import com.harsha.videoplayer.maxplayer.video.player.appmanage.ads.AdsBanner;
import com.harsha.videoplayer.maxplayer.video.player.appmanage.ads.AdsFullScreen;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {
    public AdsFullScreen adsFullScreen;
    public DrawerLayout backhide;
    FrameLayout frameLayoutCustomAd;
    private ImageView hidevideo;
    private NavigationView navlay;
    private ImageView navigation;
    private ImageView optionmenu;
    int pos = 0;
    private ImageView refresh;
    private TabLayout tablay;
    public ViewPager viewpager;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_home);
        initView();
        initListener();
        initTab();
        new AdsBanner(this, (RelativeLayout) findViewById(R.id.rl_ad), (RelativeLayout) findViewById(R.id.rl_layout)).loadBannerAd();
        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.frameLayoutCustomAd);
        this.frameLayoutCustomAd = frameLayout;
        AdsFullScreen adsFullScreen2 = new AdsFullScreen(this, frameLayout);
        this.adsFullScreen = adsFullScreen2;
        adsFullScreen2.getIsResult(false);
        this.adsFullScreen.loadFullAd();
    }

    private void initTab() {
        TabLayout tabLayout = this.tablay;
        tabLayout.addTab(tabLayout.newTab().setText("Folder"));
        TabLayout tabLayout2 = this.tablay;
        tabLayout2.addTab(tabLayout2.newTab().setText("All Videos"));
        TabLayout tabLayout3 = this.tablay;
        tabLayout3.addTab(tabLayout3.newTab().setText("Recent"));
        this.tablay.setTabGravity(0);
        this.viewpager.setAdapter(new HomeAdapter(this, getSupportFragmentManager(), this.tablay.getTabCount()));
        this.viewpager.setOffscreenPageLimit(3);
        this.viewpager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(this.tablay));
        this.viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrollStateChanged(int i) {
            }

            @Override
            public void onPageScrolled(int i, float f, int i2) {
            }

            @Override
            public void onPageSelected(int i) {
                HomeActivity.this.pos = i;
            }
        });
        this.tablay.addOnTabSelectedListener((TabLayout.OnTabSelectedListener) new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                HomeActivity.this.viewpager.setCurrentItem(tab.getPosition());
                HomeActivity.this.pos = tab.getPosition();
                ContextCompat.getColor(HomeActivity.this, R.color.white);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                ContextCompat.getColor(HomeActivity.this, R.color.light_white);
            }
        });
        ContextCompat.getColor(this, R.color.white);
        ContextCompat.getColor(this, R.color.light_white);
    }

    private void initListener() {
        this.hidevideo.setOnClickListener(this);
        this.navigation.setOnClickListener(this);
        this.refresh.setOnClickListener(this);
        this.optionmenu.setOnClickListener(this);
        this.navlay.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            @SuppressLint("WrongConstant")
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.nav_about:
                        HomeActivity.this.myStartActivity(new Intent(HomeActivity.this, AboutActivity.class));
                        break;
                    case R.id.nav_folder:
                        HomeActivity.this.viewpager.setCurrentItem(0);
                        break;
                    case R.id.nav_privacy:
                        HomeActivity.this.myStartActivity(new Intent(HomeActivity.this, SecureActivity.class));
                        break;
                    case R.id.nav_rate:
                        try {
                            HomeActivity.this.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("market://details?id=" + HomeActivity.this.getPackageName())));
                            break;
                        } catch (ActivityNotFoundException unused) {
                            Toast.makeText(HomeActivity.this, " unable to find market app", 1).show();
                            break;
                        }
                    case R.id.nav_recent:
                        HomeActivity.this.viewpager.setCurrentItem(2);
                        break;
                    case R.id.nav_share:
                        try {
                            Intent intent = new Intent("android.intent.action.SEND");
                            intent.setType("text/plain");
                            intent.putExtra("android.intent.Extra.SUBJECT", HomeActivity.this.getResources().getString(R.string.app_name));
                            intent.putExtra("android.intent.Extra.TEXT", "\nLet me recommend you this application\n\n" + "https://play.google.com/store/apps/details?id=" +
                                    BuildConfig.APPLICATION_ID + "\n\n");
                            HomeActivity.this.startActivity(Intent.createChooser(intent, "choose one"));
                            break;
                        } catch (Exception unused2) {
                            break;
                        }
                    case R.id.nav_video:
                        HomeActivity.this.viewpager.setCurrentItem(1);
                        break;
                }
                HomeActivity.this.backhide.closeDrawer(GravityCompat.START);
                return false;
            }
        });
    }

    private void initView() {
        this.navigation = (ImageView) findViewById(R.id.navigation);
        this.refresh = (ImageView) findViewById(R.id.refresh);
        this.backhide = (DrawerLayout) findViewById(R.id.drawer_lay);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_lay);
        this.navlay = navigationView;
        navigationView.setItemIconTintList(null);
        this.tablay = (TabLayout) findViewById(R.id.tab_lay);
        this.viewpager = (ViewPager) findViewById(R.id.view_pager);
        this.optionmenu = (ImageView) findViewById(R.id.option_menu);
        this.hidevideo = (ImageView) findViewById(R.id.hide_video);
    }

    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.navigation) {
            this.backhide.openDrawer(GravityCompat.START);
        } else if (id == R.id.option_menu) {
            PopupMenu popupMenu = new PopupMenu(this, view);
            popupMenu.inflate(R.menu.home_menu);
            popupMenu.getMenu();
            popupMenu.setOnMenuItemClickListener(menuItem -> {
                switch (menuItem.getItemId()) {
                    case R.id.menu_date:
                        EventBus event_Bus = new EventBus();
                        event_Bus.setType(2);
                        event_Bus.setValue(3);
                        org.greenrobot.eventbus.EventBus.getDefault().post(event_Bus);
                        break;
                    case R.id.menu_grid:
                        if (VideoPlayerManager.getViewBy() != 1) {
                            VideoPlayerManager.putViewBy(1);
                            EventBus event_Bus2 = new EventBus();
                            event_Bus2.setType(0);
                            event_Bus2.setValue(1);
                            org.greenrobot.eventbus.EventBus.getDefault().post(event_Bus2);
                            break;
                        }
                        break;
                    case R.id.menu_list:
                        if (VideoPlayerManager.getViewBy() != 0) {
                            VideoPlayerManager.putViewBy(0);
                            EventBus event_Bus3 = new EventBus();
                            event_Bus3.setType(0);
                            event_Bus3.setValue(0);
                            org.greenrobot.eventbus.EventBus.getDefault().post(event_Bus3);
                            break;
                        }
                        break;
                    case R.id.menu_name:
                        EventBus event_Bus4 = new EventBus();
                        event_Bus4.setType(2);
                        event_Bus4.setValue(2);
                        org.greenrobot.eventbus.EventBus.getDefault().post(event_Bus4);
                        break;
                    case R.id.menu_size:
                        EventBus event_Bus5 = new EventBus();
                        event_Bus5.setType(2);
                        event_Bus5.setValue(0);
                        org.greenrobot.eventbus.EventBus.getDefault().post(event_Bus5);
                        break;
                }
                return false;
            });
            popupMenu.show();
        } else if (id == R.id.refresh) {
            EventBus event_Bus = new EventBus();
            event_Bus.setType(1);
            event_Bus.setValue(0);
            org.greenrobot.eventbus.EventBus.getDefault().post(event_Bus);
        } else if (id == R.id.hide_video) {
            myStartActivity(new Intent(this, SecureActivity.class));
        }
    }

    @SuppressLint("WrongConstant")
    @Override
    public void onBackPressed() {
        FrameLayout frameLayout;
        if (this.adsFullScreen != null && (frameLayout = this.frameLayoutCustomAd) != null && frameLayout.getVisibility() == 0) {
            this.frameLayoutCustomAd.setVisibility(8);
            this.adsFullScreen.openNextActivity();
        } else if (this.backhide.isDrawerOpen(GravityCompat.START)) {
            this.backhide.closeDrawer(GravityCompat.START);
        } else {
            super.finish();
        }
    }

    public void myStartActivity(Intent intent) {
        AdsFullScreen adsFullScreen2 = this.adsFullScreen;
        if (adsFullScreen2 == null || this.frameLayoutCustomAd == null) {
            startActivity(intent);
        } else {
            adsFullScreen2.showFullAd(intent);
        }
    }
}
