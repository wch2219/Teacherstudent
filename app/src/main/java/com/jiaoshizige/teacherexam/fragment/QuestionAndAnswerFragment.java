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

import com.jwenfeng.library.pulltorefresh.BaseRefreshListener;
import com.jwenfeng.library.pulltorefresh.ViewStatus;
import com.jiaoshizige.teacherexam.R;
import com.jiaoshizige.teacherexam.yy.activity.QuestionDetialActivity;
import com.jiaoshizige.teacherexam.base.MBaseFragment;
import com.jiaoshizige.teacherexam.http.ApiUrl;
import com.jiaoshizige.teacherexam.http.MyCallBack;
import com.jiaoshizige.teacherexam.http.Xutil;
import com.jiaoshizige.teacherexam.model.QuestionAnswerReponse;
import com.jiaoshizige.teacherexam.utils.PullToRefreshLayoutRewrite;
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
 * 课程问答
 */

public class QuestionAndAnswerFragment extends MBaseFragment {
    @BindView(R.id.question_answer_list)
    RecyclerView mQuestionAnswerList;
//    @BindView(R.id.course_no_data)
//    LinearLayout mCourseNoData;
//    @BindView(R.id.course_error)
//    LinearLayout mCourseError;
    @BindView(R.id.pulltorefresh)
    PullToRefreshLayoutRewrite mPullToRefresh;
    @BindView(R.id.scroll)
   NestedScrollView NestedScrollView;
    private String mPageSize = "10";
    private String mPageNum = "1";
    private int mPage = 1;
    private String mGroupId;
    List<QuestionAnswerReponse.mData> mDataList = new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.question_answer_fragment,container,false);
    }

    @SuppressLint("ValidFragment")
    public QuestionAndAnswerFragment(String group_id) {
        this.mGroupId = group_id;
    }

    public QuestionAndAnswerFragment() {
    }

    @Override
    protected void onInitViews(Bundle savedInstanceState) {
        mPullToRefresh.setRefreshListener(new BaseRefreshListener() {
            @Override
            public void refresh() {
                mPage = 1;
                mPageNum = "1";
                requestQuestionAnswer();
            }

            @Override
            public void loadMore() {
                mPage ++;
                mPageNum = String.valueOf(mPage);
                requestQuestionAnswer();
            }
        });
       mPullToRefresh.setOnErrorListener(new PullToRefreshLayoutRewrite.onClickCallBackListener() {
           @Override
           public void onCallBackListener(View v) {
               mPageNum = "1";
               mPage = 1;
               requestQuestionAnswer();//网络失败 点击重新加载
           }
       });
        mQuestionAnswerList.setNestedScrollingEnabled(false);
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
        requestQuestionAnswer();
    }

    /**
     * 问答列表
     */
    private void requestQuestionAnswer(){
        Map<String,String> map = new HashMap<>();
        map.put("page",mPageNum);
        map.put("page_size",mPageSize);
        map.put("course_id",mGroupId);
//        map.put("course_id","8");
        Log.e("SSD",map.toString());
        Log.e("TAG","4");
//        GloableConstant.getInstance().startProgressDialog(getActivity());
        Xutil.GET(ApiUrl.QALIST,map,new MyCallBack<QuestionAnswerReponse>(){
            @Override
            public void onSuccess(QuestionAnswerReponse result) {
                super.onSuccess(result);
//                GloableConstant.getInstance().stopProgressDialog1();
                mPullToRefresh.finishRefresh();
                mPullToRefresh.finishLoadMore();
                mPullToRefresh.showView(ViewStatus.CONTENT_STATUS);
                Log.e("TAG","44");
//                mCourseNoData.setVisibility(View.GONE);
//                mCourseError.setVisibility(View.GONE);
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
                        mQuestionAnswerList.setLayoutManager(new LinearLayoutManager(getActivity()));
                        mQuestionAnswerList.setAdapter(new CommonAdapter<QuestionAnswerReponse.mData>(getActivity(),R.layout.item_question_answer,mDataList) {
                            @Override
                            protected void convert(ViewHolder holder, final QuestionAnswerReponse.mData mData, int position) {
                                holder.setText(R.id.question,mData.getContent());
                                holder.setText(R.id.sanwer,mData.getAnswer().getContent());
                                holder.setText(R.id.sanwer_num,"查看"+mData.getCount()+"人回答");
                                holder.setText(R.id.question_time,mData.getCreated_at());
                                final Intent intent = new Intent();
                                intent.setClass(getActivity(),QuestionDetialActivity.class);
                                holder.setOnClickListener(R.id.all_item, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        intent.putExtra("q_id",mData.getId());
                                        startActivity(intent);
                                    }
                                });
                                holder.setOnClickListener(R.id.sanwer_num, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        intent.putExtra("q_id",mData.getId());
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
                Log.e("TAG","44"+ex.getMessage());
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
