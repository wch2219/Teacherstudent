package com.jiaoshizige.teacherexam.model;

import java.util.List;

/**
 * Created by Administrator on 2017/a1/6.
 */

public class ExamSubjectModel {
    private String title;
    private String subtitle;
    private String answera;
    private String answerb;
    private String answerc;
    private String answerd;
    private String answere;
    private String type;
    private List<String> otheranswer;

    public List<String> getOtheranswer() {
        return otheranswer;
    }

    public void setOtheranswer(List<String> otheranswer) {
        this.otheranswer = otheranswer;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getAnswera() {
        return answera;
    }

    public void setAnswera(String answera) {
        this.answera = answera;
    }

    public String getAnswerb() {
        return answerb;
    }

    public void setAnswerb(String answerb) {
        this.answerb = answerb;
    }

    public String getAnswerc() {
        return answerc;
    }

    public void setAnswerc(String answerc) {
        this.answerc = answerc;
    }

    public String getAnswerd() {
        return answerd;
    }

    public void setAnswerd(String answerd) {
        this.answerd = answerd;
    }

    public String getAnswere() {
        return answere;
    }

    public void setAnswere(String answere) {
        this.answere = answere;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
