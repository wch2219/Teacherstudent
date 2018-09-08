package com.jiaoshizige.teacherexam.activity.aaa.xx.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.easefun.polyvsdk.PolyvDownloader;
import com.easefun.polyvsdk.PolyvDownloaderManager;
import com.jiaoshizige.teacherexam.R;
import com.jiaoshizige.teacherexam.activity.aaa.xx.adapter.DownManagerproAdapter;
import com.jiaoshizige.teacherexam.activity.aaa.xx.bean.ChapterBean;
import com.jiaoshizige.teacherexam.palyer.PolyvDownloadInfo;
import com.jiaoshizige.teacherexam.palyer.PolyvDownloadSQLiteHelper;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 下载管理children
 */
public class DownManagerProssActivity extends AppCompatActivity implements DownManagerproAdapter.CheckListener {

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
    RecyclerView rvList;
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
        course_id = getIntent().getIntExtra("course_id",0);
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
        allChapter = PolyvDownloadSQLiteHelper.getInstance(ctx).getAllChapter(course_id);
        downManagerproAdapter = new DownManagerproAdapter(ctx, allChapter);
        downManagerproAdapter.setCheckListener(this);
        rvList.setAdapter(downManagerproAdapter);
    }

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
                    seleteTotalSize +=chapterBean.getInfos().size();
                }
                if (checkNum == seleteTotalSize) {
                    //全部删除


                    downloadSQLiteHelper.deleteManager(course_id);
                    allChapter.clear();
                }else{
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
                }else{
                    setAllCheckOrUnCheck(false);
                }
                break;
        }
    }

    @Override
    public void checkPosition(int position,int parentposition) {
        List<PolyvDownloadInfo> downloadInfos = allChapter.get(parentposition).getInfos();
        PolyvDownloadInfo downloadInfo = downloadInfos.get(position);
        downloadInfo.setCheck(!downloadInfo.isCheck());
       checkNum = 0;
        for (PolyvDownloadInfo info : downloadInfos) {
            if (downloadInfo.isCheck()) {
                checkNum++;
            }
            tvDelete.setText("删除("+checkNum+")");
            if (checkNum == downloadInfos.size()) {
                tvSelete.setText("取消全选");
            }else{
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
                tvDelete.setText("删除("+ checkNum +")");
                if (checkNum == downloadInfos.size()) {
                    tvSelete.setText("取消全选");
                }else{
                    tvSelete.setText("全选");
                }
            }
        }


        downManagerproAdapter.notifyDataSetChanged();
    }
}
