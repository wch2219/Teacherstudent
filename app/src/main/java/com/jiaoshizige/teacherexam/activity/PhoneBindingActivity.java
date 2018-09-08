package com.jiaoshizige.teacherexam.activity;

import android.content.Intent;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.jiaoshizige.teacherexam.MainActivity;
import com.jiaoshizige.teacherexam.R;
import com.jiaoshizige.teacherexam.base.BaseActivity;
import com.jiaoshizige.teacherexam.http.ApiUrl;
import com.jiaoshizige.teacherexam.http.MyCallBack;
import com.jiaoshizige.teacherexam.http.SupportResponse;
import com.jiaoshizige.teacherexam.http.Xutil;
import com.jiaoshizige.teacherexam.model.AccountNumberResponse;
import com.jiaoshizige.teacherexam.utils.SPKeyValuesUtils;
import com.jiaoshizige.teacherexam.utils.SPUtils;
import com.jiaoshizige.teacherexam.utils.ToastUtil;
import com.jiaoshizige.teacherexam.widgets.ClearEditText;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/12/25 0025.
 */

public class PhoneBindingActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.phone)
    EditText mPhone;
    @BindView(R.id.code)
    ClearEditText mCode;
    @BindView(R.id.identifying_code)
    TextView mIdentifying;
    @BindView(R.id.login_bt)
    Button mLogin;

    private String phone, code;
    private String  unionid,openid,nickname,headimgurl;
    private Intent intent;

    @Override
    protected int getLayoutId() {
        return R.layout.phone_binding_layout;
    }

    @Override
    protected void initView() {
        setSubTitle().setText("");
        setToolbarTitle().setText("绑定手机");
        if (getIntent().getStringExtra("unionid")!=null){
            unionid=getIntent().getStringExtra("unionid");
        }
        if (getIntent().getStringExtra("openid")!=null){
            openid=getIntent().getStringExtra("openid");
        }
        if (getIntent().getStringExtra("nickname")!=null){
            nickname=getIntent().getStringExtra("nickname");
        }
        if (getIntent().getStringExtra("headimgurl")!=null){
            headimgurl=getIntent().getStringExtra("headimgurl");
        }

        Log.e("*********getintent", unionid + "++++++" + openid + "+++++" + nickname + "+++++" + headimgurl);

        mIdentifying.setOnClickListener(this);
        mLogin.setOnClickListener(this);
        mPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                mLogin.setBackgroundResource(R.drawable.button_gray);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mLogin.setBackgroundResource(R.drawable.purple_shape);
            }

            @Override
            public void afterTextChanged(Editable s) {
                mLogin.setBackgroundResource(R.drawable.purple_shape);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.identifying_code:
                getacode();
                break;
            case R.id.login_bt:
                binding();
                break;
        }
    }

    //微信绑定
    private void binding(){
        phone=mPhone.getText().toString().trim();
        code=mCode.getText().toString().trim();
        if (TextUtils.isEmpty(phone)){
            ToastUtil.showShortToast("请输入手机号码");
        }
        if (TextUtils.isEmpty(code)){
            ToastUtil.showShortToast("请输入验证码");
        }
        Map<String,Object>map=new HashMap<>();
        map.put("mobile",phone);
        map.put("code",code);
        map.put("nick_name",nickname);
        map.put("open_id",openid);
        map.put("user_picture",headimgurl);
        map.put("unionid",unionid);
        map.put("service","1");
        Log.e("******bindmap",map.toString());
        Xutil.Post(ApiUrl.BING,map,new MyCallBack<AccountNumberResponse>(){
            @Override
            public void onSuccess(AccountNumberResponse result) {
                super.onSuccess(result);
                if(result.getStatus_code().equals("204")){
                    ToastUtil.showShortToast(result.getMessage());
                    SPUtils.setSPValues(SPUtils.TYPE_STRING, SPKeyValuesUtils.SP_USER_ID, result.getData().getUser_id());
                    SPUtils.setSPValues(SPUtils.TYPE_STRING, SPKeyValuesUtils.Token, result.getData().getToken());
                    intent=new Intent();
                    intent.setClass(PhoneBindingActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
                if (result.getStatus_code().equals("205")){
                    ToastUtil.showShortToast(result.getMessage());
//                    finish();
                }
            }
        });
    }


    //获取验证码
    private void getacode(){
        phone=mPhone.getText().toString().trim();
        if (TextUtils.isEmpty(phone)){
            ToastUtil.showShortToast("请输入手机号码");
        }
        Map<String,Object> map=new HashMap<>();
        map.put("mobile",phone);
        map.put("type",1);
        map.put("is_weixin","1");
        Xutil.Post(ApiUrl.SENDCODE,map,new MyCallBack<SupportResponse>(){
            @Override
            public void onSuccess(SupportResponse result) {
                super.onSuccess(result);
                if (result.getStatus_code().equals("204")){
                    ToastUtil.showShortToast(result.getMessage());
                }
                if (result.getStatus_code().equals("205")){
                    ToastUtil.showShortToast(result.getMessage());
                    finish();
                }
                sec = 60;
                mIdentifying.postDelayed(runnable, 0);
            }

            @Override
            public void onFinished() {
                super.onFinished();
            }


        });
    }

    private int sec;
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (sec == 0) {
                //发送前
                sec = 60;
                mIdentifying.setTextColor(Color.parseColor("#9A70C4"));
                mIdentifying.setText("获取验证码");
                mIdentifying.setClickable(true);
                mIdentifying.removeCallbacks(runnable);
            }else {
                //倒计时
                mIdentifying.setClickable(false);
                mIdentifying.setTextColor(Color.parseColor("#999999"));
                mIdentifying.setText(sec+"s后重试");
                sec--;
                mIdentifying.postDelayed(runnable,1000);
            }
        }
    };
}
