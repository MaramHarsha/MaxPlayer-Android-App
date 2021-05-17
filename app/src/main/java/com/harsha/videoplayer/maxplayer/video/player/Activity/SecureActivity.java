package com.harsha.videoplayer.maxplayer.video.player.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.exifinterface.media.ExifInterface;

import com.harsha.videoplayer.maxplayer.video.player.R;
import com.harsha.videoplayer.maxplayer.video.player.Util.VideoPlayerManager;
import com.harsha.videoplayer.maxplayer.video.player.appmanage.ads.AdsBanner;

public class SecureActivity extends AppCompatActivity {
    private TextView forgot;
    public ImageView p1;
    public ImageView p2;
    public ImageView p3;
    public ImageView p4;
    public String passcode;
    public int temp = 0;
    public TextView title;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_secure);
        initView();
        new AdsBanner(this, findViewById(R.id.rl_ad), findViewById(R.id.rl_layout)).loadBannerAd();
    }

    @SuppressLint({"WrongConstant", "SetTextI18n"})
    private void initView() {
        this.title = findViewById(R.id.title);
        this.forgot = findViewById(R.id.forgot);
        ImageView backsecure = findViewById(R.id.back_secure);
        if (VideoPlayerManager.getSetPass()) {
            this.title.setText("Enter passcode");
        } else {
            this.title.setText("Enter new passcode");
        }
        if (VideoPlayerManager.getSetPass()) {
            this.forgot.setVisibility(0);
        } else {
            this.forgot.setVisibility(8);
        }
        this.forgot.setOnClickListener(view -> SecureActivity.this.startActivityForResult(new Intent(SecureActivity.this, SecurityquestionActivity.class).putExtra("type", 1), 100));
        backsecure.setOnClickListener(view -> SecureActivity.this.onBackPressed());
        initViewSet();
    }

    private void initViewSet() {
        this.p1 = findViewById(R.id.p1);
        this.p2 = findViewById(R.id.p2);
        this.p3 = findViewById(R.id.p3);
        this.p4 = findViewById(R.id.p4);
        final EditText editText = findViewById(R.id.pass);
        findViewById(R.id.b0).setOnClickListener(view -> editText.setText(editText.getText().toString().trim() + "0"));
        findViewById(R.id.b1).setOnClickListener(view -> editText.setText(editText.getText().toString().trim() + "1"));
        findViewById(R.id.b2).setOnClickListener(view -> editText.setText(editText.getText().toString().trim() + ExifInterface.GPS_MEASUREMENT_2D));
        findViewById(R.id.b3).setOnClickListener(view -> editText.setText(editText.getText().toString().trim() + ExifInterface.GPS_MEASUREMENT_3D));
        findViewById(R.id.b4).setOnClickListener(view -> editText.setText(editText.getText().toString().trim() + "4"));
        findViewById(R.id.b5).setOnClickListener(view -> editText.setText(editText.getText().toString().trim() + "5"));
        findViewById(R.id.b6).setOnClickListener(view -> editText.setText(editText.getText().toString().trim() + "6"));
        findViewById(R.id.b7).setOnClickListener(view -> editText.setText(editText.getText().toString().trim() + "7"));
        findViewById(R.id.b8).setOnClickListener(view -> editText.setText(editText.getText().toString().trim() + "8"));
        findViewById(R.id.b9).setOnClickListener(view -> editText.setText(editText.getText().toString().trim() + "9"));
        editText.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable editable) {
            }

            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            @SuppressLint("WrongConstant")
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                if (charSequence.length() == 0) {
                    SecureActivity.this.p1.setImageDrawable(ContextCompat.getDrawable(SecureActivity.this, R.drawable.iv_unselected));
                    SecureActivity.this.p2.setImageDrawable(ContextCompat.getDrawable(SecureActivity.this, R.drawable.iv_unselected));
                    SecureActivity.this.p3.setImageDrawable(ContextCompat.getDrawable(SecureActivity.this, R.drawable.iv_unselected));
                    SecureActivity.this.p4.setImageDrawable(ContextCompat.getDrawable(SecureActivity.this, R.drawable.iv_unselected));
                } else if (charSequence.length() == 1) {
                    SecureActivity.this.p1.setImageDrawable(ContextCompat.getDrawable(SecureActivity.this, R.drawable.iv_selected));
                    SecureActivity.this.p2.setImageDrawable(ContextCompat.getDrawable(SecureActivity.this, R.drawable.iv_unselected));
                    SecureActivity.this.p3.setImageDrawable(ContextCompat.getDrawable(SecureActivity.this, R.drawable.iv_unselected));
                    SecureActivity.this.p4.setImageDrawable(ContextCompat.getDrawable(SecureActivity.this, R.drawable.iv_unselected));
                } else if (charSequence.length() == 2) {
                    SecureActivity.this.p1.setImageDrawable(ContextCompat.getDrawable(SecureActivity.this, R.drawable.iv_selected));
                    SecureActivity.this.p2.setImageDrawable(ContextCompat.getDrawable(SecureActivity.this, R.drawable.iv_selected));
                    SecureActivity.this.p3.setImageDrawable(ContextCompat.getDrawable(SecureActivity.this, R.drawable.iv_unselected));
                    SecureActivity.this.p4.setImageDrawable(ContextCompat.getDrawable(SecureActivity.this, R.drawable.iv_unselected));
                } else if (charSequence.length() == 3) {
                    SecureActivity.this.p1.setImageDrawable(ContextCompat.getDrawable(SecureActivity.this, R.drawable.iv_selected));
                    SecureActivity.this.p2.setImageDrawable(ContextCompat.getDrawable(SecureActivity.this, R.drawable.iv_selected));
                    SecureActivity.this.p3.setImageDrawable(ContextCompat.getDrawable(SecureActivity.this, R.drawable.iv_selected));
                    SecureActivity.this.p4.setImageDrawable(ContextCompat.getDrawable(SecureActivity.this, R.drawable.iv_unselected));
                } else if (charSequence.length() == 4) {
                    SecureActivity.this.p1.setImageDrawable(ContextCompat.getDrawable(SecureActivity.this, R.drawable.iv_selected));
                    SecureActivity.this.p2.setImageDrawable(ContextCompat.getDrawable(SecureActivity.this, R.drawable.iv_selected));
                    SecureActivity.this.p3.setImageDrawable(ContextCompat.getDrawable(SecureActivity.this, R.drawable.iv_selected));
                    SecureActivity.this.p4.setImageDrawable(ContextCompat.getDrawable(SecureActivity.this, R.drawable.iv_selected));
                    if (!VideoPlayerManager.getSetPass()) {
                        if (SecureActivity.this.temp == 0) {
                            SecureActivity.this.passcode = String.valueOf(charSequence);
                            editText.setText("");
                            SecureActivity.this.temp = 1;
                            SecureActivity.this.title.setText("Enter confirm passcode");
                        } else if (SecureActivity.this.passcode.equals(charSequence.toString())) {
                            VideoPlayerManager.putPass(SecureActivity.this.passcode);
                            VideoPlayerManager.putSetPass(true);
                            SecureActivity.this.setPasswordSuccess();
                            Toast.makeText(SecureActivity.this, "Passcode set successfully!", 0).show();
                        } else {
                            editText.setText("");
                            SecureActivity.this.temp = 0;
                            SecureActivity.this.title.setText("Enter new passcode");
                            Toast.makeText(SecureActivity.this, "Please confirm password as same!", 0).show();
                        }
                    } else if (VideoPlayerManager.getPass().equals(charSequence.toString())) {
                        SecureActivity.this.setPasswordSuccess();
                    } else {
                        editText.setText("");
                        Toast.makeText(SecureActivity.this, "Please enter correct passcode!", 0).show();
                    }
                }
            }
        });
    }

    public void setPasswordSuccess() {
        if (VideoPlayerManager.getSetQuestion()) {
            startActivity(new Intent(this, HidevideoActivity.class));
            finish();
            return;
        }
        startActivity(new Intent(this, SecurityquestionActivity.class).putExtra("type", 0));
        finish();
    }

    @SuppressLint("WrongConstant")
    @Override
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i2 == -1 && i == 100) {
            if (VideoPlayerManager.getSetPass()) {
                this.title.setText("Enter passcode");
            } else {
                this.title.setText("Enter new passcode");
            }
            if (VideoPlayerManager.getSetPass()) {
                this.forgot.setVisibility(0);
            } else {
                this.forgot.setVisibility(8);
            }
            this.p1.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.iv_unselected));
            this.p2.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.iv_unselected));
            this.p3.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.iv_unselected));
            this.p4.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.iv_unselected));
            this.passcode = "";
        }
    }
}
