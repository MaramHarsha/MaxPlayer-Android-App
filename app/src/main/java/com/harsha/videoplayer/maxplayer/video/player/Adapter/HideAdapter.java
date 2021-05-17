package com.harsha.videoplayer.maxplayer.video.player.Adapter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
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
import com.harsha.videoplayer.maxplayer.video.player.Activity.HidevideoActivity;
import com.harsha.videoplayer.maxplayer.video.player.Activity.VideoPlayerActivity;
import com.harsha.videoplayer.maxplayer.video.player.Database.Database;
import com.harsha.videoplayer.maxplayer.video.player.Dialog.VideoDetailsDialog;
import com.harsha.videoplayer.maxplayer.video.player.Extra.MediaData;
import com.harsha.videoplayer.maxplayer.video.player.Model.EventBus;
import com.harsha.videoplayer.maxplayer.video.player.Model.HideData;
import com.harsha.videoplayer.maxplayer.video.player.R;
import com.harsha.videoplayer.maxplayer.video.player.Util.Constant;
import com.harsha.videoplayer.maxplayer.video.player.Util.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;

public class HideAdapter extends RecyclerView.Adapter<HideAdapter.ViewHolder> {
    FragmentActivity activity;
    public final Database database;
    int i;
    LayoutInflater inflater;
    ArrayList<MediaData> mediadatas;

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

