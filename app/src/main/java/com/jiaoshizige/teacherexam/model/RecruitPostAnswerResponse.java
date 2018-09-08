package com.jiaoshizige.teacherexam.model;

import com.jiaoshizige.teacherexam.http.JsonResponseParser;
import com.jiaoshizige.teacherexam.http.SupportResponse;

import org.xutils.http.annotation.HttpResponse;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/8/10.
 */
@HttpResponse(parser = JsonResponseParser.class)
public class RecruitPostAnswerResponse extends SupportResponse implements Serializable{
    private mData data;

    public mData getData() {
        return data;
    }

    public void setData(mData data) {
        this.data = data;
    }

    public static class mData implements Serializable{
        private String id;
        private String guanqia_next_id;
        private String name;
        private String is_pass;
        private String total_score;
        private String pass_score;
        private String error_score;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getGuanqia_next_id() {
            return guanqia_next_id;
        }

        public void setGuanqia_next_id(String guanqia_next_id) {
            this.guanqia_next_id = guanqia_next_id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getIs_pass() {
            return is_pass;
        }

        public void setIs_pass(String is_pass) {
            this.is_pass = is_pass;
        }

        public String getTotal_score() {
            return total_score;
        }

        public void setTotal_score(String total_score) {
            this.total_score = total_score;
        }

        public String getPass_score() {
            return pass_score;
        }

        public void setPass_score(String pass_score) {
            this.pass_score = pass_score;
        }

        public String getError_score() {
            return error_score;
        }

        public void setError_score(String error_score) {
            this.error_score = error_score;
        }
    }
}
