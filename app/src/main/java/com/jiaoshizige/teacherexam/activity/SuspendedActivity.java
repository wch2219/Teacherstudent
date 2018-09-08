package com.jiaoshizige.teacherexam.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jiaoshizige.teacherexam.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/7/25.
 */

public class SuspendedActivity extends AppCompatActivity implements View.OnClickListener{
    @BindView(R.id.name)
    TextView mTitle;
    @BindView(R.id.continue_to)
    LinearLayout mContinue;
    @BindView(R.id.replay)
    LinearLayout mReplay;
    @BindView(R.id.exit)
    LinearLayout mExit;

    private String name;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.suspended_layout);
        ButterKnife.bind(this);
        if (getIntent().getStringExtra("name") != null) {
            name = getIntent().getStringExtra("name");
        } else {
            name = "";
        }
        if (!name.equals("")) {
            mTitle.setText(name);
        }

        mContinue.setOnClickListener(this);
        mReplay.setOnClickListener(this);
        mExit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = getIntent();
        switch (v.getId()){
            case R.id.continue_to:
                String msg1 = intent.getStringExtra("name");
                //返回到MainActivity类
                setResult(3);
                finish();
                break;
            case R.id.replay:

                // 接收从MainActivity类传递过来的信息msg
                String msg = intent.getStringExtra("name");
                //返回到MainActivity类
                setResult(1);
                finish();
                break;
            case R.id.exit:
                // 接收从MainActivity类传递过来的信息msg
                String message = intent.getStringExtra("name");
                //返回到MainActivity类
                setResult(2);
                finish();
                break;
                default:
                    break;

        }
    }
}
