package com.harsha.videoplayer.maxplayer.video.player.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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

import com.harsha.videoplayer.maxplayer.video.player.Adapter.FolderAdapter;
import com.harsha.videoplayer.maxplayer.video.player.Model.EventBus;
import com.harsha.videoplayer.maxplayer.video.player.Model.Folder;
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

public class FolderFragment extends Fragment {
    ArrayList<Folder> folderlist = new ArrayList<>();
    private RecyclerView folderrecycler;
    private ImageView ivnodata;
    private ProgressBar progress;
    private View view;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        org.greenrobot.eventbus.EventBus.getDefault().register(this);
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        this.view = layoutInflater.inflate(R.layout.fragment_folder, viewGroup, false);
        @SuppressLint("WrongConstant") NetworkInfo activeNetworkInfo = ((ConnectivityManager) getActivity().getSystemService("connectivity")).getActiveNetworkInfo();
        if (activeNetworkInfo != null) {
            activeNetworkInfo.isConnected();
        }
        initView();
        new Thread(new Runnable() {
            public void run() {
                FolderFragment.this.getVideo();
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
            initAdapter(this.folderlist, event_Bus.getValue());
        } else if (type == 1) {
            this.progress.setVisibility(0);
            this.folderrecycler.setVisibility(8);
            new Thread(new Runnable() {
                public void run() {
                    getVideo();
                }
            }).start();
        } else if (type == 2) {
            int value = event_Bus.getValue();
            if (value == 0) {
                Collections.sort(this.folderlist, new Comparator<Folder>() {
                    public int compare(Folder folder, Folder folder2) {
                        return Long.compare(folder2.getMedia_data().size(), folder.getMedia_data().size());
                    }
                });
                initAdapter(this.folderlist, VideoPlayerManager.getViewBy());
            } else if (value == 2) {
                Collections.sort(this.folderlist, new Comparator<Folder>() {
                    public int compare(Folder folder, Folder folder2) {
                        return folder.getName().compareTo(folder2.getName());
                    }
                });
                initAdapter(this.folderlist, VideoPlayerManager.getViewBy());
            } else if (value == 3) {
                Collections.sort(this.folderlist, new Comparator<Folder>() {
                    public int compare(Folder folder, Folder folder2) {
                        return (Long.compare(new File(folder2.getMedia_data().get(0).getPath()).getParentFile().lastModified(),
                                new File(folder.getMedia_data().get(0).getPath()).getParentFile().lastModified()));
                    }
                });
                initAdapter(this.folderlist, VideoPlayerManager.getViewBy());
            }
        }
    }

    private void initView() {
        this.folderrecycler = (RecyclerView) this.view.findViewById(R.id.folder_recycler);
        this.progress = (ProgressBar) this.view.findViewById(R.id.progress);
        this.ivnodata = (ImageView) this.view.findViewById(R.id.iv_nodata);
    }

    public void getVideo() {
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        arrayList.clear();
        arrayList2.clear();
        this.folderlist.clear();
        char c = 0;
        char c2 = 1;
        char c3 = 2;
        char c4 = 3;
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
                long j = query.getLong(4);
                String string5 = query.getString(query.getColumnIndex(strArr[5]));
                String string6 = query.getString(query.getColumnIndex(strArr[6]));
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
            }
            query.close();
        }
        final ArrayList arrayList3 = new ArrayList();
        if (arrayList.size() > 0) {
            for (int i = 0; i < arrayList.size(); i++) {
                Folder folder = new Folder();
                folder.setName((String) arrayList.get(i));
                ArrayList<MediaData> arrayList4 = new ArrayList<>();
                for (int i2 = 0; i2 < arrayList2.size(); i2++) {
                    if (((MediaData) arrayList2.get(i2)).getFolder().equals(arrayList.get(i))) {
                        arrayList4.add((MediaData) arrayList2.get(i2));
                    }
                }
                folder.setMedia_data(arrayList4);
                if (arrayList4.size() != 0) {
                    arrayList3.add(folder);
                }
            }
        }
        this.folderlist.addAll(arrayList3);
        getActivity().runOnUiThread(new Runnable() {
            public void run() {
                FolderFragment.this.initAdapter(arrayList3, VideoPlayerManager.getViewBy());
            }
        });
    }

    @SuppressLint("WrongConstant")
    public void initAdapter(ArrayList<Folder> arrayList, int i) {
        this.progress.setVisibility(8);
        if (arrayList.size() > 0) {
            this.folderrecycler.setVisibility(0);
            this.ivnodata.setVisibility(8);
            FolderAdapter folder_Adapter = new FolderAdapter(getActivity(), arrayList, i);
            if (i == 0) {
                this.folderrecycler.setLayoutManager(new LinearLayoutManager(getActivity(), 1, false));
            } else {
                this.folderrecycler.setLayoutManager(new GridLayoutManager((Context) getActivity(), 2, 1, false));
            }
            this.folderrecycler.setAdapter(folder_Adapter);
            return;
        }
        this.folderrecycler.setVisibility(8);
        this.ivnodata.setVisibility(0);
    }
}
