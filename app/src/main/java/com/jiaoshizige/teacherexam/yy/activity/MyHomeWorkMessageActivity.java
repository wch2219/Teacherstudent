package com.jiaoshizige.teacherexam.yy.activity;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
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
import com.jiaoshizige.teacherexam.http.SupportResponse;
import com.jiaoshizige.teacherexam.http.Xutil;
import com.jiaoshizige.teacherexam.model.MyHomeMessageResponse;
import com.jiaoshizige.teacherexam.utils.PullToRefreshLayoutRewrite;
import com.jiaoshizige.teacherexam.utils.SPKeyValuesUtils;
import com.jiaoshizige.teacherexam.utils.SPUtils;
import com.jiaoshizige.teacherexam.utils.ToastUtil;
import com.jiaoshizige.teacherexam.widgets.MediaManager;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/1/17.
 * 我的作业
 */

public class MyHomeWorkMessageActivity extends BaseActivity {
    @BindView(R.id.my_homework_list)
    RecyclerView mMyHomeWorkList;
    @BindView(R.id.pulltorefresh)
    PullToRefreshLayoutRewrite mPullToRefresh;
    View animView;
    private String mCourseId;
    private String mPageSize = "15";
    private int mPage = 1;
    private CommRecyclerViewAdapter<MyHomeMessageResponse.mData> mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.my_homework_layout;
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("我的作业");
        MobclickAgent.onResume(this);
        mPage = 1;
        requestMyHomeWork();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("我的作业");
        MobclickAgent.onPause(this);
    }

    @Override
    protected void initView() {
//        if(getIntent().getExtras().get("course_id") !=null){
//            mCourseId = (String) getIntent().getExtras().get("course_id");
//        }else{
//            mCourseId = "";
//        }
        setSubTitle().setText("");
        setToolbarTitle().setText("我的作业");
        requestMyHomeWork();
        mPullToRefresh.setRefreshListener(new BaseRefreshListener() {
            @Override
            public void refresh() {
                mPage = 1;
                requestMyHomeWork();
            }

            @Override
            public void loadMore() {
                mPage++;
                requestMyHomeWork();
            }
        });
        mPullToRefresh.setOnErrorListener(new PullToRefreshLayoutRewrite.onClickCallBackListener() {
            @Override
            public void onCallBackListener(View v) {
                mPage = 1;
                requestMyHomeWork();
            }
        });
    }

    /**
     * 我的作业
     */
    private void requestMyHomeWork() {
        Map<String, Object> map = new HashMap<>();
//        map.put("user_id","1");
        map.put("user_id", (String) SPUtils.getSpValues(SPKeyValuesUtils.SP_USER_ID, SPUtils.TYPE_STRING));
        map.put("type", 5);
        map.put("page", mPage);
        map.put("page_size", mPageSize);
        Log.e("TAG", map.toString());
        GloableConstant.getInstance().startProgressDialog(this);
        Xutil.Post(ApiUrl.PUSH, map, new MyCallBack<MyHomeMessageResponse>() {
            @Override
            public void onSuccess(MyHomeMessageResponse result) {
                super.onSuccess(result);
                GloableConstant.getInstance().stopProgressDialog1();
                mPullToRefresh.finishRefresh();
                mPullToRefresh.finishLoadMore();
                mPullToRefresh.showView(ViewStatus.CONTENT_STATUS);
                if (result.getStatus_code().equals("200")) {
                    if (mPage == 1) {
                        getAdapter().updateData(result.getData());
                    } else {
                        getAdapter().appendData(result.getData());
                    }
                    if (result.getData() != null && result.getData().size() > 0) {
//                        if(mPageNum.equals("1")){
//                            mDataList.clear();
//                            mDataList = result.getData();
//                        }else{
//                            if (null == mDataList){
//                                mDataList = result.getData();
//                            }
//                            else{
//                                mDataList.addAll(result.getData());
//                            }
//                        }
//                        mMyHomeWorkList.setLayoutManager(new LinearLayoutManager(MyHomeWorkMessageActivity.this));
//                        mMyHomeWorkList.setAdapter(new CommonAdapter<MyHomeMessageResponse.mData>(MyHomeWorkMessageActivity.this,R.layout.item_my_homework,mDataList) {
//                            @Override
//                            protected void convert(ViewHolder holder, final MyHomeMessageResponse.mData mData, int position) {
//                                ImageView mMinePhoto = (ImageView) holder.getConvertView().findViewById(R.id.mine_photo);
//                                Glide.with(MyHomeWorkMessageActivity.this).load(mData.getUser_img()).apply(GloableConstant.getInstance().getOptions()).into(mMinePhoto);
//                                holder.setText(R.id.mine_nickname,mData.getName());
//                                holder.setOnClickListener(R.id.work, new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View v) {
//                                        Intent intent=new Intent();
//                                        intent.setClass(MyHomeWorkMessageActivity.this,YYHomeWorkDetailActivity.class);
//                                        intent.putExtra("work_id",mData.getW_id());
//                                        intent.putExtra("is_done","1");
//                                        intent.putExtra("message_id",mData.getId());
//                                        Log.e("*************work_id",mData.getW_id());
//                                        startActivity(intent);
//
//                                    }
//                                });
//
//                                /*文字*/
//                                if(mData.getContent().equals("")){
//                                    holder.setVisible(R.id.answer_content,false);
//                                }else{
//                                    holder.setText(R.id.answer_content,mData.getU_content());
//                                }
//                                /*语音*/
//                                if(mData.getU_audio_url().equals("")){
//                                    holder.setVisible(R.id.vioce_rt,false);
//                                }else{
//                                    if(!mData.getU_duration().equals(""))
//                                    holder.setText(R.id.vioce_length,mData.getU_duration());
//
//                                    holder.setOnClickListener(R.id.vioce_rt, new View.OnClickListener() {
//                                        @Override
//                                        public void onClick(View v) {
//                                            //操作下载音频数据 不取数据
//                                            if (animView != null) {
//                                                animView.setBackground(ContextCompat.getDrawable(MyHomeWorkMessageActivity.this, R.mipmap.home_iocn_play_default));
//                                                animView = null;
//                                            }
//                                            animView = (View) findViewById(R.id.bo_vioce);
//                                            animView.setBackgroundResource(R.drawable.play_anim);
//                                            AnimationDrawable animation = (AnimationDrawable) animView.getBackground();
//                                            animation.start();
////                                            String s = "http://www.w3school.com.cn/i/horse.mp3";
//                                            MediaManager.playSound(MyHomeWorkMessageActivity.this, Uri.parse(mData.getU_audio_url()), new MediaPlayer.OnCompletionListener() {
//                                                public void onCompletion(MediaPlayer mp) {
//                                                    animView.setBackground(ContextCompat.getDrawable(MyHomeWorkMessageActivity.this, R.mipmap.home_iocn_play_default));
//                                                }
//                                            });
//                                        }
//                                    });
//                                }
//                                /*图片*/
//                                LinearLayout mMineAddPicLy = (LinearLayout) holder.getConvertView().findViewById(R.id.add_pic);
//                                if(mData.getU_answer_img() != null && mData.getU_answer_img().size() > 0){
//                                    mMineAddPicLy.removeAllViews();
//                                    for(int i = 0;i< mData.getU_answer_img().size();i++){
//                                        ImageView imageView = new ImageView(MyHomeWorkMessageActivity.this);
//                                        Glide.with(MyHomeWorkMessageActivity.this).load(mData.getU_answer_img().get(i).getImg()).apply(GloableConstant.getInstance().getDefaultOptionssmall()).into(imageView);
//                                        imageView.setPadding(10, 0, 10, 0);
//                                        mMineAddPicLy.addView(imageView);
//                                    }
//                                }else{
//                                    mMineAddPicLy.removeAllViews();
//                                    mMineAddPicLy.setVisibility(View.GONE);
//                                }
//                                /*删除*/
//                                holder.setOnClickListener(R.id.del_answer, new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View v) {
//                                        requestDeleHomeWoek(mData.getId());
//                                    }
//                                });
//                                holder.setText(R.id.reply_teacher_name,mData.getReply().getName());
//                                ImageView mTeacherReplyType = (ImageView) holder.getConvertView().findViewById(R.id.reply_teacher_type);
//                                if(mData.getReply() !=null && mData.getReply().getName() !=null) {
//                                    if (mData.getReply().getType().equals("2")) {
//                                        mTeacherReplyType.setBackground(ContextCompat.getDrawable(MyHomeWorkMessageActivity.this, R.mipmap.zhu));
//                                    } else {
//                                        mTeacherReplyType.setBackground(ContextCompat.getDrawable(MyHomeWorkMessageActivity.this, R.mipmap.ban));
//                                    }
//                                    holder.setText(R.id.teacher_reply_content, mData.getReply().getContent());
//                                    holder.setText(R.id.creat_time, mData.getReply().getCreated_at());
//                                }else{
//                                    holder.setVisible(R.id.teacher_reply_ly,false);
//                                }
//                            }
//                        });
                    } else {
                        if (mPage > 1) {
                            mPage--;
                            ToastUtil.showShortToast("没有更多数据了");
                        } else {
                            ToastUtil.showShortToast("没有更多数据了");
                            mPullToRefresh.showView(ViewStatus.EMPTY_STATUS);
                        }

                    }
                } else if (result.getStatus_code().equals("203")) {
                    if (mPage > 1) {
                        mPage--;
                        ToastUtil.showShortToast("没有更多数据了");
                    } else {
                        ToastUtil.showShortToast("没有更多数据了");
                        mPullToRefresh.showView(ViewStatus.EMPTY_STATUS);
                    }
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
                Log.e("TAG", ex.getMessage() + "");
                GloableConstant.getInstance().stopProgressDialog1();
                mPullToRefresh.finishRefresh();
                mPullToRefresh.finishLoadMore();
                mPullToRefresh.showView(ViewStatus.EMPTY_STATUS);
            }
        });
    }

    /**
     * 删除作业
     *
     * @param
     */
    private void requestDeleHomeWoek(final MyHomeMessageResponse.mData data) {
        Map<String, Object> map = new HashMap<>();
//        map.put("user_id","1");
        map.put("user_id", (String) SPUtils.getSpValues(SPKeyValuesUtils.SP_USER_ID, SPUtils.TYPE_STRING));
        map.put("work_id", data.getId());
        Xutil.Post(ApiUrl.DELETHOMEWORK, map, new MyCallBack<SupportResponse>() {
            @Override
            public void onSuccess(SupportResponse result) {
                super.onSuccess(result);
                if (result.getStatus_code().equals("204")) {
                    ToastUtil.showShortToast("作业删除成功");
                    getAdapter().removeData(data);
//                    requestMyHomeWork();
                } else {
                    ToastUtil.showShortToast(result.getMessage());
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
            }
        });
    }

    private CommRecyclerViewAdapter<MyHomeMessageResponse.mData> getAdapter() {
        if (null == mAdapter) {
            mAdapter = new CommRecyclerViewAdapter<MyHomeMessageResponse.mData>(this, R.layout.item_my_homework, null) {
                @Override
                protected void convert(ViewHolderZhy holder, final MyHomeMessageResponse.mData mData, final int position) {
                    ImageView mMinePhoto = (ImageView) holder.getConvertView().findViewById(R.id.mine_photo);
                    Glide.with(MyHomeWorkMessageActivity.this).load(mData.getUser_img()).apply(GloableConstant.getInstance().getOptions()).into(mMinePhoto);
                    holder.setText(R.id.mine_nickname, mData.getName());
                    LinearLayout mWork= (LinearLayout) holder.getConvertView().findViewById(R.id.work);
                    TextView mName= (TextView) holder.getConvertView().findViewById(R.id.mine_nickname);
                    if (mData.getIs_read().equals("1")){
                        mWork.setAlpha((float) 0.5);
                        mName.setTextColor(ContextCompat.getColor(MyHomeWorkMessageActivity.this,R.color.text_color6));
                    }
                    else {
                        mWork.setAlpha((float) 1.0);
                        mName.setTextColor(ContextCompat.getColor(MyHomeWorkMessageActivity.this,R.color.text_color3));

                    }
                    holder.setOnClickListener(R.id.work, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent();
                            intent.setClass(MyHomeWorkMessageActivity.this, YYHomeWorkDetailActivity.class);
                            intent.putExtra("work_id", mData.getW_id());
                            intent.putExtra("is_done", "1");
                            intent.putExtra("message_id", mData.getId());
                            Log.e("*************work_id", mData.getW_id());
                            startActivity(intent);

                        }
                    });

                    /*文字*/
                    if (mData.getContent().equals("")) {
                        holder.setVisible(R.id.answer_content, false);
                    } else {
                        holder.setText(R.id.answer_content, mData.getU_content());
                    }
                    /*语音*/
                    if (mData.getU_audio_url().equals("")) {
                        holder.setVisible(R.id.vioce_rt, false);
                    } else {
                        if (!mData.getU_duration().equals(""))
                            holder.setText(R.id.vioce_length, mData.getU_duration());

                        holder.setOnClickListener(R.id.vioce_rt, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //操作下载音频数据 不取数据
                                if (animView != null) {
                                    animView.setBackground(ContextCompat.getDrawable(MyHomeWorkMessageActivity.this, R.mipmap.home_iocn_play_default));
                                    animView = null;
                                }
                                animView = (View) findViewById(R.id.bo_vioce);
                                animView.setBackgroundResource(R.drawable.play_anim);
                                AnimationDrawable animation = (AnimationDrawable) animView.getBackground();
                                animation.start();
//                                            String s = "http://www.w3school.com.cn/i/horse.mp3";
                                MediaManager.playSound(MyHomeWorkMessageActivity.this, Uri.parse(mData.getU_audio_url()), new MediaPlayer.OnCompletionListener() {
                                    public void onCompletion(MediaPlayer mp) {
                                        animView.setBackground(ContextCompat.getDrawable(MyHomeWorkMessageActivity.this, R.mipmap.home_iocn_play_default));
                                    }
                                });
                            }
                        });
                    }
                    /*图片*/
                    LinearLayout mMineAddPicLy = (LinearLayout) holder.getConvertView().findViewById(R.id.add_pic);
                    if (mData.getU_answer_img() != null && mData.getU_answer_img().size() > 0) {
                        mMineAddPicLy.removeAllViews();
                        for (int i = 0; i < mData.getU_answer_img().size(); i++) {
                            ImageView imageView = new ImageView(MyHomeWorkMessageActivity.this);
                            Glide.with(MyHomeWorkMessageActivity.this).load(mData.getU_answer_img().get(i).getImg()).apply(GloableConstant.getInstance().getDefaultOptionssmall()).into(imageView);
                            imageView.setPadding(10, 0, 10, 0);
                            mMineAddPicLy.addView(imageView);
                        }
                    } else {
                        mMineAddPicLy.removeAllViews();
                        mMineAddPicLy.setVisibility(View.GONE);
                    }
                    /*删除*/
                    holder.setOnClickListener(R.id.del_answer, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            requestDeleHomeWoek(mData);
                        }
                    });
                    holder.setText(R.id.reply_teacher_name, mData.getReply().getName());
                    ImageView mTeacherReplyType = (ImageView) holder.getConvertView().findViewById(R.id.reply_teacher_type);
                    if (mData.getReply() != null && mData.getReply().getName() != null) {
                        if (mData.getReply().getType().equals("2")) {
                            mTeacherReplyType.setBackground(ContextCompat.getDrawable(MyHomeWorkMessageActivity.this, R.mipmap.zhu));
                        } else {
                            mTeacherReplyType.setBackground(ContextCompat.getDrawable(MyHomeWorkMessageActivity.this, R.mipmap.ban));
                        }
                        holder.setText(R.id.teacher_reply_content, mData.getReply().getContent());
                        holder.setText(R.id.creat_time, mData.getReply().getCreated_at());
                    } else {
                        holder.setVisible(R.id.teacher_reply_ly, false);
                    }
                }
            };
            mMyHomeWorkList.setLayoutManager(new LinearLayoutManager(this));
            mMyHomeWorkList.setAdapter(mAdapter);
        }
        return mAdapter;
    }
}
