package com.jiaoshizige.teacherexam.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.jiaoshizige.teacherexam.mycourse.activity.MyNewCoursesActivity;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.jiaoshizige.teacherexam.R;
import com.jiaoshizige.teacherexam.adapter.TabPagerAdapter;
import com.jiaoshizige.teacherexam.base.BaseActivity;
import com.jiaoshizige.teacherexam.base.GloableConstant;
import com.jiaoshizige.teacherexam.fragment.ClassIntroduceFragment;
import com.jiaoshizige.teacherexam.yy.fragment.EvaluateFragment;
import com.jiaoshizige.teacherexam.fragment.SanJIdeLieBiaoFragment;
import com.jiaoshizige.teacherexam.fragment.SignalCourseCatalogFragmnet;
import com.jiaoshizige.teacherexam.http.ApiUrl;
import com.jiaoshizige.teacherexam.http.MyCallBack;
import com.jiaoshizige.teacherexam.http.SupportResponse;
import com.jiaoshizige.teacherexam.http.Xutil;
import com.jiaoshizige.teacherexam.model.CourseDetialResponse;
import com.jiaoshizige.teacherexam.model.CreatResponse;
import com.jiaoshizige.teacherexam.utils.SPKeyValuesUtils;
import com.jiaoshizige.teacherexam.utils.SPUtils;
import com.jiaoshizige.teacherexam.utils.ShareUtils;
import com.jiaoshizige.teacherexam.utils.ToastUtil;
import com.jiaoshizige.teacherexam.utils.ToolUtils;
import com.jiaoshizige.teacherexam.widgets.OnSelectItemListener;
import com.jiaoshizige.teacherexam.widgets.SharePopWindow;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/a1/13.
 * 课程详情1212
 */

public class CoursesDetialActivity extends BaseActivity implements ViewPager.OnPageChangeListener {
    @BindView(R.id.cover_photo)
    ImageView mCoverPhoto;
    @BindView(R.id.course_name)
    TextView mCourseName;
    @BindView(R.id.course_price)
    TextView mCoursePrice;
    @BindView(R.id.student_num)
    TextView mStudentNum;
    @BindView(R.id.original_price)
    TextView mOriginalPrice;
    @BindView(R.id.give_book)
    TextView mGIveBook;
    @BindView(R.id.courses_tag_list)
    RecyclerView mCoursesTagList;
    @BindView(R.id.activity_first)
    TextView mActivityFirst;
    @BindView(R.id.activity_num)
    TextView mActivityNum;
    @BindView(R.id.activity_switch)
    ToggleButton mActivitySwitch;
    //    @BindView(R.id.activity_second)
//    TextView mActivitySecond;
//    @BindView(R.id.second_icon)
//    ImageView mSecondIcon;
    @BindView(R.id.tag_ly)
    LinearLayout mTagLy;
    //    @BindView(R.id.hide_activity)
//    RecyclerView mHideActivity;
    @BindView(R.id.tablayout)
    TabLayout mTabLayout;
    @BindView(R.id.viewpager)
    ViewPager mViewPager;
    @BindView(R.id.activity_ly)
    RelativeLayout mActivityLy;
    @BindView(R.id.buy_bottom)
    LinearLayout mBuyBottom;
    @BindView(R.id.consultation)
    LinearLayout mConsultation;
    @BindView(R.id.collection)
    LinearLayout mCollection;
    @BindView(R.id.all_add_buy)
    TextView mAddBuy;
    @BindView(R.id.collect_img)
    ImageView mCollectImg;
    @BindView(R.id.effective_ly)
    LinearLayout mEffectiveLy;
    @BindView(R.id.effective)
    TextView mEffective;
    @BindView(R.id.first_icon)
    ImageView mFirstIcon;
//    @BindView(R.id.single_add_buy)
//    TextView mSingleAddBuy;
    private List<Fragment> mFragments;
    private List<String> tabTitles;
    private TabPagerAdapter pagerAdapter;
    private int mPosition;
    private String mType;//1班级2课程
    private String mCourseId;//课程id
    private String mActivity_id;
    GridLayoutManager gridLayoutManager;
    private String collectType;
    private List<CourseDetialResponse.mActivity> activity;
    private CoursesDetialActivity mActivity;
    SharePopWindow mSharePopWindow;
    private String mFlag;
    private String title;
    private String mImage;
    private String type;
    private String user_id;
    SanJIdeLieBiaoFragment sanJIdeLieBiaoFragment;
    SignalCourseCatalogFragmnet signalCourseCatalogFragmnet;
    private String isBuy;
    private String covermap;

