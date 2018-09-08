package com.jiaoshizige.teacherexam.model;

import com.jiaoshizige.teacherexam.http.JsonResponseParser;
import com.jiaoshizige.teacherexam.http.SupportResponse;

import org.xutils.http.annotation.HttpResponse;

import java.util.List;

/**
 * Created by Administrator on 2017/a1/24.
 * 课程目录
 */
@HttpResponse(parser = JsonResponseParser.class)
public class CourseCatalogResponse extends SupportResponse {
    private mData data;

    public mData getData() {
        return data;
    }

    public void setData(mData data) {
        this.data = data;
    }

    public static class mData{
        private List<mCatalog> catalog;
        private String course_type;

        public List<mCatalog> getCatalog() {
            return catalog;
        }

        public void setCatalog(List<mCatalog> catalog) {
            this.catalog = catalog;
        }

        public String getCourse_type() {
            return course_type;
        }

        public void setCourse_type(String course_type) {
            this.course_type = course_type;
        }
    }
    public static class mCatalog{
        private String id;//章id
        private String parent_id;//父级id
        private String title;//章名称
        private String type;//0章，1视频2试题3图文
        private String content;//
        private String video;//
        private String practice;//
        private List<mSon> son ;//
        private boolean isCheck;
        private String learn_time;//学习完成时间：分钟
        private String live_id;//

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

        public boolean isCheck() {
            return isCheck;
        }

        public void setCheck(boolean check) {
            isCheck = check;
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

        public List<mSon> getSon() {
            return son;
        }

        public void setSon(List<mSon> son) {
            this.son = son;
        }
    }
    public static class mSon{
        private String id;//
        private String parent_id;//
        private String title;//小节名称
        private String type;//1视频2试题3图文
        private String content;//
        private String video;//
        private String practice;//
        private boolean isCheck;
        private String learn_time;//学习完成时间：分钟
        private String live_id;//

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

        public boolean isCheck() {
            return isCheck;
        }

        public void setCheck(boolean check) {
            isCheck = check;
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
    }
}
