package com.jiaoshizige.teacherexam.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.support.design.widget.TabLayout;
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
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.jiaoshizige.teacherexam.R;
import com.jiaoshizige.teacherexam.adapter.TabPagerAdapter;
import com.jiaoshizige.teacherexam.base.BaseActivity;
import com.jiaoshizige.teacherexam.base.GloableConstant;
import com.jiaoshizige.teacherexam.fragment.ClassIntroduceFragment;
import com.jiaoshizige.teacherexam.yy.fragment.EvaluateFragment;
import com.jiaoshizige.teacherexam.http.ApiUrl;
import com.jiaoshizige.teacherexam.http.MyCallBack;
import com.jiaoshizige.teacherexam.http.SupportResponse;
import com.jiaoshizige.teacherexam.http.Xutil;
import com.jiaoshizige.teacherexam.model.BookDetailResponse;
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
 * Created by Administrator on 2017/a1/27.
 * 图书详情
 */

public class BookDetailActivity extends BaseActivity implements ViewPager.OnPageChangeListener {
    @BindView(R.id.book_cover)
    ImageView mBookCover;
    @BindView(R.id.book_name)
    TextView mBookName;
    @BindView(R.id.book_price)
    TextView mBookPrice;
    @BindView(R.id.book_original_price)
    TextView mOriginalPrice;
    @BindView(R.id.sell_num)
    TextView mSellNum;
    @BindView(R.id.book_tag_list)
    RecyclerView mBookTagList;
    @BindView(R.id.activity_first)
    TextView mActivityFirst;
    @BindView(R.id.first_icon)
    ImageView mFirstIcon;
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
    @BindView(R.id.activity_ly)
    RelativeLayout mActivityLy;
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
    @BindView(R.id.all_add_buy)
    TextView mAddBuy;
    @BindView(R.id.collect_img)
    ImageView mCollectImg;
//    @BindView(R.id.single_add_buy)
//    TextView mSingle;
    private List<Fragment> mFragments;
    private List<String> tabTitles;
    private TabPagerAdapter pagerAdapter;
    GridLayoutManager gridLayoutManager;

    private String mBookId;
    private String mActivity_id;
    private String collectType;
    private BookDetailActivity mActivity;
    private List<BookDetailResponse.mActivity> activity;
    private String user_id;
    private String mImage;
    private String mTitle;

    @Override
    protected int getLayoutId() {
        return R.layout.book_detial_activity;
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("图书详情");
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("图书详情");
        MobclickAgent.onPause(this);
    }

    @Override
    protected void initView() {
        if (getIntent().getExtras().get("book_id") != null) {
            mBookId = (String) getIntent().getExtras().get("book_id");
        } else {
            mBookId = "";
        }

        mActivity = this;
        user_id = (String) SPUtils.getSpValues(SPKeyValuesUtils.SP_USER_ID, SPUtils.TYPE_STRING);
        setSubTitle().setText("");
        setSubTitle().setBackground(ContextCompat.getDrawable(BookDetailActivity.this, R.mipmap.share_icon));
        setToolbarTitle().setText("图书");
        setSubTitle().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSharePopWindow();
            }
        });
//        mSingle.setVisibility(View.GONE);
        mFragments = new ArrayList<>();
        tabTitles = new ArrayList<>();
        tabTitles.add("详情");
        tabTitles.add("评价");
        mFragments.add(new ClassIntroduceFragment("3", mBookId));
        mFragments.add(new EvaluateFragment(mBookId, "3"));//在评价的fragment中 因为课程是1班级是2  所以type为1传入评价为3 type为2传入评价为1  所以设定图书type为3 评价传入为2
        for (int i = 0; i < tabTitles.size(); i++) {
            mTabLayout.addTab(mTabLayout.newTab().setText(tabTitles.get(i)));

        }
