package com.jiaoshizige.teacherexam.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/1/26.
 */

public class AddLearnRecordModel implements Serializable{
    private String group_id;//
    private String course_id;//
    private String course_name;//
    private String chapter_id;//
    private String chapter_name;//
    private String section_id;//
    private String section_name;//
    private String learn_time;//
    private String type;
    private int class_id;//最外层的课程id或者班级id
    private String classname;//最外层的课程名称
    private String chapter;//章节名
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getGroup_id() {
        return group_id;
    }

    public void setGroup_id(String group_id) {
        this.group_id = group_id;
    }

    public String getCourse_id() {
        return course_id;
    }

    public void setCourse_id(String course_id) {
        this.course_id = course_id;
    }

    public String getCourse_name() {
        return course_name;
    }

    public void setCourse_name(String course_name) {
        this.course_name = course_name;
    }

    public String getChapter_id() {
        return chapter_id;
    }

    public void setChapter_id(String chapter_id) {
        this.chapter_id = chapter_id;
    }

    public String getChapter_name() {
        return chapter_name;
    }

    public void setChapter_name(String chapter_name) {
        this.chapter_name = chapter_name;
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

    public int getClass_id() {
        return class_id;
    }

    public void setClass_id(int class_id) {
        this.class_id = class_id;
    }

    public String getClassname() {
        return classname;
    }

    public void setClassname(String classname) {
        this.classname = classname;
    }

    public String getChapter() {
        return chapter;
    }

    public void setChapter(String chapter) {
        this.chapter = chapter;
    }
}
