package com.jiaoshizige.teacherexam.model;


import com.jiaoshizige.teacherexam.http.JsonResponseParser;
import com.jiaoshizige.teacherexam.http.SupportResponse;

import org.xutils.http.annotation.HttpResponse;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2018/8/10.
 */
@HttpResponse(parser = JsonResponseParser.class)
public class TestResultDetailResponse extends SupportResponse implements Serializable {
    private List<mData> data;

    public List<mData> getData() {
        return data;
    }

    public void setData(List<mData> data) {
        this.data = data;
    }

    public static class mData implements Serializable {
        private String id;//试题id
        private String type;//试题类型（1单选2简答3辨析4论述5写作6材料分析8教学设计9活动设计题）
        private String course;
        private String chapter;
        private String stem;//试题名称
        private List<mMetas> metas;//选项
        private String answer;//正确答案
        private String analysis;//解析
        private String shiti_type;//试题类型（1闯关2真题）
        private String xueduan;//真题学段
        private String nianfen;//年份（真题年份）
        private String pfbz;//评分标准（分析题、写作题、简单题）
        private String totle_post;//本题被答次数
        private String totle_success;
        private String easy_error;//易错选项
        private String score;
        private String user_answer;//用户答案
        private String kaodian_name;//所属考点
        private String totle_user;//总答题次数
        private String success;//正确率
        private String is_collect;
        private String video;//视频解析
        private String first_image;
        private String course_name;
        private String k_name;
        private String course_id;
        private String qid;//题号

        public String getQid() {
            return qid;
        }

        public void setQid(String qid) {
            this.qid = qid;
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

        public String getVideo() {
            return video;
        }

        public void setVideo(String video) {
            this.video = video;
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

        public String getStem() {
            return stem;
        }

        public void setStem(String stem) {
            this.stem = stem;
        }

        public List<mMetas> getMetas() {
            return metas;
        }

        public void setMetas(List<mMetas> metas) {
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

        public String getScore() {
            return score;
        }

        public void setScore(String score) {
            this.score = score;
        }

        public String getUser_answer() {
            return user_answer;
        }

        public void setUser_answer(String user_answer) {
            this.user_answer = user_answer;
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