    @Override
    protected int getLayoutId() {
        return R.layout.course_detial_activity;
    }

    protected void onStart() {
        super.onStart();
        ToolUtils.setIndicator(mTabLayout, 25, 25);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("课程详情");
        MobclickAgent.onResume(this);
//        requestCourseDetial();
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("课程详情");
        MobclickAgent.onPause(this);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void initView() {
        mActivity = this;
      /*  if(getIntent().getExtras().get("type") != null){
            mType = (String) getIntent().getExtras().get("type");
            if(mType.equals("1")){
                mBuyBottom.setVisibility(View.GONE);
            }else{
                mBuyBottom.setVisibility(View.VISIBLE);
                mAddBuy.setText("购买课程");
            }
        }*/
        user_id = (String) SPUtils.getSpValues(SPKeyValuesUtils.SP_USER_ID, SPUtils.TYPE_STRING);
        if (getIntent().getExtras().get("type") != null) {
            mType = (String) getIntent().getExtras().get("type");
            Log.e("*******type", mType);
            if (mType.equals("2")) {
                //隐藏单科购买
//                mSingleAddBuy.setVisibility(View.GONE);
            }
        } else {
            mType = "";
        }
        if (getIntent().getExtras().get("course_id") != null) {
            mCourseId = (String) getIntent().getExtras().get("course_id");
        } else {
            mCourseId = "";
        }
        if (getIntent().getStringExtra("is_buy") != null) {
            isBuy = getIntent().getStringExtra("is_buy");
        } else {
            isBuy = "";
        }

        Log.d("///////isBuy", isBuy + "**********");
        setSubTitle().setText("");
        setSubTitle().setBackground(ContextCompat.getDrawable(CoursesDetialActivity.this, R.mipmap.share_icon));
        setToolbarTitle().setText("课程");
        requestCourseDetial();

        mFragments = new ArrayList<>();
        tabTitles = new ArrayList<>();
        tabTitles.add("介绍");
        tabTitles.add("目录");
        tabTitles.add("评价");
//        mFragments.add(new ClassIntroduceFragment("2",mCourseId));
        mFragments.add(new ClassIntroduceFragment(mType, mCourseId));
        sanJIdeLieBiaoFragment = new SanJIdeLieBiaoFragment(mCourseId, mType, isBuy);
        signalCourseCatalogFragmnet = new SignalCourseCatalogFragmnet(mCourseId, mType, isBuy,true);

        if (mType.equals("1")) {
//            mFragments.add(new NewCourseCatalogFragmnet(mCourseId,mType));
//            mFragments.add(new SanJIdeLieBiaoFragment(mCourseId,mType));
            mFragments.add(sanJIdeLieBiaoFragment);
        } else {
//            mFragments.add(new SignalCourseCatalogFragmnet(mCourseId,mType));
            mFragments.add(signalCourseCatalogFragmnet);
        }
//        mFragments.add(new CourseCatalogFragmnet("",mCourseId,0,""));

        mFragments.add(new EvaluateFragment(mCourseId, mType));//在评价的fragment中 因为课程是1班级是2  所以type为1传入评价为3 type为2传入评价为1  所以设定图书type为3 评价传入为2
        for (int i = 0; i < tabTitles.size(); i++) {
            mTabLayout.addTab(mTabLayout.newTab().setText(tabTitles.get(i)));
        }
        pagerAdapter = new TabPagerAdapter(getSupportFragmentManager(), mFragments, tabTitles);
        mViewPager.setAdapter(pagerAdapter);
        mViewPager.setOffscreenPageLimit(3);
        mTabLayout.setupWithViewPager(mViewPager);
        mViewPager.addOnPageChangeListener(this);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        gridLayoutManager = new GridLayoutManager(this, 4);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return 1;
            }
        });
