package com.jiaoshizige.teacherexam.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jwenfeng.library.pulltorefresh.BaseRefreshListener;
import com.jwenfeng.library.pulltorefresh.ViewStatus;
import com.jiaoshizige.teacherexam.R;
import com.jiaoshizige.teacherexam.base.BaseActivity;
import com.jiaoshizige.teacherexam.base.GloableConstant;
import com.jiaoshizige.teacherexam.http.ApiUrl;
import com.jiaoshizige.teacherexam.http.MyCallBack;
import com.jiaoshizige.teacherexam.http.Xutil;
import com.jiaoshizige.teacherexam.model.ClassListResponse;
import com.jiaoshizige.teacherexam.model.NewCourseListListResponse;
import com.jiaoshizige.teacherexam.model.NewCourseListResponse;
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
 * Created by Administrator on 2018/3/13.
 * 0元体验  热门推荐 VIP精讲  公用
 */

public class NewClassListActivity extends BaseActivity{
    @BindView(R.id.class_list)
    RecyclerView mCourseList;
    @BindView(R.id.pulltorefresh)
    PullToRefreshLayoutRewrite mPullToRefresh;
    List<ClassListResponse.mData> mList = new ArrayList<>();
    private String mPage = "1";
    private String mPageSize = "15";
    private int mPageNum = 1;
    private Context mContext;
    private NewCourseListResponse.mData mListData;
    @Override
    protected int getLayoutId() {
        return R.layout.information_list_activity;
    }

    @Override
    protected void initView() {
        mContext=this;
        if(getIntent().getExtras().get("data") !=null){
            mListData = (NewCourseListResponse.mData) getIntent().getExtras().get("data");
        }
        Log.e("*********mListData",mListData.getId());
        setSubTitle().setText("");
        setToolbarTitle().setText(mListData.getCname());
        mPullToRefresh.setRefreshListener(new BaseRefreshListener() {
            @Override
            public void refresh() {
//                mPage = "1";
//                mPageNum = 1;
//                requestList();
                mPageNum++;
                mPage = String.valueOf(mPageNum);
                requestList();
            }
            @Override
            public void loadMore() {
                mPageNum++;
                mPage = String.valueOf(mPageNum);
                requestList();
            }
        });
        mPullToRefresh.setOnErrorListener(new PullToRefreshLayoutRewrite.onClickCallBackListener() {
            @Override
            public void onCallBackListener(View v) {
                mPage = "1";
                mPageNum = 1;
                requestList();
            }
        });
        requestList();
    }

    List<NewCourseListListResponse.mData> mDataList = new ArrayList<>();
    private void requestList(){
        Map<String,String> map = new HashMap<>();
        map.put("type_id",mListData.getId());
        map.put("page",mPage);
        map.put("page_size",mPageSize);
        map.put("user_id", (String) SPUtils.getSpValues(SPKeyValuesUtils.SP_USER_ID,SPUtils.TYPE_STRING));
        Log.e("********requestListmap",map.toString());
        GloableConstant.getInstance().startProgressDialog(NewClassListActivity.this);
        Xutil.GET(ApiUrl.COURSETYPELISTS,map,new MyCallBack<NewCourseListListResponse>(){
            @Override
            public void onSuccess(final NewCourseListListResponse result) {
                super.onSuccess(result);
                GloableConstant.getInstance().stopProgressDialog1();
                mPullToRefresh.finishRefresh();
                mPullToRefresh.finishLoadMore();
                mPullToRefresh.showView(ViewStatus.CONTENT_STATUS);
                if(result.getStatus_code().equals("200")){
                    if(result.getData() != null && result.getData().size() > 0){
                        if(mPage.equals("1")){
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
                        mCourseList.setLayoutManager(new LinearLayoutManager(NewClassListActivity.this));
                        mCourseList.setAdapter(new CommonAdapter<NewCourseListListResponse.mData>(NewClassListActivity.this,R.layout.item_new_course_list,mDataList) {
                            @Override
                            protected void convert(ViewHolder holder, final NewCourseListListResponse.mData mData, final int position) {
                                holder.setText(R.id.class_name,mData.getGroup_name());
                                TextView mMarkPrice = (TextView) holder.getConvertView().findViewById(R.id.market_price);
                                mMarkPrice.getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG);
                                mMarkPrice.setText("￥"+mData.getMarket_price());
                                holder.setText(R.id.price,mData.getPrice());
                                holder.setText(R.id.learn_num,mData.getNum());
                                if(!mData.getTeacher_name().equals("")){
                                    holder.setText(R.id.teacher_name,mData.getTeacher_name());
                                }else{
                                    holder.setVisible(R.id.teacher_name,false);
                                }
                                if(!mData.getAssistant_name().equals("")){
                                    holder.setText(R.id.assistant_name,mData.getAssistant_name());
                                }else{
                                    holder.setVisible(R.id.assistant_name,false);
                                }

                                holder.setText(R.id.course_type,mData.getBq());
                                ImageView imageView= (ImageView) holder.getConvertView().findViewById(R.id.image);
                                Glide.with(mContext).load(ApiUrl.BASEIMAGE+result.getData().get(position).getBq_img())
                                        .apply(GloableConstant.getInstance().getOptions())
                                        .into(imageView);
                                if(mListData.getType().equals("1")){
                                    holder.setBackgroundRes(R.id.course_type,R.drawable.purple_bottom_shape5);
                                }else if(mListData.getType().equals("2")){
                                    holder.setBackgroundRes(R.id.course_type,R.drawable.orange_bottom_shape5);
                                }else if(mListData.getType().equals("3")){
                                    holder.setBackgroundRes(R.id.course_type,R.drawable.red_bottom_shape5);
                                }else{
                                    holder.setBackgroundRes(R.id.course_type,R.drawable.orange_bottom_shape5);
                                }
                                holder.setOnClickListener(R.id.course_item, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent();
                                        //1班级2课程
                                        intent.setClass(NewClassListActivity.this,CoursesDetialActivity.class);
                                        intent.putExtra("course_id",mData.getType_id());
//                                        intent.putExtra("position",position);
                                        intent.putExtra("type",mData.getType());   //1班级2课程
                                        intent.putExtra("is_buy",mData.getIs_buy());
                                        startActivity(intent);
                                        Log.e("********type",mData.getType()+"/////////"+mData.getType_id());
                                    }
                                });
                            }
                        });
                    }else{
                        if(mPageNum > 1){
                            ToastUtil.showShortToast("没有更多数据了");
                        }else{
                            mPullToRefresh.showView(ViewStatus.EMPTY_STATUS);
                        }
                    }
                }else if(result.getStatus_code().equals("203")){
                    if(mPageNum > 1){
                        ToastUtil.showShortToast("没有更多数据了");
                    }else if (result.getStatus_code().equals("401")) {
                        GloableConstant.getInstance().clearAll();
                        ToastUtil.showShortToast("请重新登录");
                        startActivity(new Intent(NewClassListActivity.this, LoginActivity.class));
                    }else{
                        mPullToRefresh.showView(ViewStatus.EMPTY_STATUS);
                    }
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

            @Override
            public void onFinished() {
                super.onFinished();
                GloableConstant.getInstance().stopProgressDialog1();
                mPullToRefresh.finishRefresh();
                mPullToRefresh.finishLoadMore();
            }
        });
    }

}
