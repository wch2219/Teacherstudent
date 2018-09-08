package com.jiaoshizige.teacherexam.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.umeng.analytics.MobclickAgent;
import com.jiaoshizige.teacherexam.R;
import com.jiaoshizige.teacherexam.base.BaseActivity;
import com.jiaoshizige.teacherexam.base.GloableConstant;
import com.jiaoshizige.teacherexam.base.MyApplication;
import com.jiaoshizige.teacherexam.http.ApiUrl;
import com.jiaoshizige.teacherexam.http.MyCallBack;
import com.jiaoshizige.teacherexam.http.SupportResponse;
import com.jiaoshizige.teacherexam.http.Xutil;
import com.jiaoshizige.teacherexam.model.BindResponse;
import com.jiaoshizige.teacherexam.utils.MethodUtils;
import com.jiaoshizige.teacherexam.utils.SPKeyValuesUtils;
import com.jiaoshizige.teacherexam.utils.SPUtils;
import com.jiaoshizige.teacherexam.utils.ToastUtil;
import com.jiaoshizige.teacherexam.wxapi.Content;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/a1/3 0003.
 * 我的账号
 */

public class MyAcccountActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.exit)
    Button exit;
    @BindView(R.id.account_security)
    LinearLayout safety;
    @BindView(R.id.binding)
    TextView mBinding;
    private Context mContext;
    private View view;
    private PopupWindow pop;
    private TextView dialog_content, exit_no, exit_sure;
    private Intent intent;
    private String user_id;
    private IWXAPI iwxapi;
    private String  unionid,openid,nickname,headimgurl;
    private String isBind;

    @Override
    protected int getLayoutId() {
        return R.layout.my_account_number_layout;
    }

    @Override
    protected void onResume() {
        super.onResume();
        MethodUtils.myInfo("onResume+code:" + MyApplication.WX_CODE);
        loadUserInfoAndLogin();
        MobclickAgent.onPageStart("个人信息");
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("个人信息");
        MobclickAgent.onPause(this);
    }

    @Override
    protected void initView() {
        mContext = this;
        setSubTitle().setText("");
        setToolbarTitle().setText("个人信息");
        iwxapi= WXAPIFactory.createWXAPI(mContext, Content.APP_ID);
        iwxapi.registerApp(Content.APP_ID);
        user_id = (String) SPUtils.getSpValues(SPKeyValuesUtils.SP_USER_ID, SPUtils.TYPE_STRING);
        exit.setOnClickListener(this);
        safety.setOnClickListener(this);
        mBinding.setOnClickListener(this);
        bind();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.exit:
                exitPopWindow();
                break;
            case R.id.binding:
             if (isBind.equals("1")){
                 unBinding();
             }else {
                 if (!MyApplication.mWxApi.isWXAppInstalled()) {
                     //未安装微信
                     ToastUtil.showShortToast("您还未安装微信客户端");
                 } else if (!MyApplication.mWxApi.isWXAppSupportAPI()) {
                     //未安装支持微信登录的微信客户端 需要跟新
                     ToastUtil.showShortToast("请先更新微信");
                 } else {
                     SendAuth.Req req = new SendAuth.Req();
                     req.scope = "snsapi_userinfo";
                     req.state = "diandi_wx_login";
                     //向微信发送请求
                     MyApplication.mWxApi.sendReq(req);

                 }
             }
                break;
            case R.id.exit_no:
                if (pop.isShowing()) {
                    pop.dismiss();
                }
                break;
            case R.id.exit_sure:
                if (pop.isShowing()) {
                    pop.dismiss();
                }
                exitLogin();

                break;
            case R.id.account_security:
                intent = new Intent(MyAcccountActivity.this, AccountSecurityActivity.class);
                startActivity(intent);
                break;
        }

    }

    /**
     * 获取用户信息并登录
     */
    private void loadUserInfoAndLogin() {
//        String accessTokenUrl = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + MainConstant.WX.WEIXIN_APP_ID + "&secret=" + MainConstant.WX.WEIXIN_APP_SERECET + "&code=" + WX_CODE + "&grant_type=authorization_code";
        String accessTokenUrl="https://api.weixin.qq.com/sns/oauth2/access_token?appid="+ Content.APP_ID.trim()+"&secret="+Content.APP_SERECET.trim()+"&code="+MyApplication.WX_CODE+"&grant_type=authorization_code" ;
        Log.e("*********accessTokenUrl",accessTokenUrl);
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
                    Log.e("**********",accessToken+"***********"+openId);
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
                                        bindWeiXin();

                                        Log.e("*********weixindenglu", unionid + "++++++" + openid + "+++++" + nickname + "+++++" + headimgurl);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Log.e("***********ee",e.getMessage());
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
     * 解绑
     */
    private void unBinding(){
        Map<String,Object> map=new HashMap<>();
        map.put("user_id",user_id);
        GloableConstant.getInstance().startProgressDialog(this);
        Xutil.Post(ApiUrl.UNBINDING,map,new MyCallBack<SupportResponse>(){
            @Override
            public void onSuccess(SupportResponse result) {
                super.onSuccess(result);
                GloableConstant.getInstance().stopProgressDialog();
                if (result.getStatus_code().equals("204")){
                    mBinding.setText("未绑定");
                    isBind="0";
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
                GloableConstant.getInstance().stopProgressDialog();
            }

            @Override
            public void onFinished() {
                super.onFinished();
                GloableConstant.getInstance().stopProgressDialog();
            }
        });
    }
    /**
 * 绑定微信
 */
    private void bindWeiXin(){
        Map<String,Object> map=new HashMap<>();
        map.put("user_id",user_id);
        map.put("nick_name",nickname);
        map.put("open_id",openid);
        map.put("user_picture",headimgurl);
        map.put("unionid",unionid);
//        map.put("user_id",9);
//        map.put("nick_name","chen元成");
//        map.put("open_id","o05rajvzihmmRApbbCcDGAEErtRs");
//        map.put("user_picture","http://wx.qlogo.cn/mmopen/jGLF0ice2HOb1QianB6bWNXvOpicMUAgVKq66M3HUlLdNNF5UBZ8VpfmWxlxx8VickElfRQFBUsgOG95o5Ww6HX7Cfq3WhUczUeH/0");
//        map.put("unionid","o0Pout5V4InqVJCVkEyUuSMBz-Z8");
        Log.e("********map",map.toString());
        GloableConstant.getInstance().startProgressDialog(mContext);
        Xutil.Post(ApiUrl.BINDWECAT,map,new MyCallBack<SupportResponse>(){
            @Override
            public void onSuccess(SupportResponse result) {
                super.onSuccess(result);

                Log.e("*********result",result.getStatus_code());
                GloableConstant.getInstance().stopProgressDialog1();
                if (result.getStatus_code().equals("204")){
                    ToastUtil.showShortToast(result.getMessage());
                    mBinding.setText("已绑定");
                    isBind="1";
                }else  if (result.getStatus_code().equals("205")) {
                    ToastUtil.showShortToast(result.getMessage());
                    mBinding.setText("未绑定");
                    isBind="0";
                }
            }
        });
    }

    /**
     * 是否绑定微信
     */
    private void bind() {
        Map<String, String> map = new HashMap<>();
        map.put("user_id",user_id);
        GloableConstant.getInstance().startProgressDialog(mContext);
        Xutil.GET(ApiUrl.BIND,map,new MyCallBack<BindResponse>(){
            @Override
            public void onSuccess(BindResponse result) {
                super.onSuccess(result);
                GloableConstant.getInstance().stopProgressDialog();
                if (result.getStatus_code().equals("200")){
                    isBind=result.getData().getIs_bind();
                    if (result.getData().getIs_bind().equals("1")){
                        mBinding.setText("已绑定");
                    }else {
                        mBinding.setText("未绑定");
                    }
                }
            }

            @Override
            public void onFinished() {
                super.onFinished();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
            }
        });
    }

    /**
     * 退出登录
     */

    private void exitLogin() {
        Map<String, String> map = new HashMap<>();
        map.put("user_id", user_id);
        Log.e("*********map",map.toString());
        GloableConstant.getInstance().startProgressDialog(this);
        Xutil.GET(ApiUrl.EXIT, map, new MyCallBack<SupportResponse>() {
            @Override
            public void onSuccess(SupportResponse result) {
                super.onSuccess(result);
                GloableConstant.getInstance().stopProgressDialog();
                Log.e("**********result",result.getStatus_code());
                if (result.getStatus_code().equals("204")) {
                    GloableConstant.getInstance().clearAll();
                    ToastUtil.showShortToast(result.getMessage());
                    startActivity(new Intent(MyAcccountActivity.this,LoginActivity.class));
                    setResult(2);
                    finish();
                }
            }

            @Override
            public void onFinished() {
                super.onFinished();
                GloableConstant.getInstance().stopProgressDialog();
            }
        });
    }

    public void exitPopWindow() {
        view = LayoutInflater.from(mContext).inflate(R.layout.exit_phone_pop_window, null);

        pop = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        pop.setFocusable(true);// 取得焦点
        pop.setBackgroundDrawable(new BitmapDrawable());
        //点击外部消失
        pop.setOutsideTouchable(true);
        //设置可以点击
        pop.setTouchable(true);
        pop.showAtLocation(view, Gravity.CENTER, 0, 0);
        setBackgroundAlpha(0.5f);//设置屏幕透明度
        pop.setOnDismissListener(new poponDismissListener(1));
        dialog_content = (TextView) view.findViewById(R.id.dialog_content);
        dialog_content.setText("您确认要退出登录吗？");
        exit_no = (TextView) view.findViewById(R.id.exit_no);
        exit_no.setOnClickListener(this);
        exit_sure = (TextView) view.findViewById(R.id.exit_sure);
        exit_sure.setOnClickListener(this);


    }

    public void setBackgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha;
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        getWindow().setAttributes(lp);
    }

    class poponDismissListener implements PopupWindow.OnDismissListener {
        int flag;

        public poponDismissListener(int a) {
            flag = a;
        }


        @Override
        public void onDismiss() {
            setBackgroundAlpha(1f);
        }
    }

}
