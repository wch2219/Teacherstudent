package com.jiaoshizige.teacherexam.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.bumptech.glide.Glide;
import com.umeng.analytics.MobclickAgent;
import com.jiaoshizige.teacherexam.R;
import com.jiaoshizige.teacherexam.base.BaseActivity;
import com.jiaoshizige.teacherexam.base.GloableConstant;
import com.jiaoshizige.teacherexam.http.ApiUrl;
import com.jiaoshizige.teacherexam.http.MyCallBack;
import com.jiaoshizige.teacherexam.http.Xutil;
import com.jiaoshizige.teacherexam.model.BookConfirmResponse;
import com.jiaoshizige.teacherexam.model.CreatResponse;
import com.jiaoshizige.teacherexam.model.SelectAddressRespomse;
import com.jiaoshizige.teacherexam.utils.SPKeyValuesUtils;
import com.jiaoshizige.teacherexam.utils.SPUtils;
import com.jiaoshizige.teacherexam.utils.ToastUtil;
import com.jiaoshizige.teacherexam.utils.ToolUtils;
import com.jiaoshizige.teacherexam.yy.activity.AddressListActivity;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/a1/21.
 * 确认订单
 */

public class ConfirmOrderActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.express_price)
    TextView mFreight;
    @BindView(R.id.shop_name)
    TextView mName;
    @BindView(R.id.price)
    TextView mPrice;
    @BindView(R.id.leave_word)
    EditText mLeave;
    @BindView(R.id.teacher_coin_switch)
    ToggleButton mSwitch;
    @BindView(R.id.coin_money)
    TextView mCoin;
    @BindView(R.id.full_cut)
    TextView mFullCut;
    @BindView(R.id.coupon)
    LinearLayout mCoupon;
    @BindView(R.id.select_red_packet)
    TextView mCouponText;
    @BindView(R.id.express_money)
    TextView mShipping;
    @BindView(R.id.total_money)
    TextView mAllPrice;
    @BindView(R.id.select_address)
    LinearLayout mAdress;
    @BindView(R.id.select_address_null)
    LinearLayout mSelectAddress;
    @BindView(R.id.name)
    TextView mPeopleName;
    @BindView(R.id.phone)
    TextView mPhone;
    @BindView(R.id.detail)
    TextView mDetail;
    @BindView(R.id.discount)
    LinearLayout mDiscount;
    @BindView(R.id.alipay)
    CheckBox mAlipay;
    @BindView(R.id.weixin_pay)
    CheckBox mWeiXin;
    @BindView(R.id.btn_paymoney)
    Button mButton;
    @BindView(R.id.shop_price)
    TextView mShopPrice;
    @BindView(R.id._img)
    ImageView _img;
    @BindView(R.id.text_title)
    TextView mDescribe;
    @BindView(R.id.recycle_view)
    RecyclerView mrecyclerView;
    @BindView(R.id.my_phone_num)
    LinearLayout mLL;
    @BindView(R.id.my_phone)
    EditText mMyPhone;
    @BindView(R.id.yunfei)
    LinearLayout mYunFei;
    @BindView(R.id.mLine)
    View mLine;
    @BindView(R.id.all_goodsprice)
    TextView mAllGoods;
    @BindView(R.id.all_price)
    LinearLayout mAll;

    private Intent intent;
    private String _id, address_id, coupons_id, pay_type, type, mobile = "", remark;
    private double mCoupon_price, price, shipping, mJsb_limit, off_money, allprice;
    private String integral, str;
    private String flag, user_id;//flag 1 图书确认订单 2班级确认订单  3课程确认订单
    private List<BookConfirmResponse.mCoupons> mList;
    private View view;
    private PopupWindow pop;
    private Context mContext;
    private java.text.DecimalFormat myformat;
    private int mNoAddress = 0;//是否有收货地址判断 0没有地址
    private String tag;//赠送图书0是有图书
    private String mType;//1 是套餐  2是单科购买

    @Override
    protected int getLayoutId() {
        return R.layout.confirm_order_activity;
    }

    @Override
    protected void onResume() {
        super.onResume();
//        Log.e("*******onResume",tag);
        if (tag != null && tag != "") {
            if (tag.equals("0")) {
                selectAddress("1");

            }
        }
        MobclickAgent.onPageStart("确认订单");
        MobclickAgent.onResume(this);

    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("确认订单");
        MobclickAgent.onPause(this);
    }

    @Override
    protected void initView() {
        setSubTitle().setText("");
        setToolbarTitle().setText("确认订单");
        mContext = this;
        myformat = new java.text.DecimalFormat("0.00");
        user_id = (String) SPUtils.getSpValues(SPKeyValuesUtils.SP_USER_ID, SPUtils.TYPE_STRING);
        intent = getIntent();
        if (intent.getStringExtra("flag") != null) {
            flag = intent.getStringExtra("flag");
        }

        Log.d("************flag", flag);
        //图书
        if (flag.equals("1")) {
            _id = intent.getStringExtra("book_id");
            Log.d("************book_id", _id);
            BookConfirm();
            type = "2";
        }
        if (flag.equals("2")) {
            _id = intent.getStringExtra("group_id");
            classConfirm();
            type = "3";

        }
        //班级 课程
        if (flag.equals("3")) {
            _id = intent.getStringExtra("course_id");
            mType=intent.getStringExtra("type");
            if (mType.equals("1")){
                type = "3";
            }else if (mType.equals("2")){
                type = "1";
            }
            courseConfirm();
//            type = "1";
        }

        if (mSwitch.isChecked()) {
            integral = "1";
        } else {
            integral = "0";
        }
        if (mAlipay.isChecked()) {
            mWeiXin.setChecked(false);
            pay_type = "1";
        } else {
            mWeiXin.setChecked(true);
            pay_type = "2";
        }
        mCoupon.setOnClickListener(this);
        mSelectAddress.setOnClickListener(this);
//        mSwitch.setOnClickListener(this);
        mButton.setOnClickListener(this);
        mAlipay.setOnClickListener(this);
        mWeiXin.setOnClickListener(this);
        mButton.setOnClickListener(this);

        mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (mSwitch.isChecked()) {
//                    ToastUtil.showShortToast("1111111111");
                    integral = "1";
                    mCoin.setVisibility(View.VISIBLE);
                    mCoin.setText("-￥" + mJsb_limit);
                    allprice = (price + shipping - mJsb_limit - off_money - mCoupon_price);
                } else {
                    integral = "0";
                    mCoin.setVisibility(View.GONE);
                    allprice = ((price + shipping - off_money - mCoupon_price));
                }
//                double c = 3.157215;
                str = myformat.format(allprice);
                Log.e("********allpricestr", str);
                Log.e("********price", price + "/");
                Log.e("********shipping", shipping + "/");
                Log.e("********off_money", off_money + "/");
                Log.e("********mCoupon_price", mCoupon_price + "/");
                Log.e("********mJsb_limit", mJsb_limit + "/");
                mAllPrice.setText(str);
            }
        });
        selectAddress("active");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.coupon:
                showCoupons();
                break;
            case R.id.select_address_null:
                selectAddress("");
                Intent intent1 = new Intent();
                intent1.setClass(ConfirmOrderActivity.this, AddressListActivity.class);
                startActivity(intent1);
                break;
            case R.id.alipay:
                mWeiXin.setChecked(false);
                mAlipay.setChecked(true);
                pay_type = "1";
                break;
            case R.id.weixin_pay:
                mWeiXin.setChecked(true);
                mAlipay.setChecked(false);
                pay_type = "2";
                break;
            case R.id.btn_paymoney:
                if (mNoAddress == 1) {
                    if (tag.equals("0")) {
                        creatOrder();
                    } else {
                        mobile = mMyPhone.getText().toString().trim();
                        Log.e("********moble", mobile);
                        if (TextUtils.isEmpty(mobile)) {
                            ToastUtil.showShortToast("请输入手机号码");
                        } else {
                           if ( ToolUtils.isCellphone(mobile)){
                               creatOrder();
                           }else {
                               ToastUtil.showShortToast("手机号码格式不正确");
                           }

                        }
                    }

                } else {
                    ToastUtil.showShortToast("请先选择收货地址");
                }

                break;
            default:
                break;
        }
    }

    /**
     * 课程确认订单
     */
    private void courseConfirm() {
        Map<String, Object> map = new HashMap<>();
//        map.put("user_id", 1);
        map.put("user_id", (String) SPUtils.getSpValues(SPKeyValuesUtils.SP_USER_ID, SPUtils.TYPE_STRING));
        map.put("course_id", _id);
        map.put("type_id",mType);
        Log.e("*******course", map.toString());
        GloableConstant.getInstance().showmeidialog(this);
        Xutil.Post(ApiUrl.COURSE_CONFIRM, map, new MyCallBack<BookConfirmResponse>() {
            @Override
            public void onSuccess(final BookConfirmResponse result) {
                super.onSuccess(result);
                GloableConstant.getInstance().stopProgressDialog();
                if (result.getStatus_code().equals("200")) {
                    mFreight.setText("￥" +result.getData().getShipping_price());
                    mShipping.setText("含运费：￥" + result.getData().getShipping_price() + "元");
                    mName.setText(result.getData().getName());
                    mPrice.setText("￥" +result.getData().getPrice());
                    mAllGoods.setText("￥"+result.getData().getPrice());
                    mShopPrice.setText("￥" + result.getData().getMarket_price());
                    mShopPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
                    Glide.with(mContext).load(result.getData().getImages()).apply(GloableConstant.getInstance().getDefaultOptionssmall()).into(_img);
                    if (result.getData().getGive_books().size() > 0) {
                        tag = "0";
                        mDescribe.setVisibility(View.VISIBLE);
                        mrecyclerView.setVisibility(View.VISIBLE);
                        mSelectAddress.setVisibility(View.VISIBLE);
                        mYunFei.setVisibility(View.VISIBLE);
                        mLine.setVisibility(View.VISIBLE);
//                        mMyPhone.setVisibility(View.GONE);
                        mLL.setVisibility(View.GONE);
                        mAll.setVisibility(View.GONE);
                        mrecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
                        mrecyclerView.setAdapter(new CommonAdapter<BookConfirmResponse.mGive_Books>(mContext, R.layout.item_give_book_layout, result.getData().getGive_books()) {
                            @Override
                            protected void convert(ViewHolder holder, BookConfirmResponse.mGive_Books mGive_books, int position) {
                                ImageView imageView = (ImageView) holder.getConvertView().findViewById(R.id.give_image);
                                Glide.with(mContext).load(mGive_books.getImages()).apply(GloableConstant.getInstance().getDefaultOptionssmall()).into(imageView);
                                holder.setText(R.id.give_name, mGive_books.getBook_name());
                                holder.setText(R.id.give_price, "￥" + mGive_books.getPrice());
                            }
                        });
                        selectAddress("1");
                    } else {
                        mNoAddress = 1;
                        tag = "1";
                        mDescribe.setVisibility(View.GONE);
                        mrecyclerView.setVisibility(View.GONE);
                        mSelectAddress.setVisibility(View.GONE);
//                        mMyPhone.setVisibility(View.VISIBLE);
                        mLL.setVisibility(View.VISIBLE);
                        mYunFei.setVisibility(View.GONE);
                        mLine.setVisibility(View.GONE);
                        mAll.setVisibility(View.VISIBLE);
                    }

                    if (result.getData().getActivity() == "") {
                        mDiscount.setVisibility(View.GONE);
                    } else {
                        mDiscount.setVisibility(View.VISIBLE);
                        mFullCut.setText(result.getData().getActivity());
                        off_money = result.getData().getActivity_price();
                    }

                    price = result.getData().getPrice();
                    shipping = result.getData().getShipping_price();
                    mJsb_limit = result.getData().getJsb_limit();

                    if (mSwitch.isChecked()) {
                        mCoin.setVisibility(View.VISIBLE);
                        mCoin.setText("-￥" + mJsb_limit);
                        allprice = (result.getData().getPrice() + result.getData().getShipping_price() - mJsb_limit);
                        Log.e("***********mJsb_limit", mJsb_limit + "");
                    } else {
                        mCoin.setVisibility(View.GONE);
                        allprice = (result.getData().getPrice() + result.getData().getShipping_price());
                    }
                    str = myformat.format(allprice);
                    mAllPrice.setText(str);

                    if (result.getData().getCoupons().size() > 0 && result.getData().getCoupons() != null) {
                        mList = result.getData().getCoupons();
                        mCoupon.setClickable(true);
                        mCouponText.setText("可用的优惠券");
                        allprice = (result.getData().getPrice() + result.getData().getShipping_price() -
                                result.getData().getJsb_limit() - off_money);
                        str = myformat.format(allprice);
                        mAllPrice.setText(str);
                    } else {
                        mCoupon.setClickable(false);
                        mCouponText.setText("无可用的优惠券");

                        if (mSwitch.isChecked()) {
                            allprice = (result.getData().getPrice() + result.getData().getShipping_price() -
                                    result.getData().getJsb_limit() - off_money);

                        } else {
                            allprice = (result.getData().getPrice() + result.getData().getShipping_price() - off_money);
                        }
                        str = myformat.format(allprice);
                        mAllPrice.setText(str);
                    }
                }
            }

            @Override
            public void onFinished() {
                super.onFinished();
                GloableConstant.getInstance().stopProgressDialog();
            }
        });
    }

    /**
     * 班级确认订单
     */
    private void classConfirm() {
        Map<String, Object> map = new HashMap<>();
//        map.put("user_id", 1);
        map.put("user_id", (String) SPUtils.getSpValues(SPKeyValuesUtils.SP_USER_ID, SPUtils.TYPE_STRING));
        map.put("group_id", _id);
        GloableConstant.getInstance().showmeidialog(this);
        Xutil.Post(ApiUrl.CLASS_CONFIRM, map, new MyCallBack<BookConfirmResponse>() {
            @Override
            public void onSuccess(final BookConfirmResponse result) {
                super.onSuccess(result);
                GloableConstant.getInstance().stopProgressDialog();
                if (result.getStatus_code().equals("200")) {
                    mFreight.setText(result.getData().getShipping_price() + "元");
                    mShipping.setText("含运费：￥" + result.getData().getShipping_price() + "元");
                    mName.setText(result.getData().getGroup_name());
                    mPrice.setText("￥" + result.getData().getPrice());
                    mShopPrice.setText("￥" + result.getData().getMarket_price());
                    mShopPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
                    Glide.with(mContext).load(result.getData().getImages()).apply(GloableConstant.getInstance().getDefaultOptionssmall()).into(_img);
                    if (result.getData().getGive_books().size() > 0) {
                        tag = "0";
                        mDescribe.setVisibility(View.VISIBLE);
                        mrecyclerView.setVisibility(View.VISIBLE);
                        mSelectAddress.setVisibility(View.VISIBLE);
                        mLL.setVisibility(View.GONE);
                        mAll.setVisibility(View.GONE);
                        mrecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
                        mrecyclerView.setAdapter(new CommonAdapter<BookConfirmResponse.mGive_Books>(mContext, R.layout.item_give_book_layout, result.getData().getGive_books()) {
                            @Override
                            protected void convert(ViewHolder holder, BookConfirmResponse.mGive_Books mGive_books, int position) {
                                ImageView imageView = (ImageView) holder.getConvertView().findViewById(R.id.give_image);
                                Glide.with(mContext).load(mGive_books.getImages()).apply(GloableConstant.getInstance().getDefaultOptionssmall()).into(imageView);
                                holder.setText(R.id.give_name, mGive_books.getBook_name());
                                holder.setText(R.id.give_price, "￥" + mGive_books.getPrice());
                            }
                        });
                        selectAddress("1");
                    } else {
                        mNoAddress = 1;
                        tag = "1";
                        mDescribe.setVisibility(View.GONE);
                        mrecyclerView.setVisibility(View.GONE);
                        mSelectAddress.setVisibility(View.GONE);
                        mLL.setVisibility(View.VISIBLE);
                        mAll.setVisibility(View.VISIBLE);
                    }
                    if (result.getData().getActivity() == "") {
                        mDiscount.setVisibility(View.GONE);
                    } else {
                        mDiscount.setVisibility(View.VISIBLE);
                        mFullCut.setText(result.getData().getActivity());
                        off_money = result.getData().getActivity_price();
                    }
                    price = result.getData().getPrice();
                    shipping = result.getData().getShipping_price();
                    mJsb_limit = result.getData().getJsb_limit();
                    if (result.getData().getCoupons().size() > 0 && result.getData().getCoupons() != null) {
                        mList = result.getData().getCoupons();
                        mCoupon.setClickable(true);
                        mCouponText.setText("可用的优惠券");
                        allprice = (result.getData().getPrice() + result.getData().getShipping_price() -
                                result.getData().getJsb_limit() - off_money);
                        str = myformat.format(allprice);
                        mAllPrice.setText(str);
                    } else {
                        mCoupon.setClickable(false);
                        mCouponText.setText("无可用的优惠券");
                        if (mSwitch.isChecked()) {
                            allprice = (result.getData().getPrice() + result.getData().getShipping_price() -
                                    result.getData().getJsb_limit() - off_money);
                        } else {
                            allprice = (result.getData().getPrice() + result.getData().getShipping_price() - off_money);
                        }
                        str = myformat.format(allprice);
                        mAllPrice.setText(str);
                    }
                    if (mSwitch.isChecked()) {
                        mCoin.setVisibility(View.VISIBLE);
                        mCoin.setText("-￥" + result.getData().getJsb_limit());
                        Log.e("***********", result.getData().getJsb_limit() + "");
                    } else {
                        mCoin.setVisibility(View.GONE);
                    }
//                    mSwitch.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            if (mSwitch.isChecked()) {
//                                mCoin.setVisibility(View.VISIBLE);
//                                mCoin.setText("-￥" + result.getData().getJsb_limit());
//                                Log.d("*******result", result.getData().getJsb_limit() + "");
//                            } else {
//                                mCoin.setVisibility(View.GONE);
//                            }
//                        }
//                    });


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
     * 图书确认订单
     */
    private void BookConfirm() {
        Map<String, Object> map = new HashMap<>();
//        map.put("user_id", 1);
        map.put("user_id", (String) SPUtils.getSpValues(SPKeyValuesUtils.SP_USER_ID, SPUtils.TYPE_STRING));
        map.put("book_id", _id);
        Log.e("**********map", map.toString());
        GloableConstant.getInstance().showmeidialog(this);
        GloableConstant.getInstance().showmeidialog(this);
        Xutil.Post(ApiUrl.BOOKCONFIRM, map, new MyCallBack<BookConfirmResponse>() {
            @Override
            public void onSuccess(final BookConfirmResponse result) {
                super.onSuccess(result);
                GloableConstant.getInstance().stopProgressDialog();
                if (result.getStatus_code().equals("200")) {
                    tag = "0";
                    selectAddress("1");
                    mLL.setVisibility(View.GONE);
                    mFreight.setText(result.getData().getShipping_price() + "元");
                    mShipping.setText("含运费：￥" + result.getData().getShipping_price() + "元");
                    mName.setText(result.getData().getBook_name());
                    mPrice.setText(result.getData().getPrice() + "元");
                    if (result.getData().getActivity() == "") {
                        mDiscount.setVisibility(View.GONE);
                    } else {
                        mDiscount.setVisibility(View.VISIBLE);
                        mFullCut.setText(result.getData().getActivity());
                        off_money = result.getData().getActivity_price();
                    }
                    mShopPrice.setText("￥" + result.getData().getMarket_price());
                    mShopPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
                    Glide.with(mContext).load(result.getData().getImages()).apply(GloableConstant.getInstance().getDefaultOptionssmall()).into(_img);
                    mDescribe.setVisibility(View.GONE);
                    mrecyclerView.setVisibility(View.GONE);
                    price = result.getData().getPrice();
                    shipping = result.getData().getShipping_price();
                    mJsb_limit = result.getData().getJsb_limit();
                    if (result.getData().getCoupons().size() > 0 && result.getData().getCoupons() != null) {
                        mList = result.getData().getCoupons();
                        mCoupon.setClickable(true);
                        mCouponText.setText("可用的优惠券");
                        allprice = (result.getData().getPrice() + result.getData().getShipping_price() -
                                result.getData().getJsb_limit() - off_money);
                        str = myformat.format(allprice);
                        mAllPrice.setText(str);
                    } else {
                        mCoupon.setClickable(false);
                        mCouponText.setText("无可用的优惠券");

                        if (mSwitch.isChecked()) {
                            allprice = (result.getData().getPrice() + result.getData().getShipping_price() -
                                    result.getData().getJsb_limit() - off_money);

                        } else {
                            allprice = (result.getData().getPrice() + result.getData().getShipping_price() - off_money);
                        }
                        str = myformat.format(allprice);
                        mAllPrice.setText(str);

                    }
                    if (mSwitch.isChecked()) {
                        mCoin.setVisibility(View.VISIBLE);
                        mCoin.setText("-￥" + result.getData().getJsb_limit());
                        Log.e("***********", result.getData().getJsb_limit() + "");
                    } else {
                        mCoin.setVisibility(View.GONE);
                    }
                    mSwitch.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (mSwitch.isChecked()) {
                                mCoin.setVisibility(View.VISIBLE);
                                mCoin.setText("-￥" + result.getData().getJsb_limit());
                                Log.d("*******result", result.getData().getJsb_limit() + "");
                            } else {
                                mCoin.setVisibility(View.GONE);
                            }
                        }
                    });


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
     * 选择收货地址
     */
    private void selectAddress(String active) {
        Map<String, Object> map = new HashMap<>();
//        map.put("user_id", 1);
        map.put("user_id", (String) SPUtils.getSpValues(SPKeyValuesUtils.SP_USER_ID, SPUtils.TYPE_STRING));
        map.put("active", active);
        GloableConstant.getInstance().showmeidialog(this);
        Xutil.Post(ApiUrl.SELECTADRESS, map, new MyCallBack<SelectAddressRespomse>() {
            @Override
            public void onSuccess(SelectAddressRespomse result) {
                super.onSuccess(result);
                GloableConstant.getInstance().stopProgressDialog();
                if (result.getStatus_code().equals("200")) {
                    if (result.getData() != null && result.getData().size() > 0) {
                        mNoAddress = 1;
                        mAdress.setVisibility(View.VISIBLE);
                        address_id = result.getData().get(0).getId();
                        mobile = result.getData().get(0).getMobile();
                        mPeopleName.setText(result.getData().get(0).getName());
                        mPhone.setText(result.getData().get(0).getMobile());
                        mDetail.setText(result.getData().get(0).getProvince() + result.getData().get(0).getCity() +
                                result.getData().get(0).getCounty());

                    } else {
                        mAdress.setVisibility(View.GONE);
                    }
                } else if (result.getStatus_code().equals("203")) {
                    mAdress.setVisibility(View.GONE);
                    mNoAddress = 1;
                }
            }

            @Override
            public void onFinished() {
                super.onFinished();
                GloableConstant.getInstance().stopProgressDialog();
            }
        });

    }

    /**
     * 弹出优惠券
     */
    private RecyclerView mRecyclerView;

    private void showCoupons() {
        view = LayoutInflater.from(this).inflate(R.layout.class_fragment_layout, null);
        pop = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT,
                800);
        pop.setFocusable(true);// 取得焦点
        pop.setBackgroundDrawable(new BitmapDrawable());
        //添加弹出、弹入的动画
        pop.setAnimationStyle(R.style.Popupwindow);
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        pop.showAtLocation(view, Gravity.LEFT | Gravity.BOTTOM, 0, -location[1]);
        //点击外部消失
        pop.setOutsideTouchable(true);
        //设置可以点击
        pop.setTouchable(true);
        setBackgroundAlpha(0.5f);//设置屏幕透明度
        pop.showAtLocation(view, Gravity.CENTER, 0, 0);
        pop.setOnDismissListener(new poponDismissListener());
        mRecyclerView = (RecyclerView) view.findViewById(R.id.class_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(new CommonAdapter<BookConfirmResponse.mCoupons>(this, R.layout.item_my_coupons, mList) {

            @Override
            protected void convert(ViewHolder holder, final BookConfirmResponse.mCoupons mCoupons, final int position) {
                holder.setText(R.id.coupon_name, mCoupons.getName());
                holder.setText(R.id.coupon_min_price, "满" + mCoupons.getMin_price() + "减" + mCoupons.getPrice());
                holder.setText(R.id.coupon_price, "￥" + mCoupons.getPrice());
                holder.setText(R.id.coupon_end_time, "有效期至" + mCoupons.getEnd_time());
                TextView textView = (TextView) holder.getConvertView().findViewById(R.id.coupon_use);
                textView.setVisibility(View.GONE);
                if (clickTemp == position) {
                    holder.setBackgroundRes(R.id.layout, R.drawable.yellow_select);

                } else {
                    holder.setBackgroundRes(R.id.layout, R.color.white);
                }
                holder.setOnClickListener(R.id.layout, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mCouponText.setText("-￥" + mCoupons.getPrice());
                        mCoupon_price = mCoupons.getPrice();
                        coupons_id = mCoupons.getId();
                        Log.e("********mCounpon_price", mCoupon_price + "");
                        if (mSwitch.isChecked()) {
                            allprice = (price + shipping - mCoupon_price - mJsb_limit - off_money);
                        } else {
                            allprice = (price + shipping - mCoupon_price - off_money);
                        }
                        str = myformat.format(allprice);
                        mAllPrice.setText(str);
                        setSelection(position);
                        notifyDataSetChanged();
                        if (pop.isShowing()) {
                            pop.dismiss();
                        }

                    }
                });

            }
        });

    }

    private int clickTemp = -1;

    public void setSelection(int position) {
        clickTemp = position;
    }

    /**
     * 提交订单
     */
    private String order_id;

    private void creatOrder() {
        remark = mLeave.getText().toString().trim();
        if (tag.equals("1")) {
            mobile = mMyPhone.getText().toString().trim();
//            Log.e("**********moble", mobile);
            if (TextUtils.isEmpty(mobile)) {
                ToastUtil.showShortToast("请输入手机号码");
            }
        }

        Map<String, Object> map = new HashMap<>();
//        map.put("user_id", 1);
        map.put("user_id", (String) SPUtils.getSpValues(SPKeyValuesUtils.SP_USER_ID, SPUtils.TYPE_STRING));
        map.put("id", _id);
        map.put("integral", integral);
        map.put("address_id", address_id);
        map.put("coupons_id", coupons_id);
        map.put("pay_type", pay_type);
        map.put("type", type);
        map.put("mobile", mobile);
        map.put("remark", remark);
        map.put("type_id",mType);
        Log.e("*******mapcreat", map.toString());
        GloableConstant.getInstance().showmeidialog(this);
        Xutil.Post(ApiUrl.CREAT, map, new MyCallBack<CreatResponse>() {
            @Override
            public void onSuccess(CreatResponse result) {
                super.onSuccess(result);
                GloableConstant.getInstance().stopProgressDialog();
                if (result.getStatus_code().equals("200")) {
                    Intent intent2 = new Intent();
                    order_id = result.getData().getOrder_id();
                    intent2.setClass(ConfirmOrderActivity.this, MyOrderDetailActivity.class);
                    intent2.putExtra("order_id", order_id);
                    intent2.putExtra("confirm","1");
                    intent2.putExtra("flag",flag);
                    intent2.putExtra("mobile",mobile);
                    Log.e("************order_id", order_id);
                    startActivity(intent2);
                    finish();
                }else {
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


    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha 屏幕透明度0.0-1.0 1表示完全不透明
     */
    public void setBackgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha;
        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        getWindow().setAttributes(lp);
    }


    class poponDismissListener implements PopupWindow.OnDismissListener {

        @Override
        public void onDismiss() {
            setBackgroundAlpha(1f);
        }
    }
}
