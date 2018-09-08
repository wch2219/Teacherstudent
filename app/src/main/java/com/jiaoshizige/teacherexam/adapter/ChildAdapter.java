package com.jiaoshizige.teacherexam.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
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
import com.easefun.polyvsdk.PolyvSDKUtil;
import com.easefun.polyvsdk.download.listener.IPolyvDownloaderProgressListener;
import com.easefun.polyvsdk.download.listener.IPolyvDownloaderSpeedListener;
import com.easefun.polyvsdk.download.listener.IPolyvDownloaderStartListener;
import com.easefun.polyvsdk.vo.PolyvVideoVO;
import com.jiaoshizige.teacherexam.R;
import com.jiaoshizige.teacherexam.activity.ImageTextActivity;
import com.jiaoshizige.teacherexam.activity.LiveActivity;
import com.jiaoshizige.teacherexam.activity.PracticeActivity;
import com.jiaoshizige.teacherexam.activity.RecordingActivity;
import com.jiaoshizige.teacherexam.model.AddLearnRecordModel;
import com.jiaoshizige.teacherexam.model.NewCourseCatalogResponse;
import com.jiaoshizige.teacherexam.palyer.PolyvDownloadInfo;
import com.jiaoshizige.teacherexam.palyer.PolyvDownloadSQLiteHelper;
import com.jiaoshizige.teacherexam.utils.Sphelper;
import com.jiaoshizige.teacherexam.utils.ToastUtil;
import com.jiaoshizige.teacherexam.utils.ToolUtils;
import com.jiaoshizige.teacherexam.widgets.PercentageBallView;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * 二级菜单适配器
 * Created by Administrator on 2016/7/18.
 */
public class ChildAdapter extends BaseExpandableListAdapter {
    private Context mContext;
    private ArrayList<NewCourseCatalogResponse.mCatalog> mDatas;
    private List<NewCourseCatalogResponse.mCourseList> mParentsData;
    int mPosition;
    //////////////////////
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
    private String mGroudId;
    AddLearnRecordModel model = new AddLearnRecordModel();
    public ChildAdapter(Context context,List<NewCourseCatalogResponse.mCourseList> parent_data,ArrayList<NewCourseCatalogResponse.mCatalog> data,int pos,String groupid){
        this.mContext = context;
        this.mDatas = data;
        this.mPosition = pos;
        this.mDownloadSQLiteHelper = PolyvDownloadSQLiteHelper.getInstance(mContext);
        this.mParentsData = parent_data;
        this.mGroudId = groupid;
    }
    @Override
    public int getGroupCount() {
        return mDatas!= null?mDatas.size():0;
    }

    @Override
    public int getChildrenCount(int childPosition) {
        return mDatas.get(childPosition).getSon()!=null
                ?mDatas.get(childPosition).getSon().size():0;
    }

    @Override
    public Object getGroup(int parentPosition) {
        return mDatas.get(parentPosition);
    }

    @Override
    public Object getChild(int parentPosition, int childPosition) {
        return mDatas.get(parentPosition).getSon().get(childPosition);
    }

    @Override
    public long getGroupId(int parentPosition) {
        return parentPosition;
    }

