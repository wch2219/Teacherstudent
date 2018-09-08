package com.jiaoshizige.teacherexam.activity;

import com.umeng.analytics.MobclickAgent;
import com.jiaoshizige.teacherexam.R;
import com.jiaoshizige.teacherexam.base.BaseActivity;

/**
 * Created by Administrator on 2017/a1/8 0008.
 */

/**
 * 关于我们
 */

public class AboutUsActivity extends BaseActivity {
    @Override
    protected int getLayoutId() {
        return R.layout.about_us_layout;
    }
    @Override
    protected void initView() {
     setSubTitle().setText("");
        setToolbarTitle().setText("关于我们");
    }
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("关于我们"); //手动统计页面("SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(this); //统计时长
    }
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("关于我们"); //手动统计页面("SplashScreen"为页面名称，可自定义)，必须保证 onPageEnd 在 onPause 之前调用，因为SDK会在 onPause 中保存onPageEnd统计到的页面数据。
        MobclickAgent.onPause(this);
    }
}
