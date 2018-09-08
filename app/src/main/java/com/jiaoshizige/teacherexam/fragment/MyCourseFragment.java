package com.jiaoshizige.teacherexam.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.jiaoshizige.teacherexam.activity.ClassCoursesDetialActivity;
import com.jiaoshizige.teacherexam.base.GloableConstant;
import com.jiaoshizige.teacherexam.base.MBaseFragment;
import com.jiaoshizige.teacherexam.http.ApiUrl;
import com.jiaoshizige.teacherexam.http.MyCallBack;
import com.jiaoshizige.teacherexam.http.Xutil;
import com.jiaoshizige.teacherexam.model.CourseCollectResponse;
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
 * Created by Administrator on 2017/12/6 0006.
 */

public class MyCourseFragment extends MBaseFragment {

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.pull_refresh)
    PullToRefreshLayoutRewrite mPullToRefresh;

    private String user_id;
    private Context mContext;
    private String mPageNum="1",mPageSize="5";
    private int mPage=1;
    private List<CourseCollectResponse.mData> mList;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.my_course_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }


    @Override
    protected void onInitViews(Bundle savedInstanceState) {
        mContext = getActivity();
        mList=new ArrayList<>();
        user_id = (String) SPUtils.getSpValues(SPKeyValuesUtils.SP_USER_ID, SPUtils.TYPE_STRING);
        mPullToRefresh.setRefreshListener(new BaseRefreshListener() {
            @Override
            public void refresh() {
                mPage=1;
                mPageNum="1";
                course();
            }

            @Override
            public void loadMore() {
                mPage++;
                mPageNum=String.valueOf(mPage);
                course();
            }
        });
        mPullToRefresh.setOnErrorListener(new PullToRefreshLayoutRewrite.onClickCallBackListener() {
            @Override
            public void onCallBackListener(View v) {
                mPage=1;
                mPageNum="1";
                course();
            }
        });
        course();
    }

    private void course() {
        Map<String, Object> map = new HashMap<>();
//        map.put("user_id", 1);
        map.put("user_id",SPUtils.getSpValues(SPKeyValuesUtils.SP_USER_ID, SPUtils.TYPE_STRING));
        map.put("page",mPageNum);
        map.put("page_size",mPageSize);
        Log.e("*******map", map.toString());
        GloableConstant.getInstance().startProgressDialog(getActivity());
        Xutil.Post(ApiUrl.COURSE, map, new MyCallBack<CourseCollectResponse>() {
            @Override
            public void onSuccess(final CourseCollectResponse result) {
                super.onSuccess(result);
                GloableConstant.getInstance().stopProgressDialog();
                mPullToRefresh.finishRefresh();
                mPullToRefresh.finishLoadMore();
                mPullToRefresh.showView(ViewStatus.CONTENT_STATUS);
                if (result.getStatus_code().equals("200")) {
                    if(result.getData() != null && result.getData().size() > 0) {
                        if (mPageNum.equals("1")) {
                            mList.clear();
                            mList = result.getData();
                        } else {
                            if (null == mList) {
                                mList = result.getData();
                            } else {
                                mList.addAll(result.getData());
                            }
                        }
                        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
                        mRecyclerView.setAdapter(new CommonAdapter<CourseCollectResponse.mData>(mContext, R.layout.item_my_class_layout, result.getData()) {
                            @Override
                            protected void convert(ViewHolder holder, final CourseCollectResponse.mData mData, final int position) {
                                holder.setText(R.id.course_name, mData.getName());
                                holder.setText(R.id.sell_num, mData.getSale_num() + "人在学习");
                                ImageView mCourseCover = (ImageView) holder.getConvertView().findViewById(R.id.book_cover);
                                Glide.with(mContext).load(mData.getCover_image()).apply(GloableConstant.getInstance().getDefaultOptionssmall()).into(mCourseCover);
                                holder.setOnClickListener(R.id.item_book, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent();
                                        intent.setClass(mContext, ClassCoursesDetialActivity.class);
                                        intent.putExtra("flag", "1");
                                        intent.putExtra("type", "1");
                                        intent.putExtra("group_id", mData.getId());
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
