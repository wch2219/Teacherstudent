package com.jiaoshizige.teacherexam.activity;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.jiaoshizige.teacherexam.R;
import com.jiaoshizige.teacherexam.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/7/28 0028.
 * (废弃)
 */

public class RegresterB extends BaseActivity {


    @BindView(R.id.registerb_next)
    Button registerb_next;
    @BindView(R.id.registerb_user_pass)
    EditText registerb_user_pass;
    protected int getLayoutId() {
        return R.layout.regresterb;
    }

    @Override
    protected void initView() {
        setSubTitle().setText("");
        setToolbarTitle().setText("注册");
        registerb_user_pass.addTextChangedListener(mTextWatcher);

    }

    @OnClick({R.id.registerb_next,R.id.leftbar_return})
    public void btOnClick(View view){

        switch (view.getId()){
            case R.id.registerb_next:
                Toast.makeText(RegresterB.this,"完成",Toast.LENGTH_SHORT).show();
                break;
            case R.id.leftbar_return:
                finish();
                break;
        }

    }
    TextWatcher mTextWatcher=new TextWatcher() {

        private CharSequence temp;


        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            temp = s;
        }

        @Override
        public void afterTextChanged(Editable s) {
            registerb_next.setBackgroundResource(R.drawable.purple_shape);


        }
    };
}
