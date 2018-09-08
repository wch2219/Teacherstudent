package com.jiaoshizige.teacherexam.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.bumptech.glide.Glide;
import com.jiaoshizige.teacherexam.model.busmodel.BusRefreshClass;
import com.jwenfeng.library.pulltorefresh.BaseRefreshListener;
import com.jwenfeng.library.pulltorefresh.ViewStatus;
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
import com.jiaoshizige.teacherexam.model.MyOpenClassResponse;
import com.jiaoshizige.teacherexam.model.PayResponse;
import com.jiaoshizige.teacherexam.utils.PayResult;
import com.jiaoshizige.teacherexam.utils.PullToRefreshLayoutRewrite;
import com.jiaoshizige.teacherexam.utils.SPKeyValuesUtils;
import com.jiaoshizige.teacherexam.utils.SPUtils;
import com.jiaoshizige.teacherexam.utils.ToastUtil;
import com.jiaoshizige.teacherexam.utils.ToolUtils;
import com.jiaoshizige.teacherexam.wxapi.Content;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/12/8 0008.
 */

public class MyOpenClassActivity extends BaseActivity {
    @BindView(R.id.recycle_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.pull_refresh)
    PullToRefreshLayoutRewrite mPullToRefresh;

    private String user_id, order_id;
    private Context mContext;
    private String status, pay_type;
    private Intent intent;
    private IWXAPI iwxapi;
    private String mPageNum = "1", mPageSize = "10";
    private int mPage = 1;
    private List<MyOpenClassResponse.mData> mList;

    @Override
    protected int getLayoutId() {
        return R.layout.my_open_class_layout;
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("我的公开课");
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("我的公开课");
        MobclickAgent.onPause(this);
    }

