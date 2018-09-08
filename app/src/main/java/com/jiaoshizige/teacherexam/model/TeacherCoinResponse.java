package com.jiaoshizige.teacherexam.model;

import com.jiaoshizige.teacherexam.http.JsonResponseParser;
import com.jiaoshizige.teacherexam.http.SupportResponse;

import org.xutils.http.annotation.HttpResponse;

import java.util.List;

/**
 * Created by Administrator on 2017/a1/22 0022.
 */
@HttpResponse(parser = JsonResponseParser.class)
public class TeacherCoinResponse extends SupportResponse {
    private List<mData> data;

    public List<mData> getData() {
        return data;
    }

    public void setData(List<mData> data) {
        this.data = data;
    }

    public static class  mData{
        private String teacher_coin;
        private String pay_price;

        public String getTeacher_coin() {
            return teacher_coin;
        }

        public void setTeacher_coin(String teacher_coin) {
            this.teacher_coin = teacher_coin;
        }

        public String getPay_price() {
            return pay_price;
        }

        public void setPay_price(String pay_price) {
            this.pay_price = pay_price;
        }
    }
}
