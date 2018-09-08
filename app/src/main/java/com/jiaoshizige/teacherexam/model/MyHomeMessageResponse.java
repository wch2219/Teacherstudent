package com.jiaoshizige.teacherexam.model;

import com.jiaoshizige.teacherexam.http.JsonResponseParser;
import com.jiaoshizige.teacherexam.http.SupportResponse;

import org.xutils.http.annotation.HttpResponse;

import java.util.List;

/**
 * Created by Administrator on 2018/4/4 0004.
 */
@HttpResponse(parser = JsonResponseParser.class)
public class MyHomeMessageResponse extends SupportResponse {
    private List<mData> data;

    public List<mData> getData() {
        return data;
    }

    public void setData(List<mData> data) {
        this.data = data;
    }

    public static class mData {
        private String user_id;
        private String id;
        private String title;
        private String content;
        private String detail;
        private String type;
        private String type_id;
        private String images;
        private String created_at;
        private String name;
        private String user_img;
        private String u_content;
        private List<u_answer_img> u_answer_img;
        private String u_audio_url;
        private String u_duration;
        private String w_title;
        private String w_id;
        private String w_images;
        private mReply reply;
        private String is_read;

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getDetail() {
            return detail;
        }

        public void setDetail(String detail) {
            this.detail = detail;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getType_id() {
            return type_id;
        }

        public void setType_id(String type_id) {
            this.type_id = type_id;
        }

        public String getImages() {
            return images;
        }

        public void setImages(String images) {
            this.images = images;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUser_img() {
            return user_img;
        }

        public void setUser_img(String user_img) {
            this.user_img = user_img;
        }

        public String getU_content() {
            return u_content;
        }

        public void setU_content(String u_content) {
            this.u_content = u_content;
        }

        public List<MyHomeMessageResponse.u_answer_img> getU_answer_img() {
            return u_answer_img;
        }

        public void setU_answer_img(List<MyHomeMessageResponse.u_answer_img> u_answer_img) {
            this.u_answer_img = u_answer_img;
        }

        public String getU_audio_url() {
            return u_audio_url;
        }

        public void setU_audio_url(String u_audio_url) {
            this.u_audio_url = u_audio_url;
        }

        public String getU_duration() {
            return u_duration;
        }

        public void setU_duration(String u_duration) {
            this.u_duration = u_duration;
        }

        public String getW_title() {
            return w_title;
        }

        public void setW_title(String w_title) {
            this.w_title = w_title;
        }

        public String getW_id() {
            return w_id;
        }

        public void setW_id(String w_id) {
            this.w_id = w_id;
        }

        public String getW_images() {
            return w_images;
        }

        public void setW_images(String w_images) {
            this.w_images = w_images;
        }

        public mReply getReply() {
            return reply;
        }

        public void setReply(mReply reply) {
            this.reply = reply;
        }

        public String getIs_read() {
            return is_read;
        }

        public void setIs_read(String is_read) {
            this.is_read = is_read;
        }
    }

    public static class u_answer_img {
        private String img;

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }
    }

    public static class mReply {
        private String name;
        private String type;
        private String content;
        private String created_at;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }
    }
}
