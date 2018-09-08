package com.jiaoshizige.teacherexam.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.jwenfeng.library.pulltorefresh.BaseRefreshListener;
import com.jwenfeng.library.pulltorefresh.ViewStatus;
import com.jiaoshizige.teacherexam.R;
import com.jiaoshizige.teacherexam.activity.LoginActivity;
import com.jiaoshizige.teacherexam.activity.NewClassListActivity;
import com.jiaoshizige.teacherexam.base.GloableConstant;
import com.jiaoshizige.teacherexam.base.MBaseFragment;
import com.jiaoshizige.teacherexam.http.ApiUrl;
import com.jiaoshizige.teacherexam.http.MyCallBack;
import com.jiaoshizige.teacherexam.http.Xutil;
import com.jiaoshizige.teacherexam.model.ClassListResponse;
import com.jiaoshizige.teacherexam.model.NewCourseListResponse;
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
 *
 * 新的课程
 */
public class NewPrivateCourseFragment extends MBaseFragment{
    @BindView(R.id.courses_list)
    RecyclerView mCourseList;
    @BindView(R.id.pulltorefresh)
    PullToRefreshLayoutRewrite mPullToRefresh;
    List<ClassListResponse.mData> mList = new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.new_private_course_layout,container,false);
    }


    @Override
    protected void onInitViews(Bundle savedInstanceState) {
        mPullToRefresh.setRefreshListener(new BaseRefreshListener() {
            @Override
            public void refresh() {
                request();
            }
            @Override
            public void loadMore() {
                request();
            }
        });
      mPullToRefresh.setOnErrorListener(new PullToRefreshLayoutRewrite.onClickCallBackListener() {
          @Override
          public void onCallBackListener(View v) {
              request();
          }
      });
       /* ClassListResponse.mData data1 = new ClassListResponse.mData();
        data1.setGroup_name("0元体验");
        mList.add(data1);
        ClassListResponse.mData data2 = new ClassListResponse.mData();
        data2.setGroup_name("热门推荐");
        mList.add(data2);
        ClassListResponse.mData data3 = new ClassListResponse.mData();
        data3.setGroup_name("VIP");
        mList.add(data3);*/
        request();
    }
    public void request(){
        Map<String,String> map = new HashMap<>();
        GloableConstant.getInstance().startProgressDialog(getActivity());
        Xutil.GET(ApiUrl.COURSETYPE,map,new MyCallBack<NewCourseListResponse>(){
            @Override
            public void onSuccess(NewCourseListResponse result) {
                super.onSuccess(result);
                GloableConstant.getInstance().stopProgressDialog();
                mPullToRefresh.finishRefresh();
                mPullToRefresh.finishLoadMore();
                mPullToRefresh.showView(ViewStatus.CONTENT_STATUS);
                if(result.getStatus_code().equals("200")){
                    if(result.getData() !=null && result.getData().size() > 0){

                        mCourseList.setLayoutManager(new LinearLayoutManager(getActivity()));
                        mCourseList.setAdapter(new CommonAdapter<NewCourseListResponse.mData>(getActivity(),R.layout.item_new_course_fragment,result.getData()) {
                            @Override
                            protected void convert(ViewHolder holder, final NewCourseListResponse.mData mData, int position) {
                                holder.setText(R.id.course_name,mData.getCname());
                                ImageView mCover = (ImageView) holder.getConvertView().findViewById(R.id.course_cover);
                                Glide.with(getActivity()).load(mData.getCimg()).apply(GloableConstant.getInstance().getDefaultOption()).into(mCover);
                                holder.setText(R.id.learn_num,mData.getNum()+"人在学习");
                                holder.setOnClickListener(R.id.course_item, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent();
                                        intent.setClass(getActivity(),NewClassListActivity.class);
                                        intent.putExtra("data", mData);
                                        startActivity(intent);
                                    }
                                });
                            }
                        });
                    }else{
                        mPullToRefresh.showView(ViewStatus.EMPTY_STATUS);
                    }
                }else if(result.getStatus_code().equals("203")){
                    ToastUtil.showShortToast("数据为空");
                    mPullToRefresh.showView(ViewStatus.EMPTY_STATUS);
                }else if (result.getStatus_code().equals("401")){
                    GloableConstant.getInstance().clearAll();
                    ToastUtil.showShortToast("请重新登录");
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                }
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
