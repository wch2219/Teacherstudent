package com.jiaoshizige.teacherexam.model;

import com.jiaoshizige.teacherexam.palyer.PolyvDownloadInfo;

import java.util.List;

/**
 * Created by Administrator on 2018/1/19.
 */

public class RecordModel {
    public String chapter;
    private boolean check;
    public List<PolyvDownloadInfo> mPolyvDownloadInfo;

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    public String getChapter() {
        return chapter;
    }

    public void setChapter(String chapter) {
        this.chapter = chapter;
    }

    public List<PolyvDownloadInfo> getmPolyvDownloadInfo() {
        return mPolyvDownloadInfo;
    }

    public void setmPolyvDownloadInfo(List<PolyvDownloadInfo> mPolyvDownloadInfo) {
        this.mPolyvDownloadInfo = mPolyvDownloadInfo;
    }
}
