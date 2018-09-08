package com.jiaoshizige.teacherexam.activity;

import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.jiaoshizige.teacherexam.R;
import com.jiaoshizige.teacherexam.base.BaseActivity;
import com.jiaoshizige.teacherexam.base.GloableConstant;
import com.jiaoshizige.teacherexam.http.ApiUrl;
import com.jiaoshizige.teacherexam.http.MyCallBack;
import com.jiaoshizige.teacherexam.http.SupportResponse;
import com.jiaoshizige.teacherexam.http.Xutil;
import com.jiaoshizige.teacherexam.model.NoteDetailResponse;
import com.jiaoshizige.teacherexam.utils.SPKeyValuesUtils;
import com.jiaoshizige.teacherexam.utils.SPUtils;
import com.jiaoshizige.teacherexam.utils.ToastUtil;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/a1/17.
 * 笔记详情
 */

public class CoursesNoteDetialActivity extends BaseActivity{
    @BindView(R.id.note_type)
    TextView mNoteType;
    @BindView(R.id.note_time)
    TextView mNoteTime;
    @BindView(R.id.note_content)
    TextView mNoteContent;
    @BindView(R.id.give_zan)
    ImageView mGiveZza;
    private String mNoteId;
    @Override
    protected int getLayoutId() {
        return R.layout.course_note_detial_activity;
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("笔记详情");
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("笔记详情");
        MobclickAgent.onPause(this);
    }

    @Override
    protected void initView() {
        setSubTitle().setText("");
        setToolbarTitle().setText("笔记详情");
        if(getIntent().getExtras().get("note_id") != null){
            mNoteId = (String) getIntent().getExtras().get("note_id");
        }else{
            mNoteId = "";
        }
        requestNoteDetail();
    }
    @OnClick(R.id.give_zan)
    public void onClick(){
        requestZan();
    }
    private void requestNoteDetail(){
        Map<String,String> map = new HashMap<>();
        map.put("note_id",mNoteId);
        map.put("user_id", (String) SPUtils.getSpValues(SPKeyValuesUtils.SP_USER_ID, SPUtils.TYPE_STRING));
        Log.e("TAGmap",map.toString());
        GloableConstant.getInstance().startProgressDialog(this);
        Xutil.GET(ApiUrl.NOTEDETAIL,map,new MyCallBack<NoteDetailResponse>(){
            @Override
            public void onSuccess(NoteDetailResponse result) {
                super.onSuccess(result);
                GloableConstant.getInstance().stopProgressDialog1();
                if(result.getStatus_code().equals("200")){
                    if(result.getData() != null){
                        mNoteType.setText(result.getData().getTitle());
                        mNoteContent.setText(result.getData().getContent());
                        mNoteTime.setText(result.getData().getCreated_at());
                        if(result.getData().getIs_like().equals("1")){
                            mGiveZza.setBackground(ContextCompat.getDrawable(CoursesNoteDetialActivity.this,R.mipmap.common_like_pre));
                        }else{
                            mGiveZza.setBackground(ContextCompat.getDrawable(CoursesNoteDetialActivity.this,R.mipmap.common_like));
                        }
                    }
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
                GloableConstant.getInstance().stopProgressDialog1();
            }
        });
    }
    private void requestZan(){
        Map<String,Object> map = new HashMap<>();
//        map.put("user_id","1");
        map.put("user_id", (String) SPUtils.getSpValues(SPKeyValuesUtils.SP_USER_ID, SPUtils.TYPE_STRING));
        map.put("be_like_id",mNoteId);
        map.put("type","2");
        Xutil.Post(ApiUrl.GETZAN,map,new MyCallBack<SupportResponse>(){
            @Override
            public void onSuccess(SupportResponse result) {
                super.onSuccess(result);
                if(result.getStatus_code().equals("204")){
                    ToastUtil.showShortToast("点赞成功");
                    mGiveZza.setBackground(ContextCompat.getDrawable(CoursesNoteDetialActivity.this,R.mipmap.common_like_pre));
                }else if(result.getStatus_code().equals("205")){
                    ToastUtil.showShortToast("取消点赞成功");
                    mGiveZza.setBackground(ContextCompat.getDrawable(CoursesNoteDetialActivity.this,R.mipmap.common_like));
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
            }
        });

    }
}
