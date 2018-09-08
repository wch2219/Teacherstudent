package com.jiaoshizige.teacherexam.zz.database;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

@DatabaseTable(tableName = "useranswer") // 指定数据表的名称

public class UserAnswer {
    @DatabaseField(generatedId = true, columnName = "id", useGetSet = true)
    private int id;
    @DatabaseField(columnName = "ztid", useGetSet = true)
    private String ztid;
    @DatabaseField(columnName = "data", useGetSet = true)
    private String data;
    @DatabaseField(columnName = "is_sumit", useGetSet = true, defaultValue = "0")
    private String is_sumit;
    @DatabaseField(columnName = "userid", useGetSet = true)
    private String userid;
    @DatabaseField(columnName = "time", useGetSet = true)
    private String time;
    @DatabaseField(columnName = "tiku_id", useGetSet = true)
    private String tiku_id;
    @DatabaseField(columnName = "fenshu", useGetSet = true)
    private String fenshu;
    @DatabaseField(columnName = "name", useGetSet = true)
    private String name;
    @DatabaseField(columnName = "eid", useGetSet = true)
    private String eid;

    public UserAnswer() {

    }

    public UserAnswer(String eid, String userid, String ztid, String data, String is_sumit, String time, String tiku_id, String name, String fenshu) {
        this.eid = eid;
        this.userid = userid;
        this.ztid = ztid;
        this.data = data;
        this.is_sumit = is_sumit;
        this.time = time;
        this.tiku_id = tiku_id;
        this.name = name;
        this.fenshu = fenshu;
    }

    public String getEid() {
        return eid;
    }

    public void setEid(String eid) {
        this.eid = eid;
    }

    public String getFenshu() {
        return fenshu;
    }

    public void setFenshu(String fenshu) {
        this.fenshu = fenshu;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getResultTotal() {
        return fenshu;
    }

    public void setResultTotal(String fenshu) {
        this.fenshu = fenshu;
    }

    public String getTiku_id() {
        return tiku_id;
    }

    public void setTiku_id(String tiku_id) {
        this.tiku_id = tiku_id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getIs_sumit() {
        return is_sumit;
    }

    public void setIs_sumit(String is_sumit) {
        this.is_sumit = is_sumit;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public static class mFenShu implements Serializable {
        private String tixing;
        private String defen;
        private String defenlv;

        public mFenShu(String tixing, String defen, String defenlv) {
            this.tixing = tixing;
            this.defen = defen;
            this.defenlv = defenlv;
        }

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

    @Override
    public String toString() {
        return "UserAnswer{" +
                "id=" + id +
                ", ztid=" + ztid + '\'' +
                ", data='" + data + '\'' +
                ", is_sumit=" + is_sumit + '\'' +
                ", tiku_id=" + tiku_id + '\'' +
                ", fenshu=" + fenshu + '\'' +
                ", name=" + name + '\'' +
                ", time=" + time + '\'' +
                ", userid=" + userid +
                '}';
    }
}
