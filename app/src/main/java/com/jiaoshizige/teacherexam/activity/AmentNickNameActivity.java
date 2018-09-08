package com.jiaoshizige.teacherexam.activity;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
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

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/a1/6 0006.
 */

/**
 * 修改昵称
 */

public class AmentNickNameActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.new_nickname)
    EditText mNick_Name;
    @BindView(R.id.complete)
    Button mComplete;
    private String mNickName,user_id;

    @Override
    protected int getLayoutId() {
        return R.layout.ament_nickname_layout;
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("修改昵称");
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("修改昵称");
        MobclickAgent.onPause(this);
    }

    @Override
    protected void initView() {
        setSubTitle().setText("");
        setToolbarTitle().setText("修改昵称");
        user_id= (String) SPUtils.getSpValues(SPKeyValuesUtils.SP_USER_ID, SPUtils.TYPE_STRING);
        mComplete.setOnClickListener(this);
        mNick_Name.addTextChangedListener(new TextWatcher() {
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
                UpdataMessage();
                break;
        }
    }
    private void UpdataMessage(){
        mNickName=mNick_Name.getText().toString().trim();
        mNickName = mNickName.replaceAll(" ", "");
        if (TextUtils.isEmpty(mNickName)){
            ToastUtil.showShortToast("修改的昵称不能为空");
            return;
        }
        if(mNickName.length() < 2 || mNickName.length() > 20){
            ToastUtil.showShortToast("修改的昵称为2至20个连续字符");
            return;
        }
        Map<String,Object> map=new HashMap<>();
//        map.put("user_id",user_id);
        map.put("user_id", (String) SPUtils.getSpValues(SPKeyValuesUtils.SP_USER_ID, SPUtils.TYPE_STRING));
        map.put("nick_name",mNickName);
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
}
