package com.jiaoshizige.teacherexam.palyer;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.jiaoshizige.teacherexam.activity.aaa.xx.bean.ChapterBean;
import com.jiaoshizige.teacherexam.activity.aaa.xx.bean.DownManagerBean;
import com.jiaoshizige.teacherexam.activity.aaa.xx.utils.WcHLogUtils;
import com.jiaoshizige.teacherexam.model.RecordModel;
import com.jiaoshizige.teacherexam.utils.ToolUtils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class PolyvDownloadSQLiteHelper extends SQLiteOpenHelper {
    private static PolyvDownloadSQLiteHelper sqLiteHelper;
    private static final String DATABASENAME = "downloadlist.db";
    private static final int VERSION = 1;

    public static PolyvDownloadSQLiteHelper getInstance(Context context) {
        if (sqLiteHelper == null) {
            synchronized (PolyvDownloadSQLiteHelper.class) {
                if (sqLiteHelper == null)
                    sqLiteHelper = new PolyvDownloadSQLiteHelper(context.getApplicationContext(), DATABASENAME, null, VERSION);
            }
        }
        return sqLiteHelper;
    }

    private PolyvDownloadSQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "create table if not exists downloadlist(vid varchar(20),title varchar(100),duration varchar(20),filesize int,bitrate int,percent int default 0,total int default 0,chapter varchar(100),chapter_id varchar(10),section_id varchar(10),section_name varchar(10),learn_time varchar(100),progress int default 0,parent_id int,classname varchar(100),type varchar(10),course_id int,sid int,downspend int,primary key (vid, bitrate))");

        db.execSQL(
                "create table if not exists downmanagerlist(id int,course_id int,covermap varchar(20),title varchar(20),primary key (id)) ");
        //小章节
        db.execSQL(
                "create table if not exists chapter(id int,course_id int,chapter_id int,chaptertitle vachar(20),primary key(id))"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("drop table if exists downloadlist");
        /**
         * 数据库升级  添加字段操作  需要在downloadinfo中添加相应字段的get  set方法  在insert 中插入
         */
//        db.execSQL("alter table downloadlist add io int default 1");
        Log.e("KK",oldVersion+"FFF"+newVersion);
        onCreate(db);
    }

    /**
     * 添加下载信息至数据库
     *
     * @param info
     */
    public void insert(PolyvDownloadInfo info) {
        SQLiteDatabase db = getWritableDatabase();
        String sql = "insert into downloadlist(vid,title,duration,filesize,bitrate,progress,chapter,chapter_id,section_id,section_name,learn_time,classname,type,parent_id,course_id,sid) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        db.execSQL(sql, new Object[]{info.getVid(), info.getTitle(), info.getDuration(),
                info.getFilesize(), info.getBitrate(), info.getProgress(), info.getChapter(),info.getChapter_id(),info.getSection_id(),info.getSection_name(),info.getLearn_time(), info.getClassname(),info.getType(),info.getParent_id(), info.getCourse_id(), info.getSid()});

    }

    /**
     * 插入当前的章节
     * @return
     */
    public void inserChapter (ChapterBean chapterBean){
        boolean b = checkChapter(chapterBean);
        if (!b) {
            SQLiteDatabase db = getWritableDatabase();
            String sql = "insert into chapter(course_id,chapter_id,chaptertitle) values(?,?,?)";
            db.execSQL(sql,new Object[]{chapterBean.getCourse_id(),chapterBean.getChapter_id(),chapterBean.getChapter()});
            WcHLogUtils.I("插入成功");
        }
    }

    /**
     * 减捡测当前章节是否存在
     */
    private boolean checkChapter(ChapterBean chapterBean) {
        SQLiteDatabase db = getWritableDatabase();
        boolean b = false;
        String sql = "select * from chapter where course_id=? and chapter_id = ?";
        Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(chapterBean.getCourse_id()), String.valueOf(chapterBean.getChapter_id())});
        if (cursor.moveToNext()) {
            return true;
        }
        if (cursor != null)
            cursor.close();
        return false;
    }

    public void insertMannger(DownManagerBean managerBean){
        boolean b = checkMannger(managerBean.getCourse_id());
        if (!b) {
            SQLiteDatabase db = getWritableDatabase();
            WcHLogUtils.I("插入");
            String sql = "insert into downmanagerlist(course_id,covermap,title) values(?,?,?)";
            db.execSQL(sql,new Object[]{managerBean.getCourse_id(),managerBean.getCovermap(),managerBean.getTitle()});
        }
    }
    public List<DownManagerBean> getAllData(){
        SQLiteDatabase db = getWritableDatabase();
        String sql = "SELECT * FROM downmanagerlist GROUP BY course_id";
//        Cursor cursor = db.rawQuery(sql, null);
        Cursor cursor = db.query("downmanagerlist",null,null,null,null,null,null);
        List<DownManagerBean> managerBeans = new ArrayList<>();
        while (cursor.moveToNext()) {
            DownManagerBean managerBean = new DownManagerBean();
            String covermap = cursor.getString(cursor.getColumnIndex("covermap"));
            int course_id = cursor.getInt(cursor.getColumnIndex("course_id"));
            String title = cursor.getString(cursor.getColumnIndex("title"));
            List<PolyvDownloadInfo> all = getAll(course_id,-1);
            managerBean.setCovermap(covermap);
            managerBean.setCourse_id(course_id);
            managerBean.setDownloadInfos(all);
            managerBean.setTitle(title);
            managerBeans.add(managerBean);
            getAllChapter(course_id);
        }
        if (cursor != null)
            cursor.close();
        return managerBeans;
    }
    public List<ChapterBean> getAllChapter(int course){
        SQLiteDatabase db = getWritableDatabase();
        String sql = "select * from chapter where course_id=?";
        Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(course)});
        List<ChapterBean> chapterBeanist = new ArrayList<>();
        while (cursor.moveToNext()){
            ChapterBean chapterBean = new ChapterBean();
            int chapter_id = cursor.getInt(cursor.getColumnIndex("chapter_id"));
            String chapter = cursor.getString(cursor.getColumnIndex("chaptertitle"));
            List<PolyvDownloadInfo> all = getAll(course,chapter_id);
            chapterBean.setChapter(chapter);
            chapterBean.setChapter_id(chapter_id);
            chapterBean.setInfos(all);
            chapterBeanist.add(chapterBean);
        }
        WcHLogUtils.I(chapterBeanist.size()+"章节数量");
        return chapterBeanist;
    }



    /**
     * 删除
     * @param course_id
     */
    public void deleteManager(int course_id){
        SQLiteDatabase db = getWritableDatabase();
        String sql = "delete from downmanagerlist where course_id = ?";
        db.execSQL(sql,new Object[]{course_id});
        String sql1 = "delete from downloadlist where course_id=?";
        db.execSQL(sql1,new Object[]{course_id});
        String sql2 = "delete from chapter where course_id=?";
        db.execSQL(sql2,new Object[]{course_id});
    }


    /**
     * 检测当前id是否存在
     * @param course_id
     * @return
     */
    public boolean checkMannger(int course_id){
        SQLiteDatabase db = getReadableDatabase();
        String sql = "select * from downmanagerlist where course_id =?";
        Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(course_id)});
        while (cursor.moveToNext()){
            WcHLogUtils.I("存在");
            return true;
        }
        if (cursor != null)
            cursor.close();
        return false;
    }
    /**
     * 判断 当前
     */
    /**
     * 移除下载信息从数据库中
     *
     * @param info
     */
    public void delete(PolyvDownloadInfo info) {
        SQLiteDatabase db = getWritableDatabase();
        String sql = "delete * from downloadlist where vid=? and bitrate=?";
        db.execSQL(sql, new Object[]{info.getVid(), info.getBitrate()});
    }

    /**
     * 清空数据库中所有数据
     */
    public void deleteAll() {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("delete from downloadlist");
        db.execSQL("delete from downmanagerlist");
        db.close();
    }
    /**
     * 根据vid删除数据
     */
    public void deletecontent(String vid){
        SQLiteDatabase db = getWritableDatabase();
        String sql = "delete from downloadlist where vid=?";
        db.execSQL(sql, new Object[]{vid});
        db.close();
//        db.execSQL("delete from person where name=? ",new Object[]{vid});
    }

    /**
     * 更新下载的进度至数据库
     *
     * @param info
     * @param percent
     * @param total
     */
    public void update(PolyvDownloadInfo info, long percent, long total, int progress) {
        SQLiteDatabase db = getWritableDatabase();
        String sql = "update downloadlist set percent=?,total=?,progress=? where vid=? and bitrate=?";
        db.execSQL(sql, new Object[]{percent, total, progress, info.getVid(), info.getBitrate()});
    }
    public void updateProgress( long percent, long total, int progress, String vid) {
        SQLiteDatabase db = getWritableDatabase();
        String sql = "update downloadlist set percent=?,total=?,progress=? where vid=?";
        db.execSQL(sql, new Object[]{percent,total,progress,vid});
    }

    /**
     * 更新下载进度
     * @param span
     * @param vid
     * @param bitrate
     */
    public void upDownSpan (int span,String vid,int bitrate){
        SQLiteDatabase db = getWritableDatabase();
        String sql = "update downloadlist set downspend=? where vid=? and bitrate=?";
        db.execSQL(sql,new Object[]{span,vid,bitrate});
    }

    public PolyvDownloadInfo getDownSpan(String vid, int bitrate){
        SQLiteDatabase db = getWritableDatabase();
        String sql = "select * from downloadlist where vid=? and bitrate=?";
        Cursor cursor = db.rawQuery(sql, new String[]{vid, String.valueOf(bitrate)});
        PolyvDownloadInfo downIngData = new PolyvDownloadInfo();
        while (cursor.moveToNext()){
            String title = cursor.getString(cursor.getColumnIndex("title"));
            String duration = cursor.getString(cursor.getColumnIndex("duration"));
            long filesize = cursor.getInt(cursor.getColumnIndex("filesize"));
            long percent = cursor.getInt(cursor.getColumnIndex("percent"));
            long total = cursor.getInt(cursor.getColumnIndex("total"));
            int progress = cursor.getInt(cursor.getColumnIndex("progress"));
            String chapter = cursor.getString(cursor.getColumnIndex("chapter"));
            int parent_id = cursor.getInt(cursor.getColumnIndex("parent_id"));
            int course_id = cursor.getInt(cursor.getColumnIndex("course_id"));
            String classname = cursor.getString(cursor.getColumnIndex("classname"));
            String chapter_id = cursor.getString(cursor.getColumnIndex("chapter_id"));
            String section_id = cursor.getString(cursor.getColumnIndex("section_id"));
            String section_name = cursor.getString(cursor.getColumnIndex("section_name"));
            String learn_time = cursor.getString(cursor.getColumnIndex("learn_time"));
            String type = cursor.getString(cursor.getColumnIndex("type"));
//                int class_id = cursor.getInt(cursor.getColumnIndex("class_id"));
            int sid = cursor.getInt(cursor.getColumnIndex("sid"));
            int downspend = cursor.getInt(cursor.getColumnIndex("downspend"));
            PolyvDownloadInfo info = new PolyvDownloadInfo(vid, duration, filesize, bitrate, title, progress, chapter);
            info.setPercent(percent);
            info.setTotal(total);
            info.setCourse_id(course_id);
            info.setParent_id(parent_id);
            info.setSid(sid);
            info.setClassname(classname);
            info.setChapter_id(chapter_id);
            info.setSection_id(section_id);
            info.setSection_name(section_name);
            info.setLearn_time(learn_time);
            info.setType(type);
            info.setDownspend(downspend);
            downIngData = info;
        }
        return downIngData;
    }
    /**
     * 判断该下载信息是否已以添加过
     *
     * @param info
     * @return
     */
    public boolean isAdd(PolyvDownloadInfo info) {
        SQLiteDatabase db = getWritableDatabase();
        String sql = "select vid ,duration,filesize,bitrate from downloadlist where vid=? and bitrate=?";
        Cursor cursor = null;
        try {
            cursor = db.rawQuery(sql, new String[]{info.getVid(), info.getBitrate() + ""});
            return cursor.getCount() == 1 ? true : false;
        } finally {
            if (cursor != null)
                cursor.close();
        }
    }

    /**
     * 判断是否下载
     */
    public boolean isAddVid(String vid) {
        SQLiteDatabase db = getWritableDatabase();
        String sql = "select vid from downloadlist where vid=?";
        Cursor cursor = null;
        try {
            cursor = db.rawQuery(sql, new String[]{vid});
            return cursor.getCount() == 1 ? true : false;
        } finally {
            if (cursor != null)
                cursor.close();
        }
    }

    /**
     * 根据vid获取数据
     */
    public PolyvDownloadInfo getInfo(String id) {
        SQLiteDatabase db = getWritableDatabase();
        PolyvDownloadInfo info = null;
        String sql = "select * from downloadlist where vid=?";
        Cursor cursor = null;
        try {
            cursor = db.rawQuery(sql, new String[]{id});


            while (cursor.moveToNext()) {
                String vid = cursor.getString(cursor.getColumnIndex("vid"));
                String title = cursor.getString(cursor.getColumnIndex("title"));
                String duration = cursor.getString(cursor.getColumnIndex("duration"));
                long filesize = cursor.getInt(cursor.getColumnIndex("filesize"));
                int bitrate = cursor.getInt(cursor.getColumnIndex("bitrate"));
                long percent = cursor.getInt(cursor.getColumnIndex("percent"));
                long total = cursor.getInt(cursor.getColumnIndex("total"));
                int progress = cursor.getInt(cursor.getColumnIndex("progress"));
                String chapter = cursor.getString(cursor.getColumnIndex("chapter"));
                int parent_id = cursor.getInt(cursor.getColumnIndex("parent_id"));
                int course_id = cursor.getInt(cursor.getColumnIndex("course_id"));
                String classname = cursor.getString(cursor.getColumnIndex("classname"));
                String chapter_id = cursor.getString(cursor.getColumnIndex("chapter_id"));
                String section_id = cursor.getString(cursor.getColumnIndex("section_id"));
                String section_name = cursor.getString(cursor.getColumnIndex("section_name"));
                String learn_time = cursor.getString(cursor.getColumnIndex("learn_time"));
                int downspend = cursor.getInt(cursor.getColumnIndex("downspend"));
                String type = cursor.getString(cursor.getColumnIndex("type"));
//                int class_id = cursor.getInt(cursor.getColumnIndex("class_id"));
                int sid = cursor.getInt(cursor.getColumnIndex("sid"));
                DownManagerBean managerBean = new DownManagerBean();
                managerBean.setCourse_id(course_id);
                managerBean.setCovermap(chapter);
                info = new PolyvDownloadInfo(vid, duration, filesize, bitrate, title, progress, chapter);
                info.setPercent(percent);
                info.setTotal(total);
                info.setCourse_id(course_id);
                info.setParent_id(parent_id);
                info.setSid(sid);
                info.setClassname(classname);
                info.setChapter_id(chapter_id);
                info.setSection_id(section_id);
                info.setSection_name(section_name);
                info.setLearn_time(learn_time);
                info.setType(type);
                info.setDownspend(downspend);
//                info.setClass_id(class_id);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return info;
    }

    /**
     * 获取所有的下载信息对象
     *
     * @return
     */
    public LinkedList<PolyvDownloadInfo> getAll(int course,int chapt_id) {
        LinkedList<PolyvDownloadInfo> infos = new LinkedList<PolyvDownloadInfo>();
        SQLiteDatabase db = getWritableDatabase();
        String sqlcourse_id;
        Cursor cursor = null;
        if (chapt_id == -1) {
             sqlcourse_id = "SELECT * FROM downloadlist where course_id=?";//
            cursor = db.rawQuery(sqlcourse_id, new String[]{String.valueOf(course)});//
        }else{
            sqlcourse_id = "SELECT * FROM downloadlist where course_id=? and chapter_id = ?";//
            cursor = db.rawQuery(sqlcourse_id, new String[]{String.valueOf(course), String.valueOf(chapt_id)});//
        }

        String sql = "select vid,title,duration,filesize,bitrate,percent,total,progress,chapter,chapter_id,section_id,learn_time,section_name,classname,type,parent_id,course_id,sid from downloadlist";

        try {

            while (cursor.moveToNext()) {
                String vid = cursor.getString(cursor.getColumnIndex("vid"));
                String title = cursor.getString(cursor.getColumnIndex("title"));
                String duration = cursor.getString(cursor.getColumnIndex("duration"));
                long filesize = cursor.getInt(cursor.getColumnIndex("filesize"));
                int bitrate = cursor.getInt(cursor.getColumnIndex("bitrate"));
                long percent = cursor.getInt(cursor.getColumnIndex("percent"));
                long total = cursor.getInt(cursor.getColumnIndex("total"));
                int progress = cursor.getInt(cursor.getColumnIndex("progress"));
                String chapter = cursor.getString(cursor.getColumnIndex("chapter"));
                int parent_id = cursor.getInt(cursor.getColumnIndex("parent_id"));
                int course_id = cursor.getInt(cursor.getColumnIndex("course_id"));
                String classname = cursor.getString(cursor.getColumnIndex("classname"));
                String chapter_id = cursor.getString(cursor.getColumnIndex("chapter_id"));
                String section_id = cursor.getString(cursor.getColumnIndex("section_id"));
                String section_name = cursor.getString(cursor.getColumnIndex("section_name"));
                String learn_time = cursor.getString(cursor.getColumnIndex("learn_time"));
                String type = cursor.getString(cursor.getColumnIndex("type"));
//                int class_id = cursor.getInt(cursor.getColumnIndex("class_id"));
                int sid = cursor.getInt(cursor.getColumnIndex("sid"));
                int downspend = cursor.getInt(cursor.getColumnIndex("downspend"));
                PolyvDownloadInfo info = new PolyvDownloadInfo(vid, duration, filesize, bitrate, title, progress, chapter);
                info.setPercent(percent);
                info.setTotal(total);
                info.setCourse_id(course_id);
                info.setParent_id(parent_id);
                info.setSid(sid);
                info.setClassname(classname);
                info.setChapter_id(chapter_id);
                info.setSection_id(section_id);
                info.setSection_name(section_name);
                info.setLearn_time(learn_time);
                info.setType(type);
                info.setDownspend(downspend);
//                info.setClass_id(class_id);
                infos.addLast(info);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return infos;
    }

    /**
     * 获取course_id
     *
     */
    public LinkedList<RecordModel> getModel() {
        LinkedList<RecordModel> models = new LinkedList<>();
        LinkedList<PolyvDownloadInfo> infos = new LinkedList<PolyvDownloadInfo>();
        LinkedList<PolyvDownloadInfo> infos1 = new LinkedList<PolyvDownloadInfo>();
        LinkedList<PolyvDownloadInfo> infoListSame = new LinkedList<PolyvDownloadInfo>();
        LinkedList<PolyvDownloadInfo> infoListSameui = null;
        LinkedList<LinkedList<PolyvDownloadInfo>> classifyList = new LinkedList<>();
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = null;
        String sqlcourse_id = "SELECT * FROM downloadlist GROUP BY course_id";
        try {
            cursor = db.rawQuery(sqlcourse_id, null);
            while (cursor.moveToNext()) {
                String vid = cursor.getString(cursor.getColumnIndex("vid"));
                String title = cursor.getString(cursor.getColumnIndex("title"));
                String duration = cursor.getString(cursor.getColumnIndex("duration"));
                long filesize = cursor.getInt(cursor.getColumnIndex("filesize"));
                int bitrate = cursor.getInt(cursor.getColumnIndex("bitrate"));
                long percent = cursor.getInt(cursor.getColumnIndex("percent"));
                long total = cursor.getInt(cursor.getColumnIndex("total"));
                int progress = cursor.getInt(cursor.getColumnIndex("progress"));
                String chapter = cursor.getString(cursor.getColumnIndex("chapter"));
                int parent_id = cursor.getInt(cursor.getColumnIndex("parent_id"));
                int course_id = cursor.getInt(cursor.getColumnIndex("course_id"));
                int sid = cursor.getInt(cursor.getColumnIndex("sid"));
                String classname = cursor.getString(cursor.getColumnIndex("classname"));
                String chapter_id = cursor.getString(cursor.getColumnIndex("chapter_id"));
                String section_id = cursor.getString(cursor.getColumnIndex("section_id"));
                String section_name = cursor.getString(cursor.getColumnIndex("section_name"));
                String learn_time = cursor.getString(cursor.getColumnIndex("learn_time"));
                String type = cursor.getString(cursor.getColumnIndex("type"));
//                int class_id = cursor.getInt(cursor.getColumnIndex("class_id"));
                PolyvDownloadInfo info = new PolyvDownloadInfo(vid, duration, filesize, bitrate, title,progress,chapter);
                info.setPercent(percent);
                info.setTotal(total);
                info.setCourse_id(course_id);
                info.setParent_id(parent_id);
                info.setSid(sid);
                info.setClassname(classname);
                info.setChapter_id(chapter_id);
                info.setSection_id(section_id);
                info.setSection_name(section_name);
                info.setLearn_time(learn_time);
                info.setType(type);
//                info.setClass_id(class_id);
                infos.addLast(info);
            }
        }finally {
            if (cursor != null)
                cursor.close();
        }
        String[] mCourseId = new String[infos.size()];
//        String[] mChapter = new String[infos.size()];
        String[] mClassName = new String[infos.size()];
        for(int i = 0;i< infos.size();i++){
            mCourseId[i] = String.valueOf(infos.get(i).getCourse_id());
//            mChapter[i] = String.valueOf(infos.get(i).getChapter());
            mClassName[i] = String.valueOf(infos.get(i).getClassname());
        }
        String sqlBycourse_id = "SELECT * FROM downloadlist WHERE course_id = ? and progress = ?";
//        String sql = "select vid,title,duration,filesize,bitrate,percent,total,progress,chapter,chapter_id,section_id,learn_time,section_name,parent_id,course_id,sid from downloadlist";
        for(int j = 0;j <mCourseId.length;j++){
            String courseId = "-1";
            infoListSameui = new LinkedList<PolyvDownloadInfo>();
            try {
                Log.e("TAGdddss",mCourseId[j]+"");
                cursor = db.rawQuery(sqlBycourse_id,new String[]{mCourseId[j],"100"});
                while (cursor.moveToNext()) {
                    String vid = cursor.getString(cursor.getColumnIndex("vid"));
                    String title = cursor.getString(cursor.getColumnIndex("title"));
                    String duration = cursor.getString(cursor.getColumnIndex("duration"));
                    long filesize = cursor.getInt(cursor.getColumnIndex("filesize"));
                    int bitrate = cursor.getInt(cursor.getColumnIndex("bitrate"));
                    long percent = cursor.getInt(cursor.getColumnIndex("percent"));
                    long total = cursor.getInt(cursor.getColumnIndex("total"));
                    int progress = cursor.getInt(cursor.getColumnIndex("progress"));
                    String chapter = cursor.getString(cursor.getColumnIndex("chapter"));
                    int parent_id = cursor.getInt(cursor.getColumnIndex("parent_id"));
                    int course_id = cursor.getInt(cursor.getColumnIndex("course_id"));
                    int sid = cursor.getInt(cursor.getColumnIndex("sid"));
                    String classname = cursor.getString(cursor.getColumnIndex("classname"));
                    String chapter_id = cursor.getString(cursor.getColumnIndex("chapter_id"));
                    String section_id = cursor.getString(cursor.getColumnIndex("section_id"));
                    String section_name = cursor.getString(cursor.getColumnIndex("section_name"));
                    String learn_time = cursor.getString(cursor.getColumnIndex("learn_time"));
                    String type = cursor.getString(cursor.getColumnIndex("type"));
//                    int class_id = cursor.getInt(cursor.getColumnIndex("class_id"));
                    PolyvDownloadInfo info = new PolyvDownloadInfo(vid, duration, filesize, bitrate, title,progress,chapter);
                    info.setPercent(percent);
                    info.setTotal(total);
                    info.setCourse_id(course_id);
                    info.setParent_id(parent_id);
                    info.setSid(sid);
                    info.setClassname(classname);
                    info.setChapter_id(chapter_id);
                    info.setSection_id(section_id);
                    info.setSection_name(section_name);
                    info.setLearn_time(learn_time);
                    info.setType(type);
//                    info.setClass_id(class_id);
                    infoListSame.addLast(info);
                    infoListSameui.add(info);
                }
                classifyList.add(infoListSameui);
            }finally {
                if (cursor != null)
                    cursor.close();
            }
        }
        for (int i = 0;i <mCourseId.length;i++){
            LinkedList<PolyvDownloadInfo> infos2 = new LinkedList<PolyvDownloadInfo>();
            for(int j = 0;j <classifyList.get(i).size();j++){
                Log.e("Iooooo","opop"+classifyList.get(i).get(j).getSection_name()+classifyList.get(i).get(j).getChapter());
                if(Integer.valueOf(mCourseId[i]) == classifyList.get(i).get(j).getCourse_id()){
                    infos2.addLast(classifyList.get(i).get(j));
                }
            }
            if(infos2 != null && infos2.size() > 0){
            RecordModel model = new RecordModel();
            model.setChapter(mClassName[i]);
//                model.setChapter(mChapter[i]);
            model.setmPolyvDownloadInfo(infos2);
            models.addLast(model);
            }
        }
        Log.e("TAGsize",models.size()+"");
        for(int i=0;i< models.size();i++){
            for (int j = 0;j < models.get(i).getmPolyvDownloadInfo().size();j++){
                Log.e("TAGS",models.get(i).getmPolyvDownloadInfo().size()+""+models.get(i).getChapter()+models.get(i).getmPolyvDownloadInfo().get(j).getSection_name());
            }

        }
        return models;
    }
    /**
     *获取章节 单位
     */
    public LinkedList<RecordModel> getChapterModel(){
        LinkedList<RecordModel> models = new LinkedList<>();
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = null;
        String sqlcourse_id = "SELECT * FROM downloadlist GROUP BY chapter_id";
        LinkedList<PolyvDownloadInfo> infos = new LinkedList<PolyvDownloadInfo>();
        LinkedList<PolyvDownloadInfo> infoListSameui = null;
        LinkedList<LinkedList<PolyvDownloadInfo>> classifyList = new LinkedList<>();

        try {
            cursor = db.rawQuery(sqlcourse_id, null);
            while (cursor.moveToNext()) {
                String vid = cursor.getString(cursor.getColumnIndex("vid"));
                String title = cursor.getString(cursor.getColumnIndex("title"));
                String duration = cursor.getString(cursor.getColumnIndex("duration"));
                long filesize = cursor.getInt(cursor.getColumnIndex("filesize"));
                int bitrate = cursor.getInt(cursor.getColumnIndex("bitrate"));
                long percent = cursor.getInt(cursor.getColumnIndex("percent"));
                long total = cursor.getInt(cursor.getColumnIndex("total"));
                int progress = cursor.getInt(cursor.getColumnIndex("progress"));
                String chapter = cursor.getString(cursor.getColumnIndex("chapter"));
                int parent_id = cursor.getInt(cursor.getColumnIndex("parent_id"));
                int course_id = cursor.getInt(cursor.getColumnIndex("course_id"));
                int sid = cursor.getInt(cursor.getColumnIndex("sid"));
                String classname = cursor.getString(cursor.getColumnIndex("classname"));
                String chapter_id = cursor.getString(cursor.getColumnIndex("chapter_id"));
                String section_id = cursor.getString(cursor.getColumnIndex("section_id"));
                String section_name = cursor.getString(cursor.getColumnIndex("section_name"));
                String learn_time = cursor.getString(cursor.getColumnIndex("learn_time"));
                String type = cursor.getString(cursor.getColumnIndex("type"));
                PolyvDownloadInfo info = new PolyvDownloadInfo(vid, duration, filesize, bitrate, title,progress,chapter);
                info.setPercent(percent);
                info.setTotal(total);
                info.setCourse_id(course_id);
                info.setParent_id(parent_id);
                info.setSid(sid);
                info.setClassname(classname);
                info.setChapter_id(chapter_id);
                info.setSection_id(section_id);
                info.setSection_name(section_name);
                info.setLearn_time(learn_time);
                info.setType(type);
                infos.addLast(info);
            }
        }finally {
            if (cursor != null)
                cursor.close();
        }
        String[] mChapter = new String[infos.size()];
        String[] mChapterId = new String[infos.size()];
        for(int i = 0;i< infos.size();i++){
            mChapter[i] = String.valueOf(infos.get(i).getChapter());
            mChapterId[i] = String.valueOf(infos.get(i).getChapter_id());
        }
        String sqlBChapterId = "SELECT * FROM downloadlist WHERE chapter_id = ? and progress = ?";
        for(int j = 0;j <mChapterId.length;j++){
            infoListSameui = new LinkedList<PolyvDownloadInfo>();
            try {
                cursor = db.rawQuery(sqlBChapterId,new String[]{mChapterId[j],"100"});
                while (cursor.moveToNext()) {
                    String vid = cursor.getString(cursor.getColumnIndex("vid"));
                    String title = cursor.getString(cursor.getColumnIndex("title"));
                    String duration = cursor.getString(cursor.getColumnIndex("duration"));
                    long filesize = cursor.getInt(cursor.getColumnIndex("filesize"));
                    int bitrate = cursor.getInt(cursor.getColumnIndex("bitrate"));
                    long percent = cursor.getInt(cursor.getColumnIndex("percent"));
                    long total = cursor.getInt(cursor.getColumnIndex("total"));
                    int progress = cursor.getInt(cursor.getColumnIndex("progress"));
                    String chapter = cursor.getString(cursor.getColumnIndex("chapter"));
                    int parent_id = cursor.getInt(cursor.getColumnIndex("parent_id"));
                    int course_id = cursor.getInt(cursor.getColumnIndex("course_id"));
                    int sid = cursor.getInt(cursor.getColumnIndex("sid"));
                    String classname = cursor.getString(cursor.getColumnIndex("classname"));
                    String chapter_id = cursor.getString(cursor.getColumnIndex("chapter_id"));
                    String section_id = cursor.getString(cursor.getColumnIndex("section_id"));
                    String section_name = cursor.getString(cursor.getColumnIndex("section_name"));
                    String learn_time = cursor.getString(cursor.getColumnIndex("learn_time"));
                    String type = cursor.getString(cursor.getColumnIndex("type"));
                    PolyvDownloadInfo info = new PolyvDownloadInfo(vid, duration, filesize, bitrate, title,progress,chapter);
                    info.setPercent(percent);
                    info.setTotal(total);
                    info.setCourse_id(course_id);
                    info.setParent_id(parent_id);
                    info.setSid(sid);
                    info.setClassname(classname);
                    info.setChapter_id(chapter_id);
                    info.setSection_id(section_id);
                    info.setSection_name(section_name);
                    info.setLearn_time(learn_time);
                    info.setType(type);
                    infoListSameui.add(info);
                }
                classifyList.add(infoListSameui);
            }finally {
                if (cursor != null)
                    cursor.close();
            }
        }
        for (int i = 0;i <mChapterId.length;i++){
            LinkedList<PolyvDownloadInfo> infos2 = new LinkedList<PolyvDownloadInfo>();
            for(int j = 0;j <classifyList.get(i).size();j++){
                if(Integer.valueOf(mChapterId[i]) == Integer.valueOf(classifyList.get(i).get(j).getChapter_id())){
                    infos2.addLast(classifyList.get(i).get(j));
                }
            }
            if(infos2 != null && infos2.size() > 0){
                RecordModel model = new RecordModel();
                model.setChapter(mChapter[i]);
//                Log.e("OPOPsss",infos2.get(i).getChapter()+"||||"+mChapter[i]);
                model.setmPolyvDownloadInfo(infos2);
                models.addLast(model);
            }
        }
        for(int i=0;i< models.size();i++){
            for (int j = 0;j < models.get(i).getmPolyvDownloadInfo().size();j++){
//                Log.e("TAGS",models.get(i).getmPolyvDownloadInfo().size()+""+models.get(i).getChapter()+models.get(i).getmPolyvDownloadInfo().get(j).getSection_name());
            }

        }
        return models;
    }



    /**
     * 下载总M数
     *
     */
    public String getTotal(){
        String total;
        float fileTotal = 0;
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = null;
        try{
            cursor = db.rawQuery("SELECT sum(filesize) FROM downloadlist WHERE progress=?", new String[]{"100"});
            while (cursor.moveToNext()) {
                fileTotal = cursor.getFloat(cursor.getColumnIndex("sum(filesize)"));
            }
        }finally {
            if (cursor != null)
                cursor.close();
        }
        total = ToolUtils.FormatTwoPoint(String.valueOf(fileTotal /1024 /1024));
        return total;
    }

    /**
     * 获取下载进度
     */
    public long getProgree(String vid){
        long mProgress = 0;
        SQLiteDatabase db = getWritableDatabase();
        String sql = "select progress from downloadlist where vid=?";
        Cursor cursor = null;
        try {
            cursor = db.rawQuery(sql, new String[]{vid});
            while (cursor.moveToNext()){
                mProgress = cursor.getInt(cursor.getColumnIndex("progress"));
            }
        }finally {
            if (cursor != null)
                cursor.close();
        }
            return mProgress;
    }
    public void charu(PolyvDownloadInfo info) {
        SQLiteDatabase db = getWritableDatabase();
        String sql = "insert into downloadlist(vid,title,duration,filesize,bitrate,progress,ckassname,type,chapter,parent_id,course_id,sid) values(?,?,?,?,?,?,?,?,?,?)";
        db.execSQL(sql, new Object[]{info.getVid(), info.getTitle(), info.getDuration(),
                info.getFilesize(), info.getBitrate(), info.getProgress(),info.getClassname(),info.getType(), info.getChapter(), info.getParent_id(), info.getCourse_id(), info.getSid()});
    }
}
