package com.jiaoshizige.teacherexam.model;

import com.jiaoshizige.teacherexam.http.JsonResponseParser;
import com.jiaoshizige.teacherexam.http.SupportResponse;

import org.xutils.http.annotation.HttpResponse;

/**
 * Created by Administrator on 2018/8/3.
 */
@HttpResponse(parser = JsonResponseParser.class)
public class SimulationResponse extends SupportResponse {
    private mData data;

    public mData getData() {
        return data;
    }

    public void setData(mData data) {
        this.data = data;
    }

    public static class mData{
        private String name;
        private String smalltext;
        private String moni_num;
        private String moni_score;
        private String moni_ava;

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

        public String getMoni_num() {
            return moni_num;
        }

        public void setMoni_num(String moni_num) {
            this.moni_num = moni_num;
        }

        public String getMoni_score() {
            return moni_score;
        }

        public void setMoni_score(String moni_score) {
            this.moni_score = moni_score;
        }

        public String getMoni_ava() {
            return moni_ava;
        }

        public void setMoni_ava(String moni_ava) {
            this.moni_ava = moni_ava;
        }
    }
}
