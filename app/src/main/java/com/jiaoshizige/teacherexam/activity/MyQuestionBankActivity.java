package com.jiaoshizige.teacherexam.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.jiaoshizige.teacherexam.R;
import com.jiaoshizige.teacherexam.base.BaseActivity;
import com.jiaoshizige.teacherexam.base.CommRecyclerViewAdapter;
import com.jiaoshizige.teacherexam.base.GloableConstant;
import com.jiaoshizige.teacherexam.base.ViewHolderZhy;
import com.jiaoshizige.teacherexam.http.ApiUrl;
import com.jiaoshizige.teacherexam.http.MyCallBack;
import com.jiaoshizige.teacherexam.http.Xutil;
import com.jiaoshizige.teacherexam.model.QusstionBankResponse;
import com.jiaoshizige.teacherexam.utils.PullToRefreshLayoutRewrite;
import com.jiaoshizige.teacherexam.utils.SPKeyValuesUtils;
import com.jiaoshizige.teacherexam.utils.SPUtils;
import com.jiaoshizige.teacherexam.utils.ToastUtil;
import com.jiaoshizige.teacherexam.zz.utils.NetworkUtils;
import com.jiaoshizige.teacherexam.zz.database.QuestionBankList;
import com.jiaoshizige.teacherexam.zz.database.QuestionBankListDao;
import com.jwenfeng.library.pulltorefresh.BaseRefreshListener;
import com.jwenfeng.library.pulltorefresh.ViewStatus;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/7/17.
 */

public class MyQuestionBankActivity extends BaseActivity {
    @BindView(R.id.recycle_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.pull_refresh)
    PullToRefreshLayoutRewrite mPullToRefresh;
    @BindView(R.id.course_no_data)
    LinearLayout mCourseNoData;

    private String user_id;
    private Context mContext;
    private String mPageNum = "1", mPageSize = "10";
    private int mPage = 1;
    private List<QusstionBankResponse.mData> mList;
    private String examtypeid;


    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("我的题库");
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("我的题库");
        MobclickAgent.onPause(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.my_open_class_layout;
    }

    @Override
    protected void initView() {
        setSubTitle().setText("");
        setToolbarTitle().setText("我的题库");
        mContext = this;
        mList = new ArrayList<>();
        user_id = (String) SPUtils.getSpValues(SPKeyValuesUtils.SP_USER_ID, SPUtils.TYPE_STRING);
        examtypeid = (String) SPUtils.getSpValues("examType", 1);
        mPullToRefresh.setRefreshListener(new BaseRefreshListener() {
            @Override
            public void refresh() {
                mPageNum = "1";
                mPage = 1;
                getQusetionBank();
            }

            @Override
            public void loadMore() {
                mPage++;
                mPageNum = String.valueOf(mPage);
                getQusetionBank();
            }
        });

        //判断网络是否连接
        if (NetworkUtils.isNetworkConnected(MyQuestionBankActivity.this)) {
            mPageNum = "1";
            getQusetionBank();
        } else {
            getLocalData();
        }

    }

