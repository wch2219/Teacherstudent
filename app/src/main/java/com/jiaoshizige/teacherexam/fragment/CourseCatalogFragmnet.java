package com.jiaoshizige.teacherexam.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.format.Formatter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.easefun.polyvsdk.PolyvDownloader;
import com.easefun.polyvsdk.PolyvDownloaderErrorReason;
import com.easefun.polyvsdk.PolyvDownloaderManager;
import com.easefun.polyvsdk.download.listener.IPolyvDownloaderProgressListener;
import com.easefun.polyvsdk.download.listener.IPolyvDownloaderSpeedListener;
import com.easefun.polyvsdk.download.listener.IPolyvDownloaderStartListener;
import com.jiaoshizige.teacherexam.R;
import com.jiaoshizige.teacherexam.activity.RecordingActivity;
import com.jiaoshizige.teacherexam.adapter.CourseCatalogFragmentAdapter;
import com.jiaoshizige.teacherexam.base.GloableConstant;
import com.jiaoshizige.teacherexam.base.MBaseFragment;
import com.jiaoshizige.teacherexam.http.ApiUrl;
import com.jiaoshizige.teacherexam.http.MyCallBack;
import com.jiaoshizige.teacherexam.http.Xutil;
import com.jiaoshizige.teacherexam.model.AddLearnRecordModel;
import com.jiaoshizige.teacherexam.model.CourseCatalogResponse;
import com.jiaoshizige.teacherexam.palyer.PolyvDownloadInfo;
import com.jiaoshizige.teacherexam.palyer.PolyvDownloadSQLiteHelper;
import com.jiaoshizige.teacherexam.widgets.CustomExpandableListView;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/a1/15.
 * 课程 目录
 */

public class CourseCatalogFragmnet extends MBaseFragment {
    @BindView(R.id.expandable_list_view)
    CustomExpandableListView mExpandableView;
    /*@BindView(R.id.task)
    RecyclerView mTask;*/
//    @BindView(R.id.pulltorefresh)
//    PullToRefreshLayoutRewrite mPullToRefresh;
//    @BindView(R.id.layout_swipe_refresh)
//    SwipeRefreshView mSwipeRefreshView;
    ///////////////////////////////
    private String state = "";
    private static final String DOWNLOADED = "已下载";
    private static final String DOWNLOADING = "正在下载";
    private static final String PAUSEED = "暂停下载";
    private static final String WAITED = "等待下载";
    private int progress = 0;
    private int bitrate = 1;
    private String vid = "c538856dde2600e0096215c16592d4d3_c";
    private static PolyvDownloadSQLiteHelper mDownloadSQLiteHelper;
    PolyvDownloadInfo downloadInfo;
    private long mTotal;
    ///////////////////////////////////////
    private List<CourseCatalogResponse.mCatalog> mCourseTitle;
    private List<CourseCatalogResponse.mSon> mCourseContent;
    private List<List<CourseCatalogResponse.mSon>> childArray;
    private CourseCatalogFragmentAdapter mExpandableViewAdapter;
    private String mCourseId;
    private String mGroupId;
    private String mCourseName;
    private int mIsBuy;
    private String mChapterId;
    private String mChapterName;
    @SuppressLint("ValidFragment")
    public CourseCatalogFragmnet(String group_id, String course_id, int isBuy, String course_name) {
        this.mGroupId = group_id;
        this.mCourseId = course_id;
        this.mIsBuy = isBuy;
        this.mCourseName = course_name;
        Log.e("TAGdsdsssswww222d",mCourseName);
    }