//        mHideActivity.setLayoutManager(new LinearLayoutManager(this));
        if (Build.VERSION.SDK_INT >= 23) {
            String[] mPermissionList = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CALL_PHONE, Manifest.permission.READ_LOGS, Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.SET_DEBUG_APP, Manifest.permission.SYSTEM_ALERT_WINDOW, Manifest.permission.GET_ACCOUNTS, Manifest.permission.WRITE_APN_SETTINGS};
            ActivityCompat.requestPermissions(this, mPermissionList, 123);
        }
    }
    @OnClick({R.id.all_add_buy, R.id.consultation, R.id.collection, R.id.float_action_btn, R.id.toolbar_subtitle})// R.id.single_add_buy
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.all_add_buy:
                if (mFlag != null && mFlag != "") {
                    if (mFlag.equals("0")) {
                        orderOpenClass();
                    } else if (mFlag.equals("2")) {
                        Intent intent = new Intent();
                        intent.putExtra("id", mCourseId);
                        intent.putExtra("progress", mPercent);
                        intent.putExtra("name", mName);
//                       if(mType.equals("1")){
////                           map.put("type", "3");
//                           intent.putExtra("type", "3");
//                       }else{
////                           map.put("type", "1");
//                           intent.putExtra("type", "1");
//                       }
                        intent.putExtra("type", mType);
                        intent.setClass(CoursesDetialActivity.this, MyNewCoursesActivity.class);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent();
                        intent.setClass(CoursesDetialActivity.this, ConfirmOrderActivity.class);
                        intent.putExtra("course_id", mCourseId);
                        intent.putExtra("flag", "3");
                        intent.putExtra("type", mType);
                        startActivity(intent);
                    }
                }
                break;
            case R.id.consultation:
                if (ToolUtils.checkApkExist(this, "com.tencent.mobileqq")) {
//                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("mqqwpa://im/chat?chat_type=wpa&uin="+"3068373138")));
                    if (qq != null) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("mqqwpa://im/chat?chat_type=wpa&uin=" + qq)));
                    } else {
                        ToastUtil.showShortToast("老师尚未在线，请稍后重试");
                    }

                } else {
                    ToastUtil.showShortToast("本机未安装QQ应用");
                }
                break;
            case R.id.collection:
                collectType = GloableConstant.getInstance().getCollectType();
                Log.e("********collectType", collectType);
                if (mType.equals("2")) {
                    //课程
                    type = "1";
                }
                if (mType.equals("1")) {
                    //班级
                    type = "3";
                }
                if (collectType.equals("1")) {

                    requestCancleCollect(type);
                } else {
                    requestCollect(type);
                }
                break;
            case R.id.float_action_btn:
                startActivity(new Intent(this, PutQuestionsActivity.class));
                break;
            case R.id.toolbar_subtitle:
                showSharePopWindow();
                break;
