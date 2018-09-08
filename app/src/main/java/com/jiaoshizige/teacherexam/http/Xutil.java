package com.jiaoshizige.teacherexam.http;

import com.jiaoshizige.teacherexam.utils.AppLog;
import com.jiaoshizige.teacherexam.utils.SPKeyValuesUtils;
import com.jiaoshizige.teacherexam.utils.SPUtils;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.Map;


public class Xutil {
    /**
     * get
     *
     * @param url
     * @param map
     * @param callback
     * @param <T>
     * @return
     */
    public String token;

    public static <T> Callback.Cancelable GET(String url, Map<String, String> map, Callback.CommonCallback<T> callback) {
        AppLog.instance().d(map.toString());
        RequestParams params = new RequestParams(url);
//        params.setHeader("Content-Type", "application/json");
//        params.setHeader("Authorization","Bearer no");
        params.setHeader("Authorization", "Bearer " + (String) SPUtils.getSpValues(SPKeyValuesUtils.Token, SPUtils.TYPE_STRING));
//        params.setAsJsonContent(true);
        params.setConnectTimeout(1000 * 20);
        if (null != map) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                params.addQueryStringParameter(entry.getKey(), entry.getValue());
            }
        }
        Callback.Cancelable cancelable = x.http().get(params, callback);
        return cancelable;
    }

    /**
     * get
     *
     * @param url
     * @param map
     * @param callback
     * @param <T>
     * @return
     */
    public static <T> Callback.Cancelable GETObj(String url, Map<String, Object> map, Callback.CommonCallback<T> callback) {
        RequestParams params = new RequestParams(url);
        params.setHeader("Content-Type", "application/json");
        params.setAsJsonContent(true);
        params.setConnectTimeout(1000 * 20);
        if (null != map) {
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                params.addParameter(entry.getKey(), entry.getValue());
            }
        }
        Callback.Cancelable cancelable = x.http().get(params, callback);
        return cancelable;
    }

    /**
     * Post
     *
     * @param url
     * @param map
     * @param callback
     * @param <T>
     * @return
     */
    public static <T> Callback.Cancelable Post(String url, Map<String, Object> map, Callback.CommonCallback<T> callback) {
        AppLog.instance().d(map.toString());
        RequestParams params = new RequestParams(url);
        params.setHeader("Content-Type", "application/json");
//        params.setHeader("Content-Type", "   application/x-www-form-urlencoded");

//        params.setAsJsonContent(true);
//        params.setHeader("Authorization", "Bearer no");
//        params.setHeader("Authorization", (String) SPUtils.getSpValues(SPKeyValuesUtils.Token, SPUtils.TYPE_STRING));

        params.setHeader("Authorization", "Bearer " + (String) SPUtils.getSpValues(SPKeyValuesUtils.Token, SPUtils.TYPE_STRING));
        params.setConnectTimeout(1000 * 20);
        if (null != map) {
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                params.addParameter(entry.getKey(), entry.getValue());
            }
        }
        Callback.Cancelable cancelable = x.http().post(params, callback);
        return cancelable;
    }

    public static <T> Callback.Cancelable PostImageFiles(RequestParams params, Callback.CommonCallback<T> callback) {
//        params.setHeader("Content-Type", "application/json");
        params.setHeader("Content-Type", "application/x-www-form-urlencoded");
//        params.setHeader("Authorization","Bearer no");
        params.setHeader("Authorization", "Bearer " + (String) SPUtils.getSpValues(SPKeyValuesUtils.Token, SPUtils.TYPE_STRING));
        params.setConnectTimeout(1000 * 20);
        Callback.Cancelable cancelable = x.http().post(params, callback);
        return cancelable;
    }

    /**
     * 上传文件
     *
     * @param <T>
     */
    public static <T> Callback.Cancelable UpLoadFile(String url, Map<String, Object> map, Callback.CommonCallback<T> callback) {
        RequestParams params = new RequestParams(url);
        if (null != map) {
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                params.addParameter(entry.getKey(), entry.getValue());
            }
        }
        params.setMultipart(true);
        Callback.Cancelable cancelable = x.http().get(params, callback);
        return cancelable;
    }

    /**
     * 下载文件
     *
     * @param <T>
     */
    public static <T> Callback.Cancelable DownLoadFile(String url, String filepath, Callback.CommonCallback<T> callback) {
        RequestParams params = new RequestParams(url);
        //设置断点续传
        params.setAutoResume(true);
        params.setSaveFilePath(filepath);
        Callback.Cancelable cancelable = x.http().get(params, callback);
        return cancelable;
    }

}

