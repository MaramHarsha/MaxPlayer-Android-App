package com.harsha.videoplayer.maxplayer.video.player.Adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.harsha.videoplayer.maxplayer.video.player.Activity.VideolistActivity;
import com.harsha.videoplayer.maxplayer.video.player.Model.Folder;
import com.harsha.videoplayer.maxplayer.video.player.R;

import java.util.ArrayList;

public class FolderAdapter extends RecyclerView.Adapter<FolderAdapter.ViewHolder> {
    FragmentActivity activity;
    ArrayList<Folder> folders;
    int i;
    LayoutInflater inflater;

    public class ViewHolder extends RecyclerView.ViewHolder {
        FrameLayout doubles;
        TextView foldername;
        ImageView folderoption;
        ImageView folderthumb;
        ImageView folderthumb1;
        ImageView folderthumb2;
        ImageView folderthumb3;
        ImageView folderthumb4;
        ImageView folderthumb5;
        CardView single;
        FrameLayout triple;
        TextView videocount;

        public ViewHolder(View view) {
            super(view);
            this.foldername = (TextView) view.findViewById(R.id.folder_name);
            this.videocount = (TextView) view.findViewById(R.id.video_count);
            this.folderthumb = (ImageView) view.findViewById(R.id.folder_thumb);
            this.folderthumb1 = (ImageView) view.findViewById(R.id.folder_thumb1);
            this.folderthumb2 = (ImageView) view.findViewById(R.id.folder_thumb2);
            this.folderthumb3 = (ImageView) view.findViewById(R.id.folder_thumb3);
            this.folderthumb4 = (ImageView) view.findViewById(R.id.folder_thumb4);
            this.folderthumb5 = (ImageView) view.findViewById(R.id.folder_thumb5);
            this.folderoption = (ImageView) view.findViewById(R.id.folder_option);
            this.doubles = (FrameLayout) view.findViewById(R.id.doubles);
            this.triple = (FrameLayout) view.findViewById(R.id.triple);
        }
    }

    public FolderAdapter(FragmentActivity fragmentActivity, ArrayList<Folder> arrayList, int i2) {
        this.activity = fragmentActivity;
        this.folders = arrayList;
        this.i = i2;
        this.inflater = LayoutInflater.from(fragmentActivity);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i2) {
        View view;
        if (this.i == 0) {
            view = this.inflater.inflate(R.layout.item_folder, viewGroup, false);
        } else {
            view = this.inflater.inflate(R.layout.item_folder_grid, viewGroup, false);
        }
        return new ViewHolder(view);
    }

    public void onBindViewHolder(final ViewHolder viewHolder, int i2) {
        viewHolder.foldername.setText(this.folders.get(viewHolder.getAdapterPosition()).getName());
        TextView textView = viewHolder.videocount;
        textView.setText("(" + this.folders.get(viewHolder.getAdapterPosition()).getMedia_data().size() + ")");
        viewHolder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(FolderAdapter.this.activity, VideolistActivity.class);
            intent.putExtra("data", FolderAdapter.this.folders.get(viewHolder.getAdapterPosition()));
            FolderAdapter.this.activity.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return this.folders.size();
    }
}
