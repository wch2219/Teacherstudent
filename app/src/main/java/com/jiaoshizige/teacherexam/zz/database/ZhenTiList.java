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
@DatabaseTable(tableName = "zhentilist") // 指定数据表的名称
public class ZhenTiList {
    // 定义字段在数据库中的字段名
    public static final String COLUMNNAME_ZTID = "ztid";
    public static final String COLUMNNAME_ID = "id";
    public static final String COLUMNNAME_NAME = "name";
    public static final String COLUMNNAME_ISEXAM = "is_exam";
    public static final String COLUMNNAME_ZHENTIID = "zhenti_id";
    public static final String COLUMNNAME_DOWNLOAD = "is_download";
    public static final String COLUMNNAME_USERID = "userid";
    public static final String COLUMNNAME_TIKUID = "tiku_id";


    @DatabaseField(generatedId = true, columnName = COLUMNNAME_ID, useGetSet = true)
    private int id;
    @DatabaseField(columnName = COLUMNNAME_ZTID, useGetSet = true)
    private String ztid;
    @DatabaseField(columnName = COLUMNNAME_NAME, useGetSet = true)
    private String name;
    @DatabaseField(columnName = COLUMNNAME_ISEXAM, useGetSet = true, defaultValue = "0")
    private int is_exam;
    @DatabaseField(columnName = COLUMNNAME_ZHENTIID, useGetSet = true)
    private String zhenti_id;
    @DatabaseField(columnName = COLUMNNAME_DOWNLOAD, useGetSet = true, defaultValue = "0")
    private int is_download;
    @DatabaseField(columnName = COLUMNNAME_USERID, useGetSet = true)
    private String userid;
    @DatabaseField(columnName = COLUMNNAME_TIKUID, useGetSet = true)
    private String tiku_id;
//    @ForeignCollectionField(eager = true)
//    private ForeignCollection<ZhenTi> articles;

    public ZhenTiList() {
    }

    public ZhenTiList(String ztid, String name, int is_exam, String zhenti_id, String userid, String tiku_id) {
        this.name = name;
        this.ztid = ztid;
        this.is_exam = is_exam;
        this.zhenti_id = zhenti_id;
        this.userid = userid;
        this.tiku_id = tiku_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getZtid() {
        return ztid;
    }

    public void setZtid(String ztid) {
        this.ztid = ztid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIs_exam() {
        return is_exam;
    }

    public void setIs_exam(int is_exam) {
        this.is_exam = is_exam;
    }

    public String getZhenti_id() {
        return zhenti_id;
    }

    public void setZhenti_id(String zhenti_id) {
        this.zhenti_id = zhenti_id;
    }

    public int getIs_download() {
        return is_download;
    }

    public void setIs_download(int is_download) {
        this.is_download = is_download;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getTiku_id() {
        return tiku_id;
    }

    public void setTiku_id(String tiku_id) {
        this.tiku_id = tiku_id;
    }

//    public ForeignCollection<ZhenTi> getArticles() {
//        return articles;
//    }
//
//    public void setArticles(ForeignCollection<ZhenTi> articles) {
//        this.articles = articles;
//    }

    @Override
    public String toString() {
        return "ZhenTiList{" +
                "id=" + id +
                ", ztid=" + ztid + '\'' +
                ", name='" + name + '\'' +
                ", is_exam=" + is_exam + '\'' +
                ", zhenti_id=" + zhenti_id + '\'' +
                ", is_download=" + is_download + '\'' +
                ", tiku_id=" + tiku_id + '\'' +
                ", userid=" + userid +
                '}';
    }
}