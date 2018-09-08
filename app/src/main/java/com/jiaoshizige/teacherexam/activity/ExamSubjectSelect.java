package com.jiaoshizige.teacherexam.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.jiaoshizige.teacherexam.MainActivity;
import com.jiaoshizige.teacherexam.R;
import com.jiaoshizige.teacherexam.adapter.ExamTypeAdapter;
import com.jiaoshizige.teacherexam.adapter.ProvinceSelectAdapter;
import com.jiaoshizige.teacherexam.base.BaseActivity;
import com.jiaoshizige.teacherexam.base.GloableConstant;
import com.jiaoshizige.teacherexam.http.ApiUrl;
import com.jiaoshizige.teacherexam.http.MyCallBack;
import com.jiaoshizige.teacherexam.http.SupportResponse;
import com.jiaoshizige.teacherexam.http.Xutil;
import com.jiaoshizige.teacherexam.model.ExamTypeResponse;
import com.jiaoshizige.teacherexam.model.provincesResponse;
import com.jiaoshizige.teacherexam.utils.AllUtils;
import com.jiaoshizige.teacherexam.utils.SPKeyValuesUtils;
import com.jiaoshizige.teacherexam.utils.SPUtils;
import com.jiaoshizige.teacherexam.utils.Sphelper;
import com.jiaoshizige.teacherexam.utils.ToastUtil;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/8/1.
 * 选择考试科目
 */

public class ExamSubjectSelect extends BaseActivity {
    @BindView(R.id.examtype)
    TextView mExamType;
    @BindView(R.id.examair)
    TextView mExamair;
    @BindView(R.id.submit_bt)
    Button mSubmit;
    //    private String mExamTypeText = "";
//    private String mProvinceText = "";
//    private String mExamTypeId = "";
    Intent intent;
    String tag, user_id;
    private Context mContext;
    private String flag;


    @Override
    protected int getLayoutId() {
        return R.layout.examsubject_select;
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("选择考试科目");
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("选择考试科目");
        MobclickAgent.onPause(this);
    }

    @Override
    protected void initView() {
        user_id = (String) SPUtils.getSpValues(SPKeyValuesUtils.SP_USER_ID, SPUtils.TYPE_STRING);
        mContext = this;
        isFirstUse = Sphelper.getBoolean(ExamSubjectSelect.this, "isFirstUse", "isfiestuser");
        Log.e("*********isFirstUse", isFirstUse+"");
//        intent=getIntent();
//        tag=intent.getStringExtra("tag");
//        if (tag!=null&&tag.equals("1")){
//            setToolbarTitle().setText("我的兴趣");
        if (getIntent().getStringExtra("exam_area") != null) {
            mProvince = getIntent().getStringExtra("exam_area");
            mExamair.setText(getIntent().getStringExtra("exam_area"));
        }
//
        if (getIntent().getStringExtra("exam_type") != null) {
            type_name = getIntent().getStringExtra("exam_type");
            mExamType.setText(type_name);
        }
        if (getIntent().getStringExtra("exam_type_id") != null) {
            type_id = getIntent().getStringExtra("exam_type_id");
        }
//        }else {
//            setToolbarTitle().setText("选择考试科目");
//        }
        setToolbarTitle().setText("选择考试科目");
        setSubTitle().setText("");
    }

