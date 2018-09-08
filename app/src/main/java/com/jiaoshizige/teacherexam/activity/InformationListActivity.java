package com.jiaoshizige.teacherexam.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

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
import com.jiaoshizige.teacherexam.model.InformationResponse;
import com.jiaoshizige.teacherexam.utils.PullToRefreshLayoutRewrite;
import com.jiaoshizige.teacherexam.utils.SPKeyValuesUtils;
import com.jiaoshizige.teacherexam.utils.SPUtils;
import com.jiaoshizige.teacherexam.utils.ToastUtil;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/a1/22.
 * 资讯列表
 */

public class InformationListActivity extends BaseActivity{
    @BindView(R.id.class_list)
    RecyclerView mInformationListRecyclerView;
    @BindView(R.id.pulltorefresh)
    PullToRefreshLayoutRewrite mPullToRefresh;
    private String mPageNum = "1";
    private String mPageSize = "10";
    private int mPage = 1;
    @Override
    protected int getLayoutId() {
        return R.layout.information_list_activity;
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("资讯列表");
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("资讯列表");
        MobclickAgent.onPause(this);
    }

    @Override
    protected void initView() {
        setSubTitle().setText("");
        setToolbarTitle().setText("资讯");
        requestInfoList();
        mPullToRefresh.setRefreshListener(new BaseRefreshListener() {
            @Override
            public void refresh() {
                mPageNum = "1";
                requestInfoList();
            }

            @Override
            public void loadMore() {
                mPage ++;
                mPageNum = String.valueOf(mPage);
                requestInfoList();
            }
        });
        mPullToRefresh.setOnErrorListener(new PullToRefreshLayoutRewrite.onClickCallBackListener() {
            @Override
            public void onCallBackListener(View v) {
                mPageNum = "1";
                requestInfoList();
            }
        });
    }
    private void requestInfoList(){
        Map<String,String> map = new HashMap<>();
        map.put("page",mPageNum);
        map.put("page_size",mPageSize);
        map.put("user_id", (String) SPUtils.getSpValues(SPKeyValuesUtils.SP_USER_ID,SPUtils.TYPE_STRING));
        GloableConstant.getInstance().startProgressDialog(this);
        Xutil.GET(ApiUrl.ARTICLELIST,map,new MyCallBack<InformationResponse>(){
            @Override
            public void onSuccess(InformationResponse result) {
                super.onSuccess(result);
                GloableConstant.getInstance().stopProgressDialog1();
                mPullToRefresh.finishRefresh();
                mPullToRefresh.finishLoadMore();
                mPullToRefresh.showView(ViewStatus.CONTENT_STATUS);
                if(result.getStatus_code().equals("200")) {
                    if (result.getData() != null && result.getData().size() > 0) {
                        mInformationListRecyclerView.setLayoutManager(new LinearLayoutManager(InformationListActivity.this));
                        mInformationListRecyclerView.setAdapter(new CommonAdapter<InformationResponse.mData>(InformationListActivity.this, R.layout.item_information_fragment_layout, result.getData()) {
                            @Override
                            protected void convert(ViewHolder holder, final InformationResponse.mData s, int position) {
                                holder.setText(R.id.information_product_name, s.getTitle());
                                holder.setText(R.id.information_product_describe, s.getDescription());
                                holder.setText(R.id.information_product_people, s.getView_count() + "人已看");
                                holder.setText(R.id.information_product_date, s.getCreated_at());
//                            holder.setText(R.id.information_product_image, Glide.with(this).load(s.getImages()).apply().into(holder.));
                                ImageView mImg = (ImageView) holder.getConvertView().findViewById(R.id.information_product_image);
                                Glide.with(InformationListActivity.this).load(s.getImages()).apply(GloableConstant.getInstance().getDefaultOptionssmall()).into(mImg);
                                holder.setOnClickListener(R.id.information_list_item, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent();
                                        intent.setClass(InformationListActivity.this, InformationDetialActivity.class);
                                        intent.putExtra("info_id", s.getId());
                                        intent.putExtra("title",s.getTitle());
                                        intent.putExtra("content",s.getDescription());
                                        intent.putExtra("image",s.getImages());
                                        startActivity(intent);
                                    }
                                });
                            }
                        });
                    }else{
                        mPullToRefresh.showView(ViewStatus.EMPTY_STATUS);
                    }
                }else if(result.getStatus_code().equals("203")){
                    ToastUtil.showShortToast("没有更多数据");
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
                GloableConstant.getInstance().stopProgressDialog1();
                mPullToRefresh.finishRefresh();
                mPullToRefresh.finishLoadMore();
                mPullToRefresh.showView(ViewStatus.EMPTY_STATUS);
            }
        });
    }
}
