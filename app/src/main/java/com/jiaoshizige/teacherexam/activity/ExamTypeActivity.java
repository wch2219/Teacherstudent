package com.jiaoshizige.teacherexam.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.jiaoshizige.teacherexam.R;
import com.jiaoshizige.teacherexam.adapter.ExamTypeAdapter;
import com.jiaoshizige.teacherexam.base.BaseActivity;
import com.jiaoshizige.teacherexam.base.GloableConstant;
import com.jiaoshizige.teacherexam.http.ApiUrl;
import com.jiaoshizige.teacherexam.http.MyCallBack;
import com.jiaoshizige.teacherexam.http.Xutil;
import com.jiaoshizige.teacherexam.model.ExamTypeResponse;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/8/1.
 * 选择考试类型
 */

public class ExamTypeActivity extends BaseActivity {
    //    @BindView(R.id.child)
//    RadioButton mChildType;
//    @BindView(R.id.primary_sc)
//    RadioButton mPrimarySchoolType;
//    @BindView(R.id.junior_middle_sc)
//    RadioButton mJuniorMiddleSchoolType;
//    @BindView(R.id.typerg)
//    RadioGroup mTypeRg;
//    private String mType;
    @BindView(R.id.lv_list)
    ListView mListView;
    private ExamTypeAdapter mExamTypeAdapter;
    private Context mContext;
    private String type_name,type_id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        setToolbarTitle().setText("选择考试类型");
        setSubTitle().setText("");
    }

    @Override
    protected int getLayoutId() {
        return R.layout.exam_type_layout;
    }


    @Override
    protected void initView() {
        examType();
        mContext = this;
        mExamTypeAdapter = new ExamTypeAdapter(mContext);
        mListView.setAdapter(mExamTypeAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ExamTypeResponse.mData mData= (ExamTypeResponse.mData) parent.getAdapter().getItem(position);
                type_id=mData.getId();
                type_name=mData.getName();
                Log.e("*********type",type_id+"*******"+type_name);
                Intent mIntent = new Intent();
//                mIntent.putExtra("typeText", mType);
                mIntent.putExtra("type_id",type_id);
                mIntent.putExtra("type_name",type_name);
                setResult(RESULT_OK, mIntent);
//                finish();
            }
        });
    }

    private void examType() {
        Map<String, String> map = new HashMap<>();
        GloableConstant.getInstance().startProgressDialog(this);
        Xutil.GET(ApiUrl.EXAMTYPE, map, new MyCallBack<ExamTypeResponse>() {
            @Override
            public void onSuccess(ExamTypeResponse result) {
                super.onSuccess(result);
                GloableConstant.getInstance().stopProgressDialog1();
                if (result.getStatus_code().equals("200")) {
                    if (result.getData().size()>0){
                        mExamTypeAdapter.setmList(result.getData());
                    }

                }
            }
        });
    }



}
