package com.jiaoshizige.teacherexam.model;

import com.jiaoshizige.teacherexam.http.JsonResponseParser;
import com.jiaoshizige.teacherexam.http.SupportResponse;

import org.xutils.http.annotation.HttpResponse;

/**
 * Created by Administrator on 2017/12/21 0021.
 */
@HttpResponse(parser = JsonResponseParser.class)
public class PushDatailResponse extends SupportResponse {
    public mData data;

    public mData getData() {
        return data;
    }

    public void setData(mData data) {
        this.data = data;
    }

    public static class mData{
        private String id;
        private String title;
        private String detail;
        private int count_zan;
        private String is_like;
        private String count_view;
        private String content;

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDetail() {
            return detail;
        }

        public void setDetail(String detail) {
            this.detail = detail;
        }

        public int getCount_zan() {
            return count_zan;
        }

        public void setCount_zan(int count_zan) {
            this.count_zan = count_zan;
        }

        public String getIs_like() {
            return is_like;
        }

        public void setIs_like(String is_like) {
            this.is_like = is_like;
        }

        public String getCount_view() {
            return count_view;
        }

        public void setCount_view(String count_view) {
            this.count_view = count_view;
        }
    }
}
