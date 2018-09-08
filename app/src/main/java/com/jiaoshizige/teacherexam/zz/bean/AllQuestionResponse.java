package com.jiaoshizige.teacherexam.zz.bean;

import com.jiaoshizige.teacherexam.http.JsonResponseParser;
import com.jiaoshizige.teacherexam.http.SupportResponse;

import org.xutils.http.annotation.HttpResponse;

import java.util.List;

@HttpResponse(parser = JsonResponseParser.class)
public class AllQuestionResponse extends SupportResponse {


    /**
     * status_code : 200
     * message : 请求成功
     * data : [{"id":13,"name":"2011年下半年小学教师资格考试综合素质","is_exam":0,"zhenti_id":""},{"id":15,"name":"2012年上半年小学教师资格考试综合素质","is_exam":0,"zhenti_id":""},{"id":16,"name":"2012年下半年小学教师资格考试综合素质","is_exam":0,"zhenti_id":""},{"id":17,"name":"2013年上半年小学教师资格考试综合素质","is_exam":0,"zhenti_id":""},{"id":18,"name":"2013年下半年小学教师资格考试综合素质","is_exam":0,"zhenti_id":""},{"id":19,"name":"2014年上半年小学教师资格考试综合素质","is_exam":0,"zhenti_id":""},{"id":20,"name":"2014年下半年小学教师资格考试综合素质","is_exam":0,"zhenti_id":""},{"id":21,"name":"2015年上半年小学教师资格考试综合素质","is_exam":0,"zhenti_id":""},{"id":22,"name":"2015年下半年小学教师资格考试综合素质","is_exam":0,"zhenti_id":""},{"id":23,"name":"2016年上半年小学教师资格考试综合素质","is_exam":0,"zhenti_id":""},{"id":24,"name":"2016年下半年小学教师资格考试综合素质","is_exam":0,"zhenti_id":""},{"id":25,"name":"2017年上半年小学教师资格考试综合素质","is_exam":0,"zhenti_id":""},{"id":26,"name":"2017年下半年小学教师资格考试综合素质","is_exam":0,"zhenti_id":""},{"id":27,"name":"2018年上半年小学教师资格考试综合素质","is_exam":0,"zhenti_id":""}]
     */

    public List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 13
         * name : 2011年下半年小学教师资格考试综合素质
         * is_exam : 0
         * zhenti_id :
         */

        private String id;
        private String name;
        private int is_exam;
        private String zhenti_id;

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

        public int getIs_exam() {
            return is_exam;
        }

        public void setIs_exam(int is_exam) {
            this.is_exam = is_exam;
        }

        public String getZhenti_id() {
            return zhenti_id;
        }

        public void setZhenti_id(String zhenti_id) {
            this.zhenti_id = zhenti_id;
        }
    }
}
