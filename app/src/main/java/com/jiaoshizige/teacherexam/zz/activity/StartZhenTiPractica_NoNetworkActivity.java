package com.jiaoshizige.teacherexam.zz.activity;

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
import com.jiaoshizige.teacherexam.activity.SuspendedActivity;
import com.jiaoshizige.teacherexam.adapter.ViewPagerAdapter;
import com.jiaoshizige.teacherexam.base.GloableConstant;
import com.jiaoshizige.teacherexam.utils.SPKeyValuesUtils;
import com.jiaoshizige.teacherexam.utils.SPUtils;
import com.jiaoshizige.teacherexam.zz.database.ZhenTi;
import com.jiaoshizige.teacherexam.zz.database.ZhenTiDao;
import com.jiaoshizige.teacherexam.zz.fragment.StartZhenTiPractice_NoNetWorkFragment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/8/3.
 */

public class StartZhenTiPractica_NoNetworkActivity extends AppCompatActivity {
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
    private StartZhenTiPractice_NoNetWorkFragment mFragment;
    private FragmentManager mManager;
    private ViewPagerAdapter mAdapter;
    private List<ZhenTi> mDataList;

    private String user_id;
    private String zhenti_id;
    private String name;
    private String tiku_id;


    private long mRecordTime;
    private int hour;

    private FragmentSkipInterface mFragmentSkipInterface;
    public static StartZhenTiPractica_NoNetworkActivity startZhenTiPractica_noNetworkActivity;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_practice_layout);
        ButterKnife.bind(this);
        user_id = (String) SPUtils.getSpValues(SPKeyValuesUtils.SP_USER_ID, SPUtils.TYPE_STRING);
        startZhenTiPractica_noNetworkActivity = this;

        if (mRecordTime != 0) {
            timer.setBase(timer.getBase() + (SystemClock.elapsedRealtime() - mRecordTime));
        } else {
            timer.setBase(SystemClock.elapsedRealtime());
        }
        hour = (int) ((SystemClock.elapsedRealtime() - timer.getBase()) / 1000 / 60);
        timer.setFormat("0" + String.valueOf(hour) + ":%s");
        timer.start();


        zhenti_id = getIntent().getStringExtra("zhenti_id");
        if (zhenti_id == null) {
            zhenti_id = "";
        }

        if (getIntent().getStringExtra("name") != null) {
            name = getIntent().getStringExtra("name");
        } else {
            name = "";
        }


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

        if (!zhenti_id.equals("")) {
            getLocalData();

        }


        mSuspended.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("************name", name + "--------------");
                if (!name.equals("")) {
                    Intent intent = new Intent();
                    intent.setClass(StartZhenTiPractica_NoNetworkActivity.this, SuspendedActivity.class);
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
                intent.setClass(StartZhenTiPractica_NoNetworkActivity.this, ZhenTiSheet_NoNetworkActivity.class);
                intent.putExtra("data", (Serializable) mDataList);
                intent.putExtra("zhenti_id", zhenti_id);
                intent.putExtra("name", name);
                long aa = SystemClock.elapsedRealtime() - timer.getBase();
                Log.d("*****************aa", aa + "-----------");
                intent.putExtra("hour", String.valueOf(aa));
                startActivityForResult(intent, 2);


            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            switch (resultCode) {
                case 1:
                    // tag = "1";
                    refresh();
                    getLocalData();
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
        Intent intent = new Intent(StartZhenTiPractica_NoNetworkActivity.this, StartZhenTiPractica_NoNetworkActivity.class);
        intent.putExtra("zhenti_id", zhenti_id);
        intent.putExtra("name", name);
        startActivity(intent);

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

    private void getLocalData() {
        if (GloableConstant.getInstance().getTiku_id() != null) {
            tiku_id = GloableConstant.getInstance().getTiku_id();
        } else {
            tiku_id = "";
        }
        mDataList = new ZhenTiDao(StartZhenTiPractica_NoNetworkActivity.this).orderBy("qid", true, "zhenti_id", zhenti_id, "tiku_id", tiku_id);

        if (mDataList != null && mDataList.size() > 0) {
            for (int i = 0; i < mDataList.size(); i++) {
                Log.d("timu", mDataList.get(i).getQid() + "");
                mFragment = new StartZhenTiPractice_NoNetWorkFragment(StartZhenTiPractica_NoNetworkActivity.this, mDataList.get(i), i, String.valueOf(mDataList.size()));
                mFragmentList.add(mFragment);
            }
            mManager = getSupportFragmentManager();
            mAdapter = new ViewPagerAdapter(mManager, mFragmentList);
            mViewpager.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();
        }
    }
}
