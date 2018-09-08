package com.jiaoshizige.teacherexam.activity;

import android.content.Intent;
import android.os.Handler;
import android.util.Log;

import com.jiaoshizige.teacherexam.MainActivity;
import com.jiaoshizige.teacherexam.R;
import com.jiaoshizige.teacherexam.base.BaseActivity;
import com.jiaoshizige.teacherexam.utils.SPKeyValuesUtils;
import com.jiaoshizige.teacherexam.utils.SPUtils;
import com.jiaoshizige.teacherexam.utils.ScreenUtil;
import com.jiaoshizige.teacherexam.utils.Sphelper;

/**
 * Created by Administrator on 2018/3/5.
 * 欢迎页面
 */

public class SplashActivity extends BaseActivity {
    private boolean isFirstUse;// 是否是第一次使用

    @Override
    protected int getLayoutId() {
        return R.layout.splash_layout;
    }

    @Override
    protected void initView() {
        //点击home键返回。再次点击回到原界面
        if (!isTaskRoot()) {

            finish();

            return;

        }
        ScreenUtil.showFullScreen(this, true);
        ScreenUtil.hideBottomUIMenu(this);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
//                Sphelper.save(SplashActivity.this, "isFirstUse", "isfiestuser", true);
                isFirstUse = Sphelper.getBoolean(SplashActivity.this, "isFirstUse", "isfiestuser");
                Log.e("*********isFirstUse", isFirstUse+"");
                if (!isFirstUse) {
                    startActivity(new Intent(SplashActivity.this, WelcomeActivity.class));
                    finish();
                } else {
                    if (SPUtils.getSpValues(SPKeyValuesUtils.SP_IS_LOGIN, SPUtils.TYPE_STRING).equals("1") && SPUtils.getSpValues(SPKeyValuesUtils.SP_IS_LOGIN, SPUtils.TYPE_STRING) != null) {
                        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            }
        }, 3000);
      /*  try {
            Thread.sleep(4500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if(SPUtils.getSpValues(SPKeyValuesUtils.SP_IS_LOGIN, SPUtils.TYPE_STRING).equals("1") && SPUtils.getSpValues(SPKeyValuesUtils.SP_IS_LOGIN, SPUtils.TYPE_STRING) != null){
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }else{
            Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }*/
    }

}
