package com.harsha.videoplayer.maxplayer.video.player.Extra;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.harsha.videoplayer.maxplayer.video.player.R;


import java.util.ArrayList;

public class VideolistAdapter extends RecyclerView.Adapter<VideolistAdapter.ViewHolder> {
    Context context;
    LayoutInflater inflater;
    ArrayList<MediaData> mediadatas;
    OnClickVideo onClickVideo;

    public interface OnClickVideo {
        void onClickVideo(int i);
    }

    public VideolistAdapter(Context context2, ArrayList<MediaData> arrayList, OnClickVideo onClickVideo2) {
        this.context = context2;
        this.mediadatas = arrayList;
        this.inflater = LayoutInflater.from(context2);
        this.onClickVideo = onClickVideo2;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder(this.inflater.inflate(R.layout.item_video_list, viewGroup, false));
    }

    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        viewHolder.videotitle.setText(this.mediadatas.get(viewHolder.getAdapterPosition()).getName());
        viewHolder.duration.setText(this.mediadatas.get(viewHolder.getAdapterPosition()).getDuration());
        Glide.with(this.context).load(this.mediadatas.get(viewHolder.getAdapterPosition()).getPath()).into(viewHolder.videothumb);
    }

    @Override
    public int getItemCount() {
        return this.mediadatas.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView duration;
        ImageView videothumb;
        TextView videotitle;

        public ViewHolder(View view) {
            super(view);
            videothumb = (ImageView) view.findViewById(R.id.video_thumb);
            duration = (TextView) view.findViewById(R.id.duration);
            videotitle = (TextView) view.findViewById(R.id.video_title);
            view.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    VideolistAdapter.this.onClickVideo.onClickVideo(ViewHolder.this.getAdapterPosition());
                }
            });
        }
    }
}