//            case R.id.single_add_buy:
//                Intent intent1 = new Intent();
//                intent1.setClass(CoursesDetialActivity.this, NewSinglePurchaseActivity.class);
//                intent1.putExtra("cc_type", mType);
//                intent1.putExtra("course_id", mCourseId);
//                startActivity(intent1);
//                break;
        }
    }

    @OnClick(R.id.activity_switch)
    public void onToggleButtonClick() {
        if (mActivitySwitch.isChecked()) {
            showPopwindow();
        } else {
            mPopupWindow.dismiss();
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        mPosition = position;
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private String qq, mName, mPercent;

    private void requestCourseDetial() {
        Map<String, String> map = new HashMap<>();
        map.put("user_id", (String) SPUtils.getSpValues(SPKeyValuesUtils.SP_USER_ID, SPUtils.TYPE_STRING));
        map.put("course_id", mCourseId);
        map.put("type_id", mType);
        map.put("is_az", "1");
        Log.i("*********map", map.toString());
        Log.i("*********map", (String) SPUtils.getSpValues(SPKeyValuesUtils.SP_USER_ID, SPUtils.TYPE_STRING));
        GloableConstant.getInstance().showmeidialog(this);
        Xutil.GET(ApiUrl.COURSERDETAIL, map, new MyCallBack<CourseDetialResponse>() {
            @Override
            public void onSuccess(final CourseDetialResponse result) {
                super.onSuccess(result);
                Gson gson = new Gson();
                String s = gson.toJson(result);
                Log.i("wch",s);
                GloableConstant.getInstance().stopProgressDialog();
                if (result.getStatus_code().equals("200")) {
                    if (result.getData() != null) {
                        qq = result.getData().getQq();
                        mName = result.getData().getName();
                        covermap = result.getData().getImage();
                        mPercent = result.getData().getLearn_percent();
                        Glide.with(CoursesDetialActivity.this).load(result.getData().getImage()).apply(GloableConstant.getInstance().getDefaultOptionssmall()).into(mCoverPhoto);
                        mCourseName.setText(result.getData().getName());
                        mCoursePrice.setText("￥" + result.getData().getPrice());
                        title = result.getData().getName();
                        mImage = result.getData().getImage();
                        mStudentNum.setText(result.getData().getSale_num() + "人在学习");
                        mOriginalPrice.setText("￥" + result.getData().getMarket_price());
                        mOriginalPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                        if (result.getData().getPrice().equals("0.00")) {
//                            mSingleAddBuy.setVisibility(View.GONE);
                            mAddBuy.setText("立即领取");
                            mFlag = "0";
                        } else {
                            mAddBuy.setText("加入购买");
                            mFlag = "1";
                            Log.e("WOCWOWO", mFlag);
                        }
                        if (!result.getData().getBook_id().equals("")) {
                            mGIveBook.setVisibility(View.VISIBLE);
                        } else {
                            mGIveBook.setVisibility(View.GONE);
                        }
                        if (result.getData().getDays().equals("0")) {
                            mEffectiveLy.setVisibility(View.GONE);
                        } else {
                            mEffectiveLy.setVisibility(View.VISIBLE);
//                            mEffective.setText(result.getData().getStart_time()+"至"+result.getData().getEnd_time());
                            mEffective.setText("报名即学  有效期" + result.getData().getDays() + "天");
                        }
                        if (result.getData().getIs_buy().equals("1")) {
//                            mSingleAddBuy.setVisibility(View.GONE);
                            mAddBuy.setText("已购买 去学习");
                            mAddBuy.setBackground(ContextCompat.getDrawable(CoursesDetialActivity.this, R.color.purple4));
                            mAddBuy.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent();
                                    intent.putExtra("id", result.getData().getId());
                                    intent.putExtra("progress", result.getData().getLearn_percent());
                                    intent.putExtra("name", result.getData().getName());
                                    intent.putExtra("type", mType);
                                    intent.putExtra("is_buy", isBuy);
                                    intent.putExtra("course_id",mCourseId);
                                    intent.putExtra("covermap",covermap);
                                    Log.i("/////////8888is_buy", isBuy + "------");
                                    Log.i("*********intentgetId", result.getData().getId() + "***getLearn_percent" + result.getData().getLearn_percent() +
                                            "*******getName" + result.getData().getName() + "*******mType" + mType);
//                                    intent.setClass(CoursesDetialActivity.this, NewMyClassDetialActivity.class);
                                    intent.setClass(CoursesDetialActivity.this, MyNewCoursesActivity.class);
                                    startActivity(intent);
                                }
                            });
                        } else if (result.getData().getIs_buy().equals("2")) {
                            mFlag = "1";
//                            mSingleAddBuy.setVisibility(View.GONE);
                            if (result.getData().getPrice().equals("0.00")) {
//                                mSingleAddBuy.setVisibility(View.GONE);
                                mAddBuy.setText("立即领取");
                                mFlag = "0";
                            } else {
                                mAddBuy.setText("已到期 去续费");
                                mFlag = "1";
                            }

                            mAddBuy.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent();
                                    intent.setClass(CoursesDetialActivity.this, ConfirmOrderActivity.class);
                                    intent.putExtra("course_id", mCourseId);
                                    intent.putExtra("flag", "3");
                                    intent.putExtra("type", mType);
                                    startActivity(intent);
                                }
                            });
                        }
                        mCoursesTagList.setLayoutManager(gridLayoutManager);
                        if (result.getData().getTag_list() != null && result.getData().getTag_list().size() > 0) {
                            mCoursesTagList.setAdapter(new CommonAdapter<CourseDetialResponse.mTagList>(CoursesDetialActivity.this, R.layout.item_courses_detial_tag, result.getData().getTag_list()) {
                                @Override
                                protected void convert(ViewHolder holder, CourseDetialResponse.mTagList mTagList, int position) {
                                    Log.e("TAG", "sdsdsdsdsd" + mTagList.getTag_name());
                                    holder.setText(R.id.tag_name, mTagList.getTag_name());
                                    ImageView mTagImg = (ImageView) holder.getConvertView().findViewById(R.id.tag_img);
                                    Glide.with(CoursesDetialActivity.this).load(mTagList.getImages()).into(mTagImg);
                                }
                            });
                        } else {
                            mTagLy.setVisibility(View.GONE);
                        }
                        if (result.getData().getCollect_status().equals("1")) {
                            GloableConstant.getInstance().setCollectType("1");
                            mCollectImg.setBackground(ContextCompat.getDrawable(CoursesDetialActivity.this, R.mipmap.tab_collection_pre));
                        } else {
                            GloableConstant.getInstance().setCollectType("0");
                            mCollectImg.setBackground(ContextCompat.getDrawable(CoursesDetialActivity.this, R.mipmap.tab_collection));

                        }
                        collectType = GloableConstant.getInstance().getCollectType();
                        Log.e("*******collectType*****", collectType + "////////" + result.getData().getCollect_status());

                        if (result.getData().getActivity().size() > 0) {
                            mActivityNum.setText(result.getData().getActivity().size() + "个活动");
                            if (result.getData().getActivity().size() > 0 && result.getData().getActivity() != null) {
                                activity = result.getData().getActivity();
                            }
                            Log.e("*******activity", activity.size() + "*****size");

                            if (result.getData().getActivity().size() == 1) {
                                mActivityFirst.setText(result.getData().getActivity().get(0).getActivity_name());
                                Glide.with(CoursesDetialActivity.this).load(result.getData().getActivity().get(0).getIcon()).into(mFirstIcon);
                                mActivitySwitch.setVisibility(View.INVISIBLE);
                            } else {
                                mActivityFirst.setText(result.getData().getActivity().get(0).getActivity_name());
                                Glide.with(CoursesDetialActivity.this).load(result.getData().getActivity().get(0).getIcon()).into(mFirstIcon);
                            }
                            mActivitySwitch.setVisibility(View.VISIBLE);
                        } else {
                            mActivityLy.setVisibility(View.GONE);
                        }

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

    /**
     * 收藏
     */
    private void requestCollect(String mType) {
        Map<String, Object> map = new HashMap<>();
        map.put("goods_id", mCourseId);
//        map.put("user_id","1");
        map.put("user_id", (String) SPUtils.getSpValues(SPKeyValuesUtils.SP_USER_ID, SPUtils.TYPE_STRING));
        map.put("type", mType);
        Log.e("******收藏map", map.toString());
        GloableConstant.getInstance().showmeidialog(this);
        Xutil.Post(ApiUrl.ADDCOLLECT, map, new MyCallBack<SupportResponse>() {
            @Override
            public void onSuccess(SupportResponse result) {
                super.onSuccess(result);
                GloableConstant.getInstance().stopProgressDialog();
                if (result.getStatus_code().equals("204")) {
                    ToastUtil.showShortToast(result.getMessage());
//                    collectType="1";
                    GloableConstant.getInstance().setCollectType("1");
                    mCollectImg.setBackground(ContextCompat.getDrawable(CoursesDetialActivity.this, R.mipmap.tab_collection_pre));
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
    private void requestCancleCollect(String mType) {
        Map<String, Object> map = new HashMap<>();
        map.put("goods_id", mCourseId);
//        map.put("user_id","1");
        map.put("user_id", SPUtils.getSpValues(SPKeyValuesUtils.SP_USER_ID, SPUtils.TYPE_STRING));
        map.put("type", mType);
        Log.e("******取消收藏map", map.toString());
        GloableConstant.getInstance().showmeidialog(this);
        Xutil.Post(ApiUrl.CANCLECOLLECT, map, new MyCallBack<SupportResponse>() {
            @Override
            public void onSuccess(SupportResponse result) {
                super.onSuccess(result);
                GloableConstant.getInstance().stopProgressDialog();
                if (result.getStatus_code().equals("204")) {
                    ToastUtil.showShortToast(result.getMessage());
//                    collectType = "0";
                    GloableConstant.getInstance().setCollectType("0");
//                    mCollectImg.setImageResource(R.mipmap.tab_collection);
                    mCollectImg.setBackground(ContextCompat.getDrawable(CoursesDetialActivity.this, R.mipmap.tab_collection));
                    EventBus.getDefault().post("课程取消收藏");
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
     * 免费课程预约
     */
    private void orderOpenClass() {
        Map<String, Object> map = new HashMap<>();
        map.put("user_id", SPUtils.getSpValues(SPKeyValuesUtils.SP_USER_ID, SPUtils.TYPE_STRING));
        map.put("id", mCourseId);
        map.put("integral", "0");
        map.put("pay_type", "1");
        if (mType.equals("1")) {
            map.put("type", "3");
        } else {
            map.put("type", "1");
        }
        Log.e("******ordermap", map.toString());
        GloableConstant.getInstance().showmeidialog(this);
        Xutil.Post(ApiUrl.CREAT, map, new MyCallBack<CreatResponse>() {
            @Override
            public void onSuccess(CreatResponse result) {
                super.onSuccess(result);
                GloableConstant.getInstance().stopProgressDialog();


                if (result.getStatus_code().equals("200")) {
                    Log.e("******getStatus_code*", result.getStatus_code());
                    mAddBuy.setText("已购买 去学习");
                    mAddBuy.setTextColor(ContextCompat.getColor(CoursesDetialActivity.this, R.color.white));
                    mAddBuy.setBackgroundResource(R.color.purple4);
                    if (mType.equals("1")) {
                        sanJIdeLieBiaoFragment.refreshCatalog();
                    } else {
                        signalCourseCatalogFragmnet.refreshCatalog();
                    }
                    mFlag = "2";
                } else {
                    ToastUtil.showShortToast("预约失败，请重新预约");
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
                Log.e("***********ex", ex.getMessage() + "///////");
            }
        });
    }

    /**
     * 领取活动优惠券
     */
    private void getCoupons() {
        Map<String, Object> map = new HashMap<>();
//        map.put("user_id",1);
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
                if (mPopupWindow.isShowing()) {
                    mPopupWindow.dismiss();
                    mActivitySwitch.setChecked(false);
                }
            }
        });
        if (activity.size() > 0 && activity != null) {
            mRecycleView.setLayoutManager(new LinearLayoutManager(this));
            mRecycleView.setAdapter(new CommonAdapter<CourseDetialResponse.mActivity>(CoursesDetialActivity.this, R.layout.item_school_courses_detial_tag, activity) {

                @Override
                protected void convert(ViewHolder holder, final CourseDetialResponse.mActivity mActivity, int position) {
                    holder.setText(R.id.tag_name, mActivity.getActivity_name());
                    ImageView mIcon = (ImageView) holder.getConvertView().findViewById(R.id.tag_img);
                    Glide.with(CoursesDetialActivity.this).load(mActivity.getIcon()).into(mIcon);
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

    public void showSharePopWindow() {
        mSharePopWindow = new SharePopWindow(this);
        mSharePopWindow.isShowing();
        final String url = ApiUrl.BASEIMAGE + "api/v1/course_share?type_id=" + mType + "&course_id=" + mCourseId + "&user_id=" + user_id;
        mSharePopWindow.setOnSelectItemListener(new OnSelectItemListener() {
            @Override
            public void selectItem(String name, int type) {
                if (mSharePopWindow != null && mSharePopWindow.isShowing()) {
                    mSharePopWindow.dismiss();

                    switch (type) {
                        case SharePopWindow.POP_WINDOW_ITEM_1:
                            ShareUtils.shareWeb(CoursesDetialActivity.this, url, title
                                    , title, mImage, R.mipmap.ic_launcher, SHARE_MEDIA.WEIXIN
                            );
                            break;
                        case SharePopWindow.POP_WINDOW_ITEM_2:
                            ShareUtils.shareWeb(CoursesDetialActivity.this, url, title
                                    , title, mImage, R.mipmap.ic_launcher, SHARE_MEDIA.WEIXIN_CIRCLE
                            );
                            break;
                        case SharePopWindow.POP_WINDOW_ITEM_3:
                            ShareUtils.shareWeb(CoursesDetialActivity.this, url, title
                                    , title, mImage, R.mipmap.ic_launcher, SHARE_MEDIA.QQ
                            );
                            break;
                        case SharePopWindow.POP_WINDOW_ITEM_4:

                            ShareUtils.shareWeb(CoursesDetialActivity.this, url, title
                                    , title, mImage, R.mipmap.ic_launcher, SHARE_MEDIA.QZONE
                            );
                            break;
                        case SharePopWindow.POP_WINDOW_ITEM_5:
                            ShareUtils.shareWeb(CoursesDetialActivity.this, url, title
                                    , title, mImage, R.mipmap.ic_launcher, SHARE_MEDIA.SMS
                            );
                            break;
                        case SharePopWindow.POP_WINDOW_ITEM_6:
                            Log.e("wwwww", "www1");
                            ShareUtils.shareWeibo(CoursesDetialActivity.this, url, title
                                    , title, mImage, R.mipmap.ic_launcher, SHARE_MEDIA.SINA
                            );
                            break;
                        default:
                            break;
                    }
                }
            }
        });
    }

}
