package com.harsha.videoplayer.maxplayer.video.player.Util;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.harsha.videoplayer.maxplayer.video.player.R;

import java.text.DecimalFormat;
import java.util.Date;

public class Utils {
    public static Dialog customDialog;

    public static final String makeShortTimeString(Context context, long j) {
        int i = (int) j;
        int i2 = i % 60;
        int i3 = (i / 60) % 60;
        int i4 = i / 3600;
        if (i4 > 0) {
            return String.format("%02d:%02d:%02d", Integer.valueOf(i4), Integer.valueOf(i3), Integer.valueOf(i2));
        }
        return String.format("%02d:%02d", Integer.valueOf(i3), Integer.valueOf(i2));
    }

    public static String setDuration(long j) {
        String str;
        String str2;
        String str3;
        int i = (int) (j / 3600000);
        long j2 = j % 3600000;
        int i2 = ((int) j2) / 60000;
        int i3 = (int) ((j2 % 60000) / 1000);
        if (i < 10 && i != 0) {
            str = "0" + i + ":";
        } else if (i >= 10) {
            str = i + ":";
        } else {
            str = "";
        }
        if (i2 < 10) {
            str2 = "0" + i2;
        } else {
            str2 = i2 + "";
        }
        if (i3 < 10) {
            str3 = "0" + i3;
        } else {
            str3 = i3 + "";
        }
        return str + str2 + ":" + str3;
    }

    public static String formateSize(long j) {
        if (j <= 0) {
            return "0";
        }
        double d = (double) j;
        int log10 = (int) (Math.log10(d) / Math.log10(1024.0d));
        StringBuilder sb = new StringBuilder();
        DecimalFormat decimalFormat = new DecimalFormat("#,##0.#");
        double pow = Math.pow(1024.0d, (double) log10);
        Double.isNaN(d);
        Double.isNaN(d);
        sb.append(decimalFormat.format(d / pow));
        sb.append(" ");
        sb.append(new String[]{"B", "KB", "MB", "GB", "TB"}[log10]);
        return sb.toString();
    }

    public static String getDateFormate(String str) {
        return (str == null || TextUtils.isEmpty(str)) ? "" : DateFormat.format("dd/MM/yyyy", new Date(Long.parseLong(str))).toString();
    }

    public static void showProgressDialog(Activity activity) {
        System.out.println("Show");
        Dialog dialog = customDialog;
        if (dialog != null) {
            dialog.dismiss();
            customDialog = null;
        }
        customDialog = new Dialog(activity);
        View inflate = LayoutInflater.from(activity).inflate(R.layout.progress_dialog, (ViewGroup) null);
        customDialog.setCancelable(false);
        customDialog.setContentView(inflate);
        if (!customDialog.isShowing() && !activity.isFinishing()) {
            customDialog.show();
        }
    }

    public static void hideProgressDialog(Activity activity) {
        System.out.println("Hide");
        Dialog dialog = customDialog;
        if (dialog != null && dialog.isShowing()) {
            customDialog.dismiss();
        }
    }
}