    public HideAdapter(FragmentActivity fragmentActivity, ArrayList<MediaData> arrayList, int i2) {
        this.activity = fragmentActivity;
        this.mediadatas = arrayList;
        this.i = i2;
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
            viewHolder.videothumb.getLayoutParams().height = (displayMetrics.widthPixels - dimensionPixelSize) / 3;
        }
        Glide.with(this.activity).load(this.mediadatas.get(viewHolder.getAdapterPosition()).getPath()).into(viewHolder.videothumb);
        viewHolder.videotitle.setText(this.mediadatas.get(viewHolder.getAdapterPosition()).getName());
        viewHolder.videosize.setText(Utils.formateSize(Long.parseLong(this.mediadatas.get(viewHolder.getAdapterPosition()).getLength())));
        viewHolder.duration.setText(this.mediadatas.get(viewHolder.getAdapterPosition()).getDuration());
        viewHolder.videodate.setText(DateFormat.format("dd/MM/yyyy", new Date(new File(this.mediadatas.get(viewHolder.getAdapterPosition()).getPath()).lastModified())).toString());
        viewHolder.videooption.setOnClickListener((View.OnClickListener) view -> {
            PopupMenu popupMenu = new PopupMenu(HideAdapter.this.activity, view);
            popupMenu.inflate(R.menu.video_menu);
            popupMenu.getMenu().findItem(R.id.menu_v_hide).setTitle("Unhide");
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @SuppressLint("ResourceType")
                public boolean onMenuItemClick(MenuItem menuItem) {
                    switch (menuItem.getItemId()) {
                        case R.id.menu_v_delete:
                            AlertDialog.Builder builder = new AlertDialog.Builder(HideAdapter.this.activity);
                            builder.setTitle("Delete Video");
                            builder.setMessage("Are you sure you have to Delete " + HideAdapter.this.mediadatas.get(viewHolder.getAdapterPosition()).getName() + " ?");
                            builder.setPositiveButton(17039379, (dialogInterface, i) -> {
                                File file = new File(HideAdapter.this.mediadatas.get(viewHolder.getAdapterPosition()).getPath());
                                if (file.exists() && file.delete()) {
                                    HideAdapter.this.mediadatas.remove(viewHolder.getAdapterPosition());
                                    HideAdapter.this.notifyItemRemoved(viewHolder.getAdapterPosition());
                                    if (HideAdapter.this.mediadatas.size() == 0) {
                                        HidevideoActivity.ivnodata.setVisibility(0);
                                        HidevideoActivity.hiderecycler.setVisibility(8);
                                    }
                                }
                            });
                            builder.setNegativeButton(17039369, (dialogInterface, i) -> dialogInterface.cancel());
                            builder.show();
                            return false;
                        case R.id.menu_v_details:
                            VideoDetailsDialog.getInstance(HideAdapter.this.mediadatas.get(viewHolder.getAdapterPosition())).show(((AppCompatActivity) HideAdapter.this.activity).getSupportFragmentManager(), "");
                            return false;
                        case R.id.menu_v_hide:
                            AlertDialog.Builder builder2 = new AlertDialog.Builder(HideAdapter.this.activity);
                            builder2.setTitle("Unhide Video");
                            builder2.setMessage("Are you sure you have to Unhide " + HideAdapter.this.mediadatas.get(viewHolder.getAdapterPosition()).getName() + " ?");
                            builder2.setPositiveButton(17039379, (dialogInterface, i) -> {
                                HideData hideData = HideAdapter.this.database.getHideData(HideAdapter.this.mediadatas.get(viewHolder.getAdapterPosition()).getName());
                                String parent = new File(HideAdapter.this.mediadatas.get(viewHolder.getAdapterPosition()).getPath()).getParent();
                                HideAdapter.this.mediadatas.get(viewHolder.getAdapterPosition()).getPath().substring(HideAdapter.this.mediadatas.get(viewHolder.getAdapterPosition()).getPath().lastIndexOf("."));
                                File file = new File(parent, HideAdapter.this.mediadatas.get(viewHolder.getAdapterPosition()).getPath().substring(HideAdapter.this.mediadatas.get(viewHolder.getAdapterPosition()).getPath().lastIndexOf("/") + 1));
                                if (hideData != null) {
                                    File file2 = new File(hideData.getPath(), HideAdapter.this.mediadatas.get(viewHolder.getAdapterPosition()).getName());
                                    if (file.renameTo(file2)) {
                                        HideAdapter.this.database.deleteHide(hideData.getName());
                                        HideAdapter.this.mediadatas.remove(viewHolder.getAdapterPosition());
                                        HideAdapter.this.notifyItemRemoved(viewHolder.getAdapterPosition());
                                        if (HideAdapter.this.mediadatas.size() == 0) {
                                            HidevideoActivity.ivnodata.setVisibility(0);
                                            HidevideoActivity.hiderecycler.setVisibility(8);
                                        }
                                        try {
                                            if (Build.VERSION.SDK_INT >= 19) {
                                                Intent intent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
                                                intent.setData(Uri.fromFile(file2));
                                                HideAdapter.this.activity.sendBroadcast(intent);
                                                EventBus event_Bus = new EventBus();
                                                event_Bus.setType(1);
                                                event_Bus.setValue(0);
                                                org.greenrobot.eventbus.EventBus.getDefault().post(event_Bus);
                                                return;
                                            }
                                            HideAdapter.this.activity.sendBroadcast(new Intent("android.intent.action.MEDIA_MOUNTED", Uri.fromFile(file2)));
                                            EventBus event_Bus2 = new EventBus();
                                            event_Bus2.setType(1);
                                            event_Bus2.setValue(0);
                                            org.greenrobot.eventbus.EventBus.getDefault().post(event_Bus2);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                } else {
                                    if (!new File(Constant.YOUR_PATH).exists()) {
                                        new File(Constant.YOUR_PATH).mkdirs();
                                    }
                                    File file3 = new File(Constant.YOUR_PATH, HideAdapter.this.mediadatas.get(viewHolder.getAdapterPosition()).getName());
                                    if (file.renameTo(file3)) {
                                        HideAdapter.this.mediadatas.remove(viewHolder.getAdapterPosition());
                                        HideAdapter.this.notifyItemRemoved(viewHolder.getAdapterPosition());
                                        if (HideAdapter.this.mediadatas.size() == 0) {
                                            HidevideoActivity.ivnodata.setVisibility(0);
                                            HidevideoActivity.hiderecycler.setVisibility(8);
                                        }
                                        try {
                                            if (Build.VERSION.SDK_INT >= 19) {
                                                Intent intent2 = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
                                                intent2.setData(Uri.fromFile(file3));
                                                HideAdapter.this.activity.sendBroadcast(intent2);
                                                EventBus event_Bus3 = new EventBus();
                                                event_Bus3.setType(1);
                                                event_Bus3.setValue(0);
                                                org.greenrobot.eventbus.EventBus.getDefault().post(event_Bus3);
                                                return;
                                            }
                                            HideAdapter.this.activity.sendBroadcast(new Intent("android.intent.action.MEDIA_MOUNTED", Uri.fromFile(file3)));
                                            EventBus event_Bus4 = new EventBus();
                                            event_Bus4.setType(1);
                                            event_Bus4.setValue(0);
                                            org.greenrobot.eventbus.EventBus.getDefault().post(event_Bus4);
                                        } catch (Exception e2) {
                                            e2.printStackTrace();
                                        }
                                    }
                                }
                            });
                            builder2.setNegativeButton(17039369, (dialogInterface, i) -> dialogInterface.cancel());
                            builder2.show();
                            return false;
                        case R.id.menu_v_share:
                            Uri uriForFile = FileProvider.getUriForFile(HideAdapter.this.activity, HideAdapter.this.activity.getPackageName() + ".provider", new File(HideAdapter.this.mediadatas.get(viewHolder.getAdapterPosition()).getPath()));
                            Intent intent = new Intent();
                            intent.setAction("android.intent.action.SEND");
                            intent.setType("video/*");
                            intent.addFlags(1);
                            intent.putExtra("android.intent.Extra.TEXT", (HideAdapter.this.activity.getResources().getString(R.string.app_name) + " Created By :") + "\n" + ("https://play.google.com/store/apps/details?id=" + HideAdapter.this.activity.getPackageName()));
                            intent.putExtra("android.intent.Extra.SUBJECT", HideAdapter.this.activity.getResources().getString(R.string.app_name));
                            intent.putExtra("android.intent.Extra.STREAM", uriForFile);
                            HideAdapter.this.activity.startActivity(Intent.createChooser(intent, "Share with..."));
                            return false;
                        default:
                            return false;
                    }
                }
            });
            popupMenu.show();
        });
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                ((HidevideoActivity) HideAdapter.this.activity).myStartActivity(VideoPlayerActivity.getIntent(HideAdapter.this.activity, mediadatas, viewHolder.getAdapterPosition()));
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
                    Toast.makeText(HideAdapter.this.activity, "Enter folder name!", 0).show();
                    return;
                }
                String parent = new File(HideAdapter.this.mediadatas.get(i2).getPath()).getParent();
                File file = new File(parent, HideAdapter.this.mediadatas.get(i2).getPath().substring(HideAdapter.this.mediadatas.get(i2).getPath().lastIndexOf("/") + 1));
                File file2 = new File(parent, editText.getText().toString().trim());
                if (file.exists() && file.renameTo(file2)) {
                    HideAdapter.this.mediadatas.get(i2).setName(file2.getName());
                    HideAdapter.this.mediadatas.get(i2).setPath(file2.getPath());
                    HideAdapter.this.mediadatas.set(i2, HideAdapter.this.mediadatas.get(i2));
                    HideAdapter.this.notifyItemChanged(i2);
                    dialog.dismiss();
                }
            }
        });
        dialog.show();
    }

    @Override
    public int getItemCount() {
        return this.mediadatas.size();
    }
}
