package com.jiaoshizige.teacherexam.model;



import com.jiaoshizige.teacherexam.http.JsonResponseParser;
import com.jiaoshizige.teacherexam.http.SupportResponse;

import org.xutils.http.annotation.HttpResponse;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/8/23.
 * maxiao
 * 资讯的列表
 */
@HttpResponse(parser = JsonResponseParser.class)
public class InformationResponse extends SupportResponse {
        private List<mData> data;
        public List<InformationResponse.mData> getData() {
            return data;
        }
        public void setData(List<InformationResponse.mData> data) {
            this.data = data;
        }
    public static class mData implements Serializable{
        private String id;//咨询id
        private String title;//标题
        private String images;//图片
        private String content;//：内容
        private String view_count;//浏览量
        private String created_at;//发布时间
        private String article_id;
        private String description;
        private String author;
        private String updated_at;

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getUpdated_at() {
            return updated_at;
        }

        public void setUpdated_at(String updated_at) {
            this.updated_at = updated_at;
        }

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public String getArticle_id() {
            return article_id;
        }

        public void setArticle_id(String article_id) {
            this.article_id = article_id;
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

        public String getImages() {
            return images;
        }

        public void setImages(String images) {
            this.images = images;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getView_count() {
            return view_count;
        }

        public void setView_count(String view_count) {
            this.view_count = view_count;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }
    }


}
