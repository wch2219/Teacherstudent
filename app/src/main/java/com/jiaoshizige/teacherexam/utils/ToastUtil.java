package com.jiaoshizige.teacherexam.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * 
 * @author MaXiao
 *
 */
public class ToastUtil {
	private static Context mContext;
	private static Toast mToast;

	public static void init(Context context) {
		if (context == null)
			throw new IllegalArgumentException("传入的context参数不能为null");

		mContext = context;
	}

	public static void showLongToast(String text) {
		showToast(text, Toast.LENGTH_LONG);
	}

	public static void showLongToast(int resId) {
		showToast(resId, Toast.LENGTH_LONG);
	}

	public static void showShortToast(String text) {
		showToast(text, Toast.LENGTH_SHORT);
	}

	public static void showShortToast(int resId) {
		showToast(resId, Toast.LENGTH_SHORT);
	}

	public static void showToast(int resId, int duration) {
		showToast(mContext.getResources().getString(resId), duration);
	}

	public static void showToast(String text, int duration) {
		if (mContext == null)
			throw new IllegalStateException("使用前必须调用init函数初始化");
		if (mToast == null)
		{
			mToast = Toast.makeText(mContext, text, duration);
		}
		else {
			mToast.cancel();
			mToast = Toast.makeText(mContext, text, duration);
		}
//		mToast.setGravity(Gravity.CENTER, 0, 0);
		mToast.show();
	}

	public static void cancel() {
		mToast.cancel();
	}

}
