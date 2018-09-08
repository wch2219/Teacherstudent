package com.jiaoshizige.teacherexam.model;

import com.jiaoshizige.teacherexam.http.JsonResponseParser;
import com.jiaoshizige.teacherexam.http.SupportResponse;

import org.xutils.http.annotation.HttpResponse;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2018/8/6.
 */
@HttpResponse(parser = JsonResponseParser.class)
public class MoNiResponse extends SupportResponse implements Serializable{
    private List<mData> data;

    public List<mData> getData() {
        return data;
    }

    public void setData(List<mData> data) {
        this.data = data;
    }

    public static class mData implements Serializable{
        private String id;
        private String type;
        private String stem;
        private List<mMetas> metas;
        private String answer;
        private String shiti_type;
        private String xueduan;
        private String nianfen;
        private String pfbz;
        private String score;
        private String is_done;
        private String qid;//题号
        private String user_answer;//自己的答案


        public String getUser_answer() {
            return user_answer;
        }

        public void setUser_answer(String user_answer) {
            this.user_answer = user_answer;
        }

        public String getQid() {
            return qid;
        }

        public void setQid(String qid) {
            this.qid = qid;
        }

        public String getIs_done() {
            return is_done;
        }

        public void setIs_done(String is_done) {
            this.is_done = is_done;
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
    public static class mMetas implements Serializable{
        private mChoose choose;

        public mChoose getChoose() {
            return choose;
        }

        public void setChoose(mChoose choose) {
            this.choose = choose;
        }
    }
    public static class mChoose implements Serializable{
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
