package com.jiaoshizige.teacherexam.model;

import com.jiaoshizige.teacherexam.http.JsonResponseParser;
import com.jiaoshizige.teacherexam.http.SupportResponse;

import org.xutils.http.annotation.HttpResponse;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2018/7/23.
 */
@HttpResponse(parser = JsonResponseParser.class)
public class CheckPointResponse  implements Serializable{
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

    public CheckPointResponse(List<mData> data) {
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

        public mData(String id, String name, String smalltext, String huangguan_totle_num, String huangguan_have_totle_num, String is_pass, String is_open) {
            this.id = id;
            this.name = name;
            this.smalltext = smalltext;
            this.huangguan_totle_num = huangguan_totle_num;
            this.huangguan_have_totle_num = huangguan_have_totle_num;
            this.is_pass = is_pass;
            this.is_open = is_open;
        }

        @Override
        public String toString() {
            return "mData{" +
                    "id='" + id + '\'' +
                    ", name='" + name + '\'' +
                    ", smalltext='" + smalltext + '\'' +
                    ", huangguan_totle_num='" + huangguan_totle_num + '\'' +
                    ", huangguan_have_totle_num='" + huangguan_have_totle_num + '\'' +
                    ", is_pass='" + is_pass + '\'' +
                    ", is_open='" + is_open + '\'' +
                    '}';
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

}
