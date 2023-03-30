package com.wpl.weatherforecast.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.wpl.weatherforecast.R;

public class SharepreferencesUtils {


    /**
     * 获取背景图
     *
     * @param context
     * @return
     */
    public static int getbackground(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("xml", 0);
        int bgId = sharedPreferences.getInt("bgId", R.drawable.background);
        return bgId;
    }

    /**
     * 设置背景图
     *
     * @param context
     */
    public static void setbackground(Context context, int bgId) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("xml", 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("bgId", bgId);

        editor.commit();
    }


}