    @Override
    protected void initView() {
        setSubTitle().setText("");
        setToolbarTitle().setText("我的公开课");
        mContext = this;
        mList = new ArrayList<>();
        iwxapi = WXAPIFactory.createWXAPI(mContext, Content.APP_ID);
        iwxapi.registerApp(Content.APP_ID);
        intent = new Intent();
        user_id = (String) SPUtils.getSpValues(SPKeyValuesUtils.SP_USER_ID, SPUtils.TYPE_STRING);
        mPullToRefresh.setRefreshListener(new BaseRefreshListener() {
            @Override
            public void refresh() {
                mPage = 1;
                mPageNum = "1";
                myOpenClass();
            }

            @Override
            public void loadMore() {
                mPage=1;
                mPageNum=String.valueOf(mPage);
                myOpenClass();
            }
        });
        mPullToRefresh.setOnErrorListener(new PullToRefreshLayoutRewrite.onClickCallBackListener() {
            @Override
            public void onCallBackListener(View v) {
                mPage = 1;
                mPageNum = "1";
                myOpenClass();
            }
        });
        myOpenClass();
    }
private String _id;
    private void myOpenClass() {
        Map<String, Object> map = new HashMap<>();
//        map.put("user_id", 1);
        map.put("user_id",  SPUtils.getSpValues(SPKeyValuesUtils.SP_USER_ID, SPUtils.TYPE_STRING));
        GloableConstant.getInstance().startProgressDialog(this);
        Xutil.Post(ApiUrl.MYOPEN, map, new MyCallBack<MyOpenClassResponse>() {
            @Override
            public void onSuccess(final MyOpenClassResponse result) {
                super.onSuccess(result);
                GloableConstant.getInstance().stopProgressDialog();
                mPullToRefresh.finishRefresh();
                mPullToRefresh.finishLoadMore();
                mPullToRefresh.showView(ViewStatus.CONTENT_STATUS);
                if (result.getStatus_code().equals("200")) {
                    if(result.getData() != null && result.getData().size() > 0) {
                        if (mPageNum.equals("1")) {
                            mList.clear();
                            mList = result.getData();
                        } else {
                            if (null == mList) {
                                mList.clear();
                                mList = result.getData();
                            } else {
                                mList.clear();
                                mList.addAll(result.getData());
                            }
                        }
                        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
                        mRecyclerView.setAdapter(new CommonAdapter<MyOpenClassResponse.mData>(mContext, R.layout.item_my_open_class, mList) {

                            @Override
                            protected void convert(ViewHolder holder, final MyOpenClassResponse.mData mData, int position) {
                                ImageView imageView = (ImageView) holder.getConvertView().findViewById(R.id.open_class_image);
                                Glide.with(mContext).load(mData.getImage()).apply(GloableConstant.getInstance().getDefaultOptionssmall()).into(imageView);
                                holder.setText(R.id.open_class_name, mData.getName());
                                holder.setText(R.id.open_class_content, mData.getDescription());
                                status = mData.getStatus();

                                if (status.equals("1")) {
                                    holder.setText(R.id.open_class_, "待付款");

                                    holder.setOnClickListener(R.id.item_open_class, new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            if (mData.getPay_type().equals("1")) {
                                                order_id = mData.getOrder_id();
                                                pay_type = mData.getPay_type();
                                                orderPay();
                                            }
                                        }
                                    });
                                }
                                if (status.equals("6")) {
                                    _id=mData.getOpen_course_id();
                                    holder.setText(R.id.open_class_, "已完成");
                                    holder.setOnClickListener(R.id.item_open_class, new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            intent.setClass(MyOpenClassActivity.this, OpenClassDetailActivity.class);
                                            intent.putExtra("_id", _id);
                                            intent.putExtra("recent", "now");
                                            intent.putExtra("chat_room_id", mData.getLive_info().getChat_room_id());
                                            if (mData.getLive_info().getVideo_url()!=null){
                                                intent.putExtra("live_url", mData.getLive_info().getPull_url());
                                            }else {
                                                intent.putExtra("lb_url", mData.getLive_info().getVideo_url());
                                            }



                                            startActivity(intent);
                                        }
                                    });
                                }

                            }
                        });
                    }else{
                        if(mPage > 1){
                            ToastUtil.showShortToast("没有更多数据了");
                        }else{
                            mPullToRefresh.showView(ViewStatus.EMPTY_STATUS);
                        }
                    }
                }else if(result.getStatus_code().equals("203")){
                    if(mPage > 1){
                        ToastUtil.showShortToast("没有更多数据了");
                    }else{
                        mPullToRefresh.showView(ViewStatus.EMPTY_STATUS);
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
                GloableConstant.getInstance().stopProgressDialog();
                mPullToRefresh.finishRefresh();
                mPullToRefresh.finishLoadMore();
                mPullToRefresh.showView(ViewStatus.EMPTY_STATUS);
            }
        });


    }


    // 支付接口
    private void orderPay() {
        Map<String, Object> map = new HashMap<>();
        map.put("user_id", user_id);
        map.put("order_id", order_id);
        map.put("pay_type", pay_type);
        map.put("type", "4");
        map.put("az_pay", "1");
        Log.e("*********parmap", map.toString());
        GloableConstant.getInstance().startProgressDialog(this);
        Xutil.Post(ApiUrl.PAY, map, new MyCallBack<PayResponse>() {
            @Override
            public void onSuccess(PayResponse result) {
                super.onSuccess(result);
                GloableConstant.getInstance().stopProgressDialog();
                if (result.getStatus_code().equals("200")) {
                    if (pay_type.equals("1")) {
                        final String str = result.getData().getSign();
                        Log.e("*********str", str);
                        Runnable payRunnable = new Runnable() {

                            @Override
                            public void run() {
                                // 构造PayTask 对象
                                PayTask alipay = new PayTask(MyOpenClassActivity.this);
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
                      if (ToolUtils.isWeChatAppInstalled(mContext)){
                          PayReq req = new PayReq();
                          req.prepayId = result.getData().getPrepayid();
                          req.partnerId = result.getData().getPartnerid();
                          req.packageValue = result.getData().getPack();
                          req.appId = result.getData().getAppid();
                          req.nonceStr = result.getData().getNoncestr();
                          req.timeStamp = result.getData().getTimestamp();
                          req.sign = result.getData().getSign();
                          iwxapi.sendReq(req);
                      }else {
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
                        Toast.makeText(MyOpenClassActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                        intent.setClass(MyOpenClassActivity.this, PayResultActivity.class);
                        intent.putExtra("flag", "success");
                        startActivity(intent);
                        finish();
                    } else {
                        // 判断resultStatus 为非"9000"则代表可能支付失败
                        // "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            Toast.makeText(MyOpenClassActivity.this, "支付结果确认中", Toast.LENGTH_SHORT).show();
                        } else if (TextUtils.equals(resultStatus, "4000")) {
                            intent.setClass(MyOpenClassActivity.this, PayResultActivity.class);
                            intent.putExtra("flag", "failure");
                            startActivity(intent);
                            finish();
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            Toast.makeText(MyOpenClassActivity.this, "您还未安装支付宝客户端", Toast.LENGTH_SHORT).show();
                        } else if (TextUtils.equals(resultStatus, "6001")) {
                            Toast.makeText(MyOpenClassActivity.this, "您取消了支付,请重新支付!", Toast.LENGTH_SHORT).show();
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