    @Override
    public long getChildId(int parentPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    /**
     * 主菜单布局
     * @param parentPosition
     * @param isExpandabled  是否展开
     * @param view
     * @param viewGroup
     * @return
     */
    @Override
    public View getGroupView(int parentPosition, boolean isExpandabled, View view, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if(view == null){
            view = LayoutInflater.from(mContext).inflate(R.layout.child_adapter,null);
            holder = new ViewHolder();
//            holder.mChildPosition = (TextView)view.findViewById(R.id.child_position);
            holder.mImg = (ImageView)view.findViewById(R.id.back_img);
            holder.mChildTitle = (TextView)view.findViewById(R.id.child_title);
            view.setTag(holder);
        }else{
            holder = (ViewHolder) view.getTag();
        }
        if(isExpandabled){
            holder.mImg.setImageResource(R.mipmap.cehua_arrow_small_up);
        }else{
            holder.mImg.setImageResource(R.mipmap.cehua_arrow_small_down);
        }
        holder.mChildTitle.setText(mDatas.get(parentPosition).getTitle());
        return view;
    }
    class ViewHolder {

//        private TextView mChildPosition;
        private ImageView mImg;
        private TextView mChildTitle;
    }
    /**
     * 子菜单布局
     * @param parentPosition
     * @param childPosition
     * @param isExpandabled
     * @param view
     * @param viewGroup
     * @return
     */
    @Override
    public View getChildView(final int parentPosition, final int childPosition, boolean isExpandabled, View view, ViewGroup viewGroup) {
        ChildHolder holder = null;
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(
                    R.layout.child_child, null);
            holder = new ChildHolder();
            holder.mContentTitle = (TextView) view.findViewById(R.id.content_title);
            holder.mContentFlag = (TextView) view.findViewById(R.id.content_flag);
            holder.mContentType = (TextView) view.findViewById(R.id.content_type);
                    view.setTag(holder);
            holder.mBallView = (PercentageBallView) view.findViewById(R.id.progress_ball);
        } else {
            holder = (ChildHolder) view.getTag();
        }


        PolyvDownloader downloader = PolyvDownloaderManager.getPolyvDownloader(vid, bitrate);
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

        switch (Integer.valueOf(mDatas.get(parentPosition).getSon().get(childPosition).getType())){//0章，1视频2试题3图文
            case 0:
//                        holder.setText(R.id.content_type,"文档");
                break;
            case 1:
//                        vid = lists.getVideo();//暂时注掉
               /* if(mDownloadSQLiteHelper != null && mDownloadSQLiteHelper.isAddVid(vid)){
                    progress = (int) mDownloadSQLiteHelper.getInfo(vid).getProgress();
                }

                if(progress == 100){
                    //已经下载
                    state = DOWNLOADED;
                    holder.mContentFlag.setBackground(ContextCompat.getDrawable(mContext,R.mipmap.down_pre));
                }else if(downloader.isDownloading()){
                    //正在下载
                    state = DOWNLOADING;
                    holder.mContentFlag.setText(progress+"%");
                    holder.mContentFlag.setBackground(ContextCompat.getDrawable(mContext,R.mipmap.home_png_speed_default));
                }else if(PolyvDownloaderManager.isWaitingDownload(vid,bitrate)){
                    //等待下载
                    state = WAITED;
                    holder.mContentFlag.setBackground(ContextCompat.getDrawable(mContext,R.mipmap.down));
                }else if(progress == 0){
                    state = WAITED;
                    holder.mContentFlag.setBackground(ContextCompat.getDrawable(mContext,R.mipmap.down));
                }else if(progress > 0 && progress < 100){
                    state = DOWNLOADING;
                    holder.mContentFlag.setText(progress+"%");
                    holder.mContentFlag.setBackground(ContextCompat.getDrawable(mContext,R.mipmap.home_png_speed_default));
                }else{
                    //  暂停下载
                    state = PAUSEED;
                    holder.mContentFlag.setBackground(ContextCompat.getDrawable(mContext,R.mipmap.pause));
                }
                new Thread(new Runnable() {
                    public void run() {
                        try {
                            PolyvVideoVO video = PolyvSDKUtil.loadVideoJSON2Video(vid);
                            downloadInfo = new PolyvDownloadInfo(vid, video.getDuration(),
                                    video.getFileSizeMatchVideoType(bitrate), bitrate, video.getTitle(),progress,mDatas.get(parentPosition).getTitle());
                            if(mDownloadSQLiteHelper != null  && !mDownloadSQLiteHelper.isAdd(downloadInfo)){
                                *//*downloadInfo.setCourse_id(Integer.valueOf(mCourseId));
                                downloadInfo.setParent_id(Integer.valueOf(lists.getParent_id()));
                                downloadInfo.setSid(Integer.valueOf(lists.getId()));*//*
                                downloadInfo.setCourse_id(Integer.valueOf(mDatas.get(parentPosition).getId()));
                                downloadInfo.setParent_id(Integer.valueOf(mDatas.get(parentPosition).getId()));
                                downloadInfo.setSid(Integer.valueOf(mDatas.get(parentPosition).getSon().get(childPosition).getId()));
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
                }).start();*/
                break;
            case 2:

                break;
            case 3:

                break;
            case 4:

                break;
        }

        //1视频2练习3图文4直播
        if(mDatas.get(parentPosition).getSon().get(childPosition).getType().equals("1")){
            if(mParentsData.get(mPosition).getIs_buy().equals("1")){
                holder.mContentType.setBackground(ContextCompat.getDrawable(mContext,R.mipmap.video1_pre));
                holder.mContentFlag.setBackground(ContextCompat.getDrawable(mContext,R.mipmap.down_pre));
            }else{
                holder.mContentType.setBackground(ContextCompat.getDrawable(mContext,R.mipmap.video1));
                holder.mContentFlag.setBackground(ContextCompat.getDrawable(mContext,R.mipmap.down));
            }
            if(mDownloadSQLiteHelper != null && mDownloadSQLiteHelper.isAddVid(vid)){
                progress = (int) mDownloadSQLiteHelper.getInfo(vid).getProgress();
            }

            if(progress == 100){
                //已经下载
                state = DOWNLOADED;
                holder.mContentFlag.setBackground(ContextCompat.getDrawable(mContext,R.mipmap.down_pre));
            }else if(downloader.isDownloading()){
                //正在下载
                state = DOWNLOADING;
                holder.mContentFlag.setText(progress+"%");
                holder.mContentFlag.setBackground(ContextCompat.getDrawable(mContext,R.mipmap.home_png_speed_default));
            }else if(PolyvDownloaderManager.isWaitingDownload(vid,bitrate)){
                //等待下载
                state = WAITED;
                holder.mContentFlag.setBackground(ContextCompat.getDrawable(mContext,R.mipmap.down));
            }else if(progress == 0){
                state = WAITED;
                holder.mContentFlag.setBackground(ContextCompat.getDrawable(mContext,R.mipmap.down));
            }else if(progress > 0 && progress < 100){
                state = DOWNLOADING;
                holder.mContentFlag.setText(progress+"%");
                holder.mContentFlag.setBackground(ContextCompat.getDrawable(mContext,R.mipmap.home_png_speed_default));
            }else{
                //  暂停下载
                state = PAUSEED;
                holder.mContentFlag.setBackground(ContextCompat.getDrawable(mContext,R.mipmap.pause));
            }
            new Thread(new Runnable() {
                public void run() {
                    try {
                        PolyvVideoVO video = PolyvSDKUtil.loadVideoJSON2Video(vid);
                        downloadInfo = new PolyvDownloadInfo(vid, video.getDuration(),
                                video.getFileSizeMatchVideoType(bitrate), bitrate, video.getTitle(),progress,mDatas.get(parentPosition).getTitle());
                        if(mDownloadSQLiteHelper != null  && !mDownloadSQLiteHelper.isAdd(downloadInfo)){
                                /*downloadInfo.setCourse_id(Integer.valueOf(mCourseId));
                                downloadInfo.setParent_id(Integer.valueOf(lists.getParent_id()));
                                downloadInfo.setSid(Integer.valueOf(lists.getId()));*/
                            downloadInfo.setCourse_id(Integer.valueOf(mDatas.get(parentPosition).getId()));
                              downloadInfo.setParent_id(Integer.valueOf(mDatas.get(parentPosition).getId()));
                            downloadInfo.setSid(Integer.valueOf(mDatas.get(parentPosition).getSon().get(childPosition).getId()));
                            downloadInfo.setChapter(mDatas.get(parentPosition).getSon().get(childPosition).getTitle());
                            downloadInfo.setClassname(mDatas.get(parentPosition).getTitle());
                            downloadInfo.setType(mParentsData.get(mPosition).getCourse_type());
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
        }else if(mDatas.get(parentPosition).getSon().get(childPosition).getType().equals("2")){
            if(mParentsData.get(mPosition).getIs_buy().equals("1")){
                holder.mContentType.setBackground(ContextCompat.getDrawable(mContext,R.mipmap.exercise_pre));
                holder.mContentFlag.setBackground(ContextCompat.getDrawable(mContext,R.mipmap.exercise_type_pre));
            }else{
                holder.mContentType.setBackground(ContextCompat.getDrawable(mContext,R.mipmap.exercise));
                holder.mContentFlag.setBackground(ContextCompat.getDrawable(mContext,R.mipmap.exercise_type));
            }

        }else if(mDatas.get(parentPosition).getSon().get(childPosition).getType().equals("3")){
            if(mParentsData.get(mPosition).getIs_buy().equals("1")){
                holder.mContentType.setBackground(ContextCompat.getDrawable(mContext,R.mipmap.image_text_pre));
                holder.mContentFlag.setBackground(ContextCompat.getDrawable(mContext,R.mipmap.image_text_flag_pre));
            }else{
                holder.mContentType.setBackground(ContextCompat.getDrawable(mContext,R.mipmap.image_text));
                holder.mContentFlag.setBackground(ContextCompat.getDrawable(mContext,R.mipmap.image_text_flag));
            }

        }else if(mDatas.get(parentPosition).getSon().get(childPosition).getType().equals("4")){
            if(mParentsData.get(mPosition).getIs_buy().equals("1")){
                holder.mContentType.setBackground(ContextCompat.getDrawable(mContext,R.mipmap.live_pre));
                holder.mContentFlag.setBackground(ContextCompat.getDrawable(mContext,R.mipmap.live_type_pre));
            }else{
                holder.mContentType.setBackground(ContextCompat.getDrawable(mContext,R.mipmap.live));
                holder.mContentFlag.setBackground(ContextCompat.getDrawable(mContext,R.mipmap.live_type));
            }

        }
        holder.mBallView.setmWaterLevel(1f,"");
        holder.mBallView.startWave();
        holder.mContentTitle.setText(mDatas.get(parentPosition).getSon().get(childPosition).getTitle());
        final ChildHolder finalHolder = holder;
        holder.mContentFlag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                model.setGroup_id(mGroudId);
                model.setCourse_id(mParentsData.get(parentPosition).getId());
                model.setCourse_name(mDatas.get(parentPosition).getTitle());
                model.setChapter_id(mDatas.get(parentPosition).getId());
                model.setChapter_name(mDatas.get(parentPosition).getTitle());
                model.setSection_id(String.valueOf(mDatas.get(parentPosition).getSon().get(childPosition).getId()));
                model.setSection_name(mDatas.get(parentPosition).getSon().get(childPosition).getTitle());
                model.setLearn_time(mDatas.get(parentPosition).getSon().get(childPosition).getLearn_time());
                model.setType(mParentsData.get(parentPosition).getCourse_type());
                if(mParentsData.get(mPosition).getIs_buy().equals("1")){

                 if(mDatas.get(parentPosition).getSon().get(childPosition).getType().equals("2")){
                     Intent pIntent = new Intent();
                     pIntent.setClass(mContext, PracticeActivity.class);
                     pIntent.putExtra("mPaperId",mDatas.get(parentPosition).getSon().get(childPosition).getParent_id());
                     pIntent.putExtra("model", model);
                     mContext.startActivity(pIntent);
                }else if(mDatas.get(parentPosition).getSon().get(childPosition).getType().equals("3")){
                     Intent webIntent = new Intent();
                     webIntent.setClass(mContext, ImageTextActivity.class);
                     webIntent.putExtra("content",mDatas.get(parentPosition).getSon().get(childPosition).getContent());
                     webIntent.putExtra("title",mDatas.get(parentPosition).getSon().get(childPosition).getTitle());
                     webIntent.putExtra("id",mDatas.get(parentPosition).getSon().get(childPosition).getParent_id());
                     webIntent.putExtra("model", model);
                     mContext.startActivity(webIntent);
                }else if(mDatas.get(parentPosition).getSon().get(childPosition).getType().equals("4")){
                     Intent lIntent = new Intent();
                     lIntent.putExtra("url",mDatas.get(parentPosition).getSon().get(childPosition).getLive_info().getPull_url());
                     lIntent.putExtra("room_id",mDatas.get(parentPosition).getSon().get(childPosition).getLive_info().getChat_room_id());
                     lIntent.putExtra("model", model);
                     lIntent.putExtra("name", mDatas.get(parentPosition).getSon().get(childPosition).getLive_info().getName());
                     lIntent.setClass(mContext, LiveActivity.class);
                     mContext.startActivity(lIntent);
                }else if(mDatas.get(parentPosition).getSon().get(childPosition).getType().equals("1")){
                     mDownloadSQLiteHelper.getModel();
                     if(ToolUtils.isNetworkAvailable(mContext)) {
                         //仅在wifi下下载
                         if (Sphelper.getString(mContext, "WIFISWITCH", "wifi_switch") != null) {
                             if (Sphelper.getString(mContext, "WIFISWITCH", "wifi_switch").equals("1")) {
                                 if (ToolUtils.isWifi(mContext)) {
                                     DownLoad(finalHolder, vid, mDatas.get(parentPosition).getTitle(), Integer.valueOf(mDatas.get(parentPosition).getId()), Integer.valueOf(mDatas.get(parentPosition).getSon().get(childPosition).getId()));
                                 }else{
                                     ToastUtil.showShortToast("当前处于移动网络");
                                 }
                             } else {
                                 DownLoad(finalHolder, vid, mDatas.get(parentPosition).getTitle(), Integer.valueOf(mDatas.get(parentPosition).getId()), Integer.valueOf(mDatas.get(parentPosition).getSon().get(childPosition).getId()));
                             }
                         }
                     }else{
                         ToastUtil.showShortToast("当前网络不可用，请检查网络设置");
                     }

                 }
                }else{
                    ToastUtil.showShortToast("未购买");
                }
//                ToastUtil.showShortToast(mDatas.get(parentPosition).getTitle()+""+mDatas.get(parentPosition).getSon().get(childPosition).getTitle()+mParentsData.get(mPosition).getName());
            }
        });
        return view;
    }
    class ChildHolder {

        private TextView mContentTitle;
        private TextView mContentFlag;
        private TextView mContentType;
        private PercentageBallView mBallView;
    }
    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }
    public void DownLoad(final ChildHolder holder, final String vid, final String mChapter, final int parentid, final int id){
        PolyvDownloaderManager.getPolyvDownloader(vid, 1);

        if (mDownloadSQLiteHelper.getInfo(vid) != null  &&  mDownloadSQLiteHelper.getInfo(vid).getProgress() < 100) {
            Log.e("TAG3333",state);
            Log.e("TAG3444444",state+mDownloadSQLiteHelper.getInfo(vid).getProgress());
            PolyvDownloader downloader = PolyvDownloaderManager.getPolyvDownloader(vid, 1);
            if(state.equals(DOWNLOADED) || state.equals("下载完成了")){//已下载
                state = DOWNLOADED;
                holder.mContentFlag.setText("100%");
                holder.mContentFlag.setBackground(ContextCompat.getDrawable(mContext,R.mipmap.home_png_speed_default));
                Intent intent = RecordingActivity.newIntent(mContext, RecordingActivity.PlayMode.portrait, vid, bitrate, true, true);

                intent.putExtra("model",model);
                // 在线视频和下载的视频播放的时候只显示播放器窗口，用该参数来控制
                intent.putExtra(RecordingActivity.IS_VLMS_ONLINE, false);
                mContext.startActivity(intent);
            }else if(state.equals(DOWNLOADING)){
                state = PAUSEED;
                holder.mContentFlag.setText("");
                holder.mContentFlag.setBackground(ContextCompat.getDrawable(mContext,R.mipmap.home_icon_suspend_pressed));
                downloader.stop();
            }else if(state.equals(PAUSEED) || state.equals(WAITED)){
                state = DOWNLOADING;
                holder.mContentFlag.setText(progress+"%");
                holder.mContentFlag.setBackground(ContextCompat.getDrawable(mContext,R.mipmap.home_png_speed_default));
                downloader.start();
            }
            downloader.setPolyvDownloadSpeedListener(new IPolyvDownloaderSpeedListener() {
                @Override
                public void onSpeed(int speed) {
                    //下载速度监听
                    Log.e("TAG","xia sudu "+speed+ Formatter.formatShortFileSize(mContext, speed));
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
                    holder.mContentFlag.setText(progress+"%");
                    holder.mContentFlag.setBackground(ContextCompat.getDrawable(mContext,R.mipmap.home_png_speed_default));
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
                    holder.mContentFlag.setText("100%");
                    holder.mContentFlag.setBackground(ContextCompat.getDrawable(mContext,R.mipmap.home_png_speed_default));
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
            Intent intent = RecordingActivity.newIntent(mContext, RecordingActivity.PlayMode.portrait, vid, bitrate, true, true);
            intent.putExtra("model",model);
            Log.e("TT555T",model.getCourse_name());
            intent.putExtra(RecordingActivity.IS_VLMS_ONLINE, false);
            mContext.startActivity(intent);
            Log.e("TAGS","OK"+progress+"||||"+mDownloadSQLiteHelper.getInfo(vid));
        }
    }
}
