package com.jiaoshizige.teacherexam.activity.aaa.xx.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.Formatter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.easefun.polyvsdk.PolyvDownloader;
import com.easefun.polyvsdk.PolyvDownloaderErrorReason;
import com.easefun.polyvsdk.PolyvDownloaderManager;
import com.easefun.polyvsdk.download.listener.IPolyvDownloaderProgressListener;
import com.easefun.polyvsdk.download.listener.IPolyvDownloaderSpeedListener;
import com.easefun.polyvsdk.download.listener.IPolyvDownloaderStartListener;
import com.jiaoshizige.teacherexam.R;
import com.jiaoshizige.teacherexam.activity.LiveActivity;
import com.jiaoshizige.teacherexam.activity.RecordingActivity;
import com.jiaoshizige.teacherexam.activity.aaa.xx.bean.NewCourseListBean;
import com.jiaoshizige.teacherexam.http.ApiUrl;
import com.jiaoshizige.teacherexam.http.MyCallBack;
import com.jiaoshizige.teacherexam.http.Xutil;
import com.jiaoshizige.teacherexam.model.AddLearnRecordModel;
import com.jiaoshizige.teacherexam.model.OnLineResponse;
import com.jiaoshizige.teacherexam.palyer.PolyvDownloadInfo;
import com.jiaoshizige.teacherexam.palyer.PolyvDownloadSQLiteHelper;
import com.jiaoshizige.teacherexam.utils.Sphelper;
import com.jiaoshizige.teacherexam.utils.TimeUtils;
import com.jiaoshizige.teacherexam.utils.ToastUtil;
import com.jiaoshizige.teacherexam.utils.ToolUtils;
import com.jiaoshizige.teacherexam.widgets.PercentageBallView;
import com.zhy.adapter.recyclerview.CommonAdapter;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/8/30.
 */

public class SanJiDeLeiBiaoAdapter extends RecyclerView.Adapter<SanJiDeLeiBiaoAdapter.ViewHolder>{

