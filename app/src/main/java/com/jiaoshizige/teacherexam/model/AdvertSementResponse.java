package com.jiaoshizige.teacherexam.model;

import com.jiaoshizige.teacherexam.http.JsonResponseParser;
import com.jiaoshizige.teacherexam.http.SupportResponse;

import org.xutils.http.annotation.HttpResponse;

/**
 * Created by Administrator on 2018/3/23.
 */
@HttpResponse(parser = JsonResponseParser.class)
public class AdvertSementResponse extends SupportResponse{
    private mData data;

    public mData getData() {
        return data;
    }

    public void setData(mData data) {
        this.data = data;
    }

    public static class mData{
        private String id;//
        private String title;//标题
        private String content;//
        private String detail;//
        private String type;//'1班级2课程3图书4图文5外链'
        private String type_id;//
        private String view;//
        private String images;//
        private String del_user;//
        private String created_at;//

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

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getDetail() {
            return detail;
        }

        public void setDetail(String detail) {
            this.detail = detail;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getType_id() {
            return type_id;
        }

        public void setType_id(String type_id) {
            this.type_id = type_id;
        }

        public String getView() {
            return view;
        }

        public void setView(String view) {
            this.view = view;
        }

        public String getImages() {
            return images;
        }

        public void setImages(String images) {
            this.images = images;
        }

        public String getDel_user() {
            return del_user;
        }

        public void setDel_user(String del_user) {
            this.del_user = del_user;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }
    }
}
