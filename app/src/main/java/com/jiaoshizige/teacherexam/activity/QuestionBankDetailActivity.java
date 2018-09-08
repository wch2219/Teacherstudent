package com.jiaoshizige.teacherexam.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jiaoshizige.teacherexam.R;
import com.jiaoshizige.teacherexam.base.BaseActivity;
import com.jiaoshizige.teacherexam.base.CommRecyclerViewAdapter;
import com.jiaoshizige.teacherexam.base.GloableConstant;
import com.jiaoshizige.teacherexam.base.ViewHolderZhy;
import com.jiaoshizige.teacherexam.http.ApiUrl;
import com.jiaoshizige.teacherexam.http.MyCallBack;
import com.jiaoshizige.teacherexam.http.Xutil;
import com.jiaoshizige.teacherexam.model.QuestionBankDetailResponse;
import com.jiaoshizige.teacherexam.utils.PullToRefreshLayoutRewrite;
import com.jiaoshizige.teacherexam.utils.SPKeyValuesUtils;
import com.jiaoshizige.teacherexam.utils.SPUtils;
import com.jiaoshizige.teacherexam.utils.ToastUtil;
import com.jiaoshizige.teacherexam.zz.AllQuestionsActivity;
import com.jiaoshizige.teacherexam.zz.utils.NetworkUtils;
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

