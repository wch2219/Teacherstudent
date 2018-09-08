package com.jiaoshizige.teacherexam.activity;


import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.nex3z.flowlayout.FlowLayout;
import com.jiaoshizige.teacherexam.MainActivity;
import com.jiaoshizige.teacherexam.R;
import com.jiaoshizige.teacherexam.adapter.GridviewAdapter;
import com.jiaoshizige.teacherexam.adapter.ImageAdapter;
import com.jiaoshizige.teacherexam.base.BaseActivity;
import com.jiaoshizige.teacherexam.base.GloableConstant;
import com.jiaoshizige.teacherexam.http.ApiUrl;
import com.jiaoshizige.teacherexam.http.MyCallBack;
import com.jiaoshizige.teacherexam.http.SupportResponse;
import com.jiaoshizige.teacherexam.http.Xutil;
import com.jiaoshizige.teacherexam.model.EvaluationReportResponse;
import com.jiaoshizige.teacherexam.model.ShanXinViewData;
import com.jiaoshizige.teacherexam.utils.SPKeyValuesUtils;
import com.jiaoshizige.teacherexam.utils.SPUtils;
import com.jiaoshizige.teacherexam.utils.ToastUtil;
import com.jiaoshizige.teacherexam.utils.ToolUtils;
import com.jiaoshizige.teacherexam.widgets.MScrollerGridView;
import com.jiaoshizige.teacherexam.widgets.ShanXinView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/8/2 0002.
 * qyy测评报告
 */

