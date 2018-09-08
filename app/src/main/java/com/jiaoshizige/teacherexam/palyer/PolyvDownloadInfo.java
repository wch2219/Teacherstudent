package com.jiaoshizige.teacherexam.palyer;

import java.io.Serializable;

public class PolyvDownloadInfo implements Serializable {
    // vid
    private String vid;
    // 时长
    private String duration;
    // 文件大小
    private long filesize;
    // 码率
    private int bitrate;
    // 标题
    private String title;
    // 已下载的文件大小(mp4)/已下载的文件个数(ts)
    private long percent;
    // 总文件大小(mp4)/总个数(ts)
    private long total;
    private String chapter;//章节名
    private String chapter_id;//章id
    private String section_id;//节id
    private String section_name;//节名
    private int progress;//下载进度100%
    private int course_id;//课程id或者班级id 单科的话班级等于课程
    private int parent_id;//视频父id
    private int sid;//视频id
    private String classname;//最外层的课程名称
    private int class_id;//最外层的课程id或者班级id
    private String type;
    private String learn_time;
    private boolean check;
    private long downspend;
    public PolyvDownloadInfo(){}

    public PolyvDownloadInfo(String vid, String duration, long filesize, int bitrate, String title,int progress,String chapter) {
        this.vid = vid;
        this.duration = duration;
        this.filesize = filesize;
        this.bitrate = bitrate;
        this.title = title;
        this.chapter = chapter;
        this.progress = progress;
      /*  this.parent_id = parent_id;
        this.course_id = course_id;*/
    }
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

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

    public String getVid() {
        return vid;
    }

    public void setVid(String vid) {
        this.vid = vid;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public long getFilesize() {
        return filesize;
    }

    public void setFilesize(long filesize) {
        this.filesize = filesize;
    }

    public int getBitrate() {
        return bitrate;
    }

    public void setBitrate(int bitrate) {
        this.bitrate = bitrate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getPercent() {
        return percent;
    }

    public void setPercent(long percent) {
        this.percent = percent;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public int getCourse_id() {
        return course_id;
    }

    public void setCourse_id(int course_id) {
        this.course_id = course_id;
    }

    public int getParent_id() {
        return parent_id;
    }

    public void setParent_id(int parent_id) {
        this.parent_id = parent_id;
    }

    public int getSid() {
        return sid;
    }

    public void setSid(int sid) {
        this.sid = sid;
    }

    public String getClassname() {
        return classname;
    }

    public void setClassname(String classname) {
        this.classname = classname;
    }

    public int getClass_id() {
        return class_id;
    }

    public void setClass_id(int class_id) {
        this.class_id = class_id;
    }

    public String getChapter_id() {
        return chapter_id;
    }

    public void setChapter_id(String chapter_id) {
        this.chapter_id = chapter_id;
    }

    public String getSection_id() {
        return section_id;
    }

    public void setSection_id(String section_id) {
        this.section_id = section_id;
    }

    public String getSection_name() {
        return section_name;
    }

    public void setSection_name(String section_name) {
        this.section_name = section_name;
    }

    public String getLearn_time() {
        return learn_time;
    }

    public void setLearn_time(String learn_time) {
        this.learn_time = learn_time;
    }

    public long getDownspend() {
        return downspend;
    }

    public void setDownspend(long downspend) {
        this.downspend = downspend;
    }

    @Override
    public String toString() {
        return "PolyvDownloadInfo{" +
                "vid='" + vid + '\'' +
                ", duration='" + duration + '\'' +
                ", filesize=" + filesize +
                ", bitrate=" + bitrate +
                ", title='" + title + '\'' +
                ", percent=" + percent +
                ", total=" + total +
                ", chapter='" + chapter + '\'' +
                ", chapter_id='" + chapter_id + '\'' +
                ", section_id='" + section_id + '\'' +
                ", section_name='" + section_name + '\'' +
                ", progress=" + progress +
                ", course_id=" + course_id +
                ", parent_id=" + parent_id +
                ", sid=" + sid +
                ", classname='" + classname + '\'' +
                ", class_id=" + class_id +
                ", type='" + type + '\'' +
                ", learn_time='" + learn_time + '\'' +
                ", check=" + check +
                ", downspend=" + downspend +
                '}';
    }
}
