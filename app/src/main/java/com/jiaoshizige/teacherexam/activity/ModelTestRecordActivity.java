package com.jiaoshizige.teacherexam.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.jiaoshizige.teacherexam.R;
import com.jiaoshizige.teacherexam.base.BaseActivity;
import com.jiaoshizige.teacherexam.base.CommRecyclerViewAdapter;
import com.jiaoshizige.teacherexam.base.GloableConstant;
import com.jiaoshizige.teacherexam.base.ViewHolderZhy;
import com.jiaoshizige.teacherexam.http.ApiUrl;
import com.jiaoshizige.teacherexam.http.MyCallBack;
import com.jiaoshizige.teacherexam.http.Xutil;
import com.jiaoshizige.teacherexam.model.ModelTextRecordResponse;
import com.jiaoshizige.teacherexam.utils.PullToRefreshLayoutRewrite;
import com.jiaoshizige.teacherexam.utils.SPKeyValuesUtils;
import com.jiaoshizige.teacherexam.utils.SPUtils;
import com.jwenfeng.library.pulltorefresh.BaseRefreshListener;
import com.jwenfeng.library.pulltorefresh.ViewStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnEditorAction;

/**
 * Created by Administrator on 2018/8/3.
 */

public class ModelTestRecordActivity extends BaseActivity {
    @BindView(R.id.recycle_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.pull_refresh)
    PullToRefreshLayoutRewrite mPullToRefresh;


    private Context mContext;
    private String user_id;
    private String tiku_id;
    private List<ModelTextRecordResponse.mData> mList;
    private String mPageNum = "1", mPageSize = "10";
    private int mPage = 1;

    @Override
    protected int getLayoutId() {
        return R.layout.my_open_class_layout;
    }

    @Override
    protected void initView() {
        setToolbarTitle().setText("模考记录");
        setSubTitle().setText("");
        mContext = this;
        mList = new ArrayList<>();
        user_id = (String) SPUtils.getSpValues(SPKeyValuesUtils.SP_USER_ID, SPUtils.TYPE_STRING);
        if (GloableConstant.getInstance().getTiku_id() != null) {
            tiku_id = GloableConstant.getInstance().getTiku_id();
        } else {
            tiku_id = "";
        }
        mPullToRefresh.setRefreshListener(new BaseRefreshListener() {
            @Override
            public void refresh() {
                mPage = 1;
                mPageNum = "1";
                getModelTextRecord();
            }

            @Override
            public void loadMore() {
                mPage = 1;
                mPageNum = String.valueOf(mPage);
                getModelTextRecord();
            }
        });
        mPullToRefresh.setOnErrorListener(new PullToRefreshLayoutRewrite.onClickCallBackListener() {
            @Override
            public void onCallBackListener(View v) {
                mPage = 1;
                mPageNum = "1";
                getModelTextRecord();
            }
        });
        if (!tiku_id.equals("")) {
            mPageNum = "1";
            getModelTextRecord();
        }
    }

    private void getModelTextRecord() {
        Map<String, String> map = new HashMap<>();
        map.put("user_id", user_id);
        map.put("tiku_id", tiku_id);
        GloableConstant.getInstance().startProgressDialog(mContext);
        Xutil.GET(ApiUrl.MODELTESTRECORD, map, new MyCallBack<ModelTextRecordResponse>() {
            @Override
            public void onSuccess(ModelTextRecordResponse result) {
                super.onSuccess(result);
                GloableConstant.getInstance().stopProgressDialog();
                mPullToRefresh.finishRefresh();
                mPullToRefresh.finishLoadMore();
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
                        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
                        mRecyclerView.setAdapter(new CommRecyclerViewAdapter<ModelTextRecordResponse.mData>(mContext, R.layout.item_model_test_layout, mList) {


                            @Override
                            protected void convert(ViewHolderZhy holder, final ModelTextRecordResponse.mData item, int position) {
                                holder.setText(R.id.name, item.getName());
                                holder.setText(R.id.time, item.getCreated_at() + ", 本次得分");
                                holder.setText(R.id.score, item.getMoni_totle());
                                holder.setOnClickListener(R.id.item, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent();
                                        intent.setClass(ModelTestRecordActivity.this, ModelTestRecordDetailActivity.class);
                                        intent.putExtra("id", item.getId());
                                        intent.putExtra("name", item.getName());
                                        startActivity(intent);
                                    }
                                });
                            }
                        });

                    }
                }
            }

            @Override
            public void onFinished() {
                super.onFinished();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
            }
        });
    }
}
