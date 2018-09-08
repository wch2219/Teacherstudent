package com.jiaoshizige.teacherexam.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jiaoshizige.teacherexam.R;
import com.jiaoshizige.teacherexam.activity.LuckDrawActivity;
import com.jiaoshizige.teacherexam.activity.RuleDescriptionActuivity;
import com.jiaoshizige.teacherexam.adapter.SignAdapter;
import com.jiaoshizige.teacherexam.base.GloableConstant;
import com.jiaoshizige.teacherexam.http.ApiUrl;
import com.jiaoshizige.teacherexam.http.MyCallBack;
import com.jiaoshizige.teacherexam.http.Xutil;
import com.jiaoshizige.teacherexam.model.SignDayResponse;
import com.jiaoshizige.teacherexam.model.SignDialogResponse;
import com.jiaoshizige.teacherexam.utils.SPKeyValuesUtils;
import com.jiaoshizige.teacherexam.utils.SPUtils;
import com.jiaoshizige.teacherexam.utils.ToastUtil;
import com.jiaoshizige.teacherexam.widgets.HorizontalListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/a1/8.
 * 签到dialog
 */

public class SignDialogFragmnet extends DialogFragment {
    private Activity mContext;
    public static final String REQUESE="RESOIBSE";
    private LinearLayout mClose;
    private ImageView mPhoto;
    private TextView mTotalCoin;
    private HorizontalListView mSignDateListView;
    private TextView mSignedDays;
    private TextView mRemainDays;
    private TextView mSignRules;
    private SignAdapter mSignAdapter;
    private int mSignNum = 0;
    private int mCionNum = 0;
    private List<SignDialogResponse.mWeeks> mList = new ArrayList<>();
    @SuppressLint("ValidFragment")
    public SignDialogFragmnet(Activity context) {
        this.mContext = context;
    }

