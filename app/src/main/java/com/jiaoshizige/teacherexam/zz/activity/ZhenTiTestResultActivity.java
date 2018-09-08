package com.jiaoshizige.teacherexam.zz.activity;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.jiaoshizige.teacherexam.R;
import com.jiaoshizige.teacherexam.activity.TestResultDetailActivity;
import com.jiaoshizige.teacherexam.base.BaseActivity;
import com.jiaoshizige.teacherexam.model.TestResultResponse;
import com.jiaoshizige.teacherexam.utils.NoScrollListView;
import com.jiaoshizige.teacherexam.zz.utils.NetworkUtils;
import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/8/9.
 */

public class ZhenTiTestResultActivity extends BaseActivity {
    @BindView(R.id.title)
    TextView mTitle;
    @BindView(R.id.total_score)
    TextView mTotalScore;
    @BindView(R.id.user_score)
    TextView mUserScore;
    @BindView(R.id.time)
    TextView mTime;
    @BindView(R.id.objective_score)
    TextView mObject;
    @BindView(R.id.subjective_score)
    TextView mSubject;
    @BindView(R.id.listView)
    NoScrollListView mListView;
    @BindView(R.id.prasing)
    TextView mPrasing;


    private Context mContext;
    private TestResultResponse.mData mData;

    @Override
    protected int getLayoutId() {
        return R.layout.test_result_layout;
    }

    @Override
    protected void initView() {
        mContext = this;
        setSubTitle().setText("");
        setToolbarTitle().setText("练习结果");
        if (getIntent().getExtras().get("data") != null) {
            mData = (TestResultResponse.mData) getIntent().getExtras().get("data");
            Log.d("*********mData", mData.getFenshu().get(0).getDefen() + "-----------");
            mTitle.setText("练习类型: " + mData.getName());
            mTotalScore.setText("总分: " + mData.getPaper_totle());
            mUserScore.setText(mData.getMoni_totle());
            mTime.setText("用时: " + mData.getTime());
            mObject.setText(mData.getKeguan() + "分");
            mSubject.setText(mData.getZhuguan() + "分");
            mListView.setAdapter(new CommonAdapter<TestResultResponse.mFenShu>(mContext, R.layout.item_test_result_layout, mData.getFenshu()) {
                @Override
                protected void convert(ViewHolder viewHolder, TestResultResponse.mFenShu item, int position) {
                    viewHolder.setText(R.id.topic, item.getTixing());
                    viewHolder.setText(R.id.score, item.getDefen());
                    viewHolder.setText(R.id.score_average, item.getDefenlv());
                }
            });
            mPrasing.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    if (NetworkUtils.isNetworkConnected(ZhenTiTestResultActivity.this)) {
                        intent.setClass(ZhenTiTestResultActivity.this, TestResultDetailActivity.class);
                    } else {
                        intent.setClass(ZhenTiTestResultActivity.this, TestResultDetail_NonetWorkActivity.class);
                    }
                    intent.putExtra("id", mData.getId());
                    intent.putExtra("name", mData.getName());
                    intent.putExtra("is_zhenti", "zhenti");
                    startActivity(intent);

                }
            });
        }

    }
}
