package com.jiaoshizige.teacherexam.activity.aaa.xx.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.easefun.polyvsdk.PolyvDownloader;
import com.easefun.polyvsdk.PolyvDownloaderManager;
import com.jiaoshizige.teacherexam.R;
import com.jiaoshizige.teacherexam.activity.aaa.xx.activity.DownManagerProssActivity;
import com.jiaoshizige.teacherexam.activity.aaa.xx.bean.DownManagerBaseBean;
import com.jiaoshizige.teacherexam.activity.aaa.xx.bean.DownManagerBean;
import com.jiaoshizige.teacherexam.activity.aaa.xx.utils.FormatUtil;
import com.jiaoshizige.teacherexam.palyer.PolyvDownloadInfo;
import com.jiaoshizige.teacherexam.palyer.PolyvDownloadSQLiteHelper;
import com.zhy.adapter.recyclerview.CommonAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/9/4.
 * 下载管理
 */

public class DownManagerAdapter extends RecyclerView.Adapter<DownManagerAdapter.ViewHolder> {
    private Context ctx;
    private DownManagerBaseBean baseBean;
    public DownManagerAdapter(Context ctx,DownManagerBaseBean baseBean) {
        super();
        this.ctx = ctx;
       this.baseBean = baseBean;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(ctx).inflate(R.layout.item_down_manger1, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int parentposition) {
        final LinearLayoutManager manager = new LinearLayoutManager(ctx);
        holder.rvList.setLayoutManager(manager);
        final int itemCount = getItemCount();
        List<DownManagerBean> beans;
        if (itemCount == 2) {
            if (parentposition == 0) {//正在下载
                holder.tvTittle.setText("正在下载");
                beans = baseBean.getDownIngBeans();
            }else{//已下载
                holder.tvTittle.setText("已下载");
                beans = baseBean.getDownSuccBeans();
            }
        }else{
            holder.tvTittle.setText("已下载");
            beans = baseBean.getDownSuccBeans();
        }
        holder.rvList.setAdapter(new CommonAdapter<DownManagerBean>(ctx,R.layout.item_downmanager,beans) {
            @Override
            protected void convert(com.zhy.adapter.recyclerview.base.ViewHolder holder, final DownManagerBean managerBean, final int position) {

                Glide.with(ctx).load(managerBean.getCovermap()).into((ImageView) holder.getView(R.id.iv_pic));
                holder.setText(R.id.tv_title,managerBean.getTitle());
                if (itemCount == 2) {
                    if (parentposition == 0) {//正在下载
                        holder.setVisible(R.id.tv_downsuccess,false);
                        holder.setVisible(R.id.ll_down,true);
                        for (PolyvDownloadInfo downloadInfo : managerBean.getDownloadInfos()) {
                            String vid = downloadInfo.getVid();
                            int bitrate = downloadInfo.getBitrate();
                            PolyvDownloader downloader = PolyvDownloaderManager.getPolyvDownloader(vid, bitrate);
                            if (downloader.isDownloading()) {
                                holder.setText(R.id.tv_smalltitle,downloadInfo.getTitle());
                                holder.setText(R.id.tv_downType,"正在下载");
                                long progree = PolyvDownloadSQLiteHelper.getInstance(ctx).getProgree(vid);
                                holder.setProgress(R.id.progress, (int) progree,100);
                                holder.setText(R.id.t_filesize,FormatUtil.sizeFormatNum2String(downloadInfo.getFilesize()));
                            }
                        }
                    }else{//已下载
                        holder.setVisible(R.id.tv_downsuccess,true);
                        holder.setVisible(R.id.ll_down,false);
                        holder.setText(R.id.tv_downsuccess,"已缓存"+managerBean.getDownloadInfos().size()+"节");
                    }
                }else{
                    holder.setVisible(R.id.tv_downsuccess,true);
                    holder.setVisible(R.id.ll_down,false);
                    holder.setText(R.id.tv_downsuccess,"已缓存"+managerBean.getDownloadInfos().size()+"节");
                }
                holder.setVisible(R.id.cb_check,isshowCheck);
                holder.setChecked(R.id.cb_check,managerBean.isCheck());
                holder.setOnClickListener(R.id.ll_parent, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (isshowCheck) {
                            if (checkListener != null) {
                                checkListener.checkPosition(position,itemCount);
                            }

                        }else {
                            Intent intent = new Intent(ctx, DownManagerProssActivity.class);
                            intent.putExtra("title", managerBean.getTitle());
                            intent.putExtra("course_id", managerBean.getCourse_id());
                            ctx.startActivity(intent);
                        }
                    }
                });
            }
        });
    }
    @Override
    public int getItemCount() {
        List<DownManagerBean> downIngBeans = baseBean.getDownIngBeans();
        if (downIngBeans.size()>0) {
            return 2;
        }
        return 1;
    }

     class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_tittle)
        TextView tvTittle;
        @BindView(R.id.rv_list)
        RecyclerView rvList;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
    private boolean isshowCheck;
    public void showCheck(boolean ischeck) {
        this.isshowCheck = ischeck;
        notifyDataSetChanged();
    }
    public interface CheckListener{
        void checkPosition(int position,int parentType);
    }

    private CheckListener checkListener;
    public void setCheckListener(CheckListener checkListener){
        this.checkListener = checkListener;

    }
}
