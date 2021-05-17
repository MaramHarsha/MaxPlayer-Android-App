package com.harsha.videoplayer.maxplayer.video.player.Extra;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class TextureView1 extends TextreView {
    protected static final String TAG = "JZResizeTextureView";
    public int currentVideoHeight;
    public int currentVideoWidth;

    public TextureView1(Context context) {
        super(context);
        this.currentVideoWidth = 0;
        this.currentVideoHeight = 0;
        this.currentVideoWidth = 0;
        this.currentVideoHeight = 0;
    }

    public TextureView1(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.currentVideoWidth = 0;
        this.currentVideoHeight = 0;
        this.currentVideoWidth = 0;
        this.currentVideoHeight = 0;
    }

    @Override // com.videoplayer.hdmaxplayer.video.player.saxhdvideoplayer.Extra.TextreView
    public void setVideoSize(int i, int i2) {
        if (this.currentVideoWidth != i || this.currentVideoHeight != i2) {
            this.currentVideoWidth = i;
            this.currentVideoHeight = i2;
            requestLayout();
        }
    }

    @Override // com.videoplayer.hdmaxplayer.video.player.saxhdvideoplayer.Extra.TextreView
    public void setRotation(float f) {
        if (f != getRotation()) {
            super.setRotation(f);
            requestLayout();
        }
    }

    @Override // com.videoplayer.hdmaxplayer.video.player.saxhdvideoplayer.Extra.TextreView
    public void onMeasure(int i, int i2) {
        int i3;
        int i4;
        int i5 = 0;
        int i6;
        Log.i(TAG, "onMeasure  [" + hashCode() + "] ");
        int rotation = (int) getRotation();
        int i7 = this.currentVideoWidth;
        int i8 = this.currentVideoHeight;
        int measuredHeight = ((View) getParent()).getMeasuredHeight();
        int measuredWidth = ((View) getParent()).getMeasuredWidth();
        if (!(measuredWidth == 0 || measuredHeight == 0 || i7 == 0 || i8 == 0 || Video.VIDEO_IMAGE_DISPLAY_TYPE != 1)) {
            if (rotation == 90 || rotation == 270) {
                measuredWidth = measuredHeight;
                measuredHeight = measuredWidth;
            }
            i8 = (i7 * measuredHeight) / measuredWidth;
        }
        if (rotation == 90 || rotation == 270) {
            i3 = i;
            i4 = i2;
        } else {
            i4 = i;
            i3 = i2;
        }
        int defaultSize = getDefaultSize(i7, i4);
        int defaultSize2 = getDefaultSize(i8, i3);
        if (i7 > 0 && i8 > 0) {
            int mode = MeasureSpec.getMode(i4);
            int size = MeasureSpec.getSize(i4);
            int mode2 = MeasureSpec.getMode(i3);
            int size2 = MeasureSpec.getSize(i3);
            Log.i(TAG, "widthMeasureSpec  [" + MeasureSpec.toString(i4) + "]");
            Log.i(TAG, "heightMeasureSpec [" + MeasureSpec.toString(i3) + "]");
            if (mode == 1073741824 && mode2 == 1073741824) {
                int i9 = i7 * size2;
                int i10 = size * i8;
                if (i9 < i10) {
                    defaultSize = i9 / i8;
                } else if (i9 > i10) {
                    i5 = i10 / i7;
                } else {
                    defaultSize = size;
                }
                defaultSize2 = size2;
            } else if (mode == 1073741824) {
                i5 = (size * i8) / i7;
                if (mode2 == Integer.MIN_VALUE && i5 > size2) {
                    defaultSize = (size2 * i7) / i8;
                    defaultSize2 = size2;
                }
            } else {
                if (mode2 == 1073741824) {
                    i6 = (size2 * i7) / i8;
                    if (mode == Integer.MIN_VALUE && i6 > size) {
                        i5 = (size * i8) / i7;
                    }
                } else {
                    if (mode2 != Integer.MIN_VALUE || i8 <= size2) {
                        i6 = i7;
                        size2 = i8;
                    } else {
                        i6 = (size2 * i7) / i8;
                    }
                    if (mode == Integer.MIN_VALUE && i6 > size) {
                        i5 = (size * i8) / i7;
                    }
                }
                defaultSize = i6;
                defaultSize2 = size2;
            }
            defaultSize = size;
            defaultSize2 = i5;
        }
        if (!(measuredWidth == 0 || measuredHeight == 0 || i7 == 0 || i8 == 0)) {
            if (Video.VIDEO_IMAGE_DISPLAY_TYPE != 3) {
                if (Video.VIDEO_IMAGE_DISPLAY_TYPE == 2) {
                    if (rotation == 90 || rotation == 270) {
                        measuredWidth = measuredHeight;
                        measuredHeight = measuredWidth;
                    }
                    double d = (double) i8;
                    double d2 = (double) i7;
                    Double.isNaN(d);
                    Double.isNaN(d2);
                    double d3 = d / d2;
                    double d4 = (double) measuredHeight;
                    double d5 = (double) measuredWidth;
                    Double.isNaN(d4);
                    Double.isNaN(d5);
                    double d6 = d4 / d5;
                    if (d3 > d6) {
                        double d7 = (double) defaultSize;
                        Double.isNaN(d5);
                        Double.isNaN(d7);
                        double d8 = d5 / d7;
                        double d9 = (double) defaultSize2;
                        Double.isNaN(d9);
                        i8 = (int) (d8 * d9);
                        i7 = measuredWidth;
                    } else if (d3 < d6) {
                        double d10 = (double) defaultSize2;
                        Double.isNaN(d4);
                        Double.isNaN(d10);
                        double d11 = d4 / d10;
                        double d12 = (double) defaultSize;
                        Double.isNaN(d12);
                        i7 = (int) (d11 * d12);
                        i8 = measuredHeight;
                    }
                }
            }
            setMeasuredDimension(i7, i8);
        }
        i7 = defaultSize;
        i8 = defaultSize2;
        setMeasuredDimension(i7, i8);
    }
}
