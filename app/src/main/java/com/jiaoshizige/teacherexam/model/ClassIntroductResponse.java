package com.jiaoshizige.teacherexam.model;

import com.jiaoshizige.teacherexam.http.JsonResponseParser;
import com.jiaoshizige.teacherexam.http.SupportResponse;

import org.xutils.http.annotation.HttpResponse;

import java.util.List;

/**
 * Created by Administrator on 2017/a1/27.
 * 班级介绍
 */
@HttpResponse(parser = JsonResponseParser.class)
public class ClassIntroductResponse extends SupportResponse {
    private mData data;

    public mData getData() {
        return data;
    }

    public void setData(mData data) {
        this.data = data;
    }

    public static class mData{
        private String mobile_detail;//详情
        private String tag_id;//标签id
        private mTeacher teacher ;//
        private List<mTags> tags ;//

        public String getMobile_detail() {
            return mobile_detail;
        }

        public void setMobile_detail(String mobile_detail) {
            this.mobile_detail = mobile_detail;
        }

        public String getTag_id() {
            return tag_id;
        }

        public void setTag_id(String tag_id) {
            this.tag_id = tag_id;
        }

        public mTeacher getTeacher() {
            return teacher;
        }

        public void setTeacher(mTeacher teacher) {
            this.teacher = teacher;
        }

        public List<mTags> getTags() {
            return tags;
        }

        public void setTags(List<mTags> tags) {
            this.tags = tags;
        }
    }
    public static class mTeacher{
        private String id;//班主任id
        private String name;//班主任名称
        private String headImg;//班主任头像
        private String qq;//qq

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

        public String getHeadImg() {
            return headImg;
        }

        public void setHeadImg(String headImg) {
            this.headImg = headImg;
        }

        public String getQq() {
            return qq;
        }

        public void setQq(String qq) {
            this.qq = qq;
        }
    }
    public static class mTags{
        private String tag_name;//标签名称
        private String img_path;//图标

        public String getTag_name() {
            return tag_name;
        }

        public void setTag_name(String tag_name) {
            this.tag_name = tag_name;
        }

        public String getImg_path() {
            return img_path;
        }

        public void setImg_path(String img_path) {
            this.img_path = img_path;
        }
    }
}
