package com.jiaoshizige.teacherexam.activity;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jiaoshizige.teacherexam.R;
import com.jiaoshizige.teacherexam.base.BaseActivity;
import com.jiaoshizige.teacherexam.base.CommRecyclerViewAdapter;
import com.jiaoshizige.teacherexam.base.GloableConstant;
import com.jiaoshizige.teacherexam.base.ViewHolderZhy;
import com.jiaoshizige.teacherexam.http.ApiUrl;
import com.jiaoshizige.teacherexam.http.MyCallBack;
import com.jiaoshizige.teacherexam.http.Xutil;
import com.jiaoshizige.teacherexam.model.MineStudyResponse;
import com.jiaoshizige.teacherexam.mycourse.activity.MyNewCoursesActivity;
import com.jiaoshizige.teacherexam.utils.PullToRefreshLayoutRewrite;
import com.jiaoshizige.teacherexam.utils.SPKeyValuesUtils;
import com.jiaoshizige.teacherexam.utils.SPUtils;
import com.jiaoshizige.teacherexam.utils.ToastUtil;
import com.jiaoshizige.teacherexam.widgets.ProgressBarView;
import com.jwenfeng.library.pulltorefresh.BaseRefreshListener;
import com.jwenfeng.library.pulltorefresh.ViewStatus;
import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/6/13 0013.
 */

public class MineCourseActivityOne extends BaseActivity {
    @BindView(R.id.course_list)
    RecyclerView mCourseList;
    @BindView(R.id.layout)
    PullToRefreshLayoutRewrite mLayout;
    private String mPageSize = "10";
    private int mPage = 1;
    private CommRecyclerViewAdapter<MineStudyResponse.mData> mAdapter;
    private String is_buy;

