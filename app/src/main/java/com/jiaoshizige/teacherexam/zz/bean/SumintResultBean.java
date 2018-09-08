package com.jiaoshizige.teacherexam.zz.bean;

import com.jiaoshizige.teacherexam.http.JsonResponseParser;
import com.jiaoshizige.teacherexam.http.SupportResponse;

import org.xutils.http.annotation.HttpResponse;

@HttpResponse(parser = JsonResponseParser.class)
public class SumintResultBean extends SupportResponse {

    private String data;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
