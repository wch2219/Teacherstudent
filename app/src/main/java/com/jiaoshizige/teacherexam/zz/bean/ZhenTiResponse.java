package com.jiaoshizige.teacherexam.zz.bean;

import com.alibaba.fastjson.JSON;
import com.jiaoshizige.teacherexam.http.JsonResponseParser;
import com.jiaoshizige.teacherexam.http.SupportResponse;
import com.jiaoshizige.teacherexam.zz.database.ZhenTiList;

import org.xutils.http.annotation.HttpResponse;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2018/8/6.
 */
@HttpResponse(parser = JsonResponseParser.class)
public class ZhenTiResponse extends SupportResponse implements Serializable {
    private List<mData> data;

    public List<mData> getData() {
        return data;
    }

    public void setData(List<mData> data) {
        this.data = data;
    }

    public static class mData implements Serializable {
        private String id;
        private String type;
        private String course;
        private String chapter;
        private String point;
        private String stem;
        private List<mMetas> metas;
        private String answer;
        private String analysis;
        private String shiti_type;
        private String xueduan;
        private String nianfen;
        private String pfbz;
        private String totle_post;
        private String totle_success;
        private String easy_error;
        private String score;
        private String is_done;//是否做过
        private String qid;//题号
        private String user_answer;//自己的答案
        private String kaodian_name;
        private String totle_user;
        private String success;
        private String is_collect;

        public mData(String id, String type, String course, String chapter, String stem, String metas, String answer, String analysis, String shiti_type, String xueduan, String nianfen,
                     String pfbz, String totle_post, String totle_success,
                     String easy_error, String score, String user_answer, String kaodian_name, String totle_user, String success, String is_collect) {

            this.id = id;
            this.type = type;
            this.course = course;
            this.chapter = chapter;
            this.stem = stem;
            this.metas = JSON.parseArray(metas, mMetas.class);
            this.answer = answer;
            this.analysis = analysis;
            this.shiti_type = shiti_type;
            this.xueduan = xueduan;
            this.nianfen = nianfen;
            this.pfbz = pfbz;
            this.totle_post = totle_post;
            this.totle_success = totle_success;
            this.easy_error = easy_error;
            this.score = score;
            this.user_answer = user_answer;
            this.kaodian_name = kaodian_name;
            this.totle_user = totle_user;
            this.success = success;
            this.is_collect = is_collect;

        }

        public String getKaodian_name() {
            return kaodian_name;
        }

        public void setKaodian_name(String kaodian_name) {
            this.kaodian_name = kaodian_name;
        }

        public String getTotle_user() {
            return totle_user;
        }

        public void setTotle_user(String totle_user) {
            this.totle_user = totle_user;
        }

        public String getSuccess() {
            return success;
        }

        public void setSuccess(String success) {
            this.success = success;
        }

        public String getIs_collect() {
            return is_collect;
        }

        public void setIs_collect(String is_collect) {
            this.is_collect = is_collect;
        }

        public String getIs_done() {
            return is_done;
        }

        public void setIs_done(String is_done) {
            this.is_done = is_done;
        }

        public String getQid() {
            return qid;
        }

        public void setQid(String qid) {
            this.qid = qid;
        }

        public String getUser_answer() {
            return user_answer;
        }

        public void setUser_answer(String user_answer) {
            this.user_answer = user_answer;
        }

        public String getCourse() {
            return course;
        }

        public void setCourse(String course) {
            this.course = course;
        }

        public String getChapter() {
            return chapter;
        }

        public void setChapter(String chapter) {
            this.chapter = chapter;
        }

        public String getPoint() {
            return point;
        }

        public void setPoint(String point) {
            this.point = point;
        }

        public List<mMetas> getMetas() {
            return metas;
        }

        public void setMetas(List<mMetas> metas) {
            this.metas = metas;
        }

        public String getAnalysis() {
            return analysis;
        }

        public void setAnalysis(String analysis) {
            this.analysis = analysis;
        }

        public String getTotle_post() {
            return totle_post;
        }

        public void setTotle_post(String totle_post) {
            this.totle_post = totle_post;
        }

        public String getTotle_success() {
            return totle_success;
        }

        public void setTotle_success(String totle_success) {
            this.totle_success = totle_success;
        }

        public String getEasy_error() {
            return easy_error;
        }

        public void setEasy_error(String easy_error) {
            this.easy_error = easy_error;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getStem() {
            return stem;
        }

        public void setStem(String stem) {
            this.stem = stem;
        }


        public String getAnswer() {
            return answer;
        }

        public void setAnswer(String answer) {
            this.answer = answer;
        }

        public String getShiti_type() {
            return shiti_type;
        }

        public void setShiti_type(String shiti_type) {
            this.shiti_type = shiti_type;
        }

        public String getXueduan() {
            return xueduan;
        }

        public void setXueduan(String xueduan) {
            this.xueduan = xueduan;
        }

        public String getNianfen() {
            return nianfen;
        }

        public void setNianfen(String nianfen) {
            this.nianfen = nianfen;
        }

        public String getPfbz() {
            return pfbz;
        }

        public void setPfbz(String pfbz) {
            this.pfbz = pfbz;
        }

        public String getScore() {
            return score;
        }

        public void setScore(String score) {
            this.score = score;
        }
    }

    public static class mMetas implements Serializable {
        private mChoose choose;

        public mChoose getChoose() {
            return choose;
        }

        public void setChoose(mChoose choose) {
            this.choose = choose;
        }
    }

    public static class mChoose implements Serializable {
        private String id;
        private String text;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }
}

