package com.jiaoshizige.teacherexam.model;

import com.jiaoshizige.teacherexam.http.JsonResponseParser;
import com.jiaoshizige.teacherexam.http.SupportResponse;

import org.xutils.http.annotation.HttpResponse;

import java.util.List;

/**
 * Created by Administrator on 2018/1/9 0009.
 */
@HttpResponse(parser = JsonResponseParser.class)
public class PracticeResponse extends SupportResponse {
    public mData data;

    public mData getData() {
        return data;
    }

    public void setData(mData data) {
        this.data = data;
    }

    public static class mData{
        private String id;//试卷id;
        private String name;//试卷名称;
        private String do_status;//1已做过，0未做过
        private List<mQuestion>questions;

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

        public String getDo_status() {
            return do_status;
        }

        public void setDo_status(String do_status) {
            this.do_status = do_status;
        }

        public List<mQuestion> getQuestions() {
            return questions;
        }

        public void setQuestions(List<mQuestion> questions) {
            this.questions = questions;
        }
    }

    public static class mQuestion{
        private String id;//题目id;
        private String type;//题目类型：1单项选择题，2不定项选择3材料分析题，4判断题，5填空题
        private String stem;//题干
        private List<mMetas> metas;//选项;
        private String analysis;//解析;
        private String user_answer;//用户答案
        private String answer;//答案

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

        public String getAnalysis() {
            return analysis;
        }

        public void setAnalysis(String analysis) {
            this.analysis = analysis;
        }

        public String getUser_answer() {
            return user_answer;
        }

        public void setUser_answer(String user_answer) {
            this.user_answer = user_answer;
        }

        public String getAnswer() {
            return answer;
        }

        public void setAnswer(String answer) {
            this.answer = answer;
        }

        public List<mMetas> getMetas() {
            return metas;
        }

        public void setMetas(List<mMetas> metas) {
            this.metas = metas;
        }
    }
    public static class mMetas{
        private mChoose choose;

        public mChoose getChoose() {
            return choose;
        }

        public void setChoose(mChoose choose) {
            this.choose = choose;
        }
    }
    public static class mChoose{
        private String id;
        private String status;
        private String text;
        private String consequence;

        public String getConsequence() {
            return consequence;
        }

        public void setConsequence(String consequence) {
            this.consequence = consequence;
        }

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

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
}