    private void getQusetionBank() {
        Map<String, String> map = new HashMap<>();
        map.put("user_id", user_id);
        map.put("page", mPageNum);
        map.put("page_size", mPageSize);
        Log.d("**************map", map.toString());
        GloableConstant.getInstance().startProgressDialog(mContext);
        Xutil.GET(ApiUrl.QUESTIONBANK, map, new MyCallBack<QusstionBankResponse>() {
            @Override
            public void onSuccess(QusstionBankResponse result) {
                super.onSuccess(result);
                GloableConstant.getInstance().stopProgressDialog();
                mPullToRefresh.finishLoadMore();
                mPullToRefresh.finishRefresh();
                mPullToRefresh.showView(ViewStatus.CONTENT_STATUS);
                if (result.getStatus_code().equals("200")) {
                    if (result.getData() != null && result.getData().size() > 0) {
                        if (mPageNum.equals("1")) {
                            mList.clear();
                            mList = result.getData();
                        } else {
                            if (null == mList) {
                                mList.clear();
                                mList = result.getData();
                            } else {
                                mList.clear();
                                mList.addAll(result.getData());
                            }
                        }
                        GloableConstant.getInstance().setTiku_id(null);
                        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
                        mRecyclerView.setAdapter(new CommRecyclerViewAdapter<QusstionBankResponse.mData>(mContext, R.layout.item_quetion_bank, mList) {

                            @Override
                            protected void convert(ViewHolderZhy holder, final QusstionBankResponse.mData item, int position) {
                                holder.setText(R.id.title, item.getName());
                                holder.setText(R.id.type, item.getExam_name());
                                holder.setText(R.id.to_confirm_progress, item.getGuanqia_totle_pass_num());
                                holder.setText(R.id.all_to_confirm, "/" + item.getGuanqia_totle_num());
                                holder.setText(R.id.crown_progress, item.getHuangguan_have_totle_num());
                                holder.setText(R.id.all_crown, "/" + item.getHuangguan_totle_num());
                                holder.setOnClickListener(R.id.item, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        GloableConstant.getInstance().setTiku_id(item.getId());
                                        Intent intent = new Intent();
                                        intent.setClass(MyQuestionBankActivity.this, QuestionBankDetailActivity.class);
                                        intent.putExtra("name", item.getName());
                                        intent.putExtra("id", item.getId());
                                        startActivity(intent);

                                    }
                                });

                            }
                        });
                    } else {
                        if (mPage > 1) {
                            ToastUtil.showShortToast("没有更多数据了");
                        } else {
                            mPullToRefresh.showView(ViewStatus.EMPTY_STATUS);
                        }
                    }
                } else if (result.getStatus_code().equals("203")) {
                    if (mPage > 1) {
                        ToastUtil.showShortToast("没有更多数据了");
                    } else {
                        mPullToRefresh.showView(ViewStatus.EMPTY_STATUS);
                    }
                }
            }

            @Override
            public void onFinished() {
                super.onFinished();
                GloableConstant.getInstance().stopProgressDialog();
                mPullToRefresh.finishLoadMore();
                mPullToRefresh.finishRefresh();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
                Log.e("/////////", ex.getMessage() + "///////");
                GloableConstant.getInstance().stopProgressDialog();
                mPullToRefresh.finishLoadMore();
                mPullToRefresh.finishRefresh();
                mPullToRefresh.showView(ViewStatus.ERROR_STATUS);
            }
        });
    }

    private void getLocalData() {
        List<QuestionBankList> tmplist = new QuestionBankListDao(MyQuestionBankActivity.this).queryByColumnNameAndColumnName("user_id", user_id, "examtype_id", examtypeid);
        if (tmplist != null && tmplist.size() > 0) {
            //表中有数据
            mCourseNoData.setVisibility(View.GONE);
            mPullToRefresh.setVisibility(View.VISIBLE);

            mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
            mRecyclerView.setAdapter(new CommRecyclerViewAdapter<QuestionBankList>(mContext, R.layout.item_quetion_bank, tmplist) {

                @Override
                protected void convert(ViewHolderZhy holder, final QuestionBankList item, int position) {
                    holder.setText(R.id.title, item.getName());
                    holder.setText(R.id.type, item.getExam_name());
                    holder.setText(R.id.to_confirm_progress, item.getGuanqia_totle_pass_num());
                    holder.setText(R.id.all_to_confirm, "/" + item.getGuanqia_totle_num());
                    holder.setText(R.id.crown_progress, item.getHuangguan_have_totle_num());
                    holder.setText(R.id.all_crown, "/" + item.getHuangguan_totle_num());
                    holder.setOnClickListener(R.id.item, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            GloableConstant.getInstance().setTiku_id(item.getQbid());
                            Intent intent = new Intent();
                            intent.setClass(MyQuestionBankActivity.this, QuestionBankDetailActivity.class);
                            intent.putExtra("name", item.getName());
                            intent.putExtra("id", item.getId());
                            startActivity(intent);

                        }
                    });

                }
            });

        } else {
            //表中没有数据
            mCourseNoData.setVisibility(View.VISIBLE);
            mPullToRefresh.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
