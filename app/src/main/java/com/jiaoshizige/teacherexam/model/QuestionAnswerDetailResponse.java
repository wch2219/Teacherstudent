package com.jiaoshizige.teacherexam.model;

import com.jiaoshizige.teacherexam.http.JsonResponseParser;
import com.jiaoshizige.teacherexam.http.SupportResponse;

import org.xutils.http.annotation.HttpResponse;

import java.util.List;

/**
 * Created by Administrator on 2017/12/1.
 * 问题详情
 */
@HttpResponse(parser = JsonResponseParser.class)
public class QuestionAnswerDetailResponse extends SupportResponse {
    private mData data;

    public mData getData() {
        return data;
    }

    public void setData(mData data) {
        this.data = data;
    }

    public static class mData{
        private mQuestion quiz;
        private List<mReply> reply;

        public mQuestion getQuiz() {
            return quiz;
        }

        public void setQuiz(mQuestion quiz) {
            this.quiz = quiz;
        }

        public List<mReply> getReply() {
            return reply;
        }

        public void setReply(List<mReply> reply) {
            this.reply = reply;
        }
    }
    public static class mQuestion{
        private String content;//内容
        private String name;//姓名
        private String avatar;//头像
        private String created_at;//提问时间

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }
    }
    public static class mReply{
        private String id;//id
        private String content;//回答内容
        private String name;//姓名
        private String avatar;//头像
        private String created_at;//回答时间
        private String count_zan;//赞数
        private String is_like;//是或否赞

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getIs_like() {
            return is_like;
        }

        public void setIs_like(String is_like) {
            this.is_like = is_like;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public String getCount_zan() {
            return count_zan;
        }

        public void setCount_zan(String count_zan) {
            this.count_zan = count_zan;
        }
    }
}
