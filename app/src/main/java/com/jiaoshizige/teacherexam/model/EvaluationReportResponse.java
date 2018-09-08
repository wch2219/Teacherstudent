package com.jiaoshizige.teacherexam.model;

import com.jiaoshizige.teacherexam.http.JsonResponseParser;
import com.jiaoshizige.teacherexam.http.SupportResponse;

import org.xutils.http.annotation.HttpResponse;

import java.util.List;

/**
 * Created by Administrator on 2018/1/2 0002.
 */
@HttpResponse(parser = JsonResponseParser.class)
public class EvaluationReportResponse extends SupportResponse {
    public mData data;

    public mData getData() {
        return data;
    }

    public void setData(mData data) {
        this.data = data;
    }

    public static class mData {
        private User_info user_info;
        private String content;//政策解读
        private List<Tag> tag;//用户标签数组
        private List<Hole> hole;//同壕人数
        private List<City> city;//人数
        private List<Other> other;//

        public User_info getUser_info() {
            return user_info;
        }

        public void setUser_info(User_info user_info) {
            this.user_info = user_info;
        }

        public List<Other> getOther() {
            return other;
        }

        public void setOther(List<Other> other) {
            this.other = other;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public List<Tag> getTag() {
            return tag;
        }

        public void setTag(List<Tag> tag) {
            this.tag = tag;
        }

        public List<Hole> getHole() {
            return hole;
        }

        public void setHole(List<Hole> hole) {
            this.hole = hole;
        }

        public List<City> getCity() {
            return city;
        }

        public void setCity(List<City> city) {
            this.city = city;
        }
    }

    public static class User_info {
        private String nick_name;//用户昵称
        private String avatar;

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getNick_name() {
            return nick_name;
        }

        public void setNick_name(String nick_name) {
            this.nick_name = nick_name;
        }
    }

    public static class Tag {
        private String tag;

        public String getTag() {
            return tag;
        }

        public void setTag(String tag) {
            this.tag = tag;
        }
    }

    public static class Hole {
        public String exam_type;
        public double num;
//        public String name; //名字
//        public int value;   //数值

        public int color;   //颜色
        public float percentage; //百分比
        public float angle; //角度

        public Hole(String exam_type, int num) {
            this.exam_type = exam_type;
            this.num = num;
        }

        public float getAngle() {
            return angle;
        }

        public void setAngle(float angle) {
            this.angle = angle;
        }

        public int getColor() {
            return color;
        }

        public void setColor(int color) {
            this.color = color;
        }

        public float getPercentage() {
            return percentage;
        }

        public void setPercentage(float percentage) {
            this.percentage = percentage;
        }

        public String getExam_type() {
            return exam_type;
        }

        public void setExam_type(String exam_type) {
            this.exam_type = exam_type;
        }

        public double getNum() {
            return num;
        }

        public void setNum(double num) {
            this.num = num;
        }
    }

    public static class City {
        private String province;
        private double num;

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public double getNum() {
            return num;
        }

        public void setNum(double num) {
            this.num = num;
        }
    }

    public static class Other {
        private String avatar;

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }
    }
}