    @Override
    protected int getLayoutId() {
        return R.layout.mine_course_layout_one;
    }
    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("我的课程列表");
        MobclickAgent.onResume(this);
        mPage = 1;
        requestMineClass();
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("我的课程列表");
        MobclickAgent.onPause(this);
    }
    @Override
    protected void initView() {
        setSubTitle().setText("");
        setToolbarTitle().setText("我的课程");
        mLayout.setRefreshListener(new BaseRefreshListener() {
            @Override
            public void refresh() {
                mPage = 1;
                requestMineClass();
            }

            @Override
            public void loadMore() {
                mPage++;
                requestMineClass();
            }
        });
        requestMineClass();
    }
    @OnClick(R.id.add_class_ly)
    public void onClick(View view){
        if(view.getId() == R.id.add_class_ly){
            GloableConstant.getInstance().setFlag("1");
            sendBroadcast();
            finish();
        }
    }


    private void sendBroadcast() {
        Intent intent = new Intent();
        intent.setAction("classSchool");
        sendBroadcast(intent);
    }
    /**
     * 我的班级列表
     */
    private void requestMineClass(){
        Map<String,Object> map = new HashMap<>();
        map.put("user_id", SPUtils.getSpValues(SPKeyValuesUtils.SP_USER_ID, SPUtils.TYPE_STRING));
//        map.put("user_id",67);
        map.put("page_size",mPageSize);
        map.put("page",mPage);
        GloableConstant.getInstance().startProgressDialog(this);
        Log.e("********requestM**map",map.toString());
        Xutil.Post(ApiUrl.ALLMYGROUP, map,new MyCallBack<MineStudyResponse>(){
            @Override
            public void onSuccess(MineStudyResponse result) {
                super.onSuccess(result);
                mLayout.finishLoadMore();
                mLayout.finishRefresh();
                GloableConstant.getInstance().stopProgressDialog1();
                Log.e("********mdata",result.getStatus_code()+"/////");
                if(result.getStatus_code().equals("200")){
                    if(mPage == 1){
                        getAdapter().updateData(result.getData());
                    } else {
                        getAdapter().appendData(result.getData());
                    }
//                    Log.e("********11111mdata",mData.size()+"/////");
                    if(result.getData() != null && result.getData().size() > 0){
//                        if (mPageNum.equals("1")){
//                            mData.clear();
//                            mData = result.getData();
//                        }else {
//
//                            if (null == mData){
//                                mData = result.getData();
//                            } else{
//                                mData.addAll(result.getData());
//                            }
//                            Log.e("********madata",mData.size()+"//////");
//                        }
//
//                        Log.e("********mdata",mData.size()+"/////");
//                       mCourseList.setAdapter(new CommonAdapter<MineStudyResponse.mData>(MineCourseActivityOne.this, R.layout.item_class_study, mData) {
//                           @Override
//                           protected void convert(ViewHolder holder, final MineStudyResponse.mData mData, int position) {
//                               holder.setText(R.id.class_name, mData.getGroup_name());
//                               holder.setText(R.id.section_name,mData.getSection_name());
//                               ImageView mTypeImg = (ImageView) holder.getConvertView().findViewById(R.id.class_type);
//                               //1视频2试题3图文
//                               if(mData.getType().equals("1")){
//                                   mTypeImg.setBackground(ContextCompat.getDrawable(MineCourseActivityOne.this,R.mipmap.stop));
//                               }else if(mData.getType().equals("2")){
//                                   mTypeImg.setBackground(ContextCompat.getDrawable(MineCourseActivityOne.this,R.mipmap.stop));
//                               }else if(mData.getType().equals("3")){
//                                   mTypeImg.setBackground(ContextCompat.getDrawable(MineCourseActivityOne.this,R.mipmap.stop));
//                               }
//                               ProgressBarView mViewBar = (ProgressBarView) holder.getConvertView().findViewById(R.id.view_bar);
//                               Log.e("*******getLearn_percent",mData.getLearn_percent()+"////////"+position+"********"+mData.getCourse_id());
//                               mViewBar.setProgress((int) (Double.valueOf(mData.getLearn_percent()) * 3.6));
////                                mViewBar.setProgress(Integer.valueOf(mData.getLearn_percent()) );
//                               mViewBar.setReachedBarColor(ContextCompat.getColor(MineCourseActivityOne.this, R.color.buy_color));
//                               mViewBar.SetTextColor(ContextCompat.getColor(MineCourseActivityOne.this, R.color.buy_color));
////                                mViewBar.setText(String.valueOf((int) (Integer.valueOf(mData.getLearn_percent()) / 3.6)));
//                               mViewBar.setText(String.valueOf((int) (Integer.valueOf(mData.getLearn_percent()))));
////                                final String progress = String.valueOf((int) (Integer.valueOf(mData.getLearn_percent()) / 3.6));
//                               final String progress = String.valueOf((int) (Integer.valueOf(mData.getLearn_percent())));
//                               if(Integer.valueOf(mData.getLearn_percent())> 0){
//                                   holder.setTextColor(R.id.section_name,R.color.purple4);
//                               }
//                               holder.setOnClickListener(R.id.item_mine_class, new View.OnClickListener() {
//                                   @Override
//                                   public void onClick(View v) {
//                                       Intent intent = new Intent();
//                                       intent.putExtra("id", mData.getId());
//                                       intent.putExtra("progress", progress);
//                                       Log.e("********progress",progress);
//                                       intent.putExtra("name", mData.getGroup_name());
////                                        intent.putExtra("type", mData.getType());
//                                       intent.putExtra("type", mData.getCourse_type());//以前传的是1 2 3 4 视频 图文等 现在type变成1班级2课堂
//                                       intent.setClass(MineCourseActivityOne.this, NewMyClassDetialActivity.class);
//                                       startActivity(intent);
//                                   }
//                               });
//                           }
//                       });
//                        mCourseList.setLayoutManager(new LinearLayoutManager(MineCourseActivityOne.this));
//                        mCourseList.setAdapter(new com.zhy.adapter.recyclerview.CommonAdapter<MineStudyResponse.mData>
//                                (MineCourseActivityOne.this, R.layout.item_class_study, mData) {
//                            @Override
//                            protected void convert(com.zhy.adapter.recyclerview.base.ViewHolder holder, final MineStudyResponse.mData mData, int position) {
//                                holder.setText(R.id.class_name, mData.getGroup_name());
//                                ImageView mTypeImg = (ImageView) holder.getConvertView().findViewById(R.id.class_type);
//                                TextView mTextView= (TextView) holder.getConvertView().findViewById(R.id.section_name);
//                                TextView mDay1= (TextView) holder.getConvertView().findViewById(R.id.day1);
//                                TextView mDay2= (TextView) holder.getConvertView().findViewById(R.id.day2);
//                                ProgressBarView mViewBar = (ProgressBarView) holder.getConvertView().findViewById(R.id.view_bar);
//                                Log.e("*******getLearn_percent",mData.getLearn_percent()+"////////"+position+"********"+mData.getCourse_id());
//                                mViewBar.setProgress((int) (Double.valueOf(mData.getLearn_percent()) * 3.6));
////                                mViewBar.setProgress(Integer.valueOf(mData.getLearn_percent()) );
//                                mViewBar.setReachedBarColor(ContextCompat.getColor(MineCourseActivityOne.this, R.color.buy_color));
//                                mViewBar.SetTextColor(ContextCompat.getColor(MineCourseActivityOne.this, R.color.buy_color));
////                                mViewBar.setText(String.valueOf((int) (Integer.valueOf(mData.getLearn_percent()) / 3.6)));
//                                mViewBar.setText(String.valueOf((int) (Integer.valueOf(mData.getLearn_percent()))));
////                                final String progress = String.valueOf((int) (Integer.valueOf(mData.getLearn_percent()) / 3.6));
//                                final String progress = String.valueOf((int) (Integer.valueOf(mData.getLearn_percent())));
//                                if(Integer.valueOf(mData.getLearn_percent())> 0){
//                                    holder.setTextColor(R.id.section_name,R.color.purple4);
//                                }
//                                if (mData.getIs_buy().equals("1")){
//                                    mDay1.setVisibility(View.VISIBLE);
//                                    mDay2.setVisibility(View.VISIBLE);
//                                    holder.setText(R.id.section_days,mData.getEnd_time());
//                                    if ( mData.getSection_name().equals("")){
//                                        holder.setText(R.id.section_name, "未开始学习");
//                                        mTextView.setTextColor(ContextCompat.getColor(MineCourseActivityOne.this,R.color.text_color9));
//                                        mTypeImg.setBackground(ContextCompat.getDrawable(MineCourseActivityOne.this, R.mipmap.picture));
//                                    }else {
//                                        //1视频2试题3图文
//                                        holder.setText(R.id.section_name,mData.getSection_name());
//                                        mTextView.setTextColor(ContextCompat.getColor(MineCourseActivityOne.this,R.color.purple4));
//                                        if(mData.getType().equals("1")){
//                                            mTypeImg.setBackground(ContextCompat.getDrawable(MineCourseActivityOne.this,R.mipmap.live_pre));
//                                        }else if(mData.getType().equals("2")){
//                                            mTypeImg.setBackground(ContextCompat.getDrawable(MineCourseActivityOne.this,R.mipmap.exercise_pre));
//                                        }else if(mData.getType().equals("3")){
//                                            mTypeImg.setBackground(ContextCompat.getDrawable(MineCourseActivityOne.this,R.mipmap.image_text_pre));
//                                        }
//                                    }
//
//
//                                    holder.setOnClickListener(R.id.item_mine_class, new View.OnClickListener() {
//                                        @Override
//                                        public void onClick(View v) {
//                                            Intent intent = new Intent();
//                                            intent.putExtra("id", mData.getId());
//                                            intent.putExtra("progress", progress);
//                                            Log.e("********progress",progress);
//                                            intent.putExtra("name", mData.getGroup_name());
////                                        intent.putExtra("type", mData.getType());
//                                            intent.putExtra("type", mData.getCourse_type());//以前传的是1 2 3 4 视频 图文等 现在type变成1班级2课堂
//                                            intent.setClass(MineCourseActivityOne.this, NewMyClassDetialActivity.class);
//                                            startActivity(intent);
//                                        }
//                                    });
//
//                                }else {
//                                    mDay1.setVisibility(View.GONE);
//                                    mDay2.setVisibility(View.GONE);
//                                    holder.setText(R.id.section_days,"课程已到期，请前往续费");
//                                    mTypeImg.setBackground(ContextCompat.getDrawable(MineCourseActivityOne.this, R.mipmap.picture));
//                                    holder.setOnClickListener(R.id.item_mine_class, new View.OnClickListener() {
//                                        @Override
//                                        public void onClick(View v) {
//                                            Intent intent = new Intent();
//                                            intent.putExtra("course_id", mData.getId());
//                                            intent.putExtra("type", mData.getCourse_type());//以前传的是1 2 3 4 视频 图文等 现在type变成1班级2课堂
//                                            intent.setClass(MineCourseActivityOne.this, CoursesDetialActivity.class);
//                                            startActivity(intent);
//                                        }
//                                    });
//                                }
//
//
//                            }
//                        });
                    } else {
                        if(mPage > 1){
                            mPage--;
                            ToastUtil.showShortToast("没有更多数据了");
                        }else{
                            mLayout.showView(ViewStatus.EMPTY_STATUS);
                        }
                    }
                }else if(result.getStatus_code().equals("203")){
                    if(mPage > 1){
                        mPage--;
                        ToastUtil.showShortToast("没有更多数据了");
                    }else{
                        mLayout.showView(ViewStatus.EMPTY_STATUS);
                    }
                }
            }
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
                mLayout.finishLoadMore();
                mLayout.finishRefresh();

                GloableConstant.getInstance().stopProgressDialog1();
                Log.e("********ex",ex.getMessage());
            }

        });
    }

    private CommRecyclerViewAdapter<MineStudyResponse.mData> getAdapter() {
        if (null == mAdapter) {
            mAdapter = new CommRecyclerViewAdapter<MineStudyResponse.mData>(this, R.layout.item_class_study, null) {
                @Override
                protected void convert(ViewHolderZhy holder, final MineStudyResponse.mData mData, final int position) {
                    holder.setText(R.id.class_name, mData.getGroup_name());
                    ImageView mTypeImg = (ImageView) holder.getConvertView().findViewById(R.id.class_type);
                    TextView mTextView= (TextView) holder.getConvertView().findViewById(R.id.section_name);
                    TextView mDay1= (TextView) holder.getConvertView().findViewById(R.id.day1);
                    TextView mDay2= (TextView) holder.getConvertView().findViewById(R.id.day2);
                    ProgressBarView mViewBar = (ProgressBarView) holder.getConvertView().findViewById(R.id.view_bar);
                    Log.e("*******getLearn_percent",mData.getLearn_percent()+"////////"+position+"********"+mData.getCourse_id());
                    mViewBar.setProgress((int) (Double.valueOf(mData.getLearn_percent()) * 3.6));
//                                mViewBar.setProgress(Integer.valueOf(mData.getLearn_percent()) );
                    mViewBar.setReachedBarColor(ContextCompat.getColor(MineCourseActivityOne.this, R.color.buy_color));
                    mViewBar.SetTextColor(ContextCompat.getColor(MineCourseActivityOne.this, R.color.buy_color));
//                                mViewBar.setText(String.valueOf((int) (Integer.valueOf(mData.getLearn_percent()) / 3.6)));
                    mViewBar.setText(String.valueOf((int) (Integer.valueOf(mData.getLearn_percent()))));
//                                final String progress = String.valueOf((int) (Integer.valueOf(mData.getLearn_percent()) / 3.6));
                    final String progress = String.valueOf((int) (Integer.valueOf(mData.getLearn_percent())));
                    if(Integer.valueOf(mData.getLearn_percent())> 0){
                        holder.setTextColor(R.id.section_name,R.color.purple4);
                    }
                    if (mData.getIs_buy().equals("1")){
                        mDay1.setVisibility(View.VISIBLE);
                        mDay2.setVisibility(View.VISIBLE);
                        holder.setText(R.id.section_days,mData.getEnd_time());
                        if ( mData.getSection_name().equals("")){
                            holder.setText(R.id.section_name, "未开始学习");
                            mTextView.setTextColor(ContextCompat.getColor(MineCourseActivityOne.this,R.color.text_color9));
                            mTypeImg.setBackground(ContextCompat.getDrawable(MineCourseActivityOne.this, R.mipmap.picture));
                        }else {
                            //1视频2试题3图文
                            holder.setText(R.id.section_name,mData.getSection_name());
                            mTextView.setTextColor(ContextCompat.getColor(MineCourseActivityOne.this,R.color.purple4));
                            if(mData.getType().equals("1")){
                                mTypeImg.setBackground(ContextCompat.getDrawable(MineCourseActivityOne.this,R.mipmap.live_pre));
                            }else if(mData.getType().equals("2")){
                                mTypeImg.setBackground(ContextCompat.getDrawable(MineCourseActivityOne.this,R.mipmap.exercise_pre));
                            }else if(mData.getType().equals("3")){
                                mTypeImg.setBackground(ContextCompat.getDrawable(MineCourseActivityOne.this,R.mipmap.image_text_pre));
                            }
                        }


                        holder.setOnClickListener(R.id.item_mine_class, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent();
                                intent.putExtra("id", mData.getId());
                                intent.putExtra("progress", progress);
                                Log.e("********progress",progress);
                                intent.putExtra("name", mData.getGroup_name());
                                intent.putExtra("course_id", mData.getId());
                                intent.putExtra("is_buy",mData.getIs_buy());
                                intent.putExtra("covermap",mData.getImages());
//                                        intent.putExtra("type", mData.getType());
                                intent.putExtra("type", mData.getCourse_type());//以前传的是1 2 3 4 视频 图文等 现在type变成1班级2课堂
                                //TOdo
//                                intent.setClass(MineCourseActivityOne.this, NewMyClassDetialActivity.class);
                                intent.setClass(MineCourseActivityOne.this, MyNewCoursesActivity.class);
                                startActivity(intent);
                            }
                        });

                    }else {
                        mTextView.setTextColor(ContextCompat.getColor(MineCourseActivityOne.this,R.color.text_color9));
                        mDay1.setVisibility(View.GONE);
                        mDay2.setVisibility(View.GONE);
                        holder.setText(R.id.section_days,"课程已到期，请前往续费");
                        mTypeImg.setBackground(ContextCompat.getDrawable(MineCourseActivityOne.this, R.mipmap.picture));
                        holder.setOnClickListener(R.id.item_mine_class, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent();
                                intent.putExtra("course_id", mData.getId());
                                intent.putExtra("covermap",mData.getImages());
                                intent.putExtra("type", mData.getCourse_type());//以前传的是1 2 3 4 视频 图文等 现在type变成1班级2课堂
                                intent.setClass(MineCourseActivityOne.this, CoursesDetialActivity.class);
                                startActivity(intent);
                            }
                        });
                    }
                }
            };
            mCourseList.setLayoutManager(new LinearLayoutManager(this));
            mCourseList.setAdapter(mAdapter);
        }
        return mAdapter;
    }
}
