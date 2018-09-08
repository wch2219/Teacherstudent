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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jiaoshizige.teacherexam.R;
import com.jiaoshizige.teacherexam.activity.aaa.xx.adapter.DownManagerAdapter;
import com.jiaoshizige.teacherexam.activity.aaa.xx.bean.DownManagerBaseBean;
import com.jiaoshizige.teacherexam.activity.aaa.xx.bean.DownManagerBean;
import com.jiaoshizige.teacherexam.activity.aaa.xx.utils.WcHLogUtils;
import com.jiaoshizige.teacherexam.palyer.PolyvDownloadInfo;
import com.jiaoshizige.teacherexam.palyer.PolyvDownloadSQLiteHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 下载管理界面
 */

public class DownVideosMangerActivity extends AppCompatActivity implements DownManagerAdapter.CheckListener {

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
    @BindView(R.id.nodata)
    ImageView nodata;
    @BindView(R.id.course_no_data)
    LinearLayout courseNoData;
    @BindView(R.id.rv_list)
    RecyclerView rvList;
    @BindView(R.id.check_all)
    TextView checkAll;
    @BindView(R.id.delete)
    TextView delete;
    @BindView(R.id.ll)
    LinearLayout ll;
    private Context ctx;
    private DownManagerAdapter downManagerAdapter;
    private DownManagerBaseBean baseBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_down_videos_manger);
        ButterKnife.bind(this);
        this.ctx = this;
        initView();

    }

    private void initView() {
        toolbarTitle.setText("下载管理");
        toolbarSubtitle.setText("管理");
        toolbarSubtitle.setTextColor(getResources().getColor(R.color.blue));
        initTabBar();

    }

    @Override
    protected void onResume() {
        super.onResume();
        getAll();
    }

    private void getAll() {
        List<DownManagerBean> all = PolyvDownloadSQLiteHelper.getInstance(ctx).getAllData();
        if (all == null || all.size() == 0) {
            courseNoData.setVisibility(View.VISIBLE);
            rvList.setVisibility(View.GONE);
        } else {
            List<DownManagerBean> DownIngBeans = new ArrayList<>();
            List<DownManagerBean> DownSuccBeans = new ArrayList<>();
            for (DownManagerBean managerBean : all) {
                List<PolyvDownloadInfo> downloadInfos = managerBean.getDownloadInfos();
                for (PolyvDownloadInfo downloadInfo : downloadInfos) {
                    int progress = downloadInfo.getProgress();
                    if (progress < 100) {//正在下载
                        managerBean.setDownNum(managerBean.getDownNum() + 1);
                    }
                }
                if (managerBean.getDownNum() > 0) {
                    DownIngBeans.add(managerBean);
                } else {
                    DownSuccBeans.add(managerBean);
                }
            }
            baseBean = new DownManagerBaseBean();
            baseBean.setDownIngBeans(DownIngBeans);
            baseBean.setDownSuccBeans(DownSuccBeans);
            LinearLayoutManager manager = new LinearLayoutManager(ctx);
            rvList.setLayoutManager(manager);
            downManagerAdapter = new DownManagerAdapter(ctx, baseBean);
            rvList.setAdapter(downManagerAdapter);
            downManagerAdapter.setCheckListener(this);
        }
        WcHLogUtils.I(new Gson().toJson(all));
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
    @OnClick({R.id.toolbar_subtitle, R.id.check_all, R.id.delete})
    public void onViewClicked(View view) {
        List<DownManagerBean> downIngBeans = baseBean.getDownIngBeans();
        List<DownManagerBean> downSuccBeans = baseBean.getDownSuccBeans();
        switch (view.getId()) {
            case R.id.toolbar_subtitle:
                ll.setVisibility(ll.getVisibility() == 0 ? View.GONE : View.VISIBLE);
                downManagerAdapter.showCheck(ll.getVisibility() == 0 ? true : false);
                checkAll.setText("全选");

                for (DownManagerBean downIngBean : downIngBeans) {
                    downIngBean.setCheck(false);
                }
                for (DownManagerBean downSuccBean : downSuccBeans) {
                    downSuccBean.setCheck(false);
                }
                downManagerAdapter.notifyDataSetChanged();
                break;
            case R.id.check_all:
                String selete = checkAll.getText().toString().trim();

                if (selete.equals("全选")) {
                    checkAll.setText("取消选择");
                    for (DownManagerBean downIngBean : downIngBeans) {
                        downIngBean.setCheck(true);
                    }
                    for (DownManagerBean downSuccBean : downSuccBeans) {
                        downSuccBean.setCheck(true);
                    }
                } else {
                    checkAll.setText("全选");
                    for (DownManagerBean downIngBean : downIngBeans) {
                        downIngBean.setCheck(false);
                    }
                    for (DownManagerBean downSuccBean : downSuccBeans) {
                        downSuccBean.setCheck(false);
                    }
                }
                break;
            case R.id.delete:

                if (allcheckNum == downIngBeans.size() + downSuccBeans.size()) {

                    PolyvDownloadSQLiteHelper.getInstance(ctx).deleteAll();
                    getAll();
                } else {
                    for (DownManagerBean downIngBean : downIngBeans) {
                        if (downIngBean.isCheck()) {
                            PolyvDownloadSQLiteHelper.getInstance(ctx).deleteManager(downIngBean.getCourse_id());
                        }
                    }
                    for (DownManagerBean downSuccBean : downSuccBeans) {
                        if (downSuccBean.isCheck()) {
                            PolyvDownloadSQLiteHelper.getInstance(ctx).deleteManager(downSuccBean.getCourse_id());
                        }
                    }
                }
                getAll();
                break;
        }
    }

    private int allcheckNum;

    @Override
    public void checkPosition(int position, int parentType) {
        allcheckNum = 0;
        int downingNunm = 0;
        int downSuccNum = 0;
        List<DownManagerBean> downIngBeans = baseBean.getDownIngBeans();
        List<DownManagerBean> downSuccBeans = baseBean.getDownSuccBeans();
        if (downIngBeans.size() != 0) {//正在下载
            if (parentType == 1) {//正在下载
                downIngBeans.get(position).setCheck(!downIngBeans.get(position).isCheck());
                for (int i = 0; i < downIngBeans.size(); i++) {
                    DownManagerBean managerBean = downIngBeans.get(i);
                    if (position == i) {

                        managerBean.setCheck(!managerBean.isCheck());
                    }
                    if (managerBean.isCheck()) {
                        downingNunm++;
                    }

                }
            } else {//下载成功
                downSuccBeans.get(position).setCheck(!downSuccBeans.get(position).isCheck());
                for (int i = 0; i < downSuccBeans.size(); i++) {
                    DownManagerBean managerBean = downSuccBeans.get(i);
                    if (position == i) {
                        managerBean.setCheck(!managerBean.isCheck());
                    }
                    if (managerBean.isCheck()) {
                        downSuccNum++;
                    }
                }
            }
        } else {//下载完成
            for (int i = 0; i < downSuccBeans.size(); i++) {
                DownManagerBean managerBean = downSuccBeans.get(i);
                if (position == i) {
                    managerBean.setCheck(!managerBean.isCheck());
                }
                if (managerBean.isCheck()) {
                    downSuccNum++;
                }
            }
        }
        allcheckNum = downingNunm + downSuccNum;
        delete.setText("删除(" + allcheckNum + ")");
        if (allcheckNum == downIngBeans.size() + downSuccBeans.size()) {
            checkAll.setText("取消选择");
        } else {
            checkAll.setText("全选");
        }

        downManagerAdapter.notifyDataSetChanged();
    }
}
