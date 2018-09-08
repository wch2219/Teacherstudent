package com.jiaoshizige.teacherexam.activity.aaa.xx.bean;

import com.jiaoshizige.teacherexam.palyer.PolyvDownloadInfo;

import java.util.List;

/**
 * Created by Administrator on 2018/9/7.
 */

public class ChapterBean {
    private int course_id;
    private String chapter;
    private int chapter_id;
    private List<PolyvDownloadInfo> infos;

    public int getCourse_id() {
        return course_id;
    }

    public void setCourse_id(int course_id) {
        this.course_id = course_id;
    }

    public String getChapter() {
        return chapter;
    }

    public void setChapter(String chapter) {
        this.chapter = chapter;
    }

    public int getChapter_id() {
        return chapter_id;
    }

    public void setChapter_id(int chapter_id) {
        this.chapter_id = chapter_id;
    }

    public List<PolyvDownloadInfo> getInfos() {
        return infos;
    }

    public void setInfos(List<PolyvDownloadInfo> infos) {
        this.infos = infos;
    }
}
