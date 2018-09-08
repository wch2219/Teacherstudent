package com.jiaoshizige.teacherexam.activity;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.jiaoshizige.teacherexam.R;
import com.jiaoshizige.teacherexam.base.BaseActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/a1/8.
 * 测评完成
 */

public class ExamCompleteActivity extends BaseActivity {
    @BindView(R.id.flag_img)
    ImageView mFalgImg;
    @BindView(R.id.phrase)
    TextView mPhrase;
    @BindView(R.id.complete)
    Button mComplete;
    @BindView(R.id.retest)
    Button mRestart;

    private ArrayList<String> mList = new ArrayList<String>();
    private ArrayList<String> list = new ArrayList<>();
    private Intent intent;

    @Override
    protected int getLayoutId() {
        return R.layout.exam_complete_activity;
    }


    @Override
    protected void initView() {
        setToolbarTitle().setText("学前向导测评");
        setSubTitle().setText("");

        mList = getIntent().getStringArrayListExtra("mList");
        if (mList.size() > 0) {
//            mList=getIntent().getStringArrayListExtra("mList");
            for (int i = 0; i < mList.size(); i++) {
                list.add(mList.get(i));
            }
        }


    }

    @OnClick({R.id.complete, R.id.retest})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.complete:
               if (list.size()>0){
                   intent = new Intent();
                   intent.setClass(this, EvaluationReport.class);
                   intent.putStringArrayListExtra("list", list);
                   startActivity(intent);
                   finish();
               }
                break;
            case R.id.retest:
                Intent intent = new Intent();
                intent.putExtra("tag", "0");
                intent.setClass(ExamCompleteActivity.this, NewGiftPackActivity.class);
                startActivity(intent);
//                Sphelper.save(ExamCompleteActivity.this,"hadexam","isexam",false);
                finish();
                break;
        }
    }
}
