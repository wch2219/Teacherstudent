package com.jiaoshizige.teacherexam.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.jwenfeng.library.pulltorefresh.BaseRefreshListener;
import com.jwenfeng.library.pulltorefresh.ViewStatus;
import com.jiaoshizige.teacherexam.R;
import com.jiaoshizige.teacherexam.yy.activity.YYHomeWorkDetailActivity;
import com.jiaoshizige.teacherexam.base.GloableConstant;
import com.jiaoshizige.teacherexam.base.MBaseFragment;
import com.jiaoshizige.teacherexam.http.ApiUrl;
import com.jiaoshizige.teacherexam.http.MyCallBack;
import com.jiaoshizige.teacherexam.http.Xutil;
import com.jiaoshizige.teacherexam.model.HomeWorkListResponse;
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
 * Created by Administrator on 2017/a1/15.
 * 课程 作业
 */

public class HomeworkFragment extends MBaseFragment {
    @BindView(R.id.homework_list)
    RecyclerView mHomeWorkList;
//    @BindView(R.id.course_no_data)
//    LinearLayout mCourseNoData;
//    @BindView(R.id.course_error)
//    LinearLayout mCourseError;
    @BindView(R.id.pulltorefresh)
    PullToRefreshLayoutRewrite mPullToRefresh;
    @BindView(R.id.scroll)
   NestedScrollView NestedScrollView;
    private String mCourseId;
    private String mPageNum = "1";
    private String mPageSize = "15";
    private String mChapterId;
    private int mPage = 1;
    List<HomeWorkListResponse.mData> mDataList = new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.course_honework_fragment,container,false);
    }

    public HomeworkFragment() {
    }

    @SuppressLint("ValidFragment")
    public HomeworkFragment(String course_id) {
        this.mCourseId = course_id;
    }

    @Override
    protected void onInitViews(Bundle savedInstanceState) {
//        reuqestHomeWork("");
        mPullToRefresh.setRefreshListener(new BaseRefreshListener() {
            @Override
            public void refresh() {
              mPageNum = "1";
                mPage = 1;
                reuqestHomeWork(mChapterId);
            }

            @Override
            public void loadMore() {
              mPage++;
                mPageNum = String.valueOf(mPage);
                reuqestHomeWork(mChapterId);
            }
        });
        mPullToRefresh.setOnErrorListener(new PullToRefreshLayoutRewrite.onClickCallBackListener() {
            @Override
            public void onCallBackListener(View v) {
                mPageNum = "1";
                mPage = 1;
                reuqestHomeWork(mChapterId);
            }
        });
        mHomeWorkList.setNestedScrollingEnabled(false);
        NestedScrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                mPullToRefresh.setEnabled
                        (NestedScrollView.getScrollY() == 0);
            }
        });
    }
    @Override
    public void onResume() {
        super.onResume();
        reuqestHomeWork("");
    }
    public void backToTop(){
        mHomeWorkList.smoothScrollToPosition(0);
    }
    public void changeChapter(String chapter_id){
        mChapterId = chapter_id;
        reuqestHomeWork(chapter_id);
    }
    private void reuqestHomeWork(String chapter_id){
        Map<String,String> map = new HashMap<>();
        map.put("page",mPageNum);
        map.put("page_size",mPageSize);
        map.put("course_id",mCourseId);
        map.put("user_id", (String) SPUtils.getSpValues(SPKeyValuesUtils.SP_USER_ID, SPUtils.TYPE_STRING));
        map.put("chapter_id",chapter_id);
        Log.e("TAG",map.toString());
        Log.e("TAG","2");
//        GloableConstant.getInstance().startProgressDialog(getActivity());
        Xutil.GET(ApiUrl.HOMEWORKLIST,map,new MyCallBack<HomeWorkListResponse>(){
            @Override
            public void onSuccess(HomeWorkListResponse result) {
                super.onSuccess(result);
//                GloableConstant.getInstance().stopProgressDialog1();
                mPullToRefresh.finishRefresh();
                mPullToRefresh.finishLoadMore();
                mPullToRefresh.showView(ViewStatus.CONTENT_STATUS);
//                mCourseNoData.setVisibility(View.GONE);
//                mCourseError.setVisibility(View.GONE);
                Log.e("TAG","22");
                if(result.getStatus_code().equals("200")){
                    if(result.getData() != null && result.getData().size() > 0){
                        if(mPageNum.equals("1")){
                            mDataList.clear();
                            mDataList = result.getData();
                        }else{
                            if (null == mDataList){
                                mDataList = result.getData();
                            }
                            else{
                                mDataList.addAll(result.getData());
                            }
                        }
//                        LinearLayoutManager layout = new FastScrollManger(getActivity(), LinearLayoutManager.VERTICAL, false);
//                        mHomeWorkList.setLayoutManager(layout);//竖直放置
                        mHomeWorkList.setLayoutManager(new LinearLayoutManager(getActivity()));
                        mHomeWorkList.setAdapter(new CommonAdapter<HomeWorkListResponse.mData>(getActivity(),R.layout.item_homework_list,mDataList) {
                            @Override
                            protected void convert(final ViewHolder holder, final HomeWorkListResponse.mData mData, int position) {
                                holder.setText(R.id.title,mData.getTitle());
                                holder.setText(R.id.class_teacher,mData.getName());
                                holder.setText(R.id.create_time,mData.getCreated_at());
                                holder.setText(R.id.remark,mData.getRemark());
                                holder.setText(R.id.answer_num,mData.getCount());
                                ImageView mCover = (ImageView) holder.getConvertView().findViewById(R.id.cover);
                                Glide.with(getActivity()).load(mData.getImages()).apply(GloableConstant.getInstance().getDefaultOptionssmall()).into(mCover);
                                if(mData.getIs_done().equals("1")){
                                    holder.setVisible(R.id.complete,true);
                                }else{
                                    holder.setVisible(R.id.complete,false);
                                }
                                holder.setOnClickListener(R.id.item_homework, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                      //
                                        Intent intent = new Intent();
//                                        intent.setClass(getActivity(), HomeWorkDetailActivity.class);
                                        intent.setClass(getActivity(), YYHomeWorkDetailActivity.class);
                                        intent.putExtra("work_id",mData.getId());
                                        intent.putExtra("is_done",mData.getIs_done());
                                        startActivity(intent);
                                    }
                                });
                            }
                        });
                    }else{
                        if(mPage > 1){
                            ToastUtil.showShortToast("没有更多数据了");
                        }else{
                            if(mHomeWorkList.getAdapter().getItemCount() < 1){
                                mPullToRefresh.showView(ViewStatus.EMPTY_STATUS);
                            }

//                            mCourseNoData.setVisibility(View.VISIBLE);
                        }
                    }
                }else if(result.getStatus_code().equals("203")){
                    if(mPage > 1){
                        ToastUtil.showShortToast("没有更多数据了");
                    }else{
                        mPullToRefresh.showView(ViewStatus.EMPTY_STATUS);
//                        mCourseNoData.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
//                GloableConstant.getInstance().stopProgressDialog1();
//                mCourseError.setVisibility(View.VISIBLE);
//                mCourseNoData.setVisibility(View.GONE);
                mPullToRefresh.finishRefresh();
                mPullToRefresh.finishLoadMore();
                mPullToRefresh.showView(ViewStatus.EMPTY_STATUS);
            }
        });
    }
}
