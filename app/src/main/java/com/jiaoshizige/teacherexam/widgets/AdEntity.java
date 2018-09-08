package com.jiaoshizige.teacherexam.widgets;


import com.jiaoshizige.teacherexam.model.InformationResponse;

public class AdEntity{
    private String mFront; //前面的文字
    private String mBack; //后面的文字
//    private String mUrl;//包含的链接
    private InformationResponse.mData mUrl;

    public AdEntity(String mFront, String mBack, InformationResponse.mData mUrl) {
        this.mFront = mFront;
        this.mBack = mBack;
        this.mUrl = mUrl;
    }

    public InformationResponse.mData getmUrl() {
        return mUrl;
    }

    public void setmUrl(InformationResponse.mData mUrl) {
        this.mUrl = mUrl;
    }

    public String getmFront() {
        return mFront;
    }

    public void setmFront(String mFront) {
        this.mFront = mFront;
    }

    public String getmBack() {
        return mBack;
    }

    public void setmBack(String mBack) {
        this.mBack = mBack;
    }
}