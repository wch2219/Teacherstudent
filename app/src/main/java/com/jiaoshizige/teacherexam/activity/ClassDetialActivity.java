package com.jiaoshizige.teacherexam.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.bumptech.glide.Glide;
import com.jiaoshizige.teacherexam.R;
import com.jiaoshizige.teacherexam.adapter.TabPagerAdapter;
import com.jiaoshizige.teacherexam.base.BaseActivity;
import com.jiaoshizige.teacherexam.base.GloableConstant;
import com.jiaoshizige.teacherexam.fragment.ClassIntroduceFragment;
import com.jiaoshizige.teacherexam.yy.fragment.EvaluateFragment;
import com.jiaoshizige.teacherexam.fragment.NewCourseCatalogFragmnet;
import com.jiaoshizige.teacherexam.http.ApiUrl;
import com.jiaoshizige.teacherexam.http.MyCallBack;
import com.jiaoshizige.teacherexam.http.SupportResponse;
import com.jiaoshizige.teacherexam.http.Xutil;
import com.jiaoshizige.teacherexam.model.ClassDetailResponse;
import com.jiaoshizige.teacherexam.utils.SPKeyValuesUtils;
import com.jiaoshizige.teacherexam.utils.SPUtils;
import com.jiaoshizige.teacherexam.utils.ToastUtil;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/a1/20.
 * 班级详情(废弃)
 */
