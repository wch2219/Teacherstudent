package com.jiaoshizige.teacherexam.model;


import com.jiaoshizige.teacherexam.http.JsonResponseParser;
import com.jiaoshizige.teacherexam.http.SupportResponse;

import org.xutils.http.annotation.HttpResponse;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2018/7/18.
 */
@HttpResponse(parser = JsonResponseParser.class)
public class QuestionBankDetailResponse extends SupportResponse implements Serializable {
    private mData data;

    public mData getData() {
        return data;
    }

    public void setData(mData data) {
        this.data = data;
    }

    public static class mData {
        private List<mTotle> totle;
        private List<mKaoDianList> kaodian_list;

        public List<mTotle> getTotle() {
            return totle;
        }

        public void setTotle(List<mTotle> totle) {
            this.totle = totle;
        }

        public List<mKaoDianList> getKaodian_list() {
            return kaodian_list;
        }

        public void setKaodian_list(List<mKaoDianList> kaodian_list) {
            this.kaodian_list = kaodian_list;
        }
    }

    public static class mTotle implements Serializable{
        private String percent;//完成度
        private String guanqia_totle_num;//总关卡数
        private String guanqia_totle_pass_num;//已闯关卡数
        private String huangguan_totle_num;//皇冠总数
        private String huangguan_have_totle_num;//已获得皇冠数
        private String datiliang;//答题量
        private String rank_percent;//超过多少学霸
        private String paiming;//当前名次
        private String totle_user;//总排名数
        private String full_huangguan;//满星通过关卡数
        private String accuracy;//正确率

        public String getPercent() {
            return percent;
        }

        public void setPercent(String percent) {
            this.percent = percent;
        }

        public String getGuanqia_totle_num() {
            return guanqia_totle_num;
        }

        public void setGuanqia_totle_num(String guanqia_totle_num) {
            this.guanqia_totle_num = guanqia_totle_num;
        }

        public String getGuanqia_totle_pass_num() {
            return guanqia_totle_pass_num;
        }

        public void setGuanqia_totle_pass_num(String guanqia_totle_pass_num) {
            this.guanqia_totle_pass_num = guanqia_totle_pass_num;
        }

        public String getHuangguan_totle_num() {
            return huangguan_totle_num;
        }

        public void setHuangguan_totle_num(String huangguan_totle_num) {
            this.huangguan_totle_num = huangguan_totle_num;
        }

        public String getHuangguan_have_totle_num() {
            return huangguan_have_totle_num;
        }

        public void setHuangguan_have_totle_num(String huangguan_have_totle_num) {
            this.huangguan_have_totle_num = huangguan_have_totle_num;
        }

        public String getDatiliang() {
            return datiliang;
        }

        public void setDatiliang(String datiliang) {
            this.datiliang = datiliang;
        }

        public String getRank_percent() {
            return rank_percent;
        }

        public void setRank_percent(String rank_percent) {
            this.rank_percent = rank_percent;
        }

        public String getPaiming() {
            return paiming;
        }

        public void setPaiming(String paiming) {
            this.paiming = paiming;
        }

        public String getTotle_user() {
            return totle_user;
        }

        public void setTotle_user(String totle_user) {
            this.totle_user = totle_user;
        }

        public String getFull_huangguan() {
            return full_huangguan;
        }

        public void setFull_huangguan(String full_huangguan) {
            this.full_huangguan = full_huangguan;
        }

        public String getAccuracy() {
            return accuracy;
        }

        public void setAccuracy(String accuracy) {
            this.accuracy = accuracy;
        }
    }

    public static class mKaoDianList implements Serializable{
        private String id;//考点id
        private String name;//考点名称
        private String guanqia_totle_num;//关卡总数
        private String guanqia_totle_pass_num;//总闯关卡数
        private String huangguan_totle_num;//皇冠数
        private String huangguan_have_totle_num;//已获得皇冠数
        private String is_pass;//是否全部通关

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getGuanqia_totle_num() {
            return guanqia_totle_num;
        }

        public void setGuanqia_totle_num(String guanqia_totle_num) {
            this.guanqia_totle_num = guanqia_totle_num;
        }

        public String getGuanqia_totle_pass_num() {
            return guanqia_totle_pass_num;
        }

        public void setGuanqia_totle_pass_num(String guanqia_totle_pass_num) {
            this.guanqia_totle_pass_num = guanqia_totle_pass_num;
        }

        public String getHuangguan_totle_num() {
            return huangguan_totle_num;
        }

        public void setHuangguan_totle_num(String huangguan_totle_num) {
            this.huangguan_totle_num = huangguan_totle_num;
        }

        public String getHuangguan_have_totle_num() {
            return huangguan_have_totle_num;
        }

        public void setHuangguan_have_totle_num(String huangguan_have_totle_num) {
            this.huangguan_have_totle_num = huangguan_have_totle_num;
        }

        public String getIs_pass() {
            return is_pass;
        }

        public void setIs_pass(String is_pass) {
            this.is_pass = is_pass;
        }
    }
}
