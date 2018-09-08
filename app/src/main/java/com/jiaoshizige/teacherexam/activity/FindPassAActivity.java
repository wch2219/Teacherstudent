package com.jiaoshizige.teacherexam.activity;

import android.text.Editable;
import android.text.TextWatcher;

import com.jiaoshizige.teacherexam.R;
import com.jiaoshizige.teacherexam.base.BaseActivity;

/**
 * Created by Administrator on 2017/7/28 0028.
 */

public class FindPassAActivity extends BaseActivity {

//    @BindView(R.id.tiltle_bar)
//    TextView tiltle;
//    @BindView(R.id.register_next)
//    Button register_next;
//    @BindView(R.id.register_user_pass)
//    EditText register_user_pass;

    @Override
    protected int getLayoutId() {
        return R.layout.regrester_activity;
    }


    @Override
    protected void initView() {
//        tiltle.setText("找回密码");
//        register_user_pass.addTextChangedListener(mTextWatcher);
    }

//    @OnClick({R.id.register_next,R.id.leftbar_return})
//    public void OnClick(View view){
//        switch (view.getId()){
//            case R.id.register_next:
//                startActivity(new Intent(FindPassAActivity.this,FindPassBActivity.class));
//                break;
//            case R.id.leftbar_return:
//                finish();
//                break;
//        }

//    }
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
//            register_next.setBackgroundResource(R.drawable.purple_shape);


        }
    };
}
