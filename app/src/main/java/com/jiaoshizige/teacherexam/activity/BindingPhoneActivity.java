package com.jiaoshizige.teacherexam.activity;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.jiaoshizige.teacherexam.R;
import com.jiaoshizige.teacherexam.base.BaseActivity;
import com.jiaoshizige.teacherexam.widgets.ClearEditText;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/a1/10 0010.
 */

public class BindingPhoneActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.phone)
    EditText mPhone;
    @BindView(R.id.code)
    ClearEditText mCode;
    @BindView(R.id.identifying_code)
    TextView mIdentifying;
    @BindView(R.id.login_bt)
    Button mLogin;
    @BindView(R.id.service_deal)
    TextView mService;
    @BindView(R.id.text)
    TextView mTest;


    @Override
    protected int getLayoutId() {
        return R.layout.phone_short_cut_fragment;
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("绑定手机");
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("绑定手机");
        MobclickAgent.onPause(this);
    }

    @Override
    protected void initView() {
        mTest.setVisibility(View.GONE);
        mService.setText("绑定手机号，获取最新考试提醒");
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
                break;
            case R.id.login_bt:
                break;
            default:
                break;
        }
    }
}
