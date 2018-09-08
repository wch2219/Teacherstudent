package com.jiaoshizige.teacherexam.utils;
/**
 * 功能：SharedPreferences存储方法
 */
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class Sphelper {

	private static SharedPreferences sp;
	public static void initial(Context mContext, String fileName) {
		sp = mContext.getSharedPreferences(fileName, 0);
	}
	//存储方式,存储位置：/data/data/<package name>/shared_prefs
	public static void save(Context mContext, String fileName, String key,
			Object value) {
		initial(mContext, fileName);
		if (value instanceof String) {
			sp.edit().putString(key, (String) value).commit();
		} else if (value instanceof Boolean) {
			sp.edit().putBoolean(key, (Boolean) value).commit();
		} else if (value instanceof Integer) {
			sp.edit().putInt(key, (Integer) value).commit();
		} else if (value instanceof Float) {
			sp.edit().putFloat(key, (Float) value).commit();
		} else if (value instanceof Long) {
			sp.edit().putLong(key, (Long) value).commit();
		}
	}

	// 获取SharedPreferences数据指定key所对应的value，如果该key不存在，返回默认值false(boolen),""(string);
	public static Boolean getBoolean(Context mContext, String fileName,
			String key) {
		initial(mContext, fileName);
		return sp.getBoolean(key, false);
	}

	public static String getString(Context mContext, String fileName,
			String key) {
		initial(mContext, fileName);
		return sp.getString(key, "");
	}
	
	public static int getInt(Context mContext,String fileName,String key)
	{
		initial(mContext, fileName);
		return sp.getInt(key, 0);
	}
	/**
	 * 清空所有数据
	 * @param mContext
	 * @param fileName
	 */
	public static void removeFile(Context mContext, String fileName) {
		initial(mContext, fileName);
		Editor editor = sp.edit();
		editor.clear();
		editor.commit();
	}

}