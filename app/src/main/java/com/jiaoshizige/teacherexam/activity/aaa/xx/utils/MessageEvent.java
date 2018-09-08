package com.jiaoshizige.teacherexam.activity.aaa.xx.utils;

/**
 * Created by Administrator on 2018/9/7.
 */

public class MessageEvent {
    private String vid;
    private int bitRate;
    public MessageEvent(String vid,int bitRate) {
        this.vid = vid;
        this.bitRate = bitRate;
    }

    public String getVid() {
        return vid;
    }

    public void setVid(String vid) {
        this.vid = vid;
    }

    public int getBitRate() {
        return bitRate;
    }

    public void setBitRate(int bitRate) {
        this.bitRate = bitRate;
    }
}