//        ToolUtils.reflex(mTabLayout);
        mTabLayout.post(new Runnable() {
            @Override
            public void run() {
                ToolUtils.setIndicator(mTabLayout, 20, 20);
            }
        });
        pagerAdapter = new TabPagerAdapter(getSupportFragmentManager(), mFragments, tabTitles);
        mViewPager.setAdapter(pagerAdapter);
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
        requestBookDetail();
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

    @OnClick({R.id.all_add_buy, R.id.consultation, R.id.collection})
    public void OnClick(View v) {
        switch (v.getId()) {
            case R.id.all_add_buy:
                Intent intent = new Intent();
                intent.setClass(BookDetailActivity.this, ConfirmBookOrderActivity.class);
                intent.putExtra("book_id", mBookId);
                intent.putExtra("flag", "1");
                startActivity(intent);
                break;
            case R.id.consultation:
                if (ToolUtils.checkApkExist(this, "com.tencent.mobileqq")) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("mqqwpa://im/chat?chat_type=wpa&uin=" + qq)));
                } else {
                    ToastUtil.showShortToast("本机未安装QQ应用");
                }
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
                if (collectType.equals("1")) {
                    requestCancleCollect();
                } else {
                    requestCollect();
                }
                break;
        }
    }

    @OnClick(R.id.activity_switch)
    public void onToggleButtonClick() {
//        if (mActivitySwitch.isChecked()) {
////            mHideActivity.setVisibility(View.VISIBLE);
//        } else {
////            mHideActivity.setVisibility(View.GONE);
//        }

        if (mActivitySwitch.isChecked()) {
            showPopwindow();
        } else {
            mPopupWindow.dismiss();
        }
    }

    private String qq;

    public void requestBookDetail() {
        final Map<String, Object> map = new HashMap<>();
        map.put("book_id", mBookId);
        map.put("user_id", user_id);
        Log.e("TAg", mBookId);
        GloableConstant.getInstance().showmeidialog(this);
        Xutil.Post(ApiUrl.BOOKDETAIL, map, new MyCallBack<BookDetailResponse>() {
            @Override
            public void onSuccess(BookDetailResponse result) {
                super.onSuccess(result);
                GloableConstant.getInstance().stopProgressDialog();
                if (result.getStatus_code().equals("200")) {
                    if (result.getData() != null) {
                        Glide.with(BookDetailActivity.this).load(result.getData().getXimages())
                                .apply(GloableConstant.getInstance().getDefaultOptionssmall()).into(mBookCover);
                        mImage = result.getData().getImages().get(0).getPath();
                        mTitle = result.getData().getBook_name();
                        if (result.getData().getQq()!=null){
                            qq=result.getData().getQq();
                        }
                        Log.e("**********mTitle", mTitle + "*-***********mImage" + mImage);
                        mBookName.setText(result.getData().getBook_name());
                        mBookPrice.setText(result.getData().getPrice());
                        mOriginalPrice.setText("￥" + result.getData().getMarket_price());
                        mOriginalPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                        mSellNum.setText(result.getData().getSale_num() + "人已购买");
                        Log.e("**********status", result.getData().getCollect_status());
                        if (result.getData().getCollect_status().equals("1")) {
                            GloableConstant.getInstance().setCollectType("1");
                            mCollectImg.setBackground(ContextCompat.getDrawable(BookDetailActivity.this, R.mipmap.tab_collection_pre));
                        } else {
                            GloableConstant.getInstance().setCollectType("0");
//                            mCollectImg.setImageResource(R.mipmap.tab_collection);
                            mCollectImg.setBackground(ContextCompat.getDrawable(BookDetailActivity.this, R.mipmap.tab_collection));

                        }
                        collectType = GloableConstant.getInstance().getCollectType();
                        mBookTagList.setLayoutManager(gridLayoutManager);
                        if (result.getData().getTags() != null && result.getData().getTags().size() > 0) {
                            mBookTagList.setAdapter(new CommonAdapter<BookDetailResponse.mTag>(BookDetailActivity.this, R.layout.item_courses_detial_tag, result.getData().getTags()) {
                                @Override
                                protected void convert(ViewHolder holder, BookDetailResponse.mTag mTagList, int position) {
                                    holder.setText(R.id.tag_name, mTagList.getTag_name());
                                    ImageView mTagImg = (ImageView) holder.getConvertView().findViewById(R.id.tag_img);
                                    Glide.with(BookDetailActivity.this).load(mTagList.getImg_path()).into(mTagImg);


                                }
                            });
                        } else {
                            mTagLy.setVisibility(View.GONE);
                        }
                        if (result.getData().getActivity().size() > 0) {
                            mActivityNum.setText(result.getData().getActivity().size() + "个活动");
                            activity = result.getData().getActivity();
                            if (result.getData().getActivity().size() == 1) {
                                mActivityFirst.setText(result.getData().getActivity().get(0).getActivity_name());
                                Glide.with(BookDetailActivity.this).load(result.getData().getActivity().get(0).getIcon()).into(mFirstIcon);
                                mActivitySwitch.setVisibility(View.INVISIBLE);
                            } else {
                                mActivitySwitch.setVisibility(View.VISIBLE);
                                mActivityFirst.setText(result.getData().getActivity().get(0).getActivity_name());
                                Glide.with(BookDetailActivity.this).load(result.getData().getActivity().get(0).getIcon()).into(mFirstIcon);
//                                mActivitySecond.setText(result.getData().getActivity().get(1).getActivity_name());
//                                Glide.with(BookDetailActivity.this).load(result.getData().getActivity().get(1).getIcon()).into(mSecondIcon);
                            }
//                            else {
//                                mActivitySwitch.setVisibility(View.VISIBLE);
//                                mHideActivity.setAdapter(new CommonAdapter<BookDetailResponse.mActivity>(BookDetailActivity.this, R.layout.item_school_courses_detial_tag, result.getData().getActivity()) {
//
//                                    @Override
//                                    protected void convert(ViewHolder holder, final BookDetailResponse.mActivity mActivity, int position) {
//                                        holder.setText(R.id.tag_name, mActivity.getActivity_name());
//                                        ImageView mIconImg = (ImageView) holder.getConvertView().findViewById(R.id.tag_img);
//                                        Glide.with(BookDetailActivity.this).load(mActivity.getIcon()).into(mIconImg);
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
//                            }
                        } else {
                            mActivityLy.setVisibility(View.GONE);
                        }
                    }
                }else if (result.getStatus_code().equals("401")) {
                    GloableConstant.getInstance().clearAll();
                    ToastUtil.showShortToast("请重新登录");
                    startActivity(new Intent(BookDetailActivity.this, LoginActivity.class));
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

            }
        });
    }

    /**
     * 收藏
     */
    private void requestCollect() {
        Map<String, Object> map = new HashMap<>();
        map.put("goods_id", mBookId);
//        map.put("user_id","1");
        map.put("user_id", (String) SPUtils.getSpValues(SPKeyValuesUtils.SP_USER_ID, SPUtils.TYPE_STRING));
        map.put("type", "2");
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
//                    mCollectImg.setImageResource(R.mipmap.tab_collection_pre);
                    mCollectImg.setBackground(ContextCompat.getDrawable(BookDetailActivity.this, R.mipmap.tab_collection_pre));

//                    mCollectImg.setBackground(ContextCompat.getDrawable(ClassDetialActivity.this,R.mipmap.tab_collection_pre));
                }else if (result.getStatus_code().equals("401")) {
                    GloableConstant.getInstance().clearAll();
                    ToastUtil.showShortToast("请重新登录");
                    startActivity(new Intent(BookDetailActivity.this, LoginActivity.class));
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
        map.put("goods_id", mBookId);
//        map.put("user_id","1");
        map.put("user_id", (String) SPUtils.getSpValues(SPKeyValuesUtils.SP_USER_ID, SPUtils.TYPE_STRING));
        map.put("type", "2");
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
                    mCollectImg.setBackground(ContextCompat.getDrawable(BookDetailActivity.this, R.mipmap.tab_collection));
                    EventBus.getDefault().post("图书取消收藏");
//                    mCollectImg.setImageResource(R.mipmap.tab_collection);
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
                }else if (result.getStatus_code().equals("401")) {
                    GloableConstant.getInstance().clearAll();
                    ToastUtil.showShortToast("请重新登录");
                    startActivity(new Intent(BookDetailActivity.this, LoginActivity.class));
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
            mRecycleView.setAdapter(new CommonAdapter<BookDetailResponse.mActivity>(BookDetailActivity.this, R.layout.item_school_courses_detial_tag, activity) {

                @Override
                protected void convert(ViewHolder holder, final BookDetailResponse.mActivity mActivity, int position) {
                    holder.setText(R.id.tag_name, mActivity.getActivity_name());
                    ImageView mIcon = (ImageView) holder.getConvertView().findViewById(R.id.tag_img);
                    Glide.with(BookDetailActivity.this).load(mActivity.getIcon()).into(mIcon);
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

    SharePopWindow mSharePopWindow;

    public void showSharePopWindow() {

        mSharePopWindow = new SharePopWindow(this);
        mSharePopWindow.isShowing();
        final String url = ApiUrl.BASEIMAGE +"api/v1/book_share?book_id=" + mBookId+"&user_id="+user_id;
        mSharePopWindow.setOnSelectItemListener(new OnSelectItemListener() {
            @Override
            public void selectItem(String name, int type) {
                if (mSharePopWindow != null && mSharePopWindow.isShowing()) {
                    mSharePopWindow.dismiss();
                    switch (type) {
                        case SharePopWindow.POP_WINDOW_ITEM_1:
//
                            ShareUtils.shareWeb(BookDetailActivity.this, url, mTitle
                                    , mTitle, mImage, R.mipmap.ic_launcher, SHARE_MEDIA.WEIXIN
                            );
                            break;
                        case SharePopWindow.POP_WINDOW_ITEM_2:
//
                            ShareUtils.shareWeb(BookDetailActivity.this, url, mTitle
                                    , mTitle, mImage, R.mipmap.ic_launcher, SHARE_MEDIA.WEIXIN_CIRCLE
                            );
//
                            break;
                        case SharePopWindow.POP_WINDOW_ITEM_3:
//
                            ShareUtils.shareWeb(BookDetailActivity.this, url, mTitle
                                    , mTitle, mImage, R.mipmap.ic_launcher, SHARE_MEDIA.QQ
                            );
                            break;
                        case SharePopWindow.POP_WINDOW_ITEM_4:

                            ShareUtils.shareWeb(BookDetailActivity.this, url, mTitle
                                    , mTitle, mImage, R.mipmap.ic_launcher, SHARE_MEDIA.QZONE
                            );
                            break;
                        case SharePopWindow.POP_WINDOW_ITEM_5:
                            ShareUtils.shareWeb(BookDetailActivity.this, url, mTitle
                                    , mTitle, mImage, R.mipmap.ic_launcher, SHARE_MEDIA.SMS
                            );
//
                            break;
                        case SharePopWindow.POP_WINDOW_ITEM_6:

                            ShareUtils.shareWeibo(BookDetailActivity.this, url, mTitle
                                    , mTitle, mImage, R.mipmap.ic_launcher, SHARE_MEDIA.SINA
                            );
//
                            break;
                        default:
                            break;
                    }
                }
            }
        });
    }
}
