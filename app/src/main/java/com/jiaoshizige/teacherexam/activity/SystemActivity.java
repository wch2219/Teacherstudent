package com.jiaoshizige.teacherexam.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.jwenfeng.library.pulltorefresh.BaseRefreshListener;
import com.jwenfeng.library.pulltorefresh.ViewStatus;
import com.umeng.analytics.MobclickAgent;
import com.jiaoshizige.teacherexam.R;
import com.jiaoshizige.teacherexam.base.BaseActivity;
import com.jiaoshizige.teacherexam.base.GloableConstant;
import com.jiaoshizige.teacherexam.http.ApiUrl;
import com.jiaoshizige.teacherexam.http.MyCallBack;
import com.jiaoshizige.teacherexam.http.SupportResponse;
import com.jiaoshizige.teacherexam.http.Xutil;
import com.jiaoshizige.teacherexam.model.MessageCenterResponse;
import com.jiaoshizige.teacherexam.utils.PullToRefreshLayoutRewrite;
import com.jiaoshizige.teacherexam.utils.SPKeyValuesUtils;
import com.jiaoshizige.teacherexam.utils.SPUtils;
import com.jiaoshizige.teacherexam.utils.ToastUtil;
import com.jiaoshizige.teacherexam.widgets.CancelOrOkDialog;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/4/4 0004.
 */

public class SystemActivity extends BaseActivity {
    @BindView(R.id.listView)
    RecyclerView mRecyclerView;
    @BindView(R.id.pulltorefresh)
    PullToRefreshLayoutRewrite mLayout;
    private String user_id;
    private Context context;
    private Intent intent;
    private String mPagerNum = "1";
    private String mPageSize = "10";
    private int mPage = 1;
    private String messageId;
    private List<MessageCenterResponse.mData> mList;

    @Override
    protected int getLayoutId() {
        return R.layout.new_message_center;
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("系统公告");
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("系统公告");
        MobclickAgent.onPause(this);
    }

    @Override
    protected void initView() {
        setSubTitle().setText("");
        setToolbarTitle().setText("系统公告");
        user_id = (String) SPUtils.getSpValues(SPKeyValuesUtils.SP_USER_ID, SPUtils.TYPE_STRING);
        context = this;
        mList = new ArrayList<>();
        mLayout.setRefreshListener(new BaseRefreshListener() {
            @Override
            public void refresh() {
                mPagerNum = "1";
                mPage = 1;
                systemMessage();
            }

            @Override
            public void loadMore() {
                mPage++;
                mPagerNum = String.valueOf(mPage);
                systemMessage();
            }
        });
        mLayout.setOnErrorListener(new PullToRefreshLayoutRewrite.onClickCallBackListener() {
            @Override
            public void onCallBackListener(View v) {
                mPage = 1;
                mPagerNum = "1";
                systemMessage();
            }
        });
        systemMessage();
    }

    private void systemMessage() {
        Map<String, Object> map = new HashMap<>();
        map.put("user_id", user_id);
        map.put("type", 2);
        map.put("page", mPagerNum);
        map.put("page_size", mPageSize);
        Log.e("*********map",map.toString());
        GloableConstant.getInstance().showmeidialog(context);
        Xutil.Post(ApiUrl.PUSH, map, new MyCallBack<MessageCenterResponse>() {
            @Override
            public void onSuccess(final MessageCenterResponse result) {
                super.onSuccess(result);
                GloableConstant.getInstance().stopProgressDialog1();
                if (result.getStatus_code().equals("200")) {
                    if (result.getData().size() > 0) {
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
                        
                        mRecyclerView.setAdapter(new CommonAdapter<MessageCenterResponse.mData>(context, R.layout.item_system_activity,mList) {
                            @Override
                            protected void convert(ViewHolder holder, final MessageCenterResponse.mData mData, final int position) {
                                holder.setText(R.id.system_title,mData.getTitle());
                                holder.setText(R.id.system_content,mData.getContent());
                                holder.setText(R.id.system_time,""+mData.getCreated_at());
                                WebView mWebView= (WebView) holder.getConvertView().findViewById(R.id.system_image);
                                WebSettings webSettings = mWebView.getSettings();
                                webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
                                webSettings.setJavaScriptEnabled(true);
                                String test = "<style type='text/css'>p{margin: 0;padding: 0;}img {max-width:100%!important;height:auto!important;font-size:14px;}</style>"+mData.getDetail();
                                mWebView.loadDataWithBaseURL(null, test, "text/html", "utf-8", null);
                                holder.setOnClickListener(R.id.system, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        intent = new Intent();
                                        intent.setClass(SystemActivity.this, MessageDetailActivity.class);
                                        intent.putExtra("_id",mData.getId());
//                                        intent.putExtra("message_id",mData.getId());
                                        startActivity(intent);
                                    }
                                });
                                holder.setOnLongClickListener(R.id.system, new View.OnLongClickListener() {
                                    @Override
                                    public boolean onLongClick(View v) {
                                        messageId = mData.getId();
                                        CancelOrOkDialog dialog = new CancelOrOkDialog(SystemActivity.this, "确定要删除此信息吗?") {
                                            @Override
                                            public void ok() {
                                                super.ok();
                                                result.getData().remove(position);
                                                notifyItemRemoved(position);
                                                //必须调用这行代码
                                                notifyItemRangeChanged(position, result.getData().size());
                                                deleteMessage(messageId);
                                                notifyDataSetChanged();
                                                dismiss();
                                            }
                                        };
                                        dialog.show();
                                        return true;
                                    }
                                });

                            }
                        });
                    } else {
                        if (mPage > 1) {
                            ToastUtil.showShortToast("没有更多数据了");
                        } else {
//                            mLayout.showView(ViewStatus.EMPTY_STATUS);
                            ToastUtil.showShortToast("没有更多数据了");
                        }
                    }
                }
                if (result.getStatus_code().equals("203")) {

                   Log.e("********status",result.getStatus_code());
//                    mLayout.showView(ViewStatus.EMPTY_STATUS);
                    if (mPage > 1) {
                        ToastUtil.showShortToast("没有更多数据了");
                    } else {
//                        mLayout.showView(ViewStatus.EMPTY_STATUS);
                        ToastUtil.showShortToast("没有更多数据了");
                    }
                }
            }

            @Override
            public void onFinished() {
                super.onFinished();
                GloableConstant.getInstance().stopProgressDialog();
                mLayout.finishRefresh();
                mLayout.finishLoadMore();

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
                Log.e("*******ex",ex.getMessage()+"///////");
                GloableConstant.getInstance().stopProgressDialog();
                mLayout.finishRefresh();
                mLayout.finishLoadMore();
                mLayout.showView(ViewStatus.EMPTY_STATUS);
            }
        });
    }

    /**
     * 删除推送消息
     *
     * @param
     */
    private void deleteMessage(String message_id) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", message_id);
        map.put("user_id", user_id);
        map.put("type", 2);
        Xutil.Post(ApiUrl.PUSHDELETE, map, new MyCallBack<SupportResponse>() {
            @Override
            public void onSuccess(SupportResponse result) {
                super.onSuccess(result);
                if (result.getStatus_code().equals("204")) {
                    ToastUtil.showShortToast("删除成功");
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
            }
        });
    }
}
