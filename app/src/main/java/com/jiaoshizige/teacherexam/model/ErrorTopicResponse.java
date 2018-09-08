package com.jiaoshizige.teacherexam.model;

import android.os.Parcelable;

import com.jiaoshizige.teacherexam.http.JsonResponseParser;
import com.jiaoshizige.teacherexam.http.SupportResponse;

import org.xutils.http.annotation.HttpResponse;

import java.util.List;


/**
 * Created by Administrator on 2018/7/31.
 */
@HttpResponse(parser = JsonResponseParser.class)
public class ErrorTopicResponse extends SupportResponse {
    private List<mData>data;

    public List<mData> getData() {
        return data;
    }

    public void setData(List<mData> data) {
        this.data = data;
    }

    public static class mData{
        private String id;
        private String tiku_id;
        private String name;
        private String parent_id;
        private List<mSon> son;
        private String totle_question;

        public String getTotle_question() {
            return totle_question;
        }

        public void setTotle_question(String totle_question) {
            this.totle_question = totle_question;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTiku_id() {
            return tiku_id;
        }

        public void setTiku_id(String tiku_id) {
            this.tiku_id = tiku_id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getParent_id() {
            return parent_id;
        }

        public void setParent_id(String parent_id) {
            this.parent_id = parent_id;
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
        private String tiku_id;
        private String name;
        private String parent_id;
        private String totle_question;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTiku_id() {
            return tiku_id;
        }

        public void setTiku_id(String tiku_id) {
            this.tiku_id = tiku_id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getParent_id() {
            return parent_id;
        }

        public void setParent_id(String parent_id) {
            this.parent_id = parent_id;
        }

        public String getTotle_question() {
            return totle_question;
        }

        public void setTotle_question(String totle_question) {
            this.totle_question = totle_question;
        }
    }
}
