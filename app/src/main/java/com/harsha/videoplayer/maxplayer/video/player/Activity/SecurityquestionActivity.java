package com.harsha.videoplayer.maxplayer.video.player.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.harsha.videoplayer.maxplayer.video.player.R;
import com.harsha.videoplayer.maxplayer.video.player.Util.VideoPlayerManager;
import com.harsha.videoplayer.maxplayer.video.player.appmanage.ads.AdsBanner;

public class SecurityquestionActivity extends AppCompatActivity {
    public Spinner questionSpinner;
    private RelativeLayout root;
    public EditText securityAnswer;
    private int type;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_securityquestion);
        this.type = getIntent().getIntExtra("type", 0);
        initView();
        new AdsBanner(this, (RelativeLayout) findViewById(R.id.rl_ad), (RelativeLayout) findViewById(R.id.rl_layout)).loadBannerAd();
    }

    private void initView() {
        questionSpinner = (Spinner) findViewById(R.id.questionSpinner);
        TextView saveQuestion = (TextView) findViewById(R.id.saveQuestion);
        this.securityAnswer = (EditText) findViewById(R.id.securityAnswer);
        ImageView backquestion = (ImageView) findViewById(R.id.back_question);
        this.root = (RelativeLayout) findViewById(R.id.root);
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, (int) R.layout.spinner_list, getResources().getStringArray(R.array.security_question));
        arrayAdapter.setDropDownViewResource(R.layout.spinner_list);
        this.questionSpinner.setAdapter((SpinnerAdapter) arrayAdapter);
        if (this.type == 1) {
            this.questionSpinner.setSelection(Integer.parseInt(VideoPlayerManager.getSecurityQuestion()));
            this.questionSpinner.setEnabled(false);
        }
        saveQuestion.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("WrongConstant")
            public void onClick(View view) {
                String obj = SecurityquestionActivity.this.securityAnswer.getText().toString();
                if (TextUtils.isEmpty(obj)) {
                    Toast.makeText(SecurityquestionActivity.this, "Enter Answer", 0).show();
                    return;
                }
                SecurityquestionActivity.this.hideSoftKeyboard();
                if (!VideoPlayerManager.getSetQuestion()) {
                    VideoPlayerManager.putSetQuestion(true);
                    VideoPlayerManager.putSecurityQuestion(String.valueOf(SecurityquestionActivity.this.questionSpinner.getSelectedItemPosition()));
                    VideoPlayerManager.putAnswerQuestion(obj);
                    SecurityquestionActivity.this.setPasswordSuccess();
                } else if (VideoPlayerManager.getAnswerQuestion().equals(obj)) {
                    VideoPlayerManager.putSetPass(false);
                    VideoPlayerManager.putPass("");
                    SecurityquestionActivity.this.setResult(-1, new Intent());
                    SecurityquestionActivity.this.finish();
                } else {
                    Toast.makeText(SecurityquestionActivity.this, "Enter correct security answer!", 0).show();
                }
            }
        });
        backquestion.setOnClickListener(view -> SecurityquestionActivity.this.onBackPressed());
    }

    public void setPasswordSuccess() {
        startActivity(new Intent(this, HidevideoActivity.class));
        finish();
    }

    @SuppressLint("WrongConstant")
    public void hideSoftKeyboard() {
        ((InputMethodManager) getSystemService("input_method")).hideSoftInputFromWindow(this.root.getWindowToken(), 0);
    }
}
