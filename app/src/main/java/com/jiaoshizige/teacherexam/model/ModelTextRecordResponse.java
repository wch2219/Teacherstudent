package com.jiaoshizige.teacherexam.model;

import com.jiaoshizige.teacherexam.http.JsonResponseParser;
import com.jiaoshizige.teacherexam.http.SupportResponse;

import org.xutils.http.annotation.HttpResponse;

import java.util.List;

/**
 * Created by Administrator on 2018/8/3.
 */
@HttpResponse(parser = JsonResponseParser.class)
public class ModelTextRecordResponse extends SupportResponse {
    private List<mData> data;

    public List<mData> getData() {
        return data;
    }

    public void setData(List<mData> data) {
        this.data = data;
    }

    public static class mData{
        private String id;
        private String name;
        private String created_at;
        private String paper_totle;
        private String moni_totle;

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

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public String getPaper_totle() {
            return paper_totle;
        }

        public void setPaper_totle(String paper_totle) {
            this.paper_totle = paper_totle;
        }

        public String getMoni_totle() {
            return moni_totle;
        }

        public void setMoni_totle(String moni_totle) {
            this.moni_totle = moni_totle;
        }
    }
}
