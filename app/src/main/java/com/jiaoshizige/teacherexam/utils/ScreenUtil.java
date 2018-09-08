package com.jiaoshizige.teacherexam.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

public class ScreenUtil {
    public static int getWidth(Context mContext) {
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity) mContext).getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenWidth = dm.widthPixels;
        return screenWidth;
    }

    public static int getHeight(Context mContext) {
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity) mContext).getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenHeight = dm.heightPixels;
        return screenHeight;
    }

    public static boolean isInRight(Context mContext, int xWeight) {
        return (xWeight > getWidth(mContext) * 1 / 2);
    }

    public static boolean isInLeft(Context mContext, int xWeight) {
        return (xWeight < getWidth(mContext) * 1 / 2);
    }
    
    
    /**
     * 是否横屏
     * @param mContext
     * @return
     */
    public static boolean screenIsLanscape(Context mContext){
        boolean ret = false;
        switch (mContext.getResources().getConfiguration().orientation) {
        case Configuration.ORIENTATION_PORTRAIT:
            ret=false;
            break;
        case Configuration.ORIENTATION_LANDSCAPE:
            ret = true;
            break;
        default:
            break;
        }
        return ret;
    }
    
    /**
     * 获取当前屏幕状态
     * @param mContext
     * @return
     */
    public static int getOrientation(Context mContext){
        return mContext.getResources().getConfiguration().orientation;
    }
    
    /**
     * 根据屏幕宽度获取16-10的屏幕高度
     */
    public static int getImageWidth16_10(int heightPx){
    	return  (int) (heightPx * 1.6); 
    }
    
    public static int getImageHeight16_10(int widthPx){
    	return (int)(widthPx/1.6);
    }
    
    public static int getImageHeight16_9(int widthPx){
    	return (int)((widthPx *9)/16);
    }
    
    public static int getImageHeight7_2(int widthPx){
    	return (int)((widthPx *2)/7);
    }
    
    
    /**
	 * 隐藏状态栏
	 * 
	 * @param enable
	 */
	public static void showFullScreen(Activity activity, boolean enable) {
		if (enable) {
			WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
			lp.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
			activity.getWindow().setAttributes(lp);
			activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
		} else {
			WindowManager.LayoutParams attr = activity.getWindow().getAttributes();
			attr.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
			activity.getWindow().setAttributes(attr);
			activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
		}
	}
    public static void  hideBottomUIMenu(Activity activity) {
        //隐藏虚拟按键，并且全屏
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
            View v = activity.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else if (Build.VERSION.SDK_INT >= 19) {
            //for new api versions.
            View decorView = activity.getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }

//    public static int getWidthPx(Context context) {
//        DisplayMetrics var = new DisplayMetrics();
//        ((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(var);
//        return var.widthPixels;
//    }
//
//    public static int getHeightPx(Context context) {
//        DisplayMetrics var = new DisplayMetrics();
//        ((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(var);
//        return var.heightPixels;
//    }

    /**
     * 获取屏幕的宽度
     *
     * @param context
     * @return
     */
    public static int getScreenWidth(Context context) {
        WindowManager manager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();

        return display.getWidth();
    }

    /**
     * 获取屏幕的高度
     *
     * @param context
     * @return
     */
    public static int getScreenHeight(Context context) {
        WindowManager manager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        return display.getHeight();
    }

    /**
     * 获取屏幕中控件顶部位置的高度--即控件顶部的Y点
     *
     * @return
     */
    public static int getScreenViewTopHeight(View view) {
        return view.getTop();
    }

    /**
     * 获取屏幕中控件底部位置的高度--即控件底部的Y点
     *
     * @return
     */
    public static int getScreenViewBottomHeight(View view) {
        return view.getBottom();
    }

    /**
     * 获取屏幕中控件左侧的位置--即控件左侧的X点
     *
     * @return
     */
    public static int getScreenViewLeftHeight(View view) {
        return view.getLeft();
    }

    /**
     * 获取屏幕中控件右侧的位置--即控件右侧的X点
     *
     * @return
     */
    public static int getScreenViewRightHeight(View view) {
        return view.getRight();
    }





}
