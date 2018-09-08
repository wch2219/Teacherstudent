package com.jiaoshizige.teacherexam.base;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.jiaoshizige.teacherexam.R;
import com.jiaoshizige.teacherexam.activity.LoginActivity;
import com.jiaoshizige.teacherexam.model.busmodel.BusIsLogin;
import com.jiaoshizige.teacherexam.model.busmodel.BusRefreshClass;
import com.jiaoshizige.teacherexam.utils.CleanLeakUtils;
import com.umeng.socialize.UMShareAPI;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/8/2.
 */
public abstract class BaseActivity extends AppCompatActivity {
    private static final String TAG = BaseActivity.class.getSimpleName();
    @BindView(R.id.toolbar_subtitle)
    TextView mToolbarSubTitle;
    @BindView(R.id.toolbar_title)
    TextView mToolbarTitle;
    @BindView(R.id.back_im)
    TextView mBack;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        if (null != savedInstanceState)
            savedInstanceState = null;
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
        this.setContentView(this.getLayoutId());//缺少这一行
        ButterKnife.bind(this);
        initView();
        /* if (mToolbar != null) {
            //将Toolbar显示到界面
            setSupportActionBar(mToolbar);
        }
        if (mToolbarTitle != null) {
            //getTitle()的值是activity的android:lable属性值
            mToolbarTitle.setText(getTitle());
            //设置默认的标题不显示
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }*/
//        new SystemStatusManager(this).setTranslucentStatus(R.color.white);
    }

    @Override
    protected void onStart() {
        super.onStart();
        /**
         * 判断是否有Toolbar,并默认显示返回按钮
         */
        if (null != getToolbar() && isShowBacking()) {
            showBack();
        } else {
            mBack.setVisibility(View.GONE);
        }
    }


    /**
     * 获取头部标题的TextView
     *
     * @return
     */
    public TextView setToolbarTitle() {
        return mToolbarTitle;
    }

    /**
     * 获取头部标题的TextView
     *
     * @return
     */
    public TextView setSubTitle() {
        return mToolbarSubTitle;
    }

    /**
     * 设置头部标题
     *
     * @param title
     */
    public void setToolBarTitle(CharSequence title) {
        if (mToolbarTitle != null) {
            mToolbarTitle.setText(title);
        } else {
            getToolbar().setTitle(title);
            setSupportActionBar(getToolbar());
        }
    }

    /**
     * this Activity of tool bar.
     * 获取头部.
     *
     * @return support.v7.widget.Toolbar.
     */
    public Toolbar getToolbar() {
        return (Toolbar) findViewById(R.id.toolbar);
    }


    /**
     * 版本号小于21的后退按钮图片
     */
    public void showBack() {
        //setNavigationIcon必须在setSupportActionBar(toolbar);方法后面加入
        getToolbar().setNavigationIcon(R.mipmap.back);
        getToolbar().setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    /**
     * 是否显示后退按钮,默认显示,可在子类重写该方法.
     *
     * @return
     */
    protected boolean isShowBacking() {
        return true;
    }

    /**
     * this activity layout res
     * 设置layout布局,在子类重写该方法.
     *
     * @return res layout xml id
     */
    protected abstract int getLayoutId();

    protected abstract void initView();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        Log.v(TAG, "onDestroy...");
        CleanLeakUtils.fixInputMethodManagerLeak(this);
        CleanLeakUtils.fixHuaWeiMemoryLeak(this);
    }

    /**
     *
     * @param requestCode
     * @param resultCode
     * @param data
     * QQ与新浪不需要添加Activity，但需要在使用QQ分享或者授权的Activity中，添加
     * 注意onActivityResult不可在fragment中实现，如果在fragment中调用登录或分享，需要在fragment依赖的Activity中实现
     * 所以在基类的activity中添加  qq 微博的
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    @Subscribe
    public void isLogin(BusIsLogin isLogin){
        if(!isLogin.isLogin){
            if(isActivityTop(this.getClass(),this)){
                showActivity(LoginActivity.class);
            }
            finish();
        }
    }

    protected void showActivity(Class<? extends Activity> showClass, Bundle bundle){
        Intent intent = new Intent(this, showClass);
        if(null != bundle){
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    protected void showActivity(Class<? extends Activity> showClass){
        showActivity(showClass,null);
    }

    /**
     *
     * 判断某activity是否处于栈顶
     * @return  true在栈顶 false不在栈顶
     */
    private boolean isActivityTop(Class cls,Context context){
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        String name = manager.getRunningTasks(1).get(0).topActivity.getClassName();
        return name.equals(cls.getName());
    }

    @Subscribe
    public void refresh(BusRefreshClass refreshClass){
        if (refreshClass.mClassName.equals(getClass().getSimpleName())) {
            refresh();
        } else if("refresh".equals(refreshClass.mClassName)){
            refresh();
        }
    }
    protected void refresh(){

    }
}
