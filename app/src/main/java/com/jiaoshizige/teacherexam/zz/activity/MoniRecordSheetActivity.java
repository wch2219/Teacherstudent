package com.jiaoshizige.teacherexam.zz.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.jiaoshizige.teacherexam.R;
import com.jiaoshizige.teacherexam.base.BaseActivity;
import com.jiaoshizige.teacherexam.base.CommRecyclerViewAdapter;
import com.jiaoshizige.teacherexam.base.GloableConstant;
import com.jiaoshizige.teacherexam.base.ViewHolderZhy;
import com.jiaoshizige.teacherexam.http.ApiUrl;
import com.jiaoshizige.teacherexam.http.MyCallBack;
import com.jiaoshizige.teacherexam.http.Xutil;
import com.jiaoshizige.teacherexam.model.RecordDetailResponse;
import com.jiaoshizige.teacherexam.model.TestResultDetailResponse;
import com.jiaoshizige.teacherexam.model.TestResultResponse;
import com.jiaoshizige.teacherexam.utils.SPKeyValuesUtils;
import com.jiaoshizige.teacherexam.utils.SPUtils;
import com.jiaoshizige.teacherexam.utils.TimeUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/8/8.
 */

public class MoniRecordSheetActivity extends BaseActivity {
    @BindView(R.id.single_topic_text1)
    TextView mSingleText1;
    @BindView(R.id.single_topic_recycleview1)
    RecyclerView mSingleRecycleView1;
    @BindView(R.id.view1)
    View view1;
    @BindView(R.id.short_answer_text2)
    TextView mShortText2;
    @BindView(R.id.short_answer_recycle2)
    RecyclerView mShortRecycleView2;
    @BindView(R.id.view2)
    View view2;
    @BindView(R.id.analysis_topic_text3)
    TextView mAnalysisText3;
    @BindView(R.id.analysis_topic_recycle3)
    RecyclerView mAnalysisRecycleView3;
    @BindView(R.id.view3)
    View view3;
    @BindView(R.id.essay_topic_text4)
    TextView mEssayText4;
    @BindView(R.id.essay_topic_recycleview4)
    RecyclerView mEssayRecycleView4;
    @BindView(R.id.view4)
    View view4;
    @BindView(R.id.write_topic_text5)
    TextView mWriteText5;
    @BindView(R.id.write_topic_recycleview5)
    RecyclerView mWriteRecycleView5;
    @BindView(R.id.view5)
    View view5;
    @BindView(R.id.material_topic_text6)
    TextView mMaterialText6;
    @BindView(R.id.material_topic_recycleview6)
    RecyclerView mMaterialRecycl6;
    @BindView(R.id.view6)
    View view6;
    @BindView(R.id.teacher_topic_text8)
    TextView mTeachText8;
    @BindView(R.id.teacher_topic_recycleview8)
    RecyclerView mTeachRecycleView8;
    @BindView(R.id.view8)
    View view8;
    @BindView(R.id.activity_topic_text9)
    TextView mActivityText9;
    @BindView(R.id.activity_topic_recycleview9)
    RecyclerView mActivityRecycleView9;
    @BindView(R.id.result)
    TextView mResult;


    private List<RecordDetailResponse.mData> mDataList;
    private List<RecordDetailResponse.mData> list1;
    private List<RecordDetailResponse.mData> list2;
    private List<RecordDetailResponse.mData> list3;
    private List<RecordDetailResponse.mData> list4;
    private List<RecordDetailResponse.mData> list5;
    private List<RecordDetailResponse.mData> list6;
    private List<RecordDetailResponse.mData> list8;
    private List<RecordDetailResponse.mData> list9;
    private Context mContext;
    private String time;
    private String user_id;
    private String tiku_id;
    private String zhenti_id;
    LinkedHashMap<String, String> answer = new LinkedHashMap<>();
    String json;
    private String flag;

    @Override
    protected int getLayoutId() {
        return R.layout.moni_sheet_layout;
    }

