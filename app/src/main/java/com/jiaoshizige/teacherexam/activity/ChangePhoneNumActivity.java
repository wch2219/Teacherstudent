package com.jiaoshizige.teacherexam.activity;

import android.graphics.Color;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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
 * Created by Administrator on 2017/a1/7 0007.
 */

public class ChangePhoneNumActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.complete)
    Button mComplete;
    @BindView(R.id.code)
    TextView mCode;
    @BindView(R.id.new_phone_num)
    EditText mNew_Phone;
    @BindView(R.id.identifying)
    EditText midentifying;
    private String mNewPhone,mIdentifying,user_id;

    @Override
    protected int getLayoutId() {
        return R.layout.change_phone_num_layout;
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("更换手机号");
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("更换手机号");
        MobclickAgent.onPause(this);
    }

    @Override
    protected void initView() {
        setSubTitle().setText("");
        setToolbarTitle().setText("更换手机号");
        user_id= (String) SPUtils.getSpValues(SPKeyValuesUtils.SP_USER_ID,SPUtils.TYPE_STRING);
        mComplete.setOnClickListener(this);
        mCode.setOnClickListener(this);
        mNew_Phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                mComplete.setBackgroundResource(R.drawable.button_gray);
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mComplete.setBackgroundResource(R.drawable.purple_shape);
            }

            @Override
            public void afterTextChanged(Editable s) {
                mComplete.setBackgroundResource(R.drawable.purple_shape);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.complete:
                mNewPhone = mNew_Phone.getText().toString().trim();
                if (TextUtils.isEmpty(mNewPhone)){
                    ToastUtil.showShortToast("请输入手机号码");
                }else {
                    if ( ToolUtils.isCellphone(mNewPhone)){
                        UpdataMessage();
                    }else {
                        ToastUtil.showShortToast("您输入的手机号码不正确，请重新输入");
                    }
                }

                break;
            case R.id.code:
                mNewPhone = mNew_Phone.getText().toString().trim();
                if (TextUtils.isEmpty(mNewPhone)){
                    ToastUtil.showShortToast("请输入手机号码");
                }else {
                   if ( ToolUtils.isCellphone(mNewPhone)){
                        getCode();
                    }else {
                       ToastUtil.showShortToast("您输入的手机号码不正确，请重新输入");
                   }
                }

                break;
            default:
                break;
        }
    }
    private void UpdataMessage(){
//        mNewPhone = mNew_Phone.getText().toString().trim();
        mIdentifying=midentifying.getText().toString().trim();
//        if (TextUtils.isEmpty(mNewPhone)){
//            ToastUtil.showShortToast("请输入电话号码");
//        }
        if (TextUtils.isEmpty(mIdentifying)){
            ToastUtil.showShortToast("请输入验证码");
            return;
        }
        Map<String,Object> map=new HashMap<>();
//        map.put("user_id",user_id);
        map.put("user_id", (String) SPUtils.getSpValues(SPKeyValuesUtils.SP_USER_ID, SPUtils.TYPE_STRING));
        map.put("mobile",mNewPhone);
        map.put("code",mIdentifying);
        Log.e("*******map",map.toString());
        GloableConstant.getInstance().showmeidialog(this);
        Xutil.Post(ApiUrl.UPDATA,map,new MyCallBack<SupportResponse>(){
            @Override
            public void onSuccess(SupportResponse result) {
                super.onSuccess(result);
                GloableConstant.getInstance().stopProgressDialog();
                Log.e("******result",result.getStatus_code());
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

    /**
     * 获取验证码
     */
    public void getCode() {

        Map<String, Object> map = new HashMap<>();
        map.put("mobile", mNewPhone);
        map.put("type", "4");
        GloableConstant.getInstance().showmeidialog(this);
        Xutil.Post(ApiUrl.SENDCODE, map, new MyCallBack<SupportResponse>() {
            @Override
            public void onSuccess(SupportResponse result) {
                super.onSuccess(result);
                GloableConstant.getInstance().stopProgressDialog();
                if (result.getStatus_code().equals("204")) {
                    ToastUtil.showShortToast(result.getMessage());
                }
                sec = 60;
                mCode.postDelayed(runnable, 0);

            }

            @Override
            public void onFinished() {
                super.onFinished();
                GloableConstant.getInstance().stopProgressDialog();
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
                mCode.setTextColor(Color.parseColor("#9A70C4"));
                mCode.setText("获取验证码");
                mCode.setClickable(true);
                mCode.removeCallbacks(runnable);
            }else {
                //倒计时
                mCode.setClickable(false);
                mCode.setTextColor(Color.parseColor("#999999"));
                mCode.setText(sec+"s后重试");
                sec--;
                mCode.postDelayed(runnable,1000);
            }
        }
    };
}
