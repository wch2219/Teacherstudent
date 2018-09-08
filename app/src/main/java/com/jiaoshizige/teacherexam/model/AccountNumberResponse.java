package com.jiaoshizige.teacherexam.model;

import com.jiaoshizige.teacherexam.http.JsonResponseParser;
import com.jiaoshizige.teacherexam.http.SupportResponse;

import org.xutils.http.annotation.HttpResponse;

/**
 * Created by Administrator on 2017/a1/17 0017.
 */
@HttpResponse(parser = JsonResponseParser.class)
public class AccountNumberResponse extends SupportResponse {
    public mData data;

    public mData getData() {
        return data;
    }

    public void setData(mData data) {
        this.data = data;
    }

    public  static class mData{
        private String user_id;
        private String token;
        private String evaluate_status;
        private String nick_name;
        private String avatar;
        private String is_reg;

        public String getIs_reg() {
            return is_reg;
        }

        public void setIs_reg(String is_reg) {
            this.is_reg = is_reg;
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

        public String getEvaluate_status() {
            return evaluate_status;
        }

        public void setEvaluate_status(String evaluate_status) {
            this.evaluate_status = evaluate_status;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }
    }

}
