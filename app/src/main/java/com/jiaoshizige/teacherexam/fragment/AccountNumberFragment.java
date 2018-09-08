package com.jiaoshizige.teacherexam.fragment;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.jiaoshizige.teacherexam.MainActivity;
import com.jiaoshizige.teacherexam.R;
import com.jiaoshizige.teacherexam.activity.RegresterA;
import com.jiaoshizige.teacherexam.activity.RuleDescriptionActuivity;
import com.jiaoshizige.teacherexam.base.GloableConstant;
import com.jiaoshizige.teacherexam.base.MBaseFragment;
import com.jiaoshizige.teacherexam.http.ApiUrl;
import com.jiaoshizige.teacherexam.http.MyCallBack;
import com.jiaoshizige.teacherexam.http.Xutil;
import com.jiaoshizige.teacherexam.model.AccountNumberResponse;
import com.jiaoshizige.teacherexam.utils.SPKeyValuesUtils;
import com.jiaoshizige.teacherexam.utils.SPUtils;
import com.jiaoshizige.teacherexam.utils.Sphelper;
import com.jiaoshizige.teacherexam.utils.ToastUtil;
import com.jiaoshizige.teacherexam.utils.ToolUtils;
import com.jiaoshizige.teacherexam.widgets.ClearEditText;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/a1/10 0010.
 */

public class AccountNumberFragment extends MBaseFragment implements View.OnClickListener {
    @BindView(R.id.phone_num)
    ClearEditText mPhone;
    @BindView(R.id.pass_word)
    ClearEditText mPassWord;
    @BindView(R.id.forget)
    TextView mForget;
    @BindView(R.id.login_bt)
    Button mLogin;
    @BindView(R.id.service_deal)
    TextView mService;
    @BindView(R.id.register)
    TextView mRegieter;
    private String phone, password;
    private Intent intent;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.account_number_fragment_layout, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("账号密码登录");
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("账号密码登录");
    }

    @Override
    protected void onInitViews(Bundle savedInstanceState) {
        String str = "点击登录即表示接受<font color='#9A70C4'>《服务协议》</font>";
        mService.setTextSize(14);
        mService.setText(Html.fromHtml(str));
        mService.setOnClickListener(this);
        mForget.setOnClickListener(this);
        mLogin.setOnClickListener(this);
        mRegieter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(getActivity(), RegresterA.class);
                intent.putExtra("flag", "1");
                startActivity(intent);
            }
        });
        mPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                mLogin.setBackgroundResource(R.drawable.button_gray);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                mLogin.setBackgroundResource(R.drawable.purple_shape);
            }

            @Override
            public void afterTextChanged(Editable s) {
                mLogin.setBackgroundResource(R.drawable.purple_shape);
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.forget:
                intent = new Intent(getActivity(), RegresterA.class);
                intent.putExtra("flag", "2");
                startActivity(intent);
                break;
            case R.id.login_bt:
                phone = mPhone.getText().toString().trim();
                password = mPassWord.getText().toString().trim();
                if (TextUtils.isEmpty(phone)) {
                    ToastUtil.showShortToast("请输入手机号码");
                } else if (!ToolUtils.isCellphone(phone)) {
                    ToastUtil.showShortToast("您输入的手机号码不正确，请重新输入");
                } else if (TextUtils.isEmpty(password)) {
                    ToastUtil.showShortToast("请输入密码");
                } else {
                    if (ToolUtils.isCellphone(phone)) {
                        accountnumberLogin();
                    } else {
                        ToastUtil.showShortToast("您输入的手机号码不正确，请重新输入");
                    }
                }
                break;
            case R.id.service_deal:
                intent = new Intent();
                intent.setClass(getActivity(), RuleDescriptionActuivity.class);
                intent.putExtra("flag", "service");
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    /**
     * 账号密码登录
     */
    private void accountnumberLogin() {

        String systemtype = Build.MODEL;//手机型号
        if (systemtype == null) {
            systemtype = "";
        }
        String systemversion = String.valueOf(android.os.Build.VERSION.RELEASE);//系统版本号
        if (systemversion == null) {
            systemversion = "";
        }

        PackageManager packageManager = getActivity().getPackageManager();
        PackageInfo packInfo = null;
        try {
            packInfo = packageManager.getPackageInfo(getActivity().getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String appversion = packInfo.versionName;
        if (appversion == null) {
            appversion = "";
        }

        Map<String, Object> map = new HashMap<>();
        map.put("mobile", phone);
        map.put("password", password);
        map.put("service", "1");
        map.put("ops", "安卓");
        map.put("systemtype", systemtype);
        map.put("systemversion", systemversion);
        map.put("appversion", appversion);
        Log.e("*******map", map + "***********" + ApiUrl.LOGIN);
        GloableConstant.getInstance().startProgressDialog(getActivity());
        Xutil.Post(ApiUrl.LOGIN, map, new MyCallBack<AccountNumberResponse>() {
            @Override
            public void onSuccess(AccountNumberResponse result) {
                super.onSuccess(result);
                GloableConstant.getInstance().stopProgressDialog1();
                if (result.getStatus_code().equals("200")) {
                    startActivity(new Intent(getActivity(), MainActivity.class));
                    String user_id = result.getData().getUser_id();
                    String token = result.getData().getToken();
                    if (result.data.getEvaluate_status().equals("1")) {
                        Sphelper.save(getActivity(), "hadexam", "isexam", true);
                    } else {
                        Sphelper.save(getActivity(), "hadexam", "isexam", false);
                    }
                    Log.e("*********", user_id + "*******" + token);
                    SPUtils.setSPValues(SPUtils.TYPE_STRING, SPKeyValuesUtils.SP_USER_ID, user_id);
                    SPUtils.setSPValues(SPUtils.TYPE_STRING, SPKeyValuesUtils.Token, token);
                    SPUtils.setSPValues(SPUtils.TYPE_STRING, SPKeyValuesUtils.SP_IS_LOGIN, "1");
                    Sphelper.save(getActivity(), "NICKNAME", "nick_name", result.getData().getNick_name());
                    Sphelper.save(getActivity(), "AVATAR", "avatar", result.getData().getAvatar());
                    Log.e("*******user_id", SPUtils.getSpValues(SPKeyValuesUtils.SP_IS_LOGIN, SPUtils.TYPE_STRING) + "//***********" +
                            SPUtils.getSpValues(SPKeyValuesUtils.Token, SPUtils.TYPE_STRING) + "*********" +
                            SPUtils.getSpValues(SPKeyValuesUtils.SP_USER_ID, SPUtils.TYPE_STRING));
                    startActivity(new Intent(getActivity(), MainActivity.class));
                    getActivity().finish();
                } else {
                    ToastUtil.showShortToast(result.getMessage());
                }
                if (result.getStatus_code().equals("204")) {
                    ToastUtil.showShortToast(result.getMessage());
                }

            }


            @Override
            public void onFinished() {
                super.onFinished();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
                GloableConstant.getInstance().stopProgressDialog1();
            }
        });


    }

}
