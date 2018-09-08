package com.jiaoshizige.teacherexam.zz.bean;

import com.jiaoshizige.teacherexam.http.JsonResponseParser;
import com.jiaoshizige.teacherexam.http.SupportResponse;

import org.xutils.http.annotation.HttpResponse;

import java.util.List;

@HttpResponse(parser = JsonResponseParser.class)
public class ZhenTiDownLoadBean extends SupportResponse {


    /**
     * status_code : 200
     * message : 请求成功
     * data : [{"id":12,"type":1,"course":31,"chapter":74,"stem":"<style type='text/css'>p,div{display:inline;padding:0;margin:0;height:auto;line-height:30px}img {max-width:100%!important;}<\/style><div style=\"width:100%;font-size:16px;word-break: inherit;\"><span style=\"color:#8D70DA\">(单选题)&nbsp;&nbsp;<\/span>&nbsp;&nbsp;2018年(上)&nbsp;&nbsp;<p>测试题<\/p><\/div>","metas":[{"choose":{"id":1,"text":"<div style='margin: 0;padding: 0;font-size:16px;line-height:30px'>1<\/div>"}},{"choose":{"id":2,"text":"<div style='margin: 0;padding: 0;font-size:16px;line-height:30px'>2<\/div>"}},{"choose":{"id":3,"text":"<div style='margin: 0;padding: 0;font-size:16px;line-height:30px'>3<\/div>"}},{"choose":{"id":4,"text":"<div style='margin: 0;padding: 0;font-size:16px;line-height:30px'>4<\/div>"}}],"answer":"D","analysis":"<p>我是解析<\/p>","shiti_type":2,"xueduan":"上","nianfen":"2018","pfbz":"","totle_post":192,"totle_success":191,"easy_error":"C","score":2,"user_answer":"","kaodian_name":"","totle_user":192,"success":99,"is_collect":0}]
     */

    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 12
         * type : 1
         * course : 31
         * chapter : 74
         * stem : <style type='text/css'>p,div{display:inline;padding:0;margin:0;height:auto;line-height:30px}img {max-width:100%!important;}</style><div style="width:100%;font-size:16px;word-break: inherit;"><span style="color:#8D70DA">(单选题)&nbsp;&nbsp;</span>&nbsp;&nbsp;2018年(上)&nbsp;&nbsp;<p>测试题</p></div>
         * metas : [{"choose":{"id":1,"text":"<div style='margin: 0;padding: 0;font-size:16px;line-height:30px'>1<\/div>"}},{"choose":{"id":2,"text":"<div style='margin: 0;padding: 0;font-size:16px;line-height:30px'>2<\/div>"}},{"choose":{"id":3,"text":"<div style='margin: 0;padding: 0;font-size:16px;line-height:30px'>3<\/div>"}},{"choose":{"id":4,"text":"<div style='margin: 0;padding: 0;font-size:16px;line-height:30px'>4<\/div>"}}]
         * answer : D
         * analysis : <p>我是解析</p>
         * shiti_type : 2
         * xueduan : 上
         * nianfen : 2018
         * pfbz :
         * totle_post : 192
         * totle_success : 191
         * easy_error : C
         * score : 2
         * user_answer :
         * kaodian_name :
         * totle_user : 192
         * success : 99
         * is_collect : 0
         */

        private String id;
        private int type;
        private String course;
        private String chapter;
        private String stem;
        private String answer;
        private String analysis;
        private int shiti_type;
        private String xueduan;
        private String nianfen;
        private String pfbz;
        private String totle_post;
        private String totle_success;
        private String easy_error;
        private String score;
        private String user_answer;
        private String kaodian_name;
        private String totle_user;
        private String success;
        private String is_collect;
        private List<MetasBean> metas;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
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

        public int getShiti_type() {
            return shiti_type;
        }

        public void setShiti_type(int shiti_type) {
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

        public List<MetasBean> getMetas() {
            return metas;
        }

        public void setMetas(List<MetasBean> metas) {
            this.metas = metas;
        }

        public static class MetasBean {
            /**
             * choose : {"id":1,"text":"<div style='margin: 0;padding: 0;font-size:16px;line-height:30px'>1<\/div>"}
             */

            private ChooseBean choose;

            public ChooseBean getChoose() {
                return choose;
            }

            public void setChoose(ChooseBean choose) {
                this.choose = choose;
            }

            public static class ChooseBean {
                /**
                 * id : 1
                 * text : <div style='margin: 0;padding: 0;font-size:16px;line-height:30px'>1</div>
                 */

                private int id;
                private String text;

                public int getId() {
                    return id;
                }

                public void setId(int id) {
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
    }
}
