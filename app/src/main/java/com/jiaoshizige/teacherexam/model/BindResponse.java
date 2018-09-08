package com.jiaoshizige.teacherexam.model;

import com.jiaoshizige.teacherexam.http.JsonResponseParser;
import com.jiaoshizige.teacherexam.http.SupportResponse;

import org.xutils.http.annotation.HttpResponse;

/**
 * Created by Administrator on 2018/1/18 0018.
 */
@HttpResponse(parser = JsonResponseParser.class)
public class BindResponse extends SupportResponse {
    private mData data;

    public mData getData() {
        return data;
    }

    public void setData(mData data) {
        this.data = data;
    }

    public static class mData{
        private String is_bind;

        public String getIs_bind() {
            return is_bind;
        }

        public void setIs_bind(String is_bind) {
            this.is_bind = is_bind;
        }
    }
}
