package com.jiaoshizige.teacherexam.utils;

public class SDOtherUtil {
    public static int strToInt(String strInt){
        try {
            return Integer.valueOf(strInt);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static double strToDouble(String strDouble){
        try {
            return Double.valueOf(strDouble);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static float strToFloat(String strFloat){
        try {
            return Float.valueOf(strFloat);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return 0;
        }
    }
}