    @OnClick({R.id.examtype, R.id.examair, R.id.submit_bt, R.id.back_im})
    public void onClick(View view) {
//        if (view.getId() == R.id.examtype) {
//            startActivityForResult(new Intent(ExamSubjectSelect.this, ExamTypeActivity.class), 1);
//        } else if (view.getId() == R.id.examair) {
//            startActivityForResult(new Intent(ExamSubjectSelect.this, ProvinceActivity.class), 2);
//        } else if (view.getId() == R.id.back_im) {
//
//        } else if (view.getId() == R.id.submit_bt) {
//            if (mExamTypeText.equals("")) {
//                ToastUtil.showShortToast("请先选择考试类型");
//            } else if (mProvinceText.equals("")) {
//                ToastUtil.showShortToast("请先选择考试地区");
//            } else {
//                if(tag.equals("1")){
//                    UpDataMessage();
//                }else{
//                    startActivity(new Intent(ExamSubjectSelect.this, MainActivity.class));
//                    UpDataMessage();
//                    finish();
//                }
//
//
//            }
//
//        }
        switch (view.getId()) {
            case R.id.examtype:
                flag = "1";
                examType();
                break;
            case R.id.examair:
                flag = "2";
                showPopWindow();
                break;
            case R.id.back_im:
                break;
            case R.id.submit_bt:
//                if (mExamTypeText.equals("")) {
//                    ToastUtil.showShortToast("请先选择考试类型");
//                } else if (mProvinceText.equals("")) {
//                    ToastUtil.showShortToast("请先选择考试地区");
//                } else {
//                    startActivity(new Intent(ExamSubjectSelect.this, MainActivity.class));
//                UpDataMessage();
//                    finish();
//                if(tag.equals("1")){
//                    UpDataMessage();
//                }else{
//                    startActivity(new Intent(ExamSubjectSelect.this, MainActivity.class));
//                    UpDataMessage();
//                    finish();
//                }
//                }
                if (TextUtils.isEmpty(type_id)) {
                    ToastUtil.showShortToast("请先选择考试类型");
                } else if (TextUtils.isEmpty(mProvince)) {
                    ToastUtil.showShortToast("请先选择考试地区");
                }else {
                    UpDataMessage();
                }



                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == 1) {
////            if (resultCode == RESULT_OK && data.getExtras().get("typeText") != null) {
////                mExamTypeText = (String) data.getExtras().get("typeText");
////                mExamType.setText(mExamTypeText);
////            }
//            if (resultCode == RESULT_OK && data.getExtras().get("type_name") != null) {
//                mExamTypeText = (String) data.getExtras().get("type_name");
//                mExamType.setText(mExamTypeText);
//            }
//            if (resultCode == RESULT_OK && data.getExtras().get("type_id") != null) {
//                mExamTypeId = (String) data.getExtras().get("type_id");
//            }
//            Log.e("*********mExamTypeText", mExamTypeText + "*******mExamTypeId" + mExamTypeId);
//        } else {
//            if (resultCode == RESULT_OK && data.getExtras().get("provinceText") != null) {
//                mProvinceText = (String) data.getExtras().get("provinceText");
//                mExamair.setText(mProvinceText);
//            }
//        }
    }

    /**
     *
     */
    private PopupWindow mPopupWindow;
    private View mContentView;
    ListView mListView;
    private String type_name, type_id;
    private String mProvince;
    private ExamTypeAdapter mExamTypeAdapter;
    private ProvinceSelectAdapter mProvinceSelectAdapter;
    private TextView mTitle;
    private RelativeLayout mDelete;
    private boolean isFirstUse;

    private void showPopWindow() {

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mContentView = inflater.inflate(R.layout.list_view, null);
        mListView = (ListView) mContentView.findViewById(R.id.lv_list);
        mTitle = (TextView) mContentView.findViewById(R.id.title);
        mDelete = (RelativeLayout) mContentView.findViewById(R.id.delete);


        if (flag.equals("1")) {
            mTitle.setText("考试类型");
            mPopupWindow = new PopupWindow(mContentView, ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            mExamTypeAdapter = new ExamTypeAdapter(mContext);
            mListView.setAdapter(mExamTypeAdapter);
            if (String.valueOf(GloableConstant.getInstance().getSite())!=null){
                Log.e("*******SITE",GloableConstant.getInstance().getSite()+"////");
                mExamTypeAdapter.setSelection(GloableConstant.getInstance().getSite());
                mExamTypeAdapter.notifyDataSetChanged();
            }

        } else {
//            int screenHeigh = getResources().getDisplayMetrics().heightPixels;
//            mPopupWindow.setHeight(Math.round(screenHeigh * 0.6f));
            mTitle.setText("考试地区");
            mPopupWindow = new PopupWindow(mContentView, ViewGroup.LayoutParams.MATCH_PARENT,
                    1000);
            String fileName = "provinces.json";
            String foodJson = AllUtils.getJson(this, fileName);
            provincesResponse provincesResponse = AllUtils.JsonToObject(foodJson, provincesResponse.class);
            Log.e("*******Response", provincesResponse.getProvinces().size() + "");
            if (provincesResponse.getProvinces().size() > 0) {
                mProvinceSelectAdapter = new ProvinceSelectAdapter(mContext);
                mListView.setAdapter(mProvinceSelectAdapter);
                mProvinceSelectAdapter.setmList(provincesResponse.getProvinces());
                if (isFirstUse){
                    mProvinceSelectAdapter.setSelection(GloableConstant.getInstance().getLocation());
                    mProvinceSelectAdapter.notifyDataSetChanged();
                }


            }
        }
        mDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPopupWindow.isShowing()) {
                    mPopupWindow.dismiss();
                }
            }
        });
        mPopupWindow.setFocusable(true);// 取得焦点
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        //点击外部消失
        mPopupWindow.setOutsideTouchable(true);
        //设置可以点击
        mPopupWindow.setTouchable(true);
        setBackgroundAlpha(0.5f);//设置屏幕透明度
        mPopupWindow.showAtLocation(mContentView, Gravity.BOTTOM, 0, 0);
        mPopupWindow.setOnDismissListener(new poponDismissListener());
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (flag.equals("1")) {
                    mExamTypeAdapter.setSelection(position);
                    mExamTypeAdapter.notifyDataSetChanged();
                    ExamTypeResponse.mData mData = (ExamTypeResponse.mData) parent.getAdapter().getItem(position);
                    type_id = mData.getId();
                    type_name = mData.getName();
                    mExamType.setText(type_name);
                    GloableConstant.getInstance().setSite(position);
                   if (mPopupWindow.isShowing()){
                       mPopupWindow.dismiss();
                   }
                    Log.e("*********type", type_id + "*******" + type_name);
                } else {
                    mProvinceSelectAdapter.setSelection(position);
                    mProvinceSelectAdapter.notifyDataSetChanged();
                    provincesResponse.mProvinces provinces = (provincesResponse.mProvinces) parent.getAdapter().getItem(position);
                    mProvince = provinces.getProvinceName();
                    mExamair.setText(mProvince);
                   if (mPopupWindow.isShowing()){
                       mPopupWindow.dismiss();
                   }
                }
                GloableConstant.getInstance().setLocation(position);
