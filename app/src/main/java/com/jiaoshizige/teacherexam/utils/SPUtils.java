package com.jiaoshizige.teacherexam.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.jiaoshizige.teacherexam.base.MyApplication;


/**
 * Created by gtz on 2017/6/20 0020.
 */

public class SPUtils {
    public static final int TYPE_INT=0;
    public static final int TYPE_STRING =1;
    public static final int TYPE_BOOLEAN =2;
    private static Object valueData;
    private static int valueInt;
    private static String valueString;
    private static boolean valueBoolean;
    private Context context= MyApplication.getContext();
    private static SharedPreferences sp= MyApplication.getSp();

    /**
     * 获取sp值
     * @param keyValue  Sp数据的键值
     * @param valueType 将要获取的数据类型
     * @return
     */
    public static Object getSpValues(String keyValue, int valueType){
        switch (valueType){
            case TYPE_INT:
                valueData=getIntValues(keyValue);
                break;
            case TYPE_STRING:
                valueData=getStringValues(keyValue);
                break;
            case TYPE_BOOLEAN:
                valueData=getBooleanValues(keyValue);
                break;
        }
        return valueData;
    }

    /**
     * 获取int类型的数据
     * @param keyValue SP数据键值
     * @return
     */
    private static int getIntValues(String keyValue){
        valueInt=sp.getInt(keyValue,0);
        return valueInt;
    }

    /**
     * 获取String类型的数据
     * @param keyValue SP数据键值
     * @return
     */
    private static String getStringValues(String keyValue){
        valueString=sp.getString(keyValue,"");
        return valueString;
    }

    /**
     * 获取boolean类型的数据
     * @param keyValue SP数据键值
     * @return
     */
    private static boolean getBooleanValues(String keyValue){
        valueBoolean =sp.getBoolean(keyValue,false);
        return valueBoolean;
    }

    /**
     * 存储sp数据
     * @param valueType 数据类型
     * @param keyValue  键值
     * @param valueData 存储的数据
     * @return 是否保存成功 true为保存成功 false为保存失败
     */
    public static boolean setSPValues(int valueType, String keyValue, Object valueData){
        boolean commitSuccess = false;
            switch (valueType){

                case TYPE_INT:
                    commitSuccess= sp.edit().putInt(keyValue,(int)valueData).commit();
                    break;
                case TYPE_STRING:
                    commitSuccess=  sp.edit().putString(keyValue, (String) valueData).commit();
                    break;
                case TYPE_BOOLEAN:
                    commitSuccess=  sp.edit().putBoolean(keyValue, (Boolean) valueData).commit();
                    break;
            }
        return commitSuccess;
    }



}
