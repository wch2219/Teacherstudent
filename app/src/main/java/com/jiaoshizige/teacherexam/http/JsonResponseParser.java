package com.jiaoshizige.teacherexam.http;



import com.jiaoshizige.teacherexam.model.busmodel.BusIsLogin;
import com.jiaoshizige.teacherexam.utils.GsonUtils;

import org.greenrobot.eventbus.EventBus;
import org.xutils.http.app.ResponseParser;
import org.xutils.http.request.UriRequest;

import java.lang.reflect.Type;


public class JsonResponseParser implements ResponseParser {
    @Override
    public void checkResponse(UriRequest request) throws Throwable {

    }

    @Override
    public Object parse(Type resultType, Class<?> resultClass, String result) throws Throwable {
//        SupportResponse parser = GsonUtils.INSTANCE.parseToBean(result, SupportResponse.class);
//        if(parser != null){
//            String status_code = parser.getStatus_code();
//            if (ApiStatusCode.SUCCESS.equals(status_code) || ApiStatusCode.NET_SUCCESS.equals(status_code)) {
//                return GsonUtils.INSTANCE.parseToBean(result, resultClass);
//            } else {
////                ToastUtil.showShortToast(parser.getMessage());
//            }
//        }
//        return null;
//        SupportResponse parser = GsonUtils.INSTANCE.parseToBean(result, SupportResponse.class);
//        if(parser != null){
//            String status_code = parser.getStatus_code();
//            if (ApiStatusCode.SUCCESS.equals(status_code) || ApiStatusCode.NET_SUCCESS.equals(status_code)) {
                return GsonUtils.INSTANCE.parseToBean(result, resultClass);
//            } else {
////                ToastUtil.showShortToast(parser.getMessage());
//            }
//        }
//        return null;

//        SupportResponse parser = GsonUtils.INSTANCE.parseToBean(result, SupportResponse.class);
//        if(null != parser){
//            String status_code = parser.getStatus_code();
//            if(ApiStatusCode.TOKEN_ERROR.equals(status_code)){
//                EventBus.getDefault().post(new BusIsLogin(false));
//            }
//        }
//        return GsonUtils.INSTANCE.parseToBean(result, resultClass);

//        System.out.print("map"+ new Gson().fromJson(result,resultClass).toString());
//      return new Gson().fromJson(result,resultClass);
    }
}
