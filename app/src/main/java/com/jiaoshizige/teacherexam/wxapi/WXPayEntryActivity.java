package com.jiaoshizige.teacherexam.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jiaoshizige.teacherexam.model.busmodel.BusRefreshClass;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.jiaoshizige.teacherexam.MainActivity;
import com.jiaoshizige.teacherexam.activity.PayResultActivity;
import com.jiaoshizige.teacherexam.activity.TeacherCoinActivity;
import com.jiaoshizige.teacherexam.utils.AppManager;
import com.jiaoshizige.teacherexam.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {
    private static final String TAG = "WXPayEntryActivity";
    TextView title, tip;
    ImageView jump;
    private IWXAPI api;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout);
        Toast.makeText(WXPayEntryActivity.this, "oncreate", Toast.LENGTH_SHORT);
        api = WXAPIFactory.createWXAPI(this, Content.APP_ID);
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
        Toast.makeText(WXPayEntryActivity.this, "BaseReq" + req.toString(), Toast.LENGTH_SHORT);

    }

    @Override
    public void onResp(BaseResp resp) {
        Log.e("-----------", "onPayFinish, errCode = " + resp);
        Toast.makeText(WXPayEntryActivity.this, "onPayFinish" + resp.errCode, Toast.LENGTH_SHORT);
        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            int code = resp.errCode;
            String msg = "";
            switch (code) {
                case 0:
                    msg = "支付成功！";

                    break;
                case -1:
                    msg = "支付失败！";
                    break;
                case -2:
                    msg = "您取消了支付！";
                    break;
                default:
                    msg = "支付失败！";
                    break;
            }
            Log.e("----------", code + msg);

            handler.sendEmptyMessage(code);

//            tip.setText(msg);
//            AlertDialog.Builder builder = new AlertDialog.Builder(this);
//            builder.setTitle(R.string.app_tip);
//            builder.setMessage(getString(R.string.pay_result_callback_msg,msg));
////            builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
////                @Override
////                public void onClick(DialogInterface dialog, int which) {
////                    dialog.dismiss();
//
////                    WXPayEntryActivity.this.finish();
////
////
////                }
////            });
//
//            builder.show();
        }
    }

    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Log.e("*********msg", msg.what + "///////");
            switch (msg.what) {
                case 0:
//                    AppManager appManager = AppManager.getInstance();
//                    appManager.killActivity(TeacherCoinActivity.class);
                    //跳转至支付界面
//                    startActivity(new Intent(WXPayEntryActivity.this, PayResoultActivity.class));
                    intent = new Intent();
                    intent.setClass(WXPayEntryActivity.this, PayResultActivity.class);
                    intent.putExtra("flag", "success");
                    startActivity(intent);
                    WXPayEntryActivity.this.finish();
                    EventBus.getDefault().post(new BusRefreshClass());
                    finish();
                    ToastUtil.showShortToast("支付成功");
                    break;
                case -1:
                    //支付失败
                    intent = new Intent();
                    intent.setClass(WXPayEntryActivity.this, PayResultActivity.class);
                    intent.putExtra("flag", "failure");
                    startActivity(intent);
                    ToastUtil.showShortToast("支付失败,请重新支付");
                    finish();
                    break;
                case -2:
                    intent = new Intent();
                    intent.setClass(WXPayEntryActivity.this, PayResultActivity.class);
                    intent.putExtra("flag", "failure");
                    startActivity(intent);
                    ToastUtil.showShortToast("支付失败,请重新支付");
                    finish();
                    //失败了
                    break;

            }

        }
    };
}
