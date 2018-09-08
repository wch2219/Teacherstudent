package com.jiaoshizige.teacherexam.activity;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

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
import cn.jiguang.g.c.a;

/**
 * Created by Administrator on 2018/7/26.
 */

public class ErrorCorrectActivity extends BaseActivity {
    @BindView(R.id.suggest)
    EditText mSuggest;
    @BindView(R.id.button)
    TextView mButton;

    private String id;
    private String user_id;
    private String content;
    private Context mContext;

    @Override
    protected int getLayoutId() {
        return R.layout.error_correct_layout;
    }

    @Override
    protected void initView() {

        setToolbarTitle().setText("纠错");
        if (getIntent().getStringExtra("id") != null) {
            id = getIntent().getStringExtra("id");
        } else {
            id = "";
        }
        mContext = this;
        user_id = (String) SPUtils.getSpValues(SPKeyValuesUtils.SP_USER_ID, SPUtils.TYPE_STRING);
        mSuggest.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String str = mSuggest.getText().toString();
                if (str.length() > 0) {
                    mButton.setBackgroundResource(R.drawable.purple_shape_30);
                } else {
                    mButton.setBackgroundResource(R.drawable.error_correct);
                }
            }
        });
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                content = mSuggest.getText().toString().trim();
                if (!id.equals("") && !content.equals("") && content != null) {
                    getError();
                }
            }
        });
    }

    private void getError() {
        Map<String, String> map = new HashMap<>();
        map.put("user_id", user_id);
        map.put("shiti_id", id);
        map.put("miaoshu", content);
        GloableConstant.getInstance().startProgressDialog(mContext);
        Xutil.GET(ApiUrl.ERRORCORRECT, map, new MyCallBack<SupportResponse>() {
            @Override
            public void onSuccess(SupportResponse result) {
                super.onSuccess(result);
                GloableConstant.getInstance().stopProgressDialog();
                if (result.getStatus_code().equals("204")) {
                    ToastUtil.showShortToast("提交成功");
                    finish();
                }
            }

            @Override
            public void onFinished() {
                super.onFinished();
                GloableConstant.getInstance().stopProgressDialog();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
                GloableConstant.getInstance().stopProgressDialog();
                Log.d("--------------", ex.getMessage() + "--------------");
            }
        });
    }
}
