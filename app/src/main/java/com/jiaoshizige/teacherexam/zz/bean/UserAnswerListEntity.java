package com.jiaoshizige.teacherexam.zz.bean;

public class UserAnswerListEntity {
    private String ztid;
    private String data;
    private int is_sumit;
    private String userid;

    public UserAnswerListEntity(String userid, String ztid, String data, int is_sumit) {
        this.userid = userid;
        this.ztid = ztid;
        this.data = data;
        this.is_sumit = is_sumit;
    }

    public String getZtid() {
        return ztid;
    }

    public void setZtid(String ztid) {
        this.ztid = ztid;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getIs_sumit() {
        return is_sumit;
    }

    public void setIs_sumit(int is_sumit) {
        this.is_sumit = is_sumit;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }
}
