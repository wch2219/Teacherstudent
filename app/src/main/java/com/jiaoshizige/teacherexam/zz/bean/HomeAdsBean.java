package com.jiaoshizige.teacherexam.zz.bean;

import com.jiaoshizige.teacherexam.http.JsonResponseParser;
import com.jiaoshizige.teacherexam.http.SupportResponse;

import org.xutils.http.annotation.HttpResponse;

import java.util.List;

@HttpResponse(parser = JsonResponseParser.class)
public class HomeAdsBean extends SupportResponse {


    /**
     * data : {"id":38,"title":"1张测试1","content":"1张测试1","detail":"<style type='text/css'>img {max-width:100%!important;}<\/style><div style=\"width:100%\"><p>1张测试1<\/p><\/div>","view":"","del_user":"","updated_at":"2018-08-07 18:18:28","img_arr":[{"type":"2","type_id":"174","images":"https://wxapi.zgclass.com/uploads/j_push/20180807181828197.png"},{"type":"2","type_id":"174","images":"https://wxapi.zgclass.com/uploads/j_push/20180807181828537.png"}]}
     */

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 38
         * title : 1张测试1
         * content : 1张测试1
         * detail : <style type='text/css'>img {max-width:100%!important;}</style><div style="width:100%"><p>1张测试1</p></div>
         * view :
         * del_user :
         * updated_at : 2018-08-07 18:18:28
         * img_arr : [{"type":"2","type_id":"174","images":"https://wxapi.zgclass.com/uploads/j_push/20180807181828197.png"},{"type":"2","type_id":"174","images":"https://wxapi.zgclass.com/uploads/j_push/20180807181828537.png"}]
         */

        private int id;
        private String title;
        private String content;
        private String detail;
        private String view;
        private String del_user;
        private String updated_at;
        private List<ImgArrBean> img_arr;

        public int getId() {
            return id;
        }

        public void setId(int id) {
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

        public String getView() {
            return view;
        }

        public void setView(String view) {
            this.view = view;
        }

        public String getDel_user() {
            return del_user;
        }

        public void setDel_user(String del_user) {
            this.del_user = del_user;
        }

        public String getUpdated_at() {
            return updated_at;
        }

        public void setUpdated_at(String updated_at) {
            this.updated_at = updated_at;
        }

        public List<ImgArrBean> getImg_arr() {
            return img_arr;
        }

        public void setImg_arr(List<ImgArrBean> img_arr) {
            this.img_arr = img_arr;
        }

        public static class ImgArrBean {
            /**
             * type : 2
             * type_id : 174
             * images : https://wxapi.zgclass.com/uploads/j_push/20180807181828197.png
             */

            private String type;
            private String type_id;
            private String images;

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

            public String getImages() {
                return images;
            }

            public void setImages(String images) {
                this.images = images;
            }
        }
    }
}
