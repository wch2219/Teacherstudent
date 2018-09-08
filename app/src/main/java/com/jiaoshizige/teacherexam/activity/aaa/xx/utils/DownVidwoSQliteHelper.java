package com.jiaoshizige.teacherexam.activity.aaa.xx.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Administrator on 2018/9/7.
 */

public class DownVidwoSQliteHelper extends SQLiteOpenHelper {
    private static DownVidwoSQliteHelper sqLiteHelper;
    private static final String DATABASENAME = "downvideo.db";
    private static final int VERSION = 1;
    public static DownVidwoSQliteHelper getInstance(Context context) {
        if (sqLiteHelper == null) {
            synchronized (DownVidwoSQliteHelper.class) {
                if (sqLiteHelper == null)
                    sqLiteHelper = new DownVidwoSQliteHelper(context.getApplicationContext(), DATABASENAME, null, VERSION);
            }
        }
        return sqLiteHelper;
    }
    public DownVidwoSQliteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //创建课程
        db.execSQL(
                "create table if not exists curriculum(id int,course_id int,covermap varchar(20),title varchar(20),primary key (id)) ");
        //小章节
        db.execSQL(
                "create table if not exists chapter(id int,course_id int,chapter_id int,title vachar(20),primary key(id))"
        );
        //下载详情
        db.execSQL(
                "create table if not exists vodeodesc(id int,course_id int,chapter_id int,name vachar(20),progrress int,filesize int,bitrate int,duration int,dowtype int)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists curriculum");
        db.execSQL("drop table if exists chapter");
        db.execSQL("drop table if exists vodeodesc");
        Log.e("KK",oldVersion+"FFF"+newVersion);
        onCreate(db);
    }
}
