package com.jiaoshizige.teacherexam.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.alipay.sdk.app.PayTask;
import com.jiaoshizige.teacherexam.model.busmodel.BusRefreshClass;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.umeng.analytics.MobclickAgent;
import com.jiaoshizige.teacherexam.R;
import com.jiaoshizige.teacherexam.base.BaseActivity;
import com.jiaoshizige.teacherexam.base.GloableConstant;
import com.jiaoshizige.teacherexam.http.ApiUrl;
import com.jiaoshizige.teacherexam.http.MyCallBack;
import com.jiaoshizige.teacherexam.http.Xutil;
import com.jiaoshizige.teacherexam.model.CreatResponse;
import com.jiaoshizige.teacherexam.model.OpenOrderResponse;
import com.jiaoshizige.teacherexam.model.PayResponse;
import com.jiaoshizige.teacherexam.utils.PayResult;
import com.jiaoshizige.teacherexam.utils.SPKeyValuesUtils;
import com.jiaoshizige.teacherexam.utils.SPUtils;
import com.jiaoshizige.teacherexam.utils.ToastUtil;
import com.jiaoshizige.teacherexam.utils.ToolUtils;
import com.jiaoshizige.teacherexam.wxapi.Content;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/12/12 0012.
 */

public class OpenClassOrderActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.shop_name)
    TextView mName;
    @BindView(R.id.shop_price)
    TextView mPrice;
    @BindView(R.id.teacher_coin_switch)
    ToggleButton mSwitch;
    @BindView(R.id.coin_money)
    TextView mCoin;
    @BindView(R.id.select_red_packet)
    TextView mCouponsText;
    //    @BindView(R.id.coupon)
