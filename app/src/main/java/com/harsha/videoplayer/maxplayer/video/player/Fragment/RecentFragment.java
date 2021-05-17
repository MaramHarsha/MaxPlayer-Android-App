package com.harsha.videoplayer.maxplayer.video.player.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.harsha.videoplayer.maxplayer.video.player.Adapter.VideoAdapter;
import com.harsha.videoplayer.maxplayer.video.player.Model.EventBus;
import com.harsha.videoplayer.maxplayer.video.player.R;
import com.harsha.videoplayer.maxplayer.video.player.Util.VideoPlayerManager;
import com.harsha.videoplayer.maxplayer.video.player.Extra.MediaData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class RecentFragment extends Fragment {
    @SuppressLint("StaticFieldLeak")
    public static ImageView ivnodata;
    public static RecyclerView recentrecycler;
    ArrayList<MediaData> mediadataslist = new ArrayList<>();
    private ProgressBar progress;
    private View view;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        org.greenrobot.eventbus.EventBus.getDefault().register(this);
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        this.view = layoutInflater.inflate(R.layout.fragment_recent, viewGroup, false);
        @SuppressLint("WrongConstant")
        NetworkInfo activeNetworkInfo = ((ConnectivityManager) getActivity().getSystemService("connectivity")).getActiveNetworkInfo();
        if (activeNetworkInfo != null) {
            activeNetworkInfo.isConnected();
        }
        initView();
        getVideo();
        return this.view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        org.greenrobot.eventbus.EventBus.getDefault().unregister(this);
    }

    @SuppressLint("WrongConstant")
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(EventBus event_Bus) {
        int type = event_Bus.getType();
        if (type == 0) {
            initAdapter(this.mediadataslist, event_Bus.getValue());
        } else if (type == 1) {
            this.progress.setVisibility(0);
            recentrecycler.setVisibility(8);
            new Thread(new Runnable() {
                public void run() {
                    RecentFragment.this.getVideo();
                }
            }).start();
        } else if (type == 2) {
            int value = event_Bus.getValue();
            if (value == 0) {
                Collections.sort(this.mediadataslist, new Comparator<MediaData>() {
                    public int compare(MediaData media_Data, MediaData media_Data2) {
                        return (Long.compare(Long.parseLong(media_Data2.getLength()), Long.parseLong(media_Data.getLength())));
                    }
                });
                initAdapter(this.mediadataslist, VideoPlayerManager.getViewBy());
            } else if (value == 1) {
                Collections.sort(this.mediadataslist, new Comparator<MediaData>() {
                    public int compare(MediaData media_Data, MediaData media_Data2) {
                        return Long.compare(Integer.parseInt(media_Data2.getDuration()), Integer.parseInt(media_Data.getDuration()));
                    }
                });
                initAdapter(this.mediadataslist, VideoPlayerManager.getViewBy());
            } else if (value == 2) {
                Collections.sort(this.mediadataslist, new Comparator<MediaData>() {
                    public int compare(MediaData media_Data, MediaData media_Data2) {
                        return media_Data.getName().compareTo(media_Data2.getName());
                    }
                });
                initAdapter(this.mediadataslist, VideoPlayerManager.getViewBy());
            } else if (value == 3) {
                Collections.sort(this.mediadataslist, new Comparator<MediaData>() {
                    public int compare(MediaData media_Data, MediaData media_Data2) {
                        return media_Data2.getModifieddate().compareTo(media_Data.getModifieddate());
                    }
                });
                initAdapter(this.mediadataslist, VideoPlayerManager.getViewBy());
            }
        } else if (type == 3) {
            this.progress.setVisibility(0);
            recentrecycler.setVisibility(8);
            new Thread(new Runnable() {
                public void run() {
                    RecentFragment.this.getVideo();
                }
            }).start();
        }
    }

    @SuppressLint("WrongConstant")
    public void getVideo() {
        this.mediadataslist.clear();
        if (!TextUtils.isEmpty(VideoPlayerManager.getRecentPlay())) {
            final ArrayList arrayList = (ArrayList) new Gson().fromJson(VideoPlayerManager.getRecentPlay(), new TypeToken<List<MediaData>>() {

            }.getType());
            Collections.reverse(arrayList);
            this.mediadataslist.addAll(arrayList);
            getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    RecentFragment.this.initAdapter(arrayList, VideoPlayerManager.getViewBy());
                }
            });
            return;
        }
        this.progress.setVisibility(8);
        recentrecycler.setVisibility(8);
        ivnodata.setVisibility(0);
    }

    @SuppressLint("WrongConstant")
    public void initAdapter(ArrayList<MediaData> arrayList, int i) {
        this.progress.setVisibility(8);
        if (arrayList.size() > 0) {
            recentrecycler.setVisibility(0);
            ivnodata.setVisibility(8);
            VideoAdapter video_Adapter = new VideoAdapter(getActivity(), arrayList, i, 2);
            if (i == 0) {
                recentrecycler.setLayoutManager(new LinearLayoutManager(getActivity(), 1, false));
            } else {
                recentrecycler.setLayoutManager(new GridLayoutManager((Context) getActivity(), 3, 1, false));
            }
            recentrecycler.setAdapter(video_Adapter);
            return;
        }
        recentrecycler.setVisibility(8);
        ivnodata.setVisibility(0);
    }

    private void initView() {
        recentrecycler = (RecyclerView) this.view.findViewById(R.id.hide_recycler);
        progress = (ProgressBar) this.view.findViewById(R.id.progress);
        ivnodata = (ImageView) this.view.findViewById(R.id.iv_nodata);
    }
}
