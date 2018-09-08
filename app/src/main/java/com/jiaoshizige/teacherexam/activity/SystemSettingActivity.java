package com.jiaoshizige.teacherexam.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.BitmapDrawable;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.format.Formatter;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.jiaoshizige.teacherexam.R;
import com.jiaoshizige.teacherexam.base.BaseActivity;
import com.jiaoshizige.teacherexam.base.GloableConstant;
import com.jiaoshizige.teacherexam.http.ApiUrl;
import com.jiaoshizige.teacherexam.http.MyCallBack;
import com.jiaoshizige.teacherexam.http.SupportResponse;
import com.jiaoshizige.teacherexam.http.Xutil;
import com.jiaoshizige.teacherexam.utils.DataCleanManager;
import com.jiaoshizige.teacherexam.utils.SPKeyValuesUtils;
import com.jiaoshizige.teacherexam.utils.SPUtils;
import com.jiaoshizige.teacherexam.utils.Sphelper;
import com.jiaoshizige.teacherexam.utils.ToastUtil;
import com.jiaoshizige.teacherexam.widgets.UpdateManager;
import com.jiaoshizige.teacherexam.zz.database.DatabaseHelper;
import com.umeng.analytics.MobclickAgent;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.jpush.android.api.JPushInterface;

/**
 * Created by Administrator on 2017/a1/8 0008.
 */

/**
 * 系统设置
 */

