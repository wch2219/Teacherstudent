package com.jiaoshizige.teacherexam.zz;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONType;
import com.jiaoshizige.teacherexam.R;
import com.jiaoshizige.teacherexam.base.BaseActivity;
import com.jiaoshizige.teacherexam.base.GloableConstant;
import com.jiaoshizige.teacherexam.http.ApiUrl;
import com.jiaoshizige.teacherexam.http.MyCallBack;
import com.jiaoshizige.teacherexam.http.Xutil;
import com.jiaoshizige.teacherexam.model.TestResultResponse;
import com.jiaoshizige.teacherexam.utils.SPKeyValuesUtils;
import com.jiaoshizige.teacherexam.utils.SPUtils;
import com.jiaoshizige.teacherexam.utils.ToastUtil;
import com.jiaoshizige.teacherexam.zz.activity.StartZhenTiPracticaActivity;
import com.jiaoshizige.teacherexam.zz.activity.StartZhenTiPractica_NoNetworkActivity;
import com.jiaoshizige.teacherexam.zz.activity.ZhenTiTestResultActivity;
import com.jiaoshizige.teacherexam.zz.bean.AllQuestionResponse;
import com.jiaoshizige.teacherexam.zz.bean.SumintResultBean;
import com.jiaoshizige.teacherexam.zz.bean.ZhenTiDownLoadBean;
import com.jiaoshizige.teacherexam.zz.bean.ZhenTiSumitBean;
import com.jiaoshizige.teacherexam.zz.database.UserAnswer;
import com.jiaoshizige.teacherexam.zz.database.UserAnswerDao;
import com.jiaoshizige.teacherexam.zz.database.ZhenTi;
import com.jiaoshizige.teacherexam.zz.database.ZhenTiDao;
import com.jiaoshizige.teacherexam.zz.database.ZhenTiList;
import com.jiaoshizige.teacherexam.zz.database.ZhenTiListDao;
import com.jiaoshizige.teacherexam.zz.utils.NetworkUtils;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AllQuestionsActivity extends BaseActivity {

    @BindView(R.id.recycle_view)
    RecyclerView mRecycleView;
    @BindView(R.id.tv_nodata)
    TextView mTvNodata;
    private Context mContext;
    private int PRICTICERESULTCODE = 123;
    private List<AllQuestionResponse.DataBean> mDataBeans = new ArrayList<>();
    private boolean isNetWork = false;

    private String tiku_id;
    private String user_id;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_all_questions;
    }

    @Override
    protected void initView() {
        setSubTitle().setText("");
        setToolbarTitle().setText("历年真题");

        mContext = this;

        user_id = (String) SPUtils.getSpValues(SPKeyValuesUtils.SP_USER_ID, SPUtils.TYPE_STRING);
        if (GloableConstant.getInstance().getTiku_id() != null) {
            tiku_id = GloableConstant.getInstance().getTiku_id();
        } else {
            tiku_id = "";
        }

        isNetWork = NetworkUtils.isNetworkConnected(AllQuestionsActivity.this);
        if (isNetWork) {
            isSumit();
        } else {
            getLocalData();
        }

    }

    private void getData() {
        GloableConstant.getInstance().startProgressDialog(AllQuestionsActivity.this);
        Map<String, String> map = new HashMap<>();
        map.put("user_id", user_id);
        map.put("tiku_id", tiku_id);
        GloableConstant.getInstance().startProgressDialog(this);
        Xutil.GET(ApiUrl.ZHENTILIST, map, new MyCallBack<AllQuestionResponse>() {
            @Override
            public void onSuccess(AllQuestionResponse result) {
                super.onSuccess(result);
                GloableConstant.getInstance().stopProgressDialog1();
                if (result.getStatus_code().equals("200")) {
                    if (result.getData() != null) {
                        if (result.getData().size() > 0) {
                            mDataBeans = result.getData();
                            mRecycleView.setLayoutManager(new LinearLayoutManager(mContext));
                            mRecycleView.setAdapter(new CommonAdapter<AllQuestionResponse.DataBean>(mContext, R.layout.item_all_question, result.getData()) {
                                @Override
                                protected void convert(final ViewHolder holder, final AllQuestionResponse.DataBean dataBean, int position) {

                                    final ImageView imageView = holder.getView(R.id.catalog_arrow);
                                    final LinearLayout ll_option = holder.getView(R.id.ll_option);
                                    final ImageView iv_prictice_result = holder.getView(R.id.iv_prictice_result);
                                    TextView tv_prictice_result = holder.getView(R.id.tv_prictice_result);

                                    final int isExam = dataBean.getIs_exam();
                                    final String zhenti_id = dataBean.getZhenti_id();//仅查看结果用
                                    final String name = dataBean.getName();
                                    final String id = dataBean.getId();

                                    //判断用户是否下载过真题试卷
                                    final List<ZhenTiList> userzhentiList = new ZhenTiListDao(AllQuestionsActivity.this).queryByColumnNames("userid", user_id, "ztid", id, "tiku_id", tiku_id);

                                    imageView.setImageResource(R.mipmap.cehua_arrow_small_down);
                                    ll_option.setVisibility(View.GONE);
                                    holder.setText(R.id.course_title, dataBean.getName());

                                    if (isExam == 1) {
                                        holder.setText(R.id.course_status, "已完成");
                                        iv_prictice_result.setImageResource(R.mipmap.home_icon_lxjg_pressed);
                                        tv_prictice_result.setTextColor(getResources().getColor(R.color.text_color6));
                                    } else {
                                        holder.setText(R.id.course_status, "未完成");
                                        iv_prictice_result.setImageResource(R.mipmap.home_icon_lxjg_disabled);
                                        tv_prictice_result.setTextColor(getResources().getColor(R.color.text_color9));
                                    }

                                    if (userzhentiList.size() > 0) {
                                        holder.setImageResource(R.id.iv_download, R.mipmap.home_icon_xzwc_disabled);
                                        holder.setText(R.id.tv_download, "下载完成");
                                        holder.setTextColor(R.id.tv_download, getResources().getColor(R.color.text_color9));
                                    } else {
                                        holder.setImageResource(R.id.iv_download, R.mipmap.home_icon_sjxz_default);
                                        holder.setText(R.id.tv_download, "试卷下载");
                                        holder.setTextColor(R.id.tv_download, getResources().getColor(R.color.text_color6));
                                    }

                                    holder.setOnClickListener(R.id.ll_item, new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            if (ll_option.getVisibility() == View.GONE) {
                                                imageView.setImageResource(R.mipmap.cehua_arrow_small_up);
                                                ll_option.setVisibility(View.VISIBLE);
                                            } else {
                                                imageView.setImageResource(R.mipmap.cehua_arrow_small_down);
                                                ll_option.setVisibility(View.GONE);
                                            }
                                        }
                                    });


                                    holder.setOnClickListener(R.id.question_prictice_result, new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            if (isExam == 1) {
                                                getResult(zhenti_id);
                                            }
                                        }
                                    });
                                    holder.setOnClickListener(R.id.question_start_prictice, new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Intent intent = new Intent();
                                            intent.setClass(AllQuestionsActivity.this, StartZhenTiPracticaActivity.class);
                                            intent.putExtra("zhenti_id", id);
                                            intent.putExtra("name", name);
                                            startActivityForResult(intent, PRICTICERESULTCODE);

                                        }
                                    });

                                    holder.setOnClickListener(R.id.question_download, new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            if (userzhentiList.size() == 0) {
                                                zhentiDownload(id, name, isExam, zhenti_id, dataBean);
                                            }
                                        }
                                    });
                                }

                            });

                        } else {
                            mTvNodata.setVisibility(View.VISIBLE);
                            mRecycleView.setVisibility(View.GONE);
                        }
                    }
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
                GloableConstant.getInstance().stopProgressDialog1();
                mTvNodata.setVisibility(View.VISIBLE);
                mRecycleView.setVisibility(View.GONE);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (resultCode == PRICTICERESULTCODE) {
                isNetWork = NetworkUtils.isNetworkConnected(AllQuestionsActivity.this);
                if (isNetWork) {
                    getData();
                } else {
                    getLocalData();
                }
            }
        }
    }


    List<ZhenTiDownLoadBean.DataBean> mDataList;

    private void zhentiDownload(final String id, final String name, final int isExam, final String zhenti_id, final AllQuestionResponse.DataBean dataBean) {
        Map<String, String> map = new HashMap<>();
        map.put("user_id", user_id);
        map.put("zhenti_id", id);
        map.put("tiku_id", tiku_id);
        Xutil.GET(ApiUrl.ZHENTIDOWN, map, new MyCallBack<ZhenTiDownLoadBean>() {
            @Override
            public void onSuccess(ZhenTiDownLoadBean result) {
                super.onSuccess(result);
                if (result.getStatus_code().equals("200")) {
                    if (result.getData() != null && result.getData().size() > 0) {
                        mDataList = result.getData();

                        // 保存列表数据
                        String userid = (String) SPUtils.getSpValues(SPKeyValuesUtils.SP_USER_ID, SPUtils.TYPE_STRING);
                        ZhenTiList zhenTiList = new ZhenTiList(id, name, isExam, zhenti_id, userid, tiku_id + "");
                        new ZhenTiListDao(AllQuestionsActivity.this).insert(zhenTiList);

                        //判断是否其他用户下载过
                        List<ZhenTi> zhenTiList1 = new ZhenTiDao(AllQuestionsActivity.this).queryByColumnNameAndColumnName("zhenti_id", id, "tiku_id", tiku_id);
                        if (zhenTiList1.size() > 0) {

                        } else {
                            // 保存真题数据
                            for (int i = 0; i < mDataList.size(); i++) {
                                int qid = i + 1;
                                String metasJsonStr = JSON.toJSONString(mDataList.get(i).getMetas());
                                ZhenTi zhenTi = new ZhenTi(mDataList.get(i).getId(), mDataList.get(i).getType(), mDataList.get(i).getCourse(), mDataList.get(i).getChapter(), mDataList.get(i).getStem(), metasJsonStr, mDataList.get(i).getAnswer(), mDataList.get(i).getAnalysis(), mDataList.get(i).getShiti_type(), mDataList.get(i).getXueduan(),
                                        mDataList.get(i).getNianfen(), mDataList.get(i).getPfbz(), mDataList.get(i).getTotle_post(), mDataList.get(i).getTotle_success(), mDataList.get(i).getEasy_error(), mDataList.get(i).getScore(), mDataList.get(i).getUser_answer(), mDataList.get(i).getKaodian_name(), mDataList.get(i).getTotle_user(),
                                        mDataList.get(i).getSuccess(), mDataList.get(i).getIs_collect(), tiku_id, id, qid);
                                new ZhenTiDao(AllQuestionsActivity.this).insert(zhenTi);
                            }
                        }
                        Toast.makeText(AllQuestionsActivity.this, "下载完成", Toast.LENGTH_SHORT).show();
                        dataBean.setIs_exam(1);
                        mRecycleView.getAdapter().notifyDataSetChanged();
                    }

                }
            }

            @Override
            public void onFinished() {
                super.onFinished();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
            }
        });
    }


    private void getLocalData() {
        List<ZhenTiList> tmplist = new ZhenTiListDao(AllQuestionsActivity.this).queryByColumnNameAndColumnName("userid", user_id, "tiku_id", tiku_id);
        if (tmplist != null && tmplist.size() > 0) {
            //表中有数据
            mTvNodata.setVisibility(View.GONE);
            mRecycleView.setVisibility(View.VISIBLE);

            mRecycleView.setLayoutManager(new LinearLayoutManager(mContext));
            mRecycleView.setAdapter(new CommonAdapter<ZhenTiList>(mContext, R.layout.item_all_question, tmplist) {
                @Override
                protected void convert(final ViewHolder holder, ZhenTiList dataBean, int position) {

                    final ImageView imageView = holder.getView(R.id.catalog_arrow);
                    final LinearLayout ll_option = holder.getView(R.id.ll_option);
                    final ImageView iv_prictice_result = holder.getView(R.id.iv_prictice_result);
                    TextView tv_prictice_result = holder.getView(R.id.tv_prictice_result);

                    final int isExam = dataBean.getIs_exam();
                    final String zhenti_id = dataBean.getZhenti_id();//仅查看结果用
                    final String name = dataBean.getName();
                    final String id = dataBean.getZtid();

                    final List<UserAnswer> userAnswerList = new UserAnswerDao(AllQuestionsActivity.this).queryByColumnNames("userid", user_id, "ztid", id, "tiku_id", tiku_id);

                    imageView.setImageResource(R.mipmap.cehua_arrow_small_down);
                    ll_option.setVisibility(View.GONE);
                    //下载完成
                    holder.setImageResource(R.id.iv_download, R.mipmap.home_icon_xzwc_disabled);
                    holder.setText(R.id.tv_download, "下载完成");
                    holder.setTextColor(R.id.tv_download, getResources().getColor(R.color.text_color9));

                    holder.setText(R.id.course_title, dataBean.getName());

                    if (userAnswerList.size() > 0) {
                        holder.setText(R.id.course_status, "已完成");
                        iv_prictice_result.setImageResource(R.mipmap.home_icon_lxjg_pressed);
                        tv_prictice_result.setTextColor(getResources().getColor(R.color.text_color6));
                    } else {
                        holder.setText(R.id.course_status, "未完成");
                        iv_prictice_result.setImageResource(R.mipmap.home_icon_lxjg_disabled);
                        tv_prictice_result.setTextColor(getResources().getColor(R.color.text_color9));
                    }


                    holder.setOnClickListener(R.id.ll_item, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (ll_option.getVisibility() == View.GONE) {
                                imageView.setImageResource(R.mipmap.cehua_arrow_small_up);
                                ll_option.setVisibility(View.VISIBLE);
                            } else {
                                imageView.setImageResource(R.mipmap.cehua_arrow_small_down);
                                ll_option.setVisibility(View.GONE);
                            }
                        }
                    });


                    holder.setOnClickListener(R.id.question_prictice_result, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (userAnswerList.size() > 0) {
                                try {
                                    TestResultResponse.mData mData = JSON.parseObject(userAnswerList.get(0).getFenshu(), TestResultResponse.mData.class);
                                    Intent intent = new Intent();
                                    intent.setClass(AllQuestionsActivity.this, ZhenTiTestResultActivity.class);
                                    intent.putExtra("data", mData);
                                    startActivity(intent);
                                } catch (Exception e) {
                                }

                            }
                        }
                    });
                    holder.setOnClickListener(R.id.question_start_prictice, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent();
                            intent.setClass(AllQuestionsActivity.this, StartZhenTiPractica_NoNetworkActivity.class);
                            intent.putExtra("zhenti_id", id);
                            intent.putExtra("name", name);
                            startActivityForResult(intent, PRICTICERESULTCODE);

                        }
                    });

                }

            });

        } else {
            //表中没有数据
            mTvNodata.setVisibility(View.VISIBLE);
            mRecycleView.setVisibility(View.GONE);
        }
    }

    private void getResult(String zhenti_id) {
        Map<String, String> map = new HashMap<>();
        map.put("user_id", user_id);
        map.put("is_zhenti", "1");
        map.put("moni_id", zhenti_id);
        GloableConstant.getInstance().startProgressDialog(mContext);
        Xutil.GET(ApiUrl.MONIRESULT, map, new MyCallBack<TestResultResponse>() {
            @Override
            public void onSuccess(TestResultResponse result) {
                super.onSuccess(result);
                GloableConstant.getInstance().stopProgressDialog();
                if (result.getStatus_code().equals("200")) {
                    TestResultResponse.mData testResultResponse = result.getData();
                    Intent intent = new Intent();
                    intent.setClass(AllQuestionsActivity.this, ZhenTiTestResultActivity.class);
                    intent.putExtra("data", testResultResponse);
                    startActivity(intent);
                } else {
                    ToastUtil.showShortToast(result.getMessage());
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


    private void isSumit() {
        List<UserAnswer> list = new UserAnswerDao(AllQuestionsActivity.this).queryByColumnNameAndColumnName("userid", user_id, "is_sumit", "0");
        List<ZhenTiSumitBean> sumitBeanList = new ArrayList<>();
        if (list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                UserAnswer userAnswer = list.get(i);
                ZhenTiSumitBean mData = JSON.parseObject(userAnswer.getFenshu(), ZhenTiSumitBean.class);
                List<LinkedHashMap<String, String>> map = getValue(userAnswer.getData());
                mData.setShiti_id(map);
                mData.setEid(userAnswer.getEid());
                mData.setTiku_id(userAnswer.getTiku_id());
                mData.setZhenti_id(userAnswer.getZtid());
                sumitBeanList.add(mData);
            }
            if (sumitBeanList.size() > 0) {
                postData(JSON.toJSONString(sumitBeanList), list);
            } else {
                getData();
            }

        } else {
            getData();
        }
    }

    private List<LinkedHashMap<String, String>> getValue(String json) {
        List<LinkedHashMap<String, String>> answer = new ArrayList<>();
        try {
            LinkedHashMap map = JSON.parseObject(json, LinkedHashMap.class);
            if (map.size() > 0) {
                for (Object obj : map.keySet()) {
                    String key = obj.toString();
                    String value = String.valueOf(map.get(obj));
                    LinkedHashMap<String, String> tmp = new LinkedHashMap<>();
                    tmp.put(key, value);
                    answer.add(tmp);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return answer;
    }

    private void postData(String shiti_info, final List<UserAnswer> list) {
        Map<String, Object> map = new HashMap<>();
        map.put("user_id", user_id);
        map.put("shiti_info", shiti_info);
        Xutil.Post(ApiUrl.LOCALZHENTIPOST, map, new MyCallBack<SumintResultBean>() {
            @Override
            public void onSuccess(SumintResultBean result) {
                super.onSuccess(result);
                if (result.getStatus_code().equals("200")) {
                    if (list.size() > 0) {
                        for (int i = 0; i < list.size(); i++) {
                            UserAnswer userAnswer = list.get(i);
                            userAnswer.setIs_sumit("1");
                            new UserAnswerDao(AllQuestionsActivity.this).update(userAnswer);
                        }
                    }
                }
                getData();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
            }

            @Override
            public void onFinished() {
                super.onFinished();
            }
        });

    }


}
