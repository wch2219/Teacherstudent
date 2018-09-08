package com.jiaoshizige.teacherexam.activity;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.jiaoshizige.teacherexam.R;
import com.jiaoshizige.teacherexam.base.BaseActivity;
import com.jiaoshizige.teacherexam.base.CommRecyclerViewAdapter;
import com.jiaoshizige.teacherexam.base.ViewHolderZhy;
import com.jiaoshizige.teacherexam.http.SupportResponse;
import com.jiaoshizige.teacherexam.model.PracticeRecordsResponse;
import com.jiaoshizige.teacherexam.model.RecordDetailResponse;
import com.jiaoshizige.teacherexam.model.TestResultDetailResponse;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/7/27.
 */

public class SheetActivity extends BaseActivity {
    @BindView(R.id.name)
    TextView mName;
    @BindView(R.id.recycle_view)
    RecyclerView mRecycleView;

    private List<PracticeRecordsResponse.mData> mData;//练习记录答题卡
    private List<RecordDetailResponse.mData> mDataList;//模拟记录答题卡
    private List<TestResultDetailResponse.mData> mTestList;//模拟测试结果答题卡
    private String name;
    private Context mContext;
    private String flag;

    @Override
    protected int getLayoutId() {
        return R.layout.sheet_layout;
    }

    @Override
    protected void initView() {
        setSubTitle().setText("");
        setToolbarTitle().setText("答题卡");
        mContext = this;
        if (getIntent().getStringExtra("name") != null) {
            name = getIntent().getStringExtra("name");
        } else {
            name = "";
        }
        if (getIntent().getStringExtra("flag") != null) {
            flag = getIntent().getStringExtra("flag");
        } else {
            flag = "";
        }
        if (flag.equals("1")) {
            mDataList = (List<RecordDetailResponse.mData>) getIntent().getSerializableExtra("data");
            if (mDataList.size() > 0) {
                GridLayoutManager mgr = new GridLayoutManager(mContext, 5);
                mRecycleView.setLayoutManager(mgr);
                mRecycleView.setAdapter(new CommRecyclerViewAdapter<RecordDetailResponse.mData>(mContext, R.layout.item_sheet_layout, mDataList) {

                    @Override
                    protected void convert(ViewHolderZhy holder, RecordDetailResponse.mData item, final int position) {

                        holder.setText(R.id.topic_number, String.valueOf(position + 1));

                        if (item.getUser_answer() == null && item.getUser_answer().equals("")) {
                            holder.setBackgroundRes(R.id.topic_number, R.mipmap.lianxi_img_default);
                        } else {
                            if (item.getUser_answer().equals(item.getAnswer())) {
                                holder.setBackgroundRes(R.id.topic_number, R.mipmap.lianxi_img_ture);
                            } else {
                                holder.setBackgroundRes(R.id.topic_number, R.mipmap.lianxi_img_false);
                            }
                        }
                        holder.setOnClickListener(R.id.topic_number, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent();
                                //把返回数据存入Intent
                                intent.putExtra("result", position + "");
                                //设置返回数据
                                SheetActivity.this.setResult(RESULT_OK, intent);
                                //关闭Activity
                                SheetActivity.this.finish();
                            }
                        });


                    }
                });
            }
        } else if (flag.equals("2")) {
            mTestList = (List<TestResultDetailResponse.mData>) getIntent().getSerializableExtra("data");
            if (mTestList.size() > 0) {
                GridLayoutManager mgr = new GridLayoutManager(mContext, 5);
                mRecycleView.setLayoutManager(mgr);
                mRecycleView.setAdapter(new CommRecyclerViewAdapter<TestResultDetailResponse.mData>(mContext, R.layout.item_sheet_layout, mTestList) {

                    @Override
                    protected void convert(ViewHolderZhy holder, TestResultDetailResponse.mData item, final int position) {

                        holder.setText(R.id.topic_number, String.valueOf(position + 1));

                        if (item.getUser_answer() == null && item.getUser_answer().equals("")) {
                            holder.setBackgroundRes(R.id.topic_number, R.mipmap.lianxi_img_default);
                        } else {
                            if (item.getUser_answer().equals(item.getAnswer())) {
                                holder.setBackgroundRes(R.id.topic_number, R.mipmap.lianxi_img_ture);
                            } else {
                                holder.setBackgroundRes(R.id.topic_number, R.mipmap.lianxi_img_false);
                            }
                        }
                        holder.setOnClickListener(R.id.topic_number, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent();
                                //把返回数据存入Intent
                                intent.putExtra("result", position + "");
                                //设置返回数据
                                SheetActivity.this.setResult(RESULT_OK, intent);
                                //关闭Activity
                                SheetActivity.this.finish();
                            }
                        });


                    }
                });
            }
        } else {
            mData = (List<PracticeRecordsResponse.mData>) getIntent().getSerializableExtra("data");
            if (mData.size() > 0) {
                GridLayoutManager mgr = new GridLayoutManager(mContext, 5);
                mRecycleView.setLayoutManager(mgr);
                mRecycleView.setAdapter(new CommRecyclerViewAdapter<PracticeRecordsResponse.mData>(mContext, R.layout.item_sheet_layout, mData) {

                    @Override
                    protected void convert(ViewHolderZhy holder, PracticeRecordsResponse.mData item, final int position) {

                        holder.setText(R.id.topic_number, String.valueOf(position + 1));

                        if (item.getUser_answer() == null && item.getUser_answer().equals("")) {
                            holder.setBackgroundRes(R.id.topic_number, R.mipmap.lianxi_img_default);
                        } else {
                            if (item.getUser_answer().equals(item.getAnswer())) {
                                holder.setBackgroundRes(R.id.topic_number, R.mipmap.lianxi_img_ture);
                            } else {
                                holder.setBackgroundRes(R.id.topic_number, R.mipmap.lianxi_img_false);
                            }
                        }
                        holder.setOnClickListener(R.id.topic_number, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent();
                                //把返回数据存入Intent
                                intent.putExtra("result", position + "");
                                //设置返回数据
                                SheetActivity.this.setResult(RESULT_OK, intent);
                                //关闭Activity
                                SheetActivity.this.finish();
                            }
                        });


                    }
                });
            }
        }

        if (!name.equals("")) {
            mName.setText(name);
        }

    }
}