//    LinearLayout  mCoupons;
    @BindView(R.id.full_cut)
    EditText mPhone;
    @BindView(R.id.alipay)
    CheckBox mAlipay;
    @BindView(R.id.weixin_pay)
    CheckBox mWeiXin;
    @BindView(R.id.total_money)
    TextView mMoney;
    @BindView(R.id.btn_paymoney)
    Button mButton;
    @BindView(R.id.btn_layout_alipay)
    LinearLayout btn_layout_alipay;
    @BindView(R.id.btn_layout_weixin_pay)
    LinearLayout btn_layout_weixin_pay;

    private String _id, user_id, coupons_id, order_id;
    private List<OpenOrderResponse.mCoupons> mList;
    private View view;
    private PopupWindow pop;
    private float mCoupon_price, goodsPrice, mJsb_limit, allprice;
    private int type, integral;
    private String phone;
    private Intent intent;
    private IWXAPI iwxapi;
    private Context mContext;


    @Override
    protected int getLayoutId() {
        return R.layout.open_class_order_layout;
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("公开课确认订单");
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("公开课确认订单");
        MobclickAgent.onPause(this);
    }

    @Override
    protected void initView() {
        setSubTitle().setText("");
        setToolbarTitle().setText("确认订单");
        mContext = this;
        iwxapi = WXAPIFactory.createWXAPI(mContext, Content.APP_ID);
        iwxapi.registerApp(Content.APP_ID);
        user_id = (String) SPUtils.getSpValues(SPKeyValuesUtils.SP_USER_ID, SPUtils.TYPE_STRING);
        if (getIntent().getStringExtra("_id") != null) {
            _id = getIntent().getStringExtra("_id");
        }
        if (mAlipay.isChecked()) {
            mWeiXin.setChecked(false);
            type = 1;
        }
        if (mWeiXin.isChecked()) {
            mAlipay.setChecked(false);
            type = 2;
        }
        if (mSwitch.isChecked()) {
            integral = 1;
        } else {
            integral = 0;
        }

        mCouponsText.setOnClickListener(this);
        mAlipay.setOnClickListener(this);
        mWeiXin.setOnClickListener(this);
        mButton.setOnClickListener(this);
        mSwitch.setOnClickListener(this);
        btn_layout_alipay.setOnClickListener(this);
        btn_layout_weixin_pay.setOnClickListener(this);
        openOder();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.teacher_coin_switch:
                if (mSwitch.isChecked()) {
                    integral = 1;
                    mCoin.setVisibility(View.VISIBLE);
                } else {
                    integral = 0;
                    mCoin.setVisibility(View.GONE);
                }
                break;
            case R.id.select_red_packet:
                showCoupons();
                break;
            case R.id.btn_layout_alipay:
                mAlipay.setChecked(true);
                mWeiXin.setChecked(false);
                type = 1;
                break;
            case R.id.btn_layout_weixin_pay:
                mAlipay.setChecked(false);
                mWeiXin.setChecked(true);
                type = 2;
                break;
            case R.id.alipay:
                mAlipay.setChecked(true);
                mWeiXin.setChecked(false);
                type = 1;
                break;
            case R.id.weixin_pay:
                mAlipay.setChecked(false);
                mWeiXin.setChecked(true);
                type = 2;
                break;
            case R.id.btn_paymoney:
                phone = mPhone.getText().toString().trim();
                if (TextUtils.isEmpty(phone)) {
                    ToastUtil.showShortToast("请输入您的手机号码");
                } else {
                    if (ToolUtils.isCellphone(phone)) {
                        openCommit();
                    } else {
                        ToastUtil.showShortToast("您输入的手机号码不正确，请重新输入");
                    }

                }

                break;
        }
    }

    /**
     * 公开课提交订单
     */
    private void openCommit() {
        phone = mPhone.getText().toString().trim();
//        if (TextUtils.isEmpty(phone)) {
//            ToastUtil.showShortToast("请输入您的手机号码");
//        }
        Map<String, Object> map = new HashMap<>();
//        map.put("user_id",1);
        map.put("user_id", SPUtils.getSpValues(SPKeyValuesUtils.SP_USER_ID, SPUtils.TYPE_STRING));
        map.put("id", _id);
        map.put("integral", integral);
        map.put("coupons_id", coupons_id);
        map.put("pay_type", type);
        map.put("type", 4);
        map.put("mobile", phone);
        GloableConstant.getInstance().showmeidialog(this);
        Xutil.Post(ApiUrl.CREAT, map, new MyCallBack<CreatResponse>() {
            @Override
            public void onSuccess(CreatResponse result) {
                super.onSuccess(result);
                GloableConstant.getInstance().stopProgressDialog();
                if (result.getStatus_code().equals("200")) {
                    order_id = result.getData().getOrder_id();

                    orderPay();


                } else {
                    ToastUtil.showShortToast("请求失败");
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
     * 确认订单
     */
    private void openOder() {
        Map<String, Object> map = new HashMap<>();
//        map.put("user_id", 1);
        map.put("user_id", SPUtils.getSpValues(SPKeyValuesUtils.SP_USER_ID, SPUtils.TYPE_STRING));
        map.put("open_course_id", _id);
        Log.e("*********openmap", map.toString());
        GloableConstant.getInstance().showmeidialog(this);
        Xutil.Post(ApiUrl.OPENORDER, map, new MyCallBack<OpenOrderResponse>() {
            @Override
            public void onSuccess(OpenOrderResponse result) {
                super.onSuccess(result);
                GloableConstant.getInstance().stopProgressDialog();
                Log.e("*********openresult", result.getStatus_code());
                if (result.getStatus_code().equals("200")) {
                    mName.setText(result.getData().getName());
                    mPrice.setText(result.getData().getPrice() + "");
                    goodsPrice = result.getData().getPrice();
                    if (result.getData().getCoupons().size() > 0 && result.getData().getCoupons() != null) {
                        mList = result.getData().getCoupons();
                        mCouponsText.setClickable(true);
                        mCouponsText.setText("可用的优惠券");
                    } else {
                        mCouponsText.setClickable(false);
                        mCouponsText.setText("无可用的优惠券");
                    }
                    if (result.getData().getJsb_limit() != 0) {
                        mJsb_limit = result.getData().getJsb_limit();
                        if (mSwitch.isChecked()) {
                            mCoin.setVisibility(View.VISIBLE);
                            mCoin.setText(result.getData().getJsb_limit() + "");
                            mMoney.setText((goodsPrice - mJsb_limit) + "");
                        } else {
                            mCoin.setVisibility(View.GONE);
                            mCoin.setText("");
                            mMoney.setText(goodsPrice + "");
                        }
                    } else {
                        if (mSwitch.isChecked()) {
                            mCoin.setVisibility(View.VISIBLE);
                            mCoin.setText(result.getData().getJsb_limit() + "");
                            mMoney.setText((goodsPrice) + "");
                        } else {
                            mCoin.setVisibility(View.GONE);
                            mCoin.setText("");
                            mMoney.setText(goodsPrice + "");
                        }
//
                    }

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
        mRecyclerView.setAdapter(new CommonAdapter<OpenOrderResponse.mCoupons>(this, R.layout.item_my_coupons, mList) {

            @Override
            protected void convert(ViewHolder holder, final OpenOrderResponse.mCoupons mCoupons, final int position) {
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
                        mCouponsText.setText("-￥" + mCoupons.getPrice());
                        mCoupon_price = mCoupons.getPrice();
                        coupons_id = mCoupons.getId();
                        Log.e("********mCounpon_price", mCoupon_price + "");
                        if (mSwitch.isChecked()) {
                            allprice = goodsPrice - mJsb_limit - mCoupon_price;
                        } else {
                            allprice = goodsPrice - mJsb_limit;
                        }
                        mMoney.setText(allprice + "");
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

    // 支付接口
    private void orderPay() {
        Map<String, Object> map = new HashMap<>();
//        map.put("user_id", 1);
        map.put("user_id", SPUtils.getSpValues(SPKeyValuesUtils.SP_USER_ID, SPUtils.TYPE_STRING));
        map.put("order_id", order_id);
        map.put("pay_type", type);
        map.put("type", type);
        map.put("az_pay", "1");
        Log.e("*********parmap", map.toString());
        GloableConstant.getInstance().showmeidialog(this);
        Xutil.Post(ApiUrl.PAY, map, new MyCallBack<PayResponse>() {
            @Override
            public void onSuccess(PayResponse result) {
                super.onSuccess(result);
                GloableConstant.getInstance().stopProgressDialog();
                if (result.getStatus_code().equals("200")) {
                    GloableConstant.getInstance().setOrder("2");
                    if (type == 1) {
                        final String str = result.getData().getSign();
                        Log.e("*********str", str);
                        Runnable payRunnable = new Runnable() {

                            @Override
                            public void run() {
                                // 构造PayTask 对象
                                PayTask alipay = new PayTask(OpenClassOrderActivity.this);
                                // 调用支付接口，获取支付结果
                                String result = alipay.pay(str, true);
                                Message msg = new Message();
                                msg.what = SDK_PAY_FLAG;
                                msg.obj = result;
                                mHandler.sendMessage(msg);
                            }
                        };
                        // 必须异步调用
                        Thread payThread = new Thread(payRunnable);
                        payThread.start();
                    } else {
                        if (ToolUtils.isWeChatAppInstalled(mContext)) {
                            PayReq req = new PayReq();
                            req.appId = result.getData().getAppid();
                            req.prepayId = result.getData().getPrepayid();
                            req.partnerId = result.getData().getPartnerid();
                            req.packageValue = result.getData().getPack();
                            req.timeStamp = result.getData().getTimestamp();
                            req.nonceStr = result.getData().getNoncestr();
                            req.sign = result.getData().getSign();
                            iwxapi.sendReq(req);
                        } else {
                            ToastUtil.showShortToast("您还未安装微信客户端");
                        }
                    }

                } else {
                    ToastUtil.showShortToast("获取失败");
                }

            }

            @Override
            public void onFinished() {
                super.onFinished();
                GloableConstant.getInstance().stopProgressDialog();
            }
        });
    }


    //支付宝集成
    private static final int SDK_PAY_FLAG = 1;
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    PayResult payResult = new PayResult((String) msg.obj);
                    /**
                     * 同步返回的结果必须放置到服务端进行验证
                     *  建议商户依赖异步通知
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {
                        EventBus.getDefault().post(new BusRefreshClass());

                        Toast.makeText(OpenClassOrderActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                        intent = new Intent();
                        intent.setClass(OpenClassOrderActivity.this, PayResultActivity.class);
                        intent.putExtra("flag", "success");
                        startActivity(intent);
                        finish();
                    } else {
                        // 判断resultStatus 为非"9000"则代表可能支付失败
                        // "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            Toast.makeText(OpenClassOrderActivity.this, "支付结果确认中", Toast.LENGTH_SHORT).show();
                        } else if (TextUtils.equals(resultStatus, "4000")) {
                            intent = new Intent();
                            intent.setClass(OpenClassOrderActivity.this, PayResultActivity.class);
                            intent.putExtra("flag", "failure");
                            startActivity(intent);
                            finish();
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            Toast.makeText(OpenClassOrderActivity.this, "您还未安装支付宝客户端", Toast.LENGTH_SHORT).show();
                        } else if (TextUtils.equals(resultStatus, "6001")) {
                            Toast.makeText(OpenClassOrderActivity.this, "您取消了支付,请重新支付!", Toast.LENGTH_SHORT).show();
                        }


                    }


                    break;
                }
                default:
                    break;
            }
        }


    };


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
