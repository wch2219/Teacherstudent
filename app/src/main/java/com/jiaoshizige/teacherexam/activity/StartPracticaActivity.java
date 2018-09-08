package com.jiaoshizige.teacherexam.activity;


import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Chronometer;
import android.widget.TextView;

import com.jiaoshizige.teacherexam.R;
import com.jiaoshizige.teacherexam.adapter.ViewPagerAdapter;
import com.jiaoshizige.teacherexam.base.GloableConstant;
import com.jiaoshizige.teacherexam.fragment.StartPracticeFragment;
import com.jiaoshizige.teacherexam.http.ApiUrl;
import com.jiaoshizige.teacherexam.http.MyCallBack;
import com.jiaoshizige.teacherexam.http.Xutil;
import com.jiaoshizige.teacherexam.model.MoNiResponse;
import com.jiaoshizige.teacherexam.utils.SPKeyValuesUtils;
import com.jiaoshizige.teacherexam.utils.SPUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by Administrator on 2018/8/3.
 */

public class StartPracticaActivity extends AppCompatActivity {

    @BindView(R.id.suspended)
    TextView mSuspended;
    @BindView(R.id.timer)
    Chronometer timer;
    @BindView(R.id.toolbar_title)
    TextView mTitle;
    @BindView(R.id.sheet)
    TextView mSheet;
    @BindView(R.id.viewpager)
    ViewPager mViewpager;


    private List<Fragment> mFragmentList = new ArrayList<>();
    private StartPracticeFragment mFragment;
    private FragmentManager mManager;
    private ViewPagerAdapter mAdapter;
    private List<MoNiResponse.mData> mDataList;

    private FragmentSkipInterface mFragmentSkipInterface;


