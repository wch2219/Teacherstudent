package com.jiaoshizige.teacherexam.zz.database;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * ArticleBean实体类，存储article数据表中的数据
 * 数据库中的article表和user表是关联的，因此我们需要在article表中配置外键
 * <p>
 * foreignColumnName：外键约束指向的类中的属性名
 * foreign：当前字段是否是外键
 * foreignAutoRefresh：如果这个属性设置为true，在关联查询的时候就不需要再调用refresh()方法了
 */
@DatabaseTable(tableName = "zhenti")
public class ZhenTi implements Serializable {
    // article表中各个字段的名称
    public static final String COLUMNNAME_ID = "id";
    // public static final String COLUMNNAME_ZTLISTID = "ztlistid";
    public static final String COLUMNNAME_ZTID = "ztid";//题目原本的id
    public static final String COLUMNNAME_TYPE = "type";
    public static final String COLUMNNAME_COURSE = "course";
    public static final String COLUMNNAME_CHAPTER = "chapter";
    public static final String COLUMNNAME_STEM = "stem";
    public static final String COLUMNNAME_METAS = "metas";
    public static final String COLUMNNAME_ANSWER = "answer";
    public static final String COLUMNNAME_ANALYSIS = "analysis";
    public static final String COLUMNNAME_SHITITYPE = "shiti_type";
    public static final String COLUMNNAME_XUAN = "xueduan";
    public static final String COLUMNNAME_NIANFEN = "nianfen";
    public static final String COLUMNNAME_PFBZ = "pfbz";
    public static final String COLUMNNAME_TOTALPOST = "totle_post";
    public static final String COLUMNNAME_TOTALSUCCESS = "totle_success";
    public static final String COLUMNNAME_EASYERROR = "easy_error";
    public static final String COLUMNNAME_SCORE = "score";
    public static final String COLUMNNAME_USERANSWER = "user_answer";//自己的答案
    public static final String COLUMNNAME_KAODIANNAME = "kaodian_name";
    public static final String COLUMNNAME_TOTALUSER = "totle_user";
    public static final String COLUMNNAME_SUCCESS = "success";
    public static final String COLUMNNAME_ISCOLLECT = "is_collect";
    public static final String COLUMNNAME_ISDONE = "is_done";//是否做过
    public static final String COLUMNNAME_QID = "qid";//题号
    public static final String COLUMNNAME_TIKUID = "tiku_id";//题库id
    public static final String COLUMNNAME_ZHENTIID = "zhenti_id";//所属真题试卷的id

    @DatabaseField(generatedId = true, columnName = COLUMNNAME_ID, useGetSet = true)
    private int id;
    @DatabaseField(columnName = COLUMNNAME_ZTID, useGetSet = true)
    private String ztid;
    @DatabaseField(columnName = COLUMNNAME_TYPE, useGetSet = true, defaultValue = "0")
    private int type;
    @DatabaseField(columnName = COLUMNNAME_COURSE, useGetSet = true)
    private String course;
    @DatabaseField(columnName = COLUMNNAME_CHAPTER, useGetSet = true)
    private String chapter;
    @DatabaseField(columnName = COLUMNNAME_STEM, useGetSet = true)
    private String stem;
    @DatabaseField(columnName = COLUMNNAME_METAS, useGetSet = true)
    private String metas;
    @DatabaseField(columnName = COLUMNNAME_ANSWER, useGetSet = true)
    private String answer;
    @DatabaseField(columnName = COLUMNNAME_ANALYSIS, useGetSet = true)
    private String analysis;
    @DatabaseField(columnName = COLUMNNAME_SHITITYPE, useGetSet = true, defaultValue = "0")
    private int shiti_type;
    @DatabaseField(columnName = COLUMNNAME_XUAN, useGetSet = true)
    private String xueduan;
    @DatabaseField(columnName = COLUMNNAME_NIANFEN, useGetSet = true)
    private String nianfen;
    @DatabaseField(columnName = COLUMNNAME_PFBZ, useGetSet = true)
    private String pfbz;
    @DatabaseField(columnName = COLUMNNAME_TOTALPOST, useGetSet = true)
    private String totle_post;
    @DatabaseField(columnName = COLUMNNAME_TOTALSUCCESS, useGetSet = true)
    private String totle_success;
    @DatabaseField(columnName = COLUMNNAME_EASYERROR, useGetSet = true)
    private String easy_error;
    @DatabaseField(columnName = COLUMNNAME_SCORE, useGetSet = true)
    private String score;
    @DatabaseField(columnName = COLUMNNAME_USERANSWER, useGetSet = true)
    private String user_answer;
    @DatabaseField(columnName = COLUMNNAME_KAODIANNAME, useGetSet = true)
    private String kaodian_name;
    @DatabaseField(columnName = COLUMNNAME_TOTALUSER, useGetSet = true)
    private String totle_user;
    @DatabaseField(columnName = COLUMNNAME_SUCCESS, useGetSet = true)
    private String success;
    @DatabaseField(columnName = COLUMNNAME_ISCOLLECT, useGetSet = true)
    private String is_collect;
    @DatabaseField(columnName = COLUMNNAME_ISDONE, useGetSet = true, defaultValue = "0")
    private String is_done;
    @DatabaseField(columnName = COLUMNNAME_QID, useGetSet = true)
    private int qid;
    @DatabaseField(columnName = COLUMNNAME_TIKUID, useGetSet = true)
    private String tiku_id;
    @DatabaseField(columnName = COLUMNNAME_ZHENTIID, useGetSet = true)
    private String zhenti_id;
//    @DatabaseField(columnName = COLUMNNAME_ZTLISTID, foreign = true, foreignAutoRefresh = true, foreignAutoCreate = true,
//            foreignColumnName = ZhenTiList.COLUMNNAME_ID)
//    private ZhenTiList ztlistid;

