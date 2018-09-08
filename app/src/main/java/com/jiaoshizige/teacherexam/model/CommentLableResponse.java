package com.jiaoshizige.teacherexam.model;

import com.jiaoshizige.teacherexam.http.JsonResponseParser;
import com.jiaoshizige.teacherexam.http.SupportResponse;

import org.xutils.http.annotation.HttpResponse;

import java.util.List;

/**
 * Created by Administrator on 2017/a1/29 0029.
 */
@HttpResponse(parser = JsonResponseParser.class)
public class CommentLableResponse extends SupportResponse {
    public List<mData> data;

    public List<mData> getData() {
        return data;
    }

    public void setData(List<mData> data) {
        this.data = data;
    }

    public static class mData{
        private String id;
        private String label_name;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getLabel_name() {
            return label_name;
        }

        public void setLabel_name(String label_name) {
            this.label_name = label_name;
        }
    }
}
