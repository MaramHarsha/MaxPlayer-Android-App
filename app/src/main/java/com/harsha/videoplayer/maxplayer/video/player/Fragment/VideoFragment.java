package com.harsha.videoplayer.maxplayer.video.player.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
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

import com.harsha.videoplayer.maxplayer.video.player.Adapter.VideoAdapter;
import com.harsha.videoplayer.maxplayer.video.player.Model.EventBus;
import com.harsha.videoplayer.maxplayer.video.player.R;
import com.harsha.videoplayer.maxplayer.video.player.Util.Utils;
import com.harsha.videoplayer.maxplayer.video.player.Util.VideoPlayerManager;
import com.harsha.videoplayer.maxplayer.video.player.Extra.MediaData;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class VideoFragment extends Fragment {
    public static ImageView ivnodata;
    public static RecyclerView videorecycler;
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
        this.view = layoutInflater.inflate(R.layout.fragment_video, viewGroup, false);
        initView();
        new Thread(new Runnable() {
            public void run() {
                VideoFragment.this.getVideo();
            }
        }).start();
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
            videorecycler.setVisibility(8);
            new Thread(new Runnable() {
                public void run() {
                   getVideo();
                }
            }).start();
        } else if (type == 2) {
            int value = event_Bus.getValue();
            if (value == 0) {
                Collections.sort(this.mediadataslist, new Comparator<MediaData>() {
                    public int compare(MediaData media_Data, MediaData media_Data2) {
                        return Long.compare(Long.parseLong(media_Data2.getLength()), Long.parseLong(media_Data.getLength()));
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
        }
    }

    @SuppressLint("WrongConstant")
    public void initAdapter(ArrayList<MediaData> arrayList, int i) {
        this.progress.setVisibility(8);
        if (arrayList.size() > 0) {
            videorecycler.setVisibility(0);
            ivnodata.setVisibility(8);
            VideoAdapter video_Adapter = new VideoAdapter(getActivity(), arrayList, i, 0);
            if (i == 0) {
                videorecycler.setLayoutManager(new LinearLayoutManager(getActivity(), 1, false));
            } else {
                videorecycler.setLayoutManager(new GridLayoutManager((Context) getActivity(), 3, 1, false));
            }
            videorecycler.setAdapter(video_Adapter);
            return;
        }
        videorecycler.setVisibility(8);
        ivnodata.setVisibility(0);
    }

    private void initView() {
        videorecycler = (RecyclerView) this.view.findViewById(R.id.video_recycler);
        this.progress = (ProgressBar) this.view.findViewById(R.id.progress);
        ivnodata = (ImageView) this.view.findViewById(R.id.iv_nodata);
    }

    public void getVideo() {
        ArrayList arrayList = new ArrayList();
        final ArrayList arrayList2 = new ArrayList();
        arrayList.clear();
        arrayList2.clear();
        this.mediadataslist.clear();
        char c = 0;
        char c2 = 1;
        char c3 = 2;
        char c4 = 3;
        int i = 4;
        String[] strArr = {"_data", "title", "date_modified", "bucket_display_name", "_size", "date_added", "duration", "resolution"};
        Cursor query = getActivity().getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, strArr, null, null, "datetaken DESC");
        if (query != null) {
            File file = null;
            while (query.moveToNext()) {
                String string = query.getString(query.getColumnIndex(strArr[c]));
                String string2 = query.getString(query.getColumnIndex(strArr[c2]));
                if (string2 == null) {
                    string2 = "";
                }
                String string3 = query.getString(query.getColumnIndex(strArr[c3]));
                String string4 = query.getString(query.getColumnIndex(strArr[c4]));
                long j = query.getLong(i);
                String string5 = query.getString(query.getColumnIndex(strArr[5]));
                String string6 = query.getString(query.getColumnIndex(strArr[6]));
                int i2 = query.getInt(6);
                String string7 = query.getString(7);
                try {
                    file = new File(string);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (!arrayList.contains(string4) && string4 != null) {
                    arrayList.add(string4);
                }
                if (file != null && file.exists()) {
                    MediaData media_Data = new MediaData();
                    media_Data.setName(string2);
                    media_Data.setPath(string);
                    media_Data.setFolder(string4);
                    media_Data.setLength(String.valueOf(j));
                    media_Data.setAddeddate(string5);
                    media_Data.setModifieddate(string3);
                    media_Data.setVideoDuration(i2);
                    media_Data.setLayoutType(2);
                    if (string6 == null) {
                        string6 = null;
                    } else if (!string6.contains(":")) {
                        string6 = Utils.makeShortTimeString(getActivity(), Long.parseLong(string6) / 1000);
                    }
                    if (string7 == null || TextUtils.isEmpty(string7)) {
                        media_Data.setResolution("0");
                    } else {
                        media_Data.setResolution(string7);
                    }
                    if (string6 != null) {
                        media_Data.setDuration(string6);
                        arrayList2.add(media_Data);
                    }
                }
                c = 0;
                c2 = 1;
                c3 = 2;
                c4 = 3;
                i = 4;
            }
            query.close();
            this.mediadataslist.addAll(arrayList2);
        }
        getActivity().runOnUiThread(new Runnable() {
            public void run() {
                VideoFragment.this.initAdapter(arrayList2, VideoPlayerManager.getViewBy());
            }
        });
    }
}
