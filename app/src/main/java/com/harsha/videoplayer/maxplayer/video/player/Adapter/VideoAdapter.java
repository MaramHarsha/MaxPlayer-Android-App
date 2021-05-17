package com.harsha.videoplayer.maxplayer.video.player.Adapter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.harsha.videoplayer.maxplayer.video.player.Activity.HomeActivity;
import com.harsha.videoplayer.maxplayer.video.player.Activity.SecureActivity;
import com.harsha.videoplayer.maxplayer.video.player.Activity.VideoPlayerActivity;
import com.harsha.videoplayer.maxplayer.video.player.Activity.VideolistActivity;
import com.harsha.videoplayer.maxplayer.video.player.Database.Database;
import com.harsha.videoplayer.maxplayer.video.player.Dialog.VideoDetailsDialog;
import com.harsha.videoplayer.maxplayer.video.player.Extra.MediaData;
import com.harsha.videoplayer.maxplayer.video.player.Model.EventBus;
import com.harsha.videoplayer.maxplayer.video.player.Model.HideData;
import com.harsha.videoplayer.maxplayer.video.player.R;
import com.harsha.videoplayer.maxplayer.video.player.Util.Constant;
import com.harsha.videoplayer.maxplayer.video.player.Util.Utils;
import com.harsha.videoplayer.maxplayer.video.player.Util.VideoPlayerManager;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.harsha.videoplayer.maxplayer.video.player.Activity.VideolistActivity.videolist;
import static com.harsha.videoplayer.maxplayer.video.player.Fragment.RecentFragment.ivnodata;
import static com.harsha.videoplayer.maxplayer.video.player.Fragment.RecentFragment.recentrecycler;
import static com.harsha.videoplayer.maxplayer.video.player.Fragment.VideoFragment.videorecycler;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.ViewHolder> {
    FragmentActivity activity;
    public final Database database;
    RecyclerView.ViewHolder holder;
    int i;
    LayoutInflater inflater;
    ArrayList<MediaData> mediadatas;
    int type;


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView duration;
        TextView videodate;
        ImageView videooption;
        TextView videosize;
        ImageView videothumb;
        TextView videotitle;

        public ViewHolder(View view) {
            super(view);
            this.videotitle = (TextView) view.findViewById(R.id.video_title);
            this.videodate = (TextView) view.findViewById(R.id.video_date);
            this.videosize = (TextView) view.findViewById(R.id.video_size);
            this.duration = (TextView) view.findViewById(R.id.duration);
            this.videothumb = (ImageView) view.findViewById(R.id.video_thumb);
            this.videooption = (ImageView) view.findViewById(R.id.video_option);
        }
    }

    public VideoAdapter(FragmentActivity fragmentActivity, ArrayList<MediaData> arrayList, int i2, int i3) {
        this.activity = fragmentActivity;
        this.mediadatas = arrayList;
        this.i = i2;
        this.type = i3;
        this.database = new Database(fragmentActivity);
        this.inflater = LayoutInflater.from(fragmentActivity);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i2) {
        View view;
        if (this.i == 0) {
            view = this.inflater.inflate(R.layout.item_video, viewGroup, false);
        } else {
            view = this.inflater.inflate(R.layout.item_video_grid, viewGroup, false);
        }
        return new ViewHolder(view);
    }

    public void onBindViewHolder(final ViewHolder viewHolder, int i2) {
        if (this.i == 1) {
            int dimensionPixelSize = this.activity.getResources().getDimensionPixelSize(R.dimen._16sdp);
            DisplayMetrics displayMetrics = new DisplayMetrics();
            this.activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            viewHolder.videothumb.getLayoutParams().width = (displayMetrics.widthPixels - dimensionPixelSize) / 2;
            viewHolder.videothumb.getLayoutParams().height = (displayMetrics.widthPixels - dimensionPixelSize) / 5;
        }
        Glide.with(this.activity).load(this.mediadatas.get(viewHolder.getAdapterPosition()).getPath()).into(viewHolder.videothumb);
        viewHolder.videotitle.setText(this.mediadatas.get(viewHolder.getAdapterPosition()).getName());
        viewHolder.videosize.setText(Utils.formateSize(Long.parseLong(this.mediadatas.get(viewHolder.getAdapterPosition()).getLength())));
        viewHolder.duration.setText(this.mediadatas.get(viewHolder.getAdapterPosition()).getDuration());
        viewHolder.videodate.setText(DateFormat.format("dd/MM/yyyy", new Date(new File(this.mediadatas.get(viewHolder.getAdapterPosition()).getPath()).lastModified())).toString());
        viewHolder.videooption.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(VideoAdapter.this.activity, view);
                popupMenu.inflate(R.menu.video_menu);
                Menu menu = popupMenu.getMenu();
                if (VideoAdapter.this.type == 2) {
                    menu.findItem(R.id.menu_v_delete).setTitle("Remove");
                }
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @SuppressLint("ResourceType")
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        boolean z = true;
                        switch (menuItem.getItemId()) {
                            case R.id.menu_v_delete:
                                if (VideoAdapter.this.type != 2) {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(VideoAdapter.this.activity);
                                    builder.setTitle("Delete Video");
                                    builder.setMessage("Are you sure you have to Delete " + VideoAdapter.this.mediadatas.get(viewHolder.getAdapterPosition()).getName() + " ?");
                                    builder.setPositiveButton(17039379, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            boolean z;
                                            File file = new File(VideoAdapter.this.mediadatas.get(viewHolder.getAdapterPosition()).getPath());
                                            if (file.exists() && file.delete()) {
                                                if (VideoAdapter.this.type == 1) {
                                                    EventBus event_Bus = new EventBus();
                                                    event_Bus.setType(1);
                                                    event_Bus.setValue(0);
                                                    org.greenrobot.eventbus.EventBus.getDefault().post(event_Bus);
                                                }
                                                List list = (List) new Gson().fromJson(VideoPlayerManager.getRecentPlay(), new TypeToken<List<MediaData>>() {

                                                }.getType());
                                                if (list != null) {
                                                    int i2 = 0;
                                                    while (true) {
                                                        if (i2 >= list.size()) {
                                                            i2 = 0;
                                                            z = false;
                                                            break;
                                                        } else if (((MediaData) list.get(i2)).getPath().equals(VideoAdapter.this.mediadatas.get(viewHolder.getAdapterPosition()).getPath())) {
                                                            z = true;
                                                            break;
                                                        } else {
                                                            i2++;
                                                        }
                                                    }
                                                    if (z) {
                                                        list.remove(i2);
                                                        VideoPlayerManager.putRecentPlay(new Gson().toJson(list));
                                                    }
                                                }
                                                VideoAdapter.this.mediadatas.remove(viewHolder.getAdapterPosition());
                                                VideoAdapter.this.notifyItemRemoved(viewHolder.getAdapterPosition());
                                                if (VideoAdapter.this.type == 0) {
                                                    if (list.size() == 0) {
                                                        ivnodata.setVisibility(0);
                                                        videorecycler.setVisibility(8);
                                                    }
                                                } else if (VideoAdapter.this.type == 1 && list.size() == 0) {
                                                    ivnodata.setVisibility(0);
                                                    videolist.setVisibility(8);
                                                }
                                            }
                                        }
                                    });
                                    builder.setNegativeButton(17039369, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialogInterface.cancel();
                                        }
                                    });
                                    builder.show();
                                    break;
                                } else {
                                    List list = (List) new Gson().fromJson(VideoPlayerManager.getRecentPlay(), new TypeToken<List<MediaData>>() {

                                    }.getType());
                                    if (list == null) {
                                        VideoAdapter.this.mediadatas.remove(viewHolder.getAdapterPosition());
                                        VideoAdapter.this.notifyItemRemoved(viewHolder.getAdapterPosition());
                                        if (list.size() == 0) {
                                            ivnodata.setVisibility(0);
                                            recentrecycler.setVisibility(8);
                                            break;
                                        }
                                    } else {
                                        int i = 0;
                                        while (true) {
                                            if (i >= list.size()) {
                                                i = 0;
                                                z = false;
                                            } else if (!((MediaData) list.get(i)).getPath().equals(VideoAdapter.this.mediadatas.get(viewHolder.getAdapterPosition()).getPath())) {
                                                i++;
                                            }
                                            if (z) {
                                                list.remove(i);
                                                VideoPlayerManager.putRecentPlay(new Gson().toJson(list));
                                            }
                                        }
                                    }
                                }
                                break;
                            case R.id.menu_v_details:
                                VideoDetailsDialog.getInstance(VideoAdapter.this.mediadatas.get(viewHolder.getAdapterPosition())).show(((AppCompatActivity) VideoAdapter.this.activity).getSupportFragmentManager(), "");
                                break;
                            case R.id.menu_v_hide:
                                if (VideoPlayerManager.getSetPass()) {
                                    AlertDialog.Builder builder2 = new AlertDialog.Builder(VideoAdapter.this.activity);
                                    builder2.setTitle("Hide Video");
                                    builder2.setMessage("Are you sure you have to Hide " + VideoAdapter.this.mediadatas.get(viewHolder.getAdapterPosition()).getName() + " ?");
                                    builder2.setPositiveButton(17039379, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            boolean z;
                                            if (!new File(Constant.HIDE_PATH).exists()) {
                                                new File(Constant.HIDE_PATH).mkdirs();
                                            }
                                            String parent = new File(VideoAdapter.this.mediadatas.get(viewHolder.getAdapterPosition()).getPath()).getParent();
                                            String str = Constant.HIDE_PATH + "/" + VideoAdapter.this.database.getID() + "_" + VideoAdapter.this.mediadatas.get(viewHolder.getAdapterPosition()).getName() + VideoAdapter.this.mediadatas.get(viewHolder.getAdapterPosition()).getPath().substring(VideoAdapter.this.mediadatas.get(viewHolder.getAdapterPosition()).getPath().lastIndexOf("."));
                                            File file = new File(parent, VideoAdapter.this.mediadatas.get(viewHolder.getAdapterPosition()).getPath().substring(VideoAdapter.this.mediadatas.get(viewHolder.getAdapterPosition()).getPath().lastIndexOf("/") + 1));
                                            File file2 = new File(str);
                                            if (file.renameTo(file2)) {
                                                HideData hide_Data = new HideData();
                                                hide_Data.setName(file2.getName());
                                                hide_Data.setPath(parent);
                                                VideoAdapter.this.database.addHide(hide_Data);
                                                try {
                                                    if (Build.VERSION.SDK_INT >= 19) {
                                                        Intent intent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
                                                        intent.setData(Uri.fromFile(file2));
                                                        VideoAdapter.this.activity.sendBroadcast(intent);
                                                        if (VideoAdapter.this.type == 1) {
                                                            EventBus event_Bus = new EventBus();
                                                            event_Bus.setType(1);
                                                            event_Bus.setValue(0);
                                                            org.greenrobot.eventbus.EventBus.getDefault().post(event_Bus);
                                                        }
                                                    } else {
                                                        VideoAdapter.this.activity.sendBroadcast(new Intent("android.intent.action.MEDIA_MOUNTED", Uri.fromFile(file2)));
                                                        if (VideoAdapter.this.type == 1) {
                                                            EventBus event_Bus2 = new EventBus();
                                                            event_Bus2.setType(1);
                                                            event_Bus2.setValue(0);
                                                            org.greenrobot.eventbus.EventBus.getDefault().post(event_Bus2);
                                                        }
                                                    }
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                                List list = (List) new Gson().fromJson(VideoPlayerManager.getRecentPlay(), new TypeToken<List<MediaData>>() {
                                                }.getType());
                                                if (list != null) {
                                                    int i2 = 0;
                                                    while (true) {
                                                        if (i2 >= list.size()) {
                                                            i2 = 0;
                                                            z = false;
                                                            break;
                                                        } else if (((MediaData) list.get(i2)).getPath().equals(VideoAdapter.this.mediadatas.get(viewHolder.getAdapterPosition()).getPath())) {
                                                            z = true;
                                                            break;
                                                        } else {
                                                            i2++;
                                                        }
                                                    }
                                                    if (z) {
                                                        list.remove(i2);
                                                        VideoPlayerManager.putRecentPlay(new Gson().toJson(list));
                                                    }
                                                }
                                                VideoAdapter.this.mediadatas.remove(viewHolder.getAdapterPosition());
                                                VideoAdapter.this.notifyItemRemoved(viewHolder.getAdapterPosition());
                                                if (VideoAdapter.this.type == 0) {
                                                    if (list.size() == 0) {
                                                        ivnodata.setVisibility(0);
                                                        videorecycler.setVisibility(8);
                                                    }
                                                } else if (VideoAdapter.this.type == 1) {
                                                    if (list.size() == 0) {
                                                        VideolistActivity.ivnodata.setVisibility(0);
                                                        videolist.setVisibility(8);
                                                    }
                                                } else if (list.size() == 0) {
                                                    ivnodata.setVisibility(0);
                                                    recentrecycler.setVisibility(8);
                                                }
                                            }
                                        }
                                    });
                                    builder2.setNegativeButton(17039369, new DialogInterface.OnClickListener() {

                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialogInterface.cancel();
                                        }
                                    });
                                    builder2.show();
                                    break;
                                } else {
                                    VideoAdapter.this.activity.startActivity(new Intent(VideoAdapter.this.activity, SecureActivity.class));
                                    break;
                                }
                            case R.id.menu_v_share:
                                Uri uriForFile = FileProvider.getUriForFile(VideoAdapter.this.activity, VideoAdapter.this.activity.getPackageName() + ".provider", new File(VideoAdapter.this.mediadatas.get(viewHolder.getAdapterPosition()).getPath()));
                                Intent intent = new Intent();
                                intent.setAction("android.intent.action.SEND");
                                intent.setType("video/*");
                                intent.addFlags(1);
                                intent.putExtra("android.intent.Extra.TEXT", (VideoAdapter.this.activity.getResources().getString(R.string.app_name) + " Created By :") + "\n" + ("https://play.google.com/store/apps/details?id=" + VideoAdapter.this.activity.getPackageName()));
                                intent.putExtra("android.intent.Extra.SUBJECT", VideoAdapter.this.activity.getResources().getString(R.string.app_name));
                                intent.putExtra("android.intent.Extra.STREAM", uriForFile);
                                VideoAdapter.this.activity.startActivity(Intent.createChooser(intent, "Share with..."));
                                break;
                        }
                        return false;
                    }
                });
                popupMenu.show();
            }
        });
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                boolean z = true;
                if (VideoAdapter.this.type == 0) {
                    ((HomeActivity) VideoAdapter.this.activity).myStartActivity(VideoPlayerActivity.getIntent(VideoAdapter.this.activity, VideoAdapter.this.mediadatas, viewHolder.getAdapterPosition()));
                } else if (VideoAdapter.this.type == 1) {
                    ((VideolistActivity) VideoAdapter.this.activity).myStartActivity(VideoPlayerActivity.getIntent(VideoAdapter.this.activity, VideoAdapter.this.mediadatas, viewHolder.getAdapterPosition()));
                } else {
                    ((HomeActivity) VideoAdapter.this.activity).myStartActivity(VideoPlayerActivity.getIntent(VideoAdapter.this.activity, mediadatas, viewHolder.getAdapterPosition()));
                }
                List list = (List) new Gson().fromJson(VideoPlayerManager.getRecentPlay(), new TypeToken<List<MediaData>>() {
                }.getType());
                if (list != null) {
                    int i = 0;
                    int i2 = 0;
                    while (true) {
                        if (i2 >= list.size()) {
                            z = false;
                            break;
                        } else if (((MediaData) list.get(i2)).getPath().equals(VideoAdapter.this.mediadatas.get(viewHolder.getAdapterPosition()).getPath())) {
                            i = i2;
                            break;
                        } else {
                            i2++;
                        }
                    }
                    if (z) {
                        list.remove(i);
                        list.add(VideoAdapter.this.mediadatas.get(viewHolder.getAdapterPosition()));
                        VideoPlayerManager.putRecentPlay(new Gson().toJson(list));
                    } else {
                        list.add(VideoAdapter.this.mediadatas.get(viewHolder.getAdapterPosition()));
                        VideoPlayerManager.putRecentPlay(new Gson().toJson(list));
                    }
                } else {
                    ArrayList arrayList = new ArrayList();
                    arrayList.add(VideoAdapter.this.mediadatas.get(viewHolder.getAdapterPosition()));
                    VideoPlayerManager.putRecentPlay(new Gson().toJson(arrayList));
                }
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        EventBus event_Bus = new EventBus();
                        event_Bus.setType(3);
                        event_Bus.setValue(0);
                        org.greenrobot.eventbus.EventBus.getDefault().post(event_Bus);
                    }
                }, 1500);
            }
        });
    }

    @SuppressLint("ResourceType")
    public void initRenameDialog(final int i2) {
        final Dialog dialog = new Dialog(this.activity, R.style.WideDialog);
        dialog.setContentView(R.layout.dialog_new_folder);
        dialog.getWindow().setBackgroundDrawableResource(17170445);
        final EditText editText = (EditText) dialog.findViewById(R.id.folder_name);
        ((TextView) dialog.findViewById(R.id.title)).setText("Rename");
        editText.setText(this.mediadatas.get(i2).getName());
        ((Button) dialog.findViewById(R.id.cancel)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        ((Button) dialog.findViewById(R.id.ok)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (TextUtils.isEmpty(editText.getText().toString().trim())) {
                    Toast.makeText(VideoAdapter.this.activity, "Enter folder name!", 0).show();
                    return;
                }
                String parent = new File(VideoAdapter.this.mediadatas.get(i2).getPath()).getParent();
                File file = new File(parent, VideoAdapter.this.mediadatas.get(i2).getPath().substring(VideoAdapter.this.mediadatas.get(i2).getPath().lastIndexOf("/") + 1));
                String substring = VideoAdapter.this.mediadatas.get(i2).getPath().substring(VideoAdapter.this.mediadatas.get(i2).getPath().lastIndexOf("."));
                File file2 = new File(parent, editText.getText().toString().trim() + substring);
                if (file.exists() && file.renameTo(file2)) {
                    VideoAdapter.this.mediadatas.get(i2).setName(file2.getName());
                    VideoAdapter.this.mediadatas.get(i2).setPath(file2.getPath());
                    VideoAdapter.this.mediadatas.set(i2, VideoAdapter.this.mediadatas.get(i2));
                    VideoAdapter.this.notifyItemChanged(i2);
                    if (VideoAdapter.this.type == 1) {
                        EventBus event_Bus = new EventBus();
                        event_Bus.setType(1);
                        event_Bus.setValue(0);
                        org.greenrobot.eventbus.EventBus.getDefault().post(event_Bus);
                    }
                    dialog.dismiss();
                }
            }
        });
        dialog.show();
    }

    @Override
    public int getItemCount() {
        return mediadatas.size();
    }
}
