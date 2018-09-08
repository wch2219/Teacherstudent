package com.jiaoshizige.teacherexam.activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.jiaoshizige.teacherexam.R;
import com.jiaoshizige.teacherexam.adapter.ExamCenterAdapter;
import com.jiaoshizige.teacherexam.base.BaseActivity;
import com.jiaoshizige.teacherexam.model.ExamSubjectBean;
import com.jiaoshizige.teacherexam.utils.Sphelper;
import com.jiaoshizige.teacherexam.utils.ToastUtil;
import com.jiaoshizige.teacherexam.widgets.TImeDialog;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/a1/3.
 * 测评中心  做题 viewpager+fragment
 */

public class ExamCenterActivity extends BaseActivity implements ViewPager.OnPageChangeListener {
    @BindView(R.id.exam_viewpager)
    ViewPager mExamViewPager;
    @BindView(R.id.serial_number)
    TextView mSerialNumber;
//    @BindView(R.id.progress)
//    ImageView mProgress;
    @BindView(R.id.nextorsubmit)
    LinearLayout mNextOrSunmit;
    @BindView(R.id.submit)
    Button mSubmit;
    @BindView(R.id.last0rnext)
    LinearLayout mLastOrNext;
    @BindView(R.id.last)
    Button mLast;
    @BindView(R.id.next)
    Button mNext;
    @BindView(R.id.seekBar)
    SeekBar mSeekbar;

    private ExamCenterAdapter mAdapter;
    private String mProvince;
    private String mProvinceStr;//传参的省份
    private String mGraduationTime;
    private int mPsition;
//    private List<String> mList = new ArrayList<>();
    private Intent intent;
    private int hasdo = 0;

    @Override
    protected int getLayoutId() {
        return R.layout.exam_center_layout;
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("测评中心");
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("测评中心");
        MobclickAgent.onPause(this);
    }

    @Override
    protected void initView() {
        setToolbarTitle().setText("学前向导测评");
        setSubTitle().setText("");
        mSeekbar.setEnabled(false);
        mSeekbar.setMax(13);
        mSeekbar.setProgress(1);
        mAdapter = new ExamCenterAdapter(this);
        mAdapter.setmList(ExamSubjectBean.bean());
        mExamViewPager.setAdapter(mAdapter);
        mExamViewPager.addOnPageChangeListener(this);
        mExamViewPager.setOffscreenPageLimit(13);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        mPsition = position;
        mSerialNumber.setText((position + 1) + "/13");
        mSeekbar.setProgress((position + 1));
        if (position == 0) {
            mLastOrNext.setVisibility(View.GONE);
            mNextOrSunmit.setVisibility(View.VISIBLE);
            mSubmit.setText("下一题");
        } else if (position == 12) {
            mLastOrNext.setVisibility(View.GONE);
            mNextOrSunmit.setVisibility(View.VISIBLE);
            mSubmit.setText("提交");
        } else {
            mLastOrNext.setVisibility(View.VISIBLE);
            mNextOrSunmit.setVisibility(View.GONE);
            mLast.setText("上一题");
            mNext.setText("下一题");
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            if (resultCode == RESULT_OK && data.getExtras().get("provinceText") != null) {
                String str = (String) data.getExtras().get("province");
                String province;
                if(str.equals("黑龙江省")){
                    province = "黑龙江";
                }else if(str.equals("广西壮族自治区")){
                    province = "广西";
                }else if(str.equals("宁夏回族自治区")){
                    province = "宁夏";
                }else{
                    province = str.substring(0,2);
                }
                setmProvince((String) data.getExtras().get("provinceText"));
                setmProvinceStr(province);
                mAdapter.notifyDataSetChanged();
            }
        }
    }

    public String getmGraduationTime() {
        return mGraduationTime;
    }

    public void setmGraduationTime(String mGraduationTime) {
        this.mGraduationTime = mGraduationTime.substring(0, mGraduationTime.length() - 2);
    }

    public String getmProvinceStr() {
        return mProvinceStr;
    }

    public void setmProvinceStr(String mProvinceStr) {
        this.mProvinceStr = mProvinceStr;
    }

    public String getmProvince() {
        return mProvince;
    }

    public void setmProvince(String mProvince) {
        this.mProvince = mProvince;
    }

    @OnClick({R.id.submit, R.id.last, R.id.next})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.submit:
                if (mPsition == 0) {
                    mExamViewPager.setCurrentItem(1);
                } else {
                    //tijiao
                    ToastUtil.showShortToast("提交数据");
                    for (int i = 0; i < mAdapter.getOPtions().length; i++) {

                        if (mAdapter.getOPtions()[i] == null|| mAdapter.getOPtions()[i].equals("")) {
                            ToastUtil.showShortToast("您有未选择的题目");
//                            return;
                        } else {
                            hasdo++;
                            Log.e("TAGx",hasdo+"");
                        }

                    }
                    if(hasdo == 13){
                     ArrayList<String>  mList = new ArrayList<>(Arrays.asList(mAdapter.getOPtions()));
//                        mList.add(mAdapter.getOPtions()[i].toString());
                        intent=new Intent();
                        intent.setClass(this, ExamCompleteActivity.class);
                        intent.putStringArrayListExtra("mList",mList);
                        Sphelper.save(ExamCenterActivity.this,"hadexam","isexam",true);
                        startActivity(intent);
                        finish();
                    }
                }
                break;
            case R.id.last:
                mExamViewPager.setCurrentItem(mPsition - 1);
                break;
            case R.id.next:
                mExamViewPager.setCurrentItem(mPsition + 1);
                break;

        }
    }

    public void showDialog() {
        final TImeDialog.Builder builder = new TImeDialog.Builder(this);
        builder.setPositiveButton(new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                setmGraduationTime(builder.getStr());
                mAdapter.notifyDataSetChanged();
            }
        });

        builder.setNegativeButton(new android.content.DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        showDialog(builder);
    }

    public void showDialog(TImeDialog.Builder builder) {
        Dialog dialog = builder.create();
        Window dialogWindow = dialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setGravity(Gravity.CENTER);
        WindowManager m = getWindowManager();
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
        WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        p.width = (int) (d.getWidth() * 0.75); // 宽度设置为屏幕的0.65
        dialogWindow.setAttributes(p);
        dialog.show();
    }

}
