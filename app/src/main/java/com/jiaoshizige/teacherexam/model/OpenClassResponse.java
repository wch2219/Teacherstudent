package com.jiaoshizige.teacherexam.model;

import com.jiaoshizige.teacherexam.http.JsonResponseParser;
import com.jiaoshizige.teacherexam.http.SupportResponse;

import org.xutils.http.annotation.HttpResponse;

import java.util.List;

/**
 * Created by Administrator on 2017/12/a1 0011.
 */
@HttpResponse(parser = JsonResponseParser.class)
public class OpenClassResponse extends SupportResponse {
    public mData data;

    public mData getData() {
        return data;
    }

    public void setData(mData data) {
        this.data = data;
    }

    public static class mData {
        private List<mNow> now;
        private List<mRecent> recent;
        private List<mHistory> history;

        public List<mNow> getNow() {
            return now;
        }

        public void setNow(List<mNow> now) {
            this.now = now;
        }

        public List<mRecent> getRecent() {
            return recent;
        }

        public void setRecent(List<mRecent> recent) {
            this.recent = recent;
        }

        public List<mHistory> getHistory() {
            return history;
        }

        public void setHistory(List<mHistory> history) {
            this.history = history;
        }
    }

    public static class mNow {
        private String id;
        private String name;
        private String image;
        private String live_id;
        private String lb_url;
        private String start_time;
        private String end_time;
        private String price;
        private String is_buy;
        private long start_times;
        private long end_times;
        private mLiveinfo live_info;
        private String sale_num;//预约人数
        private String date_y;
        private String date_y2;
        private String date_d;

        public String getDate_y() {
            return date_y;
        }

        public void setDate_y(String date_y) {
            this.date_y = date_y;
        }

        public String getDate_y2() {
            return date_y2;
        }

        public void setDate_y2(String date_y2) {
            this.date_y2 = date_y2;
        }

        public String getDate_d() {
            return date_d;
        }

        public void setDate_d(String date_d) {
            this.date_d = date_d;
        }

        public mLiveinfo getLive_info() {
            return live_info;
        }

        public void setLive_info(mLiveinfo live_info) {
            this.live_info = live_info;
        }

        public long getStart_times() {
            return start_times;
        }

        public void setStart_times(long start_times) {
            this.start_times = start_times;
        }

        public long getEnd_times() {
            return end_times;
        }

        public void setEnd_times(long end_times) {
            this.end_times = end_times;
        }


        public String getSale_num() {
            return sale_num;
        }

        public void setSale_num(String sale_num) {
            this.sale_num = sale_num;
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

        public String getLive_id() {
            return live_id;
        }

        public void setLive_id(String live_id) {
            this.live_id = live_id;
        }

        public String getLb_url() {
            return lb_url;
        }

        public void setLb_url(String lb_url) {
            this.lb_url = lb_url;
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

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getIs_buy() {
            return is_buy;
        }

        public void setIs_buy(String is_buy) {
            this.is_buy = is_buy;
        }
    }

    public static class mRecent {
        private String id;
        private String name;
        private String image;
        private String live_id;
        private String lb_url;
        private String start_time;
        private String end_time;
        private String price;
        private String is_buy;
        private long start_times;
        private long end_times;
        private String sale_num;//预约人数
        private String date_y;
        private String date_y2;
        private String date_d;

        public String getDate_y() {
            return date_y;
        }

        public void setDate_y(String date_y) {
            this.date_y = date_y;
        }

        public String getDate_y2() {
            return date_y2;
        }

        public void setDate_y2(String date_y2) {
            this.date_y2 = date_y2;
        }

        public String getDate_d() {
            return date_d;
        }

        public void setDate_d(String date_d) {
            this.date_d = date_d;
        }

        public long getStart_times() {
            return start_times;
        }

        public void setStart_times(long start_times) {
            this.start_times = start_times;
        }

        public long getEnd_times() {
            return end_times;
        }

        public void setEnd_times(long end_times) {
            this.end_times = end_times;
        }

        public String getSale_num() {
            return sale_num;
        }

        public void setSale_num(String sale_num) {
            this.sale_num = sale_num;
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

        public String getLive_id() {
            return live_id;
        }

        public void setLive_id(String live_id) {
            this.live_id = live_id;
        }

        public String getLb_url() {
            return lb_url;
        }

        public void setLb_url(String lb_url) {
            this.lb_url = lb_url;
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

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getIs_buy() {
            return is_buy;
        }

        public void setIs_buy(String is_buy) {
            this.is_buy = is_buy;
        }
    }

    public static class mHistory {
        private String id;
        private String name;
        private String image;
        private String live_id;
        private String lb_url;
        private String start_time;
        private String end_time;
        private String price;
        private String is_buy;
        private long start_times;
        private long end_times;
        private String sale_num;//预约人数
        private String date_y;
        private String date_y2;
        private String date_d;

        public String getDate_y() {
            return date_y;
        }

        public void setDate_y(String date_y) {
            this.date_y = date_y;
        }

        public String getDate_y2() {
            return date_y2;
        }

        public void setDate_y2(String date_y2) {
            this.date_y2 = date_y2;
        }

        public String getDate_d() {
            return date_d;
        }

        public void setDate_d(String date_d) {
            this.date_d = date_d;
        }

        public long getStart_times() {
            return start_times;
        }

        public void setStart_times(long start_times) {
            this.start_times = start_times;
        }

        public long getEnd_times() {
            return end_times;
        }

        public void setEnd_times(long end_times) {
            this.end_times = end_times;
        }

        public String getSale_num() {
            return sale_num;
        }

        public void setSale_num(String sale_num) {
            this.sale_num = sale_num;
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

        public String getLive_id() {
            return live_id;
        }

        public void setLive_id(String live_id) {
            this.live_id = live_id;
        }

        public String getLb_url() {
            return lb_url;
        }

        public void setLb_url(String lb_url) {
            this.lb_url = lb_url;
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

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getIs_buy() {
            return is_buy;
        }

        public void setIs_buy(String is_buy) {
            this.is_buy = is_buy;
        }
    }
    public static class  mLiveinfo{
        private String app_name;
        private String stream_name;
        private String name;
        private String image;
        private String pull_url;
        private String video_url;
        private String chat_room_id;//融云房间号
        private String push_url;//直播地址
        private String token;//融云token

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

        public String getPush_url() {
            return push_url;
        }

        public void setPush_url(String push_url) {
            this.push_url = push_url;
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
}