public class EvaluationReport extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.grid_view)
    GridView mGridView;
    @BindView(R.id.trench_view)
    ShanXinView mTrench;
    @BindView(R.id.city)
    ShanXinView mCity;
    @BindView(R.id.text)
    TextView text;
    @BindView(R.id.evaluation_Image)
    ImageView mImageView;
    @BindView(R.id.name)
    TextView mName;
    @BindView(R.id.flow_layout)
    FlowLayout mFlowLayout;
    @BindView(R.id.content)
    TextView mContent;
    @BindView(R.id.appraisal)
    Button mAppraisal;
    @BindView(R.id.img_grid)
    MScrollerGridView mScrollerGridView;

    private List<String> mList;
    private ArrayList<String> list;
    private ArrayList<ShanXinViewData> shanXinViewData;   //数据
    private ArrayList<ShanXinViewData> mShanXinViewData;
    private List<String> content = new ArrayList<>();
    private String user_id;
    GridviewAdapter mAdapter;
    private Context mContext;
    private ImageAdapter mImageAdapter;
    private String province, exam_type, subject, is_exam, ready_status, study_time, is_buy;
    private String proforma, is_graduate, graduate_time, is_normal_stu, edu, major;
    private String result;

    private double kid, senior, allNum, junior, primary,
            precentnum0, precentnum1, precentnum2, precentnum3,
            allNumCity, city1, city2, city3, city4;

    private String percent0, percent1, percent2, percent3, percentCity1, percentCity2, percentCity3, percentCity4;

    private String str = "您的测评报告已经生成，恭喜您已经成为中冠会员,领专属会员编号，享0元畅听中冠会员微课堂,请加QQ群: <font color='#9A70C4'>4601717778</font>";


    @Override
    protected int getLayoutId() {
        return R.layout.evaluation_layout;
    }

    @Override
    protected void initView() {
        setSubTitle().setText("");
        setToolbarTitle().setText("测评报告");
        user_id = (String) SPUtils.getSpValues(SPKeyValuesUtils.SP_USER_ID, SPUtils.TYPE_STRING);
        mContext = this;
        text.setTextSize(16);
        text.setText(Html.fromHtml(str));
        text.setOnClickListener(this);
        mAppraisal.setOnClickListener(this);


        result = GloableConstant.getInstance().getResult();
        mImageAdapter = new ImageAdapter(mContext);
        mScrollerGridView.setAdapter(mImageAdapter);

        mList = new ArrayList<>();
        initdata();
        mAdapter = new GridviewAdapter(mContext, mList);
        mGridView.setAdapter(mAdapter);

        if (result.equals("0")) {
            list = getIntent().getStringArrayListExtra("list");
            if (list.size() > 0) {
                for (int i = 0; i < list.size(); i++) {
                    Log.e("**********结果list", list.get(i));
                }

                province = list.get(0);
                exam_type = list.get(1);
                subject = list.get(2);
                is_exam = list.get(3);
                ready_status = list.get(4);
                study_time = list.get(5);
                is_buy = list.get(6);
                proforma = list.get(7);
                is_graduate = list.get(8);
                graduate_time = list.get(9);
                is_normal_stu = list.get(10);
                edu = list.get(11);
                major = list.get(12);

                evaluate();
            }
        }else {
            evaluate();
        }


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.appraisal:
                appraisal();
                break;
        }

    }

    private void evaluate() {
        Map<String, Object> map = new HashMap<>();
       if (result.equals("0")){
           map.put("user_id", user_id);
           map.put("province", province);
           map.put("exam_type", exam_type);
           map.put("subject", subject);
           map.put("is_exam", is_exam);
           map.put("ready_status", ready_status);
           map.put("study_time", study_time);
           map.put("is_buy", is_buy);
           map.put("proforma", proforma);
           map.put("is_graduate", is_graduate);
           map.put("graduate_time", graduate_time);
           map.put("is_normal_stu", is_normal_stu);
           map.put("edu", edu);
           map.put("major", major);
       }else {
           map.put("user_id", user_id);
       }
//        map.put("user_id", 1);
//        map.put("province", "安徽省铜陵市");
//        map.put("exam_type", "小学");
//        map.put("subject", "政治");
//        map.put("is_exam", "未参加过");
//        map.put("ready_status", "已掌握一些知识");
//        map.put("study_time", "6~8小时");
//        map.put("is_buy", "是");
//        map.put("proforma", "自学");
//        map.put("is_graduate", "在校生");
//        map.put("graduate_time", "2018年01月");
//        map.put("is_normal_stu", "师范生");
//        map.put("edu", "大专");
//        map.put("major", "语文");
        Log.e("*********map", map.toString());
        GloableConstant.getInstance().showmeidialog(this);
        Xutil.Post(ApiUrl.EVALUATE, map, new MyCallBack<EvaluationReportResponse>() {
            @Override
            public void onSuccess(EvaluationReportResponse result) {
                super.onSuccess(result);
                GloableConstant.getInstance().stopProgressDialog();
                if (result.getStatus_code().equals("200")) {
                    Glide.with(mContext).load(result.getData().getUser_info().getAvatar())
                            .apply(GloableConstant.getInstance().getOptions())
                            .into(mImageView);
                    mName.setText(result.getData().getUser_info().getNick_name());
                    mContent.setText(result.getData().getContent());
                    mImageAdapter.setmList(result.getData().getOther());
                    for (int i = 0; i < result.getData().getTag().size(); i++) {
                        content.add(result.getData().getTag().get(i).getTag());
                    }
                    Log.e("************cotent", content.toString());
                    for (int i = 0; i < result.getData().getTag().size(); i++) {
                        //创建一个Textview
                        final TextView textView = new TextView(mContext);
                        textView.setText(content.get(i));
                        textView.setTag(i);
                        textView.setBackgroundResource(R.drawable.gray_shap_white_5);
                        textView.setTextColor(getResources().getColor(R.color.text_color6));
                        //设置padding
                        int pxlr = textView.getResources().getDimensionPixelOffset(R.dimen.dp_10);
                        int pxlb = textView.getResources().getDimensionPixelOffset(R.dimen.margin_10);
                        textView.setPadding(pxlr, pxlb, pxlr, pxlr);
                        mFlowLayout.addView(textView);
                    }
                    for (int a = 0; a < result.getData().getHole().size(); a++) {
                        allNum = allNum + result.getData().getHole().get(a).getNum();
                    }
                    Log.e("*********allNum", allNum + "");
                    for (int i = 0; i < result.getData().getHole().size(); i++) {
                        Log.e("*******for", result.getData().getHole().get(i).getNum() + "");
                        if (result.getData().getHole().get(i).getExam_type().equals("幼儿")) {
                            kid = result.getData().getHole().get(i).getNum();
                            precentnum0 = kid / allNum * 100;
                            percent0 = ToolUtils.FormatTwoPoint(String.valueOf(precentnum0));
                            Log.e("********precentnum0000", precentnum0 + "********lid" + kid);
                            Log.e("*********", percent0);
                        }
                        if (result.getData().getHole().get(i).getExam_type().equals("小学")) {
                            primary = result.getData().getHole().get(i).getNum();
                            precentnum1 = primary / allNum * 100;
                            percent1 = ToolUtils.FormatTwoPoint(String.valueOf(precentnum1));
                        }
                        if (result.getData().getHole().get(i).getExam_type().equals("初中")) {
                            junior = result.getData().getHole().get(i).getNum();
                            precentnum2 = junior / allNum * 100;
                            percent2 = ToolUtils.FormatTwoPoint(String.valueOf(precentnum2));
                        }
                        if (result.getData().getHole().get(i).getExam_type().equals("高中")) {
                            senior = result.getData().getHole().get(i).getNum();
                            precentnum3 = senior / allNum * 100;
                            percent3 = ToolUtils.FormatTwoPoint(String.valueOf(precentnum3));

                        }

                    }

                    for (int i = 0; i < result.getData().getCity().size(); i++) {
                        allNumCity = allNumCity + result.getData().getCity().get(i).getNum();
                    }
                    for (int i = 0; i < result.getData().getCity().size(); i++) {
                        if (result.getData().getCity().get(i).getProvince().equals("北京")) {
                            city1 = result.getData().getCity().get(i).getNum();
                            percentCity1 = ToolUtils.FormatTwoPoint(String.valueOf(city1 / allNumCity * 100));
                            Log.e("*********city1", city1 + "*********percentCity1" + percentCity1);
                        }
                        if (result.getData().getCity().get(i).getProvince().equals("上海")) {
                            city2 = result.getData().getCity().get(i).getNum();
                            percentCity2 = ToolUtils.FormatTwoPoint(String.valueOf(city2 / allNumCity * 100));
                            Log.e("*********city2", city2 + "*********percentCity2" + percentCity2);

                        }
                        if (result.getData().getCity().get(i).getProvince().equals("河南")) {
                            city3 = result.getData().getCity().get(i).getNum();
                            percentCity3 = ToolUtils.FormatTwoPoint(String.valueOf(city3 / allNumCity * 100));
                            Log.e("*********city3", city3 + "*********percentCity3" + percentCity3);

                        }
                        if (result.getData().getCity().get(i).getProvince().equals("其他")) {
                            city4 = result.getData().getCity().get(i).getNum();
                            percentCity4 = ToolUtils.FormatTwoPoint(String.valueOf(city4 / allNumCity * 100));
                            Log.e("*********city4", city4 + "*********percentCity4" + percentCity4);

                        }
                    }

                    addData();
                }


            }
        });


    }

    private void addData() {
        //
        shanXinViewData = new ArrayList<>();
        shanXinViewData.add(new ShanXinViewData((int) kid, percent0 + "%"));
        shanXinViewData.add(new ShanXinViewData((int) primary, percent1 + "%"));
        shanXinViewData.add(new ShanXinViewData((int) junior, percent2 + "%"));
        shanXinViewData.add(new ShanXinViewData((int) senior, percent3 + "%"));
        //城市
        mShanXinViewData = new ArrayList<>();
        mShanXinViewData.add(new ShanXinViewData((int) city1, percentCity1 + "%"));
        mShanXinViewData.add(new ShanXinViewData((int) city2, percentCity2 + "%"));
        mShanXinViewData.add(new ShanXinViewData((int) city3, percentCity3 + "%"));
        mShanXinViewData.add(new ShanXinViewData((int) city4, percentCity4 + "%"));
        Log.e("*******mShanXinViewData", mShanXinViewData.size() + "");
        mTrench.setData(shanXinViewData);
        mCity.setData(mShanXinViewData);
    }

    private void appraisal() {
        Map<String, Object> map = new HashMap<>();
        map.put("user_id", user_id);
        Xutil.Post(ApiUrl.APPRAISAL, map, new MyCallBack<SupportResponse>() {
            @Override
            public void onSuccess(SupportResponse result) {
                super.onSuccess(result);
                if (result.getStatus_code().equals("204")) {
                    ToastUtil.showShortToast(result.getMessage());
                    startActivity(new Intent(EvaluationReport.this, MainActivity.class));
                    finish();
                }
            }
        });
    }

    private void initdata() {
        mList.add("笔试\n报名");
        mList.add("准备资料\n现场确认");
        mList.add("笔试\n备考");
        mList.add("准考证\n打印");
        mList.add("准备资料\n现场审核");
        mList.add("面试\n报名");
        mList.add("笔试成\n绩查询");
        mList.add("笔试\n考试");
        mList.add("面试\n备考");
        mList.add("准考证\n打印");
        mList.add("面试\n考试");
        mList.add("面试成\n绩查询");
        mList.add("笔试\n资料");
        mList.add("领取教师\n资格证");
        mList.add("体检");
        mList.add("申请\n认定");
    }


}
