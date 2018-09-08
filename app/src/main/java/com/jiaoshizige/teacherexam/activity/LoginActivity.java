package com.jiaoshizige.teacherexam.activity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.umeng.analytics.MobclickAgent;
import com.jiaoshizige.teacherexam.MainActivity;
import com.jiaoshizige.teacherexam.R;
import com.jiaoshizige.teacherexam.adapter.TabPagerAdapter;
import com.jiaoshizige.teacherexam.base.GloableConstant;
import com.jiaoshizige.teacherexam.base.MyApplication;
import com.jiaoshizige.teacherexam.fragment.AccountNumberFragment;
import com.jiaoshizige.teacherexam.fragment.PhoneShortCutFragment;
import com.jiaoshizige.teacherexam.http.ApiUrl;
import com.jiaoshizige.teacherexam.http.MyCallBack;
import com.jiaoshizige.teacherexam.http.Xutil;
import com.jiaoshizige.teacherexam.model.AccountNumberResponse;
import com.jiaoshizige.teacherexam.utils.AppManager;
import com.jiaoshizige.teacherexam.utils.MethodUtils;
import com.jiaoshizige.teacherexam.utils.SPKeyValuesUtils;
import com.jiaoshizige.teacherexam.utils.SPUtils;
import com.jiaoshizige.teacherexam.utils.ToastUtil;
import com.jiaoshizige.teacherexam.utils.ToolUtils;
import com.jiaoshizige.teacherexam.wxapi.Content;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/7/27 0027.
 */

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.tab_trends_title)
    TabLayout mTableLayout;
    @BindView(R.id.vp_trends)
    ViewPager mViewPager;
    //    @BindView(R.id.register)
