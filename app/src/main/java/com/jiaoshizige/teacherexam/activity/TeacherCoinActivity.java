package com.jiaoshizige.teacherexam.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.jiaoshizige.teacherexam.model.busmodel.BusRefreshClass;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.umeng.analytics.MobclickAgent;
import com.jiaoshizige.teacherexam.R;
import com.jiaoshizige.teacherexam.adapter.TeacherCoinAdapter;
import com.jiaoshizige.teacherexam.base.BaseActivity;
import com.jiaoshizige.teacherexam.base.GloableConstant;
import com.jiaoshizige.teacherexam.http.ApiUrl;
import com.jiaoshizige.teacherexam.http.MyCallBack;
import com.jiaoshizige.teacherexam.http.Xutil;
import com.jiaoshizige.teacherexam.model.PayResponse;
import com.jiaoshizige.teacherexam.model.TeacherCoinPayResponse;
import com.jiaoshizige.teacherexam.model.TeacherCoinResponse;
import com.jiaoshizige.teacherexam.utils.PayResult;
import com.jiaoshizige.teacherexam.utils.SPKeyValuesUtils;
import com.jiaoshizige.teacherexam.utils.SPUtils;
import com.jiaoshizige.teacherexam.utils.ToastUtil;
import com.jiaoshizige.teacherexam.utils.ToolUtils;
import com.jiaoshizige.teacherexam.wxapi.Content;
import com.jiaoshizige.teacherexam.yy.activity.CoinRecordActivity;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/a1/9 0009.
 * 充值教师币
 */

public class TeacherCoinActivity extends BaseActivity implements View.OnClickListener {


    @BindView(R.id.list_view)
    ListView mListView;
    @BindView(R.id.alipay)
    CheckBox mAlipay;
    @BindView(R.id.weixin)
    CheckBox mWeiXin;
    @BindView(R.id.change_phone_num)
    Button mButton;
    @BindView(R.id.coin)
    TextView coin;
    @BindView(R.id.record)
    TextView mRecord;
    @BindView(R.id.btn_layout_alipay)
    RelativeLayout btn_layout_alipay;
    @BindView(R.id.btn_layout_weixin_pay)
    RelativeLayout btn_layout_weixin_pay;

    private String mMoney, mCoin;
    private int pay_type;
    TeacherCoinAdapter mTeacherCoinAdapter;
    private Context mContext;
    private Intent intent;
    private IWXAPI iwxapi;

    @Override
    protected int getLayoutId() {
        return R.layout.tesacher_coin_activity;
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("充值教师币");
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("充值教师币");
        MobclickAgent.onPause(this);
    }

