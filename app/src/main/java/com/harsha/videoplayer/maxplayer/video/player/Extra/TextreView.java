package com.harsha.videoplayer.maxplayer.video.player.Extra;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class TextreView extends android.view.TextureView {
    protected static final String TAG = "JZResizeTextureView";
    public int currentVideoHeight;
    public int currentVideoWidth;

    public TextreView(Context context) {
        super(context);
        this.currentVideoWidth = 0;
        this.currentVideoHeight = 0;
        this.currentVideoWidth = 0;
        this.currentVideoHeight = 0;
    }

    public TextreView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.currentVideoWidth = 0;
        this.currentVideoHeight = 0;
        this.currentVideoWidth = 0;
        this.currentVideoHeight = 0;
    }

    public void setVideoSize(int i, int i2) {
        if (this.currentVideoWidth != i || this.currentVideoHeight != i2) {
            this.currentVideoWidth = i;
            this.currentVideoHeight = i2;
            requestLayout();
        }
    }

    public void setRotation(float f) {
        if (f != getRotation()) {
            super.setRotation(f);
            requestLayout();
        }
    }

    public void onMeasure(int i, int i2) {
        int i3;
        int i4;
        Log.i(TAG, "onMeasure  [" + hashCode() + "] ");
        int rotation = (int) getRotation();
        int i5 = this.currentVideoWidth;
        int i6 = this.currentVideoHeight;
        int measuredHeight = ((View) getParent()).getMeasuredHeight();
        int measuredWidth = ((View) getParent()).getMeasuredWidth();
        if (!(measuredWidth == 0 || measuredHeight == 0 || i5 == 0 || i6 == 0 || Video.VIDEO_IMAGE_DISPLAY_TYPE != 1)) {
            if (rotation == 90 || rotation == 270) {
                measuredWidth = measuredHeight;
            }
            i6 = (i5 * measuredHeight) / measuredWidth;
        }
        if (rotation == 90 || rotation == 270) {
            i3 = i;
            i4 = i2;
        } else {
            i4 = i;
            i3 = i2;
        }
        int defaultSize = getDefaultSize(i5, i4);
        int defaultSize2 = getDefaultSize(i6, i3);
        if (i5 > 0 && i6 > 0) {
            int mode = MeasureSpec.getMode(i4);
            int size = MeasureSpec.getSize(i4);
            int mode2 = MeasureSpec.getMode(i3);
            int size2 = MeasureSpec.getSize(i3);
            Log.i(TAG, "widthMeasureSpec  [" + MeasureSpec.toString(i4) + "]");
            Log.i(TAG, "heightMeasureSpec [" + MeasureSpec.toString(i3) + "]");
            int i7 = 0;
            if (mode == 1073741824 && mode2 == 1073741824) {
                int i8 = size2 * i5;
                int i9 = size * i6;
                if (i8 < i9) {
                    int i10 = i8 / i6;
                } else if (i8 > i9) {
                    i7 = i9 / i5;
                }
            } else if (mode == 1073741824) {
                int i11 = (size * i6) / i5;
                if (mode2 == Integer.MIN_VALUE && i11 > size2) {
                    int i12 = (size2 * i5) / i6;
                }
                i7 = i11;
            } else if (mode2 == 1073741824) {
                int i13 = (size2 * i5) / i6;
                if (mode == Integer.MIN_VALUE && i13 > size) {
                    i7 = (size * i6) / i5;
                }
            } else {
                int i14 = (mode2 != Integer.MIN_VALUE || i6 <= size2) ? i5 : (size2 * i5) / i6;
                if (mode == Integer.MIN_VALUE && i14 > size) {
                    int i15 = (size * i6) / i5;
                }
            }
            defaultSize = size;
            defaultSize2 = i7;
        }
        if (!(measuredWidth == 0 || measuredHeight == 0 || i5 == 0 || i6 == 0)) {
            if (Video.VIDEO_IMAGE_DISPLAY_TYPE != 3 && Video.VIDEO_IMAGE_DISPLAY_TYPE == 2) {
                if (rotation == 90 || rotation == 270) {
                    measuredWidth = measuredHeight;
                }
                double d = (double) i6;
                double d2 = (double) i5;
                Double.isNaN(d);
                Double.isNaN(d2);
                Double.isNaN(d);
                Double.isNaN(d2);
                double d3 = d / d2;
                double d4 = (double) measuredHeight;
                double d5 = (double) measuredWidth;
                Double.isNaN(d4);
                Double.isNaN(d5);
                Double.isNaN(d4);
                Double.isNaN(d5);
                double d6 = d4 / d5;
                if (d3 > d6) {
                    double d7 = (double) defaultSize;
                    Double.isNaN(d5);
                    Double.isNaN(d7);
                    Double.isNaN(d5);
                    Double.isNaN(d7);
                    double d8 = d5 / d7;
                    double d9 = (double) defaultSize2;
                    Double.isNaN(d9);
                    Double.isNaN(d9);
                    measuredHeight = (int) (d8 * d9);
                    i5 = measuredWidth;
                } else if (d3 < d6) {
                    double d10 = (double) defaultSize2;
                    Double.isNaN(d4);
                    Double.isNaN(d10);
                    Double.isNaN(d4);
                    Double.isNaN(d10);
                    double d11 = d4 / d10;
                    double d12 = (double) defaultSize;
                    Double.isNaN(d12);
                    Double.isNaN(d12);
                    i5 = (int) (d11 * d12);
                }
                setMeasuredDimension(i5, measuredHeight);
            }
            measuredHeight = i6;
            setMeasuredDimension(i5, measuredHeight);
        }
        setMeasuredDimension(defaultSize, defaultSize2);
    }
}
