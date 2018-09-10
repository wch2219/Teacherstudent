package com.jiaoshizige.teacherexam.activity.aaa.xx.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.easefun.polyvsdk.PolyvDownloader;
import com.easefun.polyvsdk.PolyvDownloaderErrorReason;
import com.easefun.polyvsdk.PolyvDownloaderManager;
import com.easefun.polyvsdk.download.listener.IPolyvDownloaderProgressListener;
import com.easefun.polyvsdk.download.listener.IPolyvDownloaderSpeedListener;
import com.jiaoshizige.teacherexam.R;
import com.jiaoshizige.teacherexam.activity.aaa.xx.adapter.DownManagerproAdapter;
import com.jiaoshizige.teacherexam.activity.aaa.xx.bean.ChapterBean;
import com.jiaoshizige.teacherexam.activity.aaa.xx.utils.FormatUtil;
import com.jiaoshizige.teacherexam.activity.aaa.xx.utils.MyRecyclerView;
import com.jiaoshizige.teacherexam.palyer.PolyvDownloadInfo;
import com.jiaoshizige.teacherexam.palyer.PolyvDownloadSQLiteHelper;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 下载管理children
 */
public class DownManagerProssActivity extends AppCompatActivity implements DownManagerproAdapter.CheckListener, DownManagerproAdapter.DownSpenListener {

    @BindView(R.id.back_im)
    TextView backIm;
    @BindView(R.id.toolbar_subtitle)
    TextView toolbarSubtitle;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar_submenu)
    TextView toolbarSubmenu;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.ll_alldown)
    LinearLayout llAlldown;
    @BindView(R.id.ll_allstop)
    LinearLayout llAllstop;
    @BindView(R.id.rv_list)
    MyRecyclerView rvList;
    @BindView(R.id.ll_closecheck)
    LinearLayout llClosecheck;
    @BindView(R.id.ll_delete)
    LinearLayout llDelete;
    @BindView(R.id.ll_bottom)
    LinearLayout llBottom;
    @BindView(R.id.tv_selete)
    TextView tvSelete;
    @BindView(R.id.tv_delete)
    TextView tvDelete;
    @BindView(R.id.cb_check)
    CheckBox cbCheck;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_downType)
    TextView tvDownType;
    @BindView(R.id.tv_totalsize)
    TextView tvTotalsize;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.ll_down)
    LinearLayout llDown;
    @BindView(R.id.ll_parent)
    LinearLayout llParent;
    private Context ctx;
    private String title;
    private DownManagerproAdapter downManagerproAdapter;
    private int checkNum;
    private int course_id;
    private List<ChapterBean> allChapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_down_manager_pross);
        ButterKnife.bind(this);
        title = getIntent().getStringExtra("title");
        course_id = getIntent().getIntExtra("course_id", 0);
        ctx = this;
        initTabBar();
        initView();
    }

    private void initView() {
        toolbarTitle.setText(title);
        toolbarSubtitle.setText("编辑");
        toolbarSubtitle.setTextColor(getResources().getColor(R.color.blue));
        LinearLayoutManager manager = new LinearLayoutManager(ctx);
        rvList.setLayoutManager(manager);

        showData();
    }

    private void showData() {
        allChapter = PolyvDownloadSQLiteHelper.getInstance(ctx).getAllChapter(course_id);
        screenData();
        showDownPro();

        downManagerproAdapter = new DownManagerproAdapter(ctx, allChapter);
        downManagerproAdapter.setCheckListener(this);
        downManagerproAdapter.setDownSpenListener(this);
        rvList.setAdapter(downManagerproAdapter);
    }

    private void showDownPro() {
        if (downIngData != null) {
            llParent.setVisibility(View.VISIBLE);
            llDown.setVisibility(View.VISIBLE);
            tvTitle.setText(downIngData.getTitle());
            tvDownType.setText(FormatUtil.sizeFormatNum2String(downIngData.getDownspend()));
            tvTotalsize.setText(FormatUtil.sizeFormatNum2String(downIngData.getFilesize()));
            progressBar.setMax(100);
            progressBar.setProgress(downIngData.getProgress());
            PolyvDownloader downloader = PolyvDownloaderManager.getPolyvDownloader(downIngData.getVid(), downIngData.getBitrate());
            downloader.setPolyvDownloadSpeedListener(new IPolyvDownloaderSpeedListener() {
                @Override
                public void onSpeed(int i) {
                    PolyvDownloadSQLiteHelper.getInstance(ctx).upDownSpan(i,downIngData.getVid(),downIngData.getBitrate());
                    downIngData = PolyvDownloadSQLiteHelper.getInstance(ctx).getDownSpan(downIngData.getVid(), downIngData.getBitrate());
                    showDownPro();
                }
            });
            downloader.setPolyvDownloadProressListener(new IPolyvDownloaderProgressListener() {
                @Override
                public void onDownload(long l, long l1) {

                }

                @Override
                public void onDownloadSuccess() {
                        showData();
                }

                @Override
                public void onDownloadFail(@NonNull PolyvDownloaderErrorReason polyvDownloaderErrorReason) {

                }
            });
        }else{
            llParent.setVisibility(View.GONE);

        }
    }

    /**
     * 筛选数据
     */
    private void screenData() {
        if (allChapter != null) {
            for (int i = 0; i < allChapter.size(); i++) {
                ChapterBean chapterBean = allChapter.get(i);
                List<PolyvDownloadInfo> infos = chapterBean.getInfos();
                if (infos.size() == 0) {
                    allChapter.remove(i);
                    screenData();
                }
                for (int j = 0; j < infos.size(); j++) {
                    PolyvDownloadInfo polyvDownloadInfo = infos.get(j);
                    PolyvDownloader downloader = PolyvDownloaderManager.getPolyvDownloader(polyvDownloadInfo.getVid(), polyvDownloadInfo.getBitrate());
                    if (downloader.isDownloading()) {
                        downIngData = polyvDownloadInfo;
                        infos.remove(j);
                        screenData();
                    }
                }

            }

        }
    }

    private PolyvDownloadInfo downIngData;
    public void initTabBar() {
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.back);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:

                finish();

                break;
        }

        return true;
    }

    @SuppressLint("WrongConstant")
    @OnClick({R.id.toolbar_subtitle, R.id.ll_delete, R.id.ll_closecheck})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.toolbar_subtitle:
                llBottom.setVisibility(llBottom.getVisibility() == 0 ? View.GONE : View.VISIBLE);
                downManagerproAdapter.showCheck(llBottom.getVisibility() == 0 ? true : false);
                setAllCheckOrUnCheck(false);
                break;
            case R.id.ll_delete:
                PolyvDownloadSQLiteHelper downloadSQLiteHelper = PolyvDownloadSQLiteHelper.getInstance(ctx);
