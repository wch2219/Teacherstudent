package com.jiaoshizige.teacherexam.model;

import com.jiaoshizige.teacherexam.http.JsonResponseParser;
import com.jiaoshizige.teacherexam.http.SupportResponse;

import org.xutils.http.annotation.HttpResponse;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2018/8/9.
 */
@HttpResponse(parser = JsonResponseParser.class)
public class TestResultResponse extends SupportResponse implements Serializable {
    private mData data;

    public mData getData() {
        return data;
    }

    public void setData(mData data) {
        this.data = data;
    }

    public static class mData implements Serializable {
        private String name;
        private String paper_totle;
        private String moni_totle;
        private String keguan;
        private String zhuguan;
        private String time;
        private String id;
        private String tiku_id;
        private List<mFenShu> fenshu;

        public mData() {

        }

        public mData(String name, String paper_totle, String moni_totle, String keguan, String zhuguan, String time, String id, List<mFenShu> fenshu) {
            this.name = name;
            this.paper_totle = paper_totle;
            this.moni_totle = moni_totle;
            this.keguan = keguan;
            this.zhuguan = zhuguan;
            this.time = time;
            this.id = id;
            this.fenshu = fenshu;
        }

        public String getTiku_id() {
            return tiku_id;
        }

        public void setTiku_id(String tiku_id) {
            this.tiku_id = tiku_id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
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

        public String getKeguan() {
            return keguan;
        }

        public void setKeguan(String keguan) {
            this.keguan = keguan;
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

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public List<mFenShu> getFenshu() {
            return fenshu;
        }

        public void setFenshu(List<mFenShu> fenshu) {
            this.fenshu = fenshu;
        }
    }

    public static class mFenShu implements Serializable {
        private String tixing;
        private String defen;
        private String defenlv;

        public mFenShu() {

        }

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
}
