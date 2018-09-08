package com.jiaoshizige.teacherexam.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.bumptech.glide.Glide;
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
import com.jiaoshizige.teacherexam.http.SupportResponse;
import com.jiaoshizige.teacherexam.http.Xutil;
import com.jiaoshizige.teacherexam.model.OrderDatailResponse;
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
import java.util.Map;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/a1/30 0030.
 */

public class MyCourseOrderDetailActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.logistics_status)
    TextView mLogisticsStataus;
    @BindView(R.id.logistics_time)
    TextView mLogisticsTime;
    @BindView(R.id.name)
    TextView mName;
    @BindView(R.id.phone)
    TextView mPhone;
    @BindView(R.id.detail)
    TextView mAddress;
    @BindView(R.id.book_img)
    ImageView mBookImg;
    @BindView(R.id.book_name)
    TextView mBookName;
    @BindView(R.id.price)
    TextView mPrice;
    @BindView(R.id.shop_price)
    TextView mShopPrice;
    @BindView(R.id.number)
    TextView mNum;
    @BindView(R.id.pay_mode)
    TextView mMode;
    @BindView(R.id.shipping_mode)
    TextView mShipping;
    @BindView(R.id.card_price)
    TextView mCard;
    @BindView(R.id.teacher_coin_mode)
    TextView mTeacherCoin;
    @BindView(R.id.content)
    TextView mContent;
    @BindView(R.id.shipping_price)
    TextView mShippingPrice;
    @BindView(R.id.express_money)
    TextView mExpressMoney;
    @BindView(R.id.all_price)
    TextView mAllPrice;
    @BindView(R.id.total_money)
    TextView mTotalMoney;
    @BindView(R.id.shop_all_price1)
    TextView mShopAllPrice1;
    @BindView(R.id.shop_all_price)
    TextView mShopAllPrice;
    @BindView(R.id.btn_paymoney)
    Button mButton;
    @BindView(R.id.copy)
    TextView mCopy;
    @BindView(R.id.order_num)
    TextView mOrderNum;
    @BindView(R.id.logistics)
    LinearLayout mLogistics;
    @BindView(R.id.text)
    RelativeLayout textView;
    @BindView(R.id.recycle_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.layout)
    LinearLayout mLayout;
    @BindView(R.id.select_address)
    LinearLayout mSelectAddress;
    @BindView(R.id.view)
    View mView;
    @BindView(R.id.delivery_style)
    RelativeLayout mDeliveryStyle;
    @BindView(R.id.my_phone)
    TextView mPhone1;
    @BindView(R.id.my_phone_num)
    LinearLayout mPhoneNum;
    @BindView(R.id.all_goodprice)
    RelativeLayout mAllGoodPrice;
    @BindView(R.id.all_goods)
    LinearLayout mAllGoods;
    @BindView(R.id.pay_status)
    TextView mPayStatus;
    @BindView(R.id.order_time)
    TextView mTime;

    private String user_id, order_id, company, courier;
    private Context mContext;
    private Intent intent;
    private String image_url;
    private String status;
    private String pay_type, type;

    private String flag;
    private IWXAPI iwxapi;
    private String tag;
    private String mConfirm;
    private String mobile;


    @Override
    protected int getLayoutId() {
        return R.layout.order_course_detail_activity;
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("订单详情");
        MobclickAgent.onResume(this);
        orderDetail();
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("订单详情");
        MobclickAgent.onPause(this);
    }

    @Override
    protected void initView() {
        setSubTitle().setText("");
        setToolbarTitle().setText("订单详情");
        mContext = this;
        iwxapi = WXAPIFactory.createWXAPI(mContext, Content.APP_ID);
        iwxapi.registerApp(Content.APP_ID);
        intent = new Intent();
        user_id = (String) SPUtils.getSpValues(SPKeyValuesUtils.SP_USER_ID, SPUtils.TYPE_STRING);
        if (getIntent().getStringExtra("mobile") != null) {
            mobile = getIntent().getStringExtra("mobile");
        }
        if (getIntent().getStringExtra("order_id") != null) {
            order_id = getIntent().getStringExtra("order_id");
        }
        if (getIntent().getStringExtra("tag") != null) {
            tag = getIntent().getStringExtra("tag");
            Log.e("*********tag", tag);
        }
        if (getIntent().getStringExtra("confirm") != null) {
            mConfirm = getIntent().getStringExtra("confirm");
            Log.e("*********confirm", mConfirm);
        }
        if (getIntent().getStringExtra("flag") != null) {
            flag = getIntent().getStringExtra("flag");
        }
        Log.e("*********flag", flag);
        if (getIntent().getStringExtra("status") != null) {
            status = getIntent().getStringExtra("status");
        }


        mCopy.setOnClickListener(this);
        mLogistics.setOnClickListener(this);
        mButton.setOnClickListener(this);
        orderDetail();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.logistics:
              if (content!=null){
                  intent.setClass(MyCourseOrderDetailActivity.this, LogisticsDetailActivity.class);
                  Log.e("OPOPO", courier);
                  intent.putExtra("courier", courier);
                  startActivity(intent);
              }else {
                  ToastUtil.showShortToast("暂无物流信息");
              }
                break;
            default:
                break;
        }

    }

    private String content;

    private void orderDetail() {
        Map<String, Object> map = new HashMap<>();
//        map.put("user_id", 1);
        map.put("user_id", SPUtils.getSpValues(SPKeyValuesUtils.SP_USER_ID, SPUtils.TYPE_STRING));
        map.put("order_id", order_id);
        Log.e("*******map", map.toString());
        GloableConstant.getInstance().showmeidialog(this);
        Xutil.Post(ApiUrl.ORDERDATAIL, map, new MyCallBack<OrderDatailResponse>() {
            @Override
            public void onSuccess(final OrderDatailResponse result) {
                super.onSuccess(result);
                GloableConstant.getInstance().stopProgressDialog();
                Log.e("**********result", result.getStatus_code());
                if (result.getStatus_code().equals("200")) {
                    mOrderNum.setText(result.getData().getOrder_sn());
                    mTime.setText(result.getData().getCreated_at());
                    content=result.getData().getExpress_check_info().getContext();

                    Glide.with(mContext).load(result.getData().getGoods().get(0).getImages()).into(mBookImg);
                    mBookName.setText(result.getData().getGoods().get(0).getGoods_name());
                    mPrice.setText("￥" + result.getData().getGoods().get(0).getPrice());
                    mShopPrice.setText("￥" + result.getData().getGoods().get(0).getMarket_price());
                    mShopPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
                    mNum.setText("x" + result.getData().getGoods().get(0).getGoods_num());

                    type = result.getData().getType();
                    Log.e("*********type", type);
                    Log.e("********getStatus()", result.getData().getStatus());
                    switch (result.getData().getStatus()) {
                        case "1":
                            mButton.setText("去支付");
                            mPayStatus.setText("待付款");
                            mLogistics.setVisibility(View.GONE);
                            mLayout.setVisibility(View.VISIBLE);
                            mButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    orderPay();
                                }
                            });
                            break;
                        case "2":
                            mButton.setText("未发货");
                            mPayStatus.setText("交易成功");
                            if (result.getData().getExpress_check_info().getContext() != null) {
                                mLogisticsStataus.setText(result.getData().getExpress_check_info().getContext());
                                mLogisticsTime.setVisibility(View.VISIBLE);
                                mLogisticsTime.setText(result.getData().getExpress_check_info().getTime());
                            } else {
                                mLogisticsStataus.setText("等待商家发货");
                                mLogisticsTime.setVisibility(View.GONE);
                            }

                            mLayout.setVisibility(View.INVISIBLE);
                            Log.e("*********tag", tag);
                            if (tag.equals("1")) {
                                mLogistics.setVisibility(View.VISIBLE);
                            } else {
                                mLogistics.setVisibility(View.GONE);
                            }
//                            mButton.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    confirm();
//                                }
//                            });
                            break;
                        case "4":
                            if (result.getData().getExpress_check_info().getContext() != null) {
                                mLogisticsStataus.setText(result.getData().getExpress_check_info().getContext());
                                mLogisticsTime.setVisibility(View.VISIBLE);
                                mLogisticsTime.setText(result.getData().getExpress_check_info().getTime());
                            } else {
                                mLogisticsStataus.setText("等待商家发货");
                                mLogisticsTime.setVisibility(View.GONE);
                            }

                            mButton.setText("确认收货");
                            mPayStatus.setText("交易成功");
                            mLayout.setVisibility(View.INVISIBLE);
                            Log.e("*********tag", tag);
                            if (tag.equals("1")) {
                                mLogistics.setVisibility(View.VISIBLE);
                            } else {
                                mLogistics.setVisibility(View.GONE);
                            }
                            mButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    confirm();
                                }
                            });
                            break;
                        case "5":
                            mButton.setText("去评价");
                            mPayStatus.setText("交易成功");
                            mLayout.setVisibility(View.INVISIBLE);
                            Log.e("*********tag", tag);
                            if (tag.equals("1")) {
                                mLogistics.setVisibility(View.VISIBLE);
                            } else {
                                mLogistics.setVisibility(View.GONE);
                            }
                            mButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    intent.setClass(MyCourseOrderDetailActivity.this, MyOrderCommentActivity.class);
                                    intent.putExtra("order_id", order_id);
                                    intent.putExtra("flag", flag);
                                    intent.putExtra("name", result.getData().getGoods().get(0).getGoods_name());
                                    startActivity(intent);
                                }
                            });
                            break;
                        case "6":
                            mPayStatus.setText("交易成功");
                            mButton.setText("已完成");
                            mLayout.setVisibility(View.INVISIBLE);
                            Log.e("*********tag", tag);
                            if (tag.equals("1")) {
                                mLogistics.setVisibility(View.VISIBLE);
                            } else {
                                mLogistics.setVisibility(View.GONE);
                            }
                            mButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    finish();
                                }
                            });
                            break;
                    }
                    if (result.getData().getGive_books().size() > 0) {
                        Log.e("TAG", "sdsdsdsdsd111");
                        textView.setVisibility(View.VISIBLE);
                        mAllGoods.setVisibility(View.VISIBLE);
                        mAllGoodPrice.setVisibility(View.GONE);
                        mName.setText(result.getData().getConsignee());
                        mPhone.setText(result.getData().getMobile());
                        mOrderNum.setText(result.getData().getOrder_sn());
                        mAddress.setText(result.getData().getProvince() + result.getData().getCity() + result.getData().getCounty() +
                                result.getData().getAddress());
                        mView.setVisibility(View.VISIBLE);
                        mDeliveryStyle.setVisibility(View.VISIBLE);
                        mPhoneNum.setVisibility(View.GONE);
                        mRecyclerView.setVisibility(View.VISIBLE);
                        mSelectAddress.setVisibility(View.VISIBLE);
                        mView.setVisibility(View.VISIBLE);
//                        mLogistics.setVisibility(View.VISIBLE);
                        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
                        mRecyclerView.setAdapter(new CommonAdapter<OrderDatailResponse.mGive_Books>(mContext, R.layout.item_give_book_layout, result.getData().getGive_books()) {

                            @Override
                            protected void convert(ViewHolder holder, OrderDatailResponse.mGive_Books mGive_books, int position) {
                                ImageView imageView = (ImageView) holder.getConvertView().findViewById(R.id.give_image);
                                Glide.with(mContext).load(mGive_books.getImages()).apply(GloableConstant.getInstance().getDefaultOptionssmall()).into(imageView);
                                holder.setText(R.id.give_name, mGive_books.getBook_name());
                                holder.setText(R.id.give_price, "￥" + mGive_books.getPrice());
                            }
                        });
                        Log.e("TAG", "sdsdsdsdsd2222");
//                       if (!flag.equals("1")){
//                           mSelectAddress.setVisibility(View.GONE);
//                           mView.setVisibility(View.GONE);
//                           textView.setVisibility(View.GONE);
//                           mRecyclerView.setVisibility(View.GONE);
//                       }else {
                        mSelectAddress.setVisibility(View.VISIBLE);
                        mView.setVisibility(View.VISIBLE);
                        textView.setVisibility(View.VISIBLE);
                        mRecyclerView.setVisibility(View.VISIBLE);
//                       }
                    } else {
                        if (flag.equals("2")) {
                            mSelectAddress.setVisibility(View.VISIBLE);
                            mView.setVisibility(View.VISIBLE);
                            mName.setText(result.getData().getConsignee());
                            if (result.getData().getStatus().equals("1")) {
                                mLogistics.setVisibility(View.GONE);
                            } else {
                                mLogistics.setVisibility(View.VISIBLE);
                            }
                            textView.setVisibility(View.GONE);
                            mPhone.setText(result.getData().getMobile());
                            mAddress.setText(result.getData().getProvince() + result.getData().getCity() + result.getData().getCounty() +
                                    result.getData().getAddress());
                            mPhoneNum.setVisibility(View.GONE);
//                            textView.setVisibility(View.VISIBLE);
                        } else {
                            mAllGoods.setVisibility(View.GONE);
                            mAllGoodPrice.setVisibility(View.VISIBLE);
                            textView.setVisibility(View.GONE);
                            mSelectAddress.setVisibility(View.GONE);
                            mDeliveryStyle.setVisibility(View.GONE);
                            mLogistics.setVisibility(View.GONE);
                            mView.setVisibility(View.GONE);
                            mPhoneNum.setVisibility(View.VISIBLE);
                            mPhone1.setText(result.getData().getMobile());
                        }

                    }
                    if (result.getData().getPay_type().equals("1")) {
                        mMode.setText("支付宝");
                    } else if (result.getData().getPay_type().equals("2")) {
                        mMode.setText("微信");
                    }
                    company = result.getData().getExpress_company();
                    courier = result.getData().getInvoice_no();
                    image_url = result.getData().getGoods().get(0).getImages();
                    pay_type = result.getData().getPay_type();
                    Log.e("*********pay_type", pay_type);
                    mShipping.setText(result.getData().getExpress_company());
                    mCard.setText("-￥" + result.getData().getCoupons_money());
                    mTeacherCoin.setText("-￥" + result.getData().getIntegral());
                    if (result.getData().getRemark().equals("")) {
                        mContent.setText("暂无");
                    } else {
                        mContent.setText(result.getData().getRemark());
                    }
                    mShippingPrice.setText("￥" + result.getData().getShipping_price());
                    mExpressMoney.setText(("运费：￥" + result.getData().getShipping_price()));
                    mAllPrice.setText("￥" + result.getData().getPay_price());
                    mTotalMoney.setText("￥" + result.getData().getPay_price());
                    mShopAllPrice.setText("￥" + result.getData().getGoods().get(0).getPrice());
                    mShopAllPrice1.setText("￥" + result.getData().getGoods().get(0).getPrice());
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
//                Log.e("********ec",ex.getMessage());
            }

            @Override
            public void onFinished() {
                super.onFinished();
                GloableConstant.getInstance().stopProgressDialog();
            }
        });
    }

    /**
     * 确认收货
     */
    private void confirm() {
        Map<String, Object> map = new HashMap();
        map.put("user_id", user_id);
        map.put("order_id", order_id);
        Log.e("********confirmmap", map.toString());
        GloableConstant.getInstance().startProgressDialog(this);
        Xutil.Post(ApiUrl.CONFIRM, map, new MyCallBack<SupportResponse>() {
            @Override
            public void onSuccess(SupportResponse result) {
                super.onSuccess(result);
                Log.e("*****confirmstatus", result.getStatus_code());
                GloableConstant.getInstance().stopProgressDialog1();
                if (result.getStatus_code().equals("204")) {
                    ToastUtil.showShortToast(result.getMessage());
                    orderDetail();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
                GloableConstant.getInstance().stopProgressDialog1();
            }

            @Override
            public void onFinished() {
                super.onFinished();
                GloableConstant.getInstance().stopProgressDialog1();
            }
        });

    }

    // 支付接口
    private void orderPay() {
        Map<String, Object> map = new HashMap<>();
        map.put("user_id", user_id);
        map.put("order_id", order_id);
        map.put("pay_type", pay_type);
        map.put("type", type);
        map.put("az_pay", "1");
        Log.e("*********parmap", map.toString());
        GloableConstant.getInstance().showmeidialog(this);
        Xutil.Post(ApiUrl.PAY, map, new MyCallBack<PayResponse>() {
            @Override
            public void onSuccess(PayResponse result) {
                super.onSuccess(result);
                GloableConstant.getInstance().stopProgressDialog();
                GloableConstant.getInstance().setOrder("2");
                if (result.getStatus_code().equals("200")) {
                    if (pay_type.equals("1")) {
                        final String str = result.getData().getSign();
                        Log.e("*********str", str);
                        Runnable payRunnable = new Runnable() {

                            @Override
                            public void run() {
                                // 构造PayTask 对象
                                PayTask alipay = new PayTask(MyCourseOrderDetailActivity.this);
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
                    }
                    if (pay_type.equals("2")) {
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
                        Toast.makeText(MyCourseOrderDetailActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                        intent.setClass(MyCourseOrderDetailActivity.this, PayResultActivity.class);
                        intent.putExtra("flag", "success");
                        startActivity(intent);
                        finish();
                    } else {
                        // 判断resultStatus 为非"9000"则代表可能支付失败
                        // "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            Toast.makeText(MyCourseOrderDetailActivity.this, "支付结果确认中", Toast.LENGTH_SHORT).show();
                        } else if (TextUtils.equals(resultStatus, "4000")) {
                            intent.setClass(MyCourseOrderDetailActivity.this, PayResultActivity.class);
                            intent.putExtra("flag", "failure");
                            startActivity(intent);
                            finish();
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            Toast.makeText(MyCourseOrderDetailActivity.this, "您还未安装支付宝客户端", Toast.LENGTH_SHORT).show();
                        } else if (TextUtils.equals(resultStatus, "6001")) {
                            Toast.makeText(MyCourseOrderDetailActivity.this, "您取消了支付,请重新支付!", Toast.LENGTH_SHORT).show();
                        }


                    }


                    break;
                }
                default:
                    break;
            }
        }


    };


}
