package com.jiaoshizige.teacherexam.model;

import com.jiaoshizige.teacherexam.http.JsonResponseParser;
import com.jiaoshizige.teacherexam.http.SupportResponse;

import org.xutils.http.annotation.HttpResponse;

/**
 * Created by Administrator on 2017/a1/27.
 * 课程介绍 评论详情
 */
@HttpResponse(parser = JsonResponseParser.class)
public class IntroduceResponse extends SupportResponse {
    public mData data;

    public mData getData() {
        return data;
    }

    public void setData(mData data) {
        this.data = data;
    }

    public static class mData{
        private String description;
        private String mobile_detail;

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getMobile_detail() {
            return mobile_detail;
        }

        public void setMobile_detail(String mobile_detail) {
            this.mobile_detail = mobile_detail;
        }
    }
}
