package com.jiaoshizige.teacherexam.activity;

import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.util.SparseArray;
import android.widget.TextView;

import com.jiaoshizige.teacherexam.R;
import com.jiaoshizige.teacherexam.adapter.ViewPagerAdapter;
import com.jiaoshizige.teacherexam.base.BaseActivity;
import com.jiaoshizige.teacherexam.base.GloableConstant;
import com.jiaoshizige.teacherexam.fragment.SubjectFragment;
import com.jiaoshizige.teacherexam.http.ApiUrl;
import com.jiaoshizige.teacherexam.http.MyCallBack;
import com.jiaoshizige.teacherexam.http.SupportResponse;
import com.jiaoshizige.teacherexam.http.Xutil;
import com.jiaoshizige.teacherexam.model.AddLearnRecordModel;
import com.jiaoshizige.teacherexam.model.PracticeResponse;
import com.jiaoshizige.teacherexam.utils.SPKeyValuesUtils;
import com.jiaoshizige.teacherexam.utils.SPUtils;
import com.jiaoshizige.teacherexam.utils.ToastUtil;
import com.jiaoshizige.teacherexam.widgets.HorizonalPrograssBarWithPrograss;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/1/5 0005.
 * 练习
 */

public class PracticeActivity extends BaseActivity implements ViewPager.OnPageChangeListener {
    @BindView(R.id.all_topic)
    TextView mAllNum;
    @BindView(R.id.three_topic)
    TextView mThreeNum;
    @BindView(R.id.four_topic)
    TextView mFourNum;
    @BindView(R.id.five_topic)
    TextView mAllTopic;
    @BindView(R.id.progress)
    HorizonalPrograssBarWithPrograss mProgress;
    @BindView(R.id.view_pager)
    ViewPager mViewPager;
    List<Fragment> mViewList;
    SubjectFragment mSubjectFragment;
    private String user_id;
    private ViewPagerAdapter mAdapter;
    private int size;//题目的总数量
   // int status;
    private String mPaperId;
    private int isLearnTime = 0;
    private int isAdd = 0;
    Handler handler = new Handler();
    private String tag;

    private AddLearnRecordModel mModel = new AddLearnRecordModel();
    // private SparseArray<String> mRecordAnswerMap = new SparseArray<>();
    private int correctNum = 0;//正确题目的数量
    private int totalnum = 0;//已做题目的数量

    @Override
    protected int getLayoutId() {
        return R.layout.practice_activity_layout;
    }

