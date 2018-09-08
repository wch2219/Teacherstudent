package com.jiaoshizige.teacherexam.activity;


import android.content.Intent;
import android.graphics.Color;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.jiaoshizige.teacherexam.R;
import com.jiaoshizige.teacherexam.base.BaseActivity;
import com.jiaoshizige.teacherexam.base.GloableConstant;
import com.jiaoshizige.teacherexam.http.ApiUrl;
import com.jiaoshizige.teacherexam.http.MyCallBack;
import com.jiaoshizige.teacherexam.http.SupportResponse;
import com.jiaoshizige.teacherexam.http.Xutil;
import com.jiaoshizige.teacherexam.model.AccountNumberResponse;
import com.jiaoshizige.teacherexam.utils.SPKeyValuesUtils;
import com.jiaoshizige.teacherexam.utils.SPUtils;
import com.jiaoshizige.teacherexam.utils.ToastUtil;
import com.jiaoshizige.teacherexam.utils.ToolUtils;
import com.jiaoshizige.teacherexam.widgets.ClearEditText;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;


/**
 * Created by Administrator on 2017/7/28 0028.
 */

public class RegresterA extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.phone)
    ClearEditText mPhone;
    @BindView(R.id.code)
    ClearEditText mCode;
    @BindView(R.id.pass_word)
    ClearEditText mPassWord;
    @BindView(R.id.identifying_code)
    TextView mIdentifying;
    @BindView(R.id.login_bt)
    Button mLogin;
    @BindView(R.id.check)
    CheckBox mCheckBox;
    @BindView(R.id.ll)
    LinearLayout ll;
    @BindView(R.id.service_deal)
    TextView mService;
    private String phone, code, password;
    private Intent intent;
    private String flag;
    private String type;

    @Override
    protected int getLayoutId() {
        return R.layout.regrester_activity;
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("注册");
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("注册");
        MobclickAgent.onPause(this);
    }

    @Override
    protected void initView() {
        String str = "点击注册即表示接受<font color='#9A70C4'>《服务协议》</font>";
        mService.setTextSize(14);
        mService.setText(Html.fromHtml(str));
        intent = getIntent();
        flag = intent.getStringExtra("flag");

        Log.e("*********flag",flag);
        if (flag.equals("1")) {
            setSubTitle().setText("");
            setToolbarTitle().setText("注册");
            type = "1";
            ll.setVisibility(View.VISIBLE);
        }

        if (flag.equals("2")) {
            setSubTitle().setText("");
            setToolbarTitle().setText("找回密码");
            type = "3";
            ll.setVisibility(View.GONE);
        }
        mIdentifying.setOnClickListener(this);
        mCheckBox.setOnClickListener(this);
        mLogin.setOnClickListener(this);
        mPassWord.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                mLogin.setBackgroundResource(R.drawable.button_gray);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (mCheckBox.isChecked()){
                    mLogin.setBackgroundResource(R.drawable.purple_shape);
                }
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
                phone = mPhone.getText().toString().trim();
                if (TextUtils.isEmpty(phone)) {
                    ToastUtil.showShortToast("请输入手机号码");
                }else {
                    if (ToolUtils.isCellphone(phone)){
                        getCode(type);
                    }else {
                        ToastUtil.showShortToast("您输入的手机号码不正确，请重新输入");
                    }
                }

                break;
            case R.id.login_bt:

                if (flag.equals("1")) {
                    phone = mPhone.getText().toString().trim();
                    if (TextUtils.isEmpty(phone)) {
                        ToastUtil.showShortToast("请输入手机号码");
                    }else {
                        if (ToolUtils.isCellphone(phone)){
                            CodeLogin();
                        }else {
                            ToastUtil.showShortToast("您输入的手机号码不正确，请重新输入");
                        }
                    }

                }
                if (flag.equals("2")) {
                    phone = mPhone.getText().toString().trim();
                    if (TextUtils.isEmpty(phone)) {
                        ToastUtil.showShortToast("请输入手机号码");
                    }else {
                        if (ToolUtils.isCellphone(phone)){
                            forget();
                        }else {
                            ToastUtil.showShortToast("您输入的手机号码不正确，请重新输入");
                        }
                    }

                }
                break;
            case R.id.check:
//                if (mCheckBox.isChecked()){
//                    mCheckBox.setChecked(false);
//                }else {
//                    mCheckBox.setChecked(true);
//                }
                break;
            default:
                break;
        }
    }

    /**
     * 找回密码
     */
    private void forget() {

        code = mCode.getText().toString().trim();
        password = mPassWord.getText().toString().trim();

        if (TextUtils.isEmpty(code)) {
            ToastUtil.showShortToast("请输入验证码");
            return;
        }
        if (TextUtils.isEmpty(password)) {
            ToastUtil.showShortToast("请输入密码");
            return;
        }
        if(password.length()>16 || password.length() < 6){
            ToastUtil.showShortToast("密码格式不正确");
            return;
        }
        Map<String, Object> map = new HashMap<>();
        map.put("mobile", phone);
        map.put("code", code);
        map.put("password", password);
        Log.e("********forgetmap",map.toString());
        GloableConstant.getInstance().startProgressDialog(this);
        Xutil.Post(ApiUrl.FORGRT,map,new MyCallBack<SupportResponse>(){
            @Override
            public void onSuccess(SupportResponse result) {
                super.onSuccess(result);
                GloableConstant.getInstance().stopProgressDialog1();
                Log.e("************code",result.getStatus_code());
                if (result.getStatus_code().equals("204")){

                    startActivity(new Intent(RegresterA.this, LoginActivity.class));
                    finish();
                }
                ToastUtil.showShortToast(result.getMessage());
            }

            @Override
            public void onFinished() {
                super.onFinished();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
                GloableConstant.getInstance().stopProgressDialog1();
                Log.e("************ex",ex.getMessage());
            }
        });
    }

    /**
     * 注册
     */
    private void CodeLogin() {

        code = mCode.getText().toString().trim();
        password = mPassWord.getText().toString().trim();

        if (TextUtils.isEmpty(code)) {
            ToastUtil.showShortToast("请输入验证码");
            return;
        }
        if (TextUtils.isEmpty(password)) {
            ToastUtil.showShortToast("请输入密码");
            return;
        }
        if(password.length() < 6 || password.length() > 16){
            ToastUtil.showShortToast("密码格式不正确");
            return;
        }
        Map<String, Object> map = new HashMap<>();
        map.put("mobile", phone);
        map.put("sms_code", code);
        map.put("password", password);
        GloableConstant.getInstance().startProgressDialog(this);
        Xutil.Post(ApiUrl.REGISTER, map, new MyCallBack<AccountNumberResponse>() {
            @Override
            public void onSuccess(AccountNumberResponse result) {
                super.onSuccess(result);
                GloableConstant.getInstance().stopProgressDialog1();
                if (result.getStatus_code().equals("200")) {
                    String user_id = result.getData().getUser_id();
                    String token = result.getData().getToken();
                    Log.e("*********", user_id + "*******" + token);
                    SPUtils.setSPValues(SPUtils.TYPE_STRING, SPKeyValuesUtils.SP_USER_ID, user_id);
                    SPUtils.setSPValues(SPUtils.TYPE_STRING, SPKeyValuesUtils.Token, token);
                    SPUtils.setSPValues(SPUtils.TYPE_STRING, SPKeyValuesUtils.SP_IS_LOGIN, "1");
                    Log.e("*******user_id", SPUtils.getSpValues(SPKeyValuesUtils.SP_IS_LOGIN, SPUtils.TYPE_STRING) + "//***********" +
                            SPUtils.getSpValues(SPKeyValuesUtils.Token, SPUtils.TYPE_STRING) + "*********" +
                            SPUtils.getSpValues(SPKeyValuesUtils.SP_USER_ID, SPUtils.TYPE_STRING));
                    startActivity(new Intent(RegresterA.this, PersonMessageActivity.class));
//                    finish();
                    ToastUtil.showShortToast(result.getMessage());
                } else {
                    ToastUtil.showShortToast(result.getMessage());
                }
            }

            @Override
            public void onFinished() {
                super.onFinished();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
                GloableConstant.getInstance().stopProgressDialog1();
            }
        });
    }

    /**
     * 获取验证码
     */

    public void getCode(String type) {


        Map<String, Object> map = new HashMap<>();
        map.put("mobile", phone);
        map.put("type", type);
        Log.e("*********map",map.toString());
        Xutil.Post(ApiUrl.SENDCODE, map, new MyCallBack<SupportResponse>() {
            @Override
            public void onSuccess(SupportResponse result) {
                super.onSuccess(result);
                Log.e("*********map",result.getStatus_code());
                if (result.getStatus_code().equals("204")) {
                    ToastUtil.showShortToast(result.getMessage());
                }
                sec = 60;
                mIdentifying.postDelayed(runnable, 0);

                if (result.getStatus_code().equals("205")){
                    ToastUtil.showShortToast(result.getMessage());
//                    startActivity(new Intent(RegresterA.this,LoginActivity.class));
                }

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
            } else {
                //倒计时
                mIdentifying.setClickable(false);
                mIdentifying.setTextColor(Color.parseColor("#999999"));
                mIdentifying.setText(sec + "s后重试");
                sec--;
                mIdentifying.postDelayed(runnable, 1000);
            }
        }
    };

}
