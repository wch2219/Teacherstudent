package com.jiaoshizige.teacherexam.activity;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jiaoshizige.teacherexam.R;
import com.jiaoshizige.teacherexam.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/7/28 0028.
 */

public class FindPassBActivity extends BaseActivity {

    @BindView(R.id.tiltle_bar)
    TextView tiltle;
    @BindView(R.id.registerb_user_pass)
    EditText registerb_find_pass;
    @BindView(R.id.registerb_find_pass)
    TextView findpass;
    @BindView(R.id.trgisterb_login_icon_eye)
    ImageView login_icon_eye;
    @BindView(R.id.registerb_login_icon_del)
    ImageView login_icon_del;
    @BindView(R.id.registerb_next)
    Button registerb_next;

    @Override
    protected int getLayoutId() {
        return R.layout.regresterb;
    }


    @Override
    protected void initView() {
        tiltle.setText("找回密码");

        registerb_find_pass.addTextChangedListener(mTextWatcher);
    }

    @OnClick({R.id.registerb_next, R.id.leftbar_return})
    public void btOnClick(View view) {

        switch (view.getId()) {
            case R.id.registerb_next:
                Toast.makeText(FindPassBActivity.this, "完成", Toast.LENGTH_SHORT).show();
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
            if (temp.length()==0){
                login_icon_eye.setVisibility(View.GONE);
                login_icon_del.setVisibility(View.GONE);
            }
            else {
                login_icon_eye.setVisibility(View.VISIBLE);
                login_icon_del.setVisibility(View.VISIBLE);
            }

        }
    };
}