    public CourseCatalogFragmnet() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.course_catalog_fragment,container,false);
    }
    Handler handler=new Handler();
    @Override
    protected void onInitViews(Bundle savedInstanceState) {
//        mSwipeRefreshView.setEnabled(false);
        mDownloadSQLiteHelper = PolyvDownloadSQLiteHelper.getInstance(getActivity());/////////////////////////////
        requestCourseCataLog();
//        mPullToRefresh.setRefreshListener(new BaseRefreshListener() {
//            @Override
//            public void refresh() {
//                requestCourseCataLog();
//            }
//
//            @Override
//            public void loadMore() {
//                requestCourseCataLog();
//            }
//        });
//        mPullToRefresh.setOnErrorListener(new PullToRefreshLayoutRewrite.onClickCallBackListener() {
//            @Override
//            public void onCallBackListener(View v) {
//                requestCourseCataLog();
//            }
//        });
//        mExpandableView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
//            @Override
//            public void onGroupExpand(int groupPosition) {
//                for (int i = 0; i < mExpandableViewAdapter.getGroupCount(); i++) {
//                    if (groupPosition != i) {
//                        mExpandableView.collapseGroup(i);
//                    }
//                }
//            }
//        });
        handler.postDelayed(runnable, 2000);
    }
    Runnable runnable=new Runnable() {
        @Override
        public void run() {
            // TODO Auto-generated method stub
            //要做的事情
            try{
//                handler.postDelayed(this, 2000);
                Log.e("TAGwwwww","liangmiaozhixingyici  ssss");
            }catch (Exception e){
                e.printStackTrace();
            }


        }
    };

    private void requestCourseCataLog(){
        Map<String,String> map = new HashMap<>();
//        map.put("course_id","1");
        map.put("course_id",mCourseId);
        Log.e("********TAG",map.toString());
        GloableConstant.getInstance().startProgressDialog(getActivity());
        Xutil.GET(ApiUrl.COURSECATALOG,map,new MyCallBack<CourseCatalogResponse>(){
            @Override
            public void onSuccess(CourseCatalogResponse result) {
                super.onSuccess(result);
                GloableConstant.getInstance().stopProgressDialog1();
//                mPullToRefresh.finishRefresh();
//                mPullToRefresh.finishLoadMore();
//                mPullToRefresh.showView(ViewStatus.CONTENT_STATUS);
                Log.e("TAG","12");
                if(result.getStatus_code().equals("200")){
                    mCourseTitle = new ArrayList<>();
                    childArray = new ArrayList<>();
                    if(result.getData() != null && result.getData().getCatalog().size() > 0){
                        if(result.getData().getCourse_type().equals("1")){
                            mExpandableView.setVisibility(View.VISIBLE);
//                            mTask.setVisibility(View.GONE);
                        for (int i = 0;i < result.getData().getCatalog().size(); i++){
                            mCourseTitle.add(result.getData().getCatalog().get(i));
                            for(int j = 0; j < result.getData().getCatalog().get(i).getSon().size(); j++){
                                mCourseContent = new ArrayList<>();
                                mCourseContent.add(result.getData().getCatalog().get(i).getSon().get(j));
                                childArray.add(mCourseContent);
                            }
                        }
//                        mExpandableViewAdapter = new CourseCatalogFragmentAdapter(getActivity(),mCourseTitle,childArray,mIsBuy,mCourseId,mGroupId,mCourseName);
                        mExpandableView.setAdapter(mExpandableViewAdapter);
                        }else{
                            mExpandableView.setVisibility(View.GONE);
//                            mTask.setVisibility(View.VISIBLE);
//                            mTask.setLayoutManager(new LinearLayoutManager(getActivity()));
                         /*   mTask.setAdapter(new CommonAdapter<CourseCatalogResponse.mSon>(getActivity(),R.layout.item_catalog_content,result.getData().getCatalog().get(0).getSon()) {
                                @Override
                                protected void convert(final ViewHolder holder, final CourseCatalogResponse.mSon mSon, int position) {
                                    holder.setText(R.id.content_title,(position+1)+"."+mSon.getTitle());

//                PolyvDownloader downloader = PolyvDownloaderManager.getPolyvDownloader(lists.getVideo(), bitrate);
                                    PolyvDownloader downloader = PolyvDownloaderManager.getPolyvDownloader(vid, bitrate);
                                    if(mIsBuy == 1){
                                        holder.setVisible(R.id.content_flag,true);
                                    }else{
                                        holder.setVisible(R.id.content_flag,false);
                                    }
                                    if(progress == 100){
                                        //已经下载
                                        state = DOWNLOADED;
                                    }else if(downloader.isDownloading()){
                                        //正在下载
                                        state = DOWNLOADING;
                                    }else if(PolyvDownloaderManager.isWaitingDownload(vid,bitrate)){
                                        //等待下载
                                        state = WAITED;
                                    }else{
                                        //  暂停下载
                                        state = PAUSEED;
                                    }
                                    switch (Integer.valueOf(mSon.getType())){//0章，1视频2试题3图文
                                        case 0:
//                        holder.setText(R.id.content_type,"文档");
                                            break;
                                        case 1:
//                        vid = lists.getVideo();//暂时注掉
                                            if(mDownloadSQLiteHelper != null && mDownloadSQLiteHelper.isAddVid(vid)){
                                                progress = (int) mDownloadSQLiteHelper.getInfo(vid).getProgress();
                                            }

                                            holder.setText(R.id.content_type,"视频");
                                            if(progress == 100){
                                                //已经下载
                                                state = DOWNLOADED;
                                                holder.setText(R.id.content_flag,progress+"%");
                                                holder.setBackgroundRes(R.id.content_flag,R.mipmap.home_png_speed_default);
                                            }else if(downloader.isDownloading()){
                                                //正在下载
                                                state = DOWNLOADING;
                                                holder.setText(R.id.content_flag,progress+"%");
                                                holder.setBackgroundRes(R.id.content_flag,R.mipmap.home_png_speed_default);
                                            }else if(PolyvDownloaderManager.isWaitingDownload(vid,bitrate)){
                                                //等待下载
                                                state = WAITED;
                                                holder.setBackgroundRes(R.id.content_flag,R.mipmap.home_icon_download_default);
                                            }else if(progress == 0){
                                                state = WAITED;
                                                holder.setBackgroundRes(R.id.content_flag,R.mipmap.home_icon_download_default);
                                            }else if(progress > 0 && progress < 100){
                                                state = DOWNLOADING;
                                                holder.setText(R.id.content_flag,progress+"%");
                                                holder.setBackgroundRes(R.id.content_flag,R.mipmap.home_png_speed_default);
                                            }else{
                                                //  暂停下载
                                                state = PAUSEED;
                                                holder.setBackgroundRes(R.id.content_flag,R.mipmap.home_icon_suspend_pressed);
                                            }
                                            new Thread(new Runnable() {
                                                public void run() {
                                                    try {
                                                        PolyvVideoVO video = PolyvSDKUtil.loadVideoJSON2Video(vid);
                                                        downloadInfo = new PolyvDownloadInfo(vid, video.getDuration(),
                                                                video.getFileSizeMatchVideoType(bitrate), bitrate, video.getTitle(),progress,"");
//                    downloadInfo.setChapter(mChapter);

                                                        if(mDownloadSQLiteHelper != null  && !mDownloadSQLiteHelper.isAdd(downloadInfo)){
                                                            downloadInfo.setCourse_id(Integer.valueOf(mCourseId));
                                                            downloadInfo.setParent_id(Integer.valueOf(mSon.getParent_id()));
                                                            downloadInfo.setSid(Integer.valueOf(mSon.getId()));
                                                            mDownloadSQLiteHelper.insert(downloadInfo);
                                                            progress = (int) mDownloadSQLiteHelper.getInfo(vid).getProgress();
                                                            Log.e("TAG","ddd"+progress+"wewe"+mDownloadSQLiteHelper.getInfo(vid).getProgress()+"7777"+mDownloadSQLiteHelper.getInfo(vid));
                                                        }else{
                                                            if(progress == 0){
                                                                state = WAITED;
                                                            }else if(progress == 100){
                                                                state = "下载完成了";
                                                            }
                                                            Log.e("TAG",state+"333");
                                                        }

                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                        Log.e("TAG",e.getMessage()+"");
                                                    }
                                                }
                                            }).start();
                                            break;
                                        case 2:
                                            holder.setText(R.id.content_type,"练习");
                                            break;
                                        case 3:
                                            holder.setText(R.id.content_type,"文档");
                                            break;
                                        case 4:
                                            holder.setText(R.id.content_type,"直播");
                                            holder.setBackgroundRes(R.id.content_flag,R.mipmap.home_icon_suspend_pressed);
                                            break;
                                    }
                                    holder.setOnClickListener(R.id.course_item_ly, new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
//                                            ToastUtil.showShortToast(mSon.getType());
                                        }
                                    });
                                    holder.setOnClickListener(R.id.content_flag, new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            switch (Integer.valueOf(mSon.getType())){//0章，1视频2试题3图文
                                                case 0:
//                        holder.setText(R.id.content_type,"文档");
//                                mContext.startActivity(new Intent(mContext,RecordingActivity.class));
                                                    Log.e("TTTTT",  mDownloadSQLiteHelper.getModel().size()+"dsdsds"+  mDownloadSQLiteHelper.getModel().get(0).getChapter());
//                                mDownloadSQLiteHelper.getModel();
                                                    break;
                                                case 1:
//                                DownLoad(holder,lists.getVideo());
//                                mChapter = question.get(groupPosition).getTitle();
//                                Log.e("TAG",mChapter);
                                                    DownLoad(holder,vid,"",Integer.valueOf(mSon.getParent_id()),Integer.valueOf(mSon.getId()),mSon.getTitle(),mSon.getLearn_time());
                                                    break;
                                                case 2:
//                                mContext.startActivity(new Intent(mContext,RecordingActivity.class));
                                                    break;
                                                case 3:
                                                    break;
                                                case 4:
                                                    Intent intent = new Intent();
                                                    intent.putExtra("","");
                                                    intent.setClass(mContext, LiveActivity.class);
                                                    mContext.startActivity(intent);
                                                    break;
                                            }
                                        }
                                    });
                                }
                            });*/

                        }
                    }else{
//                        mPullToRefresh.showView(ViewStatus.EMPTY_STATUS);
                    }
                }
            }

            @Override
            public void onFinished() {
                super.onFinished();
                GloableConstant.getInstance().stopProgressDialog1();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
                GloableConstant.getInstance().stopProgressDialog1();
//                mPullToRefresh.finishRefresh();
//                mPullToRefresh.finishLoadMore();
//                mPullToRefresh.showView(ViewStatus.ERROR_STATUS);
            }
        });
    }
    public void DownLoad(final ViewHolder holder, final String vid, final String mChapter, final int parentid, final int id,String title,String learn_time){

        PolyvDownloaderManager.getPolyvDownloader(vid, 1);
        /**
         * 加入判断如果用户开启仅wifi下载，则判断wifi状态进行下载  如果未开启提示用户当前处于移动网络  仍然下载
         */
        if (mDownloadSQLiteHelper.getInfo(vid) != null  &&  mDownloadSQLiteHelper.getInfo(vid).getProgress() < 100) {
            Log.e("TAG3333",state);
            Log.e("TAG3444444",state+mDownloadSQLiteHelper.getInfo(vid).getProgress());
            PolyvDownloader downloader = PolyvDownloaderManager.getPolyvDownloader(vid, 1);
            if(state.equals(DOWNLOADED) || state.equals("下载完成了")){//已下载
                state = DOWNLOADED;
                holder.setText(R.id.content_flag,"100%");
                holder.setBackgroundRes(R.id.content_flag,R.mipmap.home_png_speed_default);
                Intent intent = RecordingActivity.newIntent(getActivity(), RecordingActivity.PlayMode.portrait, vid, bitrate, true, true);
                AddLearnRecordModel model = new AddLearnRecordModel();
                model.setGroup_id(mGroupId);
                model.setCourse_id(mCourseId);
                model.setCourse_name(mCourseName);
                model.setChapter_id(mChapterId);
                model.setChapter_name(mChapter);
                model.setSection_id(String.valueOf(id));
                model.setSection_name(title);
                model.setLearn_time(learn_time);
                model.setType("1");
                intent.putExtra("model",model);
                Log.e("TT33332T",model.getCourse_name());
               /* intent.putExtra("group_id",mGroupId);
                intent.putExtra("course_id",mCourseId);
                intent.putExtra("course_name",mCourseName);
                intent.putExtra("chapter_id",mChapterId);//章id
                intent.putExtra("chapter_name",mChapter);//章名称
                intent.putExtra("section_id",id);//节id
                intent.putExtra("section_name",title);//节名称
                intent.putExtra("learn_time",learn_time);////学习完成时间：分钟*/
                // 在线视频和下载的视频播放的时候只显示播放器窗口，用该参数来控制
                intent.putExtra(RecordingActivity.IS_VLMS_ONLINE, false);
                getActivity().startActivity(intent);
            }else if(state.equals(DOWNLOADING)){
                state = PAUSEED;
                holder.setText(R.id.content_flag,"");
                holder.setBackgroundRes(R.id.content_flag,R.mipmap.home_icon_suspend_pressed);
                downloader.stop();
            }else if(state.equals(PAUSEED) || state.equals(WAITED)){
                state = DOWNLOADING;
                holder.setText(R.id.content_flag,progress+"%");
                holder.setBackgroundRes(R.id.content_flag,R.mipmap.home_png_speed_default);
                downloader.start();
            }
            downloader.setPolyvDownloadSpeedListener(new IPolyvDownloaderSpeedListener() {
                @Override
                public void onSpeed(int speed) {
                    //下载速度监听
                    Log.e("TAG","xia sudu "+speed+ Formatter.formatShortFileSize(getActivity(), speed));
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
                    holder.setText(R.id.content_flag,progress+"%");
                    holder.setBackgroundRes(R.id.content_flag,R.mipmap.home_png_speed_default);
                    Log.e("TAG",progress+"xia zaijindu "+current+"++"+total);
                    mDownloadSQLiteHelper.update(downloadInfo,current,total,progress);
                }

                @Override
                public void onDownloadSuccess() {
                    Log.e("TAG","xia chenggong  ");
                    state = DOWNLOADED;
                    if (mTotal == 0)
                        mTotal = 1;
                    downloadInfo.setPercent(mTotal);
                    downloadInfo.setTotal(mTotal);
                    downloadInfo.setProgress(100);
                    mDownloadSQLiteHelper.update(downloadInfo, mTotal, mTotal,100);
                    holder.setText(R.id.content_flag,"100%");
                    holder.setBackgroundRes(R.id.content_flag,R.mipmap.home_png_speed_default);
                }

                @Override
                public void onDownloadFail(@NonNull PolyvDownloaderErrorReason polyvDownloaderErrorReason) {
                    Log.e("TAG","xia shibai ");
                }
            });
            downloader.setPolyvDownloadStartListener(new IPolyvDownloaderStartListener() {
                @Override
                public void onStart() {
                    Log.e("TAG","xia kaishile ");
                }
            });
        }else{
            Intent intent = RecordingActivity.newIntent(getActivity(), RecordingActivity.PlayMode.portrait, vid, bitrate, true, true);
            AddLearnRecordModel model = new AddLearnRecordModel();
            model.setGroup_id(mGroupId);
            model.setCourse_id(mCourseId);
            model.setCourse_name(mCourseName);
            model.setChapter_id(mChapterId);
            model.setChapter_name(mChapter);
            model.setSection_id(String.valueOf(id));
            model.setSection_name(title);
            model.setLearn_time(learn_time);
            model.setType("1");
            intent.putExtra("model",model);
            intent.putExtra(RecordingActivity.IS_VLMS_ONLINE, false);
            Log.e("TT22T",model.getCourse_name());
            getActivity().startActivity(intent);
//            getActivity().startActivity(new Intent(getActivity(),RecordingActivity.class));
        }
    }
}
