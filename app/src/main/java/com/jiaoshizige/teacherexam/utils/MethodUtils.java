package com.jiaoshizige.teacherexam.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.jiaoshizige.teacherexam.base.MyApplication;


import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/3/23 0023.
 */

public class MethodUtils {

//    public static ArrayList covertMapToArrayList(HashMap map){
//        ArrayList<ShopCarBean.DataBeanX.DataBean> list = new ArrayList();
//        for(Iterator it = map.entrySet().iterator(); it.hasNext();){
//            Map.Entry entry_date = (Map.Entry)it.next();
//            ShopCarBean.DataBeanX.DataBean item = (ShopCarBean.DataBeanX.DataBean)entry_date.getValue(); //  StatItem 是我自己定义的一个简单的JAVABEAN 对象类型
//            list.add(item);
//        }
//        return list;
//    }
    private static Context context = MyApplication.getContext();

    private static String json;
    /*MD string 加密*/
    public static String md5(String string) {
        byte[] hash;
        try {
            hash = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Huh, MD5 should be supported?", e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Huh, UTF-8 should be supported?", e);
        }

        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10)
                hex.append("0");
            hex.append(Integer.toHexString(b & 0xFF));
        }
        return hex.toString();
    }


    /**
     * 吐司字符串
     *
     * @param msg 吐司内容
     */
    public static void showToast(String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    /**
     * 吐司stinrg.xml文件中字符串
     *
     * @param strId 字符串id
     */
    public static void showToast(int strId) {
        Toast.makeText(MyApplication.getContext(), strId, Toast.LENGTH_SHORT).show();
    }

    /**
     * 弹出输入框
     *
     * @param activity 当前activity
     */
    public static void showInputMethod(Activity activity) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService
                (Context.INPUT_METHOD_SERVICE);
        if (activity.getCurrentFocus() != null) {
            imm.toggleSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(),
                    InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * 关闭输入框
     *
     * @param activity 当前activity
     */
    public static void dismissInputMethod(Activity activity) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService
                (Context.INPUT_METHOD_SERVICE);
        if (activity.getCurrentFocus() != null) {
            if (imm.isActive()) {
                imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(),
                        0);
            }
        }
    }


    /**
     * 输出指定信息
     * @param info 信息
     */
    public static void myInfo(String info){
        Log.e("--------------",info);
    }
    /**
     * 获得EditTextView 中的字符串
     *
     */
    public static String getETString(EditText ET){
        String etString=null;
        if (ET!=null){
            etString=ET.getText().toString().trim();
        }
        return etString;
    }

    public static boolean isAppAlive(Context context, String packageName) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(100);
        if (list.size() <= 0) {
            return false;
        }
        for (ActivityManager.RunningTaskInfo info : list) {
            if (info.baseActivity.getPackageName().equals(packageName)) {
                return true;
            }
        }
        return false;

    }
}
