package com.jiaoshizige.teacherexam.activity;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.EditText;

import com.umeng.analytics.MobclickAgent;
import com.jiaoshizige.teacherexam.R;
import com.jiaoshizige.teacherexam.base.BaseActivity;
import com.jiaoshizige.teacherexam.base.GloableConstant;
import com.jiaoshizige.teacherexam.http.ApiUrl;
import com.jiaoshizige.teacherexam.http.MyCallBack;
import com.jiaoshizige.teacherexam.http.SupportResponse;
import com.jiaoshizige.teacherexam.http.Xutil;
import com.jiaoshizige.teacherexam.utils.SPKeyValuesUtils;
import com.jiaoshizige.teacherexam.utils.SPUtils;
import com.jiaoshizige.teacherexam.utils.ToastUtil;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/a1/16.
 * 我要提问
 */

public class PutQuestionsActivity extends BaseActivity{
    @BindView(R.id.question_content)
    EditText mQuestionContent;
    private String mCourseId;
    @Override
    protected int getLayoutId() {
        return R.layout.put_question_activity;
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("我要提问");
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("我要提问");
        MobclickAgent.onPause(this);
    }

    @Override
    protected void initView() {
        setSubTitle().setText("提交");
        setSubTitle().setTextColor(ContextCompat.getColor(this,R.color.purple4));
        setToolbarTitle().setText("我要提问");
        if(getIntent().getExtras().get("course_id") != null){
            mCourseId = (String) getIntent().getExtras().get("course_id");
        }else{
            mCourseId = "";
        }
        setSubTitle().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!mQuestionContent.getText().toString().trim().equals("")){
                    reuqestPutQuestion(mQuestionContent.getText().toString());
                }else{
                    ToastUtil.showShortToast("请先输入提问内容");
                }
            }
        });
    }
    private void reuqestPutQuestion(String content){
        Map<String,Object> map = new HashMap<>();
//        map.put("user_id","1");
        map.put("user_id", SPUtils.getSpValues(SPKeyValuesUtils.SP_USER_ID, SPUtils.TYPE_STRING));
        map.put("course_id",mCourseId);
        map.put("content",content);
        GloableConstant.getInstance().showmeidialog(this);
        Xutil.Post(ApiUrl.ASKADD,map,new MyCallBack<SupportResponse>(){
            @Override
            public void onSuccess(SupportResponse result) {
                super.onSuccess(result);
                GloableConstant.getInstance().stopProgressDialog();
                if(result.getStatus_code().equals("204")){
                    ToastUtil.showShortToast("提问成功");
                    finish();
                }else if (result.getStatus_code().equals("401")) {
                    GloableConstant.getInstance().clearAll();
                    ToastUtil.showShortToast("请重新登录");
                    startActivity(new Intent(PutQuestionsActivity.this, LoginActivity.class));
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

