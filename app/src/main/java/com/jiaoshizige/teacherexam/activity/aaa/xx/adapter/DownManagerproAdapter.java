package com.jiaoshizige.teacherexam.activity.aaa.xx.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.easefun.polyvsdk.PolyvDownloader;
import com.easefun.polyvsdk.PolyvDownloaderManager;
import com.easefun.polyvsdk.download.listener.IPolyvDownloaderSpeedListener;
import com.jiaoshizige.teacherexam.R;
import com.jiaoshizige.teacherexam.activity.aaa.xx.bean.ChapterBean;
import com.jiaoshizige.teacherexam.activity.aaa.xx.utils.FormatUtil;
import com.jiaoshizige.teacherexam.activity.aaa.xx.utils.MessageEvent;
import com.jiaoshizige.teacherexam.activity.aaa.xx.utils.WcHLogUtils;
import com.jiaoshizige.teacherexam.palyer.PolyvDownloadInfo;
import com.jiaoshizige.teacherexam.palyer.PolyvDownloadSQLiteHelper;
import com.zhy.adapter.recyclerview.CommonAdapter;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/9/5.
 */

public class DownManagerproAdapter extends RecyclerView.Adapter<DownManagerproAdapter.ViewHolder> {
    private Context ctx;
    private List<ChapterBean> allChapter;
    private int DOWNING = 1;//正在下载
    private int DOWNPAUSE = 2;//暂停下载
    private int DOWNPREPARE = 3;//准备下载
    private int DOWNTYPE = -1;
    public DownManagerproAdapter(Context ctx, List<ChapterBean> allChapter) {
        super();
        this.ctx = ctx;
        this.allChapter = allChapter;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(ctx).inflate(R.layout.item_manager_pross, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int parentposition) {
        ChapterBean chapterBean = allChapter.get(parentposition);
        holder.tvTitle.setText(chapterBean.getChapter());
        LinearLayoutManager manager = new LinearLayoutManager(ctx);
        holder.rvList.setLayoutManager(manager);
        holder.rvList.setAdapter(new CommonAdapter<PolyvDownloadInfo>(ctx, R.layout.item_manager_pross_item, chapterBean.getInfos()) {
            @Override
            protected void convert(final com.zhy.adapter.recyclerview.base.ViewHolder holder, final PolyvDownloadInfo polyvDownloadInfo, final int position) {
                holder.setVisible(R.id.cb_check, isshowCheck);
                holder.setChecked(R.id.cb_check, polyvDownloadInfo.isCheck());
                holder.setText(R.id.tv_title, polyvDownloadInfo.getTitle());
                int progress = polyvDownloadInfo.getProgress();
                if (progress < 100) {
                    holder.setVisible(R.id.ll_down, true);
                    holder.setProgress(R.id.progressBar, polyvDownloadInfo.getProgress(), 100);
                    holder.setText(R.id.tv_totalsize, FormatUtil.sizeFormatNum2String(polyvDownloadInfo.getFilesize()));
                    //取得下载类实例
                    PolyvDownloader downloader = PolyvDownloaderManager.getPolyvDownloader(polyvDownloadInfo.getVid(), polyvDownloadInfo.getBitrate());
                    if (downloader.isDownloading()) {//正在下载
                        DOWNTYPE = DOWNING;
                        WcHLogUtils.I("显示速度:"+polyvDownloadInfo.getDownspend()+"------下载进度:"+polyvDownloadInfo.getProgress());
                        holder.setText(R.id.tv_downType, FormatUtil.sizeFormatNum2String(polyvDownloadInfo.getDownspend()));
                        downloader.setPolyvDownloadSpeedListener(new IPolyvDownloaderSpeedListener() {
                            @Override
                            public void onSpeed(int i) {
                                WcHLogUtils.I("下载速度"+i);
                                PolyvDownloadSQLiteHelper.getInstance(ctx).upDownSpan(i,polyvDownloadInfo.getVid(),polyvDownloadInfo.getBitrate());
                                if (downSpenListener != null) {
                                    downSpenListener.upUi();
                                }
                            }
                        });
                    } else {
                        long progree = PolyvDownloadSQLiteHelper.getInstance(ctx).getProgree(polyvDownloadInfo.getVid());
                        if (progree>0) {
                            holder.setText(R.id.tv_downType, "暂停下载");
                            DOWNTYPE = DOWNPAUSE;
                        }else{
                            DOWNTYPE = DOWNPREPARE;
                            holder.setText(R.id.tv_downType, "准备下载");
                        }
                    }

                } else {
                    holder.setVisible(R.id.ll_down, false);
                }
                holder.setOnClickListener(R.id.ll_parent, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //取得下载类实例
                        long progree = PolyvDownloadSQLiteHelper.getInstance(ctx).getProgree(polyvDownloadInfo.getVid());
                        PolyvDownloader downloader = PolyvDownloaderManager.getPolyvDownloader(polyvDownloadInfo.getVid(), polyvDownloadInfo.getBitrate());
                        if (downloader.isDownloading()) {
                            downloader.stop();//停止下载
                            DownManagerproAdapter.this.notifyDataSetChanged();
                        }else if (progree<100) {
                            EventBus.getDefault().post(new MessageEvent(polyvDownloadInfo.getVid(),polyvDownloadInfo.getBitrate()));
                        }else if (checkListener != null) {
                            checkListener.checkPosition(position, parentposition);
                        }
                        if (downSpenListener != null) {
                            downSpenListener.upUi();
                        }
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return allChapter.size();
    }

    private boolean isshowCheck;

    public void showCheck(boolean ischeck) {
        this.isshowCheck = ischeck;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_title)
        TextView tvTitle;
        @BindView(R.id.rv_list)
        RecyclerView rvList;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public interface CheckListener {
        void checkPosition(int position, int parentposition);
    }

    private CheckListener checkListener;

    public void setCheckListener(CheckListener checkListener) {
        this.checkListener = checkListener;

    }

    private DownSpenListener downSpenListener;

    public interface DownSpenListener {
        void upUi();
    }

    public void setDownSpenListener(DownSpenListener downSpenListener){
        this.downSpenListener = downSpenListener;
    }
}
