package com.jiaoshizige.teacherexam.activity;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.jiaoshizige.teacherexam.R;
import com.jiaoshizige.teacherexam.base.BaseActivity;
import com.jiaoshizige.teacherexam.utils.SPKeyValuesUtils;
import com.jiaoshizige.teacherexam.utils.SPUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/a1/7 0007.
 */

public class LoginNumActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.phone_num)
    TextView phone_num;
    @BindView(R.id.change_phone_num)
    Button mChange;

    private Intent intent;
    private String phone, user_id;

    @Override
    protected int getLayoutId() {
        return R.layout.login_num_activity;
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("登录手机号码");
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("登录手机号码");
        MobclickAgent.onPause(this);
    }

    @Override
    protected void initView() {
        setSubTitle().setText("");
        setToolbarTitle().setText("登录手机号码");
        mChange.setOnClickListener(this);
        user_id = (String) SPUtils.getSpValues(SPKeyValuesUtils.SP_USER_ID, SPUtils.TYPE_STRING);
        intent = getIntent();
        phone = intent.getStringExtra("phone");
        Log.e("**********,,", phone + "*********" + user_id);
        //     用****替换手机号码中间4位
        if (isMobileNum(phone)) {
            String maskNumber = phone.substring(0, 3) + "****" + phone.substring(7, phone.length());
            Log.e("**********,,",maskNumber);
            phone_num.setText(maskNumber);
        }


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.change_phone_num:
                intent = new Intent(LoginNumActivity.this, ChangePhoneNumActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }

    /**
     * 检查是否是电话号码
     *
     * @return
     */
    public static boolean isMobileNum(String mobiles) {
        Pattern pattern = Pattern.compile("1[0-9]{10}");
        Matcher matcher = pattern.matcher(mobiles);
        return matcher.matches();

    }
}
