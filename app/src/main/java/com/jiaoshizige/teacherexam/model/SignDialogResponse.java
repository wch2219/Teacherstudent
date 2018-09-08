package com.jiaoshizige.teacherexam.model;

import com.jiaoshizige.teacherexam.http.JsonResponseParser;
import com.jiaoshizige.teacherexam.http.SupportResponse;

import org.xutils.http.annotation.HttpResponse;

import java.util.List;

/**
 * Created by Administrator on 2017/12/21.
 */
@HttpResponse(parser = JsonResponseParser.class)
public class SignDialogResponse extends SupportResponse {
    private mData data;

    public mData getData() {
        return data;
    }

    public void setData(mData data) {
        this.data = data;
    }

    public static class mData{
        private String avatar;//用户头像
        private String integral;//教师币数量
        private String days;//连续签到天数
        private String is_sign;//今天是否前天 1签 0 否
        private String jiang_num;
        private List<mWeeks> weeks;

        public String getJiang_num() {
            return jiang_num;
        }

        public void setJiang_num(String jiang_num) {
            this.jiang_num = jiang_num;
        }

        public String getIs_sign() {
            return is_sign;
        }

        public void setIs_sign(String is_sign) {
            this.is_sign = is_sign;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getIntegral() {
            return integral;
        }

        public void setIntegral(String integral) {
            this.integral = integral;
        }

        public String getDays() {
            return days;
        }

        public void setDays(String days) {
            this.days = days;
        }

        public List<mWeeks> getWeeks() {
            return weeks;
        }

        public void setWeeks(List<mWeeks> weeks) {
            this.weeks = weeks;
        }
    }
    public static class mWeeks{
        private String integral;//签到送分
        private String is_sign;//0未签到，1已签到

        public String getIntegral() {
            return integral;
        }

        public void setIntegral(String integral) {
            this.integral = integral;
        }

        public String getIs_sign() {
            return is_sign;
        }

        public void setIs_sign(String is_sign) {
            this.is_sign = is_sign;
        }
    }
}
