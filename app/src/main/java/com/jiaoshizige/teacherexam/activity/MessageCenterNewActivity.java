package com.jiaoshizige.teacherexam.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jwenfeng.library.pulltorefresh.BaseRefreshListener;
import com.jwenfeng.library.pulltorefresh.ViewStatus;
import com.umeng.analytics.MobclickAgent;
import com.jiaoshizige.teacherexam.R;
import com.jiaoshizige.teacherexam.base.BaseActivity;
import com.jiaoshizige.teacherexam.base.GloableConstant;
import com.jiaoshizige.teacherexam.http.ApiUrl;
import com.jiaoshizige.teacherexam.http.MyCallBack;
import com.jiaoshizige.teacherexam.http.Xutil;
import com.jiaoshizige.teacherexam.model.MessageCenterResponse;
import com.jiaoshizige.teacherexam.model.NewMessageResponse;
import com.jiaoshizige.teacherexam.utils.PullToRefreshLayoutRewrite;
import com.jiaoshizige.teacherexam.utils.SPKeyValuesUtils;
import com.jiaoshizige.teacherexam.utils.SPUtils;
import com.jiaoshizige.teacherexam.utils.ToastUtil;
import com.jiaoshizige.teacherexam.yy.activity.SystemActivity;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/4/3 0003.
 */

public class MessageCenterNewActivity extends BaseActivity {
    //    @BindView(R.id.logistics_message)
//    LinearLayout mLogistics;
//    @BindView(R.id.system_message)
//    LinearLayout mSystem;
//    @BindView(R.id.work_message)
//    LinearLayout mWork;
//    @BindView(R.id.answer_message)
//    LinearLayout mAnswer;
    @BindView(R.id.pulltorefresh)
    PullToRefreshLayoutRewrite mLayout;
    @BindView(R.id.listView)
    RecyclerView mRecyclerView;
    private String user_id;
    private Context context;
    private Intent intent;
    private String mPagerNum = "1";
    private String mPageSize = "10";
    private int mPage = 1;
    private List<NewMessageResponse.mData> mList;

    @Override
    protected int getLayoutId() {
        return R.layout.new_message_center;
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("消息中心");
        MobclickAgent.onResume(this);
        getMessage();
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("消息中心");
        MobclickAgent.onPause(this);
    }

    @Override
    protected void initView() {
        setSubTitle().setText("");
        setToolbarTitle().setText("消息中心");
        user_id = (String) SPUtils.getSpValues(SPKeyValuesUtils.SP_USER_ID, SPUtils.TYPE_STRING);
        context = this;
        mList = new ArrayList<>();
        intent = new Intent();
        mLayout.setRefreshListener(new BaseRefreshListener() {
            @Override
            public void refresh() {
                mPagerNum = "1";
                mPage = 1;
                getMessage();
            }

            @Override
            public void loadMore() {
                mPage=1;
                mPagerNum = String.valueOf(mPage);
                getMessage();
            }
        });
        getMessage();
    }

    private void getMessage() {
        Map<String, Object> map = new HashMap<>();
        map.put("user_id", user_id);
        map.put("page", mPagerNum);
        map.put("page_size", mPageSize);
//        map.put("user_id", 93);
        GloableConstant.getInstance().startProgressDialog(this);
        Xutil.Post(ApiUrl.INDEX, map, new MyCallBack<NewMessageResponse>() {
            @Override
            public void onSuccess(final NewMessageResponse result) {
                super.onSuccess(result);
                GloableConstant.getInstance().stopProgressDialog();
                mLayout.finishLoadMore();
                mLayout.finishRefresh();
                Log.e("*********result", result.getStatus_code());
                if (result.getStatus_code().equals("200")) {
                    if (result.getData().size() > 0 && result.getData() != null) {
                        if (mPagerNum.equals("1")) {
                            mList.clear();
                            mList = result.getData();
                        } else {
                            if (null == mList) {
                                mList = result.getData();
                            } else {
                                mList.addAll(result.getData());
                            }
                        }
                        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
                        mRecyclerView.setAdapter(new CommonAdapter<NewMessageResponse.mData>(context, R.layout.item_new_message, mList) {

                            @Override
                            protected void convert(ViewHolder holder, final NewMessageResponse.mData mData, int position) {
                                holder.setText(R.id.logistics_name, mData.getName());
                                Log.e("*********200result", mData.getName());
                                Log.e("*********Is_read()11111", mData.getIs_read());
                                holder.setText(R.id.logistics_content, mData.getTitle());
                                Log.e("*********Is_read()22222", mData.getIs_read());
                                ImageView imageView = (ImageView) holder.getConvertView().findViewById(R.id.logistics_image);
                                Glide.with(context).load(mData.getImg()).apply(GloableConstant.getInstance().getDefaultOption()).into(imageView);
                                TextView mDot = (TextView) holder.getConvertView().findViewById(R.id.logistics_dot);
                                Log.e("*********Is_read()", mData.getIs_read());
                                if (mData.getIs_read().equals("0")) {
                                    mDot.setVisibility(View.VISIBLE);
                                } else {
                                    mDot.setVisibility(View.GONE);
                                }
                                holder.setOnClickListener(R.id.logistics_message, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        switch (mData.getType()) {
                                            case "2":
                                                intent.setClass(MessageCenterNewActivity.this, SystemActivity.class);
                                                startActivity(intent);
                                                break;
                                            case "5":
                                                intent.setClass(MessageCenterNewActivity.this, MyHomeWorkMessageActivity.class);
                                                startActivity(intent);
                                                break;
                                            case "6":
                                                intent.setClass(MessageCenterNewActivity.this, MyAnswerListActivity.class);
                                                startActivity(intent);
                                                break;
                                            case "7":
                                                intent.setClass(MessageCenterNewActivity.this, LogisticsListActivity.class);
                                                startActivity(intent);
                                                break;
                                        }
                                    }
                                });
                            }
                        });
                    }else {
                        if (mPage > 1) {
                            ToastUtil.showShortToast("没有更多数据了");
                        } else {
                            mLayout.showView(ViewStatus.EMPTY_STATUS);
                        }
                    }
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
                mLayout.finishLoadMore();
                mLayout.finishRefresh();
                Log.e("*********ex", ex.getMessage() + "******");
            }
        });
    }

}