    public SignDialogFragmnet() {
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getDialog().setCanceledOnTouchOutside(true);
        View dialogView = LayoutInflater.from(getActivity()).inflate(R.layout.sign_dialog, container);
        mClose = (LinearLayout) dialogView.findViewById(R.id.close_sign);
        mTotalCoin = (TextView) dialogView.findViewById(R.id.total_coin);
        mPhoto = (ImageView) dialogView.findViewById(R.id.photo);
        mSignDateListView = (HorizontalListView) dialogView.findViewById(R.id.sign_date);
        mSignedDays = (TextView) dialogView.findViewById(R.id.signed_days);
        mRemainDays = (TextView) dialogView.findViewById(R.id.remain_days);
        mSignRules = (TextView) dialogView.findViewById(R.id.sing_rules);
        mClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                GloableConstant.getInstance().setmSignFlag(1);
                if (getTargetFragment() != null) {
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra(REQUESE,"ok");
                    getTargetFragment().onActivityResult(StudyFragment.REUEST_CODDE,
                            Activity.RESULT_OK,
                            resultIntent);
                }
            }
        });
        mSignRules.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), RuleDescriptionActuivity.class);
                intent.putExtra("flag","sign");
                getActivity().startActivity(intent);
            }
        });
        mRemainDays.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                requestSign();
            }
        });

        mSignAdapter = new SignAdapter(mContext);
        mSignDateListView.setAdapter(mSignAdapter);
        return dialogView;
    }

    @Override
    public void onResume() {
        super.onResume();
        requestSignDialog();
    }

    public void requestSignDialog(){
        Map<String,Object> map = new HashMap<>();
//        map.put("user_id","1");
        map.put("user_id",  SPUtils.getSpValues(SPKeyValuesUtils.SP_USER_ID, SPUtils.TYPE_STRING));
        Xutil.Post(ApiUrl.SIGNDIALOG,map,new MyCallBack<SignDialogResponse>(){
            @Override
            public void onSuccess(SignDialogResponse result) {
                super.onSuccess(result);
                if(result.getStatus_code().equals("200")){

                    if(!result.getData().getJiang_num().equals("0")){
                        mRemainDays.setText("抽奖"+"("+result.getData().getJiang_num()+")");
                        mRemainDays.setTextColor(ContextCompat.getColor(mContext,R.color.white));
                        mRemainDays.setClickable(true);
                        mRemainDays.setBackground(ContextCompat.getDrawable(mContext,R.drawable.orange_shape));
                        mRemainDays.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                ToastUtil.showShortToast("去抽奖");
                                Intent intent = new Intent();
                                intent.putExtra("user_id",(String) SPUtils.getSpValues(SPKeyValuesUtils.SP_USER_ID, SPUtils.TYPE_STRING));
                                intent.setClass(getActivity(), LuckDrawActivity.class);
                                getActivity().startActivity(intent);
                                Log.e("IIII","eeeee");
                            }
                        });
                    }else{
                        if(Integer.valueOf(result.getData().getDays()) % 7 == 0){
                            mRemainDays.setText("抽奖");
                            mRemainDays.setClickable(true);
                            mRemainDays.setBackground(ContextCompat.getDrawable(mContext,R.drawable.orange_shape));
                        }else{
                            mRemainDays.setText("连续签到参与抽奖");
                            mRemainDays.setClickable(false);
                        }
                    }
                   /* if(Integer.valueOf(result.getData().getDays()) % 7 == 0){
                        if(result.getData().getIs_sign().equals("1")){
                            mRemainDays.setText("领取教师币成功");
                            mRemainDays.setTextColor(ContextCompat.getColor(mContext,R.color.text_color6));
                            mRemainDays.setBackground(ContextCompat.getDrawable(mContext,R.drawable.gray_shap_5));
                            mRemainDays.setClickable(false);
                        }else{
                            mRemainDays.setText("领取教师币");
                            mRemainDays.setClickable(true);
                        }
                    }else{
                        if(result.getData().getIs_sign().equals("1")){
                            mRemainDays.setText("签到成功");
                            mRemainDays.setTextColor(ContextCompat.getColor(mContext,R.color.text_color6));
                            mRemainDays.setBackground(ContextCompat.getDrawable(mContext,R.drawable.gray_shap_5));
                            mRemainDays.setClickable(false);
                        }else{
                            mRemainDays.setText("点击签到");
                            mRemainDays.setClickable(true);
                        }

                    }*/
                    mList = result.getData().getWeeks();
                    mSignAdapter.setmList(mList);
                    mCionNum = Integer.valueOf(result.getData().getIntegral());
                    mTotalCoin.setText("我的教师币 "+result.getData().getIntegral());
                    mSignNum = Integer.valueOf(result.getData().getDays());
//                    mSignedDays.setText("连续签到还差"+(7 - Integer.valueOf(result.getData().getDays()))+"天");
                    mSignedDays.setText("已连续签到"+(Integer.valueOf(result.getData().getDays()))+"天");
                    Glide.with(mContext).load(result.getData().getAvatar()).apply(GloableConstant.getInstance().getOptions()).into(mPhoto);
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
            }
        });
    }

    /**
     * 签到
     */
    public void requestSign(){
        Map<String,Object> map = new HashMap<>();
//        map.put("user_id","1");
        map.put("user_id",  SPUtils.getSpValues(SPKeyValuesUtils.SP_USER_ID, SPUtils.TYPE_STRING));
        Xutil.Post(ApiUrl.USERSIGN,map,new MyCallBack<SignDayResponse>(){
            @Override
            public void onSuccess(SignDayResponse result) {
                super.onSuccess(result);
                if(result.getStatus_code().equals("204")){
                    if(Integer.valueOf(result.getData().getSign_num()) % 7 == 0){
                        mRemainDays.setText("抽奖");
                    }else{
                        mRemainDays.setText("签到成功");
                    }
                    mList.get(Integer.valueOf(result.getData().getSign_num()) - 1).setIs_sign("1");
                    mSignAdapter.notifyDataSetChanged();
                    mTotalCoin.setText("我的教师币 "+(mCionNum + 1));
                    mSignedDays.setText("已经连续签到"+(mSignNum + 1)+"天");
                    mRemainDays.setTextColor(ContextCompat.getColor(mContext,R.color.text_color6));
                    mRemainDays.setBackground(ContextCompat.getDrawable(mContext,R.drawable.gray_shap_5));
                }else{
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
