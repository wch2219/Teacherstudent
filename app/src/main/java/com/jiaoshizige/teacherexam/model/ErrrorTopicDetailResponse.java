package com.jiaoshizige.teacherexam.model;

import android.renderscript.ScriptIntrinsicYuvToRGB;

import com.jiaoshizige.teacherexam.http.JsonResponseParser;
import com.jiaoshizige.teacherexam.http.SupportResponse;

import org.xutils.http.annotation.HttpResponse;

import java.util.List;

/**
 * Created by Administrator on 2018/8/1.
 */
@HttpResponse(parser = JsonResponseParser.class)
public class ErrrorTopicDetailResponse extends SupportResponse {
    private List<mData> data;

    public List<mData> getData() {
        return data;
    }

    public void setData(List<mData> data) {
        this.data = data;
    }

    public static class mData {
        private String id;
        private String type;
        private String stem;
        private List<mMates> metas;
        private String answer;
        private String analysis;
        private String shiti_type;
        private String nianfen;
        private String xueduan;
        private String pfbz;
        private String totle_post;
        private String totle_success;
        private String easy_error;
        private String kaodian_name;
        private String totle_user;
        private String success;
        private String user_answer;
        private String is_collect;
        private String video;//视频解析
        private String first_image;
        private String course_name;
        private String k_name;
        private String course_id;

        public String getVideo() {
            return video;
        }

        public void setVideo(String video) {
            this.video = video;
        }

        public String getFirst_image() {
            return first_image;
        }

        public void setFirst_image(String first_image) {
            this.first_image = first_image;
        }

        public String getCourse_name() {
            return course_name;
        }

        public void setCourse_name(String course_name) {
            this.course_name = course_name;
        }

        public String getK_name() {
            return k_name;
        }

        public void setK_name(String k_name) {
            this.k_name = k_name;
        }

        public String getCourse_id() {
            return course_id;
        }

        public void setCourse_id(String course_id) {
            this.course_id = course_id;
        }

        public String getIs_collect() {
            return is_collect;
        }

        public void setIs_collect(String is_collect) {
            this.is_collect = is_collect;
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

        public List<mMates> getMetas() {
            return metas;
        }

        public void setMetas(List<mMates> metas) {
            this.metas = metas;
        }

        public String getAnswer() {
            return answer;
        }

        public void setAnswer(String answer) {
            this.answer = answer;
        }

        public String getAnalysis() {
            return analysis;
        }

        public void setAnalysis(String analysis) {
            this.analysis = analysis;
        }

        public String getShiti_type() {
            return shiti_type;
        }

        public void setShiti_type(String shiti_type) {
            this.shiti_type = shiti_type;
        }

        public String getNianfen() {
            return nianfen;
        }

        public void setNianfen(String nianfen) {
            this.nianfen = nianfen;
        }

        public String getXueduan() {
            return xueduan;
        }

        public void setXueduan(String xueduan) {
            this.xueduan = xueduan;
        }

        public String getPfbz() {
            return pfbz;
        }

        public void setPfbz(String pfbz) {
            this.pfbz = pfbz;
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

        public String getUser_answer() {
            return user_answer;
        }

        public void setUser_answer(String user_answer) {
            this.user_answer = user_answer;
        }
    }

    public static class mMates {
        private mChoose choose;

        public mChoose getChoose() {
            return choose;
        }

        public void setChoose(mChoose choose) {
            this.choose = choose;
        }
    }

    public static class mChoose {
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