    @Override
    protected void initView() {
        mContext = this;
        setSubTitle().setText("使用规则");
        setSubTitle().setTextColor(getResources().getColor(R.color.text_color6));
        setToolbarTitle().setText("教师币充值");
        iwxapi = WXAPIFactory.createWXAPI(this, Content.APP_ID);
        iwxapi.registerApp(Content.APP_ID);
        if (getIntent().getStringExtra("teacherCoin") != null) {
            coin.setText(getIntent().getStringExtra("teacherCoin"));
        }
        if (mAlipay.isChecked()) {
            mWeiXin.setChecked(false);
            mAlipay.setChecked(true);
            pay_type = 1;
        }
        if (mWeiXin.isChecked()) {
            mAlipay.setChecked(false);
            mWeiXin.setChecked(true);
            pay_type = 2;
        }

        mTeacherCoinAdapter = new TeacherCoinAdapter(mContext);
        mListView.setAdapter(mTeacherCoinAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mTeacherCoinAdapter.setSelection(position);
                mTeacherCoinAdapter.notifyDataSetChanged();
                TeacherCoinResponse.mData mData = (TeacherCoinResponse.mData) parent.getAdapter().getItem(position);
                mMoney = mData.getPay_price();
                mCoin = mData.getTeacher_coin();

            }
        });
        setSubTitle().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent();
                intent.setClass(TeacherCoinActivity.this, RuleDescriptionActuivity.class);
                intent.putExtra("flag", "teacher");
                startActivity(intent);
            }
        });
        mAlipay.setOnClickListener(this);
        mWeiXin.setOnClickListener(this);
        mButton.setOnClickListener(this);
        mRecord.setOnClickListener(this);
        btn_layout_alipay.setOnClickListener(this);
        btn_layout_weixin_pay.setOnClickListener(this);
        postCoin();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_layout_alipay:
                mWeiXin.setChecked(false);
                mAlipay.setChecked(true);
                pay_type = 1;
                break;
            case R.id.btn_layout_weixin_pay:
                mAlipay.setChecked(false);
                mWeiXin.setChecked(true);
                pay_type = 2;
                break;
            case R.id.alipay:
                mWeiXin.setChecked(false);
                mAlipay.setChecked(true);
                pay_type = 1;
                break;
            case R.id.weixin:
                mAlipay.setChecked(false);
                mWeiXin.setChecked(true);
                pay_type = 2;
                break;
            case R.id.change_phone_num:
                if (pay_type == 1) {
                    paycoin();
                }
                if (pay_type == 2) {
                    if (ToolUtils.isWeChatAppInstalled(mContext)){
                        weiXinPayCoin();
                    }else {
                        ToastUtil.showShortToast("您还未安装微信客户端");
                    }

                }
                break;
            case R.id.record:
                startActivity(new Intent(TeacherCoinActivity.this,CoinRecordActivity.class));
                break;
        }
    }

    /**
     * 支付宝充值
     */
    private void paycoin() {
        Map<String, Object> map = new HashMap<>();
        if (TextUtils.isEmpty(mCoin)) {
            ToastUtil.showShortToast("请选择充值金额");
        }
//        map.put("user_id", 1);
        map.put("user_id", SPUtils.getSpValues(SPKeyValuesUtils.SP_USER_ID, SPUtils.TYPE_STRING));
        map.put("pay_type", pay_type);
        map.put("teacher_coin", mCoin);
        Log.e("********map", map.toString());
        GloableConstant.getInstance().startProgressDialog(this);
        Xutil.Post(ApiUrl.PAYCOIN, map, new MyCallBack<PayResponse>() {
            @Override
            public void onSuccess(PayResponse result) {
                super.onSuccess(result);
                GloableConstant.getInstance().stopProgressDialog();
                GloableConstant.getInstance().setOrder("1");
                Log.e("********result", result.getStatus_code());
                if (result.getStatus_code().equals("200")) {
                    final String str = result.getData().getSign();
                    Log.e("*********str", str);
                    Runnable payRunnable = new Runnable() {

                        @Override
                        public void run() {
                            // 构造PayTask 对象
                            PayTask alipay = new PayTask(TeacherCoinActivity.this);
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

    /**
     * 微信充值
     */
    private void weiXinPayCoin() {
        Map<String, Object> map = new HashMap<>();
        if (TextUtils.isEmpty(mCoin)) {
            ToastUtil.showShortToast("请选择充值金额");
        }
//        map.put("user_id", 1);
        map.put("user_id",SPUtils.getSpValues(SPKeyValuesUtils.SP_USER_ID, SPUtils.TYPE_STRING));
        map.put("pay_type", pay_type);
        map.put("teacher_coin", mCoin);
        Log.e("********map", map.toString());
        GloableConstant.getInstance().startProgressDialog(this);
        Xutil.Post(ApiUrl.PAYCOIN, map, new MyCallBack<TeacherCoinPayResponse>() {
            @Override
            public void onSuccess(TeacherCoinPayResponse result) {
                super.onSuccess(result);
                Log.e("********result",result.getStatus_code());
                GloableConstant.getInstance().stopProgressDialog();
                GloableConstant.getInstance().setOrder("1");
               if (result.getStatus_code().equals("200")){

                   PayReq req = new PayReq();
                   req.appId = result.getData().getAppid();
                   req.prepayId = result.getData().getPrepayid();
                   req.partnerId = result.getData().getPartnerid();
                   req.packageValue = result.getData().getPack();
                   req.nonceStr = result.getData().getNoncestr();
                   req.timeStamp = result.getData().getTimestamp();
                   req.sign = result.getData().getSign();
                   iwxapi.sendReq(req);
               }
            }

            @Override
            public void onFinished() {
                super.onFinished();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
                Log.e("*********ex",ex.getMessage()+"//////////");
            }
        });
    }



    private void postCoin() {
        Map<String, Object> map = new HashMap<>();
        GloableConstant.getInstance().startProgressDialog(this);
        Xutil.Post(ApiUrl.TEACHER_COIN, map, new MyCallBack<TeacherCoinResponse>() {
            @Override
            public void onSuccess(TeacherCoinResponse result) {
                super.onSuccess(result);
                GloableConstant.getInstance().stopProgressDialog();
                if (result.getStatus_code().equals("200") && result.getData().size() > 0) {
                    mTeacherCoinAdapter.setmList(result.getData());
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
                        Toast.makeText(TeacherCoinActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                        intent.setClass(TeacherCoinActivity.this, PayResultActivity.class);
                        intent.putExtra("flag", "success");
                        startActivity(intent);

                        intent.putExtra("icon", mCoin);
                        setResult(RESULT_OK, intent);
                        finish();
                    } else {
                        // 判断resultStatus 为非"9000"则代表可能支付失败
                        // "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            Toast.makeText(TeacherCoinActivity.this, "支付结果确认中", Toast.LENGTH_SHORT).show();
                        } else if (TextUtils.equals(resultStatus, "4000")) {
                            intent.setClass(TeacherCoinActivity.this, PayResultActivity.class);
                            intent.putExtra("flag", "failure");
                            startActivity(intent);
                            finish();
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            Toast.makeText(TeacherCoinActivity.this, "您还未安装支付宝客户端", Toast.LENGTH_SHORT).show();
                        } else if (TextUtils.equals(resultStatus, "6001")) {
                            Toast.makeText(TeacherCoinActivity.this, "您取消了支付,请重新支付!", Toast.LENGTH_SHORT).show();
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
