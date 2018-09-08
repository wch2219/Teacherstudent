package com.jiaoshizige.teacherexam.model;

import com.jiaoshizige.teacherexam.http.JsonResponseParser;
import com.jiaoshizige.teacherexam.http.SupportResponse;

import org.xutils.http.annotation.HttpResponse;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2018/7/10.
 */
@HttpResponse(parser = JsonResponseParser.class)
public class NewCourseResponse extends SupportResponse {
    private  mData data;

    public mData getData() {
        return data;
    }

    public void setData(mData data) {
        this.data = data;
    }

    public static class mData{
        private List<mZero> zero;
        private List<mNozero> nozero;

        public List<mZero> getZero() {
            return zero;
        }

        public void setZero(List<mZero> zero) {
            this.zero = zero;
        }

        public List<mNozero> getNozero() {
            return nozero;
        }

        public void setNozero(List<mNozero> nozero) {
            this.nozero = nozero;
        }
    }
    public static class  mZero implements Serializable {
        private String name;
        private String desc;
        private String bq;
        private String bq_img;
        private String price;
        private String sale_num;
        private String keshi;

        public String getKeshi() {
            return keshi;
        }

        public void setKeshi(String keshi) {
            this.keshi = keshi;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getBq() {
            return bq;
        }

        public void setBq(String bq) {
            this.bq = bq;
        }

        public String getBq_img() {
            return bq_img;
        }

        public void setBq_img(String bq_img) {
            this.bq_img = bq_img;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getSale_num() {
            return sale_num;
        }

        public void setSale_num(String sale_num) {
            this.sale_num = sale_num;
        }
    }

    public static class mNozero{
        private String cid;
        private String type_id;
        private String type;
        private String sale_num;
        private String bq_img;
        private String bq;
        private String group_name;
        private String price;
        private String market_price;
        private String assistant_name;
        private String teacher_name;
        private String is_activity;
        private String is_buy;

        public String getCid() {
            return cid;
        }

        public void setCid(String cid) {
            this.cid = cid;
        }

        public String getType_id() {
            return type_id;
        }

        public void setType_id(String type_id) {
            this.type_id = type_id;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getSale_num() {
            return sale_num;
        }

        public void setSale_num(String sale_num) {
            this.sale_num = sale_num;
        }

        public String getBq_img() {
            return bq_img;
        }

        public void setBq_img(String bq_img) {
            this.bq_img = bq_img;
        }

        public String getBq() {
            return bq;
        }

        public void setBq(String bq) {
            this.bq = bq;
        }

        public String getGroup_name() {
            return group_name;
        }

        public void setGroup_name(String group_name) {
            this.group_name = group_name;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getMarket_price() {
            return market_price;
        }

        public void setMarket_price(String market_price) {
            this.market_price = market_price;
        }

        public String getAssistant_name() {
            return assistant_name;
        }

        public void setAssistant_name(String assistant_name) {
            this.assistant_name = assistant_name;
        }

        public String getTeacher_name() {
            return teacher_name;
        }

        public void setTeacher_name(String teacher_name) {
            this.teacher_name = teacher_name;
        }

        public String getIs_activity() {
            return is_activity;
        }

        public void setIs_activity(String is_activity) {
            this.is_activity = is_activity;
        }

        public String getIs_buy() {
            return is_buy;
        }

        public void setIs_buy(String is_buy) {
            this.is_buy = is_buy;
        }
    }
}
