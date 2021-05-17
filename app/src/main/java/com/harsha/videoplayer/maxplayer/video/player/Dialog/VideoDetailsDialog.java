package com.harsha.videoplayer.maxplayer.video.player.Dialog;

import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

import com.harsha.videoplayer.maxplayer.video.player.Extra.MediaData;
import com.harsha.videoplayer.maxplayer.video.player.R;
import com.harsha.videoplayer.maxplayer.video.player.Util.Utils;

import java.io.File;
import java.util.Date;

public class VideoDetailsDialog extends DialogFragment {
    MediaData video;
    private View view;

    public static VideoDetailsDialog getInstance(MediaData media_Data) {
        VideoDetailsDialog videoDetailsDialog = new VideoDetailsDialog();
        videoDetailsDialog.video = media_Data;
        return videoDetailsDialog;
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.dialog_video_details, viewGroup, false);
        this.view = inflate;
        return inflate;
    }

    @Override
    public void onViewCreated(View view2, Bundle bundle) {
        super.onViewCreated(view2, bundle);
        initView();
    }

    private void initView() {
        TextView videoName = (TextView) this.view.findViewById(R.id.videoName);
        TextView videoPath = (TextView) this.view.findViewById(R.id.videoPath);
        TextView videoModifyDate = (TextView) this.view.findViewById(R.id.videoModifyDate);
        TextView videoResolution = (TextView) this.view.findViewById(R.id.videoResolution);
        TextView videoSize = (TextView) this.view.findViewById(R.id.videoSize);
        TextView videoDuration = (TextView) this.view.findViewById(R.id.videoDuration);
        Button btnOk = (Button) this.view.findViewById(R.id.btnOk);
        Button btnCancel = (Button) this.view.findViewById(R.id.btnCancel);
        videoName.setText(this.video.getName());
        videoPath.setText(this.video.getPath());
        videoModifyDate.setText(DateFormat.format("dd/MM/yyyy", new Date(new File(this.video.getPath()).lastModified())).toString());
        videoSize.setText(Utils.formateSize(Long.parseLong(this.video.getLength())));
        videoDuration.setText(this.video.getDuration());
        videoResolution.setText(this.video.getResolution());
        btnOk.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                VideoDetailsDialog.this.getDialog().dismiss();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                VideoDetailsDialog.this.getDialog().dismiss();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
