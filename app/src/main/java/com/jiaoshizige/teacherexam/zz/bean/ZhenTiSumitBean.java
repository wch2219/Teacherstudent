package com.jiaoshizige.teacherexam.zz.bean;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ZhenTiSumitBean {


    /**
     * keguan : 4
     * fenshu : [{"tixing":"单选题","defen":"4/40","defenlv":"10.00%"},{"tixing":"简答题","defen":"0/30","defenlv":"0.00%"},{"tixing":"材料分析题","defen":"0/40","defenlv":"0.00%"},{"tixing":"教学设计题","defen":"0/40","defenlv":"0.00%"}]
     * zhenti_id : 37
     * zhuguan : 0
     * time : 00:00:09
     * shiti_id : [{"138":"B#1"},{"140":"A#1"},{"195":"B#1"},{"147":"C#1"},{"180":"C#1"},{"187":"C#1"},{"176":"B#1"},{"39":"B#1"},{"43":"B#1"},{"42":"B#1"},{"58":"B#1"},{"56":"C#1"},{"141":"C#1"},{"210":"B#1"},{"164":"#1"},{"157":"#1"},{"158":"#1"},{"217":"#1"},{"204":"#1"},{"199":"#1"},{"64":"#2"},{"151":"#2"},{"219":"#2"},{"194":"#6"},{"203":"#6"}]
     * eid : 3
     * tiku_id : 53
     * paper_totle : 150
     * moni_totle : 4
     * name : 2016年下半年小学教师资格考试教育教学知识与能力
     */

    private String keguan;
    private String zhenti_id;
    private String zhuguan;
    private String time;
    private String eid;
    private String tiku_id;
    private String paper_totle;
    private String moni_totle;
    private String name;
    private List<FenshuBean> fenshu;
    private List<LinkedHashMap<String, String>> shiti_id;


    public List<LinkedHashMap<String, String>> getShiti_id() {
        return shiti_id;
    }

    public void setShiti_id(List<LinkedHashMap<String, String>> shiti_id) {
        this.shiti_id = shiti_id;
    }

    public String getKeguan() {
        return keguan;
    }

    public void setKeguan(String keguan) {
        this.keguan = keguan;
    }

    public String getZhenti_id() {
        return zhenti_id;
    }

    public void setZhenti_id(String zhenti_id) {
        this.zhenti_id = zhenti_id;
    }

    public String getZhuguan() {
        return zhuguan;
    }

    public void setZhuguan(String zhuguan) {
        this.zhuguan = zhuguan;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getEid() {
        return eid;
    }

    public void setEid(String eid) {
        this.eid = eid;
    }

    public String getTiku_id() {
        return tiku_id;
    }

    public void setTiku_id(String tiku_id) {
        this.tiku_id = tiku_id;
    }

    public String getPaper_totle() {
        return paper_totle;
    }

    public void setPaper_totle(String paper_totle) {
        this.paper_totle = paper_totle;
    }

    public String getMoni_totle() {
        return moni_totle;
    }

    public void setMoni_totle(String moni_totle) {
        this.moni_totle = moni_totle;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<FenshuBean> getFenshu() {
        return fenshu;
    }

    public void setFenshu(List<FenshuBean> fenshu) {
        this.fenshu = fenshu;
    }


    public static class FenshuBean {
        /**
         * tixing : 单选题
         * defen : 4/40
         * defenlv : 10.00%
         */

        private String tixing;
        private String defen;
        private String defenlv;

        public String getTixing() {
            return tixing;
        }

        public void setTixing(String tixing) {
            this.tixing = tixing;
        }

        public String getDefen() {
            return defen;
        }

        public void setDefen(String defen) {
            this.defen = defen;
        }

        public String getDefenlv() {
            return defenlv;
        }

        public void setDefenlv(String defenlv) {
            this.defenlv = defenlv;
        }
    }

}
