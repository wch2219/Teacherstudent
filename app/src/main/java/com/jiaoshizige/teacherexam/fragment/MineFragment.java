package com.jiaoshizige.teacherexam.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jiaoshizige.teacherexam.R;
import com.jiaoshizige.teacherexam.activity.AboutUsActivity;
import com.jiaoshizige.teacherexam.activity.AccountSecurityActivity;
import com.jiaoshizige.teacherexam.activity.ExamSubjectSelect;
import com.jiaoshizige.teacherexam.activity.LoginActivity;
import com.jiaoshizige.teacherexam.activity.MineCourseActivityOne;
import com.jiaoshizige.teacherexam.activity.MyCollectionActivity;
import com.jiaoshizige.teacherexam.activity.MyOrderActivity;
import com.jiaoshizige.teacherexam.activity.MyQuestionBankActivity;
import com.jiaoshizige.teacherexam.activity.NewGiftPackActivity;
import com.jiaoshizige.teacherexam.activity.OpinionTicklingActivity;
import com.jiaoshizige.teacherexam.activity.ShareActivity;
import com.jiaoshizige.teacherexam.activity.SystemSettingActivity;
import com.jiaoshizige.teacherexam.activity.TeacherCoinActivity;
import com.jiaoshizige.teacherexam.activity.aaa.xx.activity.DownVideosMangerActivity;
import com.jiaoshizige.teacherexam.base.GloableConstant;
import com.jiaoshizige.teacherexam.base.MBaseFragment;
import com.jiaoshizige.teacherexam.http.ApiUrl;
import com.jiaoshizige.teacherexam.http.MyCallBack;
import com.jiaoshizige.teacherexam.http.Xutil;
import com.jiaoshizige.teacherexam.model.PersonMessageResponse;
import com.jiaoshizige.teacherexam.palyer.PolyvDownloadSQLiteHelper;
import com.jiaoshizige.teacherexam.utils.SPKeyValuesUtils;
import com.jiaoshizige.teacherexam.utils.SPUtils;
import com.jiaoshizige.teacherexam.utils.Sphelper;
import com.jiaoshizige.teacherexam.utils.ToastUtil;
import com.jiaoshizige.teacherexam.yy.activity.AddressListActivity;
import com.jiaoshizige.teacherexam.yy.activity.MyCouponActivity;
import com.jiaoshizige.teacherexam.yy.activity.MyOpenClassActivity;
import com.jiaoshizige.teacherexam.zz.utils.NetworkUtils;
import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/10/27.
 */
//  depingwangchengactivity
public class MineFragment extends MBaseFragment implements View.OnClickListener {
    @BindView(R.id.login_icon_jt_ID)
    RelativeLayout mine_id;
    @BindView(R.id.my_collection)
    RelativeLayout mCollection;
    @BindView(R.id.system_setting)
    ImageView mSystem;
    @BindView(R.id.my_interest)
    RelativeLayout mInterest;
    @BindView(R.id.teacher_coin)
    RelativeLayout mTeacher;
    @BindView(R.id.mine_coupon)
    RelativeLayout mCoupon;
    @BindView(R.id.my_course)
    RelativeLayout mCourse;
    @BindView(R.id.my_order)
    RelativeLayout mOrder;
    @BindView(R.id.avar)
    ImageView mAvar;
    @BindView(R.id.nick_name)
    TextView mNickName;
    @BindView(R.id.study_day)
    TextView mStudy;
    @BindView(R.id.perform)
    TextView mPerform;
    @BindView(R.id.coin)
    TextView mCoin;
    @BindView(R.id.my_address)
    RelativeLayout mAddress;
    @BindView(R.id.my_appraisal)
    RelativeLayout mAppraisal;
    @BindView(R.id.my_open_class)
    RelativeLayout mOpenClass;
    @BindView(R.id.mine_feedback)
    RelativeLayout mFeedBack;
    @BindView(R.id.my_recommend)
    RelativeLayout mRecommend;
    @BindView(R.id.about_us)
    RelativeLayout mAboutUs;
    @BindView(R.id.down_course)
    RelativeLayout mDown;
    @BindView(R.id.updowm)
    TextView mUpdown;
    @BindView(R.id.credit_score)
    TextView mCreditScore;
    @BindView(R.id.question)
    RelativeLayout mQuestion;

