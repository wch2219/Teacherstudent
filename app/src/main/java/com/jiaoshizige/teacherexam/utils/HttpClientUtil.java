package com.jiaoshizige.teacherexam.utils;

import com.loopj.android.http.AsyncHttpClient;

/**
 * Created by Administrator on 2018/2/22 0022.
 */

public class HttpClientUtil {
    private static AsyncHttpClient client;
    public static AsyncHttpClient getClient(){
        if(client == null){
            client = new AsyncHttpClient();
        }
        client.setTimeout(Config.TIMEOUT);
        return client;
    }
}
