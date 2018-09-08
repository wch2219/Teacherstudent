package com.jiaoshizige.teacherexam.activity.aaa;

import com.jiaoshizige.teacherexam.http.JsonResponseParser;
import com.jiaoshizige.teacherexam.model.CheckPointResponse;

import org.xutils.http.annotation.HttpResponse;

import java.io.Serializable;
import java.util.List;

/**
 * author: sunjian
 * created on: 2017/8/24 下午3:14
 * description:
 */
@HttpResponse(parser = JsonResponseParser.class)
public class MyBean implements Serializable {
    public List<mData> data;
    public List<mData> getData() {
        return data;
    }

    public void setData(List<mData> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "CheckPointResponse{" +
                "data=" + data +
                '}';
    }

    public MyBean(List<mData> data) {
        this.data = data;
    }

    public static class mData implements Serializable{
        private String id;
        private String name;
        private String smalltext;
        private String huangguan_totle_num;
        private String huangguan_have_totle_num;
        private String is_pass;
        private String is_open;
        private String img;

    public String getImg() {
        return img;
    }
        public String getIs_open() {
            return is_open;
        }

        public void setIs_open(String is_open) {
            this.is_open = is_open;
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

        public String getSmalltext() {
            return smalltext;
        }

        public void setSmalltext(String smalltext) {
            this.smalltext = smalltext;
        }

        public String getHuangguan_totle_num() {
            return huangguan_totle_num;
        }

        public void setHuangguan_totle_num(String huangguan_totle_num) {
            this.huangguan_totle_num = huangguan_totle_num;
        }

        public String getHuangguan_have_totle_num() {
            return huangguan_have_totle_num;
        }

        public void setHuangguan_have_totle_num(String huangguan_have_totle_num) {
            this.huangguan_have_totle_num = huangguan_have_totle_num;
        }

        public String getIs_pass() {
            return is_pass;
        }

        public void setIs_pass(String is_pass) {
            this.is_pass = is_pass;
        }
    }



//    private long id;
//    private String img;
//
//    public MyBean(String img) {
//        this.img = img;
//    }
//
//    public String getImg() {
//        return img;
//    }
//
//    @Override
//    public String toString() {
//        return "MyBean{" +
//                "id=" + id +
//                ", img='" + img + '\'' +
//                '}';
//    }
}
