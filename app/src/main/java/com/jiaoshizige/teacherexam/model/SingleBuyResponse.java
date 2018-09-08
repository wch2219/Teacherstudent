package com.jiaoshizige.teacherexam.model;

import com.jiaoshizige.teacherexam.http.JsonResponseParser;
import com.jiaoshizige.teacherexam.http.SupportResponse;

import org.xutils.http.annotation.HttpResponse;

import java.util.List;

/**
 * Created by Administrator on 2018/3/15.
 */
@HttpResponse(parser = JsonResponseParser.class)
public class SingleBuyResponse extends SupportResponse{
    private List<Data> mData;

    public List<Data> getmData() {
        return mData;
    }

    public void setmData(List<Data> mData) {
        this.mData = mData;
    }

    public static class Data{
        private String title;
        private List<Child> mChild;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public List<Child> getmChild() {
            return mChild;
        }

        public void setmChild(List<Child> mChild) {
            this.mChild = mChild;
        }
    }
    public static class Child{
        private String title;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }
}