public class SystemSettingActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.message_switch)
    ToggleButton mMessage;
    @BindView(R.id.updata)
    RelativeLayout mUpdata;
    @BindView(R.id.down_switch)
    ToggleButton mDown;
    @BindView(R.id.clear_cache)
    RelativeLayout mClear;
    @BindView(R.id.tv_Data)
    TextView tvData;
    @BindView(R.id.vision)
    TextView vision;
    @BindView(R.id.choose)
    RelativeLayout mChoose;
    @BindView(R.id.examtype)
    TextView mExam;

    @BindView(R.id.exit)
    Button exit;
    @BindView(R.id.mine_feedback)
    RelativeLayout mFeedBack;
    @BindView(R.id.my_recommend)
    RelativeLayout mRecommend;
    @BindView(R.id.about_us)
    RelativeLayout mAboutUs;
    @BindView(R.id.ticking)
    RelativeLayout mTicking;
    @BindView(R.id.question_Data)
    TextView mQuestionData;
    @BindView(R.id.question_clear_cache)
    RelativeLayout mQuestionClearCache;

    private Context mContext;
    private View view;
    private PopupWindow pop;
    private TextView dialog_content, exit_no, exit_sure;
    private Intent intent;
    private WifiManager wifiManager;
    private String wifi_switch;
    private String push_switch;
    private String user_id;
    private String versionName;
    private UpdateManager mUpdateManager;
    private String mExamTypeText;
    private String mProvinceText;
    private String mExamTypeId;

    @Override
    protected int getLayoutId() {
        return R.layout.system_setting_activity;
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("系统设置");
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("系统设置");
        MobclickAgent.onPause(this);
    }

    @Override
    protected void initView() {
        setToolbarTitle().setText("系统设置");
        setSubTitle().setText("");
        user_id = (String) SPUtils.getSpValues(SPKeyValuesUtils.SP_USER_ID, SPUtils.TYPE_STRING);
        if (getIntent().getStringExtra("wifi_switch") != null) {
            wifi_switch = getIntent().getStringExtra("wifi_switch");
            Sphelper.save(SystemSettingActivity.this, "WIFISWITCH", "wifi_switch", wifi_switch);
        }
        if (getIntent().getStringExtra("push_switch") != null) {
            push_switch = getIntent().getStringExtra("push_switch");
        }
        if (wifiManager == null) {
            wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        }
        if (getIntent().getStringExtra("exam_area") != null) {
            mProvinceText = getIntent().getStringExtra("exam_area");
        }

        if (getIntent().getStringExtra("exam_type") != null) {
            mExamTypeText = getIntent().getStringExtra("exam_type");
        }
        if (getIntent().getStringExtra("exam_type_id") != null) {
            mExamTypeId = getIntent().getStringExtra("exam_type_id");
        }

        if (mProvinceText != null && mExamTypeText != null) {
            mExam.setText(mProvinceText + "  " + mExamTypeText);
            mExam.setTextColor(ContextCompat.getColor(this, R.color.purple4));
        } else {
            mExam.setText("请选择考试类型");
            mExam.setTextColor(ContextCompat.getColor(this, R.color.text_color3));
        }
        mContext = this;
        mMessage.setOnClickListener(this);
        mUpdata.setOnClickListener(this);
        mDown.setOnClickListener(this);
        mClear.setOnClickListener(this);
        mChoose.setOnClickListener(this);
        exit.setOnClickListener(this);
        mFeedBack.setOnClickListener(this);
        mRecommend.setOnClickListener(this);
        mAboutUs.setOnClickListener(this);
        mQuestionClearCache.setOnClickListener(this);

        if (push_switch.equals("1")) {
            mMessage.setChecked(true);
        } else {
            mMessage.setChecked(false);
        }
        if (wifi_switch.equals("1")) {
            mDown.setChecked(true);
        } else {
            mDown.setChecked(false);
        }
       /* if (mDown.isChecked()) {
            wifiManager.setWifiEnabled(true);
        } else {
            wifiManager.setWifiEnabled(false);
        }*/
        if (mMessage.isChecked()) {
            JPushInterface.resumePush(getApplicationContext());
        } else {
            JPushInterface.stopPush(getApplicationContext());
        }

        String data = null;
        try {
            data = DataCleanManager.getTotalCacheSize(SystemSettingActivity.this);
            tvData.setText(data);
            Log.e("********data", data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            PackageManager packageManager = getPackageManager();
            PackageInfo packInfo = packageManager.getPackageInfo(
                    getPackageName(), 0);
            versionName = packInfo.versionName;
            vision.setText("V" + versionName);//当前版本
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        try {
            File file = new File(getDatabasePath("mydb.db").getPath());
            String lengthStr = Formatter.formatFileSize(this, file.length());
            mQuestionData.setText(lengthStr);
        } catch (Exception e) {
            mQuestionData.setText("0KB");
        }


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.message_switch:
                if (push_switch.equals("1")) {
                    mMessage.setChecked(false);
                    push_switch = "0";
                } else {
                    mMessage.setChecked(true);
                    push_switch = "1";
                }
                if (mMessage.isChecked()) {
                    JPushInterface.resumePush(getApplicationContext());
                } else {
                    JPushInterface.stopPush(getApplicationContext());
                }
                updata();
                break;
            case R.id.updata:
                ToastUtil.showShortToast("正在检查更新...");
                mUpdateManager = new UpdateManager(this, false);
                mUpdateManager.checkUpdateInfo();
                break;
            case R.id.down_switch:
                if (wifi_switch.equals("1")) {
                    mDown.setChecked(false);
                    wifi_switch = "0";
                    Sphelper.save(SystemSettingActivity.this, "WIFISWITCH", "wifi_switch", wifi_switch);
                } else {
                    mDown.setChecked(true);
                    wifi_switch = "1";
                    Sphelper.save(SystemSettingActivity.this, "WIFISWITCH", "wifi_switch", wifi_switch);
                }
                if (mDown.isChecked()) {
//                    wifiManager.setWifiEnabled(true);
                } else {
//                    wifiManager.setWifiEnabled(false);
                }
                updata();
                break;
            case R.id.clear_cache:
                exitPopWindow();
                break;
            case R.id.exit_no:
                if (pop.isShowing()) {
                    pop.dismiss();
                }
                break;
            case R.id.exit_sure:
                DataCleanManager.clearAllCache(SystemSettingActivity.this);
                tvData.setText("0.0KB");
                ToastUtil.showShortToast("缓存已清理");
                if (pop.isShowing()) {
                    pop.dismiss();
                }
                break;
            case R.id.choose:
                Intent intent = new Intent();
                intent.setClass(SystemSettingActivity.this, ExamSubjectSelect.class);
                intent.putExtra("exam_type", mExamTypeText);
                intent.putExtra("exam_area", mProvinceText);
                intent.putExtra("exam_type_id", mExamTypeId);
                startActivity(intent);
                break;
            case R.id.exit:
                exitPopWindow1();
                break;
            case R.id.mine_feedback:
                intent = new Intent(SystemSettingActivity.this, OpinionTicklingActivity.class);
                startActivity(intent);
                break;
            case R.id.my_recommend:
                intent = new Intent(SystemSettingActivity.this, ShareActivity.class);
                startActivity(intent);
                break;
            case R.id.about_us:
                intent = new Intent(SystemSettingActivity.this, AboutUsActivity.class);
                startActivity(intent);
                break;
            case R.id.question_clear_cache:
                exitPopWindow2();
                break;
            default:
                break;
        }
    }


    public void exitPopWindow1() {
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
        exit_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pop.isShowing()) {
                    pop.dismiss();
                }
            }
        });
        exit_sure = (TextView) view.findViewById(R.id.exit_sure);
        exit_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pop.isShowing()) {
                    pop.dismiss();
                }
                exitLogin();
            }
        });


    }


    /**
     * 退出登录
     */

    private void exitLogin() {
        Map<String, String> map = new HashMap<>();
        map.put("user_id", user_id);
        Log.e("*********map", map.toString());
        GloableConstant.getInstance().startProgressDialog(this);
        Xutil.GET(ApiUrl.EXIT, map, new MyCallBack<SupportResponse>() {
            @Override
            public void onSuccess(SupportResponse result) {
                super.onSuccess(result);
                GloableConstant.getInstance().stopProgressDialog();
                Log.e("**********result", result.getStatus_code());
                if (result.getStatus_code().equals("204")) {
                    GloableConstant.getInstance().clearAll();
                    ToastUtil.showShortToast(result.getMessage());
                    startActivity(new Intent(SystemSettingActivity.this, LoginActivity.class));
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
        dialog_content.setText("您确认要清除缓存吗？");
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
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

    private void updata() {
        Map<String, Object> map = new HashMap<>();
        map.put("user_id", user_id);
        map.put("wifi_switch", wifi_switch);
        map.put("push_switch", push_switch);

        Log.e("*********map", map.toString());
        GloableConstant.getInstance().startProgressDialog(this);
        Xutil.Post(ApiUrl.UPDATA, map, new MyCallBack<SupportResponse>() {
            @Override
            public void onSuccess(SupportResponse result) {
                super.onSuccess(result);
                GloableConstant.getInstance().stopProgressDialog();
                if (result.getStatus_code().equals("204")) {
                    ToastUtil.showShortToast(result.getMessage());

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

    public void exitPopWindow2() {
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
        dialog_content.setText("您确认要清除离线题库吗？");
        exit_no = (TextView) view.findViewById(R.id.exit_no);
        exit_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pop.isShowing()) {
                    pop.dismiss();
                }
            }
        });
        exit_sure = (TextView) view.findViewById(R.id.exit_sure);
        exit_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pop.isShowing()) {
                    pop.dismiss();
                }

                try {
                    File file = new File(getDatabasePath("mydb.db").getPath());
                    file.delete();
                    ToastUtil.showShortToast("离线题库已清除");
                    mQuestionData.setText("0KB");
                } catch (Exception e) {
                    ToastUtil.showShortToast("删除失败");
                }
                
            }
        });


    }


}
