package com.jiaoshizige.teacherexam.activity;

import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.jiaoshizige.teacherexam.R;
import com.jiaoshizige.teacherexam.activity.aaa.MyBean;
import com.jiaoshizige.teacherexam.activity.aaa.MyCardHandler;
import com.jiaoshizige.teacherexam.base.BaseActivity;
import com.jiaoshizige.teacherexam.base.GloableConstant;
import com.jiaoshizige.teacherexam.http.ApiUrl;
import com.jiaoshizige.teacherexam.http.MyCallBack;
import com.jiaoshizige.teacherexam.http.Xutil;
import com.jiaoshizige.teacherexam.model.CheckPointResponse;
import com.jiaoshizige.teacherexam.utils.SPKeyValuesUtils;
import com.jiaoshizige.teacherexam.utils.SPUtils;
import com.jiaoshizige.teacherexam.widgets.CardViewPager;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/7/23.
 */

public class CheckPointListActivity extends BaseActivity {
    private TextView mButton;
    private CardViewPager viewPager;
    private boolean isCard = true;
    private String id;
    private String user_id;
    private List<MyBean.mData> list;

    @Override
    protected int getLayoutId() {
        return R.layout.check_point_layout;
    }

    @Override
    protected void initView() {
        viewPager = (CardViewPager) findViewById(R.id.viewpager);
        user_id = (String) SPUtils.getSpValues(SPKeyValuesUtils.SP_USER_ID, SPUtils.TYPE_STRING);
        if (getIntent().getStringExtra("id") != null) {
            id = getIntent().getStringExtra("id");
        } else {
            id = "";
        }

        if (getIntent().getStringExtra("name") != null) {
            setToolbarTitle().setText(getIntent().getStringExtra("name"));
        }
        setSubTitle().setText("");
        list = new ArrayList<>();

        mButton = (TextView) findViewById(R.id.button);
//        viewPager.bind(getSupportFragmentManager(), new MyCardHandler(), list);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Log.d("MainActivity", "position:" + position + "pos:" + (position % list.size()));
                mButton.setText((position % list.size() + 1) + "/" + list.size());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        if (!id.equals("")) {
            getCheckPoint();
        }
    }

    private void getCheckPoint() {
        Map<String, String> map = new HashMap<>();
        map.put("user_id", user_id);
        map.put("kaodian_id", id);
        Log.d("**********data", map.toString() + "/////");
        GloableConstant.getInstance().startProgressDialog(CheckPointListActivity.this);
        Xutil.GET(ApiUrl.CHECKPOINT, map, new MyCallBack<MyBean>() {
            @Override
            public void onSuccess(MyBean result) {
                super.onSuccess(result);
                GloableConstant.getInstance().stopProgressDialog();
                Log.d("**********data", result.getData().size() + "/////");
                if (result.getData() != null && result.getData().size() > 0) {
                    list = result.getData();
                    mButton.setText("1/" + list.size());
                    viewPager.bind(getSupportFragmentManager(), new MyCardHandler(), list);

                }


            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

                Log.d("--------------", ex.getMessage() + "--------------");
            }

            @Override
            public void onFinished() {
                super.onFinished();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("关卡列表");
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("关卡列表");
        MobclickAgent.onPause(this);
    }


}
