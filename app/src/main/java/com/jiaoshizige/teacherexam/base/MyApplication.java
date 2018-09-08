package com.jiaoshizige.teacherexam.base;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.multidex.MultiDexApplication;
import android.text.TextUtils;
import android.util.Log;

import com.alivc.player.AliVcMediaPlayer;
import com.easefun.polyvsdk.PolyvDevMountInfo;
import com.easefun.polyvsdk.PolyvDownloaderManager;
import com.easefun.polyvsdk.PolyvSDKClient;
import com.jiaoshizige.teacherexam.R;
import com.jiaoshizige.teacherexam.utils.AppLog;
import com.jiaoshizige.teacherexam.utils.SPKeyValuesUtils;
import com.jiaoshizige.teacherexam.utils.ToastUtil;
import com.jiaoshizige.teacherexam.widgets.LiveKit;
import com.jiaoshizige.teacherexam.wxapi.Content;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;

import org.xutils.x;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by Administrator on 2017/7/31.
 */

public class MyApplication extends MultiDexApplication {
    public static SharedPreferences sp;
    private static Context context;
    public static IWXAPI mWxApi;
    public static String WX_CODE = "";
    //加密秘钥和加密向量，在后台->设置->API接口中获取，用于解密SDK加密串
    //值修改请参考https://github.com/easefun/polyv-android-sdk-demo/wiki/10.%E5%85%B3%E4%BA%8E-SDK%E5%8A%A0%E5%AF%86%E4%B8%B2-%E4%B8%8E-%E7%94%A8%E6%88%B7%E9%85%8D%E7%BD%AE%E4%BF%A1%E6%81%AF%E5%8A%A0%E5%AF%86%E4%BC%A0%E8%BE%93
//    /** 加密秘钥 */
//    private String aeskey = "VXtlHmwfS2oYm0CZ";
//    /** 加密向量 */
//    private String iv = "2u9gDPKdX6GyQJKU";
    /**
     * 加密秘钥
     */
    private String aeskey = "VXtlHmwfS2oYm0CZ";
    /**
     * 加密向量
     */
    private String iv = "2u9gDPKdX6GyQJKU";

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        registerToWX();
        ToastUtil.init(this);
        x.Ext.init(this);
        x.Ext.setDebug(true);
        UMShareAPI.get(this);//初始化sdk
        //开启debug模式，方便定位错误，具体错误检查方式可以查看http://dev.umeng.com/social/android/quick-integration的报错必看，正式发布，请关闭该模式
        Config.DEBUG = false;
        UMConfigure.init(context, UMConfigure.DEVICE_TYPE_PHONE, null);
        UMConfigure.setLogEnabled(true);
        //        PlatformConfig.setWeixin("wxebf76684ff0ea84a", "450f30421dcd7937d9d11b1afd441424");
        PlatformConfig.setWeixin(getResources().getString(R.string.weichat_app_id), getResources().getString(R.string.weichat_app_secret));
        //新浪微博(第三个参数为回调地址)
//        PlatformConfig.setSinaWeibo("2927824963", "bd664447b67d4a8aa69971420737a78c","http://zgclass.com");
        PlatformConfig.setSinaWeibo(getResources().getString(R.string.sina_app_key), getResources().getString(R.string.sina_app_secret), "http://zgclass.com");
        //QQ
        PlatformConfig.setQQZone(getResources().getString(R.string.qq_app_id), getResources().getString(R.string.qq_app_key));
        AppLog.instance().d("ceshi6666");
        GloableConstant.getInstance().init(this);
        sp = getSharedPreferences(SPKeyValuesUtils.SP_NAME, Context.MODE_PRIVATE);


        JPushInterface.setDebugMode(false);//正式版的时候设置false，关闭调试
        JPushInterface.init(this);
        //建议添加tag标签，发送消息的之后就可以指定tag标签来发送了
        Set<String> set = new HashSet<>();
        set.add("andfixdemo");//名字任意，可多添加几个
        JPushInterface.setTags(this, set, null);//设置标签
        ///////////// 阿里
        AliVcMediaPlayer.init(context, "");
        LiveKit.init(context, "tdrvipkstq975");//vnroth0kv4ufo自己的
//        LiveKit.init(context,"pkfcgjstpoyh8");//vnroth0kv4ufo自己的tdrvipkstq975
        initPolyvCilent();
        ////////////
        Resources res = super.getResources();
        Configuration config = new Configuration();
        config.setToDefaults();
        res.updateConfiguration(config, res.getDisplayMetrics());

    }

    public static Context getContext() {
        return context;
    }

    public static SharedPreferences getSp() {
        return sp;
    }

    private void registerToWX() {
        //第二个参数是指你应用在微信开放平台上的AppID
        mWxApi = WXAPIFactory.createWXAPI(this, Content.APP_ID, true);
        // 将该app注册到微信
        mWxApi.registerApp(Content.APP_ID);
    }
    //    //各个平台的配置
