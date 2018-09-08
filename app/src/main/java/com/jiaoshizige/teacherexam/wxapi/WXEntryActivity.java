package com.jiaoshizige.teacherexam.wxapi;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.umeng.socialize.weixin.view.WXCallbackActivity;
import com.jiaoshizige.teacherexam.base.MyApplication;
import com.jiaoshizige.teacherexam.utils.MethodUtils;
import com.jiaoshizige.teacherexam.utils.ToastUtil;


public class WXEntryActivity extends WXCallbackActivity implements IWXAPIEventHandler {

    private static final int RETURN_MSG_TYPE_LOGIN = 1; //登录
    private static final int RETURN_MSG_TYPE_SHARE = 2; //分享
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        ToastUtil.showShortToast("callback oncreate");
        MethodUtils.myInfo("callback oncreate");
        //微信登录回调
        MyApplication.mWxApi.handleIntent(getIntent(), this);

    }

    @Override
    public void onReq(BaseReq req) {
//        finish();
    }

    @Override
    public void onResp(BaseResp resp) {
        int type = resp.getType(); //类型：分享还是登录
        Log.e("**resp.errCode",resp.errCode+"///////");
        switch (resp.errCode) {
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                //用户拒绝授权
                ToastUtil.showShortToast("拒绝授权微信登录");
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                //用户取消
                String message = "";
                if (type == RETURN_MSG_TYPE_LOGIN) {
                    message = "取消了微信登录";
                } else if (type == RETURN_MSG_TYPE_SHARE) {
                    message = "取消了微信分享";
                }
                ToastUtil.showShortToast(message);
                break;
            case BaseResp.ErrCode.ERR_OK:
                if (type==RETURN_MSG_TYPE_LOGIN){
                    SendAuth.Resp sendResp = (SendAuth.Resp) resp;
                    MyApplication.WX_CODE = sendResp.code;
                    MethodUtils.myInfo("code" + sendResp.code);
                    finish();
                }else if (type == RETURN_MSG_TYPE_SHARE) {
                    ToastUtil.showShortToast( "微信分享成功");
                }
                break;
            default:
                Toast.makeText(this, "登录失败", Toast.LENGTH_LONG).show();
                break;
        }

        finish();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        MyApplication.mWxApi.handleIntent(intent, this);
        finish();
    }

}
