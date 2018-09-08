package com.jiaoshizige.teacherexam.activity.aaa.xx.bean;

import com.jiaoshizige.teacherexam.palyer.PolyvDownloadInfo;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2018/9/5.
 */

public class DownManagerBean implements Serializable{
    private int course_id;//课程id
    private String covermap;//缩略图
    private String title;//课程名称
    private int downNum;
    private boolean check;
    public List<PolyvDownloadInfo> downloadInfos;



    public int getCourse_id() {
        return course_id;
    }

    public void setCourse_id(int course_id) {
        this.course_id = course_id;
    }

    public String getCovermap() {
        return covermap;
    }

    public void setCovermap(String covermap) {
        this.covermap = covermap;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getDownNum() {
        return downNum;
    }

    public void setDownNum(int downNum) {
        this.downNum = downNum;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    public List<PolyvDownloadInfo> getDownloadInfos() {
        return downloadInfos;
    }

    public void setDownloadInfos(List<PolyvDownloadInfo> downloadInfos) {
        this.downloadInfos = downloadInfos;
    }

}