    private Context ctx;
    private static PolyvDownloadSQLiteHelper mDownloadSQLiteHelper;
    //    private String vid = "1ff1d46899ce59bf78e818eb3e6de9e8_1";
    private String vid = "";
    private int bitrate = 1;//清晰度
    //    private int bitrate = 2;//清晰度 1标清 2 高清 3 超清
    private String state = "";
    private static final String DOWNLOADED = "已下载";
    private static final String DOWNLOADING = "正在下载";
    private static final String PAUSEED = "暂停下载";
    private static final String WAITED = "等待下载";
    private int progress = 0;
    private long time;
    AddLearnRecordModel model = new AddLearnRecordModel();
    private List<NewCourseListBean.DataBean.CourseListBean> course_list;
    private boolean isshowPross;
    private boolean isdown;
    private int mIsBuy;
    private long mTotal;
    private String mCourseId;//单个套餐的id
    private String mType;//1班级2课程
    private String mPushUrl;
    private String mToken;
    private String mChatRoom;
    PolyvDownloadInfo downloadInfo;
    private String name;
    long nowtime = 0;
    public SanJiDeLeiBiaoAdapter(Context ctx, List<NewCourseListBean.DataBean.CourseListBean> course_list, boolean isshowPross) {
        super();
        this.ctx = ctx;
        this.course_list = course_list;
        this.isshowPross = isshowPross;
    }
    public SanJiDeLeiBiaoAdapter(Context ctx, List<NewCourseListBean.DataBean.CourseListBean> course_list, boolean isshowPross, boolean isdown, String type, String course_id, int isBuy) {
        super();
        this.isdown = isdown;
        this.ctx = ctx;
        this.course_list = course_list;
        this.isshowPross = isshowPross;
        this.mDownloadSQLiteHelper = PolyvDownloadSQLiteHelper.getInstance(ctx);
        this.mIsBuy = isBuy;//每个mCourseList的isbuy
        this.mCourseId = course_id;//每个mCourseList的id
        this.mType = type;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(ctx).inflate(R.layout.item_sanjileibiao,parent,false);

        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        name = course_list.get(position).getName();
        holder.tv_title.setText(name);
        LinearLayoutManager manager =  new LinearLayoutManager(ctx);
        holder.rv_list.setLayoutManager(manager);
        List<NewCourseListBean.DataBean.CourseListBean.CatalogBean> catalog = course_list.get(position).getCatalog();
//        List<NewCourseListBean.DataBean.CourseListBean.CatalogBean> catalog = course_list.get(position).getCatalog();
//        holder.rv_list.setAdapter(new SanJiDeLeiBiaoSonAdapter(ctx,catalog));
        holder.rv_list.setAdapter(new CommonAdapter<NewCourseListBean.DataBean.CourseListBean.CatalogBean>(ctx, R.layout.item_catalog_content, catalog) {
            @Override
            protected void convert(final com.zhy.adapter.recyclerview.base.ViewHolder holder, final NewCourseListBean.DataBean.CourseListBean.CatalogBean lists, final int position) {
                holder.setText(R.id.content_title, (position + 1) + "." + lists.getTitle());
                PercentageBallView progressBall = (PercentageBallView) holder.getConvertView().findViewById(R.id.progress_ball);
//                PolyvDownloader downloader = PolyvDownloaderManager.getPolyvDownloader(lists.getVideo(), bitrate);
                if (lists != null) {
                    if (lists.getVideo() != null && !lists.getVideo().equals("")) {
                        vid = lists.getVideo();
                    }
                }
                final PolyvDownloader downloader = PolyvDownloaderManager.getPolyvDownloader(vid, bitrate);
//                TextView textView = (TextView) holder.getConvertView().findViewById(R.id.content_flag);
                if (mIsBuy == 1) {
//                    textView.setVisibility(View.VISIBLE);
                    progressBall.setVisibility(View.VISIBLE);
                    if (lists.getLearn_percent()==0) {
                        progressBall.setmWaterLevel(0, "");
                    } else {
                        progressBall.setmWaterLevel(lists.getLearn_percent(), "");
                    }
                } else {
//                    textView.setVisibility(View.GONE);
                    progressBall.setVisibility(View.INVISIBLE);
                }
                if (progress >= 100) {
                    //已经下载
                    state = DOWNLOADED;
                } else if (downloader.isDownloading()) {
                    //正在下载
                    state = DOWNLOADING;
                } else if (PolyvDownloaderManager.isWaitingDownload(vid, bitrate)) {
                    //等待下载
                    state = WAITED;
                } else {
                    //  暂停下载
                    state = PAUSEED;
                }


                switch (Integer.valueOf(lists.getType())) {//0章，1视频2试题3图文
                    case 0:
//                        holder.setText(R.id.content_type,"文档");
                        break;
                    case 1:
                        vid = lists.getVideo();//暂时注掉
                        if (mDownloadSQLiteHelper != null && mDownloadSQLiteHelper.isAddVid(vid)) {
                            progress = (int) mDownloadSQLiteHelper.getInfo(vid).getProgress();
                        } else {
                            progress = 0;
                        }
                        if (mIsBuy == 1) {
                            holder.setBackgroundRes(R.id.content_type, R.mipmap.video1_pre);
//                            holder.setBackgroundRes(R.id.content_flag,R.mipmap.down_pre);
                        } else {
                            holder.setBackgroundRes(R.id.content_type, R.mipmap.video1);
//                            holder.setBackgroundRes(R.id.content_flag, R.mipmap.down);
                            Log.e("TAG", "er");
                        }
                        holder.setText(R.id.tv_time,"");//视频时长
                        if (progress >= 100) {
                            //已经下载
                            state = DOWNLOADED;
//                            holder.setText(R.id.content_flag,progress+"%");
//                            holder.setBackgroundRes(R.id.content_flag, R.mipmap.down_pre);
//                            holder.setBackgroundRes(R.id.content_flag,R.mipmap.home_png_speed_default);
                        } else if (downloader.isDownloading()) {
                            //正在下载
                            state = DOWNLOADING;
//                            holder.setText(R.id.content_flag, progress + "%");
//                            holder.setBackgroundRes(R.id.content_flag, R.mipmap.home_png_speed_default);
                        } else if (PolyvDownloaderManager.isWaitingDownload(vid, bitrate)) {
                            //等待下载
                            state = WAITED;
//                            holder.setBackgroundRes(R.id.content_flag, R.mipmap.down);
                        } else if (progress == 0) {
                            state = WAITED;
//                            holder.setBackgroundRes(R.id.content_flag, R.mipmap.down);
                        } else if (progress > 0 && progress < 100) {
                            state = DOWNLOADING;
//                            holder.setText(R.id.content_flag, progress + "%");
//                            holder.setBackgroundRes(R.id.content_flag, R.mipmap.home_png_speed_default);
                        } else {
                            //  暂停下载
                            state = PAUSEED;
//                            holder.setBackgroundRes(R.id.content_flag, R.mipmap.pause);
                        }
                        holder.setText(R.id.tv_downType, state);

                        break;
                    case 2:
                        if (mIsBuy == 1) {
                            holder.setBackgroundRes(R.id.content_type, R.mipmap.exercise_pre);
//                            holder.setBackgroundRes(R.id.content_flag, R.mipmap.exercise_type_pre);
                        } else {
                            holder.setBackgroundRes(R.id.content_type, R.mipmap.exercise);
//                            holder.setBackgroundRes(R.id.content_flag, R.mipmap.exercise_type);
                        }
                        break;
                    case 3:
                        if (mIsBuy == 1) {
                            holder.setBackgroundRes(R.id.content_type, R.mipmap.image_text_pre);
//                            holder.setBackgroundRes(R.id.content_flag, R.mipmap.image_text_flag_pre);
                        } else {
                            holder.setBackgroundRes(R.id.content_type, R.mipmap.image_text);
//                            holder.setBackgroundRes(R.id.content_flag, R.mipmap.image_text_flag);
                        }
                        break;
                    case 4:
                        if (!lists.getLive_info().getVideo_url().equals("") && lists.getLive_info().getVideo_url() != null) {
                            if (mIsBuy == 1) {
                                holder.setBackgroundRes(R.id.content_type, R.mipmap.video1_pre);
//                                holder.setBackgroundRes(R.id.content_flag, R.mipmap.live_type_pre);
                            } else {
                                holder.setBackgroundRes(R.id.content_type, R.mipmap.video1);
//                                holder.setBackgroundRes(R.id.content_flag, R.mipmap.down);
                            }
                        } else {
                            if (mIsBuy == 1) {
                                holder.setBackgroundRes(R.id.content_type, R.mipmap.live_pre);
//                                holder.setBackgroundRes(R.id.content_flag, R.mipmap.live_type_pre);
                            } else {
                                holder.setBackgroundRes(R.id.content_type, R.mipmap.live);
//                                holder.setBackgroundRes(R.id.content_flag, R.mipmap.live_type);
                            }
                        }

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    time = TimeUtils.stringToLong(TimeUtils.getCurrentTime(), "yyyy-MM-dd HH:mm:ss");
//                                    Log.e("**************time", TimeUtils.getNetTime() + "//////" + TimeUtils.stringToLong(TimeUtils.getNetTime(), "yyyy-MM-dd HH:mm:ss") + "");
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();
                        break;
                }
                holder.setOnClickListener(R.id.course_item_ly, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                                ToastUtil.showShortToast("dianjitiaozhuan");
                        //todo
                        Intent intent = new Intent();
                        Log.e("OPOPswww", course_list.get(position).getId());
                        model.setGroup_id(course_list.get(position).getId());
                        if ("1".equals(mType)) {
                            model.setClass_id(Integer.valueOf(course_list.get(position).getId()));
                        } else {
                            model.setClass_id(Integer.valueOf(mCourseId));
                        }
                        model.setClassname(course_list.get(position).getName());
                        model.setChapter(lists.getTitle());
                        model.setChapter_id(String.valueOf(lists.getId()));
                        model.setCourse_name(course_list.get(position).getName());
                        model.setCourse_id(mCourseId);
                        model.setChapter_name(lists.getTitle());
                        model.setSection_id(String.valueOf(lists.getId()));
                        model.setSection_name(lists.getTitle());
                        model.setLearn_time(String.valueOf(lists.getLearn_time()));
                        model.setType(mType);
                        Log.e("**********isbuy", mIsBuy + "///" + model.getChapter_id() + model.getChapter() + model.getClass_id() + model.getCourse_id());
                        String vid;

                        Log.e("******getLive_info*", lists.getType() + "*********");
                        if (lists.getType()==4) {
                            //直播
                            vid = lists.getLive_info().getVideo_url();
                            if (mIsBuy == 1) {
                                if (!lists.getLive_info().getVideo_url().equals("") && lists.getLive_info().getVideo_url() != null) {
                                    intent = RecordingActivity.newIntent(ctx, RecordingActivity.PlayMode.portrait, vid, 1, true, true);
                                    intent.setClass(ctx, RecordingActivity.class);
                                    Log.e("000000", vid);
                                    intent.putExtra("live_url", vid);
                                    intent.putExtra("no_course_id", "1");
                                    intent.putExtra("model", model);
                                    intent.putExtra("flag", "1");//flag 为1 表示点击列表 为0表示点击下载进入
                                    ctx.startActivity(intent);
                                    //TODO
//                                    videoPlayListener.startPlay(vid);

                                } else {
//                                holder.setBackgroundRes(R.id.content_type, R.mipmap.live_pre);
//                                holder.setBackgroundRes(R.id.content_flag, R.mipmap.live_type_pre);
                                    try {
                                        Thread.sleep(1000);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    //1.未开始  2开始
                                    Log.e("***********bijioa", ToolUtils.getTime(time, lists.getLive_info().getStart_time()));
                                    if (lists.getLive_info().getPush_url() != null) {
//                                    if (ToolUtils.getTime(time, lists.getLive_info().getStart_time()).equals("2")){
//                                        mPushUrl=lists.getLive_info().getPush_url();
//                                        mToken=lists.getLive_info().getToken();
//                                        mChatRoom=lists.getLive_info().getChat_room_id();
//                                        onLine(lists.getLive_info().getApp_name());
//                                    }else {
//                                        ToastUtil.showShortToast("直播还未开始");
//                                    }

//                                        mPushUrl=lists.getLive_info().getPush_url();
//                                        mToken=lists.getLive_info().getToken();
//                                        mChatRoom=lists.getLive_info().getChat_room_id();
//                                        Log.e("***********mPushUrl", mPushUrl+"++++++++++++++");
//                                        onLine(lists.getLive_info().getApp_name());
                                    } else {
                                        ToastUtil.showShortToast("暂无直播");
                                    }

                                }
                            } else {
                                ToastUtil.showShortToast("请购买后观看");
                            }
                        } else if (lists.getType()==1) {
                            //视频
                            vid = lists.getVideo();
                            if (mIsBuy == 1) {
//                                if (!lists.getLive_info().getVideo_url().equals("") && lists.getLive_info().getVideo_url() != null) {
                                if (!vid.equals("") && vid != null) {
//                                    intent = RecordingActivity.newIntent(ctx, RecordingActivity.PlayMode.portrait, vid, 1, true, true);
//                                    intent.setClass(ctx, RecordingActivity.class);
//                                    Log.e("000000", vid);
//                                    intent.putExtra("live_url", vid);
//                                    intent.putExtra("no_course_id", "1");
//                                    intent.putExtra("model", model);
//                                    intent.putExtra("flag", "1");//flag 为1 表示点击列表 为0表示点击下载进入
//                                    ctx.startActivity(intent);
                                    if (videoPlayListener != null) {
                                        //返回播放
                                        videoPlayListener.startPlay(vid,lists.getTitle());
                                    }

                                } else {
//                                holder.setBackgroundRes(R.id.content_type, R.mipmap.live_pre);
//                                holder.setBackgroundRes(R.id.content_flag, R.mipmap.live_type_pre);
                                    try {
                                        Thread.sleep(1000);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    //1.未开始  2开始
                                    Log.e("***********bijioa", ToolUtils.getTime(time, lists.getLive_info().getStart_time()));
                                    if (lists.getLive_info().getPush_url() != null) {
                                        if (ToolUtils.getTime(time, lists.getLive_info().getStart_time()).equals("2")) {
                                            mPushUrl = lists.getLive_info().getPush_url();
                                            Log.e("***********mPushUrl", mPushUrl + "++++++++++++++");
                                            mToken = lists.getLive_info().getToken();
                                            mChatRoom = lists.getLive_info().getChat_room_id();
                                            onLine(lists.getLive_info().getApp_name());
                                        } else {
                                            ToastUtil.showShortToast("直播还未开始");
                                        }
                                        onLine(lists.getLive_info().getApp_name());
                                    } else {
                                        ToastUtil.showShortToast("暂无直播");
                                    }

                                }
                            } else {
                                ToastUtil.showShortToast("请购买后观看");
                            }
                        }

                    }
                });


//                holder.setOnClickListener(R.id.content_flag_ly, new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                      /*  if(mType.equals("1")){
//
//                        }else{
//                            model.setGroup_id(lists.getId());
//                        }*/
//
//                      //TODO
//                        model.setGroup_id(course_list.get(position).getId());
//                        if (mType.equals("1")) {
//                            model.setCourse_id(mCourseId);
//                        } else {
//                            model.setCourse_id(course_list.get(position).getId());
//                        }
//
//                        model.setCourse_name(course_list.get(position).getName());
//                        model.setChapter_id(String.valueOf(lists.getId()));
//                        model.setChapter_name(lists.getTitle());
//                        model.setSection_id(String.valueOf(lists.getId()));
//                        model.setSection_name(lists.getTitle());
//                        model.setLearn_time(String.valueOf(lists.getLearn_time()));
//                        model.setType(mType);
//                        if (mIsBuy == 1) {
//                            Log.e("********type", lists.getType()+"");
//                            //0章，1视频2试题3图文
//                            switch (Integer.valueOf(lists.getType())) {
//                                case 0:
////                        holder.setText(R.id.content_type,"文档");
////                                mDownloadSQLiteHelper.getModel();
//                                    break;
//                                case 1:
//                                    vid = lists.getVideo();
//                                    if (mIsBuy == 1) {
//                                        new Thread(new Runnable() {
//                                            public void run() {
//                                                try {
//                                                    PolyvVideoVO video = PolyvSDKUtil.loadVideoJSON2Video(vid);
//                                                    downloadInfo = new PolyvDownloadInfo(vid, video.getDuration(),
//                                                            video.getFileSizeMatchVideoType(bitrate), bitrate, video.getTitle(), progress, lists.getTitle());
//                                                    if (mDownloadSQLiteHelper != null && !mDownloadSQLiteHelper.isAdd(downloadInfo)) {
//                                                        downloadInfo.setClassname(course_list.get(position).getName());
//                                                        if (mType.equals("1")) {
//                                                            downloadInfo.setClass_id(Integer.valueOf(course_list.get(position).getId()));
//                                                        } else {
//                                                            downloadInfo.setClass_id(Integer.valueOf(mCourseId));
//                                                        }
//                                                        downloadInfo.setChapter(lists.getTitle());
//                                                        downloadInfo.setChapter_id(String.valueOf(lists.getId()));
//                                                        Log.e("TA", lists.getTitle() + course_list.get(position).getName());
//                                                        downloadInfo.setSection_name(lists.getTitle());
//                                                        downloadInfo.setSection_id(String.valueOf(lists.getId()));
//                                                        downloadInfo.setCourse_id(Integer.valueOf(mCourseId));
//                                                        downloadInfo.setLearn_time(String.valueOf(lists.getLearn_time()));
//                                                        downloadInfo.setType(mType);
//                                                        mDownloadSQLiteHelper.insert(downloadInfo);
////                                        progress = (int) mDownloadSQLiteHelper.getInfo(vid).getProgress();
//                                                        Log.e("TAG", "ddd" + position + "rrrrrrrr" + downloadInfo.getSection_name() + downloadInfo.getChapter() + downloadInfo.getClassname());
//
//                                                    } else {
//                                                        if (progress == 0) {
//                                                            state = WAITED;
//                                                            Log.e("TAG111111", "ddd1" + position + "//////" + downloadInfo.getSection_name() + downloadInfo.getChapter() + downloadInfo.getClassname());
//                                                        } else if (progress == 100) {
//                                                            state = "下载完成了";
//                                                        }
//                                                        Log.e("TAG", state + "333");
//                                                    }
//
//                                                } catch (JSONException e) {
//                                                    e.printStackTrace();
//                                                    Log.e("TAG", e.getMessage() + "");
//                                                }
//                                            }
//                                        }).start();
//                                        new Handler().postDelayed(new Runnable() {//存在一个问题 上一个线程往数据库中插入数据 会存在点击下载时候未插入所以  新建一个 让主线程 停留两秒  可能时间有点长 暂时定为两秒
//                                            @Override
//                                            public void run() {
//                                                DownLoad(holder, vid, lists.getTitle(), Integer.valueOf(course_list.get(position).getId()), Integer.valueOf(lists.getId()));
//                                            }
//                                        }, 500);
//                                    } else {
//                                        ToastUtil.showShortToast("未购买");
//                                    }
//                                    break;
//                                case 2:
//                                    Intent pIntent = new Intent();
//                                    pIntent.setClass(ctx, PracticeActivity.class);
//                                    pIntent.putExtra("mPaperId", lists.getPractice());
//                                    pIntent.putExtra("model", model);
//                                    ctx.startActivity(pIntent);
//                                    break;
//                                case 3:
//                                    Intent webIntent = new Intent();
//                                    webIntent.setClass(ctx, ImageTextActivity.class);
//                                    webIntent.putExtra("content", lists.getContent());
//                                    webIntent.putExtra("title", lists.getTitle());
//                                    webIntent.putExtra("id", course_list.get(position).getId());
//                                    webIntent.putExtra("model", model);
//                                    Log.e("********time", model.getLearn_time() + "*********");
//                                    ctx.startActivity(webIntent);
//                                    break;
//                                case 4:
//
//
//                                    Log.d("/////////////", lists.getLive_info().getVideo_url() + "222222");
//                                    try {
//                                        Thread.sleep(500);
//                                    } catch (InterruptedException e) {
//                                        e.printStackTrace();
//                                    }
//
//
//                                    if (lists.getLive_info().getVideo_url().equals("")) {
//                                        //1.未开始  2开始
//                                        Log.d("/////////////bijioa", time + "/////////////" + lists.getLive_info().getStart_time());
//                                        if (ToolUtils.getTime(time, lists.getLive_info().getStart_time()).equals("2")) {
//                                            mPushUrl = lists.getLive_info().getPush_url();
//                                            Log.e("********mPushUrl", mPushUrl + "///////");
//                                            mToken = lists.getLive_info().getToken();
//                                            Log.e("********mToken", mToken + "///////");
//                                            mChatRoom = lists.getLive_info().getChat_room_id();
//                                            name = lists.getLive_info().getName();
//                                            ////以前是根据是否有vid也即是video_url是否为null来判断是否为空
//                                            if (lists.getLive_info().getPush_url() != null) {
//                                                if (System.currentTimeMillis() - nowtime > 2000) {
//                                                    onLine(lists.getLive_info().getApp_name());
//                                                    nowtime = System.currentTimeMillis();
//                                                }
//
//                                            } else {
//                                                ToastUtil.showShortToast("暂无直播");
//                                            }
//                                        } else {
//                                            ToastUtil.showShortToast("直播还未开始");
//                                        }
//                                    } else {
//                                        vid = lists.getLive_info().getVideo_url();
//                                        if (mIsBuy == 1) {
//                                            new Thread(new Runnable() {
//                                                public void run() {
//                                                    try {
//                                                        PolyvVideoVO video = PolyvSDKUtil.loadVideoJSON2Video(vid);
//                                                        downloadInfo = new PolyvDownloadInfo(vid, video.getDuration(),
//                                                                video.getFileSizeMatchVideoType(bitrate), bitrate, video.getTitle(), progress, lists.getTitle());
//                                                        if (mDownloadSQLiteHelper != null && !mDownloadSQLiteHelper.isAdd(downloadInfo)) {
//                                                            downloadInfo.setClassname(course_list.get(position).getName());
//                                                            if (mType.equals("1")) {
//                                                                downloadInfo.setClass_id(Integer.valueOf(course_list.get(position).getId()));
//                                                            } else {
//                                                                downloadInfo.setClass_id(Integer.valueOf(mCourseId));
//                                                            }
//                                                            downloadInfo.setChapter(lists.getTitle());
//                                                            downloadInfo.setChapter_id(String.valueOf(lists.getId()));
//                                                            Log.e("TA", lists.getTitle());
//                                                            downloadInfo.setSection_name(lists.getTitle());
//                                                            downloadInfo.setSection_id(String.valueOf(lists.getId()));
//                                                            downloadInfo.setCourse_id(Integer.valueOf(mCourseId));
//                                                            downloadInfo.setLearn_time(String.valueOf(lists.getLearn_time()));
//                                                            downloadInfo.setType(mType);
//                                                            mDownloadSQLiteHelper.insert(downloadInfo);
////                                        progress = (int) mDownloadSQLiteHelper.getInfo(vid).getProgress();
//                                                            Log.e("TAG", "ddd" + position + "rrrrrrrr" + downloadInfo.getSection_name() + downloadInfo.getChapter() + downloadInfo.getClassname());
//
//                                                        } else {
//                                                            if (progress == 0) {
//                                                                state = WAITED;
//                                                                Log.e("TAG111111", "ddd1" + position + "//////" + downloadInfo.getSection_name() + downloadInfo.getChapter() + downloadInfo.getClassname());
//                                                            } else if (progress == 100) {
//                                                                state = "下载完成了";
//                                                            }
//                                                            Log.e("TAG", state + "333");
//                                                        }
//
//                                                    } catch (JSONException e) {
//                                                        e.printStackTrace();
//                                                        Log.e("TAG", e.getMessage() + "");
//                                                    }
//                                                }
//                                            }).start();
//                                            new Handler().postDelayed(new Runnable() {//存在一个问题 上一个线程往数据库中插入数据 会存在点击下载时候未插入所以  新建一个 让主线程 停留两秒  可能时间有点长 暂时定为两秒
//                                                @Override
//                                                public void run() {
//                                                    DownLoad(holder, vid, lists.getTitle(), Integer.valueOf(course_list.get(position).getId()), Integer.valueOf(lists.getId()));
//                                                }
//                                            }, 500);
//                                        } else {
//                                            ToastUtil.showShortToast("未购买");
//                                        }
//                                    }
//
//                                    break;
//                            }
//                        } else {
//                            ToastUtil.showShortToast("未购买");
//                        }
//                    }
//                });

//                if (!isdown) {
//                    holder.setVisible(R.id.content_flag,false);
//                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return course_list.size();
    }
    class ViewHolder extends RecyclerView.ViewHolder{
        public TextView tv_title;
        public RecyclerView rv_list;
        public ViewHolder(View itemView) {
            super(itemView);
            tv_title = itemView.findViewById(R.id.tv_title);
            rv_list = itemView.findViewById(R.id.rv_list);
        }
    }
    private static OnItemVideoPlayListener videoPlayListener;
    public  interface OnItemVideoPlayListener{
        void startPlay(String path,String title);
    }

    public static void setOnItemVideoPlayListener(OnItemVideoPlayListener onItemVideoPlayListener){
        videoPlayListener = onItemVideoPlayListener;
    }

    public void DownLoad(final com.zhy.adapter.recyclerview.base.ViewHolder holder, final String vid, final String mChapter, final int parentid, final int id) {
        PolyvDownloaderManager.getPolyvDownloader(vid, 1);

        if (mDownloadSQLiteHelper.getInfo(vid) != null && mDownloadSQLiteHelper.getInfo(vid).getProgress() < 100) {
            Log.e("TAG3333", state);
            Log.e("TAG3444444", vid + state + mDownloadSQLiteHelper.getInfo(vid).getProgress());
            final PolyvDownloader downloader = PolyvDownloaderManager.getPolyvDownloader(vid, 1);
            if (state.equals(DOWNLOADING)) {
                state = PAUSEED;
                holder.setText(R.id.content_flag, "");
                holder.setBackgroundRes(R.id.content_flag, R.mipmap.pause);
                downloader.stop();
                Log.e("OPOP", "top");
            } else if (state.equals(PAUSEED) || state.equals(WAITED)) {
                state = DOWNLOADING;
                if (mDownloadSQLiteHelper.getInfo(vid).getProgress() > 0 && mDownloadSQLiteHelper.getInfo(vid).getProgress() < 100) {
                    Log.e("opopo", mDownloadSQLiteHelper.getInfo(vid).getProgress() + "%%%%");
                    holder.setText(R.id.content_flag, mDownloadSQLiteHelper.getInfo(vid).getProgress() + "%");
                } else {
                    holder.setText(R.id.content_flag, progress + "%");
                    Log.e("opoposss", progress + "%%%%" + mDownloadSQLiteHelper.getInfo(vid).getProgress());
                }

                holder.setBackgroundRes(R.id.content_flag, R.mipmap.home_png_speed_default);
                //////////////////////
                if (ToolUtils.isNetworkAvailable(ctx)) {
                    //仅在wifi下下载
                    if (Sphelper.getString(ctx, "WIFISWITCH", "wifi_switch") != null) {

                        Log.e("********WIFISWITCH", Sphelper.getString(ctx, "WIFISWITCH", "wifi_switch"));
                        if (Sphelper.getString(ctx, "WIFISWITCH", "wifi_switch").equals("1")) {
                            if (ToolUtils.isWifi(ctx)) {
                                downloader.start();
//                                                    DownLoad(holder, vid, lists.getTitle(), Integer.valueOf(lists.getParent_id()), Integer.valueOf(lists.getId()));
                            } else {
                                ToastUtil.showShortToast("已开启仅WiFi下载，当前处于移动网络");
                            }
                        } else {
                            if (ToolUtils.isWifi(ctx)) {
                                downloader.start();
                                ToastUtil.showShortToast("未开启仅wifi下载 当前是移动网络");
                            } else {
                                ToastUtil.showShortToast("未开启仅wifi下载 当前是移动网络");
                                downloader.start();
                            }
                        }
                    }
                } else {
                    ToastUtil.showShortToast("当前网络不可用，请检查网络设置");
                }
//                            downloader.start();///先注掉
            }
            downloader.setPolyvDownloadSpeedListener(new IPolyvDownloaderSpeedListener() {
                @Override
                public void onSpeed(int speed) {
                    //下载速度监听
                    Log.e("TAG", "xia sudu " + speed + Formatter.formatShortFileSize(ctx, speed));
                }
            });
            downloader.setPolyvDownloadProressListener(new IPolyvDownloaderProgressListener() {
                @Override
                public void onDownload(long current, long total) {
                    //下载进度 current当前进度 total 总进度
                    mTotal = total;
                    state = DOWNLOADING;
                    downloadInfo.setPercent(current);
                    downloadInfo.setTotal(total);
                    int progress = (int) (current * 100 / total);
                    holder.setText(R.id.content_flag, progress + "%");
                    holder.setBackgroundRes(R.id.content_flag, R.mipmap.home_png_speed_default);
                    mDownloadSQLiteHelper.updateProgress(current, total, progress, vid);
                }

                @Override
                public void onDownloadSuccess() {
                    state = DOWNLOADED;
                    if (mTotal == 0)
                        mTotal = 1;
                    downloadInfo.setPercent(mTotal);
                    downloadInfo.setTotal(mTotal);
                    downloadInfo.setProgress(100);
                    mDownloadSQLiteHelper.updateProgress(mTotal, mTotal, 100, vid);
                    holder.setText(R.id.content_flag, "100%");
                    holder.setBackgroundRes(R.id.content_flag, R.mipmap.home_png_speed_default);
                    if (progress == 100) {
                        holder.setBackgroundRes(R.id.content_flag, R.mipmap.down_pre);
                    }
                }

                @Override
                public void onDownloadFail(@NonNull PolyvDownloaderErrorReason polyvDownloaderErrorReason) {
                }
            });
            downloader.setPolyvDownloadStartListener(new IPolyvDownloaderStartListener() {
                @Override
                public void onStart() {
                }
            });
        } else {
            Intent intent = RecordingActivity.newIntent(ctx, RecordingActivity.PlayMode.portrait, vid, bitrate, true, true);
            intent.putExtra("model", model);
            intent.putExtra(RecordingActivity.IS_VLMS_ONLINE, false);
            intent.putExtra("flag", "0");//flag 为1 表示点击列表 为0表示点击下载进入
            ctx.startActivity(intent);
        }
    }

    private void onLine(final String appname) {
        Map<String, String> map = new HashMap<>();
        map.put("app_name", appname);
        Log.e("OPOP", appname);
        Xutil.GET(ApiUrl.ONLINE, map, new MyCallBack<OnLineResponse>() {
            @Override
            public void onSuccess(OnLineResponse result) {
                super.onSuccess(result);
                if (result.getStatus_code().equals("200")) {
                    if (result.getData().getIs_online() != null) {
                        // 备注：0直播不在线 1正在直播
                        if (result.getData().getIs_online().equals("0")) {
                            ToastUtil.showShortToast("直播不在线");
                        } else {
                            Intent intent = new Intent();
                            intent.putExtra("url", mPushUrl);
                            intent.putExtra("chat_room_id", mChatRoom);
                            intent.putExtra("token", mToken);
                            intent.putExtra("model", model);
                            intent.putExtra("name", appname);


//                            intent.putExtra("chat_room_id",Now.getLive_info().getChat_room_id());
//                            intent.putExtra("token",Now.getLive_info().getToken());
//                            intent.putExtra("live_url",Now.getLive_info().getPush_url());
//                            intent.putExtra("_id", _id);
//                            intent.putExtra("name", Now.getLive_info().getName());
                            intent.setClass(ctx, LiveActivity.class);
                            ctx.startActivity(intent);
                        }
                    }
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
            }

            @Override
            public void onFinished() {
                super.onFinished();
            }
        });

    }
}
