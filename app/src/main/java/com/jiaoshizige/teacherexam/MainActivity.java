package com.jiaoshizige.teacherexam;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.Settings;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.jiaoshizige.teacherexam.activity.aaa.xx.service.DownService;
import com.jiaoshizige.teacherexam.base.GloableConstant;
import com.jiaoshizige.teacherexam.fragment.MineFragment;
import com.jiaoshizige.teacherexam.fragment.PrivateSchoolFragment;
import com.jiaoshizige.teacherexam.fragment.StudyFragment;
import com.jiaoshizige.teacherexam.http.ApiUrl;
import com.jiaoshizige.teacherexam.http.MyCallBack;
import com.jiaoshizige.teacherexam.http.SupportResponse;
import com.jiaoshizige.teacherexam.http.Xutil;
import com.jiaoshizige.teacherexam.model.MessageEvent;
import com.jiaoshizige.teacherexam.model.busmodel.BusRefreshClass;
import com.jiaoshizige.teacherexam.utils.SPKeyValuesUtils;
import com.jiaoshizige.teacherexam.utils.SPUtils;
import com.jiaoshizige.teacherexam.utils.ToastUtil;
import com.jiaoshizige.teacherexam.widgets.UpdateManager;
import com.jiaoshizige.teacherexam.yy.fragment.OPenClassFragment;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import cn.jpush.android.api.JPushInterface;

/**
 * Created by Administrator on 2017/10/27.
 */

public class MainActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener {
    private LinearLayout mLayout;
    private RadioGroup mRadioGroup;
    private RadioButton mMainStudy;
    private RadioButton mPrivateSchool;
    private RadioButton mMine;
    private RadioButton mOpenClass;
    FragmentHolder holder = null;
    private long mTemptime;
    private String user_id;
    private String register_id;
    //    private String versionName;
//    private int versionCode;
    UpdateManager mUpdateManager;
    private Context mContext;

    private static final int REQUEST_CODE_WRITE_STORAGE = 1002;
    private static final int REQUEST_CODE_UNKNOWN_APP = 2001;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
//        try {
//            PackageManager packageManager = getPackageManager();
//            PackageInfo packInfo = packageManager.getPackageInfo(
//                    getPackageName(), 0);
//            versionName = packInfo.versionName;
//            versionCode = packInfo.versionCode;
//            Log.e("******versionName", versionName + "////////////" + versionCode);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        mUpdateManager = new UpdateManager(this, true);
        //这里来检测版本是否需要更新
//        mUpdateManager.checkUpdateInfo();