//    TextView mRegieter;
    @BindView(R.id.share_weibo)
    LinearLayout mSina;
    @BindView(R.id.share_qq)
    LinearLayout mQQ;
    @BindView(R.id.share_weixin)
    LinearLayout mWeiXin;

    private TabPagerAdapter mTabPagerAdapter;
    private List<String> tabTitles;
    private List<Fragment> fragments;
    private int number = 0;
    private Intent intent;
    private String flag;
    public static IWXAPI iwxapi;
    private String unionid, openid, nickname, headimgurl;

    @Override
    protected void onStart() {
        super.onStart();
        ToolUtils.setIndicator(mTableLayout, 40, 40);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mWeiXin.setClickable(true);
        MethodUtils.myInfo("onResume+code:" + MyApplication.WX_CODE);
        loadUserInfoAndLogin();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        ButterKnife.bind(this);
        //注册微信
        AppManager appManager = AppManager.getInstance();
        appManager.addActivity(this);
        iwxapi = WXAPIFactory.createWXAPI(this, Content.APP_ID, true);
        iwxapi.registerApp(Content.APP_ID);
//        mRegieter.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                intent = new Intent(LoginActivity.this, RegresterA.class);
//                intent.putExtra("flag", "1");
//                startActivity(intent);
//            }
//        });


        fragments = new ArrayList<>();
        fragments.add(new AccountNumberFragment());
        fragments.add(new PhoneShortCutFragment());
        tabTitles = new ArrayList<>();
        tabTitles.add("账号密码登录");
        tabTitles.add("手机快捷登录");


        for (int i = 0; i < tabTitles.size(); i++) {
            mTableLayout.addTab(mTableLayout.newTab().setText(tabTitles.get(i)));
        }
        //设置tablayout模式
        mTableLayout.setTabMode(TabLayout.MODE_FIXED);
        mTabPagerAdapter = new TabPagerAdapter(this.getSupportFragmentManager(), fragments, tabTitles);
        mViewPager.setAdapter(mTabPagerAdapter);
        mViewPager.setCurrentItem(number);
        mTableLayout.setupWithViewPager(mViewPager);
        mSina.setOnClickListener(this);
        mQQ.setOnClickListener(this);
        mWeiXin.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.share_weibo:
//                startActivity(new Intent(LoginActivity.this, BindingPhoneActivity.class));
//                break;
//            case R.id.share_qq:
//                break;
            case R.id.share_weixin:
                if (!MyApplication.mWxApi.isWXAppInstalled()) {
                    //未安装微信
                    ToastUtil.showShortToast("您还未安装微信客户端");
                } else if (!MyApplication.mWxApi.isWXAppSupportAPI()) {
                    //未安装支持微信登录的微信客户端 需要跟新
                    ToastUtil.showShortToast("请先更新微信");
                } else {
                    //微信登录
                    SendAuth.Req req = new SendAuth.Req();
                    req.scope = "snsapi_userinfo";
                    req.state = "diandi_wx_login";
                    //向微信发送请求
                    MyApplication.mWxApi.sendReq(req);
                    ToastUtil.showShortToast("调起登录");
                }
                break;
        }
    }

    /**
     * 获取用户信息并登录
     */
    private void loadUserInfoAndLogin() {
//        String accessTokenUrl = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + MainConstant.WX.WEIXIN_APP_ID + "&secret=" + MainConstant.WX.WEIXIN_APP_SERECET + "&code=" + WX_CODE + "&grant_type=authorization_code";
        String accessTokenUrl = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + Content.APP_ID.trim() + "&secret=" + Content.APP_SERECET.trim() + "&code=" + MyApplication.WX_CODE + "&grant_type=authorization_code";
        Log.e("*********accessTokenUrl", accessTokenUrl);
        //1.通过code获取access_token
        Map<String, String> map = new HashMap<>();
        Xutil.GET(accessTokenUrl, map, new MyCallBack<String>() {
            @Override
            public void onSuccess(String result) {
                super.onSuccess(result);
                // TODO: 2017/4/20 0020
                try {
                    JSONObject jsonObject;
                    jsonObject = new JSONObject(result);
                    String accessToken = jsonObject.getString("access_token");
                    String openId = jsonObject.getString("openid");
                    Log.e("**********", accessToken + "***********" + openId);
                    if (TextUtils.isEmpty(accessToken) || TextUtils.isEmpty(openId)) {
                        //获取access_token失败
                        ToastUtil.showShortToast(R.string.NetworkCannotConnect);
                    } else {
                        //获取access_token成功
                        String userUrl = "https://api.weixin.qq.com/sns/userinfo?access_token=" + accessToken + "&openid=" + openId;
                        Map<String, Object> map1 = new HashMap<String, Object>();
                        Xutil.Post(userUrl, map1, new MyCallBack<String>() {
                            @Override
                            public void onSuccess(String result) {
                                super.onSuccess(result);
                                try {
                                    JSONObject jsonObject = new JSONObject(result);
                                    unionid = jsonObject.getString("unionid");
                                    openid = jsonObject.getString("openid");
                                    nickname = jsonObject.getString("nickname");
                                    headimgurl = jsonObject.getString("headimgurl");
                                    if (TextUtils.isEmpty(unionid) || TextUtils.isEmpty(openid)) {
                                        //获取用户信息失败
                                        ToastUtil.showShortToast(R.string.NetworkCannotConnect);
                                    } else {
                                        //TODO: 2017/8/26登录与服务器对接
//                                        loginByWeChat(unionid, openid, nickname, headimgurl);
                                        thirdLogin();

                                        Log.e("*********weixindenglu", unionid + "++++++" + openid + "+++++" + nickname + "+++++" + headimgurl);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Log.e("***********ee", e.getMessage());
                                }

                            }

                            @Override
                            public void onError(Throwable ex, boolean isOnCallback) {
                                super.onError(ex, isOnCallback);
                            }
                        });

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
            }
        });
    }


    /**
     * 微信登录
     */
    private void thirdLogin() {

        String systemtype = Build.MODEL;//手机型号
        if (systemtype == null) {
            systemtype = "";
        }
        String systemversion = String.valueOf(android.os.Build.VERSION.RELEASE);//系统版本号
        if (systemversion == null) {
            systemversion = "";
        }

        PackageManager packageManager = getPackageManager();
        PackageInfo packInfo = null;
        try {
            packInfo = packageManager.getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String appversion = packInfo.versionName;
        if (appversion == null) {
            appversion = "";
        }

        Map<String, Object> map = new HashMap<>();
        map.put("open_id", openid);
        map.put("unionid", unionid);
        map.put("service", "1");
        map.put("ops", "安卓");
        map.put("systemtype", systemtype);
        map.put("systemversion", systemversion);
        map.put("appversion", appversion);
        Log.e("***********map", map.toString());
        GloableConstant.getInstance().showmeidialog(this);
        Xutil.Post(ApiUrl.THIRDLOGIN, map, new MyCallBack<AccountNumberResponse>() {
            @Override
            public void onSuccess(AccountNumberResponse result) {
                super.onSuccess(result);
                GloableConstant.getInstance().stopProgressDialog();
                Log.e("***********result", result.getStatus_code());

                if (result.getStatus_code().equals("204")) {
                    intent = new Intent();
                    intent.setClass(LoginActivity.this, PhoneBindingActivity.class);
                    intent.putExtra("unionid", unionid);
                    intent.putExtra("openid", openid);
                    intent.putExtra("nickname", nickname);
                    intent.putExtra("headimgurl", headimgurl);
                    startActivity(intent);

                }
                if (result.getStatus_code().equals("200")) {
                    ToastUtil.showShortToast(result.getMessage());
                    SPUtils.setSPValues(SPUtils.TYPE_STRING, SPKeyValuesUtils.SP_USER_ID, result.getData().getUser_id());
                    SPUtils.setSPValues(SPUtils.TYPE_STRING, SPKeyValuesUtils.Token, result.getData().getToken());
                    Log.e("********userid", (String) SPUtils.getSpValues(SPKeyValuesUtils.SP_USER_ID, SPUtils.TYPE_STRING));
                    intent = new Intent();
                    intent.setClass(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                }
                ToastUtil.showShortToast(result.getMessage());
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
                ToastUtil.showShortToast(ex.getMessage());

//                Log.e("********ex",ex.getMessage());
            }
        });

    }
}


