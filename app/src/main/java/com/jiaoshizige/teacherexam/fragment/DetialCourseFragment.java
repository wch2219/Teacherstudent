package com.jiaoshizige.teacherexam.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.jiaoshizige.teacherexam.R;
import com.jiaoshizige.teacherexam.activity.CoursesDetialActivity;
import com.jiaoshizige.teacherexam.base.GloableConstant;
import com.jiaoshizige.teacherexam.base.MBaseFragment;
import com.jiaoshizige.teacherexam.http.ApiUrl;
import com.jiaoshizige.teacherexam.http.MyCallBack;
import com.jiaoshizige.teacherexam.http.Xutil;
import com.jiaoshizige.teacherexam.model.CourseResponse;
import com.jiaoshizige.teacherexam.utils.SPKeyValuesUtils;
import com.jiaoshizige.teacherexam.utils.SPUtils;
import com.jiaoshizige.teacherexam.utils.SwipeRefreshView;
import com.jiaoshizige.teacherexam.utils.ToastUtil;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/a1/20.
 * 详情页的 课程列表
 * mType 1 已拥有的课程 2未拥有
 */

public class DetialCourseFragment extends MBaseFragment {
    @BindView(R.id.course_recycler)
    RecyclerView mCourseRecycler;
    @BindView(R.id.layout_swipe_refresh)
    SwipeRefreshView mSwipeRefreshView;
//    @BindView(R.id.pulltorefresh)
//    PullToRefreshLayoutRewrite mPullToRefresh;
    private String mType;
    private String mGroupId;
    private String mPageNum = "1";
    private String mPageSize = "10";
    private int mPage = 1;
    public DetialCourseFragment() {
    }
    @SuppressLint("ValidFragment")
    public DetialCourseFragment(String type, String group_id) {
        this.mType = type;
        this.mGroupId = group_id;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.detial_course_fragment,container,false);
    }
    @Override
    protected void onInitViews(Bundle savedInstanceState) {
        mSwipeRefreshView.setEnabled(false);
  /*      mPullToRefresh.setRefreshListener(new BaseRefreshListener() {
            @Override
            public void refresh() {
                mPageNum = "1";
                requestCourseList();
            }
            @Override
            public void loadMore() {
                mPage++;
                mPageNum = String.valueOf(mPage);
                requestCourseList();
            }
        });
        mPullToRefresh.setOnErrorListener(new PullToRefreshLayoutRewrite.onClickCallBackListener() {
            @Override
            public void onCallBackListener(View v) {
                mPageNum = "1";
                requestCourseList();
            }
        });*/

        requestCourseList();
    }

    private void requestCourseList(){
        Map<String,Object> map = new HashMap<>();
        map.put("group_id",mGroupId);
//        map.put("user_id","1");
        map.put("user_id", SPUtils.getSpValues(SPKeyValuesUtils.SP_USER_ID, SPUtils.TYPE_STRING));
        Log.e("TTG",map.toString());
        Xutil.Post(ApiUrl.CLASSCOURSELIST,map,new MyCallBack<CourseResponse>(){
            @Override
            public void onSuccess(CourseResponse result) {
                super.onSuccess(result);
//                mPullToRefresh.finishRefresh();
//                mPullToRefresh.finishLoadMore();
//                mPullToRefresh.showView(ViewStatus.CONTENT_STATUS);
                if(result.getStatus_code().equals("200")){
                    if(result.getData() != null && result.getData().size() > 0){
                        mCourseRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
                        mCourseRecycler.setAdapter(new CommonAdapter<CourseResponse.mData>(getActivity(),R.layout.item_my_course_fragment,result.getData()) {
                            @Override
                            protected void convert(ViewHolder holder, final CourseResponse.mData mData, int position) {
                                holder.setText(R.id.course_name,mData.getName());
                                holder.setText(R.id.course_price,"￥"+mData.getPrice());
                                holder.setText(R.id.student_num,mData.getSale_num()+"人在学习");
                                TextView mOriginalPrice = (TextView) holder.getConvertView().findViewById(R.id.original_price);
                                mOriginalPrice.setText("￥"+mData.getMarket_price());
                                mOriginalPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                                holder.setText(R.id.original_price,"￥"+mData.getMarket_price());
                                ImageView mCoverImage = (ImageView) holder.getConvertView().findViewById(R.id.cover_photo);
                                Log.e("********mData.getImag",mData.getImage());
                                Glide.with(getActivity()).load(mData.getImage()).apply(GloableConstant.getInstance().getDefaultOptionssmall()).into(mCoverImage);
                                if(mData.getBook_id() != null && !mData.getBook_id().equals("")){
                                    holder.setVisible(R.id.give_book,true);
                                }else{
                                    holder.setVisible(R.id.give_book,false);
                                }
                                holder.setOnClickListener(R.id.course_item, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent();
                                        intent.setClass(getActivity(),CoursesDetialActivity.class);
                                        intent.putExtra("type",mType);
                                        intent.putExtra("flag","0");
                                        intent.putExtra("course_id",mData.getId());
                                        startActivity(intent);
                                    }
                                });
                            }
                        });
                    }else{
                        if(mPage > 1){
                            ToastUtil.showShortToast("没有更多数据了");
                        }else {
                            Log.e("TAG","SSSc22222");
//                            mPullToRefresh.showView(ViewStatus.EMPTY_STATUS);
                        }
                    }
                }else if(result.getStatus_code().equals("204")){
                    if(mPage > 1){
                        ToastUtil.showShortToast("没有更多数据了");
                    }else {
                        Log.e("TAG","SSSc22222");
//                        mPullToRefresh.showView(ViewStatus.EMPTY_STATUS);
                    }
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
//                mPullToRefresh.finishRefresh();
//                mPullToRefresh.finishLoadMore();
//                mPullToRefresh.showView(ViewStatus.ERROR_STATUS);
            }
        });
    }
}
