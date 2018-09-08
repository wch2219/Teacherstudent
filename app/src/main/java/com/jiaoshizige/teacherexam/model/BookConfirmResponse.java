package com.jiaoshizige.teacherexam.model;

import com.jiaoshizige.teacherexam.http.JsonResponseParser;
import com.jiaoshizige.teacherexam.http.SupportResponse;

import org.xutils.http.annotation.HttpResponse;

import java.util.List;

/**
 * Created by Administrator on 2017/a1/24 0024.
 */
@HttpResponse(parser = JsonResponseParser.class)
public class BookConfirmResponse extends SupportResponse {
    public mData data;

    public mData getData() {
        return data;
    }

    public void setData(mData data) {
        this.data = data;
    }

    public static class mData {
        private String id;//id
        private String book_name;//图书名称
        private double price;//价格
        private double market_price;
        private String images;//图书封面
        private double shipping_price;//邮费
        private double jsb_limit;//教师币使用限制
        private String book_id;
        private String activity;//活动优惠
        private double activity_price;
        private String group_name;//班级名称
        private String name;//课程名称
        private List<mCoupons> coupons;
        private List<mGive_Books> give_books;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getBook_name() {
            return book_name;
        }

        public void setBook_name(String book_name) {
            this.book_name = book_name;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public double getMarket_price() {
            return market_price;
        }

        public void setMarket_price(double market_price) {
            this.market_price = market_price;
        }

        public String getImages() {
            return images;
        }

        public void setImages(String images) {
            this.images = images;
        }

        public double getShipping_price() {
            return shipping_price;
        }

        public void setShipping_price(double shipping_price) {
            this.shipping_price = shipping_price;
        }

        public double getJsb_limit() {
            return jsb_limit;
        }

        public void setJsb_limit(double jsb_limit) {
            this.jsb_limit = jsb_limit;
        }

        public String getBook_id() {
            return book_id;
        }

        public void setBook_id(String book_id) {
            this.book_id = book_id;
        }

        public String getActivity() {
            return activity;
        }

        public void setActivity(String activity) {
            this.activity = activity;
        }

        public double getActivity_price() {
            return activity_price;
        }

        public void setActivity_price(double activity_price) {
            this.activity_price = activity_price;
        }

        public String getGroup_name() {
            return group_name;
        }

        public void setGroup_name(String group_name) {
            this.group_name = group_name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<mCoupons> getCoupons() {
            return coupons;
        }

        public void setCoupons(List<mCoupons> coupons) {
            this.coupons = coupons;
        }

        public List<mGive_Books> getGive_books() {
            return give_books;
        }

        public void setGive_books(List<mGive_Books> give_books) {
            this.give_books = give_books;
        }
    }

    public static class mGive_Books {
        private String id;
        private String book_name;
        private String images;
        private double price;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public String getBook_name() {
            return book_name;
        }

        public void setBook_name(String book_name) {
            this.book_name = book_name;
        }

        public String getImages() {
            return images;
        }

        public void setImages(String images) {
            this.images = images;
        }
    }

    public static class mCoupons {
        private String id;//优惠券id
        private String name;//卡券名称
        private double min_price;//优惠券使用门槛
        private double price;//优惠券金额
        private String end_time;//结束时间

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public double getMin_price() {
            return min_price;
        }

        public void setMin_price(double min_price) {
            this.min_price = min_price;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public String getEnd_time() {
            return end_time;
        }

        public void setEnd_time(String end_time) {
            this.end_time = end_time;
        }
    }
}
