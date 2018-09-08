package com.jiaoshizige.teacherexam.zz.database;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * UserBean实体类，存储数据库中user表中的数据
 * <p>
 * 注解：
 * DatabaseTable：通过其中的tableName属性指定数据库名称
 * DatabaseField：代表数据表中的一个字段
 * ForeignCollectionField：一对多关联，表示一个UserBean关联着多个ArticleBean（必须使用ForeignCollection集合）
 * <p>
 * 属性：
 * id：当前字段是不是id字段（一个实体类中只能设置一个id字段）
 * columnName：表示当前属性在表中代表哪个字段
 * generatedId：设置属性值在数据表中的数据是否自增
 * useGetSet：是否使用Getter/Setter方法来访问这个字段
 * canBeNull：字段是否可以为空，默认值是true
 * unique：是否唯一
 * defaultValue：设置这个字段的默认值
 */
@DatabaseTable(tableName = "questionbanklist") // 指定数据表的名称
public class QuestionBankList {
    // 定义字段在数据库中的字段名
    public static final String COLUMNNAME_ZTID = "qbid";
    public static final String COLUMNNAME_ID = "id";
    public static final String COLUMNNAME_NAME = "name";
    public static final String COLUMNNAME_EXAMNAME = "exam_name";
    public static final String COLUMNNAME_TOTALNMU = "guanqia_totle_num";
    public static final String COLUMNNAME_TOTALPASSNUM = "guanqia_totle_pass_num";
    public static final String COLUMNNAME_HGTOTALNUM = "huangguan_totle_num";
    public static final String COLUMNNAME_HGHAVETOTALNUM = "huangguan_have_totle_num";
    public static final String COLUMNNAME_EXAMTYPEID = "examtype_id";
    public static final String COLUMNNAME_USERID = "user_id";

    @DatabaseField(generatedId = true, columnName = COLUMNNAME_ID, useGetSet = true)
    private int id;
    @DatabaseField(columnName = COLUMNNAME_ZTID, useGetSet = true)
    private String qbid;
    @DatabaseField(columnName = COLUMNNAME_NAME, useGetSet = true)
    private String name;
    @DatabaseField(columnName = COLUMNNAME_EXAMNAME, useGetSet = true, defaultValue = "0")
    private String exam_name;
    @DatabaseField(columnName = COLUMNNAME_TOTALNMU, useGetSet = true, defaultValue = "0")
    private String guanqia_totle_num;
    @DatabaseField(columnName = COLUMNNAME_TOTALPASSNUM, useGetSet = true, defaultValue = "0")
    private String guanqia_totle_pass_num;
    @DatabaseField(columnName = COLUMNNAME_HGTOTALNUM, useGetSet = true, defaultValue = "0")
    private String huangguan_totle_num;
    @DatabaseField(columnName = COLUMNNAME_HGHAVETOTALNUM, useGetSet = true, defaultValue = "0")
    private String huangguan_have_totle_num;
    @DatabaseField(columnName = COLUMNNAME_EXAMTYPEID, useGetSet = true, defaultValue = "0")
    private String examtype_id;
    @DatabaseField(columnName = COLUMNNAME_USERID, useGetSet = true, defaultValue = "0")
    private String user_id;


    public QuestionBankList() {
    }

    public QuestionBankList(String qbid, String name, String exam_name, String guanqia_totle_num, String guanqia_totle_pass_num, String huangguan_totle_num, String huangguan_have_totle_num, String examtype_id,String user_id) {
        this.qbid = qbid;
        this.name = name;
        this.exam_name = exam_name;
        this.guanqia_totle_num = guanqia_totle_num;
        this.guanqia_totle_pass_num = guanqia_totle_pass_num;
        this.huangguan_totle_num = huangguan_totle_num;
        this.huangguan_have_totle_num = huangguan_have_totle_num;
        this.examtype_id = examtype_id;
        this.user_id = user_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getExamtype_id() {
        return examtype_id;
    }

    public void setExamtype_id(String examtype_id) {
        this.examtype_id = examtype_id;
    }

    public static String getColumnnameZtid() {
        return COLUMNNAME_ZTID;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getQbid() {
        return qbid;
    }

    public void setQbid(String qbid) {
        this.qbid = qbid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getExam_name() {
        return exam_name;
    }

    public void setExam_name(String exam_name) {
        this.exam_name = exam_name;
    }

    public String getGuanqia_totle_num() {
        return guanqia_totle_num;
    }

    public void setGuanqia_totle_num(String guanqia_totle_num) {
        this.guanqia_totle_num = guanqia_totle_num;
    }

    public String getGuanqia_totle_pass_num() {
        return guanqia_totle_pass_num;
    }

    public void setGuanqia_totle_pass_num(String guanqia_totle_pass_num) {
        this.guanqia_totle_pass_num = guanqia_totle_pass_num;
    }

    public String getHuangguan_totle_num() {
        return huangguan_totle_num;
    }

    public void setHuangguan_totle_num(String huangguan_totle_num) {
        this.huangguan_totle_num = huangguan_totle_num;
    }

    public String getHuangguan_have_totle_num() {
        return huangguan_have_totle_num;
    }

    public void setHuangguan_have_totle_num(String huangguan_have_totle_num) {
        this.huangguan_have_totle_num = huangguan_have_totle_num;
    }


    @Override
    public String toString() {
        return "QuestionBankList{" +
                "id=" + id +
                ", qbid=" + qbid + '\'' +
                ", name='" + name + '\'' +
                ", exam_name='" + exam_name + '\'' +
                ", guanqia_totle_num=" + guanqia_totle_num + '\'' +
                ", guanqia_totle_pass_num=" + guanqia_totle_pass_num +
                ", huangguan_totle_num=" + huangguan_totle_num + '\'' +
                ", huangguan_have_totle_num=" + huangguan_have_totle_num + '\'' +
                ", examtype_id=" + examtype_id + '\'' +
                ", user_id=" + user_id +
                '}';
    }
}