public class ClassDetialActivity extends BaseActivity implements ViewPager.OnPageChangeListener {
    @BindView(R.id.back)
    ImageView mBack;
    @BindView(R.id.share)
    ImageView mShare;
    @BindView(R.id.class_cover)
    ImageView mClassCover;
    @BindView(R.id.class_title)
    TextView mClassTitle;
    @BindView(R.id.class_price)
    TextView mClassPrice;
    @BindView(R.id.class_original_price)
    TextView mClassOriginalPrice;
    @BindView(R.id.learn_num)
    TextView mLearnNum;
    @BindView(R.id.class_hour)
    TextView mClassHour;
    @BindView(R.id.give_book)
    TextView mGIveBook;
    /* @BindView(R.id.activity_ly)
     LinearLayout mActivityLy;*/
    @BindView(R.id.activity_first)
    TextView mActivityFirst;
    @BindView(R.id.first_icon)
    ImageView mFirstIcon;
    /*  @BindView(R.id.second_icon)
      ImageView mSecondIcon;*/
    @BindView(R.id.activity_num)
    TextView mActivityNum;
    @BindView(R.id.activity_switch)
    ToggleButton mActivitySwitch;
    /* @BindView(R.id.second_activity_ly)
     LinearLayout mSecondActivityLy;*/
  /*  @BindView(R.id.activity_second)
    TextView mActivitySecond;
    @BindView(R.id.hide_activity)
    RecyclerView mHideActivity;*/
    @BindView(R.id.tablayout)
    TabLayout mTabLayout;
    @BindView(R.id.viewpager)
    ViewPager mViewPager;
    @BindView(R.id.buy_bottom)
    LinearLayout mBuyBottom;
    @BindView(R.id.consultation)
    LinearLayout mConsultation;
    @BindView(R.id.collection)
    LinearLayout mCollection;
    @BindView(R.id.collect_img)
    ImageView mCollectImg;
    @BindView(R.id.all_add_buy)
    TextView mAddBuy;
    @BindView(R.id.effective)
    TextView mEffective;
    @BindView(R.id.effective_ly)
    LinearLayout mEffectiveLy;
    @BindView(R.id.activity_rl)
    LinearLayout mActivityRl;
//    @BindView(R.id.single_add_buy)
//    TextView mSingleAddBuy;
    private List<Fragment> mFragments;
    private List<String> tabTitles;
    private TabPagerAdapter pagerAdapter;
    private String mGroupId;//班级id
    private String collectType;
    private String mActivity_id, mGroup_id;
    private ClassDetialActivity mActivity;
    private List<ClassDetailResponse.mActivity> activity;
    private int mFlag = 0;//标记 0元 vip  精选
    private String mType;   //1班级2课程
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    protected int getLayoutId() {
        return R.layout.class_detial_activity;
    }
    @Override
    protected void initView() {
        if (getIntent().getExtras().get("class_id") != null) {
            mGroupId = (String) getIntent().getExtras().get("class_id");
            Log.e("**********bookid", mGroupId);
            //班级id
        } else {
            mGroupId = "";
        }
        if(getIntent().getExtras().get("position") != null){
            mFlag = (int) getIntent().getExtras().get("position");
//            if(mFlag == 0){
//                mSingleAddBuy.setVisibility(View.GONE);
//            }else{
//                mSingleAddBuy.setVisibility(View.VISIBLE);
//            }
        }
        if(getIntent().getExtras().get("type") != null){
            mType = (String) getIntent().getExtras().get("type");
        }else{
            mType = "";
        }
        mActivity = this;
        requestClassDetail();
        mFragments = new ArrayList<>();
        tabTitles = new ArrayList<>();
        tabTitles.add("介绍");
        tabTitles.add("目录");
        tabTitles.add("评价");
        mFragments.add(new ClassIntroduceFragment("1", mGroupId));
      /*  if(mType.equals("2")){
            mFragments.add(new CourseCatalogFragmnet("",mGroupId,0,"2"));
        }else{
            mFragments.add(new NewCourseCatalogFragmnet(mGroupId,mType));
        }*/
        mFragments.add(new NewCourseCatalogFragmnet(mGroupId,mType));
//        mFragments.add(new DetialCourseFragment("2", mGroupId));

        mFragments.add(new EvaluateFragment(mGroupId, "3"));
        for (int i = 0; i < tabTitles.size(); i++) {
            mTabLayout.addTab(mTabLayout.newTab().setText(tabTitles.get(i)));
        }
        pagerAdapter = new TabPagerAdapter(getSupportFragmentManager(), mFragments, tabTitles);
        mViewPager.setAdapter(pagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        mViewPager.addOnPageChangeListener(this);

    }

    @OnClick({R.id.all_add_buy, R.id.consultation, R.id.collection, R.id.back, R.id.share, R.id.activity_rl})//,R.id.single_add_buy
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.all_add_buy:
                Intent intent = new Intent();
                intent.setClass(ClassDetialActivity.this, ConfirmOrderActivity.class);
                intent.putExtra("flag", "2");
                intent.putExtra("group_id", mGroup_id);
                startActivity(intent);
//                ToastUtil.showShortToast("1");
                break;
            case R.id.consultation:
//                ToastUtil.showShortToast("1");
                break;
            case R.id.collection:
//                if (collectType != 1) {
//                    requestCollect();
//                    collectType = 1;
//                } else {
////                    ToastUtil.showShortToast("取消");
//                    requestCancleCollect();
//                    collectType = 0;
//                }
                if (collectType.equals("1")){
//                    ToastUtil.showShortToast("取消");
                    requestCancleCollect();
                }else {
                    requestCollect();
                }
                break;
            case R.id.back:
                finish();
                break;
            case R.id.share:
//                mShare.setBackground(ContextCompat.getDrawable(ClassDetialActivity.this, R.mipmap.tab_collection));
                ToastUtil.showShortToast("分享");
                break;
            case R.id.activity_rl:
//                showPopwindow();
                break;
//            case R.id.single_add_buy:
//                startActivity(new Intent(ClassDetialActivity.this,SinglePurchaseActivity.class));
//
//                break;
        }
    }
    @OnClick(R.id.activity_switch)
    public void onToggleButtonClick(){
        if(mActivitySwitch.isChecked()){
            showPopwindow();
        }else{
            mPopupWindow.dismiss();
        }
    }
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private void requestClassDetail() {
        Map<String, String> map = new HashMap<>();
        map.put("group_id", mGroupId);
//        map.put("user_id", "1");
        map.put("user_id", (String) SPUtils.getSpValues(SPKeyValuesUtils.SP_USER_ID, SPUtils.TYPE_STRING));
        Log.e("TAG",map.toString());
        GloableConstant.getInstance().startProgressDialog(this);
        Xutil.GET(ApiUrl.CLASSSDETAIL, map, new MyCallBack<ClassDetailResponse>() {
            @Override
            public void onSuccess(ClassDetailResponse result) {
                super.onSuccess(result);
                GloableConstant.getInstance().stopProgressDialog1();
                if (result.getStatus_code().equals("200")) {
                    if (result.getData() != null) {
                        Glide.with(ClassDetialActivity.this).load(result.getData().getImages()).apply(GloableConstant.getInstance().getDefaultOptionssmall()).into(mClassCover);
                        mClassTitle.setText(result.getData().getGroup_name());
                        mClassPrice.setText(result.getData().getPrice());
                        mClassOriginalPrice.setText(result.getData().getMarket_price());
                        mClassOriginalPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                        mLearnNum.setText(result.getData().getSale_num() + "人在学习");
                        mClassHour.setText("总课时：" + result.getData().getCourse_num());
                        mGroup_id = result.getData().getId();
                        if(result.getData().getStart_time().equals("0")){
                            mEffectiveLy.setVisibility(View.GONE);
                        }else{
                            mEffectiveLy.setVisibility(View.VISIBLE);
                            mEffective.setText(result.getData().getStart_time()+"至"+result.getData().getEnd_time());
                        }

                        if (result.getData().getGive_book().equals("yes")) {
                            mGIveBook.setVisibility(View.VISIBLE);
                        } else {
                            mGIveBook.setVisibility(View.GONE);
                        }
                        if (result.getData().getCollect_status().equals("1")) {
//                            collectType = "1";
                            GloableConstant.getInstance().setCollectType("1");
                            mCollectImg.setBackground(ContextCompat.getDrawable(ClassDetialActivity.this, R.mipmap.tab_collection_pre));
                        } else {
//                            collectType = "0";
                            GloableConstant.getInstance().setCollectType("0");
                            mCollectImg.setBackground(ContextCompat.getDrawable(ClassDetialActivity.this, R.mipmap.tab_collection));
                        }
                        collectType=GloableConstant.getInstance().getCollectType();
                        Log.e("*********collectType",collectType);
                        Log.e("***********getActivity", result.getData().getActivity().size() + "");
                        if (result.getData().getActivity() != null && result.getData().getActivity().size() > 0) {
                            mActivityRl.setVisibility(View.VISIBLE);
                            mActivityNum.setText(result.getData().getActivity().size() + "个活动");
                            activity = result.getData().getActivity();
                            Log.e("**********activity", activity.size() + "");
                            mActivityFirst.setText(activity.get(0).getActivity_name());
                            Glide.with(ClassDetialActivity.this).load(activity.get(0).getIcon()).into(mFirstIcon);

//                            if (result.getData().getActivity().size() > 2) {
//                                mSecondActivityLy.setVisibility(View.VISIBLE);
//                                mHideActivity.setAdapter(new CommonAdapter<ClassDetailResponse.mActivity>(ClassDetialActivity.this, R.layout.item_school_courses_detial_tag, result.getData().getActivity()) {
//                                    @Override
//                                    protected void convert(ViewHolder holder, final ClassDetailResponse.mActivity mActivity, int position) {
//                                        holder.setText(R.id.tag_name, mActivity.getActivity_name());
//                                        ImageView mIcon = (ImageView) holder.getConvertView().findViewById(R.id.tag_img);
//                                        Glide.with(ClassDetialActivity.this).load(mActivity.getIcon()).into(mIcon);
//                                        TextView textView = (TextView) holder.getConvertView().findViewById(R.id.get);
//                                        if (mActivity.getType().equals("3")) {
//                                            textView.setVisibility(View.VISIBLE);
//                                            holder.setOnClickListener(R.id.get_coupons, new View.OnClickListener() {
//                                                @Override
//                                                public void onClick(View v) {
//                                                    mActivity_id = mActivity.getId();
//                                                    getCoupons();
//                                                }
//                                            });
//                                        } else {
//                                            textView.setVisibility(View.GONE);
//                                        }
//                                    }
//                                });
//                            } else {
//                                if (result.getData().getActivity().size() == 1) {
//                                    mActivityFirst.setText(result.getData().getActivity().get(0).getActivity_name());
//                                    Glide.with(ClassDetialActivity.this).load(result.getData().getActivity().get(0).getIcon()).into(mFirstIcon);
//                                    mSecondActivityLy.setVisibility(View.GONE);
//                                } else {
//                                    mActivityFirst.setText(result.getData().getActivity().get(0).getActivity_name());
//                                    Glide.with(ClassDetialActivity.this).load(result.getData().getActivity().get(0).getIcon()).into(mFirstIcon);
//                                    mActivitySecond.setText(result.getData().getActivity().get(1).getActivity_name());
//                                    Glide.with(ClassDetialActivity.this).load(result.getData().getActivity().get(1).getIcon()).into(mSecondIcon);
//                                    mSecondActivityLy.setVisibility(View.VISIBLE);
//                                }
//                            }
                        }else{
                            mActivityRl.setVisibility(View.GONE);
                        }
                    }
                }
            }

            @Override
            public void onFinished() {
                super.onFinished();
//                GloableConstant.getInstance().stopProgressDialog();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
//                GloableConstant.getInstance().stopProgressDialog();
            }
        });
    }

    /**
     * 收藏
     */
    private void requestCollect() {
        Map<String, Object> map = new HashMap<>();
        map.put("goods_id", mGroupId);
//        map.put("user_id", "1");
        map.put("user_id",SPUtils.getSpValues(SPKeyValuesUtils.SP_USER_ID, SPUtils.TYPE_STRING));
        map.put("type", "3");
        map.put("", "");
        GloableConstant.getInstance().showmeidialog(this);
        Xutil.Post(ApiUrl.ADDCOLLECT, map, new MyCallBack<SupportResponse>() {
            @Override
            public void onSuccess(SupportResponse result) {
                super.onSuccess(result);
                GloableConstant.getInstance().stopProgressDialog();
                if (result.getStatus_code().equals("204")) {
                    ToastUtil.showShortToast("收藏成功");
                    collectType = "1";
                    GloableConstant.getInstance().setCollectType("1");
                    Log.e("*********collectType",collectType);
//                    mCollectImg.setImageResource(R.mipmap.tab_collection_pre);
                    mCollectImg.setBackground(ContextCompat.getDrawable(ClassDetialActivity.this, R.mipmap.tab_collection_pre));
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
            }
        });
    }

    /**
     * 取消收藏
     */
    private void requestCancleCollect() {
        Map<String, Object> map = new HashMap<>();
        map.put("goods_id", mGroupId);
//        map.put("user_id", "1");
        map.put("user_id", (String) SPUtils.getSpValues(SPKeyValuesUtils.SP_USER_ID, SPUtils.TYPE_STRING));
        map.put("type", "3");
        GloableConstant.getInstance().showmeidialog(this);
        Xutil.Post(ApiUrl.CANCLECOLLECT, map, new MyCallBack<SupportResponse>() {
            @Override
            public void onSuccess(SupportResponse result) {
                super.onSuccess(result);
                GloableConstant.getInstance().stopProgressDialog();
                if (result.getStatus_code().equals("204")) {
                    ToastUtil.showShortToast("取消收藏成功");
                    collectType = "0";
                    GloableConstant.getInstance().setCollectType("0");
                    Log.e("*********collectType",collectType);
                    mCollectImg.setBackground(ContextCompat.getDrawable(ClassDetialActivity.this, R.mipmap.tab_collection));
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

    /**
     * 领取活动优惠券
     */
    private void getCoupons() {
        Map<String, Object> map = new HashMap<>();
//        map.put("user_id", 1);
        map.put("user_id", (String) SPUtils.getSpValues(SPKeyValuesUtils.SP_USER_ID, SPUtils.TYPE_STRING));
        map.put("activity_id", mActivity_id);
        GloableConstant.getInstance().showmeidialog(this);
        Xutil.Post(ApiUrl.GETCOUPONS, map, new MyCallBack<SupportResponse>() {
            @Override
            public void onSuccess(SupportResponse result) {
                super.onSuccess(result);
                GloableConstant.getInstance().stopProgressDialog();
                if (result.getStatus_code().equals("204")) {
                    ToastUtil.showShortToast(result.getMessage());
                }
            }

            @Override
            public void onFinished() {
                super.onFinished();
                GloableConstant.getInstance().stopProgressDialog();
            }
        });
    }

    private PopupWindow mPopupWindow;
    private View mContentView;
    private RecyclerView mRecycleView;
    private TextView mCancel;

    private void showPopwindow() {
        LayoutInflater inflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mContentView = inflater.inflate(R.layout.activity_layout, null);
        mRecycleView = (RecyclerView) mContentView.findViewById(R.id.recycle_view);
        mCancel = (TextView) mContentView.findViewById(R.id.cancel);
        mPopupWindow = new PopupWindow(mContentView, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setFocusable(true);// 取得焦点
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        //点击外部消失
        mPopupWindow.setOutsideTouchable(true);
        //设置可以点击
        mPopupWindow.setTouchable(true);
        setBackgroundAlpha(0.5f);//设置屏幕透明度
        mPopupWindow.showAtLocation(mContentView, Gravity.BOTTOM, 0, 0);
        mPopupWindow.setOnDismissListener(new poponDismissListener());
        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPopupWindow.isShowing()){
                    mPopupWindow.dismiss();
                }
            }
        });
        if (activity.size() > 0 && activity != null) {
            mRecycleView.setLayoutManager(new LinearLayoutManager(this));
            mRecycleView.setAdapter(new CommonAdapter<ClassDetailResponse.mActivity>(ClassDetialActivity.this, R.layout.item_school_courses_detial_tag, activity) {
                @Override
                protected void convert(ViewHolder holder, final ClassDetailResponse.mActivity mActivity, int position) {
                    holder.setText(R.id.tag_name, mActivity.getActivity_name());
                    ImageView mIcon = (ImageView) holder.getConvertView().findViewById(R.id.tag_img);
                    Glide.with(ClassDetialActivity.this).load(mActivity.getIcon()).into(mIcon);
                    TextView textView = (TextView) holder.getConvertView().findViewById(R.id.get);
                    if (mActivity.getType().equals("3")) {
                        textView.setVisibility(View.VISIBLE);
                        holder.setOnClickListener(R.id.get_coupons, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mActivity_id = mActivity.getId();
                                getCoupons();
                            }
                        });
                    } else {
                        textView.setVisibility(View.GONE);
                    }
                }
            });
        }
    }

    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha 屏幕透明度0.0-1.0 1表示完全不透明
     */
    public void setBackgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = mActivity.getWindow().getAttributes();
        lp.alpha = bgAlpha;
        mActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        mActivity.getWindow().setAttributes(lp);
    }


    class poponDismissListener implements PopupWindow.OnDismissListener {

        @Override
        public void onDismiss() {
            mActivitySwitch.setChecked(false);
            setBackgroundAlpha(1f);
        }
    }
}

