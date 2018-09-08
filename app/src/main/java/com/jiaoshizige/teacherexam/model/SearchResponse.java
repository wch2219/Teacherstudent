package com.jiaoshizige.teacherexam.model;

import com.jiaoshizige.teacherexam.http.JsonResponseParser;
import com.jiaoshizige.teacherexam.http.SupportResponse;

import org.xutils.http.annotation.HttpResponse;

import java.util.List;

/**
 * Created by Administrator on 2017/a1/28.
 * 搜索
 */
@HttpResponse(parser = JsonResponseParser.class)
public class SearchResponse extends SupportResponse {
    private mData data;

    public mData getData() {
        return data;
    }

    public void setData(mData data) {
        this.data = data;
    }

    public static class mData{
        private List<mHotTag> hot_tag;
        private List<mMyTag> my_tag;

        public List<mHotTag> getHot_tag() {
            return hot_tag;
        }

        public void setHot_tag(List<mHotTag> hot_tag) {
            this.hot_tag = hot_tag;
        }

        public List<mMyTag> getMy_tag() {
            return my_tag;
        }

        public void setMy_tag(List<mMyTag> my_tag) {
            this.my_tag = my_tag;
        }
    }
    public static class mHotTag{
        private String keyword;

        public String getKeyword() {
            return keyword;
        }

        public void setKeyword(String keyword) {
            this.keyword = keyword;
        }
    }
    public static class mMyTag{
        private String keyword;

        public String getKeyword() {
            return keyword;
        }

        public void setKeyword(String keyword) {
            this.keyword = keyword;
        }
    }
}

