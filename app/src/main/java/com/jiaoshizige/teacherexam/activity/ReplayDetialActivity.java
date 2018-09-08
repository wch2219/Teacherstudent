package com.jiaoshizige.teacherexam.activity;

import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.umeng.analytics.MobclickAgent;
import com.jiaoshizige.teacherexam.R;
import com.jiaoshizige.teacherexam.base.BaseActivity;
import com.jiaoshizige.teacherexam.base.GloableConstant;
import com.jiaoshizige.teacherexam.http.ApiUrl;
import com.jiaoshizige.teacherexam.http.MyCallBack;
import com.jiaoshizige.teacherexam.http.SupportResponse;
import com.jiaoshizige.teacherexam.http.Xutil;
import com.jiaoshizige.teacherexam.model.GetZanReplyResponse;
import com.jiaoshizige.teacherexam.model.ReplayDetialResponse;
import com.jiaoshizige.teacherexam.utils.SDViewUtil;
import com.jiaoshizige.teacherexam.utils.SPKeyValuesUtils;
import com.jiaoshizige.teacherexam.utils.SPUtils;
import com.jiaoshizige.teacherexam.utils.ToastUtil;
import com.jiaoshizige.teacherexam.utils.ToolUtils;
import com.jiaoshizige.teacherexam.widgets.MediaManager;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/12/19.
 * 回复详情
 */

public class ReplayDetialActivity extends BaseActivity{
    @BindView(R.id.landlord_photo)
    ImageView mLandLordPhoto;
    @BindView(R.id.landlord_name)
    TextView mLandLordName;
    @BindView(R.id.landlord_answer_time)
    TextView mLandLordAnswerTime;
    @BindView(R.id.landlord_answer_content)
    TextView mLandLordAnswerContent;
    @BindView(R.id.landlord_answer_pic)
    LinearLayout mLandLordAnswerPic;
    @BindView(R.id.landlord_vioce_rt)
    RelativeLayout mLandLordVioceRt;
    @BindView(R.id.landlord_vioce_length)
    TextView mLandLordVioceLength;
    @BindView(R.id.bo_vioce)
    View mBoVioce;
    @BindView(R.id.teacher_reply_ly)
    LinearLayout mTeacherReplyLy;
    @BindView(R.id.reply_teacher_name)
    TextView mReplayTeacherName;
    @BindView(R.id.reply_teacher_type)
    ImageView mReplayTeacherType;
    @BindView(R.id.teacher_reply_content)
    TextView mTeacherreplayContent;
    @BindView(R.id.teacher_reply_time)
    TextView mTeacherreplayTime;
    @BindView(R.id.replay_landlord)
    RecyclerView mReplayLandLord;
    @BindView(R.id.classmate_reply_ly)
    LinearLayout mClassmateReplayLy;
    @BindView(R.id.replay_content)
    EditText mReplayContent;
    @BindView(R.id.replay)
    Button mReplay;
    private String mAnswerId;
    private View animView;
    private String mPid;

    @Override
    protected int getLayoutId() {
        return R.layout.replay_detial_activity;
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("回复详情");
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("回复详情");
        MobclickAgent.onPause(this);
    }

    @Override
    protected void initView() {
        setSubTitle().setText("");
        setToolbarTitle().setText("回复详情");
        if(getIntent().getExtras().get("answer_id") != null){
            mAnswerId = (String) getIntent().getExtras().get("answer_id");
        }else{
            mAnswerId = "";
        }
        mPid = (String) getIntent().getExtras().get("pid");
        if(TextUtils.isEmpty(mPid)){
            mPid = "";
        }
        requestReplayDetial();
    }
    @OnClick({R.id.replay,R.id.btn_replay})
    public void click(View v){
        switch (v.getId()){
            case R.id.replay:
                if(!mReplayContent.getText().toString().trim().equals("")){
                    requestReply(mAnswerId,mReplayContent.getText().toString().trim(),mPid);
                }else{
                    ToastUtil.showShortToast("请输入回复内容");
                }
                break;
            case R.id.btn_replay:
//                ToastUtil.showShortToast("功能开发中...");
                showPopReplayEdit(v);
                break;
        }
    }

    private PopupWindow mPopWindReplayEdit;