public class QuestionBankDetailActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.progress)
    TextView mProgress;
    @BindView(R.id.breakthrough_progress)
    TextView mBreathroughProgress;
    @BindView(R.id.crown_num)
    TextView mCrownNum;
    @BindView(R.id.test_data)
    LinearLayout mTestData;
    @BindView(R.id.recycle_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.pull_refresh)
    PullToRefreshLayoutRewrite mPullToRefresh;
    @BindView(R.id.my_practice)
    RelativeLayout mPractice;
    @BindView(R.id.my_collection)
    RelativeLayout mCollection;
    @BindView(R.id.error_topic)
    RelativeLayout mErrorTopic;
    @BindView(R.id.simulation_questions)
    RelativeLayout mSimulation;
    @BindView(R.id.all_questions)
    RelativeLayout mAllQuestions;

    private String user_id;
    private Context mContext;
    private String mPageNum = "1", mPageSize = "10";
    private int mPage = 1;
    private String name;
    private String mQuestionBankId;
    private List<QuestionBankDetailResponse.mKaoDianList> mList;
    QuestionBankDetailResponse.mTotle mTotle;

    @Override
    protected int getLayoutId() {
        return R.layout.question_bank_detail_layout;
    }

    @Override
    protected void initView() {
        mContext = this;
        mList = new ArrayList<>();
        user_id = (String) SPUtils.getSpValues(SPKeyValuesUtils.SP_USER_ID, SPUtils.TYPE_STRING);
        if (null != getIntent().getStringExtra("name")) {
            name = getIntent().getStringExtra("name");
        } else {
            name = "";
        }
        if (null != getIntent().getStringExtra("id")) {
            mQuestionBankId = getIntent().getStringExtra("id");
        } else {
            mQuestionBankId = "";
        }
        setSubTitle().setText("");
        if (!name.equals("")) {
            setToolbarTitle().setText(name);
        }
        mPullToRefresh.setRefreshListener(new BaseRefreshListener() {
            @Override
            public void refresh() {
                mPageNum = "1";
                mPage = 1;
                getquestiondetial();
            }

            @Override
            public void loadMore() {
                mPage++;
                mPageNum = String.valueOf(mPage);
                getquestiondetial();
            }
        });
        mPageNum = "1";
        getquestiondetial();
        mTestData.setOnClickListener(this);
        mPractice.setOnClickListener(this);
        mCollection.setOnClickListener(this);
        mErrorTopic.setOnClickListener(this);
        mSimulation.setOnClickListener(this);
        mAllQuestions.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.test_data:
                if (NetworkUtils.isNetworkConnected(QuestionBankDetailActivity.this)) {
                    intent.setClass(QuestionBankDetailActivity.this, TestDataActivity.class);
                    intent.putExtra("mTotle", mTotle);
                    intent.putExtra("name", name);
                    startActivity(intent);
                }

                break;
            case R.id.my_practice:
                if (NetworkUtils.isNetworkConnected(QuestionBankDetailActivity.this)) {
                    intent.setClass(QuestionBankDetailActivity.this, MyPracticeNoteActivity.class);
                    startActivity(intent);
                }

                break;
            case R.id.my_collection:
                if (NetworkUtils.isNetworkConnected(QuestionBankDetailActivity.this)) {
                    intent.setClass(QuestionBankDetailActivity.this, MyQuestionBankCollectionActivity.class);
                    startActivity(intent);
                }

                break;
            case R.id.error_topic:
                if (NetworkUtils.isNetworkConnected(QuestionBankDetailActivity.this)) {
                    intent.setClass(QuestionBankDetailActivity.this, ErrorTopicActivity.class);
                    startActivity(intent);
                }

                break;
            case R.id.simulation_questions:
                if (NetworkUtils.isNetworkConnected(QuestionBankDetailActivity.this)) {
                    intent.setClass(QuestionBankDetailActivity.this, SimulationQuestionActivity.class);
                    intent.putExtra("name", name);
                    startActivity(intent);
                }

                break;
            case R.id.all_questions:
                intent.setClass(QuestionBankDetailActivity.this, AllQuestionsActivity.class);
                startActivity(intent);
                break;

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("题库详情");
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("题库详情");
        MobclickAgent.onPause(this);
    }

    private void getquestiondetial() {
        Map<String, String> map = new HashMap<>();
        map.put("user_id", user_id);
        map.put("tiku_id", mQuestionBankId);
        map.put("page", mPageNum);
        map.put("page_size", mPageSize);
        Log.d("**************map", map.toString());
        GloableConstant.getInstance().startProgressDialog(mContext);
        Xutil.GET(ApiUrl.QUESTIONBANKDETAIL, map, new MyCallBack<QuestionBankDetailResponse>() {
            @Override
            public void onSuccess(QuestionBankDetailResponse result) {
                super.onSuccess(result);
                GloableConstant.getInstance().stopProgressDialog();
                mPullToRefresh.finishRefresh();
                mPullToRefresh.finishLoadMore();
                mPullToRefresh.showView(ViewStatus.CONTENT_STATUS);
                if (result.getStatus_code().equals("200")) {
                    if (result.getData().getTotle() != null && result.getData().getTotle().size() > 0) {
                        mTotle = result.getData().getTotle().get(0);
                        mProgress.setText(result.getData().getTotle().get(0).getPercent());
                        mBreathroughProgress.setText(result.getData().getTotle().get(0).getGuanqia_totle_pass_num() + "/" +
                                result.getData().getTotle().get(0).getGuanqia_totle_num());
                        mCrownNum.setText(result.getData().getTotle().get(0).getHuangguan_have_totle_num() + "/" +
                                result.getData().getTotle().get(0).getHuangguan_totle_num());
                    } else {

                    }
                    if (result.getData().getKaodian_list() != null && result.getData().getKaodian_list().size() > 0) {
                        if (mPageNum.equals("1")) {
                            mList.clear();
                            mList = result.getData().getKaodian_list();
                        } else {
                            if (null == mList) {
                                mList.clear();
                                mList = result.getData().getKaodian_list();
                            } else {
                                mList.clear();
                                mList.addAll(result.getData().getKaodian_list());
                            }
                        }
                        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
                        mRecyclerView.setAdapter(new CommRecyclerViewAdapter<QuestionBankDetailResponse.mKaoDianList>(mContext, R.layout.item_question_bank_detail, mList) {

                            @Override
                            protected void convert(ViewHolderZhy holder, final QuestionBankDetailResponse.mKaoDianList item, int position) {
                                int srial = position + 1;
                                if (srial < 10) {
                                    holder.setText(R.id.serial_number, "0" + srial + " " + item.getName());
                                } else {
                                    holder.setText(R.id.serial_number, srial + item.getName());
                                }
                                holder.setText(R.id.to_confirm_progress, item.getGuanqia_totle_pass_num());
                                holder.setText(R.id.all_to_confirm, "/" + item.getGuanqia_totle_num());
                                holder.setText(R.id.crown_progress, item.getHuangguan_have_totle_num());
                                holder.setText(R.id.all_crown, "/" + item.getHuangguan_totle_num());
                                holder.setOnClickListener(R.id.item, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent();
                                        intent.setClass(QuestionBankDetailActivity.this, CheckPointListActivity.class);
                                        intent.putExtra("id", item.getId());
                                        intent.putExtra("name", item.getName());
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
                GloableConstant.getInstance().stopProgressDialog();
                mPullToRefresh.finishRefresh();
                mPullToRefresh.finishLoadMore();
                mPullToRefresh.showView(ViewStatus.ERROR_STATUS);
                Log.e("************ex", ex.getMessage() + "///////");
            }
        });

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

}
