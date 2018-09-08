package com.jiaoshizige.teacherexam.model;

import com.jiaoshizige.teacherexam.http.JsonResponseParser;
import com.jiaoshizige.teacherexam.http.SupportResponse;

import org.xutils.http.annotation.HttpResponse;

import java.util.List;

/**
 * Created by Administrator on 2018/3/21.
 * 目录
 */
@HttpResponse(parser = JsonResponseParser.class)
public class NewCourseCatalogResponse extends SupportResponse{
    private mData data;

    public mData getData() {
        return data;
    }

    public void setData(mData data) {
        this.data = data;
    }

    public static class mData{
        private List<mCourseList> course_list;

        public List<mCourseList> getCourse_list() {
            return course_list;
        }

        public void setCourse_list(List<mCourseList> course_list) {
            this.course_list = course_list;
        }
    }
    public static class mCourseList{
        private String name;
        private String course_type;
        private String is_buy;
        private String id;
        private String price;
        private List<mCatalog> catalog;

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

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getCourse_type() {
            return course_type;
        }

        public void setCourse_type(String course_type) {
            this.course_type = course_type;
        }

        public List<mCatalog> getCatalog() {
            return catalog;
        }

        public void setCatalog(List<mCatalog> catalog) {
            this.catalog = catalog;
        }
        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }
    public static class mCatalog{
        private String id;//章id
        private String parent_id;//父级id
        private String title;//章名称
        private String type;//0章，1视频2练习3图文4直播
        private String content;
        private String video;
        private String practice;
        private String learn_time;
        private String live_id;
        private List<mSon> son;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getParent_id() {
            return parent_id;
        }

        public void setParent_id(String parent_id) {
            this.parent_id = parent_id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getVideo() {
            return video;
        }

        public void setVideo(String video) {
            this.video = video;
        }

        public String getPractice() {
            return practice;
        }

        public void setPractice(String practice) {
            this.practice = practice;
        }

        public String getLearn_time() {
            return learn_time;
        }

        public void setLearn_time(String learn_time) {
            this.learn_time = learn_time;
        }

        public String getLive_id() {
            return live_id;
        }

        public void setLive_id(String live_id) {
            this.live_id = live_id;
        }

        public List<mSon> getSon() {
            return son;
        }

        public void setSon(List<mSon> son) {
            this.son = son;
        }
    }
    public static class mSon{
        private String id;
        private String parent_id;
        private String title;//节名称
        private String type;//0章，1视频2试题3图文
        private String content;
        private String video;
        private String practice;//试题id
        private String learn_time;
        private String live_id;//直播id
        private String learn_percent;//是否学过
        private mLiveInfo live_info;


        public String getLearn_percent() {
            return learn_percent;
        }

        public void setLearn_percent(String learn_percent) {
            this.learn_percent = learn_percent;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getParent_id() {
            return parent_id;
        }

        public void setParent_id(String parent_id) {
            this.parent_id = parent_id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getVideo() {
            return video;
        }

        public void setVideo(String video) {
            this.video = video;
        }

        public String getPractice() {
            return practice;
        }

        public void setPractice(String practice) {
            this.practice = practice;
        }

        public String getLearn_time() {
            return learn_time;
        }

        public void setLearn_time(String learn_time) {
            this.learn_time = learn_time;
        }

        public String getLive_id() {
            return live_id;
        }

        public void setLive_id(String live_id) {
            this.live_id = live_id;
        }

        public void setLive_info(mLiveInfo live_info) {
            this.live_info = live_info;
        }

        public mLiveInfo getLive_info() {
            return live_info;
        }
    }
    public static class mLiveInfo{
        private String stream_name;
        private String name;
        private String image;
        private String pull_url;//推流地址
        private String video_url;
        private String token;
        private String chat_room_id;//房间id
        private String push_url;
        private String app_name;
        private long start_time;

        public String getApp_name() {
            return app_name;
        }

        public void setApp_name(String app_name) {
            this.app_name = app_name;
        }

        public long getStart_time() {
            return start_time;
        }

        public void setStart_time(long start_time) {
            this.start_time = start_time;
        }

        public String getPush_url() {
            return push_url;
        }

        public void setPush_url(String push_url) {
            this.push_url = push_url;
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
}
