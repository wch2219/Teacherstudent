package com.jiaoshizige.teacherexam.activity;

import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.jiaoshizige.teacherexam.MainActivity;
import com.jiaoshizige.teacherexam.R;
import com.jiaoshizige.teacherexam.base.BaseActivity;
import com.jiaoshizige.teacherexam.base.GloableConstant;
import com.jiaoshizige.teacherexam.yy.activity.CoinRecordActivity;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/12/9 0009.
 */

public class PayResultActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.pay_success)
    LinearLayout mSuccess;
    @BindView(R.id.pay_failure)
    LinearLayout mFailure;
    @BindView(R.id.shopping)
    TextView mShopping;
    @BindView(R.id.checkout)
    TextView mCheckout;
    @BindView(R.id.pay_again)
    TextView mAgain;
    @BindView(R.id.failure_check)
    TextView mCheck;

    private Intent intent;

    @Override
    protected int getLayoutId() {
        return R.layout.pay_result_layout;
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("支付结果");
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("支付结果");
        MobclickAgent.onPause(this);
    }

    @Override
    protected void initView() {
        setSubTitle().setText("");
        setToolbarTitle().setText("支付结果");
        if (getIntent().getStringExtra("flag").equals("success")){
            mSuccess.setVisibility(View.VISIBLE);
            mFailure.setVisibility(View.GONE);
        }else {
            mSuccess.setVisibility(View.GONE);
            mFailure.setVisibility(View.VISIBLE);
        }

        mShopping.setOnClickListener(this);
        mCheckout.setOnClickListener(this);
        mAgain.setOnClickListener(this);
        mCheck.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.shopping:
//                MainActivity mainActivity = new MainActivity();
//                mainActivity.changeSchoolTab();
                intent = new Intent();
                if (GloableConstant.getInstance().getOrder().equals("1")){
                    intent.setClass(PayResultActivity.this,TeacherCoinActivity.class);
                    startActivity(intent);
                }else {
                    intent.setClass(PayResultActivity.this,MainActivity.class);
                    startActivity(intent);
                    GloableConstant.getInstance().setFlag("1");
                }
                finish();
                break;
            case R.id.checkout:
                intent = new Intent();
                if (GloableConstant.getInstance().getOrder().equals("1")){
                    intent.setClass(PayResultActivity.this, CoinRecordActivity.class);
                }else {
                    intent.setClass(PayResultActivity.this,MyOrderActivity.class);
                    startActivity(intent);
                }

                finish();
                break;
            case R.id.pay_again:
                intent = new Intent();
                if (GloableConstant.getInstance().getOrder().equals("1")){
                    intent.setClass(PayResultActivity.this, CoinRecordActivity.class);
                }else {
                    intent.setClass(PayResultActivity.this,MyOrderActivity.class);
                    startActivity(intent);
                }
                finish();
                break;
            case R.id.failure_check:
                intent = new Intent();
                if (GloableConstant.getInstance().getOrder().equals("1")){
                    intent.setClass(PayResultActivity.this,CoinRecordActivity.class);
                }else {
                    intent.setClass(PayResultActivity.this,MyOrderActivity.class);
                    startActivity(intent);
                }
                finish();
                break;
        }

    }
}