        onFinView();
        initView();
        startService(new Intent(this, DownService.class));
//        initEvent();

    }

    private void initEvent() {

        if (ContextCompat.checkSelfPermission(mContext, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) mContext, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {//用户已拒绝过一次
                //提示用户如果想要正常使用，要手动去设置中授权。
                ToastUtil.showShortToast("请到设置-应用管理中开启此应用的读写权限");
            } else {
                ActivityCompat.requestPermissions((Activity) mContext, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE
                }, REQUEST_CODE_WRITE_STORAGE);
            }
        } else {

            if (Build.VERSION.SDK_INT >= 26) {
                boolean b = getPackageManager().canRequestPackageInstalls();
                if (b) {
//                    mUpdateManager = new UpdateManager(this, true);
//                    mUpdateManager.checkUpdateInfo();
                } else {
                    startInstallPermissionSettingActivity();
                }
            } else {
//                mUpdateManager = new UpdateManager(this, true);
//                mUpdateManager.checkUpdateInfo();
            }
//            mUpdateManager.checkUpdateInfo();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_UNKNOWN_APP) {
            Log.e("************", resultCode + "");
            if (Build.VERSION.SDK_INT >= 26) {
                boolean b = getPackageManager().canRequestPackageInstalls();
                if (b) {
//                    mUpdateManager.checkUpdateInfo();
                } else {
                    startInstallPermissionSettingActivity();
                }
            } else {
//                mUpdateManager.checkUpdateInfo();
            }
        }
        if (requestCode == 10086 && resultCode == RESULT_OK) {
//            mUpdateManager.checkUpdateInfo();
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private void startInstallPermissionSettingActivity() {
        Uri packageURI = Uri.parse("package:" + getPackageName());
        //注意这个是8.0新API
        Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES, packageURI);
        startActivityForResult(intent, 10086);
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (GloableConstant.getInstance().getFlag().equals("1")) {//课程列表
            changeSchoolTab("0");
        } else if (GloableConstant.getInstance().getFlag().equals("2")) {//图书列表
            changeSchoolTab("1");
        } else if (GloableConstant.getInstance().getFlag().equals("3")) {//公开课
            changeLiveTab();
        }

        GloableConstant.getInstance().setFlag("");
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    public void onFinView() {
        mLayout = (LinearLayout) findViewById(R.id.container);
        mRadioGroup = (RadioGroup) findViewById(R.id.main_tab);
        mMainStudy = (RadioButton) findViewById(R.id.main_study);
        mPrivateSchool = (RadioButton) findViewById(R.id.private_school);
        mMine = (RadioButton) findViewById(R.id.mine);
        mOpenClass = (RadioButton) findViewById(R.id.open_class);
        user_id = (String) SPUtils.getSpValues(SPKeyValuesUtils.SP_USER_ID, SPUtils.TYPE_STRING);
        register_id = JPushInterface.getRegistrationID(this);
        GloableConstant.getInstance().setFlag("");
        binding();
    }

    public void initView() {
        initFragments();
        mRadioGroup.setOnCheckedChangeListener(this);
        mRadioGroup.check(R.id.main_study);
    }

    private static class FragmentHolder {
        public Class<? extends Fragment> cls;
        public String tag;
        public Fragment fragment;
    }

    private FragmentHolder[] mFragments = {new FragmentHolder(),
            new FragmentHolder(), new FragmentHolder(), new FragmentHolder()};

    private void initFragments() {
        mFragments[0].cls = StudyFragment.class;
        mFragments[0].tag = "StudyFragment";
        mFragments[1].cls = PrivateSchoolFragment.class;
        mFragments[1].tag = "PrivateSchoolFragment";

        mFragments[2].cls = OPenClassFragment.class;
        mFragments[2].tag = "OPenClassFragment";

        mFragments[3].cls = MineFragment.class;
        mFragments[3].tag = "MineFragment";

    }

    public void changeSchoolTab(String isbook) {
        //1图书 0课程
        GloableConstant.getInstance().setmLuckBook(isbook);
        mPrivateSchool.setChecked(true);

    }

    public void changeMainTab() {
        mMainStudy.setChecked(true);
    }

    public void changeLiveTab() {
        mOpenClass.setChecked(true);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        String oldTag = (String) group.getTag();
        switch (checkedId) {
            case R.id.main_study:
                holder = mFragments[0];
                break;
            case R.id.private_school:
                Log.d("++++++++++++message", GloableConstant.getInstance().getMessage() + "////////");
                if (GloableConstant.getInstance().getMessage() != null && GloableConstant.getInstance().getMessage().equals("1")) {
                    EventBus.getDefault().post(new MessageEvent("课程"));
                }
                holder = mFragments[1];
                break;
            case R.id.open_class:
                holder = mFragments[2];
                break;
            case R.id.mine:
                holder = mFragments[3];
                break;
            default:
                holder = mFragments[0];
        }
        if (holder.tag.equals(oldTag))
            return;
        try {
            group.setTag(holder.tag);
            if (holder.fragment == null)
                holder.fragment = holder.cls.newInstance();
            FragmentManager fm = getSupportFragmentManager();
            Fragment from = fm.findFragmentByTag(oldTag);

            Fragment to = holder.fragment;
            if (from == null) {
                // fm.beginTransaction().add(R.id.container, to,
                // holder.tag).commit();
                fm.beginTransaction().add(R.id.container, to, holder.tag)
                        .commitAllowingStateLoss();
                return;
            }
            if (!to.isAdded())
                // fm.beginTransaction().hide(from).add(R.id.container, to,
                // holder.tag).commit();
                fm.beginTransaction().hide(from)
                        .add(R.id.container, to, holder.tag)
                        .commitAllowingStateLoss();
            else
                // fm.beginTransaction().hide(from).show(to).commit();
                fm.beginTransaction().hide(from).show(to)
                        .commitAllowingStateLoss();

        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)
                && (event.getAction() == KeyEvent.ACTION_DOWN)) {
            if (System.currentTimeMillis() - mTemptime > 2000) {
                ToastUtil.showShortToast("请在按一次返回退出");
                mTemptime = System.currentTimeMillis();
            } else {
                finish();
                System.exit(0); // 凡是非零都表示异常退出!0表示正常退出!
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 绑定登录设备
     */
    private void binding() {
        Map<String, Object> map = new HashMap<>();
        map.put("user_id", user_id);
        map.put("registration_id", register_id);
        Log.e("********map", map.toString());
        GloableConstant.getInstance().startProgressDialog(this);
        Xutil.Post(ApiUrl.BINDING, map, new MyCallBack<SupportResponse>() {
            @Override
            public void onSuccess(SupportResponse result) {
                super.onSuccess(result);
                GloableConstant.getInstance().stopProgressDialog();
                if (result.getStatus_code().equals("204")) {
//                    ToastUtil.showShortToast(result.getMessage());
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

    @Subscribe
    public void refresh(BusRefreshClass refreshClass) {
        if (refreshClass.mClassName.equals(getClass().getSimpleName())) {
            refresh();
        } else if ("refresh".equals(refreshClass.mClassName)) {//全页面刷新
            refresh();
        }
    }

    protected void refresh() {

    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        onFinView();
        initView();
    }
}
