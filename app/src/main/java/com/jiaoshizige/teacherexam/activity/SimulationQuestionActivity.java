package com.jiaoshizige.teacherexam.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.jiaoshizige.teacherexam.R;
import com.jiaoshizige.teacherexam.base.BaseActivity;
import com.jiaoshizige.teacherexam.base.GloableConstant;
import com.jiaoshizige.teacherexam.http.ApiUrl;
import com.jiaoshizige.teacherexam.http.MyCallBack;
import com.jiaoshizige.teacherexam.http.Xutil;
import com.jiaoshizige.teacherexam.model.SimulationResponse;
import com.jiaoshizige.teacherexam.utils.SPKeyValuesUtils;
import com.jiaoshizige.teacherexam.utils.SPUtils;

import java.util.HashMap;
import java.util.Map;

import javax.microedition.khronos.opengles.GL;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/8/3.
 */

public class SimulationQuestionActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.name)
    TextView mName;
    @BindView(R.id.content)
    TextView mContent;
    @BindView(R.id.number)
    TextView mNum;
    @BindView(R.id.score)
    TextView mScore;
    @BindView(R.id.avar)
    TextView mAvar;
    @BindView(R.id.record)
    TextView mRecord;
    @BindView(R.id.start)
    TextView mStart;


    private Context mContext;
    private String user_id;
    private String tiku_id;
    private Intent intent;
    private String name;

    @Override
    protected int getLayoutId() {
        return R.layout.simulation_question_layout;
    }

    @Override
    protected void initView() {
        setToolbarTitle().setText("");
        setSubTitle().setText("");
        mContext = this;
        intent = new Intent();
        user_id = (String) SPUtils.getSpValues(SPKeyValuesUtils.SP_USER_ID, SPUtils.TYPE_STRING);
        if (GloableConstant.getInstance().getTiku_id() != null) {
            tiku_id = GloableConstant.getInstance().getTiku_id();
        } else {
            tiku_id = "";
        }

        if (getIntent().getStringExtra("name")!=null){
            name=getIntent().getStringExtra("name");
        }else {
            name="";
        }
        mRecord.setOnClickListener(this);
        mStart.setOnClickListener(this);
        if (!tiku_id.equals("")) {
            getSimulation();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.record:
                intent.setClass(SimulationQuestionActivity.this, ModelTestRecordActivity.class);
                startActivity(intent);
                break;
            case R.id.start:
                if (!name.equals("")){
                    intent.setClass(SimulationQuestionActivity.this,StartPracticaActivity.class);
                    intent.putExtra("name",name);
                    startActivity(intent);
                }

                break;
        }
    }

    private void getSimulation() {
        Map<String, String> map = new HashMap<>();
        map.put("user_id", user_id);
        map.put("tiku_id", tiku_id);
        GloableConstant.getInstance().startProgressDialog(mContext);
        Xutil.GET(ApiUrl.SIMULATION, map, new MyCallBack<SimulationResponse>() {
            @Override
            public void onSuccess(SimulationResponse result) {
                super.onSuccess(result);
                GloableConstant.getInstance().stopProgressDialog();
                if (result.getStatus_code().equals("200")) {
                    mName.setText(result.getData().getName());
                    mContent.setText(result.getData().getSmalltext());
                    mNum.setText(result.getData().getMoni_num());
                    mScore.setText(result.getData().getMoni_score());
                    mAvar.setText(result.getData().getMoni_ava());
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
            }
        });
    }


}
