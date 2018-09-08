package com.jiaoshizige.teacherexam.http;

import android.text.TextUtils;

import com.jiaoshizige.teacherexam.base.GloableConstant;
import com.jiaoshizige.teacherexam.model.busmodel.BusIsLogin;
import com.jiaoshizige.teacherexam.utils.AppLog;
import com.jiaoshizige.teacherexam.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.xutils.common.Callback;


public class MyCallBack<SupportRespnse> implements Callback.CommonCallback<SupportRespnse> {
    @Override
    public void onSuccess(SupportRespnse result) {
        if(result == null){
            return;
        }

    }

    @Override
    public void onError(Throwable ex, boolean isOnCallback) {
        String errorStr = ex.toString();
        AppLog.instance().d("onError"+errorStr);
        if(!TextUtils.isEmpty(errorStr)){
            if(errorStr.contains("errorCode: 401")){
                EventBus.getDefault().post(new BusIsLogin(false));
            }else if(errorStr.contains("failed")){
                ToastUtil.showShortToast("请确认网络已连接");
            }
        }
    }

    @Override
    public void onCancelled(CancelledException cex) {

    }

    @Override
    public void onFinished() {
        GloableConstant.getInstance().stopProgressDialog();
        GloableConstant.getInstance().stopProgressDialog1();
    }
}
