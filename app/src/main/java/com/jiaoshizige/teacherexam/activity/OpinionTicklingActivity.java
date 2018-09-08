package com.jiaoshizige.teacherexam.activity;

/**
 * Created by Administrator on 2017/a1/8 0008.
 */

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
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
import com.jiaoshizige.teacherexam.utils.ToolUtils;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;

/**
 * 意见反馈
 */

public class OpinionTicklingActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.commit)
    Button mCommit;
    @BindView(R.id.phone)
    EditText mPhone;
    @BindView(R.id.suggest)
    EditText mSuggest;

    private String user_id;
    private String phone,suggest;

    @Override
    protected int getLayoutId() {
        return R.layout.opinion_ticking_activity;

    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("意见反馈");
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("意见反馈");
        MobclickAgent.onPause(this);
    }

    @Override
    protected void initView() {
        setSubTitle().setText("");
        setToolbarTitle().setText("意见反馈");
        user_id= (String) SPUtils.getSpValues(SPKeyValuesUtils.SP_USER_ID,SPUtils.TYPE_STRING);
        mCommit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.commit:
                opinionTicking();

//                phone=mPhone.getText().toString().trim();
//                if (TextUtils.isEmpty(phone)){
//                    ToastUtil.showShortToast("请输入您的联系方式");
//                }else {
//                    if (ToolUtils.isCellphone(phone)){
//                        opinionTicking();
//                    }else {
//                        ToastUtil.showShortToast("您输入的手机号码不正确，请重新输入");
//                    }
//                }



                break;
        }
    }
    private void opinionTicking(){

        suggest=mSuggest.getText().toString().trim();
        phone=mPhone.getText().toString().trim();
        if (TextUtils.isEmpty(suggest)){
            ToastUtil.showShortToast("请输入您的意见");
            return;
        }
        if(!ToolUtils.isCellphone(phone)){
            ToastUtil.showShortToast("您输入的手机号码不正确，请重新输入");
            return;
        }
        Map<String,Object> map=new HashMap<>();
//        map.put("user_id",1);
        map.put("user_id",SPUtils.getSpValues(SPKeyValuesUtils.SP_USER_ID, SPUtils.TYPE_STRING));
        map.put("mobile",phone);
        map.put("content",suggest);
        GloableConstant.getInstance().startProgressDialog(this);
        Xutil.Post(ApiUrl.SUGGEST,map,new MyCallBack<SupportResponse>(){
            @Override
            public void onSuccess(SupportResponse result) {
                super.onSuccess(result);
                GloableConstant.getInstance().stopProgressDialog();
                if (result.getStatus_code().equals("204")){
                    ToastUtil.showShortToast(result.getMessage());
                    finish();
                }
            }

            @Override
            public void onFinished() {
                super.onFinished();
                GloableConstant.getInstance().stopProgressDialog();
            }
        });

    }
}