    public ZhenTi() {
    }

    public ZhenTi(String ztid, int type, String course, String chapter, String stem, String metas, String answer, String analysis, int shiti_type, String xueduan, String nianfen, String pfbz, String totle_post, String totle_success,
                  String easy_error, String score, String user_answer, String kaodian_name, String totle_user, String success, String is_collect, String tiku_id, String zhenti_id, int qid) {

        this.ztid = ztid;
        this.type = type;
        this.course = course;
        this.chapter = chapter;
        this.stem = stem;
        this.metas = metas;
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
        //   this.ztlistid = ztlistid;
        this.tiku_id = tiku_id;
        this.zhenti_id = zhenti_id;
        this.qid = qid;

    }

    public String getTiku_id() {
        return tiku_id;
    }

    public void setTiku_id(String tiku_id) {
        this.tiku_id = tiku_id;
    }

    public String getZhenti_id() {
        return zhenti_id;
    }

    public void setZhenti_id(String zhenti_id) {
        this.zhenti_id = zhenti_id;
    }

    public static String getColumnnameId() {
        return COLUMNNAME_ID;
    }

    public String getIs_done() {
        return is_done;
    }

    public void setIs_done(String is_done) {
        this.is_done = is_done;
    }

    public int getQid() {
        return qid;
    }

    public void setQid(int qid) {
        this.qid = qid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public static String getColumnnameChapter() {
        return COLUMNNAME_CHAPTER;
    }

    public String getZtid() {
        return ztid;
    }

    public void setZtid(String ztid) {
        this.ztid = ztid;
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

    public String getMetas() {
        return metas;
    }

    public void setMetas(String metas) {
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

    @Override
    public String toString() {
        return "ZhenTi{" +
                "id=" + id +
                ", ztid='" + ztid + '\'' +
                ", type='" + type + '\'' +
                ", course='" + course + '\'' +
                ", chapter='" + chapter + '\'' +
                ", stem='" + stem + '\'' +
                ", metas='" + metas + '\'' +
                ", answer='" + answer + '\'' +
                ", analysis='" + analysis + '\'' +
                ", shiti_type='" + shiti_type + '\'' +
                ", xueduan='" + xueduan + '\'' +
                ", nianfen='" + nianfen + '\'' +
                ", totle_post='" + totle_post + '\'' +
                ", totle_success='" + totle_success + '\'' +
                ", easy_error='" + easy_error + '\'' +
                ", score='" + score + '\'' +
                ", user_answer='" + user_answer + '\'' +
                ", kaodian_name='" + kaodian_name + '\'' +
                ", totle_user='" + totle_user + '\'' +
                ", success='" + success + '\'' +
                ", success='" + success + '\'' +
                ", is_done='" + is_done + '\'' +
                ", qid='" + qid + '\'' +
                // ", ztlistid=" + ztlistid + '\'' +
                ", tiku_id=" + tiku_id + '\'' +
                ", zhenti_id=" + zhenti_id +
                '}';
    }


}