    /**回复输入框
     * @param view
     */
    public void showPopReplayEdit(View view) {
        String nickname = mLandLordName.getText().toString().trim();

        backgroundAlpha(0.6f);
        if(mPopWindReplayEdit != null){
            EditText pop_input = (EditText) mPopWindReplayEdit.getContentView().findViewById(R.id.pop_ed_input_content);
            pop_input.setHint("@"+nickname);
            SDViewUtil.showInputMethod(pop_input,300);
            mPopWindReplayEdit.showAtLocation(view, Gravity.BOTTOM, 0, 0);
            return;
        }
        View menuView = LayoutInflater.from(this).inflate(R.layout.pop_relpay_ed_input, null);
        final EditText pop_ed_input_content = (EditText) menuView.findViewById(R.id.pop_ed_input_content);
        Button pop_btn_send_replay = (Button) menuView.findViewById(R.id.pop_btn_send_replay);
        pop_ed_input_content.setHint("@"+nickname);
        mPopWindReplayEdit = new PopupWindow(menuView,-1,-2);
        mPopWindReplayEdit.setTouchable(true);
        mPopWindReplayEdit.setFocusable(true);
        mPopWindReplayEdit.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        mPopWindReplayEdit.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        mPopWindReplayEdit.setBackgroundDrawable(new BitmapDrawable());
        mPopWindReplayEdit.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                SDViewUtil.hideInputMethod(getToolbar());
                backgroundAlpha(1.0f);
            }
        });
