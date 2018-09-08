package com.jiaoshizige.teacherexam.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.jwenfeng.library.pulltorefresh.BaseRefreshListener;
import com.jwenfeng.library.pulltorefresh.ViewStatus;
import com.jiaoshizige.teacherexam.R;
import com.jiaoshizige.teacherexam.activity.MyClassDetialActivity;
import com.jiaoshizige.teacherexam.base.GloableConstant;
import com.jiaoshizige.teacherexam.base.MBaseFragment;
import com.jiaoshizige.teacherexam.http.ApiUrl;
import com.jiaoshizige.teacherexam.http.MyCallBack;
import com.jiaoshizige.teacherexam.http.Xutil;
import com.jiaoshizige.teacherexam.model.MineStudyResponse;
import com.jiaoshizige.teacherexam.utils.PullToRefreshLayoutRewrite;
import com.jiaoshizige.teacherexam.utils.SPKeyValuesUtils;
import com.jiaoshizige.teacherexam.utils.SPUtils;
import com.jiaoshizige.teacherexam.utils.ToastUtil;
import com.jiaoshizige.teacherexam.widgets.ProgressBarView;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/12/6 0006.
 */

public class MyClassFragment extends MBaseFragment {
    @BindView(R.id.class_list)
    RecyclerView mRecyclerView;
    @BindView(R.id.nodata)
    ImageView imageView;
    @BindView(R.id.pulltorefresh)
    PullToRefreshLayoutRewrite mPullToRefresh;

    private String user_id,group_id;
    private Context mContext;
    private Intent intent;
    private String mPageNum="1",mPageSize="5";
    private int mPage=1;
    private List<MineStudyResponse.mData> mList;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.class_fragment_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mContext = getActivity();
        intent=new Intent();
        mList=new ArrayList<>();
        user_id = (String) SPUtils.getSpValues(SPKeyValuesUtils.SP_USER_ID, SPUtils.TYPE_STRING);
        mPullToRefresh.setRefreshListener(new BaseRefreshListener() {
            @Override
            public void refresh() {
                mPage=1;
                mPageNum="1";
                postGroup();
            }

            @Override
            public void loadMore() {
                mPage++;
                mPageNum=String.valueOf(mPage);
                postGroup();
            }
        });
        mPullToRefresh.setOnErrorListener(new PullToRefreshLayoutRewrite.onClickCallBackListener() {
            @Override
            public void onCallBackListener(View v) {
                mPage = 1;
                mPageNum = "1";
                postGroup();
            }
        });
        postGroup();
    }

    @Override
    protected void onInitViews(Bundle savedInstanceState) {

    }

    private void  postGroup(){
        Map<String,Object> map = new HashMap<>();
//        map.put("user_id","1");
        map.put("user_id",SPUtils.getSpValues(SPKeyValuesUtils.SP_USER_ID, SPUtils.TYPE_STRING));
        map.put("page",mPageNum);
        map.put("page_size",mPageSize);
        Log.e("TGGS",map.toString());
        GloableConstant.getInstance().startProgressDialog(getActivity());
        Xutil.Post(ApiUrl.MYGROUP,map,new MyCallBack<MineStudyResponse>(){
            @Override
            public void onSuccess(MineStudyResponse result) {
                super.onSuccess(result);
                GloableConstant.getInstance().stopProgressDialog();
                mPullToRefresh.finishRefresh();
                mPullToRefresh.finishLoadMore();
                mPullToRefresh.showView(ViewStatus.CONTENT_STATUS);
                if(result.getStatus_code().equals("200")){
                    if(result.getData() != null && result.getData().size() > 0){
                        if (mPageNum.equals("1")){
                            mList.clear();
                            mList=result.getData();
                        }else {
                            if (null==mList){
                                mList=result.getData();
                            }else {
                                mList.addAll(result.getData());
                            }
                        }
                        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                        mRecyclerView.setAdapter(new CommonAdapter<MineStudyResponse.mData>(getActivity(),R.layout.item_class_study,result.getData()) {
                            @Override
                            protected void convert(ViewHolder holder, final MineStudyResponse.mData mData, int position) {
                                holder.setText(R.id.class_name,mData.getGroup_name());
                                ImageView mClassCover = (ImageView) holder.getConvertView().findViewById(R.id.class_cover);
                                Glide.with(getActivity()).load(mData.getImages()).apply(GloableConstant.getInstance().getDefaultOptionssmall()).into(mClassCover);
                                ProgressBarView mViewBar = (ProgressBarView) holder.getConvertView().findViewById(R.id.view_bar);
                                mViewBar.setProgress((int) (Double.valueOf(mData.getLearn_percent()) * 3.6));
//                                mViewBar.setProgress((Integer.valueOf(mData.getLearn_percent())));
                                mViewBar.setReachedBarColor(ContextCompat.getColor(getActivity(),R.color.buy_color));
                                mViewBar.SetTextColor(ContextCompat.getColor(getActivity(),R.color.buy_color));
                                mViewBar.setText(String.valueOf((int)(Integer.valueOf(mData.getLearn_percent()) / 3.6))+ "%");
                                final String progress = String.valueOf((int)(Integer.valueOf(mData.getLearn_percent()) / 3.6));
                                holder.setOnClickListener(R.id.item_mine_class, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent();
                                        intent.putExtra("id",mData.getId());
                                        intent.putExtra("progress",progress);
//
                                        intent.setClass(getActivity(),MyClassDetialActivity.class);
                                        startActivity(intent);
                                    }
                                });
                            }
                        });
                    }else{
                        if(mPage > 1){
                            ToastUtil.showShortToast("没有更多数据了");
                        }else{
                            mPullToRefresh.showView(ViewStatus.EMPTY_STATUS);
                        }
                    }
                }else if(result.getStatus_code().equals("203")){
                    if(mPage > 1){
                        ToastUtil.showShortToast("没有更多数据了");
                    }else{
                        mPullToRefresh.showView(ViewStatus.EMPTY_STATUS);
                    }
                }
            }

            @Override
            public void onFinished() {
                super.onFinished();
                GloableConstant.getInstance().stopProgressDialog();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
                GloableConstant.getInstance().stopProgressDialog();
                mPullToRefresh.finishRefresh();
                mPullToRefresh.finishLoadMore();
                mPullToRefresh.showView(ViewStatus.EMPTY_STATUS);
            }
        });
    }
}
