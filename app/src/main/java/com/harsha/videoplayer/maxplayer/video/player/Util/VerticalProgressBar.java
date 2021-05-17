package com.harsha.videoplayer.maxplayer.video.player.Util;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ProgressBar;

public class VerticalProgressBar extends ProgressBar {
    private int w;
    private int x;
    private int y;
    private int z;

    public void drawableStateChanged() {
        super.drawableStateChanged();
    }

    public VerticalProgressBar(Context context) {
        super(context);
    }

    public VerticalProgressBar(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    public VerticalProgressBar(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i2, i, i4, i3);
        this.x = i;
        this.y = i2;
        this.z = i3;
        this.w = i4;
    }

    public synchronized void onMeasure(int i, int i2) {
        super.onMeasure(i2, i);
        setMeasuredDimension(getMeasuredHeight(), getMeasuredWidth());
    }

    public void onDraw(Canvas canvas) {
        canvas.rotate(-90.0f);
        canvas.translate((float) (-getHeight()), 0.0f);
        super.onDraw(canvas);
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (!isEnabled()) {
            return false;
        }
        int action = motionEvent.getAction();
        if (action == 0) {
            setSelected(true);
            setPressed(true);
        } else if (action == 1) {
            setSelected(false);
            setPressed(false);
        } else if (action == 2) {
            setProgress(getMax() - ((int) ((((float) getMax()) * motionEvent.getY()) / ((float) getHeight()))));
            onSizeChanged(getWidth(), getHeight(), 0, 0);
        }
        return true;
    }

    public synchronized void setProgress(int i) {
        if (i >= 0) {
            super.setProgress(i);
        } else {
            super.setProgress(0);
        }
        onSizeChanged(this.x, this.y, this.z, this.w);
    }
}