    private Intent intent;
    private String tag = "1", user_id;
    private String isLogin;
    private String teacherCoin;
    private String wifi_switch = "1";
    private String push_switch = "1";
    private String exam_type, mExamTypeId;
    private String exam_area;


    private static PolyvDownloadSQLiteHelper downloadSQLiteHelper;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.main_my_activityone, container, false);
    }


    @Override
    protected void onInitViews(Bundle savedInstanceState) {
        user_id = (String) SPUtils.getSpValues(SPKeyValuesUtils.SP_USER_ID, SPUtils.TYPE_STRING);
        downloadSQLiteHelper = PolyvDownloadSQLiteHelper.getInstance(getActivity());


        mine_id.setOnClickListener(this);
        mCollection.setOnClickListener(this);
        mSystem.setOnClickListener(this);
        mInterest.setOnClickListener(this);
        mTeacher.setOnClickListener(this);
        mCoupon.setOnClickListener(this);
        mCourse.setOnClickListener(this);
        mOrder.setOnClickListener(this);
        mAddress.setOnClickListener(this);
        mAppraisal.setOnClickListener(this);
        mOpenClass.setOnClickListener(this);
        mFeedBack.setOnClickListener(this);
        mRecommend.setOnClickListener(this);
        mAboutUs.setOnClickListener(this);
        mDown.setOnClickListener(this);
        mQuestion.setOnClickListener(this);
        GainMessage();


    }

    @Override
    public void onResume() {
        super.onResume();
        if (downloadSQLiteHelper.getTotal() != null) {
            mUpdown.setText(downloadSQLiteHelper.getTotal() + "M");
        }
        isLogin = (String) SPUtils.getSpValues(SPKeyValuesUtils.SP_IS_LOGIN, SPUtils.TYPE_STRING);

        MobclickAgent.onPageStart("我的");

        if (NetworkUtils.isNetworkConnected(getActivity())) {
            GainMessage();
        } else {
            mNickName.setText(String.valueOf(SPUtils.getSpValues("userName", SPUtils.TYPE_STRING)));
        }

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            if (isLogin.equals("0")) {
                mAvar.setImageResource(R.mipmap.login_icon_headportraitr_default);
                mNickName.setText("中冠网校");
                mStudy.setText("0");
                mPerform.setText("0");
                mCoin.setText("0");
                mUpdown.setText("0");
                mCoin.setText("0");
            } else {
                GainMessage();
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("我的");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_icon_jt_ID:
//                intent = new Intent(getActivity(), MyAcccountActivity.class);
//                startActivityForResult(intent, 1);
                intent = new Intent(getActivity(), AccountSecurityActivity.class);
                startActivity(intent);
                break;
            case R.id.my_collection:
                intent = new Intent(getActivity(), MyCollectionActivity.class);
                startActivity(intent);
                break;
            case R.id.system_setting:
                intent = new Intent(getActivity(), SystemSettingActivity.class);
                intent.putExtra("push_switch", push_switch);
                intent.putExtra("wifi_switch", wifi_switch);
                intent.putExtra("exam_type", exam_type);
                intent.putExtra("exam_area", exam_area);
                intent.putExtra("exam_type_id", mExamTypeId);
                startActivity(intent);
                break;
            case R.id.my_interest:
                intent = new Intent(getActivity(), ExamSubjectSelect.class);
                intent.putExtra("tag", tag);
                intent.putExtra("exam_type", exam_type);
                intent.putExtra("exam_area", exam_area);
                intent.putExtra("exam_type_id", mExamTypeId);
                startActivity(intent);
                break;
            case R.id.teacher_coin:
                intent = new Intent(getActivity(), TeacherCoinActivity.class);
                intent.putExtra("teacherCoin", teacherCoin);
                startActivity(intent);
                break;
            case R.id.mine_coupon:
                intent = new Intent(getActivity(), MyCouponActivity.class);
                startActivity(intent);
                break;
            case R.id.my_course:
//                intent = new Intent(getActivity(), MineCourseActivity.class);
                intent = new Intent(getActivity(), MineCourseActivityOne.class);
                startActivity(intent);
                break;
            case R.id.my_order:
                intent = new Intent(getActivity(), MyOrderActivity.class);
                startActivity(intent);
                break;
            case R.id.my_address:
                intent = new Intent(getActivity(), AddressListActivity.class);
                startActivity(intent);
                break;
            case R.id.my_appraisal:
                intent = new Intent();
                intent.setClass(getActivity(), NewGiftPackActivity.class);
                intent.putExtra("tag", "1");
                startActivity(intent);
                break;
            case R.id.my_open_class:
                intent = new Intent();
                intent.setClass(getActivity(), MyOpenClassActivity.class);
                startActivity(intent);
                break;
            case R.id.mine_feedback:
                intent = new Intent(getActivity(), OpinionTicklingActivity.class);
                startActivity(intent);
                break;
            case R.id.my_recommend:
                intent = new Intent(getActivity(), ShareActivity.class);
                startActivity(intent);
                break;
            case R.id.about_us:
                intent = new Intent(getActivity(), AboutUsActivity.class);
                startActivity(intent);
                break;
            case R.id.down_course:
//                intent = new Intent(getActivity(), DownLoadActivity.class);
                intent = new Intent(getActivity(), DownVideosMangerActivity.class);
                startActivity(intent);
                break;
            case R.id.question:
                startActivity(new Intent(getActivity(), MyQuestionBankActivity.class));
                break;
            default:
                break;
        }

    }

    /*获取信息*/
    private void GainMessage() {
        Map<String, String> map = new HashMap<>();
//        map.put("user_id", "1");
        map.put("user_id", (String) SPUtils.getSpValues(SPKeyValuesUtils.SP_USER_ID, SPUtils.TYPE_STRING));
        GloableConstant.getInstance().startProgressDialog(getActivity());
        Log.e("*********map", map.toString());
        Xutil.GET(ApiUrl.USER, map, new MyCallBack<PersonMessageResponse>() {
            @Override
            public void onSuccess(PersonMessageResponse result) {
                super.onSuccess(result);
                GloableConstant.getInstance().stopProgressDialog1();
                Log.e("*********result", result.getStatus_code());
                if (result.getStatus_code().equals("200")) {
                    Glide.with(getActivity()).load(result.getData().getAvatar())
                            .apply(GloableConstant.getInstance().getOptions()).into(mAvar);
                    mNickName.setText(result.getData().getNick_name());
                    mStudy.setText(result.getData().getSign_num());
                    mPerform.setText(result.getData().getTask_num());
                    mCoin.setText("余额：" + result.getData().getIntegral());
                    teacherCoin = result.getData().getIntegral();
                    mCreditScore.setText(result.getData().getCredit());
                    push_switch = result.getData().getPush_switch();
                    wifi_switch = result.getData().getWifi_switch();
                    exam_type = result.getData().getExam_type();
                    exam_area = result.getData().getExam_area();
                    mExamTypeId = result.getData().getExam_type_id();
                    Sphelper.save(getActivity(), "NICKNAME", "nick_name", result.getData().getNick_name());
                    Sphelper.save(getActivity(), "AVATAR", "avatar", result.getData().getAvatar());

                } else if (result.getStatus_code().equals("401")) {
                    GloableConstant.getInstance().clearAll();
                    ToastUtil.showShortToast("请重新登录");
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                }
            }

            @Override
            public void onFinished() {
                super.onFinished();
                GloableConstant.getInstance().stopProgressDialog();
            }
        });


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == 2) {
                getActivity().finish();
            }
        }
    }
}
