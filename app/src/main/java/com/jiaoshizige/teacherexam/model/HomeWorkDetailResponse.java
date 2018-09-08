package com.jiaoshizige.teacherexam.model;

import com.jiaoshizige.teacherexam.http.JsonResponseParser;
import com.jiaoshizige.teacherexam.http.SupportResponse;

import org.xutils.http.annotation.HttpResponse;

import java.util.List;

/**
 * Created by Administrator on 2017/12/12.
 * 作业详情
 */
@HttpResponse(parser = JsonResponseParser.class)
public class HomeWorkDetailResponse extends SupportResponse {

    private mData data;

    public mData getData() {
        return data;
    }

    public void setData(mData data) {
        this.data = data;
    }

    public static class mData{
        private mHomeWork homework;
        private mMyHomeWork my_homework;
        private List<mOtherHomeWork> other_homework;

        public mHomeWork getHomework() {
            return homework;
        }

        public void setHomework(mHomeWork homework) {
            this.homework = homework;
        }

        public mMyHomeWork getMy_homework() {
            return my_homework;
        }

        public void setMy_homework(mMyHomeWork my_homework) {
            this.my_homework = my_homework;
        }

        public List<mOtherHomeWork> getOther_homework() {
            return other_homework;
        }

        public void setOther_homework(List<mOtherHomeWork> other_homework) {
            this.other_homework = other_homework;
        }
    }
    public static class mHomeWork{
        private String id;//作业id
        private String title;//作业标题
        private String content;//作业内容
        private String created_at;//作业发布时间
        private String name;//老师姓名
        private String type;//1班主任，2助教
        private String headimg;//头像

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

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getHeadimg() {
            return headimg;
        }

        public void setHeadimg(String headimg) {
            this.headimg = headimg;
        }
    }
    public static class mMyHomeWork{
        private String id;//作业id
        private String content;//回答内容
        private String audio_url;//
        private String type;//1图文格式，2音频
        private String is_seen;//1任何人可见，2仅对楼主可见
        private List<mImages> images;//回答的图片
        private String nick_name;//
        private String avatar;//
        private String created_at;//
        private String is_done;
        private String duration;
        private String count_reply;
        private mTeacherReply teacher_reply;

        public String getCount_reply() {
            return count_reply;
        }

        public void setCount_reply(String count_reply) {
            this.count_reply = count_reply;
        }

        public String getDuration() {
            return duration;
        }

        public void setDuration(String duration) {
            this.duration = duration;
        }

        public mTeacherReply getTeacher_reply() {
            return teacher_reply;
        }

        public String getIs_done() {
            return is_done;
        }

        public void setIs_done(String is_done) {
            this.is_done = is_done;
        }

        public void setTeacher_reply(mTeacherReply teacher_reply) {
            this.teacher_reply = teacher_reply;
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

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
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

        public String getIs_seen() {
            return is_seen;
        }

        public void setIs_seen(String is_seen) {
            this.is_seen = is_seen;
        }

        public List<mImages> getImages() {
            return images;
        }

        public void setImages(List<mImages> images) {
            this.images = images;
        }
    }
    public static class mImages{
        private String img;

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }
    }
    public static class mTeacherReply{
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
    public static class mOtherHomeWork{
        private String id;//回复id
        private String nick_name;//昵称
        private String avatar;//头像
        private String content;//
        private List<mImages> images;//回答的图片
        private String audio_url;//音频地址
        private String duration;
        private String type;//1图文格式，2音频
        private String is_seen;//1任何人可见，2仅对楼主可见
        private String created_at;//写作业时间
        private String count_zan;//点赞数
        private String is_like;//0未点赞，1已点赞
        private mTeacherReply teacher_reply;
        private String count_reply;

        public String getDuration() {
            return duration;
        }

        public void setDuration(String duration) {
            this.duration = duration;
        }

        public String getCount_reply() {
            return count_reply;
        }

        public void setCount_reply(String count_reply) {
            this.count_reply = count_reply;
        }

        public mTeacherReply getTeacher_reply() {
            return teacher_reply;
        }

        public void setTeacher_reply(mTeacherReply teacher_reply) {
            this.teacher_reply = teacher_reply;
        }
        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
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

        public List<mImages> getImages() {
            return images;
        }

        public void setImages(List<mImages> images) {
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

        public String getIs_seen() {
            return is_seen;
        }

        public void setIs_seen(String is_seen) {
            this.is_seen = is_seen;
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

        public String getIs_like() {
            return is_like;
        }

        public void setIs_like(String is_like) {
            this.is_like = is_like;
        }
    }
}
