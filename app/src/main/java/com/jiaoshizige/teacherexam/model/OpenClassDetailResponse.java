package com.jiaoshizige.teacherexam.model;

import com.jiaoshizige.teacherexam.http.JsonResponseParser;
import com.jiaoshizige.teacherexam.http.SupportResponse;

import org.xutils.http.annotation.HttpResponse;

import java.util.List;

/**
 * Created by Administrator on 2017/12/12 0012.
 */
@HttpResponse(parser = JsonResponseParser.class)
public class OpenClassDetailResponse extends SupportResponse {
    public mData data;

    public mData getData() {
        return data;
    }

    public void setData(mData data) {
        this.data = data;
    }

    public static class mData{
        private String id;
        private String name;
        private String image;
        private String price;
        private float market_price;
        private String sale_num;
        private String tag_id;
        private String description;
        private String start_time;
        private String end_time;
        private String d_b;
        private String date_y;
        private String date_y2;
        private String start_times;
        private String end_times;
        private List<mTags> tags;
        private String is_buy;
        private String live_id;
        private mLiveinfo live_info;
        private String qq;

        public String getQq() {
            return qq;
        }

        public void setQq(String qq) {
            this.qq = qq;
        }

        public String getDate_y() {
            return date_y;
        }

        public void setDate_y(String date_y) {
            this.date_y = date_y;
        }

        public String getD_b() {
            return d_b;
        }

        public void setD_b(String d_b) {
            this.d_b = d_b;
        }

        public String getDate_y2() {
            return date_y2;
        }

        public void setDate_y2(String date_y2) {
            this.date_y2 = date_y2;
        }

        public String getStart_times() {
            return start_times;
        }

        public void setStart_times(String start_times) {
            this.start_times = start_times;
        }

        public String getEnd_times() {
            return end_times;
        }

        public void setEnd_times(String end_times) {
            this.end_times = end_times;
        }

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

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }


        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public float getMarket_price() {
            return market_price;
        }

        public void setMarket_price(float market_price) {
            this.market_price = market_price;
        }

        public String getSale_num() {
            return sale_num;
        }

        public void setSale_num(String sale_num) {
            this.sale_num = sale_num;
        }

        public String getTag_id() {
            return tag_id;
        }

        public void setTag_id(String tag_id) {
            this.tag_id = tag_id;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getStart_time() {
            return start_time;
        }

        public void setStart_time(String start_time) {
            this.start_time = start_time;
        }

        public String getEnd_time() {
            return end_time;
        }

        public void setEnd_time(String end_time) {
            this.end_time = end_time;
        }

        public List<mTags> getTags() {
            return tags;
        }

        public void setTags(List<mTags> tags) {
            this.tags = tags;
        }

        public String getIs_buy() {
            return is_buy;
        }

        public void setIs_buy(String is_buy) {
            this.is_buy = is_buy;
        }

        public String getLive_id() {
            return live_id;
        }

        public void setLive_id(String live_id) {
            this.live_id = live_id;
        }

        public mLiveinfo getLive_info() {
            return live_info;
        }

        public void setLive_info(mLiveinfo live_info) {
            this.live_info = live_info;
        }
    }

    public static class  mLiveinfo{
        private String stream_name;
        private String name;
        private String image;
        private String pull_url;
        private String video_url;
        private String chat_room_id;
        private String token;
        private String app_name;

        public String getApp_name() {
            return app_name;
        }

        public void setApp_name(String app_name) {
            this.app_name = app_name;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getStream_name() {
            return stream_name;
        }

        public void setStream_name(String stream_name) {
            this.stream_name = stream_name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getPull_url() {
            return pull_url;
        }

        public void setPull_url(String pull_url) {
            this.pull_url = pull_url;
        }

        public String getVideo_url() {
            return video_url;
        }

        public void setVideo_url(String video_url) {
            this.video_url = video_url;
        }

        public String getChat_room_id() {
            return chat_room_id;
        }

        public void setChat_room_id(String chat_room_id) {
            this.chat_room_id = chat_room_id;
        }
    }
    public static class mTags{
        private String tag_name;
        private String img_path;

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
}
