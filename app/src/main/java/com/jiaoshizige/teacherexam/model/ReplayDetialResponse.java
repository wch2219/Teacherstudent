package com.jiaoshizige.teacherexam.model;

import com.jiaoshizige.teacherexam.http.JsonResponseParser;
import com.jiaoshizige.teacherexam.http.SupportResponse;

import org.xutils.http.annotation.HttpResponse;

import java.util.List;

/**
 * Created by Administrator on 2017/12/19.
 * 回复详情
 */
@HttpResponse(parser = JsonResponseParser.class)
public class ReplayDetialResponse extends SupportResponse {
    private mData data;

    public mData getData() {
        return data;
    }

    public void setData(mData data) {
        this.data = data;
    }

    public static class mData{
        private mAnswer answer;
        private mTeacherReplay teacher_reply;
        private List<mOtherReplay> other_reply;

        public mAnswer getAnswer() {
            return answer;
        }

        public void setAnswer(mAnswer answer) {
            this.answer = answer;
        }

        public mTeacherReplay getTeacher_reply() {
            return teacher_reply;
        }

        public void setTeacher_reply(mTeacherReplay teacher_reply) {
            this.teacher_reply = teacher_reply;
        }

        public List<mOtherReplay> getOther_reply() {
            return other_reply;
        }

        public void setOther_reply(List<mOtherReplay> other_reply) {
            this.other_reply = other_reply;
        }
    }
    public static class mAnswer{
        private String name;//回复人姓名
        private String avatar;//头像
        private String id;//回复作业id
        private String content;//回复作业内容
        private List<mImage> images;//
        private String audio_url;//回复作业音频
        private String type;//：1图文格式，2音频
        private String duration;//音频时长
        private String created_at;

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public String getDuration() {
            return duration;
        }

        public void setDuration(String duration) {
            this.duration = duration;
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

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public List<mImage> getImages() {
            return images;
        }

        public void setImages(List<mImage> images) {
            this.images = images;
        }

        public String getAudio_url() {
            return audio_url;
        }

        public void setAudio_url(String audio_url) {
            this.audio_url = audio_url;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }
    public static class mTeacherReplay{
        private String name;//教师姓名
        private String type;//1班主任，2助教
        private String content;//教师回复内容
        private String created_at;//回复时间

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
    public static class mOtherReplay{
        private String nick_name;//
        private String avatar;//
        private String content;//
        private String created_at;//
        private String reply_sbd;//回复谁的
        private String id;//
        private String is_like;//是否赞
        private String count_zan;//点赞数

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

        public String getCount_zan() {
            return count_zan;
        }

        public void setCount_zan(String count_zan) {
            this.count_zan = count_zan;
        }

        public String getReply_sbd() {
            return reply_sbd;
        }

        public void setReply_sbd(String reply_sbd) {
            this.reply_sbd = reply_sbd;
        }

        public String getNick_name() {
            return nick_name;
        }

        public void setNick_name(String nick_name) {
            this.nick_name = nick_name;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
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
    public static class mImage{
        private String img;

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }
    }
}
