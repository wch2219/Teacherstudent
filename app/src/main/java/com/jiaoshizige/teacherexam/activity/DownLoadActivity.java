package com.jiaoshizige.teacherexam.activity;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jiaoshizige.teacherexam.R;
import com.jiaoshizige.teacherexam.adapter.ExpandListViewAdapter;
import com.jiaoshizige.teacherexam.base.BaseActivity;
import com.jiaoshizige.teacherexam.base.GloableConstant;
import com.jiaoshizige.teacherexam.model.RecordModel;
import com.jiaoshizige.teacherexam.palyer.PolyvDownloadInfo;
import com.jiaoshizige.teacherexam.palyer.PolyvDownloadSQLiteHelper;
import com.jiaoshizige.teacherexam.widgets.CustomExpandableListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/1/19 0019.
 */

public class DownLoadActivity extends BaseActivity implements View.OnClickListener {
    //    @BindView(R.id.pulltorefresh)
//    PullToRefreshLayoutRewrite mPullToRefresh;
    @BindView(R.id.course_no_data)
    LinearLayout mCourseNoData;
    @BindView(R.id.expandable_list_view)
    CustomExpandableListView mListView;
    @BindView(R.id.ll)
    LinearLayout mLinearLayout;
    @BindView(R.id.check_all)
    TextView mAll;
    @BindView(R.id.delete)
    TextView mDelete;
    private static PolyvDownloadSQLiteHelper mDownloadSQLiteHelper;
    private boolean flag = false;
    private Context context;
    private String vid;


    //    List<CourseCatalogResponse.mCatalog> mListData;
    List<PolyvDownloadInfo> thirdModelList;
//    List<CourseCatalogResponse.mSon> thirdModelList;
//    private List<List<CourseCatalogResponse.mSon>> childArray;

    private List<List<PolyvDownloadInfo>> childArray;
    private ExpandListViewAdapter mAdapter;

    private List<RecordModel> lists;


    @Override
    protected int getLayoutId() {
        return R.layout.down_load_layout;
    }

    @Override
    protected void initView() {
        setSubTitle().setText("编辑");
        setToolbarTitle().setText("已下载课件");
        context = this;
        GloableConstant.getInstance().setAll("0");
        mDownloadSQLiteHelper = PolyvDownloadSQLiteHelper.getInstance(context);
        initDate();
        setSubTitle().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GloableConstant.getInstance().setCheck("0");
                flag = !flag;
                if (flag) {
                    setSubTitle().setText("取消");
                    mAdapter.isShow(false);
                    mLinearLayout.setVisibility(View.VISIBLE);
                    GloableConstant.getInstance().setAll("0");
                } else {
                    setSubTitle().setText("编辑");
                    mAdapter.isShow(true);
                    mLinearLayout.setVisibility(View.GONE);
                    for (int i = 0; i < lists.size(); i++) {
                        lists.get(i).setCheck(false);

                    }
                    GloableConstant.getInstance().setAll("1");
                    mAdapter.notifyDataSetChanged();
                }

            }
        });
        mListView.setGroupIndicator(null);
        mAll.setOnClickListener(this);
        mDelete.setOnClickListener(this);
        mAdapter = new ExpandListViewAdapter(lists, childArray, context);
        mListView.setAdapter(mAdapter);
        mListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Intent intent = RecordingActivity.newIntent(context, RecordingActivity.PlayMode.portrait,
                        lists.get(groupPosition).getmPolyvDownloadInfo().get(childPosition).getVid(),
                        PolyvDownloadSQLiteHelper.getInstance(context).getInfo(lists.get(groupPosition).getmPolyvDownloadInfo().get(childPosition).getVid()).getBitrate(),
                        true, true);

                // 在线视频和下载的视频播放的时候只显示播放器窗口，用该参数来控制
                intent.putExtra(RecordingActivity.IS_VLMS_ONLINE, false);
                startActivity(intent);
                return false;

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.check_all:
                for (int i = 0; i < lists.size(); i++) {
                    lists.get(i).setCheck(true);
                    GloableConstant.getInstance().setCheck("1");
                }
                mAdapter.notifyDataSetChanged();
                break;
            case R.id.delete:
                Log.e("*********getCheck", GloableConstant.getInstance().getCheck());
                if (GloableConstant.getInstance().getCheck().equals("1")) {
                    mDownloadSQLiteHelper.deleteAll();
                    lists.clear();
                    childArray.clear();
                    mAdapter.notifyDataSetChanged();
                    mLinearLayout.setVisibility(View.GONE);
                    mCourseNoData.setVisibility(View.VISIBLE);
                } else {
                    mAdapter.removeAll();
                    mLinearLayout.setVisibility(View.GONE);
                    mCourseNoData.setVisibility(View.VISIBLE);
                }


                break;
        }
    }

    private void initDate() {
        lists = new ArrayList<>();
        childArray = new ArrayList<>();
        /**
         * 已班级为单位的筛选
         */
        if (PolyvDownloadSQLiteHelper.getInstance(this).getModel().size() > 0) {
            mCourseNoData.setVisibility(View.GONE);
            setSubTitle().setVisibility(View.VISIBLE);
            setSubTitle().setClickable(true);
            lists.addAll(PolyvDownloadSQLiteHelper.getInstance(this).getModel());
            for (int i = 0; i < lists.size(); i++) {
                thirdModelList = new ArrayList<>();
                for (int j = 0; j < lists.get(i).getmPolyvDownloadInfo().size(); j++) {
                    thirdModelList.add(lists.get(i).getmPolyvDownloadInfo().get(j));
                }
                childArray.add(thirdModelList);
            }
        } else {
            setSubTitle().setVisibility(View.GONE);
            setSubTitle().setClickable(false);
//            mPullToRefresh.showView(ViewStatus.EMPTY_STATUS);
            mCourseNoData.setVisibility(View.VISIBLE);
        }
        /**
         * 已章节为单位的筛选
         */
     /*   if (PolyvDownloadSQLiteHelper.getInstance(this).getChapterModel().size() > 0) {
            mCourseNoData.setVisibility(View.GONE);
            setSubTitle().setVisibility(View.VISIBLE);
            setSubTitle().setClickable(true);
            lists.addAll(PolyvDownloadSQLiteHelper.getInstance(this).getChapterModel());
            for (int i = 0; i < lists.size(); i++) {
                thirdModelList = new ArrayList<>();
                for (int j = 0; j < lists.get(i).getmPolyvDownloadInfo().size(); j++) {
                    thirdModelList.add(lists.get(i).getmPolyvDownloadInfo().get(j));
                }
                childArray.add(thirdModelList);
            }
        } else {
            setSubTitle().setVisibility(View.GONE);
            setSubTitle().setClickable(false);
            mCourseNoData.setVisibility(View.VISIBLE);
        }*/

    }
}
