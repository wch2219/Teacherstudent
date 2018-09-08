package com.jiaoshizige.teacherexam.model;

import com.jiaoshizige.teacherexam.http.JsonResponseParser;
import com.jiaoshizige.teacherexam.http.SupportResponse;

import org.xutils.http.annotation.HttpResponse;

import java.util.List;

/**
 * Created by Administrator on 2018/7/17.
 */
@HttpResponse(parser = JsonResponseParser.class)
public class QusstionBankResponse extends SupportResponse {
    private List<mData> data;

    public List<mData> getData() {
        return data;
    }

    public void setData(List<mData> data) {
        this.data = data;
    }

    public static class mData{
        private String id;//题库id
        private String name;//题库名称
        private String exam_name;//考试类型
        private String guanqia_totle_num;//关卡总数
        private String guanqia_totle_pass_num;//关卡通过数
        private String huangguan_totle_num;//总皇冠数
        private String huangguan_have_totle_num;//已获得皇冠数

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

        public String getExam_name() {
            return exam_name;
        }

        public void setExam_name(String exam_name) {
            this.exam_name = exam_name;
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
    }
}
