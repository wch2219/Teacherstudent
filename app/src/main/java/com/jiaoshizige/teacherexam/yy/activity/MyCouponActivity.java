package com.jiaoshizige.teacherexam.yy.activity;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.jwenfeng.library.pulltorefresh.BaseRefreshListener;
import com.jwenfeng.library.pulltorefresh.ViewStatus;
import com.umeng.analytics.MobclickAgent;
import com.jiaoshizige.teacherexam.R;
import com.jiaoshizige.teacherexam.adapter.MyCouponAdapter;
import com.jiaoshizige.teacherexam.base.BaseActivity;
import com.jiaoshizige.teacherexam.base.GloableConstant;
import com.jiaoshizige.teacherexam.http.ApiUrl;
import com.jiaoshizige.teacherexam.http.MyCallBack;
import com.jiaoshizige.teacherexam.http.Xutil;
import com.jiaoshizige.teacherexam.model.MyCouponResponse;
import com.jiaoshizige.teacherexam.utils.PullToRefreshLayoutRewrite;
import com.jiaoshizige.teacherexam.utils.SPKeyValuesUtils;
import com.jiaoshizige.teacherexam.utils.SPUtils;
import com.jiaoshizige.teacherexam.utils.ToastUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/a1/9 0009.
 * 我的卡券
 */

public class MyCouponActivity extends BaseActivity {
    @BindView(R.id.list_view)
    ListView mListView;
    @BindView(R.id.pull_refresh)
    PullToRefreshLayoutRewrite mPtr;
    private MyCouponAdapter mMyCouponAdapter;
    private Context mContext;
    private String user_id;
    private String mPageSize="5";
    private int mPage=1;
    private List<MyCouponResponse.mData> mList;
    @Override
    protected int getLayoutId() {
        return R.layout.my_coupon_activity;
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("我的卡券");
        MobclickAgent.onResume(this);

    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("我的卡券");
        MobclickAgent.onPause(this);
    }

    @Override
    protected void initView() {
        setSubTitle().setText("");
        setToolbarTitle().setText("我的卡券");
        mContext=this;
        mList=new ArrayList<>();
        user_id= (String) SPUtils.getSpValues(SPKeyValuesUtils.SP_USER_ID,SPUtils.TYPE_STRING);
        mMyCouponAdapter=new MyCouponAdapter(mContext);
        mListView.setAdapter(mMyCouponAdapter);

        mPtr.setRefreshListener(new BaseRefreshListener() {
            @Override
            public void refresh() {
                mPage=1;
                postCoupon();
            }

            @Override
            public void loadMore() {
                mPage++;
                postCoupon();
            }
        });
        mPtr.setOnErrorListener(new PullToRefreshLayoutRewrite.onClickCallBackListener() {
            @Override
            public void onCallBackListener(View v) {
                mPage=1;
                postCoupon();
            }
        });
        postCoupon();
    }
    private void postCoupon(){
        Map<String,Object> map=new HashMap<>();
        map.put("user_id",user_id);
        map.put("page",mPage);
        map.put("page_size",mPageSize);
        Log.e("********map",map.toString());
        GloableConstant.getInstance().startProgressDialog(this);
        Xutil.Post(ApiUrl.MYCOUPON,map,new MyCallBack<MyCouponResponse>(){
            @Override
            public void onSuccess(MyCouponResponse result) {
                super.onSuccess(result);
//                mPtr.showView(ViewStatus.CONTENT_STATUS);
                mPtr.finishLoadMore();
                mPtr.finishRefresh();
                GloableConstant.getInstance().stopProgressDialog();
                if (result.getStatus_code().equals("200")){
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
//                       mMyCouponAdapter.setmList(result.getData());
                       if(mPage == 1){
                           mMyCouponAdapter.setmList(result.getData());
                       } else {
                           mMyCouponAdapter.addAll(result.getData());
                       }
                   }else {
                       if (mPage>1){
                           mPage--;
                           ToastUtil.showShortToast("没有更多数据了");
                       }else {
                           mPtr.showView(ViewStatus.EMPTY_STATUS);
                       }
                   }
                }else if (result.getStatus_code().equals("203")){
                    if (mPage>1){
                        mPage--;
                        ToastUtil.showShortToast("没有更多数据了");
                    }else {
                        mPtr.showView(ViewStatus.EMPTY_STATUS);
                    }
                }
            }

            @Override
            public void onFinished() {
                super.onFinished();
                GloableConstant.getInstance().stopProgressDialog();
                mPtr.finishRefresh();
                mPtr.finishLoadMore();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
                GloableConstant.getInstance().stopProgressDialog();
                mPtr.finishRefresh();
                mPtr.finishLoadMore();
                mPtr.showView(ViewStatus.EMPTY_STATUS);
            }
        });

    }
}