//                finish();


            }
        });
    }

    /**
     * 选择考试类型
     */

    private void examType() {
        Map<String, String> map = new HashMap<>();
        GloableConstant.getInstance().startProgressDialog(this);
        Xutil.GET(ApiUrl.EXAMTYPE, map, new MyCallBack<ExamTypeResponse>() {
            @Override
            public void onSuccess(ExamTypeResponse result) {
                super.onSuccess(result);
                GloableConstant.getInstance().stopProgressDialog1();
                if (result.getStatus_code().equals("200")) {
                    if (result.getData().size() > 0) {
                        showPopWindow();
                        mExamTypeAdapter.setmList(result.getData());
                        if (isFirstUse){
                            mExamTypeAdapter.setSelection(GloableConstant.getInstance().getSite());
                            mExamTypeAdapter.notifyDataSetChanged();
                        }

                    }

                }
            }
        });
    }

    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha 屏幕透明度0.0-1.0 1表示完全不透明
     */
    public void setBackgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha;
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        getWindow().setAttributes(lp);
    }


    class poponDismissListener implements PopupWindow.OnDismissListener {

        @Override
        public void onDismiss() {

            setBackgroundAlpha(1f);
        }
    }

    private void UpDataMessage() {
        Map<String, Object> map = new HashMap<>();
        if (TextUtils.isEmpty(type_id)) {
            ToastUtil.showShortToast("请先选择考试类型");
        }
        if (TextUtils.isEmpty(mProvince)) {
            ToastUtil.showShortToast("请先选择考试地区");
        }
        map.put("user_id", SPUtils.getSpValues(SPKeyValuesUtils.SP_USER_ID, SPUtils.TYPE_STRING));
        map.put("exam_type", type_id);
        map.put("exam_area", mProvince);
        Log.e("********map", map.toString());
        GloableConstant.getInstance().startProgressDialog(this);
        Xutil.Post(ApiUrl.UPDATA, map, new MyCallBack<SupportResponse>() {
            @Override
            public void onSuccess(SupportResponse result) {
                super.onSuccess(result);
                GloableConstant.getInstance().stopProgressDialog1();
                if (result.getStatus_code().equals("204")) {
                    finish();
                    ToastUtil.showShortToast(result.getMessage());
                    intent = new Intent(ExamSubjectSelect.this, MainActivity.class);
                    startActivity(intent);
                } else {
                    ToastUtil.showShortToast(result.getMessage());
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
            }
        });
    }
}
