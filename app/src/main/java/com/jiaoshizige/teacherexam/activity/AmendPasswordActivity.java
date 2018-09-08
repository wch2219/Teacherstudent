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
 * Created by Administrator on 2017/a1/7 0007.
 */

public class AmendPasswordActivity extends BaseActivity implements View.OnClickListener{

    @BindView(R.id.old_pass_word)
    EditText mOld_Pass_Word;
    @BindView(R.id.new_pass_word)
    EditText mNew_Pass_Word;

    @BindView(R.id.complete)
    Button mComplete;
    private String mOldPassWord,mNewPassWord,user_id;

    @Override
    protected int getLayoutId() {
        return R.layout.amend_password_activity_layout;
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("修改密码");
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("修改密码");
        MobclickAgent.onPause(this);
    }

    @Override
    protected void initView() {
        setSubTitle().setText("");
        setToolbarTitle().setText("修改密码");
        user_id= (String) SPUtils.getSpValues(SPKeyValuesUtils.SP_USER_ID,SPUtils.TYPE_STRING);
        mComplete.setOnClickListener(this);
        mOld_Pass_Word.addTextChangedListener(new TextWatcher() {
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
        switch (v.getId()){
            case R.id.complete:
                UpdataMessage();
                break;
        }
    }
    private void UpdataMessage(){
        mNewPassWord=mNew_Pass_Word.getText().toString().trim();
        mOldPassWord=mOld_Pass_Word.getText().toString().trim();
        if (TextUtils.isEmpty(mOldPassWord)){
            ToastUtil.showShortToast("请输入原密码");
            return;
        }
        if(mOldPassWord.length() < 6 || mOldPassWord.length() > 16){
            ToastUtil.showShortToast("原密码格式不正确");
            return;
        }
        if (TextUtils.isEmpty(mNewPassWord)){
            ToastUtil.showShortToast("请输入新密码");
            return;
        }
        if(mNewPassWord.length() < 6 || mNewPassWord.length() > 16){
            ToastUtil.showShortToast("新密码格式不正确");
            return;
        }
        if(mNew_Pass_Word.equals(mOld_Pass_Word)){
            ToastUtil.showShortToast("原密码与新密码不能相同");
            return;
        }

        Map<String,Object>map=new HashMap<>();
//        map.put("user_id",user_id);
        map.put("user_id", (String) SPUtils.getSpValues(SPKeyValuesUtils.SP_USER_ID, SPUtils.TYPE_STRING));
        map.put("old_password",mOldPassWord);
        map.put("new_password",mNewPassWord);
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
                }else {
                    ToastUtil.showShortToast(result.getMessage());
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
