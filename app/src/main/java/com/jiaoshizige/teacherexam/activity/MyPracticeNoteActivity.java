package com.jiaoshizige.teacherexam.activity;

import android.content.Context;
import android.content.Intent;
import android.provider.SyncStateContract;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.jiaoshizige.teacherexam.R;
import com.jiaoshizige.teacherexam.base.BaseActivity;
import com.jiaoshizige.teacherexam.base.CommRecyclerViewAdapter;
import com.jiaoshizige.teacherexam.base.GloableConstant;
import com.jiaoshizige.teacherexam.base.ViewHolderZhy;
import com.jiaoshizige.teacherexam.http.ApiUrl;
import com.jiaoshizige.teacherexam.http.MyCallBack;
import com.jiaoshizige.teacherexam.http.Xutil;
import com.jiaoshizige.teacherexam.model.MyPracticeNoteResponse;
import com.jiaoshizige.teacherexam.utils.PullToRefreshLayoutRewrite;
import com.jiaoshizige.teacherexam.utils.SPKeyValuesUtils;
import com.jiaoshizige.teacherexam.utils.SPUtils;
import com.jiaoshizige.teacherexam.utils.ToastUtil;
import com.jwenfeng.library.pulltorefresh.BaseRefreshListener;
import com.jwenfeng.library.pulltorefresh.ViewStatus;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/7/19.
 */

public class MyPracticeNoteActivity extends BaseActivity {
    @BindView(R.id.recycle_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.pull_refresh)
    PullToRefreshLayoutRewrite mPullToRefresh;

    private String user_id;
    private Context mContext;
    private String mPageNum = "1", mPageSize = "10";
    private int mPage = 1;
    private List<MyPracticeNoteResponse.mData> mList;
    private String tiku_id;

    @Override
    protected int getLayoutId() {
        return R.layout.my_open_class_layout;
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("练习记录");
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("练习记录");
        MobclickAgent.onPause(this);
    }

    @Override
    protected void initView() {
        mContext = this;
        setSubTitle().setText("");
        setToolbarTitle().setText("练习记录");
        mContext = this;
        mList = new ArrayList<>();
        user_id = (String) SPUtils.getSpValues(SPKeyValuesUtils.SP_USER_ID, SPUtils.TYPE_STRING);
        if (GloableConstant.getInstance().getTiku_id()!=null){
            tiku_id=GloableConstant.getInstance().getTiku_id();
        }
        mPullToRefresh.setRefreshListener(new BaseRefreshListener() {
            @Override
            public void refresh() {
                mPageNum = "1";
                getPracticeNote();
            }

            @Override
            public void loadMore() {
                mPage++;
                mPageNum=String.valueOf(mPage);
                getPracticeNote();
            }
        });
        mPageNum = "1";
        getPracticeNote();
    }


    private void getPracticeNote() {
        Map<String, String> map = new HashMap<>();
        map.put("user_id", user_id);
        map.put("tiku_id",tiku_id);
        map.put("page", mPageNum);
        map.put("page_size", mPageSize);
        Log.d("**************map", map.toString());
        GloableConstant.getInstance().startProgressDialog(mContext);
        Xutil.GET(ApiUrl.PRACTICENOTE, map, new MyCallBack<MyPracticeNoteResponse>() {
            @Override
            public void onSuccess(MyPracticeNoteResponse result) {
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
                                mList = result.getData();
                            } else {
                                mList.clear();
                                mList.addAll(result.getData());
                            }
                        }
                        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
                        mRecyclerView.setAdapter(new CommRecyclerViewAdapter<MyPracticeNoteResponse.mData>(mContext, R.layout.item_practice_note_layout, mList) {

                            @Override
                            protected void convert(ViewHolderZhy holder, final MyPracticeNoteResponse.mData item, int position) {
                                holder.setText(R.id.name,item.getName());
                                holder.setText(R.id.time,item.getCreated_at());
                                holder.setText(R.id.total_num,item.getTotal_score());
                                holder.setText(R.id.do_num,item.getPass_score());
                                holder.setOnClickListener(R.id.item, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent=new Intent();
                                        intent.setClass(MyPracticeNoteActivity.this,MyPracticeNoteDetailActivity.class);
                                        intent.putExtra("id",item.getId());
                                        intent.putExtra("name",item.getName());
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
}
