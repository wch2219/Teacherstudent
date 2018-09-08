package com.jiaoshizige.teacherexam.adapter;

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
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
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
import com.jiaoshizige.teacherexam.http.ApiUrl;
import com.jiaoshizige.teacherexam.http.MyCallBack;
import com.jiaoshizige.teacherexam.http.Xutil;
import com.jiaoshizige.teacherexam.model.AddLearnRecordModel;
import com.jiaoshizige.teacherexam.model.NewCourseCatalogResponse;
import com.jiaoshizige.teacherexam.model.OnLineResponse;
import com.jiaoshizige.teacherexam.palyer.PolyvDownloadInfo;
import com.jiaoshizige.teacherexam.palyer.PolyvDownloadSQLiteHelper;
import com.jiaoshizige.teacherexam.utils.SDOtherUtil;
import com.jiaoshizige.teacherexam.utils.Sphelper;
import com.jiaoshizige.teacherexam.utils.TimeUtils;
import com.jiaoshizige.teacherexam.utils.ToastUtil;
import com.jiaoshizige.teacherexam.utils.ToolUtils;
import com.jiaoshizige.teacherexam.widgets.PercentageBallView;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/10/17 0017,
 * mx
 * 目录
 */

public class CourseCatalogFragmentAdapter extends BaseExpandableListAdapter {
    private List<NewCourseCatalogResponse.mCatalog> question;
    private List<List<NewCourseCatalogResponse.mSon>> childArray;
    private List<NewCourseCatalogResponse.mCourseList> mCourseLists;
    //    private NewCourseCatalogResponse.mCourseList mCourseListItem;
    private Context mContext;
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
    private int mIsBuy;
    private long mTotal;
    private String mCourseId;//单个套餐的id
    private String mGroupId;////用于请去的id 可以是套餐课程 可以是单科课程
    private String mType;//1班级2课程
    private String mGroupName;
    private long time;
    private String mPushUrl;
    private String mToken;
    private String mChatRoom;
    private String name;
    PolyvDownloadInfo downloadInfo;
    AddLearnRecordModel model = new AddLearnRecordModel();
    long nowtime = 0;

    public CourseCatalogFragmentAdapter(Context context, List<NewCourseCatalogResponse.mCatalog> groupArray, List<List<NewCourseCatalogResponse.mSon>> childArray, List<NewCourseCatalogResponse.mCourseList> courseLists, String type, String group_id, String course_id, int isBuy, String group_name) {
        mContext = context;
        this.question = groupArray;
        this.childArray = childArray;
        this.mDownloadSQLiteHelper = PolyvDownloadSQLiteHelper.getInstance(mContext);
        this.mIsBuy = isBuy;//每个mCourseList的isbuy
        this.mCourseId = course_id;//每个mCourseList的id
        this.mGroupId = group_id;//用于请去的id
//        this.mCourseName = course_name;
        this.mGroupName = group_name;
        this.mCourseLists = courseLists;
        this.mType = type;

    }

