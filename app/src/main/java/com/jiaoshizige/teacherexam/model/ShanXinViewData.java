package com.jiaoshizige.teacherexam.model;

/**
 * Created by jiyang on 16-12-25.
 * 扇形图数据
 */

public class ShanXinViewData {
    public String name; //名字
    public int value;   //数值

    public int color;   //颜色
    public float percentage; //百分比
    public float angle; //角度

    public ShanXinViewData(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public float getPercentage() {
        return percentage;
    }

    public void setPercentage(float percentage) {
        this.percentage = percentage;
    }

    public float getAngle() {
        return angle;
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }
}
