package com.jiaoshizige.teacherexam.model;

import com.jiaoshizige.teacherexam.http.JsonResponseParser;
import com.jiaoshizige.teacherexam.http.SupportResponse;

import org.xutils.http.annotation.HttpResponse;

/**
 * Created by Administrator on 2017/a1/21 0021.
 */
@HttpResponse(parser = JsonResponseParser.class)
public class PersonMessageResponse extends SupportResponse {
    public mData data;

    public mData getData() {
        return data;
    }

    public void setData(mData data) {
        this.data = data;
    }

    public static class mData{
        public String name;
        public String nick_name;
        public String mobile;
        public String integral;//教师币
        public String avatar;
        public String sign_num;//连续打卡天数
        public String task_num;//完成任务天数
        private String credit;
        private String is_sign;//0未签到，1已签到
        private String exam_type;//考试类型
        private String exam_area;//考试地区
        private String wifi_switch;//wifi开关 1开0关
        private String push_switch;//推送开关 1开0关
        private String sign_time;//签到时间
        private String exam_type_id;
        private String is_read_num;

        public String getIs_read_num() {
            return is_read_num;
        }

        public void setIs_read_num(String is_read_num) {
            this.is_read_num = is_read_num;
        }

        public String getExam_type_id() {
            return exam_type_id;
        }

        public void setExam_type_id(String exam_type_id) {
            this.exam_type_id = exam_type_id;
        }

        public String getWifi_switch() {
            return wifi_switch;
        }

        public void setWifi_switch(String wifi_switch) {
            this.wifi_switch = wifi_switch;
        }

        public String getSign_time() {
            return sign_time;
        }

        public void setSign_time(String sign_time) {
            this.sign_time = sign_time;
        }

        public String getPush_switch() {
            return push_switch;
        }

        public void setPush_switch(String push_switch) {
            this.push_switch = push_switch;
        }

        public String getIs_sign() {
            return is_sign;
        }

        public void setIs_sign(String is_sign) {
            this.is_sign = is_sign;
        }

        public String getExam_type() {
            return exam_type;
        }

        public void setExam_type(String exam_type) {
            this.exam_type = exam_type;
        }

        public String getExam_area() {
            return exam_area;
        }

        public void setExam_area(String exam_area) {
            this.exam_area = exam_area;
        }

        public String getCredit() {
            return credit;
        }

        public void setCredit(String credit) {
            this.credit = credit;
        }

        public String getTask_num() {
            return task_num;
        }

        public void setTask_num(String task_num) {
            this.task_num = task_num;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getSign_num() {
            return sign_num;
        }

        public void setSign_num(String sign_num) {
            this.sign_num = sign_num;
        }

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

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getIntegral() {
            return integral;
        }

        public void setIntegral(String integral) {
            this.integral = integral;
        }
    }
}