    private String user_id;
    private String tiku_id;
    private String name;
    private String tag;
    private long mRecordTime;
    int hour;
    public static StartPracticaActivity startPracticaActivity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_practice_layout);
        ButterKnife.bind(this);

        startPracticaActivity = this;
        user_id = (String) SPUtils.getSpValues(SPKeyValuesUtils.SP_USER_ID, SPUtils.TYPE_STRING);

        if (mRecordTime != 0) {
            timer.setBase(timer.getBase() + (SystemClock.elapsedRealtime() - mRecordTime));
        } else {
            timer.setBase(SystemClock.elapsedRealtime());
        }
        hour = (int) ((SystemClock.elapsedRealtime() - timer.getBase()) / 1000 / 60);
        timer.setFormat("0" + String.valueOf(hour) + ":%s");
        timer.start();

        if (GloableConstant.getInstance().getTiku_id() != null) {
            tiku_id = GloableConstant.getInstance().getTiku_id();
        } else {
            tiku_id = "";
        }
        if (getIntent().getStringExtra("name") != null) {
            name = getIntent().getStringExtra("name");
        } else {
            name = "";
        }
        mSuspended.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("************name", name + "--------------");
                if (!name.equals("")) {
                    Intent intent = new Intent();
                    intent.setClass(StartPracticaActivity.this, SuspendedActivity.class);
                    intent.putExtra("name", name);
                    startActivityForResult(intent, 1);
                    timer.stop();
                    mRecordTime = SystemClock.elapsedRealtime();
                    Log.d("*************time", mRecordTime + "//////////////");// 保存这次记录了的时间
//                    startActivity(intent);
                }

            }
        });
        mSheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(StartPracticaActivity.this, MoNiSheetActivity.class);
                intent.putExtra("data", (Serializable) mDataList);
                long aa = SystemClock.elapsedRealtime() - timer.getBase();
                Log.d("*****************aa", aa + "-----------");
                intent.putExtra("hour", String.valueOf(aa));
                startActivityForResult(intent, 2);


            }
        });

        mDataList = new ArrayList<>();
        mViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (!String.valueOf(mDataList.size()).equals("")) {
                    mTitle.setText(String.valueOf(position + 1) + "/" + mDataList.size());
                } else {
                    mTitle.setText("0/0");
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        if (getIntent().getStringExtra("tag") != null) {
            tag = getIntent().getStringExtra("tag");
        } else {
            tag = "";
        }
        if (!tiku_id.equals("")) {
            getMoNi(tag);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            switch (resultCode) {
                case 1:
                    tag = "1";
                    refresh();
                    getMoNi(tag);
                    timer.setBase(SystemClock.elapsedRealtime());//计时器清零
                    hour = (int) ((SystemClock.elapsedRealtime() - timer.getBase()) / 1000 / 60);
                    timer.setFormat("0" + String.valueOf(hour) + ":%s");
                    timer.start();
                    break;
                case 2:
                    finish();
                    break;
                case 3:
                    if (mRecordTime != 0) {
                        timer.setBase(timer.getBase() + (SystemClock.elapsedRealtime() - mRecordTime));
                    } else {
                        timer.setBase(SystemClock.elapsedRealtime());
                    }
                    hour = (int) ((SystemClock.elapsedRealtime() - timer.getBase()) / 1000 / 60);
                    timer.setFormat("0" + String.valueOf(hour) + ":%s");
                    timer.start();

                    break;
                default:
                    break;
            }
        } else if (requestCode == 2) {
            if (resultCode == RESULT_OK) {
                String result = data.getExtras().getString("result");//得到新Activity 关闭后返回的数据
                Log.e("************result", result);
                mViewpager.setCurrentItem(Integer.valueOf(result));
            }
        }
    }

    public void refresh() {
        finish();
        Intent intent = new Intent(StartPracticaActivity.this, StartPracticaActivity.class);
//        intent.putExtra("id",id);
        intent.putExtra("name", name);
        startActivity(intent);

    }

    private void getMoNi(String is_replay) {
        Map<String, String> map = new HashMap<>();
        map.put("user_id", user_id);
        map.put("tiku_id", tiku_id);
        map.put("is_replay", is_replay);
        Log.e("***************map", map.toString());
        GloableConstant.getInstance().startProgressDialog(StartPracticaActivity.this);
        Xutil.GET(ApiUrl.MONIQUESTION, map, new MyCallBack<MoNiResponse>() {
            @Override
            public void onSuccess(MoNiResponse result) {
                super.onSuccess(result);
                GloableConstant.getInstance().stopProgressDialog();
                if (result.getStatus_code().equals("200")) {
                    if (result.getData() != null && result.getData().size() > 0) {
                        mDataList = result.getData();

                        for (int i = 0; i < result.getData().size(); i++) {
                            mDataList.get(i).setQid(String.valueOf(i + 1));
                            mDataList.get(i).setIs_done("0");
                            mDataList.get(i).setUser_answer("");
                            mFragment = new StartPracticeFragment(StartPracticaActivity.this, result.getData().get(i), i, String.valueOf(result.getData().size()));
                            mFragmentList.add(mFragment);
                        }

                        mManager = getSupportFragmentManager();
                        mAdapter = new ViewPagerAdapter(mManager, mFragmentList);
                        mViewpager.setAdapter(mAdapter);
                        mAdapter.notifyDataSetChanged();
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
                Log.e("***************ex", ex.getMessage() + "-------------");
            }
        });
    }


    public void setFragmentSkipInterface(FragmentSkipInterface fragmentSkipInterface) {
        mFragmentSkipInterface = fragmentSkipInterface;
    }

    /**
     * Fragment跳转
     */
    public void skipToFragment() {
        if (mFragmentSkipInterface != null) {
            mFragmentSkipInterface.gotoFragment(mViewpager);
        }
    }

    public interface FragmentSkipInterface {
        /**
         * ViewPager中子Fragment之间跳转的实现方法
         */
        void gotoFragment(ViewPager viewPager);
    }

}
