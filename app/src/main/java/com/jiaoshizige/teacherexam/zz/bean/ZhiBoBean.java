package com.jiaoshizige.teacherexam.zz.bean;

import com.jiaoshizige.teacherexam.http.JsonResponseParser;
import com.jiaoshizige.teacherexam.http.SupportResponse;

import org.xutils.http.annotation.HttpResponse;

import java.util.List;

@HttpResponse(parser = JsonResponseParser.class)
public class ZhiBoBean extends SupportResponse{


    /**
     * status_code : 200
     * data : [{"id":43,"description":"111","sale_num":0,"zhibo_type":1,"name":"720公开课","image":"https://wxapi.zgclass.com/uploads/open_course/20180712110028320.jpg","live_id":"115","lb_url":"","start_time":"2018-08-20 09:36:59","end_time":"2018-08-20 22:14:52","price":"0.00","is_live":1,"is_buy":0,"start_times":"1534729019000","end_times":"1534774492000","date_y":"08月20日","date_y2":"8月20日","date_d":"09:36-22:14","home_time":"2018/08/20 09:36-22:14","live_info":{"app_name":"720gscz","stream_name":"wdwdw22111","name":"720gscz","image":"uploads/live/20180720165252978.jpg","push_url":"rtmp://video.zhongguanjiaoshi.com/720gscz/wdwdw22111?vhost=video.zhongguanjiaoshi.com&auth_key=1532098588-0-0-be98fd25be07b2912000de0a5f76a169","pull_url":"http://video.zhongguanjiaoshi.com/720gscz/wdwdw22111.flv?auth_key=1532098588-0-0-66676fabf15e100b9d04e3a4cc0fd9f3","video_url":"","chat_room_id":1532076772,"token":"uNBbljziJ2S0mvQ8kxzhpMV/Bqw0qFkQEGIxJnHTeUadOm8wTvO4q7aNwzAlVjLZcXrheOZXRVJAwD6YKHIZNw=="}},{"id":44,"description":"1111","sale_num":0,"zhibo_type":1,"name":"720近期测试啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊","image":"https://wxapi.zgclass.com/uploads/open_course/20180803170535297.jpg","live_id":"6","lb_url":"","start_time":"2018-08-21 14:21:23","end_time":"2018-08-21 22:00:00","price":"0.00","is_live":0,"is_buy":0,"start_times":"1534832483000","end_times":"1534860000000","date_y":"08月21日","date_y2":"8月21日","date_d":"14:21-22:00","home_time":"2018/08/21 14:21-22:00"}]
     */

    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 43
         * description : 111
         * sale_num : 0
         * zhibo_type : 1
         * name : 720公开课
         * image : https://wxapi.zgclass.com/uploads/open_course/20180712110028320.jpg
         * live_id : 115
         * lb_url :
         * start_time : 2018-08-20 09:36:59
         * end_time : 2018-08-20 22:14:52
         * price : 0.00
         * is_live : 1
         * is_buy : 0
         * start_times : 1534729019000
         * end_times : 1534774492000
         * date_y : 08月20日
         * date_y2 : 8月20日
         * date_d : 09:36-22:14
         * home_time : 2018/08/20 09:36-22:14
         * live_info : {"app_name":"720gscz","stream_name":"wdwdw22111","name":"720gscz","image":"uploads/live/20180720165252978.jpg","push_url":"rtmp://video.zhongguanjiaoshi.com/720gscz/wdwdw22111?vhost=video.zhongguanjiaoshi.com&auth_key=1532098588-0-0-be98fd25be07b2912000de0a5f76a169","pull_url":"http://video.zhongguanjiaoshi.com/720gscz/wdwdw22111.flv?auth_key=1532098588-0-0-66676fabf15e100b9d04e3a4cc0fd9f3","video_url":"","chat_room_id":1532076772,"token":"uNBbljziJ2S0mvQ8kxzhpMV/Bqw0qFkQEGIxJnHTeUadOm8wTvO4q7aNwzAlVjLZcXrheOZXRVJAwD6YKHIZNw=="}
         */

        private String id;
        private String description;
        private int sale_num;
        private int zhibo_type;
        private String name;
        private String image;
        private String live_id;
        private String lb_url;
        private String start_time;
        private String end_time;
        private String price;
        private int is_live;
        private int is_buy;
        private String start_times;
        private String end_times;
        private String date_y;
        private String date_y2;
        private String date_d;
        private String home_time;
        private LiveInfoBean live_info;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public int getSale_num() {
            return sale_num;
        }

        public void setSale_num(int sale_num) {
            this.sale_num = sale_num;
        }

        public int getZhibo_type() {
            return zhibo_type;
        }

        public void setZhibo_type(int zhibo_type) {
            this.zhibo_type = zhibo_type;
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

        public int getIs_live() {
            return is_live;
        }

        public void setIs_live(int is_live) {
            this.is_live = is_live;
        }

        public int getIs_buy() {
            return is_buy;
        }

        public void setIs_buy(int is_buy) {
            this.is_buy = is_buy;
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

        public String getHome_time() {
            return home_time;
        }

        public void setHome_time(String home_time) {
            this.home_time = home_time;
        }

        public LiveInfoBean getLive_info() {
            return live_info;
        }

        public void setLive_info(LiveInfoBean live_info) {
            this.live_info = live_info;
        }

        public static class LiveInfoBean {
            /**
             * app_name : 720gscz
             * stream_name : wdwdw22111
             * name : 720gscz
             * image : uploads/live/20180720165252978.jpg
             * push_url : rtmp://video.zhongguanjiaoshi.com/720gscz/wdwdw22111?vhost=video.zhongguanjiaoshi.com&auth_key=1532098588-0-0-be98fd25be07b2912000de0a5f76a169
             * pull_url : http://video.zhongguanjiaoshi.com/720gscz/wdwdw22111.flv?auth_key=1532098588-0-0-66676fabf15e100b9d04e3a4cc0fd9f3
             * video_url :
             * chat_room_id : 1532076772
             * token : uNBbljziJ2S0mvQ8kxzhpMV/Bqw0qFkQEGIxJnHTeUadOm8wTvO4q7aNwzAlVjLZcXrheOZXRVJAwD6YKHIZNw==
             */

            private String app_name;
            private String stream_name;
            private String name;
            private String image;
            private String push_url;
            private String pull_url;
            private String video_url;
            private int chat_room_id;
            private String token;

            public String getApp_name() {
                return app_name;
            }

            public void setApp_name(String app_name) {
                this.app_name = app_name;
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

            public String getPush_url() {
                return push_url;
            }

            public void setPush_url(String push_url) {
                this.push_url = push_url;
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

            public int getChat_room_id() {
                return chat_room_id;
            }

            public void setChat_room_id(int chat_room_id) {
                this.chat_room_id = chat_room_id;
            }

            public String getToken() {
                return token;
            }

            public void setToken(String token) {
                this.token = token;
            }
        }
    }
}
