package com.jiaoshizige.teacherexam.activity.aaa.xx.adapter;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.easefun.polyvsdk.PolyvDownloader;
import com.easefun.polyvsdk.PolyvDownloaderManager;
import com.easefun.polyvsdk.PolyvSDKUtil;
import com.easefun.polyvsdk.vo.PolyvVideoVO;
import com.jiaoshizige.teacherexam.R;
import com.jiaoshizige.teacherexam.activity.aaa.xx.bean.ChapterBean;
import com.jiaoshizige.teacherexam.activity.aaa.xx.bean.DownManagerBean;
import com.jiaoshizige.teacherexam.activity.aaa.xx.bean.NewCourseListBean;
import com.jiaoshizige.teacherexam.activity.aaa.xx.utils.MyRecyclerView;
import com.jiaoshizige.teacherexam.palyer.PolyvDownloadInfo;
import com.jiaoshizige.teacherexam.palyer.PolyvDownloadSQLiteHelper;
import com.zhy.adapter.recyclerview.CommonAdapter;

import org.json.JSONException;

import java.util.List;

/**
 * Created by Administrator on 2018/9/4.
 * 视频下载界面
 */
public class VideoDownAdapter extends RecyclerView.Adapter<VideoDownAdapter.ViewHolder> {
    private List<NewCourseListBean.DataBean.CourseListBean> course_list;
    private Context ctx;
    private int bitrate = 2;//清晰度 1标清 2 高清 3 超清
    private static PolyvDownloadSQLiteHelper mDownloadSQLiteHelper;
    private int progress = 0;
    private String state = "";
    private static final String DOWNLOADED = "已下载";
    private static final String DOWNLOADING = "正在下载";
    private static final String PAUSEED = "暂停下载";
    private static final String WAITED = "等待下载";
    private String mType;
    private String mCourseId;
    private PolyvDownloadInfo downloadInfo;
    private String covermap;
    private String mName;
    public VideoDownAdapter(Context ctx, List<NewCourseListBean.DataBean.CourseListBean> course_list, int bitrate,String type,String mCourseId,String covermap,String name) {
        super();
        this.ctx = ctx;
        this.course_list = course_list;
        this.bitrate = bitrate;
        this.mDownloadSQLiteHelper = PolyvDownloadSQLiteHelper.getInstance(ctx);
        this.mType = type;
        this.mCourseId = mCourseId;
        this.covermap = covermap;
        this.mName = name;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(ctx).inflate(R.layout.item_sanjileibiao, parent, false);

        return new ViewHolder(view);
    }
    public void setBitrate(int bitrate){
        this.bitrate = bitrate;
        this.notifyDataSetChanged();
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, final int parentposition) {
        final NewCourseListBean.DataBean.CourseListBean courseListBean = course_list.get(parentposition);
        List<NewCourseListBean.DataBean.CourseListBean.CatalogBean> catalog = courseListBean.getCatalog();
        Log.i("wch", catalog.size() + "数量");
        holder.tv_title.setText(courseListBean.getName());

        LinearLayoutManager manager = new LinearLayoutManager(ctx);
        holder.rv_list.setLayoutManager(manager);
        holder.rv_list.setAdapter(new CommonAdapter<NewCourseListBean.DataBean.CourseListBean.CatalogBean>(ctx, R.layout.item_down_list, catalog) {
            @Override
            protected void convert(final com.zhy.adapter.recyclerview.base.ViewHolder holder, final NewCourseListBean.DataBean.CourseListBean.CatalogBean catalogBean, final int position) {
                //Todo
                holder.setText(R.id.tv_name, catalogBean.getTitle());
                String video = catalogBean.getVideo();
                final PolyvDownloader downloader = PolyvDownloaderManager.getPolyvDownloader(video, bitrate);
                if (mDownloadSQLiteHelper != null && mDownloadSQLiteHelper.isAddVid(video)) {
                    progress = (int) mDownloadSQLiteHelper.getInfo(video).getProgress();

                } else {
                    progress = 0;
                }
                if (progress >=100) {
                    //已经下载
                    state = DOWNLOADED;
                } else if (downloader.isDownloading()) {
                    //正在下载
                    state = DOWNLOADING;
                } else if (PolyvDownloaderManager.isWaitingDownload(video, bitrate)) {
                    //等待下载
                    state = WAITED;
                }else if (progress>0&&progress<100) {
                    state = DOWNLOADING;
                } else if (progress == 0) {
                    state = WAITED;
                } else {
                    //  暂停下载
                    state = PAUSEED;
                }
                holder.setText(R.id.tv_downType, state);

                holder.setOnClickListener(R.id.ll_parent, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        downid = courseListBean.getId();
                        String name = courseListBean.getName();
                        if (backInterface != null&&!state.equals(DOWNLOADED)&&!state.equals(DOWNLOADING)) {
                            VideoDownAdapter.this.catalogBean = catalogBean;
                            clickposition = position;
                            parentpositionclick = parentposition;
                            String vid = catalogBean.getVideo();
                            new DownAsyncTast().execute(vid);
                            holder.setText(R.id.tv_downType,"正在下载");
                            backInterface.backDown(catalogBean.getVideo(),downloadInfo,bitrate);
                        }
                    }
                });
            }
        });
    }
    private NewCourseListBean.DataBean.CourseListBean.CatalogBean catalogBean;
    private int clickposition;
    private int parentpositionclick;
    private String downid;
    @Override
    public int getItemCount() {
        return course_list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_title;
        public MyRecyclerView rv_list;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_title = itemView.findViewById(R.id.tv_title);
            rv_list = itemView.findViewById(R.id.rv_list);
        }
    }

    public interface DownVideoBackInterface {
        void backDown(String vid,PolyvDownloadInfo downloadInfo,int bitrate);
    }

    public static DownVideoBackInterface backInterface;

    public static void setOnDownVideoBackInterface(DownVideoBackInterface videoBackInterface) {
        backInterface = videoBackInterface;
    }
    private class DownAsyncTast extends AsyncTask<String,Void,PolyvVideoVO>{
        @Override
        protected PolyvVideoVO doInBackground(String... strings) {

            try {
                return PolyvSDKUtil.loadVideoJSON2Video(strings[0]);
            } catch (JSONException e) {
                e.printStackTrace();
            }
          return null;
        }

        @Override
        protected void onPostExecute(PolyvVideoVO video) {
            super.onPostExecute(video);
            if (video == null) {
                Toast.makeText(ctx, "操作失败,重新下载", Toast.LENGTH_SHORT).show();
                return;
            }
            if (video.getDuration() == null) {
                Toast.makeText(ctx, "操作失败,重新下载", Toast.LENGTH_SHORT).show();
                return;
            }
            downloadInfo = new PolyvDownloadInfo(video.getVid(), video.getDuration(),
                    video.getFileSizeMatchVideoType(bitrate), bitrate, video.getTitle(), progress, catalogBean.getTitle());
            if (mDownloadSQLiteHelper != null && !mDownloadSQLiteHelper.isAdd(downloadInfo)) {
                downloadInfo.setClassname(course_list.get(parentpositionclick).getName());
                if (mType.equals("1")) {
                    downloadInfo.setClass_id(Integer.valueOf(course_list.get(parentpositionclick).getId()));
                } else {
                    downloadInfo.setClass_id(Integer.valueOf(mCourseId));
                }
                downloadInfo.setChapter(course_list.get(parentpositionclick).getName());
                downloadInfo.setChapter_id(String.valueOf(course_list.get(parentpositionclick).getId()));
                Log.e("TA", catalogBean.getTitle());
                downloadInfo.setSection_name(catalogBean.getTitle());
                downloadInfo.setSection_id(String.valueOf(catalogBean.getId()));
                downloadInfo.setCourse_id(Integer.valueOf(mCourseId));
                downloadInfo.setLearn_time(String.valueOf(catalogBean.getLearn_time()));
                downloadInfo.setType(mType);
                mDownloadSQLiteHelper.insert(downloadInfo);
                notifyDataSetChanged();
                DownManagerBean managerBean = new DownManagerBean();
                managerBean.setCovermap(covermap);
                managerBean.setCourse_id(Integer.valueOf(mCourseId));
                managerBean.setTitle(mName);
                mDownloadSQLiteHelper.insertMannger(managerBean);
                ChapterBean chapterBean = new ChapterBean();
                chapterBean.setChapter_id(Integer.parseInt(course_list.get(parentpositionclick).getId()));
                chapterBean.setCourse_id(Integer.parseInt(mCourseId));
                chapterBean.setChapter(course_list.get(parentpositionclick).getName());
                mDownloadSQLiteHelper.inserChapter(chapterBean);
            }
        }
    }
}
