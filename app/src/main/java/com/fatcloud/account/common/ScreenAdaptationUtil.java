package com.fatcloud.account.common;

import android.app.Activity;
import android.app.Application;
import android.content.ComponentCallbacks;
import android.content.res.Configuration;
import android.util.DisplayMetrics;


/**
 * Created  2020/9/3 0003 16:13.
 * <p>
 * 自定义设备的Density  用户屏幕适配，该适配方案，来源于今日头条的适配方案
 * <p>
 * 使用方法在基类的Activity中的onCreate方法中调用即可，注意使用时要在setContentView()之前！
 *
 */
public class ScreenAdaptationUtil {

    private static float sNoncompatDensity;
    private static float sNoncompatScaledDensity;

    //默认假设 设计图宽360dp 我们根据实际设计图的尺寸修改
    private static int designWidth = 750;
    //默认假设 设计图高640dp 我们根据实际设计图的尺寸修改
    private static int designHeight = 720;

    /**
     * 初始设计图尺寸 单位 dp
     * @param width
     * @param height
     */
    public static void initDesignSize(int width, int height) {
        designHeight = height;
        designWidth = width;
    }

    private static void setCustomDensity(Activity activity, final Application application, boolean isWidth) {
        DisplayMetrics displayMetrics = application.getResources().getDisplayMetrics();
        if (sNoncompatDensity == 0) {
            sNoncompatDensity = displayMetrics.density;
            sNoncompatScaledDensity = displayMetrics.scaledDensity;
            application.registerComponentCallbacks(new ComponentCallbacks() {
                @Override
                public void onConfigurationChanged(Configuration newConfig) {
                    if (newConfig != null && newConfig.fontScale > 0) {
                        sNoncompatScaledDensity = application.getResources().getDisplayMetrics().scaledDensity;
                    }
                }

                @Override
                public void onLowMemory() {

                }
            });
        }


        float targetDensity;
        if (isWidth) {
            //根据宽适配
            targetDensity = displayMetrics.widthPixels / designWidth;
        } else {
            //根据高适配
            targetDensity = displayMetrics.heightPixels / designHeight;
        }
        float targetScaledDensity = targetDensity * (sNoncompatScaledDensity / sNoncompatDensity);
        int targetDensityDpi = (int) (160 * targetDensity);
        displayMetrics.density = targetDensity;
        displayMetrics.scaledDensity = targetScaledDensity;
        displayMetrics.densityDpi = targetDensityDpi;

        DisplayMetrics activityDisplayMetrics = activity.getResources().getDisplayMetrics();
        activityDisplayMetrics.density = targetDensity;
        activityDisplayMetrics.scaledDensity = targetScaledDensity;
        activityDisplayMetrics.densityDpi = targetDensityDpi;
    }

    /**
     * 根据设计图宽设定density
     *
     * @param activity
     * @param application
     */
    public static void setDensityByWidth(Activity activity, final Application application) {
        setCustomDensity(activity, application, true);
    }

    /**
     * 根据设计图高设定density
     *
     * @param activity
     * @param application
     */
    public static void setDensityByHeight(Activity activity, final Application application) {
        setCustomDensity(activity, application, false);
    }
}
