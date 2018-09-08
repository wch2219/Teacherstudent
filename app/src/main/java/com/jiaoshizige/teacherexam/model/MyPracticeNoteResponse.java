package com.jiaoshizige.teacherexam.model;

import com.jiaoshizige.teacherexam.http.JsonResponseParser;
import com.jiaoshizige.teacherexam.http.SupportResponse;

import org.xutils.http.annotation.HttpResponse;

import java.util.List;

/**
 * Created by Administrator on 2018/7/19.
 */
@HttpResponse(parser = JsonResponseParser.class)
public class MyPracticeNoteResponse extends SupportResponse {
    private List<mData> data;

    public List<mData> getData() {
        return data;
    }

    public void setData(List<mData> data) {
        this.data = data;
    }

    public static class  mData{
        private String id;//练习记录id;
        private String name;//关卡名称
        private String created_at;//练习时间
        private String total_score;//总题数
        private String pass_score;//做对题数

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

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
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
    }
}