    @Override
    public int getGroupCount() {
        return question.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return childArray.get(groupPosition).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return question.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return childArray.get(groupPosition).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupHolder holder = null;
        if (convertView == null) {
            holder = new GroupHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_courser_catalog_fragment, null);
            holder.mCourseTitle = (TextView) convertView.findViewById(R.id.course_title);
            holder.mCatalogArrow = (ImageView) convertView.findViewById(R.id.catalog_arrow);
            convertView.setTag(holder);
        } else {
            holder = (GroupHolder) convertView.getTag();
        }
        //判断是否已经打开列表
        if (isExpanded) {
            holder.mCatalogArrow.setImageResource(R.mipmap.cehua_arrow_small_up);
        } else {
            holder.mCatalogArrow.setImageResource(R.mipmap.cehua_arrow_small_down);
        }
        holder.mCourseTitle.setText(question.get(groupPosition).getTitle());
        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, final ViewGroup parent) {

        ChildHolder holder = null;
        holder = new ChildHolder();
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_course_catalog_content_list, null);
            holder.mCataLogList = (RecyclerView) convertView.findViewById(R.id.catalog_list);
            convertView.setTag(holder);
        } else {
            holder = (ChildHolder) convertView.getTag();
        }
        Log.e("SSD", groupPosition + "dd" + mCourseLists.size());
        Log.e("TAGdsdsd", mIsBuy + "");
        holder.mCataLogList.setLayoutManager(new LinearLayoutManager(mContext));
        List<NewCourseCatalogResponse.mSon> data = new ArrayList<>();
        data.add(question.get(groupPosition).getSon().get(childPosition));
        holder.mCataLogList.setAdapter(new CommonAdapter<NewCourseCatalogResponse.mSon>(mContext, R.layout.item_catalog_content, question.get(groupPosition).getSon()) {
            @Override
            protected void convert(final ViewHolder holder, final NewCourseCatalogResponse.mSon lists, int position) {
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
                    if (lists.getLearn_percent().equals("0")) {
                        progressBall.setmWaterLevel(0, "");
                    } else {
                        progressBall.setmWaterLevel(SDOtherUtil.strToFloat(lists.getLearn_percent()), "");
                    }
                } else {
//                    textView.setVisibility(View.GONE);
                    progressBall.setVisibility(View.INVISIBLE);
                }
                if (progress == 100) {
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
//                            holder.setBackgroundRes(R.id.content_type, R.mipmap.video1);
//                            holder.setBackgroundRes(R.id.content_flag, R.mipmap.down);
                            Log.e("TAG", "er");
                        }
                        if (progress == 100) {
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
                        Intent intent = new Intent();
                        Log.e("OPOPswww", mGroupId);
                        model.setGroup_id(mGroupId);
                        if (mType.equals("1")) {
                            model.setClass_id(Integer.valueOf(mGroupId));
                        } else {
                            model.setClass_id(Integer.valueOf(mCourseId));
                        }
                        model.setClassname(mGroupName);
                        model.setChapter(question.get(groupPosition).getTitle());
                        model.setChapter_id(question.get(groupPosition).getId());
                        model.setCourse_name(mGroupName);
                        model.setCourse_id(mCourseId);
                        model.setChapter_name(question.get(groupPosition).getTitle());
                        model.setSection_id(String.valueOf(lists.getId()));
                        model.setSection_name(lists.getTitle());
                        model.setLearn_time(lists.getLearn_time());
                        model.setType(mType);
                        Log.e("**********isbuy", mIsBuy + "///" + model.getChapter_id() + model.getChapter() + model.getClass_id() + model.getCourse_id());
                        String vid;

                        Log.e("******getLive_info*", lists.getType() + "*********");
                        if (lists.getType().equals("4")) {
                            //直播
                            vid = lists.getLive_info().getVideo_url();
                            if (mIsBuy == 1) {
                                if (!lists.getLive_info().getVideo_url().equals("") && lists.getLive_info().getVideo_url() != null) {
                                    intent = RecordingActivity.newIntent(mContext, RecordingActivity.PlayMode.portrait, vid, 1, true, true);
                                    intent.setClass(mContext, RecordingActivity.class);
                                    Log.e("000000", vid);
                                    intent.putExtra("live_url", vid);
                                    intent.putExtra("no_course_id", "1");
                                    intent.putExtra("model", model);
                                    intent.putExtra("flag", "1");//flag 为1 表示点击列表 为0表示点击下载进入
                                    mContext.startActivity(intent);
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
                        } else if (lists.getType().equals("1")) {
                            //视频
                            vid = lists.getVideo();
                            if (mIsBuy == 1) {
//                                if (!lists.getLive_info().getVideo_url().equals("") && lists.getLive_info().getVideo_url() != null) {
                                if (!vid.equals("") && vid != null) {
                                    intent = RecordingActivity.newIntent(mContext, RecordingActivity.PlayMode.portrait, vid, 1, true, true);
                                    intent.setClass(mContext, RecordingActivity.class);
                                    Log.e("000000", vid);
                                    intent.putExtra("live_url", vid);
                                    intent.putExtra("no_course_id", "1");
                                    intent.putExtra("model", model);
                                    intent.putExtra("flag", "1");//flag 为1 表示点击列表 为0表示点击下载进入
                                    mContext.startActivity(intent);
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
//                            model.setGroup_id(question.get(groupPosition).getId());
//                        }*/
//                        model.setGroup_id(mGroupId);
//                        if (mType.equals("1")) {
//                            model.setCourse_id(mCourseId);
//                        } else {
//                            model.setCourse_id(mGroupId);
//                        }
//
//                        model.setCourse_name(mGroupName);
//                        model.setChapter_id(question.get(groupPosition).getId());
//                        model.setChapter_name(question.get(groupPosition).getTitle());
//                        model.setSection_id(String.valueOf(lists.getId()));
//                        model.setSection_name(lists.getTitle());
//                        model.setLearn_time(lists.getLearn_time());
//                        model.setType(mType);
//                        if (mIsBuy == 1) {
//                            Log.e("********type", lists.getType());
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
//                                                            video.getFileSizeMatchVideoType(bitrate), bitrate, video.getTitle(), progress, question.get(groupPosition).getTitle());
//                                                    if (mDownloadSQLiteHelper != null && !mDownloadSQLiteHelper.isAdd(downloadInfo)) {
//                                                        downloadInfo.setClassname(mGroupName);
//                                                        if (mType.equals("1")) {
//                                                            downloadInfo.setClass_id(Integer.valueOf(mGroupId));
//                                                        } else {
//                                                            downloadInfo.setClass_id(Integer.valueOf(mCourseId));
//                                                        }
//                                                        downloadInfo.setChapter(question.get(groupPosition).getTitle());
//                                                        downloadInfo.setChapter_id(question.get(groupPosition).getId());
//                                                        Log.e("TA", lists.getTitle() + mGroupName);
//                                                        downloadInfo.setSection_name(lists.getTitle());
//                                                        downloadInfo.setSection_id(lists.getId());
//                                                        downloadInfo.setCourse_id(Integer.valueOf(mCourseId));
//                                                        downloadInfo.setLearn_time(lists.getLearn_time());
//                                                        downloadInfo.setType(mType);
//                                                        mDownloadSQLiteHelper.insert(downloadInfo);
////                                        progress = (int) mDownloadSQLiteHelper.getInfo(vid).getProgress();
//                                                        Log.e("TAG", "ddd" + childPosition + "rrrrrrrr" + downloadInfo.getSection_name() + downloadInfo.getChapter() + downloadInfo.getClassname());
//
//                                                    } else {
//                                                        if (progress == 0) {
//                                                            state = WAITED;
//                                                            Log.e("TAG111111", "ddd1" + childPosition + "//////" + downloadInfo.getSection_name() + downloadInfo.getChapter() + downloadInfo.getClassname());
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
//                                                DownLoad(holder, vid, question.get(groupPosition).getTitle(), Integer.valueOf(lists.getParent_id()), Integer.valueOf(lists.getId()));
//                                            }
//                                        }, 500);
//                                    } else {
//                                        ToastUtil.showShortToast("未购买");
//                                    }
//                                    break;
//                                case 2:
//                                    Intent pIntent = new Intent();
//                                    pIntent.setClass(mContext, PracticeActivity.class);
//                                    pIntent.putExtra("mPaperId", lists.getPractice());
//                                    pIntent.putExtra("model", model);
//                                    mContext.startActivity(pIntent);
//                                    break;
//                                case 3:
//                                    Intent webIntent = new Intent();
//                                    webIntent.setClass(mContext, ImageTextActivity.class);
//                                    webIntent.putExtra("content", lists.getContent());
//                                    webIntent.putExtra("title", lists.getTitle());
//                                    webIntent.putExtra("id", lists.getParent_id());
//                                    webIntent.putExtra("model", model);
//                                    Log.e("********time", model.getLearn_time() + "*********");
//                                    mContext.startActivity(webIntent);
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
//                                                                video.getFileSizeMatchVideoType(bitrate), bitrate, video.getTitle(), progress, question.get(groupPosition).getTitle());
//                                                        if (mDownloadSQLiteHelper != null && !mDownloadSQLiteHelper.isAdd(downloadInfo)) {
//                                                            downloadInfo.setClassname(mGroupName);
//                                                            if (mType.equals("1")) {
//                                                                downloadInfo.setClass_id(Integer.valueOf(mGroupId));
//                                                            } else {
//                                                                downloadInfo.setClass_id(Integer.valueOf(mCourseId));
//                                                            }
//                                                            downloadInfo.setChapter(question.get(groupPosition).getTitle());
//                                                            downloadInfo.setChapter_id(question.get(groupPosition).getId());
//                                                            Log.e("TA", lists.getTitle() + mGroupName);
//                                                            downloadInfo.setSection_name(lists.getTitle());
//                                                            downloadInfo.setSection_id(lists.getId());
//                                                            downloadInfo.setCourse_id(Integer.valueOf(mCourseId));
//                                                            downloadInfo.setLearn_time(lists.getLearn_time());
//                                                            downloadInfo.setType(mType);
//                                                            mDownloadSQLiteHelper.insert(downloadInfo);
////                                        progress = (int) mDownloadSQLiteHelper.getInfo(vid).getProgress();
//                                                            Log.e("TAG", "ddd" + childPosition + "rrrrrrrr" + downloadInfo.getSection_name() + downloadInfo.getChapter() + downloadInfo.getClassname());
//
//                                                        } else {
//                                                            if (progress == 0) {
//                                                                state = WAITED;
//                                                                Log.e("TAG111111", "ddd1" + childPosition + "//////" + downloadInfo.getSection_name() + downloadInfo.getChapter() + downloadInfo.getClassname());
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
//                                                    DownLoad(holder, vid, question.get(groupPosition).getTitle(), Integer.valueOf(lists.getParent_id()), Integer.valueOf(lists.getId()));
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
            }
        });

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    class GroupHolder {
        public TextView mCourseTitle;
        public ImageView mCatalogArrow;
    }

    class ChildHolder {
        public RecyclerView mCataLogList;
        public ImageView sound;
    }

    public void DownLoad(final ViewHolder holder, final String vid, final String mChapter, final int parentid, final int id) {
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
                if (ToolUtils.isNetworkAvailable(mContext)) {
                    //仅在wifi下下载
                    if (Sphelper.getString(mContext, "WIFISWITCH", "wifi_switch") != null) {

                        Log.e("********WIFISWITCH", Sphelper.getString(mContext, "WIFISWITCH", "wifi_switch"));
                        if (Sphelper.getString(mContext, "WIFISWITCH", "wifi_switch").equals("1")) {
                            if (ToolUtils.isWifi(mContext)) {
                                downloader.start();
//                                                    DownLoad(holder, vid, question.get(groupPosition).getTitle(), Integer.valueOf(lists.getParent_id()), Integer.valueOf(lists.getId()));
                            } else {
                                ToastUtil.showShortToast("已开启仅WiFi下载，当前处于移动网络");
                            }
                        } else {
                            if (ToolUtils.isWifi(mContext)) {
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
                    Log.e("TAG", "xia sudu " + speed + Formatter.formatShortFileSize(mContext, speed));
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
            Intent intent = RecordingActivity.newIntent(mContext, RecordingActivity.PlayMode.portrait, vid, bitrate, true, true);
            intent.putExtra("model", model);
            intent.putExtra(RecordingActivity.IS_VLMS_ONLINE, false);
            intent.putExtra("flag", "0");//flag 为1 表示点击列表 为0表示点击下载进入
            mContext.startActivity(intent);
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
                            intent.setClass(mContext, LiveActivity.class);
                            mContext.startActivity(intent);
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