//    {
//        //微信
////        PlatformConfig.setWeixin("wxebf76684ff0ea84a", "450f30421dcd7937d9d11b1afd441424");
//        PlatformConfig.setWeixin(getResources().getString(R.string.weichat_app_id), getResources().getString(R.string.weichat_app_secret));
//        //新浪微博(第三个参数为回调地址)
////        PlatformConfig.setSinaWeibo("2927824963", "bd664447b67d4a8aa69971420737a78c","http://zgclass.com");
//        PlatformConfig.setSinaWeibo(getResources().getString(R.string.sina_app_key), getResources().getString(R.string.sina_app_secret),"http://zgclass.com");
//        //QQ
//        PlatformConfig.setQQZone(getResources().getString(R.string.qq_app_id), getResources().getString(R.string.qq_app_key));
//    }
    public void initPolyvCilent() {
        //网络方式取得SDK加密串，（推荐）
        //网络获取到的SDK加密串可以保存在本地SharedPreference中，下次先从本地获取
//		new LoadConfigTask().execute();
        PolyvSDKClient client = PolyvSDKClient.getInstance();
        //使用SDK加密串来配置
        String config = "o9Yr9r/ThWYmezDZktyg4hdccnE5rtHbAVtzvjJJ+EyIuNszyzSq7ssNDLSU2YpV8A8fJmwFskwd9He2iyaT79M8903wSrBC9XlumWhHdS7RVrgj6na56a47qGZ7b9w86Csxqs5oMXgiCpi3/EAO3g==";//后台发的
        client.setConfig(config, aeskey, iv, getApplicationContext());
//        client.setConfig("CMWht3MlpVkgpFzrLNAebYi4RdQDY/Nhvk3Kc+qWcck6chwHYKfl9o2aOVBvXVTRZD/14XFzVP7U5un43caq1FXwl0cYmTfimjTmNUYa1sZC1pkHE8gEsRpwpweQtEIiTGVEWrYVNo4/o5jI2/efzA==", aeskey, iv, getApplicationContext());
        //初始化SDK设置
        client.initSetting(context);
        //启动Bugly
        client.initCrashReport(context);
        //启动Bugly后，在学员登录时设置学员id
//		client.crashReportSetUserId(userId);
        //获取SD卡信息
        PolyvDevMountInfo.getInstance().init(this, new PolyvDevMountInfo.OnLoadCallback() {

            @Override
            public void callback() {
                //是否有可移除的存储介质（例如 SD 卡）或内部（不可移除）存储可供使用。
                if (!PolyvDevMountInfo.getInstance().isSDCardAvaiable()) {
                    // TODO 没有可用的存储设备,后续不能使用视频缓存功能
                    Log.e("TAG", "没有可用的存储设备,后续不能使用视频缓存功能");
                    return;
                }

                //可移除的存储介质（例如 SD 卡），需要写入特定目录/storage/sdcard1/Android/data/包名/。
                String externalSDCardPath = PolyvDevMountInfo.getInstance().getExternalSDCardPath();
                if (!TextUtils.isEmpty(externalSDCardPath)) {
                    StringBuilder dirPath = new StringBuilder();
                    dirPath.append(externalSDCardPath).append(File.separator).append("Android").append(File.separator).append("data")
                            .append(File.separator).append(getPackageName()).append(File.separator).append("zgwxdownload");
                    File saveDir = new File(dirPath.toString());
                    if (!saveDir.exists()) {
                        getExternalFilesDir(null); // 生成包名目录
                        saveDir.mkdirs();//创建下载目录
                    }
                    //设置下载存储目录
                    PolyvSDKClient.getInstance().setDownloadDir(saveDir);
                    return;
                }

                //如果没有可移除的存储介质（例如 SD 卡），那么一定有内部（不可移除）存储介质可用，都不可用的情况在前面判断过了。
                File saveDir = new File(PolyvDevMountInfo.getInstance().getInternalSDCardPath() + File.separator + "zgwxdownload");
                if (!saveDir.exists()) {
                    saveDir.mkdirs();//创建下载目录
                }

                //设置下载存储目录
                PolyvSDKClient.getInstance().setDownloadDir(saveDir);
            }
        });

        // 设置下载队列总数，多少个视频能同时下载。(默认是1，设置负数和0是没有限制)
        PolyvDownloaderManager.setDownloadQueueCount(1);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        if (newConfig.fontScale != 1)
            getResources();
        super.onConfigurationChanged(newConfig);
    }

//    @Override
//    public Resources getResources() {
//        Resources res = super.getResources();
//        if (res.getConfiguration().fontScale != 1) {
//            Configuration newConfig = new Configuration();
//            newConfig.setToDefaults();
//            res.updateConfiguration(newConfig, res.getDisplayMetrics());
//        }
//        return res;
//    }

}