    @Override
    protected void initView() {
        setToolbarTitle().setText("答题卡");
        mResult.setVisibility(View.GONE);

        mContext = this;
        list1 = new ArrayList<>();
        list2 = new ArrayList<>();
        list3 = new ArrayList<>();
        list4 = new ArrayList<>();
        list5 = new ArrayList<>();
        list6 = new ArrayList<>();
        list8 = new ArrayList<>();
        list9 = new ArrayList<>();
        setSubTitle().setBackgroundResource(R.mipmap.home_btn_close_pressed);
        setSubTitle().setText("");
        setSubTitle().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        if (getIntent().getStringExtra("hour") != null) {
            time = getIntent().getStringExtra("hour");
//            Long aa=Long.valueOf(getIntent().getStringExtra("hour"));
//            Log.d("************aa----", TimeUtils.formatTimes(aa)+"------------");
        } else {
            time = "";
        }

        if (getIntent().getStringExtra("zhenti_id") != null) {
            zhenti_id = getIntent().getStringExtra("zhenti_id");
        } else {
            zhenti_id = "";
        }

        if (GloableConstant.getInstance().getTiku_id() != null) {
            tiku_id = GloableConstant.getInstance().getTiku_id();
        } else {
            tiku_id = "";
        }

        mDataList = (List<RecordDetailResponse.mData>) getIntent().getSerializableExtra("data");
        user_id = (String) SPUtils.getSpValues(SPKeyValuesUtils.SP_USER_ID, SPUtils.TYPE_STRING);
        //Log.d("/////////useranswer", mDataList.get(0).getUser_answer() + "--------------");
        if (mDataList.size() > 0 && mDataList != null) {

            try {
                for (int i = 0; i < mDataList.size(); i++) {
                    String qid = String.valueOf(i + 1);
                    mDataList.get(i).setQid(qid);
                    if (mDataList.get(i).getType().equals("1")) {
                        list1.add(mDataList.get(i));
                    }
                    if (mDataList.get(i).getType().equals("2")) {
                        list2.add(mDataList.get(i));
                    }
                    if (mDataList.get(i).getType().equals("3")) {
                        list3.add(mDataList.get(i));
                    }
                    if (mDataList.get(i).getType().equals("4")) {
                        list4.add(mDataList.get(i));
                    }
                    if (mDataList.get(i).getType().equals("5")) {
                        list5.add(mDataList.get(i));
                    }
                    if (mDataList.get(i).getType().equals("6")) {
                        list6.add(mDataList.get(i));
                    }
                    if (mDataList.get(i).getType().equals("8")) {
                        list8.add(mDataList.get(i));
                    }
                    if (mDataList.get(i).getType().equals("9")) {
                        list9.add(mDataList.get(i));
                    }
                }
            } catch (Exception e) {
                String str = e.toString();
            }

            if (list1.size() > 0) {
                mSingleText1.setVisibility(View.VISIBLE);
                mSingleRecycleView1.setVisibility(View.VISIBLE);
                view1.setVisibility(View.VISIBLE);
                GridLayoutManager mgr = new GridLayoutManager(mContext, 5);
                mSingleRecycleView1.setLayoutManager(mgr);
                mSingleRecycleView1.setAdapter(new CommRecyclerViewAdapter<RecordDetailResponse.mData>(mContext, R.layout.item_sheet_layout, list1) {

                    @Override
                    protected void convert(ViewHolderZhy holder, RecordDetailResponse.mData item, final int position) {
                        holder.setText(R.id.topic_number, item.getQid());
                        if (TextUtils.isEmpty(item.getUser_answer())) {
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
                                intent.putExtra("result", String.valueOf(Integer.valueOf(list1.get(position).getQid()) - 1));
                                //设置返回数据
                                MoniRecordSheetActivity.this.setResult(RESULT_OK, intent);
                                //关闭Activity
                                MoniRecordSheetActivity.this.finish();
                            }
                        });
                    }
                });
            } else {
                mSingleText1.setVisibility(View.GONE);
                mSingleRecycleView1.setVisibility(View.GONE);
                view1.setVisibility(View.GONE);
            }
            if (list2.size() > 0) {
                mShortText2.setVisibility(View.VISIBLE);
                mShortRecycleView2.setVisibility(View.VISIBLE);
                view2.setVisibility(View.VISIBLE);
                GridLayoutManager mgr = new GridLayoutManager(mContext, 5);
                mShortRecycleView2.setLayoutManager(mgr);
                mShortRecycleView2.setAdapter(new CommRecyclerViewAdapter<RecordDetailResponse.mData>(mContext, R.layout.item_sheet_layout, list2) {

                    @Override
                    protected void convert(ViewHolderZhy holder, RecordDetailResponse.mData item, final int position) {
                        holder.setText(R.id.topic_number, item.getQid());
                        if (TextUtils.isEmpty(item.getUser_answer())) {
                            holder.setBackgroundRes(R.id.topic_number, R.mipmap.lianxi_img_default);
                        } else {
                            if (Integer.parseInt(item.getUser_answer()) > 0) {
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
                                intent.putExtra("result", String.valueOf(Integer.valueOf(list2.get(position).getQid()) - 1));
                                //设置返回数据
                                MoniRecordSheetActivity.this.setResult(RESULT_OK, intent);
                                //关闭Activity
                                MoniRecordSheetActivity.this.finish();
                            }
                        });
                    }
                });
            } else {
                mShortText2.setVisibility(View.GONE);
                mShortRecycleView2.setVisibility(View.GONE);
                view2.setVisibility(View.GONE);
            }

            if (list3.size() > 0) {
                mAnalysisText3.setVisibility(View.VISIBLE);
                mAnalysisRecycleView3.setVisibility(View.VISIBLE);
                view3.setVisibility(View.VISIBLE);
                GridLayoutManager mgr = new GridLayoutManager(mContext, 5);
                mAnalysisRecycleView3.setLayoutManager(mgr);
                mAnalysisRecycleView3.setAdapter(new CommRecyclerViewAdapter<RecordDetailResponse.mData>(mContext, R.layout.item_sheet_layout, list3) {

                    @Override
                    protected void convert(ViewHolderZhy holder, RecordDetailResponse.mData item, final int position) {
                        holder.setText(R.id.topic_number, item.getQid());
                        if (TextUtils.isEmpty(item.getUser_answer())) {
                            holder.setBackgroundRes(R.id.topic_number, R.mipmap.lianxi_img_default);
                        } else {
                            if (Integer.parseInt(item.getUser_answer()) > 0) {
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
                                intent.putExtra("result", String.valueOf(Integer.valueOf(list3.get(position).getQid()) - 1));
                                //设置返回数据
                                MoniRecordSheetActivity.this.setResult(RESULT_OK, intent);
                                //关闭Activity
                                MoniRecordSheetActivity.this.finish();
                            }
                        });
                    }
                });
            } else {
                mAnalysisText3.setVisibility(View.GONE);
                mAnalysisRecycleView3.setVisibility(View.GONE);
                view3.setVisibility(View.GONE);
            }
            if (list4.size() > 0) {
                mEssayText4.setVisibility(View.VISIBLE);
                mEssayRecycleView4.setVisibility(View.VISIBLE);
                view4.setVisibility(View.VISIBLE);
                GridLayoutManager mgr = new GridLayoutManager(mContext, 5);
                mEssayRecycleView4.setLayoutManager(mgr);
                mEssayRecycleView4.setAdapter(new CommRecyclerViewAdapter<RecordDetailResponse.mData>(mContext, R.layout.item_sheet_layout, list4) {

                    @Override
                    protected void convert(ViewHolderZhy holder, RecordDetailResponse.mData item, final int position) {
                        holder.setText(R.id.topic_number, item.getQid());
                        if (TextUtils.isEmpty(item.getUser_answer())) {
                            holder.setBackgroundRes(R.id.topic_number, R.mipmap.lianxi_img_default);
                        } else {
                            if (Integer.parseInt(item.getUser_answer()) > 0) {
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
                                intent.putExtra("result", String.valueOf(Integer.valueOf(list4.get(position).getQid()) - 1));
                                //设置返回数据
                                MoniRecordSheetActivity.this.setResult(RESULT_OK, intent);
                                //关闭Activity
                                MoniRecordSheetActivity.this.finish();
                            }
                        });
                    }
                });
            } else {
                mEssayText4.setVisibility(View.GONE);
                mEssayRecycleView4.setVisibility(View.GONE);
                view4.setVisibility(View.GONE);
            }
            if (list5.size() > 0) {
                mWriteText5.setVisibility(View.VISIBLE);
                mWriteRecycleView5.setVisibility(View.VISIBLE);
                view5.setVisibility(View.VISIBLE);
                GridLayoutManager mgr = new GridLayoutManager(mContext, 5);
                mWriteRecycleView5.setLayoutManager(mgr);
                mWriteRecycleView5.setAdapter(new CommRecyclerViewAdapter<RecordDetailResponse.mData>(mContext, R.layout.item_sheet_layout, list5) {

                    @Override
                    protected void convert(ViewHolderZhy holder, RecordDetailResponse.mData item, final int position) {
                        holder.setText(R.id.topic_number, item.getQid());
                        if (TextUtils.isEmpty(item.getUser_answer())) {
                            holder.setBackgroundRes(R.id.topic_number, R.mipmap.lianxi_img_default);
                        } else {
                            if (Integer.parseInt(item.getUser_answer()) > 0) {
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
                                intent.putExtra("result", String.valueOf(Integer.valueOf(list5.get(position).getQid()) - 1));
                                //设置返回数据
                                MoniRecordSheetActivity.this.setResult(RESULT_OK, intent);
                                //关闭Activity
                                MoniRecordSheetActivity.this.finish();
                            }
                        });
                    }
                });
            } else {
                mWriteText5.setVisibility(View.GONE);
                mWriteRecycleView5.setVisibility(View.GONE);
                view5.setVisibility(View.GONE);
            }
            if (list6.size() > 0) {
                mMaterialText6.setVisibility(View.VISIBLE);
                mMaterialRecycl6.setVisibility(View.VISIBLE);
                view6.setVisibility(View.VISIBLE);
                GridLayoutManager mgr = new GridLayoutManager(mContext, 5);
                mMaterialRecycl6.setLayoutManager(mgr);
                mMaterialRecycl6.setAdapter(new CommRecyclerViewAdapter<RecordDetailResponse.mData>(mContext, R.layout.item_sheet_layout, list6) {

                    @Override
                    protected void convert(ViewHolderZhy holder, RecordDetailResponse.mData item, final int position) {
                        holder.setText(R.id.topic_number, item.getQid());
                        if (TextUtils.isEmpty(item.getUser_answer())) {
                            holder.setBackgroundRes(R.id.topic_number, R.mipmap.lianxi_img_default);
                        } else {
                            if (Integer.parseInt(item.getUser_answer()) > 0) {
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
                                intent.putExtra("result", String.valueOf(Integer.valueOf(list6.get(position).getQid()) - 1));
                                //设置返回数据
                                MoniRecordSheetActivity.this.setResult(RESULT_OK, intent);
                                //关闭Activity
                                MoniRecordSheetActivity.this.finish();
                            }
                        });
                    }
                });
            } else {
                mMaterialText6.setVisibility(View.GONE);
                mMaterialRecycl6.setVisibility(View.GONE);
                view6.setVisibility(View.GONE);
            }
            if (list8.size() > 0) {
                mTeachText8.setVisibility(View.VISIBLE);
                mTeachRecycleView8.setVisibility(View.VISIBLE);
                view8.setVisibility(View.VISIBLE);
                GridLayoutManager mgr = new GridLayoutManager(mContext, 5);
                mTeachRecycleView8.setLayoutManager(mgr);
                mTeachRecycleView8.setAdapter(new CommRecyclerViewAdapter<RecordDetailResponse.mData>(mContext, R.layout.item_sheet_layout, list8) {

                    @Override
                    protected void convert(ViewHolderZhy holder, RecordDetailResponse.mData item, final int position) {
                        holder.setText(R.id.topic_number, item.getQid());
                        if (TextUtils.isEmpty(item.getUser_answer())) {
                            holder.setBackgroundRes(R.id.topic_number, R.mipmap.lianxi_img_default);
                        } else {
                            if (Integer.parseInt(item.getUser_answer()) > 0) {
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
                                intent.putExtra("result", String.valueOf(Integer.valueOf(list8.get(position).getQid()) - 1));
                                //设置返回数据
                                MoniRecordSheetActivity.this.setResult(RESULT_OK, intent);
                                //关闭Activity
                                MoniRecordSheetActivity.this.finish();
                            }
                        });
                    }
                });
            } else {
                mTeachText8.setVisibility(View.GONE);
                mTeachRecycleView8.setVisibility(View.GONE);
                view8.setVisibility(View.GONE);
            }
            if (list9.size() > 0) {
                mActivityText9.setVisibility(View.VISIBLE);
                mActivityRecycleView9.setVisibility(View.VISIBLE);
                GridLayoutManager mgr = new GridLayoutManager(mContext, 5);
                mActivityRecycleView9.setLayoutManager(mgr);
                mActivityRecycleView9.setAdapter(new CommRecyclerViewAdapter<RecordDetailResponse.mData>(mContext, R.layout.item_sheet_layout, list9) {

                    @Override
                    protected void convert(ViewHolderZhy holder, RecordDetailResponse.mData item, final int position) {
                        holder.setText(R.id.topic_number, item.getQid());
                        if (TextUtils.isEmpty(item.getUser_answer())) {
                            holder.setBackgroundRes(R.id.topic_number, R.mipmap.lianxi_img_default);
                        } else {
                            if (Integer.parseInt(item.getUser_answer()) > 0) {
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
                                intent.putExtra("result", String.valueOf(Integer.valueOf(list9.get(position).getQid()) - 1));
                                //设置返回数据
                                MoniRecordSheetActivity.this.setResult(RESULT_OK, intent);
                                //关闭Activity
                                MoniRecordSheetActivity.this.finish();
                            }
                        });
                    }
                });
            } else {
                mActivityText9.setVisibility(View.GONE);
                mActivityRecycleView9.setVisibility(View.GONE);
            }

        }


        mResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < mDataList.size(); i++) {
                    answer.put(mDataList.get(i).getId(), mDataList.get(i).getUser_answer() + "#" + mDataList.get(i).getType());
                }
                if (!answer.isEmpty()) {
                    json = JSON.toJSONString(answer);
                } else {
                    json = "";
                }
                Log.d("*********map", answer.toString() + "---------" + json);
                if (!tiku_id.equals("") && !time.equals("") && !json.equals("")) {
                    getPaper();
                }
                Log.d("*******flag", flag + "--------");
