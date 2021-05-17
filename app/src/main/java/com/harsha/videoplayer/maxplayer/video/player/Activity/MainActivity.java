package com.harsha.videoplayer.maxplayer.video.player.Activity;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.harsha.videoplayer.maxplayer.video.player.BuildConfig;
import com.harsha.videoplayer.maxplayer.video.player.Fragment.FolderFragment;
import com.harsha.videoplayer.maxplayer.video.player.Fragment.RecentFragment;
import com.harsha.videoplayer.maxplayer.video.player.Fragment.VideoFragment;
import com.harsha.videoplayer.maxplayer.video.player.Model.EventBus;
import com.harsha.videoplayer.maxplayer.video.player.R;
import com.harsha.videoplayer.maxplayer.video.player.Util.VideoPlayerManager;
import com.special.ResideMenu.ResideMenu;
import com.special.ResideMenu.ResideMenuItem;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    ImageView option;
    ImageView refresh;
    private ResideMenuItem itemAboutUs;
    private ResideMenuItem itemFolder;
    private ResideMenuItem itemHideVideos;
    private ResideMenuItem itemRateUs;
    private ResideMenuItem itemRecent;
    private ResideMenuItem itemShare;
    private ResideMenuItem itemVideos;
    private ResideMenu.OnMenuListener menuListener = new ResideMenu.OnMenuListener() {
        @Override
        public void closeMenu() {
        }

        @Override
        public void openMenu() {
        }
    };
    public int position;
    public ResideMenu resideMenu;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_main);
        setUpMenu();
        if (bundle == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment, new VideoFragment(), "fragment").setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit();
        }
        this.refresh = (ImageView) findViewById(R.id.refresh);
        this.option = (ImageView) findViewById(R.id.option_menu);
        this.refresh.setOnClickListener(view -> {
            EventBus event_Bus = new EventBus();
            event_Bus.setType(1);
            event_Bus.setValue(0);
            org.greenrobot.eventbus.EventBus.getDefault().post(event_Bus);
        });
        this.option.setOnClickListener(view -> {
            PopupMenu popupMenu = new PopupMenu(MainActivity.this, view);
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
        });
    }

    private void setUpMenu() {
        ResideMenu resideMenu2 = new ResideMenu(this);
        this.resideMenu = resideMenu2;
        resideMenu2.attachToActivity(this);
        this.resideMenu.setMenuListener(this.menuListener);
        this.resideMenu.setScaleValue(0.6f);
        this.itemVideos = new ResideMenuItem(this, (int) R.drawable.ic_baseline_videocam_24, "Video");
        this.itemFolder = new ResideMenuItem(this, (int) R.drawable.ic_baseline_folder_24, "Folder");
        this.itemRecent = new ResideMenuItem(this, (int) R.drawable.ic_baseline_access_time_24, "Recent");
        this.itemHideVideos = new ResideMenuItem(this, (int) R.drawable.ic_baseline_lock_24, "Hide Videos");
        this.itemRateUs = new ResideMenuItem(this, (int) R.drawable.ic_baseline_star_rate_24, "Rate Us");
        this.itemShare = new ResideMenuItem(this, (int) R.drawable.ic_baseline_share_24, "Share App");
        this.itemAboutUs = new ResideMenuItem(this, (int) R.drawable.ic_baseline_error_24, "About Us");
        this.itemVideos.setOnClickListener(this);
        this.itemFolder.setOnClickListener(this);
        this.itemRecent.setOnClickListener(this);
        this.itemHideVideos.setOnClickListener(this);
        this.itemRateUs.setOnClickListener(this);
        this.itemShare.setOnClickListener(this);
        this.itemAboutUs.setOnClickListener(this);
        this.resideMenu.addMenuItem(this.itemVideos, 0);
        this.resideMenu.addMenuItem(this.itemFolder, 0);
        this.resideMenu.addMenuItem(this.itemRecent, 0);
        this.resideMenu.addMenuItem(this.itemHideVideos, 0);
        this.resideMenu.addMenuItem(this.itemRateUs, 0);
        this.resideMenu.addMenuItem(this.itemShare, 0);
        this.resideMenu.addMenuItem(this.itemAboutUs, 0);
        findViewById(R.id.title_bar_left_menu).setOnClickListener(view -> MainActivity.this.resideMenu.openMenu(0));
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        return this.resideMenu.dispatchTouchEvent(motionEvent);
    }

    @SuppressLint("WrongConstant")
    public void onClick(View view) {
        if (view == this.itemVideos) {
            changeFragment(new VideoFragment());
        } else if (view == this.itemFolder) {
            changeFragment(new FolderFragment());
        } else if (view == this.itemRecent) {
            changeFragment(new RecentFragment());
        } else if (view == this.itemHideVideos) {
            startActivity(new Intent(this, SecureActivity.class));
        } else if (view == this.itemRateUs) {
            try {
                startActivity(new Intent("android.intent.action.VIEW", Uri.parse("market://details?id=" + getPackageName())));
            } catch (ActivityNotFoundException unused) {
                Toast.makeText(this, " unable to find market app", 1).show();
            }
        } else if (view == this.itemShare) {
            Intent intent = new Intent("android.intent.action.SEND");
            intent.setType("text/plain");
            intent.putExtra("android.intent.Extra.SUBJECT", getResources().getString(R.string.app_name));
            intent.putExtra("android.intent.Extra.TEXT", "\nLet me recommend you this application\n\n" + "https://play.google.com/store/apps/details?id=" +
                    BuildConfig.APPLICATION_ID + "\n\n");
            startActivity(Intent.createChooser(intent, "choose one"));
        } else if (view == this.itemAboutUs) {
            startActivity(new Intent(this, AboutActivity.class));
        }
        this.resideMenu.closeMenu();
    }

    public ResideMenu getResideMenu() {
        return this.resideMenu;
    }

    public void changeFragment(final Fragment fragment) {
        this.resideMenu.clearIgnoredViewList();
        if (this.resideMenu.isOpened()) {
            this.resideMenu.closeMenu();
        }
        new Handler() {

        }.postDelayed(() -> MainActivity.this.getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment, fragment, "fragment")
                .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit(), 600);
    }
}