//
                int seleteTotalSize = 0;
                for (ChapterBean chapterBean : allChapter) {
                    seleteTotalSize += chapterBean.getInfos().size();
                }
                if (checkNum == seleteTotalSize) {
                    //全部删除


                    downloadSQLiteHelper.deleteManager(course_id);
                    allChapter.clear();
                } else {
                    for (ChapterBean chapterBean : allChapter) {
                        List<PolyvDownloadInfo> downloadInfos = chapterBean.getInfos();
                        for (PolyvDownloadInfo downloadInfo : downloadInfos) {
                            if (downloadInfo.isCheck()) {
                                downloadSQLiteHelper.deletecontent(downloadInfo.getVid());
                                PolyvDownloader downloader = PolyvDownloaderManager.getPolyvDownloader(downloadInfo.getVid(), downloadInfo.getBitrate());
                                downloader.deleteVideo(downloadInfo.getVid(), downloadInfo.getBitrate());
                            }
                        }
                    }
                }
                allChapter = downloadSQLiteHelper.getAllChapter(course_id);
                downManagerproAdapter.notifyDataSetChanged();
                break;
            case R.id.ll_closecheck:
                String selete = tvSelete.getText().toString().trim();
                if (selete.equals("全选")) {

                    setAllCheckOrUnCheck(true);
                } else {
                    setAllCheckOrUnCheck(false);
                }
                break;
        }
    }

    @Override
    public void checkPosition(int position, int parentposition) {
        List<PolyvDownloadInfo> downloadInfos = allChapter.get(parentposition).getInfos();
        PolyvDownloadInfo downloadInfo = downloadInfos.get(position);
        downloadInfo.setCheck(!downloadInfo.isCheck());
        checkNum = 0;
        for (PolyvDownloadInfo info : downloadInfos) {
            if (downloadInfo.isCheck()) {
                checkNum++;
            }
            tvDelete.setText("删除(" + checkNum + ")");
            if (checkNum == downloadInfos.size()) {
                tvSelete.setText("取消全选");
            } else {
                tvSelete.setText("全选");
            }
        }
        downManagerproAdapter.notifyDataSetChanged();
    }

    public void setAllCheckOrUnCheck(boolean ischeck) {
        checkNum = 0;
        for (ChapterBean chapterBean : allChapter) {
            List<PolyvDownloadInfo> downloadInfos = chapterBean.getInfos();
            for (PolyvDownloadInfo downloadInfo : downloadInfos) {
                downloadInfo.setCheck(ischeck);
                if (downloadInfo.isCheck()) {
                    checkNum++;
                }
                tvDelete.setText("删除(" + checkNum + ")");
                if (checkNum == downloadInfos.size()) {
                    tvSelete.setText("取消全选");
                } else {
                    tvSelete.setText("全选");
                }
            }
        }


        downManagerproAdapter.notifyDataSetChanged();
    }

    @Override
    public void upUi() {
//        showData();
    }
}
