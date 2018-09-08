package com.jiaoshizige.teacherexam.yy.activity;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

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
import com.jiaoshizige.teacherexam.model.CoinRcordResponse;
import com.jiaoshizige.teacherexam.utils.PullToRefreshLayoutRewrite;
import com.jiaoshizige.teacherexam.utils.SPKeyValuesUtils;
import com.jiaoshizige.teacherexam.utils.SPUtils;
import com.jiaoshizige.teacherexam.utils.ToastUtil;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/1/18 0018.
 */

public class CoinRecordActivity extends BaseActivity {
    @BindView(R.id.recycle_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.pull_refresh)
    PullToRefreshLayoutRewrite mLayout;

    private String user_id;
    private Context mContext;
    private String mPageSize="5";
    private int mPage=1;
    private List<CoinRcordResponse.mData> mList;
    private CommRecyclerViewAdapter<CoinRcordResponse.mData> mAdapter;
    @Override
    protected int getLayoutId() {
        return R.layout.coin_record_layout;
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("教师币充值记录");
        MobclickAgent.onResume(this);
        mPage = 1;
        coinRecord();
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("教师币充值记录");
        MobclickAgent.onPause(this);
    }

    @Override
    protected void initView() {
        setSubTitle().setText("");
        setToolbarTitle().setText("教师币充值记录");
        mContext=this;
        mList=new ArrayList<>();
        user_id= (String) SPUtils.getSpValues(SPKeyValuesUtils.SP_USER_ID,SPUtils.TYPE_STRING);
        mLayout.setRefreshListener(new BaseRefreshListener() {
            @Override
            public void refresh() {
                mPage=1;
                coinRecord();
            }

            @Override
            public void loadMore() {
                mPage++;
                coinRecord();

            }
        });
        coinRecord();
    }
    private void coinRecord(){
        Map<String,Object> map=new HashMap<>();
//        map.put("user_id",1);
        map.put("page",mPage);
        map.put("page_size",mPageSize);
        map.put("user_id", (String) SPUtils.getSpValues(SPKeyValuesUtils.SP_USER_ID, SPUtils.TYPE_STRING));
        GloableConstant.getInstance().startProgressDialog(mContext);
        Xutil.Post(ApiUrl.RECORD,map,new MyCallBack<CoinRcordResponse>(){
            @Override
            public void onSuccess(CoinRcordResponse result) {
                super.onSuccess(result);
                if (result.getStatus_code().equals("200")){
                    Log.e("********result",result.getData().size()+"");
                    GloableConstant.getInstance().stopProgressDialog();
                    mLayout.finishRefresh();
                    mLayout.finishLoadMore();
                    mLayout.showView(ViewStatus.CONTENT_STATUS);
                    if(mPage == 1){
                        getAdapter().updateData(result.getData());
                    } else {
                        getAdapter().appendData(result.getData());
                    }
                   if (result.getData().size()>0){
//                       if (mPageNum.equals("1")){
//                           mList.clear();
//                           mList=result.getData();
//                       }else {
//                           if (null==mList){
//                               mList=result.getData();
//                           }else {
//                               mList.addAll(result.getData());
//                           }
//                       }
//                       mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
//                       mRecyclerView.setAdapter(new CommonAdapter<CoinRcordResponse.mData>(mContext,R.layout.item_coin_record_layout,result.getData()) {
//                           @Override
//                           protected void convert(ViewHolder holder, CoinRcordResponse.mData mData, int position) {
//                               holder.setText(R.id.title,"充值教师币"+mData.getTeacher_coin()+"个");
//                               holder.setText(R.id.time,mData.getCreated_at());
//                               holder.setText(R.id.price,"-￥"+mData.getPay_price());
//                           }
//                       });
                   }else {
                       if (mPage>1){
                           mPage--;
                           ToastUtil.showShortToast("没有更多数据了");
                       }else {
                           mLayout.showView(ViewStatus.EMPTY_STATUS);
                       }
                   }
                }else if (result.getStatus_code().equals("203")){
                    if (mPage>1){
                        mPage--;
                        ToastUtil.showShortToast("没有更多数据了");
                    }else {
                        mLayout.showView(ViewStatus.EMPTY_STATUS);
                    }
                }
            }



            @Override
            public void onFinished() {
                super.onFinished();
                GloableConstant.getInstance().stopProgressDialog();
                mLayout.finishLoadMore();
                mLayout.finishRefresh();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
                GloableConstant.getInstance().stopProgressDialog();
                mLayout.finishLoadMore();
                mLayout.finishRefresh();
                mLayout.showView(ViewStatus.EMPTY_STATUS);
            }
        });
    }

    private CommRecyclerViewAdapter<CoinRcordResponse.mData> getAdapter() {
        if (null == mAdapter) {
            mAdapter = new CommRecyclerViewAdapter<CoinRcordResponse.mData>(this, R.layout.item_coin_record_layout, null) {
                @Override
                protected void convert(ViewHolderZhy holder, final CoinRcordResponse.mData mData, final int position) {
                    holder.setText(R.id.title,"充值教师币"+mData.getTeacher_coin()+"个");
                    holder.setText(R.id.time,mData.getCreated_at());
                    holder.setText(R.id.price,"-￥"+mData.getPay_price());
                }
            };
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            mRecyclerView.setAdapter(mAdapter);
        }
        return mAdapter;
    }
}