//               if (flag.equals("1")){
//
//               }else {
//                   ToastUtil.showShortToast("您有题目还未完成,不能交卷");
//               }


            }
        });
    }

    protected boolean isShowBacking() {
        return false;
    }

    TestResultResponse.mData mData;

    private void getPaper() {
        Map<String, String> map = new HashMap<>();
        map.put("user_id", user_id);
        map.put("shiti_id", json);
        map.put("tiku_id", tiku_id);
        map.put("time", String.valueOf(TimeUtils.formatTimes(Long.valueOf(time))));
        map.put("zhenti_id", zhenti_id);
        Log.d("*******chuandicanshu*", map.toString());
        GloableConstant.getInstance().startProgressDialog(mContext);
        Xutil.GET(ApiUrl.ZHENTIPOST, map, new MyCallBack<TestResultResponse>() {
            @Override
            public void onSuccess(TestResultResponse result) {
                super.onSuccess(result);
                GloableConstant.getInstance().stopProgressDialog();
                if (result.getStatus_code().equals("200")) {
                    mData = result.getData();
                    Log.d("**********2000", mData.getId() + "-----------");
                    Intent intent = new Intent();
                    intent.setClass(MoniRecordSheetActivity.this, ZhenTiTestResultActivity.class);
                    intent.putExtra("data", mData);
                    startActivity(intent);
                    MoniRecordSheetActivity.this.finish();
                    StartZhenTiPracticaActivity.startZhenTiPracticaActivity.finish();
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
                Log.d("***************ex", ex.getMessage() + "----------");
            }
        });
    }
}
