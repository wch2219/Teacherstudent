package com.jiaoshizige.teacherexam.model;

import com.jiaoshizige.teacherexam.http.JsonResponseParser;
import com.jiaoshizige.teacherexam.http.SupportResponse;

import org.xutils.http.annotation.HttpResponse;

import java.util.List;

/**
 * Created by Administrator on 2017/a1/27.
 */
@HttpResponse(parser = JsonResponseParser.class)
public class BookDetailResponse extends SupportResponse {
    private mData data;

    public mData getData() {
        return data;
    }

    public void setData(mData data) {
        this.data = data;
    }

    public static class mData{
        private String id;//图书id
        private String book_name;//图书名称
        private String price;//价格
        private String market_price;//市场价
        private String stock;//库存
        private String sale_num;//已卖数量
        private String mobile_detail;//详情
        private String tag_id;//
        private String qq;
        private String collect_status;//收藏状态0未收藏1已收藏
        private List<mImage> images;
        private String ximages;//图书封面
        private List<mTag> tags;//
        private List<mActivity> activity;

        public String getQq() {
            return qq;
        }

        public void setQq(String qq) {
            this.qq = qq;
        }

        public String getXimages() {
            return ximages;
        }

        public void setXimages(String ximages) {
            this.ximages = ximages;
        }

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

        public String getStock() {
            return stock;
        }

        public void setStock(String stock) {
            this.stock = stock;
        }

        public String getSale_num() {
            return sale_num;
        }

        public void setSale_num(String sale_num) {
            this.sale_num = sale_num;
        }

        public String getMobile_detail() {
            return mobile_detail;
        }

        public void setMobile_detail(String mobile_detail) {
            this.mobile_detail = mobile_detail;
        }

        public String getTag_id() {
            return tag_id;
        }

        public void setTag_id(String tag_id) {
            this.tag_id = tag_id;
        }

        public String getCollect_status() {
            return collect_status;
        }

        public void setCollect_status(String collect_status) {
            this.collect_status = collect_status;
        }

        public List<mImage> getImages() {
            return images;
        }

        public void setImages(List<mImage> images) {
            this.images = images;
        }

        public List<mTag> getTags() {
            return tags;
        }

        public void setTags(List<mTag> tags) {
            this.tags = tags;
        }

        public List<mActivity> getActivity() {
            return activity;
        }

        public void setActivity(List<mActivity> activity) {
            this.activity = activity;
        }
    }
    public static class mImage{
        private String path;

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }
    }
    public static class mTag{
        private String tag_name;//
        private String img_path;//

        public String getTag_name() {
            return tag_name;
        }

        public void setTag_name(String tag_name) {
            this.tag_name = tag_name;
        }

        public String getImg_path() {
            return img_path;
        }

        public void setImg_path(String img_path) {
            this.img_path = img_path;
        }
    }
    public static class mActivity{
        private String activity_name;//
        private String icon;//
        private String type;
        private String id;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getActivity_name() {
            return activity_name;
        }

        public void setActivity_name(String activity_name) {
            this.activity_name = activity_name;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }
    }
}
