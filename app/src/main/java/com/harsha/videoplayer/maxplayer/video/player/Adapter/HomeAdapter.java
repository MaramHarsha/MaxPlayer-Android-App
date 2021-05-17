package com.harsha.videoplayer.maxplayer.video.player.Adapter;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.harsha.videoplayer.maxplayer.video.player.Fragment.FolderFragment;
import com.harsha.videoplayer.maxplayer.video.player.Fragment.RecentFragment;
import com.harsha.videoplayer.maxplayer.video.player.Fragment.VideoFragment;

public class HomeAdapter extends FragmentPagerAdapter {
    private final Context myContext;
    int totalTabs;

    public HomeAdapter(Context context, FragmentManager fragmentManager, int i) {
        super(fragmentManager);
        this.myContext = context;
        this.totalTabs = i;
    }

    @Override
    public Fragment getItem(int i) {
        if (i == 0) {
            return new FolderFragment();
        }
        if (i == 1) {
            return new VideoFragment();
        }
        if (i != 2) {
            return null;
        }
        return new RecentFragment();
    }

    @Override
    public int getCount() {
        return this.totalTabs;
    }
}
