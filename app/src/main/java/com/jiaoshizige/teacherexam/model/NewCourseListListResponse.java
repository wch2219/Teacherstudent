package com.jiaoshizige.teacherexam.model;

import com.jiaoshizige.teacherexam.http.JsonResponseParser;
import com.jiaoshizige.teacherexam.http.SupportResponse;

import org.xutils.http.annotation.HttpResponse;

import java.util.List;

/**
 * Created by Administrator on 2018/3/21.
 * 课程列表里面的列表
 */
@HttpResponse(parser = JsonResponseParser.class)
public class NewCourseListListResponse extends SupportResponse{
    private List<mData> data;

    public List<mData> getData() {
        return data;
    }

    public void setData(List<mData> data) {
        this.data = data;
    }

    public static class mData{
        private String type_id;//课程或班级id
        private String type;//1为班级，2为课程
        private String group_name;//班级或课程名称
        private String price;//价格
        private String assistant_name;//助教
        private String teacher_name;//助教
        private String market_price;//市场价
        private String num;//学习人数
        private String cid;//所属分类id
        private String is_activity;
        private String is_buy;
        private String bq;
        private String bq_img;

        public String getIs_activity() {
            return is_activity;
        }

        public void setIs_activity(String is_activity) {
            this.is_activity = is_activity;
        }

        public String getIs_buy() {
            return is_buy;
        }

        public void setIs_buy(String is_buy) {
            this.is_buy = is_buy;
        }

        public String getBq() {
            return bq;
        }

        public void setBq(String bq) {
            this.bq = bq;
        }

        public String getBq_img() {
            return bq_img;
        }

        public void setBq_img(String bq_img) {
            this.bq_img = bq_img;
        }

        public String getType_id() {
            return type_id;
        }

        public void setType_id(String type_id) {
            this.type_id = type_id;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getGroup_name() {
            return group_name;
        }

        public void setGroup_name(String group_name) {
            this.group_name = group_name;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getAssistant_name() {
            return assistant_name;
        }

        public void setAssistant_name(String assistant_name) {
            this.assistant_name = assistant_name;
        }

        public String getTeacher_name() {
            return teacher_name;
        }

        public void setTeacher_name(String teacher_name) {
            this.teacher_name = teacher_name;
        }

        public String getMarket_price() {
            return market_price;
        }

        public void setMarket_price(String market_price) {
            this.market_price = market_price;
        }

        public String getNum() {
            return num;
        }

        public void setNum(String num) {
            this.num = num;
        }

        public String getCid() {
            return cid;
        }

        public void setCid(String cid) {
            this.cid = cid;
        }
    }
}