//        SDViewUtil.showInputMethod(pop_input_content,300);
//        SDViewUtil.showInputMethod(pop_ed_input_content,300);
        mPopWindReplayEdit.showAtLocation(view, Gravity.BOTTOM, 0, 0);
        pop_btn_send_replay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String contentStr = pop_ed_input_content.getText().toString().trim();
                if(TextUtils.isEmpty(contentStr)){
                    ToastUtil.showShortToast("请输入回复内容");
                    return;
                }
                requestReply(mAnswerId,contentStr,mPid);
                pop_ed_input_content.setText("");
                mPopWindReplayEdit.dismiss();
            }
        });
    }
    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    protected void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().setAttributes(lp);
    }
    /**
     * 回复详情
     */
    private void requestReplayDetial(){
        Map<String,String> map = new HashMap<>();
//        map.put("user_id","35");
        map.put("user_id", (String) SPUtils.getSpValues(SPKeyValuesUtils.SP_USER_ID, SPUtils.TYPE_STRING));
        map.put("answer_id",mPid);
//        map.put("answer_id","1");
        Log.e("TAG1111",map.toString());
        Xutil.GET(ApiUrl.REPLAYDETIAL,map,new MyCallBack<ReplayDetialResponse>(){
            @Override
            public void onSuccess(ReplayDetialResponse result) {
                super.onSuccess(result);
                if(result.getStatus_code().equals("200")){
                    if(result.getData().getAnswer() != null){
                        Glide.with(ReplayDetialActivity.this).load(result.getData().getAnswer().getAvatar()).apply(GloableConstant.getInstance().getOptions()).into(mLandLordPhoto);
                        mLandLordName.setText(result.getData().getAnswer().getName());
                        mLandLordAnswerTime.setText(result.getData().getAnswer().getCreated_at());
                        if(!result.getData().getAnswer().getContent().equals("")){
                            mLandLordAnswerContent.setText(result.getData().getAnswer().getContent());
                        }else{
                            mLandLordAnswerContent.setVisibility(View.GONE);
                        }
                        if(result.getData().getAnswer().getImages() !=null && result.getData().getAnswer().getImages().size() > 0){
                            //操作添加图片展示
                            mLandLordAnswerPic.removeAllViews();
                            WindowManager wm = (WindowManager) ReplayDetialActivity.this
                                    .getSystemService(ReplayDetialActivity.this.WINDOW_SERVICE);
                            mLandLordAnswerPic.setOrientation(LinearLayout.HORIZONTAL);
                            int width = wm.getDefaultDisplay().getWidth();
                            for(int i = 0;i< result.getData().getAnswer().getImages().size();i++){
                                ImageView imageView = new ImageView(ReplayDetialActivity.this);
                                imageView.setLayoutParams(new ViewGroup.LayoutParams((width / 5), (width / 5)));

                                Glide.with(ReplayDetialActivity.this).load(result.getData().getAnswer().getImages().get(i).getImg()).apply(GloableConstant.getInstance().getDefaultOptionssmall()).into(imageView);
                                imageView.setPadding(10, 0, 10, 0);
                                mLandLordAnswerPic.addView(imageView);
                            }
                        }else{
                            mLandLordAnswerPic.removeAllViews();
                            mLandLordAnswerPic.setVisibility(View.GONE);
                        }
                        if(!result.getData().getAnswer().getAudio_url().equals("")){
                            mLandLordVioceRt.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    //操作下载音频数据 不取数据
                                    if (animView != null) {
                                        animView.setBackground(ContextCompat.getDrawable(ReplayDetialActivity.this, R.mipmap.home_iocn_play_default));
                                        animView = null;
                                    }
                                    animView = (View) findViewById(R.id.bo_vioce);
                                    animView.setBackgroundResource(R.drawable.play_anim);
                                    AnimationDrawable animation = (AnimationDrawable) animView.getBackground();
                                    animation.start();
                                    String s = "http://www.w3school.com.cn/i/horse.mp3";
                                    Uri sd = Uri.parse("http://www.w3school.com.cn/i/horse.mp3");
                                    MediaManager.playSound(ReplayDetialActivity.this, Uri.parse(s), new MediaPlayer.OnCompletionListener() {
                                        public void onCompletion(MediaPlayer mp) {
                                            animView.setBackground(ContextCompat.getDrawable(ReplayDetialActivity.this, R.mipmap.home_iocn_play_default));
                                        }
                                    });
                                }
                            });
                        }else{
                            mLandLordVioceRt.setVisibility(View.GONE);
                        }

                    }else{
                        //应该不会出现吧
                    }
                    //教师回复
                    if(result.getData().getTeacher_reply() != null && result.getData().getTeacher_reply().getName() !=null){
                        mReplayTeacherName.setText(result.getData().getTeacher_reply().getName());
                        if(result.getData().getTeacher_reply().getType().equals("1")){
                            mReplayTeacherType.setBackground(ContextCompat.getDrawable(ReplayDetialActivity.this,R.mipmap.ban));
                        }else{
                            mReplayTeacherType.setBackground(ContextCompat.getDrawable(ReplayDetialActivity.this,R.mipmap.zhu));
                        }
                        mTeacherreplayContent.setText(result.getData().getTeacher_reply().getContent());
                        mTeacherreplayTime.setText(result.getData().getTeacher_reply().getCreated_at());
                    }else{
                        mTeacherReplyLy.setVisibility(View.GONE);
                    }
                    //同学回复
                    if(result.getData().getOther_reply().size() > 0&& result.getData().getOther_reply() != null){
                        mReplayLandLord.setLayoutManager(new LinearLayoutManager(ReplayDetialActivity.this));
                        mReplayLandLord.setAdapter(new CommonAdapter<ReplayDetialResponse.mOtherReplay>(ReplayDetialActivity.this,R.layout.item_classmate_replay,result.getData().getOther_reply()) {
                            @Override
                            protected void convert(final ViewHolder holder, final ReplayDetialResponse.mOtherReplay mOtherReplay, int position) {
                                holder.setText(R.id.other_name,mOtherReplay.getNick_name());
                                holder.setText(R.id.other_answer_time,mOtherReplay.getCreated_at());
                                holder.setText(R.id.replay_landlord_name,mOtherReplay.getReply_sbd());
                                holder.setText(R.id.other_answer_content,mOtherReplay.getContent());
                                ImageView imageView= (ImageView) holder.getConvertView().findViewById(R.id.other_photo);
                                Glide.with(ReplayDetialActivity.this).load(mOtherReplay.getAvatar()).apply(GloableConstant.getInstance().getOptions()).into(imageView);
                                holder.setVisible(R.id.reply, false);
//                                holder.setText(R.id.zan,mOtherReplay.getCount_zan());
                                final TextView mZanText = (TextView) holder.getConvertView().findViewById(R.id.zan);
                                mZanText.setText(mOtherReplay.getCount_zan());
                                if(mOtherReplay.getIs_like().equals("0")){//0未点赞，1已点赞
                                    Drawable drawable = ContextCompat.getDrawable(mContext,R.mipmap.common_like);
                                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                                    mZanText.setCompoundDrawables(drawable,null,null,null);
                                }else{
                                    Drawable drawable = ContextCompat.getDrawable(mContext,R.mipmap.common_like_pre);
                                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                                    mZanText.setCompoundDrawables(drawable,null,null,null);
                                }
                                holder.setOnClickListener(R.id.zan, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        requestGetZan(mOtherReplay.getId(),mZanText);
//                                        notifyDataSetChanged();
                                    }
                                });
                                holder.setOnClickListener(R.id.reply, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        mPid = mOtherReplay.getId();
                                        mClassmateReplayLy.setVisibility(View.VISIBLE);
                                        RelativeLayout.LayoutParams lp = ( RelativeLayout.LayoutParams) mClassmateReplayLy.getLayoutParams();
                                        lp.setMargins(0, 0, 0, ToolUtils.dip2px(ReplayDetialActivity.this,50));
                                        mClassmateReplayLy.setLayoutParams(lp);
                                    }
                                });
                            }
                        });
                    }else{
                        //没有回复
                        Log.e("YYY","55555");
                    }
                }else{
                    //失败
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
                Log.e("TAG",ex.getMessage()+"");
            }
        });
    }
    /**
     *点赞
     */
    private void requestGetZan(String be_like_id, final TextView mZanText){
        Map<String,Object> map = new HashMap<>();
//        map.put("user_id","1");
        map.put("user_id",SPUtils.getSpValues(SPKeyValuesUtils.SP_USER_ID, SPUtils.TYPE_STRING));
        map.put("be_like_id",be_like_id);
        map.put("type","5");//1评论，2笔记, 3问答回复，4作业，5作业回复
        Xutil.Post(ApiUrl.GETZAN,map,new MyCallBack<GetZanReplyResponse>(){
            @Override
            public void onSuccess(GetZanReplyResponse result) {
                super.onSuccess(result);
                if(result.getStatus_code().equals("204")){
                    ToastUtil.showShortToast("点赞成功");
                    Drawable drawable = ContextCompat.getDrawable(ReplayDetialActivity.this,R.mipmap.common_like_pre);
                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                    mZanText.setCompoundDrawables(drawable,null,null,null);
                    mZanText.setText(result.getData().getCount());
//                    mOtherReplay.setCount_zan(String.valueOf(Integer.valueOf(mOtherReplay.getCount_zan())+1));
                }else if(result.getStatus_code().equals("205")){
                    ToastUtil.showShortToast("取消点赞成功");
                    Drawable drawable = ContextCompat.getDrawable(ReplayDetialActivity.this,R.mipmap.common_like);
                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                    mZanText.setCompoundDrawables(drawable,null,null,null);
                    mZanText.setText(result.getData().getCount());
//                    mOtherReplay.setCount_zan(String.valueOf(Integer.valueOf(mOtherReplay.getCount_zan())-1));
                }else{
                    ToastUtil.showShortToast(result.getMessage());
                }
            }
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
            }
        });
    }
    /**
     * 回复作业
     */
    private void requestReply(String answer_id,String content,String pid){
        Map<String,Object> map = new HashMap<>();
//        map.put("user_id","1");
        map.put("user_id",SPUtils.getSpValues(SPKeyValuesUtils.SP_USER_ID, SPUtils.TYPE_STRING));
        map.put("answer_id",answer_id);
        map.put("content",content);
        map.put("pid",pid);
        Xutil.Post(ApiUrl.REPLAYHOMEWORK,map,new MyCallBack<SupportResponse>(){
            @Override
            public void onSuccess(SupportResponse result) {
                super.onSuccess(result);
                if(result.getStatus_code().equals("204")){
                    SDViewUtil.hideInputMethod(getToolbar());
                    ToastUtil.showShortToast("回复成功");
                    mClassmateReplayLy.setVisibility(View.GONE);
//                    RelativeLayout.LayoutParams lp = ( RelativeLayout.LayoutParams) mClassmateReplayLy.getLayoutParams();
//                    lp.setMargins(0, 0, 0, ToolUtils.dip2px(ReplayDetialActivity.this,0));
//                    mClassmateReplayLy.setLayoutParams(lp);
                    requestReplayDetial();
                }else{
                    ToastUtil.showShortToast(result.getMessage());
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
            }
        });
    }
}
