package com.jiaoshizige.teacherexam.yy.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jiaoshizige.teacherexam.model.MessageEvent;
import com.jwenfeng.library.pulltorefresh.BaseRefreshListener;
import com.jwenfeng.library.pulltorefresh.ViewStatus;
import com.umeng.analytics.MobclickAgent;
import com.jiaoshizige.teacherexam.R;
import com.jiaoshizige.teacherexam.base.BaseActivity;
import com.jiaoshizige.teacherexam.base.CommRecyclerViewAdapter;
import com.jiaoshizige.teacherexam.base.GloableConstant;
import com.jiaoshizige.teacherexam.base.ViewHolderZhy;
import com.jiaoshizige.teacherexam.http.ApiUrl;
import com.jiaoshizige.teacherexam.http.MyCallBack;
import com.jiaoshizige.teacherexam.http.Xutil;
import com.jiaoshizige.teacherexam.model.CourseCollectResponse;
import com.jiaoshizige.teacherexam.model.NewMessageResponse;
import com.jiaoshizige.teacherexam.utils.PullToRefreshLayoutRewrite;
import com.jiaoshizige.teacherexam.utils.SPKeyValuesUtils;
import com.jiaoshizige.teacherexam.utils.SPUtils;
import com.jiaoshizige.teacherexam.utils.ToastUtil;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import q.rorbin.badgeview.QBadgeView;

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
//    @BindView(R.id.pulltorefresh)
//    PullToRefreshLayoutRewrite mLayout;
    @BindView(R.id.listView)
    RecyclerView mRecyclerView;
    private String user_id;
    private Context context;
    private Intent intent;
    private String mPageSize = "10";
    private int mPage = 1;
    private CommRecyclerViewAdapter<NewMessageResponse.mData> mAdapter;
    QBadgeView mQBadgeView;


    @Override
    protected int getLayoutId() {
        return R.layout.new_message_centerone;
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("消息中心");
        MobclickAgent.onResume(this);
        mPage = 1;
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
        mQBadgeView = new QBadgeView(this);
        user_id = (String) SPUtils.getSpValues(SPKeyValuesUtils.SP_USER_ID, SPUtils.TYPE_STRING);
        context = this;
        intent = new Intent();
        getMessage();
    }

    private void getMessage() {
        Map<String, Object> map = new HashMap<>();
        map.put("user_id", user_id);
        map.put("page", mPage);
        map.put("page_size", mPageSize);
//        map.put("user_id", 93);
        GloableConstant.getInstance().startProgressDialog(this);
        Xutil.Post(ApiUrl.INDEX, map, new MyCallBack<NewMessageResponse>() {
            @Override
            public void onSuccess(final NewMessageResponse result) {
                super.onSuccess(result);
                GloableConstant.getInstance().stopProgressDialog();
//                mLayout.finishLoadMore();
//                mLayout.finishRefresh();
                EventBus.getDefault().post(new MessageEvent("消息中心"));
                Log.e("*********result", result.getStatus_code());
                if (result.getStatus_code().equals("200")) {
                    if (mPage == 1) {
                        getAdapter().updateData(result.getData());
                    } else {
                        getAdapter().appendData(result.getData());
                    }
                    if (result.getData() != null && result.getData().size() > 0) {
//                        if (mPagerNum.equals("1")) {
//                            mList.clear();
//                            mList = result.getData();
//                        } else {
//                            if (null == mList) {
//                                mList = result.getData();
//                            } else {
//                                mList.addAll(result.getData());
//                            }
//                        }
//                        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
//                        mRecyclerView.setAdapter(new CommonAdapter<NewMessageResponse.mData>(context, R.layout.item_new_message, mList) {
//
//                            @Override
//                            protected void convert(ViewHolder holder, final NewMessageResponse.mData mData, int position) {
//                                holder.setText(R.id.logistics_name, mData.getName());
//                                Log.e("*********result", mData.getName());
//                                holder.setText(R.id.logistics_content, mData.getTitle());
//                                ImageView imageView = (ImageView) holder.getConvertView().findViewById(R.id.logistics_image);
//                                Glide.with(context).load(mData.getImg()).apply(GloableConstant.getInstance().getDefaultOption()).into(imageView);
//                                TextView mDot = (TextView) holder.getConvertView().findViewById(R.id.logistics_dot);
//                                if (mData.getIs_read().equals("0")) {
//                                    mDot.setVisibility(View.VISIBLE);
//                                } else {
//                                    mDot.setVisibility(View.GONE);
//                                }
//                                holder.setOnClickListener(R.id.logistics_message, new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View v) {
//                                        switch (mData.getType()) {
//                                            case "2":
//                                                intent.setClass(MessageCenterNewActivity.this, SystemActivity.class);
//                                                startActivity(intent);
//                                                break;
//                                            case "5":
//                                                intent.setClass(MessageCenterNewActivity.this, MyHomeWorkMessageActivity.class);
//                                                startActivity(intent);
//                                                break;
//                                            case "6":
//                                                intent.setClass(MessageCenterNewActivity.this, MyAnswerListActivity.class);
//                                                startActivity(intent);
//                                                break;
//                                            case "7":
//                                                intent.setClass(MessageCenterNewActivity.this, LogisticsListActivity.class);
//                                                startActivity(intent);
//                                                break;
//                                        }
//                                    }
//                                });
//                            }
//                        });
                    } else {
                        if (mPage > 1) {
                            mPage--;
                            ToastUtil.showShortToast("没有更多数据了");
                        } else {
//                            mLayout.showView(ViewStatus.EMPTY_STATUS);
                        }
                    }
                } else if ("203".equals(result.getStatus_code())) {
                    if (mPage > 1) {
                        mPage--;
                        ToastUtil.showShortToast("没有更多数据了");
                    } else {
//                        mLayout.showView(ViewStatus.EMPTY_STATUS);
                    }
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
//                mLayout.finishLoadMore();
//                mLayout.finishRefresh();
                Log.e("*********ex", ex.getMessage() + "******");
            }
        });
    }

    private CommRecyclerViewAdapter<NewMessageResponse.mData> getAdapter() {
        if (null == mAdapter) {
            mAdapter = new CommRecyclerViewAdapter<NewMessageResponse.mData>(this, R.layout.item_new_message, null) {
                @Override
                protected void convert(ViewHolderZhy holder, final NewMessageResponse.mData mData, final int position) {
                    holder.setText(R.id.logistics_name, mData.getName());
                    Log.e("*********result", mData.getName());
                    holder.setText(R.id.logistics_content, mData.getTitle());
                    ImageView imageView = (ImageView) holder.getConvertView().findViewById(R.id.logistics_image);
                    Glide.with(context).load(mData.getImg()).apply(GloableConstant.getInstance().getDefaultOption()).into(imageView);
                    TextView mLogisticsDot = (TextView) holder.getConvertView().findViewById(R.id.logistics_dot);
                    LinearLayout mDot = (LinearLayout) holder.getConvertView().findViewById(R.id.dot);
//                    if (mData.getIs_read_num().equals("0")&&mData.getIs_read_num()==null){
//                        new QBadgeView(context).bindTarget(mDot).setBadgeNumber(Integer.valueOf(mData.getIs_read())).setBadgeTextSize(12, true).hide(true);
//
//                    }else {
                    new QBadgeView(context).bindTarget(mDot).setBadgeNumber(Integer.valueOf(mData.getIs_read_num())).setBadgeTextSize(12, true);

//                    }

                    Log.e("*********getIs_re", mData.getIs_read());
                    if (mData.getIs_read().equals("0")) {
                        mLogisticsDot.setVisibility(View.VISIBLE);
                    } else {
                        mLogisticsDot.setVisibility(View.GONE);
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
            };
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            mRecyclerView.setAdapter(mAdapter);
        }
        return mAdapter;
    }

}
