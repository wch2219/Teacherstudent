package com.jiaoshizige.teacherexam.model;

import com.jiaoshizige.teacherexam.http.JsonResponseParser;
import com.jiaoshizige.teacherexam.http.SupportResponse;

import org.xutils.http.annotation.HttpResponse;

import java.util.List;

/**
 * Created by Administrator on 2017/12/1.
 * 问答列表
 */
@HttpResponse(parser = JsonResponseParser.class)
public class QuestionAnswerReponse extends SupportResponse {
    private List<mData> data;

    public List<mData> getData() {
        return data;
    }

    public void setData(List<mData> data) {
        this.data = data;
    }

    public static class mData {
        private String id;//问题id
        private String content;//问题内容
        private String created_at;//提问时间
        private String count;//回答数
        private mAnswer answer;//
        public String getContent() {
            return content;
        }
        public void setContent(String content) {
            this.content = content;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public String getCount() {
            return count;
        }

        public void setCount(String count) {
            this.count = count;
        }

        public mAnswer getAnswer() {
            return answer;
        }

        public void setAnswer(mAnswer answer) {
            this.answer = answer;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }

    public static class mAnswer {
        private String content;//最近的一条回答内容

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }
}