    @Override
    protected void initView() {
        setSubTitle().setText("");
//        setSubTitle().setBackgroundResource(R.mipmap.share_icon);//没有分享页面 去掉吧  不知道分享什么
        setToolbarTitle().setText("练习");
//        EventBus.getDefault().register(this);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        if (getIntent().getExtras().get("mPaperId") != null) {
            mPaperId = (String) getIntent().getExtras().get("mPaperId");
        } else {
            mPaperId = "";
        }
        if (getIntent().getExtras().get("model") != null) {

            mModel = (AddLearnRecordModel) getIntent().getExtras().get("model");
            isLearnTime = Integer.valueOf(mModel.getLearn_time()) * 60 * 1000;
            Log.e("TTT", mModel.getCourse_name() + isLearnTime);
        }
        user_id = (String) SPUtils.getSpValues(SPKeyValuesUtils.SP_USER_ID, SPUtils.TYPE_STRING);
        mViewPager.addOnPageChangeListener(this);
        GloableConstant.getInstance().setStatus(0);
        getPaper();
//        handler.postDelayed(runnable, isLearnTime);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {

//        GloableConstant.getInstance().setPosition(position);
//        mAllNum.setText((size - (position + 1)) + "题");
//        status = GloableConstant.getInstance().getStatus();
//        Log.e("**********getDone", GloableConstant.getInstance().getDone());
//        if (GloableConstant.getInstance().getDone().equals("0")) {
//            GloableConstant.getInstance().setCorrect(false);
//        } else {
//            if (GloableConstant.getInstance().isCorrect()) {
//                status = status + 1;
//                mProgress.setProgress(status);
//                GloableConstant.getInstance().setStatus(status);
//            } else {
//                mProgress.setProgress(status);
//            }
//        }

    }

    public void setLastPagerStatus(boolean isCorrect) {
        totalnum++;
        if (isCorrect) {
            correctNum++;
        }
        int shengyuNum = size - totalnum;
        if (shengyuNum < 0) {
            shengyuNum = 0;
        }
        mAllNum.setText(shengyuNum + "题");
        mProgress.setProgress(correctNum);
        if (size == totalnum) {
            addLearnRecord();
        }

    }

    @Override
    public void onPageScrollStateChanged(int state) {

        Log.e("*onScrollStateChanged", state + "");
    }

    private void getPaper() {
        Map<String, String> map = new HashMap<>();
        map.put("user_id", (String) SPUtils.getSpValues(SPKeyValuesUtils.SP_USER_ID, SPUtils.TYPE_STRING));
        map.put("paper_id", mPaperId);
        GloableConstant.getInstance().showmeidialog(this);
        Log.e("**********map", map.toString());
        Xutil.GET(ApiUrl.PAPER, map, new MyCallBack<PracticeResponse>() {
            @Override
            public void onSuccess(PracticeResponse result) {
                super.onSuccess(result);
                GloableConstant.getInstance().stopProgressDialog();
                Log.e("**********code", result.getStatus_code());
                if (result.getStatus_code().equals("200")) {
                    Log.e("**********size", result.getData().getQuestions().size() + "");
                    if (result.getData().getQuestions().size() > 0) {
                        mProgress.setMax(result.getData().getQuestions().size());
                        mProgress.setProgress((int) 0.5);
                        size = result.getData().getQuestions().size();
                        mAllNum.setText(size + "题");
                        mAllTopic.setText(result.getData().getQuestions().size() + "");
                        float three = Float.valueOf(result.getData().getQuestions().size()) / 5 * 3;
                        float four = Float.valueOf(result.getData().getQuestions().size()) / 5 * 4;
                        mThreeNum.setText(Math.round(three) + "");
                        mFourNum.setText(Math.round(four) + "");
                        mViewList = new ArrayList<>();
                        for (int i = 0; i < result.getData().getQuestions().size(); i++) {
                            mSubjectFragment = new SubjectFragment(PracticeActivity.this, result.getData().getQuestions().get(i), size, i);
                            mViewList.add(mSubjectFragment);
                        }
                        mAdapter = new ViewPagerAdapter(getSupportFragmentManager(), mViewList);
                        mViewPager.setAdapter(mAdapter);
                        mAdapter.notifyDataSetChanged();
                    }


                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
                GloableConstant.getInstance().stopProgressDialog();
                ToastUtil.showShortToast(ex.getMessage());
//                Log.e("******ex.getMessage()",ex.getMessage());

            }

            @Override
            public void onFinished() {
                super.onFinished();
                GloableConstant.getInstance().stopProgressDialog();
            }
        });

    }

//    Runnable runnable = new Runnable() {
//        @Override
//        public void run() {
//            // TODO Auto-generated method stub
//            //要做的事情
//            try {
//                addLearnRecord();
//                handler.postDelayed(this, 2000);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    };

    public void addLearnRecord() {
        Map<String, Object> map = new HashMap<>();
//        map.put("user_id","1");
        map.put("user_id", SPUtils.getSpValues(SPKeyValuesUtils.SP_USER_ID, SPUtils.TYPE_STRING));
        map.put("type", "2");
        map.put("group_id", mModel.getGroup_id());
        map.put("course_id", mModel.getCourse_id());
        map.put("course_name", mModel.getCourse_name());
        map.put("chapter_id", mModel.getChapter_id());//章id
        map.put("chapter_name", mModel.getChapter_name());//章名称
        map.put("section_id", mModel.getSection_id());//节id
        map.put("section_name", mModel.getSection_name());//节名称
        map.put("type_id", mModel.getType());//1班级2课程
        map.put("ltime", "1");
        Log.e("TTT", "ok" + map.toString());
//        map.put("learn_time",mModel.getLearn_time());////学习完成时间：分钟
        Xutil.Post(ApiUrl.AddLEARNRECORD, map, new MyCallBack<SupportResponse>() {
            @Override
            public void onSuccess(SupportResponse result) {
                super.onSuccess(result);
//                handler.removeCallbacks(runnable);
                if (result.getStatus_code().equals("204")) {
                    //成功
                    isAdd = 0;
                } else if (result.getStatus_code().equals("205")) {
                    //重复提交
                    isAdd = 0;
                } else if (result.getStatus_code().equals("206")) {
                    //提交失败
                    isAdd = 1;
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
//                handler.removeCallbacks(runnable);
            }
        });
    }
}
