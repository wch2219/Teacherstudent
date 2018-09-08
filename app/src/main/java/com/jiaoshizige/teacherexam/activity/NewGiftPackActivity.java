package com.jiaoshizige.teacherexam.activity;


import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.jiaoshizige.teacherexam.R;
import com.jiaoshizige.teacherexam.base.BaseActivity;
import com.jiaoshizige.teacherexam.base.GloableConstant;
import com.jiaoshizige.teacherexam.utils.Sphelper;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/a1/3.
 * 新人大礼包
 */

public class NewGiftPackActivity extends BaseActivity implements View.OnClickListener {

    //    @BindView(R.id.guide)
//    RadioButton mGuide;
//    @BindView(R.id.submit_bt)
//    Button mSunmit;
//    @BindView(R.id.guide)
//    TextView mGuide;
    @BindView(R.id.submit_bt)
    RelativeLayout mSunmit;
    @BindView(R.id.again_appraisal)
    TextView mAgain;
    @BindView(R.id.check)
    TextView mCheck;
    @BindView(R.id.again)
    RelativeLayout again;
    private int flag = 0;
    private Context mContext;

    @Override
    protected int getLayoutId() {

        return R.layout.new_gift_pack_layout;
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("新人大礼包");
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("新人大礼包");
        MobclickAgent.onPause(this);
    }

    @Override
    protected void initView() {
        mContext=this;
        if (getIntent().getStringExtra("tag") != null) {
            if (getIntent().getStringExtra("tag").equals("0")) {
                setToolbarTitle().setText("新人大礼包");
            } else if (getIntent().getStringExtra("tag").equals("1")) {
                setToolbarTitle().setText("我的测评");
                if(!Sphelper.getBoolean(mContext,"hadexam","isexam") && Sphelper.getBoolean(mContext,"hadexam","isexam") != null){
                 //未测评
                   mSunmit.setVisibility(View.VISIBLE);
                    again.setVisibility(View.GONE);
                    GloableConstant.getInstance().setResult("0");
                }else {
                    //测评过
                    mSunmit.setVisibility(View.GONE);
                    again.setVisibility(View.VISIBLE);
                    GloableConstant.getInstance().setResult("1");
                }
            } else {
                setToolbarTitle().setText("");
            }
        }
        setSubTitle().setText("");
        mSunmit.setOnClickListener(this);
        mAgain.setOnClickListener(this);
        mCheck.setOnClickListener(this);
    }

//    @OnClick(R.id.submit_bt)
//    public void submitClick() {
//        if (flag == 1) {
//            startActivity(new Intent(this, ExamCenterActivity.class));
//            finish();
//        }
//
//    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submit_bt:
                startActivity(new Intent(this, ExamCenterActivity.class));
                finish();
                break;
            case R.id.again_appraisal:
                startActivity(new Intent(this, ExamCenterActivity.class));
                finish();
                break;
            case R.id.check:
                startActivity(new Intent(this, EvaluationReport.class));
                finish();
                break;

        }
    }

//    @OnCheckedChanged({R.id.guide, R.id.guidance, R.id.psychology})
//    public void ClickRadioGroup(CompoundButton view, boolean ischanged) {
//        switch (view.getId()) {
//            case R.id.guide:
//                flag = 1;
//                break;
//            case R.id.guidance:
//                break;
//            case R.id.psychology:
//                break;
//        }
//    }


}
