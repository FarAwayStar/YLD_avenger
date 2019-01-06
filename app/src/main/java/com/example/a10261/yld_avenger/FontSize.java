package com.example.a10261.yld_avenger;

import android.app.Activity;
import android.content.res.Configuration;
import android.util.DisplayMetrics;


public class FontSize {

    public static final float SMALL=0.7f;
    public static final float Middle=1.0f;
    public static final float Big=1.3f;
    public static void scaleTextSize(Activity activity,float size) {

        Configuration configuration = activity.getResources().getConfiguration();
        configuration.fontScale = size; //0.85 small size, 1 normal size, 1.5 big etc
        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        metrics.scaledDensity = configuration.fontScale * metrics.density;
        activity.getBaseContext().getResources().updateConfiguration(configuration, metrics);//更新全局的配置
    }
}
