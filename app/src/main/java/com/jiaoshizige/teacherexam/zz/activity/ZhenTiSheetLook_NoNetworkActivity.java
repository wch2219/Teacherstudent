package com.jiaoshizige.teacherexam.zz.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.jiaoshizige.teacherexam.R;
import com.jiaoshizige.teacherexam.base.BaseActivity;
import com.jiaoshizige.teacherexam.base.CommRecyclerViewAdapter;
import com.jiaoshizige.teacherexam.base.GloableConstant;
import com.jiaoshizige.teacherexam.base.ViewHolderZhy;
import com.jiaoshizige.teacherexam.utils.SPKeyValuesUtils;
import com.jiaoshizige.teacherexam.utils.SPUtils;
import com.jiaoshizige.teacherexam.zz.database.ZhenTi;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/8/8.
 */

public class ZhenTiSheetLook_NoNetworkActivity extends BaseActivity {
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


    private List<ZhenTi> mDataList = new ArrayList<>();
    private List<ZhenTi> list1;
    private List<ZhenTi> list2;
    private List<ZhenTi> list3;
    private List<ZhenTi> list4;
    private List<ZhenTi> list5;
    private List<ZhenTi> list6;
    private List<ZhenTi> list8;
    private List<ZhenTi> list9;
    private Context mContext;
    private String time;
    private String user_id;
    private String tiku_id;
    private String zhenti_id;
    LinkedHashMap<Integer, String> answer = new LinkedHashMap<>();
    String json;
    private String flag;
    private String name;


    @Override
    protected int getLayoutId() {
        return R.layout.moni_sheet_layout;
    }

