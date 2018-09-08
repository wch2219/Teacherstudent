package com.jiaoshizige.teacherexam.fragment;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
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
import android.widget.EditText;
import android.widget.TextView;

import com.jiaoshizige.teacherexam.MainActivity;
import com.jiaoshizige.teacherexam.R;
import com.jiaoshizige.teacherexam.activity.PersonMessageActivity;
import com.jiaoshizige.teacherexam.activity.RuleDescriptionActuivity;
import com.jiaoshizige.teacherexam.base.GloableConstant;
import com.jiaoshizige.teacherexam.base.MBaseFragment;
import com.jiaoshizige.teacherexam.http.ApiUrl;
import com.jiaoshizige.teacherexam.http.MyCallBack;
import com.jiaoshizige.teacherexam.http.SupportResponse;
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
public class PhoneShortCutFragment extends MBaseFragment implements View.OnClickListener {
    @BindView(R.id.phone)
    ClearEditText mPhone;
    @BindView(R.id.code)
    EditText mCode;
    @BindView(R.id.identifying_code)
    TextView mIdentifying;
    @BindView(R.id.login_bt)
    Button mLogin;
    @BindView(R.id.service_deal)
    TextView mService;
    private String phone, code;
    private Intent intent;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.phone_short_cut_fragment, container, false);
    }

    @Override
    protected void onInitViews(Bundle savedInstanceState) {
        String str = "点击登录即表示接受<font color='#9A70C4'>《服务协议》</font>";
        mService.setTextSize(14);
        mService.setText(Html.fromHtml(str));
        mService.setOnClickListener(this);
        mIdentifying.setOnClickListener(this);
        mLogin.setOnClickListener(this);
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
            case R.id.identifying_code:
                phone = mPhone.getText().toString().trim();
                if (TextUtils.isEmpty(phone)) {
                    ToastUtil.showShortToast("请输入手机号码");
                } else {
                    if (ToolUtils.isCellphone(phone)) {
                        getCode();
                    } else {
                        ToastUtil.showShortToast("您输入的手机号码不正确，请重新输入");
                    }
                }


                break;
            case R.id.login_bt:
                phone = mPhone.getText().toString().trim();
                if (TextUtils.isEmpty(phone)) {
                    ToastUtil.showShortToast("请输入手机号码");
                } else if (!ToolUtils.isCellphone(phone)) {
                    ToastUtil.showShortToast("您输入的手机号码不正确，请重新输入");
                } else if (mCode.getText().toString().trim().equals("")) {
                    ToastUtil.showShortToast("请输入验证码");
                } else {
                    phone = mPhone.getText().toString().trim();
                    if (ToolUtils.isCellphone(phone)) {
                        CodeLogin();
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
     * 验证码登录
     */
    private void CodeLogin() {

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

        phone = mPhone.getText().toString().trim();
        code = mCode.getText().toString().trim();
        Map<String, Object> map = new HashMap<>();
        map.put("mobile", phone);
        map.put("code", code);
        map.put("service", "1");
        map.put("ops", "安卓");
        map.put("systemtype", systemtype);
        map.put("systemversion", systemversion);
        map.put("appversion", appversion);
        Log.e("TAG********", map.toString());
        GloableConstant.getInstance().startProgressDialog(getActivity());
        Xutil.Post(ApiUrl.CODELOGIN, map, new MyCallBack<AccountNumberResponse>() {
            @Override
            public void onSuccess(AccountNumberResponse result) {
                super.onSuccess(result);
                GloableConstant.getInstance().stopProgressDialog1();
                Log.e("*********getStatus_code", result.getStatus_code());
                if (result.getStatus_code().equals("204")) {
                    if (result.getData() != null && result.getData().getUser_id() != null) {
                        if (result.getData().getIs_reg().equals("0")) {
                            String user_id = result.getData().getUser_id();
                            String token = result.getData().getToken();
                            //getEvaluate_status  是否测评
                            if (result.data.getEvaluate_status().equals("1")) {
                                Sphelper.save(getActivity(), "hadexam", "isexam", true);
                            } else {
                                Sphelper.save(getActivity(), "hadexam", "isexam", false);
                            }
                            SPUtils.setSPValues(SPUtils.TYPE_STRING, SPKeyValuesUtils.SP_USER_ID, user_id);
                            SPUtils.setSPValues(SPUtils.TYPE_STRING, SPKeyValuesUtils.Token, token);
                            SPUtils.setSPValues(SPUtils.TYPE_STRING, SPKeyValuesUtils.SP_IS_LOGIN, "1");
                            Sphelper.save(getActivity(), "NICKNAME", "nick_name", result.getData().getNick_name());
                            Sphelper.save(getActivity(), "AVATAR", "avatar", result.getData().getAvatar());
                            startActivity(new Intent(getActivity(), PersonMessageActivity.class));
                            getActivity().finish();
                        } else {
                            String user_id = result.getData().getUser_id();
                            String token = result.getData().getToken();
                            SPUtils.setSPValues(SPUtils.TYPE_STRING, SPKeyValuesUtils.SP_USER_ID, user_id);
                            SPUtils.setSPValues(SPUtils.TYPE_STRING, SPKeyValuesUtils.Token, token);
                            SPUtils.setSPValues(SPUtils.TYPE_STRING, SPKeyValuesUtils.SP_IS_LOGIN, "1");
                            Sphelper.save(getActivity(), "NICKNAME", "nick_name", result.getData().getNick_name());
                            Sphelper.save(getActivity(), "AVATAR", "avatar", result.getData().getAvatar());
                            startActivity(new Intent(getActivity(), MainActivity.class));
                            getActivity().finish();
                        }
                    } else {
                        ToastUtil.showShortToast(result.getMessage());
                    }


                } else {
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

    /**
     * 获取验证码
     */

    public void getCode() {

        Map<String, Object> map = new HashMap<>();
        map.put("mobile", phone);
        map.put("type", "2");
        Xutil.Post(ApiUrl.SENDCODE, map, new MyCallBack<SupportResponse>() {
            @Override
            public void onSuccess(SupportResponse result) {
                super.onSuccess(result);
                if (result.getStatus_code().equals("204")) {
                    ToastUtil.showShortToast(result.getMessage());
                }
                sec = 60;
                mIdentifying.postDelayed(runnable, 0);

            }

            @Override
            public void onFinished() {
                super.onFinished();
            }

        });
    }

    private int sec;
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (sec == 0) {
                //发送前
                sec = 60;
                mIdentifying.setTextColor(Color.parseColor("#9A70C4"));
                mIdentifying.setText("获取验证码");
                mIdentifying.setClickable(true);
                mIdentifying.removeCallbacks(runnable);
            } else {
                //倒计时
                mIdentifying.setClickable(false);
                mIdentifying.setTextColor(Color.parseColor("#999999"));
                mIdentifying.setText(sec + "s后重试");
                sec--;
                mIdentifying.postDelayed(runnable, 1000);
            }
        }
    };

}