    @Override
    protected void initView() {
        setToolbarTitle().setText("答题卡");

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
        mResult.setVisibility(View.GONE);

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

        if (getIntent().getStringExtra("name") != null) {
            name = getIntent().getStringExtra("name");
        } else {
            name = "";
        }

        if (GloableConstant.getInstance().getTiku_id() != null) {
            tiku_id = GloableConstant.getInstance().getTiku_id();
        } else {
            tiku_id = "";
        }
        mDataList = (List<ZhenTi>) getIntent().getSerializableExtra("data");
        user_id = (String) SPUtils.getSpValues(SPKeyValuesUtils.SP_USER_ID, SPUtils.TYPE_STRING);
        //  Log.d("/////////useranswer", mDataList.get(0).getUser_answer() + "--------------");
        if (mDataList.size() > 0 && mDataList != null) {
            for (int i = 0; i < mDataList.size(); i++) {
                if (mDataList.get(i).getType() == 1) {
                    list1.add(mDataList.get(i));
                }
                if (mDataList.get(i).getType() == 2) {
                    list2.add(mDataList.get(i));
                }
                if (mDataList.get(i).getType() == 3) {
                    list3.add(mDataList.get(i));
                }
                if (mDataList.get(i).getType() == 4) {
                    list4.add(mDataList.get(i));
                }
                if (mDataList.get(i).getType() == 5) {
                    list5.add(mDataList.get(i));
                }
                if (mDataList.get(i).getType() == 6) {
                    list6.add(mDataList.get(i));
                }
                if (mDataList.get(i).getType() == 7) {
                    list8.add(mDataList.get(i));
                }
                if (mDataList.get(i).getType() == 8) {
                    list9.add(mDataList.get(i));
                }
            }
            if (list1.size() > 0) {
                mSingleText1.setVisibility(View.VISIBLE);
                mSingleRecycleView1.setVisibility(View.VISIBLE);
                view1.setVisibility(View.VISIBLE);
                GridLayoutManager mgr = new GridLayoutManager(mContext, 5);
                mSingleRecycleView1.setLayoutManager(mgr);
                mSingleRecycleView1.setAdapter(new CommRecyclerViewAdapter<ZhenTi>(mContext, R.layout.item_sheet_layout, list1) {

                    @Override
                    protected void convert(ViewHolderZhy holder, ZhenTi item, final int position) {
                        holder.setText(R.id.topic_number, list1.get(position).getQid() + "");
                        if (list1.get(position).getIs_done().equals("1")) {
                            if (list1.get(position).getUser_answer().equals(list1.get(position).getAnswer())) {
                                holder.setBackgroundRes(R.id.topic_number, R.mipmap.lianxi_img_ture);
                            } else {
                                holder.setBackgroundRes(R.id.topic_number, R.mipmap.lianxi_img_false);
                            }

                        } else {
                            holder.setBackgroundRes(R.id.topic_number, R.mipmap.lianxi_img_default);
                        }
                        holder.setOnClickListener(R.id.topic_number, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent();
                                //把返回数据存入Intent
                                intent.putExtra("result", String.valueOf(Integer.valueOf(list1.get(position).getQid()) - 1));
                                //设置返回数据
                                ZhenTiSheetLook_NoNetworkActivity.this.setResult(RESULT_OK, intent);
                                //关闭Activity
                                ZhenTiSheetLook_NoNetworkActivity.this.finish();
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
                mShortRecycleView2.setAdapter(new CommRecyclerViewAdapter<ZhenTi>(mContext, R.layout.item_sheet_layout, list2) {

                    @Override
                    protected void convert(ViewHolderZhy holder, ZhenTi item, final int position) {
                        holder.setText(R.id.topic_number, item.getQid() + "");
                        if (list2.get(position).getIs_done().equals("1")) {
                            if (list2.get(position).getUser_answer().equals(list2.get(position).getAnswer())) {
                                holder.setBackgroundRes(R.id.topic_number, R.mipmap.lianxi_img_ture);
                            } else {
                                holder.setBackgroundRes(R.id.topic_number, R.mipmap.lianxi_img_false);
                            }

                        } else {
                            holder.setBackgroundRes(R.id.topic_number, R.mipmap.lianxi_img_default);
                        }
                        holder.setOnClickListener(R.id.topic_number, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent();
                                //把返回数据存入Intent
                                intent.putExtra("result", String.valueOf(Integer.valueOf(list2.get(position).getQid()) - 1));
                                //设置返回数据
                                ZhenTiSheetLook_NoNetworkActivity.this.setResult(RESULT_OK, intent);
                                //关闭Activity
                                ZhenTiSheetLook_NoNetworkActivity.this.finish();
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
                mAnalysisRecycleView3.setAdapter(new CommRecyclerViewAdapter<ZhenTi>(mContext, R.layout.item_sheet_layout, list3) {

                    @Override
                    protected void convert(ViewHolderZhy holder, ZhenTi item, final int position) {
                        holder.setText(R.id.topic_number, item.getQid() + "");
                        if (list3.get(position).getIs_done().equals("1")) {
                            if (list3.get(position).getUser_answer().equals(list3.get(position).getAnswer())) {
                                holder.setBackgroundRes(R.id.topic_number, R.mipmap.lianxi_img_ture);
                            } else {
                                holder.setBackgroundRes(R.id.topic_number, R.mipmap.lianxi_img_false);
                            }

                        } else {
                            holder.setBackgroundRes(R.id.topic_number, R.mipmap.lianxi_img_default);
                        }
                        holder.setOnClickListener(R.id.topic_number, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent();
                                //把返回数据存入Intent
                                intent.putExtra("result", String.valueOf(Integer.valueOf(list3.get(position).getQid()) - 1));
                                //设置返回数据
                                ZhenTiSheetLook_NoNetworkActivity.this.setResult(RESULT_OK, intent);
                                //关闭Activity
                                ZhenTiSheetLook_NoNetworkActivity.this.finish();
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
                mEssayRecycleView4.setAdapter(new CommRecyclerViewAdapter<ZhenTi>(mContext, R.layout.item_sheet_layout, list4) {

                    @Override
                    protected void convert(ViewHolderZhy holder, ZhenTi item, final int position) {
                        holder.setText(R.id.topic_number, item.getQid() + "");
                        if (list4.get(position).getIs_done().equals("1")) {
                            if (list4.get(position).getUser_answer().equals(list4.get(position).getAnswer())) {
                                holder.setBackgroundRes(R.id.topic_number, R.mipmap.lianxi_img_ture);
                            } else {
                                holder.setBackgroundRes(R.id.topic_number, R.mipmap.lianxi_img_false);
                            }
                        } else {
                            holder.setBackgroundRes(R.id.topic_number, R.mipmap.lianxi_img_default);
                        }
                        holder.setOnClickListener(R.id.topic_number, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent();
                                //把返回数据存入Intent
                                intent.putExtra("result", String.valueOf(Integer.valueOf(list4.get(position).getQid()) - 1));
                                //设置返回数据
                                ZhenTiSheetLook_NoNetworkActivity.this.setResult(RESULT_OK, intent);
                                //关闭Activity
                                ZhenTiSheetLook_NoNetworkActivity.this.finish();
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
                mWriteRecycleView5.setAdapter(new CommRecyclerViewAdapter<ZhenTi>(mContext, R.layout.item_sheet_layout, list5) {

                    @Override
                    protected void convert(ViewHolderZhy holder, ZhenTi item, final int position) {
                        holder.setText(R.id.topic_number, item.getQid() + "");
                        if (list5.get(position).getIs_done().equals("1")) {
                            if (list5.get(position).getUser_answer().equals(list5.get(position).getAnswer())) {
                                holder.setBackgroundRes(R.id.topic_number, R.mipmap.lianxi_img_ture);
                            } else {
                                holder.setBackgroundRes(R.id.topic_number, R.mipmap.lianxi_img_false);
                            }
                        } else {
                            holder.setBackgroundRes(R.id.topic_number, R.mipmap.lianxi_img_default);
                        }
                        holder.setOnClickListener(R.id.topic_number, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent();
                                //把返回数据存入Intent
                                intent.putExtra("result", String.valueOf(Integer.valueOf(list5.get(position).getQid()) - 1));
                                //设置返回数据
                                ZhenTiSheetLook_NoNetworkActivity.this.setResult(RESULT_OK, intent);
                                //关闭Activity
                                ZhenTiSheetLook_NoNetworkActivity.this.finish();
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
                mMaterialRecycl6.setAdapter(new CommRecyclerViewAdapter<ZhenTi>(mContext, R.layout.item_sheet_layout, list6) {

                    @Override
                    protected void convert(ViewHolderZhy holder, ZhenTi item, final int position) {
                        holder.setText(R.id.topic_number, item.getQid() + "");
                        if (list6.get(position).getIs_done().equals("1")) {
                            if (list6.get(position).getUser_answer().equals(list6.get(position).getAnswer())) {
                                holder.setBackgroundRes(R.id.topic_number, R.mipmap.lianxi_img_ture);
                            } else {
                                holder.setBackgroundRes(R.id.topic_number, R.mipmap.lianxi_img_false);
                            }
                        } else {
                            holder.setBackgroundRes(R.id.topic_number, R.mipmap.lianxi_img_default);
                        }
                        holder.setOnClickListener(R.id.topic_number, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent();
                                //把返回数据存入Intent
                                intent.putExtra("result", String.valueOf(Integer.valueOf(list6.get(position).getQid()) - 1));
                                //设置返回数据
                                ZhenTiSheetLook_NoNetworkActivity.this.setResult(RESULT_OK, intent);
                                //关闭Activity
                                ZhenTiSheetLook_NoNetworkActivity.this.finish();
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
                mTeachRecycleView8.setAdapter(new CommRecyclerViewAdapter<ZhenTi>(mContext, R.layout.item_sheet_layout, list8) {

                    @Override
                    protected void convert(ViewHolderZhy holder, ZhenTi item, final int position) {
                        holder.setText(R.id.topic_number, item.getQid() + "");
                        if (list8.get(position).getIs_done().equals("1")) {
                            if (list8.get(position).getUser_answer().equals(list8.get(position).getAnswer())) {
                                holder.setBackgroundRes(R.id.topic_number, R.mipmap.lianxi_img_ture);
                            } else {
                                holder.setBackgroundRes(R.id.topic_number, R.mipmap.lianxi_img_false);
                            }
                        } else {
                            holder.setBackgroundRes(R.id.topic_number, R.mipmap.lianxi_img_default);
                        }
                        holder.setOnClickListener(R.id.topic_number, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent();
                                //把返回数据存入Intent
                                intent.putExtra("result", String.valueOf(Integer.valueOf(list8.get(position).getQid()) - 1));
                                //设置返回数据
                                ZhenTiSheetLook_NoNetworkActivity.this.setResult(RESULT_OK, intent);
                                //关闭Activity
                                ZhenTiSheetLook_NoNetworkActivity.this.finish();
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
                mActivityRecycleView9.setAdapter(new CommRecyclerViewAdapter<ZhenTi>(mContext, R.layout.item_sheet_layout, list9) {

                    @Override
                    protected void convert(ViewHolderZhy holder, ZhenTi item, final int position) {
                        holder.setText(R.id.topic_number, item.getQid() + "");
                        if (list9.get(position).getIs_done().equals("1")) {
                            if (list9.get(position).getUser_answer().equals(list9.get(position).getAnswer())) {
                                holder.setBackgroundRes(R.id.topic_number, R.mipmap.lianxi_img_ture);
                            } else {
                                holder.setBackgroundRes(R.id.topic_number, R.mipmap.lianxi_img_false);
                            }
                        } else {
                            holder.setBackgroundRes(R.id.topic_number, R.mipmap.lianxi_img_default);
                        }
                        holder.setOnClickListener(R.id.topic_number, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent();
                                //把返回数据存入Intent
                                intent.putExtra("result", String.valueOf(Integer.valueOf(list9.get(position).getQid()) - 1));
                                //设置返回数据
                                ZhenTiSheetLook_NoNetworkActivity.this.setResult(RESULT_OK, intent);
                                //关闭Activity
                                ZhenTiSheetLook_NoNetworkActivity.this.finish();
                            }
                        });
                    }
                });
            } else {
                mActivityText9.setVisibility(View.GONE);
                mActivityRecycleView9.setVisibility(View.GONE);
            }

        }

    }

    protected boolean isShowBacking() {
        return false;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
